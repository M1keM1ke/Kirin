package kirin.reflection.collection.type.handler;

import java.lang.reflect.Type;
import java.util.Collection;

public interface ICollectionHandler {
    boolean isAssignableFromCollection(Type type);
    <T extends Collection> T getNewCollectionInstance(Type type);
}
