package project.cyberproton.atom.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Message {
    @NotNull
    String id();

    @Nullable
    String defaultMessage();

    int code();

    @NotNull
    Type type();

    enum Type {
        DEFAULT,
        INFO,
        SUCCESS,
        WARNING,
        ERROR,
        ;

        @Nullable
        public static Type getType(@NotNull String name) {
            try {
                return valueOf(name);
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }
}
