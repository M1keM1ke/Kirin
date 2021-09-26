package kirin.core.configurator;

import kirin.core.annotation.Primary;
import kirin.core.annotation.Qualifier;
import kirin.core.context.ApplicationContext;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaBeanConfigurator implements BeanConfigurator {
    private final Reflections scanner;
    private ApplicationContext applicationContext;

    public JavaBeanConfigurator(String[] packagesToScan, ApplicationContext applicationContext) {
        this.scanner = new Reflections(Arrays.asList(packagesToScan));
        this.applicationContext = applicationContext;
    }

    @Override
    public Reflections getScanner() {
        return scanner;
    }

    @Override
    public <T> Class<? extends T> findImplementationClass(Field interfaceField) {
        Set<Class<? extends T>> implClasses = scanner.getSubTypesOf((Class<T>) interfaceField.getType());

        if (implClasses.size() != 1) {
            List<Class<? extends T>> primaryImpl = implClasses.stream()
                    .filter(impl -> impl.isAnnotationPresent(Primary.class))
                    .collect(Collectors.toList())
            ;

            if (primaryImpl.size() != 1) {
                if (interfaceField.isAnnotationPresent(Qualifier.class)) {
                    String beanName = interfaceField.getDeclaredAnnotation(Qualifier.class).beanName();
                    return (Class<? extends T>) applicationContext.getBeanInstance(beanName).getClass();
                } else {
                    throw new RuntimeException("No interface implementation selected for " + interfaceField
                            + "\nAdvice: use @Qualifier or @Primary annotation to choose correct implementation.",
                            new Throwable("No interface implementation selected for " + interfaceField)
                    );
                }
            }
            return primaryImpl.iterator().next();
        }
        return implClasses.stream().findFirst().get();
    }

    /**
     * Search interface implementations in context
     * @param interfaceType type of the interface
     * @return list of interface instances, that were found in context
     */
    @Override
    public List<Object> findInterfaceImplementations(Type interfaceType) {
        Set<Class<?>> implClasses = scanner.getSubTypesOf((Class<Object>) interfaceType);
        List<Object> beans = new ArrayList<>();

        implClasses.forEach(impl -> beans.add(applicationContext.getBeanInstance(impl)));

        return beans;
    }
}
