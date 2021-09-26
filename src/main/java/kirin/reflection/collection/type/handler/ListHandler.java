package kirin.reflection.collection.type.handler;

import kirin.reflection.KReflections;
import kirin.reflection.collection.KCollections;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListHandler implements ICollectionHandler {
    @Override
    public boolean isAssignableFromCollection(Type type) {
        return List.class.isAssignableFrom((Class<?>) type);
    }

    @Override
    public <T extends Collection> T getNewCollectionInstance(Type type) {
        if (KCollections.isList((Class<?>) type)) {
            return (T) new ArrayList();
        } else {
            return KReflections.newInstance((Class<?>) type);
        }
    }
}
