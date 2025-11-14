package SE.demo.exception.user;

public class UserDataAccessException extends RuntimeException {
    public UserDataAccessException(String message) {
        super(message);
    }
}
