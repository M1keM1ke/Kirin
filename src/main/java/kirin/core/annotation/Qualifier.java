package kirin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Determines the priority of the bean injection
 * The equivalent is {@link Primary}
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Qualifier {
    /**
     * Returns the specified priority bean name
     * @return bean name
     */
    String beanName();
}
