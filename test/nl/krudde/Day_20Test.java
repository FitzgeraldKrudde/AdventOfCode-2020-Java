package nl.krudde;

import nl.krudde.Day_20.Edge;
import nl.krudde.Day_20.Orientation;
import nl.krudde.Day_20.Position;
import nl.krudde.Day_20.Tile;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nl.krudde.Day_20.Edge.*;
import static nl.krudde.Day_20.Orientation.*;
import static org.assertj.core.api.Assertions.assertThat;

class Day_20Test {

    @Test
    void testRotate() {
        List<String> list = Arrays.asList("123", "456", "789");

        list = new Day_20().rotate90Clockwise(list);

        assertThat(list.get(0)).isEqualTo("741");
        assertThat(list.get(1)).isEqualTo("852");
        assertThat(list.get(2)).isEqualTo("963");
    }

    @Test
    void testFlipHorizontal() {
        List<String> list = Arrays.asList("123", "456", "789");

        list = new Day_20().flipHorizontal(list);

        assertThat(list.get(0)).isEqualTo("321");
        assertThat(list.get(1)).isEqualTo("654");
        assertThat(list.get(2)).isEqualTo("987");
    }

    @Test
    void testFlipVertical() {
        List<String> list = Arrays.asList("123", "456", "789");

        list = new Day_20().flipVertical(list);

        assertThat(list.get(0)).isEqualTo("789");
        assertThat(list.get(1)).isEqualTo("456");
        assertThat(list.get(2)).isEqualTo("123");
    }

    @Test
    void testNeighbour() {
        Position position = new Position(1, 1);
        assertThat(position.isNeighbour(new Position(0, 0))).isFalse();
        assertThat(position.isNeighbour(new Position(0, 1))).isTrue();
        assertThat(position.isNeighbour(new Position(1, 0))).isTrue();
    }

    @Test
    void testEdges() throws Exception {
        Day_20 day = new Day_20();
        List<Tile> tiles = day.parseInput(day.readInput("day_20_01.txt"));

        Map<Edge, Integer> tile1951 = getTile(tiles, 1951, R0_FLIPV);
        Map<Edge, Integer> tile2311 = getTile(tiles, 2311, R0_FLIPV);
        Map<Edge, Integer> tile3079 = getTile(tiles, 3079, R0_NOFLIP);

        Map<Edge, Integer> tile2729 = getTile(tiles, 2729, R0_FLIPV);
        Map<Edge, Integer> tile1427 = getTile(tiles, 1427, R0_FLIPV);
        Map<Edge, Integer> tile2473 = getTile(tiles, 2473, R90_FLIPV);

        Map<Edge, Integer> tile2971 = getTile(tiles, 2971, R0_FLIPV);
        Map<Edge, Integer> tile1489 = getTile(tiles, 1489, R0_FLIPV);
        Map<Edge, Integer> tile1171 = getTile(tiles, 1171, R0_FLIPH);


        // horizontal checks
        assertThat(tile1951.get(RIGHT)).isEqualTo(tile2311.get(LEFT));
        assertThat(tile2311.get(RIGHT)).isEqualTo(tile3079.get(LEFT));

        assertThat(tile2729.get(RIGHT)).isEqualTo(tile1427.get(LEFT));
        assertThat(tile1427.get(RIGHT)).isEqualTo(tile2473.get(LEFT));

        assertThat(tile2971.get(RIGHT)).isEqualTo(tile1489.get(LEFT));
        assertThat(tile1489.get(RIGHT)).isEqualTo(tile1171.get(LEFT));

        // vertical checks
        assertThat(tile1951.get(BOTTOM)).isEqualTo(tile2729.get(TOP));
        assertThat(tile2729.get(BOTTOM)).isEqualTo(tile2971.get(TOP));

        assertThat(tile2311.get(BOTTOM)).isEqualTo(tile1427.get(TOP));
        assertThat(tile1427.get(BOTTOM)).isEqualTo(tile1489.get(TOP));

        assertThat(tile3079.get(BOTTOM)).isEqualTo(tile2473.get(TOP));
        assertThat(tile2473.get(BOTTOM)).isEqualTo(tile1171.get(TOP));

    }

    private Map<Edge, Integer> getTile(List<Tile> tiles, int id, Orientation orientation) {
        return tiles.stream().filter(t -> t.id() == id).findAny().get().tileVariations().get(orientation).edges();
    }

    @Test
    void testSeaMonsterDetection() {
        Day_20 day = new Day_20();

        List<String> image = Arrays.asList(
                ".......................",
                "....................#..",
                "..#....##....##....###.",
                "...#..#..#..#..#..#....",
                ".......................");
        int monsters = day.findNrOfSeaMonsters(image);

        assertThat(monsters).isEqualTo(1);
    }

    @Test
    void testSeaMonsterDetectionFartherRight() {
        Day_20 day = new Day_20();

        List<String> image = Arrays.asList(
                "...............................................................#..",
                ".............................................#....##....##....###.",
                "..............................................#..#..#..#..#..#....");
        int monsters = day.findNrOfSeaMonsters(image);

        assertThat(monsters).isEqualTo(1);
    }

    @Test
    void testSeaMonsterDetectionWithExtraHashtags() {
        Day_20 day = new Day_20();

        List<String> image = Arrays.asList(
                "......................",
                "...................#..",
                ".#....##.##.##....###.",
                "..#..#..#..#..#..#....",
                "......................");
        int monsters = day.findNrOfSeaMonsters(image);

        assertThat(monsters).isEqualTo(1);
    }

    @Test
    void testSeaMonsterDetectionTopLeftEdge() {
        Day_20 day = new Day_20();

        List<String> image = Arrays.asList(
                "..................#.",
                "#....##....##....###",
                ".#..#..#..#..#..#...",
                "....................");
        int monsters = day.findNrOfSeaMonsters(image);

        assertThat(monsters).isEqualTo(1);
    }

    @Test
    void testSeaMonsterDetectionBottomRightEdge() {
        Day_20 day = new Day_20();

        List<String> image = Arrays.asList(
                ".....................",
                "...................#.",
                ".#....##....##....###",
                "..#..#..#..#..#..#...");
        int monsters = day.findNrOfSeaMonsters(image);

        assertThat(monsters).isEqualTo(1);
    }

    @Test
    void testSeaMonsterDetection2Sequential() {
        Day_20 day = new Day_20();

        List<String> image = Arrays.asList(
                "........................................",
                "..................#...................#.",
                "#....##....##....####....##....##....###",
                ".#..#..#..#..#..#....#..#..#..#..#..#...");
        int monsters = day.findNrOfSeaMonsters(image);

        assertThat(monsters).isEqualTo(2);
    }

    @Test
    void doPart1() throws Exception {
        Day_20 day = new Day_20();

        assertThat(day.doPart1(day.readInput("day_20_01.txt"))).isEqualTo("20899048083289");
    }

    @Test
    void doPart2() throws Exception {
        Day_20 day = new Day_20();

        assertThat(day.doPart2(day.readInput("day_20_01.txt"))).isEqualTo("273");
    }

}