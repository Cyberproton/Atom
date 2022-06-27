package project.cyberproton.atom.state;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.state.listener.BoundStateListener;
import project.cyberproton.atom.state.listener.ConcreteStateListener;
import project.cyberproton.atom.state.listener.MultipleStateListener;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractMutableStore extends AbstractStore implements MutableStore {
    protected final Map<TypedKey<?>, Map<UUID, StateListener>> listeners = new HashMap<>();
    protected final Map<UUID, StateListener> globalListeners = new HashMap<>();
    protected final Map<UUID, StateListener> allListeners = new HashMap<>();

    protected AbstractMutableStore(@NotNull StoreConfiguration configuration) {
        super(configuration);
    }

    protected AbstractMutableStore() {
        super();
    }

    protected void internalUpdate(@NotNull Collection<Update<?>> updates) {
        Map<UUID, Map<TypedKey<?>, Change<?>>> changes = new LinkedHashMap<>();
        Map<UUID, StateListener> lst = new HashMap<>();
        for (Update<?> update : updates) {
            TypedKey<?> key = update.getKey();
            Interceptor<?> interceptor = interceptors.get(update.getKey());
            Value<?> current = interceptor == null ? get(update.getKey()) : interceptor.getOnGet().get();
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

    protected void internalUpdate(@NotNull Update<?> @NotNull ... updates) {
        internalUpdate(Arrays.asList(updates));
    }

    protected void notifyListeners(@NotNull Collection<@NotNull Change<?>> changeList) {
        Map<UUID, Map<TypedKey<?>, Change<?>>> changes = new LinkedHashMap<>();
        Map<UUID, StateListener> lst = new HashMap<>();
        for (Change<?> change : changeList) {
            for (StateListener listener : globalListeners.values()) {
                changes.computeIfAbsent(listener.getUniqueId(), k -> new HashMap<>()).put(change.getKey(), change);
                lst.put(listener.getUniqueId(), listener);
            }
            Map<UUID, StateListener> listenersOfKey = listeners.get(change.getKey());
            if (listenersOfKey != null) {
                for (StateListener listener : listenersOfKey.values()) {
                    changes.computeIfAbsent(listener.getUniqueId(), k -> new HashMap<>()).put(change.getKey(), change);
                    lst.put(listener.getUniqueId(), listener);
                }
            }
        }
        for (Map.Entry<UUID, Map<TypedKey<?>, Change<?>>> entry : changes.entrySet()) {
            try {
                lst.get(entry.getKey()).onStateChange(entry.getValue());
            } catch (Exception ex) {
                Atom.getInstance().getLog().wt("Store", "An exception has happened whilst listening to changes on listener " + lst.get(entry.getKey()).getUniqueId());
                ex.printStackTrace();
            }
        }
    }

    protected void notifyListeners(@NotNull Change<?> @NotNull... changes) {
        notifyListeners(Arrays.asList(changes));
    }

    @NotNull
    @Override
    public BoundStateListener subscribe(@NotNull StateListener listener) {
        Objects.requireNonNull(listener, "listener");
        try {
            lock.lock();
            BoundStateListener boundStateListener = BoundStateListener.bind(listener, this);
            if (listener instanceof ConcreteStateListener<?>) {
                Map<UUID, StateListener> lst = listeners
                        .computeIfAbsent(((ConcreteStateListener<?>) listener).getKey(), k -> new HashMap<>());
                lst.put(listener.getUniqueId(), listener);
            } else if (listener instanceof MultipleStateListener) {
                for (TypedKey<?> key : ((MultipleStateListener) listener).getKeys()) {
                    Map<UUID, StateListener> lst = listeners
                        .computeIfAbsent(key, k -> new HashMap<>());
                    lst.put(listener.getUniqueId(), listener);
                }
            } else {
                globalListeners.put(listener.getUniqueId(), listener);
            }
            allListeners.put(listener.getUniqueId(), listener);
            return boundStateListener;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void unsubscribe(@NotNull StateListener listener) {
        Objects.requireNonNull(listener, "listener");
        try {
            lock.lock();
            if (!allListeners.containsKey(listener.getUniqueId())) {
                return;
            }
            if (listener instanceof ConcreteStateListener<?>) {
                if (!listeners.containsKey(((ConcreteStateListener<?>) listener).getKey())) {
                    return;
                }
                Map<UUID, StateListener> lst = listeners
                        .get(((ConcreteStateListener<?>) listener).getKey());
                lst.remove(listener.getUniqueId());
            } else if (listener instanceof MultipleStateListener) {
                for (TypedKey<?> key : ((MultipleStateListener) listener).getKeys()) {
                    if (!listeners.containsKey(key)) {
                        return;
                    }
                    Map<UUID, StateListener> lst = listeners
                        .get(key);
                    lst.remove(listener.getUniqueId());
                }
            } else {
                globalListeners.remove(listener.getUniqueId());
            }
            allListeners.remove(listener.getUniqueId());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void unsubscribe(@NotNull UUID listenerUUID) {
        Objects.requireNonNull(listenerUUID, "listenerUUID");
        try {
            lock.lock();
            StateListener stateListener = allListeners.get(listenerUUID);
            if (stateListener == null) {
                return;
            }
            unsubscribe(stateListener);
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    @Override
    public <T> State<T> stateOf(TypedKey<T> key, @Nullable T initialState) {
        DefaultState<T> state = new DefaultState<>(key, this);
        update(Update.of(state.getKey(), Value.of(initialState)));
        return state;
    }

    @NotNull
    @Override
    public <T> State<T> stateOf(@NotNull TypeToken<T> type, @Nullable T initialState) {
        DefaultState<T> state = new DefaultState<>(type, this);
        update(Update.of(state.getKey(), Value.of(initialState)));
        return state;
    }
}
