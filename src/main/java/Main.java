import config.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Environment.PORT)) {
            serverSocket.setReuseAddress(true);
            try (Socket clientSocket = serverSocket.accept();
                 InputStream is = clientSocket.getInputStream();
                 Scanner in = new Scanner(is);
                 OutputStream os = clientSocket.getOutputStream();
                 PrintWriter out = new PrintWriter(os)) {
                while (in.hasNextLine()) {
                    if (in.nextLine().contains("PING")) {
                        out.write("+PONG\r\n");
                        out.flush();
                    }
                }
            }
        } catch (IOException ioe) {
            System.err.printf("IOException: %s%n", ioe.getMessage());
        }
    }

}
