package frames;

import common.eProperty;
import java.awt.GraphicsEnvironment;
import java.io.File;
import javax.swing.DefaultComboBoxModel;

/**
 * <p>
 * ��������� ������� </p>
 */
public class Property extends javax.swing.JDialog {

    private String[] fontName = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    public Property(java.awt.Window owner) {
        super(owner);
        initComponents();

        txtWord.setText(eProperty.cmd_word.read());
        txtExcel.setText(eProperty.cmd_excel.read());
        txtHtml.setText(eProperty.cmd_html.read());
        
        txtURL.setText(eProperty.url_src.read());
        Integer num = Integer.valueOf(eProperty.web_port.read());
        boolean start = (eProperty.web_start.read().equals("true") == true) ? true : false;

        comboBox.setModel(new DefaultComboBoxModel(fontName));
        String name = eProperty.fontname.read();
        Integer size = Integer.valueOf(eProperty.fontsize.read());
        for (int i = 0; i < fontName.length; i++) {
            if (name.equals(fontName[i])) {
                comboBox.setSelectedIndex(i);
            }
        }
        spinner2.setValue(size);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panCentr = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnProp2 = new javax.swing.JButton();
        comboBox = new javax.swing.JComboBox();
        spinner2 = new javax.swing.JSpinner();
        pan3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnProp3 = new javax.swing.JButton();
        txtWord = new javax.swing.JTextField();
        txtExcel = new javax.swing.JTextField();
        txtHtml = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        pan4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtURL = new javax.swing.JTextField();
        btnProp4 = new javax.swing.JButton();
        panTool = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Параметры системные");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/image/px32/d033.gif")).getImage()));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panCentr.setPreferredSize(new java.awt.Dimension(500, 370));
        panCentr.setLayout(new javax.swing.BoxLayout(panCentr, javax.swing.BoxLayout.PAGE_AXIS));

        pan2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Шрифт"));
        pan2.setPreferredSize(new java.awt.Dimension(500, 100));

        jLabel5.setText("<html>Установка шрифта программы<br> \n\n");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel5.setPreferredSize(new java.awt.Dimension(158, 64));

        jLabel7.setText("Font\n");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel7.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel8.setText("Размер");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel8.setPreferredSize(new java.awt.Dimension(44, 18));

        btnProp2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/px16/b031.gif"))); // NOI18N
        btnProp2.setText("Сохранить шрифт в настройках программы");
        btnProp2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnProp2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProp2.setMaximumSize(new java.awt.Dimension(80, 25));
        btnProp2.setMinimumSize(new java.awt.Dimension(0, 0));
        btnProp2.setPreferredSize(new java.awt.Dimension(280, 25));
        btnProp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProp2(evt);
            }
        });

        comboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBox.setBorder(null);
        comboBox.setMinimumSize(new java.awt.Dimension(100, 18));
        comboBox.setPreferredSize(new java.awt.Dimension(100, 20));

        spinner2.setBorder(null);
        spinner2.setMinimumSize(new java.awt.Dimension(36, 18));
        spinner2.setPreferredSize(new java.awt.Dimension(36, 20));

        javax.swing.GroupLayout pan2Layout = new javax.swing.GroupLayout(pan2);
        pan2.setLayout(pan2Layout);
        pan2Layout.setHorizontalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinner2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnProp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pan2Layout.setVerticalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinner2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnProp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        panCentr.add(pan2);

        pan3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Офис"));
        pan3.setPreferredSize(new java.awt.Dimension(500, 150));
        pan3.setRequestFocusEnabled(false);

        jLabel6.setText("<html> &nbsp;Измените путь к программам<br>\n&nbsp;запуска отчётов, если на<br> \n&nbsp;вашем компьютере установка<br>\n&nbsp;Office  была изменена  ");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel6.setPreferredSize(new java.awt.Dimension(158, 70));

        btnProp3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/px16/b031.gif"))); // NOI18N
        btnProp3.setText("Сохранить параметры отчётов");
        btnProp3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnProp3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProp3.setMaximumSize(new java.awt.Dimension(80, 25));
        btnProp3.setMinimumSize(new java.awt.Dimension(0, 0));
        btnProp3.setPreferredSize(new java.awt.Dimension(280, 25));
        btnProp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProp3(evt);
            }
        });

        txtWord.setAutoscrolls(false);
        txtWord.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtWord.setMinimumSize(new java.awt.Dimension(236, 18));
        txtWord.setPreferredSize(new java.awt.Dimension(236, 18));

        txtExcel.setAutoscrolls(false);
        txtExcel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtExcel.setMinimumSize(new java.awt.Dimension(236, 18));
        txtExcel.setPreferredSize(new java.awt.Dimension(236, 18));

        txtHtml.setAutoscrolls(false);
        txtHtml.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtHtml.setMinimumSize(new java.awt.Dimension(236, 18));
        txtHtml.setPreferredSize(new java.awt.Dimension(236, 18));

        jLabel3.setText("Word");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel3.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel4.setText("Excel");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel4.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel9.setText("HTML");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel9.setPreferredSize(new java.awt.Dimension(40, 18));

        javax.swing.GroupLayout pan3Layout = new javax.swing.GroupLayout(pan3);
        pan3.setLayout(pan3Layout);
        pan3Layout.setHorizontalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnProp3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan3Layout.createSequentialGroup()
                        .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHtml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        pan3Layout.setVerticalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHtml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addComponent(btnProp3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        panCentr.add(pan3);

        pan4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Сервер обновлений"));

        jLabel11.setText("<html>С этого сервера будут<br>скачиваться обновления<br>   ");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel11.setPreferredSize(new java.awt.Dimension(158, 40));

        jLabel13.setText("Путь");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel13.setPreferredSize(new java.awt.Dimension(40, 18));

        txtURL.setAutoscrolls(false);
        txtURL.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtURL.setMinimumSize(new java.awt.Dimension(236, 18));
        txtURL.setPreferredSize(new java.awt.Dimension(236, 18));

        btnProp4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/px16/b031.gif"))); // NOI18N
        btnProp4.setText("Сохранить путь к серверу");
        btnProp4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnProp4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProp4.setMaximumSize(new java.awt.Dimension(80, 25));
        btnProp4.setMinimumSize(new java.awt.Dimension(0, 0));
        btnProp4.setPreferredSize(new java.awt.Dimension(280, 25));
        btnProp4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProp4(evt);
            }
        });

        javax.swing.GroupLayout pan4Layout = new javax.swing.GroupLayout(pan4);
        pan4.setLayout(pan4Layout);
        pan4Layout.setHorizontalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan4Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnProp4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan4Layout.setVerticalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan4Layout.createSequentialGroup()
                        .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnProp4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(31, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        panCentr.add(pan4);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panTool.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panTool.setPreferredSize(new java.awt.Dimension(500, 50));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/px16/b009.gif"))); // NOI18N
        btnExit.setText("Выход");
        btnExit.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnExit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnExit.setMaximumSize(new java.awt.Dimension(80, 25));
        btnExit.setMinimumSize(new java.awt.Dimension(0, 0));
        btnExit.setPreferredSize(new java.awt.Dimension(100, 25));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit(evt);
            }
        });

        btnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/px24/c026.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/hint"); // NOI18N
        btnHelp.setToolTipText(bundle.getString("Справка")); // NOI18N
        btnHelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnHelp.setFocusable(false);
        btnHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHelp.setMaximumSize(new java.awt.Dimension(25, 25));
        btnHelp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnHelp.setPreferredSize(new java.awt.Dimension(25, 25));
        btnHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panToolLayout = new javax.swing.GroupLayout(panTool);
        panTool.setLayout(panToolLayout);
        panToolLayout.setHorizontalGroup(
            panToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panToolLayout.createSequentialGroup()
                .addContainerGap(386, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panToolLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(461, Short.MAX_VALUE)))
        );
        panToolLayout.setVerticalGroup(
            panToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panToolLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panToolLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(panTool, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit
        this.dispose();
}//GEN-LAST:event_btnExit

    private void btnHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpActionPerformed
        //ExecuteCmd.startHelp(this.getClass().getName());
}//GEN-LAST:event_btnHelpActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //mAdmin.Property.window = null;
    }//GEN-LAST:event_formWindowClosed

    private void btnProp2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProp2
        eProperty.fontname.write(fontName[comboBox.getSelectedIndex()]);
        eProperty.fontsize.write(String.valueOf(spinner2.getValue()));
        eProperty.save();
    }//GEN-LAST:event_btnProp2

    private void btnProp3(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProp3
        eProperty.cmd_word.write(txtWord.getText());
        eProperty.cmd_excel.write(txtExcel.getText());
        eProperty.cmd_html.write(txtHtml.getText());
        eProperty.save();
}//GEN-LAST:event_btnProp3

    private void btnProp4(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProp4
        eProperty.url_src.write(txtURL.getText());
        eProperty.save();
    }//GEN-LAST:event_btnProp4

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnProp2;
    private javax.swing.JButton btnProp3;
    private javax.swing.JButton btnProp4;
    private javax.swing.JComboBox comboBox;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panTool;
    private javax.swing.JSpinner spinner2;
    private javax.swing.JTextField txtExcel;
    private javax.swing.JTextField txtHtml;
    private javax.swing.JTextField txtURL;
    private javax.swing.JTextField txtWord;
    // End of variables declaration//GEN-END:variables
}
