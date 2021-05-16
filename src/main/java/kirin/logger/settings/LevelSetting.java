package kirin.logger.settings;

import kirin.logger.AnsiColor;
import kirin.logger.LogLevel;

import java.util.Optional;

public class LevelSetting extends Setting {
    private String name = "level";
    private LogLevel globalLevel;
    private LogLevel localLevel;
    private String globalColor;
    private String infoColor;
    private String warnColor;
    private String errorColor;

    public void setLocalLevel(LogLevel localLevel) {
        this.localLevel = localLevel;
    }

    @Override
    public String attend() throws Exception {
        if (localLevel.getValue() < globalLevel.getValue()) {
            throw new Exception();
        }


        return setColor(localLevel.getName());
    }

    private String setColor(String value) {
        if (globalColor != null) {
            return globalColor + value + AnsiColor.RESET;
        }
        switch (localLevel.getName()) {
            case "INFO":
                return infoColor != null
                        ? infoColor + value + AnsiColor.RESET : globalColor != null
                        ? globalColor + value + AnsiColor.RESET : value;
            case "WARN":
                return warnColor != null
                        ? warnColor + value + AnsiColor.RESET : globalColor != null
                        ? globalColor + value + AnsiColor.RESET : value;
            case "ERROR":
                return errorColor != null
                        ? errorColor + value + AnsiColor.RESET : globalColor != null
                        ? globalColor + value + AnsiColor.RESET : value;

            default:
                return value;
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setValue(String name, String value) {
        switch (name) {
            case "level":
                globalLevel = LogLevel.findLevel(value);
                break;
            case "level.priority":
                priority = Integer.parseInt(value);
                break;
            case "level.color.global":
                Optional<String> colorByName = new AnsiColor()
                        .findColorByName(value).or(() -> new AnsiColor().findColorByValue(value));
                globalColor = colorByName.orElseThrow(RuntimeException::new);
                break;
            case "level.color.info":
                Optional<String> colorByNameInfo = new AnsiColor()
                        .findColorByName(value).or(() -> new AnsiColor().findColorByValue(value));
                infoColor = colorByNameInfo.orElseThrow(RuntimeException::new);
                break;
            case "level.color.warn":
                Optional<String> colorByNameWarn = new AnsiColor()
                        .findColorByName(value).or(() -> new AnsiColor().findColorByValue(value));
                warnColor = colorByNameWarn.orElseThrow(RuntimeException::new);
                break;
            case "level.color.error":
                Optional<String> colorByNameError = new AnsiColor()
                        .findColorByName(value).or(() -> new AnsiColor().findColorByValue(value));
                errorColor = colorByNameError.orElseThrow(RuntimeException::new);
                break;
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public Integer getPriority() {
        return priority;
    }
}
