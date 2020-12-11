package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_11Test {

    @Test
    void doPart1() throws Exception {
        Day_11 day = new Day_11();

        assertThat(day.doPart1(day.readInput("day_11_01.txt"))).isEqualTo("37");
    }

    @Test
    void doPart2() throws Exception {
        Day_11 day = new Day_11();

        assertThat(day.doPart2(day.readInput("day_11_01.txt"))).isEqualTo("26");
    }

}