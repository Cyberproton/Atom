package project.cyberproton.atom.exception;

public class PromiseChainException extends AtomException {

    public PromiseChainException(Throwable cause) {
        super("promise chain", cause);
    }

}

