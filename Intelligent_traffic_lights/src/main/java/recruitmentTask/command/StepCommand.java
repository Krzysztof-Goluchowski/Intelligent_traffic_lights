package recruitmentTask.command;

import recruitmentTask.intersection.IntersectionManager;

public class StepCommand extends Command {
    public StepCommand() {
        super(CommandType.step);
    }

    @Override
    public void execute(IntersectionManager intersectionManager) {
        intersectionManager.step();
    }
}
