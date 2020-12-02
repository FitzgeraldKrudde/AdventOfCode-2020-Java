package nl.krudde;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Day_02 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<PasswordEntry> listPasswordEntries = parseInput(inputRaw);

        long result = listPasswordEntries.stream()
                .filter(PasswordEntry::isValid)
                .count();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<PasswordEntry> listPasswordEntries = parseInput(inputRaw);

        long result = listPasswordEntries.stream()
                .filter(PasswordEntry::isValidCorp)
                .count();

        return String.valueOf(result);
    }

    private List<PasswordEntry> parseInput(List<String> input) {
        return input.stream()
                .map(line -> {
                    String[] words = line.split(" ");
                    String[] policy = words[0].split("-");
                    int min = Integer.parseInt(policy[0]);
                    int max = Integer.parseInt(policy[1]);
                    char letter = words[1].charAt(0);
                    String password = words[2];
                    return new PasswordEntry(min, max, letter, password);
                })
                .collect(toList());
    }

    record PasswordEntry(int min, int max, char letter, String password) {
        public boolean isValid() {
            long occurrences = password.chars()
                    .filter(c -> c == letter)
                    .count();

            return occurrences >= min && occurrences <= max;
        }

        public boolean isValidCorp() {
            return password.charAt(min - 1) == letter ^ password.charAt(max - 1) == letter;
        }
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
