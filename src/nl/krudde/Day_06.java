package nl.krudde;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Day_06 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<GroupAnswers> listGroupAnswers = parseInput(inputRaw);

        long result = listGroupAnswers.stream()
                .map(GroupAnswers::countDistinctAnyoneAnswers)
                .mapToLong(Long::longValue)
                .sum();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<GroupAnswers> listGroupAnswers = parseInput(inputRaw);

        long result = listGroupAnswers.stream()
                .map(GroupAnswers::countDistinctEveryoneAnswers)
                .mapToLong(Long::longValue)
                .sum();

        return String.valueOf(result);
    }

    private List<GroupAnswers> parseInput(List<String> inputRaw) {
        List<GroupAnswers> listGroupAnswers = new ArrayList<>();
        GroupAnswers groupAnswers = new GroupAnswers();

        for (String line : inputRaw) {
            if (line.length() == 0) {
                listGroupAnswers.add(groupAnswers);
                groupAnswers = new GroupAnswers();
            } else {
                groupAnswers.add(line);
            }
        }
        listGroupAnswers.add(groupAnswers);

        return listGroupAnswers;
    }

    static class GroupAnswers {
        List<String> groupAnswers = new ArrayList<>();

        void add(String answer) {
            groupAnswers.add(answer);
        }

        long countDistinctAnyoneAnswers() {
            return groupAnswers.stream()
                    .flatMap(ga -> ga.chars().boxed())
                    .distinct()
                    .count();
        }

        long countDistinctEveryoneAnswers() {
            return groupAnswers.stream()
                    .flatMap(ga -> ga.chars().boxed())
                    .collect(groupingBy(Function.identity(), counting()))
                    .entrySet()
                    .stream()
                    .filter(es -> es.getValue() == groupAnswers.size())
                    .count();
        }
    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_06().main(filename);
    }
}
