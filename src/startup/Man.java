package startup;

import builder.Wincalc;
import common.FrameProgress;
import frames.Util;
import java.awt.Frame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.JCheckBoxMenuItem;
import common.ListenerFrame;
import common.eProperty;
import java.awt.Dimension;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

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

    private void mnLookAndFeel(java.awt.event.ActionEvent evt) {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if (((JCheckBoxMenuItem) evt.getSource()).getText().equals(laf.getName()) == true) {
                eProperty.lookandfeel.write(laf.getName());
                eProperty.save();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppmMain = new javax.swing.JPopupMenu();
        mn10 = new javax.swing.JMenu();
        mn11 = new javax.swing.JMenuItem();
        mn12 = new javax.swing.JMenuItem();
        mn20 = new javax.swing.JMenu();
        sep1 = new javax.swing.JPopupMenu.Separator();
        mn30 = new javax.swing.JMenuItem();
        buttonLookAndFiilGroup = new javax.swing.ButtonGroup();
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
        tab1 = new javax.swing.JTabbedPane();
        pan1 = new javax.swing.JPanel();
        btn2 = new javax.swing.JButton();

        ppmMain.setFont(frames.Util.getFont(1,1));

        mn10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn10.setText("Данные                                   ");
        mn10.setFont(frames.Util.getFont(1,1));

        mn11.setFont(frames.Util.getFont(1,1));
        mn11.setText("Текст ячейки");
        mn11.setName("1"); // NOI18N
        mn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn11cellValueType(evt);
            }
        });
        mn10.add(mn11);

        mn12.setFont(frames.Util.getFont(1,1));
        mn12.setText("Целое число");
        mn12.setName("2"); // NOI18N
        mn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn12cellValueType(evt);
            }
        });
        mn10.add(mn12);

        ppmMain.add(mn10);

        mn20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b061.gif"))); // NOI18N
        mn20.setText("Вид интерфейса");
        mn20.setFont(frames.Util.getFont(1,1));
        ppmMain.add(mn20);
        ppmMain.add(sep1);

        mn30.setFont(frames.Util.getFont(1,1));
        mn30.setText("Выход");
        mn30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnExit(evt);
            }
        });
        ppmMain.add(mn30);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SA-OKNA   <АРМ Менеджер>");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setMinimumSize(new java.awt.Dimension(423, 60));
        setPreferredSize(new java.awt.Dimension(659, 80));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                wndowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Man.this.windowClosing(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                wndowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                Man.this.windowIconified(evt);
            }
        });

        pan3.setPreferredSize(new java.awt.Dimension(771, 10));
        pan3.setLayout(new java.awt.BorderLayout());

        tab4.setPreferredSize(new java.awt.Dimension(841, 10));

        pan4.setBackground(new java.awt.Color(192, 224, 236));
        pan4.setMinimumSize(new java.awt.Dimension(281, 10));
        pan4.setPreferredSize(new java.awt.Dimension(308, 10));
        pan4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 1));

        btn3.setFont(frames.Util.getFont(1,1));
        btn3.setText("Статусы");
        btn3.setActionCommand("");
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
        btn3.getAccessibleContext().setAccessibleName("");
        btn3.getAccessibleContext().setAccessibleDescription("");

        btn4.setFont(frames.Util.getFont(1,1));
        btn4.setText("Категории");
        btn4.setActionCommand("");
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
        btn4.getAccessibleContext().setAccessibleName("");

        btn11.setFont(frames.Util.getFont(1,1));
        btn11.setText("Xxxxx");
        btn11.setActionCommand("");
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
        btn11.getAccessibleContext().setAccessibleName("");

        tab4.addTab("<html><font size=\"3\"><b>\n&nbsp;&nbsp\nСправочники\n&nbsp;&nbsp", pan4);

        pan5.setBackground(new java.awt.Color(192, 224, 236));
        pan5.setMinimumSize(new java.awt.Dimension(281, 10));
        pan5.setPreferredSize(new java.awt.Dimension(308, 10));
        pan5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 1));

        btn5.setFont(frames.Util.getFont(1,1));
        btn5.setText("Контрагенты");
        btn5.setActionCommand("");
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
        btn5.getAccessibleContext().setAccessibleName("");

        btn6.setFont(frames.Util.getFont(1,1));
        btn6.setText("Заказы");
        btn6.setActionCommand("");
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
        btn6.getAccessibleContext().setAccessibleName("");

        btn7.setFont(frames.Util.getFont(1,1));
        btn7.setText("Vvvvv");
        btn7.setActionCommand("");
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
        btn7.getAccessibleContext().setAccessibleName("");

        tab4.addTab("<html><font size=\"3\"><b>\n&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp \nЗаказы\n&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp", pan5);

        pan6.setBackground(new java.awt.Color(192, 224, 236));
        pan6.setMinimumSize(new java.awt.Dimension(281, 10));
        pan6.setPreferredSize(new java.awt.Dimension(308, 10));
        pan6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 1));

        btn8.setFont(frames.Util.getFont(1,1));
        btn8.setText("Спецификация");
        btn8.setActionCommand("");
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
        btn8.getAccessibleContext().setAccessibleName("");

        btn9.setFont(frames.Util.getFont(1,1));
        btn9.setText("Смета");
        btn9.setActionCommand("");
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
        btn9.getAccessibleContext().setAccessibleName("");

        btn10.setFont(frames.Util.getFont(1,1));
        btn10.setText("Счёт-фактура");
        btn10.setActionCommand("");
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
        btn10.getAccessibleContext().setAccessibleName("");

        tab4.addTab("<html><font size=\"3\"><b>\n&nbsp;&nbsp&nbsp;&nbsp\nОтчёты\n&nbsp;&nbsp&nbsp;&nbsp", pan6);

        pan3.add(tab4, java.awt.BorderLayout.CENTER);

        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
        });

        pan1.setBackground(new java.awt.Color(192, 224, 236));
        pan1.setAlignmentY(4.0F);
        pan1.setPreferredSize(new java.awt.Dimension(106, 10));
        pan1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 1));

        btn2.setFont(frames.Util.getFont(1,1));
        btn2.setText("Выход");
        btn2.setActionCommand("");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(120, 30));
        btn2.setMinimumSize(new java.awt.Dimension(87, 26));
        btn2.setPreferredSize(new java.awt.Dimension(96, 26));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnExit(evt);
            }
        });
        pan1.add(btn2);
        btn2.getAccessibleContext().setAccessibleName("");
        btn2.getAccessibleContext().setAccessibleDescription("");

        tab1.addTab("<html><font size=\"3\"><b>\n&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\nМеню\n&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp", pan1);

        pan3.add(tab1, java.awt.BorderLayout.WEST);

        getContentPane().add(pan3, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void windowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosing
        mnExit(null);
    }//GEN-LAST:event_windowClosing

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

        FrameProgress.create(Man.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Partner.createFrame(Man.this);
            }
        });
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

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        ppmMain.show(pan1, pan1.getX(), pan1.getY() - pan1.getHeight());
    }//GEN-LAST:event_tab1MouseClicked

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.ButtonGroup buttonLookAndFiilGroup;
    private javax.swing.JMenu mn10;
    private javax.swing.JMenuItem mn11;
    private javax.swing.JMenuItem mn12;
    private javax.swing.JMenu mn20;
    private javax.swing.JMenuItem mn30;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPopupMenu ppmMain;
    private javax.swing.JPopupMenu.Separator sep1;
    private javax.swing.JTabbedPane tab1;
    private javax.swing.JTabbedPane tab4;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    
    private void initElements() {
        setTitle(getTitle() + Util.designName());
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            JCheckBoxMenuItem mnIt = new javax.swing.JCheckBoxMenuItem();
            buttonLookAndFiilGroup.add(mnIt);
            hmLookAndFill.put(laf.getName(), mnIt);
            mn20.add(mnIt);
            mnIt.setFont(frames.Util.getFont(1, 1));
            mnIt.setText(laf.getName());
            mnIt.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    mnLookAndFeel(evt);
                }
            });
            if (lookAndFeel.getName().equals(laf.getName())) {
                mnIt.setSelected(true);
            }
        }        
    }
}
