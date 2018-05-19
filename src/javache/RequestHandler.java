package javache;

import javache.http.HttpRequest;
import javache.http.HttpRequestImpl;
import javache.http.HttpResponse;
import javache.http.HttpResponseImpl;
import javache.io.Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestHandler {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;


    public byte[] handleRequest(String requestContent) {
        this.httpRequest = new HttpRequestImpl(requestContent);
        this.httpResponse = new HttpResponseImpl();
        String requestUrl = this.httpRequest.getRequestUrl();

        if (this.httpRequest.isResources()) {
            String path = WebConstants.RESOURCE_FOLDER + requestUrl;
            return getResource(path);
        } else {
            String path = WebConstants.PAGE_FOLDER + requestUrl + WebConstants.EXTENSION;
            return getResource(path);
        }
    }

    private byte[] getResource(String path) {
        Path pathToRes = Paths.get(path);

        try {
            String type = Files.probeContentType(pathToRes);
            httpResponse.addHeader("Content-Type", type);
            httpResponse.setStatusCode(200);
            httpResponse.setContent(Files.readAllBytes(pathToRes));
        } catch (IOException e) {
            Path path404 = Paths.get(WebConstants.PAGE_FOLDER + "/404.html");
            httpResponse.setStatusCode(404);
            try {
                httpResponse.setContent(Files.readAllBytes(path404));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return httpResponse.getBytes();
    }


}
