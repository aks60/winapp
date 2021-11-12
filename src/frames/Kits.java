package frames;

import common.listener.ListenerRecord;
import dataset.Conn;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eColmap;
import domain.eKitdet;
import domain.eKitpar1;
import domain.eKits;
import frames.dialog.DicArtikl2;
import frames.dialog.DicColor;
import frames.swing.DefCellBoolRenderer;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import java.util.HashSet;
import javax.swing.JRadioButton;

public class Kits extends javax.swing.JFrame {

    private Query qKits = new Query(eKits.values());
    private Query qKitdet = new Query(eKitdet.values());
    private Query qKitpar1 = new Query(eKitpar1.values());
    private ListenerRecord listenerArtikl, listenerColor1, listenerColor2, listenerColor3;

    public Kits() {
        initComponents();
        initElements();
        listenerSet();
        loadingData(0);
        loadingModel();
        listenerAdd();
    }

    public void loadingData(int type) {
        qKits.select(eKits.up, "where", eKits.types, "=", type, "order by", eKits.name);
    }

    public void loadingModel() {
        new DefTableModel(tab1, qKits, eKits.name);
        new DefTableModel(tab2, qKitdet, eKitdet.artikl_id, eKitdet.artikl_id, eKitdet.color1_id, eKitdet.color2_id, eKitdet.color3_id, eKitdet.flag) {

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
        new DefTableModel(tab3, qKitpar1, eKitpar1.kitdet_id, eKitpar1.text);
        DefCellBoolRenderer br = new DefCellBoolRenderer();
        tab2.getColumnModel().getColumn(5).setCellRenderer(br);
        UGui.setSelectedRow(tab1);
    }

    public void listenerAdd() {
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
    }

    public void listenerSet() {

        listenerArtikl = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            if (tab2.getBorder() != null) {
                int index = UGui.getIndexRec(tab2);
                qKitdet.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab2), eKitdet.artikl_id);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                UGui.setSelectedIndex(tab2, index);
            }
        };

        listenerColor1 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            int index = UGui.getIndexRec(tab2);
            Record record2 = qKitdet.get(index);
            record2.set(eKitdet.color1_id, record.getInt(eColor.id));
            ((DefaultTableModel) tab2.getModel()).fireTableRowsUpdated(tab2.getSelectedRow(), tab2.getSelectedRow());
        };

        listenerColor2 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            int index = UGui.getIndexRec(tab2);
            Record record2 = qKitdet.get(index);
            record2.set(eKitdet.color2_id, record.getInt(eColor.id));
            ((DefaultTableModel) tab2.getModel()).fireTableRowsUpdated(tab2.getSelectedRow(), tab2.getSelectedRow());
        };

        listenerColor3 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3);
            int index = UGui.getIndexRec(tab2);
            Record record2 = qKitdet.get(index);
            record2.set(eKitdet.color3_id, record.getInt(eColor.id));
            ((DefaultTableModel) tab2.getModel()).fireTableRowsUpdated(tab2.getSelectedRow(), tab2.getSelectedRow());
        };
    }

    public void selectionTab1(ListSelectionEvent event) {
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
        int index = UGui.getIndexRec(tab2);
        if (index != -1) {
            Record record = qKitdet.get(index);
            Integer id = record.getInt(eKitdet.id);
            qKitpar1.select(eKitpar1.up, "where", eKitpar1.kitdet_id, "=", id, "order by", eKitpar1.grup);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab3);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup1 = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        rbtn1 = new javax.swing.JRadioButton();
        rbtn2 = new javax.swing.JRadioButton();
        rbtn3 = new javax.swing.JRadioButton();
        rbtn4 = new javax.swing.JRadioButton();
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

        btnGroup1.add(rbtn1);
        rbtn1.setFont(frames.UGui.getFont(0, 0));
        rbtn1.setSelected(true);
        rbtn1.setText("Продажа");
        rbtn1.setMaximumSize(new java.awt.Dimension(96, 25));
        rbtn1.setMinimumSize(new java.awt.Dimension(96, 25));
        rbtn1.setPreferredSize(new java.awt.Dimension(96, 25));
        rbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnActionPerformed(evt);
            }
        });

        btnGroup1.add(rbtn2);
        rbtn2.setFont(frames.UGui.getFont(0, 0));
        rbtn2.setText("Скатка");
        rbtn2.setMaximumSize(new java.awt.Dimension(96, 25));
        rbtn2.setMinimumSize(new java.awt.Dimension(96, 25));
        rbtn2.setPreferredSize(new java.awt.Dimension(96, 25));
        rbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnActionPerformed(evt);
            }
        });

        btnGroup1.add(rbtn3);
        rbtn3.setFont(frames.UGui.getFont(0, 0));
        rbtn3.setText("Стеклопакет");
        rbtn3.setMaximumSize(new java.awt.Dimension(96, 25));
        rbtn3.setMinimumSize(new java.awt.Dimension(96, 25));
        rbtn3.setPreferredSize(new java.awt.Dimension(96, 25));
        rbtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnActionPerformed(evt);
            }
        });

        btnGroup1.add(rbtn4);
        rbtn4.setFont(frames.UGui.getFont(0, 0));
        rbtn4.setText("Ламинация");
        rbtn4.setMaximumSize(new java.awt.Dimension(96, 25));
        rbtn4.setMinimumSize(new java.awt.Dimension(96, 25));
        rbtn4.setPreferredSize(new java.awt.Dimension(96, 25));
        rbtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnActionPerformed(evt);
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
                .addGap(91, 91, 91)
                .addComponent(rbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtn4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
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
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbtn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rbtn4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                {"111111111111111",  new Integer(1)},
                {"222222222222222",  new Integer(2)}
            },
            new String [] {
                "Название комплекта", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setMaxWidth(46);
        }

        west.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(west, java.awt.BorderLayout.WEST);

        centr.setPreferredSize(new java.awt.Dimension(600, 200));
        centr.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "111", "1", "1", "1", null,  new Integer(1)},
                {"2", "222", "2", "2", "2", null,  new Integer(2)}
            },
            new String [] {
                "Артикул", "Название", "Основная текстура", "Внутренняя текстура", "Внешняя текстура", "Основной элемент", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Integer.class
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
            tab2.getColumnModel().getColumn(5).setMaxWidth(60);
            tab2.getColumnModel().getColumn(6).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(6).setMaxWidth(46);
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

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 837, Short.MAX_VALUE)
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

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Object val = 1005;
        System.out.println(eArtikl.get((int) val));

//        qKits.select(eKits.up, "order by", eKits.name);
//        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
//        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?", "Предупреждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                Record kitsRec = qKits.get(UGui.getIndexRec(tab1));
                kitsRec.set(eKits.up, Query.DEL);
                qKits.delete(kitsRec);
                qKits.removeRec(UGui.getIndexRec(tab1));
                ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
                UGui.setSelectedRow(tab1);

            } else if (tab2.getBorder() != null) {
                Record kitdetRc = qKitdet.get(UGui.getIndexRec(tab2));
                kitdetRc.set(eColor.up, Query.DEL);
                qKitdet.delete(kitdetRc);
                qKitdet.removeRec(UGui.getIndexRec(tab2));
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                UGui.setSelectedRow(tab2);

            } else if (tab3.getBorder() != null) {
                Record kitpar1Rec = qKitpar1.get(UGui.getIndexRec(tab3));
                kitpar1Rec.set(eColmap.up, Query.DEL);
                qKitpar1.delete(kitpar1Rec);
                qKitpar1.removeRec(UGui.getIndexRec(tab3));
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                UGui.setSelectedRow(tab3);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            Record record = eKits.up.newRecord(Query.INS);
            int id = Conn.instanc().genId(eKits.up);
            record.setNo(eKits.id, id);
            qKits.add(record);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();

        } else if (tab2.getBorder() != null) {
            int index = UGui.getIndexRec(tab1);
            Record kitsRec = qKits.get(index);
            Record kitdetRec = eKitdet.up.newRecord(Query.INS);
            kitdetRec.setNo(eKitdet.id, Conn.instanc().genId(eKitdet.up));
            kitdetRec.setNo(eKitdet.kits_id, kitsRec.getInt(eKits.id));
            qKitdet.add(kitdetRec);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            UGui.scrollRectToIndex(qKitdet.size() - 1, tab2);

        } else if (tab3.getBorder() != null) {
            int index = UGui.getIndexRec(tab2);
            Record kitdetRec = qKitdet.get(index);
            Record kitpar1Rec = eKitpar1.up.newRecord(Query.INS);
            kitpar1Rec.setNo(eColmap.id, Conn.instanc().genId(eColmap.up));
            kitpar1Rec.setNo(eKitpar1.kitdet_id, kitdetRec.getInt(eKitdet.id));
            qKitpar1.add(kitpar1Rec);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            UGui.scrollRectToIndex(qKitpar1.size() - 1, tab3);
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1, tab2, tab3);
        Arrays.asList(qKits, qKitdet, qKitpar1).forEach(q -> q.execsql());
    }//GEN-LAST:event_windowClosed

    private void rbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnActionPerformed
        JRadioButton rbtn = (JRadioButton) evt.getSource();
        UGui.stopCellEditing(tab1, tab2, tab3);
        Arrays.asList(tab1, tab2, tab3).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (rbtn == rbtn1) {
            loadingData(0);
        } else if (rbtn == rbtn2) {
            loadingData(1);
        } else if (rbtn == rbtn4) {
            loadingData(3);
        } else if (rbtn == rbtn3) {
            loadingData(4);
        }
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_rbtnActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.ButtonGroup btnGroup1;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JRadioButton rbtn1;
    private javax.swing.JRadioButton rbtn2;
    private javax.swing.JRadioButton rbtn3;
    private javax.swing.JRadioButton rbtn4;
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
        btnIns.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3));
        btnDel.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3));
        btnRef.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3));
        new FrameToFile(this, btnClose);
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
