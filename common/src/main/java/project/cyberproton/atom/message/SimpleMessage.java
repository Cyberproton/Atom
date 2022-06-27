package project.cyberproton.atom.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleMessage implements Message {
    @Override
    public @NotNull String id() {
        return null;
    }

    @Override
    public @Nullable String defaultMessage() {
        return null;
    }

    @Override
    public int code() {
        return 0;
    }

    @Override
    public @NotNull Type type() {
        return null;
    }
}
