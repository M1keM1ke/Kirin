package kirin.logger.settings;

import kirin.logger.AnsiColor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StacktraceSetting extends Setting {
    private String name = "stacktrace.enable";
    private boolean stacktraceEnable;
    private static final String LOGGER_CLASS_NAME = "kirin.logger.Klogger";
    private static final String METHOD_LOGGER_NAME = "log";
    private String color;

    @Override
    public String attend() {
        String stacktraceMessage = StackWalker
                .getInstance()
                .walk((Stream<StackWalker.StackFrame> frames) -> {
                    List<StackWalker.StackFrame> stacktraceList = frames.collect(Collectors.toList());
                    StackWalker.StackFrame initiallyCalledFrame = null;

                    for (int i = 0; i < stacktraceList.size(); i++) {
                        StackWalker.StackFrame preFrame = stacktraceList.get(i);
                        if (isLogMethodFrame(preFrame)) {
                            initiallyCalledFrame = stacktraceList.get(i + 1);
                            break;
                        }
                    }

                    return String.format("%s.%s(%s:%s)",
                            initiallyCalledFrame.getClassName(), initiallyCalledFrame.getMethodName(),
                            initiallyCalledFrame.getFileName(), initiallyCalledFrame.getLineNumber());
                });
        return stacktraceEnable ? setColor(stacktraceMessage) : "";
    }

    private String setColor(String value) {
        return color + value + AnsiColor.RESET;
    }

    private boolean isLogMethodFrame(StackWalker.StackFrame preFrame) {
        return preFrame.getClassName().equals(LOGGER_CLASS_NAME) && preFrame.getMethodName().equals(METHOD_LOGGER_NAME);
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
            case "stacktrace.enable":
                stacktraceEnable = Boolean.parseBoolean(value);
                break;
            case "stacktrace.priority":
                priority = Integer.parseInt(value);
                break;
            case "stacktrace.color":
                Optional<String> colorByName = new AnsiColor()
                        .findColorByName(value).or(() -> new AnsiColor().findColorByValue(value));
                color = colorByName.orElseThrow(RuntimeException::new);
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
