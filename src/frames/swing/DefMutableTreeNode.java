package frames.swing;

import builder.model.AreaSimple;
import builder.model.Com5t;
import dataset.Record;
import domain.eSystree;
import javax.swing.tree.DefaultMutableTreeNode;

public class DefMutableTreeNode extends DefaultMutableTreeNode {

    public Com5t com5t = null;
    public Record systreeRec = null;

    public DefMutableTreeNode(Record systreeRec) {
        super();
        this.systreeRec = systreeRec;
    }

    public DefMutableTreeNode(Com5t com5t) {
        super();
        this.com5t = com5t;
    }

    public String toString() {
        if (com5t != null) {
            if (com5t instanceof AreaSimple) {
                return com5t.type().name;
            } else {
                return com5t.type().name + ", " + com5t.layout().name.toLowerCase();
            }
        } else {
            return systreeRec.getStr(eSystree.name);
        }
    }
}
