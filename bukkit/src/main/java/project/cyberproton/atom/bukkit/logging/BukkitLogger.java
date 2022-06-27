package project.cyberproton.atom.bukkit.logging;

import project.cyberproton.atom.logging.AbstractLogger;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitLogger extends AbstractLogger {
    private final BukkitPlugin plugin;

    public BukkitLogger(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public BukkitPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void logTagged(@NotNull Level level, @Nullable String tag, @Nullable Object... messages) {
        if (mode == TagFilterMode.WHITELIST && !tagWhitelist.contains(tag) ||
            mode == TagFilterMode.BLACKLIST && tagBlacklist.contains(tag))
        {
            return;
        }
        StringBuilder builder = tag == null ? new StringBuilder() : new StringBuilder("[" + tag + "] ");
        switch (level) {
            case DEBUG:
                builder.append("[D] ");
                break;
            case INFO:
                builder.append("[I] ");
                break;
            case WARNING:
                builder.append("[W] ");
                break;
            case SEVERE:
                builder.append("[S] ");
                break;
        }
        if (messages != null) {
            for (Object message : messages) {
                builder.append(message).append(" ");
            }
        }
        switch (level) {
            case DEBUG:
            case INFO:
                plugin.getLogger().info(builder.toString());
                break;
            case WARNING:
                plugin.getLogger().warning(builder.toString());
                break;
            case SEVERE:
                plugin.getLogger().severe(builder.toString());
                break;
        }
    }
}
