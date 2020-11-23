package convert;

import common.eProperty;
import dataset.ConnApp;
import dataset.ConnFb;
import dataset.Query;
import dataset.eExcep;
import java.awt.Color;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Convert extends javax.swing.JFrame {

    private JTextField smallField, bigField;

    public Convert() {
        initComponents();
        initElements();
        loadingModel();
    }

    private void loadingModel() {
        //localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251";
        if (eProperty.base_num.read().equals("1")) {
            labPath2.setText(eProperty.server1.read() + "/" + eProperty.port.read() + "\\" + eProperty.base1.read());
            edPath.setText("D:\\Okna\\Database\\Profstroy4\\ITEST.FDB");
        } else if (eProperty.base_num.read().equals("2")) {
            labPath2.setText(eProperty.server2.read() + "/" + eProperty.port.read() + "\\" + eProperty.base2.read());
            edPath.setText("D:\\Okna\\Database\\Sialbase2\\base3.fdb");
        } else if (eProperty.base_num.read().equals("3")) {
            labPath2.setText(eProperty.server3.read() + "/" + eProperty.port.read() + "\\" + eProperty.base3.read());
            edPath.setText("D:\\Okna\\Database\\Sialbase2\\base4.fdb");
        }
        edPort.setText("3050");
        edServer.setText("localhost");
        edUser.setText("sysdba");
        edPass.setText("masterkey");
    }

    private void test() {
        appendToPane(txtPane, "1111111111111\n", Color.RED);
        appendToPane(txtPane, "2222222222222\n", Color.BLUE);
        appendToPane(txtPane, "3333333333333\n", Color.GREEN);
        appendToPane(txtPane, "4444444444444", Color.MAGENTA);
        appendToPane(txtPane, "5555555555555\n", Color.ORANGE);
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNorth = new javax.swing.JPanel();
        pan5 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        lab2 = new javax.swing.JLabel();
        lab3 = new javax.swing.JLabel();
        lab4 = new javax.swing.JLabel();
        edServer = new javax.swing.JTextField();
        edPath = new javax.swing.JTextField();
        edUser = new javax.swing.JTextField();
        edPass = new javax.swing.JPasswordField();
        lab5 = new javax.swing.JLabel();
        edPort = new javax.swing.JTextField();
        btnTest = new javax.swing.JButton();
        pan4 = new javax.swing.JPanel();
        labPath2 = new javax.swing.JLabel();
        panCent = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        txtPane = new javax.swing.JTextPane();
        panSouth = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnExit1 = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Конвертор баз данных");
        setPreferredSize(new java.awt.Dimension(700, 650));

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setPreferredSize(new java.awt.Dimension(700, 120));
        panNorth.setLayout(new javax.swing.BoxLayout(panNorth, javax.swing.BoxLayout.PAGE_AXIS));

        pan5.setBorder(javax.swing.BorderFactory.createTitledBorder("  База данных источник"));
        pan5.setPreferredSize(new java.awt.Dimension(500, 70));

        lab1.setFont(frames.Util.getFont(0,0));
        lab1.setText("Cервер (host)");
        lab1.setAlignmentX(0.5F);
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab1.setMaximumSize(new java.awt.Dimension(100, 18));
        lab1.setMinimumSize(new java.awt.Dimension(100, 18));
        lab1.setPreferredSize(new java.awt.Dimension(100, 18));

        lab2.setFont(frames.Util.getFont(0,0));
        lab2.setText("Путь к базе");
        lab2.setAlignmentX(0.5F);
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab2.setMaximumSize(new java.awt.Dimension(100, 18));
        lab2.setMinimumSize(new java.awt.Dimension(100, 18));
        lab2.setPreferredSize(new java.awt.Dimension(100, 18));

        lab3.setFont(frames.Util.getFont(0,0));
        lab3.setText("Пользователь");
        lab3.setAlignmentX(0.5F);
        lab3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab3.setMaximumSize(new java.awt.Dimension(100, 18));
        lab3.setMinimumSize(new java.awt.Dimension(100, 18));
        lab3.setPreferredSize(new java.awt.Dimension(100, 18));

        lab4.setFont(frames.Util.getFont(0,0));
        lab4.setText("Пароль");
        lab4.setAlignmentX(0.5F);
        lab4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab4.setPreferredSize(new java.awt.Dimension(60, 18));

        edServer.setFont(frames.Util.getFont(0,0));
        edServer.setText("localhost");
        edServer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edServer.setMinimumSize(new java.awt.Dimension(0, 0));
        edServer.setPreferredSize(new java.awt.Dimension(120, 16));

        edPath.setFont(frames.Util.getFont(0,0));
        edPath.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        edPath.setMinimumSize(new java.awt.Dimension(0, 0));
        edPath.setPreferredSize(new java.awt.Dimension(80, 16));

        edUser.setFont(frames.Util.getFont(0,0));
        edUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edUser.setMinimumSize(new java.awt.Dimension(0, 0));
        edUser.setPreferredSize(new java.awt.Dimension(120, 16));

        edPass.setFont(frames.Util.getFont(0,0));
        edPass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPass.setPreferredSize(new java.awt.Dimension(80, 16));

        lab5.setFont(frames.Util.getFont(0,0));
        lab5.setText("Порт");
        lab5.setAlignmentX(0.5F);
        lab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab5.setMaximumSize(new java.awt.Dimension(40, 18));
        lab5.setMinimumSize(new java.awt.Dimension(40, 18));
        lab5.setPreferredSize(new java.awt.Dimension(60, 18));

        edPort.setFont(frames.Util.getFont(0,0));
        edPort.setText("3050");
        edPort.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPort.setMinimumSize(new java.awt.Dimension(0, 0));
        edPort.setPreferredSize(new java.awt.Dimension(80, 16));

        btnTest.setFont(frames.Util.getFont(0,0));
        btnTest.setText("Тест соединения");
        btnTest.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTest.setMaximumSize(new java.awt.Dimension(80, 25));
        btnTest.setMinimumSize(new java.awt.Dimension(0, 0));
        btnTest.setPreferredSize(new java.awt.Dimension(102, 25));
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestBtnStartClick(evt);
            }
        });

        javax.swing.GroupLayout pan5Layout = new javax.swing.GroupLayout(pan5);
        pan5.setLayout(pan5Layout);
        pan5Layout.setHorizontalGroup(
            pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan5Layout.createSequentialGroup()
                        .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan5Layout.createSequentialGroup()
                        .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan5Layout.createSequentialGroup()
                        .addGap(0, 18, Short.MAX_VALUE)
                        .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(edPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan5Layout.setVerticalGroup(
            pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panNorth.add(pan5);

        pan4.setBorder(javax.swing.BorderFactory.createTitledBorder(" База данных приёмник"));
        pan4.setPreferredSize(new java.awt.Dimension(500, 40));

        labPath2.setBackground(new java.awt.Color(255, 255, 255));
        labPath2.setFont(frames.Util.getFont(0,0));
        labPath2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        labPath2.setOpaque(true);
        labPath2.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout pan4Layout = new javax.swing.GroupLayout(pan4);
        pan4.setLayout(pan4Layout);
        pan4Layout.setHorizontalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labPath2, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                .addContainerGap())
        );
        pan4Layout.setVerticalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan4Layout.createSequentialGroup()
                .addComponent(labPath2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        panNorth.add(pan4);

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCent.setPreferredSize(new java.awt.Dimension(20, 20));
        panCent.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        txtPane.setBorder(null);
        scr1.setViewportView(txtPane);

        panCent.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panCent, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setPreferredSize(new java.awt.Dimension(700, 30));

        btnExit.setFont(frames.Util.getFont(0,0));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b009.gif"))); // NOI18N
        btnExit.setText("Выход");
        btnExit.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnExit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnExit.setMaximumSize(new java.awt.Dimension(80, 25));
        btnExit.setMinimumSize(new java.awt.Dimension(0, 0));
        btnExit.setPreferredSize(new java.awt.Dimension(80, 25));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit(evt);
            }
        });

        btnExit1.setFont(frames.Util.getFont(0,0));
        btnExit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b026.gif"))); // NOI18N
        btnExit1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnExit1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnExit1.setMaximumSize(new java.awt.Dimension(25, 25));
        btnExit1.setMinimumSize(new java.awt.Dimension(0, 0));
        btnExit1.setPreferredSize(new java.awt.Dimension(25, 25));

        btnStart.setFont(frames.Util.getFont(0,0));
        btnStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b059.gif"))); // NOI18N
        btnStart.setText("Конвертировать");
        btnStart.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnStart.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnStart.setMaximumSize(new java.awt.Dimension(80, 25));
        btnStart.setMinimumSize(new java.awt.Dimension(0, 0));
        btnStart.setPreferredSize(new java.awt.Dimension(120, 25));
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartBtnStartClick(evt);
            }
        });

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSouthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnExit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 380, Short.MAX_VALUE)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panSouthLayout.createSequentialGroup()
                .addComponent(btnExit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTestBtnStartClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestBtnStartClick

        ConnApp Src = new ConnFb();
        eExcep excep = Src.createConnection(edServer.getText().trim(), edPort.getText().trim(),
                edPath.getText().trim(), edUser.getText().trim(), edPass.getPassword());
        JOptionPane.showMessageDialog(this, excep.mes, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnTestBtnStartClick

    private void btnExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit
        this.dispose();
    }//GEN-LAST:event_btnExit

    private void btnStartBtnStartClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartBtnStartClick
        try {
            Query.connection.close();
            eProperty.user.write("sysdba");
            eProperty.password = String.valueOf("masterkey");
            int num_base = Integer.valueOf(eProperty.base_num.read());
            ConnApp con1 = ConnApp.initConnect();
            con1.createConnection(num_base);
            Connection c1 = con1.getConnection();

            ConnApp con2 = new ConnFb();
            eExcep excep = con2.createConnection(edServer.getText().trim(), edPort.getText().trim(),
                    edPath.getText().trim(), edUser.getText().trim(), edPass.getPassword());
            Connection c2 = con2.getConnection();

            if (excep.yesConn != excep) {
                JOptionPane.showMessageDialog(this, excep.mes, "Сообщение", JOptionPane.INFORMATION_MESSAGE);

            } else {
                Profstroy.convert(txtPane, c1, c2);
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
           //Query.connection. 
        }
    }//GEN-LAST:event_btnStartBtnStartClick

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnExit1;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnTest;
    private javax.swing.JPasswordField edPass;
    private javax.swing.JTextField edPath;
    private javax.swing.JTextField edPort;
    private javax.swing.JTextField edServer;
    private javax.swing.JTextField edUser;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab3;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab5;
    private javax.swing.JLabel labPath2;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel panCent;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JTextPane txtPane;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

    }
}
