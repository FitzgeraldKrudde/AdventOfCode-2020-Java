package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_24Test {

    @Test
    void doPart1() throws Exception {
        Day_24 day = new Day_24();

        assertThat(day.doPart1(day.readInput("day_24_01.txt"))).isEqualTo("10");
    }

    @Test
    void doPart2() throws Exception {
        Day_24 day = new Day_24();

        assertThat(day.doPart2(day.readInput("day_24_01.txt"))).isEqualTo("2208");
    }

}