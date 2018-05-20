package javache;

import javache.http.HttpRequest;
import javache.http.HttpRequestImpl;
import javache.http.HttpResponse;
import javache.http.HttpResponseImpl;

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

        if(requestUrl.equals("/")) requestUrl = "/index";

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
            this.httpResponse.addHeader("Content-Type", type);
            this.httpResponse.addHeader("Content-Disposition", "inline");
            this.httpResponse.setStatusCode(200);
            this.httpResponse.setContent(Files.readAllBytes(pathToRes));
        } catch (IOException e) {
            Path path404 = Paths.get(WebConstants.PAGE_FOLDER + "/404.html");
            this.httpResponse.setStatusCode(404);
            try {
                this.httpResponse.setContent(Files.readAllBytes(path404));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return this.httpResponse.getBytes();
    }


}
