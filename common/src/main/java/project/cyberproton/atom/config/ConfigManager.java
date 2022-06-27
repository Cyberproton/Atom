package project.cyberproton.atom.config;

import org.spongepowered.configurate.CommentedConfigurationNode;

import org.jetbrains.annotations.NotNull;

public interface ConfigManager {
    @NotNull
    CommentedConfigurationNode getDefaultConfig();
}
