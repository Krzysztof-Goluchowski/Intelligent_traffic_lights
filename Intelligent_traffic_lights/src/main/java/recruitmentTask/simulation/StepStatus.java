package recruitmentTask.simulation;

import recruitmentTask.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class StepStatus {
    private final List<String> leftVehicles;

    public StepStatus(){
        leftVehicles = new ArrayList<>();
    }

    public StepStatus(List<Vehicle> vehicles){
        leftVehicles = new ArrayList<>();
        addAllVehicles(vehicles);
    }

    private void addAllVehicles(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            addVehicle(vehicle);
        }
    }

    public void addVehicle(Vehicle vehicle) {
        leftVehicles.add(vehicle.vehicleId());
    }

    public List<String> getLeftVehicles() {
        return leftVehicles;
    }

    public boolean isEmpty() {
        return leftVehicles.isEmpty();
    }

    @Override
    public String toString() {
        return leftVehicles.toString();
    }
}
