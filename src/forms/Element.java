package forms;

import common.FrameAdapter;
import common.FrameListener;
import common.FrameToFile;
import common.Util;
import static common.Util.scrollRectToVisible;
import dataset.ConnApp;
import dataset.Enam;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eParams;
import domain.eElemdet;
import domain.eElement;
import domain.eElemgrp;
import domain.eElempar1;
import domain.eElempar2;
import domain.eJoindet;
import domain.eJoinvar;
import domain.eSysprof;
import enums.ParamList;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefFieldEditor;
import swing.DefTableModel;

public class Element extends javax.swing.JFrame
        implements FrameListener<DefTableModel, Object> {

    private Query qElemgrp = new Query(eElemgrp.values()).select(eElemgrp.up, "order by", eElemgrp.level, ",", eElemgrp.name);
    private Query qElement = new Query(eElement.values(), eArtikl.values());
    private Query qElemdet = new Query(eElemdet.values(), eArtikl.values());
    private Query qElempar1 = new Query(eElempar1.values(), eParams.values());
    private Query qElempar2 = new Query(eElempar2.values(), eParams.values());
    private FrameListener listenerFrame = null;
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;
    private FocusListener listenerFocus = new FocusListener() {

        javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

        public void focusGained(FocusEvent e) {
            FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            tab1.setBorder(null);
            tab2.setBorder(null);
            tab3.setBorder(null);
            tab4.setBorder(null);
            tab4.setBorder(null);
            if (e.getSource() instanceof JTable) {
                ((JTable) e.getSource()).setBorder(border);
            }
        }

        public void focusLost(FocusEvent e) {
        }
    };
    private FrameListener<Object, Record> listenerDict = new FrameListener<Object, Record>() {
        @Override
        public void actionResponse(Record record) {
            System.out.println(record);
        }
    };

    public Element() {
        initComponents();
        initElements();
        initDatamodel();
        loadingTab1();
    }

    public Element(java.awt.Window owner, int nuni) {
        initComponents();
        initElements();
        this.nuni = nuni;
        this.owner = owner;
        listenerFrame = (FrameListener) owner;
        owner.setEnabled(false);
        Query query = new Query(eSysprof.artikl_id).select(eSysprof.up, "where", eSysprof.systree_id, "=", nuni).table(eSysprof.up);
        query.stream().forEach(rec -> subsql = subsql + "," + rec.getStr(eSysprof.artikl_id));
        subsql = "(" + subsql.substring(1) + ")";
        initDatamodel();
        loadingTab1();
    }

    private void initDatamodel() {

        tab1.getTableHeader().setEnabled(false);
        new DefTableModel(tab1, qElemgrp, eElemgrp.name);
        new DefTableModel(tab2, qElement, eArtikl.code, eArtikl.name, eElement.name, eElement.vtype,
                eArtikl.series, eElement.bind, eElement.bind, eElement.markup);
        new DefTableModel(tab3, qElemdet, eArtikl.code, eArtikl.name, eElemdet.color_fk, eElemdet.types);
        new DefTableModel(tab4, qElempar1, eElempar1.grup, eElempar1.text) {

            public Object actionPreview(Field field, int row, Object val) {

                if (field == eElempar1.grup && val != null) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        return qElempar1.table(eParams.up).get(row).get(eParams.text);

                    } else {
                        int numb = qElempar1.getAs(row, eElempar1.numb, -1);
                        for (Enam en : ParamList.values()) {
                            if (en.numb() == Integer.valueOf(String.valueOf(val))) {
                                return en.text();
                            }
                        }
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qElempar2, eElempar2.grup, eElempar2.text) {

            public Object actionPreview(Field field, int row, Object val) {

                if (field == eElempar2.grup && val != null) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        return qElempar2.table(eParams.up).get(row).get(eParams.text);

                    } else {
                        int numb = qElempar2.getAs(row, eElempar2.numb, -1);
                        for (Enam en : ParamList.values()) {
                            if (en.numb() == Integer.valueOf(String.valueOf(val))) {
                                return en.text();
                            }
                        }
                    }
                }
                return val;
            }
        };

        JButton btnT2C1 = new JButton("...");
        tab2.getColumnModel().getColumn(1).setCellEditor(new DefFieldEditor(listenerDict, btnT2C1));
        btnT2C1.addActionListener(event -> {

            DicArtikl frame = new DicArtikl(this, listenerDict, 1, 2, 3);
            FrameToFile.setFrameSize(frame);
            frame.setVisible(true);
        });

        JButton btnT4C0 = new JButton("...");
        tab4.getColumnModel().getColumn(0).setCellEditor(new DefFieldEditor(listenerDict, btnT4C0));
        btnT4C0.addActionListener(event -> {

            DicEnums frame = new DicEnums(this, listenerDict, 31000, 37000);
            FrameToFile.setFrameSize(frame);
            frame.setVisible(true);
        });

        JButton btnT5C0 = new JButton("...");
        tab5.getColumnModel().getColumn(0).setCellEditor(new DefFieldEditor(listenerDict, btnT5C0));
        btnT5C0.addActionListener(event -> {

            DicEnums frame = new DicEnums(this, listenerDict, 33000, 34000, 38000, 39000, 40000);
            FrameToFile.setFrameSize(frame);
            frame.setVisible(true);
        });
    }

    private void loadingTab1() {

        Record record = qElemgrp.table(eElemgrp.up).newRecord(Query.SEL);
        record.setNo(eElemgrp.id, -1);
        record.setNo(eElemgrp.name, "<html><font size='3' color='red'>&nbsp;&nbsp;&nbsp;ПРОФИЛИ</font>");
        qElemgrp.table(eElemgrp.up).add(0, record);
        for (int index = 0; index < qElemgrp.table(eElemgrp.up).size(); ++index) {
            int level = qElemgrp.table(eElemgrp.up).getAs(index, eElemgrp.level, -1);
            if (level == 5) {
                Record record2 = qElemgrp.table(eElemgrp.up).newRecord(Query.SEL);
                record2.setNo(eElemgrp.id, -5);
                record2.setNo(eElemgrp.name, "<html><font size='3' color='red'>&nbsp;&nbsp;ЗАПОЛНЕНИЯ</font>");
                qElemgrp.table(eElemgrp.up).add(index, record2);
                break;
            }
        }
        Util.selectRecord(tab1, 0);
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = tab1.getSelectedRow();
        if (row != -1) {
            Record record = qElemgrp.get(row);
            Integer id = record.getInt(eElemgrp.id);
            if (id == -1 || id == -5) {
                if (subsql.isEmpty() == false) {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "left join", eElemgrp.up, "on", eElemgrp.id, "=", eElement.elemgrp_id, "where", eElemgrp.level, "=", Math.abs(id), "and", eElement.artikl_id, "in " + subsql);
                } else {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "left join", eElemgrp.up, "on", eElemgrp.id, "=", eElement.elemgrp_id, "where", eElemgrp.level, "=", Math.abs(id));
                }
            } else {
                if (subsql.isEmpty() == false) {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "where", eElement.elemgrp_id, "=", id, "and", eElement.artikl_id, "in " + subsql, "order by", eElement.name);
                } else {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "where", eElement.elemgrp_id, "=", id, "order by", eElement.name);
                }
            }
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.selectRecord(tab2, 0);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = tab2.getSelectedRow();
        if (row != -1) {
            Record record = qElement.table(eElement.up).get(row);
            Integer p1 = record.getInt(eElement.id);
            qElemdet.select(eElemdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eElemdet.artikl_id, "where", eElemdet.element_id, "=", p1);
            qElempar1.select(eElempar1.up, "left join", eParams.up, "on", eParams.grup, "=", eElempar1.grup,
                    "and", eParams.numb, "= 0", "where", eElempar1.element_id, "=", p1);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.selectRecord(tab3, 0);
            Util.selectRecord(tab4, 0);
        }
    }

    private void selectionTab3(ListSelectionEvent event) {
        int row = tab3.getSelectedRow();
        if (row != -1) {
            Record record = qElemdet.table(eElemdet.up).get(row);
            Integer p1 = record.getInt(eElemdet.id);
            qElempar2.select(eElempar2.up, "left join", eParams.up, "on", eParams.grup, "=", eElempar2.grup,
                    "and", eParams.numb, "= 0", "where", eElempar2.elemdet_id, "=", p1);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.selectRecord(tab5, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        panSouth = new javax.swing.JPanel();
        panCentr = new javax.swing.JPanel();
        panNorth2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        panCentr2 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        panWest = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Составы");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(600, 29));

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

        btnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btnFind.setToolTipText(bundle.getString("Поиск")); // NOI18N
        btnFind.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFind.setFocusable(false);
        btnFind.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFind.setMaximumSize(new java.awt.Dimension(25, 25));
        btnFind.setMinimumSize(new java.awt.Dimension(25, 25));
        btnFind.setPreferredSize(new java.awt.Dimension(25, 25));
        btnFind.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindbtnFilter(evt);
            }
        });

        javax.swing.GroupLayout panNorthLayout = new javax.swing.GroupLayout(panNorth);
        panNorth.setLayout(panNorthLayout);
        panNorthLayout.setHorizontalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153)
                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 491, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFind, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 801, Short.MAX_VALUE)
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        panCentr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panCentr.setPreferredSize(new java.awt.Dimension(858, 560));
        panCentr.setLayout(new java.awt.BorderLayout());

        panNorth2.setPreferredSize(new java.awt.Dimension(847, 320));
        panNorth2.setLayout(new java.awt.BorderLayout());

        scr2.setPreferredSize(new java.awt.Dimension(454, 320));

        tab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"aaaa", null, "vvvvvvvvvvvvvvv", "ddd", null, null, null,  new Double(0.0)},
                {"aaaa", null, "hhhhhhhhhhhhhh", "fff", null, null, null,  new Double(0.0)}
            },
            new String [] {
                "Артикул", "Название", "Наименование", "Тип состава", "Для серии", "Умолчание", "Обязательно", "Наценка"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(2).setMinWidth(160);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(4).setMaxWidth(160);
            tab2.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(5).setMaxWidth(50);
            tab2.getColumnModel().getColumn(6).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(6).setMaxWidth(50);
            tab2.getColumnModel().getColumn(7).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(7).setMaxWidth(50);
        }

        panNorth2.add(scr2, java.awt.BorderLayout.CENTER);

        scr4.setPreferredSize(new java.awt.Dimension(260, 320));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"ххххххххххххххххххх", "111"},
                {"vvvvvvvvvvvvvvvvvvv", "222"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        panNorth2.add(scr4, java.awt.BorderLayout.EAST);

        panCentr.add(panNorth2, java.awt.BorderLayout.NORTH);

        panCentr2.setLayout(new java.awt.BorderLayout());

        scr5.setPreferredSize(new java.awt.Dimension(260, 204));

        tab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxxxxxxxxxxxxxx", "111"},
                {"zzzzzzzzzzzzzzzzzzzz", "222"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab5.setFillsViewportHeight(true);
        tab5.setMinimumSize(new java.awt.Dimension(6, 64));
        tab5.setPreferredSize(new java.awt.Dimension(0, 64));
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        panCentr2.add(scr5, java.awt.BorderLayout.EAST);

        scr3.setPreferredSize(new java.awt.Dimension(454, 204));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxxxxx", null, "mmmmm", "kkkkkkk"},
                {"zzzzzzzzz", null, "mmmmm", "kkkkkkk"}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab3.setFillsViewportHeight(true);
        scr3.setViewportView(tab3);

        panCentr2.add(scr3, java.awt.BorderLayout.CENTER);

        panCentr.add(panCentr2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panWest.setPreferredSize(new java.awt.Dimension(140, 560));
        panWest.setLayout(new java.awt.BorderLayout());

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"wwwwwww"},
                {"ddddddddd"}
            },
            new String [] {
                "Категория"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);

        panWest.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panWest, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        qElemgrp.select(eElemgrp.up, "order by", eElemgrp.level, ",", eElemgrp.name);
        loadingTab1();
        Util.selectRecord(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?",
                "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            if (tab1.getBorder() != null) {
                int row = tab1.getSelectedRow();
                if (row != -1) {
                    Record record = qElemgrp.get(row);
                    record.set(eElemgrp.up, Query.DEL);
                    qElemgrp.delete(record);
                    qElemgrp.removeRec(row);
                    ((DefTableModel) tab1.getModel()).fireTableDataChanged();
                }
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            int row = tab1.getSelectedRow();
            if (row != -1) {
                int level = qElemgrp.getAs(row, eElemgrp.level, -999);
                Record elemgrpRec = qElemgrp.newRecord(Query.INS);
                elemgrpRec.setNo(eElemgrp.id, ConnApp.instanc().generatorId(eElemgrp.up));
                elemgrpRec.setNo(eElemgrp.level, level);
                for (int index = 0; index < qElemgrp.size(); index++) {
                    if ((int) qElemgrp.getAs(index, eElemgrp.id, -999) == -1 * level) {
                        qElemgrp.add(++index, elemgrpRec);
                        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
                        Rectangle cellRect = tab1.getCellRect(index, 0, false);
                        tab1.scrollRectToVisible(cellRect);
                    }
                }
            }
        } else if (tab2.getBorder() != null) {
            int row = tab1.getSelectedRow();
            if (row != -1) {
                int elegrp_id = qElemgrp.getAs(row, eElemgrp.id, -999);
                Record elementRec = qElement.newRecord(Query.INS);
                elementRec.setNo(eElement.id, ConnApp.instanc().generatorId(eElement.up));
                elementRec.setNo(eElement.elemgrp_id, elegrp_id);
                qElement.add(elementRec);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                scrollRectToVisible(qElement, tab2);
            }
        } else if (tab3.getBorder() != null) {
            int row = tab2.getSelectedRow();
            if (row != -1) {
                Record elementRec = qElement.get(row);
                Record elemdetRec = qElemdet.newRecord(Query.INS);
                elemdetRec.setNo(eElemdet.id, ConnApp.instanc().generatorId(eElemdet.up));
                elemdetRec.setNo(eElemdet.element_id, elementRec.getInt(eElement.id));
                qElemdet.add(elemdetRec);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qElemdet, tab3);
            }
        } else if (tab4.getBorder() != null) {
            int row = tab2.getSelectedRow();
            if (row != -1) {
                Record elementRec = qElement.get(row);
                Record elempar1Rec = qElempar1.newRecord(Query.INS);
                elempar1Rec.setNo(eElempar1.id, ConnApp.instanc().generatorId(eElempar1.up));
                elempar1Rec.setNo(eElempar1.element_id, elementRec.getInt(eElement.id));
                qElempar1.add(elempar1Rec);
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qElempar1, tab4);
            }
        } else if (tab5.getBorder() != null) {
            int row = tab3.getSelectedRow();
            if (row != -1) {
                Record elemdetRec = qElemdet.get(row);
                Record elempar2Rec = qElempar2.newRecord(Query.INS);
                elempar2Rec.setNo(eElempar2.id, ConnApp.instanc().generatorId(eElempar2.up));
                elempar2Rec.setNo(eElempar1.element_id, elemdetRec.getInt(eElement.id));
                qElempar2.add(elempar2Rec);
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qElempar2, tab5);
            }
        }

    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(qElemgrp, qElement, qElemdet, qElempar1, qElempar2).forEach(q -> q.execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed

    private void btnFindbtnFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindbtnFilter

    }//GEN-LAST:event_btnFindbtnFilter
// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panCentr2;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panNorth2;
    private javax.swing.JPanel panSouth;
    private javax.swing.JPanel panWest;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        btnIns.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        btnDel.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        btnRef.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Категории составов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки составов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Спецификация составов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab2(event);
                }
            }
        });
        tab3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                selectionTab3(event);
                if (event.getValueIsAdjusting() == false) {
                    selectionTab3(event);
                }
            }
        });
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
        tab3.addFocusListener(listenerFocus);
        tab4.addFocusListener(listenerFocus);
        tab5.addFocusListener(listenerFocus);
    }
}
