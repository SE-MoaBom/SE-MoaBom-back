package SE.demo.exception.user;

public class PasswordNotEqualException extends RuntimeException {
    public PasswordNotEqualException(String message) {
        super(message);
    }
}
