package recruitmentTask.simulation;

import recruitmentTask.controller.IntersectionController;
import recruitmentTask.command.Command;
import recruitmentTask.road.Direction;
import recruitmentTask.road.Road;
import recruitmentTask.vehicle.Vehicle;
import recruitmentTask.vehicle.VehicleFactory;

import java.util.*;

public class Simulation {
    private final IntersectionController intersectionController;
    private Map<Direction, Road> roads;
    private final List<List<String>> stepStatuses;
    private final List<Command> commands;
    private final List<Vehicle> vehiclesAtTheIntersection = new ArrayList<>();

    public Simulation(List<Command> commands) {
        this.commands = commands;
        intersectionController = new IntersectionController();
        stepStatuses = new ArrayList<>();
        initializeRoads();
    }

    private void initializeRoads() {
        roads = new HashMap<>();
        roads.put(Direction.north, new Road(Direction.north));
        roads.put(Direction.south, new Road(Direction.south));
        roads.put(Direction.west, new Road(Direction.west));
        roads.put(Direction.east, new Road(Direction.east));
    }

    public void run() {
        for (Command command : commands) {
            switch (command.getCommandType()) {
                case step -> step();
                case addVehicle -> addVehicle(command);
            }
        }
    }

    private void step() {
        List<Direction> busyRoads = getBusyRoads();
        boolean changingLight = isNeedForChangingLight(busyRoads.getFirst());

        if (changingLight) {
            intersectionController.handleTraffic(busyRoads);
            leaveIntersection();
        }

        List<String> leftVehicles = moveVehicles(busyRoads);

        recordStepStatus(leftVehicles);
    }

    private List<Direction> getBusyRoads() {
        int northSouthTrafficLoad = getVehicleCount(Direction.north) + getVehicleCount(Direction.south);
        int westEastTrafficLoad = getVehicleCount(Direction.west) + getVehicleCount(Direction.east);

        if (northSouthTrafficLoad > westEastTrafficLoad) {
            return Direction.northSouth();
        }

        return Direction.westEast();
    }

    private int getVehicleCount(Direction direction) {
        return roads.get(direction).getVehicleCount();
    }

    private boolean isNeedForChangingLight(Direction direction) {
        return !intersectionController.isGreenOn(direction);
    }

    private void leaveIntersection() {
        for (Vehicle vehicle : vehiclesAtTheIntersection) {
            stepStatuses.getLast().add(vehicle.vehicleId());
        }

        vehiclesAtTheIntersection.clear();
    }

    private List<String> moveVehicles(List<Direction> directions) {
        List<String> leftVehicles = new ArrayList<>();

        enterIntersection(directions);
        leftVehicles.addAll(exitIntersection());

        return leftVehicles;
    }

    private List<String> exitIntersection() {
        List<String> leftVehicles = new ArrayList<>();

        if (vehiclesAtTheIntersection.size() == 1) {
            leftVehicles.add(vehiclesAtTheIntersection.getFirst().vehicleId());
            vehiclesAtTheIntersection.clear();
        }

        if (vehiclesAtTheIntersection.size() == 2) {
            leftVehicles.addAll(resolveIntersection());
        }

        return leftVehicles;
    }

    private List<String> resolveIntersection() {
        Vehicle vehicle1 = vehiclesAtTheIntersection.get(0);
        Vehicle vehicle2 = vehiclesAtTheIntersection.get(1);

        boolean priority1 = havePriority(vehicle1);
        boolean priority2 = havePriority(vehicle2);

        if (priority1 && priority2 || !priority1 && !priority2) {
            vehiclesAtTheIntersection.remove(vehicle1);
            vehiclesAtTheIntersection.remove(vehicle2);
            return List.of(vehicle1.vehicleId(), vehicle2.vehicleId());
        }

        if (priority1) {
            vehiclesAtTheIntersection.remove(vehicle1);
            return List.of(vehicle1.vehicleId());
        } else {
            vehiclesAtTheIntersection.remove(vehicle2);
            return List.of(vehicle2.vehicleId());
        }
    }

    private void enterIntersection(List<Direction> directions) {
        Direction dir1 = directions.get(0);
        Direction dir2 = directions.get(1);

        if (vehiclesAtTheIntersection.isEmpty()) {
            Vehicle vehicle1 = roads.get(dir1).removeVehicle();
            Vehicle vehicle2 = roads.get(dir2).removeVehicle();

            if (vehicle1 != null) {
                vehiclesAtTheIntersection.add(vehicle1);
            }
            if (vehicle2 != null) {
                vehiclesAtTheIntersection.add(vehicle2);
            }

            return;
        }

        Direction opositeDirection = getOpositeDirection(vehiclesAtTheIntersection.getFirst().startRoad());
        Vehicle vehicle = roads.get(opositeDirection).removeVehicle();

        if (vehicle != null) {
            vehiclesAtTheIntersection.add(vehicle);
        }
    }

    private Direction getOpositeDirection(Direction direction) {
        return Direction.getOpositeDirection(direction);
    }

    private boolean havePriority(Vehicle vehicle) {
        return !Direction.isTurningLeft(vehicle.startRoad(), vehicle.endRoad());
    }

    private void recordStepStatus(List<String> leftVehicles) {
        stepStatuses.add(leftVehicles);
    }

    private void addVehicle(Command command) {
        Road road = roads.get(command.getStartRoad());
        Vehicle vehicle = VehicleFactory.create(command);
        road.addVehicle(vehicle);
    }

    public Map<Direction, Road> getRoads() {
        return roads;
    }

    public List<List<String>> getStepStatuses() {
        return stepStatuses;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<Vehicle> getVehiclesAtTheIntersection() {
        return vehiclesAtTheIntersection;
    }

    public IntersectionController getIntersectionController() {
        return intersectionController;
    }
}