package recruitmentTask.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recruitmentTask.command.Command;
import recruitmentTask.command.CommandType;
import recruitmentTask.road.Direction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    private Simulation simulation;

    @BeforeEach
    void setUp() {
        simulation = new Simulation(List.of());
    }

    private Command createAddVehicleCommand(String vehicleId, Direction start, Direction end) {
        return new Command(CommandType.addVehicle, vehicleId, start, end);
    }

    private Command createStepCommand() {
        return new Command(CommandType.step);
    }

    @Test
    void testRunWithSingleStep() {
        Command command1 = createAddVehicleCommand("V1", Direction.north, Direction.south);
        Command command2 = createStepCommand();

        Simulation simulation = new Simulation(List.of(command1, command2));
        simulation.run();

        List<List<String>> statuses = simulation.getStepStatuses();
        assertEquals(1, statuses.size());
        assertEquals(List.of("V1"), statuses.get(0));
    }

    @Test
    void testRecordStepStatus() {
        Command addVehicle = createAddVehicleCommand("V1", Direction.north, Direction.south);
        Command step = createStepCommand();

        simulation = new Simulation(List.of(addVehicle, step, step));
        simulation.run();

        List<List<String>> statuses = simulation.getStepStatuses();

        assertEquals(2, statuses.size());
        assertEquals(List.of("V1"), statuses.get(0));
        assertTrue(statuses.get(1).isEmpty());
    }

    @Test
    void testNoVehicles() {
        Command stepCommand = new Command(CommandType.step);

        simulation = new Simulation(List.of(stepCommand));
        simulation.run();

        assertEquals(1, simulation.getStepStatuses().size());
        assertTrue(simulation.getStepStatuses().get(0).isEmpty());
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

        List<List<String>> statuses = simulation.getStepStatuses();

        assertEquals(List.of("V1"), statuses.get(0), "First vehicle from the north should pass.");
        assertEquals(List.of("V2"), statuses.get(1), "Second vehicle from the north should pass.");
        assertEquals(List.of("V3"), statuses.get(2), "Third vehicle from the north should pass.");

        assertEquals(List.of("V4"), statuses.get(3), "Vehicle turning left (V4) should pass next.");
        assertEquals(List.of("V5"), statuses.get(4), "Vehicle behind V4 should pass after it.");
        assertEquals(List.of("V6"), statuses.get(5), "Next vehicle from the south should pass last.");
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

        List<List<String>> statuses = simulation.getStepStatuses();

        assertEquals(List.of("V1", "V4"), statuses.get(0), "First vehicle from the north and first from the south should pass.");
        assertEquals(List.of("V2"), statuses.get(1), "Second vehicle from the north should pass.");
        assertEquals(List.of("V3"), statuses.get(2), "Third vehicle from the north should pass.");

        assertEquals(List.of("V5"), statuses.get(3), "V5 should pass next.");
        assertEquals(List.of("V6"), statuses.get(4), "Next vehicle from the south should pass after V5.");
        assertTrue(statuses.get(5).isEmpty(), "No vehicles should remain after all have passed.");
    }
}