package recruitmentTask.road;

import recruitmentTask.vehicle.Vehicle;

import java.util.LinkedList;
import java.util.Queue;

public class Road {
    private final Queue<Vehicle> vehicles;
    private final Direction name;

    public Road(Direction direction) {
        this.vehicles = new LinkedList<>();
        this.name = direction;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public String removeVehicle() {
        Vehicle vehicle = vehicles.poll();
        if (vehicle == null) {
            return null;
        }
        return vehicle.vehicleId();
    }

    public Queue<Vehicle> getVehicles() {
        return vehicles;
    }

    public int getVehicleCount() {
        return vehicles.size();
    }

    public Direction getName() {
        return name;
    }
}
