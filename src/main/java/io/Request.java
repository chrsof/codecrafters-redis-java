package io;

import redis.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Request {

    private final Type type;
    private final Command command;
    private final List<String> arguments;
    private final String error;

    public Request(Request.Builder builder) {
        Objects.requireNonNull(builder.type);

        this.type = builder.type;
        this.command = builder.command;
        this.arguments = List.copyOf(builder.arguments);
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Type getType() {
        return type;
    }

    public Command getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public String getError() {
        return error;
    }

    public static class Builder {
        private Type type;
        private Command command;
        private List<String> arguments = new ArrayList<>();
        private String error;

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public Builder forCommand(Command command) {
            this.command = command;
            return this;
        }

        public Builder withArguments(List<String> arguments) {
            this.arguments = arguments;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

}