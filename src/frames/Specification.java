package frames;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import common.eProperty;
import dataset.Record;
import domain.eArtikl;
import domain.eElemdet;
import domain.eFurndet;
import domain.eGlasdet;
import domain.eJoindet;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import builder.Wincalc;
import builder.making.Specific;
import common.Util;
import dataset.Query;
import domain.eSysprod;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import static java.util.stream.Collectors.toList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import startup.App;
import frames.swing.listener.ListenerFrame;
import common.eProfile;
import domain.ePrjprod;
import frames.swing.FilterTable;
import frames.swing.colgroup.ColumnGroup;
import frames.swing.colgroup.GroupableTableHeader;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class Specification extends javax.swing.JFrame {

    private DecimalFormat df0 = new DecimalFormat("#0");
    private DecimalFormat df1 = new DecimalFormat("#0.0");
    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private DecimalFormat df3 = new DecimalFormat("#0.000");
    private builder.Wincalc iwin = new Wincalc();
    private FilterTable filterTable = null;

    public Specification() {
        initComponents();
        initElements();
        createIwin();
        loadingTab1(groups(1));
        Uti5.setSelectedRow(tab1);
    }

    private void createIwin() {

        if (eProfile.profile == eProfile.P02) {
            int sysprodID = Integer.valueOf(eProperty.sysprodID.read());
            Record sysprodRec = eSysprod.find(sysprodID);
            if (sysprodRec == null) {
                JOptionPane.showMessageDialog(this, "Выберите конструкцию в системе профилей", "Предупреждение", JOptionPane.OK_OPTION);
                this.dispose();
            } else {
                String script = sysprodRec.getStr(eSysprod.script);
                JsonElement je = new Gson().fromJson(script, JsonElement.class);
                je.getAsJsonObject().addProperty("nuni", sysprodRec.getInt(eSysprod.systree_id));
                iwin.build(je.toString());
                Query.listOpenTable.forEach(q -> q.clear());
                iwin.constructiv(cbx2.getSelectedIndex() == 0);
            }

        } else {
            int prjprodID = Integer.valueOf(eProperty.prjprodID.read());
            Record prjprodRec = ePrjprod.find(prjprodID);
            if (prjprodRec == null) {
                JOptionPane.showMessageDialog(this, "Выберите конструкцию в списке заказов", "Предупреждение", JOptionPane.OK_OPTION);
                this.dispose();
            } else {
                String script = prjprodRec.getStr(ePrjprod.script);
                JsonElement je = new Gson().fromJson(script, JsonElement.class
                );
                je.getAsJsonObject().addProperty("nuni", prjprodRec.getInt(ePrjprod.systree_id));
                iwin = new Wincalc();
                iwin.build(je.toString());
                Query.listOpenTable.forEach(q -> q.clear());
                iwin.constructiv(true);
            }
        }
    }

    private void loadingTab1(List<Specific> listSpec) {
        tab1.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                lab.setBackground(new java.awt.Color(212, 208, 200));
                return lab;
            }
        });
        DefaultTableModel dtm = ((DefaultTableModel) tab1.getModel());
        dtm.getDataVector().clear();
        dtm.fireTableDataChanged();

        if (listSpec != null && listSpec.isEmpty() == false) {
            int indexLast = listSpec.get(0).getVector(0).size();
            float sum1 = 0, sum2 = 0, sum9 = 0, sum13 = 0;
            for (int i = 0; i < listSpec.size(); i++) { //заполним спецификацию
                Vector v = listSpec.get(i).getVector(i);
                dtm.addRow(v);
                sum1 = sum1 + (Float) v.get(indexLast - 1);
                sum2 = sum2 + (Float) v.get(indexLast - 2);
                sum9 = sum9 + (Float) v.get(indexLast - 9);
                sum13 = sum13 + (Float) v.get(indexLast - 13);
            }
            Vector vectorLast = new Vector();
            for (int i = 0; i < indexLast; i++) {
                vectorLast.add(null);
            }
            vectorLast.set(indexLast - 1, sum1);
            vectorLast.set(indexLast - 2, sum2);
            vectorLast.set(indexLast - 9, sum9);
            vectorLast.set(indexLast - 13, sum13);
            dtm.addRow(vectorLast);
            labSum.setText("Итого:" + sum1);
        }
    }

    private List<Specific> groups(int num) {
        HashSet<String> hs = new HashSet();
        List<Specific> list = new ArrayList();
        Map<String, Specific> map = new HashMap();

        for (Specific spc : iwin.listSpec) {
            String key = (num == 1)
                    ? spc.name + spc.artikl + spc.colorID1 + spc.colorID2 + spc.colorID3 + spc.width + spc.height + spc.anglCut1 + spc.anglCut2 + spc.wastePrc + spc.price1
                    : spc.name + spc.artikl + spc.colorID1 + spc.colorID2 + spc.colorID3 + spc.wastePrc + spc.price1;
            if (hs.add(key)) {
                map.put(key, new Specific(spc));
            } else {
                Specific s = map.get(key);
                s.weight = s.weight + spc.weight;
                s.anglCut1 = 0;
                s.anglCut2 = 0;
                s.anglHoriz = 0;
                s.count = s.count + spc.count;
                s.quant1 = s.quant1 + spc.quant1;
                s.quant2 = s.quant2 + spc.quant2;
                s.price2 = s.price2 + spc.price2;
                s.cost1 = s.cost1 + spc.cost1;
                s.cost2 = s.cost2 + spc.cost2;
            }
        }
        map.entrySet().forEach(act -> list.add(act.getValue()));
        Collections.sort(list, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name).compareTo(o2.place.subSequence(0, 3) + o2.name));
        return list;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnArtikles = new javax.swing.JButton();
        btnConstructiv = new javax.swing.JButton();
        cbx1 = new javax.swing.JComboBox<>();
        cbx2 = new javax.swing.JComboBox<>();
        btnTest = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        south = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        labSum = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Спецификация");

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(900, 29));

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

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport.setFocusable(false);
        btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
            }
        });

        btnArtikles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c055.gif"))); // NOI18N
        btnArtikles.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnArtikles.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArtikles.setFocusable(false);
        btnArtikles.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnArtikles.setMaximumSize(new java.awt.Dimension(25, 25));
        btnArtikles.setMinimumSize(new java.awt.Dimension(25, 25));
        btnArtikles.setPreferredSize(new java.awt.Dimension(25, 25));
        btnArtikles.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnArtikles.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnArtikles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtikles(evt);
            }
        });

        btnConstructiv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c014.gif"))); // NOI18N
        btnConstructiv.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnConstructiv.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnConstructiv.setFocusable(false);
        btnConstructiv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConstructiv.setMaximumSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setMinimumSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setPreferredSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnConstructiv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConstructiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructiv(evt);
            }
        });

        cbx1.setBackground(new java.awt.Color(212, 208, 200));
        cbx1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbx1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Детализация 1 ур.", "Детализация 2 ур.", "Детализация 3 ур.", "Соединения", "Вставки", "Заполнения", "Фурнитура" }));
        cbx1.setSelectedIndex(1);
        cbx1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cbx1.setPreferredSize(new java.awt.Dimension(140, 25));
        cbx1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxGroupBy(evt);
            }
        });

        cbx2.setBackground(new java.awt.Color(212, 208, 200));
        cbx2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbx2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Учитывать норму отх.", "Без нормы отхода " }));
        cbx2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cbx2.setPreferredSize(new java.awt.Dimension(160, 25));
        cbx2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCalcType(evt);
            }
        });

        btnTest.setText("Test");
        btnTest.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTest.setMaximumSize(new java.awt.Dimension(25, 25));
        btnTest.setMinimumSize(new java.awt.Dimension(25, 25));
        btnTest.setPreferredSize(new java.awt.Dimension(25, 25));
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest(evt);
            }
        });

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c038.gif"))); // NOI18N
        btnRefresh.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnRefresh.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRefresh.setFocusable(false);
        btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefresh.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRefresh.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRefresh.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRefresh.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbx2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnArtikles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConstructiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 318, Short.MAX_VALUE)
                .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnArtikles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConstructiv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbx2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(900, 500));
        centr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", null, "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {"", "", null, "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nпп", "PK", "FK", "Расположенние", "Артикул", "Наименование", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Масса", "  1", "  2", "гориз.", "<html>Кол.<br/>единиц", "<html>Единица<br/>измерения", "<html>Процент<br/> отхода", "<html>Кол.без<br/>отхода", "<html>Кол. с <br/>отходом", "за ед. изм.", "с отх.", "без ск.", "со ск."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Specification.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(24);
            tab1.getColumnModel().getColumn(0).setMaxWidth(40);
            tab1.getColumnModel().getColumn(1).setMinWidth(0);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(34);
            tab1.getColumnModel().getColumn(1).setMaxWidth(40);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(28);
            tab1.getColumnModel().getColumn(2).setMaxWidth(40);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(3).setMaxWidth(60);
            tab1.getColumnModel().getColumn(4).setPreferredWidth(100);
            tab1.getColumnModel().getColumn(5).setPreferredWidth(240);
            tab1.getColumnModel().getColumn(9).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(10).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(11).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(12).setPreferredWidth(28);
            tab1.getColumnModel().getColumn(13).setPreferredWidth(28);
            tab1.getColumnModel().getColumn(14).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(15).setPreferredWidth(30);
            tab1.getColumnModel().getColumn(16).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(17).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(18).setPreferredWidth(48);
            tab1.getColumnModel().getColumn(19).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(20).setPreferredWidth(44);
            tab1.getColumnModel().getColumn(21).setPreferredWidth(44);
            tab1.getColumnModel().getColumn(22).setPreferredWidth(52);
            tab1.getColumnModel().getColumn(23).setPreferredWidth(52);
        }

        centr.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        filler1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.add(filler1);

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

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        tab1.setColumnSelectionInterval(5, 5);
    }//GEN-LAST:event_btnReport

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        filterTable.mousePressed((JTable) evt.getSource());
    }//GEN-LAST:event_mousePressed

    private void btnArtikles(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtikles
        float id = Util.getFloat(tab1.getValueAt(tab1.getSelectedRow(), 1).toString());
        Specific recordSpc = iwin.listSpec.stream().filter(spc -> spc.id == id).findFirst().get();
        FrameProgress.create(this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Artikles.createFrame(Specification.this, recordSpc.artiklDet);
            }
        });
    }//GEN-LAST:event_btnArtikles

    private void btnConstructiv(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConstructiv
        float id = Util.getFloat(tab1.getValueAt(tab1.getSelectedRow(), 1).toString());
        String str = tab1.getValueAt(tab1.getSelectedRow(), 3).toString().substring(0, 3);
        Specific recordSpc = iwin.listSpec.stream().filter(spc -> spc.id == id).findFirst().get();
        Record recordDet = recordSpc.detailRec;
        if (recordDet != null) {
            FrameProgress.create(Specification.this, new ListenerFrame() {
                public void actionRequest(Object obj) {
                    if (str.equals("ВСТ")) {
                        App.Element.createFrame(Specification.this, iwin.calcElements.listVariants, recordDet.getInt(eElemdet.id));

                    } else if (str.equals("СОЕ")) {
                        App.Joining.createFrame(Specification.this, iwin.calcJoining.listVariants, recordDet.getInt(eJoindet.id));

                    } else if (str.equals("ЗАП")) {
                        App.Filling.createFrame(Specification.this, iwin.calcFilling.listVariants, recordDet.getInt(eGlasdet.id));

                    } else if (str.equals("ФУР")) {
                        App.Furniture.createFrame(Specification.this, iwin.calcFurniture.listVariants, recordDet.getInt(eFurndet.id));
                    }
                }
            });
        } else {
            FrameProgress.create(Specification.this, new ListenerFrame() {
                public void actionRequest(Object obj) {
                    if (str.equals("ВСТ")) {
                        App.Systree.createFrame(Specification.this, recordSpc.artiklDet.getInt(eArtikl.id));

                    } else if (str.equals("ЗАП")) {
                        App.Systree.createFrame(Specification.this);
                    }
                }
            });
        }
    }//GEN-LAST:event_btnConstructiv

    private void cbxGroupBy(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxGroupBy

        filterTable.getTxt().setText(null);
        float id = (Uti5.getIndexRec(tab1) == -1) ? -1 : Util.getFloat(tab1.getValueAt(Uti5.getIndexRec(tab1), 1).toString());

        if (cbx1.getSelectedIndex() == 0) {
            loadingTab1(iwin.listSpec);

        } else if (cbx1.getSelectedIndex() == 1) {
            loadingTab1(groups(1));

        } else if (cbx1.getSelectedIndex() == 2) {
            loadingTab1(groups(2));

        } else if (cbx1.getSelectedIndex() == 3) {
            List<Specific> listSpec = iwin.listSpec.stream().filter(rec -> "СОЕ".equals(rec.place.substring(0, 3))).collect(toList());
            loadingTab1(listSpec);

        } else if (cbx1.getSelectedIndex() == 4) {
            List<Specific> listSpec = iwin.listSpec.stream().filter(rec -> "ВСТ".equals(rec.place.substring(0, 3))).collect(toList());
            loadingTab1(listSpec);

        } else if (cbx1.getSelectedIndex() == 5) {
            List<Specific> listSpec = iwin.listSpec.stream().filter(rec -> "ЗАП".equals(rec.place.substring(0, 3))).collect(toList());
            loadingTab1(listSpec);

        } else if (cbx1.getSelectedIndex() == 6) {
            List<Specific> listSpec = iwin.listSpec.stream().filter(rec -> "ФУР".equals(rec.place.substring(0, 3))).collect(toList());
            loadingTab1(listSpec);
        }

        for (int i = 0; i < tab1.getRowCount() - 1; i++) {
            if (tab1.getValueAt(i, 1) != null && Util.getFloat(tab1.getValueAt(i, 1).toString()) == id) {
                Uti5.setSelectedRow(tab1, i);
                return;
            }
        }
        Uti5.setSelectedRow(tab1);
    }//GEN-LAST:event_cbxGroupBy

    private void cbxCalcType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCalcType
        FrameProgress.create(this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                createIwin();
                loadingTab1(iwin.listSpec);
                Uti5.setSelectedRow(tab1);
            }
        });
    }//GEN-LAST:event_cbxCalcType

    private void btnTest(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest
        FrameProgress.create(Specification.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.DBCompare.createFrame(Specification.this, iwin);
            }
        });
    }//GEN-LAST:event_btnTest

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        int index = Uti5.getIndexRec(tab1);
        createIwin();
        loadingTab1(groups(cbx1.getSelectedIndex()));
        Uti5.setSelectedRow(tab1, index);
    }//GEN-LAST:event_btnRefresh

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArtikles;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConstructiv;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnTest;
    private javax.swing.JComboBox<String> cbx1;
    private javax.swing.JComboBox<String> cbx2;
    private javax.swing.JPanel centr;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel labSum;
    private javax.swing.JPanel north;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        filterTable = new FilterTable(tab1, 4);
        south.add(filterTable, 0);
        filterTable.getTxt().grabFocus();

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tab1.getModel());
        tab1.setRowSorter(sorter);
        tab1.getTableHeader().setPreferredSize(new Dimension(0, 32));
//        DefaultTableModel dtm = new DefaultTableModel() {
//            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//            }
//        };
//        tab1.setModel(dtm);
        DefaultTableCellRenderer cellRenderer0 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    value = (value.equals("virtual")) ? null : value;
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        DefaultTableCellRenderer cellRenderer1 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    value = (Util.getFloat(value.toString()) > 0) ? df0.format(value) : null;
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        DefaultTableCellRenderer cellRenderer2 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    value = (Util.getFloat(value.toString()) > 0) ? df1.format(value) : null;
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        DefaultTableCellRenderer cellRenderer3 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    value = (Util.getFloat(value.toString()) > 0) ? df2.format(value) : null;
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        DefaultTableCellRenderer cellRenderer4 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    value = (Util.getFloat(value.toString()) > 0) ? df3.format(value) : null;
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        tab1.getColumnModel().getColumn(6).setCellRenderer(cellRenderer0);
        tab1.getColumnModel().getColumn(7).setCellRenderer(cellRenderer0);
        tab1.getColumnModel().getColumn(8).setCellRenderer(cellRenderer0);
        tab1.getColumnModel().getColumn(9).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(10).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(11).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(12).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(13).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(14).setCellRenderer(cellRenderer1);
        tab1.getColumnModel().getColumn(15).setCellRenderer(cellRenderer1);
        tab1.getColumnModel().getColumn(17).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(18).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(19).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(20).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(21).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(22).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(23).setCellRenderer(cellRenderer3);
        TableColumnModel cm = tab1.getColumnModel();
        ColumnGroup angl = new ColumnGroup("Угол");
        angl.add(cm.getColumn(12));
        angl.add(cm.getColumn(13));
        angl.add(cm.getColumn(14));
        ColumnGroup sebe = new ColumnGroup("Себестоимость");
        sebe.add(cm.getColumn(20));
        sebe.add(cm.getColumn(21));
        ColumnGroup cost = new ColumnGroup("Стоимость");
        cost.add(cm.getColumn(22));
        cost.add(cm.getColumn(23));
        GroupableTableHeader header = (GroupableTableHeader) tab1.getTableHeader();
        header.addColumnGroup(sebe);
        header.addColumnGroup(angl);
        header.addColumnGroup(cost);
    }
}
