package TSFSBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String GT_username) {
        super("User with GT_username " + GT_username + " does not exist");
    }
}
