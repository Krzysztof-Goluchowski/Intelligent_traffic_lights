package recruitmentTask.simulation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import recruitmentTask.TrafficLight.TrafficLightController;
import recruitmentTask.command.Command;
import recruitmentTask.intersection.IntersectionManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Simulation {
    private final TrafficLightController lightController;
    private final IntersectionManager intersectionManager;
    private List<StepStatus> stepStatuses;
    private final List<Command> commands;
    private String outFile;

    public Simulation(List<Command> commands, String outFile) {
        this.commands = commands;
        lightController = new TrafficLightController();
        stepStatuses = new ArrayList<>();
        this.intersectionManager = new IntersectionManager(lightController, stepStatuses);
        this.outFile = outFile;
    }

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

        if (outFile != null) {
            writeStepStatusesToFile(outFile);
        }
    }

    private void writeStepStatusesToFile(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            Map<String, List<StepStatus>> output = new HashMap<>();
            output.put("stepStatuses", stepStatuses);

            gson.toJson(output, writer);
            System.out.println("Step statuses successfully written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing step statuses to file: " + e.getMessage());
        }
    }

    public List<StepStatus> getStepStatuses() {
        return stepStatuses;
    }

    public void setStepStatuses(List<StepStatus> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public TrafficLightController getIntersectionController() {
        return lightController;
    }
}