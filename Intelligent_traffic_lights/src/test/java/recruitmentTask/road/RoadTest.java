package recruitmentTask.road;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recruitmentTask.vehicle.Vehicle;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class RoadTest {

    private Road road;

    @BeforeEach
    void setUp() {
        road = new Road(Direction.north);
    }

    @Test
    void testAddVehicle() {
        Vehicle vehicle1 = new Vehicle("V1", Direction.north, Direction.south);
        Vehicle vehicle2 = new Vehicle("V2", Direction.north, Direction.south);

        road.addVehicle(vehicle1);
        road.addVehicle(vehicle2);

        Queue<Vehicle> vehicles = road.getVehicles();
        assertEquals(2, vehicles.size());
    }

    @Test
    void testRemoveVehicle() {
        Vehicle vehicle1 = new Vehicle("V1", Direction.north, Direction.south);
        Vehicle vehicle2 = new Vehicle("V2", Direction.north, Direction.south);

        road.addVehicle(vehicle1);
        road.addVehicle(vehicle2);
        String removedVehicle1Id = road.removeVehicle().vehicleId();
        String removedVehicle2Id = road.removeVehicle().vehicleId();

        assertEquals("V1", removedVehicle1Id);
        assertEquals("V2", removedVehicle2Id);
        assertTrue(road.getVehicles().isEmpty());
    }

    @Test
    void testRemoveVehicleFromEmptyRoad() {
        Vehicle removedVehicleId = road.removeVehicle();
        assertNull(removedVehicleId);
        assertEquals(0, road.getVehicleCount());
    }

    @Test
    void testVehiclesCount() {
        Vehicle vehicle1 = new Vehicle("V1", Direction.north, Direction.south);
        Vehicle vehicle2 = new Vehicle("V2", Direction.north, Direction.south);

        road.addVehicle(vehicle1);
        road.addVehicle(vehicle2);

        assertEquals(2, road.getVehicleCount());
    }

    @Test
    void testGetName() {
        assertEquals(Direction.north, road.getName());
    }
}