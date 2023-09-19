package ab.control;

public class FatalError extends RuntimeException {
    public FatalError(Exception e) {
        super(e);
    }
}
