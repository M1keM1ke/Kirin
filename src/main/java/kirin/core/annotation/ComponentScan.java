package kirin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the packages required to scan for kirin context components
 * By default, all root packages are scanned
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ComponentScan {
    /**
     * @return scan packages
     */
    String[] packageToScan() default "";
}
