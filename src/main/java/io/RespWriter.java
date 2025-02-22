package io;

import redis.Command;
import util.ErrorMessage;

public final class RespWriter {

    public static Reply write(Request request) {
        return switch (request) {
            case Request r when r.getType() == Type.ERROR -> new Reply(Type.ERROR, request.getError());
            case Request r when r.getCommand() == Command.PING -> new Reply(Type.SIMPLE, "PONG");
            case Request r when r.getCommand() == Command.ECHO ->
                    new Reply(Type.SIMPLE, String.join(" ", request.getArguments()));
            default -> new Reply(Type.ERROR, ErrorMessage.ERROR_WRITE_REPLY);
        };
    }

}
