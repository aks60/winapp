package common;

public interface FrameListener<A, B> {

    public void request(A obj);

    public void response(B obj);
}

