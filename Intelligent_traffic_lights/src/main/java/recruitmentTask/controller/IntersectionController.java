package recruitmentTask.controller;

import recruitmentTask.TrafficLight.TrafficLight;
import recruitmentTask.TrafficLight.TrafficLightState;
import recruitmentTask.road.Direction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntersectionController {
    private final Map<Direction, TrafficLight> trafficLights;

    public IntersectionController() {
        trafficLights = new HashMap<>();
        trafficLights.put(Direction.north, new TrafficLight());
        trafficLights.put(Direction.south, new TrafficLight());
        trafficLights.put(Direction.west, new TrafficLight());
        trafficLights.put(Direction.east, new TrafficLight());

        changeLight(List.of(Direction.north, Direction.south));
    }

    public Map<Direction, TrafficLight> getTrafficLights() {
        return trafficLights;
    }

    public void handleTraffic(List<Direction> busyRoads) {
        List<Direction> perpendicularRoads = Direction.getPerpendicularRoads(busyRoads);
        changeLight(perpendicularRoads);
        changeLight(busyRoads);
    }

    private void changeLight(List<Direction> roads) {
        for (Direction direction : roads) {
            trafficLights.get(direction).changeState();
            trafficLights.get(direction).changeState();
        }
    }

    public boolean isGreenOn(Direction direction) {
        return trafficLights.get(direction).getState() == TrafficLightState.GREEN;
    }
}
