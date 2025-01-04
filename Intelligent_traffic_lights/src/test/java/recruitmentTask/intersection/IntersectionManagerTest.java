package recruitmentTask.intersection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recruitmentTask.TrafficLight.TrafficLightController;
import recruitmentTask.command.AddVehicleCommand;
import recruitmentTask.road.Direction;
import recruitmentTask.simulation.StepStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionManagerTest {

    private TrafficLightController lightController;

    private List<StepStatus> stepStatuses;

    private IntersectionManager manager;

    @BeforeEach
    void setUp() {
        this.lightController = new TrafficLightController();
        this.stepStatuses = new ArrayList<>();
        this.manager = new IntersectionManager(lightController, stepStatuses);
    }

    private AddVehicleCommand createAddVehicleCommand(String vehicleId, Direction start, Direction end) {
        return new AddVehicleCommand(vehicleId, start, end);
    }

    @Test
    void testAddVehicle() {
        AddVehicleCommand addVehicle = createAddVehicleCommand("V1", Direction.north, Direction.south);

        manager.addVehicle(addVehicle);

        assertEquals(1, manager.getRoads().get(Direction.north).getVehicleCount());
        assertEquals(0, manager.getRoads().get(Direction.south).getVehicleCount());
        assertEquals(0, manager.getRoads().get(Direction.west).getVehicleCount());
        assertEquals(0, manager.getRoads().get(Direction.east).getVehicleCount());
    }

    @Test
    void testLightChange() {
        AddVehicleCommand addVehicle = createAddVehicleCommand("V1", Direction.west, Direction.east);
        verifyInitialTrafficLightState();

        manager.addVehicle(addVehicle);
        manager.step();

        verifyUpdatedTrafficLightState();
    }

    private void verifyInitialTrafficLightState() {
        assertFalse(lightController.isGreenOn(Direction.west), "West light should be red initially.");
        assertFalse(lightController.isGreenOn(Direction.east), "East light should be red initially.");
        assertTrue(lightController.isGreenOn(Direction.north), "North light should be green initially.");
        assertTrue(lightController.isGreenOn(Direction.south), "South light should be green initially.");
    }

    private void verifyUpdatedTrafficLightState() {
        assertTrue(lightController.isGreenOn(Direction.west), "West light should be green after step.");
        assertTrue(lightController.isGreenOn(Direction.east), "East light should be green after step.");
        assertFalse(lightController.isGreenOn(Direction.north), "North light should be red after step.");
        assertFalse(lightController.isGreenOn(Direction.south), "South light should be red after step.");
    }

    @Test
    void testMoveVehicles() {
        AddVehicleCommand addVehicle1 = createAddVehicleCommand("V1", Direction.north, Direction.south);
        AddVehicleCommand addVehicle2 = createAddVehicleCommand("V2", Direction.south, Direction.north);

        manager.addVehicle(addVehicle1);
        manager.addVehicle(addVehicle2);
        manager.step();

        List<StepStatus> statuses = manager.getStepStatuses();

        assertEquals(List.of("V1", "V2"), statuses.getFirst().getLeftVehicles());
    }

    @Test
    void testStepWithEmptyIntersection() {
        manager.step();
        assertTrue(stepStatuses.getFirst().isEmpty(), "Step statuses should be empty if no vehicles are at the intersection");
    }

    @Test
    void testNoPriorityWhenTurningLeft() {
        AddVehicleCommand addVehicle1 = createAddVehicleCommand("V1", Direction.north, Direction.south);
        AddVehicleCommand addVehicle2 = createAddVehicleCommand("V2", Direction.south, Direction.west);

        manager.addVehicle(addVehicle1);
        manager.addVehicle(addVehicle2);

        manager.step();
        assertEquals(List.of("V1"), stepStatuses.getFirst().getLeftVehicles());
        assertEquals(1, manager.getVehiclesAtIntersection().size());

        manager.step();
        assertEquals(List.of("V2"), stepStatuses.get(1).getLeftVehicles());
    }

    @Test
    void testIntersectionPriority() {
        setupVehiclesForPriorityTest();

        verifyPriorityStep(0, List.of("V1", "V2"));
        verifyPriorityStep(1, List.of("V3", "V4"));
        verifyPriorityStep(2, List.of("V5", "V6"));
    }

    private void setupVehiclesForPriorityTest() {
        manager.addVehicle(createAddVehicleCommand("V1", Direction.north, Direction.east));
        manager.addVehicle(createAddVehicleCommand("V2", Direction.south, Direction.west));
        manager.addVehicle(createAddVehicleCommand("V3", Direction.north, Direction.south));
        manager.addVehicle(createAddVehicleCommand("V4", Direction.south, Direction.north));
        manager.addVehicle(createAddVehicleCommand("V5", Direction.north, Direction.south));
        manager.addVehicle(createAddVehicleCommand("V6", Direction.south, Direction.east));
    }

    private void verifyPriorityStep(int stepIndex, List<String> expectedVehicles) {
        manager.step();
        assertEquals(expectedVehicles, stepStatuses.get(stepIndex).getLeftVehicles(), "Both vehicles should leave the intersection since they have equal priority");
    }

    @Test
    void testChangeLightWhileVehicleOnIntersection() {
        setupInitialVehicles();
        performSimulationSteps();

        List<StepStatus> statuses = manager.getStepStatuses();

        verifyStatuses(statuses);
    }

    private void setupInitialVehicles() {
        manager.addVehicle(createAddVehicleCommand("V1", Direction.north, Direction.south));
        manager.addVehicle(createAddVehicleCommand("V2", Direction.north, Direction.south));
        manager.addVehicle(createAddVehicleCommand("V3", Direction.north, Direction.south));
        manager.addVehicle(createAddVehicleCommand("V4", Direction.south, Direction.west)); // V4 turns left
    }

    private void performSimulationSteps() {
        manager.step();
        manager.step();
        manager.step();
        manager.addVehicle(createAddVehicleCommand("V5", Direction.west, Direction.east));
        manager.addVehicle(createAddVehicleCommand("V6", Direction.east, Direction.west));
        manager.step();
    }

    private void verifyStatuses(List<StepStatus> statuses) {
        assertEquals(List.of("V1"), statuses.get(0).getLeftVehicles(), "At step 0, V1 should pass the intersection.");
        assertEquals(List.of("V2"), statuses.get(1).getLeftVehicles(), "At step 1, V2 should pass the intersection.");
        assertEquals(List.of("V3", "V4"), statuses.get(2).getLeftVehicles(), "At step 2, V3 and V4 should pass.");
        assertEquals(List.of("V5", "V6"), statuses.get(3).getLeftVehicles(), "At step 3, V5 and V6 should pass after the light changes.");
    }
}