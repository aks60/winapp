package startup;

import builder.Wincalc;
import common.FrameListener;
import frames.Util;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.JCheckBoxMenuItem;

public class Men extends javax.swing.JFrame {

    private Locale locale;
    private Wincalc iwin = new Wincalc();
    private javax.swing.Timer timer = null;
    private FrameListener listenerMenu;
    private HashMap<String, JCheckBoxMenuItem> hmLookAndFill = new HashMap();    

    public Men() {
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
        Data = new javax.swing.JMenu();
        data1 = new javax.swing.JMenuItem();
        data2 = new javax.swing.JMenuItem();
        Help = new javax.swing.JMenuItem();
        panMenu = new javax.swing.JPanel();
        btnMenu = new javax.swing.JButton();
        panMenuMain = new javax.swing.JPanel();
        btnSaveFile = new javax.swing.JButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 2), new java.awt.Dimension(0, 32767));

        ppmMain.setPreferredSize(new java.awt.Dimension(120, 120));

        Data.setText("Данные                                   ");

        data1.setText("Текст ячейки");
        data1.setName("1"); // NOI18N
        data1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data1cellValueType(evt);
            }
        });
        Data.add(data1);

        data2.setText("Целое число");
        data2.setName("2"); // NOI18N
        data2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data2cellValueType(evt);
            }
        });
        Data.add(data2);

        ppmMain.add(Data);

        Help.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Help.setText("Справка");
        Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpAct(evt);
            }
        });
        ppmMain.add(Help);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SA-OKNA   <АРМ Менеджер>");
        setPreferredSize(new java.awt.Dimension(659, 80));
        setResizable(false);

        panMenu.setBackground(new java.awt.Color(255, 255, 255));
        panMenu.setAlignmentY(4.0F);
        panMenu.setPreferredSize(new java.awt.Dimension(120, 60));
        panMenu.setLayout(new java.awt.BorderLayout());

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
        panMenu.add(btnMenu, java.awt.BorderLayout.CENTER);

        panMenuMain.setBackground(new java.awt.Color(192, 224, 236));
        panMenuMain.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true));
        panMenuMain.setPreferredSize(new java.awt.Dimension(64, 38));
        panMenuMain.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 6));

        btnSaveFile.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnSaveFile.setText("Сохранить книгу");
        btnSaveFile.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, null, new java.awt.Color(0, 0, 255)));
        btnSaveFile.setMaximumSize(new java.awt.Dimension(120, 30));
        btnSaveFile.setPreferredSize(new java.awt.Dimension(96, 22));
        btnSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFile(evt);
            }
        });
        panMenuMain.add(btnSaveFile);

        panMenu.add(panMenuMain, java.awt.BorderLayout.SOUTH);
        panMenu.add(filler7, java.awt.BorderLayout.NORTH);

        getContentPane().add(panMenu, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenubtnMainMenu(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenubtnMainMenu
        ppmMain.show(panMenu, btnMenu.getX(), panMenu.getY() + 22);
    }//GEN-LAST:event_btnMenubtnMainMenu

    private void btnSaveFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFile

    }//GEN-LAST:event_btnSaveFile

    private void data1cellValueType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data1cellValueType

    }//GEN-LAST:event_data1cellValueType

    private void data2cellValueType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data2cellValueType

    }//GEN-LAST:event_data2cellValueType

    private void btnHelpAct(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpAct

    }//GEN-LAST:event_btnHelpAct

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Data;
    private javax.swing.JMenuItem Help;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSaveFile;
    private javax.swing.JMenuItem data1;
    private javax.swing.JMenuItem data2;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JPanel panMenu;
    private javax.swing.JPanel panMenuMain;
    private javax.swing.JPopupMenu ppmMain;
    // End of variables declaration//GEN-END:variables

    private void initElements() {
        setTitle(getTitle() + Util.designName());
    }   
}
