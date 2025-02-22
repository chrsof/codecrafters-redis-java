package util;

import redis.Command;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class Regex {

    public static final Supplier<Pattern> RESP_SIMPLE = () -> Pattern
            .compile("(?i)^\\+(?<command>(%s))\\r\\n$".formatted(Command.commands()));

    public static final Supplier<Pattern> RESP_BULK = () -> Pattern
            .compile("^\\$[0-9]+\\r\\n(?<data>[a-zA-Z ]*)\\r\\n$");

    public static final Supplier<Pattern> RESP_ARRAYS = () -> Pattern
            .compile("^\\*[0-9]+\\r\\n(?<data>[a-zA-Z0-9 $\\r\\n]*)");

}
