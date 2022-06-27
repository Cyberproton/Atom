package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;

public class ImmutableStore extends AbstractStore {
    public ImmutableStore(@NotNull StoreConfiguration configuration) {
        super(configuration);
    }
}
