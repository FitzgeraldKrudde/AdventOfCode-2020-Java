package nl.krudde;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Day_03 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        Map map = new Map(inputRaw);
        return String.valueOf(map.countTrees(new Slope(3, 1)));
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        Map map = new Map(inputRaw);

        return String.valueOf(map.countTrees(new Slope(1, 1))
                * map.countTrees(new Slope(3, 1))
                * map.countTrees(new Slope(5, 1))
                * map.countTrees(new Slope(7, 1))
                * map.countTrees(new Slope(1, 2))
        );
    }

    record Map(List<String> map) {
        private final static char TREE = '#';

        long countTrees(Slope slope) {
            int trees = 0;
            int x = 0, y = 0;

            while (y < map.size()) {
                if (map.get(y).charAt(x) == TREE) {
                    trees++;
                }

                x = (x + slope.x) % map.get(0).length();
                y += slope.y;
            }

            return trees;
        }
    }

    // @formatter:off
    record Slope(int x, int y) { }


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
