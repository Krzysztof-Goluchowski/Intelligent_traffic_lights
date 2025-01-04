package recruitmentTask.intersection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recruitmentTask.TrafficLight.TrafficLight;
import recruitmentTask.TrafficLight.TrafficLightController;
import recruitmentTask.TrafficLight.TrafficLightState;
import recruitmentTask.road.Direction;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightControllerTest {

    private TrafficLightController lightController;

    @BeforeEach
    void setUp() {
        lightController = new TrafficLightController();
    }

    @Test
    void testInitialState() {
        Map<Direction, TrafficLight> lights = lightController.getTrafficLights();

        assertEquals(TrafficLightState.GREEN, lights.get(Direction.north).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.south).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.west).getState());
    }

    @Test
    void testHandleTraffic() {
        List<Direction> busyRoads = List.of(Direction.west, Direction.east);

        lightController.handleTraffic(busyRoads);

        Map<Direction, TrafficLight> lights = lightController.getTrafficLights();

        assertEquals(TrafficLightState.GREEN, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.west).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.south).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.north).getState());
    }

    @Test
    void testIsGreenOn() {
        assertTrue(lightController.isGreenOn(Direction.north));
        assertTrue(lightController.isGreenOn(Direction.south));
        assertFalse(lightController.isGreenOn(Direction.east));
        assertFalse(lightController.isGreenOn(Direction.west));
    }

    @Test
    void testChangeLightMultipleTimes() {
        List<Direction> northSouth = Direction.northSouth();
        List<Direction> westEast = Direction.westEast();

        lightController.handleTraffic(westEast);
        lightController.handleTraffic(northSouth);
        lightController.handleTraffic(westEast);

        Map<Direction, TrafficLight> lights = lightController.getTrafficLights();
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.west).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.south).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.north).getState());
    }

    @Test
    void testChangeSameLightMultipleTimes() {
        List<Direction> westEast = Direction.westEast();

        lightController.handleTraffic(westEast);
        verifyWestEastLights();

        lightController.handleTraffic(westEast);
        verifyWestEastLights();

        lightController.handleTraffic(westEast);
        verifyWestEastLights();
    }

    private void verifyWestEastLights() {
        Map<Direction, TrafficLight> lights = lightController.getTrafficLights();

        assertEquals(TrafficLightState.GREEN, lights.get(Direction.east).getState());
        assertEquals(TrafficLightState.GREEN, lights.get(Direction.west).getState());

        assertEquals(TrafficLightState.RED, lights.get(Direction.south).getState());
        assertEquals(TrafficLightState.RED, lights.get(Direction.north).getState());
    }
}