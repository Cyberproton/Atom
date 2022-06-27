package project.cyberproton.atom.terminable;

import org.jetbrains.annotations.NotNull;

/**
 * Accepts {@link AutoCloseable}s (and by inheritance {@link Terminable}s),
 * as well as {@link TerminableContainer}s.
 */
@FunctionalInterface
public interface TerminableConsumer {

    /**
     * Binds with the given terminable.
     *
     * @param terminable the terminable to bind with
     * @param <T> the terminable type
     * @return the same terminable
     */
    @NotNull
    <T extends AutoCloseable> T bind(@NotNull T terminable);

    /**
     * Binds with the given terminable container.
     *
     * @param module the module to bind with
     * @param <T> the module type
     * @return the same module
     */
    @NotNull
    default <T extends TerminableContainer> T bindContainer(@NotNull T module) {
        module.setup(this);
        return module;
    }

}
