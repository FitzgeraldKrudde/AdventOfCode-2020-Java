package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_01Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_01();

        assertThat(day.doPart1(day.readInput("day_01.txt"))).isEqualTo("514579");
    }

    @Test
    void doPart2() throws Exception {
        Day day = new Day_01();

        assertThat(day.doPart2(day.readInput("day_01.txt"))).isEqualTo("241861950");
    }
}