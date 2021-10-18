package frames;

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
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import dataset.Field;
import domain.eGroups;
import frames.dialog.DicArtikl;
import frames.dialog.DicColvar;
import frames.dialog.DicEnums;
import frames.dialog.ParColor2;
import frames.dialog.ParGrup2;
import frames.dialog.ParGrup2b;
import frames.dialog.ParGrup2a;
import domain.eParams;
import enums.Enam;
import builder.param.ParamList;
import enums.LayoutFurn1;
import enums.UseFurn3;
import enums.LayoutFurn3;
import enums.TypeArtikl;
import enums.TypeGroups;
import enums.UseColor;
import enums.UseFurn1;
import enums.UseFurn2;
import frames.swing.DefCellBoolRenderer;
import frames.swing.FilterTable;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.RowFilter;
import startup.Main;
import startup.App;
import javax.swing.JOptionPane;
import frames.swing.listener.ListenerRecord;
import frames.swing.listener.ListenerFrame;

public class Furniturs extends javax.swing.JFrame {

    private Query qGroups = new Query(eGroups.values());
    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qParams = new Query(eParams.values());
    private Query qFurnall = new Query(eFurniture.values());
    private Query qFurniture = new Query(eFurniture.values());
    private Query qArtikl = new Query(eArtikl.values());
    private Query qFurndet2a = new Query(eFurndet.values());
    private Query qFurndet2b = new Query(eFurndet.values());
    private Query qFurndet2c = new Query(eFurndet.values());
    private Query qFurnside1 = new Query(eFurnside1.values());
    private Query qFurnside2 = new Query(eFurnside2.values());
    private Query qFurnpar1 = new Query(eFurnpar1.values());
    private Query qFurnpar2 = new Query(eFurnpar2.values());
    private FilterTable filterTable = new FilterTable();
    private ListenerRecord listenerArtikl, listenerPar1, listenerPar2, listenerTypset, listenerColor,
            listenerColvar, listenerSide1, listenerSide2, listenerSide3, listenerSide4, listenerVariant1, listenerVariant2;
    private String subsql = "(-1)";
    private JTable tab2 = null; //активная таблица спецификации

    public Furniturs() {
        this.subsql = null;
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        listenerAdd();
        listenerSet();
    }

    public Furniturs(Set<Object> keys) {
        if (keys.isEmpty() == false) {
            this.subsql = (keys.isEmpty()) ? "(-1)" : keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        }
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        listenerAdd();
        listenerSet();
    }

    public Furniturs(Set<Object> keys, int deteilID) {
        if (keys.isEmpty() == false) {
            this.subsql = (keys.isEmpty()) ? "(-1)" : keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        }
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        listenerAdd();
        listenerSet();
        selectionRows(deteilID);
    }

    public void loadingData() {
        tab2 = tab2a;
        qColor.select(eColor.up);
        qArtikl.select(eArtikl.up);
        qFurnall.select(eFurniture.up, "order by", eFurniture.name);
        qParams.select(eParams.up, "where", eParams.id, "=", eParams.params_id, "order by", eParams.text);
        qGroups.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.COLMAP.id);
        int types = (tbtn1.isSelected()) ? 0 : (tbtn2.isSelected()) ? 1 : -1;
        if (subsql == null) {
            qFurniture.select(eFurniture.up, "where", eFurniture.types, "=", types, "order by", eFurniture.name);
        } else {
            qFurniture.select(eFurniture.up, "where", eFurniture.id, "in", subsql, "and", eFurniture.types, "=", types, "order by", eFurniture.name);
        }
    }

    public void loadingModel() {
        new DefTableModel(tab1, qFurniture, eFurniture.name, eFurniture.view_open, eFurniture.hand_side, eFurniture.hand_set1, eFurniture.hand_set2, eFurniture.hand_set3, eFurniture.p2_max,
                eFurniture.max_width, eFurniture.max_height, eFurniture.max_weight, eFurniture.ways_use, eFurniture.pars, eFurniture.coord_lim, eFurniture.id) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurniture.view_open == field) {
                    int fk = Integer.valueOf(val.toString());
                    if (UseFurn1.P1.find(fk) != null) {
                        return UseFurn1.P1.find(fk).text();
                    }
                } else if (val != null && eFurniture.hand_side == field) {
                    int fk = Integer.valueOf(val.toString());
                    if (LayoutFurn1.BOTT.find(fk) != null) {
                        return LayoutFurn1.BOTT.find(fk).text();
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
        new DefTableModel(tab2a, qFurndet2a, eFurndet.artikl_id, eFurndet.artikl_id, eFurndet.color_fk, eFurndet.types, eFurndet.id) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurndet.color_fk == field) {
                    int colorFk = Integer.valueOf(val.toString());
                    if (Integer.valueOf(UseColor.automatic[0]) == colorFk) {
                        return UseColor.automatic[1];
                    } else if (Integer.valueOf(UseColor.precision[0]) == colorFk) {
                        return UseColor.precision[1];
                    }
                    if (colorFk > 0) {
                        return qColor.stream().filter(rec -> rec.getInt(eColor.id) == colorFk).findFirst().orElse(eColor.up.newRecord()).get(eColor.name);
                    } else {
                        return "# " + qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == -1 * colorFk).findFirst().orElse(eGroups.up.newRecord()).get(eGroups.name);
                    }

                } else if (val != null && eFurndet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    types = types & 0x0000000f;
                    return UseColor.MANUAL.find(types).text();

                } else if (eFurndet.artikl_id == field) {
                    if (qFurndet2a.get(row, eFurndet.furniture_id2) != null) {
                        int furniture_id2 = qFurndet2a.getAs(row, eFurndet.furniture_id2);
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
        new DefTableModel(tab2b, qFurndet2b, eFurndet.artikl_id, eFurndet.artikl_id, eFurndet.color_fk, eFurndet.types, eFurndet.id) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurndet.color_fk == field) {
                    int colorFk = Integer.valueOf(val.toString());
                    if (Integer.valueOf(UseColor.automatic[0]) == colorFk) {
                        return UseColor.automatic[1];
                    } else if (Integer.valueOf(UseColor.precision[0]) == colorFk) {
                        return UseColor.precision[1];
                    }
                    if (colorFk > 0) {
                        return qColor.stream().filter(rec -> rec.getInt(eColor.id) == colorFk).findFirst().orElse(eColor.up.newRecord()).get(eColor.name);
                    } else {
                        return "# " + qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == -1 * colorFk).findFirst().orElse(eGroups.up.newRecord()).get(eGroups.name);
                    }

                } else if (val != null && eFurndet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    types = types & 0x0000000f;
                    return UseColor.MANUAL.find(types).text();

                } else if (eFurndet.artikl_id == field) {
                    if (qFurndet2b.get(row, eFurndet.furniture_id2) != null) {
                        int furniture_id2 = qFurndet2b.getAs(row, eFurndet.furniture_id2);
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
        new DefTableModel(tab2c, qFurndet2c, eFurndet.artikl_id, eFurndet.artikl_id, eFurndet.color_fk, eFurndet.types, eFurndet.id) {

            public Object getValueAt(int col, int row, Object val) {

                Field field = columns[col];
                if (val != null && eFurndet.color_fk == field) {
                    int colorFk = Integer.valueOf(val.toString());
                    if (Integer.valueOf(UseColor.automatic[0]) == colorFk) {
                        return UseColor.automatic[1];
                    } else if (Integer.valueOf(UseColor.precision[0]) == colorFk) {
                        return UseColor.precision[1];
                    }
                    if (colorFk > 0) {
                        return qColor.stream().filter(rec -> rec.getInt(eColor.id) == colorFk).findFirst().orElse(eColor.up.newRecord()).get(eColor.name);
                    } else {
                        return "# " + qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == -1 * colorFk).findFirst().orElse(eGroups.up.newRecord()).get(eGroups.name);
                    }

                } else if (val != null && eFurndet.types == field) {
                    int types = Integer.valueOf(val.toString());
                    types = types & 0x0000000f;
                    return UseColor.MANUAL.find(types).text();

                } else if (eFurndet.artikl_id == field) {
                    if (qFurndet2c.get(row, eFurndet.furniture_id2) != null) {
                        int furniture_id2 = qFurndet2c.getAs(row, eFurndet.furniture_id2);
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
        new DefTableModel(tab4, qFurnpar1, eFurnpar1.params_id, eFurnpar1.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eFurnpar1.params_id && val != null) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? val + ":" + record.getStr(eParams.text) : record.getStr(eParams.text);
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
                        return Stream.of(LayoutFurn3.values()).filter(en -> en.id == v).findFirst().get().name;  //orElse(null).name;
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab6, qFurnpar2, eFurnpar2.params_id, eFurnpar2.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && field == eFurnpar2.params_id) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? val + ":" + record.getStr(eParams.text) : record.getStr(eParams.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };

        tab1.getColumnModel().getColumn(3).setCellRenderer(new DefCellBoolRenderer());
        tab1.getColumnModel().getColumn(4).setCellRenderer(new DefCellBoolRenderer());
        tab1.getColumnModel().getColumn(5).setCellRenderer(new DefCellBoolRenderer());

        UGui.setSelectedRow(tab1);
    }

    public void listenerAdd() {
        UGui.buttonCellEditor(tab1, 1).addActionListener(event -> {
            new DicEnums(this, listenerVariant1, UseFurn1.values());
        });

        UGui.buttonCellEditor(tab1, 2).addActionListener(event -> {
            new DicEnums(this, listenerSide4, LayoutFurn1.values());
        });

        UGui.buttonCellEditor(tab1, 10).addActionListener(event -> {
            new DicEnums(this, listenerVariant2, UseFurn2.values());
        });

        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            UGui.buttonCellEditor(tab, 0).addActionListener(event -> {
                new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
            });
        }
        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            UGui.buttonCellEditor(tab, 1).addActionListener(event -> {
                new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
            });
        }

        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            Query query = (tab == tab2a) ? qFurndet2a : (tab == tab2b) ? qFurndet2b : qFurndet2c;
            UGui.buttonCellEditor(tab, 2).addActionListener(event -> {
                Record record = query.get(UGui.getIndexRec(tab));
                int artikl_id = record.getInt(eFurndet.artikl_id);
                ParColor2 frame = new ParColor2(this, listenerColor, artikl_id);
            });
        }
        for (JTable tab : Arrays.asList(tab2a, tab2b, tab2c)) {
            Query query = (tab == tab2a) ? qFurndet2a : (tab == tab2b) ? qFurndet2b : qFurndet2c;
            UGui.buttonCellEditor(tab, 3).addActionListener(event -> {
                Record record = query.get(UGui.getIndexRec(tab));
                int colorFk = record.getInt(eFurndet.color_fk);
                DicColvar frame = new DicColvar(this, listenerColvar, colorFk);
            });
        }

        UGui.buttonCellEditor(tab3, 0).addActionListener(event -> {
            new DicEnums(this, listenerSide1, LayoutFurn1.values());
        });

        UGui.buttonCellEditor(tab3, 1).addActionListener(event -> {
            new DicEnums(this, listenerSide2, UseFurn3.values());
        });

        UGui.buttonCellEditor(tab4, 0).addActionListener(event -> {
            ParGrup2 frame = new ParGrup2(this, listenerPar1, eParams.joint, 21000);
        });

        UGui.buttonCellEditor(tab4, 1, (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return UGui.listenerCell(tab4, component, eFurnpar1.params_id);

        }).addActionListener(event -> {
            Record record = qFurnpar1.get(UGui.getIndexRec(tab4));
            int grup = record.getInt(eFurnpar1.params_id);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar1, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar1, list);
            }
        });

        UGui.buttonCellEditor(tab5, 0).addActionListener(event -> {
            new DicEnums(this, listenerSide3, LayoutFurn3.values());
        });

        UGui.buttonCellEditor(tab6, 0).addActionListener(event -> {
            int index = tabb1.getSelectedIndex();
            JTable table = (index == 0) ? tab2a : (index == 1) ? tab2b : tab2c;
            int index2 = UGui.getIndexRec(table);
            if (index2 != -1) {
                Query query = (index == 0) ? qFurndet2a : (index == 1) ? qFurndet2b : qFurndet2c;
                Record furndetRec = query.get(index2);
                int artikl_id = furndetRec.getInt(eFurndet.artikl_id);
                Record recordArt = eArtikl.find(artikl_id, false);
                int level = (recordArt.getInt(eArtikl.level1) == -1) ? 0 : recordArt.getInt(eArtikl.level1);
                Integer[] part = {0, 25000, 24000, 25000, 24000, 0};
                ParGrup2 frame = new ParGrup2(this, listenerPar2, eParams.furn, part[level]);
            }
        });

        UGui.buttonCellEditor(tab6, 1, (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return UGui.listenerCell(tab6, component, eFurnpar2.params_id);

        }).addActionListener(event -> {
            Record record = qFurnpar2.get(UGui.getIndexRec(tab6));
            int grup = record.getInt(eFurnpar2.params_id);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar2, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar2, list);
            }
        });
    }

    public void listenerSet() {

        listenerArtikl = (record) -> {
            UGui.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Query query = (tab2a.getBorder() != null) ? qFurndet2a : (tab2b.getBorder() != null) ? qFurndet2b : qFurndet2c;
            if (tab.getBorder() != null) {
                int index = UGui.getIndexRec(tab);
                query.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab), eFurndet.artikl_id);
                ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
                UGui.setSelectedIndex(tab, index);
            }
        };

        listenerColor = (record) -> {
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            UGui.listenerColor(record, tab, eFurndet.color_fk, eFurndet.types, tab1, tab2a, tab2b, tab2c, tab6, tab3, tab4);
        };

        listenerColvar = (record) -> {
            JTable tab = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Query query = (tab2a.getBorder() != null) ? qFurndet2a : (tab2b.getBorder() != null) ? qFurndet2b : qFurndet2c;
            UGui.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
            int index = UGui.getIndexRec(tab);
            Record furndetRec = query.get(UGui.getIndexRec(tab));
            int types = (furndetRec.getInt(eFurndet.types) == -1) ? 0 : furndetRec.getInt(eFurndet.types);
            types = (types & 0xfffffff0) + record.getInt(0);
            furndetRec.set(eFurndet.types, types);
            ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
            UGui.setSelectedIndex(tab, index);
        };

        listenerSide1 = (record) -> {
            UGui.listenerEnums(record, tab3, eFurnside1.side_num, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerSide2 = (record) -> {
            UGui.listenerEnums(record, tab3, eFurnside1.side_use, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerSide3 = (record) -> {
            UGui.listenerEnums(record, tab5, eFurnside2.side_num, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerSide4 = (record) -> {
            UGui.listenerEnums(record, tab1, eFurniture.hand_side, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerPar1 = (record) -> {
            UGui.listenerParam(record, tab4, eFurnpar1.params_id, eFurnpar1.text, tab1, tab2a, tab2b, tab2c, tab6, tab3, tab4);
        };

        listenerPar2 = (record) -> {
            UGui.listenerParam(record, tab6, eFurnpar2.params_id, eFurnpar2.text, tab1, tab2a, tab2b, tab2c, tab6, tab3, tab4);
        };

        listenerVariant1 = (record) -> {
            UGui.listenerEnums(record, tab1, eFurniture.view_open, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };

        listenerVariant2 = (record) -> {
            UGui.listenerEnums(record, tab1, eFurniture.ways_use, tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        };
    }

    public void selectionTab1(ListSelectionEvent event) {
        UGui.clearTable(tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record record = qFurniture.table(eFurniture.up).get(index);
            Integer id = record.getInt(eFurniture.id);
            qFurnside1.select(eFurnside1.up, "where", eFurnside1.furniture_id, "=", id, "order by", eFurnside1.side_num);
            qFurndet2a.select(eFurndet.up, "where", eFurndet.furniture_id1, "=", id, "and", eFurndet.furndet_id, "=", eFurndet.id);
            ((DefaultTableModel) tab2a.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab3);
            UGui.setSelectedRow(tab2a);
        }
    }

    public void selectionTab2a(ListSelectionEvent event) {
        UGui.clearTable(tab2b, tab2c, tab5, tab6);
        int index = UGui.getIndexRec(tab2a);
        if (index != -1) {
            Record record = qFurndet2a.get(index);
            int id = record.getInt(eFurndet.id);
            qFurndet2b.select(eFurndet.up, "where", eFurndet.furndet_id, "=", id, "and", eFurndet.id, "!=", eFurndet.furndet_id);
            ((DefaultTableModel) tab2b.getModel()).fireTableDataChanged();
            qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.id);
            qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
            ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
        }
    }

    public void selectionTab2b(ListSelectionEvent event) {
        UGui.clearTable(tab2c, tab5, tab6);
        int index = UGui.getIndexRec(tab2b);
        if (index != -1) {
            Record record = qFurndet2b.get(index);
            Integer id = record.getInt(eFurndet.id);
            qFurndet2c.select(eFurndet.up, "where", eFurndet.furndet_id, "=", id);
            qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.id);
            qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
            ((DefaultTableModel) tab2c.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
        }
    }

    public void selectionTab2c(ListSelectionEvent event) {
        UGui.clearTable(tab5, tab6);
        int index = UGui.getIndexRec(tab2c);
        if (index != -1) {
            Record record = qFurndet2c.get(index);
            Integer id = record.getInt(eFurndet.id);
            qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.id);
            qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
            ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
        }
    }

    public void selectionTab3(ListSelectionEvent event) {
        UGui.clearTable(tab4);
        int index = UGui.getIndexRec(tab3);
        if (index != -1) {
            Record record = qFurnside1.table(eFurnside1.up).get(index);
            Integer id = record.getInt(eFurnside1.id);
            qFurnpar1.select(eFurnpar1.up, "where", eFurnpar1.furnside_id, "=", id, "order by", eFurnpar1.id);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
        }
    }

    public void selectionRows(Query qFurn, Query qDet2a, Query qDet2b, Query qDet2c, int iTabb, int iFurn, int iDet2a, int iDet2b, int iDet2c) {

        if (qFurn.get(iFurn).getInt(eFurniture.types) == 0) {
            tbtn1.setSelected(true);
        } else if (qFurn.get(iFurn).getInt(eFurniture.types) == 1) {
            tbtn2.setSelected(true);
        } else {
            tbtn3.setSelected(true);
        }
        tbtnAction(null);
        
//        UGui.setSelectedIndex(tab1, iFurn);
//        UGui.scrollRectToRow(iFurn, tab1);
//        UGui.setSelectedIndex(tab2a, iDet2a);
//        UGui.setSelectedIndex(tab2b, iDet2b);
//        UGui.setSelectedIndex(tab2c, iDet2c);
//        tabb1.setSelectedIndex(iTabb);
//        if (iTabb == 0) {
//            UGui.scrollRectToRow(iDet2a, tab2a);
//        } else if (iTabb == 1) {
//            UGui.scrollRectToRow(iDet2b, tab2b);
//        } else {
//            UGui.scrollRectToRow(iDet2c, tab2c);
//        }
    }

    public void selectionRows(int deteilID) {
        Query qFurn = new Query(eFurniture.values());
        Query qDet2a = new Query(eFurndet.values(), eArtikl.values());
        Query qDet2b = new Query(eFurndet.values(), eArtikl.values());
        Query qDet2c = new Query(eFurndet.values(), eArtikl.values());
        try {
            for (int index0 : Arrays.asList(0, 1, -1)) {
                qFurn.select(eFurniture.up, "where", eFurniture.id, "in", subsql, "and", eFurniture.types, "=", index0, "order by", eFurniture.name);
                for (int index1 = 0; index1 < qFurn.size(); index1++) {
                    int id = qFurn.get(index1).getInt(eFurniture.id);
                    qDet2a.select(eFurndet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eFurndet.artikl_id, "where", eFurndet.furniture_id1, "=", id, "and", eFurndet.furndet_id, "=", eFurndet.id);
                    for (int index2 = 0; index2 < qDet2a.size(); index2++) {
                        if (qDet2a.get(index2).getInt(eFurndet.id) == deteilID) {
                            selectionRows(qFurn, qDet2a, qDet2b, qDet2c, 0, index1, index2, 0, 0);
                            return;
                        } else {
                            id = qDet2a.get(index2).getInt(eFurndet.id);
                            qDet2b.select(eFurndet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eFurndet.artikl_id, "where", eFurndet.furndet_id, "=", id, "and", eFurndet.id, "!=", eFurndet.furndet_id);
                            for (int index3 = 0; index3 < qDet2b.size(); index3++) {
                                if (qDet2b.get(index3).getInt(eFurndet.id) == deteilID) {
                                    selectionRows(qFurn, qDet2a, qDet2b, qDet2c, 1, index1, index2, index3, 0);
                                    return;
                                } else {
                                    id = qDet2b.get(index3).getInt(eFurndet.id);
                                    qDet2c.select(eFurndet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eFurndet.artikl_id, "where", eFurndet.furndet_id, "=", id);
                                    for (int index4 = 0; index4 < qDet2c.size(); index4++) {
                                        if (qDet2c.get(index4).getInt(eFurndet.id) == deteilID) {
                                            selectionRows(qFurn, qDet2a, qDet2b, qDet2c, 2, index1, index2, index3, index4);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка:Furniturs.deteilFind()");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        group1 = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnIns = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnConstructiv = new javax.swing.JButton();
        tbtn1 = new javax.swing.JToggleButton();
        tbtn2 = new javax.swing.JToggleButton();
        tbtn3 = new javax.swing.JToggleButton();
        btnSet = new javax.swing.JButton();
        btnTest = new javax.swing.JButton();
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
        pan11 = new javax.swing.JPanel();
        scr2a = new javax.swing.JScrollPane();
        tab2a = new javax.swing.JTable();
        scr2c = new javax.swing.JScrollPane();
        tab2c = new javax.swing.JTable();
        scr2b = new javax.swing.JScrollPane();
        tab2b = new javax.swing.JTable();
        pan10 = new javax.swing.JPanel();
        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Фурнитура");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Furniturs.this.windowClosed(evt);
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
        btnIns.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
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
        btnDel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
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
        btnRef.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
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
        btnReport.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
            }
        });

        btnConstructiv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c055.gif"))); // NOI18N
        btnConstructiv.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnConstructiv.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnConstructiv.setFocusable(false);
        btnConstructiv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConstructiv.setMaximumSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setMinimumSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setPreferredSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnConstructiv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConstructiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructiv(evt);
            }
        });

        group1.add(tbtn1);
        tbtn1.setSelected(true);
        tbtn1.setText("Основн.");
        tbtn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tbtn1.setPreferredSize(new java.awt.Dimension(60, 25));
        tbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtnAction(evt);
            }
        });

        group1.add(tbtn2);
        tbtn2.setText("Дополн.");
        tbtn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tbtn2.setPreferredSize(new java.awt.Dimension(60, 25));
        tbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtnAction(evt);
            }
        });

        group1.add(tbtn3);
        tbtn3.setText("Наборы");
        tbtn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tbtn3.setPreferredSize(new java.awt.Dimension(60, 25));
        tbtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtnAction(evt);
            }
        });

        btnSet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c040.gif"))); // NOI18N
        btnSet.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSet.setPreferredSize(new java.awt.Dimension(16, 25));
        btnSet.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInser2(evt);
            }
        });

        btnTest.setText("Test");
        btnTest.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTest.setMaximumSize(new java.awt.Dimension(25, 25));
        btnTest.setMinimumSize(new java.awt.Dimension(25, 25));
        btnTest.setPreferredSize(new java.awt.Dimension(25, 25));
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConstructiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tbtn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 342, Short.MAX_VALUE)
                .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConstructiv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnIns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tbtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tbtn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tbtn2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(btnSet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setPreferredSize(new java.awt.Dimension(900, 500));
        center.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(800, 200));
        pan1.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(500, 200));
        pan4.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"333333333333333", "3", "3", null, null, null, "3", null, null, null, null, null, null, null},
                {"444444444444444", "3", "4", null, null, null, "5", null, null, null, null, null, null, null}
            },
            new String [] {
                "Название", "Вид", "Сторона ручки", "По середине", "Константная", "Вариационная", "Р/2 максимальная", "Ширина максимальная", "Высота максимальная", "Вес максимальный", "Створка", "Использ. параметры", "Ограничения", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
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
                Furniturs.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(400);
            tab1.getColumnModel().getColumn(13).setMaxWidth(40);
        }

        pan4.add(scr1, java.awt.BorderLayout.CENTER);

        pan1.add(pan4, java.awt.BorderLayout.CENTER);

        pan5.setPreferredSize(new java.awt.Dimension(300, 200));
        pan5.setLayout(new java.awt.BorderLayout());

        pan7.setPreferredSize(new java.awt.Dimension(300, 108));
        pan7.setLayout(new java.awt.BorderLayout());

        scr3.setPreferredSize(new java.awt.Dimension(0, 0));

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
                Furniturs.this.mousePressed(evt);
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
                Furniturs.this.mousePressed(evt);
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

        pan2.setPreferredSize(new java.awt.Dimension(800, 300));
        pan2.setLayout(new java.awt.BorderLayout());

        pan6.setPreferredSize(new java.awt.Dimension(800, 300));
        pan6.setLayout(new java.awt.BorderLayout());

        pan9.setPreferredSize(new java.awt.Dimension(505, 300));
        pan9.setLayout(new java.awt.BorderLayout());

        tabb1.setPreferredSize(new java.awt.Dimension(0, 0));

        pan11.setLayout(new java.awt.BorderLayout());

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
                Furniturs.this.mousePressed(evt);
            }
        });
        scr2a.setViewportView(tab2a);
        if (tab2a.getColumnModel().getColumnCount() > 0) {
            tab2a.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab2a.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab2a.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        pan11.add(scr2a, java.awt.BorderLayout.CENTER);

        tabb1.addTab("Детализация (1 уровень)", pan11);

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
                Furniturs.this.mousePressed(evt);
            }
        });
        scr2c.setViewportView(tab2c);
        if (tab2c.getColumnModel().getColumnCount() > 0) {
            tab2c.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab2c.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab2c.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        tabb1.addTab("Детализация (3 уровень)", scr2c);

        pan9.add(tabb1, java.awt.BorderLayout.CENTER);

        scr2b.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Детализация (2 уровень)"));
        scr2b.setPreferredSize(new java.awt.Dimension(500, 140));

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
                Furniturs.this.mousePressed(evt);
            }
        });
        scr2b.setViewportView(tab2b);
        if (tab2b.getColumnModel().getColumnCount() > 0) {
            tab2b.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab2b.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab2b.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        pan9.add(scr2b, java.awt.BorderLayout.SOUTH);

        pan6.add(pan9, java.awt.BorderLayout.CENTER);

        pan10.setPreferredSize(new java.awt.Dimension(300, 300));
        pan10.setLayout(new java.awt.BorderLayout());

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
                Furniturs.this.mousePressed(evt);
            }
        });
        scr6.setViewportView(tab6);
        if (tab6.getColumnModel().getColumnCount() > 0) {
            tab6.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab6.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan10.add(scr6, java.awt.BorderLayout.CENTER);

        scr5.setPreferredSize(new java.awt.Dimension(300, 108));

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
                Furniturs.this.mousePressed(evt);
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
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(tab1, this, tab2a, tab2b, tab2c, tab3) == 0) {
                UGui.deleteRecord(tab1);
            }
        } else if (tab2a.getBorder() != null) {
            if (UGui.isDeleteRecord(tab2a, this, tab2b, tab2c, tab5, tab6) == 0) {
                UGui.deleteRecord(tab2a);
            }
        } else if (tab2b.getBorder() != null) {
            if (UGui.isDeleteRecord(tab2b, this, tab2c, tab5, tab6) == 0) {
                UGui.deleteRecord(tab2b);
            }
        } else if (tab2c.getBorder() != null) {
            if (UGui.isDeleteRecord(tab2c, this, tab5, tab6) == 0) {
                UGui.deleteRecord(tab2c);
            }
        } else if (tab3.getBorder() != null) {
            if (UGui.isDeleteRecord(tab3, this, tab4) == 0) {
                UGui.deleteRecord(tab3);
            }
        } else if (tab4.getBorder() != null) {
            if (UGui.isDeleteRecord(tab4, this) == 0) {
                UGui.deleteRecord(tab4);
            }
        } else if (tab5.getBorder() != null) {
            if (UGui.isDeleteRecord(tab5, this) == 0) {
                UGui.deleteRecord(tab5);
            }
        } else if (tab6.getBorder() != null) {
            if (UGui.isDeleteRecord(tab6, this) == 0) {
                UGui.deleteRecord(tab6);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            UGui.insertRecord(tab1, eFurniture.up, (record) -> {
                int types = (tbtn1.isSelected()) ? 0 : (tbtn2.isSelected()) ? 1 : -1;
                record.set(eFurniture.types, types);
                record.set(eFurniture.ways_use, UseFurn2.P2.id);
            });

        } else if (tab2a.getBorder() != null && tab5.getBorder() == null && tab6.getBorder() == null) {
            if (UGui.getIndexRec(tab1) != -1) {
                UGui.insertRecord(tab2a, eFurndet.up, (record) -> {
                    int id = qFurniture.getAs(UGui.getIndexRec(tab1), eFurniture.id);
                    record.set(eFurndet.furniture_id1, id);
                    record.set(eFurndet.furndet_id, record.getInt(eFurndet.id));
                });
            } else {
                JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            }

        } else if (tab2b.getBorder() != null && tab5.getBorder() == null && tab6.getBorder() == null) {
            if (UGui.getIndexRec(tab1) != -1 && UGui.getIndexRec(tab2a) != -1) {
                UGui.insertRecord(tab2b, eFurndet.up, (record) -> {
                    int id1 = qFurniture.getAs(UGui.getIndexRec(tab1), eFurniture.id);
                    int id2 = qFurndet2a.getAs(UGui.getIndexRec(tab2a), eFurndet.id);
                    record.set(eFurndet.furniture_id1, id1);
                    record.set(eFurndet.furndet_id, id2);
                });
            } else {
                JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            }

        } else if (tab2c.getBorder() != null && tab5.getBorder() == null && tab6.getBorder() == null) {
            if (UGui.getIndexRec(tab1) != -1 && UGui.getIndexRec(tab2b) != -1) {
                UGui.insertRecord(tab2c, eFurndet.up, (record) -> {
                    int id1 = qFurniture.getAs(UGui.getIndexRec(tab1), eFurniture.id);
                    int id2 = qFurndet2b.getAs(UGui.getIndexRec(tab2b), eFurndet.id);
                    record.set(eFurndet.furniture_id1, id1);
                    record.set(eFurndet.furndet_id, id2);
                });
            } else {
                JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            }

        } else if (tab3.getBorder() != null) {
            UGui.insertRecord(tab3, eFurnside1.up, (record) -> {
                int id = qFurniture.getAs(UGui.getIndexRec(tab1), eFurniture.id);
                record.set(eFurnside1.furniture_id, id);
            });

        } else if (tab4.getBorder() != null) {
            UGui.insertRecord(tab4, eFurnpar1.up, (record) -> {
                int id = qFurnside1.getAs(UGui.getIndexRec(tab3), eFurnside1.id);
                record.set(eFurnpar1.furnside_id, id);
            });

        } else if (tab5.getBorder() != null) {
            JTable table = (tabb1.getSelectedIndex() == 0) ? tab2a : (tabb1.getSelectedIndex() == 1) ? tab2c : tab2b;
            Query query = ((DefTableModel) table.getModel()).getQuery();
            if (UGui.getIndexRec(table) != -1) {
                UGui.insertRecord(tab5, eFurnside2.up, (record) -> {
                    int id = query.getAs(UGui.getIndexRec(table), eFurndet.id);
                    record.set(eFurnside2.furndet_id, id);
                });
            }
        } else if (tab6.getBorder() != null) {
            //JTable tab2 = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
            Object name = tab2.getName();
            Query query = ((DefTableModel) tab2.getModel()).getQuery();
            if (UGui.getIndexRec(tab2) != -1) {
                UGui.insertRecord(tab6, eFurnpar2.up, (record) -> {
                    int id = query.getAs(UGui.getIndexRec(tab2), eFurndet.id);
                    record.set(eFurnpar2.furndet_id, id);
                });
            }
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);
        Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
    }//GEN-LAST:event_windowClosed

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        btnSet.setVisible(!btnSet.isVisible());
    }//GEN-LAST:event_btnReport

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
        if (table == tab2a) {
            tab2 = tab2a;
            selectionTab2a(null);
        } else if (table == tab2b) {
            tab2 = tab2b;
            selectionTab2b(null);
        } else if (table == tab2c) {
            tab2 = tab2c;
            selectionTab2c(null);
        }
        filterTable.mousePressed((JTable) evt.getSource());
    }//GEN-LAST:event_mousePressed

    private void btnConstructiv(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConstructiv
        JTable table = (tab2a.getBorder() != null) ? tab2a : (tab2b.getBorder() != null) ? tab2b : tab2c;
        Record record = ((DefTableModel) table.getModel()).getQuery().get(UGui.getIndexRec(table));
        Record record2 = qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == record.getInt(eFurndet.artikl_id)).findFirst().orElse(eFurndet.up.newRecord());
        FrameProgress.create(this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Artikles.createFrame(Furniturs.this, record2);
            }
        });
    }//GEN-LAST:event_btnConstructiv

    private void tbtnAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnAction
        JTable table = null;
        if (tab2a.getBorder() != null && tab5.getBorder() == null && tab6.getBorder() == null) {
            table = tab2a;
        } else if (tab2b.getBorder() != null && tab5.getBorder() == null && tab6.getBorder() == null) {
            table = tab2b;
        } else if (tab2c.getBorder() != null && tab5.getBorder() == null && tab6.getBorder() == null) {
            table = tab2c;
        }
        int index = UGui.getIndexRec(table);
        Integer furndetID2 = (index == -1) ? null : ((DefTableModel) table.getModel()).getQuery().getAs(index, eFurndet.furniture_id2);

        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
        if (furndetID2 != null && tbtn3.isSelected()) {
            for (int index2 = 0; index2 < qFurniture.size(); ++index2) {
                Record record = qFurniture.get(index2);
                if (record.getInt(eFurniture.id) == furndetID2) {
                    UGui.setSelectedIndex(tab1, index2);
                    Rectangle cellRect = tab1.getCellRect(index2, 0, false);
                    tab1.scrollRectToVisible(cellRect);
                }
            }
        }
    }//GEN-LAST:event_tbtnAction

    private void btnInser2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInser2
        if (tab2a.getBorder() != null && tab5.getBorder() == null && tab6.getBorder() == null) {
            if (UGui.getIndexRec(tab1) != -1) {
                List list = new LinkedList();
                for (Record record : qFurnall) {
                    if (record.getInt(eFurniture.types) == -1) {
                        list.add(record.getStr(eFurniture.name));
                    }
                }
                Object result = JOptionPane.showInputDialog(Furniturs.this, "Выбор набора",
                        "Наборы", JOptionPane.QUESTION_MESSAGE, null, list.toArray(), list.toArray()[0]);

                if (result != null) {
                    for (Record record2 : qFurnall) {
                        if (result.equals(record2.get(eFurniture.name))) {

                            UGui.insertRecord(tab2a, eFurndet.up, (record) -> {
                                int id = qFurniture.getAs(UGui.getIndexRec(tab1), eFurniture.id);
                                record.set(eFurndet.furniture_id1, id);
                                record.set(eFurndet.furniture_id2, record2.getInt(eFurniture.id));
                                record.set(eFurndet.furndet_id, record.getInt(eFurndet.id));
                                record.set(eFurndet.color_fk, 0);
                                record.set(eFurndet.types, 1);
                            });
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            }
        }
    }//GEN-LAST:event_btnInser2

    private void btnTest(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTest
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConstructiv;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnSet;
    private javax.swing.JButton btnTest;
    private javax.swing.JPanel center;
    private javax.swing.ButtonGroup group1;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan11;
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
    private javax.swing.JToggleButton tbtn1;
    private javax.swing.JToggleButton tbtn2;
    private javax.swing.JToggleButton tbtn3;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        south.add(filterTable, 0);
        filterTable.getTxt().grabFocus();

        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> UGui.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Список фурнитуры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Описание сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Ограничения сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
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
        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                UGui.stopCellEditing(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6);

                if (tabb1.getSelectedIndex() == 0) {
                    UGui.updateBorderAndSql(tab2a, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
                    selectionTab2a(null);

                } else if (tabb1.getSelectedIndex() == 1) {
                    UGui.updateBorderAndSql(tab2b, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
                    selectionTab2b(null);

                } else if (tabb1.getSelectedIndex() == 2) {
                    UGui.updateBorderAndSql(tab2c, Arrays.asList(tab1, tab2a, tab2b, tab2c, tab3, tab4, tab5, tab6));
                    selectionTab2c(null);
                }
            }
        });
    }
}
