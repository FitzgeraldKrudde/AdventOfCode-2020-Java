package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_08Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_08();

        assertThat(day.doPart1(day.readInput("day_08_01.txt"))).isEqualTo("5");
    }

    @Test
    void doPart2() throws Exception {
        Day day = new Day_08();

        assertThat(day.doPart2(day.readInput("day_08_02.txt"))).isEqualTo("8");
    }

}