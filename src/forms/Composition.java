package forms;

import common.FrameListener;
import common.FrameToFile;
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
import domain.eSysprof;
import enums.ParamList;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefFieldEditor;
import swing.DefTableModel;

public class Composition extends javax.swing.JFrame {

    private Query qElemgrp = new Query(eElemgrp.values()).select(eElemgrp.up, "order by", eElemgrp.level, ",", eElemgrp.name);

    private Query qElement = new Query(eElement.values(), eArtikl.values());
    private Query qElemdet = new Query(eElemdet.values(), eArtikl.values(), eParams.values());
    private Query qElempar1 = new Query(eElempar1.values(), eParams.values());
    private Query qElempar2 = new Query(eElempar2.values(), eParams.values());
    private FrameListener listenerFrame = null;
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;
    private FocusListener listenerFocus = new FocusListener() {

        javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof JTable) {
                ((JTable) e.getSource()).setBorder(border);
            }
        }

        public void focusLost(FocusEvent e) {
            if (e.getSource() instanceof JTable) {
                ((JTable) e.getSource()).setBorder(null);
            }
        }
    };
    private FrameListener<Object, Object> listenerModify = new FrameListener() {

        Icon[] btnIM = {new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c020.gif")),
            new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"))};

        public void actionRequest(Object obj) {
            btnSave.setIcon(btnIM[0]);
        }

        public void actionResponse(Object obj) {
            btnSave.setIcon(btnIM[1]);
        }
    };
    private FrameListener<Object, Object> listenerDict = new FrameListener() {

        public void actionRequest(Object obj) {
            System.out.println(".request()");
        }
    };

    public Composition() {
        initComponents();
        initElements();
        initDatamodel();
        loadDataTab1();
    }

    public Composition(java.awt.Window owner, int nuni) {
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
        loadDataTab1();
    }

    private void initDatamodel() {

        tab1.getTableHeader().setEnabled(false);
        new DefTableModel(tab1, qElemgrp, eElemgrp.name).addFrameListener(listenerModify);
        new DefTableModel(tab2, qElement, eArtikl.code, eArtikl.name,
                eElement.name, eElement.vtype, eArtikl.series, eElement.bind, eElement.bind, eElement.markup).addFrameListener(listenerModify);
        new DefTableModel(tab3, qElemdet, eArtikl.code, eArtikl.name, eParams.text, eParams.id).addFrameListener(listenerModify);
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
        }.addFrameListener(listenerModify);
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
        }.addFrameListener(listenerModify);

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

    private void loadDataTab1() {

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
        if (tab1.getRowCount() > 0) {
            tab1.setRowSelectionInterval(0, 0);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        qElement.execsql();
        qElemdet.execsql();
        listenerModify.actionResponse(null);
        int row = tab1.getSelectedRow();
        if (row != -1) {
            Record record = qElemgrp.table(eElemgrp.up).get(row);
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
            if (tab2.getRowCount() > 0) {
                tab2.setRowSelectionInterval(0, 0);
            }
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        //qElement.execsql();
        listenerModify.actionResponse(null);
        int row = tab2.getSelectedRow();
        if (row != -1) {
            Record record = qElement.table(eElement.up).get(row);
            Integer p1 = record.getInt(eElement.id);
            qElemdet.select(eElemdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eElemdet.artikl_id, "where", eElemdet.element_id, "=", p1);
            qElempar1.select(eElempar1.up, "left join", eParams.up, "on", eParams.grup, "=", eElempar1.grup, 
                    "and", eParams.numb, "= 0", "where", eElempar1.element_id, "=", p1);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            if (tab3.getRowCount() > 0) {
                tab3.setRowSelectionInterval(0, 0);
            }
        }
    }

    private void selectionTab3(ListSelectionEvent event) {
        //qElemdet.execsql();
        listenerModify.actionResponse(null);
        int row = tab3.getSelectedRow();
        if (row != -1) {
            Record record = qElemdet.table(eElemdet.up).get(row);
            Integer p1 = record.getInt(eElemdet.id);
            qElempar2.select(eElempar2.up, "left join", eParams.up, "on", eParams.grup, "=", eElempar2.grup, 
                    "and", eParams.numb, "= 0", "where", eElempar2.elemdet_id, "=", p1);            
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
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
                btnCloseClose(evt);
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"))); // NOI18N
        btnSave.setToolTipText(bundle.getString("Сохранить")); // NOI18N
        btnSave.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setMaximumSize(new java.awt.Dimension(25, 25));
        btnSave.setMinimumSize(new java.awt.Dimension(25, 25));
        btnSave.setPreferredSize(new java.awt.Dimension(25, 25));
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave(evt);
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
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 638, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                {"aaaa", "xxxxxxxxxxxxx", "vvvvvvvvvvvvvvv", "ddd", null, null, null,  new Double(0.0)},
                {"aaaa", "zzzzzzzzzzzzzzz", "hhhhhhhhhhhhhh", "fff", null, null, null,  new Double(0.0)}
            },
            new String [] {
                "Артикул", "Название артикула", "Наименование", "Тип состава", "Для серии", "Умолчание", "Обязательно", "Наценка"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
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
                {"xxxxxxxx", "vvvvvvvv", "mmmmm", "kkkkkkk"},
                {"zzzzzzzzz", "vvvvvvvv", "mmmmm", "kkkkkkk"}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab3.setFillsViewportHeight(true);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setMinWidth(160);
        }

        panCentr2.add(scr3, java.awt.BorderLayout.CENTER);

        panCentr.add(panCentr2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panWest.setPreferredSize(new java.awt.Dimension(120, 560));
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

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh

        DicEnums frame = new DicEnums(this, listenerDict, 31000);
        FrameToFile.setFrameSize(frame);
        frame.setVisible(true);
    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave

    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed
// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnSave;
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

    private void initElements() {

        new FrameToFile(this, btnClose);
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
// </editor-fold> 
}
