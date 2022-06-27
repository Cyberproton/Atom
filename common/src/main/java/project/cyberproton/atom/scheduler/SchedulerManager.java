package project.cyberproton.atom.scheduler;

import project.cyberproton.atom.promise.PromiseBuilder;
import project.cyberproton.atom.promise.ThreadContext;

public interface SchedulerManager {
    /**
     * Gets a scheduler for the given context.
     *
     * @param context the context
     * @return a scheduler
     */
    default Scheduler get(ThreadContext context) {
        switch (context) {
            case SYNC:
                return sync();
            case ASYNC:
                return async();
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a "sync" scheduler, which executes tasks on the main server thread.
     *
     * @return a sync executor instance
     */
    Scheduler sync();

    /**
     * Returns an "async" scheduler, which executes tasks asynchronously.
     *
     * @return an async executor instance
     */
    Scheduler async();

    /**
     * Returns a {@link PromiseBuilder} instance.
     *
     * @return a promise builder
     */
    PromiseBuilder promise();
}
