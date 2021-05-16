package kirin.core.context;

import kirin.core.factory.BeanFactory;
import kirin.core.postprocessor.BeanPostProcessor;
import kirin.core.wrapper.Bean;
import kirin.logger.Klogger;
import kirin.logger.LogLevel;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private final Map<Class<?>, Bean> beanMap = new HashMap<>();
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Map<Class<?>, Bean> getBeanMap() {
        return beanMap;
    }

    public ApplicationContext() { }

    public <T> T getBeanInstance(Class<T> clazz) {
        if (beanMap.containsKey(clazz)) {
            return (T) beanMap.get(clazz).getInstance();
        } else {
            Klogger.getLogger().log(LogLevel.WARN, "Bean with class " + clazz.getName() + " not found");
            return null;
        }
    }

    public <T> T getBeanInstance(String beanName) {
        if (beanMap.entrySet().stream().anyMatch(bean -> bean.getValue().getName().equals(beanName))) {
            return (T) beanMap.entrySet().stream()
                    .filter(bean -> bean.getValue().getName().equals(beanName))
                    .iterator().next().getValue().getInstance();
        } else {
            Klogger.getLogger().log(LogLevel.WARN, String.format("Bean with name %s was not found", beanName));
            return null;
        }

    }

    /**
     * Create bean context
     */
    public void createBeanContext() {
        beanFactory.scanBeans();
        beanMap.putAll(beanFactory.getBeans());
        beanFactory.inject();
        beanMap.forEach((key, value) -> callPostProcessors(value.getInstance()));
    }

    /**
     * Call all postProcessors, implements {@link BeanPostProcessor}
     * @param instance for call postprocessors on it
     */
    private void callPostProcessors(Object instance) {
        for (Class<? extends BeanPostProcessor> processor : beanFactory.getBeanConfigurator().getScanner().getSubTypesOf(BeanPostProcessor.class)) {
            BeanPostProcessor postProcessor = null;
            try {
                postProcessor = processor.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            postProcessor.process(instance);
        }
    }
}
