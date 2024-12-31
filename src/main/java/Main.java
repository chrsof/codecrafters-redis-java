import config.Environment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Environment.PORT)) {
            serverSocket.setReuseAddress(true);
            try (Socket clientSocket = serverSocket.accept()) {
                try (OutputStream os = clientSocket.getOutputStream();
                     PrintWriter out = new PrintWriter(os)) {
                    out.write("+PONG\r\n");
                    out.flush();
                }
            }
        } catch (IOException ioe) {
            System.err.printf("IOException: %s%n", ioe.getMessage());
        }
    }

}
