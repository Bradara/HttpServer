package javache;

import javache.io.Reader;
import javache.io.Writer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private Socket clientSocket;
    private RequestHandler requestHandler;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ConnectionHandler(Socket clientSocket, RequestHandler requestHandler) {
        this.initConnection(clientSocket);
        this.requestHandler = requestHandler;
    }

    private void initConnection(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            String requestContent = Reader.readAllLines(this.inputStream);
            byte[] responseContent = this.requestHandler.handleRequest(requestContent);
            Writer.writeBytes(responseContent, this.outputStream);
            this.inputStream.close();
            this.outputStream.close();
            this.clientSocket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
