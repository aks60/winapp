package frames.swing;

import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.Com5t;
import builder.model.ElemCross;
import builder.model.ElemSimple;
import enums.Layout;
import enums.Type;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DrawScene extends javax.swing.JPanel {

    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private Wincalc iwin = null;
    private List<Float> vertList = new ArrayList();
    private List<Float> horList = new ArrayList();
    private List<Com5t> vertAreaList = new ArrayList();
    private List<Com5t> horAreaList = new ArrayList();

    public DrawScene(Wincalc iwin) {
        this.iwin = iwin;
        initComponents();
        initElements();
    }

    public void Draw(List vertList, List horList) {
//                int mov = 80;
//                for (int i = 1; i < ls1.size(); i++) {
//                    float x1 = ls1.get(i - 1), x2 = ls1.get(i);
//                    Draw.line(iwin, x1, iwin.height + mov, x2, iwin.height + mov, 0);
//                }
//                for (int i = 1; i < ls2.size(); i++) {
//                    float y1 = ls2.get(i - 1), y2 = ls2.get(i);
//                    Draw.line(iwin, (this.x2 + mov), y1, (this.x2 + mov), y2, 0);
//                }
//                if (ls1.size() > 2) { //линия общей ширины
//                    Draw.line(iwin, iwin.rootArea.x1, iwin.height + mov * 2, iwin.rootArea.x2, iwin.height + mov * 2, 0);
//                }
//                if (ls2.size() > 2) { //линия общей высоты
//                    Draw.line(iwin, iwin.width + mov * 2, 0, iwin.width + mov * 2, iwin.height, 0);
//                }
//            }        
    }

    
    public void lineList() {
        LinkedList<ElemCross> impostList = iwin.rootArea.listElem(Type.IMPOST, Type.SHTULP, Type.STOIKA);
        for (ElemSimple impostElem : impostList) { //по импостам определим точки разрыва линии
            if (Layout.VERT == impostElem.owner().layout()) {
                vertList.add(impostElem.y1 + (impostElem.y2 - impostElem.y1) / 2);
            } else {
                horList.add(impostElem.x1 + (impostElem.x2 - impostElem.x1) / 2);
            }
        }
        Collections.sort(vertList);
        Collections.sort(horList);
    }

    public void areaList() {

        List<AreaSimple> areaList = iwin.rootArea.listElem(enums.Type.AREA);
        for (AreaSimple area : areaList) {
            if (area.owner().layout() == Layout.HORIZ) {
                Com5t com = area.listChild.stream().filter(it -> it.type() == enums.Type.AREA && area.listChild.isEmpty()).findFirst().orElse(null);
                if (com != null) {
                    horAreaList.add(com);
                }
            } else if (area.owner().layout() == Layout.VERT) {
                Com5t com = area.listChild.stream().filter(it -> it.type() == enums.Type.AREA && area.layout() == Layout.VERT).findFirst().orElse(null);
                if (com != null) {
                    vertAreaList.add(com);
                }
            }
        }
        System.out.println(vertAreaList);
    }

    private void paintComponentVert(Graphics g) {
        for (Float val : vertList) {
            g.drawLine(0, (int) (val.intValue() * iwin.scale), 8, (int) (val.intValue() * iwin.scale));
        }
        g.drawLine(0, 2, 8, 2);
        g.drawLine(0, (int) (iwin.height * iwin.scale), 8, (int) (iwin.height * iwin.scale));
    }

    private void paintComponentHor(Graphics g) {
        for (Float val : horList) {
            g.drawLine((int) (val.intValue() * iwin.scale) + 20, 10, (int) (val.intValue() * iwin.scale) + 20, 18);
        }
        g.drawLine(20, 10, 20, 18);
        g.drawLine((int) (iwin.width * iwin.scale) + 20, 10, (int) (iwin.width * iwin.scale) + 20, 18);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan1 = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintComponentHor(g);
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
                paintComponentVert(g);
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
