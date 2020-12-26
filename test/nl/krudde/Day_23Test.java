package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_23Test {

    @Test
    void doPart1() throws Exception {
        Day_23 day = new Day_23();

        assertThat(day.doPart1(day.readInput("day_23_01.txt"))).isEqualTo("67384529");
    }

    @Test
    void doPart2() throws Exception {
        Day_23 day = new Day_23();

        assertThat(day.doPart2(day.readInput("day_23_01.txt"))).isEqualTo("149245887792");
    }

}