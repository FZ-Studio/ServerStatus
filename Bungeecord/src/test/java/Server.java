import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    private final ServerSocket server;

    public Server() throws IOException {
        server = new ServerSocket(235);
    }

    public void start() throws IOException {
        while (true) {
            Socket accept = server.accept();
            ClientHandler clientHandler = new ClientHandler(accept);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    public static class ClientHandler implements Runnable {
        private final Socket socket;

        @SneakyThrows
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @SneakyThrows
        public void run() {
            InputStream in = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println(message);
            }
            socket.close();
        }
    }
}
