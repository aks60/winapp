package wincalc;

import enums.eTypeElem;
import java.util.LinkedList;
import javax.swing.JComponent;

public abstract class ElemBase extends Base {


    public ElemBase(String id) {
        this.id = id;
    }

     public LinkedList<Base> getArrChild() {
         return new LinkedList();
     }
}
