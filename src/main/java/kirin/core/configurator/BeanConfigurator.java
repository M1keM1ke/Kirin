package kirin.core.configurator;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public interface BeanConfigurator {
    /**
     * Find implementation class for current field
     *  or throw exception
     *
     * @param field interface
     * @param <T>
     * @return found Class
     */
    <T> Class<? extends T> findImplementationClass(Field field);

    List<Object> findInterfaceImplementations(Type type);

    Reflections getScanner();
}
