package project.cyberproton.atom.inject;

import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class ImmutableInjector extends AbstractInjector {
    public ImmutableInjector(@Nullable AbstractInjector parent, @NotNull Config config, @Nullable AtomPlugin plugin, @NotNull List<Provider<?>> initialProviders) {
        super(parent, config, plugin, initialProviders);
    }
}
