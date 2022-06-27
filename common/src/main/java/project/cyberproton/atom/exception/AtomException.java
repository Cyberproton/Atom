package project.cyberproton.atom.exception;

public class AtomException extends RuntimeException {
    public AtomException(String what, Throwable cause) {
        super("Exception occurred while " + what, cause);
    }
}
