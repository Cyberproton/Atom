package project.cyberproton.atom.ability.trigger;

import org.jetbrains.annotations.NotNull;

public class RepeatingTrigger implements Trigger {
    private final int interval;

    public RepeatingTrigger(int interval) {
        this.interval = interval;
    }

    @NotNull
    @Override
    public String getId() {
        return "repeating";
    }

    public int getInterval() {
        return interval;
    }
}
