package kirin.core.postprocessor;

import kirin.core.annotation.PostConstruct;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostConstructBeanPostProcessor implements BeanPostProcessor{
    @Override
    public void process(Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    method.invoke(bean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
