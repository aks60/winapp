package frames;

import frames.dialog.DicArtikl;
import dataset.Conn;
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
import domain.eElempar1;
import domain.eElempar2;
import domain.eGroups;
import domain.eJoindet;
import builder.param.ParamList;
import common.eProp;
import enums.TypeGroups;
import enums.TypeSet;
import enums.UseColor;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefCellBoolRenderer;
import frames.swing.DefTableModel;
import frames.swing.FilterTable;
import java.util.Set;
import java.util.stream.Collectors;
import startup.App;
import common.listener.ListenerRecord;
import common.listener.ListenerFrame;
import domain.eSysprof;
import domain.eSystree;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.table.DefaultTableColumnModel;
import report.ExecuteCmd;
import report.HtmlOfTable;

public class Elements extends javax.swing.JFrame {

    private Query qGrMap = new Query(eGroups.values());
    private Query qGrSeri = new Query(eGroups.values());
    private Query qGrCateg = new Query(eGroups.values());
    private Query qParams = new Query(eParams.values());
    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qElement = new Query(eElement.values(), eArtikl.values());
    private Query qElemdet = new Query(eElemdet.values(), eArtikl.values());
    private Query qElempar1 = new Query(eElempar1.values());
    private Query qElempar2 = new Query(eElempar2.values());
    private FilterTable filterTable = null;
    private ListenerRecord listenerArtikl, listenerTypset, listenerSeries, listenerColor, listenerColvar1, listenerColvar2, listenerColvar3;
    private String subsql = "(-1)";

    public Elements() {
        this.subsql = null;
        initComponents();
        initElements();
        listenerSet();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    public Elements(Set<Object> keys) {
        if (keys.isEmpty() == false) {
            this.subsql = keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        }
        initComponents();
        initElements();
        listenerSet();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    public Elements(Set<Object> keys, int deteilID) {
        if (keys.isEmpty() == false) {
            this.subsql = keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        }
        initComponents();
        initElements();
        listenerSet();
        loadingData();
        loadingModel();
        listenerAdd();
        deteilFind(deteilID);
    }

    public void loadingData() {

        qColor.select(eColor.up);
        qParams.select(eParams.up, "where", eParams.elem, "= 1 and", eParams.id, "=", eParams.params_id, "order by", eParams.text);
        qGrSeri.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.SERI_PROF.id);
        qGrMap.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.COLMAP.id);
        qGrCateg.select(eGroups.up, "where", eGroups.grup, "=", TypeGroups.CATEG_VST.id, "order by", eGroups.npp, ",", eGroups.name);
        Record record = eGroups.up.newRecord(Query.SEL);
        record.setNo(eGroups.id, -1);
        record.setNo(eGroups.npp, 1);
        record.setNo(eGroups.name, "<html><font size='3' color='red'>&nbsp;&nbsp;&nbsp;ПРОФИЛИ</font>");
        qGrCateg.add(0, record);
        for (int index = 0; index < qGrCateg.size(); ++index) {
            int level = qGrCateg.getAs(index, eGroups.npp);
            if (level == 5) {
                Record record2 = eGroups.up.newRecord(Query.SEL);
                record2.setNo(eGroups.id, -5);
                record2.setNo(eGroups.npp, 5);
                record2.setNo(eGroups.name, "<html><font size='3' color='red'>&nbsp;&nbsp;ЗАПОЛНЕНИЯ</font>");
                qGrCateg.add(index, record2);
                break;
            }
        }
    }

    public void loadingModel() {

        tab1.getTableHeader().setEnabled(false);
        new DefTableModel(tab1, qGrCateg, eGroups.name);
        new DefTableModel(tab2, qElement, eArtikl.code, eArtikl.name, eElement.name, eElement.typset, eElement.signset, eElement.series_id, eElement.todef, eElement.toset, eElement.markup) {

            public Object getValueAt(int col, int row, Object val) {

                if (val != null && columns[col] == eElement.typset) {
                    int typset = Integer.valueOf(val.toString());
                    return List.of(TypeSet.values()).stream().filter(el -> el.id == typset).findFirst().orElse(TypeSet.P1).name;

                } else if (val != null && columns[col] == eElement.series_id) {
                    return qGrSeri.stream().filter(rec -> rec.getInt(eGroups.id) == Integer.valueOf(val.toString())).findFirst().orElse(eElement.up.newRecord()).get(eGroups.name);
                }
                return val;
            }
        };
        new DefTableModel(tab3, qElemdet, eArtikl.code, eArtikl.name, eElemdet.color_fk, eElemdet.types, eElemdet.types, eElemdet.types) {

            public Object getValueAt(int col, int row, Object val) {

                if (val != null) {
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
                            return "# " + qGrMap.stream().filter(rec -> rec.getInt(eGroups.id) == -1 * colorFk).findFirst().orElse(eGroups.up.newRecord()).get(eGroups.name);
                        }
                    } else if (eElemdet.types == field) {
                        int types = Integer.valueOf(val.toString());
                        types = (col == 3) ? types & 0x0000000f : (col == 4) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8;
                        return UseColor.MANUAL.find(types).text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab4, qElempar1, eElempar1.params_id, eElempar1.text) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eElempar1.params_id == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (eProp.dev) ? val + ":" + record.getStr(eParams.text) : record.getStr(eParams.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (eProp.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qElempar2, eElempar2.params_id, eElempar2.text) {

            public Object getValueAt(int col, int row, Object val) {
                if (val != null) {
                    Field field = columns[col];
                    if (field == eElempar2.params_id) {
                        if (Integer.valueOf(String.valueOf(val)) < 0) {
                            Record record = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                            return (eProp.dev) ? val + ":" + record.getStr(eParams.text) : record.getStr(eParams.text);
                        } else {
                            Enam en = ParamList.find(Integer.valueOf(val.toString()));
                            return (eProp.dev) ? en.numb() + "-" + en.text() : en.text();
                        }
                    }
                }
                return val;
            }
        };

        DefCellBoolRenderer br = new DefCellBoolRenderer();
        List.of(6, 7).forEach(index -> tab2.getColumnModel().getColumn(index).setCellRenderer(br));

        UGui.setSelectedRow(tab1);
    }

    public void listenerAdd() {
        UGui.buttonCellEditor(tab2, 0).addActionListener(event -> {
            int level = qGrCateg.getAs(UGui.getIndexRec(tab1), eGroups.npp);
            DicArtikl frame = new DicArtikl(this, listenerArtikl, level);
        });

        UGui.buttonCellEditor(tab2, 1).addActionListener(event -> {
            int level = qGrCateg.getAs(UGui.getIndexRec(tab1), eGroups.npp);
            DicArtikl frame = new DicArtikl(this, listenerArtikl, level);
        });

        UGui.buttonCellEditor(tab2, 3).addActionListener(event -> {
            DicTypset frame = new DicTypset(this, listenerTypset);
        });

        UGui.buttonCellEditor(tab2, 5).addActionListener(event -> {
            int index = UGui.getIndexRec(tab2);
            if (index != -1) {
                int id = qElement.getAs(index, eElement.series_id);
                new DicGroups(this, listenerSeries, TypeGroups.SERI_PROF, id);
            }
        });

        UGui.buttonCellEditor(tab3, 0).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4, 5);
        });

        UGui.buttonCellEditor(tab3, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4, 5);
        });

        UGui.buttonCellEditor(tab3, 2).addActionListener(event -> {
            Record record = qElemdet.get(UGui.getIndexRec(tab3));
            int artikl_id = record.getInt(eElemdet.artikl_id);
            ParColor2 frame = new ParColor2(this, listenerColor, artikl_id);
        });

        UGui.buttonCellEditor(tab3, 3).addActionListener(event -> {
            Record record = qElemdet.get(UGui.getIndexRec(tab3));
            int colorFk = record.getInt(eElemdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar1, colorFk);
        });

        UGui.buttonCellEditor(tab3, 4).addActionListener(event -> {
            Record record = qElemdet.get(UGui.getIndexRec(tab3));
            int colorFk = record.getInt(eElemdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar2, colorFk);
        });

        UGui.buttonCellEditor(tab3, 5).addActionListener(event -> {
            Record record = qElemdet.get(UGui.getIndexRec(tab3));
            int colorFk = record.getInt(eElemdet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar3, colorFk);
        });

        UGui.buttonCellEditor(tab4, 0).addActionListener(event -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record record = qGrCateg.get(index);
                int paramPart = record.getInt(eGroups.npp);
                paramPart = (paramPart == 1) ? 31000 : 37000;
                ParGrup2 frame = new ParGrup2(this, (rec) -> {

                    UGui.listenerParam(rec, tab4, eElempar1.params_id, eElempar1.text, tab1, tab2, tab3, tab4, tab5);
                }, eParams.elem, paramPart);
            }
        });

        UGui.buttonCellEditor(tab4, 1, (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return UGui.listenerCell(tab4, component, eElempar1.params_id);

        }).addActionListener(event -> {
            Record record = qElempar1.get(UGui.getIndexRec(tab4));
            int grup = record.getInt(eElempar1.params_id);
            if (grup < 0) {
                new ParGrup2a(this, (rec) -> {
                    UGui.listenerParam(rec, tab4, eElempar1.params_id, eElempar1.text, tab1, tab2, tab3, tab4, tab5);
                }, grup);
            } else {
                List list = ParamList.find(grup).dict();
                new ParGrup2b(this, (rec) -> {
                    UGui.listenerParam(rec, tab4, eElempar1.params_id, eElempar1.text, tab1, tab2, tab3, tab4, tab5);
                }, list);
            }
        });

        UGui.buttonCellEditor(tab5, 0).addActionListener(event -> {
            int index = UGui.getIndexRec(tab3);
            if (index != -1) {
                int levelGrp = qGrCateg.getAs(UGui.getIndexRec(tab1), eGroups.npp);
                Record recordDet = qElemdet.get(index);
                int artikl_id = recordDet.getInt(eJoindet.artikl_id);
                Record recordArt = eArtikl.find(artikl_id, false);
                int level = recordArt.getInt(eArtikl.level1);
                Integer[] part1 = {0, 34000, 33000, 34000, 33000, 40000, 0};
                Integer[] part2 = {0, 39000, 38000, 39000, 38000, 40000, 0};
                int grup = (levelGrp == 1) ? part1[level] : part2[level];
                ParGrup2 frame = new ParGrup2(this, (rec) -> {
                    UGui.listenerParam(rec, tab5, eElempar2.params_id, eElempar2.text, tab1, tab2, tab3, tab4, tab5);
                }, eParams.elem, grup);
            }
        });

        UGui.buttonCellEditor(tab5, 1, (component) -> { //слушатель редактирование типа и вида данных и вида ячейки таблицы
            return UGui.listenerCell(tab5, component, eElempar2.params_id);

        }).addActionListener(event -> {
            Record record = qElempar2.get(UGui.getIndexRec(tab5));
            int grup = record.getInt(eElempar2.params_id);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, (rec) -> {
                    UGui.listenerParam(rec, tab5, eElempar2.params_id, eElempar2.text, tab1, tab2, tab3, tab4, tab5);
                }, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, (rec) -> {
                    UGui.listenerParam(rec, tab5, eElempar2.params_id, eElempar2.text, tab1, tab2, tab3, tab4, tab5);
                }, list);
            }
        });
    }

    public void listenerSet() {

        listenerTypset = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                qElement.set(record.getInt(0), UGui.getIndexRec(tab2), eElement.typset);
                UGui.fireTableRowUpdated(tab2);
            }
        };

        listenerArtikl = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                qElement.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab2), eElement.artikl_id);
                qElement.table(eArtikl.up).set(record.get(eArtikl.name), UGui.getIndexRec(tab2), eArtikl.name);
                qElement.table(eArtikl.up).set(record.get(eArtikl.code), UGui.getIndexRec(tab2), eArtikl.code);
                UGui.fireTableRowUpdated(tab2);

            } else if (tab3.getBorder() != null) {
                qElemdet.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab3), eElemdet.artikl_id);
                qElemdet.table(eArtikl.up).set(record.get(eArtikl.name), UGui.getIndexRec(tab3), eArtikl.name);
                qElemdet.table(eArtikl.up).set(record.get(eArtikl.code), UGui.getIndexRec(tab3), eArtikl.code);
                UGui.fireTableRowUpdated(tab3);
            }
        };

        listenerSeries = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab2.getBorder() != null) {
                int series_id = record.getInt(eGroups.id);
                qElement.set(series_id, UGui.getIndexRec(tab2), eElement.series_id);
                UGui.fireTableRowUpdated(tab2);
            }
        };

        listenerColor = (record) -> {
            UGui.listenerColor(record, tab3, eElemdet.color_fk, eElemdet.types, tab1, tab2, tab3, tab4, tab5);
        };

        listenerColvar1 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            Record elemdetRec = qElemdet.get(UGui.getIndexRec(tab3));
            int types = (elemdetRec.getInt(eElemdet.types) == -1) ? 0 : elemdetRec.getInt(eElemdet.types);
            types = (types & 0xfffffff0) + record.getInt(0);
            elemdetRec.set(eElemdet.types, types);
            UGui.fireTableRowUpdated(tab3);
        };

        listenerColvar2 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            Record elemdetRec = qElemdet.get(UGui.getIndexRec(tab3));
            int types = (elemdetRec.getInt(eElemdet.types) == -1) ? 0 : elemdetRec.getInt(eElemdet.types);
            types = (types & 0xffffff0f) + (record.getInt(0) << 4);
            elemdetRec.set(eElemdet.types, types);
            UGui.fireTableRowUpdated(tab3);
        };

        listenerColvar3 = (record) -> {
            UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            Record elemdetRec = qElemdet.get(UGui.getIndexRec(tab3));
            int types = (elemdetRec.getInt(eElemdet.types) == -1) ? 0 : elemdetRec.getInt(eElemdet.types);
            types = (types & 0xfffff0ff) + (record.getInt(0) << 8);
            elemdetRec.set(eElemdet.types, types);
            UGui.fireTableRowUpdated(tab3);
        };
    }

    public void selectionTab1(ListSelectionEvent event) {
        UGui.clearTable(tab2, tab3, tab4, tab5);
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record record = qGrCateg.get(index);
            Integer id = record.getInt(eGroups.id);
            if (id == -1 || id == -5) {
                if (subsql == null) {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "left join", eGroups.up, "on", eGroups.id, "=", eElement.elemgrp_id, "where", eGroups.npp, "=", Math.abs(id));
                } else {
                    qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
                            "left join", eGroups.up, "on", eGroups.id, "=", eElement.elemgrp_id, "where", eGroups.npp, "=", Math.abs(id), "and", eElement.id, "in " + subsql);
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
            UGui.setSelectedRow(tab2);
        }
    }

    public void selectionTab2(ListSelectionEvent event) {
        UGui.clearTable(tab3, tab4, tab5);
        int index = UGui.getIndexRec(tab2);
        if (index != -1) {
            Record record = qElement.table(eElement.up).get(index);
            Integer p1 = record.getInt(eElement.id);
            qElemdet.select(eElemdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eElemdet.artikl_id, "where", eElemdet.element_id, "=", p1);
            qElempar1.select(eElempar1.up, "left join", eParams.up, "on", eParams.id, "=", eElempar1.params_id, "where", eElempar1.element_id, "=", p1);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab3);
            UGui.setSelectedRow(tab4);
        }
    }

    public void selectionTab3(ListSelectionEvent event) {
        int index = UGui.getIndexRec(tab3);
        if (index != -1) {
            //Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            List.of(qElempar2).forEach(q -> q.execsql());
            Record record = qElemdet.table(eElemdet.up).get(index);
            Integer p1 = record.getInt(eElemdet.id);
            qElempar2.select(eElempar2.up, "left join", eParams.up, "on", eParams.id, "=", eElempar2.params_id, "where", eElempar2.elemdet_id, "=", p1);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab5);
        }
    }

    public void deteilFind(int deteilID) {
        Query qDet = new Query(eElemdet.values(), eArtikl.values());
        for (int index = 0; index < qElement.size(); index++) {
            int element_id = qElement.get(index).getInt(eElement.id);
            qDet.select(eElemdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eElemdet.artikl_id, "where", eElemdet.element_id, "=", element_id);
            for (int index2 = 0; index2 < qDet.size(); index2++) {
                if (qDet.get(index2).getInt(eElemdet.id) == deteilID) {
                    UGui.setSelectedIndex(tab2, index);
                    UGui.scrollRectToRow(index, tab2);
                    UGui.setSelectedIndex(tab3, index2);
                    UGui.scrollRectToRow(index2, tab3);
                }
            }
        }
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
        btnFindArtikl = new javax.swing.JButton();
        btnTest = new javax.swing.JButton();
        btnFindSystree = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

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
                Elements.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(900, 29));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/hint/hint-ru"); // NOI18N
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

        btnIns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c033.gif"))); // NOI18N
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

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport.setToolTipText(bundle.getString("Закрыть")); // NOI18N
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

        btnFindArtikl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c055.gif"))); // NOI18N
        btnFindArtikl.setToolTipText(bundle.getString("Поиск записи")); // NOI18N
        btnFindArtikl.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFindArtikl.setFocusable(false);
        btnFindArtikl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFindArtikl.setMaximumSize(new java.awt.Dimension(25, 25));
        btnFindArtikl.setMinimumSize(new java.awt.Dimension(25, 25));
        btnFindArtikl.setPreferredSize(new java.awt.Dimension(25, 25));
        btnFindArtikl.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnFindArtikl.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFindArtikl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindArtikl(evt);
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

        btnFindSystree.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c014.gif"))); // NOI18N
        btnFindSystree.setToolTipText(bundle.getString("Поиск записи")); // NOI18N
        btnFindSystree.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFindSystree.setFocusable(false);
        btnFindSystree.setMaximumSize(new java.awt.Dimension(25, 25));
        btnFindSystree.setMinimumSize(new java.awt.Dimension(25, 25));
        btnFindSystree.setPreferredSize(new java.awt.Dimension(25, 25));
        btnFindSystree.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnFindSystree.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFindSystree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindSystree(evt);
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
                .addGap(18, 18, 18)
                .addComponent(btnFindArtikl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFindSystree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFindArtikl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFindSystree, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setPreferredSize(new java.awt.Dimension(758, 460));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(847, 320));
        pan1.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 0, 0, 0));
        scr2.setPreferredSize(new java.awt.Dimension(454, 320));

        tab2.setFont(frames.UGui.getFont(0,0));
        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "vvvvvvvvvvvvvvv", "111", null, null, null, null,  new Double(0.0), null},
                {null, null, "hhhhhhhhhhhhhh", "222", null, null, null, null,  new Double(0.0), null}
            },
            new String [] {
                "Артикул", "Название", "Наименование вставок", "Тип состава", "Признак состава", "Для серии", "Умолчание", "Обязательно", "Наценка%", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Double.class, java.lang.Object.class
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
                Elements.this.mousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(96);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(5).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(6).setPreferredWidth(32);
            tab2.getColumnModel().getColumn(6).setMaxWidth(80);
            tab2.getColumnModel().getColumn(7).setPreferredWidth(32);
            tab2.getColumnModel().getColumn(7).setMaxWidth(80);
            tab2.getColumnModel().getColumn(8).setPreferredWidth(32);
            tab2.getColumnModel().getColumn(8).setMaxWidth(80);
            tab2.getColumnModel().getColumn(9).setMaxWidth(40);
        }

        pan1.add(scr2, java.awt.BorderLayout.CENTER);

        scr4.setPreferredSize(new java.awt.Dimension(260, 320));

        tab4.setFont(frames.UGui.getFont(0,0));
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
                Elements.this.mousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab4.getColumnModel().getColumn(1).setMaxWidth(160);
        }

        pan1.add(scr4, java.awt.BorderLayout.EAST);

        scr1.setPreferredSize(new java.awt.Dimension(120, 404));

        tab1.setFont(frames.UGui.getFont(0,0));
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
                Elements.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);

        pan1.add(scr1, java.awt.BorderLayout.WEST);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        pan2.setLayout(new java.awt.BorderLayout());

        scr5.setPreferredSize(new java.awt.Dimension(260, 204));

        tab5.setFont(frames.UGui.getFont(0,0));
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
                Elements.this.mousePressed(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(1).setMaxWidth(160);
        }

        pan2.add(scr5, java.awt.BorderLayout.EAST);

        scr3.setPreferredSize(new java.awt.Dimension(454, 204));

        tab3.setFont(frames.UGui.getFont(0,0));
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
                Elements.this.mousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(96);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(180);
            tab3.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(3).setPreferredWidth(60);
            tab3.getColumnModel().getColumn(4).setPreferredWidth(60);
            tab3.getColumnModel().getColumn(5).setPreferredWidth(60);
            tab3.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        pan2.add(scr3, java.awt.BorderLayout.CENTER);

        centr.add(pan2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

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
        List.of(tab1, tab2, tab3, tab4, tab5).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(this, tab2) == 0) {
                UGui.deleteRecord(tab1);
            }
        } else if (tab2.getBorder() != null) {
            if (UGui.isDeleteRecord(this, tab3, tab4) == 0) {
                UGui.deleteRecord(tab2);
            }
        } else if (tab3.getBorder() != null) {
            if (UGui.isDeleteRecord(this, tab5) == 0) {
                UGui.deleteRecord(tab3);
            }
        } else if (tab4.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0) {
                UGui.deleteRecord(tab4);
            }
        } else if (tab5.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0) {
                UGui.deleteRecord(tab5);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            ppmCateg.show(north, btnIns.getX(), btnIns.getY() + 18);

        } else if (tab2.getBorder() != null) {
            UGui.insertRecordEnd(tab2, eElement.up, (record) -> {
                Record groupsRec = qGrCateg.get(UGui.getIndexRec(tab1));
                int id = groupsRec.getInt(eGroups.id);
                record.set(eElement.elemgrp_id, id);
                Record record2 = eArtikl.up.newRecord();
                qElement.table(eArtikl.up).add(record2);
            });
        } else if (tab3.getBorder() != null) {
            if (UGui.getIndexRec(tab2) != -1) {
                UGui.insertRecordEnd(tab3, eElemdet.up, (record) -> {
                    record.set(eElemdet.element_id, qElement.get(UGui.getIndexRec(tab2), eElement.id));
                    Record record2 = eArtikl.up.newRecord();
                    qElemdet.table(eArtikl.up).add(record2);
                });
            }
        } else if (tab4.getBorder() != null) {
            UGui.insertRecordEnd(tab4, eElempar1.up, (record) -> {
                int id = qElement.getAs(UGui.getIndexRec(tab2), eElement.id);
                record.set(eElempar1.element_id, id);
            });

        } else if (tab5.getBorder() != null) {
            UGui.insertRecordEnd(tab5, eElempar2.up, (record) -> {
                int id = qElemdet.getAs(UGui.getIndexRec(tab3), eElemdet.id);
                record.set(eElempar2.elemdet_id, id);
            });
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        List.of(tab1, tab2, tab3, tab4, tab5).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
    }//GEN-LAST:event_windowClosed

    private void ppmCategAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppmCategAction

        JMenuItem ppm = (JMenuItem) evt.getSource();
        int level1 = (ppm == itCateg1) ? 1 : 5;
        Object result = JOptionPane.showInputDialog(Elements.this, "Название", "Категория", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            Record elemgrpRec = eGroups.up.newRecord(Query.INS);
            int id = Conn.genId(eGroups.up);
            elemgrpRec.setNo(eGroups.id, id);
            elemgrpRec.setNo(eGroups.grup, TypeGroups.CATEG_VST.id);
            elemgrpRec.setNo(eGroups.npp, level1); //-1 -ПРОФИЛИ, -5 -ЗАПОЛНЕНИЯ
            elemgrpRec.setNo(eGroups.name, result);
            qGrCateg.insert(elemgrpRec);
            loadingData();
            for (int i = 0; i < qGrCateg.size(); ++i) {
                if (qGrCateg.get(i).getInt(eGroups.id) == id) {
                    UGui.setSelectedIndex(tab1, i - 1);
                    ((DefaultTableModel) tab1.getModel()).fireTableRowsInserted(i - 1, i - 1);
                    UGui.scrollRectToRow(i, tab1);
                    break;
                }
            }
        }
    }//GEN-LAST:event_ppmCategAction

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        HtmlOfTable.load("Вставки", tab2);
        ExecuteCmd.documentType(this);
    }//GEN-LAST:event_btnReport

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        UGui.updateBorderAndSql((JTable) evt.getSource(), List.of(tab1, tab2, tab3, tab4, tab5));
        filterTable.mousePressed((JTable) evt.getSource());
    }//GEN-LAST:event_mousePressed

    private void btnFindArtikl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindArtikl
        if (tab2.getBorder() != null) {
            Record record = ((DefTableModel) tab2.getModel()).getQuery().get(UGui.getIndexRec(tab2));
            if (record != null) {
                Record record2 = eArtikl.find(record.getInt(eElement.artikl_id), false);
                FrameProgress.create(this, new ListenerFrame() {
                    public void actionRequest(Object obj) {
                        App.Artikles.createFrame(Elements.this, record2);
                    }
                });
            }
        } else if (tab3.getBorder() != null) {
            Record record = ((DefTableModel) tab3.getModel()).getQuery().get(UGui.getIndexRec(tab3));
            if (record != null) {
                Record record2 = eArtikl.find(record.getInt(eElemdet.artikl_id), false);
                FrameProgress.create(this, new ListenerFrame() {
                    public void actionRequest(Object obj) {
                        App.Artikles.createFrame(Elements.this, record2);
                    }
                });
            }
        }
    }//GEN-LAST:event_btnFindArtikl

    private void btnTest(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest

    }//GEN-LAST:event_btnTest

    private void btnFindSystree(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindSystree
        int index = UGui.getIndexRec(tab2);
        Record record = qElement.get(index);
        List<Record> sysprofList1 = eSysprof.query().stream().filter(rec -> record.getInt(eElement.artikl_id) == rec.getInt(eSysprof.artikl_id)).collect(Collectors.toList());
        Set<Integer> sysprofList2 = new HashSet();
        sysprofList1.forEach(rec -> sysprofList2.add(rec.getInt(eSysprof.systree_id)));
        List<String> pathList = new ArrayList();
        List<Integer> keyList = new ArrayList();
        StringBuffer path = new StringBuffer();
        for (Record rec : eSystree.query()) {
            if (sysprofList2.contains(rec.get(eSystree.id))) {
                path = path.append(rec.getStr(eSystree.name));
                findPathSystree(rec, path);
                pathList.add(path.toString());
                keyList.add(rec.getInt(eSystree.id));
                path.delete(0, path.length());
            }
        }
        if (pathList.size() != 0) {
            for (int i = pathList.size(); i < 21; ++i) {
                pathList.add(null);
            }
            Object result = JOptionPane.showInputDialog(Elements.this, "Артикул в системе профилей", "Сообщение", JOptionPane.QUESTION_MESSAGE, null, pathList.toArray(), pathList.toArray()[0]);
            if (result != null || result instanceof Integer) {
                for (int i = 0; i < pathList.size(); ++i) {
                    if (result.equals(pathList.get(i))) {
                        Object id = keyList.get(i);
                        FrameProgress.create(Elements.this, new ListenerFrame() {
                            public void actionRequest(Object obj) {
                                App.Systree.createFrame(Elements.this, id);
                            }
                        });
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(Elements.this, "В системе профилей артикул не найден", "Сообщение", JOptionPane.NO_OPTION);
        }
    }//GEN-LAST:event_btnFindSystree
    private void findPathSystree(Record record, StringBuffer path) {
        for (Record rec : eSystree.query()) {
            if (record.getInt(eSystree.parent_id) == rec.getInt(eSystree.id)) {
                path.insert(0, rec.getStr(eSystree.name) + "->");
                if (rec.getInt(eSystree.id) != rec.getInt(eSystree.parent_id)) {
                    findPathSystree(rec, path); //рекурсия
                }
            }
        }
    }

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnFindArtikl;
    private javax.swing.JButton btnFindSystree;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnTest;
    private javax.swing.JPanel centr;
    private javax.swing.JMenuItem itCateg1;
    private javax.swing.JMenuItem itCtag2;
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
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        filterTable = new FilterTable(0, tab2);
        south.add(filterTable, 0);
        filterTable.getTxt().grabFocus();

        List.of(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "                 ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки вставок", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация вставок", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
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
        for (int i : List.of(3, 6, 7)) {
            ((DefaultTableColumnModel) tab2.getColumnModel()).getColumn(i).setMinWidth(0);
            ((DefaultTableColumnModel) tab2.getColumnModel()).getColumn(i).setMaxWidth(0);
        }
    }
}
