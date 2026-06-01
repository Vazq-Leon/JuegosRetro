package juegosretro.db;

public final class DatabaseConfig {
    private DatabaseConfig() {
    }

    public static String getHost() {
        return getSetting("juegosretro.db.host", "JUEGOSRETRO_DB_HOST", "localhost");
    }

    public static String getPort() {
        return getSetting("juegosretro.db.port", "JUEGOSRETRO_DB_PORT", "3306");
    }

    public static String getDatabaseName() {
        return getSetting("juegosretro.db.name", "JUEGOSRETRO_DB_NAME", "juegos_retro_db");
    }

    public static String getUser() {
        return getSetting("juegosretro.db.user", "JUEGOSRETRO_DB_USER", "root");
    }

    public static String getPassword() {
        return getSetting("juegosretro.db.password", "JUEGOSRETRO_DB_PASSWORD", "");
    }

    public static String getServerUrl() {
        return String.format(
                "jdbc:mysql://%s:%s/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                getHost(),
                getPort()
        );
    }

    public static String getDatabaseUrl() {
        return String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                getHost(),
                getPort(),
                getDatabaseName()
        );
    }

    private static String getSetting(String propertyKey, String envKey, String defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value != null && !value.isBlank()) {
            return value;
        }
        value = System.getenv(envKey);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return defaultValue;
    }
}
