package nl.krudde;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

public class Day_08 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        Program program = new Program(parseInput(inputRaw));

        DeviceV1 device = new DeviceV1(program);
        device.executeProgram();
        long result = device.accumulator;

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Statement> inputProgram = parseInput(inputRaw);

        for (int i = 0; i < inputProgram.size(); i++) {
            if ("acc".equals(inputProgram.get(i).instruction)) {
                continue;
            }

            List<Statement> modifiedProgram = new ArrayList<>(inputProgram);
            Statement statement = modifiedProgram.get(i);
            if ("nop".equals(statement.instruction)) {
                modifiedProgram.set(i, new Statement("jmp", statement.value));
            } else {
                modifiedProgram.set(i, new Statement("nop", statement.value));
            }

            DeviceV1 device = new DeviceV1(new Program(modifiedProgram));
            device.executeProgram();
            if (device.programEndedNormally()) {
                return String.valueOf(device.accumulator);
            }
        }

        throw new RuntimeException("no solution");
    }

    private List<Statement> parseInput(List<String> inputRaw) {
        List<Statement> program = inputRaw.stream()
                .map(line -> {
                    String[] fields = line.split("\\s+");
                    String operation = fields[0];
                    int value = Integer.parseInt(fields[1]);
                    return new Statement(operation, value);
                })
                .collect(toList());

        return program;
    }

    @FunctionalInterface
    interface Operation {
        /**
         * Applies this operation with the given input.
         */
        void apply(long input);
    }

    static class DeviceV1 {
        public final Map<String, Operation> operations = new TreeMap<>();

        private final Program program;
        List<Integer> executedInstructions = new ArrayList<>();

        long accumulator = 0;
        int ip = 0;

        DeviceV1(Program program) {
            this.program = program;

            operations.put("nop", this::nop);
            operations.put("acc", this::acc);
            operations.put("jmp", this::jmp);
        }

        public void nop(long input) {
        }

        public void acc(long input) {
            accumulator += input;
        }

        public void jmp(long input) {
            ip += input;
        }

        public void executeProgram() {
            Statement statement;

            while (!programEndedNormally() && !loopDetected()) {
                statement = program.getStatement(ip);
                executedInstructions.add(ip);
                operations.get(statement.instruction).apply(statement.value);
                if (!"jmp".equals(statement.instruction)) {
                    ip++;
                }
            }
        }

        public boolean programEndedNormally() {
            return ip == program.nrStatements();
        }

        public boolean loopDetected() {
            return executedInstructions.contains(ip);
        }
    }

    record Program(List<Statement> statements) {
        Statement getStatement(int i) {
            return statements.get(i);
        }

        int nrStatements() {
            return statements.size();
        }
    }

    // @formatter:off
    record Statement(String instruction, int value){}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_08().main(filename);
    }
}
