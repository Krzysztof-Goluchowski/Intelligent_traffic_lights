package recruitmentTask.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import recruitmentTask.road.Direction;

public class Command {
    @JsonProperty("type")
    private CommandType commandType;

    private String vehicleId;
    private Direction startRoad;
    private Direction endRoad;

    public CommandType getCommandType() {
        return commandType;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public Direction getStartRoad() {
        return startRoad;
    }

    public Direction getEndRoad() {
        return endRoad;
    }
}
