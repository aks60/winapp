package common;

import dataset.Field;

public interface FrameListener<A, B> extends java.awt.event.ActionListener {

    default void request(A obj) {
    }

    default void response(B obj) {
    }

    default void actionPerformed(java.awt.event.ActionEvent evt) {
    }

    default Object preview(Field field, int row, Object val) {
        return val;
    }
}
