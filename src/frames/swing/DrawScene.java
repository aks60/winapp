package frames.swing;

public class DrawScene extends javax.swing.JPanel {


    public DrawScene() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan26 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        pan27 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pan23 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pan24 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        pan25 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pan26.setMinimumSize(new java.awt.Dimension(14, 18));
        pan26.setPreferredSize(new java.awt.Dimension(14, 18));
        pan26.setLayout(new java.awt.BorderLayout());

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 0, 0));
        jButton1.setText("+");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton1.setMaximumSize(new java.awt.Dimension(16, 16));
        jButton1.setMinimumSize(new java.awt.Dimension(16, 16));
        jButton1.setPreferredSize(new java.awt.Dimension(16, 16));
        pan26.add(jButton1, java.awt.BorderLayout.WEST);

        pan27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("233");
        jLabel1.setPreferredSize(new java.awt.Dimension(23, 14));
        pan27.add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("400");
        jLabel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 0, 51)));
        jLabel2.setPreferredSize(new java.awt.Dimension(40, 14));
        pan27.add(jLabel2);

        pan26.add(pan27, java.awt.BorderLayout.CENTER);

        add(pan26, java.awt.BorderLayout.NORTH);

        pan23.setPreferredSize(new java.awt.Dimension(16, 14));
        pan23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("400");
        jLabel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(255, 0, 51)));
        jLabel3.setMinimumSize(new java.awt.Dimension(14, 40));
        jLabel3.setPreferredSize(new java.awt.Dimension(12, 120));
        pan23.add(jLabel3);

        add(pan23, java.awt.BorderLayout.LINE_START);

        pan24.setPreferredSize(new java.awt.Dimension(14, 18));
        pan24.setLayout(new java.awt.BorderLayout());

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 0, 0));
        jButton2.setText("-");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton2.setMaximumSize(new java.awt.Dimension(16, 16));
        jButton2.setMinimumSize(new java.awt.Dimension(16, 16));
        jButton2.setPreferredSize(new java.awt.Dimension(16, 16));
        pan24.add(jButton2, java.awt.BorderLayout.WEST);

        pan25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan25.setLayout(new java.awt.BorderLayout());
        pan24.add(pan25, java.awt.BorderLayout.CENTER);

        add(pan24, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel pan23;
    private javax.swing.JPanel pan24;
    private javax.swing.JPanel pan25;
    private javax.swing.JPanel pan26;
    private javax.swing.JPanel pan27;
    // End of variables declaration//GEN-END:variables
}
