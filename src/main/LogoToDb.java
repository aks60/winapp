package main;

import dataset.ConnApp;
import dataset.eExcep;
import common.FrameToFile;
import common.eProfile;
import common.eProperty;
import dataset.Query;
import javax.swing.SwingWorker;
import calculate.Wincalc;

/**
 * <p>
 * Установка соединения </p>
 */
public class LogoToDb extends javax.swing.JDialog {

    private int countCon = 0;

    public LogoToDb(java.awt.Window owner) {
        super(owner);

        initComponents();

        new FrameToFile(this, btnClose);
        if (Main.dev == false) {
            btnAdm.setVisible(false);
            btnUser.setVisible(false);
            labUser.setPreferredSize(new java.awt.Dimension(120, 18));
            edUser.setPreferredSize(new java.awt.Dimension(120, 18));
            labPass.setPreferredSize(new java.awt.Dimension(120, 18));
            edPass.setPreferredSize(new java.awt.Dimension(120, 18));

        } else {
            eProperty.logindef(false, edUser, edPass);
            ConnectToDb();
        }
        labMes.setText("");
        edUser.setText(eProperty.user.read());
        edPass.requestFocus();
        getRootPane().setDefaultButton(btnOk);
    }

    /**
     * Команда на соединение с БД.
     */
    private void ConnectToDb() {
        labMes.setText("");
        ++countCon;
        if (countCon > 3) {
            dispose();
            PathToDb.pathToDb(null);
        }

        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                progressBar.setIndeterminate(true);
                //Загрузка параметров входа
                eProperty.user.write(edUser.getText());
                eProperty.password = String.valueOf(edPass.getPassword());
                //создание соединения
                labMes.setText("Установка соединения с базой данных");
                ConnApp con = ConnApp.initConnect();
                eExcep pass = con.createConnection();
                Query.connection = con.getConnection();
                if (pass == eExcep.yesConn) {
                    //запуск главного меню
                    App1.eApp1.createApp(eProfile.profile);
                    dispose();
                    //тут мы сохраняем в файл текущего пользователя
                    eProperty.save();
                } else if (pass == eExcep.noLogin) {
                    labMes.setText(eExcep.noLogin.mes);
                } else if (pass == eExcep.noGrant) {
                    labMes.setText(eExcep.noGrant.mes);
                } else {
                    dispose();
                    //установим путь к базе
                    PathToDb.pathToDb(null);
                }
                return null;
            }

            public void done() {
                progressBar.setIndeterminate(false);
            }
        }.execute();
    }
    public static void logoToDb() {
        LogoToDb frame = new LogoToDb(null);
        FrameToFile.setFrameSize(frame);
        frame.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        labPass = new javax.swing.JLabel();
        edPass = new javax.swing.JPasswordField();
        labUser = new javax.swing.JLabel();
        edUser = new javax.swing.JTextField();
        btnUser = new javax.swing.JButton();
        btnAdm = new javax.swing.JButton();
        labMes = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Авторизация доступа");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(372, 220));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));
        jPanel2.setPreferredSize(new java.awt.Dimension(342, 150));

        progressBar.setBorder(null);
        progressBar.setPreferredSize(new java.awt.Dimension(336, 2));
        progressBar.setRequestFocusEnabled(false);

        labPass.setFont(common.Util.getFont(0,0));
        labPass.setText("Пароль");
        labPass.setAlignmentX(0.5F);
        labPass.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        labPass.setPreferredSize(new java.awt.Dimension(90, 18));

        edPass.setFont(common.Util.getFont(0,0));
        edPass.setText("masterkey");
        edPass.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        edPass.setPreferredSize(new java.awt.Dimension(82, 18));
        edPass.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                edPassonCaretUpdate(evt);
            }
        });
        edPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edPassActionPerformed(evt);
            }
        });

        labUser.setFont(common.Util.getFont(0,0));
        labUser.setText("Пользователь");
        labUser.setAlignmentX(0.5F);
        labUser.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        labUser.setPreferredSize(new java.awt.Dimension(90, 18));

        edUser.setFont(common.Util.getFont(0,0));
        edUser.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        edUser.setMinimumSize(new java.awt.Dimension(0, 0));
        edUser.setPreferredSize(new java.awt.Dimension(82, 18));
        edUser.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                edUseronCaretUpdate(evt);
            }
        });

        btnUser.setFont(common.Util.getFont(0,0));
        btnUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d001.gif"))); // NOI18N
        btnUser.setText("user");
        btnUser.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnUser.setFocusable(false);
        btnUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUser.setMaximumSize(new java.awt.Dimension(38, 55));
        btnUser.setMinimumSize(new java.awt.Dimension(38, 55));
        btnUser.setPreferredSize(new java.awt.Dimension(42, 58));
        btnUser.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserConnect(evt);
            }
        });

        btnAdm.setFont(common.Util.getFont(0,0));
        btnAdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d001.gif"))); // NOI18N
        btnAdm.setText("admin");
        btnAdm.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnAdm.setFocusable(false);
        btnAdm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdm.setMargin(new java.awt.Insets(20, 0, 0, 0));
        btnAdm.setMaximumSize(new java.awt.Dimension(38, 55));
        btnAdm.setMinimumSize(new java.awt.Dimension(38, 55));
        btnAdm.setPreferredSize(new java.awt.Dimension(42, 58));
        btnAdm.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnAdm.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmConnect(evt);
            }
        });

        labMes.setFont(common.Util.getFont(0,1));
        labMes.setText("<html>Ошибка соединения с базой данных!");
        labMes.setMaximumSize(new java.awt.Dimension(200, 14));
        labMes.setPreferredSize(new java.awt.Dimension(320, 14));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labMes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(edPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(edUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(labMes, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        btnOk.setFont(common.Util.getFont(0,0));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b028.gif"))); // NOI18N
        btnOk.setText("ОК");
        btnOk.setToolTipText("");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        ConnectToDb();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnAdmConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmConnect
        eProperty.logindef(true, edUser, edPass);
        ConnectToDb();
    }//GEN-LAST:event_btnAdmConnect

    private void btnUserConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserConnect
        eProperty.logindef(false, edUser, edPass);
        ConnectToDb();
    }//GEN-LAST:event_btnUserConnect

    private void edPassonCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_edPassonCaretUpdate
        labMes.setText("");
        if (edPass.getPassword().length > 0 && !edUser.getText().isEmpty()) {
            btnOk.setEnabled(true);
        } else {
            btnOk.setEnabled(false);
        }
    }//GEN-LAST:event_edPassonCaretUpdate

    private void edUseronCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_edUseronCaretUpdate
        labMes.setText("");
        if (edPass.getPassword().length > 0 && !edUser.getText().isEmpty()) {
            btnOk.setEnabled(true);
        } else {
            btnOk.setEnabled(false);
        }
    }//GEN-LAST:event_edUseronCaretUpdate

    private void edPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edPassActionPerformed
        //
    }//GEN-LAST:event_edPassActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
//        if (Main.dev == true) {
//            this.setVisible(false);       
//        }
    }//GEN-LAST:event_formWindowActivated
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdm;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnUser;
    private javax.swing.JPasswordField edPass;
    private javax.swing.JTextField edUser;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labMes;
    private javax.swing.JLabel labPass;
    private javax.swing.JLabel labUser;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
