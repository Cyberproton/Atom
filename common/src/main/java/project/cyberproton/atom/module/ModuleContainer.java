package project.cyberproton.atom.module;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class ModuleContainer<T extends Module> {
    private final T module;
    private Module.Lifecycle lifecycle = Module.Lifecycle.DISABLED;
    private long lastTicked = -1;

    public ModuleContainer(@NotNull T module) {
        Objects.requireNonNull(module, "module");
        this.module = module;
    }

    @NotNull
    public T getModule() {
        return module;
    }

    @NotNull
    public Module.Lifecycle getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(@NotNull Module.Lifecycle lifecycle) {
        Objects.requireNonNull(lifecycle, "state");
        this.lifecycle = lifecycle;
    }

    public long getLastTicked() {
        return lastTicked;
    }

    public void setLastTicked(long lastTicked) {
        this.lastTicked = lastTicked;
    }
}
