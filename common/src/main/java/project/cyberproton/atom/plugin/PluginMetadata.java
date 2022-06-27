package project.cyberproton.atom.plugin;

import java.nio.file.Path;

public class PluginMetadata {
    private final Path path;
    private final Path dataPath;
    private final Path databasePath;

    public PluginMetadata(Path path, Path dataPath, Path databasePath) {
        this.path = path;
        this.dataPath = dataPath;
        this.databasePath = databasePath;
    }

    public Path getPath() {
        return path;
    }

    public Path getDataPath() {
        return dataPath;
    }

    public Path getDatabasePath() {
        return databasePath;
    }
}
