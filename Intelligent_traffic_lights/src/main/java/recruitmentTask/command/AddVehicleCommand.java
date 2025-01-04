package recruitmentTask.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import recruitmentTask.intersection.IntersectionManager;
import recruitmentTask.road.Direction;

public class AddVehicleCommand extends Command {

    private final String vehicleId;

    private final Direction startRoad;

    private final Direction endRoad;

    @JsonCreator
    public AddVehicleCommand(
            @JsonProperty("vehicleId") String vehicleId,
            @JsonProperty("startRoad") Direction startRoad,
            @JsonProperty("endRoad") Direction endRoad
    ) {
        super(CommandType.addVehicle);
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
    }

    @Override
    public void execute(IntersectionManager intersectionManager) {
        intersectionManager.addVehicle(this);
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
