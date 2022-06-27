package project.cyberproton.atom.exception;

import project.cyberproton.atom.Platform;
import project.cyberproton.atom.util.Delegate;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Central handler for exceptions that occur within user-written
 * Runnables and handlers running in helper.
 */
public final class Exceptions {
    private Exceptions() {}

    private static final ThreadLocal<AtomicBoolean> NOT_TODAY_STACK_OVERFLOW_EXCEPTION =
            ThreadLocal.withInitial(() -> new AtomicBoolean(false));

    private static void log(AtomException exception) {
        // print to logger
        Platform.getAtom().getLog().severeTagged(exception.getMessage(), exception);
        exception.printStackTrace();

        // call event
        AtomicBoolean firing = NOT_TODAY_STACK_OVERFLOW_EXCEPTION.get();
        if (firing.compareAndSet(false, true)) {
            try {
                //Events.call(new HelperExceptionEvent(exception));
            } finally {
                firing.set(false);
            }
        }
    }

    public static <R> R runCatchingSilently(Callable<R> callable) {
        try {
            return callable.call();
        } catch (Exception ex) {
            return null;
        }
    }

    public static void runCatchingSilently(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ignored) {}
    }

    public static void reportScheduler(Throwable throwable) {
        log(new SchedulerTaskException(throwable));
    }

    public static void reportPromise(Throwable throwable) {
        log(new PromiseChainException(throwable));
    }

    public static void reportEvent(Object event, Throwable throwable) {
        log(new EventHandlerException(throwable, event));
    }

    public static Runnable wrapSchedulerTask(Runnable runnable) {
        return new SchedulerWrappedRunnable(runnable);
    }

    private static final class SchedulerWrappedRunnable implements Runnable, Delegate<Runnable> {
        private final Runnable delegate;

        private SchedulerWrappedRunnable(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                this.delegate.run();
            } catch (Throwable t) {
                reportScheduler(t);
            }
        }

        @Override
        public Runnable getDelegate() {
            return this.delegate;
        }
    }

}
