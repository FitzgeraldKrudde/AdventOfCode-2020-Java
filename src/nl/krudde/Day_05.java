package nl.krudde;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Day_05 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        long result = inputRaw.stream()
                .map(this::calculateSeatNumber)
                .max(Integer::compare)
                .orElseThrow(() -> new RuntimeException("no solution"));

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<String> rowWith7Seats = inputRaw.stream()
                .collect(groupingBy(this::getRow))
                .entrySet()
                .stream().filter(es -> es.getValue().size() == 7)
                .findAny()
                .orElseThrow(() -> new RuntimeException("no solution"))
                .getValue();

        int row = getRow(rowWith7Seats.get(0));
        int column = IntStream.concat(IntStream.rangeClosed(0, 7), rowWith7Seats.stream().mapToInt(this::getColumn))
                .boxed()
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .findAny()
                .orElseThrow(() -> new RuntimeException("no solution"));

        return String.valueOf(calculateSeatNumber(row, column));
    }

    private int calculateSeatNumber(String s) {
        return calculateSeatNumber(getRow(s), getColumn(s));
    }

    private int calculateSeatNumber(int row, int column) {
        return 8 * row + column;
    }

    private int getColumn(String s) {
        return Integer.parseInt(s.substring(7).replace('L', '0').replace('R', '1'), 2);
    }

    private int getRow(String s) {
        return Integer.parseInt(s.substring(0, 7).replace('F', '0').replace('B', '1'), 2);
    }


    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_05().main(filename);
    }
}
