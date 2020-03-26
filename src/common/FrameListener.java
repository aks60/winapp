package common;

import dataset.Field;

public interface FrameListener<A, B> {

    default void actionRequest(A o) {
    }

    default void actionResponse(B o) {
    }

    default Object actionPreview(int col, int row, Object val) {
        return val;
    }
}
