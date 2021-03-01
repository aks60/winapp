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
        pan8 = new javax.swing.JPanel();
        pan9 = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        btnMenu = new javax.swing.JButton();
        pan2 = new javax.swing.JPanel();
        btn2 = new javax.swing.JButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 2), new java.awt.Dimension(0, 32767));
        pan3 = new javax.swing.JPanel();
        tab4 = new javax.swing.JTabbedPane();
        pan4 = new javax.swing.JPanel();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn11 = new javax.swing.JButton();
        pan5 = new javax.swing.JPanel();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        pan6 = new javax.swing.JPanel();
        btn8 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn10 = new javax.swing.JButton();
        pan7 = new javax.swing.JPanel();
        pan11 = new javax.swing.JPanel();
        pan10 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();

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

        pan9.setPreferredSize(new java.awt.Dimension(120, 10));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SA-OKNA   <АРМ Менеджер>");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(659, 80));
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
        pan1.setPreferredSize(new java.awt.Dimension(106, 80));
        pan1.setLayout(new java.awt.BorderLayout());

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
        pan2.setPreferredSize(new java.awt.Dimension(64, 32));
        pan2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 1));

        btn2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn2.setText("Сохранить книгу");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(120, 30));
        btn2.setMinimumSize(new java.awt.Dimension(87, 26));
        btn2.setPreferredSize(new java.awt.Dimension(96, 26));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2(evt);
            }
        });
        pan2.add(btn2);

        pan1.add(pan2, java.awt.BorderLayout.SOUTH);
        pan1.add(filler7, java.awt.BorderLayout.NORTH);

        getContentPane().add(pan1, java.awt.BorderLayout.WEST);

        pan3.setPreferredSize(new java.awt.Dimension(771, 80));
        pan3.setLayout(new java.awt.BorderLayout());

        tab4.setPreferredSize(new java.awt.Dimension(841, 60));

        pan4.setBackground(new java.awt.Color(192, 224, 236));
        pan4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));

        btn3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn3.setText("Xxxxx");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(120, 30));
        btn3.setMinimumSize(new java.awt.Dimension(87, 26));
        btn3.setPreferredSize(new java.awt.Dimension(96, 26));
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3(evt);
            }
        });
        pan4.add(btn3);

        btn4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn4.setText("Xxxxx");
        btn4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn4.setMaximumSize(new java.awt.Dimension(120, 30));
        btn4.setMinimumSize(new java.awt.Dimension(87, 26));
        btn4.setPreferredSize(new java.awt.Dimension(96, 26));
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4(evt);
            }
        });
        pan4.add(btn4);

        btn11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn11.setText("Xxxxx");
        btn11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn11.setMaximumSize(new java.awt.Dimension(120, 30));
        btn11.setMinimumSize(new java.awt.Dimension(87, 26));
        btn11.setPreferredSize(new java.awt.Dimension(96, 26));
        btn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn11(evt);
            }
        });
        pan4.add(btn11);

        tab4.addTab("<html><font size=\"3\">\n&nbsp;&nbsp\nСправочники\n&nbsp;&nbsp", pan4);

        pan5.setBackground(new java.awt.Color(192, 224, 236));
        pan5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));

        btn5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn5.setText("Xxxxx");
        btn5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn5.setMaximumSize(new java.awt.Dimension(120, 30));
        btn5.setMinimumSize(new java.awt.Dimension(87, 26));
        btn5.setPreferredSize(new java.awt.Dimension(96, 26));
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5(evt);
            }
        });
        pan5.add(btn5);

        btn6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn6.setText("Xxxxx");
        btn6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn6.setMaximumSize(new java.awt.Dimension(120, 30));
        btn6.setMinimumSize(new java.awt.Dimension(87, 26));
        btn6.setPreferredSize(new java.awt.Dimension(96, 26));
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6(evt);
            }
        });
        pan5.add(btn6);

        btn7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn7.setText("Xxxxx");
        btn7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn7.setMaximumSize(new java.awt.Dimension(120, 30));
        btn7.setMinimumSize(new java.awt.Dimension(87, 26));
        btn7.setPreferredSize(new java.awt.Dimension(96, 26));
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7(evt);
            }
        });
        pan5.add(btn7);

        tab4.addTab("<html><font size=\"3\">\n&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp \nЗаказы\n&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp", pan5);

        pan6.setBackground(new java.awt.Color(192, 224, 236));
        pan6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));

        btn8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn8.setText("Xxxxx");
        btn8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn8.setMaximumSize(new java.awt.Dimension(120, 30));
        btn8.setMinimumSize(new java.awt.Dimension(87, 26));
        btn8.setPreferredSize(new java.awt.Dimension(96, 26));
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8(evt);
            }
        });
        pan6.add(btn8);

        btn9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn9.setText("Xxxxx");
        btn9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn9.setMaximumSize(new java.awt.Dimension(120, 30));
        btn9.setMinimumSize(new java.awt.Dimension(87, 26));
        btn9.setPreferredSize(new java.awt.Dimension(96, 26));
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9(evt);
            }
        });
        pan6.add(btn9);

        btn10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn10.setText("Xxxxx");
        btn10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn10.setMaximumSize(new java.awt.Dimension(120, 30));
        btn10.setMinimumSize(new java.awt.Dimension(87, 26));
        btn10.setPreferredSize(new java.awt.Dimension(96, 26));
        btn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn10(evt);
            }
        });
        pan6.add(btn10);

        tab4.addTab("<html><font size=\"3\">\n&nbsp;&nbsp&nbsp;&nbsp\nОтчёты\n&nbsp;&nbsp&nbsp;&nbsp", pan6);

        pan3.add(tab4, java.awt.BorderLayout.CENTER);

        getContentPane().add(pan3, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(98, 80));
        pan7.setLayout(new java.awt.BorderLayout());

        pan11.setPreferredSize(new java.awt.Dimension(10, 20));

        javax.swing.GroupLayout pan11Layout = new javax.swing.GroupLayout(pan11);
        pan11.setLayout(pan11Layout);
        pan11Layout.setHorizontalGroup(
            pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        pan11Layout.setVerticalGroup(
            pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        pan7.add(pan11, java.awt.BorderLayout.NORTH);

        pan10.setBackground(new java.awt.Color(192, 224, 236));
        pan10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pan10.setPreferredSize(new java.awt.Dimension(10, 18));
        pan10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        btn1.setText("Выход");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(120, 26));
        btn1.setMinimumSize(new java.awt.Dimension(26, 26));
        btn1.setPreferredSize(new java.awt.Dimension(80, 26));
        pan10.add(btn1);

        pan7.add(pan10, java.awt.BorderLayout.CENTER);

        getContentPane().add(pan7, java.awt.BorderLayout.EAST);

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenubtnMainMenu(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenubtnMainMenu
        ppmMain.show(pan1, btnMenu.getX(), pan1.getY() + 22);
    }//GEN-LAST:event_btnMenubtnMainMenu

    private void btn2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2

    }//GEN-LAST:event_btn2

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

    private void btn3(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3
        // TODO add your handling code here:
    }//GEN-LAST:event_btn3

    private void btn4(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4
        // TODO add your handling code here:
    }//GEN-LAST:event_btn4

    private void btn5(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5
        // TODO add your handling code here:
    }//GEN-LAST:event_btn5

    private void btn6(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6
        // TODO add your handling code here:
    }//GEN-LAST:event_btn6

    private void btn7(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7
        // TODO add your handling code here:
    }//GEN-LAST:event_btn7

    private void btn8(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8
        // TODO add your handling code here:
    }//GEN-LAST:event_btn8

    private void btn9(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9
        // TODO add your handling code here:
    }//GEN-LAST:event_btn9

    private void btn10(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn10
        // TODO add your handling code here:
    }//GEN-LAST:event_btn10

    private void btn11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn11
        // TODO add your handling code here:
    }//GEN-LAST:event_btn11

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnMenu;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JMenu mn01;
    private javax.swing.JMenuItem mn02;
    private javax.swing.JMenuItem mn11;
    private javax.swing.JMenuItem mn12;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JPopupMenu ppmMain;
    private javax.swing.JTabbedPane tab4;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    
    private void initElements() {
        setTitle(getTitle() + Util.designName());
    }
}
