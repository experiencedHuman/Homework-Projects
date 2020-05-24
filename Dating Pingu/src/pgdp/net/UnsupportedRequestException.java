package pgdp.net;

public class UnsupportedRequestException extends RuntimeException{
    private String m;
    public UnsupportedRequestException() {
        super();
    }

    public UnsupportedRequestException(String m) {
        super(m);
        this.m = m;
    }

    public String getMessage() {
        return m;
    }
}
