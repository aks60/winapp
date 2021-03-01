package frames;

import dataset.ConnApp;
import dataset.eExcep;
import common.FrameToFile;
import common.eProfile;
import common.eProperty;
import dataset.Query;
import javax.swing.SwingWorker;
import startup.App;
import startup.Main;

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
            labUser.setPreferredSize(new java.awt.Dimension(110, 18));
            edUser.setPreferredSize(new java.awt.Dimension(120, 18));
            labPass.setPreferredSize(new java.awt.Dimension(110, 18));
            edPass.setPreferredSize(new java.awt.Dimension(120, 18));

        } else {
            eProperty.logindef(false, edUser, edPass);
            connectToDb();
        }
        labMes.setText("");
        edUser.setText(eProperty.user.read());
        edPass.requestFocus();
        getRootPane().setDefaultButton(btnOk);
    }

    /**
     * Команда на соединение с БД.
     */
    private void connectToDb() {
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

                //Создание соединения
                labMes.setText("Установка соединения с базой данных");
                ConnApp con = ConnApp.initConnect();
                int num_base = Integer.valueOf(eProperty.base_num.read());
                eExcep pass = con.createConnection(num_base);
                Query.connection = con.getConnection();
                if (pass == eExcep.yesConn) { //запуск главного меню   
                    
                    if (eProfile.P02.roleSet.contains(con.getRole())) {
                        App.createApp(eProfile.P02);

                    } else if (eProfile.P16.roleSet.contains(con.getRole())) {
                        App.createApp(eProfile.P16);
                    }
                    eProperty.save();  //свойства текущего пользователя
                    dispose();
                } else if (pass == eExcep.noLogin) {
                    labMes.setText(eExcep.noLogin.mes);

                } else if (pass == eExcep.noGrant) {
                    labMes.setText(eExcep.noGrant.mes);

                } else {
                    dispose();
                    PathToDb.pathToDb(null);  //установим путь к базе
                }
                return null;
            }

            public void done() {
                progressBar.setIndeterminate(false);
            }
        }.execute();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan2 = new javax.swing.JPanel();
        labPass = new javax.swing.JLabel();
        edPass = new javax.swing.JPasswordField();
        labUser = new javax.swing.JLabel();
        edUser = new javax.swing.JTextField();
        btnUser = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        btnAdm = new javax.swing.JButton();
        labMes = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Авторизация доступа");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(320, 220));
        setResizable(false);

        pan2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));
        pan2.setPreferredSize(new java.awt.Dimension(290, 132));

        labPass.setFont(frames.Util.getFont(0,0));
        labPass.setText("Пароль");
        labPass.setAlignmentX(0.5F);
        labPass.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        labPass.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        labPass.setPreferredSize(new java.awt.Dimension(76, 18));

        edPass.setFont(frames.Util.getFont(0,0));
        edPass.setText("masterkey");
        edPass.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        edPass.setPreferredSize(new java.awt.Dimension(72, 18));
        edPass.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                passonCaretUpdate(evt);
            }
        });

        labUser.setFont(frames.Util.getFont(0,0));
        labUser.setText("Пользователь");
        labUser.setAlignmentX(0.5F);
        labUser.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        labUser.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        edUser.setFont(frames.Util.getFont(0,0));
        edUser.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        edUser.setMinimumSize(new java.awt.Dimension(0, 0));
        edUser.setPreferredSize(new java.awt.Dimension(72, 18));
        edUser.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                useronCaretUpdate(evt);
            }
        });

        btnUser.setFont(frames.Util.getFont(0,0));
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
                userConnect(evt);
            }
        });

        progressBar.setBorder(null);
        progressBar.setFocusable(false);
        progressBar.setPreferredSize(new java.awt.Dimension(240, 2));
        progressBar.setRequestFocusEnabled(false);
        progressBar.setVerifyInputWhenFocusTarget(false);

        btnAdm.setFont(frames.Util.getFont(0,0));
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
                admConnect(evt);
            }
        });

        labMes.setFont(frames.Util.getFont(0,1));
        labMes.setText("<html>Ошибка соединения с базой данных!");
        labMes.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        labMes.setPreferredSize(new java.awt.Dimension(240, 14));

        javax.swing.GroupLayout pan2Layout = new javax.swing.GroupLayout(pan2);
        pan2.setLayout(pan2Layout);
        pan2Layout.setHorizontalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(edPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44))
        );
        pan2Layout.setVerticalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        btnOk.setFont(frames.Util.getFont(0,0));
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
                okAction(evt);
            }
        });

        btnClose.setFont(frames.Util.getFont(0,0));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b029.gif"))); // NOI18N
        btnClose.setText("Отмена");
        btnClose.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnClose.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClose.setMaximumSize(new java.awt.Dimension(80, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(0, 0));
        btnClose.setPreferredSize(new java.awt.Dimension(80, 25));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeAction(evt);
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
                        .addGap(110, 110, 110)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAction
        System.exit(0);
    }//GEN-LAST:event_closeAction

    private void okAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okAction
        connectToDb();
    }//GEN-LAST:event_okAction

    private void admConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_admConnect
        eProperty.logindef(true, edUser, edPass);
        connectToDb();
    }//GEN-LAST:event_admConnect

    private void userConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userConnect
        eProperty.logindef(false, edUser, edPass);
        connectToDb();
    }//GEN-LAST:event_userConnect

    private void passonCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_passonCaretUpdate
        labMes.setText("");
        if (edPass.getPassword().length > 0 && !edUser.getText().isEmpty()) {
            btnOk.setEnabled(true);
        } else {
            btnOk.setEnabled(false);
        }
    }//GEN-LAST:event_passonCaretUpdate

    private void useronCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_useronCaretUpdate
        labMes.setText("");
        if (edPass.getPassword().length > 0 && !edUser.getText().isEmpty()) {
            btnOk.setEnabled(true);
        } else {
            btnOk.setEnabled(false);
        }
    }//GEN-LAST:event_useronCaretUpdate
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdm;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnUser;
    private javax.swing.JPasswordField edPass;
    private javax.swing.JTextField edUser;
    private javax.swing.JLabel labMes;
    private javax.swing.JLabel labPass;
    private javax.swing.JLabel labUser;
    private javax.swing.JPanel pan2;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
