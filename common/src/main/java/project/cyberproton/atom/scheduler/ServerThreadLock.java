package project.cyberproton.atom.scheduler;

import project.cyberproton.atom.Platform;
import project.cyberproton.atom.terminable.Terminable;

/**
 * A tool to synchronize code with the main server thread
 *
 * <p>It is highly recommended to use this interface with try-with-resource blocks.</p>
 *
 * @see project.cyberproton.atom.promise.ThreadContext#SYNC
 */
public interface ServerThreadLock extends Terminable {

    /**
     * Blocks the current thread until a {@link ServerThreadLock} can be obtained.
     *
     * <p>Will attempt to return immediately if the calling thread is the main thread itself.</p>
     *
     * @return a lock
     */
    static ServerThreadLock obtain() {
        return new DefaultServerThreadLock(Platform.getBootstrap().getScheduler());
    }

    /**
     * Closes the lock, and allows the main thread to continue
     */
    @Override
    void close();

}
