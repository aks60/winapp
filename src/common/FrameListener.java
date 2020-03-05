package common;

import dataset.Field;

public interface FrameListener<A, B> extends java.awt.event.ActionListener {

    default void actionRequest(A obj) {
    }

    default void actionResponse(B obj) {
    }

    default void actionPerformed(java.awt.event.ActionEvent evt) {
    }

    default Object actionPreview(Field field, int row, Object val) {
        return val;
    }
}
