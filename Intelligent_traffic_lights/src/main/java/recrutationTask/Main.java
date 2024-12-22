package recrutationTask;

import recrutationTask.command.Command;
import recrutationTask.command.CommandReader;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String inFile = args[0];
        String outFile = args[1];
        System.out.println("Input file: " + inFile);
        System.out.println("Output file: " + outFile);

        try {
            List<Command> commands = CommandReader.readCommands(inFile);
            for (Command command : commands) {
                System.out.println(command.getCommandType());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}