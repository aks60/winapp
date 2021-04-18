package frames;

import builder.Wincalc;
import builder.calc.SpecificRec;
import common.FrameToFile;
import dataset.Record;
import domain.eArtikl;
import domain.eSetting;
import java.awt.Dimension;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.RowFilter;
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
        loadingTab(iwin);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tab1.getModel());
        tab1.setRowSorter(sorter);
    }

    public void loadingTab(Wincalc iwin) {
        try {
            Map<String, Vector> hmSpc = new HashMap();
            Set<String> setSpc1 = new HashSet();
            Set<String> setSpc2 = new HashSet();
            iwin.listSpec.forEach(rec -> setSpc1.add(rec.artikl));
            setSpc1.forEach(el -> hmSpc.put(el, new Vector(Arrays.asList(el, 0f, 0f, 0f, 0f, 0f, 0f))));
            iwin.listSpec.forEach(rec -> {
                List<Float> val = hmSpc.get(rec.artikl);
                val.set(1, val.get(1) + rec.count);
                val.set(3, val.get(3) + rec.quant1);
            });

            //=== Таблица 1 ===
            ((DefaultTableModel) tab1.getModel()).getDataVector().clear();
            Connection cn = Test.connect1();
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
                String artikl = rs.getString("ANUMB"); //артикл
                double leng = rs.getDouble("ALENG"); //длина
                float count = rs.getFloat("AQTYP"); //колич
                float pogonag = rs.getFloat("AQTYA"); //погонаж
                double perc = rs.getDouble("APERC"); //отход
                double cost = rs.getDouble("APRC1"); //стоим.без.ск.за ед.изм
                double costdec = rs.getDouble("APRCD"); //стоим.со.ск.за.ед.изм                                
                double value1 = (perc * pogonag / 100 + pogonag) * cost;
                double value2 = (perc * pogonag / 100 + pogonag) * costdec;
                Record artiklRec = eArtikl.query().stream().filter(r -> artikl.equals(r.get(eArtikl.code))).findFirst().orElse(eArtikl.up.newRecord());
                sum1 = sum1 + value1;
                sum2 = sum2 + value2;
                setSpc2.add(artikl);
                List<Float> val = hmSpc.get(artikl);
                if (val == null) {
                    hmSpc.put(artikl, new Vector(Arrays.asList(artikl, 0f, 0f, 0f, 0f, 0f, 0f)));
                    val = hmSpc.get(artikl);
                }
                val.set(2, val.get(2) + count);
                val.set(4, val.get(4) + pogonag);

                vectorRec.add(4, artiklRec.get(eArtikl.name)); //имя артикула                
                vectorRec.add(value1); //стоим. элемента без скидки
                vectorRec.add(value2); //стоим. элемента со скидкой

                ((DefaultTableModel) tab1.getModel()).getDataVector().add(vectorRec);
            }
            rs.close();
            lab1.setText("Проект: pnumb = " + iwin.rootGson.prj + "    Изд: punic = "
                    + punic + "   Стоим.без.ск = " + Util.df.format(sum1) + "   Стоим.со.ск = " + Util.df.format(sum2));

            //=== Таблица 2 ===
            ((DefaultTableModel) tab2.getModel()).getDataVector().clear();
            Set<String> setSpc1x = new HashSet(setSpc1);
            Set<String> setSpc2x = new HashSet(setSpc2);
            setSpc1x.removeAll(setSpc2);
            setSpc2x.removeAll(setSpc1);
            ((DefaultTableModel) tab2.getModel()).getDataVector().add(new Vector(Arrays.asList("--- SAOkna  за.выч.Профстрой ---")));
            setSpc1x.forEach(e -> ((DefaultTableModel) tab2.getModel()).getDataVector().add(new Vector(Arrays.asList(e))));
            ((DefaultTableModel) tab2.getModel()).getDataVector().add(new Vector(Arrays.asList("--- ПрофСтрой  за.выч.SAOkna ---")));
            setSpc2x.forEach(e -> ((DefaultTableModel) tab2.getModel()).getDataVector().add(new Vector(Arrays.asList(e))));

            //=== Таблица 3 ===
            ((DefaultTableModel) tab3.getModel()).getDataVector().clear();
            for (Map.Entry<String, Vector> entry : hmSpc.entrySet()) {
                Vector vec = entry.getValue();
                vec.set(5, (float) vec.get(3) - (float) vec.get(4));
                ((DefaultTableModel) tab3.getModel()).getDataVector().add(vec);
            }
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pan4 = new javax.swing.JPanel();
        scr = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();
        labSum = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DBCompare");

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

        center.setPreferredSize(new java.awt.Dimension(800, 550));
        center.setLayout(new java.awt.BorderLayout());

        pan4.setLayout(new java.awt.BorderLayout());

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "№пп", "ATYPM", "ATYPP", "Артикул", "Наименование", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Угол 1", "Угол 2", "Количество", "Погонаж", "<html>Норма<br/>отхода", "<html>Себестоимость<br/> за ед.изм.", "<html>Ст.без.ск<br/> за ед.изм.", "<html>Ст.со.ск<br/> за ед.изм.", "<html>Стоим.<br/>без.скид.", "<html>Стоим.<br/>со.скидк."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab1MousePressed(evt);
            }
        });
        scr.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(0).setMaxWidth(60);
            tab1.getColumnModel().getColumn(1).setMinWidth(0);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(0);
            tab1.getColumnModel().getColumn(1).setMaxWidth(0);
            tab1.getColumnModel().getColumn(2).setMinWidth(0);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(0);
            tab1.getColumnModel().getColumn(2).setMaxWidth(0);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(140);
            tab1.getColumnModel().getColumn(4).setPreferredWidth(240);
            tab1.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(6).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(7).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(8).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(9).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(10).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(11).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(13).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(14).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(15).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(16).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(17).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(18).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(19).setPreferredWidth(80);
        }

        pan4.add(scr, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Спецификация\n", pan4);

        pan3.setLayout(new java.awt.BorderLayout());

        scr2.setPreferredSize(new java.awt.Dimension(454, 200));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null}
            },
            new String [] {
                "Артикул"
            }
        ));
        tab2.setFillsViewportHeight(true);
        scr2.setViewportView(tab2);

        pan3.add(scr2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Сравнение 1", pan3);

        pan5.setLayout(new java.awt.BorderLayout());

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Артикул", "Колич.  SA", "Колич.  PS", "Погонаж  SA", "Погонаж  PS", "Дельта"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(200);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(50);
            tab3.getColumnModel().getColumn(2).setPreferredWidth(50);
            tab3.getColumnModel().getColumn(3).setPreferredWidth(50);
            tab3.getColumnModel().getColumn(4).setPreferredWidth(50);
            tab3.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        pan5.add(scr3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Сравнение 2", pan5);

        center.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
        labFilter.setMaximumSize(new java.awt.Dimension(100, 14));
        labFilter.setMinimumSize(new java.awt.Dimension(100, 14));
        labFilter.setPreferredSize(new java.awt.Dimension(100, 14));
        south.add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(180, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(180, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(180, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFilterfilterUpdate(evt);
            }
        });
        south.add(txtFilter);

        checkFilter.setText("в конце строки   ");
        south.add(checkFilter);

        labSum.setText("sum:0");
        labSum.setMaximumSize(new java.awt.Dimension(200, 14));
        labSum.setMinimumSize(new java.awt.Dimension(200, 14));
        labSum.setPreferredSize(new java.awt.Dimension(200, 14));
        south.add(labSum);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void txtFilterfilterUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilterfilterUpdate
        JTable table = tab1;
        if (txtFilter.getText().length() == 0) {
            ((TableRowSorter<TableModel>) tab1.getRowSorter()).setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((TableRowSorter<TableModel>) tab1.getRowSorter()).setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_txtFilterfilterUpdate

    private void tab1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MousePressed
        JTable table = (JTable) evt.getSource();
        Util.updateBorderAndSql(table, Arrays.asList(tab1));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tab1MousePressed

    // <editor-fold defaultstate="collapsed" desc="Generated Code">   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel labFilter;
    private javax.swing.JLabel labSum;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JScrollPane scr;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        //TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tab1.getModel());
        //tab1.setRowSorter(sorter);
        tab1.getTableHeader().setPreferredSize(new Dimension(0, 32));
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
        for (SpecificRec spc : iwin.listSpec) {

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
            Connection conn = Test.connect1();
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

            for (SpecificRec spc : iwin.listSpec) {
                String key = spc.artikl;
                Float val = (hmDB2.get(key) == null) ? 0.f : hmDB2.get(key);
                hmDB2.put(key, val + spc.cost1);
            }

            if (detail == true) {
                System.out.printf("%-64s%-24s%-16s%-16s%-16s", new Object[]{"Name", "Artikl", "PS", "SA", "Delta"});
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
            System.out.printf("%-18s%-18s%-18s%-12s", "Prj=" + iwin.prj, "PS=" + String.format("%.2f", iwinTotal), "SA="
                    + String.format("%.2f", jarTotal), "dx=" + String.format("%.2f", Math.abs(iwinTotal - jarTotal)));
            System.out.println();

        } catch (SQLException e) {
            println("Ошибка: DBCompare.iwinRec().  " + e);
        }
    }
}
