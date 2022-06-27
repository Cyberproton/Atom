package project.cyberproton.atom.database;

public class DatabaseConnectionConfig {
    private final String host;
    private final String port;
    private final String sc;

    public DatabaseConnectionConfig(String host, String port, String sc) {
        this.host = host;
        this.port = port;
        this.sc = sc;
    }
}
