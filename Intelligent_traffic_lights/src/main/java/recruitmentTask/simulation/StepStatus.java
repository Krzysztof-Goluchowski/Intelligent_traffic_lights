package recruitmentTask.simulation;

import recruitmentTask.vehicle.Vehicle;

import java.util.LinkedList;
import java.util.List;

public class StepStatus {
    private final List<String> leftVehicles;

    public StepStatus(){
        leftVehicles = new LinkedList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        leftVehicles.add(vehicle.vehicleId());
    }

    public List<String> getLeftVehicles() {
        return leftVehicles;
    }
}
