package frames;

import common.FrameToFile;
import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eColor;
import domain.eColmap;
import domain.eGroups;
import domain.eParams;
import enums.TypeGroups;
import frames.dialog.DicColor;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefCellBoolRenderer;
import frames.swing.DefCellEditor;
import frames.swing.DefTableModel;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import common.ListenerSQL;
import common.ListenerRecord;

public class Color extends javax.swing.JFrame {

    private Query qGroup1 = new Query(eGroups.values());
    private Query qGroup2 = new Query(eGroups.values());
    private Query qColall = new Query(eColor.values());
    private Query qColor = new Query(eColor.values());
    private Query qColmap = new Query(eColmap.values());
    private ListenerRecord listenerColor1, listenerColor2;
    private ListenerSQL preset = (record) -> {
    };

    public Color() {
        initComponents();
        initElements();
        listenerSet();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    private void loadingData() {
        qColall.select(eColor.up, "order by", eColor.name);
        qGroup1.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.COLOR.id, "order by", eGroups.name);
        qGroup2.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.COLMAP.id, "order by", eGroups.name);
    }

    private void loadingModel() {

        new DefTableModel(tab1, qGroup1, eGroups.name, eGroups.val);
        new DefTableModel(tab2, qColor, eColor.id, eColor.name, eColor.coef1, eColor.coef2, eColor.coef3, eColor.is_prod);
        new DefTableModel(tab3, qGroup2, eGroups.name, eGroups.id);
        new DefTableModel(tab4, qColmap, eColmap.color_id1, eColmap.color_id1, eColmap.color_id2, eColmap.color_id2,
                eColmap.joint, eColmap.elem, eColmap.glas, eColmap.furn, eColmap.komp, eColmap.komp) {
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eColmap.color_id1) {
                    Record record = qColall.stream().filter(rec -> rec.get(eColor.id).equals(val)).findFirst().orElse(eColor.up.newRecord());
                    if (col == 0) {
                        Record record2 = qGroup1.stream().filter(rec -> rec.get(eGroups.id).equals(record.getInt(eColor.colgrp_id))).findFirst().orElse(eColor.up.newRecord());
                        return record2.getStr(eGroups.name);
                    } else if (col == 1) {
                        return record.getStr(eColor.name);
                    }
                } else if (field == eColmap.color_id2) {
                    Record record = qColall.stream().filter(rec -> rec.get(eColor.id).equals(val)).findFirst().orElse(eColor.up.newRecord());
                    if (col == 2) {
                        Record record2 = qGroup1.stream().filter(rec -> rec.get(eGroups.id).equals(record.getInt(eColor.colgrp_id))).findFirst().orElse(eColor.up.newRecord());
                        return record2.getStr(eGroups.name);
                    } else if (col == 3) {
                        return record.getStr(eColor.name);
                    }

                }
                return val;
            }
        };
        tab2.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                int rgb = qColor.getAs(row, eColor.rgb);
                lab.setBackground(new java.awt.Color(rgb));
                return lab;
            }
        });
        tab2.getColumnModel().getColumn(5).setCellRenderer(new DefCellBoolRenderer());
        tab2.getColumnModel().getColumn(2).setCellEditor(new DefCellEditor(3));
        tab2.getColumnModel().getColumn(3).setCellEditor(new DefCellEditor(3));
        tab2.getColumnModel().getColumn(4).setCellEditor(new DefCellEditor(3));

        DefCellBoolRenderer br = new DefCellBoolRenderer();
        Arrays.asList(4, 5, 6, 7, 8, 9).forEach(index -> tab4.getColumnModel().getColumn(index).setCellRenderer(br));

        Util.setSelectedRow(tab1);
        Util.setSelectedRow(tab3);
    }

    private void listenerAdd() {

        Util.buttonCellEditor(tab2, 0).addActionListener(event -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4);
            java.awt.Color color = JColorChooser.showDialog(this, "Выбор цвета", java.awt.Color.WHITE);
            qColor.set(color.getRGB(), Util.getIndexRec(tab2), eColor.rgb);
            qColor.execsql();
        });
        Util.buttonCellEditor(tab4, 0).addActionListener(event -> {
            DicColor frame = new DicColor(this, listenerColor1);
        });
        Util.buttonCellEditor(tab4, 1).addActionListener(event -> {
            DicColor frame = new DicColor(this, listenerColor1);
        });
        Util.buttonCellEditor(tab4, 2).addActionListener(event -> {
            DicColor frame = new DicColor(this, listenerColor2);
        });
        Util.buttonCellEditor(tab4, 3).addActionListener(event -> {
            DicColor frame = new DicColor(this, listenerColor2);
        });
    }

    public void listenerSet() {

        listenerColor1 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4);
            int index = Util.getIndexRec(tab4);
            Record record2 = qColmap.get(index);
            record2.set(eColmap.color_id1, record.getInt(eParams.id));
            qColmap.update(record2);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, index);
        };

        listenerColor2 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4);
            int index = Util.getIndexRec(tab4);
            Record record2 = qColmap.get(index);
            record2.set(eColmap.color_id2, record.getInt(eParams.id));
            qColmap.update(record2);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, index);
        };
    }

    public static void setDefaultTableEditorsClicks(JTable table, int clickCountToStart) {
        TableCellEditor editor;
        editor = table.getDefaultEditor(Object.class);
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor) editor).setClickCountToStart(clickCountToStart);
        }
        editor = table.getDefaultEditor(Number.class);
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor) editor).setClickCountToStart(clickCountToStart);
        }
        editor = table.getDefaultEditor(Boolean.class);
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor) editor).setClickCountToStart(clickCountToStart);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {

        Util.stopCellEditing(tab1, tab2, tab3, tab4);
        Arrays.asList(qGroup1, qColor, qGroup2, qColmap).forEach(q -> q.execsql());
        int index = Util.getIndexRec(tab1);
        if (index != -1) {

            Record record = qGroup1.table(eGroups.up).get(index);
            Integer cgrup = record.getInt(eGroups.id);
            qColor.select(eColor.up, "where", eColor.colgrp_id, "=" + cgrup);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
        }
    }

    private void selectionTab3(ListSelectionEvent event) {
        Util.stopCellEditing(tab1, tab2, tab3, tab4);
        Arrays.asList(qGroup2, qColmap).forEach(q -> q.execsql());
        int index = Util.getIndexRec(tab3);
        if (index != -1) {
            Record record = qGroup2.get(index);
            Integer cgrup = record.getInt(eGroups.id);
            qColmap.select(eColmap.up, "where", eColmap.colgrp_id, "=" + cgrup);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4);
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
        btnRep = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pan1 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Текстура");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Color.this.windowClosed(evt);
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
        btnIns.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsert(evt);
            }
        });

        btnRep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnRep.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnRep.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRep.setFocusable(false);
        btnRep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRep.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRep.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRep.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRep.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepActionPerformed(evt);
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
                .addGap(94, 94, 94)
                .addComponent(btnRep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 545, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setPreferredSize(new java.awt.Dimension(800, 500));
        centr.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(800, 500));

        pan1.setPreferredSize(new java.awt.Dimension(800, 500));
        pan1.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);
        scr2.setAutoscrolls(true);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "111", null, null, null, null, null},
                {null, "222", null, null, null, null, null}
            },
            new String [] {
                "Код текстуры", "Название", "Коэф.(основн.текстура)", "Коэф.(внутр.текстура)", "Коэф.(внешн.текстура)", "Для изделий", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(0).setMaxWidth(120);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(260);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(5).setMaxWidth(120);
            tab2.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        pan1.add(scr2, java.awt.BorderLayout.CENTER);

        scr1.setPreferredSize(new java.awt.Dimension(300, 584));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1111111",  new Double(1.0), null},
                {"222222",  new Double(3.0), null}
            },
            new String [] {
                "Название групп", "Коэффициент", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
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
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setMaxWidth(60);
            tab1.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        pan1.add(scr1, java.awt.BorderLayout.WEST);

        jTabbedPane1.addTab("<html><font size=\"3\">Справочник цветов      ", pan1);

        pan2.setPreferredSize(new java.awt.Dimension(800, 500));
        pan2.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(null);
        scr3.setAutoscrolls(true);
        scr3.setPreferredSize(new java.awt.Dimension(300, 600));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1111111111", null},
                {"2222222222", null}
            },
            new String [] {
                "Название группы", "ID"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setMaxWidth(40);
        }

        pan2.add(scr3, java.awt.BorderLayout.WEST);

        scr4.setBorder(null);
        scr4.setAutoscrolls(true);

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
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class
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
                tabMousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(0).setPreferredWidth(100);
            tab4.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab4.getColumnModel().getColumn(2).setPreferredWidth(100);
            tab4.getColumnModel().getColumn(3).setPreferredWidth(200);
            tab4.getColumnModel().getColumn(10).setMaxWidth(40);
        }

        pan2.add(scr4, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("<html><font size=\"3\">Группы соответствия цветов", pan2);

        centr.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));
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
                filterCaretUpdate(evt);
            }
        });
        south.add(txtFilter);

        checkFilter.setText("в конце строки");
        south.add(checkFilter);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Util.stopCellEditing(tab1, tab2, tab3, tab4);
        Arrays.asList(tab1, tab2, tab3, tab4).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        Arrays.asList(tab1, tab2, tab3, tab4).forEach(tab -> ((DefaultTableModel) tab.getModel()).fireTableDataChanged());
        Arrays.asList(tab1, tab2, tab3, tab4).forEach(tab -> Util.setSelectedRow(tab));
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab2) == 0) {
                Util.deleteRecord(tab1);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab2);
            }

        } else if (tab3.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab4) == 0) {
                Util.deleteRecord(tab3);
            }
        } else if (tab4.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab4);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.COLOR.id);
                record.set(eGroups.name, "");
                record.set(eGroups.val, 1);
            });
        } else if (tab2.getBorder() != null) {

            Util.insertRecord(tab2, eColor.up, (record) -> {
                Record groupRec = qGroup1.get(Util.getIndexRec(tab1));
                record.setNo(eColor.colgrp_id, groupRec.getInt(eGroups.id));
                qColall.add(record);
            });

        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab3, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.COLMAP.id);
                record.set(eGroups.name, "");
            });

        } else if (tab4.getBorder() != null) {
            Util.insertRecord(tab4, eColor.up, (record) -> {
                Record groupRec = qGroup2.get(Util.getIndexRec(tab3));
                record.setNo(eColmap.colgrp_id, groupRec.getInt(eGroups.id));
                record.set(eColmap.joint, 1);
                record.set(eColmap.elem, 1);
                record.set(eColmap.glas, 1);
                record.set(eColmap.furn, 1);
                record.set(eColmap.otkos, 1);
                record.set(eColmap.komp, 1);
            });
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2, tab3, tab4);
        Arrays.asList(tab1, tab2, tab3, tab4).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
    }//GEN-LAST:event_windowClosed

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.updateBorderAndSql(table, Arrays.asList(tab1, tab2, tab3, tab4));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab1, tab2, tab3, tab4).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate

    private void btnRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepActionPerformed

    }//GEN-LAST:event_btnRepActionPerformed
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnRep;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labFilter;
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
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {

        new FrameToFile(this, btnClose);
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab3(event);
                }
            }
        });
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Группы текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Описание текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Текстуры профилей", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Группы отображения текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
    }
}
