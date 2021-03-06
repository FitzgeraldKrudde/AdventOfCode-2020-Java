package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_02Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_02();

        assertThat(day.doPart1(day.readInput("day_02.txt")).equals("514579"));
    }

    @Test
    void doPart2() throws Exception {
        Day day = new Day_02();

        assertThat(day.doPart1(day.readInput("day_02.txt")).equals("278783190"));
    }
}