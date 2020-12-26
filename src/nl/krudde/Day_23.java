package nl.krudde;

import java.util.List;

public class Day_23 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        CupGame cupGame = new CupGame(inputRaw.get(0));
        cupGame.doMoves(100);

        return cupGame.cupsAfterCup1();
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        CupGame cupGame = new CupGame(inputRaw.get(0), 1000 * 1000);
        cupGame.doMoves(10 * 1000 * 1000);

        return cupGame.getMultipliedStarPositions();
    }

    /**
     * I build this initially with a Java LinkedList but this performs horrible for part 2.
     * I looked at Reddit and learned that a plain array is suitable for this situation!
     * This is possible as all elements appear once and only the relation to the next element is relevant
     * <p>
     * Use an array where the index is a cup and the value is the cup it points to.
     * I.e. 2 5 4 becomes
     * c[2]=5
     * c[5]=4
     * c[4]=2
     * <p>
     * element 0 is not used
     */
    static class CupGame {
        private final int[] cups;
        int currentCup;

        public CupGame(String cupsString) {
            cups = new int[cupsString.length() + 1];
            for (int i = 1; i < cupsString.length(); i++) {
                cups[Character.getNumericValue(cupsString.charAt(i - 1))] = Character.getNumericValue(cupsString.charAt(i));
            }
            // point last cup to the front
            cups[Character.getNumericValue(cupsString.charAt(cupsString.length() - 1))] = Character.getNumericValue(cupsString.charAt(0));

            currentCup = Character.getNumericValue(cupsString.charAt(0));
        }

        public CupGame(String cupsString, int max) {
            cups = new int[max + 1];
            for (int i = 1; i < cupsString.length(); i++) {
                cups[Character.getNumericValue(cupsString.charAt(i - 1))] = Character.getNumericValue(cupsString.charAt(i));
            }

            // point the last one to the first number to add
            cups[Character.getNumericValue(cupsString.charAt(cupsString.length() - 1))] = cupsString.length() + 1;
            // add numbers til the max
            for (int i = cupsString.length() + 2; i <= max; i++) {
                cups[i - 1] = i;
            }

            // point last cup to the front
            cups[max] = Character.getNumericValue(cupsString.charAt(0));

            currentCup = Character.getNumericValue(cupsString.charAt(0));
        }

        public void doMoves(int nrMoves) {
            for (int move = 1; move <= nrMoves; move++) {
                doMove();
            }
        }

        private void doMove() {
            int pickedCup1 = cups[currentCup];
            int pickedCup2 = cups[pickedCup1];
            int pickedCup3 = cups[pickedCup2];
            if (pickedCup1 <= 0 || pickedCup2 <= 0 || pickedCup3 <= 0) {
                throw new IllegalStateException("picked<0");
            }

            // select destination cup value, start at current cup - 1, check underflow
            int destinationCup = currentCup - 1;
            while (true) {
                if (destinationCup <= 0) {
                    destinationCup = cups.length - 1;
                }
                if (pickedCup1 == destinationCup || pickedCup2 == destinationCup || pickedCup3 == destinationCup) {
                    destinationCup--;
                } else {
                    break;
                }
            }

            // update references:
            // - current cup to where the 3rd deleted cup pointed
            // - 3rd picked cup points to destination
            // - destination to the first deleted element
            cups[currentCup] = cups[pickedCup3];
            cups[pickedCup3] = cups[destinationCup];
            cups[destinationCup] = pickedCup1;

            // update the current cup i.e. the next one
            currentCup = cups[currentCup];
        }

        public String cupsAfterCup1() {
            int cup = 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < 9; i++) {
                sb.append(cups[cup]);
                cup = cups[cup];
            }

            return sb.toString();
        }

        public String getMultipliedStarPositions() {
            return String.valueOf((long) cups[1] * (long) cups[cups[1]]);
        }
    }
    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_23().main(filename);
    }
}
