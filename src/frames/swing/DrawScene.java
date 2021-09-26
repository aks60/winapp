package frames.swing;

import builder.Wincalc;
import builder.model.Com5t;
import builder.model.ElemCross;
import builder.model.ElemSimple;
import domain.eArtikl;
import enums.Layout;
import enums.Type;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DrawScene extends javax.swing.JPanel {

    private DecimalFormat df1 = new DecimalFormat("#0.#");
    private Wincalc iwin = null;
    private List<Float> vertList = new ArrayList();
    private List<Float> horList = new ArrayList();

    public DrawScene() {
        initComponents();
        initElements();
    }

    public void repaint(Wincalc iwin) {
        this.iwin = iwin;
        lineList();
        pan1.repaint();
        pan4.repaint();        
    }

    private void lineList() {
        vertList.clear();
        horList.clear();
        LinkedList<ElemCross> impostList = iwin.rootArea.listElem(Type.IMPOST, Type.SHTULP, Type.STOIKA);
        for (ElemSimple elem : impostList) { //по импостам определим точки разрыва линии
            if (Layout.VERT == elem.owner().layout()) {
                vertList.add(elem.y1 + elem.artiklRecAn.getFloat(eArtikl.size_centr));
            } else {
                horList.add(elem.x1 + elem.artiklRecAn.getFloat(eArtikl.size_centr));
            }
        }
        vertList.add(iwin.rootArea.height());
        horList.add(iwin.rootArea.width());
    }

    private void paintVertical(Graphics gc) {
        Graphics2D g = (Graphics2D) gc;
        int size = (iwin.scale > .16) ? 11 : (iwin.scale > .15) ? 9 : 8;
        g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, size));
        float val_old = 0;
        for (Float val : vertList) {
            int y = (int) (val.intValue() * iwin.scale);
            int y_old = (int) (val_old * iwin.scale);
            g.drawLine(0, y, 8, y);
            int y_txt = (int) (y - 14 - (y - y_old) / 2);
            g.rotate(Math.toRadians(-90), 9, y_txt + 28);
            g.drawString(df1.format(val.floatValue() - val_old), 9, y_txt + 28);
            g.rotate(Math.toRadians(90), 9, y_txt + 28);
            val_old = val.intValue();
        }
        g.drawLine(0, 2, 8, 2);
    }

    private void paintHorizontal(Graphics gc) {
        Graphics2D g = (Graphics2D) gc;
        int size = (iwin.scale > .16) ? 11 : (iwin.scale > .15) ? 9 : 8;
        g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, size));
        float val_old = 0;
        for (Float val : horList) {
            int x = (int) (val.intValue() * iwin.scale);
            int x_old = (int) (val_old * iwin.scale);
            g.drawLine(x + 20, 10, x + 20, 18);
            int x_txt = (int) (x - 14 - (x - x_old) / 2);
            g.drawString(df1.format(val.floatValue() - val_old), x_txt + 20, 16);
            val_old = val.intValue();
        }
        g.drawLine(20, 10, 20, 18);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan1 = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintHorizontal(g);
            }
        };
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        pan2 = new javax.swing.JPanel();
        pan3 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        pan4 = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintVertical(g);
            }
        };

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                formAncestorResized(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        pan1.setMinimumSize(new java.awt.Dimension(4, 18));
        pan1.setPreferredSize(new java.awt.Dimension(4, 18));
        pan1.setLayout(new java.awt.BorderLayout());

        btn2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 0, 0));
        btn2.setText("-");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(16, 16));
        btn2.setMinimumSize(new java.awt.Dimension(16, 16));
        btn2.setPreferredSize(new java.awt.Dimension(16, 16));
        pan1.add(btn2, java.awt.BorderLayout.WEST);

        btn3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 0, 0));
        btn3.setText("-");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(16, 16));
        btn3.setMinimumSize(new java.awt.Dimension(16, 16));
        btn3.setPreferredSize(new java.awt.Dimension(16, 16));
        pan1.add(btn3, java.awt.BorderLayout.EAST);

        add(pan1, java.awt.BorderLayout.SOUTH);

        pan2.setPreferredSize(new java.awt.Dimension(18, 10));
        add(pan2, java.awt.BorderLayout.EAST);

        pan3.setMinimumSize(new java.awt.Dimension(4, 18));
        pan3.setPreferredSize(new java.awt.Dimension(4, 18));
        pan3.setLayout(new java.awt.BorderLayout());

        btn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 0, 0));
        btn1.setText("+");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(16, 16));
        btn1.setMinimumSize(new java.awt.Dimension(16, 16));
        btn1.setPreferredSize(new java.awt.Dimension(16, 16));
        pan3.add(btn1, java.awt.BorderLayout.WEST);

        btn4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 0, 0));
        btn4.setText("+");
        btn4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn4.setMaximumSize(new java.awt.Dimension(16, 16));
        btn4.setMinimumSize(new java.awt.Dimension(16, 16));
        btn4.setPreferredSize(new java.awt.Dimension(16, 16));
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });
        pan3.add(btn4, java.awt.BorderLayout.EAST);

        add(pan3, java.awt.BorderLayout.NORTH);

        pan4.setPreferredSize(new java.awt.Dimension(18, 10));
        add(pan4, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void formAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorResized

    }//GEN-LAST:event_formAncestorResized

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed

    }//GEN-LAST:event_btn4ActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 

    private void initElements() {
    }
}
