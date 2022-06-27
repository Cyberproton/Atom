package project.cyberproton.atom.gui.pane;

import project.cyberproton.atom.Identifiable;
import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public interface Pane extends Identifiable {
    static Pane single(@Nullable Element element) {
        if (element == null) return empty();
        return single(Position.zero(), element);
    }

    static Pane single(@NotNull Position position, @Nullable Element element) {
        Objects.requireNonNull(position, "position");
        if (element == null) return empty();
        return new Pane() {
            private final UUID uuid = UUID.randomUUID();

            @NotNull
            @Override
            public Map<Position, Element> onBuild() {
                return Collections.singletonMap(position, element);
            }

            @NotNull
            @Override
            public UUID getUniqueId() {
                return uuid;
            }
        };
    }

    static Pane empty() {
        return new Pane() {
            private final UUID uuid = UUID.randomUUID();

            @NotNull
            @Override
            public Map<Position, Element> onBuild() {
                return new HashMap<>();
            }

            @NotNull
            @Override
            public UUID getUniqueId() {
                return uuid;
            }
        };
    }

    @Nullable
    Map<Position, Element> onBuild();
}
