package project.cyberproton.atom.bukkit;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.BaseAtom;
import project.cyberproton.atom.GlobalConfig;
import project.cyberproton.atom.Platform;
import project.cyberproton.atom.ability.AbilityManager;
import project.cyberproton.atom.bukkit.entity.BukkitEntityManager;
import project.cyberproton.atom.bukkit.event.Events;
import project.cyberproton.atom.bukkit.gui.BukkitGuiManager;
import project.cyberproton.atom.bukkit.listener.DamageSystemListener;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.bukkit.world.BukkitWorldManager;
import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.gui.GuiManager;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.stat.StatManager;
import project.cyberproton.atom.world.WorldManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class BukkitAtom extends BukkitPlugin implements Atom {
    private final BaseAtom delegate = new BaseAtom();
    private BukkitEntityManager entityRegistry;
    private BukkitWorldManager worldManager;
    private BukkitGuiManager guiManager;
    private BukkitCommandManager commandManager;
    private BukkitAudiences audiences;
    private GlobalConfig globalConfig;

    @Override
    public @NotNull GlobalConfig globalConfig() {
        return globalConfig;
    }

    @Override
    public void onPluginLoad() {
        getLog().it("Atom", "Loading ATOM...");
        injectPlatformProvider();
        if (Platform.getServerVersion().isLowerThan(1, 13)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        delegate.load();
        getLog().it("Atom", "Done loading.");
    }

    private void loadConfig() {
        File atomDir = getMetadata().getDataPath().resolve("atom").toFile();
        if (!atomDir.exists()) {
            atomDir.mkdirs();
        }
        File configFile = atomDir.toPath().resolve("atom.yml").toFile();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            CommentedConfigurationNode root = YamlConfigurationLoader
                .builder()
                .file(configFile)
                .build()
                .load();
            globalConfig = root.get(GlobalConfig.class);
            Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Testing");
            Atom.getInstance().getLog().dt(getClass().getSimpleName(), globalConfig.test());
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPluginEnable() {
        getLog().i("Starting ATOM...");
        loadConfig();
        delegate.enable();
        audiences = BukkitAudiences.create(this);
        entityRegistry = new BukkitEntityManager(this);
        entityRegistry.register();
        worldManager = new BukkitWorldManager(this);
        worldManager.register();
        guiManager = new BukkitGuiManager(this);
        guiManager.register();
        commandManager = new BukkitCommandManager(this);
        setupCommands();
        modifyAttributes();
        Events.listen(new DamageSystemListener(), this);
        getLog().i("ATOM ready.");
    }

    @Override
    public void onPluginReload() {
        getLog().i("Reloading ATOM...");
        delegate.reload();
        loadConfig();
        getLog().i("ATOM ready.");
    }

    @Override
    public void onPluginDisable() {
        getLog().i("Disabling ATOM...");
        delegate.disable();
        audiences.close();
        getLog().i("ATOM disabled");
    }

    @Override
    public @NotNull AudienceProvider audiences() {
        return audiences;
    }

    @NotNull
    @Override
    public BukkitEntityManager getEntityManager() {
        return entityRegistry;
    }

    @NotNull
    @Override
    public StatManager getStatManager() {
        return delegate.getStatManager();
    }

    @NotNull
    @Override
    public AbilityManager getAbilityManager() {
        return delegate.getAbilityManager();
    }

    @NotNull
    @Override
    public WorldManager getWorldManager() {
        return worldManager;
    }

    @NotNull
    @Override
    public GuiManager<? extends AtomPlugin> getGuiManager() {
        return guiManager;
    }

    private void injectPlatformProvider() {
        try {
            Field providerField = Class
                    .forName("project.cyberproton.atom.Platform")
                    .getDeclaredField("provider");
            providerField.setAccessible(true);
            providerField.set(null, new BukkitPlatformProvider(this));
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupCommands() {
        commandManager.enableUnstableAPI("help");
        commandManager.getCommandContexts().registerContext(IPlayer.class, (it) -> {
            IPlayer player = Atom.getInstance().getEntityManager().getPlayer(it.popFirstArg());
            if (player == null) {
                throw new InvalidCommandArgument("&eSuprise");
            }
            return player;
        });
        commandManager.registerCommand(new Commands());
    }

    private void modifyAttributes() {

    }
}
