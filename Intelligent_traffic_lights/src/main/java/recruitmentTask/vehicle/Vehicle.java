package recruitmentTask.vehicle;

import recruitmentTask.road.Direction;

public record Vehicle(String vehicleId,
                      Direction startRoad,
                      Direction endRoad) {
}
