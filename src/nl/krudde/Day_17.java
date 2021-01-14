package nl.krudde;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static nl.krudde.Day_17.Cube.INACTIVE;

public class Day_17 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        int nrCycles = 6;
        EnergySource energySource = EnergySource.parseInput(inputRaw, nrCycles, 3);

        for (int cycle = 0; cycle < nrCycles; cycle++) {
            energySource.doCycle();
        }

        long result = energySource.countActiveCubes();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        int nrCycles = 6;
        EnergySource energySource = EnergySource.parseInput(inputRaw, nrCycles, 4);

        for (int cycle = 0; cycle < nrCycles; cycle++) {
            energySource.doCycle();
        }

        long result = energySource.countActiveCubes();

        return String.valueOf(result);
    }


    static class EnergySource {
        private final List<Cube> cubes;
        private int currentCycle = 0;

        public EnergySource(List<Cube> cubes) {
            this.cubes = cubes;

            // determine neighbours cubes, use a Map<Point, Cube>
            Map<Point, Cube> cubeMap = cubes.stream()
                    .collect(Collectors.toMap(cube -> cube.point, cube -> cube));
            cubes.forEach(cube -> cube.neighbours.addAll(cube.point.neighbours().stream()
                    .map(cubeMap::get)
                    .filter(Objects::nonNull)
                    .collect(toList())));
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

        private static EnergySource parseInput(List<String> inputRaw, int nrCycles, int dimensions) {
            int length = inputRaw.size();

            // create coordinates
            List<int[]> listCoordinates = createCoordinates(-nrCycles, length + nrCycles, dimensions);

            // create Cubes
            List<Cube> cubes = listCoordinates.stream()
                    .map(coordinates -> new Cube(new Point(coordinates), nrCycles, INACTIVE))
                    .collect(toList());

            // set the state for the Cubes in the input
            cubes.stream()
                    .filter(cube -> Arrays.stream(Arrays.copyOfRange(cube.point.coordinates, 2, cube.point.coordinates.length)).allMatch(coordinate -> coordinate == 0)) // z and higher dimensions must all be 0
                    .filter(cube -> cube.point.coordinates[0] >= 0 && cube.point.coordinates[0] < length) // x
                    .filter(cube -> cube.point.coordinates[1] >= 0 && cube.point.coordinates[1] < length) // y
                    .forEach(cube -> cube.states[0] = inputRaw.get(cube.point.coordinates[1]).charAt(cube.point.coordinates[0]));

            return new EnergySource(cubes);
        }

        public void doCycle() {
            currentCycle++;
            cubes.stream()
                    // limit the cubes a bit as the area "expands" each cycle
                    .filter(cube -> cube.point.coordinates[0]>=-currentCycle)
                    .filter(cube -> cube.point.coordinates[1]>=-currentCycle)
                    .filter(cube -> Arrays.stream(Arrays.copyOfRange(cube.point.coordinates, 2, cube.point.coordinates.length)).allMatch(coordinate -> coordinate >= -currentCycle))
                    .filter(cube -> Arrays.stream(Arrays.copyOfRange(cube.point.coordinates, 2, cube.point.coordinates.length)).allMatch(coordinate -> coordinate <= currentCycle))
                    .forEach(cube -> cube.newState(currentCycle));
        }

        public long countActiveCubes() {
            return cubes.stream()
                    .filter(cube -> cube.isActive(currentCycle))
                    .count();
        }
    }

    static class Cube {
        protected final static char ACTIVE = '#';
        protected final static char INACTIVE = '.';

        private final Point point;
        public List<Cube> neighbours;
        public char[] states;

        public Cube(Point point, int nrCycles, char initialState) {
            this.point = point;
            states = new char[nrCycles + 1];
            states[0] = initialState;
            neighbours = new ArrayList<>();
        }

        public void newState(int cycle) {
            int activeNeighbours = activeNeighbours(cycle - 1);
            if ((isActive(cycle - 1) && (activeNeighbours == 2 || activeNeighbours == 3) ||
                    (!isActive(cycle - 1) && activeNeighbours == 3))) {
                states[cycle] = ACTIVE;
            } else {
                states[cycle] = INACTIVE;
            }
        }

        private int activeNeighbours(int cycle) {
            return (int) neighbours.stream()
                    .filter(cube -> cube.isActive(cycle))
                    .count();
        }

        public boolean isActive(int cycle) {
            return states[cycle] == ACTIVE;
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
                    neighbourCoordinates[dimension] = (coordinates[dimension] + ((i / power3[dimension]) % 3 - 1));
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
