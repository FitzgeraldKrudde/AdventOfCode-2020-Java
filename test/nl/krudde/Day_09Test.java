package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_09Test {

    @Test
    void doPart1() throws Exception {
        Day_09 day = new Day_09();
        day.setLimit(5);

        assertThat(day.doPart1(day.readInput("day_09_01.txt"))).isEqualTo("127");
    }

    @Test
    void doPart2() throws Exception {
        Day_09 day = new Day_09();
        day.setLimit(5);

        assertThat(day.doPart2(day.readInput("day_09_01.txt"))).isEqualTo("62");
    }

}