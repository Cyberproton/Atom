package project.cyberproton.atom;

import project.cyberproton.atom.module.Module;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class DummyModule1 implements Module {
    private final UUID uuid = UUID.randomUUID();

    @NotNull
    @Override
    public UUID getUUID() {
        return uuid;
    }
}
