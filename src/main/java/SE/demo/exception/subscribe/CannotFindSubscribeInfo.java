package SE.demo.exception.subscribe;

public class CannotFindSubscribeInfo extends RuntimeException {
    public CannotFindSubscribeInfo(String message) {
        super(message);
    }
}
