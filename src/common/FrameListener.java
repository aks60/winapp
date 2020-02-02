package common;

import dataset.Field;

public interface FrameListener<A, B> {

    default void request(A obj) {
    }

    default void response(B obj) {
    }
    
    default Object preview(Field f, Object obj) {
        return obj;
    }
}
