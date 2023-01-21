package frames;

import common.listener.ListenerRecord;
import dataset.Query;
import dataset.Record;
import domain.eColor;
import domain.eParams;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefCellRendererBool;
import frames.swing.DefTableModel;
import dataset.Field;
import domain.eColmap;
import domain.eGroups;
import enums.TypeGroups;
import frames.dialog.DicColor;
import frames.swing.DefCellEditorBtn;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;

public class Param extends javax.swing.JFrame {

    private Query qGroups1 = new Query(eGroups.values());
    private Query qGroups2 = new Query(eGroups.values());
    private Query qGroups3 = new Query(eGroups.values());
    private Query qParams = new Query(eParams.values());
    private Query qColmap = new Query(eColmap.values());
    private Query qColor = new Query(eColor.values());

    public Param() {
        initComponents();
        initElements();
        loadData();
        loadingModel();
        listenerAdd();
    }

    public void loadData() {
        qColor.select(eColor.up, "order by", eColor.name);
        qGroups1.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.PARAM_USER.id, "order by", eGroups.npp, ",", eGroups.name);
        qGroups2.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.COLOR_MAP.id, "order by", eGroups.npp, ",", eGroups.name);
        qGroups3.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.COLOR_GRP.id, "order by", eGroups.npp, ",", eGroups.name);
    }

    public void loadingModel() {

        new DefTableModel(tab1, qGroups1, eGroups.name);
        new DefTableModel(tab2, qParams, eParams.text, eParams.kits, eParams.joint, eParams.elem, eParams.glas, eParams.furn, eParams.otkos, eParams.label);

        new DefTableModel(tab3, qGroups2, eGroups.name);
        new DefTableModel(tab4, qColmap, eColmap.color_id1, eColmap.color_id1, eColmap.color_id2, eColmap.color_id2,
                eColmap.joint, eColmap.elem, eColmap.glas, eColmap.furn, eColmap.komp, eColmap.komp) {
                    
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eColmap.color_id1) {
                    Record record = qColor.stream().filter(rec -> rec.get(eColor.id).equals(val)).findFirst().orElse(eColor.up.newRecord());
                    if (col == 0) {
                        Record record2 = qGroups3.stream().filter(rec -> rec.get(eGroups.id).equals(record.getInt(eColor.groups_id))).findFirst().orElse(eColor.up.newRecord());
                        return record2.getStr(eGroups.name);
                    } else if (col == 1) {
                        return record.getStr(eColor.name);
                    }
                } else if (field == eColmap.color_id2) {
                    Record record = qColor.stream().filter(rec -> rec.get(eColor.id).equals(val)).findFirst().orElse(eColor.up.newRecord());
                    if (col == 2) {
                        Record record2 = qGroups3.stream().filter(rec -> rec.get(eGroups.id).equals(record.getInt(eColor.groups_id))).findFirst().orElse(eColor.up.newRecord());
                        return record2.getStr(eGroups.name);
                    } else if (col == 3) {
                        return record.getStr(eColor.name);
                    }

                }
                return val;
            }
        };

        DefCellRendererBool br = new DefCellRendererBool();
        List.of(1, 2, 3, 4, 5, 6, 7).forEach(index -> tab2.getColumnModel().getColumn(index).setCellRenderer(br));
        List.of(4, 5, 6, 7, 8, 9).forEach(index -> tab4.getColumnModel().getColumn(index).setCellRenderer(br));

        UGui.setSelectedRow(tab1);
        UGui.setSelectedRow(tab3);
    }

    public void listenerAdd() {

        ListenerRecord listenerColor1 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4);
            int index = UGui.getIndexRec(tab3);
            Record colmapRec = qColmap.get(index);
            colmapRec.set(eColmap.color_id1, record.get(eColor.id));
            qColmap.update(colmapRec);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            UGui.setSelectedIndex(tab4, index);
        };

        ListenerRecord listenerColor2 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4);
            int index = UGui.getIndexRec(tab3);
            Record colmapRec = qColmap.get(index);
            colmapRec.set(eColmap.color_id2, record.get(eColor.id));
            qColmap.update(colmapRec);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            UGui.setSelectedIndex(tab4, index);
        };

        UGui.buttonCellEditor(tab4, 0).addActionListener(event -> {
            new DicColor(this, listenerColor1, false, false);
        });
        UGui.buttonCellEditor(tab4, 1).addActionListener(event -> {
            new DicColor(this, listenerColor1, false, false);
        });
        UGui.buttonCellEditor(tab4, 2).addActionListener(event -> {
            new DicColor(this, listenerColor2, false, false);
        });
        UGui.buttonCellEditor(tab4, 3).addActionListener(event -> {
            new DicColor(this, listenerColor2, false, false);
        });
    }

    public void selectionTab1(ListSelectionEvent event) {
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4);
            List.of(qGroups1, qGroups2, qParams, qColmap).forEach(q -> q.execsql());
            UGui.clearTable(tab2);
            Record groupsRec = qGroups1.get(index);
            Integer id = groupsRec.getInt(eGroups.id);
            qParams.select(eParams.up, "where", eParams.groups_id, "=", id, "order by", eParams.text);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab2);
        }
    }

    public void selectionTab3(ListSelectionEvent event) {
        int index = UGui.getIndexRec(tab3);
        if (index != -1) {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4);
            List.of(qGroups1, qGroups2, qParams, qColmap).forEach(q -> q.execsql());
            UGui.clearTable(tab4);
            Record groupsRec = qGroups2.get(index);
            Integer id = groupsRec.getInt(eGroups.id);
            qColmap.select(eColmap.up, "where", eColmap.groups_id, "=", id);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab4);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        tabbPan = new javax.swing.JTabbedPane();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(454, 563));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Параметры");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Param.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/hints/okno", common.eProp.locale); // NOI18N
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
        btnIns.setToolTipText(bundle.getString("Выбрать")); // NOI18N
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 777, Short.MAX_VALUE)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnReport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(800, 550));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(454, 304));
        pan1.setLayout(new javax.swing.BoxLayout(pan1, javax.swing.BoxLayout.LINE_AXIS));

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(200, 304));

        tab1.setFont(frames.UGui.getFont(0,0));
        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11111", null},
                {"22222", null}
            },
            new String [] {
                "Название параметра", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Param.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        pan1.add(scr1);

        scr2.setBorder(null);
        scr2.setPreferredSize(new java.awt.Dimension(454, 204));

        tab2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 0, 0));
        tab2.setFont(frames.UGui.getFont(0,0));
        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1111", null, null, null, null, null, null, null, null},
                {"2222", null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Значение параметра", "Комплекты", "Соединения", "Вставки", "Заполнения", "Фурнитура", "Откосы", "Надпись", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab2mousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setMinWidth(120);
            tab2.getColumnModel().getColumn(0).setPreferredWidth(400);
            tab2.getColumnModel().getColumn(8).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(8).setMaxWidth(60);
        }

        pan1.add(scr2);
        scr2.getAccessibleContext().setAccessibleName("Значения параметров");

        tabbPan.addTab(" Параметры элементов конструкций", pan1);

        pan2.setPreferredSize(new java.awt.Dimension(454, 204));
        pan2.setLayout(new javax.swing.BoxLayout(pan2, javax.swing.BoxLayout.LINE_AXIS));

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(200, 304));

        tab3.setFont(frames.UGui.getFont(0,0));
        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11111", null},
                {"22222", null}
            },
            new String [] {
                "Название параметра", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab3mousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        pan2.add(scr3);

        scr4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));
        scr4.setAutoscrolls(true);

        tab4.setFont(frames.UGui.getFont(0,0));
        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "xxxxxx", null, null, null, null, null, null, null, null, null},
                {null, "zzzzzzz", null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Группа", "Текстура профиля", "Группа", "Текстура элемента", "Соединения", "Вставки", "Заполнения", "Фурнитура", "Откосы", "Комплекты", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        tab4.setName("tab4"); // NOI18N
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab4mousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);

        pan2.add(scr4);

        tabbPan.addTab(" Параметры соответствия текстур", pan2);

        centr.add(tabbPan, java.awt.BorderLayout.CENTER);

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
//        UGui.stopCellEditing(tab1, tab2);
//        List.of(tab1, tab2).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
//        loadData();
//        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
//        if (tab1.getBorder() != null) {
//            if (UGui.isDeleteRecord(tab1, this, tab2) == 0) {
//                UGui.deleteRecord(tab1);
//            }
//        } else if (tab2.getBorder() != null) {
//            if (UGui.isDeleteRecord(tab2, this) == 0) {
//                UGui.deleteRecord(tab2);
//            }
//        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

//        if (tab1.getBorder() != null) {
//            UGui.insertRecordEnd(tab1, eParams.up, (record) -> {
//                record.setNo(eParams.params_id, record.getInt(eParams.id));
//                record.setDev(eParams.text, "Параметр");
//                List.of(eParams.kits.ordinal(), eParams.joint.ordinal(), eParams.elem.ordinal(), eParams.glas.ordinal(),
//                        eParams.furn.ordinal(), eParams.otkos.ordinal(), eParams.color.ordinal()).forEach(index -> record.set(index, 0));
//            });
//        } else if (tab2.getBorder() != null) {
//            UGui.insertRecordEnd(tab2, eParams.up, (record) -> {
//                Record record2 = qParams.get(UGui.getIndexRec(tab1));
//                record.setNo(eParams.params_id, record2.getInt(eParams.id));
//                record.setDev(eParams.text, "Вариант");
//                List.of(eParams.kits.ordinal(), eParams.joint.ordinal(), eParams.elem.ordinal(), eParams.glas.ordinal(),
//                        eParams.furn.ordinal(), eParams.otkos.ordinal(), eParams.color.ordinal()).forEach(index -> record.set(index, 0));
//            });
//        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1, tab2, tab3, tab4);
        List.of(qGroups1, qGroups2, qParams, qColmap).forEach(q -> q.execsql());
    }//GEN-LAST:event_windowClosed

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
//        JTable table = (JTable) evt.getSource();
//        if (table == tab2) {
//            selectionTab2(null);
//        }
//        UGui.updateBorderAndSql(table, List.of(tab1, tab2));
    }//GEN-LAST:event_mousePressed

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
//        HtmlOfTable.load("Параметры", tab1);
//        ExecuteCmd.documentType(this);
    }//GEN-LAST:event_btnReport

    private void tab2mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2mousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tab2mousePressed

    private void tab3mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab3mousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tab3mousePressed

    private void tab4mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab4mousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, List.of(tab1, tab2, tab3, tab4));
    }//GEN-LAST:event_tab4mousePressed
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel centr;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTabbedPane tabbPan;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    public void initElements() {

//        new FrameToFile(this, btnClose);
//
//        TableFieldFilter filterTable = new TableFieldFilter(0, tab1, tab2);
//        south.add(filterTable, 0);
//        filterTable.getTxt().grabFocus();
        List.of(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> UGui.stopCellEditing(tab1, tab2)));

        tab1.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting() == false) {
                selectionTab1(event);
            }
        });
        tab3.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting() == false) {
                selectionTab3(event);
            }
        });
//        editorStr = (DefaultCellEditor) tab2.getColumnModel().getColumn(0).getCellEditor();
    }
}
