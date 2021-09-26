package kirin.core.factory;

import kirin.core.annotation.Inject;
import kirin.core.annotation.stereotype.Component;
import kirin.core.config.Configuration;
import kirin.core.config.JavaConfiguration;
import kirin.core.configurator.BeanConfigurator;
import kirin.core.configurator.JavaBeanConfigurator;
import kirin.core.context.ApplicationContext;
import kirin.core.util.BeanHelper;
import kirin.core.wrapper.Bean;
import kirin.reflection.KReflections;
import kirin.reflection.collection.KCollections;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private final BeanConfigurator beanConfigurator;
    private final Configuration configuration;
    private ApplicationContext context;
    private Map<Class<?>, Bean> beans;

    public Map<Class<?>, Bean> getBeans() {
        return beans;
    }

    public BeanConfigurator getBeanConfigurator() {
        return beanConfigurator;
    }

    public BeanFactory(ApplicationContext context) {
        configuration = new JavaConfiguration();
        beanConfigurator = new JavaBeanConfigurator(configuration.getPackagesToScan(), context);
        this.context = context;
        beans = new HashMap<>();
    }

    /**
     * Get new Class instance if it's not annotation
     *
     * @param clazz for new instance
     * @param <T>   type of instance
     * @return {@link Optional} of new instance
     */
    public <T> Optional<T> getNewInstance(Class<T> clazz) {
        T bean;

        if (clazz.isAnnotation()) {
            return Optional.empty();
        }

        bean = KReflections.newInstance(clazz);

        return Optional.ofNullable(bean);
    }

    /**
     * Inject instance in class field annotated {@link Inject}
     *
     * @param clazz    for dependency inject
     * @param instance for inject
     * @param <T>
     * @throws IllegalAccessException when can't set instance in field
     */
    private <T> void injectBeanInstance(Class<? extends T> clazz, T instance) throws IllegalAccessException {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        for (Field field : fields) {
            field.setAccessible(true);

            if (Collection.class.isAssignableFrom(field.getType())) {
                injectCollection(instance, field);
                continue;
            }

            if (field.getType().isInterface()) {
                injectImplementationInterface(instance, field);
                continue;
            }

            field.set(instance, context.getBeanInstance(field.getType()));
        }
    }

    private <T> void injectCollection(T instance, Field field) throws IllegalAccessException {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Type[] actualTypeArguments = genericType.getActualTypeArguments();

        if (actualTypeArguments.length == 1) {
            Type actualTypeArgument = actualTypeArguments[0];
            List<Object> implementations = beanConfigurator.findInterfaceImplementations(actualTypeArgument);

            Collection collectionForInject = KCollections.getCollectionInstanceByType(field.getType());
            collectionForInject.addAll(implementations);

            field.set(instance, collectionForInject);
        }
    }

    private <T> void injectImplementationInterface(T instance, Field field) throws IllegalAccessException {
        Class<?> implementationClass = beanConfigurator.findImplementationClass(field);
        field.set(instance, context.getBeanInstance(implementationClass));
    }

    /**
     * Scan classes with {@link Component} annotation of classpath and
     * forms bean map
     */
    public void scanBeans() {
        Reflections scanner = beanConfigurator.getScanner();
        scanBeans(scanner);
    }

    private void scanBeans(Reflections scanner) {
        Set<Class<?>> typesAnnotatedWith = scanner.getTypesAnnotatedWith(Component.class).stream()
                .filter(clazz -> !clazz.isInterface())
                .collect(Collectors.toSet());
        fillBeanMap(typesAnnotatedWith);
    }

    private void fillBeanMap(Set<Class<?>> systemComponents) {
        systemComponents.stream().filter(clazz -> getNewInstance(clazz).isPresent()).forEach(clazz -> {
            Object instance = getNewInstance(clazz).get();
            beans.put(clazz, new Bean(BeanHelper.getClassName(clazz), clazz, instance));
        });
    }

    /**
     * Inject dependencies in all beans from bean map
     */
    public void inject() {
        beans.entrySet().forEach(bean -> {
            try {
                injectBeanInstance(bean.getKey(), bean.getValue().getInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
