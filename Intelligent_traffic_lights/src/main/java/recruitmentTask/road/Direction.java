package recruitmentTask.road;

import java.util.List;

public enum Direction {
    north,
    south,
    west,
    east;

    public static List<Direction> northSouth() {
        return List.of(north, south);
    }

    public static List<Direction> westEast() {
        return List.of(west, east);
    }


}
