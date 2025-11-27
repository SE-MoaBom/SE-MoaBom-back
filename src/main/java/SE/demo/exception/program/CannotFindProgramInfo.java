package SE.demo.exception.program;

public class CannotFindProgramInfo extends RuntimeException {
    public CannotFindProgramInfo(String message) {
        super(message);
    }
}
