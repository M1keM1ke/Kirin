package kirin.logger;

import kirin.core.resource.ResourceLoader;
import kirin.logger.settings.LevelSetting;
import kirin.logger.settings.Setting;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class LoggerConfig {
    private LoggerConfig config;

    public LoggerConfig getConfig() {
        return config;
    }

    private Properties properties;
    private final Path systemPropertiesPath = Paths.get("src/main/java/kirin/resources/kl4j.properties");
    private final URL userPropertiesUrl = ClassLoader.getSystemClassLoader().getResource("kl4j.properties");
    private ResourceLoader resourceLoader;
    private Set<Class<? extends Setting>> settingsClasses;
    private List<? extends Setting> propertyInstances;
    private List<? extends Setting> sortedInstances;
    private String resultLog;

    public LoggerConfig() {
        Reflections reflections = new Reflections("");
        settingsClasses = reflections.getSubTypesOf(Setting.class);
        propertyInstances = createPropertyInstances(settingsClasses);

        setupLoggerSettings(systemPropertiesPath);
        setupLoggerConfig();

        if (userPropertiesUrl != null) {
            try {
                setupLoggerSettings(Path.of(userPropertiesUrl.toURI()));
                setupLoggerConfig();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        sortedInstances = propertyInstances.stream()
                .sorted(Comparator.comparing(Setting::getPriority))
                .collect(Collectors.toList());
    }

    public List<? extends Setting> getPropertyInstances() {
        return propertyInstances;
    }

    public List<? extends Setting> initLoggerConfiguration() {
        this.config = new LoggerConfig();
        return config.getPropertyInstances();
    }

    public void complementSettings(LogLevel level, String message) {
        LevelSetting levelSettingProperty = (LevelSetting) sortedInstances.stream()
                .filter(instance -> instance instanceof LevelSetting)
                .findFirst()
                .get();

        levelSettingProperty.setLocalLevel(level);

        try {
            resultLog = formResultLog(sortedInstances) + " " + message;
            System.out.println(resultLog);
        } catch (Exception e) {
            //Nope!
        }
    }

    private void setupLoggerSettings(Path loggerProperties) {
        resourceLoader = new ResourceLoader();
        properties = resourceLoader.parseKl4jProperties(loggerProperties);
    }

    private void setupLoggerConfig() {
        setupSettings(propertyInstances);

    }

    private String formResultLog(List<? extends Setting> sortedInstances) throws Exception {
        StringJoiner joiner = new StringJoiner(" ");
        for (Setting setting : sortedInstances) {
            String attend = setting.attend();
            joiner.add(attend);
        }
        return joiner.toString().replaceAll(" {2}", " ");
    }

    private List<? extends Setting> createPropertyInstances(Set<Class<? extends Setting>> settingsClasses) {
        return settingsClasses.stream()
                .map(entry -> {
                    try {
                        return entry.getDeclaredConstructor().newInstance();
                    } catch (
                            InstantiationException |
                                    IllegalAccessException |
                                    InvocationTargetException |
                                    NoSuchMethodException e
                    ) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    private void setupSettings(List<? extends Setting> settingsInstances) {
        properties
                .forEach((key, value) -> settingsInstances
                        .forEach(instance -> instance.setValue((String) key, (String) value)));
    }
}
