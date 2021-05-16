package kirin.core.config;

import kirin.core.annotation.ComponentScan;
import kirin.logger.Klogger;
import kirin.logger.LogLevel;
import org.reflections.Reflections;

import java.util.Set;

public class JavaConfiguration implements Configuration {
    public JavaConfiguration() {}

    @Override
    public String[] getPackagesToScan() {
        Reflections reflections = new Reflections("");
        Set<Class<?>> componentScan = reflections.getTypesAnnotatedWith(ComponentScan.class);

        if (componentScan.size() != 1) {
            Klogger.getLogger().log(LogLevel.ERROR, "ComponentScan must be used once");
        }

        return componentScan.iterator().next().getAnnotation(ComponentScan.class).packageToScan();
    }
}
