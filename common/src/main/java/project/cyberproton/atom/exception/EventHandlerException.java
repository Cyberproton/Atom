package project.cyberproton.atom.exception;

public class EventHandlerException extends AtomException {

    public EventHandlerException(Throwable cause, Object event) {
        super("event handler for " + event.getClass().getName(), cause);
    }

}
