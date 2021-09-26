package kirin.reflection.collection;

import kirin.reflection.collection.type.handler.CollectionType;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class KCollections {

    public static Collection getCollectionInstanceByType(Type type) throws RuntimeException {
        if (isCollection((Class<?>) type)) {
            return CollectionType.getCollectionType().getNewCollectionInstance(type);
        }

        throw new RuntimeException("Can't find comparable collection for inject");
    }

    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isList(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    public static boolean isSet(Class<?> clazz) {
        return Set.class.isAssignableFrom(clazz);
    }
}
