package recruitmentTask.intersection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recruitmentTask.TrafficLight.TrafficLight;
import recruitmentTask.TrafficLight.TrafficLightState;
import recruitmentTask.road.Direction;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionControllerTest {

    private IntersectionController controller;

    @BeforeEach
    void setUp() {
        controller = new IntersectionController();
    }

    @Test
    void testInitialState() {
        Map<Direction, TrafficLight> lights = controller.getTrafficLights();

        assertEquals(TrafficLightState.GREEN, lights.get(Direction.north).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.south).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.west).getState());
    }

    @Test
    void testHandleTraffic() {
        List<Direction> busyRoads = List.of(Direction.west, Direction.east);

        controller.handleTraffic(busyRoads);

        Map<Direction, TrafficLight> lights = controller.getTrafficLights();

        assertEquals(TrafficLightState.GREEN, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.west).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.south).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.north).getState());
    }

    @Test
    void testIsGreenOn() {
        assertTrue(controller.isGreenOn(Direction.north));
        assertTrue(controller.isGreenOn(Direction.south));
        assertFalse(controller.isGreenOn(Direction.east));
        assertFalse(controller.isGreenOn(Direction.west));
    }

    @Test
    void testChangeLightMultipleTimes() {
        List<Direction> northSouth = Direction.northSouth();
        List<Direction> westEast = Direction.westEast();

        controller.handleTraffic(westEast);
        controller.handleTraffic(northSouth);
        controller.handleTraffic(westEast);

        Map<Direction, TrafficLight> lights = controller.getTrafficLights();
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.west).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.south).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.north).getState());
    }

    @Test
    void testChangeSameLightMultipleTimes() {
        List<Direction> westEast = Direction.westEast();

        controller.handleTraffic(westEast);
        verifyWestEastLights();

        controller.handleTraffic(westEast);
        verifyWestEastLights();

        controller.handleTraffic(westEast);
        verifyWestEastLights();
    }

    private void verifyWestEastLights() {
        Map<Direction, TrafficLight> lights = controller.getTrafficLights();

        assertEquals(TrafficLightState.GREEN, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.west).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.south).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.north).getState());
    }
}