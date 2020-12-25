package nl.krudde;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Day_25 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);

        long cardPublicKey = input.get(0);
        long doorPublicKey = input.get(1);

        long cardLoop = 0;
        long value = 1;
        while (cardPublicKey != value) {
            value = (value * 7) % 20201227;
            cardLoop++;
        }

        value = 1;
        for (int i = 0; i < cardLoop; i++) {
            value = (value * doorPublicKey) % 20201227;
        }
        
        long result = value;
        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);


        long result = 0;

        return String.valueOf(result);
    }

    private List<Long> parseInput(List<String> inputRaw) {
        return inputRaw.stream()
                .map(Long::valueOf)
                .collect(toList());
    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_25().main(filename);
    }
}
