package frames;

import builder.Wincalc;
import builder.specif.Specification;
import common.FrameToFile;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eSetting;
import java.awt.Dimension;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import static jdk.nashorn.internal.objects.Global.println;
import jxl.Sheet;
import jxl.Workbook;
import startup.Test;

public class DBCompare extends javax.swing.JFrame {


    enum Fld {
        ATYPM("уров1"), ATYPP("уров2"), ANUMB("артикул"), CLNUM("color1"), CLNU1("color2"), CLNU2("color3"),
        ALENG("длина"), ARADI("ширина"), AUG01("уг1"), AUG02("уг2"), AQTYP("кол"), AQTYA("погонаж"),
        APERC("норм.отх"), ASEB1("себест"), APRC1("стоим.без.ск.за ед.изм"), APRCD("стоим.со.ск.за.ед.изм");

        Fld(Object o) {
        }
    }

    public DBCompare(Wincalc iwin) {
        initComponents();
        initElements();
        loadingTab1(iwin);
    }

    public void loadingTab1(Wincalc iwin) {
        try {
            ((DefaultTableModel) tab.getModel()).getDataVector().clear();
            Connection cn = Test.connect()[0];
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("select PUNIC from LISTPRJ where PNUMB = " + iwin.rootGson.prj);
            rs.next();
            int punic = rs.getInt("PUNIC");
            rs = st.executeQuery("select a.* from SPECPAU a where a.PUNIC = " + punic + " order by a.anumb");
            int npp = 0;
            double sum1 = 0, sum2 = 0;
            while (rs.next()) {
                Vector vectorRec = new Vector();
                vectorRec.add(++npp);
                for (int i = 0; i < Fld.values().length; i++) {
                    vectorRec.add(rs.getObject(Fld.values()[i].name()));
                }
                double leng = rs.getDouble("ALENG"); //длина
                double count = rs.getDouble("AQTYP"); //колич
                double pogonag = rs.getDouble("AQTYA"); //погонаж
                double perc = rs.getDouble("APERC"); //отход
                double cost = rs.getDouble("APRC1"); //стоим.без.ск.за ед.изм
                double costdec = rs.getDouble("APRCD"); //стоим.со.ск.за.ед.изм
                double value1 = (perc * pogonag / 100 + pogonag) * cost;
                double value2 = (perc * pogonag / 100 + pogonag) * costdec;
                sum1 = sum1 + value1;
                sum2 = sum2 + value2;
                vectorRec.add(value1);
                vectorRec.add(value2);
                ((DefaultTableModel) tab.getModel()).getDataVector().add(vectorRec);
            }
            rs.close();
            lab1.setText("Проект:pnumb = " + iwin.rootGson.prj + "    Изделие:punic = "
                    + punic + "   Стоим.без.скидки = " + Util.df.format(sum1) + "   Стоим.со.скидкой = " + Util.df.format(sum2));

        } catch (SQLException e) {
            println("Ошибка: DBCompare.iwinRec().  " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        pan1 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        scr = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DBCompare");
        setPreferredSize(new java.awt.Dimension(800, 650));

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));
        north.setLayout(new java.awt.BorderLayout());

        pan2.setLayout(new java.awt.BorderLayout());

        lab1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lab1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pan2.add(lab1, java.awt.BorderLayout.CENTER);

        north.add(pan2, java.awt.BorderLayout.CENTER);

        pan1.setPreferredSize(new java.awt.Dimension(40, 25));

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

        javax.swing.GroupLayout pan1Layout = new javax.swing.GroupLayout(pan1);
        pan1.setLayout(pan1Layout);
        pan1Layout.setHorizontalGroup(
            pan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan1Layout.setVerticalGroup(
            pan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan1Layout.createSequentialGroup()
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        north.add(pan1, java.awt.BorderLayout.EAST);

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setPreferredSize(new java.awt.Dimension(800, 600));
        center.setLayout(new java.awt.BorderLayout());

        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "№пп", "ATYPM", "ATYPP", "Артикул", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Угол 1", "Угол 2", "Количествр", "Погонаж", "Норма отхода", "<html>Себестоимость<br/> за ед.изм.", "<html>Стоим.без.ск<br/> за ед.изм.", "<html>Стоим.со.ск<br/> за ед.изм.", "<html>Стоим.елем.<br/>без.скидки", "<html>Стоим.елем.<br/>со.скидкой"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, true, true
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
            tab.getColumnModel().getColumn(1).setMinWidth(0);
            tab.getColumnModel().getColumn(1).setPreferredWidth(0);
            tab.getColumnModel().getColumn(1).setMaxWidth(0);
            tab.getColumnModel().getColumn(2).setMinWidth(0);
            tab.getColumnModel().getColumn(2).setPreferredWidth(0);
            tab.getColumnModel().getColumn(2).setMaxWidth(0);
            tab.getColumnModel().getColumn(3).setPreferredWidth(200);
            tab.getColumnModel().getColumn(4).setPreferredWidth(40);
            tab.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab.getColumnModel().getColumn(6).setPreferredWidth(40);
            tab.getColumnModel().getColumn(7).setPreferredWidth(60);
            tab.getColumnModel().getColumn(8).setPreferredWidth(60);
            tab.getColumnModel().getColumn(9).setPreferredWidth(60);
            tab.getColumnModel().getColumn(10).setPreferredWidth(60);
            tab.getColumnModel().getColumn(12).setPreferredWidth(60);
            tab.getColumnModel().getColumn(13).setPreferredWidth(60);
            tab.getColumnModel().getColumn(14).setPreferredWidth(120);
            tab.getColumnModel().getColumn(15).setPreferredWidth(120);
            tab.getColumnModel().getColumn(16).setPreferredWidth(120);
            tab.getColumnModel().getColumn(17).setPreferredWidth(120);
            tab.getColumnModel().getColumn(18).setPreferredWidth(120);
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
            .addGap(0, 722, Short.MAX_VALUE)
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

    // <editor-fold defaultstate="collapsed" desc="Generated Code">   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel center;
    private javax.swing.JLabel lab1;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JScrollPane scr;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tab.getModel());
        tab.setRowSorter(sorter);
        tab.getTableHeader().setPreferredSize(new Dimension(0, 32));
    }

    //Сравнение спецификации с профстроем
    public static void iwinXls(Wincalc iwin, boolean detail) {

        System.out.println();
        System.out.println("Prj=" + iwin.prj);
        Float iwinTotal = 0f, jarTotal = 0f;
        String path = "src\\resource\\xls\\ps4\\p" + iwin.prj + ".xls";
        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val)) == true) {
            path = "src\\resource\\xls\\ps3\\p" + iwin.prj + ".xls";
        }
        //Specification.sort(spcList);
        Map<String, Float> hmXls = new LinkedHashMap();
        Map<String, Float> hmJar = new LinkedHashMap();
        Map<String, String> hmArt = new LinkedHashMap();
        for (Specification spc : iwin.listSpec) {

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
            System.out.printf("%-18s%-18s%-18s%-12s", "Prj=" + iwin.prj, "iwin=" + String.format("%.2f", iwinTotal), "jar="
                    + String.format("%.2f", jarTotal), "dx=" + String.format("%.2f", Math.abs(iwinTotal - jarTotal)));
            System.out.println();

        } catch (Exception e2) {
            System.err.println("Ошибка:Main.compareIWin " + e2);
        }
    }

    //Сравнение спецификации с профстроем
    public static void iwinRec(Wincalc iwin, boolean detail) {
        System.out.println();
        System.out.println("Prj=" + iwin.prj);
        Float iwinTotal = 0f, jarTotal = 0f;
        Map<String, Float> hmDB1 = new LinkedHashMap();
        Map<String, Float> hmDB2 = new LinkedHashMap();
        try {
            Connection conn = Test.connect()[0];
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("select PUNIC from LISTPRJ where PNUMB = " + iwin.rootGson.prj);
            rs.next();
            int punic = rs.getInt("PUNIC");
            rs = st.executeQuery("select a.* from SPECPAU a where a.PUNIC = " + punic + " order by a.anumb");
            while (rs.next()) {
                float leng = rs.getFloat("ALENG"); //длина
                float count = rs.getFloat("AQTYP"); //колич
                float pogonag = rs.getFloat("AQTYA"); //погонаж
                float perc = rs.getFloat("APERC"); //отход
                float cost = rs.getFloat("APRC1"); //стоим.без.ск.за ед.изм
                float value1 = (perc * pogonag / 100 + pogonag) * cost;
                float value2 = (hmDB1.get(rs.getString("ANUMB")) == null) ? value1
                        : value1 + hmDB1.get(rs.getString("ANUMB"));
                String key = rs.getString("ANUMB");
                hmDB1.put(key, value2);
            }
            conn.close();
            
            for (Specification spc : iwin.listSpec) {
                String key = spc.artikl;
                Float val = (hmDB2.get(key) == null) ? 0.f : hmDB2.get(key);
                hmDB2.put(key, val + spc.cost1);
            }

            if (detail == true) {
                System.out.printf("%-64s%-24s%-16s%-16s%-16s", new Object[]{"Name", "Artikl", "DB1", "DB2", "Delta"});
                System.out.println();
                for (Map.Entry<String, Float> entry : hmDB1.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = hmDB2.getOrDefault(key, 0.f);
                    hmDB2.remove(key);
                    Record rec = eArtikl.query().stream().filter(r -> key.equals(r.get(eArtikl.code))).findFirst().orElse(eArtikl.up.newRecord());
                    System.out.printf("%-64s%-24s%-16.2f%-16.2f%-16.2f", new Object[]{rec.get(eArtikl.name), key, val1, val2, Math.abs(val1 - val2)});
                    System.out.println();
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                if (hmDB2.isEmpty() == false) {
                    System.out.printf("%-24s%-20s", new Object[]{"Artikl", "Value"});
                }
                System.out.println();
                for (Map.Entry<String, Float> entry : hmDB2.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    System.out.printf("%-24s%-16.2f", "Лишние: " + key, value3);
                    System.out.println();
                    jarTotal = jarTotal + value3;
                }
            } else {
                for (Map.Entry<String, Float> entry : hmDB1.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = hmDB2.getOrDefault(key, 0.f);
                    hmDB2.remove(key);
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                for (Map.Entry<String, Float> entry : hmDB2.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    jarTotal = jarTotal + value3;
                }
            }
            System.out.printf("%-18s%-18s%-18s%-12s", "Prj=" + iwin.prj, "DB1=" + String.format("%.2f", iwinTotal), "DB2="
                    + String.format("%.2f", jarTotal), "dx=" + String.format("%.2f", Math.abs(iwinTotal - jarTotal)));
            System.out.println();

        } catch (SQLException e) {
            println("Ошибка: DBCompare.iwinRec().  " + e);
        }
    }
}
