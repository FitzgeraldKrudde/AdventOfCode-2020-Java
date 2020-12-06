package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_06Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_06();

        assertThat(day.doPart1(day.readInput("day_06_01.txt"))).isEqualTo("11");
    }

    @Test
    void doPart2() throws Exception {
        Day day = new Day_06();

        assertThat(day.doPart2(day.readInput("day_06_01.txt"))).isEqualTo("6");
    }

}