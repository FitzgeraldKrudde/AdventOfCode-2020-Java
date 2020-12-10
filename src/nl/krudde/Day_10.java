package nl.krudde;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Day_10 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);
        Collections.sort(input);

        final AtomicLong lastJolts = new AtomicLong(0);
        // calculate diffs between elements
        // this is a bit hard with Streams
        Set<Map.Entry<Long, List<Long>>> entries = input.stream()
                .map(l -> {
                    long diff = l - lastJolts.get();
                    lastJolts.set(l);
                    return diff;
                })
                .collect(groupingBy(l -> l))
                .entrySet();

        long nr1JoltDifferences = entries.stream()
                .filter(entry -> entry.getKey().equals(1L))
                .findAny()
                .orElseThrow(() -> new RuntimeException("no solution"))
                .getValue()
                .size();

        long nr3JoltDifferences = entries.stream()
                .filter(entry -> entry.getKey().equals(3L))
                .findAny()
                .orElseThrow(() -> new RuntimeException("no solution"))
                .getValue()
                .size();

        long result = nr1JoltDifferences * (nr3JoltDifferences + 1);

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);
        Collections.sort(input);

        List<List<Long>> listGroups = determineGroups(input);

        long result = listGroups.stream()
                .filter(longs -> longs.size() >= 3)
                .map(this::calculateNrArrangementsInGroup)
                .reduce(1L, (a, b) -> a * b);

        return String.valueOf(result);
    }

    private List<List<Long>> determineGroups(List<Long> input) {
        List<List<Long>> listGroups = new ArrayList<>();
        List<Long> group = new ArrayList<>();

        group.add(0L);
        listGroups.add(group);

        input.forEach(aLong -> addToGroup(listGroups, aLong));

        return listGroups;
    }

    private long calculateNrArrangementsInGroup(List<Long> longs) {
        long arrangements = (long) Math.pow(2, longs.size() - 2);

        // I spent way too much time here
        // the input was actually way more simple than I anticipated
        // I was working on a general formula for calculating the invalid arrangements like:
        // #invalidArrangements = 2^(max-min-5) + 1
        // but this works for groups size > 5
        // with group size 5 being an exception

        // but the biggest group had size 5 and thus only 1 invalid arrangement...
        if (longs.size() == 5) {
            arrangements--;
        }

        return arrangements;
    }

    private void addToGroup(List<List<Long>> listGroups, Long aLong) {
        // get the last group
        List<Long> group = listGroups.get(listGroups.size() - 1);

        // determine if the long fits in the group or we need a new group
        if (aLong - group.get(group.size() - 1) > 2) {
            group = new ArrayList<>();
            listGroups.add(group);
        }
        group.add(aLong);
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
        new Day_10().main(filename);
    }
}
