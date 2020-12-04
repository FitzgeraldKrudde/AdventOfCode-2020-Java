package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_04Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_04();
        String result = day.doPart1(day.readInput("day_04_01.txt"));

        assertThat(result).isEqualTo("2");
    }

    @Test
    void doPart2Invalid() throws Exception {
        Day day = new Day_04();
        String result = day.doPart2(day.readInput("day_04_02_invalid.txt"));

        assertThat(result).isEqualTo("0");
    }

    @Test
    void doPart2Valid() throws Exception {
        Day day = new Day_04();
        String result = day.doPart2(day.readInput("day_04_02_valid.txt"));

        assertThat(result).isEqualTo("4");
    }
}