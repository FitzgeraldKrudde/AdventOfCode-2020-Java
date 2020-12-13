package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_13Test {

    @Test
    void doPart1() throws Exception {
        Day_13 day = new Day_13();

        assertThat(day.doPart1(day.readInput("day_13_01.txt"))).isEqualTo("295");
    }

    @Test
    void doPart2() throws Exception {
        Day_13 day = new Day_13();

        assertThat(day.doPart2(day.readInput("day_13_01.txt"))).isEqualTo("1068788");
    }

}