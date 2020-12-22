package nl.krudde;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Day_22 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        CombatGame combatGame = parseInput(inputRaw);
        combatGame.doRounds();

        long result = combatGame.score();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        CombatGame combatGame = parseInput(inputRaw);
        RecursiveCombatGame recursiveCombatGame = new RecursiveCombatGame(combatGame.deckPlayer1, combatGame.deckPlayer2);

        recursiveCombatGame.didPlayer1Win();
        long result = recursiveCombatGame.score();

        return String.valueOf(result);
    }

    private CombatGame parseInput(List<String> inputRaw) {
        List<Long> deckPlayer1 = inputRaw.stream()
                .dropWhile(line -> line.startsWith("Player"))
                .takeWhile(line -> line.length() > 0)
                .map(Long::valueOf)
                .collect(toList());
        List<Long> deckPlayer2 = inputRaw.stream()
                .dropWhile(line -> line.length() > 0)
                .dropWhile(line -> line.length() == 0)
                .dropWhile(line -> line.startsWith("Player"))
                .map(Long::valueOf)
                .collect(toList());

        return new CombatGame(deckPlayer1, deckPlayer2);
    }

    record RoundDecks(List<Long> deckPlayer1, List<Long> deckPlayer2) {
    }

    record RecursiveCombatGame(List<Long> deckPlayer1, List<Long> deckPlayer2) {
        boolean didPlayer1Win() {
            List<RoundDecks> previousRoundDecks = new ArrayList<>();

            while (deckPlayer1.size() > 0 && deckPlayer2.size() > 0) {
                // check if we have the same cards as before, then player 1 wins
                RoundDecks roundDecks = new RoundDecks(new ArrayList<>(deckPlayer1), new ArrayList<>(deckPlayer2));
                if (previousRoundDecks.contains(roundDecks)) {
                    return true;
                } else {
                    previousRoundDecks.add(roundDecks);
                }

                Long cardPlayer1 = deckPlayer1.remove(0);
                Long cardPlayer2 = deckPlayer2.remove(0);

                // start a recursive subgame if both players have at least the number of cards as the card they draw
                if (deckPlayer1.size() >= cardPlayer1 && deckPlayer2.size() >= cardPlayer2) {
                    List<Long> newDeckPlayer1 = new ArrayList<>(deckPlayer1.subList(0, Math.toIntExact(cardPlayer1)));
                    List<Long> newDeckPlayer2 = new ArrayList<>(deckPlayer2.subList(0, Math.toIntExact(cardPlayer2)));
                    if (new RecursiveCombatGame(newDeckPlayer1, newDeckPlayer2).didPlayer1Win()) {
                        deckPlayer1.add(cardPlayer1);
                        deckPlayer1.add(cardPlayer2);
                    } else {
                        deckPlayer2.add(cardPlayer2);
                        deckPlayer2.add(cardPlayer1);
                    }
                } else {
                    // normal round
                    if (cardPlayer1 > cardPlayer2) {
                        deckPlayer1.add(cardPlayer1);
                        deckPlayer1.add(cardPlayer2);
                    } else {
                        deckPlayer2.add(cardPlayer2);
                        deckPlayer2.add(cardPlayer1);
                    }
                }
            }

            return deckPlayer1.size() > 0;
        }

        long score() {
            List<Long> winningDeck = deckPlayer1.size() == 0 ? deckPlayer2 : deckPlayer1;
            return IntStream.rangeClosed(1, winningDeck.size())
                    .mapToLong(i -> i * winningDeck.get(winningDeck.size() - i))
                    .sum();
        }

    }

    record CombatGame(List<Long> deckPlayer1, List<Long> deckPlayer2) {
        void doRounds() {
            while (deckPlayer1.size() > 0 && deckPlayer2.size() > 0) {
                doRound();
            }
        }

        private void doRound() {
            Long cardPlayer1 = deckPlayer1.remove(0);
            Long cardPlayer2 = deckPlayer2.remove(0);
            if (cardPlayer1 > cardPlayer2) {
                deckPlayer1.add(cardPlayer1);
                deckPlayer1.add(cardPlayer2);
            } else {
                deckPlayer2.add(cardPlayer2);
                deckPlayer2.add(cardPlayer1);
            }
        }

        long score() {
            List<Long> winningDeck = deckPlayer1.size() == 0 ? deckPlayer2 : deckPlayer1;
            return IntStream.rangeClosed(1, winningDeck.size())
                    .mapToLong(i -> i * winningDeck.get(winningDeck.size() - i))
                    .sum();
        }

    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_22().main(filename);
    }
}
