package project.cyberproton.atom.gui;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.scheduler.Task;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractGuiManager<P extends AtomPlugin> implements GuiManager<P> {
    private final P plugin;
    private final Map<Class<? extends Gui<?, ?>>, GuiHandler<?>> handlers = new HashMap<>();
    private final Map<UUID, DefaultActiveGui<?>> activeGuis = new HashMap<>();
    private Task task;

    public AbstractGuiManager(P plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        if (task != null) task.stop();
        task = getScheduler().sync().runRepeating(this::tick, 0L, 5L);
    }

    @Override
    public void onDisable() {
        if (task != null) {
            task.stop();
        }
    }

    @NotNull
    @Override
    public P getPlugin() {
        return plugin;
    }

    @Override
    public <G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> void registerHandler(Class<? extends G> cls, GuiHandler<G> handler) {
        if (handlers.containsKey(cls)) {
            throw new IllegalArgumentException("Handler for class " + cls.getSimpleName() + " has already been registered");
        }
        getLog().dt(getClass().getSimpleName(), "Class " + cls.getSimpleName() + " hc " + cls.hashCode());
        getLog().dt(getClass().getSimpleName(), "Class " + cls.getSimpleName() + " hc " + TypeToken.of(cls).hashCode());
        handlers.put(cls, handler);
    }

    @Override
    public <G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> void unregisterHandler(Class<? extends G> cls) {
        if (!handlers.containsKey(cls)) {
            throw new IllegalArgumentException("Handler for class " + cls.getSimpleName() + " has already been registered");
        }
        handlers.remove(cls);
    }

    @Override
    public void update(@NotNull UUID guiUUID) {
        DefaultActiveGui<?> activeGui = activeGuis.get(guiUUID);
        if (activeGui == null) {
            return;
        }
        activeGui.shouldUpdate = true;
    }

    @Override
    public <G extends Gui<?, ?>> void show(@NotNull G gui, @NotNull Collection<UUID> viewers) {
        DefaultActiveGui<?> activeGui = activeGuis.get(gui.getUniqueId());
        if (activeGui == null) {
            activeGui = new DefaultActiveGui<>(this, gui);
            activeGuis.put(gui.getUniqueId(), activeGui);
            gui.onCreate();
        }
        GuiHandler<G> handler = (GuiHandler<G>) resolveHandler(gui.getClass());
        if (handler == null) {
            return;
        }
        handler.show(((DefaultActiveGui<G>) activeGui), viewers);
    }

    @Override
    public <G extends Gui<?, ?>> void close(@NotNull G gui) {
        DefaultActiveGui<?> activeGui = activeGuis.get(gui.getUniqueId());
        if (activeGui == null) {
            return;
        }
        GuiHandler<? extends Gui> handler = resolveHandler(gui.getClass());
        if (handler == null) {
            return;
        }
        handler.close((ActiveGui) activeGui);
        disposeIfNoViewerLeft(activeGui);
    }

    @Override
    public <G extends Gui<?, ?>> void close(@NotNull G gui, @NotNull Collection<UUID> viewers) {
        DefaultActiveGui<?> activeGui = activeGuis.get(gui.getUniqueId());
        if (activeGui == null) {
            return;
        }
        GuiHandler<? extends Gui> handler = resolveHandler(gui.getClass());
        if (handler == null) {
            return;
        }
        handler.close((ActiveGui) activeGui, viewers);
        disposeIfNoViewerLeft(activeGui);
    }

    protected void disposeIfNoViewerLeft(@NotNull ActiveGui<?> activeGui) {
        GuiHandler<? extends Gui> handler = resolveHandler(activeGui.gui().getClass());
        if (handler == null) {
            return;
        }
        if (handler.viewersOf((ActiveGui) activeGui).isEmpty()) {
            activeGuis.remove(activeGui.getUniqueId());
            activeGui.gui().onDispose();
        }
    }

    /*
    @Override
    public void show(@NotNull ActiveGui<?> activeGui, @NotNull Collection<UUID> viewers) {
        if (!activeGuis.containsKey(activeGui.getUniqueId())) {
            activeGuis.put(activeGui.getUniqueId(), activeGui);
            activeGui.gui().onCreate();
        }
        GuiHandler<?> handler = resolveHandler(activeGui.gui().getClass());
        if (handler == null) {
            return;
        }
        handler.show((ActiveGui) activeGui, viewers);
    }

    @Override
    public void close(@NotNull ActiveGui<?> activeGui, @NotNull Collection<UUID> viewers) {
        if (!activeGuis.containsKey(activeGui.getUniqueId())) {
            return;
        }
        GuiHandler<?> handler = handlers.get(activeGui.gui().getClass());
        if (handler == null) {
            return;
        }
        handler.close((ActiveGui) activeGui, viewers);
        disposeIfNoViewerLeft(activeGui);
    }

     */

    private void tick() {
        for (DefaultActiveGui<?> activeGui : activeGuis.values()) {
            if (!activeGui.shouldUpdate) {
                continue;
            }
            GuiHandler<?> handler = resolveHandler(activeGui.gui.getClass());
            if (handler == null) {
                return;
            }
            try {
                handler.update((ActiveGui) activeGui);
            } catch (Exception ex) {
                getLog().wt("GuiManager", "An exception has happened while updating gui " + activeGui.gui.getClass() + "@" + activeGui.gui.getUniqueId());
                ex.printStackTrace();
            }
            activeGui.shouldUpdate = false;
        }
    }

    private GuiHandler<?> resolveHandler(Class<? extends Gui> guiCls) {
        Class<?> cls = guiCls;
        GuiHandler<?> handler = handlers.get(guiCls);
        while (handler == null && guiCls != Gui.class) {
            cls = cls.getSuperclass();
            handler = handlers.get(cls);
        }
        return handler;
    }

    private static class DefaultActiveGui<G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> implements ActiveGui<G> {
        private final UUID uuid;
        private final GuiManager guiManager;
        private final G gui;
        private boolean shouldUpdate;

        private DefaultActiveGui(GuiManager guiManager, G gui) {
            this.guiManager = guiManager;
            this.gui = gui;
            this.uuid = gui.getUniqueId();
        }

        @NotNull
        @Override
        public UUID getUniqueId() {
            return uuid;
        }

        @Override
        public G gui() {
            return gui;
        }

        @Override
        public void show(UUID viewerUUID) {
            guiManager.show(this.gui, viewerUUID);
        }

        @Override
        public void close() {
            guiManager.close(this.gui);
        }
    }
}
