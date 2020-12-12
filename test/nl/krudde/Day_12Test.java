package nl.krudde;

import nl.krudde.Day_12.Direction;
import org.junit.jupiter.api.Test;

import static nl.krudde.Day_12.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;

class Day_12Test {

    @Test
    void doPart1() throws Exception {
        Day_12 day = new Day_12();

        assertThat(day.doPart1(day.readInput("day_12_01.txt"))).isEqualTo("25");
    }

    @Test
    void turn() {
        Direction direction;

        direction= of('E');
        assertThat(direction.turn('L', 90)).isEqualTo(N);
        assertThat(direction.turn('L', 180)).isEqualTo(W);
        assertThat(direction.turn('L', 270)).isEqualTo(S);
        assertThat(direction.turn('R', 90)).isEqualTo(S);
        assertThat(direction.turn('R', 180)).isEqualTo(W);
        assertThat(direction.turn('R', 270)).isEqualTo(N);

        direction= of( 'N');
        assertThat(direction.turn('L', 90)).isEqualTo(W);
        assertThat(direction.turn('L', 180)).isEqualTo(S);
        assertThat(direction.turn('L', 270)).isEqualTo(E);
        assertThat(direction.turn('R', 90)).isEqualTo(E);
        assertThat(direction.turn('R', 180)).isEqualTo(S);
        assertThat(direction.turn('R', 270)).isEqualTo(W);

        direction= of( 'S');
        assertThat(direction.turn('L', 90)).isEqualTo(E);
        assertThat(direction.turn('L', 180)).isEqualTo(N);
        assertThat(direction.turn('L', 270)).isEqualTo(W);
        assertThat(direction.turn('R', 90)).isEqualTo(W);
        assertThat(direction.turn('R', 180)).isEqualTo(N);
        assertThat(direction.turn('R', 270)).isEqualTo(E);

        direction= of('W');
        assertThat(direction.turn('L', 90)).isEqualTo(S);
        assertThat(direction.turn('L', 180)).isEqualTo(E);
        assertThat(direction.turn('L', 270)).isEqualTo(N);
        assertThat(direction.turn('R', 90)).isEqualTo(N);
        assertThat(direction.turn('R', 180)).isEqualTo(E);
        assertThat(direction.turn('R', 270)).isEqualTo(S);
    }

    @Test
    void doPart2() throws Exception {
        Day_12 day = new Day_12();

        assertThat(day.doPart2(day.readInput("day_12_01.txt"))).isEqualTo("286");
    }

}