package recruitmentTask.simulation;

import recruitmentTask.TrafficLight.TrafficLightController;
import recruitmentTask.command.Command;
import recruitmentTask.intersection.IntersectionManager;

import java.util.*;

public class Simulation {
    private final TrafficLightController lightController;
    private final IntersectionManager intersectionManager;
    private final List<List<String>> stepStatuses;
    private final List<Command> commands;

    public Simulation(List<Command> commands) {
        this.commands = commands;
        lightController = new TrafficLightController();
        stepStatuses = new ArrayList<>();
        this.intersectionManager = new IntersectionManager(lightController, stepStatuses);
    }

    public void run() {
        for (Command command : commands) {
            command.execute(intersectionManager);
        }
    }

    public List<List<String>> getStepStatuses() {
        return stepStatuses;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public TrafficLightController getIntersectionController() {
        return lightController;
    }
}