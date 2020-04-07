package frame;

import dialog.ParGrup;
import dialog.ParSys;
import dialog.ParColor;
import dialog.ParUser;
import dialog.DicArtikl;
import common.DialogListener;
import common.EditorListener;
import common.FrameToFile;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eJoindet;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinpar2;
import domain.eParams;
import domain.eSysprof;
import enums.Enam;
import enums.ParamList;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefFieldEditor;
import swing.DefTableModel;
import dialog.DicJoinvar;
import domain.eJoinvar;
import swing.BooleanRenderer;
import common.Util;
import dialog.DicColvar;
import enums.VarColcalc;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Joining extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.id, eParams.grup, eParams.numb, eParams.text);
    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qArtikl = new Query(eArtikl.id, eArtikl.code, eArtikl.name);
    private Query qJoining = new Query(eJoining.values());
    private Query qJoinvar = new Query(eJoinvar.values());
    private Query qJoindet = new Query(eJoindet.values());
    private Query qJoinpar1 = new Query(eJoinpar1.values());
    private Query qJoinpar2 = new Query(eJoinpar2.values());
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;
    private EditorListener listenerEditor;
    private DialogListener listenerArtikl, listenerPar1, listenerPar2, listenerJoinvar, listenerColor, listenerColvar;

    public Joining() {
        initComponents();
        initElements();
        initData();
        listenerCell();
        listenerDict();
        initModel();
    }

    public Joining(java.awt.Window owner, int nuni) {
        this.owner = owner;
        this.nuni = nuni;
        initComponents();
        initElements();
        initData();
        listenerCell();
        listenerDict();
        initModel();
        owner.setEnabled(false);
    }

    private void initData() {

        tab1.setToolTipText("");
        qParams.select(eParams.up, "where", eParams.joint, "= 1 and", eParams.numb, "= 0 order by", eParams.text);
        qColor.select(eColor.up);
        qArtikl.select(eArtikl.up);
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

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (eJoining.artikl_id1 == field) {
                    return qArtikl.stream().filter(rec -> val.equals(rec.get(eArtikl.id))).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.code);

                } else if (eJoining.artikl_id2 == field) {
                    return qArtikl.stream().filter(rec -> val.equals(rec.get(eArtikl.id))).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.code);
                }
                return val;
            }
        };
        new DefTableModel(tab2, qJoinvar, eJoinvar.prio, eJoinvar.name, eJoinvar.mirr);
        new DefTableModel(tab3, qJoinpar1, eJoinpar1.grup, eJoinpar1.text) {

            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex == 1) {
                    setValueAt(aValue, rowIndex, eJoinpar1.text);
                }
            }

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eJoinpar1.grup == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record joinpar1Rec = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return joinpar1Rec.getStr(eJoinpar1.grup) + "-" + joinpar1Rec.getStr(eJoinpar1.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return en.numb() + "-" + en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab4, qJoindet, eJoindet.artikl_id, eJoindet.artikl_id, eJoindet.color_fk, eJoindet.types) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (eJoindet.artikl_id == field) {
                    int id = Integer.valueOf(val.toString());
                    if (col == 0) {
                        return qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == id).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.code);
                    } else if (col == 1) {
                        return qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == id).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.name);
                    }
                } else if (eJoindet.color_fk == field) {
                    int colorFk = Integer.valueOf(val.toString());

                    if (Integer.valueOf(VarColcalc.automatic[0]) == colorFk) {
                        return VarColcalc.automatic[1];
                    } else if (Integer.valueOf(VarColcalc.precision[0]) == colorFk) {
                        return VarColcalc.precision[1];
                    }
                    if (colorFk > 0) {
                        return qColor.stream().filter(rec -> rec.getInt(eColor.id) == colorFk).findFirst().orElse(eColor.up.newRecord()).get(eColor.name);
                    } else {
                        return qParams.stream().filter(rec -> rec.getInt(eParams.grup) == colorFk).findFirst().orElse(eParams.up.newRecord()).get(eParams.text);
                    }
                } else if (eJoindet.types == field) {

                    int types = Integer.valueOf(val.toString());
                    if (VarColcalc.find(types) != null) {
                        return VarColcalc.find(types).name;
                    } else {
                        return null;
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qJoinpar2, eJoinpar2.grup, eJoinpar2.text) {

            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex == 1) {
                    setValueAt(aValue, rowIndex, eJoinpar2.text);
                }
            }

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eJoinpar2.grup == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record joinpar2Rec = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return joinpar2Rec.getStr(eJoinpar2.grup) + "-" + joinpar2Rec.getStr(eJoinpar2.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return en.numb() + "-" + en.text();
                    }
                }
                return val;
            }
        };

        tab2.getColumnModel().getColumn(2).setCellRenderer(new BooleanRenderer());

        Util.buttonEditorCell(tab1, 0).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab1, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            DicJoinvar frame = new DicJoinvar(this, listenerJoinvar);
        });

        Util.buttonEditorCell(tab3, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab2);
            if (row != -1) {
                Record record = qJoinvar.get(row);
                int joinVar = record.getInt(eJoinvar.types);
                ParGrup frame = new ParGrup(this, listenerPar1, eParams.joint, joinVar * 1000);
            }
        });

        Util.buttonEditorCell(tab3, 1, listenerEditor).addActionListener(event -> {
            Record record = qJoinpar1.get(Util.getSelectedRec(tab3));
            int grup = record.getInt(eJoinpar1.grup);
            if (grup < 0) {
                ParUser frame = new ParUser(this, listenerPar1, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParSys frame = new ParSys(this, listenerPar1, list);
            }
        });

        Util.buttonEditorCell(tab4, 0).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
        });

        Util.buttonEditorCell(tab4, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
        });

        Util.buttonEditorCell(tab4, 2).addActionListener(event -> {
            Record record = qJoindet.get(Util.getSelectedRec(tab4));
            int artikl_id = record.getInt(eJoindet.artikl_id);
            ParColor frame = new ParColor(this, listenerColor, artikl_id);
        });

        Util.buttonEditorCell(tab4, 3).addActionListener(event -> {
            Record record = qJoindet.get(Util.getSelectedRec(tab4));
            int colorFk = record.getInt(eJoindet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar, colorFk);
        });

        Util.buttonEditorCell(tab5, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab4);
            if (row != -1) {
                Record recordJoin = qJoindet.get(row);
                int artikl_id = recordJoin.getInt(eJoindet.artikl_id);
                Record recordArt = eArtikl.find(artikl_id, false);
                int level = recordArt.getInt(eArtikl.level1);
                Integer[] part = {0, 1200, 11000, 12000, 11000, 0};
                ParGrup frame = new ParGrup(this, listenerPar2, eParams.joint, part[level]);
            }
        });

        Util.buttonEditorCell(tab5, 1, listenerEditor).addActionListener(event -> {
            Record record = qJoinpar2.get(Util.getSelectedRec(tab5));
            int grup = record.getInt(eJoinpar2.grup);
            if (grup < 0) {
                ParUser frame = new ParUser(this, listenerPar2, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParSys frame = new ParSys(this, listenerPar2, list);
            }
        });
        Util.setSelectedRow(tab1, 0);
    }

    private void selectionTab1(ListSelectionEvent event) {
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(qJoinvar, qJoindet, qJoinpar1, qJoinpar2).forEach(q -> q.execsql());
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Util.clearTable(tab2, tab3, tab4, tab5);
            Record record = qJoining.table(eJoining.up).get(row);
            Integer id = record.getInt(eJoining.id);
            qJoinvar.select(eJoinvar.up, "where", eJoinvar.joining_id, "=", id, "order by", eJoinvar.prio);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, 0);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(qJoindet, qJoinpar1, qJoinpar2).forEach(q -> q.execsql());
        int row = Util.getSelectedRec(tab2);
        if (row != -1) {
            Util.clearTable(tab3, tab4, tab5);
            Record record = qJoinvar.table(eJoinvar.up).get(row);
            Integer id = record.getInt(eJoinvar.id);
            qJoindet.select(eJoindet.up, "where", eJoindet.joinvar_id, "=", id, "order by", eJoindet.artikl_id);
            qJoinpar1.select(eJoinpar1.up, "where", eJoinpar1.joinvar_id, "=", id, "order by", eJoinpar1.grup);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, 0);
            Util.setSelectedRow(tab4, 0);
        }
    }

    private void selectionTab4(ListSelectionEvent event) {
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(qJoinpar2).forEach(q -> q.execsql());
        int row = Util.getSelectedRec(tab4);
        if (row != -1) {
            Record record = qJoindet.table(eJoindet.up).get(row);
            Integer id = record.getInt(eJoindet.id);
            qJoinpar2.select(eJoinpar2.up, "where", eJoinpar2.joindet_id, "=", id, "order by", eJoinpar2.grup);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab5, 0);
        }
    }

    private void listenerDict() {
        
        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab1.getBorder() != null) {
                Record joiningRec = qJoining.get(Util.getSelectedRec(tab1));
                if (tab1.getSelectedColumn() == 0) {
                    joiningRec.set(eJoining.artikl_id1, record.getInt(eArtikl.id));
                } else if (tab1.getSelectedColumn() == 1) {
                    joiningRec.set(eJoining.artikl_id2, record.getInt(eArtikl.id));
                }

            } else if (tab4.getBorder() != null) {
                int row = tab4.getSelectedRow();
                Record joindetRec = qJoindet.get(Util.getSelectedRec(tab4));
                joindetRec.set(eJoindet.artikl_id, record.getInt(eArtikl.id));
                joindetRec.set(eJoindet.color_fk, null);
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab4, row);
            }
        };

        listenerPar1 = (record) -> {
            Util.listenerParam(record, tab3, qJoinpar1, eJoinpar1.grup, eJoinpar1.numb, eJoinpar1.text, tab1, tab2, tab3, tab4, tab5);
        };

        listenerPar2 = (record) -> {
            Util.listenerParam(record, tab5, qJoinpar2, eJoinpar2.grup, eJoinpar2.numb, eJoinpar2.text, tab1, tab2, tab3, tab4, tab5);
        };

        listenerColor = (record) -> {
            Util.listenerColor(record, tab4, qJoindet, eJoindet.color_fk, eJoindet.types, tab1, tab2, tab3, tab4, tab5);
        };

        listenerColvar = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = tab4.getSelectedRow();
            Record joindetRec = qJoindet.get(Util.getSelectedRec(tab4));
            joindetRec.set(eJoindet.types, record.getInt(0));
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, row);
        };

        listenerJoinvar = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = tab2.getSelectedRow();
            Record joinvarRec = qJoinvar.get(Util.getSelectedRec(tab2));
            joinvarRec.set(eJoinvar.types, record.getInt(0));
            joinvarRec.set(eJoinvar.name, record.getStr(1));
            if (joinvarRec.get(eJoinvar.prio) == null) {
                int max = 0;
                for (Record rec : qJoinvar) {
                    max = (max < rec.getInt(eJoinvar.prio)) ? rec.getInt(eJoinvar.prio) : max;
                }
                joinvarRec.set(eJoinvar.prio, ++max);
            }
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, row);
        };
    }

    private void listenerCell() {
        listenerEditor = (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return Util.listenerCell(component, tab3, tab5, qJoinpar1, qJoinpar2, tab1, tab2, tab3, tab4, tab5);
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
        btnReport = new javax.swing.JButton();
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
        jPanel5 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Соединения");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(1000, 29));

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

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport.setFocusable(false);
        btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
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
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 749, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panNorthLayout.createSequentialGroup()
                        .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCentr.setPreferredSize(new java.awt.Dimension(1000, 668));
        panCentr.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(380, 548));
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
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(2).setMinWidth(100);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(140);
        }

        jPanel4.add(scr1, java.awt.BorderLayout.CENTER);

        panCentr.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(654, 568));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel2.setPreferredSize(new java.awt.Dimension(654, 234));
        jPanel2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr2.setPreferredSize(new java.awt.Dimension(454, 234));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Мммммммммм", null},
                {"2", "Ррррррррррр", null}
            },
            new String [] {
                "Приоритет", "Название", "Зеркальность"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(0).setMaxWidth(60);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        jPanel2.add(scr2, java.awt.BorderLayout.CENTER);

        scr3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr3.setPreferredSize(new java.awt.Dimension(300, 234));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "111"},
                {"22", "222"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab3.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        jPanel2.add(scr3, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel2);

        panCentr.add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel5.setPreferredSize(new java.awt.Dimension(800, 200));
        jPanel5.setLayout(new java.awt.BorderLayout());

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
        tab4.setName("tab4"); // NOI18N
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(3).setPreferredWidth(140);
            tab4.getColumnModel().getColumn(3).setMaxWidth(300);
        }

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
        tab5.setName("tab5"); // NOI18N
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        jPanel3.add(scr5, java.awt.BorderLayout.EAST);

        jPanel5.add(jPanel3, java.awt.BorderLayout.CENTER);

        panCentr.add(jPanel5, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(1000, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1016, Short.MAX_VALUE)
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
        Arrays.asList(qJoining, qJoinvar, qJoindet, qJoinpar1, qJoinpar2).forEach(q -> q.execsql());
        initData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab2) == 0) {
                Util.deleteRecord(tab1, qJoining, eJoining.up);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab3, tab4) == 0) {
                Util.deleteRecord(tab2, qJoinvar, eJoinvar.up);
            }
        } else if (tab3.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab3, qJoinpar1, eJoinpar1.up);
            }
        } else if (tab4.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab5) == 0) {
                Util.deleteRecord(tab4, qJoindet, eJoindet.up);
            }
        } else if (tab5.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab5, qJoinpar2, eJoinpar2.up);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, qJoining, eJoining.up);

        } else if (tab2.getBorder() != null) {
            Util.insertRecord(tab1, tab2, qJoining, qJoinvar, eJoining.up, eJoinvar.up, eJoinvar.joining_id);

        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab2, tab3, qJoinvar, qJoinpar1, eJoinvar.up, eJoinpar1.up, eJoinpar1.joinvar_id);

        } else if (tab4.getBorder() != null) {
            Util.insertRecord(tab2, tab4, qJoinvar, qJoindet, eJoinvar.up, eJoindet.up, eJoindet.joinvar_id);

        } else if (tab5.getBorder() != null) {
            Util.insertRecord(tab4, tab5, qJoindet, qJoinpar2, eJoindet.up, eJoinpar2.up, eJoinpar2.joindet_id);
        }
    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(qJoining, qJoinvar, qJoindet, qJoinpar1, qJoinpar2).forEach(q -> q.execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void tabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMouseClicked
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(tab1, tab2, tab3, tab4, tab5).forEach(tab -> tab.setBorder(null));
        ((JTable) evt.getSource()).setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
    }//GEN-LAST:event_tabMouseClicked
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
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
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Варианты соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
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
    }
}
