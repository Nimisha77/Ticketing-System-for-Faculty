package TSFSBackend.exception;

public class NotFacultyException extends RuntimeException {
    public NotFacultyException(String username) {super(username + " is not a faculty member and cannot create ticket");}
}
