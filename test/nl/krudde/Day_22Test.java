package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_22Test {

    @Test
    void doPart1() throws Exception {
        Day_22 day = new Day_22();

        assertThat(day.doPart1(day.readInput("day_22_01.txt"))).isEqualTo("306");
    }

    @Test
    void doPart2() throws Exception {
        Day_22 day = new Day_22();

        assertThat(day.doPart2(day.readInput("day_22_01.txt"))).isEqualTo("291");
    }

}