package project.cyberproton.atom.modifier;

import project.cyberproton.atom.util.Ticks;

import java.util.concurrent.TimeUnit;

public class TemporaryModifier<M extends Modifier<?>> {
    private final M modifier;
    private final long ticks;

    private TemporaryModifier(M modifier, long ticks) {
        this.modifier = modifier;
        this.ticks = ticks;
    }

    public M getModifier() {
        return modifier;
    }

    public long getTicks() {
        return ticks;
    }

    public static <M extends Modifier<?>> TemporaryModifier<M> of(M modifier, long ticks) {
        return new TemporaryModifier<>(modifier, ticks);
    }

    public static <M extends Modifier<?>> TemporaryModifier<M> of(M modifier, long duration, TimeUnit unit) {
        return new TemporaryModifier<>(modifier, Ticks.from(duration, unit));
    }
}
