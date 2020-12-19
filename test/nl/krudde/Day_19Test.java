package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_19Test {

    @Test
    void doPart1() throws Exception {
        Day_19 day = new Day_19();

        assertThat(day.doPart1(day.readInput("day_19_01.txt"))).isEqualTo("2");
        assertThat(day.doPart1(day.readInput("day_19_02.txt"))).isEqualTo("3");
    }

    @Test
    void doPart2() throws Exception {
        Day_19 day = new Day_19();

        assertThat(day.doPart2(day.readInput("day_19_03.txt"))).isEqualTo("1");
        assertThat(day.doPart2(day.readInput("day_19_02.txt"))).isEqualTo("12");
    }

}