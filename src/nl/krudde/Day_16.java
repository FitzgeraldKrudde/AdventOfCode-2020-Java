package nl.krudde;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Day_16 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        TicketDocument ticketDocument = TicketDocument.parseInput(inputRaw);

        long result = ticketDocument.nearbyTickets.stream()
                .flatMap(ticket -> ticket.values.stream())
                .filter(ticketDocument::isInvalidValue)
                .mapToLong(l -> l)
                .sum();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        TicketDocument ticketDocument = TicketDocument.parseInput(inputRaw);

        final TicketDocument cleanedTicketDocument = ticketDocument.removeInvalidTickets();
        long result = cleanedTicketDocument.getMultiplicationMyTicketValues("departure");

        return String.valueOf(result);
    }

    record TicketDocument(List<Rule> rules, Ticket myTicket, List<Ticket> nearbyTickets) {
        public static TicketDocument parseInput(List<String> inputRaw) {
            List<Rule> rules = new ArrayList<>();
            Ticket myTicket;
            List<Ticket> nearbyTickets = new ArrayList<>();
            int i = 0;
            String line = inputRaw.get(i);

            // read rules
            while (line.length() > 0) {
                rules.add(parseRule(line));
                line = inputRaw.get(++i);
            }

            // read my ticket
            i += 2;
            line = inputRaw.get(i);
            myTicket = parseTicket(line);

            // read nearby tickets
            i += 3;
            while (i < inputRaw.size() && line.length() > 0) {
                line = inputRaw.get(i++);
                nearbyTickets.add(parseTicket(line));
            }

            return new TicketDocument(rules, myTicket, nearbyTickets);
        }

        private static Ticket parseTicket(String line) {
            return new Ticket(Arrays.stream(line.split(","))
                    .map(Long::parseLong)
                    .collect(toList()));
        }

        private static Rule parseRule(String line) {
            String[] fields = line.split(": | or |-");
            return new Rule(fields[0], Arrays.asList(
                    new FieldRestriction(Integer.parseInt(fields[1]), Integer.parseInt(fields[2])),
                    new FieldRestriction(Integer.parseInt(fields[3]), Integer.parseInt(fields[4]))
            ));
        }

        public boolean isInvalidValue(Long l) {
            return rules.stream()
                    .flatMap(rule -> rule.restrictions.stream())
                    .noneMatch(restriction -> l >= restriction.min && l <= restriction.max);
        }

        public long getMultiplicationMyTicketValues(String fieldnameStart) {
            Map<Rule, Integer> rulePositionMap = determineRulePositions();

            return rulePositionMap.entrySet().stream()
                    .filter(es -> es.getKey().field.startsWith(fieldnameStart))
                    .map(Map.Entry::getValue)
                    .map(myTicket.values::get)
                    .reduce(1L, (l1, l2) -> l1 * l2);
        }

        public Map<Rule, Integer> determineRulePositions() {
            // solution
            Map<Rule, Integer> rulePositions = new HashMap<>();

            // possible places for a rule
            Map<Rule, List<Integer>> possibleRulePositions = rules.stream()
                    .collect(toMap(rule -> rule, this::findPossiblePositions));

            // go through the possible positions: find a rule with the lowest possibilities (1)
            while (possibleRulePositions.size() > 0) {
                Map.Entry<Rule, List<Integer>> ruleWithOnePossiblePosition = possibleRulePositions.entrySet().stream()
                        .min(Comparator.comparingInt(entry -> entry.getValue().size()))
                        .orElseThrow(() -> new RuntimeException("no solution"));
                // add the found position
                rulePositions.put(ruleWithOnePossiblePosition.getKey(), ruleWithOnePossiblePosition.getValue().get(0));
                // removed the found rule and the position it has taken
                possibleRulePositions.remove(ruleWithOnePossiblePosition.getKey());
                possibleRulePositions.forEach((key, value) -> value.remove(ruleWithOnePossiblePosition.getValue().get(0)));
            }
            return rulePositions;
        }

        public List<Integer> findPossiblePositions(Rule rule) {
            return IntStream.range(0, myTicket.values.size())
                    .filter(position -> isRuleValidForPosition(rule, position))
                    .boxed()
                    .collect(toList());
        }

        private boolean isRuleValidForPosition(Rule rule, int position) {
            return nearbyTickets.stream()
                    .allMatch(ticket -> isRuleValidForTicketField(ticket, rule, position));
        }

        private boolean isRuleValidForTicketField(Ticket ticket, Rule rule, int field) {
            return rule.restrictions.stream()
                    .anyMatch(restriction -> ticket.values.get(field) >= restriction.min && ticket.values.get(field) <= restriction.max);
        }

        public TicketDocument removeInvalidTickets() {
            return new TicketDocument(rules, myTicket, nearbyTickets.stream()
                    .filter(this::validTicket)
                    .collect(toList()));
        }

        public boolean validTicket(Ticket ticket) {
            return ticket.values.stream()
                    .noneMatch(this::isInvalidValue);
        }
    }

    // @formatter:off
    record Ticket(List<Long>values){}
    record Rule(String field, List<FieldRestriction> restrictions) { }
    record FieldRestriction(int min, int max) { }

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_16().main(filename);
    }
}
