package frames;

import frames.Util;

public class AboutBox extends javax.swing.JFrame {

    public AboutBox() {
        initComponents();

        labProjectVersion.setText("Версия программы - 1.0");
        labBase.setText("Версия базы данных - 1.0");
        labConfig.setText("Конфигурация  - 2.0");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panText1 = new javax.swing.JPanel();
        labTitle = new javax.swing.JLabel();
        labImage = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        labProjectVersion = new javax.swing.JLabel();
        labBase = new javax.swing.JLabel();
        labConfig = new javax.swing.JLabel();
        panText2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        panTool = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panText1.setPreferredSize(new java.awt.Dimension(300, 100));
        panText1.setLayout(new java.awt.BorderLayout());

        labTitle.setBackground(new java.awt.Color(255, 255, 255));
        labTitle.setFont(Util.getFont(0, 1));
        labTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labTitle.setText("<<SA-OKNA>>");
        labTitle.setOpaque(true);
        labTitle.setPreferredSize(new java.awt.Dimension(123, 30));
        panText1.add(labTitle, java.awt.BorderLayout.PAGE_START);

        labImage.setBackground(new java.awt.Color(255, 255, 255));
        labImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif"))); // NOI18N
        labImage.setMaximumSize(new java.awt.Dimension(300, 70));
        labImage.setOpaque(true);
        labImage.setPreferredSize(new java.awt.Dimension(80, 60));
        panText1.add(labImage, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(200, 60));
        jPanel2.setLayout(new java.awt.GridLayout(3, 0));

        labProjectVersion.setFont(Util.getFont(0, 1));
        labProjectVersion.setText("Верия программы - 1.0");
        jPanel2.add(labProjectVersion);

        labBase.setFont(Util.getFont(0, 1));
        labBase.setText("Версия базы данных - 1.0");
        jPanel2.add(labBase);

        labConfig.setFont(Util.getFont(0, 1));
        labConfig.setText("Конфигурация  - 2.0");
        jPanel2.add(labConfig);
        labConfig.getAccessibleContext().setAccessibleName("");

        panText1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panText1, java.awt.BorderLayout.NORTH);

        panText2.setPreferredSize(new java.awt.Dimension(300, 160));
        panText2.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(Util.getFont(0, 1));
        jLabel2.setText("<html>\n<body>\n<p><b>&nbsp;<span lang=\"en-us\"> </span>&nbsp;Информационно - аналитическая \nсистема<br>\n<span lang=\"en-us\">&nbsp;</span>компании АВЕРС (&quot;ФинПромМаркет-ХХ<span lang=\"en-us\">I&quot;</span>).<br>\n&nbsp;</b></p>\n<p><b><span lang=\"en-us\">&nbsp;</span>Все права защищены. Подробная инфор<span lang=\"en-us\">\n</span>&nbsp;-<span lang=\"en-us\"> </span><br>\n<span lang=\"en-us\">&nbsp;</span>мация о компании на сайте <font color=\"#0000FF\">\n<span lang=\"en-us\"><a href=\"http://www.iicavers.ru\">www.iicavers.ru</a></span>.<br>\n<span lang=\"en-us\">&nbsp;</span></font>Телефон<span lang=\"en-us\">:</span>+7 (495) 909-03-60 <br>\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+7 (495) 909-03-64 <br>\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+7 (903) 250-61-59 <br>\n&nbsp;E-mail:</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span lang=\"en-us\">office@iicavers.ru</span></b><br>&nbsp;</p>\n<p>&nbsp;</p>\n</body>\n</html>");
        jLabel2.setOpaque(true);
        panText2.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(20, 160));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );

        panText2.add(jPanel1, java.awt.BorderLayout.WEST);

        getContentPane().add(panText2, java.awt.BorderLayout.CENTER);

        panTool.setPreferredSize(new java.awt.Dimension(300, 40));

        btnClose.setFont(Util.getFont(0, 1));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b005.gif"))); // NOI18N
        btnClose.setText("ОК");
        btnClose.setPreferredSize(new java.awt.Dimension(69, 20));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panToolLayout = new javax.swing.GroupLayout(panTool);
        panTool.setLayout(panToolLayout);
        panToolLayout.setHorizontalGroup(
            panToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panToolLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        panToolLayout.setVerticalGroup(
            panToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panToolLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(panTool, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //mAdmin.AboutBox.window = null;
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labBase;
    private javax.swing.JLabel labConfig;
    private javax.swing.JLabel labImage;
    private javax.swing.JLabel labProjectVersion;
    private javax.swing.JLabel labTitle;
    private javax.swing.JPanel panText1;
    private javax.swing.JPanel panText2;
    private javax.swing.JPanel panTool;
    // End of variables declaration//GEN-END:variables
}
