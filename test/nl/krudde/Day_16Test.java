package nl.krudde;

import nl.krudde.Day_16.TicketDocument;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day_16Test {

    @Test
    void doPart1() throws Exception {
        Day_16 day = new Day_16();

        assertThat(day.doPart1(day.readInput("day_16_01.txt"))).isEqualTo("71");
    }

    @Test
    void doPart2() throws Exception {
        Day_16 day = new Day_16();

        List<String> inputRaw = day.readInput("day_16_02.txt");
        TicketDocument ticketDocument = TicketDocument.parseInput(inputRaw);
        ticketDocument = ticketDocument.removeInvalidTickets();

        assertThat(ticketDocument.getMultiplicationMyTicketValues("class")).isEqualTo(12);
        assertThat(ticketDocument.getMultiplicationMyTicketValues("row")).isEqualTo(11);
        assertThat(ticketDocument.getMultiplicationMyTicketValues("seat")).isEqualTo(13);
    }

}