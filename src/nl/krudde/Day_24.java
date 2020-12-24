package nl.krudde;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day_24 extends Day {
    private final static boolean WHITE = false;
    private final static boolean BLACK = true;

    @Override
    public String doPart1(List<String> inputRaw) {
        // use a Map with key->(x,y), value->flipped
        Map<Point, Boolean> mapTiles = new HashMap<>();

        inputRaw.stream()
                .map(this::calculateEndpoint)
                .map(point -> new SimpleEntry<>(point, mapTiles.put(point, BLACK)))
                .filter(entry -> entry.getValue() != null) // check if we got a value from the put() i.e. there was already an entry in the map
                .forEach(entry -> mapTiles.put(entry.getKey(), !entry.getValue())); // put it again in the map with a negated value

        long result = nrBlackTiles(mapTiles);

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        Map<Point, Boolean> mapTiles = new HashMap<>();

        Map<Point, Boolean> finalMapTiles = mapTiles;
        inputRaw.stream()
                .map(this::calculateEndpoint)
                .map(point -> new SimpleEntry<>(point, finalMapTiles.put(point, BLACK)))
                .filter(entry -> entry.getValue() != null) // check if we got a value from the put() i.e. there was already an entry in the map
                .forEach(entry -> finalMapTiles.put(entry.getKey(), !entry.getValue())); // put it again in the map with a negated value

        for (int day = 1; day <= 100; day++) {
            mapTiles = doDay(mapTiles);
        }

        long result = nrBlackTiles(mapTiles);

        return String.valueOf(result);
    }

    Point calculateEndpoint(String line) {
        // find all 2-part directions
        String[] directions = line.split("((?<=nw)|(?=nw))|((?<=ne)|(?=ne))|((?<=sw)|(?=sw))|((?<=se)|(?=se))");
        return Arrays.stream(directions)
                .flatMap(this::splitCombinedDirections)
                .map(direction -> convertDirectionMove((String) direction))
                .reduce(new Point(0, 0), (m1, m2) -> new Point(m1.x + m2.x, m1.y + m2.y));
    }

    private Stream<?> splitCombinedDirections(String direction) {
        return switch (direction) {
            case "ne", "nw", "se", "sw" -> Stream.of(direction);
            default -> direction.codePoints().mapToObj(c -> String.valueOf((char) c));
        };
    }

    private Point convertDirectionMove(String direction) {
        return switch (direction) {
            case "ne" -> new Point(1, -1);
            case "nw" -> new Point(-1, -1);
            case "se" -> new Point(1, 1);
            case "sw" -> new Point(-1, 1);
            case "w" -> new Point(-2, 0);
            case "e" -> new Point(2, 0);
            default -> new Point(0, 0);
        };
    }

    private long nrBlackTiles(Map<Point, Boolean> mapTiles) {
        return mapTiles.values().stream()
                .filter(b -> b == BLACK)
                .count();
    }

    private Map<Point, Boolean> doDay(Map<Point, Boolean> mapTiles) {
        // expand all existing tiles with neighbours
        mapTiles.putAll(mapTiles.entrySet().stream()
                .flatMap(entry -> entry.getKey().neighbours().stream())
                .distinct()
                .filter(key -> !mapTiles.containsKey(key))
                .collect(Collectors.toMap(entry -> entry, entry -> WHITE)));

        // flip tiles when needed
        return mapTiles.entrySet().parallelStream()
                .map(tile -> flipTileIfNeeded(mapTiles, tile))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Point, Boolean> flipTileIfNeeded(Map<Point, Boolean> mapTiles, Map.Entry<Point, Boolean> tile) {
        long nrBlackNeighbourTiles = tile.getKey().neighbours().stream()
                .filter(mapTiles::containsKey)
                .filter(point -> mapTiles.get(point) == BLACK)
                .count();

        if (tile.getValue() == BLACK) {
            if (nrBlackNeighbourTiles == 0 || nrBlackNeighbourTiles > 2) {
                return new SimpleEntry<>(tile.getKey(), WHITE);
            } else {
                return tile;
            }
        } else if (nrBlackNeighbourTiles == 2) {
            return new SimpleEntry<>(tile.getKey(), BLACK);
        } else {
            return tile;
        }
    }

    record Point(int x, int y) {
        List<Point> neighbours() {
            return Arrays.asList(
                    new Point(x + 1, y - 1),
                    new Point(x - 1, y - 1),
                    new Point(x + 1, y + 1),
                    new Point(x - 1, y + 1),
                    new Point(x - 2, y),
                    new Point(x + 2, y)
            );
        }
    }

    // @formatter:off
    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_24().main(filename);
    }
}
