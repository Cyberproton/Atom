package project.cyberproton.atom.bukkit.event;

import com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import project.cyberproton.atom.bukkit.event.functional.merged.MergedSubscriptionBuilder;
import project.cyberproton.atom.bukkit.event.functional.single.SingleSubscriptionBuilder;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * A functional event listening utility.
 */
public final class Events {

    /**
     * Makes a SingleSubscriptionBuilder for a given event
     *
     * @param eventClass the class of the event
     * @param <T>        the event type
     * @return a {@link SingleSubscriptionBuilder} to construct the event handler
     * @throws NullPointerException if eventClass is null
     */
    @NotNull
    public static <T extends Event> SingleSubscriptionBuilder<T> subscribe(@NotNull Class<T> eventClass) {
        return SingleSubscriptionBuilder.newBuilder(eventClass);
    }

    /**
     * Makes a SingleSubscriptionBuilder for a given event
     *
     * @param eventClass the class of the event
     * @param priority   the priority to listen at
     * @param <T>        the event type
     * @return a {@link SingleSubscriptionBuilder} to construct the event handler
     * @throws NullPointerException if eventClass or priority is null
     */
    @NotNull
    public static <T extends Event> SingleSubscriptionBuilder<T> subscribe(@NotNull Class<T> eventClass, @NotNull EventPriority priority) {
        return SingleSubscriptionBuilder.newBuilder(eventClass, priority);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a given super type
     *
     * @param handledClass the super type of the event handler
     * @param <T>          the super type class
     * @return a {@link MergedSubscriptionBuilder} to construct the event handler
     */
    @NotNull
    public static <T> MergedSubscriptionBuilder<T> merge(@NotNull Class<T> handledClass) {
        return MergedSubscriptionBuilder.newBuilder(handledClass);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a given super type
     *
     * @param type the super type of the event handler
     * @param <T>  the super type class
     * @return a {@link MergedSubscriptionBuilder} to construct the event handler
     */
    @NotNull
    public static <T> MergedSubscriptionBuilder<T> merge(@NotNull TypeToken<T> type) {
        return MergedSubscriptionBuilder.newBuilder(type);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link MergedSubscriptionBuilder} to construct the event handler
     */
    @NotNull
    @SafeVarargs
    public static <S extends Event> MergedSubscriptionBuilder<S> merge(@NotNull Class<S> superClass, @NotNull Class<? extends S>... eventClasses) {
        return MergedSubscriptionBuilder.newBuilder(superClass, eventClasses);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param priority     the priority to listen at
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link MergedSubscriptionBuilder} to construct the event handler
     */
    @NotNull
    @SafeVarargs
    public static <S extends Event> MergedSubscriptionBuilder<S> merge(@NotNull Class<S> superClass, @NotNull EventPriority priority, @NotNull Class<? extends S>... eventClasses) {
        return MergedSubscriptionBuilder.newBuilder(superClass, priority, eventClasses);
    }

    /**
     * Submit the event on the current thread
     *
     * @param event the event to call
     * @return the event
     */
    public static Event call(@NotNull Event event) {
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static void listen(@NotNull Listener listener, @NotNull Plugin plugin) {
        Objects.requireNonNull(listener, "listener");
        Objects.requireNonNull(plugin, "plugin");
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    private Events() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}
