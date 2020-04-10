package frame;

import common.DialogListener;
import common.FrameListener;
import common.FrameToFile;
import common.Util;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnpar1;
import domain.eFurnpar2;
import domain.eFurnside1;
import domain.eColor;
import domain.eFurnside2;
import domain.eSysfurn;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefTableModel;
import static common.Util.getSelectedRec;
import dataset.Field;
import dialog.DicArtikl;
import dialog.DicColvar;
import dialog.DicFurnside;
import dialog.ParColor;
import domain.eParams;
import enums.FurnSide;
import enums.VarColcalc;

public class Furniture extends javax.swing.JFrame {

    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qParams = new Query(eParams.id, eParams.grup, eParams.numb, eParams.text);
    private Query qFurniture = new Query(eFurniture.values());
    private Query qFurndet1 = new Query(eFurndet.values(), eArtikl.values()); //TODO список полей eArtikl надо уменьшить
    private Query qFurndet2 = new Query(eFurndet.values(), eArtikl.values());
    private Query qFurndet3 = new Query(eFurndet.values(), eArtikl.values());
    private Query qFurnside1 = new Query(eFurnside1.values());
    private Query qFurnside2 = new Query(eFurnside2.values());
    private Query qFurnpar1 = new Query(eFurnpar1.values());
    private Query qFurnpar2 = new Query(eFurnpar2.values());
    private DialogListener listenerArtikl, listenerPar1, listenerPar2, listenerTypset, listenerColor, listenerColvar, listenerSide;
    private FrameListener listenerFrame = null;
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;

    public Furniture() {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        listenerDict();
    }

    public Furniture(java.awt.Window owner, int nuni) {
        initComponents();
        initElements();
        this.owner = owner;
        this.nuni = nuni;
        listenerFrame = (FrameListener) owner;
        owner.setEnabled(false);
        loadingData();
        loadingModel();
        listenerDict();
    }

    private void loadingData() {
        qColor.select(eColor.up);
        qParams.select(eParams.up, "where", eParams.joint, "= 1 and", eParams.numb, "= 0 order by", eParams.text);
        if (owner == null) {
            qFurniture.select(eFurniture.up, "order by", eFurniture.name);
        } else {
            Query query = new Query(eSysfurn.furniture_id).select(eSysfurn.up, "where", eSysfurn.systree_id, "=", nuni).table(eSysfurn.up);
            query.stream().forEach(rec -> subsql = subsql + "," + rec.getStr(eSysfurn.furniture_id));
            subsql = "(" + subsql.substring(1) + ")";
            qFurniture.select(eFurniture.up, "where", eFurniture.id, "in", subsql, "order by", eFurniture.name);
        }
    }

    private void loadingModel() {
        new DefTableModel(tab1, qFurniture, eFurniture.name, eFurniture.view_open, eFurniture.view_open, eFurniture.p2_max, eFurniture.width_max,
                eFurniture.height_max, eFurniture.weight_max, eFurniture.types, eFurniture.pars, eFurniture.coord_lim);
        new DefTableModel(tab2a, qFurndet1, eArtikl.code, eArtikl.name, eFurndet.color_fk, eFurndet.types) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (eFurndet.color_fk == field) {
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
                } else if (eFurndet.types == field) {
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
        new DefTableModel(tab2b, qFurndet2, eArtikl.code, eArtikl.name, eFurndet.color_fk, eFurndet.types) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (eFurndet.color_fk == field) {
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
                } else if (eFurndet.types == field) {
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
        new DefTableModel(tab2c, qFurndet3, eArtikl.code, eArtikl.name, eFurndet.color_fk, eFurndet.types) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (eFurndet.color_fk == field) {
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
                } else if (eFurndet.types == field) {
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
        new DefTableModel(tab3, qFurnpar2, eFurnpar2.grup, eFurnpar2.text);
        new DefTableModel(tab4, qFurnside1, eFurnside1.npp, eFurnside1.furniture_id, eFurnside1.type_side){

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eFurnside1.type_side == field) {
                    int v = Integer.valueOf(val.toString());
                    return FurnSide.values()[v - 1].name;
                }
                return val;
            }
        };
        new DefTableModel(tab5, qFurnpar1, eFurnpar1.grup, eFurnpar1.text);
        new DefTableModel(tab6, qFurnside2, eFurnside2.side_num, eFurnside2.len_min, eFurnside2.len_max, eFurnside2.ang_min, eFurnside2.ang_max);

        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            Util.buttonEditorCell(tab, 0).addActionListener(event -> {
                new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
            });
        }
        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            Util.buttonEditorCell(tab, 1).addActionListener(event -> {
                new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
            });
        }

        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            Query query = (tab == tab2a) ? qFurndet1 : (tab == tab2b) ? qFurndet2 : qFurndet3;
            Util.buttonEditorCell(tab, 2).addActionListener(event -> {
                Record record = query.get(Util.getSelectedRec(tab));
                int artikl_id = record.getInt(eFurndet.artikl_id);
                ParColor frame = new ParColor(this, listenerColor, artikl_id);
            });
        }
        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            Query query = (tab == tab2a) ? qFurndet1 : (tab == tab2b) ? qFurndet2 : qFurndet3;
            Util.buttonEditorCell(tab, 3).addActionListener(event -> {
                Record record = query.get(Util.getSelectedRec(tab));
                int colorFk = record.getInt(eFurndet.color_fk);
                DicColvar frame = new DicColvar(this, listenerColvar, colorFk);
            });
        }

        //tab4.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboxCell));
        Util.buttonEditorCell(tab4, 2).addActionListener(event -> {
            new DicFurnside(this, listenerSide);
        });

        Util.setSelectedRow(tab1, 0);
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = getSelectedRec(tab1);
        if (row != -1) {
            Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            Util.clearTable(tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            Record record = qFurniture.table(eFurniture.up).get(row);
            Integer id = record.getInt(eFurniture.id);
            qFurnside1.select(eFurnside1.up, "where", eFurnside1.furniture_id, "=", id, "order by", eFurnside1.npp);
            qFurndet1.select(eFurndet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eFurndet.artikl_id, "where", eFurndet.furniture_id, "=", id, "order by", eArtikl.code);
            ((DefaultTableModel) tab2a.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2a, 0);
            Util.setSelectedRow(tab4, 0);
        }
    }

    private void selectionTab2a(ListSelectionEvent event) {
        int row = getSelectedRec(tab2a);
        if (row != -1) {
            Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            Util.clearTable(tab2b, tab2c, tab3, tab6);
            Record record = qFurndet1.table(eFurndet.up).get(row);
            Integer id = record.getInt(eFurndet.id);
            qFurndet2.select(eFurndet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eFurndet.artikl_id, "where", eFurndet.furndet_id, "=", id, "order by", eArtikl.code);
            ((DefaultTableModel) tab2b.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2b, 0);

            if (tabb1.getSelectedIndex() == 0) {
                qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.grup);
                qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab3, 0);
                Util.setSelectedRow(tab6, 0);
            }
        }
    }

    private void selectionTab2b(ListSelectionEvent event) {
        int row = getSelectedRec(tab2b);
        if (row != -1) {
            Util.stopCellEditing(tab2c, tab3, tab6);
            Record record = qFurndet2.table(eFurndet.up).get(row);
            Integer id = record.getInt(eFurndet.id);
            qFurndet3.select(eFurndet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eFurndet.artikl_id, "where", eFurndet.furndet_id, "=", id, "order by", eArtikl.code);
            ((DefaultTableModel) tab2c.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2c, 0);

            if (tabb1.getSelectedIndex() == 1) {
                qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.grup);
                qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab3, 0);
                Util.setSelectedRow(tab6, 0);
            }
        }
    }

    private void selectionTab2c(ListSelectionEvent event) {

        if (tabb1.getSelectedIndex() == 2) {
            int row = getSelectedRec(tab2c);
            if (row != -1) {
                Util.stopCellEditing(tab3, tab6);
                Record record = qFurndet3.table(eFurndet.up).get(row);
                Integer id = record.getInt(eFurndet.id);
                qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.grup);
                qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab3, 0);
                Util.setSelectedRow(tab6, 0);
            }
        }
    }

    private void selectionTab4(ListSelectionEvent event) {
        int row = getSelectedRec(tab4);
        if (row != -1) {
            Record record = qFurnside1.table(eFurnside1.up).get(row);
            Integer id = record.getInt(eFurnside1.id);
            qFurnpar1.select(eFurnpar1.up, "where", eFurnpar1.furnside_id, "=", id, "order by", eFurnpar1.grup);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab5, 0);
        }
    }

    private void listenerDict() {

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5);
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Query query = (tab2a.getBorder() != null) ? qFurndet1 : (tab2b.getBorder() != null) ? qFurndet2 : qFurndet3;
            if (tab.getBorder() != null) {
                int row = tab.getSelectedRow();
                query.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab), eFurndet.artikl_id);
                query.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab), eArtikl.name);
                query.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab), eArtikl.code);
                ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab, row);
            }
        };

        listenerColor = (record) -> {
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Query query = (tab2a.getBorder() != null) ? qFurndet1 : (tab2b.getBorder() != null) ? qFurndet2 : qFurndet3;
            Util.listenerColor(record, tab, query, eFurndet.color_fk, eFurndet.types, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5);
        };
        
        listenerColvar = (record) -> {
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Query query = (tab2a.getBorder() != null) ? qFurndet1 : (tab2b.getBorder() != null) ? qFurndet2 : qFurndet3;
            Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5);
            int row = tab.getSelectedRow();
            Record furndetRec = query.get(Util.getSelectedRec(tab));
            furndetRec.set(eFurndet.types, record.getInt(0));
            ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab, row);
        };
        
        listenerSide = (record) -> {
            Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            int row = tab4.getSelectedRow();
            qFurnside1.set(record.getInt(0), Util.getSelectedRec(tab4), eFurnside1.type_side);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, row);
        };        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grour1 = new javax.swing.ButtonGroup();
        comboxCell = new javax.swing.JComboBox<>();
        panNorth = new javax.swing.JPanel();
        btnIns = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        rdb1 = new javax.swing.JRadioButton();
        rdb2 = new javax.swing.JRadioButton();
        rdb3 = new javax.swing.JRadioButton();
        panCentr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan8 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        pan6 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        tabb1 = new javax.swing.JTabbedPane();
        scr2a = new javax.swing.JScrollPane();
        tab2a = new javax.swing.JTable();
        scr2b = new javax.swing.JScrollPane();
        tab2b = new javax.swing.JTable();
        scr2c = new javax.swing.JScrollPane();
        tab2c = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();

        comboxCell.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "сторона", "ось поворота", "крепление петель" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Фурнитура");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(1000, 29));

        btnIns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c033.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/prop/hint"); // NOI18N
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

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
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

        grour1.add(rdb1);
        rdb1.setSelected(true);
        rdb1.setText("Основная");
        rdb1.setMaximumSize(new java.awt.Dimension(75, 18));
        rdb1.setMinimumSize(new java.awt.Dimension(75, 18));
        rdb1.setPreferredSize(new java.awt.Dimension(100, 18));

        grour1.add(rdb2);
        rdb2.setText("Дополнительная");
        rdb2.setMaximumSize(new java.awt.Dimension(75, 18));
        rdb2.setMinimumSize(new java.awt.Dimension(75, 18));
        rdb2.setPreferredSize(new java.awt.Dimension(100, 18));

        grour1.add(rdb3);
        rdb3.setText("Комплекты");
        rdb3.setMaximumSize(new java.awt.Dimension(75, 18));
        rdb3.setMinimumSize(new java.awt.Dimension(75, 18));
        rdb3.setPreferredSize(new java.awt.Dimension(100, 18));

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
                .addGap(109, 109, 109)
                .addComponent(rdb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(rdb2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(rdb3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panNorthLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdb2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdb3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCentr.setPreferredSize(new java.awt.Dimension(1000, 562));
        panCentr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(800, 260));
        pan1.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(500, 200));
        pan4.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(450, 200));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"333333333333333", "3", "3", "3", null, null, null, null, null, null},
                {"444444444444444", "3", "4", "5", null, null, null, null, null, null}
            },
            new String [] {
                "Название", "Вид", "Сторона ручки", "Р/2 максимальная", "Ширина максимальная", "Высота максимальная", "Вес максимальный", "Створка", "Использ. параметры", "Ограничения"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(200);
        }

        pan4.add(scr1, java.awt.BorderLayout.CENTER);

        pan1.add(pan4, java.awt.BorderLayout.CENTER);

        pan5.setPreferredSize(new java.awt.Dimension(300, 200));
        pan5.setLayout(new java.awt.BorderLayout());

        pan7.setPreferredSize(new java.awt.Dimension(300, 108));
        pan7.setLayout(new java.awt.BorderLayout());

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Номер", "Вид", "Назначение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab4.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan7.add(scr4, java.awt.BorderLayout.CENTER);

        pan5.add(pan7, java.awt.BorderLayout.NORTH);

        pan8.setPreferredSize(new java.awt.Dimension(300, 90));
        pan8.setLayout(new java.awt.BorderLayout());

        scr5.setBorder(null);
        scr5.setPreferredSize(new java.awt.Dimension(300, 100));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111111111111111", "11"},
                {"222222222222222", "22"}
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
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab5.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan8.add(scr5, java.awt.BorderLayout.CENTER);

        pan5.add(pan8, java.awt.BorderLayout.CENTER);

        pan1.add(pan5, java.awt.BorderLayout.EAST);

        panCentr.add(pan1, java.awt.BorderLayout.NORTH);

        pan2.setPreferredSize(new java.awt.Dimension(800, 302));
        pan2.setLayout(new java.awt.BorderLayout());

        pan6.setPreferredSize(new java.awt.Dimension(800, 300));
        pan6.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(300, 300));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11111111111", "11"},
                {"22222222222", "22"}
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
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan6.add(scr3, java.awt.BorderLayout.EAST);

        jPanel1.setLayout(new java.awt.BorderLayout());

        scr6.setPreferredSize(new java.awt.Dimension(454, 108));

        tab6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "0", "2600", "0", "360"},
                {"2", "0", "2600", "0", "360"}
            },
            new String [] {
                "Сторона", "Мин. длина", "Макс. длина", "Мин. угол", "Макс. угол"
            }
        ));
        tab6.setFillsViewportHeight(true);
        tab6.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr6.setViewportView(tab6);

        jPanel1.add(scr6, java.awt.BorderLayout.SOUTH);

        scr2a.setBorder(null);
        scr2a.setPreferredSize(new java.awt.Dimension(500, 200));

        tab2a.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "xxxxxxxxx", "11", "11"},
                {"22", "vvvvvvvvv", "22", "22"}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab2a.setFillsViewportHeight(true);
        tab2a.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2a.setViewportView(tab2a);

        tabb1.addTab("Детализация   (1 уровень)", scr2a);

        scr2b.setBorder(null);
        scr2b.setPreferredSize(new java.awt.Dimension(500, 200));

        tab2b.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "xxxxxxxxx", "11", "11"},
                {"22", "vvvvvvvvv", "22", "22"}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab2b.setFillsViewportHeight(true);
        tab2b.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2b.setViewportView(tab2b);

        tabb1.addTab("Детализация   (2 уровень)", scr2b);

        scr2c.setBorder(null);
        scr2c.setPreferredSize(new java.awt.Dimension(500, 200));

        tab2c.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "xxxxxxxxx", "11", "11"},
                {"22", "vvvvvvvvv", "22", "22"}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab2c.setFillsViewportHeight(true);
        tab2c.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2c.setViewportView(tab2c);

        tabb1.addTab("Детализация   (3 уровень)", scr2c);

        jPanel1.add(tabb1, java.awt.BorderLayout.CENTER);

        pan6.add(jPanel1, java.awt.BorderLayout.CENTER);

        pan2.add(pan6, java.awt.BorderLayout.CENTER);

        panCentr.add(pan2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(1000, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 927, Short.MAX_VALUE)
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
//            if (Util.isDeleteRecord(this, tab2a, tab2b, tab2c, tab4) == 0) {
//                Util.deleteRecord(tab1, qFurniture, eFurniture.up);
//            }
            Record furnitureRec = qFurniture.get(getSelectedRec(tab1));
            furnitureRec.set(eFurniture.up, Query.DEL);
            qFurniture.delete(furnitureRec);
            qFurniture.removeRec(getSelectedRec(tab1));
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab1, 0);

        } else if (tab2a.getBorder() != null) {
            Record furndetRec = qFurndet1.get(getSelectedRec(tab2a));
            furndetRec.set(eFurndet.up, Query.DEL);
            qFurndet1.delete(furndetRec);
            qFurndet1.removeRec(getSelectedRec(tab2a));
            ((DefaultTableModel) tab2a.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2a, 0);

        } else if (tab3.getBorder() != null) {
            Record urnpar2Rec = qFurnpar2.get(getSelectedRec(tab3));
            urnpar2Rec.set(eFurnpar2.up, Query.DEL);
            qFurnpar2.delete(urnpar2Rec);
            qFurnpar2.removeRec(getSelectedRec(tab3));
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, 0);

        } else if (tab4.getBorder() != null) {
            Record furnside1Rec = qFurnside1.get(getSelectedRec(tab3));
            furnside1Rec.set(eFurnside1.up, Query.DEL);
            qFurnside1.delete(furnside1Rec);
            qFurnside1.removeRec(getSelectedRec(tab4));
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, 0);

        } else if (tab5.getBorder() != null) {
            Record furnpar1Rec = qFurnpar1.get(getSelectedRec(tab5));
            furnpar1Rec.set(eFurnpar1.up, Query.DEL);
            qFurnpar1.delete(furnpar1Rec);
            qFurnpar1.removeRec(getSelectedRec(tab5));
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab5, 0);

        } else if (tab6.getBorder() != null) {
            Record furnside2Rec = qFurnside2.get(getSelectedRec(tab6));
            furnside2Rec.set(eFurnside2.up, Query.DEL);
            qFurnside2.delete(furnside2Rec);
            qFurnside2.removeRec(getSelectedRec(tab6));
            ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab6, 0);
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, qFurniture, eFurniture.up);

        } else if (tab2a.getBorder() != null) {
            if (tabb1.getSelectedIndex() == 0) {
                Util.insertRecord(tab1, tab2a, qFurniture, qFurndet1, eFurniture.up, eFurndet.up, eArtikl.up, eFurndet.furniture_id);
            } else if (tabb1.getSelectedIndex() == 1) {
                Util.insertRecord(tab1, tab2b, qFurniture, qFurndet2, eFurniture.up, eFurndet.up, eArtikl.up, eFurndet.furniture_id);
            } else if (tabb1.getSelectedIndex() == 2) {
                Util.insertRecord(tab1, tab2c, qFurniture, qFurndet3, eFurniture.up, eFurndet.up, eArtikl.up, eFurndet.furniture_id);
            }
        } else if (tab3.getBorder() != null) {
            if (tabb1.getSelectedIndex() == 0) {
                Util.insertRecord(tab2a, tab3, qFurndet1, qFurnpar2, eFurndet.up, eFurnpar2.up, eFurnpar2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 0) {
                Util.insertRecord(tab2b, tab3, qFurndet2, qFurnpar2, eFurndet.up, eFurnpar2.up, eFurnpar2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 0) {
                Util.insertRecord(tab2c, tab3, qFurndet3, qFurnpar2, eFurndet.up, eFurnpar2.up, eFurnpar2.furndet_id);
            }
        } else if (tab4.getBorder() != null) {
            Util.insertRecord(tab1, tab4, qFurniture, qFurnside1, eFurniture.up, eFurnside1.up, eFurnside1.furniture_id);

        } else if (tab5.getBorder() != null) {
            Util.insertRecord(tab1, tab5, qFurniture, qFurnpar1, eFurniture.up, eFurnpar1.up, eFurnpar1.furnside_id);

        } else if (tab6.getBorder() != null) {
            if (tabb1.getSelectedIndex() == 0) {
                Util.insertRecord(tab2a, tab6, qFurndet1, qFurnside2, eFurndet.up, eFurnside2.up, eFurnside2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 1) {
                Util.insertRecord(tab2b, tab6, qFurndet2, qFurnside2, eFurndet.up, eFurnside2.up, eFurnside2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 2) {
                Util.insertRecord(tab2c, tab6, qFurndet3, qFurnside2, eFurndet.up, eFurnside2.up, eFurnside2.furndet_id);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Util.stopCellEditing(tab1, tab2a, tab3, tab4, tab5, tab6);
        Arrays.asList(qFurniture, qFurndet1, qFurnside1, qFurnpar1, qFurnside2, qFurnpar2).forEach(q -> q.execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed
// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JComboBox<String> comboxCell;
    private javax.swing.ButtonGroup grour1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JRadioButton rdb1;
    private javax.swing.JRadioButton rdb2;
    private javax.swing.JRadioButton rdb3;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2a;
    private javax.swing.JScrollPane scr2b;
    private javax.swing.JScrollPane scr2c;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2a;
    private javax.swing.JTable tab2b;
    private javax.swing.JTable tab2c;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab6;
    private javax.swing.JTabbedPane tabb1;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        FocusListener listenerFocus = new FocusListener() {

            javax.swing.border.Border border
                    = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

            public void focusGained(FocusEvent e) {
                Util.stopCellEditing(tab1, tab2a, tab3, tab4, tab5, tab6);
                tab1.setBorder(null);
                tab2a.setBorder(null);
                tab3.setBorder(null);
                tab4.setBorder(null);
                tab5.setBorder(null);
                tab6.setBorder(null);
                if (e.getSource() instanceof JTable) {
                    ((JComponent) e.getSource()).setBorder(border);
                }
            }

            public void focusLost(FocusEvent e) {
            }
        };
        btnIns.addActionListener(l -> Util.stopCellEditing(tab1, tab2a, tab3, tab4, tab5, tab6));
        btnDel.addActionListener(l -> Util.stopCellEditing(tab1, tab2a, tab3, tab4, tab5, tab6));
        btnRef.addActionListener(l -> Util.stopCellEditing(tab1, tab2a, tab3, tab4, tab5, tab6));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Спмсок фурнитуры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2a.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация фурнитуры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Описание сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Ограничения сторон сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab2a.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab2a(event);
                }
            }
        });
        tab2b.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab2b(event);
                }
            }
        });
        tab2c.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab2c(event);
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
        tab2a.addFocusListener(listenerFocus);
        tab2b.addFocusListener(listenerFocus);
        tab2c.addFocusListener(listenerFocus);
        tab3.addFocusListener(listenerFocus);
        tab4.addFocusListener(listenerFocus);
        tab5.addFocusListener(listenerFocus);
        tab6.addFocusListener(listenerFocus);
    }
}
