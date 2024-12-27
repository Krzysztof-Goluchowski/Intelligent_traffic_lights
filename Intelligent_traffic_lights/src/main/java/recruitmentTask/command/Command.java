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

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setStartRoad(Direction startRoad) {
        this.startRoad = startRoad;
    }

    public void setEndRoad(Direction endRoad) {
        this.endRoad = endRoad;
    }
}
