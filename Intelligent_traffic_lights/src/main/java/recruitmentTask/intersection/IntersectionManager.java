package recruitmentTask.intersection;

import recruitmentTask.TrafficLight.TrafficLightController;
import recruitmentTask.command.AddVehicleCommand;
import recruitmentTask.road.Direction;
import recruitmentTask.road.Road;
import recruitmentTask.simulation.StepStatus;
import recruitmentTask.vehicle.Vehicle;
import recruitmentTask.vehicle.VehicleFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntersectionManager {
    private final TrafficLightController lightController;
    private final List<StepStatus> stepStatuses;
    private final Map<Direction, Road> roads;
    private final List<Vehicle> vehiclesAtIntersection = new ArrayList<>();

    public IntersectionManager(TrafficLightController lightController, List<StepStatus> stepStatuses) {
        this.lightController = lightController;
        this.stepStatuses = stepStatuses;
        this.roads = initializeRoads();
    }

    private Map<Direction, Road> initializeRoads() {
        Map<Direction, Road> roads = new HashMap<>();
        for (Direction direction : Direction.values()) {
            roads.put(direction, new Road(direction));
        }
        return roads;
    }

    public void addVehicle(AddVehicleCommand command) {
        Vehicle vehicle = VehicleFactory.create(command);
        roads.get(command.getStartRoad()).addVehicle(vehicle);
    }

    public void step() {
        List<Direction> busyRoads = getBusyRoads();
        if (isNeedForChange(busyRoads)) {
            lightController.handleTraffic(busyRoads);
            leaveIntersection();
        }
        moveVehicles(busyRoads);
    }

    private List<Direction> getBusyRoads() {
        int northSouthTrafficLoad = getVehicleCount(Direction.north) + getVehicleCount(Direction.south);
        int westEastTrafficLoad = getVehicleCount(Direction.west) + getVehicleCount(Direction.east);

        if (northSouthTrafficLoad >= westEastTrafficLoad) {
            return Direction.northSouth();
        }

        return Direction.westEast();
    }

    private int getVehicleCount(Direction direction) {
        return roads.get(direction).getVehicleCount();
    }

    private boolean isNeedForChange(List<Direction> directions) {
        return !lightController.isGreenOn(directions.getFirst());
    }

    private void leaveIntersection() {
        for (Vehicle vehicle : vehiclesAtIntersection) {
            stepStatuses.getLast().addVehicle(vehicle);
        }

        vehiclesAtIntersection.clear();
    }

    private void moveVehicles(List<Direction> directions) {
        enterIntersection(directions);
        List<Vehicle> leftVehicles = exitIntersection();

        recordStepStatus(leftVehicles);
    }

    private void enterIntersection(List<Direction> directions) {
        Direction dir1 = directions.get(0);
        Direction dir2 = directions.get(1);

        if (vehiclesAtIntersection.isEmpty()) {
            Vehicle vehicle1 = roads.get(dir1).removeVehicle();
            Vehicle vehicle2 = roads.get(dir2).removeVehicle();

            if (vehicle1 != null) {
                vehiclesAtIntersection.add(vehicle1);
            }
            if (vehicle2 != null) {
                vehiclesAtIntersection.add(vehicle2);
            }

            return;
        }

        Direction opositeDirection = Direction.getOpositeDirection(vehiclesAtIntersection.getFirst().startRoad());
        Vehicle vehicle = roads.get(opositeDirection).removeVehicle();

        if (vehicle != null) {
            vehiclesAtIntersection.add(vehicle);
        }
    }

    private List<Vehicle> exitIntersection() {
        List<Vehicle> leftVehicles = new ArrayList<>();

        if (vehiclesAtIntersection.size() == 1) {
            leftVehicles.add(vehiclesAtIntersection.getFirst());
            vehiclesAtIntersection.clear();
        }

        if (vehiclesAtIntersection.size() == 2) {
            leftVehicles = resolveIntersection();
        }

        return leftVehicles;
    }

    private List<Vehicle> resolveIntersection() {
        Vehicle vehicle1 = vehiclesAtIntersection.get(0);
        Vehicle vehicle2 = vehiclesAtIntersection.get(1);

        boolean priority1 = havePriority(vehicle1);
        boolean priority2 = havePriority(vehicle2);

        if (priority1 && priority2 || !priority1 && !priority2) {
            vehiclesAtIntersection.remove(vehicle1);
            vehiclesAtIntersection.remove(vehicle2);
            return List.of(vehicle1, vehicle2);
        }

        if (priority1) {
            vehiclesAtIntersection.remove(vehicle1);
            return List.of(vehicle1);
        } else {
            vehiclesAtIntersection.remove(vehicle2);
            return List.of(vehicle2);
        }
    }

    private boolean havePriority(Vehicle vehicle) {
        return !Direction.isTurningLeft(vehicle.startRoad(), vehicle.endRoad());
    }

    private void recordStepStatus(List<Vehicle> leftVehicles) {
        stepStatuses.add(new StepStatus(leftVehicles));
    }

    public List<StepStatus> getStepStatuses() {
        return stepStatuses;
    }

    public Map<Direction, Road> getRoads() {
        return roads;
    }

    public List<Vehicle> getVehiclesAtIntersection() {
        return vehiclesAtIntersection;
    }
}
