package util;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class Regex {

    public static final Supplier<Pattern> PING = () -> Pattern.compile("(PING)");

}
