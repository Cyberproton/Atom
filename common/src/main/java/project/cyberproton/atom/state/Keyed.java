package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;

public interface Keyed {
    @NotNull
    Key getKey();
}
