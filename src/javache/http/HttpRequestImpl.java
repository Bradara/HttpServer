package javache.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestImpl implements HttpRequest {

    private String requestUrl;
    private String method;
    private HashMap<String, String> headers;
    private HashMap<String, String> bodyParameters;

    public HttpRequestImpl(String requestContent) {
        this.bodyParameters = new HashMap<>();
        this.headers = new HashMap<>();
        this.init(requestContent);
    }

    private void init(String requestContent) {
        String[] requestLines = requestContent.split("\r?\n");
        this.setMethod(requestLines[0].split("\\s+")[0]);
        this.setRequestUrl(requestLines[0].split("\\s+")[1]);
        int indexOfParams = Integer.MAX_VALUE;

        for (int i = 1; i < requestLines.length; i++) {
            if (requestLines[i].isEmpty())
                indexOfParams = i+1;

            if (i < indexOfParams-1){
                String[] line = requestLines[i].split(": ");
                this.addHeader(line[0], line[1]);
            } else{
                Arrays.stream(requestLines[indexOfParams].split("&")).forEach(l->{
                    String[] split = l.split("=");
                    this.addBodyParameters(split[0], split[1]);
                });
            }

        }

    }


    @Override
    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public HashMap<String, String> getBodyParameters() {
        return this.getBodyParameters();
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getRequestUrl() {
        return this.requestUrl;
    }

    @Override
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.putIfAbsent(header, value);
    }

    @Override
    public void addBodyParameters(String parameter, String value) {
        this.bodyParameters.putIfAbsent(parameter, value);
    }

    @Override
    public boolean isResources() {



        return this.requestUrl.contains(".");

    }
}
