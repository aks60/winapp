package forms;

import common.DialogListener;
import common.FrameAdapter;
import common.FrameToFile;
import common.Util;
import common.eProfile;
import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColgrp;
import domain.eJoindet;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinpar2;
import domain.eJoinvar;
import domain.eParams;
import domain.eSysprof;
import enums.Enam;
import enums.ParamList;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefFieldEditor;
import swing.DefTableModel;

public class Joining extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.values()).select(eParams.up, "where", eParams.numb, "= 0 order by", eParams.text);
    private Query qJoining = new Query(eJoining.values());
    private Query qArtikls1 = null;
    private Query qArtikls2 = null;
    private Query qJoinvar = new Query(eJoinvar.values());
    private Query qJoindet = new Query(eJoindet.values());
    private Query qJoinpar1 = new Query(eJoinpar1.values());
    private Query qJoinpar2 = new Query(eJoinpar2.values());
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;
    private DialogListener listenerArtikl, listenerPar1, listenerPar2, listenerColor, listenerEnums;

    public Joining() {
        initComponents();
        initElements();
        initData();
        listenerDict();
        initModel();
    }

    public Joining(java.awt.Window owner, int nuni) {
        this.owner = owner;
        this.nuni = nuni;
        initComponents();
        initElements();
        initData();
        initModel();
        listenerDict();
        owner.setEnabled(false);
    }

    private void initData() {

        qArtikls1 = new Query(eArtikl.id, eArtikl.code, eArtikl.name).select(eArtikl.up, ",", eJoining.up, "where", eArtikl.id, "=", eJoining.artikl_id1);
        qArtikls2 = new Query(eArtikl.id, eArtikl.code, eArtikl.name).select(eArtikl.up, ",", eJoining.up, "where", eArtikl.id, "=", eJoining.artikl_id2);

        if (owner == null) {
            qJoining.select(eJoining.up, "order by", eJoining.name);
        } else {
            Query query = new Query(eSysprof.artikl_id).select(eSysprof.up, "where", eSysprof.systree_id, "=", nuni).table(eSysprof.up);
            query.stream().forEach(rec -> subsql = subsql + "," + rec.getStr(eSysprof.artikl_id));
            subsql = "(" + subsql.substring(1) + ")";
            qJoining.select(eJoining.up, "where", eJoining.artikl_id1, "in", subsql, "and", eJoining.artikl_id2, "in", subsql, "order by", eJoining.name);
        }
    }

    private void initModel() {
        new DefTableModel(tab1, qJoining, eJoining.artikl_id1, eJoining.artikl_id2, eJoining.name) {
            @Override
            public Object actionPreview(Field field, int row, Object val) {
                if (eJoining.artikl_id1 == field) {
                    return qArtikls1.stream().filter(rec -> val.equals(rec.get(eArtikl.id))).findFirst().orElse(eArtikl.up.newRecord(Query.SEL)).get(eArtikl.code);

                } else if (eJoining.artikl_id2 == field) {
                    return qArtikls2.stream().filter(rec -> val.equals(rec.get(eArtikl.id))).findFirst().orElse(eArtikl.up.newRecord(Query.SEL)).get(eArtikl.code);
                }
                return val;
            }
        };
        new DefTableModel(tab2, qJoinvar, eJoinvar.prio, eJoinvar.name);
        new DefTableModel(tab3, qJoinpar1, eJoinpar1.grup, eJoinpar1.text) {
            public Object actionPreview(Field field, int row, Object val) {
                if (val != null && eJoinpar1.grup == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record joinpar1Rec = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord(Query.SEL));
                        return joinpar1Rec.getStr(eJoinpar1.grup) + "-" + joinpar1Rec.getStr(eJoinpar1.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return en.numb() + "-" + en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab4, qJoindet, eJoindet.artikl_id, eJoindet.artikl_id, eJoindet.color_fk, eJoindet.types);
        new DefTableModel(tab5, qJoinpar2, eJoinpar2.grup, eJoinpar2.text);

        JButton btnT1C0 = new JButton("...");
        tab1.getColumnModel().getColumn(0).setCellEditor(new DefFieldEditor(null, btnT1C0));
        btnT1C0.addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
            FrameToFile.setFrameSize(frame);
            frame.setVisible(true);
        });
        JButton btnT3C0 = new JButton("...");
        tab3.getColumnModel().getColumn(0).setCellEditor(new DefFieldEditor(btnT3C0));
        btnT3C0.addActionListener(event -> {
            int row = tab2.getSelectedRow();
            if (row != -1) {
                Record record = qJoinvar.get(row);
                int joinVar = record.getInt(eJoinvar.types);
                DicParam1 frame = new DicParam1(this, listenerPar1, eParams.joint, joinVar * 1000);
                FrameToFile.setFrameSize(frame);
                frame.setVisible(true);

            }
        });
        JButton btnT3C1 = new JButton("...");
        tab3.getColumnModel().getColumn(1).setCellEditor(new DefFieldEditor(btnT3C1));
        btnT3C1.addActionListener(event -> {
            Record record = qJoinpar1.get(tab3.getSelectedRow());
            int grup = record.getInt(eJoinpar1.grup);
            if (grup < 0) {
                DicParam2 frame = new DicParam2(this, listenerPar1, grup);
                FrameToFile.setFrameSize(frame);
                frame.setVisible(true);
            } else {
                List list = ParamList.find(grup).dict();
                DicParam3 frame = new DicParam3(this, listenerPar1, list);
                FrameToFile.setFrameSize(frame);
                frame.setVisible(true);
                System.out.println(list);
            }
        });
        JButton btnT4C2 = new JButton("...");
        tab4.getColumnModel().getColumn(0).setCellEditor(new DefFieldEditor(btnT4C2));
        btnT4C2.addActionListener(event -> {
            int row = tab4.getSelectedRow();
            Record record = qJoindet.get(row);
            int artikl_id = record.getInt(eJoindet.artikl_id);
            List<Record> artdetRec = eArtdet.find(artikl_id);

            DicColor1 frame = new DicColor1(this, listenerColor);
            FrameToFile.setFrameSize(frame);
            frame.setVisible(true);
        });
        JButton btnT5C0 = new JButton("...");
        tab5.getColumnModel().getColumn(0).setCellEditor(new DefFieldEditor(btnT5C0));
        btnT5C0.addActionListener(event -> {
            int row = tab4.getSelectedRow();
            if (row != -1) {
                Record recordJoin = qJoindet.get(row);
                int artikl_id = recordJoin.getInt(eJoindet.artikl_id);
                Record recordArt = eArtikl.find(artikl_id, false);
                int level = recordArt.getInt(eArtikl.level1);

                if (level == 1 || level == 3) {
                    level = 12000;

                } else if (level == 2 || level == 4) {
                    level = 11000;
                }
                DicEnums frame = new DicEnums(this, listenerEnums, level);
                FrameToFile.setFrameSize(frame);
                frame.setVisible(true);
            }
        });
        Util.selectRecord(tab1, 0);
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = tab1.getSelectedRow();
        if (row != -1) {
            Record record = qJoining.table(eJoining.up).get(row);
            Integer id = record.getInt(eJoining.id);
            qJoinvar.select(eJoinvar.up, "where", eJoinvar.joining_id, "=", id, "order by", eJoinvar.prio);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.selectRecord(tab2, 0);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = tab2.getSelectedRow();
        if (row != -1) {
            Record record = qJoinvar.table(eJoinvar.up).get(row);
            Integer id = record.getInt(eJoinvar.id);
            qJoindet.select(eJoindet.up, "where", eJoindet.joinvar_id, "=", id, "order by", eJoindet.artikl_id);
            qJoinpar1.select(eJoinpar1.up, "where", eJoinpar1.joinvar_id, "=", id, "order by", eJoinpar1.grup);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.selectRecord(tab3, 0);
            Util.selectRecord(tab4, 0);
        }
    }

    private void selectionTab4(ListSelectionEvent event) {
        int row = tab4.getSelectedRow();
        if (row != -1) {
            Record record = qJoindet.table(eJoindet.up).get(row);
            Integer id = record.getInt(eJoindet.id);
            qJoinpar2.select(eJoinpar2.up, "where", eJoinpar2.joindet_id, "=", id, "order by", eJoinpar2.grup);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.selectRecord(tab5, 0);
        }
    }

    private void listenerDict() {

        listenerArtikl = (record) -> {
            if (tab1.getBorder() != null) {
                System.out.println("====forms.Joining.listenerArtikl()");
            }
        };

        listenerPar1 = (record) -> {
            Record joinpar1Rec = qJoinpar1.get(tab3.getSelectedRow());

            if (eParams.values().length == record.size()) {
                joinpar1Rec.set(eJoinpar1.grup, record.getInt(eJoinpar1.grup));
                joinpar1Rec.set(eJoinpar1.numb, record.getInt(eJoinpar1.numb));
                joinpar1Rec.set(eJoinpar1.text, null);

            } else if (record.size() == 2) {
                joinpar1Rec.set(eJoinpar1.grup, record.get(0));
                joinpar1Rec.set(eJoinpar1.numb, -1);
                joinpar1Rec.set(eJoinpar1.text, null);

            } else if (record.size() == 1) {
                System.out.println(joinpar1Rec);
                joinpar1Rec.set(eJoinpar1.text, record.getStr(0));
                System.out.println(joinpar1Rec);

            }
            FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            Util.selectRecord(tab3, 0);
        };

        listenerPar2 = (record) -> {
            System.out.println("forms.Joining.listenerPar1()");
        };

        listenerColor = (record) -> {
            System.out.println("forms.Joining.listenerColor()");
        };

        listenerEnums = (record) -> {
            System.out.println("forms.Joining.listenerEnums()");
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport2 = new javax.swing.JButton();
        panCentr = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();

        scr6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        tab6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scr6.setViewportView(tab6);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Соединения");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(800, 29));

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

        btnReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport2.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport2.setFocusable(false);
        btnReport2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport2.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport2.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport2.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport2(evt);
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
                .addGap(110, 110, 110)
                .addComponent(btnReport2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 695, Short.MAX_VALUE)
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
                    .addComponent(btnReport2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCentr.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(400, 548));
        jPanel4.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr1.setPreferredSize(new java.awt.Dimension(454, 540));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzz", "aaaaa", "vvvvvvvvvvvvvvvvv"},
                {"ccccc", "vvvvv", "uuuuuuuuuuuuu"}
            },
            new String [] {
                "Артикул 1", "Артикул 2", "Название"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(2).setMinWidth(100);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        jPanel4.add(scr1, java.awt.BorderLayout.CENTER);

        panCentr.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel1.setPreferredSize(new java.awt.Dimension(654, 568));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel2.setPreferredSize(new java.awt.Dimension(654, 234));
        jPanel2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr2.setPreferredSize(new java.awt.Dimension(454, 234));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Мммммммммм"},
                {"2", "Ррррррррррр"}
            },
            new String [] {
                "Приоритет", "Название"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        jPanel2.add(scr2, java.awt.BorderLayout.CENTER);

        scr3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr3.setPreferredSize(new java.awt.Dimension(300, 234));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"kkkkkkkkkkk", "77"},
                {"hhhhhhhhh", "88"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab3.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        jPanel2.add(scr3, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel2);

        jPanel3.setPreferredSize(new java.awt.Dimension(654, 234));
        jPanel3.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr4.setPreferredSize(new java.awt.Dimension(454, 234));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"yyyyyyyy", "fffffffffffffff", "44", "7"},
                {"rrrrrrrrrrr", "llllllllllllllllllllllllllll", "77", "2"}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab4.setFillsViewportHeight(true);
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr4.setViewportView(tab4);

        jPanel3.add(scr4, java.awt.BorderLayout.CENTER);

        scr5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr5.setPreferredSize(new java.awt.Dimension(300, 234));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"eeeeeeeeee", "22"},
                {"mmmmmmm", "44"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ));
        tab5.setFillsViewportHeight(true);
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        jPanel3.add(scr5, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel3);

        panCentr.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        initData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.selectRecord(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?", "Предупреждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                Record joiningRec = qJoining.get(tab1.getSelectedRow());
                joiningRec.set(eJoining.up, Query.DEL);
                qJoining.delete(joiningRec);
                qJoining.removeRec(tab1.getSelectedRow());
                ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
                Util.selectRecord(tab1, 0);

            } else if (tab2.getBorder() != null) {
                Record joinvarRec = qJoinvar.get(tab2.getSelectedRow());
                joinvarRec.set(eJoinvar.up, Query.DEL);
                qJoinvar.delete(joinvarRec);
                qJoinvar.removeRec(tab2.getSelectedRow());
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.selectRecord(tab2, 0);

            } else if (tab3.getBorder() != null) {
                Record joinpar1Rec = qJoinpar1.get(tab3.getSelectedRow());
                joinpar1Rec.set(eJoinpar1.up, Query.DEL);
                qJoinpar1.delete(joinpar1Rec);
                qJoinpar1.removeRec(tab3.getSelectedRow());
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.selectRecord(tab3, 0);

            } else if (tab4.getBorder() != null) {
                Record joindetRec = qJoindet.get(tab4.getSelectedRow());
                joindetRec.set(eJoindet.up, Query.DEL);
                qJoindet.delete(joindetRec);
                qJoindet.removeRec(tab4.getSelectedRow());
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.selectRecord(tab4, 0);

            } else if (tab5.getBorder() != null) {
                Record joinpar2Rec = qJoinpar2.get(tab5.getSelectedRow());
                joinpar2Rec.set(eJoinpar2.up, Query.DEL);
                qJoinpar2.delete(joinpar2Rec);
                qJoinpar2.removeRec(tab5.getSelectedRow());
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.selectRecord(tab5, 0);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            Record record = qJoining.newRecord(Query.INS);
            record.setNo(eColgrp.id, ConnApp.instanc().genId(eJoining.up));
            qJoining.add(record);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qJoining, tab1);

        } else if (tab2.getBorder() != null) {
            int row = tab1.getSelectedRow();
            Record joiningRec = qJoining.get(row);
            Record joinvarRec = qJoinvar.newRecord(Query.INS);
            joinvarRec.setNo(eJoinvar.id, ConnApp.instanc().genId(eJoinvar.up));
            joinvarRec.setNo(eJoinvar.joining_id, joiningRec.getInt(eJoining.id));
            qJoinvar.add(joinvarRec);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qJoinvar, tab2);

        } else if (tab3.getBorder() != null) {
            int row = tab2.getSelectedRow();
            Record joinvarRec = qJoinvar.get(row);
            Record joinpar1Rec = qJoinpar1.newRecord(Query.INS);
            joinpar1Rec.setNo(eJoinpar1.id, ConnApp.instanc().genId(eJoinpar1.up));
            joinpar1Rec.setNo(eJoinpar1.joinvar_id, joinvarRec.getInt(eJoinpar1.id));
            qJoinpar1.add(joinpar1Rec);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qJoinpar1, tab3);

        } else if (tab4.getBorder() != null) {
            int row = tab2.getSelectedRow();
            Record joinvarRec = qJoinvar.get(row);
            Record joindetRec = qJoindet.newRecord(Query.INS);
            joindetRec.setNo(eJoindet.id, ConnApp.instanc().genId(eJoindet.up));
            joindetRec.setNo(eJoindet.joinvar_id, joinvarRec.getInt(eJoinvar.id));
            qJoindet.add(joindetRec);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qJoindet, tab4);

        } else if (tab5.getBorder() != null) {
            int row = tab2.getSelectedRow();
            Record joindetRec = qJoindet.get(row);
            Record joinpar2Rec = qJoinpar2.newRecord(Query.INS);
            joinpar2Rec.setNo(eJoinpar2.id, ConnApp.instanc().genId(eJoinpar2.up));
            joinpar2Rec.setNo(eJoinpar2.joindet_id, joindetRec.getInt(eJoinpar2.id));
            qJoinpar2.add(joinpar2Rec);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qJoinpar2, tab5);
        }
    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(qJoining, qJoinvar, qJoindet, qJoinpar1, qJoinpar2).forEach(q -> q.execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed

    private void btnReport2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport2
        JOptionPane.showMessageDialog(eProfile.appframe, "xxxxxxxx", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnReport2
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnReport1;
    private javax.swing.JButton btnReport2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab6;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        FocusListener listenerFocus = new FocusListener() {

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
        btnIns.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        btnDel.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        btnRef.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Варианты соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Спецификация соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
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
        tab4.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab4(event);
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
