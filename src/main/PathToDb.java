package main;

import common.FrameToFile;
import common.eProfile;
import common.eProperty;
import dataset.ConnApp;
import dataset.Query;
import java.io.File;
import java.util.Locale;
import javax.swing.JFileChooser;
import dataset.eExcep;
import wincalc.Wincalc;

/**
 * <p>
 * Указать путь к БД </p>
 */
public class PathToDb extends javax.swing.JDialog {

    private Locale locale;

    /**
     * Creates new form PathToDb
     */
    public PathToDb(java.awt.Window owner) {
        super(owner);
        Locale loc = new Locale("en", "US");
        this.setLocale(loc);
        this.getInputContext().selectInputMethod(loc);

        initComponents();

        new FrameToFile(this, btnClose);
        //Загрузка параметров входа
        labMes.setText("");

        if (eProperty.typedb.read().equals(eProperty.fb)) {
            cboxDB.setSelectedIndex(3);
        } else if (eProperty.typedb.read().equals(eProperty.pg)) {
            cboxDB.setSelectedIndex(4);
        }
        edHost.setText(eProperty.server.read());
        edPort.setText(eProperty.port.read());
        edPath.setText(eProperty.base.read());
        edUser.setText(eProperty.user.read());
        edPass.setText(eProperty.password);

        if (Main.dev == false) {
            btnAdm.setVisible(false);
            btnUser.setVisible(false);
            btnFile.setVisible(false);
//            labDB.setVisible(false);
//            cboxDB.setVisible(false);
        }
        if (eProperty.typedb.read().equals(eProperty.fb)) {
            btnFile.setVisible(false);
        }
        onCaretUpdate(null);
    }

    /**
     * Команда на соединение с БД.
     */
    private void ConnectToDb() {

        //Устанавливаем параметры входа
        eProperty.server.write(edHost.getText());
        eProperty.port.write(edPort.getText());
        eProperty.base.write(edPath.getText());
        eProperty.user.write(edUser.getText().trim());
        eProperty.password = String.valueOf(edPass.getPassword());

        String file = edPath.getText();
        int index = file.lastIndexOf(".") + 1;
        if (index == 0 && cboxDB.getSelectedIndex() == 1) {
            eProperty.typedb.write(eProperty.fb);
        } else if (index == 0 && cboxDB.getSelectedIndex() == 4) {
            eProperty.typedb.write(eProperty.pg);
        }
        //создание соединения
        ConnApp con = ConnApp.initConnect();
        eExcep pass = con.createConnection();
        Query.connection = con.getConnection();
        if (pass == eExcep.yesConn) {
            //запуск главного меню
            App1.eApp1.createApp(eProfile.profile);
            dispose();
            //тут мы сохраняем в файл текущего пользователя
            eProperty.storeProperty();

        } else {
            String mes = (Main.dev == true) ? pass.mes + " (код. " + pass.id + ")" : pass.mes;
            labMes.setText(mes);
        }
    }
    
    public static void pathToDb(java.awt.Window owner) {
        PathToDb  pathToDb = new PathToDb(owner);
        FrameToFile.setFrameSize(pathToDb);
        pathToDb.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        labMes = new javax.swing.JLabel();
        labUser1 = new javax.swing.JLabel();
        edHost = new javax.swing.JTextField();
        labUser2 = new javax.swing.JLabel();
        edPath = new javax.swing.JTextField();
        btnFile = new javax.swing.JButton();
        labUser3 = new javax.swing.JLabel();
        edPort = new javax.swing.JTextField();
        cboxDB = new javax.swing.JComboBox();
        labDB = new javax.swing.JLabel();
        labUser = new javax.swing.JLabel();
        labPass = new javax.swing.JLabel();
        edUser = new javax.swing.JTextField();
        edPass = new javax.swing.JPasswordField();
        btnUser = new javax.swing.JButton();
        btnAdm = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Установка соединения с базой данных");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setResizable(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));
        jPanel4.setPreferredSize(new java.awt.Dimension(510, 200));

        labMes.setFont(common.Util.getFont(1,1));
        labMes.setForeground(new java.awt.Color(0, 0, 255));
        labMes.setText("Ошибка соединения с базой данных!");
        labMes.setMaximumSize(new java.awt.Dimension(200, 14));
        labMes.setPreferredSize(new java.awt.Dimension(390, 14));

        labUser1.setFont(common.Util.getFont(1,1));
        labUser1.setText("Сервер");
        labUser1.setAlignmentX(0.5F);
        labUser1.setMaximumSize(new java.awt.Dimension(60, 14));
        labUser1.setPreferredSize(new java.awt.Dimension(60, 14));

        edHost.setFont(common.Util.getFont(0,0));
        edHost.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edHost.setMinimumSize(new java.awt.Dimension(0, 0));
        edHost.setPreferredSize(new java.awt.Dimension(146, 18));
        edHost.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                onCaretUpdate(evt);
            }
        });

        labUser2.setFont(common.Util.getFont(1,1));
        labUser2.setText("Файл БД");
        labUser2.setAlignmentX(0.5F);
        labUser2.setMaximumSize(new java.awt.Dimension(60, 14));
        labUser2.setPreferredSize(new java.awt.Dimension(60, 14));

        edPath.setFont(common.Util.getFont(0,0));
        edPath.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        edPath.setMinimumSize(new java.awt.Dimension(0, 0));
        edPath.setPreferredSize(new java.awt.Dimension(340, 16));
        edPath.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                onCaretUpdate(evt);
            }
        });

        btnFile.setFont(common.Util.getFont(0,0));
        btnFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b033.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/hint"); // NOI18N
        btnFile.setToolTipText(bundle.getString("Выбрать файл")); // NOI18N
        btnFile.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFile.setMaximumSize(new java.awt.Dimension(27, 16));
        btnFile.setMinimumSize(new java.awt.Dimension(17, 16));
        btnFile.setPreferredSize(new java.awt.Dimension(27, 26));
        btnFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileActionPerformed(evt);
            }
        });

        labUser3.setFont(common.Util.getFont(1,1));
        labUser3.setText("Порт");
        labUser3.setAlignmentX(0.5F);

        edPort.setFont(common.Util.getFont(0,0));
        edPort.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPort.setMinimumSize(new java.awt.Dimension(0, 0));
        edPort.setPreferredSize(new java.awt.Dimension(60, 18));

        cboxDB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "...", "Firebird", "Postgres" }));
        cboxDB.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cboxDB.setMinimumSize(new java.awt.Dimension(57, 20));
        cboxDB.setPreferredSize(new java.awt.Dimension(80, 20));
        cboxDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmDef2(evt);
            }
        });

        labDB.setText("База");
        labDB.setMaximumSize(new java.awt.Dimension(30, 14));
        labDB.setPreferredSize(new java.awt.Dimension(30, 14));

        labUser.setFont(common.Util.getFont(1,1));
        labUser.setText("Пользователь");
        labUser.setAlignmentX(0.5F);
        labUser.setMaximumSize(new java.awt.Dimension(120, 14));
        labUser.setPreferredSize(new java.awt.Dimension(96, 14));

        labPass.setFont(common.Util.getFont(1,1));
        labPass.setText("Пароль");
        labPass.setAlignmentX(0.5F);
        labPass.setMaximumSize(new java.awt.Dimension(120, 14));
        labPass.setPreferredSize(new java.awt.Dimension(96, 14));

        edUser.setFont(common.Util.getFont(0,0));
        edUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edUser.setMinimumSize(new java.awt.Dimension(0, 0));
        edUser.setPreferredSize(new java.awt.Dimension(120, 16));
        edUser.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                onCaretUpdate(evt);
            }
        });

        edPass.setFont(common.Util.getFont(0,0));
        edPass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPass.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                onCaretUpdate(evt);
            }
        });

        btnUser.setFont(common.Util.getFont(0,0));
        btnUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnUser.setText("user.");
        btnUser.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnUser.setFocusable(false);
        btnUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUser.setMaximumSize(new java.awt.Dimension(38, 55));
        btnUser.setMinimumSize(new java.awt.Dimension(38, 55));
        btnUser.setPreferredSize(new java.awt.Dimension(38, 55));
        btnUser.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserDef(evt);
            }
        });

        btnAdm.setFont(common.Util.getFont(0,0));
        btnAdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnAdm.setText("admin");
        btnAdm.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnAdm.setFocusable(false);
        btnAdm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdm.setMargin(new java.awt.Insets(20, 0, 0, 0));
        btnAdm.setMaximumSize(new java.awt.Dimension(38, 55));
        btnAdm.setMinimumSize(new java.awt.Dimension(38, 55));
        btnAdm.setPreferredSize(new java.awt.Dimension(38, 55));
        btnAdm.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnAdm.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmDef(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(labUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edHost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(labUser3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(labDB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cboxDB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(labMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(labUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(labPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edPass))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(labUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44)
                        .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(labMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(edHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labUser3)
                    .addComponent(edPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboxDB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(edPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(510, 46));

        btnOk.setFont(common.Util.getFont(0,0));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b028.gif"))); // NOI18N
        btnOk.setText("ОК");
        btnOk.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnOk.setEnabled(false);
        btnOk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOk.setMaximumSize(new java.awt.Dimension(80, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(0, 0));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 25));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b026.gif"))); // NOI18N
        btnHelp.setToolTipText(bundle.getString("Справка")); // NOI18N
        btnHelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnHelp.setFocusable(false);
        btnHelp.setMaximumSize(new java.awt.Dimension(25, 25));
        btnHelp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnHelp.setPreferredSize(new java.awt.Dimension(25, 25));
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpActionPerformed(evt);
            }
        });

        btnClose.setFont(common.Util.getFont(0,0));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b029.gif"))); // NOI18N
        btnClose.setText("Отмена");
        btnClose.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnClose.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClose.setMaximumSize(new java.awt.Dimension(80, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(0, 0));
        btnClose.setPreferredSize(new java.awt.Dimension(80, 25));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //Событие ввода данных
    private void onCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_onCaretUpdate
        labMes.setText("");
        if (!edPath.getText().isEmpty()
                && !edUser.getText().isEmpty()
                && edPass.getPassword().length > 0
                && !edHost.getText().isEmpty()
                && !edPort.getText().isEmpty()) {
            btnUser.setEnabled(true);
            btnAdm.setEnabled(true);
            btnOk.setEnabled(true);
        } else {
            btnUser.setEnabled(false);
            btnAdm.setEnabled(false);
            btnOk.setEnabled(false);
        }
}//GEN-LAST:event_onCaretUpdate
    //Нажал кнопку "ADM"
    private void btnAdmDef(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmDef

        eProperty.logindef(true, edUser, edPass, edHost, edPort, edPath);
        ConnectToDb();
}//GEN-LAST:event_btnAdmDef
    //Нажал кнопку "user"
    private void btnUserDef(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserDef
        if (edUser.getText() == null) {
            edUser.setText("");
        }
        if (edPass.getPassword() == null) {
            edPass.setText("");
        }
        eProperty.logindef(false, edUser, edPass);
        ConnectToDb();
}//GEN-LAST:event_btnUserDef
    //Нажал кнопку "ОК"
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        ConnectToDb();
}//GEN-LAST:event_btnOkActionPerformed
    //Нажал кнопку "ОТМЕНА"
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
}//GEN-LAST:event_btnCloseActionPerformed

    private void btnHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpActionPerformed
        // ExecuteCmd.startHelp(this.getClass().getName());
    }//GEN-LAST:event_btnHelpActionPerformed

    private void btnFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileActionPerformed
        FilesFilter filter = new FilesFilter();
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(filter);
        int result = chooser.showDialog(this, "Выбрать");
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            edPath.setText(path);
        }
}//GEN-LAST:event_btnFileActionPerformed
    //Выбрал тип базы
    private void btnAdmDef2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmDef2

        if (cboxDB.getSelectedIndex() == 1) {
            edHost.setText("localhost");
            edPort.setText("1433");
            edPath.setText("new_db");
            edUser.setText("sa");
            edPass.setText("Platina6");
        } else if (cboxDB.getSelectedIndex() == 2) {
            edHost.setText("localhost");
            edPort.setText("3306");
            edPath.setText("new_db");
            edUser.setText("root");
            edPass.setText("Platina6");
        } else if (cboxDB.getSelectedIndex() == 3) {
            edHost.setText("localhosr");
            edPort.setText("3050");
            edPath.setText("C:\\Director\\DataBase\\schooldemo.gdb");
            btnFile.setVisible(true);
            edUser.setText("sysdba");
            edPass.setText("masterkey");
        } else if (cboxDB.getSelectedIndex() == 4) {
            edHost.setText("localhost");
            edPort.setText("5432");
            edPath.setText("new_db1");
            btnFile.setVisible(true);
            edUser.setText("postgres");
            edPass.setText("Platina6");
        }
        if (cboxDB.getSelectedIndex() == 3) {
            btnFile.setVisible(true);

        } else {
            btnFile.setVisible(false);
        }
    }//GEN-LAST:event_btnAdmDef2
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdm;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnFile;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnUser;
    private javax.swing.JComboBox cboxDB;
    private javax.swing.JTextField edHost;
    private javax.swing.JPasswordField edPass;
    private javax.swing.JTextField edPath;
    private javax.swing.JTextField edPort;
    private javax.swing.JTextField edUser;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel labDB;
    private javax.swing.JLabel labMes;
    private javax.swing.JLabel labPass;
    private javax.swing.JLabel labUser;
    private javax.swing.JLabel labUser1;
    private javax.swing.JLabel labUser2;
    private javax.swing.JLabel labUser3;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    // фильтр, отбирающий файлы
    class FilesFilter extends javax.swing.filechooser.FileFilter {

        // принимаем файл или отказываем ему
        public boolean accept(java.io.File f) {
            // все каталоги принимаем
            if (f.isDirectory()) {
                return true;
            }
            // для файлов смотрим на расширение
            String fileName = f.getName();
            int i = fileName.lastIndexOf('.');
            if ((i > 0) && (i < (fileName.length() - 1))) {
                String fileExt = fileName.substring(i + 1);
                if ("gdb".equalsIgnoreCase(fileExt) || "fdb".equalsIgnoreCase(fileExt)) {
                    return true;
                }
            }
            return false;
        }

        // возвращаем описание фильтра
        public String getDescription() {
            return "Файлы баз данных (*.gdb; *.fdb))";
        }
    }
}
