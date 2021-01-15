package nl.krudde;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class Day_17 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        int nrCycles = 6;
        EnergySource energySource = EnergySource.parseInput(inputRaw, 3);

        for (int cycle = 1; cycle <= nrCycles; cycle++) {
            energySource.doCycle();
        }

        long result = energySource.countActiveCubes();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        int nrCycles = 6;
        EnergySource energySource = EnergySource.parseInput(inputRaw, 4);

        for (int cycle = 1; cycle <= nrCycles; cycle++) {
            energySource.doCycle();
        }

        long result = energySource.countActiveCubes();

        return String.valueOf(result);
    }


    static class EnergySource {
        protected final static char ACTIVE = '#';

        private final Map<Point, List<Point>> mapNeighbours = new HashMap<>();
        private Set<Point> activeCubes;

        public EnergySource(Set<Point> activeCubes) {
            this.activeCubes = activeCubes;
        }

        private static EnergySource parseInput(List<String> inputRaw, int dimensions) {
            // determine active Cubes from the input
            Set<Point> activeCubes = getActiveCubesFromInput(inputRaw, dimensions);

            return new EnergySource(activeCubes);
        }

        private static Set<Point> getActiveCubesFromInput(List<String> inputRaw, int dimensions) {
            int length = inputRaw.size();
            Set<Point> activeCubes = new HashSet<>();
            IntStream.range(0, length).forEach(x -> IntStream.range(0, length)
                    .filter(y -> inputRaw.get(y).charAt(x) == ACTIVE)
                    .mapToObj(y -> createPoint(dimensions, x, y))
                    .forEach(activeCubes::add)
            );
            return activeCubes;
        }

        private static Point createPoint(int dimensions, int x, int y) {
            int[] coordinates = new int[dimensions];
            Arrays.fill(coordinates, 0);
            coordinates[0] = x;
            coordinates[1] = y;
            return new Point(coordinates);
        }

        /**
         * A recursive way of creating multidimensional coordinates within min/max.
         * Usually the straightforward algorithm uses nested for loops but this gets ugly for more (>3) dimensions
         *
         * @param min
         * @param max
         * @param dimensions
         * @return
         */
        static List<int[]> createCoordinates(int min, int max, int dimensions) {
            if (dimensions == 1) {
                return IntStream.range(min, max).boxed()
                        .map(i -> new int[]{i}) // int[] with 1 element
                        .collect(toList());
            } else {
                return createCoordinates(min, max, dimensions - 1).stream()
                        .flatMap(ia -> IntStream.range(min, max).mapToObj(i ->
                                IntStream.concat(Arrays.stream(ia), Arrays.stream(new int[]{i})).toArray()
                        ))
                        .collect(toList());
            }
        }

        public void doCycle() {
            // count all neighbours of active Cubes and filter on 2 or 3 occurrences
            Map<Point, Long> cubesWith2Or3ActiveNeighbours = activeCubes.stream()
                    .flatMap(cube -> getNeighbours(cube).stream())
                    .collect(groupingBy(Function.identity(), counting()))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue() == 2 || entry.getValue() == 3)
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            // add the cube if it was active
            Set<Point> newActiveCubes = cubesWith2Or3ActiveNeighbours.keySet().stream()
                    .filter(point -> activeCubes.contains(point))
                    .collect(toSet());

            // add the Cube if it was inactive and had 3 active neighbours
            newActiveCubes.addAll(cubesWith2Or3ActiveNeighbours.entrySet().stream()
                    .filter(entry -> !activeCubes.contains(entry.getKey()))
                    .filter(entry -> entry.getValue() == 3)
                    .map(Map.Entry::getKey)
                    .collect(toSet())
            );
            activeCubes = newActiveCubes;
        }

        public List<Point> getNeighbours(Point point) {
            if (!mapNeighbours.containsKey(point)) {
                mapNeighbours.put(point, point.neighbours());
            }
            return mapNeighbours.get(point);
        }

        public long countActiveCubes() {
            return activeCubes.size();
        }
    }

    /**
     * A multidimensional point
     */
    record Point(int[] coordinates) {
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point otherPoint)) {
                return false;
            }
            return Arrays.equals(coordinates, otherPoint.coordinates);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(coordinates);
        }

        /**
         * A truly clean multidimensional neighbour calculation algorithm.
         * Usually the straightforward algorithm uses nested for loops but this gets ugly for more (>3) dimensions
         * <p>
         * Inspired by the Neighbors algorithm at https://www.royvanrijn.com/blog/2019/01/longest-path/
         * which has a nice explanation for the 2-dimensional case
         * <p>
         * I basically generalised this
         * <p>
         * For the 3-dimensional case the code would be:
         * for (int i = 0; i < 27; i++) {
         * int x = (i / 3) % 3 - 1;
         * int y = (i / 3) % 3 - 1;
         * int z = (i / 9) % 3 - 1;
         * neighbours.add(new Point(new int[]{x, y, z}));
         *
         * @return List of with neighbours
         */
        List<Point> neighbours() {
            int dimensions = coordinates.length;

            // cache the powers of 3
            int[] power3 = new int[dimensions + 1];
            for (int i = 0; i <= dimensions; i++) {
                power3[i] = (int) Math.pow(3, i);
            }

            List<Point> neighbours = new ArrayList<>(power3[dimensions] - 1);

            for (int i = 0; i < power3[dimensions]; i++) {
                int[] neighbourCoordinates = new int[dimensions];
                for (int dimension = 0; dimension < dimensions; dimension++) {
                    neighbourCoordinates[dimension] = coordinates[dimension] + (i / power3[dimension]) % 3 - 1;
                }

                // skip the point itself
                if (!Arrays.equals(coordinates, neighbourCoordinates)) {
                    neighbours.add(new Point(neighbourCoordinates));
                }
            }
            return neighbours;
        }

        public boolean isNeighbour(Point otherPoint) {
            if (coordinates.length != otherPoint.coordinates.length) {
                throw new IllegalStateException("different #dimensions");
            }
            if (Arrays.equals(coordinates, otherPoint.coordinates)) {
                return false;
            }
            for (int i = 0; i < coordinates.length; i++) {
                if (Math.abs(otherPoint.coordinates[i] - coordinates[i]) > 1) {
                    return false;
                }
            }
            return true;
        }
    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_17().main(filename);
    }
}
