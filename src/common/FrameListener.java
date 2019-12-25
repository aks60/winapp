package common;

public interface FrameListener<A, B> {

    default void request(A obj) {
    }

    default void response(B obj) {
    }
}
