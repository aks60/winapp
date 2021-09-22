package frames.swing;

import builder.Wincalc;
import frames.swing.rotate.VerticalLabelUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DrawScene extends javax.swing.JPanel {

    private DecimalFormat df2 = new DecimalFormat("#0.00");
    JPanel pan2 = new JPanel(new java.awt.BorderLayout());
    JPanel pan4 = new JPanel(new java.awt.BorderLayout());
    Wincalc iwin = null;

    public DrawScene(Wincalc iwin) {
        this.iwin = iwin;
        initComponents();
        initElements();
        addPanel(pan2, BorderLayout.EAST);
        addPanel(pan4, BorderLayout.WEST);
    }

    private void addPanel(JPanel pan, String layout) {
        pan.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pan.setPreferredSize(new Dimension(14, 10));
        add(pan, layout);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lab1 = new javax.swing.JLabel();
        pan3 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        pan1 = new javax.swing.JPanel();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();

        lab1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lab1.setForeground(new java.awt.Color(0, 0, 255));
        lab1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lab1.setText("400");
        lab1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(255, 0, 51)));
        lab1.setMinimumSize(new java.awt.Dimension(14, 40));
        lab1.setPreferredSize(new java.awt.Dimension(14, 120));
        lab1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                formAncestorResized(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

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
    }// </editor-fold>//GEN-END:initComponents

    private void formAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorResized

//        float width = Float.valueOf(String.valueOf(iwin.scale * m));
//        Dimension dim = new Dimension((int) width, lab2.getHeight());
//        lab2.setSize(dim);
        //lab2.setText(df2.format(width));
        //pan3.repaint();
        //lab2.update(lab2.getGraphics());
        //lab2.repaint();        
    }//GEN-LAST:event_formAncestorResized

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
//        Dimension dim = new Dimension(20, lab2.getHeight());
//        Graphics g = lab2.getGraphics();
//        lab2.setPreferredSize(dim);
//        //lab2.doLayout();
//        //lab2.updateUI();
//        lab2.update(g);
//        lab2.repaint();
//        pan3.repaint();
    }//GEN-LAST:event_btn4ActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JLabel lab1;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan3;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 

    private void initElements() {
        lab1.setUI(new VerticalLabelUI(false));
    }
}
