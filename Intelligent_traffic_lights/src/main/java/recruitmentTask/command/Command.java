package recruitmentTask.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import recruitmentTask.road.Direction;

public class Command {
    @JsonProperty("type")
    private CommandType commandType;

    private String vehicleId;
    private Direction startRoad;
    private Direction endRoad;

    public Command() {}

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public Command(CommandType commandType, String vehicleId, Direction startRoad, Direction endRoad) {
        this.commandType = commandType;
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
    }

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
