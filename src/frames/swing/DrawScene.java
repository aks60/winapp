package frames.swing;

import builder.Wincalc;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DrawScene extends javax.swing.JPanel {

    private DecimalFormat df2 = new DecimalFormat("#0.00");
    Wincalc iwin = null;

    public DrawScene(Wincalc iwin) {
        this.iwin = iwin;
        initComponents();
        initElements();
    }

    private void paintComp(Graphics g) {
        
    }
    
    private JPanel createPanel() {
        JPanel pan = new JPanel(new java.awt.BorderLayout()) {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        pan.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pan.setPreferredSize(new Dimension(14, 10));
        return pan;
    }

    private JButton  createButton(String text) {
        javax.swing.JButton btn = new javax.swing.JButton();
        btn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn.setForeground(new java.awt.Color(255, 0, 0));
        btn.setText(text);
        btn.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn.setMaximumSize(new java.awt.Dimension(16, 16));
        btn.setMinimumSize(new java.awt.Dimension(16, 16));
        btn.setPreferredSize(new java.awt.Dimension(16, 16));
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //btn4ActionPerformed(evt);
            }
        });
        return btn;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lab1 = new javax.swing.JLabel();
        pan03 = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintComp(g);
            }
        };
        btn1 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        pan01 = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintComp(g);
            }
        };
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

        pan03.setMinimumSize(new java.awt.Dimension(4, 18));
        pan03.setPreferredSize(new java.awt.Dimension(4, 18));
        pan03.setLayout(new java.awt.BorderLayout());

        btn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 0, 0));
        btn1.setText("+");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(16, 16));
        btn1.setMinimumSize(new java.awt.Dimension(16, 16));
        btn1.setPreferredSize(new java.awt.Dimension(16, 16));
        pan03.add(btn1, java.awt.BorderLayout.WEST);

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
        pan03.add(btn4, java.awt.BorderLayout.EAST);

        add(pan03, java.awt.BorderLayout.NORTH);

        pan01.setMinimumSize(new java.awt.Dimension(4, 18));
        pan01.setPreferredSize(new java.awt.Dimension(4, 18));
        pan01.setLayout(new java.awt.BorderLayout());

        btn2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 0, 0));
        btn2.setText("-");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(16, 16));
        btn2.setMinimumSize(new java.awt.Dimension(16, 16));
        btn2.setPreferredSize(new java.awt.Dimension(16, 16));
        pan01.add(btn2, java.awt.BorderLayout.WEST);

        btn3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 0, 0));
        btn3.setText("-");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(16, 16));
        btn3.setMinimumSize(new java.awt.Dimension(16, 16));
        btn3.setPreferredSize(new java.awt.Dimension(16, 16));
        pan01.add(btn3, java.awt.BorderLayout.EAST);

        add(pan01, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void formAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorResized

    }//GEN-LAST:event_formAncestorResized

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed

    }//GEN-LAST:event_btn4ActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    private JButton btn001, btn002, btn003, btn004;
    private JPanel pan1, pan2, pan3, pan4;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JLabel lab1;
    private javax.swing.JPanel pan01;
    private javax.swing.JPanel pan03;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 

    private void initElements() {
        //lab1.setUI(new VerticalLabelUI(false));
//        pan1 = createPanel();
//        add(pan1, BorderLayout.SOUTH);
//        pan1.add(createButton("-"), java.awt.BorderLayout.WEST);
//        pan1.add(createButton("-"), java.awt.BorderLayout.EAST);
//
//        pan2 = createPanel();
//        add(pan2, BorderLayout.EAST);
//
//        pan3 = createPanel();
//        add(pan3, BorderLayout.NORTH);
//        pan3.add(createButton("+"), java.awt.BorderLayout.WEST);
//        pan3.add(createButton("+"), java.awt.BorderLayout.EAST);
//
//        pan4 = createPanel();
//        add(pan4, BorderLayout.WEST);
    }
}
