package javache;

import java.io.IOException;

public class StartUp {
    public static void main(String[] args) {
        Server server = new Server(WebConstants.PORT);
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
