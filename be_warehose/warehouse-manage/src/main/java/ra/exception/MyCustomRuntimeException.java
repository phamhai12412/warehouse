package ra.exception;

public class MyCustomRuntimeException extends RuntimeException {
    public MyCustomRuntimeException(String message) {
        super(message);
    }
}
