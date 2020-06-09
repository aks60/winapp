package frames;

import common.DialogListener;
import common.EditorListener;
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
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import dataset.Field;
import dataset.Table;
import frames.dialog.DicArtikl;
import frames.dialog.DicColvar;
import frames.dialog.DicEnums;
import frames.dialog.ParColor2;
import frames.dialog.ParGrup2;
import frames.dialog.ParGrup2b;
import frames.dialog.ParGrup2a;
import domain.eParams;
import enums.Enam;
import enums.ParamList;
import enums.LayoutFurn1;
import enums.UseFurn3;
import enums.LayoutFurn3;
import enums.UseColcalc;
import enums.UseFurn1;
import enums.UseFurn2;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import startup.Main;

public class Furniture extends javax.swing.JFrame {

    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qParams = new Query(eParams.id, eParams.grup, eParams.numb, eParams.text);
    private Query qFurnall = new Query(eFurniture.values());
    private Query qFurniture = new Query(eFurniture.values());
    private Query qArtikl = new Query(eArtikl.id, eArtikl.code, eArtikl.name);
    private Query qFurndet1 = new Query(eFurndet.values());
    private Query qFurndet2 = new Query(eFurndet.values());
    private Query qFurndet3 = new Query(eFurndet.values());
    private Query qFurnside1 = new Query(eFurnside1.values());
    private Query qFurnside2 = new Query(eFurnside2.values());
    private Query qFurnpar1 = new Query(eFurnpar1.values());
    private Query qFurnpar2 = new Query(eFurnpar2.values());
    private EditorListener listenerEditor = null;
    private DialogListener listenerArtikl, listenerPar1, listenerPar2, listenerTypset, listenerColor,
            listenerColvar, listenerSide1, listenerSide2, listenerSide3, listenerSide4, listenerVariant1, listenerVariant2;
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;

    public Furniture() {
        initComponents();
        initElements();
        listenerCell();
        loadingData();
        loadingModel();
        listenerDict();
    }

    public Furniture(java.awt.Window owner, int nuni) {
        initComponents();
        initElements();
        this.owner = owner;
        this.nuni = nuni;
        listenerCell();
        loadingData();
        loadingModel();
        listenerDict();
        //owner.setEnabled(false);
    }

    private void loadingData() {
        qColor.select(eColor.up);
        qArtikl.select(eArtikl.up);
        qFurnall.select(eFurniture.up, "order by", eFurniture.name);
        qParams.select(eParams.up, "where", eParams.numb, "= 0 order by", eParams.text); //TODO отключил фильтр, а это неправильно
        //qParams.select(eParams.up, "where", eParams.furn, "= 1 and", eParams.numb, "= 0 order by", eParams.text);
        int types = (checkBox1.isSelected()) ? 0 : (checkBox2.isSelected()) ? 1 : -1;
        if (owner == null) {
            qFurniture.select(eFurniture.up, "where", eFurniture.types, "=", types, "order by", eFurniture.name);
        } else {
            Query query = new Query(eSysfurn.furniture_id).select(eSysfurn.up, "where", eSysfurn.systree_id, "=", nuni).table(eSysfurn.up);
            query.stream().forEach(rec -> subsql = subsql + "," + rec.getStr(eSysfurn.furniture_id));
            subsql = "(" + subsql.substring(1) + ")";
            qFurniture.select(eFurniture.up, "where", eFurniture.id, "in", subsql, "and", eFurniture.types, "=", types, "order by", eFurniture.name);
        }
    }

    private void loadingModel() {
        new DefTableModel(tab1, qFurniture, eFurniture.name, eFurniture.view_open, eFurniture.hand_side, eFurniture.p2_max,
                eFurniture.width_max, eFurniture.height_max, eFurniture.weight_max, eFurniture.ways_use, eFurniture.pars, eFurniture.coord_lim, eFurniture.id) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurniture.view_open == field) {
                    int fk = Integer.valueOf(val.toString());
                    if (UseFurn1.P1.find(fk) != null) {
                        return UseFurn1.P1.find(fk).text();
                    }
                } else if (val != null && eFurniture.hand_side == field) {
                    int fk = Integer.valueOf(val.toString());
                    if (LayoutFurn1.P1.find(fk) != null) {
                        return LayoutFurn1.P1.find(fk).text();
                    }
                } else if (val != null && eFurniture.ways_use == field) {
                    int fk = Integer.valueOf(val.toString());
                    if (UseFurn2.P1.find(fk) != null) {
                        return UseFurn2.P1.find(fk).text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab2a, qFurndet1, eFurndet.artikl_id, eFurndet.artikl_id, eFurndet.color_fk, eFurndet.types, eFurndet.id) {

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columns != null) {
                    Table query2 = qFurndet1.table(columns[columnIndex]);
                    Object val = query2.get(rowIndex, columns[columnIndex]);
                    return getValueAt(columnIndex, rowIndex, val);
                    //return (val != null) ? getValueAt(columnIndex, rowIndex, val) : null;
                }
                return null;
            }

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurndet.color_fk == field) {
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

                } else if (val != null && eFurndet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    return (UseColcalc.P00.find(types) != null) ? UseColcalc.P00.find(types).text() : null;

                } else if (eFurndet.artikl_id == field) {
                    if (qFurndet1.get(row, eFurndet.furniture_id2) != null) {
                        int furniture_id2 = qFurndet1.getAs(row, eFurndet.furniture_id2);
                        String name = qFurnall.stream().filter(rec -> rec.getInt(eFurniture.id) == furniture_id2).findFirst().orElse(eFurniture.up.newRecord()).getStr(eFurniture.name);
                        return (col == 0) ? "Набор" : name;
                    } else if (val != null) {
                        int artikl_id = Integer.valueOf(val.toString());
                        Record recordArt = qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == artikl_id).findFirst().orElse(eArtikl.up.newRecord());
                        return (col == 0) ? recordArt.getStr(eArtikl.code) : recordArt.getStr(eArtikl.name);
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab2b, qFurndet2, eFurndet.artikl_id, eFurndet.artikl_id, eFurndet.color_fk, eFurndet.types, eFurndet.id) {

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columns != null) {
                    Table query2 = qFurndet2.table(columns[columnIndex]);
                    Object val = query2.get(rowIndex, columns[columnIndex]);
                    return getValueAt(columnIndex, rowIndex, val);
                }
                return null;
            }

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurndet.color_fk == field) {
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

                } else if (val != null && eFurndet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    return (UseColcalc.P00.find(types) != null) ? UseColcalc.P00.find(types).text() : null;

                } else if (eFurndet.artikl_id == field) {
                    if (qFurndet2.get(row, eFurndet.furniture_id2) != null) {
                        int furniture_id2 = qFurndet2.getAs(row, eFurndet.furniture_id2);
                        String name = qFurnall.stream().filter(rec -> rec.getInt(eFurniture.id) == furniture_id2).findFirst().orElse(eFurniture.up.newRecord()).getStr(eFurniture.name);
                        return (col == 0) ? "Набор" : name;
                    } else if (val != null) {
                        int artikl_id = Integer.valueOf(val.toString());
                        Record recordArt = qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == artikl_id).findFirst().orElse(eArtikl.up.newRecord());
                        return (col == 0) ? recordArt.getStr(eArtikl.code) : recordArt.getStr(eArtikl.name);
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab2c, qFurndet3, eFurndet.artikl_id, eFurndet.artikl_id, eFurndet.color_fk, eFurndet.types, eFurndet.id) {

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columns != null) {
                    Table query2 = qFurndet3.table(columns[columnIndex]);
                    Object val = query2.get(rowIndex, columns[columnIndex]);
                    return getValueAt(columnIndex, rowIndex, val);
                }
                return null;
            }

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurndet.color_fk == field) {
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

                } else if (val != null && eFurndet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    return (UseColcalc.P00.find(types) != null) ? UseColcalc.P00.find(types).text() : null;

                } else if (eFurndet.artikl_id == field) {
                    if (qFurndet3.get(row, eFurndet.furniture_id2) != null) {
                        int furniture_id2 = qFurndet3.getAs(row, eFurndet.furniture_id2);
                        String name = qFurnall.stream().filter(rec -> rec.getInt(eFurniture.id) == furniture_id2).findFirst().orElse(eFurniture.up.newRecord()).getStr(eFurniture.name);
                        return (col == 0) ? "Набор" : name;
                    } else if (val != null) {
                        int artikl_id = Integer.valueOf(val.toString());
                        Record recordArt = qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == artikl_id).findFirst().orElse(eArtikl.up.newRecord());
                        return (col == 0) ? recordArt.getStr(eArtikl.code) : recordArt.getStr(eArtikl.name);
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab3, qFurnside1, eFurnside1.side_num, eFurnside1.side_use) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eFurnside1.side_num == field) {
                    int v = Integer.valueOf(val.toString());
                    if (v > 0 && v < 5) {
                        return LayoutFurn1.values()[v - 1].name;
                    }
                } else if (val != null && eFurnside1.side_use == field) {
                    int v = Integer.valueOf(val.toString());
                    if (v > 0 && v < 4) {
                        return UseFurn3.values()[v - 1].name;
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab4, qFurnpar1, eFurnpar1.grup, eFurnpar1.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eFurnpar1.grup && val != null) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? record.getStr(eFurnpar1.grup) + "-" + record.getStr(eFurnpar1.text) : record.getStr(eFurnpar1.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qFurnside2, eFurnside2.side_num, eFurnside2.len_min, eFurnside2.len_max, eFurnside2.ang_min, eFurnside2.ang_max) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eFurnside2.side_num == field) {
                    int v = Integer.valueOf(val.toString());
                    if (v > 0 && v < 7) {
                        return LayoutFurn3.values()[v - 1].name;
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab6, qFurnpar2, eFurnpar2.grup, eFurnpar2.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && field == eFurnpar2.grup) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? record.getStr(eFurnpar2.grup) + "-" + record.getStr(eFurnpar2.text) : record.getStr(eFurnpar2.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };
        //((TableRowSorter) tab2a.getRowSorter()).toggleSortOrder(0);
        //((TableRowSorter) tab2b.getRowSorter()).toggleSortOrder(0);
        //((TableRowSorter) tab2c.getRowSorter()).toggleSortOrder(0);

        Util.buttonEditorCell(tab1, 1).addActionListener(event -> {
            new DicEnums(this, listenerVariant1, UseFurn1.values());
        });

        Util.buttonEditorCell(tab1, 2).addActionListener(event -> {
            new DicEnums(this, listenerSide4, LayoutFurn1.values());
        });

        Util.buttonEditorCell(tab1, 7).addActionListener(event -> {
            new DicEnums(this, listenerVariant2, UseFurn2.values());
        });

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
                ParColor2 frame = new ParColor2(this, listenerColor, artikl_id);
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

        Util.buttonEditorCell(tab3, 0).addActionListener(event -> {
            new DicEnums(this, listenerSide1, LayoutFurn1.values());
        });

        Util.buttonEditorCell(tab3, 1).addActionListener(event -> {
            new DicEnums(this, listenerSide2, UseFurn3.values());
        });

        Util.buttonEditorCell(tab4, 0).addActionListener(event -> {
            ParGrup2 frame = new ParGrup2(this, listenerPar1, eParams.joint, 21000);
        });

        Util.buttonEditorCell(tab4, 1, listenerEditor).addActionListener(event -> {
            Record record = qFurnpar1.get(Util.getSelectedRec(tab4));
            int grup = record.getInt(eFurnpar1.grup);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar1, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar1, list);
            }
        });

        Util.buttonEditorCell(tab5, 0).addActionListener(event -> {
            new DicEnums(this, listenerSide3, LayoutFurn3.values());
        });

        Util.buttonEditorCell(tab6, 0).addActionListener(event -> {
            int index = tabb1.getSelectedIndex();
            JTable table = (index == 0) ? tab2a : (index == 1) ? tab2b : tab2c;
            int row = Util.getSelectedRec(table);
            if (row != -1) {
                Query query = (index == 0) ? qFurndet1 : (index == 1) ? qFurndet2 : qFurndet3;
                Record recordFurn = query.get(row);
                int artikl_id = recordFurn.getInt(eFurndet.artikl_id);
                Record recordArt = eArtikl.find(artikl_id, false);
                int level = recordArt.getInt(eArtikl.level1);
                Integer[] part = {0, 25000, 24000, 25000, 24000, 0};
                ParGrup2 frame = new ParGrup2(this, listenerPar2, eParams.joint, part[level]);
            }
        });

        Util.buttonEditorCell(tab6, 1, listenerEditor).addActionListener(event -> {
            Record record = qFurnpar2.get(Util.getSelectedRec(tab6));
            int grup = record.getInt(eFurnpar2.grup);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar2, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar2, list);
            }
        });

        Util.setSelectedRow(tab1);
    }

    private void selectionTab1(ListSelectionEvent event) {
        Util.clearTable(tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record record = qFurniture.table(eFurniture.up).get(row);
            Integer id = record.getInt(eFurniture.id);
            qFurnside1.select(eFurnside1.up, "where", eFurnside1.furniture_id, "=", id, "order by", eFurnside1.side_num);
            qFurndet1.select(eFurndet.up, "where", eFurndet.furniture_id1, "=", id, "and", eFurndet.furndet_id, "=", eFurndet.id);

            ((DefaultTableModel) tab2a.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2a);
            Util.setSelectedRow(tab3);
            String count = (qFurndet1.size() > 9) ? String.valueOf(qFurndet1.size()) : String.valueOf(qFurndet1.size()) + "  ";
            tabb1.setTitleAt(0, "Детализация (1 уровень)    " + count + "  ");
        }
    }

    private void selectionTab2a(ListSelectionEvent event) {
        Util.clearTable(tab2b, tab2c, tab5, tab6);
        int row = Util.getSelectedRec(tab2a);
        if (row != -1) {
            Record record = qFurndet1.table(eFurndet.up).get(row);
            Integer id = record.getInt(eFurndet.id);
            qFurndet2.select(eFurndet.up, "where", eFurndet.furndet_id, "=", id, "and", eFurndet.id, "!=", eFurndet.furndet_id);
            ((DefaultTableModel) tab2b.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2b);
            String count = (qFurndet2.size() > 9) ? String.valueOf(qFurndet2.size()) : String.valueOf(qFurndet2.size()) + "  ";
            tabb1.setTitleAt(1, "Детализация (2 уровень)    " + count + "  ");
            if (qFurndet2.size() == 0) {
                tabb1.setTitleAt(2, "Детализация (3 уровень)    " + count + "  ");
            }

            if (tabb1.getSelectedIndex() == 0) {
                qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.grup);
                qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
                ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab6);
                Util.setSelectedRow(tab5);
            }
        }
    }

    private void selectionTab2b(ListSelectionEvent event) {
        Util.clearTable(tab2c, tab5, tab6);
        int row = Util.getSelectedRec(tab2b);
        if (row != -1) {
            Record record = qFurndet2.table(eFurndet.up).get(row);
            Integer id = record.getInt(eFurndet.id);
            qFurndet3.select(eFurndet.up, "where", eFurndet.furndet_id, "=", id); //, "and", eFurndet.id, "!=", eFurndet.furndet_id);
            ((DefaultTableModel) tab2c.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2c);
            String count = (qFurndet3.size() > 9) ? String.valueOf(qFurndet3.size()) : String.valueOf(qFurndet3.size()) + "  ";
            tabb1.setTitleAt(2, "Детализация (3 уровень)    " + count + "  ");

            if (tabb1.getSelectedIndex() == 1) {
                qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.grup);
                qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
                ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab6);
                Util.setSelectedRow(tab5);
            }
        }
    }

    private void selectionTab2c(ListSelectionEvent event) {
        Util.clearTable(tab5, tab6);
        if (tabb1.getSelectedIndex() == 2) {
            int row = Util.getSelectedRec(tab2c);
            if (row != -1) {
                Record record = qFurndet3.table(eFurndet.up).get(row);
                Integer id = record.getInt(eFurndet.id);
                if (tabb1.getSelectedIndex() == 2) {
                    qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.grup);
                    qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
                    ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                    ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                    Util.setSelectedRow(tab6);
                    Util.setSelectedRow(tab5);
                }
            }
        }
    }

    private void selectionTab3(ListSelectionEvent event) {
        Util.clearTable(tab4);
        int row = Util.getSelectedRec(tab3);
        if (row != -1) {
            Record record = qFurnside1.table(eFurnside1.up).get(row);
            Integer id = record.getInt(eFurnside1.id);
            qFurnpar1.select(eFurnpar1.up, "where", eFurnpar1.furnside_id, "=", id, "order by", eFurnpar1.grup);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4);
        }
    }

    private void listenerDict() {

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Query query = (tab2a.getBorder() != null) ? qFurndet1 : (tab2b.getBorder() != null) ? qFurndet2 : qFurndet3;
            if (tab.getBorder() != null) {
                int row = Util.getSelectedRec(tab);
                query.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab), eFurndet.artikl_id);
                ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab, row);
            }
        };

        listenerColor = (record) -> {
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Util.listenerColor(record, tab, eFurndet.color_fk, eFurndet.types, tab1, tab2a, tab2b, tab2c, tab6, tab3, tab4);
        };

        listenerColvar = (record) -> {
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Query query = (tab2a.getBorder() != null) ? qFurndet1 : (tab2b.getBorder() != null) ? qFurndet2 : qFurndet3;
            Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            int row = Util.getSelectedRec(tab);
            Record furndetRec = query.get(Util.getSelectedRec(tab));
            furndetRec.set(eFurndet.types, record.getInt(0));
            ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab, row);
        };

        listenerSide1 = (record) -> {
            Util.listenerEnums(record, tab3, eFurnside1.side_num, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerSide2 = (record) -> {
            Util.listenerEnums(record, tab3, eFurnside1.side_use, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerSide3 = (record) -> {
            Util.listenerEnums(record, tab5, eFurnside2.side_num, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerSide4 = (record) -> {
            Util.listenerEnums(record, tab1, eFurniture.hand_side, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerPar1 = (record) -> {
            Util.listenerParam(record, tab4, eFurnpar1.grup, eFurnpar1.numb, eFurnpar1.text, tab1, tab2a, tab2b, tab2c, tab6, tab3, tab4);
        };

        listenerPar2 = (record) -> {
            Util.listenerParam(record, tab6, eFurnpar2.grup, eFurnpar2.numb, eFurnpar2.text, tab1, tab2a, tab2b, tab2c, tab6, tab3, tab4);
        };

        listenerVariant1 = (record) -> {
            Util.listenerEnums(record, tab1, eFurniture.view_open, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerVariant2 = (record) -> {
            Util.listenerEnums(record, tab1, eFurniture.ways_use, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };
    }

    private void listenerCell() {

        listenerEditor = (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return Util.listenerCell(tab4, tab6, component, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grour1 = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnIns = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        checkBox1 = new javax.swing.JCheckBox();
        checkBox2 = new javax.swing.JCheckBox();
        checkBox3 = new javax.swing.JCheckBox();
        center = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan8 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        pan6 = new javax.swing.JPanel();
        pan9 = new javax.swing.JPanel();
        tabb1 = new javax.swing.JTabbedPane();
        scr2a = new javax.swing.JScrollPane();
        tab2a = new javax.swing.JTable();
        scr2b = new javax.swing.JScrollPane();
        tab2b = new javax.swing.JTable();
        scr2c = new javax.swing.JScrollPane();
        tab2c = new javax.swing.JTable();
        pan10 = new javax.swing.JPanel();
        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Фурнитура");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Furniture.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(900, 29));

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

        grour1.add(checkBox1);
        checkBox1.setSelected(true);
        checkBox1.setText("Основная");
        checkBox1.setMaximumSize(new java.awt.Dimension(75, 25));
        checkBox1.setMinimumSize(new java.awt.Dimension(75, 25));
        checkBox1.setPreferredSize(new java.awt.Dimension(75, 25));
        checkBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAction(evt);
            }
        });

        grour1.add(checkBox2);
        checkBox2.setText("Дополн...");
        checkBox2.setMaximumSize(new java.awt.Dimension(75, 25));
        checkBox2.setMinimumSize(new java.awt.Dimension(75, 25));
        checkBox2.setPreferredSize(new java.awt.Dimension(75, 25));
        checkBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAction(evt);
            }
        });

        grour1.add(checkBox3);
        checkBox3.setText("Наборы");
        checkBox3.setMaximumSize(new java.awt.Dimension(75, 25));
        checkBox3.setMinimumSize(new java.awt.Dimension(75, 25));
        checkBox3.setPreferredSize(new java.awt.Dimension(75, 25));
        checkBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAction(evt);
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
                .addGap(80, 80, 80)
                .addComponent(checkBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 298, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setPreferredSize(new java.awt.Dimension(900, 500));
        center.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(800, 260));
        pan1.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(500, 200));
        pan4.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(450, 200));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"333333333333333", "3", "3", "3", null, null, null, null, null, null, null},
                {"444444444444444", "3", "4", "5", null, null, null, null, null, null, null}
            },
            new String [] {
                "Название", "Вид", "Сторона ручки", "Р/2 максимальная", "Ширина максимальная", "Высота максимальная", "Вес максимальный", "Створка", "Использ. параметры", "Ограничения", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
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
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(200);
            tab1.getColumnModel().getColumn(10).setMaxWidth(40);
        }

        pan4.add(scr1, java.awt.BorderLayout.CENTER);

        pan1.add(pan4, java.awt.BorderLayout.CENTER);

        pan5.setPreferredSize(new java.awt.Dimension(300, 200));
        pan5.setLayout(new java.awt.BorderLayout());

        pan7.setPreferredSize(new java.awt.Dimension(300, 108));
        pan7.setLayout(new java.awt.BorderLayout());

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxx", "mmmmm"},
                {"vvvvv", "ddddddd"}
            },
            new String [] {
                "Сторона", "Назначение"
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
            tab3.getColumnModel().getColumn(0).setPreferredWidth(80);
        }

        pan7.add(scr3, java.awt.BorderLayout.CENTER);

        pan5.add(pan7, java.awt.BorderLayout.NORTH);

        pan8.setPreferredSize(new java.awt.Dimension(300, 90));
        pan8.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(null);
        scr4.setPreferredSize(new java.awt.Dimension(300, 100));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxxxxxxxxxxxx", "11"},
                {"vvvvvvvvvvvvvvv", "22"}
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
            tab4.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab4.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan8.add(scr4, java.awt.BorderLayout.CENTER);

        pan5.add(pan8, java.awt.BorderLayout.CENTER);

        pan1.add(pan5, java.awt.BorderLayout.EAST);

        center.add(pan1, java.awt.BorderLayout.NORTH);

        pan2.setPreferredSize(new java.awt.Dimension(800, 302));
        pan2.setLayout(new java.awt.BorderLayout());

        pan6.setPreferredSize(new java.awt.Dimension(800, 300));
        pan6.setLayout(new java.awt.BorderLayout());

        pan9.setLayout(new java.awt.BorderLayout());

        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbStateChanged(evt);
            }
        });

        scr2a.setBorder(null);
        scr2a.setPreferredSize(new java.awt.Dimension(500, 200));

        tab2a.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "xxxxxxxxx", "11", "11", null},
                {"22", "vvvvvvvvv", "22", "22", null}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2a.setFillsViewportHeight(true);
        tab2a.setName("tab2a"); // NOI18N
        tab2a.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2a.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2a.setViewportView(tab2a);
        if (tab2a.getColumnModel().getColumnCount() > 0) {
            tab2a.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        tabb1.addTab("Детализация (1 уровень)", scr2a);

        scr2b.setBorder(null);
        scr2b.setPreferredSize(new java.awt.Dimension(500, 200));

        tab2b.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "xxxxxxxxx", "11", "11", null},
                {"22", "vvvvvvvvv", "22", "22", null}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор", "ID"
            }
        ));
        tab2b.setFillsViewportHeight(true);
        tab2b.setName("tab2b"); // NOI18N
        tab2b.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2b.setViewportView(tab2b);
        if (tab2b.getColumnModel().getColumnCount() > 0) {
            tab2b.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        tabb1.addTab("Детализация (2 уровень)", scr2b);

        scr2c.setBorder(null);
        scr2c.setPreferredSize(new java.awt.Dimension(500, 200));

        tab2c.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "xxxxxxxxx", "11", "11", null},
                {"22", "vvvvvvvvv", "22", "22", null}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор", "ID"
            }
        ));
        tab2c.setFillsViewportHeight(true);
        tab2c.setName("tab2c"); // NOI18N
        tab2c.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2c.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2c.setViewportView(tab2c);
        if (tab2c.getColumnModel().getColumnCount() > 0) {
            tab2c.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        tabb1.addTab("Детализация (3 уровень)", scr2c);

        pan9.add(tabb1, java.awt.BorderLayout.CENTER);

        pan6.add(pan9, java.awt.BorderLayout.CENTER);

        pan10.setPreferredSize(new java.awt.Dimension(300, 266));
        pan10.setLayout(new java.awt.BorderLayout());

        scr6.setBorder(null);
        scr6.setPreferredSize(new java.awt.Dimension(300, 200));

        tab6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"wwwwwwwwwww", "11"},
                {"sssssssssssssssss", "22"}
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
        tab6.setFillsViewportHeight(true);
        tab6.setName("tab6"); // NOI18N
        tab6.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr6.setViewportView(tab6);
        if (tab6.getColumnModel().getColumnCount() > 0) {
            tab6.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab6.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan10.add(scr6, java.awt.BorderLayout.CENTER);

        scr5.setBorder(null);
        scr5.setPreferredSize(new java.awt.Dimension(454, 108));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxxxxx", "0", "2600", "0", "360"},
                {"zzzzzzzzz", "0", "2600", "0", "360"}
            },
            new String [] {
                "Сторона", "Мин. длина", "Макс. длина", "Мин. угол", "Макс. угол"
            }
        ));
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
            tab5.getColumnModel().getColumn(0).setPreferredWidth(180);
        }

        pan10.add(scr5, java.awt.BorderLayout.NORTH);

        pan6.add(pan10, java.awt.BorderLayout.EAST);

        pan2.add(pan6, java.awt.BorderLayout.CENTER);

        center.add(pan2, java.awt.BorderLayout.CENTER);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

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
        txtFilter.setMaximumSize(new java.awt.Dimension(80, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(80, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(80, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFilter(evt);
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
        Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(tab1, this, tab2a, tab2b, tab2c, tab3) == 0) {
                Util.deleteRecord(tab1, eFurniture.up);
            }
        } else if (tab2a.getBorder() != null) {
            if (Util.isDeleteRecord(tab2a, this, tab2b, tab2c, tab5, tab6) == 0) {
                Util.deleteRecord(tab2a, eFurndet.up);
            }
        } else if (tab2b.getBorder() != null) {
            if (Util.isDeleteRecord(tab2b, this, tab2c, tab5, tab6) == 0) {
                Util.deleteRecord(tab2b, eFurndet.up);
            }
        } else if (tab2c.getBorder() != null) {
            if (Util.isDeleteRecord(tab2c, this, tab5, tab6) == 0) {
                Util.deleteRecord(tab2c, eFurndet.up);
            }
        } else if (tab3.getBorder() != null) {
            if (Util.isDeleteRecord(tab3, this, tab4) == 0) {
                Util.deleteRecord(tab3, eFurnside1.up);
            }
        } else if (tab4.getBorder() != null) {
            if (Util.isDeleteRecord(tab4, this) == 0) {
                Util.deleteRecord(tab4, eFurnpar1.up);
            }
        } else if (tab5.getBorder() != null) {
            if (Util.isDeleteRecord(tab5, this) == 0) {
                Util.deleteRecord(tab5, eFurnside2.up);
            }
        } else if (tab6.getBorder() != null) {
            if (Util.isDeleteRecord(tab6, this) == 0) {
                Util.deleteRecord(tab6, eFurnpar2.up);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab2b.getBorder() != null && Util.getSelectedRec(tab2a) == -1) {
            JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            return;
        } else if (tab2c.getBorder() != null && Util.getSelectedRec(tab2b) == -1) {
            JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            return;
        }

        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, eFurniture.up);

        } else if (tab2a.getBorder() != null) {
            Record record = Util.insertRecord(tab1, tab2a, eFurniture.up, eFurndet.up, eFurndet.furniture_id1);
            record.set(eFurndet.furndet_id, record.getInt(eFurndet.id));

        } else if (tab2b.getBorder() != null) {
            Record record = Util.insertRecord(tab1, tab2b, eFurniture.up, eFurndet.up, eFurndet.furniture_id1);
            record.set(eFurndet.furndet_id, qFurndet1.get(Util.getSelectedRec(tab2a), eFurndet.id));

        } else if (tab2c.getBorder() != null) {
            Record record = Util.insertRecord(tab1, tab2c, eFurniture.up, eFurndet.up, eFurndet.furniture_id1);
            record.set(eFurndet.furndet_id, qFurndet2.get(Util.getSelectedRec(tab2b), eFurndet.id));

        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab1, tab3, eFurniture.up, eFurnside1.up, eFurnside1.furniture_id);

        } else if (tab4.getBorder() != null) {
            Record record = Util.insertRecord(tab3, tab4, eFurnside1.up, eFurnpar1.up, eFurnpar1.furnside_id);

        } else if (tab5.getBorder() != null) {
            if (tabb1.getSelectedIndex() == 0) {
                Util.insertRecord(tab2a, tab5, eFurndet.up, eFurnside2.up, eFurnside2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 1) {
                Util.insertRecord(tab2b, tab5, eFurndet.up, eFurnside2.up, eFurnside2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 2) {
                Util.insertRecord(tab2c, tab5, eFurndet.up, eFurnside2.up, eFurnside2.furndet_id);
            }
        } else if (tab6.getBorder() != null) {
            if (tabb1.getSelectedIndex() == 0) {
                Util.insertRecord(tab2a, tab6, eFurndet.up, eFurnpar2.up, eFurnpar2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 1) {
                Util.insertRecord(tab2b, tab6, eFurndet.up, eFurnpar2.up, eFurnpar2.furndet_id);
            } else if (tabb1.getSelectedIndex() == 2) {
                Util.insertRecord(tab2c, tab6, eFurndet.up, eFurnpar2.up, eFurnpar2.furndet_id);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void tabbStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbStateChanged
        Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);

        if (tabb1.getSelectedIndex() == 0) {
            Util.listenerClick(tab2a, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
            selectionTab2a(null);

        } else if (tabb1.getSelectedIndex() == 1) {
            Util.listenerClick(tab2b, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
            selectionTab2b(null);

        } else if (tabb1.getSelectedIndex() == 2) {
            Util.listenerClick(tab2c, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
            selectionTab2c(null);
        }
    }//GEN-LAST:event_tabbStateChanged

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void txtFilter(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilter
        JTable table = Stream.of(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_txtFilter

    private void checkBoxAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAction
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_checkBoxAction
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox checkBox1;
    private javax.swing.JCheckBox checkBox2;
    private javax.swing.JCheckBox checkBox3;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.ButtonGroup grour1;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2a;
    private javax.swing.JScrollPane scr2b;
    private javax.swing.JScrollPane scr2c;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2a;
    private javax.swing.JTable tab2b;
    private javax.swing.JTable tab2c;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab6;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        labFilter.setText(tab1.getColumnName(0));
        txtFilter.setName(tab1.getName());
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Список фурнитуры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Описание сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Ограничения сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
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
        tab3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab3(event);
                }
            }
        });
        Util.getAllComponents(this.center).forEach(c -> {
            if (c instanceof JPanel) {
                ((JPanel) c).setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 1, 1, 1));
            }
        });
    }
}
