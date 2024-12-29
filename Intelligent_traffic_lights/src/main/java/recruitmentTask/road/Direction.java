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

    public static List<Direction> getPerpendicularRoads(List<Direction> roads) {
        return switch (roads.getFirst()) {
            case north, south -> westEast();
            case west, east -> northSouth();
        };
    }

    public static boolean isTurningLeft(Direction startRoad, Direction endRoad) {
        return switch (startRoad) {
            case south -> endRoad == west;
            case north -> endRoad == east;
            case west -> endRoad == north;
            case east -> endRoad == south;
        };
    }

    public static Direction getOpositeDirection(Direction direction) {
        return switch (direction) {
            case north -> south;
            case south -> north;
            case east -> west;
            case west -> east;
        };
    }
}
