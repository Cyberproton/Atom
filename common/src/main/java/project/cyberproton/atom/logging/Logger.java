package project.cyberproton.atom.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Logger {
    void logTagged(@NotNull Level level, @Nullable String tag, @Nullable Object... messages);

    default void log(@NotNull Level level, @Nullable Object @Nullable... messages) {
        logTagged(level, null, messages);
    }

    default void infoTagged(@Nullable String tag, @Nullable Object @Nullable... messages) {
        logTagged(Level.INFO, tag, messages);
    }

    default void info(@Nullable Object @Nullable... messages) {
        log(Level.INFO, messages);
    }

    default void it(@Nullable String tag, @Nullable Object @Nullable... messages) {
        infoTagged(tag, messages);
    }

    default void i(@Nullable Object @Nullable... messages) {
        info(messages);
    }

    default void debugTagged(@Nullable String tag, @Nullable Object @Nullable... messages) {
        logTagged(Level.DEBUG, tag, messages);
    }

    default void debug(@Nullable Object @Nullable... messages) {
        log(Level.DEBUG, messages);
    }

    default void dt(@Nullable String tag, @Nullable Object @Nullable... messages) {
        debugTagged(tag, messages);
    }

    default void d(@Nullable Object @Nullable... messages) {
        debug(messages);
    }

    default void warningTagged(@Nullable String tag, @Nullable Object @Nullable... messages) {
        logTagged(Level.WARNING, tag, messages);
    }

    default void warning(@Nullable Object @Nullable... messages) {
        log(Level.WARNING, messages);
    }

    default void wt(@Nullable String tag, @Nullable Object @Nullable... messages) {
        warningTagged(tag, messages);
    }

    default void w(@Nullable Object @Nullable... messages) {
        warning(messages);
    }

    default void severeTagged(@Nullable String tag, @Nullable Object @Nullable... messages) {
        logTagged(Level.SEVERE, tag, messages);
    }

    default void severe(@Nullable Object @Nullable... messages) {
        log(Level.SEVERE, messages);
    }

    default void st(@Nullable String tag, @Nullable Object @Nullable... messages) {
        severeTagged(tag, messages);
    }

    default void s(@Nullable Object @Nullable... messages) {
        severe(messages);
    }

    enum Level {
        INFO,
        DEBUG,
        WARNING,
        SEVERE,
    }
}
