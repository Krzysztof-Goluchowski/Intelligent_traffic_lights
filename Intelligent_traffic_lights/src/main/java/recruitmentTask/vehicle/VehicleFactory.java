package recruitmentTask.vehicle;

import recruitmentTask.command.Command;

public class VehicleFactory {

    public static Vehicle create(Command command) {
        return new Vehicle(
                command.getVehicleId(),
                command.getStartRoad(),
                command.getEndRoad()
        );
    }
}
