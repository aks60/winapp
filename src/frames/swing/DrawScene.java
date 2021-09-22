package frames.swing;

import frames.swing.rotate.VerticalLabelUI;

public class DrawScene extends javax.swing.JPanel {

    public DrawScene() {
        initComponents();
        initElements();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan01 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        pan03 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        pan05 = new javax.swing.JPanel();
        btn2 = new javax.swing.JButton();
        pan06 = new javax.swing.JPanel();
        btn3 = new javax.swing.JButton();
        pan3 = new javax.swing.JPanel();
        lab2 = new javax.swing.JLabel();
        lab3 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        setLayout(new java.awt.BorderLayout());

        pan01.setMinimumSize(new java.awt.Dimension(4, 18));
        pan01.setPreferredSize(new java.awt.Dimension(4, 18));
        pan01.setLayout(new java.awt.BorderLayout());

        btn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 0, 0));
        btn1.setText("+");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(16, 16));
        btn1.setMinimumSize(new java.awt.Dimension(16, 16));
        btn1.setPreferredSize(new java.awt.Dimension(16, 16));
        pan01.add(btn1, java.awt.BorderLayout.WEST);

        btn4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 0, 0));
        btn4.setText("+");
        btn4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn4.setMaximumSize(new java.awt.Dimension(16, 16));
        btn4.setMinimumSize(new java.awt.Dimension(16, 16));
        btn4.setPreferredSize(new java.awt.Dimension(16, 16));
        pan01.add(btn4, java.awt.BorderLayout.EAST);

        add(pan01, java.awt.BorderLayout.NORTH);

        pan03.setMinimumSize(new java.awt.Dimension(16, 4));
        pan03.setPreferredSize(new java.awt.Dimension(14, 4));
        pan03.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        lab1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lab1.setForeground(new java.awt.Color(0, 0, 255));
        lab1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lab1.setText("400");
        lab1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(255, 0, 51)));
        lab1.setMinimumSize(new java.awt.Dimension(14, 40));
        lab1.setPreferredSize(new java.awt.Dimension(14, 120));
        lab1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        pan03.add(lab1);

        add(pan03, java.awt.BorderLayout.LINE_START);

        pan05.setMinimumSize(new java.awt.Dimension(4, 18));
        pan05.setPreferredSize(new java.awt.Dimension(4, 18));
        pan05.setLayout(new java.awt.BorderLayout());

        btn2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 0, 0));
        btn2.setText("-");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(16, 16));
        btn2.setMinimumSize(new java.awt.Dimension(16, 16));
        btn2.setPreferredSize(new java.awt.Dimension(16, 16));
        pan05.add(btn2, java.awt.BorderLayout.WEST);

        pan06.setLayout(new java.awt.BorderLayout());

        btn3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 0, 0));
        btn3.setText("-");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(16, 16));
        btn3.setMinimumSize(new java.awt.Dimension(16, 16));
        btn3.setPreferredSize(new java.awt.Dimension(16, 16));
        pan06.add(btn3, java.awt.BorderLayout.EAST);

        pan3.setMinimumSize(new java.awt.Dimension(4, 4));
        pan3.setPreferredSize(new java.awt.Dimension(4, 4));
        pan3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        lab2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lab2.setForeground(new java.awt.Color(0, 0, 255));
        lab2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lab2.setText("233");
        lab2.setPreferredSize(new java.awt.Dimension(123, 14));
        lab2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        pan3.add(lab2);

        lab3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lab3.setForeground(new java.awt.Color(0, 0, 255));
        lab3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lab3.setText("400");
        lab3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 0, 51)));
        lab3.setPreferredSize(new java.awt.Dimension(40, 14));
        lab3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        pan3.add(lab3);

        pan06.add(pan3, java.awt.BorderLayout.CENTER);

        pan05.add(pan06, java.awt.BorderLayout.CENTER);

        add(pan05, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab3;
    private javax.swing.JPanel pan01;
    private javax.swing.JPanel pan03;
    private javax.swing.JPanel pan05;
    private javax.swing.JPanel pan06;
    private javax.swing.JPanel pan3;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 
    
    private void initElements() {
        lab1.setUI(new VerticalLabelUI(false));
    }
}
