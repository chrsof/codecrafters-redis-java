package redis;

import config.Environment;
import util.Regex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedisServer {

    public void start() {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            serverChannel.bind(new InetSocketAddress(Environment.PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select() == 0) {
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        accept(selector, key);
                    }

                    if (key.isReadable()) {
                        read(selector, key);
                    }

                    if (key.isWritable()) {
                        write(key);
                    }

                    iterator.remove();
                }
            }
        } catch (IOException ioe) {
            System.err.printf("IOException: %s%n", ioe.getMessage());
        }
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        if (Objects.isNull(channel)) {
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void read(Selector selector, SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(Environment.BUFFER_SIZE);
        if (channel.read(buffer) > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            buffer.clear();
            String message = new String(bytes, StandardCharsets.UTF_8).strip();
            channel.register(selector, SelectionKey.OP_WRITE, message);
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        String message = (String) key.attachment();

        Pattern ping = Regex.PING.get();
        int count = (int) ping.matcher(message).results().count();
        if (count > 0) {
            channel.write(ByteBuffer.wrap("+PONG\r\n".repeat(count).getBytes(StandardCharsets.UTF_8)));
        }

        key.interestOps(SelectionKey.OP_READ);
    }

}
