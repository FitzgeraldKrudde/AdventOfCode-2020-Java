package nl.krudde;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Day_14 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        SeaPortComputer seaPortComputer = new SeaPortComputer();
        inputRaw.stream()
                .forEach(line -> seaPortComputer.processInput(line));

        return String.valueOf(seaPortComputer.getSumValues());
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        SeaPortComputerV2 seaPortComputerV2 = new SeaPortComputerV2();
        inputRaw.stream()
                .forEach(line -> seaPortComputerV2.processInput(line));

        return String.valueOf(seaPortComputerV2.getSumValues());
    }

    private List<Object> parseInput(List<String> inputRaw) {
        List<Object> input = inputRaw.stream()
                .map(Long::valueOf)
                .collect(toList());

        return input;
    }

    static class SeaPortComputer {
        protected final HashMap<Long, Long> memory = new HashMap<>();
        String mask;

        public void processInput(String line) {
            String[] fields = line.split(" = ");
            if ("mask".equals(fields[0])) {
                mask = fields[1];
            } else {
                long address = Long.parseUnsignedLong(fields[0].substring(4, fields[0].length() - 1));
                long inputValue = Long.parseUnsignedLong(fields[1]);

                // apply the '0' in the mask to the value by using an xor
                String xorPattern = mask.replace('X', '0');
                long xor = Long.parseUnsignedLong(xorPattern, 2);

                // apply the '1' in the mask to the value by using an and
                String andPattern = mask.replace('X', '1');
                long and = Long.parseUnsignedLong(andPattern, 2);

                long computedValue = (xor | inputValue) & and;
                memory.put(address, computedValue);
            }
        }

        long getSumValues() {
            return memory.values().stream()
                    .mapToLong(l -> l)
                    .sum();
        }
    }

    static class SeaPortComputerV2 extends SeaPortComputer{
        @Override
        public void processInput(String line) {
            String[] fields = line.split(" = ");
            if ("mask".equals(fields[0])) {
                mask = fields[1];
            } else {
                String inputAddress = fields[0].substring(4, fields[0].length() - 1);
                long value = Long.parseUnsignedLong(fields[1]);

                // apply the '1' from the mask i.e. force these bits to '1' in the address
                String apply1Pattern = mask.replace('X', '0');
                long address = Long.parseUnsignedLong(inputAddress) | Long.parseUnsignedLong(apply1Pattern, 2);

                // replace the bits in the address with 'X' based on the mask
                StringBuilder sbAddress = new StringBuilder(Long.toBinaryString(address));
                // prefix with enough zeros
                sbAddress.insert(0, "0".repeat(36 - sbAddress.length()));
                int i = mask.indexOf('X');
                while (i>=0) {
                    sbAddress.setCharAt(i, 'X');
                    i = mask.indexOf('X',i+1);
                }

                doFloating(sbAddress.toString(), value);
            }
        }

        void doFloating(String address, long value) {
            if (address.indexOf('X') == -1) {
                memory.put(Long.parseUnsignedLong(address, 2), value);
            } else {
                doFloating(address.replaceFirst("X", "0"), value);
                doFloating(address.replaceFirst("X", "1"), value);
            }
        }
    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_14().main(filename);
    }
}
