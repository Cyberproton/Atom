package project.cyberproton.atom.exception;

public class InvalidDoubleRangeException extends IllegalArgumentException {

    public InvalidDoubleRangeException(String what) {
        super("Invalid double range: " + what);
    }

}
