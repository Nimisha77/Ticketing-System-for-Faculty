package TSFSBackend.exception;

public class UnauthorizedRequestException extends RuntimeException{
    public UnauthorizedRequestException() {
        super("Unauthorized Request");
    }
}
