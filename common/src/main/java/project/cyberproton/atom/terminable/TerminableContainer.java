package project.cyberproton.atom.terminable;

import org.jetbrains.annotations.NotNull;

/**
 * A terminable container is a class which manipulates and constructs a number
 * of {@link Terminable}s.
 */
public interface TerminableContainer {

    /**
     * Performs the tasks to setup this module
     *
     * @param consumer the terminable consumer
     */
    void setup(@NotNull TerminableConsumer consumer);

    /**
     * Registers this terminable with a terminable consumer
     *
     * @param consumer the terminable consumer
     */
    default void bindModuleWith(@NotNull TerminableConsumer consumer) {
        consumer.bindContainer(this);
    }

}
