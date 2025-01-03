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
        assertEquals(CommandType.addVehicle, commands.get(0).getCommandType());
        assertEquals("vehicle1", commands.get(0).getVehicleId());
        assertEquals(Direction.north, commands.get(0).getStartRoad());
        assertEquals(Direction.south, commands.get(0).getEndRoad());

        assertEquals(CommandType.step, commands.get(1).getCommandType());
        assertNull(commands.get(1).getVehicleId());
        assertNull(commands.get(1).getStartRoad());
        assertNull(commands.get(1).getEndRoad());

        assertEquals(CommandType.addVehicle, commands.get(2).getCommandType());
        assertEquals("vehicle2", commands.get(2).getVehicleId());
        assertEquals(Direction.west, commands.get(2).getStartRoad());
        assertEquals(Direction.east, commands.get(2).getEndRoad());

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