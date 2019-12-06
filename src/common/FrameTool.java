package common;

import javax.swing.Icon;
import javax.swing.JButton;

public class FrameTool {

    private Icon[] icon = {new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c020.gif")),
        new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"))};
    private JButton btnIns, btnDel, btnSave;

    public FrameTool() {
    }
    
    public void init(JButton btnIns, JButton btnDel, JButton btnSave) {
        this.btnIns = btnIns;
        this.btnDel = btnDel;
        this.btnSave = btnSave;
    }

    public void select() {
        btnIns.setIcon(icon[0]);
        btnDel.setIcon(icon[0]);
        btnSave.setIcon(icon[1]);
    }

    public void update() {
        btnIns.setIcon(icon[0]);
        btnDel.setIcon(icon[0]);
        btnSave.setIcon(icon[0]);
    }

    public void delete() {
        btnIns.setIcon(icon[0]);
        btnDel.setIcon(icon[0]);
        btnSave.setIcon(icon[1]);
    }
}
