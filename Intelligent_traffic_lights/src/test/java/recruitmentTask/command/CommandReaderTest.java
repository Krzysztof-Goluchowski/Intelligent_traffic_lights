package recruitmentTask.command;

import org.junit.jupiter.api.Test;
import recruitmentTask.road.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandReaderTest {

    @Test
    void testReadCommandsWithValidInputFile() throws IOException {
        String jsonContent = """
            {
                "commands": [
                    {
                        "type": "addVehicle",
                        "vehicleId": "vehicle1",
                        "startRoad": "north",
                        "endRoad": "south"
                    },
                    {
                        "type": "step"
                    },
                    {
                        "type": "addVehicle",
                        "vehicleId": "vehicle2",
                        "startRoad": "west",
                        "endRoad": "east"
                    }
                ]
            }
            """;

        Path tempFile = Files.createTempFile("input", ".json");
        Files.writeString(tempFile, jsonContent);

        List<Command> commands = CommandReader.readCommands(tempFile.toString());

        assertEquals(3, commands.size());

        assertInstanceOf(AddVehicleCommand.class, commands.getFirst());
        AddVehicleCommand addVehicle1 = (AddVehicleCommand) commands.getFirst();
        assertEquals("vehicle1", addVehicle1.getVehicleId());
        assertEquals(Direction.north, addVehicle1.getStartRoad());
        assertEquals(Direction.south, addVehicle1.getEndRoad());

        assertInstanceOf(StepCommand.class, commands.get(1));

        assertInstanceOf(AddVehicleCommand.class, commands.get(2));
        AddVehicleCommand addVehicle2 = (AddVehicleCommand) commands.get(2);
        assertEquals("vehicle2", addVehicle2.getVehicleId());
        assertEquals(Direction.west, addVehicle2.getStartRoad());
        assertEquals(Direction.east, addVehicle2.getEndRoad());

        Files.delete(tempFile);
    }

    @Test
    void testReadCommandsWithEmptyCommandsArray() throws IOException {
        String jsonContent = """
                {
                    "commands": []
                }
                """;

        Path tempFile = Files.createTempFile("input", ".json");
        Files.writeString(tempFile, jsonContent);

        List<Command> commands = CommandReader.readCommands(tempFile.toString());

        assertEquals(0, commands.size());

        Files.delete(tempFile);
    }

    @Test
    void testReadCommandsWithEmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("input", ".json");
        Files.writeString(tempFile, "");

        Exception exception = assertThrows(IOException.class, () -> {
            try {
                CommandReader.readCommands(tempFile.toString());
            } finally {
                Files.delete(tempFile);
            }
        });

        assertTrue(exception.getMessage().contains("No content"));
    }

    @Test
    void testReadCommandsWithInvalidJsonFormat() throws IOException {
        String invalidJsonContent = """
        {
            "commands": [
                {
                    "type": "addVehicle",
                    "vehicleId": "vehicle1",
                    "startRoad": "north"
        """;

        Path tempFile = Files.createTempFile("input", ".json");
        Files.writeString(tempFile, invalidJsonContent);

        Exception exception = assertThrows(IOException.class, () -> {
            try {
                CommandReader.readCommands(tempFile.toString());
            } finally {
                Files.delete(tempFile);
            }
        });

        assertTrue(exception.getMessage().contains("Unexpected end-of-input"));
    }

    @Test
    void testReadCommands_withInvalidJsonFormat2() throws IOException {
        String invalidJsonFormat = """
                {
                    "invalidField": "invalidValue"
                }
                """;

        Path tempFile = Files.createTempFile("input", ".json");
        Files.writeString(tempFile, invalidJsonFormat);

        Exception exception = assertThrows(IOException.class, () -> {
            try {
                CommandReader.readCommands(tempFile.toString());
            } finally {
                Files.delete(tempFile);
            }
        });

        assertTrue(
                exception.getMessage().contains("Cannot deserialize") ||
                exception.getMessage().contains("Unrecognized field")
        );
    }

}