package project.cyberproton.atom.resource;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.state.Key;

public interface Resource {
    @NotNull
    Key key();
}
