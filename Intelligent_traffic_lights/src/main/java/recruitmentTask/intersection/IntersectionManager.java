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
    private int currentAxisGreenDuration;
    private List<Direction> currentAxis;

    public IntersectionManager(TrafficLightController lightController, List<StepStatus> stepStatuses) {
        this.lightController = lightController;
        this.stepStatuses = stepStatuses;
        this.roads = initializeRoads();
        currentAxisGreenDuration = 0;
        this.currentAxis = Direction.northSouth();
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
            List<Direction> perpendicularRoads = Direction.getPerpendicularRoads(currentAxis);
            lightController.handleTraffic(perpendicularRoads);

            currentAxisGreenDuration = 0;
            currentAxis = perpendicularRoads;

            clearIntersection();
        }
        moveVehicles(currentAxis);

        currentAxisGreenDuration++;
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
        if (currentAxisGreenDuration >= 9) {
            return true;
        }

        return !lightController.isGreenOn(directions.getFirst());
    }

    private void clearIntersection() {
        for (Vehicle vehicle : vehiclesAtIntersection) {
            stepStatuses.getLast().addVehicle(vehicle);
        }

        vehiclesAtIntersection.clear();
    }

    private void moveVehicles(List<Direction> directions) {
        enterIntersection(directions);
        exitIntersection();
    }

    private void enterIntersection(List<Direction> directions) {
        Direction dir1 = directions.get(0);
        Direction dir2 = directions.get(1);

        if (vehiclesAtIntersection.isEmpty()) {
            addVehiclesFromRoad(dir1);
            addVehiclesFromRoad(dir2);
        } else {
            addVehicleFromOppositeDirection();
        }
    }

    private void addVehiclesFromRoad(Direction direction) {
        Vehicle vehicle = roads.get(direction).removeVehicle();
        addVehicleToIntersection(vehicle);
    }

    private void addVehicleFromOppositeDirection() {
        Direction oppositeDirection = Direction.getOpositeDirection(vehiclesAtIntersection.getFirst().startRoad());
        Vehicle vehicle = roads.get(oppositeDirection).removeVehicle();
        addVehicleToIntersection(vehicle);
    }

    private void addVehicleToIntersection(Vehicle vehicle) {
        if (vehicle != null) {
            vehiclesAtIntersection.add(vehicle);
        }
    }


    private void exitIntersection() {
        List<Vehicle> exitedVehicles = new ArrayList<>();

        if (vehiclesAtIntersection.size() == 1) {
            exitedVehicles.add(vehiclesAtIntersection.getFirst());
            vehiclesAtIntersection.clear();
        } else if (vehiclesAtIntersection.size() == 2) {
            exitedVehicles = resolveIntersection();
        }

        recordStepStatus(exitedVehicles);
    }

    private List<Vehicle> resolveIntersection() {
        Vehicle vehicle1 = vehiclesAtIntersection.get(0);
        Vehicle vehicle2 = vehiclesAtIntersection.get(1);

        boolean priority1 = havePriority(vehicle1);
        boolean priority2 = havePriority(vehicle2);

        if (priority1 && priority2 || !priority1 && !priority2) {
            vehiclesAtIntersection.clear();
            return List.of(vehicle1, vehicle2);
        }

        return priority1 ? removeVehicle(vehicle1) : removeVehicle(vehicle2);
    }

    private List<Vehicle> removeVehicle(Vehicle vehicle) {
        vehiclesAtIntersection.remove(vehicle);
        return List.of(vehicle);
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
