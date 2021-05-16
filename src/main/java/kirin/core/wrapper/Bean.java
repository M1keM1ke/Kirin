package kirin.core.wrapper;

/**
 * The bean stored in the kirin context
 */
public class Bean {
    /**
     * Bean name
     */
    private String name;
    /**
     * Bean class
     */
    private Class<?> clazz;
    /**
     * Current instance of the bean class
     */
    private Object instance;

    public Bean() {}

    public Bean(String name, Class<?> clazz, Object instance) {
        this.name = name;
        this.clazz = clazz;
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getInstance() {
        return instance;
    }
}
