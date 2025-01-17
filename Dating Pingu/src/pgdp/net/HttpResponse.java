package pgdp.net;

public final class HttpResponse {

    private HttpStatus status;
    private String body;

    public HttpResponse(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
    }

    @Override
    public String toString() {
        return "HTTP/2.0 "+status.getCode()+" "+status.getText()+"\r\n\r\n"+body;
    }
}
