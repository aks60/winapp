package frames.swing;

import builder.script.Mediate;
import dataset.Record;
import domain.eSystree;
import enums.TypeElem;
import javax.swing.tree.DefaultMutableTreeNode;

public class DefMutableTreeNode extends DefaultMutableTreeNode {

    public Mediate mediateRec = null;
    public Record systreeRec = null;

    public DefMutableTreeNode(Record systreeRec) {
        super(systreeRec.getStr(eSystree.name));
        this.systreeRec = systreeRec;
    }

    public DefMutableTreeNode(Mediate mediateRec) {
        super();
        this.mediateRec = mediateRec;
    }

    public String toString() {
        if (mediateRec != null) {
            if (mediateRec.type == TypeElem.FRAME_SIDE) {
                return mediateRec.type.name + ", " + mediateRec.layout.name.toLowerCase();
            } else if (mediateRec.type == TypeElem.AREA) {
                return mediateRec.type.name + ". " + mediateRec.layout.name + " напр.";
            } else {
                return mediateRec.type.name;
            }
        } else {
            return super.toString();
        }
    }
}
