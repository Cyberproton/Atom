package project.cyberproton.atom.state;

import project.cyberproton.atom.Platform;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public class DefaultNonExpandableStore extends AbstractMutableStore implements MutableStore {
    public DefaultNonExpandableStore(@NotNull StoreConfiguration configuration) {
        super(configuration);
    }

    public DefaultNonExpandableStore() {
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
    protected void internalUpdate(@NotNull Collection<Update<?>> updates) {
        Map<UUID, Map<TypedKey<?>, Change<?>>> changes = new LinkedHashMap<>();
        Map<UUID, StateListener> lst = new HashMap<>();
        for (Update<?> update : updates) {
            TypedKey<?> key = update.getKey();
            if (!states.containsKey(key) && interceptors.containsKey(key)) {
                continue;
            }
            Interceptor<?> interceptor = interceptors.get(update.getKey());
            Value<?> current = interceptor == null || interceptor.getOnGet() == null ? get(update.getKey()) : interceptor.getOnGet().get();
            Value<?> next = update.getNext();
            boolean shouldNotifyListeners = true;
            boolean reflectChangesOnStore = true;


            if (interceptor != null) {
                if (!interceptor.allowNullValue() && next.getOrNull() == null) {
                    throw new IllegalArgumentException("Updated value for key " + key.getKey() + " must not be null");
                }
                Interceptor.SetContext context = new Interceptor.SetContext(key, current, next);
                if (interceptor.getOnBeforeSet() != null) {
                    interceptor.getOnBeforeSet().accept(context);
                }
                if (interceptor.getOnSet() != null) {
                    next = interceptor.getOnSet().apply(context);
                }
                if (interceptor.getOnAfterSet() != null) {
                    interceptor.getOnAfterSet().accept(context);
                }
                shouldNotifyListeners = context.shouldNotifyListener();
                reflectChangesOnStore = context.reflectChangesOnStore();
            }

            if (reflectChangesOnStore) {
                if (Objects.equals(current.getOrNull(), next.getOrNull())) {
                    continue;
                }
                states.put(key, next);
            }

            if (!shouldNotifyListeners) {
                return;
            }
            Change<?> change = Change.of((TypedKey<Object>) key, current, next);
            for (StateListener listener : globalListeners.values()) {
                changes.computeIfAbsent(listener.getUniqueId(), k -> new HashMap<>()).put(change.getKey(), change);
                lst.put(listener.getUniqueId(), listener);
            }
            Map<UUID, StateListener> listenersOfKey = listeners.get(key);
            if (listenersOfKey != null) {
                for (StateListener listener : listenersOfKey.values()) {
                    changes.computeIfAbsent(listener.getUniqueId(), k -> new HashMap<>()).put(change.getKey(), change);
                    lst.put(listener.getUniqueId(), listener);
                }
            }
        }
        for (Map.Entry<UUID, Map<TypedKey<?>, Change<?>>> entry : changes.entrySet()) {
            lst.get(entry.getKey()).onStateChange(entry.getValue());
        }
    }
}
