package nl.krudde;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.AbstractMap.SimpleEntry;
import static nl.krudde.Day_20.Corner.*;
import static nl.krudde.Day_20.Edge.*;

public class Day_20 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Tile> tiles = parseInput(inputRaw);

        int nrTilesPerEdge = (int) Math.sqrt(tiles.size());
        Map<Position, TileArrangement> placedTiles = solve(new HashMap<>(), tiles, nrTilesPerEdge);

        long result = placedTiles.entrySet().stream()
                .filter(es -> es.getKey().x == 0 && es.getKey().y == 0 ||
                        es.getKey().x == 0 && es.getKey().y == nrTilesPerEdge - 1 ||
                        es.getKey().x == nrTilesPerEdge - 1 && es.getKey().y == 0 ||
                        es.getKey().x == nrTilesPerEdge - 1 && es.getKey().y == nrTilesPerEdge - 1)
                .map(es -> es.getValue().tile.id)
                .mapToLong(i -> i)
                .reduce(1L, (l1, l2) -> l1 * l2);

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Tile> input = parseInput(inputRaw);


        long result = 0;

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
            int id = Integer.parseInt(lineWithId.split("\\s+|:")[1]);
            List<String> tileLines = inputRaw.subList(i * nrInputLinesPerTile + 1, i * nrInputLinesPerTile + 1 + nrLinesPerTile);

            // calculate all variations (rotating and flipping)
            // calculate "edges" i.e. interpret '.' as 0 and '#' as 1 and convert this binary number to int
            Map<Orientation, Map<Edge, Integer>> orientationEdges = calculateOrientationEdges(tileLines);

            Tile tile = new Tile(id, tileLines, orientationEdges);
            tiles.add(tile);
        }

        System.out.println("read #tiles = " + tiles.size());
        return tiles;
    }

    public List<String>rotate(List<String>lines){
        
    }

    private Map<Orientation, Map<Edge, Integer>> calculateOrientationEdges(List<String> tileLines) {
        Map<Orientation, Map<Edge, Integer>> orientations = new HashMap<>();

        // to calculate all variations of rotation and flipping use strings for:
        // - edges
        // - ribs (edge without corner)
        // - corner
        // Use these when rotating/flipping to construct a complete string
        Map<Edge, String> edges = edges(tileLines);
        Map<Edge, String> ribs = ribs(edges);
        Map<Edge, String> ribsReversed = reverseRibs(ribs);
        Map<Corner, String> corners = corners(tileLines);

        for (Orientation orientation : Orientation.values()) {
            Map<Edge, Integer> mapEdges = new HashMap<>();
            switch (orientation) {
                case R0_NOFLIP:
                    mapEdges.put(TOP, calculateEdgeToNumber(edges.get(TOP)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(edges.get(BOTTOM)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(edges.get(RIGHT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(edges.get(LEFT)));
                    break;
                case R0_FLIPH:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribsReversed.get(TOP) + corners.get(TOP_LEFT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribsReversed.get(BOTTOM) + corners.get(TOP_LEFT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(LEFT) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribs.get(RIGHT) + corners.get(BOTTOM_RIGHT)));
                    break;
                case R0_FLIPV:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribs.get(BOTTOM) + corners.get(BOTTOM_RIGHT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(TOP) + corners.get(TOP_RIGHT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(RIGHT) + corners.get(TOP_RIGHT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribsReversed.get(LEFT) + corners.get(TOP_LEFT)));
                    break;

                case R90_NOFLIP:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribsReversed.get(LEFT) + corners.get(TOP_LEFT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(RIGHT) + corners.get(TOP_RIGHT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribsReversed.get(TOP) + corners.get(TOP_RIGHT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribsReversed.get(BOTTOM) + corners.get(BOTTOM_RIGHT)));
                    break;
                case R90_FLIPH:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(LEFT) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribs.get(RIGHT) + corners.get(BOTTOM_RIGHT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribs.get(BOTTOM) + corners.get(BOTTOM_RIGHT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(TOP) + corners.get(TOP_RIGHT)));
                    break;
                case R90_FLIPV:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(RIGHT) + corners.get(TOP_RIGHT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribs.get(LEFT) + corners.get(TOP_LEFT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribsReversed.get(TOP) + corners.get(TOP_LEFT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(BOTTOM) + corners.get(BOTTOM_LEFT)));
                    break;

                case R180_NOFLIP:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(BOTTOM) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribsReversed.get(TOP) + corners.get(TOP_LEFT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribsReversed.get(LEFT) + corners.get(TOP_LEFT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(RIGHT) + corners.get(TOP_RIGHT)));
                    break;
                case R180_FLIPH:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribs.get(BOTTOM) + corners.get(BOTTOM_RIGHT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(TOP) + corners.get(TOP_RIGHT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(BOTTOM) + corners.get(TOP_RIGHT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribsReversed.get(LEFT) + corners.get(TOP_LEFT)));
                    break;
                case R180_FLIPV:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribsReversed.get(TOP) + corners.get(TOP_LEFT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(BOTTOM) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(LEFT) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribs.get(RIGHT) + corners.get(BOTTOM_RIGHT)));
                    break;

                case R270_NOFLIP:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribs.get(RIGHT) + corners.get(BOTTOM_RIGHT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(LEFT) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(RIGHT) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribsReversed.get(RIGHT) + corners.get(TOP_LEFT)));
                    break;
                case R270_FLIPH:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(RIGHT) + corners.get(TOP_RIGHT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribsReversed.get(LEFT) + corners.get(TOP_LEFT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribsReversed.get(RIGHT) + corners.get(TOP_LEFT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(BOTTOM_RIGHT) + ribsReversed.get(BOTTOM) + corners.get(BOTTOM_LEFT)));
                    break;
                case R270_FLIPV:
                    mapEdges.put(TOP, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(LEFT) + corners.get(BOTTOM_LEFT)));
                    mapEdges.put(BOTTOM, calculateEdgeToNumber(corners.get(TOP_RIGHT) + ribs.get(RIGHT) + corners.get(BOTTOM_RIGHT)));
                    mapEdges.put(RIGHT, calculateEdgeToNumber(corners.get(BOTTOM_LEFT) + ribs.get(BOTTOM) + corners.get(BOTTOM_RIGHT)));
                    mapEdges.put(LEFT, calculateEdgeToNumber(corners.get(TOP_LEFT) + ribs.get(TOP) + corners.get(TOP_RIGHT)));
                    break;
            }
            orientations.put(orientation, mapEdges);
        }
        // TODO distinct
        return orientations;
    }

    private Map<Edge, String> reverseRibs(Map<Edge, String> ribs) {
        return ribs.entrySet().stream()
                .map(entry -> new SimpleEntry<>(entry.getKey(), new StringBuilder(entry.getValue()).reverse().toString()))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    private Map<Edge, String> ribs(Map<Edge, String> edges) {
        return edges.entrySet().stream()
                .map(entry -> new SimpleEntry<>(entry.getKey(), rib(entry.getValue())))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    private Map<Corner, String> corners(List<String> tileLines) {
        HashMap<Corner, String> corners = new HashMap<>();

        corners.put(TOP_LEFT, tileLines.get(0).substring(0, 1));
        corners.put(Corner.TOP_RIGHT, tileLines.get(0).substring(tileLines.get(0).length() - 1));
        corners.put(Corner.BOTTOM_LEFT, tileLines.get(tileLines.size() - 1).substring(0, 1));
        corners.put(Corner.BOTTOM_RIGHT, tileLines.get(tileLines.size() - 1).substring(tileLines.get(tileLines.size() - 1).length() - 1));

        return corners;
    }

    private String rib(String edge) {
        return edge.substring(1, edge.length() - 2);
    }

    private Map<Edge, String> edges(List<String> tileLines) {
        int nrLinesPerTile = tileLines.size() - 1;

        HashMap<Edge, String> edges = new HashMap<>();
        edges.put(TOP, tileLines.get(0));
        edges.put(BOTTOM, tileLines.get(nrLinesPerTile));
        edges.put(RIGHT, tileLines.stream()
                .map(line -> line.charAt(nrLinesPerTile))
                .map(Object::toString)
                .collect(Collectors.joining()));
        edges.put(LEFT, tileLines.stream()
                .map(line -> line.charAt(0))
                .map(Object::toString)
                .collect(Collectors.joining()));

        return edges;
    }

    private int calculateEdgeToNumber(String s) {
        return Integer.parseInt(s.replace('.', '0').replace('#', '1'), 2);
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
                        .map(orientation -> new TileArrangement(tile, orientation)))
//                .peek(ta -> System.out.println("position = " + nextPosition + " ta = " + ta))
                .filter(tileArrangement -> fits(placedTiles, nextPosition, tileArrangement))
                .map(tileArrangement -> tryTile(placedTiles, unplacedTiles, nrTilesPerEdge, nextPosition, tileArrangement))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    private Map<Position, TileArrangement> tryTile(Map<Position, TileArrangement> placedTiles, List<Tile> unplacedTiles, int nrTilesPerEdge, Position newPosition, TileArrangement tileArrangement) {
        // add the new tile to the list of placed tiles
        Map<Position, TileArrangement> newPlacedTiles = new HashMap<>(placedTiles);
        newPlacedTiles.put(newPosition, tileArrangement);

        // and remove the new tile from the list of unplaced tiles
        List<Tile> newUnplacedTiles = new ArrayList<>(unplacedTiles);
        newUnplacedTiles.remove(tileArrangement.tile());

        return solve(newPlacedTiles, newUnplacedTiles, nrTilesPerEdge);
    }

    private boolean fits(Map<Position, TileArrangement> placedTiles, Position position, TileArrangement newTileArrangement) {
        boolean result = placedTiles.entrySet().stream()
                // TODO determine neighbours and check if in placedtiles iso get all and check if neighbour
                .filter(positionTileArrangementEntry -> positionTileArrangementEntry.getKey().isNeighbour(position))
                .allMatch(positionTileArrangementEntry -> edgeEqual(positionTileArrangementEntry, position, newTileArrangement));
        return result;
    }

    private boolean edgeEqual(Map.Entry<Position, TileArrangement> placedTileArrangementEntry, Position position, TileArrangement newTileArrangement) {
        int NREDGES = 4;

        Tile placedTile = placedTileArrangementEntry.getValue().tile;
        Orientation placedTileOrientation = placedTileArrangementEntry.getValue().orientation;
        Position placedTilePosition = placedTileArrangementEntry.getKey();

        Tile newTile = newTileArrangement.tile;
        Orientation newTileOrientation = newTileArrangement.orientation;

        if (placedTilePosition.x != position.x) {
            // besides each other
            if (placedTilePosition.x < position.x) {
                // existing tile on the left of the new tile
                System.out.println("placedTile.orientationEdges.get(placedTileOrientation).get(RIGHT) = " + placedTile.orientationEdges.get(placedTileOrientation).get(RIGHT));
                System.out.println("newTile.orientationEdges.get(newTileOrientation).get(LEFT) = " + newTile.orientationEdges.get(newTileOrientation).get(LEFT));
                boolean result = placedTile.orientationEdges.get(placedTileOrientation).get(RIGHT).equals(newTile.orientationEdges.get(newTileOrientation).get(LEFT));
                return result;
            } else {
                // existing tile on the right of the new tile
                return placedTile.orientationEdges.get(placedTileOrientation).get(LEFT).equals(newTile.orientationEdges.get(newTileOrientation).get(RIGHT));
            }
        } else {
            // above each other
            if (placedTilePosition.y < position.y) {
                // existing tile above of the new tile
                return placedTile.orientationEdges.get(placedTileOrientation).get(BOTTOM).equals(newTile.orientationEdges.get(newTileOrientation).get(TOP));
            } else {
                // existing tile below of the new tile
                return placedTile.orientationEdges.get(placedTileOrientation).get(TOP).equals(newTile.orientationEdges.get(newTileOrientation).get(BOTTOM));
            }
        }
    }

    private Position determineNextPosition(Map<Position, TileArrangement> placedTiles, int nrTilesPerEdge) {
        int x = placedTiles.keySet().stream()
                .max(Comparator.comparingInt(tile -> tile.x))
                .map(tileArrangement -> tileArrangement.x)
                .orElse(-1);
        int y = placedTiles.keySet().stream()
                .max(Comparator.comparingInt(position -> position.y))
                .map(position -> position.y)
                .orElse(-1);
        // check last position in row
        if (x == nrTilesPerEdge - 1) {
            x = 0;
            y++;
        } else {
            x++;
            if (y == -1) {
                y++;
            }
        }
        return new Position(x, y);
    }

    record Position(int x, int y) {
        boolean isNeighbour(Position otherPosition) {
            return (otherPosition.y == y && Math.abs(otherPosition.x - x) == 1) ||
                    (otherPosition.x == x && Math.abs(otherPosition.y - y) == 1);
        }
    }

    @AllArgsConstructor
    enum Orientation {
        R0_NOFLIP, R0_FLIPH, R0_FLIPV,
        R90_NOFLIP, R90_FLIPH, R90_FLIPV,
        R180_NOFLIP, R180_FLIPH, R180_FLIPV,
        R270_NOFLIP, R270_FLIPH, R270_FLIPV
    }

    enum Edge {
        TOP, RIGHT, BOTTOM, LEFT
    }

    enum Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    record Tile(int id, List<String> lines, Map<Orientation, Map<Edge, Integer>> orientationEdges) {
        public void print() {
            System.out.println("id = " + id);
            for (String line : lines) {
                System.out.println(line);
            }
            for (Map.Entry<Orientation, Map<Edge, Integer>> entry : orientationEdges.entrySet()) {
                System.out.println("entry = " + entry);
            }
        }
    }

    // @formatter:off
    record TileArrangement(Tile tile, Orientation orientation){}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_20().main(filename);
    }
}
