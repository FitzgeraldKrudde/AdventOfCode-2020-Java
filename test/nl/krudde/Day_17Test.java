package nl.krudde;

import nl.krudde.Day_17.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day_17Test {

    @Test
    void neighbours() {
        Day_17 day = new Day_17();

        Point point3D = new Point(new int[]{1, 1, 1});
        List<Point> neighbours = point3D.neighbours();
        assertThat(neighbours.size()).isEqualTo(26);
        neighbours.forEach(neighbour -> assertThat(neighbour.isNeighbour(point3D)));
//        neighbours.forEach(l -> System.out.println(Arrays.toString(l)));

        Point point4D = new Point(new int[]{1, 1, 1, 1});
        neighbours = point4D.neighbours();
        assertThat(neighbours.size()).isEqualTo(80);
        neighbours.forEach(neighbour -> assertThat(neighbour.isNeighbour(point4D)));

        Point point5D = new Point(new int[]{1, 1, 1, 1, 1});
        neighbours = point5D.neighbours();
        assertThat(neighbours.size()).isEqualTo(242);
        neighbours.forEach(neighbour -> assertThat(neighbour.isNeighbour(point5D)));

    }

    @Test
    void doPart1() throws Exception {
        Day_17 day = new Day_17();

        assertThat(day.doPart1(day.readInput("day_17_01.txt"))).isEqualTo("112");
    }

    @Test
    void doPart2() throws Exception {
        Day_17 day = new Day_17();

        assertThat(day.doPart2(day.readInput("day_17_01.txt"))).isEqualTo("848");
    }

}