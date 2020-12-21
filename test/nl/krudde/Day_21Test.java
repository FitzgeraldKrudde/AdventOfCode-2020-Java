package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_21Test {

    @Test
    void doPart1() throws Exception {
        Day_21 day = new Day_21();

        assertThat(day.doPart1(day.readInput("day_21_01.txt"))).isEqualTo("5");
    }

    @Test
    void doPart2() throws Exception {
        Day_21 day = new Day_21();

        assertThat(day.doPart2(day.readInput("day_21_01.txt"))).isEqualTo("mxmxvkd,sqjhc,fvjkl");
    }

}