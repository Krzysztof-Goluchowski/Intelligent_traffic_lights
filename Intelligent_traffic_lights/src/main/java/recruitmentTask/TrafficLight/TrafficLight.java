package recruitmentTask.TrafficLight;

public class TrafficLight {
    private TrafficLightState state;

    public TrafficLight() {
        this.state = TrafficLightState.RED;
    }

    public void changeState() {
        switch (state) {
            case RED -> state = TrafficLightState.YELLOW_GREEN;
            case YELLOW_GREEN -> state = TrafficLightState.GREEN;
            case GREEN -> state = TrafficLightState.YELLOW_RED;
            case YELLOW_RED -> state = TrafficLightState.RED;
        }
    }

    public TrafficLightState getState() {
        return state;
    }
}
