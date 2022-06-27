package project.cyberproton.atom.logging;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.plugin.PluginModule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractLogger implements Logger, PluginModule<AtomPlugin> {
    private final UUID uuid = UUID.randomUUID();
    protected Set<String> tagWhitelist = new HashSet<>();
    protected Set<String> tagBlacklist = new HashSet<>();
    protected TagFilterMode mode = TagFilterMode.BLACKLIST;

    @NotNull
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void onEnable() {
        onReload();
    }

    @Override
    public void onReload() {
        ConfigurationNode node = getPlugin().getAtomConfig().node("logging");
        try {
            mode = TagFilterMode.valueOfOrNull(node.node("mode").getString());
            if (mode == null) {
                mode = TagFilterMode.WHITELIST;
            }
            tagWhitelist = new HashSet<>(node.node("tag-whitelist").getList(String.class, new ArrayList<>()));
            tagBlacklist = new HashSet<>(node.node("tag-blacklist").getList(String.class, new ArrayList<>()));
        } catch (SerializationException ignored) {}
    }

    @Override
    public void onDisable() {

    }

    public enum TagFilterMode {
        WHITELIST,
        BLACKLIST,
        ;

        @Nullable
        public static TagFilterMode valueOfOrNull(@Nullable String name) {
            try {
                return valueOf(name);
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }
}
