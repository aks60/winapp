package common;

import builder.making.Specific;
import java.util.ArrayList;

public class ArrayList2<E extends Specific> extends ArrayList<E> {

    public ArrayList2() {
        super();
    }

    public Specific find(float id) {
        return this.stream().filter(it -> it.id == id).findFirst().get();
    }
//    public E find2(Object id) {
//        return this.stream().filter(it -> id.equals(it.id)).findFirst().get();
//    }
}
