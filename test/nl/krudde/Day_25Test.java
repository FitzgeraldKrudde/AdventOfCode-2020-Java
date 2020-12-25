package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_25Test {

    @Test
    void doPart1() throws Exception {
        Day_25 day = new Day_25();

        assertThat(day.doPart1(day.readInput("day_25_01.txt"))).isEqualTo("14897079");
    }

    @Test
    void doPart2() throws Exception {
        Day_25 day = new Day_25();

        assertThat(day.doPart2(day.readInput("day_25_01.txt"))).isEqualTo("0");
    }

}