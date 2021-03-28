package frames;

import builder.Wincalc;
import builder.specif.Specification;
import common.FrameToFile;
import common.eProperty;
import dataset.Record;
import domain.eSetting;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import static jdk.nashorn.internal.objects.Global.println;
import jxl.Sheet;
import jxl.Workbook;
import static startup.App.Top;
import startup.Test;

public class DBCompare extends javax.swing.JFrame {

    public static int numDb = Integer.valueOf(eProperty.base_num.read());

    enum Fld { //артикул, color1, color2, color3, длина, ширина, уг1, уг2, кол, погонаж, норм.отх, себест, без.скидк, со.скидк
        ANUMB, CLNUM, CLNU1, CLNU2, ALENG, ARADI, AUG01, AUG02, AQTYP, AQTYA, APERC, ASEB1, APRC1, APRCD
    }

    public DBCompare(Wincalc iwin, int pnumb) {
        initComponents();
        initElements();
        loadingTab1(iwin, pnumb);
        setVisible(true); 
    }

    public void loadingTab1(Wincalc iwin, int prj) {
        try {
            ((DefaultTableModel) tab.getModel()).getDataVector().clear();
            Connection cn = Test.connect(numDb)[0];
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("select a.* from SPECPAU a left join LISTPRJ b on a.PUNIC = b.PUNIC where b.PNUMB = " + prj);
            int npp = 0;
            while (rs.next()) {
                Vector vectorRec = new Vector();
                vectorRec.add(++npp);
                for (int i = 0; i < Fld.values().length; i++) {
                    vectorRec.add(rs.getObject(Fld.values()[i].name()));
                }
                ((DefaultTableModel) tab.getModel()).getDataVector().add(vectorRec);
            }

        } catch (SQLException e) {
            println("Ошибка: DBCompare.iwinRec().  " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        scr = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 650));

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/prop/hint"); // NOI18N
        btnClose.setToolTipText(bundle.getString("Закрыть")); // NOI18N
        btnClose.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnClose.setFocusable(false);
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setMaximumSize(new java.awt.Dimension(25, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(25, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(25, 25));
        btnClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap(642, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setPreferredSize(new java.awt.Dimension(800, 600));
        center.setLayout(new java.awt.BorderLayout());

        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "№пп", "Артикул", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Угол 1", "Угол 2", "Погонаж", "Норма отхода", "Себесоимость", "Себест.без скидки", "Себест.со скидкой"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab.setFillsViewportHeight(true);
        scr.setViewportView(tab);
        if (tab.getColumnModel().getColumnCount() > 0) {
            tab.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab.getColumnModel().getColumn(0).setMaxWidth(60);
            tab.getColumnModel().getColumn(1).setPreferredWidth(140);
            tab.getColumnModel().getColumn(2).setPreferredWidth(60);
            tab.getColumnModel().getColumn(3).setPreferredWidth(60);
            tab.getColumnModel().getColumn(4).setPreferredWidth(60);
            tab.getColumnModel().getColumn(5).setPreferredWidth(60);
            tab.getColumnModel().getColumn(6).setPreferredWidth(60);
            tab.getColumnModel().getColumn(7).setPreferredWidth(60);
            tab.getColumnModel().getColumn(8).setPreferredWidth(60);
            tab.getColumnModel().getColumn(9).setPreferredWidth(60);
            tab.getColumnModel().getColumn(10).setPreferredWidth(60);
            tab.getColumnModel().getColumn(11).setPreferredWidth(80);
            tab.getColumnModel().getColumn(12).setPreferredWidth(80);
            tab.getColumnModel().getColumn(13).setPreferredWidth(80);
        }

        center.add(scr, java.awt.BorderLayout.CENTER);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 677, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel center;
    private javax.swing.JPanel north;
    private javax.swing.JScrollPane scr;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab;
    // End of variables declaration//GEN-END:variables

    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
    }
    
    //сравнение спецификации с профстроем
    public static void iwinXls(ArrayList<Specification> spcList, int prj, boolean detail) {

        //TODO нужна синхронизация функции
        System.out.println();
        System.out.println("Prj=" + prj);
        Float iwinTotal = 0f, jarTotal = 0f;
        String path = "src\\resource\\xls\\ps4\\p" + prj + ".xls";
        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val)) == true) {
            path = "src\\resource\\xls\\ps3\\p" + prj + ".xls";
        }
        //Specification.sort(spcList);
        Map<String, Float> hmXls = new LinkedHashMap();
        Map<String, Float> hmJar = new LinkedHashMap();
        Map<String, String> hmArt = new LinkedHashMap();
        for (Specification spc : spcList) {

            String key = spc.name.trim().replaceAll("[\\s]{1,}", " ");
            Float val = (hmJar.get(key) == null) ? 0.f : hmJar.get(key);
            hmJar.put(key, val + spc.cost1);
            hmArt.put(key, spc.artikl);
        }
        try {
            Sheet sheet = Workbook.getWorkbook(new File(path)).getSheet(0);
            if ("ps3".equals(eSetting.find(2).getStr(eSetting.val)) == true) {
                for (int i = 2; i < sheet.getRows(); i++) {

                    String art = sheet.getCell(0, i).getContents().trim();
                    String key = sheet.getCell(1, i).getContents().trim().replaceAll("[\\s]{1,}", " ");
                    String val = sheet.getCell(6, i).getContents();
                    if (key.isEmpty() || art.isEmpty() || val.isEmpty()) {
                        continue;
                    }
                    //System.out.println(art + " - " + key + " - " + val);
                    val = val.replaceAll("[\\s|\\u00A0]+", "").replace(",", ".");
                    Float val2 = (hmXls.get(key) == null) ? 0.f : hmXls.get(key);
                    try {
                        Float val3 = Float.valueOf(val) + val2;
                        hmXls.put(key, val3);
                        hmArt.put(key, art);
                    } catch (Exception e) {
                        System.err.println("Ошибка:Main.compareIWin " + e);
                        continue;
                    }
                }
            } else {
                for (int i = 5; i < sheet.getRows(); i++) {

                    String art = sheet.getCell(1, i).getContents().trim();
                    String key = sheet.getCell(2, i).getContents().trim().replaceAll("[\\s]{1,}", " ");
                    String val = sheet.getCell(10, i).getContents();
                    if (key.isEmpty() || art.isEmpty() || val.isEmpty()) {
                        continue;
                    }
                    //System.out.println(art + " - " + key + " - " + val);
                    val = val.replaceAll("[\\s|\\u00A0]+", "").replace(",", ".");
                    Float val2 = (hmXls.get(key) == null) ? 0.f : hmXls.get(key);
                    try {
                        Float val3 = Float.valueOf(val) + val2;
                        hmXls.put(key, val3);
                        hmArt.put(key, art);
                    } catch (Exception e) {
                        System.err.println("Ошибка:Main.compareIWin " + e);
                        continue;
                    }
                }
            }
            if (detail == true) {
                System.out.printf("%-64s%-24s%-16s%-16s%-16s", new Object[]{"Name", "Artikl", "Xls", "Jar", "Delta"});
                System.out.println();
                for (Map.Entry<String, Float> entry : hmXls.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = hmJar.getOrDefault(key, 0.f);
                    hmJar.remove(key);
                    System.out.printf("%-64s%-24s%-16.2f%-16.2f%-16.2f", new Object[]{key, hmArt.get(key), val1, val2, Math.abs(val1 - val2)});
                    System.out.println();
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                //System.out.println();
                if (hmJar.isEmpty() == false) {
                    System.out.printf("%-72s%-24s%-20s", new Object[]{"Name", "Artikl", "Value"});
                }
                System.out.println();
                for (Map.Entry<String, Float> entry : hmJar.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    System.out.printf("%-72s%-24s%-16.2f", "Лишние: " + key, hmArt.get(key), value3);
                    System.out.println();
                    jarTotal = jarTotal + value3;
                }
            } else {
                for (Map.Entry<String, Float> entry : hmXls.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = hmJar.getOrDefault(key, 0.f);
                    hmJar.remove(key);
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                for (Map.Entry<String, Float> entry : hmJar.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    jarTotal = jarTotal + value3;
                }
            }
            System.out.printf("%-18s%-18s%-18s%-12s", "Prj=" + prj, "iwin=" + String.format("%.2f", iwinTotal), "jar="
                    + String.format("%.2f", jarTotal), "dx=" + String.format("%.2f", Math.abs(iwinTotal - jarTotal)));
            System.out.println();

        } catch (Exception e2) {
            System.err.println("Ошибка:Main.compareIWin " + e2);
        }
    }

    //System.out.println("\u001B[31m XXX \u001B[0m");
    public static void iwinRec(Wincalc iwin, int pnumb) {
        try {
            Map<String, Record> hm = new HashMap();
            Connection cn = Test.connect(numDb)[0];
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("select a.* from SPECPAU a left join LISTPRJ b on a.PUNIC = b.PUNIC where b.PNUMB = " + pnumb);
            while (rs.next()) {
                Record rec = new Record();
                String key = rs.getString("ANUMB").trim().replaceAll("[\\s]{1,}", " ") + rs.getString("CLNUM")
                        + rs.getString("CLNU1") + rs.getString("CLNU2") + rs.getString("AUGO1") + rs.getString("AUGO2")
                        + rs.getString("ARADI") + rs.getString("ALENG");

                for (int i = 0; i < Fld.values().length; i++) {
                    rec.add(rs.getObject(Fld.values()[i].name()));
                }
                Record rec2 = hm.get(rs.getString("ANUMB"));
                if (rec2 != null) {
                    rec.set(Fld.ALENG.ordinal(), rec.getFloat(Fld.ALENG.ordinal()) + rec2.getFloat(Fld.ALENG.ordinal()));

                }
                hm.put(rs.getString("ANUMB"), rec);
                System.out.println(rec);
            }

        } catch (SQLException e) {
            println("Ошибка: DBCompare.iwinRec().  " + e);
        }
    }
}
