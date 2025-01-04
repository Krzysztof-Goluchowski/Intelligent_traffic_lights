package recruitmentTask.vehicle;

import org.junit.jupiter.api.Test;
import recruitmentTask.command.AddVehicleCommand;
import recruitmentTask.command.Command;
import recruitmentTask.command.CommandType;
import recruitmentTask.road.Direction;

import static org.junit.jupiter.api.Assertions.*;

class VehicleFactoryTest {

    private AddVehicleCommand createAddVehicleCommand(String vehicleId, Direction start, Direction end) {
        return new AddVehicleCommand(vehicleId, start, end);
    }

    @Test
    void testCreate() {
        AddVehicleCommand command = createAddVehicleCommand("Vehicle1", Direction.north, Direction.south);

        Vehicle vehicle = VehicleFactory.create(command);

        assertEquals("Vehicle1", vehicle.vehicleId());
        assertEquals(Direction.north, vehicle.startRoad());
        assertEquals(Direction.south, vehicle.endRoad());
    }
}