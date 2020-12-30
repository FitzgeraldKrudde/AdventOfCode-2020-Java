package nl.krudde;

import nl.krudde.Day_20.Position;
import nl.krudde.Day_20.Tile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day_20Test {

    @Test
    void testNoughbour() {
        Position position = new Position(1, 1);
        assertThat(position.isNeighbour(new Position(0,0))).isFalse();
        assertThat(position.isNeighbour(new Position(0,1))).isTrue();
        assertThat(position.isNeighbour(new Position(1,0))).isTrue();
    }

    @Test
    void doPart1() throws Exception {
        Day_20 day = new Day_20();

        List<Tile> tiles = day.parseInput(day.readInput("day_20_01.txt"));
        Tile tile = tiles.stream()
                .filter(t -> t.id() == 2311)
                .findAny()
                .get();
        tile.print();
        System.out.println("tile.orientationEdges().get(Day_20.Orientation.R0_FLIPV) = " + tile.orientationEdges().get(Day_20.Orientation.R0_FLIPV));


        System.out.println();

        tiles = day.parseInput(day.readInput("day_20_01.txt"));
        tile = tiles.stream()
                .filter(t -> t.id() == 1951)
                .findAny()
                .get();
        tile.print();
        System.out.println("tile.orientationEdges().get(Day_20.Orientation.R0_FLIPV) = " + tile.orientationEdges().get(Day_20.Orientation.R0_FLIPV));

        assertThat(day.doPart1(day.readInput("day_20_01.txt"))).isEqualTo("20899048083289");
    }

    @Test
    void doPart2() throws Exception {
        Day_20 day = new Day_20();

        assertThat(day.doPart2(day.readInput("day_20_01.txt"))).isEqualTo("0");
    }

}