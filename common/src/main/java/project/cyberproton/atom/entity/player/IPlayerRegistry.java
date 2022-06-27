package project.cyberproton.atom.entity.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.UUID;

public interface IPlayerRegistry {
    @NotNull
    Collection<IPlayer> getAll();

    @Nullable
    IPlayer create(UUID uniqueId);

    @Nullable
    IPlayer get(UUID uniqueId);

    @Nullable
    IPlayer remove(UUID uniqueId);
}
