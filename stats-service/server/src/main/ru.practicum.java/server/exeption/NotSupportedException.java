package server.exeption;

public class NotSupportedException extends RuntimeException {
    public NotSupportedException(String message) {
        super(message);
    }
}
