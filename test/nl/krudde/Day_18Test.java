package nl.krudde;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class Day_18Test {

    @Test
    void doPart1() throws Exception {
        Day_18 day = new Day_18();

        assertThat(day.doPart1(Collections.singletonList("1 + 2 * 3 + 4 * 5 + 6"))).isEqualTo("71");
        assertThat(day.doPart1(Collections.singletonList("1 + (2 * 3) + (4 * (5 + 6))"))).isEqualTo("51");
        assertThat(day.doPart1(Collections.singletonList("2 * 3 + (4 * 5)"))).isEqualTo("26");
        assertThat(day.doPart1(Collections.singletonList("5 + (8 * 3 + 9 + 3 * 4 * 3)"))).isEqualTo("437");
        assertThat(day.doPart1(Collections.singletonList("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"))).isEqualTo("12240");
        assertThat(day.doPart1(Collections.singletonList("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))).isEqualTo("13632");
    }

    @Test
    void doPart2() throws Exception {
        Day_18 day = new Day_18();

        assertThat(day.doPart2(Collections.singletonList("1 + 2 * 3 + 4 * 5 + 6"))).isEqualTo("231");
        assertThat(day.doPart2(Collections.singletonList("1 + (2 * 3) + (4 * (5 + 6))"))).isEqualTo("51");
        assertThat(day.doPart2(Collections.singletonList("2 * 3 + (4 * 5)"))).isEqualTo("46");
        assertThat(day.doPart2(Collections.singletonList("5 + (8 * 3 + 9 + 3 * 4 * 3)"))).isEqualTo("1445");
        assertThat(day.doPart2(Collections.singletonList("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"))).isEqualTo("669060");
        assertThat(day.doPart2(Collections.singletonList("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))).isEqualTo("23340");
    }

}