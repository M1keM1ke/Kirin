package kirin.core.annotation.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the class as a component of the kirin context
 * When you specify this annotation, the class object will be included in the context
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Component {
}
