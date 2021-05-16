package kirin.logger;

public class Klogger {
    private static final Klogger INSTANCE = new Klogger();
    private LoggerConfig config;

    private Klogger() {
        config = new LoggerConfig();
        config.initLoggerConfiguration();
    }

    public static Klogger getLogger() {
        return INSTANCE;
    }

    public void log(LogLevel level, String message) {
        config.complementSettings(level, message);
    }
}
