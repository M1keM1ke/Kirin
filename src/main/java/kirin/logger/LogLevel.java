package kirin.logger;

import java.util.List;
import java.util.Optional;

public class LogLevel {
    private String name;
    private int value;

    public static final LogLevel INFO = new LogLevel("INFO", 0);

    public static final LogLevel WARN = new LogLevel("WARN", 100);

    public static final LogLevel ERROR = new LogLevel("ERROR", 200);

    public LogLevel(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    private static final List<LogLevel> standardLevels = List.of(WARN, INFO, ERROR);

    /**
     * @param logLevel the name of the logger level or its value
     * @return logger level
     */
    public static LogLevel findLevel(String logLevel) {
        Optional<LogLevel> levelOptionalByName = standardLevels.stream()
                .filter(level -> level.getName().equals(logLevel.toUpperCase()))
                .findFirst();

        if (levelOptionalByName.isEmpty()) {
            try {
                int currentLevelValue = Integer.parseInt(logLevel);
                Optional<LogLevel> levelOptionalByValue = standardLevels.stream()
                        .filter(level -> level.getValue() == currentLevelValue)
                        .findFirst();

                if (levelOptionalByValue.isEmpty()) {
                    throw new IllegalArgumentException("Unknown level \"" + logLevel + "\"");
                }

                return levelOptionalByValue.get();
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unknown level \"" + logLevel + "\"");
            }
        }
        return levelOptionalByName.get();
    }
}
