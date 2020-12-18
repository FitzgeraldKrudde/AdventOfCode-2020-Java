package nl.krudde;

import java.util.List;

public class Day_18 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        long result = inputRaw.stream()
                .mapToLong(s -> new Expression(s).calculate())
                .sum();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        long result = inputRaw.stream()
                .mapToLong(s -> new Expression(s).calculateAdvanced())
                .sum();

        return String.valueOf(result);
    }

    record Expression(String expression) {
        public long calculate() {
            String cleanedExpression = expression.replaceAll("\\s+", "");
            return calculate(0, '+' + cleanedExpression);
        }

        private long calculate(long currentValue, String s) {
            if (s.length() == 0) {
                return currentValue;
            }

            if (s.startsWith("+(")) {
                int positionMatchingParenthesis = findCloseParentheses(s);
                String subExpression = s.substring(2, positionMatchingParenthesis);
                String remainder = s.substring(positionMatchingParenthesis + 1);
                return calculate(currentValue + calculate(0, "+" + subExpression), remainder);
            }
            if (s.startsWith("*(")) {
                int positionMatchingParenthesis = findCloseParentheses(s);
                String subExpression = s.substring(2, positionMatchingParenthesis);
                String remainder = s.substring(positionMatchingParenthesis + 1);
                return calculate(currentValue * calculate(0, "+" + subExpression), remainder);
            }
            if (s.charAt(0) == '+') {
                String remainder = s.substring(2);
                return calculate(currentValue + Character.getNumericValue(s.charAt(1)), remainder);
            }
            if (s.charAt(0) == '*') {
                String remainder = s.substring(2);
                return calculate(currentValue * Character.getNumericValue(s.charAt(1)), remainder);
            }

            throw new IllegalStateException("unmatched expression: " + s);
        }

        private int findCloseParentheses(String s) {
            int balance = 0;
            for (int j = 0; j < s.length(); j++) {
                switch (s.charAt(j)) {
                    case '(':
                        balance++;
                        break;
                    case ')':
                        if (--balance == 0) {
                            return j;
                        }
                        break;
                }
            }
            throw new IllegalStateException("no matching close parenthesis");
        }

        public long calculateAdvanced() {
            String cleanedExpression = expression.replaceAll("\\s+", "");
            return calculateAdvanced(cleanedExpression);
        }

        private long calculateAdvanced(String s) {
            if (s.length() == 0) {
                return 0;
            }

            // reduce parentheses
            int pos = s.indexOf('(');
            if (pos >= 0) {
                int positionMatchingParenthesis = findCloseParentheses(s.substring(pos));
                String subExpression = s.substring(pos + 1, pos + positionMatchingParenthesis);
                long subExpressionValue = calculateAdvanced(subExpression);
                return calculateAdvanced(s.substring(0, pos) + subExpressionValue + s.substring(pos + positionMatchingParenthesis + 1));
            }

            // reduce addition
            pos = s.indexOf('+');
            if (pos >= 0) {
                // find number before and after
                String numberStringBefore = findSubstringWithNumber(s, pos - 1, false);
                String numberStringAfter = findSubstringWithNumber(s, pos + 1, true);
                long addition = Long.parseLong(numberStringBefore) + Long.parseLong(numberStringAfter);
//                String remainderStringBefore=s.substring(0, pos - numberStringBefore.length() - 1)
                return calculateAdvanced(s.substring(0, pos - numberStringBefore.length()) + addition + s.substring(pos + numberStringAfter.length() + 1));
            }

            // reduce multiplication
            pos = s.indexOf('*');
            if (pos >= 0) {
                // find number before and after
                String numberStringBefore = findSubstringWithNumber(s, pos - 1, false);
                String numberStringAfter = findSubstringWithNumber(s, pos + 1, true);
                long multiply = Long.parseLong(numberStringBefore) * Long.parseLong(numberStringAfter);
                return calculateAdvanced(s.substring(0, pos - numberStringBefore.length()) + multiply + s.substring(pos + numberStringAfter.length() + 1));
            }

            return Long.parseUnsignedLong(s);
        }

        private String findSubstringWithNumber(String s, int pos, boolean forward) {
            StringBuilder sb = new StringBuilder();
            while (pos >= 0 && pos < s.length() && Character.isDigit(s.charAt(pos))) {
                sb.append(s.charAt(pos));
                pos += forward ? 1 : -1;
            }
            return forward ? sb.toString() : sb.reverse().toString();
        }
    }
    // @formatter:off


    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_18().main(filename);
    }
}
