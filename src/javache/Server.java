package javache;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.FutureTask;

public class Server {
    private ServerSocket server;
    private int port;
    private long count;

    public Server(int port) {
        //this.run();
        this.port = port;
        this.count = 0;
    }

    public void run() throws IOException {
        this.server = new ServerSocket(this.port);
        this.server.setSoTimeout(WebConstants.SOCKET_TIMEOUT_MILLISECONDS);
        System.out.println("Server run at port: " + this.port);

        while (true) {
            try (Socket clientSocket = this.server.accept()) {
                clientSocket.setSoTimeout(WebConstants.SOCKET_TIMEOUT_MILLISECONDS);

                ConnectionHandler connectionHandler =
                        new ConnectionHandler(clientSocket, new RequestHandler());

                FutureTask<?> task = new FutureTask<>(connectionHandler, null);
                task.run();

            } catch (SocketTimeoutException e) {
                System.out.println(this.count++ + " Timeout(s) detected!");
            }
        }

    }
}
