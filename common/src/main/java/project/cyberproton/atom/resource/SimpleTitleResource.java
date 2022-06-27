package project.cyberproton.atom.resource;

import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.state.Key;

import java.util.Objects;

public class SimpleTitleResource implements TitleResource {
    private final Key key;
    private final Title title;

    public SimpleTitleResource(@NotNull Key key, @NotNull Title title) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(title, "title");
        this.key = key;
        this.title = title;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull Title title() {
        return title;
    }
}
