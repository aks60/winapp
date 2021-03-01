package startup;

import builder.Wincalc;
import frames.Util;
import java.awt.Frame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.JCheckBoxMenuItem;
import common.ListenerFrame;

public class Man extends javax.swing.JFrame {

    private Locale locale;
    private Wincalc iwin = new Wincalc();
    private javax.swing.Timer timer = null;
    private ListenerFrame listenerMenu;
    private HashMap<String, JCheckBoxMenuItem> hmLookAndFill = new HashMap();

    public Man() {
        initComponents();
        initElements();

        locale = this.getLocale();
        Locale loc = new Locale("ru", "RU");
        this.setLocale(loc);
        this.getInputContext().selectInputMethod(loc);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppmMain = new javax.swing.JPopupMenu();
        mn01 = new javax.swing.JMenu();
        mn11 = new javax.swing.JMenuItem();
        mn12 = new javax.swing.JMenuItem();
        mn02 = new javax.swing.JMenuItem();
        pan1 = new javax.swing.JPanel();
        btnMenu = new javax.swing.JButton();
        pan2 = new javax.swing.JPanel();
        btnSaveFile = new javax.swing.JButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 2), new java.awt.Dimension(0, 32767));
        pan3 = new javax.swing.JPanel();
        tab4 = new javax.swing.JTabbedPane();
        pan4 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        pan5 = new javax.swing.JPanel();
        pan6 = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();

        ppmMain.setPreferredSize(new java.awt.Dimension(120, 120));

        mn01.setText("Данные                                   ");

        mn11.setText("Текст ячейки");
        mn11.setName("1"); // NOI18N
        mn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn11cellValueType(evt);
            }
        });
        mn01.add(mn11);

        mn12.setText("Целое число");
        mn12.setName("2"); // NOI18N
        mn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn12cellValueType(evt);
            }
        });
        mn01.add(mn12);

        ppmMain.add(mn01);

        mn02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        mn02.setText("Выход");
        mn02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnExit(evt);
            }
        });
        ppmMain.add(mn02);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SA-OKNA   <АРМ Менеджер>");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(659, 100));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                wndowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                wndowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                Man.this.windowIconified(evt);
            }
        });

        pan1.setBackground(new java.awt.Color(255, 255, 255));
        pan1.setAlignmentY(4.0F);
        pan1.setPreferredSize(new java.awt.Dimension(120, 80));
        pan1.setLayout(new java.awt.BorderLayout());

        btnMenu.setBackground(new java.awt.Color(237, 235, 231));
        btnMenu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnMenu.setText("Меню");
        btnMenu.setActionCommand("");
        btnMenu.setAlignmentY(0.0F);
        btnMenu.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        btnMenu.setFocusable(false);
        btnMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu.setIconTextGap(0);
        btnMenu.setPreferredSize(new java.awt.Dimension(64, 22));
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenubtnMainMenu(evt);
            }
        });
        pan1.add(btnMenu, java.awt.BorderLayout.CENTER);

        pan2.setBackground(new java.awt.Color(192, 224, 236));
        pan2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true));
        pan2.setPreferredSize(new java.awt.Dimension(64, 38));
        pan2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 6));

        btnSaveFile.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnSaveFile.setText("Сохранить книгу");
        btnSaveFile.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSaveFile.setMaximumSize(new java.awt.Dimension(120, 30));
        btnSaveFile.setMinimumSize(new java.awt.Dimension(87, 26));
        btnSaveFile.setPreferredSize(new java.awt.Dimension(96, 26));
        btnSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFile(evt);
            }
        });
        pan2.add(btnSaveFile);

        pan1.add(pan2, java.awt.BorderLayout.SOUTH);
        pan1.add(filler7, java.awt.BorderLayout.NORTH);

        getContentPane().add(pan1, java.awt.BorderLayout.WEST);

        pan3.setPreferredSize(new java.awt.Dimension(771, 80));
        pan3.setLayout(new java.awt.BorderLayout());

        pan4.setBackground(new java.awt.Color(192, 224, 236));

        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        btn1.setText("Выход");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(120, 26));
        btn1.setMinimumSize(new java.awt.Dimension(26, 26));
        btn1.setPreferredSize(new java.awt.Dimension(80, 26));

        javax.swing.GroupLayout pan4Layout = new javax.swing.GroupLayout(pan4);
        pan4.setLayout(pan4Layout);
        pan4Layout.setHorizontalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan4Layout.createSequentialGroup()
                .addContainerGap(746, Short.MAX_VALUE)
                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan4Layout.setVerticalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab4.addTab("tab1", pan4);

        pan5.setBackground(new java.awt.Color(192, 224, 236));

        javax.swing.GroupLayout pan5Layout = new javax.swing.GroupLayout(pan5);
        pan5.setLayout(pan5Layout);
        pan5Layout.setHorizontalGroup(
            pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 836, Short.MAX_VALUE)
        );
        pan5Layout.setVerticalGroup(
            pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );

        tab4.addTab("tab2", pan5);

        pan6.setBackground(new java.awt.Color(192, 224, 236));

        javax.swing.GroupLayout pan6Layout = new javax.swing.GroupLayout(pan6);
        pan6.setLayout(pan6Layout);
        pan6Layout.setHorizontalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 836, Short.MAX_VALUE)
        );
        pan6Layout.setVerticalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );

        tab4.addTab("tab3", pan6);

        pan3.add(tab4, java.awt.BorderLayout.CENTER);

        getContentPane().add(pan3, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(10, 80));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );

        getContentPane().add(pan7, java.awt.BorderLayout.EAST);

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenubtnMainMenu(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenubtnMainMenu
        ppmMain.show(pan1, btnMenu.getX(), pan1.getY() + 22);
    }//GEN-LAST:event_btnMenubtnMainMenu

    private void btnSaveFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFile

    }//GEN-LAST:event_btnSaveFile

    private void mn11cellValueType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn11cellValueType

    }//GEN-LAST:event_mn11cellValueType

    private void mn12cellValueType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn12cellValueType

    }//GEN-LAST:event_mn12cellValueType

    private void mnExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnExit
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.dispose());
    }//GEN-LAST:event_mnExit

    private void wndowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_wndowClosed
        this.setLocale(locale);
        this.getInputContext().selectInputMethod(locale);
    }//GEN-LAST:event_wndowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        mnExit(null);
    }//GEN-LAST:event_formWindowClosing

    private void wndowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_wndowDeiconified
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.setState(Frame.NORMAL));
    }//GEN-LAST:event_wndowDeiconified

    private void windowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowIconified
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.setState(Frame.ICONIFIED));
    }//GEN-LAST:event_windowIconified

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSaveFile;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JMenu mn01;
    private javax.swing.JMenuItem mn02;
    private javax.swing.JMenuItem mn11;
    private javax.swing.JMenuItem mn12;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPopupMenu ppmMain;
    private javax.swing.JTabbedPane tab4;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    
    private void initElements() {
        setTitle(getTitle() + Util.designName());
    }
}
