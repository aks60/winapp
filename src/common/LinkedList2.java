package common;

import builder.model.ICom5t;
import builder.script.GsonElem;
import java.util.LinkedList;

public class LinkedList2<E extends ICom5t> extends LinkedList<E> {

    public LinkedList2() {
        super();
    }

    public E find(float id) {
        return this.stream().filter(it -> it.id() == id).findFirst().get();
    }
    
    public GsonElem gson(float id) {
        ICom5t com5t = this.stream().filter(it -> it.id() == id).findFirst().orElse(null);
        if(com5t != null) {
            return com5t.gson();
        }
        return null;
    }
}
