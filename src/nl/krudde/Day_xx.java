package nl.krudde;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        invokeSuperRun();
    }

    /**
     * quite a dirty hack and a lot of lines which enable me to prevent me from changing one letter
     * i.e. now I can copy Day_xx to Day_yy without changing code
     */
    private static void invokeSuperRun() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();
        // get our full classname
        final String fullClassName = clazz.getCanonicalName();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // create instance
        Object day = Class.forName(fullClassName).getDeclaredConstructor().newInstance();
        // find run method
        Method runMethod = day.getClass().getMethod("run", String.class);
        // invoke run method for our instance
        runMethod.invoke(day, filename);
    }
}
