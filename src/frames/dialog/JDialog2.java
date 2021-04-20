package frames.dialog;

import dataset.Table;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class JDialog2 extends javax.swing.JDialog {

    private Table qTable = null;
    private JButton btn = null;

    public JDialog2(Frame owner, boolean modal) {
        super(owner, true);
    }

    public JDialog2(Frame owner, boolean modal, Table qTable, JButton btn) {
        super(owner, true);
        this.qTable = qTable;
        this.btn = btn;
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoice(evt);
            }
        });        
    }
    
    private void btnChoice(ActionEvent evt) {
        
    }
}
