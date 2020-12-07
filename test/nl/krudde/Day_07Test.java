package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_07Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_07();

        assertThat(day.doPart1(day.readInput("day_07_01.txt"))).isEqualTo("4");
    }

    @Test
    void doPart2() throws Exception {
        Day day = new Day_07();

        assertThat(day.doPart2(day.readInput("day_07_01.txt"))).isEqualTo("32");
        assertThat(day.doPart2(day.readInput("day_07_02.txt"))).isEqualTo("126");
    }

}