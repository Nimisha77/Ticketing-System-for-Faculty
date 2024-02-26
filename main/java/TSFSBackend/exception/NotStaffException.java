package TSFSBackend.exception;

public class NotStaffException extends RuntimeException{
    public NotStaffException(String username) {super(username + " is not a staff and cannot be assigned a ticket");}
}
