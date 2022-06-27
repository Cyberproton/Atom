package project.cyberproton.atom.promise;

import project.cyberproton.atom.Platform;

/**
 * Represents the two main types of {@link Thread} on the server.
 */
public enum ThreadContext {

    /**
     * Represents the main "server" thread
     */
    SYNC,

    /**
     * Represents anything which isn't the {@link #SYNC} thread.
     */
    ASYNC;

    public static ThreadContext ofCurrentThread() {
        return Platform.isOnMainThread() ? SYNC : ASYNC;
    }

}
