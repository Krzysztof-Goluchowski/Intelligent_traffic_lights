package recruitmentTask.vehicle;

import recruitmentTask.command.AddVehicleCommand;
import recruitmentTask.command.Command;

public class VehicleFactory {

    public static Vehicle create(AddVehicleCommand command) {
        return new Vehicle(
                command.getVehicleId(),
                command.getStartRoad(),
                command.getEndRoad()
        );
    }
}
