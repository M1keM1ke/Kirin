package kirin.reflection;

import kirin.logger.Klogger;
import kirin.logger.LogLevel;

import java.lang.reflect.InvocationTargetException;

public class KReflections {

    public static <T> T newInstance(Class<?> clazz, Class<?>...parameterTypes) {
        try {
           return (T) clazz.getDeclaredConstructor().newInstance(parameterTypes);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Klogger.getLogger().log(LogLevel.ERROR, String.format("Error when creating new instance of %s with parameters:%s", clazz, parameterTypes));
            e.printStackTrace();
        }
        return null;
    }
}
