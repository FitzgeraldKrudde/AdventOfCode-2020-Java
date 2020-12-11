package nl.krudde;

import lombok.*;

import java.util.Arrays;
import java.util.List;

import static nl.krudde.Day_11.SeatType.*;

public class Day_11 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        SeatLayout.setPrintDebugging(false);
        SeatLayout seatLayout = SeatLayout.of(inputRaw);

        SeatLayout newSeatLayout = seatLayout.doRoundPart1();
        while (!seatLayout.equals(newSeatLayout)) {
            seatLayout = newSeatLayout;
            newSeatLayout = seatLayout.doRoundPart1();
        }

        long result = seatLayout.nrOccupiedSeats();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        SeatLayout.setPrintDebugging(false);
        SeatLayout seatLayout = SeatLayout.of(inputRaw);

        SeatLayout newSeatLayout = seatLayout.doRoundPart2();
        while (!seatLayout.equals(newSeatLayout)) {
            seatLayout = newSeatLayout;
            newSeatLayout = seatLayout.doRoundPart2();
        }

        long result = seatLayout.nrOccupiedSeats();

        return String.valueOf(result);
    }

    @RequiredArgsConstructor
    static class SeatLayout {
        @NonNull
        private final SeatType[][] seats;
        @Setter
        private static boolean printDebugging = false;

        @Override
        public boolean equals(Object obj) {
            return Arrays.deepEquals(seats, ((SeatLayout) obj).seats);
        }

        public static SeatLayout of(List<String> input) {
            int rows = input.size();
            int seatsInrRow = input.get(0).length();
            SeatType[][] seats = new SeatType[seatsInrRow][rows];

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < seatsInrRow; x++) {
                    seats[x][y] = SeatType.of(input.get(y).charAt(x));
                }
            }

            SeatLayout seatLayout = new SeatLayout(seats);
            seatLayout.print();
            return seatLayout;
        }

        public SeatLayout doRoundPart1() {
            SeatType[][] seatsNew = new SeatType[seats.length][seats[0].length];
            for (int y = 0; y < seats[0].length; y++) {
                for (int x = 0; x < seats.length; x++) {
                    int nrAdjacentOccupiedSeats = nrAdjacentOccupiedSeats(x, y);
                    seatsNew[x][y] = switch (seats[x][y]) {
                        case EMPTY -> nrAdjacentOccupiedSeats == 0 ? OCCUPIED : EMPTY;
                        case OCCUPIED -> nrAdjacentOccupiedSeats >= 4 ? EMPTY : OCCUPIED;
                        case FLOOR -> FLOOR;
                    };
                }
            }

            SeatLayout seatLayout = new SeatLayout(seatsNew);
            seatLayout.print();
            return seatLayout;
        }

        public SeatLayout doRoundPart2() {
            SeatType[][] seatsNew = new SeatType[seats.length][seats[0].length];
            for (int y = 0; y < seats[0].length; y++) {
                for (int x = 0; x < seats.length; x++) {
                    int nrDirectionOccupiedSeats = nrOccupiedSeatsInDirections(x, y);
                    seatsNew[x][y] = switch (seats[x][y]) {
                        case EMPTY -> nrDirectionOccupiedSeats == 0 ? OCCUPIED : EMPTY;
                        case OCCUPIED -> nrDirectionOccupiedSeats >= 5 ? EMPTY : OCCUPIED;
                        case FLOOR -> FLOOR;
                    };
                }
            }

            SeatLayout seatLayout = new SeatLayout(seatsNew);
            seatLayout.print();
            return seatLayout;
        }

        public int nrOccupiedSeats() {
            int nrOccupiedSeats = 0;
            for (SeatType[] seatTypes : seats) {
                for (SeatType seatType : seatTypes) {
                    if (seatType == OCCUPIED) {
                        nrOccupiedSeats++;
                    }
                }
            }
            return nrOccupiedSeats;
        }

        private int nrAdjacentOccupiedSeats(int x, int y) {
            int occupiedSeatsAround = 0;

            occupiedSeatsAround += isValidPosition(x + 1, y) && isOccupied(x + 1, y) ? 1 : 0; // right
            occupiedSeatsAround += isValidPosition(x - 1, y) && isOccupied(x - 1, y) ? 1 : 0; // left
            occupiedSeatsAround += isValidPosition(x, y + 1) && isOccupied(x, y + 1) ? 1 : 0; // below
            occupiedSeatsAround += isValidPosition(x, y - 1) && isOccupied(x, y - 1) ? 1 : 0; // above

            occupiedSeatsAround += isValidPosition(x - 1, y - 1) && isOccupied(x - 1, y - 1) ? 1 : 0; // left above
            occupiedSeatsAround += isValidPosition(x + 1, y - 1) && isOccupied(x + 1, y - 1) ? 1 : 0; // right above
            occupiedSeatsAround += isValidPosition(x - 1, y + 1) && isOccupied(x - 1, y + 1) ? 1 : 0; // left below
            occupiedSeatsAround += isValidPosition(x + 1, y + 1) && isOccupied(x + 1, y + 1) ? 1 : 0; // right below

            return occupiedSeatsAround;
        }

        private boolean isValidPosition(int x, int y) {
            return x >= 0 && x < seats.length && y >= 0 && y < seats[x].length;
        }

        private boolean isOccupied(int x, int y) {
            return seats[x][y] == OCCUPIED;
        }

        private int seeingOccupiedChairInDirection(int startx, int starty, int movex, int movey) {
            int x = startx + movex;
            int y = starty + movey;
            while (isValidPosition(x, y)) {
                if (seats[x][y] == OCCUPIED) {
                    return 1;
                }
                if (seats[x][y] == EMPTY) {
                    return 0;
                }
                x += movex;
                y += movey;
            }

            return 0;
        }

        private int nrOccupiedSeatsInDirections(int x, int y) {
            int occupiedSeatsAround = seeingOccupiedChairInDirection(x, y, 1, 0) + // right
                    seeingOccupiedChairInDirection(x, y, -1, 0) + //left
                    seeingOccupiedChairInDirection(x, y, 0, 1) + // below
                    seeingOccupiedChairInDirection(x, y, 0, -1) + // above
                    seeingOccupiedChairInDirection(x, y, -1, -1) + // above left
                    seeingOccupiedChairInDirection(x, y, 1, -1) + // above right
                    seeingOccupiedChairInDirection(x, y, -1, 1) + // below left
                    seeingOccupiedChairInDirection(x, y, 1, 1) // below right
                    ;

            return occupiedSeatsAround;
        }

        public void print() {
            if (!printDebugging) {
                return;
            }

            System.out.println();
            for (int y = 0; y < seats[0].length; y++) {
                for (int x = 0; x < seats.length; x++) {
                    System.out.print(seats[x][y].printCharacter);
                }
                System.out.println();
            }
        }
    }

    @AllArgsConstructor
    enum SeatType {
        OCCUPIED('#'),
        EMPTY('L'),
        FLOOR('.'),
        ;

        @Getter
        private final char printCharacter;

        static SeatType of(int i) {
            return Arrays.stream(values())
                    .filter(c -> c.printCharacter == i)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("unknown seat type"));
        }
    }
    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_11().main(filename);
    }
}
