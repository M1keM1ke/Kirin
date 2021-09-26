package kirin.reflection.collection.type.handler;

import kirin.reflection.KReflections;
import org.reflections.Reflections;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionType {
    private static CollectionType collectionType;
    private Reflections reflections = new Reflections("");
    private List<ICollectionHandler> collectionHandlers = new ArrayList<>();

    private CollectionType() {
        reflections
                .getSubTypesOf(ICollectionHandler.class)
                .forEach(h -> collectionHandlers.add(KReflections.newInstance(h)))
        ;
    }

    public static CollectionType getCollectionType() {
        if (collectionType == null) {
            collectionType = new CollectionType();
        }

        return collectionType;
    }

    public Collection getNewCollectionInstance(Type type) throws RuntimeException {
        return collectionHandlers.stream()
                .filter(ch -> ch.isAssignableFromCollection(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Unable to find correct Collection Type for type:%s. This type is not supported",
                                type.getTypeName()
                        )
                ))
                .getNewCollectionInstance(type)
        ;
    }
}
