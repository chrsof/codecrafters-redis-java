package io;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public record Reply(
        Type type,
        String reply
) {

    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(serializeReply().getBytes(StandardCharsets.UTF_8));
    }

    private String serializeReply() {
        if (Objects.isNull(reply)) {
            return "";
        }
        return switch (type) {
            case ERROR -> "-%s\r\n".formatted(reply);
            case SIMPLE -> "+%s\r\n".formatted(reply);
            default ->
                    throw new UnsupportedOperationException("Reply serialization of type %s is not implemented.".formatted(type.name()));
        };
    }

}
