package project.cyberproton.atom.state;

import project.cyberproton.atom.Platform;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultExpandableStore extends AbstractMutableStore implements ExpandableStore {
    protected DefaultExpandableStore(@NotNull StoreConfiguration configuration) {
        super(configuration);
    }

    protected DefaultExpandableStore() {
        super();
    }

    @Override
    public void update(Collection<Update<?>> updates) {
        try {
            lock.lock();
            internalUpdate(updates);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateSync(@NotNull Collection<Update<?>> updates) {
        try {
            lock.lock();
            if (!Platform.isOnMainThread()) {
                Platform.getBootstrap().getScheduler().sync().execute(() -> internalUpdate(updates));
            } else {
                internalUpdate(updates);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Promise<Void> updateAsync(@NotNull Collection<Update<?>> updates) {
        try {
            lock.lock();
            return Promise.of().start().thenRunAsync(() -> internalUpdate(updates));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> boolean remove(@NotNull TypedKey<T> key) {
        try {
            lock.lock();
            return states.remove(key) != null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            states.clear();
        } finally {
            lock.unlock();
        }
    }
}
