package redis;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Command {
    PING("PING"),
    ECHO("ECHO");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static String commands() {
        return Arrays.stream(Command.values())
                .map(Command::getCommand)
                .collect(Collectors.joining("|"));
    }

    public static Command resolveCommand(String command) {
        return Arrays.stream(Command.values())
                .filter(cmd -> cmd.command.equalsIgnoreCase(command))
                .findFirst()
                .orElse(null);
    }
}
