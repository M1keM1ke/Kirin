package kirin.core.resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ResourceLoader {
    private Map<String, String> propertiesMap;

    public ResourceLoader() {
    }

    public Properties parseProperties(Path path) {
        Properties properties = new Properties();

        try {
            InputStream is = new FileInputStream(path.toFile());
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public Properties parseKl4jProperties(Path path) {
        Properties properties = parseProperties(path);
        Map<String, String> refactorProperties = properties.entrySet().stream()
                .map(entry -> {
                    String newKey = entry.getKey().toString()
                            .replaceAll("logger.", "");
                    return new String[]{newKey, (String) entry.getValue()};
                })
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));

        Properties kl4jProperties = new Properties();
        kl4jProperties.putAll(refactorProperties);

        return kl4jProperties;
    }
}
