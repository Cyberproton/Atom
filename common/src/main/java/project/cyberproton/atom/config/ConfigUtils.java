package project.cyberproton.atom.config;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.logging.Logger;
import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class ConfigUtils {
    public static BiFunction<ConfigurationNode, Class<?>, List<?>> EMPTY = (n, c) -> new ArrayList<>();

    public static <E> BiFunction<ConfigurationNode, Class<E>, List<E>> emptyListHandler() {
        return (n, c) -> new ArrayList<>();
    }

    @NotNull
    public static List<ConfigurationNode> loadAllFromDirectory(@NotNull AtomPlugin plugin, @NotNull String path) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(path, "path");
        String trimmed = path.replace("/", File.separator).trim();
        if (!trimmed.isEmpty() && trimmed.charAt(0) == File.separatorChar) {
            trimmed = trimmed.substring(1);
        }
        if (!trimmed.isEmpty() && trimmed.charAt(trimmed.length() - 1) == File.separatorChar) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        File folder = plugin.getMetadata().getDataPath().resolve(trimmed).toFile();
        if (!folder.exists() || !folder.isDirectory()) {
            return new ArrayList<>();
        }
        return loadAllFromDirectory(folder);
    }

    @NotNull
    public static List<ConfigurationNode> loadAllFromDirectory(@NotNull File folder) {
        Objects.requireNonNull(folder, "folder");
        if (!folder.exists() || !folder.isDirectory()) {
            return new ArrayList<>();
        }
        List<ConfigurationNode> nodes = new ArrayList<>();
        for (File file : folder.listFiles()) {
            try {
                nodes.add(
                        YamlConfigurationLoader
                        .builder()
                        .path(file.toPath())
                        .build()
                        .load()
                );
            } catch (ConfigurateException ex) {
                Atom.getInstance().getLog().logTagged(Logger.Level.WARNING, "ConfigUtils", "Failed to load file " + file.getAbsolutePath());
                ex.printStackTrace();
            }
        }
        return nodes;
    }

    @Nullable
    public static <E> List<E> getListOrNull(ConfigurationNode node, Class<E> elementCls) {
        try {
            return node.getList(elementCls);
        } catch (SerializationException ex) {
            return null;
        }
    }

    @Nullable
    public static <E> List<E> getListOrElse(ConfigurationNode node, Class<E> elementCls, BiFunction<ConfigurationNode, Class<E>, List<E>> elseHandler) {
        try {
            List<E> lst = node.getList(elementCls);
            if (lst == null) {
                return elseHandler.apply(node, elementCls);
            }
            return lst;
        } catch (SerializationException ex) {
            return elseHandler.apply(node, elementCls);
        }
    }

    @NotNull
    public static <E> List<E> getListOrEmpty(ConfigurationNode node, Class<E> elementCls) {
        try {
            List<E> lst = node.getList(elementCls);
            if (lst == null) {
                return new ArrayList<>();
            }
            return lst;
        } catch (SerializationException ex) {
            return new ArrayList<>();
        }
    }
}
