package project.cyberproton.atom;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public interface Identifiable {
    @NotNull
    UUID getUniqueId();
}
