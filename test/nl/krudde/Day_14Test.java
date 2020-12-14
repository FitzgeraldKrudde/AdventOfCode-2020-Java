package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_14Test {

    @Test
    void doPart1() throws Exception {
        Day_14 day = new Day_14();

        assertThat(day.doPart1(day.readInput("day_14_01.txt"))).isEqualTo("165");
    }

    @Test
    void doPart2() throws Exception {
        Day_14 day = new Day_14();

        assertThat(day.doPart2(day.readInput("day_14_02.txt"))).isEqualTo("208");
    }

}