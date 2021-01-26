package frames.swing;

import builder.model.AreaSimple;
import builder.model.ElemSimple;
import dataset.Record;
import domain.eSystree;
import javax.swing.tree.DefaultMutableTreeNode;

public class DefMutableTreeNode2<E> extends DefaultMutableTreeNode {

    public E obj = null;

    public DefMutableTreeNode2(E obj) {
        super();
        this.obj = obj;
    }

    public String toString() {
        if (obj instanceof Record) {
            return ((Record) obj).getStr(eSystree.name);

        } else if (obj instanceof AreaSimple) {
            return ((AreaSimple) obj).type().name;

        } else if (obj instanceof ElemSimple) {
            return ((ElemSimple) obj).type().name + ", " + ((ElemSimple) obj).layout().name.toLowerCase();
        }
        return null;
    }
}
