package project.cyberproton.atom.config.atom;

import org.jetbrains.annotations.NotNull;

public class DefaultConfigEntry implements ConfigEntry {
    private final String key;
    private final ConfigNode value;

    private DefaultConfigEntry(@NotNull String key, @NotNull ConfigNode value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    @Override
    public String getKey() {
        return key;
    }

    @NotNull
    @Override
    public ConfigNode getValue() {
        return value;
    }

    public static ConfigEntry of(String key, ConfigNode value) {
        return new DefaultConfigEntry(key, value);
    }
}
