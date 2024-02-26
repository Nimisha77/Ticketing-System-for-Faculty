package TSFSBackend.exception;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException() {
        super("Input field cannot be null or empty");
    }
}
