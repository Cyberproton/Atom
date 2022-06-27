package project.cyberproton.atom;

import project.cyberproton.atom.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Log {
    public static void log(@NotNull Logger.Level level, @Nullable String tag, @Nullable Object... messages) {
        Atom.getInstance().getLog().logTagged(level, tag, messages);
    }

    public static void info(@Nullable String tag, @Nullable Object... messages) {
        log(Logger.Level.INFO, tag, messages);
    }

    public static void i(@Nullable String tag, @Nullable Object... messages) {
        info(tag, messages);
    }

    public static void debug(@Nullable String tag, @Nullable Object... messages) {
        log(Logger.Level.DEBUG, tag, messages);
    }

    public static void d(@Nullable String tag, @Nullable Object... messages) {
        debug(tag, messages);
    }

    public static void warning(@Nullable String tag, @Nullable Object... messages) {
        log(Logger.Level.WARNING, tag, messages);
    }

    public static void w(@Nullable String tag, @Nullable Object... messages) {
        warning(tag, messages);
    }

    public static void severe(@Nullable String tag, @Nullable Object... messages) {
        log(Logger.Level.SEVERE, tag, messages);
    }

    public static void s(@Nullable String tag, @Nullable Object... messages) {
        severe(tag, messages);
    }
}
