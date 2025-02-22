package io;

import redis.Command;
import util.ErrorMessage;
import util.Regex;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public final class RespReader {

    public static Request read(byte[] bytes) {
        String request = new String(bytes, StandardCharsets.UTF_8);
        return switch (request) {
            case String r when r.startsWith("+") -> parseSimpleRequest(r);
            case String r when r.startsWith("*") -> parseArrayRequest(r);
            default -> Request.builder()
                    .withType(Type.ERROR)
                    .withError(ErrorMessage.ERROR_READ_REQUEST).build();
        };
    }

    private static Request parseSimpleRequest(String request) {
        Matcher matcher = Regex.RESP_SIMPLE.get().matcher(request);
        if (matcher.find()) {
            String command = matcher.group("command");

            return Optional.ofNullable(Command.resolveCommand(command))
                    .map(c -> Request.builder()
                            .withType(Type.SIMPLE)
                            .forCommand(c)
                            .build())
                    .orElse(Request.builder()
                            .withType(Type.ERROR)
                            .withError(ErrorMessage.ERROR_UNKNOWN_COMMAND)
                            .build());
        }

        return Request.builder()
                .withType(Type.ERROR)
                .withError(ErrorMessage.ERROR_NOT_RESP)
                .build();
    }

    private static Request parseArrayRequest(String request) {
        Matcher matcher = Regex.RESP_ARRAYS.get().matcher(request);
        if (matcher.find()) {
            String data = matcher.group("data");

            String[] parts = data.split("((?=\\$))");

            List<String> elements = Arrays.stream(parts)
                    .map(element ->
                            Regex.RESP_BULK.get()
                                    .matcher(element)
                                    .results()
                                    .map(matchResult -> matchResult.group("data"))
                                    .collect(Collectors.joining("")))
                    .toList();

            return Optional.ofNullable(Command.resolveCommand(elements.getFirst()))
                    .map(command -> Request.builder()
                            .withType(Type.ARRAYS)
                            .forCommand(command)
                            .withArguments(elements.stream().skip(1).toList())
                            .build())
                    .orElse(Request.builder()
                            .withType(Type.ERROR)
                            .withError(ErrorMessage.ERROR_UNKNOWN_COMMAND)
                            .build());
        }

        return Request.builder()
                .withType(Type.ERROR)
                .withError(ErrorMessage.ERROR_NOT_RESP)
                .build();
    }

}
