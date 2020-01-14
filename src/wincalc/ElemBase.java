package wincalc;

import enums.TypeElem;
import java.util.LinkedList;
import javax.swing.JComponent;

public abstract class ElemBase extends Base {

    public ElemBase(String id) {
        this.id = id;
    }

    @Override
    public LinkedList<Base> listChild() {
        return new LinkedList();
    }
}
