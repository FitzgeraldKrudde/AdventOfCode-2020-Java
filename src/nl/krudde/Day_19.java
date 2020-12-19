package nl.krudde;

import lombok.Setter;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class Day_19 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        MessageParser messageParser = MessageParser.of(inputRaw);

        long result = messageParser.countValidMessages();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        MessageParser messageParser = MessageParser.of(inputRaw);

        // for unittests, not real nice ;)
        if (inputRaw.get(0).length() % 5 == 0) {
            messageParser.setNrDigitsForShortcutMatches(5);
        }

        // find the matches for the rules 31 and 42
        messageParser.findMatchesForShortcutRules();

        long result = messageParser.countValidMessagesWithLoopingRules();

        return String.valueOf(result);
    }

    static class MessageParser {
        Map<Integer, Rule> rules;
        List<String> messages;

        // for part 2
        List<String> matchesRule31 = new ArrayList<>();
        List<String> matchesRule42 = new ArrayList<>();
        @Setter
        int nrDigitsForShortcutMatches = 8;

        public MessageParser(HashMap<Integer, Rule> rules, List<String> messages) {
            this.rules = rules;
            this.messages = messages;
        }

        static MessageParser of(List<String> input) {
            // read rules
            HashMap<Integer, Rule> rules = new HashMap<>();
            int i = 0;
            String line = input.get(i);
            while (line.length() > 0) {
                String[] fields = line.split(": ");
                int id = Integer.parseInt(fields[0]);
                // check if we have a matching character or a list of subRules
                if (fields[1].indexOf('"') != -1) {
                    rules.put(id, new Rule(new ArrayList<>(), fields[1].charAt(1)));
                } else {
                    String[] subRules = fields[1].split(" \\| ");
                    Rule rule = new Rule(new ArrayList<>(), ' ');
                    for (String ruleString : subRules) {
                        SubRule subRule = new SubRule(Arrays.stream(ruleString.split("\\s+")).map(Integer::parseInt).collect(toList()));
                        rule.subRules.add(subRule);
                    }
                    rules.put(id, rule);
                }
                line = input.get(++i);
            }

            // read messages
            List<String> messages = new ArrayList<>(input.subList(++i, input.size()));
            return new MessageParser(rules, messages);
        }

        public long countValidMessages() {
            return messages.stream()
                    .filter(this::findMatchingPartForRule)
                    .count();
        }

        private boolean findMatchingPartForRule(String message) {
            return message.equals(findMatchingPartForRule(message, 0));
        }

        private String findMatchingPartForRule(String message, int ruleId) {
            if (message.length() == 0) {
                return null;
            }

            Rule rule = rules.get(ruleId);
            switch (rule.subRules.size()) {
                case 0:
                    // no rules, check the if the first character in the message is the single rule character
                    if (message.charAt(0) == rule.matchChar) {
                        return String.valueOf(message.charAt(0));
                    } else {
                        return null;
                    }
                case 1:
                    // apply the rules (in this only subRule) in sequence, stripping the matching part
                    return findMatchingPartForRules(message, rule.subRules.get(0).ruleIds);
                default:
                    // see if any subRule matches
                    Optional<String> matchingPart = rule.subRules.stream()
                            .map(subRule -> findMatchingPartForRules(message, subRule.ruleIds))
                            .filter(Objects::nonNull)
                            .findAny();
                    return matchingPart.orElse(null);
            }
        }

        public List<String> findMatchesForRule(int ruleId) {
            List<String> matches = new ArrayList<>();

            for (int i = 0; i < Math.pow(2, nrDigitsForShortcutMatches); i++) {
                StringBuilder sb = new StringBuilder(Integer.toBinaryString(i));
                sb.insert(0, "0".repeat(nrDigitsForShortcutMatches - sb.length()));
                String s = sb.toString().replace('0', 'a').replace('1', 'b');
                boolean match = s.equals(findMatchingPartForRule(s, ruleId));
                if (match) {
                    matches.add(s);
                }
            }

            return matches;
        }

        private String findMatchingPartForRules(final String message, List<Integer> rules) {
            String matchingPart;
            String remainingPart = message;
            StringBuilder alreadyMatchedPart = new StringBuilder();
            for (int ruleId : rules) {
                matchingPart = findMatchingPartForRule(remainingPart, ruleId);
                if (matchingPart == null) {
                    return null;
                } else {
                    alreadyMatchedPart.append(matchingPart);
                    remainingPart = remainingPart.substring(matchingPart.length());
                }
            }
            return alreadyMatchedPart.toString();
        }

        public void findMatchesForShortcutRules() {
            matchesRule42 = findMatchesForRule(42);
            matchesRule31 = findMatchesForRule(31);
        }

        public long countValidMessagesWithLoopingRules() {
            return messages.stream()
                    .filter(this::isMessageValidWithLoopingRules)
                    .count();
        }

        private boolean isMessageValidWithLoopingRules(String message) {
            int matches31Length = matchesRule31.get(0).length();
            int matches42Length = matchesRule42.get(0).length();

            // check minimum length i.e. (2 times length rule-42 matches + length rule-31 matches)
            if (message.length() < (2 * matches42Length + matches31Length)) {
                return false;
            }

            // message must consist >2 rule-42 matches followed by >=1 rule-31 matches
            // also the number of rule-42 matches must be greater than the number of rule-31 matches

            // strip rule-42 matches from the start
            int nr42Matches = 0;
            while (message.length() > 0 && matchesRule42.contains(message.substring(0, matches42Length))) {
                message = message.substring(matches42Length);
                nr42Matches++;
            }
            if (nr42Matches < 2 || message.length() == 0) {
                return false;
            }

            // strip rule-31 matches
            int nr31Matches = 0;
            while (message.length() > 0 && matchesRule31.contains(message.substring(0, matches31Length))) {
                message = message.substring(matches31Length);
                nr31Matches++;
            }
            return message.length() == 0 && nr31Matches > 0 && (nr42Matches > nr31Matches);
        }
    }

    // @formatter:off
    record Rule(List<SubRule> subRules, char matchChar) { }
    record SubRule(List<Integer> ruleIds) { }

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_19().main(filename);
    }
}
