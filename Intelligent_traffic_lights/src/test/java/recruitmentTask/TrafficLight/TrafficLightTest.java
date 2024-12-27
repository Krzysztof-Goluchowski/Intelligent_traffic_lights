package recruitmentTask.TrafficLight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightTest {

    private TrafficLight trafficLight;

    @BeforeEach
    void setUp() {
        trafficLight = new TrafficLight();
    }

    @Test
    void testChangeState() {
        TrafficLightState[] expectedStates = {
                TrafficLightState.RED,
                TrafficLightState.YELLOW_GREEN,
                TrafficLightState.GREEN,
                TrafficLightState.YELLOW_RED,
                TrafficLightState.RED
        };

        for (int i = 0; i < expectedStates.length; i++) {
            TrafficLightState currentState = trafficLight.getState();
            assertEquals(expectedStates[i], currentState, "Unexpected state at step " + i);
            trafficLight.changeState();
        }
    }

    @Test
    void testGetState() {
        assertEquals(TrafficLightState.RED, trafficLight.getState());
    }
}