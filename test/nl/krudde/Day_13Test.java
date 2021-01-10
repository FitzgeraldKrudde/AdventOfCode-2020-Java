package nl.krudde;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day_13Test {

    @Test
    void doPart1() throws Exception {
        Day_13 day = new Day_13();

        assertThat(day.doPart1(day.readInput("day_13_01.txt"))).isEqualTo("295");
    }

    @Test
    void doPart2() throws Exception {
        Day_13 day = new Day_13();

        List<String> input=Arrays.asList("0","1");

        assertThat(day.doPart2(Arrays.asList("0","17,x,13,19"))).isEqualTo("3417");
        assertThat(day.doPart2(Arrays.asList("0","67,7,59,61"))).isEqualTo("754018");
        assertThat(day.doPart2(Arrays.asList("0","67,x,7,59,61"))).isEqualTo("779210");
        assertThat(day.doPart2(Arrays.asList("0","67,7,x,59,61"))).isEqualTo("1261476");
        assertThat(day.doPart2(Arrays.asList("0","1789,37,47,1889"))).isEqualTo("1202161486");

        assertThat(day.doPart2(day.readInput("day_13_01.txt"))).isEqualTo("1068781");
    }

}