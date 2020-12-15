package nl.krudde;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class Day_15Test {

    @Test
    void doPart1() throws Exception {
        Day_15 day = new Day_15();

        assertThat(day.doPart1(Arrays.asList("0", "3", "6"))).isEqualTo("436");
        assertThat(day.doPart1(Arrays.asList("1", "3", "2"))).isEqualTo("1");
        assertThat(day.doPart1(Arrays.asList("2", "1", "3"))).isEqualTo("10");
        assertThat(day.doPart1(Arrays.asList("1", "2", "3"))).isEqualTo("27");
        assertThat(day.doPart1(Arrays.asList("2", "3", "1"))).isEqualTo("78");
        assertThat(day.doPart1(Arrays.asList("3", "2", "1"))).isEqualTo("438");
        assertThat(day.doPart1(Arrays.asList("3", "1", "2"))).isEqualTo("1836");
    }

    @Test
    void doPart2() throws Exception {
        Day_15 day = new Day_15();

        assertThat(day.doPart2(Arrays.asList("0", "3", "6"))).isEqualTo("175594");
        assertThat(day.doPart2(Arrays.asList("1", "3", "2"))).isEqualTo("2578");
        assertThat(day.doPart2(Arrays.asList("2", "1", "3"))).isEqualTo("3544142");
        assertThat(day.doPart2(Arrays.asList("1", "2", "3"))).isEqualTo("261214");
        assertThat(day.doPart2(Arrays.asList("2", "3", "1"))).isEqualTo("6895259");
        assertThat(day.doPart2(Arrays.asList("3", "2", "1"))).isEqualTo("18");
        assertThat(day.doPart2(Arrays.asList("3", "1", "2"))).isEqualTo("362");
    }

}