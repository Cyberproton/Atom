package project.cyberproton.atom.exception;

public class DoubleRangeFormatException extends IllegalArgumentException {

    public DoubleRangeFormatException(String format) {
        super("Invalid double range format " + format);
    }

}
