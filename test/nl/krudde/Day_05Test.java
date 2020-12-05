package nl.krudde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_05Test {

    @Test
    void doPart1() throws Exception {
        Day day = new Day_05();

        assertThat(day.doPart1(day.readInput("day_05_01.txt"))).isEqualTo("357");
        assertThat(day.doPart1(day.readInput("day_05_02.txt"))).isEqualTo("567");
        assertThat(day.doPart1(day.readInput("day_05_03.txt"))).isEqualTo("119");
        assertThat(day.doPart1(day.readInput("day_05_04.txt"))).isEqualTo("820");
    }

}