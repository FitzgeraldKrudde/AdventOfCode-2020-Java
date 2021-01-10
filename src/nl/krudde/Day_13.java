package nl.krudde;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day_13 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        BusPlanner busPlanner = parseInput(inputRaw);

        BusDeparture earliestBusDeparture = busPlanner.busSchedules.stream()
                .map(busSchedule -> new BusDeparture(busSchedule.busId, busPlanner.calculateWaittime(busSchedule.busId)))
                .min(Comparator.comparingLong(BusDeparture::waittime))
                .orElseThrow(() -> new IllegalStateException("no solution"));

        long result = earliestBusDeparture.waittime * earliestBusDeparture.busId;

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        BusPlanner busPlanner = parseInput(inputRaw);

        long result = busPlanner.earliestSubsequentDeparture();

        return String.valueOf(result);
    }

    private BusPlanner parseInput(List<String> inputRaw) {
        long earliestDeparture = Long.parseLong(inputRaw.get(0));
        List<BusSchedule> busSchedules = new ArrayList<>();
        int offset = 0;
        for (String s : inputRaw.get(1).split(",")) {
            if (!"x".equals(s)) {
                busSchedules.add(new BusSchedule(Long.parseLong(s), offset));
            }
            offset++;
        }
        return new BusPlanner(earliestDeparture, busSchedules);
    }

    record BusPlanner(long earliestDepartureTime, List<BusSchedule> busSchedules) {
        public long calculateWaittime(Long busId) {
            return earliestDepartureTime + (busId - (earliestDepartureTime % busId)) - earliestDepartureTime;
        }

        public long earliestSubsequentDeparture() {
            long start = 0;
            long stepSize = 0;

            for (BusSchedule busSchedule : busSchedules) {
                if (stepSize == 0) {
                    stepSize = busSchedule.busId;
                } else {
                    long multiply = 1;
                    while ((start + multiply * stepSize + busSchedule.offset) % busSchedule.busId != 0) {
                        multiply++;
                    }
                    // new starting point
                    start += multiply * stepSize;
                    // new stepsize, as all busids are primes we can multiply the current step with the current busid
                    stepSize *= busSchedule.busId;
                }
            }

            return start;
        }
    }

    // @formatter:off
    record BusDeparture(long busId, long waittime){}
    record BusSchedule(long busId, long offset){}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_13().main(filename);
    }
}
