package javache.http;

import java.util.HashMap;
import java.util.stream.Collectors;

public class HttpResponseImpl implements HttpResponse {
    private HashMap<String, String> headers;
    private Integer statusCode;
    private byte[] content;

    public HttpResponseImpl() {
        this.headers = new HashMap<>();
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public byte[] getContent() {
        return this.content;
    }

    @Override
    public byte[] getBytes() {
        byte[] status = ("HTTP/1.1 " + this.statusCode.toString() + "OK\r\n").getBytes();

        byte[] header = (this.headers.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() + System.lineSeparator()).getBytes();

        byte[] result = new byte[status.length + header.length + this.content.length];

        System.arraycopy(status, 0, result, 0, status.length);
        System.arraycopy(header, 0, result, status.length, header.length);
        System.arraycopy(content, 0, result, status.length + header.length, content.length);

        return result;
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.putIfAbsent(header, value);
    }
}
