package recruitmentTask.simulation;

import recruitmentTask.intersection.IntersectionController;
import recruitmentTask.command.Command;
import recruitmentTask.intersection.IntersectionManager;

import java.util.*;

public class Simulation {
    private final IntersectionController intersectionController;
    private final IntersectionManager intersectionManager;
    private final List<List<String>> stepStatuses;
    private final List<Command> commands;

    public Simulation(List<Command> commands) {
        this.commands = commands;
        intersectionController = new IntersectionController();
        stepStatuses = new ArrayList<>();
        this.intersectionManager = new IntersectionManager(intersectionController, stepStatuses);
    }

    public void run() {
        for (Command command : commands) {
            switch (command.getCommandType()) {
                case step -> intersectionManager.step();
                case addVehicle -> intersectionManager.addVehicle(command);
            }
        }
    }

    public List<List<String>> getStepStatuses() {
        return stepStatuses;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public IntersectionController getIntersectionController() {
        return intersectionController;
    }
}