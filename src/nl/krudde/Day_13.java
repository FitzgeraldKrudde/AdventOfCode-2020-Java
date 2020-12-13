package nl.krudde;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Day_13 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        BusPlanner busPlanner = parseInput(inputRaw);

        BusDeparture earliestBusDeparture = busPlanner.busIds.stream()
                .map(busId -> new BusDeparture(busId, calculateWaittime(busPlanner, busId)))
                .min(Comparator.comparingLong(BusDeparture::waittime))
                .orElseThrow(() -> new IllegalStateException("no solution"));

        long result = earliestBusDeparture.waittime * earliestBusDeparture.busId;

        return String.valueOf(result);
    }

    private long calculateWaittime(BusPlanner busPlanner, Long busId) {
        return busPlanner.earliestDepartTime + (busId - (busPlanner.earliestDepartTime % busId)) - busPlanner.earliestDepartTime;
    }

    @Override
    public String doPart2(List<String> inputRaw) {
//        List<Long> input = parseInput(inputRaw);


        long result = 0;

        return String.valueOf(result);
    }

    private BusPlanner parseInput(List<String> inputRaw) {
        long earliestDepart = Long.parseLong(inputRaw.get(0));
        String[] listStringBusId = inputRaw.get(1).split(",");
        List<Long> busIds = Arrays.stream(listStringBusId)
                .filter(s -> !"x".equals(s))
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(toList());

        return new BusPlanner(earliestDepart, busIds);
    }

    // @formatter:off
    record BusPlanner(long earliestDepartTime,List<Long>busIds){}
    record BusDeparture(long busId, long waittime){}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_13().main(filename);
    }
}
