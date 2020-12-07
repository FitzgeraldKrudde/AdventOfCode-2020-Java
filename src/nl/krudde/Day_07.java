package nl.krudde;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Day_07 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Rule> rules = parseInput(inputRaw);
        LuggageChecker luggageChecker = new LuggageChecker(rules);
        long result = luggageChecker.findNrColorsOutsideBag("shiny gold").size();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Rule> rules = parseInput(inputRaw);
        LuggageChecker luggageChecker = new LuggageChecker(rules);
        long result = luggageChecker.findNrBags("shiny gold", 1) - 1;

        return String.valueOf(result);
    }

    private List<Rule> parseInput(List<String> inputRaw) {
        List<Rule> input = inputRaw.stream()
                .flatMap(line -> readRule(line).stream())
                .collect(toList());

        return input;
    }

    private List<Rule> readRule(String line) {
        List<Rule> rules = new ArrayList<>();

        String[] fields = line.split(" ");
        String colorOutside = fields[0] + " " + fields[1];
        if (!"no".equals(fields[4])) {
            for (int i = 0; i < fields.length / 4 - 1; i++) {
                String colorInside = fields[5 + i * 4] + " " + fields[6 + i * 4];
                int count = Integer.parseInt(fields[4 + i * 4]);
                rules.add(new Rule(colorOutside, colorInside, count));
            }
        }

        return rules;
    }

    record LuggageChecker(List<Rule> rules) {
        List<String> findNrColorsOutsideBag(String color) {
            return rules.stream()
                    .filter(rule -> rule.colorInside.equals(color))
                    .flatMap(rule -> Stream.concat(Stream.of(rule.colorOutside), findNrColorsOutsideBag(rule.colorOutside).stream()))
                    .distinct()
                    .collect(toList());
        }

        private long findNrBags(String color, int nrBagsToFind) {
            return nrBagsToFind + rules.stream()
                    .filter(rule -> rule.colorOutside.equals(color))
                    .mapToLong(rule -> findNrBags(rule.colorInside, nrBagsToFind * rule.number))
                    .sum();
        }
    }

    // @formatter:off
    record Rule(String colorOutside, String colorInside, int number){}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_07().main(filename);
    }
}
