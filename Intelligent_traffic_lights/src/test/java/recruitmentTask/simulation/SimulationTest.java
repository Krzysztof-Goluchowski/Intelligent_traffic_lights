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

    @Test
    void testAddVehicle() {
        Command command = new Command(CommandType.addVehicle, "V1", Direction.north, Direction.south);

        Simulation simulation = new Simulation(List.of(command));
        simulation.run();

        assertEquals(1, simulation.getRoads().get(Direction.north).getVehicleCount());
        assertEquals(0, simulation.getRoads().get(Direction.south).getVehicleCount());
        assertEquals(0, simulation.getRoads().get(Direction.west).getVehicleCount());
        assertEquals(0, simulation.getRoads().get(Direction.east).getVehicleCount());
    }

    @Test
    void testRunWithSingleStep() {
        Command command1 = new Command(CommandType.addVehicle, "V1", Direction.north, Direction.south);
        Command command2 = new Command(CommandType.step);

        Simulation simulation = new Simulation(List.of(command1, command2));
        simulation.run();

        assertEquals(0, simulation.getRoads().get(Direction.north).getVehicleCount());
        assertEquals(0, simulation.getRoads().get(Direction.south).getVehicleCount());
        assertEquals(0, simulation.getRoads().get(Direction.west).getVehicleCount());
        assertEquals(0, simulation.getRoads().get(Direction.east).getVehicleCount());
        assertEquals(1, simulation.getStepStatuses().size());
    }

    @Test
    void testRecordStepStatus() {
        Command addVehicle = new Command(CommandType.addVehicle, "V1", Direction.north, Direction.south);
        Command stepCommand1 = new Command(CommandType.step);
        Command stepCommand2 = new Command(CommandType.step);

        simulation = new Simulation(List.of(addVehicle, stepCommand1, stepCommand2));
        simulation.run();

        List<List<String>> statuses = simulation.getStepStatuses();

        assertEquals(2, statuses.size());
        assertEquals(List.of("V1"), statuses.get(0));
        assertTrue(statuses.get(1).isEmpty());
    }

    @Test
    void testIntersectionPriority() {
        Command addVehicle1 = new Command(CommandType.addVehicle, "V1", Direction.north, Direction.east);
        Command addVehicle2 = new Command(CommandType.addVehicle, "V2", Direction.south, Direction.west);
        Command stepCommand = new Command(CommandType.step);

        simulation = new Simulation(List.of(addVehicle1, addVehicle2, stepCommand));
        simulation.run();

        // Verify that both vehicles left the intersection (since they have equal priority)
        List<List<String>> statuses = simulation.getStepStatuses();
        assertEquals(List.of("V1", "V2"), statuses.get(0));
    }

    @Test
    void testLightChange() {
        Command addVehicle1 = new Command(CommandType.addVehicle, "V1", Direction.west, Direction.east);
        Command stepCommand = new Command(CommandType.step);

        simulation = new Simulation(List.of(addVehicle1, stepCommand));
        simulation.run();

        assertTrue(simulation.getIntersectionController().isGreenOn(Direction.west));
        assertTrue(simulation.getIntersectionController().isGreenOn(Direction.east));
        assertFalse(simulation.getIntersectionController().isGreenOn(Direction.north));
        assertFalse(simulation.getIntersectionController().isGreenOn(Direction.south));
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
        Command addVehicle1 = new Command(CommandType.addVehicle, "V1", Direction.north, Direction.south);
        Command addVehicle2 = new Command(CommandType.addVehicle, "V2", Direction.north, Direction.south);
        Command addVehicle3 = new Command(CommandType.addVehicle, "V3", Direction.north, Direction.south);

        Command addVehicle4 = new Command(CommandType.addVehicle, "V4", Direction.south, Direction.west);
        Command addVehicle5 = new Command(CommandType.addVehicle, "V5", Direction.south, Direction.north);
        Command addVehicle6 = new Command(CommandType.addVehicle, "V6", Direction.south, Direction.north);

        Command stepCommand1 = new Command(CommandType.step);
        Command stepCommand2 = new Command(CommandType.step);
        Command stepCommand3 = new Command(CommandType.step);

        simulation = new Simulation(List.of(addVehicle1, addVehicle2, addVehicle3, addVehicle4, addVehicle5, addVehicle6, stepCommand1, stepCommand2, stepCommand3, stepCommand2, stepCommand3, stepCommand2));
        simulation.run();

        List<List<String>> statuses = simulation.getStepStatuses();
        assertEquals(List.of("V1"), statuses.get(0));
        assertEquals(List.of("V2"), statuses.get(1));
        assertEquals(List.of("V3"), statuses.get(2));
        assertEquals(List.of("V4"), statuses.get(3));
        assertEquals(List.of("V5"), statuses.get(4));
        assertEquals(List.of("V6"), statuses.get(5));
    }

    @Test
    void testSecondCarTurnsLeft() {
        Command addVehicle1 = new Command(CommandType.addVehicle, "V1", Direction.north, Direction.south);
        Command addVehicle2 = new Command(CommandType.addVehicle, "V2", Direction.north, Direction.south);
        Command addVehicle3 = new Command(CommandType.addVehicle, "V3", Direction.north, Direction.south);

        Command addVehicle4 = new Command(CommandType.addVehicle, "V4", Direction.south, Direction.north);
        Command addVehicle5 = new Command(CommandType.addVehicle, "V5", Direction.south, Direction.west);
        Command addVehicle6 = new Command(CommandType.addVehicle, "V6", Direction.south, Direction.north);

        Command stepCommand1 = new Command(CommandType.step);
        Command stepCommand2 = new Command(CommandType.step);
        Command stepCommand3 = new Command(CommandType.step);

        simulation = new Simulation(List.of(addVehicle1, addVehicle2, addVehicle3, addVehicle4, addVehicle5, addVehicle6, stepCommand1, stepCommand2, stepCommand3, stepCommand2, stepCommand3, stepCommand2));
        simulation.run();

        List<List<String>> statuses = simulation.getStepStatuses();
        assertEquals(List.of("V1", "V4"), statuses.get(0));
        assertEquals(List.of("V2"), statuses.get(1));
        assertEquals(List.of("V3"), statuses.get(2));
        assertEquals(List.of("V5"), statuses.get(3));
        assertEquals(List.of("V6"), statuses.get(4));
        assertTrue(statuses.get(5).isEmpty());
    }
}