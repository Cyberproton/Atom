package project.cyberproton.atom.resource;

import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.state.Key;

public interface TitleResource extends Resource {
    @NotNull
    Title title();
}
