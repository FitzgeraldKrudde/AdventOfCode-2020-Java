package nl.krudde;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Day_xx extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
//        List<Long> input = parseInput(inputRaw);


        long result = 0;

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
//        List<Long> input = parseInput(inputRaw);


        long result = 0;

        return String.valueOf(result);
    }

    private List<Object> parseInput(List<String> inputRaw) {
        List<Object> input = inputRaw.stream()
                .map(Long::valueOf)
                .collect(toList());

        return input;
    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_xx().main(filename);
    }
}
