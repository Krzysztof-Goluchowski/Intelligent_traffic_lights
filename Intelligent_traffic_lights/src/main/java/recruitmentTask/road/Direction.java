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

    public static boolean isTurningRight(Direction startRoad, Direction endRoad) {
        return switch (startRoad) {
            case south -> endRoad == east;
            case north -> endRoad == west;
            case west -> endRoad == south;
            case east -> endRoad == north;
        };
    }

    public static boolean isOnTheRight(Direction dir1, Direction dir2) {
        return isTurningRight(dir1, dir2);
    }

    public static boolean isGoingStraight(Direction startRoad, Direction endRoad) {
        return switch (startRoad) {
            case south -> endRoad == north;
            case north -> endRoad == south;
            case east -> endRoad == west;
            case west -> endRoad == east;
        };
    }

    public static boolean isMakingUTurn(Direction startRoad, Direction endRoad) {
        return startRoad == endRoad;
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
