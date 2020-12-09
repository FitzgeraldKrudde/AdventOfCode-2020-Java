package nl.krudde;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Day_09 extends Day {
    private int limit = 25;

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);

        FixedLengthFIFOQueue<Long> fifo = new FixedLengthFIFOQueue<>(limit);
        fifo.addAll(removePreambleFromInput(input));

        long result = input.stream()
                .filter(number -> !addToQueueIfValidNextNumber(fifo, number))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no solution"));

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);
        long invalidNumber = Long.parseLong(doPart1(inputRaw));

        List<Long> contiguousSet = input.stream()
                .flatMap(startingNumber -> findMatchingContiguousSet(input, startingNumber, invalidNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no solution"));

        long result = Collections.min(contiguousSet) + Collections.max(contiguousSet);

        return String.valueOf(result);
    }

    private Stream<List<Long>> findMatchingContiguousSet(List<Long> listLongs, long startingNumber, long invalidNumber) {
        int startPosition = listLongs.indexOf(startingNumber);
        List<Long> subList = listLongs.subList(startPosition, listLongs.size());

        // start at index 2 as the minimum length is 2 elements
        return IntStream.range(2, subList.size())
                .mapToObj(i -> subList.subList(0, i))
                .takeWhile(sublistLongs -> sublistLongs.stream().mapToLong(Long::longValue).sum() <= invalidNumber)
                .filter(sublistLongs -> sublistLongs.stream().mapToLong(Long::longValue).sum() == invalidNumber);
    }

    private List<Long> removePreambleFromInput(List<Long> input) {
        List<Long> preamble = new ArrayList<>();

        while (preamble.size() < limit) {
            preamble.add(input.remove(0));
        }

        return preamble;
    }

    private boolean addToQueueIfValidNextNumber(Queue<Long> fifo, Long number) {
        if (fifo.stream()
                .flatMap(number1 -> fifo.stream()
                        .map(number2 -> new Pair(number1, number2)))
                .filter(pair -> !pair.long1.equals(pair.long2))
                .anyMatch(pair -> pair.long1 + pair.long2 == number)
        ) {
            fifo.add(number);
            return true;
        }

        return false;
    }

    private List<Long> parseInput(List<String> inputRaw) {
        List<Long> input = inputRaw.stream()
                .map(Long::valueOf)
                .collect(toList());

        return input;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    static class FixedLengthFIFOQueue<E> extends LinkedList<E> {
        private final int limit;

        public FixedLengthFIFOQueue(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(E o) {
            super.add(o);
            while (size() > limit) {
                super.remove();
            }
            return true;
        }
    }

    // @formatter:off
    record Pair(Long long1, Long long2) {}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_09().main(filename);
    }
}
