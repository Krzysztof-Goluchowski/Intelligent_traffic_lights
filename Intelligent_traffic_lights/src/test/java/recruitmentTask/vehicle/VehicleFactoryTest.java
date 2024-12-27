package recruitmentTask.vehicle;

import org.junit.jupiter.api.Test;
import recruitmentTask.command.Command;
import recruitmentTask.command.CommandType;
import recruitmentTask.road.Direction;

import static org.junit.jupiter.api.Assertions.*;

class VehicleFactoryTest {

    @Test
    void testCreate() {
        Command command = new Command();
        command.setCommandType(CommandType.addVehicle);
        command.setVehicleId("Vehicle1");
        command.setStartRoad(Direction.north);
        command.setEndRoad(Direction.south);

        Vehicle vehicle = VehicleFactory.create(command);

        assertEquals("Vehicle1", vehicle.vehicleId());
        assertEquals(Direction.north, vehicle.startRoad());
        assertEquals(Direction.south, vehicle.endRoad());
    }
}