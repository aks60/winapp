package frames;

import builder.param.ParamList;
import common.listener.ListenerRecord;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eKitdet;
import domain.eKitpar2;
import domain.eKits;
import domain.eParams;
import enums.Enam;
import enums.UseUnit;
import frames.dialog.DicArtikl2;
import frames.dialog.DicColor;
import frames.dialog.ParGrup2;
import frames.dialog.ParGrup2a;
import frames.dialog.ParGrup2b;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import frames.swing.FilterTable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComboBox;
import startup.Main;

public class Kits extends javax.swing.JFrame {

    private Query qKits = new Query(eKits.values());
    private Query qKitdet = new Query(eKitdet.values());
    private Query qKitpar2 = new Query(eKitpar2.values());
    private Query qParams = new Query(eParams.values());
    private FilterTable filterTable = null;
    private ListenerRecord listenerArtikl, listenerColor1, listenerColor2, listenerColor3;

    public Kits() {
        initComponents();
        initElements();
        listenerSet();
        loadingData("0");
        loadingModel();
        listenerAdd();
    }

    public void loadingData(String type) {
        eArtikl.query();
        qParams.select(eParams.up, "where", eParams.kits, "= 1 and", eParams.id, "=", eParams.params_id, "order by", eParams.text);
        qKits.select(eKits.up, "where", eKits.types, "=", type, "order by", eKits.categ, ",", eKits.name);
    }

    public void loadingModel() {
        new DefTableModel(tab1, qKits, eKits.categ, eKits.name);
        new DefTableModel(tab2, qKitdet, eKitdet.artikl_id, eKitdet.artikl_id, eKitdet.color1_id, eKitdet.color2_id, eKitdet.color3_id) {

            public Object getValueAt(int col, int row, Object val) {

                if (val != null && col == 0) {
                    return eArtikl.get((int) val).getStr(eArtikl.code);

                } else if (val != null && col == 1) {
                    return eArtikl.get((int) val).getStr(eArtikl.name);

                } else if (val != null && columns[col] == eKitdet.color1_id) {
                    return eColor.get((int) val).getStr(eColor.name);

                } else if (val != null && columns[col] == eKitdet.color2_id) {
                    return eColor.get((int) val).getStr(eColor.name);

                } else if (val != null && columns[col] == eKitdet.color3_id) {
                    return eColor.get((int) val).getStr(eColor.name);
                }
                return val;
            }
        };
        new DefTableModel(tab3, qKitpar2, eKitpar2.params_id, eKitpar2.text) {

            public Object getValueAt(int col, int row, Object val) {
                if (val != null) {
                    Field field = columns[col];
                    if (field == eKitpar2.params_id) {
                        if (Integer.valueOf(String.valueOf(val)) < 0) {
                            Record record = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                            return (Main.dev) ? val + ":" + record.getStr(eParams.text) : record.getStr(eParams.text);
                        } else {
                            Enam en = ParamList.find(Integer.valueOf(val.toString()));
                            return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                        }
                    }
                }
                return val;
            }
        };
        UGui.setSelectedRow(tab1);
    }

    public void listenerAdd() {

        UGui.buttonCellEditor(tab1, 0).addActionListener(event -> {

            Set set = new HashSet();
            qKits.forEach(rec -> set.add(rec.getStr(eKits.categ)));
            List list = Arrays.asList(set.toArray());
            Collections.sort(list);
            JComboBox jcb = new JComboBox(list.toArray());
            if (qKits.get(UGui.getIndexRec(tab1)).getStr(eKits.categ).isEmpty() == false) {
                int index = list.indexOf(qKits.get(UGui.getIndexRec(tab1)).getStr(eKits.categ));
                jcb.setSelectedIndex(index);
            }
            jcb.setEditable(true);
            JOptionPane.showMessageDialog(null, jcb, "Категория", JOptionPane.QUESTION_MESSAGE);
            Object result = jcb.getSelectedItem();

            UGui.stopCellEditing(tab1, tab2, tab3);
            int index = UGui.getIndexRec(tab1);
            qKits.set(result, index, eKits.categ);
            UGui.fireTableRowUpdated(tab1);
        });

        UGui.buttonCellEditor(tab2, 0).addActionListener(event -> {
            DicArtikl2 frame = new DicArtikl2(this, listenerArtikl, 1, 2, 3, 4, 5);
        });

        UGui.buttonCellEditor(tab2, 1).addActionListener(event -> {
            DicArtikl2 frame = new DicArtikl2(this, listenerArtikl, 1, 2, 3, 4, 5);
        });

        UGui.buttonCellEditor(tab2, 2).addActionListener(event -> {
            Record record = qKitdet.get(UGui.getIndexRec(tab2));
            int artikl_id = record.getInt(eKitdet.artikl_id);
            HashSet<Record> colorSet = UGui.artiklToColorSet(artikl_id);
            new DicColor(this, listenerColor1, colorSet);
        });

        UGui.buttonCellEditor(tab2, 3).addActionListener(event -> {
            Record record = qKitdet.get(UGui.getIndexRec(tab2));
            int artikl_id = record.getInt(eKitdet.artikl_id);
            HashSet<Record> colorSet = UGui.artiklToColorSet(artikl_id);
            new DicColor(this, listenerColor2, colorSet);
        });

        UGui.buttonCellEditor(tab2, 4).addActionListener(event -> {
            Record record = qKitdet.get(UGui.getIndexRec(tab2));
            int artikl_id = record.getInt(eKitdet.artikl_id);
            HashSet<Record> colorSet = UGui.artiklToColorSet(artikl_id);
            new DicColor(this, listenerColor3, colorSet);
        });

        UGui.buttonCellEditor(tab3, 0).addActionListener(event -> {
            int index = UGui.getIndexRec(tab2);
            if (index != -1) {
                int param = -1;
                Record recordDet = qKitdet.get(index);
                int artikl_id = recordDet.getInt(eKitdet.artikl_id);
                Record recordArt = eArtikl.get(artikl_id);
                if (UseUnit.PIE.id == recordArt.getInt(eArtikl.unit)) {
                    param = 7000;
                } else if (UseUnit.METR.id == recordArt.getInt(eArtikl.unit)) {
                    param = 8000;
                } else if (UseUnit.METR2.id == recordArt.getInt(eArtikl.unit)) {
                    param = 9000;
                }
                ParGrup2 frame = new ParGrup2(this, (rec) -> {
                    UGui.listenerParam(rec, tab3, eKitpar2.params_id, eKitpar2.text, tab1, tab2);
                }, eParams.kits, param);
            }
        });

        UGui.buttonCellEditor(tab3, 1, (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return UGui.listenerCell(tab3, component, eKitpar2.params_id);

        }).addActionListener(event -> {
            Record record = qKitpar2.get(UGui.getIndexRec(tab3));
            int grup = record.getInt(eKitpar2.params_id);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, (rec) -> {
                    UGui.listenerParam(rec, tab3, eKitpar2.params_id, eKitpar2.text, tab1, tab2, tab3);
                }, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, (rec) -> {
                    UGui.listenerParam(rec, tab3, eKitpar2.params_id, eKitpar2.text, tab1, tab2, tab3);
                }, list);
            }
        });
    }

    public void listenerSet() {

        listenerArtikl = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            if (tab2.getBorder() != null) {
                int index = UGui.getIndexRec(tab2);
                qKitdet.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab2), eKitdet.artikl_id);
                UGui.fireTableRowUpdated(tab2);
            }
        };

        listenerColor1 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            int index = UGui.getIndexRec(tab2);
            Record record2 = qKitdet.get(index);
            record2.set(eKitdet.color1_id, record.getInt(eColor.id));
            UGui.fireTableRowUpdated(tab2);
        };

        listenerColor2 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            int index = UGui.getIndexRec(tab2);
            Record record2 = qKitdet.get(index);
            record2.set(eKitdet.color2_id, record.getInt(eColor.id));
            UGui.fireTableRowUpdated(tab2);
        };

        listenerColor3 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            int index = UGui.getIndexRec(tab2);
            Record record2 = qKitdet.get(index);
            record2.set(eKitdet.color3_id, record.getInt(eColor.id));
            UGui.fireTableRowUpdated(tab2);
        };
    }

    public void selectionTab1(ListSelectionEvent event) {
        Arrays.asList(qKits, qKitdet, qKitpar2).forEach(q -> q.execsql());
        UGui.clearTable(tab2, tab3);
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record record = qKits.get(index);
            Integer id = record.getInt(eKits.id);
            qKitdet.select(eKitdet.up, "where", eKitdet.kits_id, "=", id, "order by", eKitdet.artikl_id);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab2);
        }
    }

    public void selectionTab2(ListSelectionEvent event) {
        Arrays.asList(qKits, qKitdet, qKitpar2).forEach(q -> q.execsql());
        UGui.clearTable(tab3);
        int index = UGui.getIndexRec(tab2);
        if (index != -1) {
            Record record = qKitdet.get(index);
            Integer id = record.getInt(eKitdet.id);
            qKitpar2.select(eKitpar2.up, "where", eKitpar2.kitdet_id, "=", id, "order by", eKitpar2.text);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab3);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        cbx1 = new javax.swing.JComboBox<>();
        west = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        centr = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Комплекты");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Kits.this.windowClosed(evt);
            }
        });

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

        btnRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c038.gif"))); // NOI18N
        btnRef.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnRef.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRef.setFocusable(false);
        btnRef.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRef.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRef.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRef.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRef.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnRef.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh(evt);
            }
        });

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c034.gif"))); // NOI18N
        btnDel.setToolTipText(bundle.getString("Удалить")); // NOI18N
        btnDel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnDel.setFocusable(false);
        btnDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDel.setMaximumSize(new java.awt.Dimension(25, 25));
        btnDel.setMinimumSize(new java.awt.Dimension(25, 25));
        btnDel.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete(evt);
            }
        });

        btnIns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c033.gif"))); // NOI18N
        btnIns.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnIns.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnIns.setFocusable(false);
        btnIns.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIns.setMaximumSize(new java.awt.Dimension(25, 25));
        btnIns.setMinimumSize(new java.awt.Dimension(25, 25));
        btnIns.setPreferredSize(new java.awt.Dimension(25, 25));
        btnIns.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnIns.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsert(evt);
            }
        });

        cbx1.setBackground(new java.awt.Color(212, 208, 200));
        cbx1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbx1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Продажа", "Скатка", "Ламинация", "Стеклопакет" }));
        cbx1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cbx1.setPreferredSize(new java.awt.Dimension(160, 25));
        cbx1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAction(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 527, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        west.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        west.setPreferredSize(new java.awt.Dimension(380, 10));
        west.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", "111111111111111",  new Integer(1)},
                {"222", "222222222222222",  new Integer(2)}
            },
            new String [] {
                "Категория", "Название комплекта", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(2).setMaxWidth(46);
        }

        west.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(west, java.awt.BorderLayout.WEST);

        centr.setPreferredSize(new java.awt.Dimension(600, 200));
        centr.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "111", "1", "1", "1",  new Integer(1)},
                {"2", "222", "2", "2", "2",  new Integer(2)}
            },
            new String [] {
                "Артикул", "Название", "Основная текстура", "Внутренняя текстура", "Внешняя текстура", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(140);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(260);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(5).setMaxWidth(46);
        }

        centr.add(scr2, java.awt.BorderLayout.CENTER);

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(0, 200));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", "1"},
                {"222", "2"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(20);
        }

        centr.add(scr3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Arrays.asList(qKits, qKitdet, qKitpar2).forEach(q -> q.execsql());
        int index = UGui.getIndexRec(tab1);
        int index2 = UGui.getIndexRec(tab2);
        int index3 = UGui.getIndexRec(tab3);
        loadingData(String.valueOf(cbx1.getSelectedIndex()));
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedIndex(tab1, index);
        UGui.setSelectedIndex(tab2, index2);
        UGui.setSelectedIndex(tab3, index3);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(this, tab2) == 0) {
                UGui.deleteRecord(tab1);
            }

        } else if (tab2.getBorder() != null) {
            if (UGui.isDeleteRecord(this, tab3) == 0) {
                UGui.deleteRecord(tab2);
            }

        } else if (tab3.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0) {
                UGui.deleteRecord(tab3);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            int index = UGui.getIndexRec(tab1);
            String name = qKits.getAs(index, eKits.categ);
            UGui.insertRecordCur(tab1, eKits.up, (record) -> {
                if (index != -1) {
                    record.setNo(eKits.categ, name);
                }
                record.setNo(eKits.types, cbx1.getSelectedIndex());
            });

        } else if (tab2.getBorder() != null) {
            int index = UGui.getIndexRec(tab1, 0);
            UGui.insertRecordCur(tab2, eKitdet.up, (record) -> {
                record.setNo(eKitdet.kits_id, qKits.getAs(index, eKits.id));
            });

        } else if (tab3.getBorder() != null) {
            int index = UGui.getIndexRec(tab3, 0);
            UGui.insertRecordCur(tab3, eKitpar2.up, (record) -> {
                record.setNo(eKitpar2.kitdet_id, qKitdet.getAs(index, eKitdet.id));
            });
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1, tab2, tab3);
        Arrays.asList(qKits, qKitdet, qKitpar2).forEach(q -> q.execsql());
    }//GEN-LAST:event_windowClosed

    private void comboBoxAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxAction
        UGui.stopCellEditing(tab1, tab2, tab3);
        Arrays.asList(tab1, tab2, tab3).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        int index = cbx1.getSelectedIndex();
        loadingData(String.valueOf(index));
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_comboBoxAction

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        UGui.updateBorderAndSql((JTable) evt.getSource(), Arrays.asList(tab1, tab2, tab3));
        filterTable.mousePressed((JTable) evt.getSource());
    }//GEN-LAST:event_tabMousePressed

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JComboBox<String> cbx1;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    public void initElements() {
        new FrameToFile(this, btnClose);
        filterTable = new FilterTable(0, tab1);
        south.add(filterTable, 0);
        
        filterTable.getTxt().grabFocus();
        btnIns.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3));
        btnDel.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3));
        btnRef.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3));

        FocusListener listenerFocus = new FocusListener() {

            javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

            public void focusGained(FocusEvent e) {
                UGui.stopCellEditing(tab1, tab2, tab3);
                tab1.setBorder(null);
                tab2.setBorder(null);
                tab3.setBorder(null);
                if (e.getSource() instanceof JTable) {
                    ((JTable) e.getSource()).setBorder(border);
                }
            }

            public void focusLost(FocusEvent e) {
            }
        };
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки комплектов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация комплектов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                selectionTab2(event);
            }
        });
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
        tab3.addFocusListener(listenerFocus);
    }
}
