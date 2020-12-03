package nl.krudde;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

public class Day_01 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);

        Pair pairResult = input.stream()
                .flatMap(long1 -> input.stream()
                        .map(long2 -> new Pair(long1, long2))
                )
                .filter(pair -> pair.long1 + pair.long2 == 2020)
                .findAny()
                .orElseThrow(() -> new RuntimeException("no solution .. :-("));
        long result = pairResult.long1 * pairResult.long2;
        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Long> input = parseInput(inputRaw);

        AtomicInteger multiplications = new AtomicInteger();
        Triple tripleResult = input.stream()
                .flatMap(l1 -> input.stream()
                        .map(l2 -> new Pair(l1, l2))
                )
                .filter(pair -> pair.long1 + pair.long2 < 2020) // optimization
                .flatMap(pair -> input.stream()
                        .map(l2 -> new Triple(pair.long1, pair.long2, l2)))
                .filter(triple -> {
                    multiplications.incrementAndGet();
                    return true;
                })
                .filter(triple -> triple.long1 + triple.long2 + triple.long3 == 2020)
                .findAny()
                .orElseThrow(() -> new RuntimeException("no solution .. :-("));
        System.out.println("#multiplications = " + multiplications);
        long result = tripleResult.long1 * tripleResult.long2 * tripleResult.long3;

        return String.valueOf(result);
    }

    private List<Long> parseInput(List<String> inputRaw) {
        return inputRaw.stream()
                .map(Long::valueOf)
                .collect(toList());
    }

    // @formatter:off
    record Pair(long long1, long long2) {}
    record Triple(long long1, long long2, long long3) {}



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
