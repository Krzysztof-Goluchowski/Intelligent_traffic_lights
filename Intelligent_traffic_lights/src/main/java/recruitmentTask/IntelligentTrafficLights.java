package recruitmentTask;

import recruitmentTask.command.Command;
import recruitmentTask.command.CommandReader;
import recruitmentTask.simulation.Simulation;

import java.io.IOException;
import java.util.List;

public class IntelligentTrafficLights {
    public static void main(String[] args) {
        System.out.println("Application starts");

        if (args.length != 2) {
            System.out.println("Invalid arguments. Usage: ");
            System.out.println("gradlew build");
            System.out.println("gradlew run --args=\"input.json output.json\"");
            return;
        }

        String inFile = args[0];
        String outFile = args[1];
        System.out.println("Input file: " + inFile);
        System.out.println("Output file: " + outFile);

        List<Command> commands;

        try {
            commands = CommandReader.readCommands(inFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Simulation simulation = new Simulation(commands, outFile);
        simulation.run();

        System.out.println("Application ends");
    }
}