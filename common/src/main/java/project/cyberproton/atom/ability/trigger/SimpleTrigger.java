package project.cyberproton.atom.ability.trigger;

import org.jetbrains.annotations.NotNull;

public class SimpleTrigger implements Trigger {
    private final String id;

    public SimpleTrigger(@NotNull String id) {
        this.id = id;
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }
}
