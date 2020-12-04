package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_03Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_03();
        String result = day.doPart1(day.readInput("day_03.txt"));

        assertThat(result).isEqualTo("7");
    }

    @Test
    void doPart2() throws Exception {
        Day day = new Day_03();
        String result = day.doPart2(day.readInput("day_03.txt"));

        assertThat(result).isEqualTo("336");
    }
}