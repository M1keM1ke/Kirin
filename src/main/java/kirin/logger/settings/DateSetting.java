package kirin.logger.settings;

import kirin.logger.AnsiColor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class DateSetting extends Setting {
    private String name = "date.enable";
    private String datePattern;
    private String enable;
    private String color;

    @Override
    public String attend() {
        boolean isDateEnable = Boolean.parseBoolean(enable);

        if (isDateEnable) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
            return setColor(simpleDateFormat.format(new Date()));
        }
        return "";
    }

    private String setColor(String value) {
        return color + value + AnsiColor.RESET;
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
            case "date.enable":
                enable = value;
                break;
            case "date.pattern":
                datePattern = value;
                break;
            case "date.priority":
                priority = Integer.parseInt(value);
                break;
            case "date.color":
                Optional<String> colorByName = new AnsiColor()
                        .findColorByName(value).or(() -> new AnsiColor().findColorByValue(value));
                color = colorByName.orElseThrow(RuntimeException::new);
                break;
        }
    }

    @Override
    public String getValue() {
        return this.value;
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
