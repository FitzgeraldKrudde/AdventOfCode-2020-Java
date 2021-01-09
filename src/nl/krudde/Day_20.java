package nl.krudde;

import lombok.AllArgsConstructor;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;
import static nl.krudde.Day_20.Edge.*;

public class Day_20 extends Day {
    List<String> seaMonster = Arrays.asList(
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   ");
    int seaMonsterLength = seaMonster.get(0).length();
    // calculate SeaMonster mask as bitwise mask to prevent running out on the left while shifting
    int seaMonsterMask = Integer.parseInt("1".repeat(seaMonsterLength), 2);
    // calculate SeaMonster sums ('#'->1)
    int[] seaMonsterSums = seaMonster.stream()
            .map(line -> line.replace(' ', '0').replace('#', '1'))
            .map(s -> parseInt(s, 2))
            .mapToInt(i -> i)
            .toArray();

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Tile> tiles = parseInput(inputRaw);

        int nrTilesPerEdge = (int) Math.sqrt(tiles.size());

        Map<Position, TileArrangement> placedTiles = solve(new HashMap<>(), tiles, nrTilesPerEdge);

        long result = multiplyCornerIds(nrTilesPerEdge, placedTiles);

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Tile> tiles = parseInput(inputRaw);

        int nrTilesPerEdge = (int) Math.sqrt(tiles.size());

        Map<Position, TileArrangement> placedTiles = solve(new HashMap<>(), tiles, nrTilesPerEdge);

        // assemble into 1 big image and strip all edges from all placed tiles
        List<String> imageLines = stripEdgesAndCombineTiles(placedTiles, nrTilesPerEdge);

        // try to find sea monsters while rotating and/or flipping
        long nrOfSeaMonsters = findMaximumNrOfSeaMonstersForVariations(imageLines);

        // calculate water roughness
        long result = nrHashes(imageLines) - (nrOfSeaMonsters * nrHashes(seaMonster));

        return String.valueOf(result);
    }

    public List<Tile> parseInput(List<String> inputRaw) {
        List<Tile> tiles = new ArrayList<>();
        // determine the number of tiles and the number of lines per 1 tile
        int i = 0;
        do {
            i++;
        } while (inputRaw.get(i).length() > 0);
        int nrLinesPerTile = i - 1;
        int nrInputLinesPerTile = nrLinesPerTile + 2;
        int nrTiles = inputRaw.size() / (nrInputLinesPerTile);
        for (i = 0; i < nrTiles; i++) {
            String lineWithId = inputRaw.get(i * (nrInputLinesPerTile));
            int id = parseInt(lineWithId.split("\\s+|:")[1]);
            List<String> tileLines = inputRaw.subList(i * nrInputLinesPerTile + 1, i * nrInputLinesPerTile + 1 + nrLinesPerTile);

            // calculate all tile variations (rotating and flipping)
            // calculate "edges" i.e. interpret '.' as 0 and '#' as 1 and convert this binary number to int
            Map<Orientation, Tile> tileVariations = calculateOrientationsAndEdges(tileLines);

            Tile tile = new Tile(id, tileLines, null, tileVariations);
            tiles.add(tile);
        }

        System.out.println("read #tiles = " + tiles.size());
        return tiles;
    }

    public Map<Position, TileArrangement> solve(Map<Position, TileArrangement> placedTiles, List<Tile> unplacedTiles, int nrTilesPerEdge) {
        if (unplacedTiles.size() == 0) {
            return placedTiles;
        }

        // determine next free spot (top->bottom, left->right)
        Position nextPosition = determineNextPosition(placedTiles, nrTilesPerEdge);

        // try all unplaced tiles in all rotations
        return unplacedTiles.stream()
                .flatMap(tile -> Arrays.stream(Orientation.values())
                        .filter(tile.tileVariations::containsKey)
                        .map(orientation -> new TileArrangement(tile, orientation)))
                .filter(tileArrangement -> fits(placedTiles, nextPosition, tileArrangement))
                .map(tileArrangement -> tryTile(placedTiles, unplacedTiles, nrTilesPerEdge, nextPosition, tileArrangement))
                .filter(Objects::nonNull)
                // there are multiple solutions as the solution can be rotated, we just pick one
                .findAny()
                .orElse(null);
    }

    private Position determineNextPosition(Map<Position, TileArrangement> placedTiles, int nrTilesPerEdge) {
        if (placedTiles.size() == 0) {
            return new Position(0, 0);
        }

        // find maximum y
        final int y = placedTiles.keySet().stream()
                .max(Comparator.comparingInt(position -> position.y))
                .map(position -> position.y)
                .orElse(-1);
        // find maximum x for this y row
        int x = placedTiles.keySet().stream()
                .filter(position -> position.y == y)
                .max(Comparator.comparingInt(tile -> tile.x))
                .map(tileArrangement -> tileArrangement.x)
                .orElse(-1);

        // check for last position in row
        if (x == nrTilesPerEdge - 1) {
            return new Position(0, y + 1);
        } else {
            return new Position(x + 1, y);
        }
    }

    private boolean fits(Map<Position, TileArrangement> placedTiles, Position position, TileArrangement newTileArrangement) {
        return position.neighbours().stream()
                .filter(placedTiles::containsKey)
                .map(pos -> new SimpleEntry<>(pos, placedTiles.get(pos)))
                .allMatch(placedTileArrangementEntry -> edgeEqual(placedTileArrangementEntry, position, newTileArrangement));
    }

    private boolean edgeEqual(Entry<Position, TileArrangement> placedTileArrangementEntry, Position position, TileArrangement newTileArrangement) {
        boolean result;

        Tile placedTile = placedTileArrangementEntry.getValue().tile;
        Orientation placedTileOrientation = placedTileArrangementEntry.getValue().orientation;
        Position placedTilePosition = placedTileArrangementEntry.getKey();

        Tile newTile = newTileArrangement.tile;
        Orientation newTileOrientation = newTileArrangement.orientation;

        if (placedTilePosition.x != position.x) {
            // besides each other, existing tile on the left of the new tile
            result = placedTile.tileVariations.get(placedTileOrientation).edges.get(RIGHT).equals(newTile.tileVariations.get(newTileOrientation).edges.get(LEFT));
        } else {
            // above each other, existing tile above of the new tile
            result = placedTile.tileVariations.get(placedTileOrientation).edges.get(BOTTOM).equals(newTile.tileVariations.get(newTileOrientation).edges.get(TOP));
        }

        return result;
    }

    private Map<Position, TileArrangement> tryTile(Map<Position, TileArrangement> placedTiles, List<Tile> unplacedTiles, int nrTilesPerEdge, Position newPosition, TileArrangement tileArrangement) {
        // add the new tile to the list of placed tiles
        Map<Position, TileArrangement> newPlacedTiles = new HashMap<>(placedTiles);
        newPlacedTiles.put(newPosition, tileArrangement);

        // and remove the new tile from the list of unplaced tiles
        List<Tile> newUnplacedTiles = new ArrayList<>(unplacedTiles);
        newUnplacedTiles.remove(tileArrangement.tile());

        // try to solve the remainder
        return solve(newPlacedTiles, newUnplacedTiles, nrTilesPerEdge);
    }

    private long multiplyCornerIds(int nrTilesPerEdge, Map<Position, TileArrangement> placedTiles) {
        return placedTiles.entrySet().stream()
                .filter(es -> es.getKey().x == 0 && es.getKey().y == 0 ||
                        es.getKey().x == 0 && es.getKey().y == nrTilesPerEdge - 1 ||
                        es.getKey().x == nrTilesPerEdge - 1 && es.getKey().y == 0 ||
                        es.getKey().x == nrTilesPerEdge - 1 && es.getKey().y == nrTilesPerEdge - 1)
                .map(es -> es.getValue().tile.id)
                .mapToLong(i -> i)
                .reduce(1L, (l1, l2) -> l1 * l2);
    }

    public List<String> rotate90Clockwise(List<String> lines) {
        List<String> rotated = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < lines.get(0).length(); x++) {
            for (String line : lines) {
                sb.append(line.charAt(x));
            }
            rotated.add(sb.reverse().toString());
            sb.setLength(0);
        }

        return rotated;
    }

    public List<String> flipHorizontal(List<String> lines) {
        List<String> flipped = new ArrayList<>();
        for (String line : lines) {
            flipped.add(new StringBuilder(line).reverse().toString());
        }

        return flipped;
    }

    public List<String> flipVertical(List<String> lines) {
        List<String> flipped = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int y = lines.size() - 1; y >= 0; y--) {
            for (int x = 0; x < lines.get(0).length(); x++) {
                sb.append(lines.get(y).charAt(x));
            }
            flipped.add(sb.toString());
            sb.setLength(0);
        }

        return flipped;
    }

    private Map<Orientation, Tile> calculateOnlyOrientations(List<String> tileLines) {
        return calculateOrientations(tileLines, false);
    }

    private Map<Orientation, Tile> calculateOrientationsAndEdges(List<String> tileLines) {
        return calculateOrientations(tileLines, true);
    }

    private Map<Orientation, Tile> calculateOrientations(List<String> tileLines, boolean calculateEdges) {
        Map<Orientation, Tile> orientations = new HashMap<>();

        List<String> R90TileLines = rotate90Clockwise(tileLines);
        List<String> R180TileLines = rotate90Clockwise(R90TileLines);
        List<String> R270TileLines = rotate90Clockwise(R180TileLines);

        for (Orientation orientation : Orientation.values()) {
            List<String> lines = switch (orientation) {
                case R0_NOFLIP -> tileLines;
                case R0_FLIPH -> flipHorizontal(tileLines);
                case R0_FLIPV -> flipVertical(tileLines);
                case R90_NOFLIP -> R90TileLines;
                case R90_FLIPH -> flipHorizontal(R90TileLines);
                case R90_FLIPV -> flipVertical(R90TileLines);
                case R180_NOFLIP -> R180TileLines;
                case R180_FLIPH -> flipHorizontal(R180TileLines);
                case R180_FLIPV -> flipVertical(R180TileLines);
                case R270_NOFLIP -> R270TileLines;
                case R270_FLIPH -> flipHorizontal(R270TileLines);
                case R270_FLIPV -> flipVertical(R270TileLines);
            };

            // calculate the edges to a number ('#'->1)
            Map<Edge, String> edges = edges(lines);
            Map<Edge, Integer> mapCalculatedEdges = new HashMap<>();
            if (calculateEdges) {
                mapCalculatedEdges.put(TOP, calculateEdgeToNumber(edges.get(TOP)));
                mapCalculatedEdges.put(BOTTOM, calculateEdgeToNumber(edges.get(BOTTOM)));
                mapCalculatedEdges.put(RIGHT, calculateEdgeToNumber(edges.get(RIGHT)));
                mapCalculatedEdges.put(LEFT, calculateEdgeToNumber(edges.get(LEFT)));
            }

            Tile tile = new Tile(0, lines, mapCalculatedEdges, null);

            if (calculateEdges) {
                // check for duplicates
                if (orientations.values().stream()
                        .noneMatch(t -> t.edges.equals(mapCalculatedEdges))) {
                    orientations.put(orientation, tile);
                }
            } else {
                orientations.put(orientation, tile);
            }
        }

        return orientations;
    }

    private Map<Edge, String> edges(List<String> tileLines) {
        int nrLinesPerTile = tileLines.size() - 1;

        HashMap<Edge, String> edges = new HashMap<>();
        edges.put(TOP, tileLines.get(0));
        edges.put(BOTTOM, tileLines.get(nrLinesPerTile));
        edges.put(RIGHT, tileLines.stream()
                .map(line -> line.charAt(nrLinesPerTile))
                .map(Object::toString)
                .collect(joining()));
        edges.put(LEFT, tileLines.stream()
                .map(line -> line.charAt(0))
                .map(Object::toString)
                .collect(joining()));

        return edges;
    }

    private int calculateEdgeToNumber(String s) {
        return parseInt(s.replace('.', '0').replace('#', '1'), 2);
    }

    private List<String> stripEdgesAndCombineTiles(Map<Position, TileArrangement> placedTiles, int nrTilesPerEdge) {
        int nrLinesPerTile = placedTiles.get(new Position(0, 0)).tile.lines.size();
        int lineLength = placedTiles.get(new Position(0, 0)).tile.lines.get(0).length();
        List<String> imageLines = new ArrayList<>();
        for (int i = 0; i < nrTilesPerEdge * (nrLinesPerTile - 2); i++) {
            imageLines.add("");
        }

        for (int y = 0; y < nrTilesPerEdge; y++) {
            for (int x = 0; x < nrTilesPerEdge; x++) {
                TileArrangement placedTileArrangement = placedTiles.get(new Position(x, y));
                Tile placedTileVariation = placedTileArrangement.tile.tileVariations.get(placedTileArrangement.orientation);
                // strip edges
                for (int l = 0; l < nrLinesPerTile - 2; l++) {
                    int imageLineIndex = y * (nrLinesPerTile - 2) + l;
                    imageLines.set(imageLineIndex, imageLines.get(imageLineIndex) + placedTileVariation.lines.get(l + 1).substring(1, lineLength - 1));
                }
            }
        }
        return imageLines;
    }

    long findMaximumNrOfSeaMonstersForVariations(List<String> imageLines) {
        return calculateOnlyOrientations(imageLines).values().stream()
                .mapToLong(image -> findNrOfSeaMonsters(image.lines))
                .max()
                .orElseThrow(() -> new IllegalStateException("no solutions"));
    }

    int findNrOfSeaMonsters(List<String> imageLines) {
        int nrFoundSeaMonsters = 0;

        int imageLineLength = imageLines.get(0).length();
        for (int l = 0; l < imageLines.size() - seaMonster.size() + 1; l++) {
            // use "a sliding" window (with the size of the SeaMonster) over the image and compare this with the SeaMonster sums
            int[] integers = new int[seaMonster.size()];
            // calculate initial values
            for (int i = 0; i < seaMonster.size(); i++) {
                String part = imageLines.get(l + i).substring(0, seaMonsterLength);
                integers[i] = Integer.parseInt(part.replace('.', '0').replace('#', '1'), 2);
            }

            for (int x = 0; x < imageLineLength - seaMonsterLength + 1; x++) {
                // update our values when we move to the next position
                // "remove" left and add right
                if (x > 0) {
                    for (int i = 0; i < seaMonster.size(); i++) {
                        // left shift 1
                        integers[i] <<= 1;
                        // make sure we do not run away on the left
                        integers[i] &= seaMonsterMask;
                        // set least significant bit to 1 if we have a '#'
                        if (imageLines.get(l + i).charAt(x + seaMonsterLength - 1) == '#') {
                            integers[i] |= 1;
                        }
                    }
                }

                // check if we have a match
                boolean possibleSeaMonster = true;
                for (int i = 0; i < seaMonster.size() && possibleSeaMonster; i++) {
                    if ((integers[i] & seaMonsterSums[i]) != seaMonsterSums[i]) {
                        possibleSeaMonster = false;
                    }
                }
                if (possibleSeaMonster) {
                    nrFoundSeaMonsters++;
                }
            }
        }

        return nrFoundSeaMonsters;
    }

    private int nrHashes(List<String> lines) {
        return (int) lines.stream()
                .mapToLong(line -> line.chars().filter(c -> c == '#').count())
                .sum();
    }


    @AllArgsConstructor
    enum Orientation {
        R0_NOFLIP, R0_FLIPH, R0_FLIPV,
        R90_NOFLIP, R90_FLIPH, R90_FLIPV,
        R180_NOFLIP, R180_FLIPH, R180_FLIPV,
        R270_NOFLIP, R270_FLIPH, R270_FLIPV;

    }

    enum Edge {
        TOP, RIGHT, BOTTOM, LEFT;
    }

    record Position(int x, int y) {
        boolean isNeighbour(Position otherPosition) {
            return (otherPosition.y == y && Math.abs(otherPosition.x - x) == 1) ||
                    (otherPosition.x == x && Math.abs(otherPosition.y - y) == 1);
        }

        List<Position> neighbours() {
            return Arrays.asList(new Position(x - 1, y),
                    new Position(x + 1, y),
                    new Position(x, y - 1),
                    new Position(x, y + 1));
        }
    }

    record Tile(int id, List<String> lines, Map<Edge, Integer> edges, Map<Orientation, Tile> tileVariations) {
    }

    // @formatter:off
    record TileArrangement(Tile tile, Orientation orientation){}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {
        }.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_20().main(filename);
    }
}
