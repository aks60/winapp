package frames;

import common.DialogListener;
import common.EditorListener;
import common.FrameToFile;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlaspar2;
import domain.eGlasprof;
import domain.eSysprof;
import java.awt.Window;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import dataset.Field;
import frames.dialog.DicArtikl;
import frames.dialog.DicColvar;
import frames.dialog.DicThicknes;
import frames.dialog.ParColor2;
import frames.dialog.ParGrup2;
import frames.dialog.ParGrup2b;
import frames.dialog.ParGrup2a;
import domain.eColor;
import domain.eElemdet;
import domain.eElement;
import domain.eElempar1;
import domain.eParams;
import enums.Enam;
import enums.ParamList;
import enums.UseColcalc;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.RowFilter;
import startup.Main;
import frames.swing.BooleanRenderer;
import java.util.Set;
import java.util.stream.Collectors;

public class Filling extends javax.swing.JFrame {

    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qParams = new Query(eParams.id, eParams.grup, eParams.numb, eParams.text);
    private Query qGlasgrp = new Query(eGlasgrp.values());
    private Query qGlasdet = new Query(eGlasdet.values(), eArtikl.values());
    private Query qGlasprof = new Query(eGlasprof.values(), eArtikl.values());
    private Query qGlaspar1 = new Query(eGlaspar1.values(), eParams.values());
    private Query qGlaspar2 = new Query(eGlaspar2.values(), eParams.values());
    private DialogListener listenerArtikl, listenerPar1, listenerPar2, listenerColor, listenerColvar1, listenerColvar2, listenerColvar3, listenerTypset, listenerThicknes;
    private EditorListener listenerEditor;
    private String subsql = "";
    private Window owner = null;

    public Filling() {
        initComponents();
        initElements();
        listenerCell();
        listenerDict();
        loadingData();
        loadingModel();
    }

    public Filling(java.awt.Window owner, Set<Object> keys) {
        this.owner = owner;
        this.subsql = keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        initComponents();
        initElements();        
        listenerCell();
        listenerDict();
        loadingData();
        loadingModel();
        //owner.setEnabled(false);
    }

    private void loadingData() {
        qColor.select(eColor.up);
        qParams.select(eParams.up, "where", eParams.glas, "= 1 and", eParams.numb, "= 0 order by", eParams.text);
        if (owner == null) {
            qGlasgrp.select(eGlasgrp.up, "order by", eGlasgrp.name);
        } else {
            qGlasgrp.select(eGlasgrp.up, "where", eGlasgrp.id, " in ", subsql);
        }
    }

    private void loadingModel() {
        new DefTableModel(tab1, qGlasgrp, eGlasgrp.name, eGlasgrp.gap, eGlasgrp.depth);
        new DefTableModel(tab2, qGlasdet, eGlasdet.depth, eArtikl.code, eArtikl.name, eGlasdet.color_fk, eGlasdet.types, eGlasdet.types, eGlasdet.types) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (eGlasdet.color_fk == field) {
                    int colorFk = Integer.valueOf(val.toString());
                    if (Integer.valueOf(UseColcalc.automatic[0]) == colorFk) {
                        return UseColcalc.automatic[1];

                    } else if (Integer.valueOf(UseColcalc.precision[0]) == colorFk) {
                        return UseColcalc.precision[1];
                    }
                    if (colorFk > 0) {
                        return qColor.stream().filter(rec -> rec.getInt(eColor.id) == colorFk).findFirst().orElse(eColor.up.newRecord()).get(eColor.name);
                    } else {
                        return qParams.stream().filter(rec -> rec.getInt(eParams.grup) == colorFk).findFirst().orElse(eParams.up.newRecord()).get(eParams.text);
                    }
                } else if (eGlasdet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    types = (col == 3) ? types & 0x0000000f : (col == 4) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8;
                    return UseColcalc.P00.find(types).text();
                }
                return val;
            }
        };
        new DefTableModel(tab3, qGlaspar1, eGlaspar1.grup, eGlaspar1.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eGlaspar1.grup == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? record.getStr(eGlaspar1.grup) + "-" + record.getStr(eGlaspar1.text) : record.getStr(eGlaspar1.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab4, qGlaspar2, eGlaspar2.grup, eGlaspar2.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && field == eGlaspar2.grup) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? record.getStr(eGlaspar2.grup) + "-" + record.getStr(eGlaspar2.text) : record.getStr(eGlaspar2.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qGlasprof, eArtikl.code, eArtikl.name, eGlasprof.sizeax, eGlasprof.toin, eGlasprof.toout);

        BooleanRenderer br = new BooleanRenderer();
        Arrays.asList(3, 4).forEach(index -> tab5.getColumnModel().getColumn(index).setCellRenderer(br));

        Util.buttonEditorCell(tab2, 0).addActionListener(event -> {
            DicThicknes frame = new DicThicknes(this, listenerThicknes);
        });

        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
        });

        Util.buttonEditorCell(tab2, 2).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
        });

        Util.buttonEditorCell(tab2, 3).addActionListener(event -> {
            Record record = qGlasdet.get(Util.getSelectedRec(tab3));
            int artikl_id = record.getInt(eElemdet.artikl_id);
            ParColor2 frame = new ParColor2(this, listenerColor, artikl_id);
        });

        Util.buttonEditorCell(tab2, 4).addActionListener(event -> {
            Record record = qGlasdet.get(Util.getSelectedRec(tab3));
            int colorFk = record.getInt(eGlasdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar1, colorFk);
        });

        Util.buttonEditorCell(tab2, 5).addActionListener(event -> {
            Record record = qGlasdet.get(Util.getSelectedRec(tab3));
            int colorFk = record.getInt(eGlasdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar2, colorFk);
        });

        Util.buttonEditorCell(tab2, 6).addActionListener(event -> {
            Record record = qGlasdet.get(Util.getSelectedRec(tab3));
            int colorFk = record.getInt(eGlasdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar3, colorFk);
        });

        Util.buttonEditorCell(tab3, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab1);
            if (row != -1) {
                ParGrup2 frame = new ParGrup2(this, listenerPar1, eParams.elem, 13000);
            }
        });

        Util.buttonEditorCell(tab3, 1, listenerEditor).addActionListener(event -> {
            Record record = qGlaspar1.get(Util.getSelectedRec(tab3));
            int grup = record.getInt(eGlaspar1.grup);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar1, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar1, list);
            }
        });

        Util.buttonEditorCell(tab4, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab2);
            if (row != -1) {
                Record record = qGlasdet.table(eArtikl.up).get(row);
                int paramPart = record.getInt(eArtikl.level1);
                paramPart = (paramPart == 1 || paramPart == 4) ? 15000 : 14000;
                ParGrup2 frame = new ParGrup2(this, listenerPar2, eParams.elem, paramPart);
            }
        });

        Util.buttonEditorCell(tab4, 1, listenerEditor).addActionListener(event -> {
            Record record = qGlaspar2.get(Util.getSelectedRec(tab4));
            int grup = record.getInt(eGlaspar1.grup);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar2, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar2, list);
            }
        });

        Util.buttonEditorCell(tab5, 0).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab5, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.setSelectedRow(tab1);
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            Arrays.asList(qGlasdet, qGlaspar1, qGlaspar2, qGlasprof).forEach(q -> q.execsql());
            Util.clearTable(tab2, tab3, tab4, tab5);
            Record record = qGlasgrp.table(eGlasgrp.up).get(row);
            Integer id = record.getInt(eGlasgrp.id);
            qGlasdet.select(eGlasdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eGlasdet.artikl_id, "where", eGlasdet.glasgrp_id, "=", id);
            qGlasprof.select(eGlasprof.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eGlasprof.artikl_id, "where", eGlasprof.glasgrp_id, "=", id);
            qGlaspar1.select(eGlaspar1.up, "left join", eParams.up, "on", eParams.grup, "=",
                    eGlaspar1.grup, "and", eParams.numb, "= 0", "where", eGlaspar1.glasgrp_id, "=", id);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
            Util.setSelectedRow(tab3);
            Util.setSelectedRow(tab5);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab2);
        if (row != -1) {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            Arrays.asList(qGlaspar2, qGlasprof).forEach(q -> q.execsql());
            Util.clearTable(tab4);
            Record record = qGlasdet.table(eGlasdet.up).get(row);
            Integer id = record.getInt(eGlasdet.id);
            qGlaspar2.select(eGlaspar2.up, "left join", eParams.up, "on", eParams.grup, "=", eGlaspar2.grup,
                    "and", eParams.numb, "= 0", "where", eGlaspar2.glasdet_id, "=", id);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4);
        }
    }

    private void listenerDict() {

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                qGlasdet.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab2), eGlasdet.artikl_id);
                qGlasdet.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab2), eArtikl.code);
                qGlasdet.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab2), eArtikl.name);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, row);

            } else if (tab5.getBorder() != null) {
                int row = Util.getSelectedRec(tab5);
                qGlasprof.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab5), eGlasprof.artikl_id);
                qGlasprof.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab5), eArtikl.code);
                qGlasprof.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab5), eArtikl.name);
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab5, row);
            }
        };

        listenerColor = (record) -> {
            Util.listenerColor(record, tab2, eGlasdet.color_fk, eGlasdet.types, tab1, tab2, tab3, tab4, tab5);
        };

        listenerColvar1 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab2);
            Record glasdetRec = qGlasdet.get(Util.getSelectedRec(tab2));
            int types = (glasdetRec.getInt(eElemdet.types) & 0xfffffff0) + record.getInt(0);
            glasdetRec.set(eGlasdet.types, types);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, row);
        };
        
        listenerColvar2 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab2);
            Record glasdetRec = qGlasdet.get(Util.getSelectedRec(tab2));
            int types = (glasdetRec.getInt(eElemdet.types) & 0xffffff0f) + (record.getInt(0) << 4);
            glasdetRec.set(eGlasdet.types, types);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, row);
        };
        
        listenerColvar3 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab2);
            Record glasdetRec = qGlasdet.get(Util.getSelectedRec(tab2));
            int types = (glasdetRec.getInt(eElemdet.types) & 0xfffff0ff) + (record.getInt(0) << 8);
            glasdetRec.set(eGlasdet.types, types);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, row);
        };

        listenerTypset = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                qGlasdet.set(record.getInt(0), Util.getSelectedRec(tab2), eElement.typset);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, row);
            }
        };

        listenerThicknes = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                String series = record.getStr(0);
                qGlasdet.set(series, Util.getSelectedRec(tab2), eGlasdet.depth);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, row);
            }
        };

        listenerPar1 = (record) -> {
            Util.listenerParam(record, tab3, eElempar1.grup, eGlaspar1.numb, eElempar1.text, tab1, tab2, tab3, tab4, tab5);
        };

        listenerPar2 = (record) -> {
            Util.listenerParam(record, tab4, eGlaspar2.grup, eGlaspar2.numb, eGlaspar2.text, tab1, tab2, tab3, tab4, tab5);
        };
    }

    private void listenerCell() {

        listenerEditor = (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return Util.listenerCell(tab3, tab4, component, tab1, tab2, tab3, tab4, tab5);
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        tabb1 = new javax.swing.JTabbedPane();
        pan4 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Заполнения");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Filling.this.windowClosed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 667, Short.MAX_VALUE)
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
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(900, 500));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(1000, 200));
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr1.setPreferredSize(new java.awt.Dimension(400, 200));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Мммммммммм", "1", "1,2,3", null},
                {"Ррррррррррр", "2", "1,2,3", null},
                {null, null, null, null}
            },
            new String [] {
                "Название", "Зазор", "Толщины доступные", "ID"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setName("tab1"); // NOI18N
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
            tab1.getColumnModel().getColumn(2).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(2).setMaxWidth(160);
            tab1.getColumnModel().getColumn(3).setMaxWidth(40);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        scr3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr3.setPreferredSize(new java.awt.Dimension(300, 200));

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
        tab3.setName("tab3"); // NOI18N
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab3.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        pan1.add(scr3, java.awt.BorderLayout.EAST);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        tabb1.setToolTipText("");
        tabb1.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabb1StateChanged(evt);
            }
        });

        pan4.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr2.setPreferredSize(new java.awt.Dimension(500, 300));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"4мм", "22www", "xxxxxxxxxxx", "qqqqqqqqqqqqqqq", "mmmmmmmmmmm", null, null, null},
                {"12мм", "44vvvv", "vvvvvvvvvvv", "hhhhhhhhhhhhhhh", "kkkkkkkkkkkkkkkkkk", null, null, null}
            },
            new String [] {
                "Толщина", "Артикул", "Название", "Текстура", "Основная", "Внутренняя", "Внешняя", "ID"
            }
        ));
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
            tab2.getColumnModel().getColumn(0).setMaxWidth(80);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(120);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(120);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(120);
            tab2.getColumnModel().getColumn(7).setMaxWidth(40);
        }

        pan4.add(scr2, java.awt.BorderLayout.CENTER);

        scr4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr4.setPreferredSize(new java.awt.Dimension(300, 300));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzz", "aaaaa"},
                {"ccccc", "vvvvv"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ));
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
            tab4.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab4.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        pan4.add(scr4, java.awt.BorderLayout.EAST);

        tabb1.addTab("Спецификация", pan4);

        pan3.setPreferredSize(new java.awt.Dimension(454, 304));
        pan3.setLayout(new java.awt.BorderLayout());

        scr5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr5.setPreferredSize(new java.awt.Dimension(454, 304));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"yyyyyyyy", "fffffffffffffff", "44", null, null, null},
                {"rrrrrrrrrrr", "pppppppppp", "77", null, null, null}
            },
            new String [] {
                "Артикул", "Название", "Размер от оси", "Внутреннее", "Внешнее", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab5.setFillsViewportHeight(true);
        tab5.setName("tab5"); // NOI18N
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(2).setPreferredWidth(60);
            tab5.getColumnModel().getColumn(2).setMaxWidth(120);
            tab5.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(3).setMaxWidth(120);
            tab5.getColumnModel().getColumn(4).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(4).setMaxWidth(120);
            tab5.getColumnModel().getColumn(5).setMaxWidth(40);
        }

        pan3.add(scr5, java.awt.BorderLayout.CENTER);

        tabb1.addTab("Профили в группе", pan3);

        centr.add(tabb1, java.awt.BorderLayout.SOUTH);
        tabb1.getAccessibleContext().setAccessibleName("");

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));
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

    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab2, tab3, tab5) == 0) {
                Util.deleteRecord(tab1, eGlasgrp.up);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab4) == 0) {
                Util.deleteRecord(tab2, eGlasdet.up);
            }
        } else if (tab3.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab3, eGlaspar1.up);
            }
        } else if (tab4.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab4, eGlaspar2.up);
            }
        } else if (tab5.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab5, eGlasprof.up);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, eGlasgrp.up);

        } else if (tab2.getBorder() != null) {
            Util.insertRecord(tab1, tab2, eGlasgrp.up, eGlasdet.up, eArtikl.up, eGlasdet.glasgrp_id);

        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab1, tab3, eGlasgrp.up, eGlaspar1.up, eGlaspar1.glasgrp_id);

        } else if (tab4.getBorder() != null) {
            Util.insertRecord(tab2, tab4, eGlasdet.up, eGlaspar2.up, eGlaspar2.glasdet_id);

        } else if (tab5.getBorder() != null) {
            Util.insertRecord(tab1, tab5, eGlasgrp.up, eGlasprof.up, eArtikl.up, eGlasprof.glasgrp_id);
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(tab1, tab2, tab3, tab4, tab5).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (owner != null) {
            owner.setEnabled(true);
            owner = null;
        }
    }//GEN-LAST:event_windowClosed

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab1, tab2, tab3, tab4, tab5).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate

    private void tabb1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabb1StateChanged
        if (tabb1.getSelectedIndex() == 0) {
            Util.listenerClick(tab2, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 1) {
            Util.listenerClick(tab5, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        }
    }//GEN-LAST:event_tabb1StateChanged
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    private void initElements() {
        new FrameToFile(this, btnClose);
        labFilter.setText(tab1.getColumnName(0));
        txtFilter.setName(tab1.getName());
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Список групп заполнений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация групп заполнений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Профили в группе заполнения", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
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
    }
}
