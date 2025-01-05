package recruitmentTask.simulation;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recruitmentTask.command.AddVehicleCommand;
import recruitmentTask.command.Command;
import recruitmentTask.command.CommandType;
import recruitmentTask.command.StepCommand;
import recruitmentTask.intersection.IntersectionManager;
import recruitmentTask.road.Direction;
import recruitmentTask.vehicle.Vehicle;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SimulationTest {

    private Simulation simulation;

    @BeforeEach
    void setUp() {
        simulation = new Simulation(List.of());
    }

    private AddVehicleCommand createAddVehicleCommand(String vehicleId, Direction start, Direction end) {
        return new AddVehicleCommand(vehicleId, start, end);
    }

    private StepCommand createStepCommand() {
        return new StepCommand();
    }

    @Test
    void testStepCommand() {
        IntersectionManager manager = mock(IntersectionManager.class);
        StepCommand stepCommand = createStepCommand();

        stepCommand.execute(manager);

        verify(manager).step();
    }

    @Test
    void testAddVehicleCommand() {
        IntersectionManager manager = mock(IntersectionManager.class);
        AddVehicleCommand addVehicleCommand = createAddVehicleCommand("V1", Direction.north, Direction.east);

        addVehicleCommand.execute(manager);

        verify(manager).addVehicle(addVehicleCommand);
    }

    @Test
    void testRunWithSingleStep() {
        Command command1 = createAddVehicleCommand("V1", Direction.north, Direction.south);
        Command command2 = createStepCommand();

        Simulation simulation = new Simulation(List.of(command1, command2));
        simulation.run();

        List<StepStatus> statuses = simulation.getStepStatuses();
        assertEquals(1, statuses.size());
        assertEquals(List.of("V1"), statuses.getFirst().getLeftVehicles());
    }

    @Test
    void testRecordStepStatus() {
        Command addVehicle = createAddVehicleCommand("V1", Direction.north, Direction.south);
        Command step = createStepCommand();

        simulation = new Simulation(List.of(addVehicle, step, step));
        simulation.run();

        List<StepStatus> statuses = simulation.getStepStatuses();

        assertEquals(2, statuses.size());
        assertEquals(List.of("V1"), statuses.get(0).getLeftVehicles());
        assertTrue(statuses.get(1).isEmpty());
    }

    @Test
    void testNoVehicles() {
        StepCommand stepCommand = createStepCommand();

        simulation = new Simulation(List.of(stepCommand));
        simulation.run();

        assertEquals(1, simulation.getStepStatuses().size());
        assertTrue(simulation.getStepStatuses().getFirst().isEmpty());
    }

    @Test
    void testFirstCarTurnsLeft() {
        Command addVehicle1 = createAddVehicleCommand("V1", Direction.north, Direction.south);
        Command addVehicle2 = createAddVehicleCommand("V2", Direction.north, Direction.south);
        Command addVehicle3 = createAddVehicleCommand("V3", Direction.north, Direction.south);

        Command addVehicle4 = createAddVehicleCommand("V4", Direction.south, Direction.west);  // V4 turns left
        Command addVehicle5 = createAddVehicleCommand("V5", Direction.south, Direction.north);
        Command addVehicle6 = createAddVehicleCommand("V6", Direction.south, Direction.north);

        Command step = createStepCommand();
        List<Command> commands = List.of(
                addVehicle1, addVehicle2, addVehicle3,
                addVehicle4, addVehicle5, addVehicle6,
                step, step, step, step, step, step
        );

        simulation = new Simulation(commands);
        simulation.run();

        List<StepStatus> statuses = simulation.getStepStatuses();

        assertEquals(List.of("V1"), statuses.get(0).getLeftVehicles(), "First vehicle from the north should pass.");
        assertEquals(List.of("V2"), statuses.get(1).getLeftVehicles(), "Second vehicle from the north should pass.");
        assertEquals(List.of("V3"), statuses.get(2).getLeftVehicles(), "Third vehicle from the north should pass.");

        assertEquals(List.of("V4"), statuses.get(3).getLeftVehicles(), "Vehicle turning left (V4) should pass next.");
        assertEquals(List.of("V5"), statuses.get(4).getLeftVehicles(), "Vehicle behind V4 should pass after it.");
        assertEquals(List.of("V6"), statuses.get(5).getLeftVehicles(), "Next vehicle from the south should pass last.");
    }

    @Test
    void testSecondCarTurnsLeft() {
        Command addVehicle1 = createAddVehicleCommand("V1", Direction.north, Direction.south);
        Command addVehicle2 = createAddVehicleCommand("V2", Direction.north, Direction.south);
        Command addVehicle3 = createAddVehicleCommand("V3", Direction.north, Direction.south);

        Command addVehicle4 = createAddVehicleCommand("V4", Direction.south, Direction.north);
        Command addVehicle5 = createAddVehicleCommand("V5", Direction.south, Direction.west);  // V5 turns left
        Command addVehicle6 = createAddVehicleCommand("V6", Direction.south, Direction.north);

        Command step = createStepCommand();

        List<Command> commands = List.of(
                addVehicle1, addVehicle2, addVehicle3,
                addVehicle4, addVehicle5, addVehicle6,
                step, step, step, step, step, step
        );

        simulation = new Simulation(commands);
        simulation.run();

        List<StepStatus> statuses = simulation.getStepStatuses();

        assertEquals(List.of("V1", "V4"), statuses.get(0).getLeftVehicles(), "First vehicle from the north and first from the south should pass.");
        assertEquals(List.of("V2"), statuses.get(1).getLeftVehicles(), "Second vehicle from the north should pass.");
        assertEquals(List.of("V3"), statuses.get(2).getLeftVehicles(), "Third vehicle from the north should pass.");

        assertEquals(List.of("V5"), statuses.get(3).getLeftVehicles(), "V5 should pass next.");
        assertEquals(List.of("V6"), statuses.get(4).getLeftVehicles(), "Next vehicle from the south should pass after V5.");
        assertTrue(statuses.get(5).isEmpty(), "No vehicles should remain after all have passed.");
    }

    @Test
    public void testWriteStepStatusesToFile() throws IOException {
        StepStatus step1 = new StepStatus(List.of(new Vehicle("vehicle1", Direction.north, Direction.south)));
        StepStatus step2 = new StepStatus(List.of());
        StepStatus step3 = new StepStatus(List.of(
                new Vehicle("vehicle2", Direction.north, Direction.south),
                new Vehicle("vehicle3", Direction.south, Direction.north)
        ));

        List<StepStatus> stepStatuses = List.of(step1, step2, step3);

        Path testFolderPath = Paths.get("src", "test", "resources");
        if (!Files.exists(testFolderPath)) {
            Files.createDirectories(testFolderPath);
        }

        Path testFilePath = testFolderPath.resolve("output-test.json");
        String testFilename = testFilePath.toString();

        Simulation simulation = new Simulation(List.of(), testFilename);
        simulation.setStepStatuses(stepStatuses);

        simulation.run();

        String expectedJson = """
        {
          "stepStatuses": [
            { "leftVehicles": ["vehicle1"] },
            { "leftVehicles": [] },
            { "leftVehicles": ["vehicle2", "vehicle3"] }
          ]
        }
        """;

        String actualJson = Files.readString(testFilePath);

        Gson gson = new Gson();
        String expected = gson.toJson(gson.fromJson(expectedJson, Object.class));
        String actual = gson.toJson(gson.fromJson(actualJson, Object.class));

        assertEquals(expected, actual);

        Files.deleteIfExists(testFilePath);
    }
}