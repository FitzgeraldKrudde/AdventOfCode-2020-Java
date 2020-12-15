package nl.krudde;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Day_15 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Integer> startingNumbers = parseInput(inputRaw);

        long result = spokenNumberAtTurn(startingNumbers, 2020);

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Integer> startingNumbers = parseInput(inputRaw);

        long result = spokenNumberAtTurn(startingNumbers, 30000000);

        return String.valueOf(result);
    }

    private long spokenNumberAtTurn(List<Integer> startingNumbers, int nrTurns) {
        HashMap<Integer, Integer> previousSpokenHashMap = new HashMap<>(nrTurns);

        // put the starting numbers in a HashMap (use the turn as value)
        // except the last, this is the last spoken number
        IntStream.range(0, startingNumbers.size() - 1)
                .forEach(i -> previousSpokenHashMap.put(startingNumbers.get(i), i + 1));

        int turn = previousSpokenHashMap.size() + 2;
        int numberSpoken = startingNumbers.get(startingNumbers.size() - 1);

        while (turn <= nrTurns) {
            Integer previousTurnNumberSpoken = previousSpokenHashMap.get(numberSpoken);
            previousSpokenHashMap.put(numberSpoken, turn - 1);
            if (previousTurnNumberSpoken == null) {
                numberSpoken = 0;
            } else {
                numberSpoken = turn - 1 - previousTurnNumberSpoken;
            }

            turn++;
        }

        return numberSpoken;
    }

    private List<Integer> parseInput(List<String> inputRaw) {
        return inputRaw.stream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(toList());
    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_15().main(filename);
    }
}
