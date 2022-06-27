package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;

public class SimpleExpiringModifier<M extends Modifier<?>> implements ExpiringModifier<M> {
    private final M modifier;
    private final long expiredAt;

    SimpleExpiringModifier(M modifier, long durationMillis) {
        this.modifier = modifier;
        this.expiredAt = System.currentTimeMillis() + durationMillis;
    }

    @Override
    public @NotNull M getModifier() {
        return modifier;
    }

    @Override
    public boolean hasExpired() {
        return System.currentTimeMillis() >= expiredAt;
    }
}
