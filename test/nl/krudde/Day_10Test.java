package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_10Test {

    @Test
    void doPart1() throws Exception {
        Day_10 day = new Day_10();

        assertThat(day.doPart1(day.readInput("day_10_01.txt"))).isEqualTo("35");
        assertThat(day.doPart1(day.readInput("day_10_02.txt"))).isEqualTo("220");
    }

    @Test
    void doPart2() throws Exception {
        Day_10 day = new Day_10();

        assertThat(day.doPart2(day.readInput("day_10_01.txt"))).isEqualTo("8");
        assertThat(day.doPart2(day.readInput("day_10_02.txt"))).isEqualTo("19208");
    }

}