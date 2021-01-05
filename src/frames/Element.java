package frames;

import frames.dialog.DicArtikl;
import common.DialogListener;
import common.EditorListener;
import common.FrameListener;
import common.FrameToFile;
import convert.Convert;
import dataset.ConnApp;
import enums.Enam;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import frames.dialog.DicColvar;
import frames.dialog.DicGroups;
import frames.dialog.DicTypset;
import frames.dialog.ParColor2;
import frames.dialog.ParGrup2;
import frames.dialog.ParGrup2b;
import frames.dialog.ParGrup2a;
import domain.eArtikl;
import domain.eColor;
import domain.eParams;
import domain.eElemdet;
import domain.eElement;
import domain.eElemgrp;
import domain.eElempar1;
import domain.eElempar2;
import domain.eGroups;
import domain.eJoindet;
import domain.eJoinpar1;
import domain.eSysprof;
import enums.ParamList;
import enums.TypeGroups;
import enums.TypeSet;
import enums.UseColor;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import startup.Main;
import frames.swing.BooleanRenderer;
import frames.swing.DefTableModel;
import java.awt.Rectangle;
import java.util.Set;
import java.util.stream.Collectors;

public class Element extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.id, eParams.id, eParams.id, eParams.text);
    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qGroups = new Query(eGroups.values());
    private Query qElemgrp = new Query(eElemgrp.values());
    private Query qElement = new Query(eElement.values(), eArtikl.values());
    private Query qElemdet = new Query(eElemdet.values(), eArtikl.values());
    private Query qElempar1 = new Query(eElempar1.values(), eParams.values());
    private Query qElempar2 = new Query(eElempar2.values(), eParams.values());
    private DialogListener listenerArtikl, listenerPar1, listenerPar2, listenerTypset, listenerSeries, listenerColor, listenerColvar1, listenerColvar2, listenerColvar3;
    private String subsql = "(-1)";
    private EditorListener listenerEditor;

    public Element() {
        this.subsql = null;
        initComponents();
        initElements();        
        listenerCell();
        listenerDict();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    public Element(Set<Object> keys) {
        if (keys.isEmpty() == false) {
            this.subsql = keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        }
        initComponents();
        initElements();
        listenerCell();
        listenerDict();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    public Element(Set<Object> keys, int deteilID) {
        if (keys.isEmpty() == false) {
            this.subsql = keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        }
        initComponents();
        initElements();
        listenerCell();
        listenerDict();
        loadingData();
        loadingModel();
        listenerAdd();
        deteilFind(deteilID);
    }

    private void loadingData() {

        qColor.select(eColor.up);
        qParams.select(eParams.up, "where", eParams.elem, "= 1 and", eParams.id, "=", eParams.params_id, "order by", eParams.text);
        qGroups.select(eGroups.up, "where grup =" + TypeGroups.SERI_PROF.id);
        qElemgrp.select(eElemgrp.up, "order by", eElemgrp.level, ",", eElemgrp.name);
        Record record = eElemgrp.up.newRecord(Query.SEL);
        record.setNo(eElemgrp.id, -1);
        record.setNo(eElemgrp.level, 1);
        record.setNo(eElemgrp.name, "<html><font size='3' color='red'>&nbsp;&nbsp;&nbsp;ПРОФИЛИ</font>");
        qElemgrp.add(0, record);
        for (int index = 0; index < qElemgrp.size(); ++index) {
            int level = qElemgrp.getAs(index, eElemgrp.level);
            if (level == 5) {
                Record record2 = eElemgrp.up.newRecord(Query.SEL);
                record2.setNo(eElemgrp.id, -5);
                record2.setNo(eElemgrp.level, 5);
                record2.setNo(eElemgrp.name, "<html><font size='3' color='red'>&nbsp;&nbsp;ЗАПОЛНЕНИЯ</font>");
                qElemgrp.add(index, record2);
                break;
            }
        }
    }

    private void loadingModel() {

        tab1.getTableHeader().setEnabled(false);
        new DefTableModel(tab1, qElemgrp, eElemgrp.name);
        new DefTableModel(tab2, qElement, eArtikl.code, eArtikl.name, eElement.name, eElement.typset, eElement.series_id, eElement.todef, eElement.toset, eElement.markup) {

            public Object getValueAt(int col, int row, Object val) {

                if (val != null && columns[col] == eElement.typset) {
                    int typset = Integer.valueOf(val.toString());
                    return Arrays.asList(TypeSet.values()).stream().filter(el -> el.id == typset).findFirst().orElse(TypeSet.P1).name;

                } else if (val != null && columns[col] == eElement.series_id) {
                    return qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == Integer.valueOf(val.toString())).findFirst().orElse(eElement.up.newRecord()).get(eGroups.name);
                }
                return val;
            }
        };
        new DefTableModel(tab3, qElemdet, eArtikl.code, eArtikl.name, eElemdet.color_fk, eElemdet.types, eElemdet.types, eElemdet.types) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (eElemdet.color_fk == field) {
                    int colorFk = Integer.valueOf(val.toString());
                    if (Integer.valueOf(UseColor.automatic[0]) == colorFk) {
                        return UseColor.automatic[1];

                    } else if (Integer.valueOf(UseColor.precision[0]) == colorFk) {
                        return UseColor.precision[1];
                    }
                    if (colorFk > 0) {
                        return qColor.stream().filter(rec -> rec.getInt(eColor.id) == colorFk).findFirst().orElse(eColor.up.newRecord()).get(eColor.name);
                    } else {
                        return qParams.stream().filter(rec -> rec.getInt(eParams.id) == colorFk).findFirst().orElse(eParams.up.newRecord()).get(eParams.text);
                    }
                } else if (eElemdet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    types = (col == 3) ? types & 0x0000000f : (col == 4) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8;
                    return UseColor.MANUAL.find(types).text();
                }
                return val;
            }
        };
        new DefTableModel(tab4, qElempar1, eElempar1.params_id, eElempar1.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eElempar1.params_id == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record elempar1Rec = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? elempar1Rec.getStr(eElempar1.params_id) + ":" + elempar1Rec.getStr(eElempar1.text) : elempar1Rec.getStr(eElempar1.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qElempar2, eElempar2.params_id, eElempar2.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eElempar2.params_id) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? record.getStr(eElempar2.id) + ":" + record.getStr(eElempar2.text) : record.getStr(eElempar2.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };

        BooleanRenderer br = new BooleanRenderer();
        Arrays.asList(5, 6).forEach(index -> tab2.getColumnModel().getColumn(index).setCellRenderer(br));

        Util.setSelectedRow(tab1);
    }

    private void selectionTab1(ListSelectionEvent event) {
        Util.clearTable(tab2, tab3, tab4, tab5);
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record record = qElemgrp.get(row);
            Integer id = record.getInt(eElemgrp.id);
            if (id == -1 || id == -5) {
                if (subsql == null) {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "left join", eElemgrp.up, "on", eElemgrp.id, "=", eElement.elemgrp_id, "where", eElemgrp.level, "=", Math.abs(id));
                } else {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "left join", eElemgrp.up, "on", eElemgrp.id, "=", eElement.elemgrp_id, "where", eElemgrp.level, "=", Math.abs(id), "and", eElement.id, "in " + subsql);
                }
            } else {
                if (subsql == null) {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "where", eElement.elemgrp_id, "=", id, "order by", eElement.name);
                } else {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "where", eElement.elemgrp_id, "=", id, "and", eElement.id, "in " + subsql, "order by", eElement.name);
                }
            }
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        Util.clearTable(tab3, tab4, tab5);
        int row = Util.getSelectedRec(tab2);
        if (row != -1) {
            Record record = qElement.table(eElement.up).get(row);
            Integer p1 = record.getInt(eElement.id);
            qElemdet.select(eElemdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eElemdet.artikl_id, "where", eElemdet.element_id, "=", p1);
            qElempar1.select(eElempar1.up, "left join", eParams.up, "on", eParams.id, "=", eElempar1.params_id, "where", eElempar1.element_id, "=", p1);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3);
            Util.setSelectedRow(tab4);
        }
    }

    private void selectionTab3(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab3);
        if (row != -1) {
            //Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            Arrays.asList(qElempar2).forEach(q -> q.execsql());
            Record record = qElemdet.table(eElemdet.up).get(row);
            Integer p1 = record.getInt(eElemdet.id);
            qElempar2.select(eElempar2.up, "left join", eParams.up, "on", eParams.id, "=", eElempar2.params_id, "where", eElempar2.elemdet_id, "=", p1);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab5);
        }
    }

    private void deteilFind(int deteilID) {
        Query qDet = new Query(eElemdet.values(), eArtikl.values());
        for (int index = 0; index < qElement.size(); index++) {
            int element_id = qElement.get(index).getInt(eElement.id);
            qDet.select(eElemdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eElemdet.artikl_id, "where", eElemdet.element_id, "=", element_id);
            for (int index2 = 0; index2 < qDet.size(); index2++) {
                if (qDet.get(index2).getInt(eElemdet.id) == deteilID) {
                    Util.setSelectedRow(tab2, index);
                    Util.scrollRectToVisible(index, tab2);
                    Util.setSelectedRow(tab3, index2);
                    Util.scrollRectToVisible(index2, tab3);
                }
            }
        }
    }

    private void listenerAdd() {
        Util.buttonEditorCell(tab2, 0).addActionListener(event -> {
            int level = qElemgrp.getAs(Util.getSelectedRec(tab1), eElemgrp.level);
            DicArtikl frame = new DicArtikl(this, listenerArtikl, level);
        });

        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            int level = qElemgrp.getAs(Util.getSelectedRec(tab1), eElemgrp.level);
            DicArtikl frame = new DicArtikl(this, listenerArtikl, level);
        });

        Util.buttonEditorCell(tab2, 3).addActionListener(event -> {
            DicTypset frame = new DicTypset(this, listenerTypset);
        });

        Util.buttonEditorCell(tab2, 4).addActionListener(event -> {
            DicGroups frame = new DicGroups(this, listenerSeries, TypeGroups.SERI_PROF);
        });

        Util.buttonEditorCell(tab3, 0).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4, 5);
        });

        Util.buttonEditorCell(tab3, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4, 5);
        });

        Util.buttonEditorCell(tab3, 2).addActionListener(event -> {
            Record record = qElemdet.get(Util.getSelectedRec(tab3));
            int artikl_id = record.getInt(eElemdet.artikl_id);
            ParColor2 frame = new ParColor2(this, listenerColor, artikl_id);
        });

        Util.buttonEditorCell(tab3, 3).addActionListener(event -> {
            Record record = qElemdet.get(Util.getSelectedRec(tab3));
            int colorFk = record.getInt(eElemdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar1, colorFk);
        });

        Util.buttonEditorCell(tab3, 4).addActionListener(event -> {
            Record record = qElemdet.get(Util.getSelectedRec(tab3));
            int colorFk = record.getInt(eElemdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar2, colorFk);
        });

        Util.buttonEditorCell(tab3, 5).addActionListener(event -> {
            Record record = qElemdet.get(Util.getSelectedRec(tab3));
            int colorFk = record.getInt(eElemdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar3, colorFk);
        });

        Util.buttonEditorCell(tab4, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab1);
            if (row != -1) {
                Record record = qElemgrp.get(row);
                int paramPart = record.getInt(eElemgrp.level);
                paramPart = (paramPart == 1) ? 31000 : 37000;
                ParGrup2 frame = new ParGrup2(this, listenerPar1, eParams.elem, paramPart);
            }
        });

        Util.buttonEditorCell(tab4, 1, listenerEditor).addActionListener(event -> {
            Record record = qElempar1.get(Util.getSelectedRec(tab4));
            int grup = record.getInt(eElempar1.params_id);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar1, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar1, list);
            }
        });

        Util.buttonEditorCell(tab5, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab3);
            if (row != -1) {
                Record recordJoin = qElemdet.get(row);
                int artikl_id = recordJoin.getInt(eJoindet.artikl_id);
                Record recordArt = eArtikl.find(artikl_id, false);
                int level = recordArt.getInt(eArtikl.level1);
                Integer[] part = {0, 39000, 38000, 39000, 38000, 40000, 0};
                ParGrup2 frame = new ParGrup2(this, listenerPar2, eParams.joint, part[level]);
            }
        });

        Util.buttonEditorCell(tab5, 1, listenerEditor).addActionListener(event -> {
            Record record = qElempar2.get(Util.getSelectedRec(tab5));
            int grup = record.getInt(eElempar2.params_id);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar2, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar2, list);
            }
        });
    }

    private void listenerDict() {

        listenerTypset = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                qElement.set(record.getInt(0), Util.getSelectedRec(tab2), eElement.typset);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, row);
            }
        };

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                qElement.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab2), eElement.artikl_id);
                qElement.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab2), eArtikl.name);
                qElement.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab2), eArtikl.code);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, row);

            } else if (tab3.getBorder() != null) {
                int row = Util.getSelectedRec(tab3);
                qElemdet.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab3), eElemdet.artikl_id);
                qElemdet.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab3), eArtikl.name);
                qElemdet.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab3), eArtikl.code);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab3, row);
            }
        };

        listenerSeries = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                int series_id = record.getInt(eGroups.id);
                qElement.set(series_id, Util.getSelectedRec(tab2), eElement.series_id);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, row);
            }
        };

        listenerColor = (record) -> {
            Util.listenerColor(record, tab3, eElemdet.color_fk, eElemdet.types, tab1, tab2, tab3, tab4, tab5);
        };

        listenerColvar1 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            Record elemdetRec = qElemdet.get(Util.getSelectedRec(tab3));
            int types = (elemdetRec.getInt(eElemdet.types) == -1) ? 0 : elemdetRec.getInt(eElemdet.types);
            types = (types & 0xfffffff0) + record.getInt(0);
            elemdetRec.set(eElemdet.types, types);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerColvar2 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            Record elemdetRec = qElemdet.get(Util.getSelectedRec(tab3));
            int types = (elemdetRec.getInt(eElemdet.types) == -1) ? 0 : elemdetRec.getInt(eElemdet.types);
            types = (types & 0xffffff0f) + (record.getInt(0) << 4);
            elemdetRec.set(eElemdet.types, types);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerColvar3 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            Record elemdetRec = qElemdet.get(Util.getSelectedRec(tab3));
            int types = (elemdetRec.getInt(eElemdet.types) == -1) ? 0 : elemdetRec.getInt(eElemdet.types);
            types = (types & 0xfffff0ff) + (record.getInt(0) << 8);
            elemdetRec.set(eElemdet.types, types);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerPar1 = (record) -> {
            Util.listenerParam(record, tab4, eElempar1.params_id, eElempar1.text, tab1, tab2, tab3, tab4, tab5);
        };

        listenerPar2 = (record) -> {
            Util.listenerParam(record, tab5, eElempar2.params_id, eElempar2.text, tab1, tab2, tab3, tab4, tab5);
        };
    }

    private void listenerCell() {

        listenerEditor = (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return Util.listenerCell(tab4, tab5, component, tab1, tab2, tab3, tab4, tab5);
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppmCateg = new javax.swing.JPopupMenu();
        itCateg1 = new javax.swing.JMenuItem();
        itCtag2 = new javax.swing.JMenuItem();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        west = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        itCateg1.setText("ПРОФИЛИ");
        itCateg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppmCategAction(evt);
            }
        });
        ppmCateg.add(itCateg1);

        itCtag2.setText("ЗАПОЛНЕНИЯ");
        itCtag2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppmCategAction(evt);
            }
        });
        ppmCateg.add(itCtag2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Вставки");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Element.this.windowClosed(evt);
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
                .addGap(148, 148, 148)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 567, Short.MAX_VALUE)
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
                    .addComponent(btnReport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setPreferredSize(new java.awt.Dimension(758, 460));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(847, 320));
        pan1.setLayout(new java.awt.BorderLayout());

        scr2.setPreferredSize(new java.awt.Dimension(454, 320));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"aaaa", null, "vvvvvvvvvvvvvvv", "ddd", null, null, null,  new Double(0.0), null},
                {"aaaa", null, "hhhhhhhhhhhhhh", "fff", null, null, null,  new Double(0.0), null}
            },
            new String [] {
                "Артикул", "Название", "Наименование", "Тип состава", "Для серии", "Умолчание", "Обязательно", "Наценка", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Double.class, java.lang.Object.class
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
            tab2.getColumnModel().getColumn(0).setPreferredWidth(96);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(200);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(5).setPreferredWidth(32);
            tab2.getColumnModel().getColumn(5).setMaxWidth(60);
            tab2.getColumnModel().getColumn(6).setPreferredWidth(32);
            tab2.getColumnModel().getColumn(6).setMaxWidth(60);
            tab2.getColumnModel().getColumn(7).setPreferredWidth(32);
            tab2.getColumnModel().getColumn(7).setMaxWidth(60);
            tab2.getColumnModel().getColumn(8).setMaxWidth(40);
        }

        pan1.add(scr2, java.awt.BorderLayout.CENTER);

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
        tab4.setName("tab4"); // NOI18N
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        pan1.add(scr4, java.awt.BorderLayout.EAST);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        pan2.setLayout(new java.awt.BorderLayout());

        scr5.setPreferredSize(new java.awt.Dimension(260, 204));

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
        tab5.setName("tab5"); // NOI18N
        tab5.setPreferredSize(new java.awt.Dimension(0, 64));
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        pan2.add(scr5, java.awt.BorderLayout.EAST);

        scr3.setPreferredSize(new java.awt.Dimension(454, 204));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxxxxx", null, "mmmmm", "kkkkkkk", "vvvvv", "ffffff", null},
                {"zzzzzzzzz", null, "mmmmm", "kkkkkkk", "ddddd", "yyyyy", null}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Основная", "Внутренняя", "Внешняя", "ID"
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
            tab3.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        pan2.add(scr3, java.awt.BorderLayout.CENTER);

        centr.add(pan2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        west.setPreferredSize(new java.awt.Dimension(140, 480));
        west.setLayout(new java.awt.BorderLayout());

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
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);

        west.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(west, java.awt.BorderLayout.WEST);

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
        Arrays.asList(tab1, tab2, tab3, tab4, tab5).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab2) == 0) {
                Util.deleteRecord(tab1, eElemgrp.up);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab3, tab4) == 0) {
                Util.deleteRecord(tab2, eElement.up);
            }
        } else if (tab3.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab5) == 0) {
                Util.deleteRecord(tab3, eElemdet.up);
            }
        } else if (tab4.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab4, eElempar1.up);
            }
        } else if (tab5.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab5, eElempar2.up);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            ppmCateg.show(north, btnIns.getX(), btnIns.getY() + 18);

        } else if (tab2.getBorder() != null) {
            Record rec = qElemgrp.get(Util.getSelectedRec(tab1));
            if (rec != null && rec.getInt(eElemgrp.id) != -1 && rec.getInt(eElemgrp.id) != -5) {
                Util.insertRecord(tab1, tab2, eElemgrp.up, eElement.up, eArtikl.up, eElement.elemgrp_id);
            } else {
                JOptionPane.showMessageDialog(this, "Не выбрана запись в списке категорий", "Предупреждение", JOptionPane.NO_OPTION);
            }
        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab2, tab3, eElement.up, eElemdet.up, eArtikl.up, eElemdet.element_id);

        } else if (tab4.getBorder() != null) {
            Util.insertRecord(tab2, tab4, eElement.up, eElempar1.up, eElempar1.element_id);

        } else if (tab5.getBorder() != null) {
            Util.insertRecord(tab3, tab5, eElemdet.up, eElempar2.up, eElempar2.elemdet_id);
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(tab1, tab2, tab3, tab4, tab5).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
    }//GEN-LAST:event_windowClosed

    private void ppmCategAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppmCategAction
        JMenuItem ppm = (JMenuItem) evt.getSource();
        int indexCateg = (ppm == itCateg1) ? 1 : 5;
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record elemgrpRec = eElemgrp.up.newRecord(Query.INS);
            elemgrpRec.setNo(eElemgrp.id, ConnApp.instanc().genId(eElemgrp.up));
            elemgrpRec.setNo(eElemgrp.level, indexCateg); //-1 -ПРОФИЛИ, -5 -ЗАПОЛНЕНИЯ
            qElemgrp.add(elemgrpRec);
            qElemgrp.execsql();
            loadingData();
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qElemgrp, tab1);
        }
    }//GEN-LAST:event_ppmCategAction

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        Util.scrollRectToVisible(qElement, tab2);
    }//GEN-LAST:event_btnReport

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JMenuItem itCateg1;
    private javax.swing.JMenuItem itCtag2;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPopupMenu ppmCateg;
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
    private javax.swing.JTextField txtFilter;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        labFilter.setText(tab1.getColumnName(0));
        txtFilter.setName(tab1.getName());
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Категории вставок", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки вставок", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация вставок", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
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
    }
}
