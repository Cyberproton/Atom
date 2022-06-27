package project.cyberproton.atom.world;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.plugin.PluginModule;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractWorldManager<P extends AtomPlugin> implements WorldManager, PluginModule<P> {
    private final P plugin;
    private final UUID uuid = UUID.fromString("8674f292-1df0-40f1-bf8c-adc78fc26c26");
    private final Graph<UUID, DefaultEdge> linkedWorlds = new SimpleGraph<>(DefaultEdge.class);

    public AbstractWorldManager(@NotNull P plugin) {
        Objects.requireNonNull(plugin, "plugin");
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @NotNull
    @Override
    public P getPlugin() {
        return plugin;
    }

    @Override
    public void linkWorlds(@NotNull Collection<World> worlds) {
        for (World world : worlds) {
            linkedWorlds.addVertex(world.getUniqueId());
        }
        for (World world : worlds) {
            for (World w : worlds) {
                linkedWorlds.addEdge(world.getUniqueId(), w.getUniqueId());
            }
        }
    }
}
