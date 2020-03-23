package dataset;

import java.util.List;

public interface Enam {

    public String name();

    public int ordinal();

    default int numb() {
        return -1;
    }

    default String text() {
        return null;
    }

    default Field[] fields() {
        return null;
    }

    default List dict() {
        return null;
    }
}
