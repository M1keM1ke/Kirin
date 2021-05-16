package kirin.core.util;

public class BeanHelper {
    /**
     * Generates the class name
     * SomeClass.class -> someClass
     * @param clazz the class whose name you want to generate
     * @return class name
     */
    public static String getClassName(Class<?> clazz) {
        //TODO: возможно надо заменить просто на Introspector.decapitalize()
        return clazz.getSimpleName().substring(0, 1).toLowerCase() +
                clazz.getSimpleName().substring(1);
    }
}
