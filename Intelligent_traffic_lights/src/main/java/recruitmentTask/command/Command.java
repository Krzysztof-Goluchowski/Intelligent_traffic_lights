package recruitmentTask.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import recruitmentTask.intersection.IntersectionManager;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StepCommand.class, name = "step"),
        @JsonSubTypes.Type(value = AddVehicleCommand.class, name = "addVehicle")
})
public abstract class Command {
    private CommandType type;

    public Command() {}

    public Command(CommandType type) {
        this.type = type;
    }

    public abstract void execute(IntersectionManager intersectionManager);

    public CommandType getType() {
        return type;
    }
}
