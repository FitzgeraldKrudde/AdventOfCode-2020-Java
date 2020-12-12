package nl.krudde;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day_12 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        // anticipated that for part 2 we might need the ship position history (..)
        final List<ShipNavigation> pointList = new ArrayList<>();
        pointList.add(new ShipNavigation(new Point(0, 0), Direction.E));

        inputRaw.forEach(line -> pointList.add(pointList.get(pointList.size() - 1).move(line)));

        Point end = pointList.get(pointList.size() - 1).shipPoint;
        long result = Math.abs(end.x()) + Math.abs(end.y());

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        final List<ShipNavigationWithWaypoint> pointList = new ArrayList<>();
        pointList.add(new ShipNavigationWithWaypoint(new Point(0, 0), new Point(10, 1)));

        inputRaw.forEach(line -> pointList.add(pointList.get(pointList.size() - 1).move(line)));

        Point end = pointList.get(pointList.size() - 1).shipPoint;
        long result = Math.abs(end.x()) + Math.abs(end.y());

        return String.valueOf(result);
    }

    @AllArgsConstructor
    enum Direction {
        N('N'),
        E('E'),
        S('S'),
        W('W');

        private final static Direction[] directions = {N, E, S, W};
        private final char printCharacter;

        static Direction of(char direction) {
            return Arrays.stream(values())
                    .filter(d -> d.printCharacter == direction)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("unknown direction"));
        }

        private int currentDirectionIndex() {
            for (int i = 0; i < directions.length; i++) {
                if (printCharacter == directions[i].printCharacter) {
                    return i;
                }
            }
            throw new IllegalArgumentException("invalid direction");
        }

        public Direction turn(char turn, int degrees) {
            int currentDirectionIndex = currentDirectionIndex();
            return switch (turn) {
                case 'L' -> directions[((currentDirectionIndex - (degrees / 90)) + directions.length) % directions.length];
                case 'R' -> directions[((currentDirectionIndex + (degrees / 90)) + directions.length) % directions.length];
                default -> throw new IllegalStateException("invalid turn");
            };
        }
    }

    record ShipNavigation(Point shipPoint, Direction currentDirection) {

        public ShipNavigation move(String navigation) {
            char direction = navigation.charAt(0);
            int value = Integer.parseInt(navigation.substring(1));
            return switch (direction) {
                case 'N', 'E', 'S', 'W' -> new ShipNavigation(shipPoint.move(Direction.of(direction), value), currentDirection);
                case 'F' -> new ShipNavigation(shipPoint.move(currentDirection, value), currentDirection);
                case 'R', 'L' -> new ShipNavigation(shipPoint, currentDirection.turn(direction, value));
                default -> throw new IllegalStateException("invalid direction");
            };
        }
    }

    record ShipNavigationWithWaypoint(Point shipPoint, Point wayPoint) {

        public ShipNavigationWithWaypoint move(String navigation) {
            char direction = navigation.charAt(0);
            int value = Integer.parseInt(navigation.substring(1));
            return switch (direction) {
                case 'N', 'E', 'S', 'W' -> new ShipNavigationWithWaypoint(shipPoint, wayPoint.move(Direction.of(direction), value));
                case 'F' -> new ShipNavigationWithWaypoint(shipPoint.move(wayPoint, value), wayPoint);
                case 'R', 'L' -> new ShipNavigationWithWaypoint(shipPoint, waypointTurn(wayPoint, direction, value));
                default -> throw new IllegalStateException("invalid direction");
            };
        }

        private Point waypointTurn(Point waypoint, char turn, int degrees) {
            if (turn == 'L') {
                degrees = 360 - degrees;
            }

            return switch (degrees) {
                case 90 -> new Point(waypoint.y, -waypoint.x);
                case 180 -> new Point(-waypoint.x, -waypoint.y);
                case 270 -> new Point(-waypoint.y, waypoint.x);
                default -> throw new IllegalStateException("Unexpected value: " + degrees);
            };
        }
    }

    record Point(int x, int y) {
        public Point move(Direction direction, int nrMoves) {
            return switch (direction) {
                case N -> new Point(x, y + nrMoves);
                case E -> new Point(x + nrMoves, y);
                case S -> new Point(x, y - nrMoves);
                case W -> new Point(x - nrMoves, y);
            };
        }

        public Point move(Point waypoint, int nrMoves) {
            return new Point(x + nrMoves * waypoint.x, y + nrMoves * waypoint.y);
        }

    }

    // @formatter:off

        static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_-1", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_12().main(filename);
    }
}
