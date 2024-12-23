package recruitmentTask.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CommandReader {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Command> readCommands(String filePath) throws IOException {
        CommandFile commandFile = objectMapper.readValue(new File(filePath), CommandFile.class);
        return commandFile.getCommands();
    }
}
