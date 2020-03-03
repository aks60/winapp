package common;

import dataset.Field;

public interface FrameListener<A, B> {

    default void request(A obj) {
    }

    default void response(B obj) {
    }

    default Object preview(Field field, int row, Object val) {
        return val;
    }
}
