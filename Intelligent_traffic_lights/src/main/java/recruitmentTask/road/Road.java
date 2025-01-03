package recruitmentTask.road;

import recruitmentTask.vehicle.Vehicle;

import java.util.LinkedList;
import java.util.Queue;

public class Road {
    private final Queue<Vehicle> vehicles;

    private final Direction name;

    private int vehicleCount = 0;

    public Road(Direction direction) {
        this.vehicles = new LinkedList<>();
        this.name = direction;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicleCount++;
    }

    public Vehicle removeVehicle() {
        Vehicle vehicle = vehicles.poll();
        if (vehicle == null) {
            return null;
        }
        vehicleCount--;
        return vehicle;
    }

    public Queue<Vehicle> getVehicles() {
        return vehicles;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public Direction getName() {
        return name;
    }
}
