package project.cyberproton.atom.exception;

public class SchedulerTaskException extends AtomException {
    public SchedulerTaskException(Throwable cause) {
        super("scheduler task", cause);
    }
}
