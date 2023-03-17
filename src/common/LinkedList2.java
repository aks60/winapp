package common;

import builder.ICom5t;
import builder.script.GsonElem;
import enums.Type;
import java.util.LinkedList;
import java.util.List;

public class LinkedList2<E extends ICom5t> extends LinkedList<E> {

    public LinkedList2() {
        super();
    }

    public E find(float id) {
        return this.stream().filter(it -> it.id() == id).findFirst().get();
    }

    public GsonElem gson(float id) {
        ICom5t com5t = this.stream().filter(it -> it.id() == id).findFirst().orElse(null);
        if (com5t != null) {
            return com5t.gson();
        }
        return null;
    }

    public int indexKey(float id) {
        for (int i = 0; i < this.size(); i++) {
            ICom5t el = this.get(i);
            if (el.id() == id) {
                return i;
            }
        }
        return -1;
    }

    public <T extends ICom5t> LinkedList2<T> filter(Type... type) {
        List tp = List.of(type);
        LinkedList2<T> list2 = new LinkedList2();
        for (E el : this) {
            if (tp.contains(el.type())) {
                list2.add((T) el);
            }
        }
        return list2;
    }

    public E find(Type type) {

        for (E el : this) {
            if (type == el.type()) {
                return (E) el;
            }
        }
        return null;
    }
}
