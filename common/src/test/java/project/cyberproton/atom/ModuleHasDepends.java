package project.cyberproton.atom;

import com.google.common.collect.Lists;
import project.cyberproton.atom.module.Module;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public class ModuleHasDepends implements Module {
    private final UUID uuid = UUID.randomUUID();

    @NotNull
    @Override
    public List<Class<?>> getDependencies() {
        return Lists.newArrayList(DummyModule1.class, DummyModule2.class);
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return uuid;
    }
}
