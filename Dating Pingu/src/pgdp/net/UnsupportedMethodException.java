package pgdp.net;

public class UnsupportedMethodException extends RuntimeException{
    private String m;
    public UnsupportedMethodException() {
        super();
    }

    public UnsupportedMethodException(String m) {
        super(m);
        this.m = m;
    }

    public String getMessage() {
        return m;
    }
}
