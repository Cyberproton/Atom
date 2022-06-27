package project.cyberproton.atom.config.atom;

import org.jetbrains.annotations.NotNull;

public interface ConfigEntry {
    static ConfigEntry of(String key, ConfigNode value) {
        return DefaultConfigEntry.of(key, value);
    }

    @NotNull
    String getKey();

    @NotNull
    ConfigNode getValue();
}
