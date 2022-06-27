package project.cyberproton.atom.scheduler.builder;

import project.cyberproton.atom.scheduler.Scheduler;
import project.cyberproton.atom.scheduler.Task;

import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * Queues execution of tasks using {@link Scheduler}, often combining parameters with
 * variables already known by this instance.
 */
public interface ContextualTaskBuilder {

    @NotNull
    Task consume(@NotNull Consumer<Task> consumer);

    @NotNull
    Task run(@NotNull Runnable runnable);

}
