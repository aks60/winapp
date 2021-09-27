package frames;

import com.google.gson.Gson;
import common.eProperty;
import dataset.Conn;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import frames.dialog.DicArtikl;
import frames.dialog.DicEnums;
import frames.dialog.DicName;
import frames.dialog.ParDefault;
import domain.eArtikl;
import domain.eFurniture;
import domain.eParams;
import domain.eSysfurn;
import domain.eSyspar1;
import domain.eSysprod;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutProduct;
import enums.LayoutHandle;
import enums.TypeArtikl;
import enums.UseSide;
import enums.TypeOpen2;
import enums.UseArtiklTo;
import enums.TypeUse;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import frames.swing.DefCellBoolRenderer;
import frames.swing.DefFieldEditor;
import frames.swing.DefTableModel;
import builder.Wincalc;
import builder.making.Furniture;
import builder.model.AreaSimple;
import builder.model.AreaStvorka;
import builder.model.ElemJoining;
import builder.model.ElemSimple;
import builder.script.GsonElem;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.UCom;
import domain.eArtdet;
import domain.eColor;
import domain.eFurndet;
import enums.Layout;
import enums.PKjson;
import enums.TypeOpen1;
import frames.dialog.DicColor;
import frames.dialog.DicHandl;
import frames.dialog.DicSysprof;
import frames.swing.Canvas;
import frames.swing.DefMutableTreeNode;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import startup.Main;
import startup.App;
import frames.swing.listener.ListenerRecord;
import frames.swing.listener.ListenerFrame;
import common.eProfile;
import domain.eJoining;
import builder.making.Joining;
import builder.making.UColor;
import domain.eJoinvar;
import enums.TypeJoin;
import frames.dialog.DicJoinvar;
import frames.swing.DrawScene;
import frames.swing.FilterTable;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.swing.JButton;

public class Systree extends javax.swing.JFrame {

    private DrawScene scene = new DrawScene();
    private Canvas canvas = new Canvas();
    private int systreeID = -1; //выбранная система
    private ListenerRecord listenerArtikl, listenerModel, listenerFurn,
            listenerParam1, listenerParam2, listenerParam3, listenerArt211, listenerArt212;

    private Query qParams = new Query(eParams.values());
    private Query qArtikl = new Query(eArtikl.id, eArtikl.code, eArtikl.name);
    private Query qSystree = new Query(eSystree.values());
    private Query qSysprod = new Query(eSysprod.values());
    private Query qSysprof = new Query(eSysprof.values(), eArtikl.values());
    private Query qSysfurn = new Query(eSysfurn.values(), eFurniture.values());
    private Query qSyspar1 = new Query(eSyspar1.values());
    private Query qSyspar2 = new Query(eSyspar1.values());

    private FilterTable filterTable = new FilterTable();
    private DefFieldEditor rsvSystree;
    private java.awt.Frame models = null;
    private DefMutableTreeNode sysNode = null;
    private DefMutableTreeNode winNode = null;
    private TreeNode[] selectedPath = null;
    private Gson gson = new GsonBuilder().create();

    public Systree() {
        initComponents();
        initElements();
        loadingData();
        loadingSys();
        loadingModel();
        listenerAdd();
        listenerSet();
    }

    public Systree(int artiklID) {
        initComponents();
        initElements();
        loadingData();
        loadingSys();
        loadingModel();
        listenerAdd();
        listenerSet();
        selectionTab2(artiklID);
    }

    private void loadingData() {
        //Получим сохр. ID системы при выходе из программы
        Record sysprodRec = eSysprod.find(Integer.valueOf(eProperty.sysprodID.read()));
        if (sysprodRec != null) {
            systreeID = sysprodRec.getInt(eSysprod.systree_id);
        }
        qSystree.select(eSystree.up, "order by id");
        qParams.select(eParams.up);
        qArtikl.select(eArtikl.up, "where", eArtikl.level1, "= 2 and", eArtikl.level2, "in (11,12)");
    }

    private void loadingSys() {
        Record recordRoot = eSystree.up.newRecord(Query.SEL);
        recordRoot.set(eSystree.id, -1);
        recordRoot.set(eSystree.parent_id, -1);
        recordRoot.set(eSystree.name, "Дерево системы профилей");
        DefMutableTreeNode rootTree = new DefMutableTreeNode(recordRoot);
        ArrayList<DefMutableTreeNode> treeList = new ArrayList();

        for (Record record : qSystree) {
            if (record.getInt(eSystree.parent_id) == record.getInt(eSystree.id)) {
                DefMutableTreeNode node2 = new DefMutableTreeNode(record);
                treeList.add(node2);
                rootTree.add(node2);
            }
        }
        ArrayList<DefMutableTreeNode> treeList2 = addChild(treeList, new ArrayList());
        ArrayList<DefMutableTreeNode> treeList3 = addChild(treeList2, new ArrayList());
        ArrayList<DefMutableTreeNode> treeList4 = addChild(treeList3, new ArrayList());
        ArrayList<DefMutableTreeNode> treeList5 = addChild(treeList4, new ArrayList());
        ArrayList<DefMutableTreeNode> treeList6 = addChild(treeList5, new ArrayList());
        sysTree.setModel(new DefaultTreeModel(rootTree));
        scr1.setViewportView(sysTree);
    }

    private void loadingModel() {
        ((DefaultTreeCellEditor) sysTree.getCellEditor()).addCellEditorListener(new CellEditorListener() {

            public void editingStopped(ChangeEvent e) {
                String str = ((DefaultTreeCellEditor) sysTree.getCellEditor()).getCellEditorValue().toString();
                sysNode.rec().set(eSystree.name, str);
                sysNode.setUserObject(str);
                txt8.setText(str);
                qSystree.update(sysNode.rec()); //сохраним в базе
            }

            public void editingCanceled(ChangeEvent e) {
                editingStopped(e);
            }
        });
        new DefTableModel(tab2, qSysprof, eSysprof.use_type, eSysprof.use_side, eArtikl.code, eArtikl.name, eSysprof.prio) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eSysprof.use_side && val != null) {
                    UseSide en = UseSide.get(Integer.valueOf(val.toString()));
                    if (en != null) {
                        return en.text();
                    }
                } else if (field == eSysprof.use_type && val != null) {
                    UseArtiklTo en = UseArtiklTo.get(Integer.valueOf(val.toString()));
                    if (en != null) {
                        return en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab3, qSysfurn, eSysfurn.npp, eFurniture.name, eSysfurn.side_open, eSysfurn.replac, eSysfurn.hand_pos, eSysfurn.artikl_id1, eSysfurn.artikl_id2) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null) {
                    if (field == eSysfurn.side_open) {
                        int id = Integer.valueOf(val.toString());
                        return Arrays.asList(TypeOpen2.values()).stream().filter(el -> el.id == id).findFirst().orElse(TypeOpen2.QUE).name;
                    } else if (field == eSysfurn.hand_pos) {
                        int id = Integer.valueOf(val.toString());
                        return Arrays.asList(LayoutHandle.values()).stream().filter(el -> el.id == id).findFirst().orElse(LayoutHandle.MIDL).name;
                    } else if (field == eSysfurn.artikl_id1) {
                        int id = Integer.valueOf(val.toString());
                        return qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == id).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.code);
                    } else if (field == eSysfurn.artikl_id2) {
                        int id = Integer.valueOf(val.toString());
                        return qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == id).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.code);
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab4, qSyspar1, eSyspar1.params_id, eSyspar1.text, eSyspar1.fixed) {
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && field == eSyspar1.params_id) {
                    if (Main.dev == true) {
                        return val + "   " + qParams.stream().filter(rec -> (rec.get(eParams.id).equals(val)
                                && rec.getInt(eParams.id) == rec.getInt(eParams.params_id))).findFirst().orElse(eParams.up.newRecord(Query.SEL)).getStr(eParams.text);
                    } else {
                        return qParams.stream().filter(rec -> (rec.get(eParams.id).equals(val)
                                && rec.getInt(eParams.id) == rec.getInt(eParams.params_id))).findFirst().orElse(eParams.up.newRecord(Query.SEL)).getStr(eParams.text);
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qSysprod, eSysprod.name, eSysprod.id);
        new DefTableModel(tab7, qSyspar2, eSyspar1.params_id, eSyspar1.text) {
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && field == eSyspar1.params_id) {
                    if (Main.dev == true) {
                        return val + "   " + qParams.stream().filter(rec -> (rec.get(eParams.id).equals(val)
                                && rec.getInt(eParams.id) == rec.getInt(eParams.params_id))).findFirst().orElse(eParams.up.newRecord(Query.SEL)).getStr(eParams.text);
                    } else {
                        return qParams.stream().filter(rec -> (rec.get(eParams.id).equals(val)
                                && rec.getInt(eParams.id) == rec.getInt(eParams.params_id))).findFirst().orElse(eParams.up.newRecord(Query.SEL)).getStr(eParams.text);
                    }
                }
                return val;
            }
        };

        tab4.getColumnModel().getColumn(2).setCellRenderer(new DefCellBoolRenderer());
        tab5.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1) {
                    Object v = qSysprod.get(row).get(eSysprod.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setIcon(null);
                }
                return label;
            }
        });

        rsvSystree = new DefFieldEditor(sysTree) {

            public Set<JTextField> set = new HashSet();

            public void setText(JTextField jtf, String str) {
                set.add(jtf);
                jtf.setText(str);
            }

            @Override
            public void load() {
                super.load();
                int typesID = sysNode.rec().getInt(eSystree.types);
                int imgviewID = sysNode.rec().getInt(eSystree.imgview);
                TypeUse typeUse = Stream.of(TypeUse.values()).filter(en -> en.numb() == typesID).findFirst().orElse(TypeUse.EMPTY);
                LayoutProduct layoutProduct = Stream.of(LayoutProduct.values()).filter(en -> en.numb() == imgviewID).findFirst().orElse(LayoutProduct.P1);
                setText(txt7, typeUse.name);
                setText(txt11, layoutProduct.name);
            }

            @Override
            public void clear() {
                super.clear();
                set.forEach(s -> s.setText(null));
            }
        };
        rsvSystree.add(eSystree.name, txt8);
        rsvSystree.add(eSystree.glas, txt1);
        rsvSystree.add(eSystree.depth, txt2);
        rsvSystree.add(eSystree.col1, txt3);
        rsvSystree.add(eSystree.col2, txt4);
        rsvSystree.add(eSystree.col3, txt5);
        rsvSystree.add(eSystree.pref, txt10);
        rsvSystree.add(eSystree.cgrp, txt15);
        rsvSystree.add(eSystree.coef, txt35);

        panDesign.add(scene, java.awt.BorderLayout.CENTER);
        scene.add(canvas, java.awt.BorderLayout.CENTER);
        canvas.setVisible(true);
        if (selectedPath != null) {
            sysTree.setSelectionPath(new TreePath(selectedPath));
        } else {
            sysTree.setSelectionRow(0);
        }
    }

    private void loadingWin(Wincalc iwin) {
        try {
            int row[] = winTree.getSelectionRows();
            DefMutableTreeNode root = UGui.loadWinTree(iwin);
            winTree.setModel(new DefaultTreeModel(root));
            winTree.setSelectionRows(row);

        } catch (Exception e) {
            System.err.println("Ошибка: Systree.loadingWin() " + e);
        }
    }

    private void loadingTab5() {
        qSysprod.select(eSysprod.up, "where", eSysprod.systree_id, "=", systreeID);
        DefaultTableModel dm = (DefaultTableModel) tab5.getModel();
        dm.getDataVector().removeAllElements();
        for (Record record : qSysprod.table(eSysprod.up)) {
            try {
                String script = record.getStr(eSysprod.script);
                Wincalc iwin2 = new Wincalc(script);
                //iwin2.correction();
                Canvas.createIcon(iwin2, 68);
                record.add(iwin2);

            } catch (Exception e) {
                System.err.println("Ошибка:Systree.loadingTab5() " + e);
            }
        }
        ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
    }

    private void listenerAdd() {
        UGui.buttonCellEditor(tab2, 0).addActionListener(event -> {
            new DicEnums(this, (record) -> {
                UGui.listenerEnums(record, tab2, eSysprof.use_type, tab2, tab3, tab4);
            }, UseArtiklTo.values());
        });

        UGui.buttonCellEditor(tab2, 1).addActionListener(event -> {
            new DicEnums(this, (record) -> {
                UGui.listenerEnums(record, tab2, eSysprof.use_side, tab2, tab3, tab4);
            }, UseSide.values());
        });

        UGui.buttonCellEditor(tab2, 2).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        UGui.buttonCellEditor(tab2, 3).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        UGui.buttonCellEditor(tab3, 1).addActionListener(event -> {
            DicName frame = new DicName(this, listenerFurn, new Query(eFurniture.values()).select(eFurniture.up, "order by", eFurniture.name), eFurniture.name);
        });

        UGui.buttonCellEditor(tab3, 2).addActionListener(event -> {
            DicEnums frame = new DicEnums(this, (record) -> {
                UGui.listenerEnums(record, tab3, eSysfurn.side_open, tab2, tab3, tab4, tab5);
            }, TypeOpen2.values());
        });

        UGui.buttonCellEditor(tab3, 4).addActionListener(event -> {
            DicEnums frame = new DicEnums(this, (record) -> {
                UGui.listenerEnums(record, tab3, eSysfurn.hand_pos, tab2, tab3, tab4, tab5);
            }, LayoutHandle.values());
        });

        UGui.buttonCellEditor(tab3, 5).addActionListener(event -> {
            int furnityreId = qSysfurn.getAs(UGui.getIndexRec(tab3), eSysfurn.furniture_id);
            DicArtikl artikl = new DicArtikl(this, listenerArt211, furnityreId, TypeArtikl.X211.id1, TypeArtikl.X211.id2);
        });

        UGui.buttonCellEditor(tab3, 6).addActionListener(event -> {
            int furnityreId = qSysfurn.getAs(UGui.getIndexRec(tab3), eSysfurn.furniture_id);
            DicArtikl artikl = new DicArtikl(this, listenerArt212, furnityreId, TypeArtikl.X212.id1, TypeArtikl.X212.id2);
        });

        UGui.buttonCellEditor(tab4, 0).addActionListener(event -> {
            ParDefault frame = new ParDefault(this, listenerParam1);
        });

        UGui.buttonCellEditor(tab4, 1).addActionListener(event -> {
            Integer grup = qSyspar1.getAs(UGui.getIndexRec(tab4), eSyspar1.params_id);
            ParDefault frame = new ParDefault(this, listenerParam2, grup);
        });

        UGui.buttonCellEditor(tab7, 1).addActionListener(event -> {
            Object grup = tab7.getValueAt(tab7.getSelectedRow(), 2);
            ParDefault frame = new ParDefault(this, listenerParam3, (int) grup);
        });
    }

    private void listenerSet() {

        listenerArtikl = (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            int index = UGui.getIndexRec(tab2);
            qSysprof.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab2), eSysprof.artikl_id);
            qSysprof.table(eArtikl.up).set(record.get(eArtikl.name), UGui.getIndexRec(tab2), eArtikl.name);
            qSysprof.table(eArtikl.up).set(record.get(eArtikl.code), UGui.getIndexRec(tab2), eArtikl.code);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab2, index);
        };

        listenerModel = (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);

            //Запишем в скрипт ветку из которого будет создаваться окно  
            String script = record.get(2).toString();
            JsonObject je = gson.fromJson(script, JsonObject.class);
            je.addProperty("nuni", systreeID);
            String script2 = gson.toJson(je);

            //Сохраним скрипт в базе
            Record sysprodRec = eSysprod.up.newRecord(Query.INS);
            sysprodRec.setNo(eSysprod.id, Conn.instanc().genId(eSysprod.id));
            sysprodRec.setNo(eSysprod.systree_id, systreeID);
            sysprodRec.setNo(eSysprod.name, record.get(1));
            sysprodRec.setNo(eSysprod.script, script2);
            qSysprod.insert(sysprodRec);

            loadingTab5();

            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            for (int index = 0; index < qSysprod.size(); ++index) {
                if (qSysprod.get(index, eSysprod.id) == sysprodRec.get(eSysprod.id)) {
                    UGui.setSelectedRow(tab5, index); //выделение рабочей записи
                    UGui.scrollRectToRow(index, tab5);
                    winTree.setSelectionRow(0);
                }
            }
        };

        listenerFurn = (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            int index = UGui.getIndexRec(tab3);
            qSysfurn.set(record.getInt(eFurniture.id), UGui.getIndexRec(tab3), eSysfurn.furniture_id);
            qSysfurn.table(eFurniture.up).set(record.get(eFurniture.name), UGui.getIndexRec(tab3), eFurniture.name);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab3, index);
        };

        listenerArt211 = (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            int index = UGui.getIndexRec(tab3);
            qSysfurn.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab3), eSysfurn.artikl_id1);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab3, index);
        };

        listenerArt212 = (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            int index = UGui.getIndexRec(tab3);
            qSysfurn.set(record.getInt(eArtikl.id), UGui.getIndexRec(tab3), eSysfurn.artikl_id2);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab3, index);
        };

        listenerParam1 = (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            int index = UGui.getIndexRec(tab4);
            qSyspar1.set(record.getInt(eParams.id), UGui.getIndexRec(tab4), eSyspar1.params_id);
            qSyspar1.set(null, UGui.getIndexRec(tab4), eSyspar1.text);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab4, index);
        };

        listenerParam2 = (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            int index = UGui.getIndexRec(tab4);
            qSyspar1.set(record.getStr(eParams.text), UGui.getIndexRec(tab4), eSyspar1.text);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab4, index);
        };

        listenerParam3 = (record) -> {
            int index = UGui.getIndexRec(tab5);
            int index2 = UGui.getIndexRec(tab7);
            if (index != -1) {
                Record sysprodRec = qSysprod.get(index);
                String script = sysprodRec.getStr(eSysprod.script);
                String script2 = UGui.paramdefAdd(script, record.getInt(eParams.id), qParams);
                sysprodRec.set(eSysprod.script, script2);
                qSysprod.execsql();
                iwin().build(script2);
                UGui.stopCellEditing(tab2, tab3, tab4, tab5, tab7);
                selectionWin();
                UGui.setSelectedRow(tab7, index2);
            }
        };
    }

    private void selectionSys() {
        UGui.stopCellEditing(tab2, tab3, tab4, tab5);
        Arrays.asList(tab2, tab3, tab4).forEach(table -> ((DefTableModel) table.getModel()).getQuery().execsql());

        sysNode = (DefMutableTreeNode) sysTree.getLastSelectedPathComponent();
        if (sysNode != null) {
            systreeID = sysNode.rec().getInt(eSystree.id);
            rsvSystree.load();
            qSysprof.select(eSysprof.up, "left join", eArtikl.up, "on", eArtikl.id, "=",
                    eSysprof.artikl_id, "where", eSysprof.systree_id, "=", sysNode.rec().getInt(eSystree.id), "order by", eSysprof.use_type, ",", eSysprof.prio);
            qSysfurn.select(eSysfurn.up, "left join", eFurniture.up, "on", eFurniture.id, "=",
                    eSysfurn.furniture_id, "where", eSysfurn.systree_id, "=", sysNode.rec().getInt(eSystree.id), "order by", eSysfurn.npp);
            qSyspar1.select(eSyspar1.up, "where", eSyspar1.systree_id, "=", sysNode.rec().getInt(eSystree.id));
            lab1.setText("ID = " + systreeID);
            lab2.setText("ID = -1");

            loadingTab5();

            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab2);
            UGui.setSelectedRow(tab3);
            UGui.setSelectedRow(tab4);
            if (qSysprod.isEmpty() == false) {

                int index = -1;
                int sysprodID = Integer.valueOf(eProperty.sysprodID.read());
                for (int i = 0; i < qSysprod.size(); ++i) {
                    if (qSysprod.get(i).getInt(eSysprod.id) == sysprodID) {
                        index = i;
                        tabb1.setSelectedIndex(4);
                    }
                }
                if (index != -1) {
                    UGui.setSelectedRow(tab5, index);
                } else {
                    UGui.setSelectedRow(tab5);
                }
            } else {
                canvas.redraw(null);
            }
        }
    }

    private void selectionWin() {
        Wincalc iwin = iwin();
        winNode = (DefMutableTreeNode) winTree.getLastSelectedPathComponent();
        if (winNode != null) {

            //Конструкции
            if (winNode.com5t().type() == enums.Type.RECTANGL || winNode.com5t().type() == enums.Type.TRAPEZE || winNode.com5t().type() == enums.Type.ARCH) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card12");
                ((TitledBorder) pan12.getBorder()).setTitle(iwin.rootArea.type().name);
                txt9.setText(eColor.find(iwin.colorID1).getStr(eColor.name));
                txt13.setText(eColor.find(iwin.colorID2).getStr(eColor.name));
                txt14.setText(eColor.find(iwin.colorID3).getStr(eColor.name));
                txt17.setText(String.valueOf(iwin.rootGson.width()));
                txt22.setText(String.valueOf(iwin.rootGson.height()));
                txt23.setText(String.valueOf(iwin.rootGson.heightAdd()));
                txt23.setEditable(winNode.com5t().type() == enums.Type.ARCH);

                //Параметры
            } else if (winNode.com5t().type() == enums.Type.PARAM) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card11");
                qSyspar2.clear();
                Map<Integer, String> map = new HashMap();
                iwin.mapPardef.forEach((pk, rec) -> map.put(pk, rec.getStr(eSyspar1.text)));
                map.forEach((pk, txt) -> qSyspar2.add(new Record(Query.SEL, pk, txt, pk, null, null)));
                ((DefTableModel) tab7.getModel()).fireTableDataChanged();

                //Рама, импост...
            } else if (winNode.com5t().type() == enums.Type.FRAME_SIDE
                    || winNode.com5t().type() == enums.Type.STVORKA_SIDE
                    || winNode.com5t().type() == enums.Type.IMPOST
                    || winNode.com5t().type() == enums.Type.STOIKA
                    || winNode.com5t().type() == enums.Type.SHTULP) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card13");
                ((TitledBorder) pan13.getBorder()).setTitle(winNode.toString());
                txt32.setText(winNode.com5t().artiklRecAn.getStr(eArtikl.code));
                txt33.setText(winNode.com5t().artiklRecAn.getStr(eArtikl.name));
                txt27.setText(eColor.find(winNode.com5t().colorID1).getStr(eColor.name));
                txt28.setText(eColor.find(winNode.com5t().colorID2).getStr(eColor.name));
                txt29.setText(eColor.find(winNode.com5t().colorID3).getStr(eColor.name));

                //Стеклопакет
            } else if (winNode.com5t().type() == enums.Type.GLASS) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card15");
                Record artiklRec = eArtikl.find(winNode.com5t().artiklRec.getInt(eArtikl.id), false);
                txt19.setText(artiklRec.getStr(eArtikl.code));
                txt18.setText(artiklRec.getStr(eArtikl.name));

                //Створка
            } else if (winNode.com5t().type() == enums.Type.STVORKA) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card16");
                AreaStvorka stv = (AreaStvorka) winNode.com5t();
                int id = stv.sysfurnRec.getInt(eSysfurn.furniture_id);;
                txt20.setText(eFurniture.find(id).getStr(eFurniture.name));
                txt30.setText(stv.typeOpen.name2);
                txt16.setText(stv.handleLayout.name);
                if (stv.handleLayout == LayoutHandle.VARIAT) {
                    txt31.setEditable(true);
                    txt31.setText(String.valueOf(stv.handleHeight));
                } else {
                    txt31.setEditable(false);
                    txt31.setText("");
                }
                txt21.setText(stv.handleRec.getStr(eArtikl.code));
                txt34.setText(stv.handleRec.getStr(eArtikl.name));
                txt24.setText(UGui.df.format(stv.mapFrame.get(Layout.BOTT).width()));
                float h = (stv.mapFrame.get(Layout.RIGHT).height() > stv.mapFrame.get(Layout.LEFT).height()) ? stv.mapFrame.get(Layout.RIGHT).height() : stv.mapFrame.get(Layout.LEFT).height();
                txt26.setText(UGui.df.format(h));
                txt25.setText(eColor.find(stv.handleColor).getStr(eColor.name));

                //Соединения
            } else if (winNode.com5t().type() == enums.Type.JOINING) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card17");
                DefMutableTreeNode nodeParent = (DefMutableTreeNode) winNode.getParent();
                ElemSimple elem5e = (ElemSimple) nodeParent.com5t();
                Object obj = elem5e.joinPoint(0);
                Object obj2 = elem5e.joinPoint(1);
                System.err.println("");
                ElemJoining ej1 = iwin.mapJoin.get(elem5e.joinPoint(0));
                ElemJoining ej2 = iwin.mapJoin.get(elem5e.joinPoint(1));
                ElemJoining ej3 = iwin.mapJoin.get(elem5e.joinPoint(2));
                Arrays.asList(txt36, txt37, txt38, txt39, txt40, txt41).forEach(it -> it.setText(""));
                Arrays.asList(lab55, lab56, lab57).forEach(it -> it.setIcon(null));
                if (ej1 != null) {
                    txt38.setText(ej1.joinvarRec.getStr(eJoinvar.name));
                    txt36.setText(ej1.joiningRec.getStr(eJoining.name));
                    lab55.setIcon(UColor.iconFromTypeJoin2(ej1.type.id));
                }
                if (ej2 != null) {
                    txt39.setText(ej2.joinvarRec.getStr(eJoinvar.name));
                    txt37.setText(ej2.joiningRec.getStr(eJoining.name));
                    lab56.setIcon(UColor.iconFromTypeJoin2(ej2.type.id));
                }
                if (ej3 != null && ej3.type == TypeJoin.VAR10) {
                    txt41.setText(ej3.joinvarRec.getStr(eJoinvar.name));
                    txt40.setText(ej3.joiningRec.getStr(eJoining.name));
                    lab57.setIcon(UColor.iconFromTypeJoin2(ej3.type.id));
                }
            }
            lab2.setText("ID = " + winNode.com5t().id());
            Arrays.asList(txt9, txt13, txt14, txt21, txt24, txt25, txt26, txt27, txt28,
                    txt29, txt19, txt20, txt30, txt34, txt36, txt37, txt38, txt39, txt40, txt41).forEach(it -> it.setCaretPosition(0));
            Arrays.asList(pan12, pan13, pan15, pan16).forEach(it -> it.repaint());
        }
    }

    private void selectionTab2(int artiklID) {
        for (int i = 0; i < qSysprof.size(); i++) {
            if (qSysprof.get(i).getInt(eSysprof.artikl_id) == artiklID) {
                UGui.setSelectedRow(tab2, i);
            }
        }
    }

    private void selectionTab5() {
        int index = UGui.getIndexRec(tab5);
        if (index != -1) {
            Record sysprodRec = qSysprod.table(eSysprod.up).get(index);
            eProperty.sysprodID.write(sysprodRec.getStr(eSysprod.id)); //запишем текущий sysprodID в файл
            App.Top.frame.setTitle(eProfile.profile.title + UGui.designTitle());

            Object v = sysprodRec.get(eSysprod.values().length);
            if (v instanceof Wincalc) { //прорисовка окна               
                Wincalc win = (Wincalc) v;
                new Furniture(win, true);
                new Joining(win, true);
                
                canvas.redraw(win);
                scene.redraw(win);
                pan1.repaint();
                pan4.repaint();
                
                loadingWin(win);
                winTree.setSelectionInterval(0, 0);

            } else {
                Graphics2D g = (Graphics2D) canvas.getGraphics();
                g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        }
    }

    private ArrayList<DefMutableTreeNode> addChild(ArrayList<DefMutableTreeNode> nodeList1, ArrayList<DefMutableTreeNode> nodeList2) {

        for (DefMutableTreeNode node : nodeList1) {
            String node2 = (String) node.getUserObject();
            for (Record record : qSystree) {
                if (record.getInt(eSystree.parent_id) == node.rec().getInt(eSystree.id)
                        && record.getInt(eSystree.parent_id) != record.getInt(eSystree.id)) {
                    DefMutableTreeNode node3 = new DefMutableTreeNode(record);
                    node.add(node3);
                    nodeList2.add(node3);
                    if (record.getInt(eSystree.id) == systreeID) {
                        selectedPath = node3.getPath(); //запомним path для nuni
                    }
                }
            }
        }
        return nodeList2;
    }

    private void updateScript(float selectID) {
        try {
            //Сохраним скрипт в базе
            String script = gson.toJson(iwin().rootGson);
            Record sysprodRec = qSysprod.get(UGui.getIndexRec(tab5));
            sysprodRec.set(eSysprod.script, script);
            qSysprod.update(sysprodRec);

            //Перерисум paintPanel 
            selectionTab5();

            //Установим курсор выделения
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) winTree.getModel().getRoot();
            do {
                if (selectID == ((DefMutableTreeNode) node).com5t().id()) {
                    TreePath path = new TreePath(node.getPath());
                    winTree.setSelectionPath(path);
                    winTree.scrollPathToVisible(path);
                    return;
                }
                node = node.getNextNode();
            } while (node != null);

        } catch (Exception e) {
            System.err.println("frames.Systree.updateScript()");;
        }
    }

    private Wincalc iwin() {
        int index = UGui.getIndexRec(tab5);
        if (index != -1) {
            Record sysprodRec = qSysprod.table(eSysprod.up).get(index);
            Object v = sysprodRec.get(eSysprod.values().length);
            if (v instanceof Wincalc) { //прорисовка окна               
                return (Wincalc) v;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan20 = new javax.swing.JPanel();
        tool = new javax.swing.JPanel();
        btnIns = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btn5 = new javax.swing.JToggleButton();
        btnReport1 = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnTest = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        sysTree = new javax.swing.JTree();
        pan1 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        panDesign = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        pan11 = new javax.swing.JPanel();
        scr7 = new javax.swing.JScrollPane();
        tab7 = new javax.swing.JTable();
        pan12 = new javax.swing.JPanel();
        pan21 = new javax.swing.JPanel();
        lab27 = new javax.swing.JLabel();
        lab31 = new javax.swing.JLabel();
        lab32 = new javax.swing.JLabel();
        btn9 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        txt9 = new javax.swing.JTextField();
        txt13 = new javax.swing.JTextField();
        txt14 = new javax.swing.JTextField();
        lab35 = new javax.swing.JLabel();
        lab38 = new javax.swing.JLabel();
        lab40 = new javax.swing.JLabel();
        txt17 = new javax.swing.JTextField();
        txt22 = new javax.swing.JTextField();
        txt23 = new javax.swing.JTextField();
        btn17 = new javax.swing.JButton();
        btn23 = new javax.swing.JButton();
        btn25 = new javax.swing.JButton();
        pan13 = new javax.swing.JPanel();
        lab33 = new javax.swing.JLabel();
        lab34 = new javax.swing.JLabel();
        txt32 = new javax.swing.JTextField();
        txt33 = new javax.swing.JTextField();
        btn22 = new javax.swing.JButton();
        pan22 = new javax.swing.JPanel();
        lab51 = new javax.swing.JLabel();
        lab52 = new javax.swing.JLabel();
        lab53 = new javax.swing.JLabel();
        txt27 = new javax.swing.JTextField();
        btn18 = new javax.swing.JButton();
        txt28 = new javax.swing.JTextField();
        btn19 = new javax.swing.JButton();
        txt29 = new javax.swing.JTextField();
        btn20 = new javax.swing.JButton();
        pan15 = new javax.swing.JPanel();
        lab29 = new javax.swing.JLabel();
        lab36 = new javax.swing.JLabel();
        btn3 = new javax.swing.JButton();
        txt19 = new javax.swing.JTextField();
        txt18 = new javax.swing.JTextField();
        pan16 = new javax.swing.JPanel();
        lab30 = new javax.swing.JLabel();
        lab37 = new javax.swing.JLabel();
        lab39 = new javax.swing.JLabel();
        lab46 = new javax.swing.JLabel();
        lab45 = new javax.swing.JLabel();
        btn10 = new javax.swing.JButton();
        btn12 = new javax.swing.JButton();
        btn14 = new javax.swing.JButton();
        btn21 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        txt20 = new javax.swing.JTextField();
        txt30 = new javax.swing.JTextField();
        txt25 = new javax.swing.JTextField();
        txt21 = new javax.swing.JTextField();
        txt31 = new javax.swing.JTextField();
        txt16 = new javax.swing.JTextField();
        lab41 = new javax.swing.JLabel();
        txt24 = new javax.swing.JTextField();
        lab42 = new javax.swing.JLabel();
        txt26 = new javax.swing.JTextField();
        btn15 = new javax.swing.JButton();
        btn16 = new javax.swing.JButton();
        lab48 = new javax.swing.JLabel();
        txt34 = new javax.swing.JTextField();
        btn24 = new javax.swing.JButton();
        pan17 = new javax.swing.JPanel();
        lab49 = new javax.swing.JLabel();
        lab50 = new javax.swing.JLabel();
        txt36 = new javax.swing.JTextField();
        txt37 = new javax.swing.JTextField();
        lab55 = new javax.swing.JLabel();
        txt38 = new javax.swing.JTextField();
        btn26 = new javax.swing.JButton();
        btn27 = new javax.swing.JButton();
        lab56 = new javax.swing.JLabel();
        txt39 = new javax.swing.JTextField();
        lab54 = new javax.swing.JLabel();
        txt40 = new javax.swing.JTextField();
        lab57 = new javax.swing.JLabel();
        txt41 = new javax.swing.JTextField();
        btn28 = new javax.swing.JButton();
        tabb1 = new javax.swing.JTabbedPane();
        pan6 = new javax.swing.JPanel();
        lab13 = new javax.swing.JLabel();
        lab14 = new javax.swing.JLabel();
        lab15 = new javax.swing.JLabel();
        lab16 = new javax.swing.JLabel();
        lab17 = new javax.swing.JLabel();
        lab19 = new javax.swing.JLabel();
        lab20 = new javax.swing.JLabel();
        lab24 = new javax.swing.JLabel();
        lab18 = new javax.swing.JLabel();
        lab23 = new javax.swing.JLabel();
        txt1 = new javax.swing.JTextField();
        txt2 = new javax.swing.JTextField();
        txt3 = new javax.swing.JTextField();
        txt4 = new javax.swing.JTextField();
        txt5 = new javax.swing.JTextField();
        txt7 = new javax.swing.JTextField();
        txt8 = new javax.swing.JTextField();
        txt10 = new javax.swing.JTextField();
        txt11 = new javax.swing.JTextField();
        txt15 = new javax.swing.JTextField();
        btn4 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn11 = new javax.swing.JButton();
        lab47 = new javax.swing.JLabel();
        txt35 = new javax.swing.JTextField();
        pan3 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan4 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan10 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        scr6 = new javax.swing.JScrollPane();
        winTree = new javax.swing.JTree();
        south = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        lab1 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        lab2 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));

        pan20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Текстура профиля", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 0)));
        pan20.setPreferredSize(new java.awt.Dimension(308, 98));

        javax.swing.GroupLayout pan20Layout = new javax.swing.GroupLayout(pan20);
        pan20.setLayout(pan20Layout);
        pan20Layout.setHorizontalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 226, Short.MAX_VALUE)
        );
        pan20Layout.setVerticalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Системы профилей");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Systree.this.windowClosed(evt);
            }
        });

        tool.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tool.setPreferredSize(new java.awt.Dimension(800, 29));

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

        btn5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c055.gif"))); // NOI18N
        btn5.setToolTipText("Артикулы в системе...");
        btn5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn5.setMaximumSize(new java.awt.Dimension(25, 25));
        btn5.setMinimumSize(new java.awt.Dimension(25, 25));
        btn5.setPreferredSize(new java.awt.Dimension(25, 25));
        btn5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findFromArtikl(evt);
            }
        });

        btnReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport1.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport1.setFocusable(false);
        btnReport1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport1.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport1.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport1.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnReport1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
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

        javax.swing.GroupLayout toolLayout = new javax.swing.GroupLayout(tool);
        tool.setLayout(toolLayout);
        toolLayout.setHorizontalGroup(
            toolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(btnReport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 665, Short.MAX_VALUE)
                .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        toolLayout.setVerticalGroup(
            toolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnIns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(toolLayout.createSequentialGroup()
                .addGroup(toolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(tool, java.awt.BorderLayout.PAGE_START);

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setPreferredSize(new java.awt.Dimension(800, 600));
        centr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(260, 550));

        sysTree.setEditable(true);
        sysTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sysTreeMousePressed(evt);
            }
        });
        scr1.setViewportView(sysTree);

        centr.add(scr1, java.awt.BorderLayout.WEST);

        pan1.setPreferredSize(new java.awt.Dimension(440, 550));
        pan1.setLayout(new java.awt.GridLayout(2, 1));

        pan2.setPreferredSize(new java.awt.Dimension(540, 200));
        pan2.setLayout(new java.awt.GridLayout(1, 2));

        panDesign.setLayout(new java.awt.BorderLayout());
        pan2.add(panDesign);

        pan7.setPreferredSize(new java.awt.Dimension(300, 200));
        pan7.setLayout(new java.awt.CardLayout());

        pan11.setLayout(new java.awt.BorderLayout());

        scr7.setBorder(null);
        scr7.setPreferredSize(new java.awt.Dimension(450, 300));

        tab7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Параметр", "Значение по умолчанию", "ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab7.setFillsViewportHeight(true);
        tab7.setName("tab7"); // NOI18N
        tab7.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Systree.this.mousePressed(evt);
            }
        });
        scr7.setViewportView(tab7);
        if (tab7.getColumnModel().getColumnCount() > 0) {
            tab7.getColumnModel().getColumn(0).setPreferredWidth(300);
            tab7.getColumnModel().getColumn(1).setPreferredWidth(140);
            tab7.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        pan11.add(scr7, java.awt.BorderLayout.CENTER);

        pan7.add(pan11, "card11");

        pan12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Основные", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 1)));
        pan12.setToolTipText("");
        pan12.setPreferredSize(new java.awt.Dimension(300, 200));

        pan21.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Текстура изделия", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 0)));
        pan21.setPreferredSize(new java.awt.Dimension(308, 104));

        lab27.setFont(frames.UGui.getFont(0,0));
        lab27.setText("Основная");
        lab27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab27.setPreferredSize(new java.awt.Dimension(80, 18));

        lab31.setFont(frames.UGui.getFont(0,0));
        lab31.setText("Внутренняя");
        lab31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab31.setPreferredSize(new java.awt.Dimension(80, 18));

        lab32.setFont(frames.UGui.getFont(0,0));
        lab32.setText("Внешняя");
        lab32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab32.setPreferredSize(new java.awt.Dimension(80, 18));

        btn9.setText("...");
        btn9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn9.setMaximumSize(new java.awt.Dimension(18, 18));
        btn9.setMinimumSize(new java.awt.Dimension(18, 18));
        btn9.setName("btn9"); // NOI18N
        btn9.setPreferredSize(new java.awt.Dimension(18, 18));
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        btn13.setText("...");
        btn13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn13.setMaximumSize(new java.awt.Dimension(18, 18));
        btn13.setMinimumSize(new java.awt.Dimension(18, 18));
        btn13.setName("btn13"); // NOI18N
        btn13.setPreferredSize(new java.awt.Dimension(18, 18));
        btn13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        btn2.setText("...");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(18, 18));
        btn2.setMinimumSize(new java.awt.Dimension(18, 18));
        btn2.setName("btn2"); // NOI18N
        btn2.setPreferredSize(new java.awt.Dimension(18, 18));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        txt9.setEditable(false);
        txt9.setBackground(new java.awt.Color(255, 255, 255));
        txt9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt9.setPreferredSize(new java.awt.Dimension(180, 18));

        txt13.setEditable(false);
        txt13.setBackground(new java.awt.Color(255, 255, 255));
        txt13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt13.setPreferredSize(new java.awt.Dimension(180, 18));

        txt14.setEditable(false);
        txt14.setBackground(new java.awt.Color(255, 255, 255));
        txt14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt14.setPreferredSize(new java.awt.Dimension(180, 18));

        javax.swing.GroupLayout pan21Layout = new javax.swing.GroupLayout(pan21);
        pan21.setLayout(pan21Layout);
        pan21Layout.setHorizontalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan21Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        pan21Layout.setVerticalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan21Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lab35.setFont(frames.UGui.getFont(0,0));
        lab35.setText("Ширина");
        lab35.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab35.setPreferredSize(new java.awt.Dimension(80, 18));

        lab38.setFont(frames.UGui.getFont(0,0));
        lab38.setText("Высота1");
        lab38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab38.setPreferredSize(new java.awt.Dimension(80, 18));

        lab40.setFont(frames.UGui.getFont(0,0));
        lab40.setText("Высота2");
        lab40.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab40.setPreferredSize(new java.awt.Dimension(80, 18));

        txt17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt17.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt17.setPreferredSize(new java.awt.Dimension(60, 18));
        txt17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        txt22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt22.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt22.setPreferredSize(new java.awt.Dimension(60, 18));
        txt22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        txt23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt23.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt23.setPreferredSize(new java.awt.Dimension(60, 18));
        txt23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        btn17.setText("...");
        btn17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn17.setMaximumSize(new java.awt.Dimension(18, 18));
        btn17.setMinimumSize(new java.awt.Dimension(18, 18));
        btn17.setName("btnField17"); // NOI18N
        btn17.setPreferredSize(new java.awt.Dimension(18, 18));
        btn17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt17Update(evt);
            }
        });

        btn23.setText("...");
        btn23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn23.setMaximumSize(new java.awt.Dimension(18, 18));
        btn23.setMinimumSize(new java.awt.Dimension(18, 18));
        btn23.setName("btnField17"); // NOI18N
        btn23.setPreferredSize(new java.awt.Dimension(18, 18));
        btn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt22Update(evt);
            }
        });

        btn25.setText("...");
        btn25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn25.setMaximumSize(new java.awt.Dimension(18, 18));
        btn25.setMinimumSize(new java.awt.Dimension(18, 18));
        btn25.setName("btnField17"); // NOI18N
        btn25.setPreferredSize(new java.awt.Dimension(18, 18));
        btn25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt23Update(evt);
            }
        });

        javax.swing.GroupLayout pan12Layout = new javax.swing.GroupLayout(pan12);
        pan12.setLayout(pan12Layout);
        pan12Layout.setHorizontalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pan21, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
            .addGroup(pan12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan12Layout.createSequentialGroup()
                        .addComponent(lab38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan12Layout.createSequentialGroup()
                        .addComponent(lab35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan12Layout.createSequentialGroup()
                        .addComponent(lab40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(180, Short.MAX_VALUE))
        );
        pan12Layout.setVerticalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan12Layout.createSequentialGroup()
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(pan21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        pan7.add(pan12, "card12");

        pan13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Рама, импост..", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 1)));
        pan13.setPreferredSize(new java.awt.Dimension(300, 200));

        lab33.setFont(frames.UGui.getFont(0,0));
        lab33.setText("Артикул");
        lab33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab33.setPreferredSize(new java.awt.Dimension(80, 18));

        lab34.setFont(frames.UGui.getFont(0,0));
        lab34.setText("Название");
        lab34.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab34.setPreferredSize(new java.awt.Dimension(80, 18));

        txt32.setEditable(false);
        txt32.setBackground(new java.awt.Color(255, 255, 255));
        txt32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt32.setPreferredSize(new java.awt.Dimension(180, 18));

        txt33.setEditable(false);
        txt33.setBackground(new java.awt.Color(255, 255, 255));
        txt33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt33.setPreferredSize(new java.awt.Dimension(180, 18));

        btn22.setText("...");
        btn22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn22.setMaximumSize(new java.awt.Dimension(18, 18));
        btn22.setMinimumSize(new java.awt.Dimension(18, 18));
        btn22.setName("btnField17"); // NOI18N
        btn22.setPreferredSize(new java.awt.Dimension(18, 18));
        btn22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysprofToFrame(evt);
            }
        });

        pan22.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Текстура изделия", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 0)));
        pan22.setPreferredSize(new java.awt.Dimension(308, 104));

        lab51.setFont(frames.UGui.getFont(0,0));
        lab51.setText("Основная");
        lab51.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab51.setPreferredSize(new java.awt.Dimension(80, 18));

        lab52.setFont(frames.UGui.getFont(0,0));
        lab52.setText("Внутренняя");
        lab52.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab52.setPreferredSize(new java.awt.Dimension(80, 18));

        lab53.setFont(frames.UGui.getFont(0,0));
        lab53.setText("Внешняя");
        lab53.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab53.setPreferredSize(new java.awt.Dimension(80, 18));

        txt27.setEditable(false);
        txt27.setBackground(new java.awt.Color(255, 255, 255));
        txt27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt27.setPreferredSize(new java.awt.Dimension(180, 18));

        btn18.setText("...");
        btn18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn18.setMaximumSize(new java.awt.Dimension(18, 18));
        btn18.setMinimumSize(new java.awt.Dimension(18, 18));
        btn18.setName("btnField17"); // NOI18N
        btn18.setPreferredSize(new java.awt.Dimension(18, 18));
        btn18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToFrame(evt);
            }
        });

        txt28.setEditable(false);
        txt28.setBackground(new java.awt.Color(255, 255, 255));
        txt28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt28.setPreferredSize(new java.awt.Dimension(180, 18));

        btn19.setText("...");
        btn19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn19.setMaximumSize(new java.awt.Dimension(18, 18));
        btn19.setMinimumSize(new java.awt.Dimension(18, 18));
        btn19.setName("btnField17"); // NOI18N
        btn19.setPreferredSize(new java.awt.Dimension(18, 18));
        btn19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToFrame(evt);
            }
        });

        txt29.setEditable(false);
        txt29.setBackground(new java.awt.Color(255, 255, 255));
        txt29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt29.setPreferredSize(new java.awt.Dimension(180, 18));

        btn20.setText("...");
        btn20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn20.setMaximumSize(new java.awt.Dimension(18, 18));
        btn20.setMinimumSize(new java.awt.Dimension(18, 18));
        btn20.setName("btnField17"); // NOI18N
        btn20.setPreferredSize(new java.awt.Dimension(18, 18));
        btn20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToFrame(evt);
            }
        });

        javax.swing.GroupLayout pan22Layout = new javax.swing.GroupLayout(pan22);
        pan22.setLayout(pan22Layout);
        pan22Layout.setHorizontalGroup(
            pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan22Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pan22Layout.setVerticalGroup(
            pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan22Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan22Layout.createSequentialGroup()
                        .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan22Layout.createSequentialGroup()
                        .addComponent(lab51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pan13Layout = new javax.swing.GroupLayout(pan13);
        pan13.setLayout(pan13Layout);
        pan13Layout.setHorizontalGroup(
            pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan13Layout.createSequentialGroup()
                        .addComponent(lab33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt32, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan13Layout.createSequentialGroup()
                        .addComponent(lab34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt33, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(pan22, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
        );
        pan13Layout.setVerticalGroup(
            pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pan22, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(120, Short.MAX_VALUE))
        );

        pan7.add(pan13, "card13");

        pan15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Стеклопакет", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 1)));
        pan15.setPreferredSize(new java.awt.Dimension(300, 200));

        lab29.setFont(frames.UGui.getFont(0,0));
        lab29.setText("Артикул");
        lab29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab29.setPreferredSize(new java.awt.Dimension(80, 18));

        lab36.setFont(frames.UGui.getFont(0,0));
        lab36.setText("Название");
        lab36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab36.setPreferredSize(new java.awt.Dimension(80, 18));

        btn3.setText("...");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(18, 18));
        btn3.setMinimumSize(new java.awt.Dimension(18, 18));
        btn3.setName("btnField17"); // NOI18N
        btn3.setPreferredSize(new java.awt.Dimension(18, 18));
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                artiklToGlass(evt);
            }
        });

        txt19.setEditable(false);
        txt19.setBackground(new java.awt.Color(255, 255, 255));
        txt19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt19.setPreferredSize(new java.awt.Dimension(180, 18));

        txt18.setEditable(false);
        txt18.setBackground(new java.awt.Color(255, 255, 255));
        txt18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt18.setPreferredSize(new java.awt.Dimension(180, 18));

        javax.swing.GroupLayout pan15Layout = new javax.swing.GroupLayout(pan15);
        pan15.setLayout(pan15Layout);
        pan15Layout.setHorizontalGroup(
            pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan15Layout.createSequentialGroup()
                        .addComponent(lab29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt19, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan15Layout.createSequentialGroup()
                        .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt18, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan15Layout.setVerticalGroup(
            pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(234, Short.MAX_VALUE))
        );

        pan7.add(pan15, "card15");

        pan16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Створка", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 1)));
        pan16.setPreferredSize(new java.awt.Dimension(3100, 200));

        lab30.setFont(frames.UGui.getFont(0,0));
        lab30.setText("Фурнитура");
        lab30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab30.setPreferredSize(new java.awt.Dimension(80, 18));

        lab37.setFont(frames.UGui.getFont(0,0));
        lab37.setText("Арт. ручки");
        lab37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab37.setMaximumSize(new java.awt.Dimension(74, 18));
        lab37.setMinimumSize(new java.awt.Dimension(14, 18));
        lab37.setPreferredSize(new java.awt.Dimension(80, 18));

        lab39.setFont(frames.UGui.getFont(0,0));
        lab39.setText("Текстура");
        lab39.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab39.setMinimumSize(new java.awt.Dimension(1, 18));
        lab39.setPreferredSize(new java.awt.Dimension(80, 18));

        lab46.setFont(frames.UGui.getFont(0,0));
        lab46.setText("Высота ручки");
        lab46.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab46.setPreferredSize(new java.awt.Dimension(80, 18));

        lab45.setFont(frames.UGui.getFont(0,0));
        lab45.setText("Напр. откр.");
        lab45.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab45.setPreferredSize(new java.awt.Dimension(80, 18));

        btn10.setText("...");
        btn10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn10.setMaximumSize(new java.awt.Dimension(18, 18));
        btn10.setMinimumSize(new java.awt.Dimension(18, 18));
        btn10.setName("btnField17"); // NOI18N
        btn10.setPreferredSize(new java.awt.Dimension(18, 18));
        btn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysfurnToStvorka(evt);
            }
        });

        btn12.setText("...");
        btn12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn12.setMaximumSize(new java.awt.Dimension(18, 18));
        btn12.setMinimumSize(new java.awt.Dimension(18, 18));
        btn12.setName("btnField17"); // NOI18N
        btn12.setPreferredSize(new java.awt.Dimension(18, 18));
        btn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handlToStvorka(evt);
            }
        });

        btn14.setText("...");
        btn14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn14.setMaximumSize(new java.awt.Dimension(18, 18));
        btn14.setMinimumSize(new java.awt.Dimension(18, 18));
        btn14.setName("btnField17"); // NOI18N
        btn14.setPreferredSize(new java.awt.Dimension(18, 18));
        btn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToHandl(evt);
            }
        });

        btn21.setText("...");
        btn21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn21.setMaximumSize(new java.awt.Dimension(18, 18));
        btn21.setMinimumSize(new java.awt.Dimension(18, 18));
        btn21.setName("btnField17"); // NOI18N
        btn21.setPreferredSize(new java.awt.Dimension(18, 18));
        btn21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeOpenToStvorka(evt);
            }
        });

        btn6.setText("...");
        btn6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn6.setMaximumSize(new java.awt.Dimension(18, 18));
        btn6.setMinimumSize(new java.awt.Dimension(18, 18));
        btn6.setName("btnField17"); // NOI18N
        btn6.setPreferredSize(new java.awt.Dimension(18, 18));
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightHandlToStvorka(evt);
            }
        });

        txt20.setEditable(false);
        txt20.setBackground(new java.awt.Color(255, 255, 255));
        txt20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt20.setPreferredSize(new java.awt.Dimension(180, 18));

        txt30.setEditable(false);
        txt30.setBackground(new java.awt.Color(255, 255, 255));
        txt30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt30.setPreferredSize(new java.awt.Dimension(180, 18));

        txt25.setEditable(false);
        txt25.setBackground(new java.awt.Color(255, 255, 255));
        txt25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt25.setPreferredSize(new java.awt.Dimension(180, 18));

        txt21.setEditable(false);
        txt21.setBackground(new java.awt.Color(255, 255, 255));
        txt21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt21.setPreferredSize(new java.awt.Dimension(180, 18));

        txt31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt31.setPreferredSize(new java.awt.Dimension(180, 18));

        txt16.setEditable(false);
        txt16.setBackground(new java.awt.Color(255, 255, 255));
        txt16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt16.setPreferredSize(new java.awt.Dimension(180, 18));

        lab41.setFont(frames.UGui.getFont(0,0));
        lab41.setText("Ширина");
        lab41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab41.setPreferredSize(new java.awt.Dimension(80, 18));

        txt24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt24.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt24.setPreferredSize(new java.awt.Dimension(60, 18));
        txt24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        lab42.setFont(frames.UGui.getFont(0,0));
        lab42.setText("Высота");
        lab42.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab42.setPreferredSize(new java.awt.Dimension(80, 18));

        txt26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt26.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt26.setPreferredSize(new java.awt.Dimension(60, 18));
        txt26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        btn15.setText("...");
        btn15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn15.setMaximumSize(new java.awt.Dimension(18, 18));
        btn15.setMinimumSize(new java.awt.Dimension(18, 18));
        btn15.setName("btnField17"); // NOI18N
        btn15.setPreferredSize(new java.awt.Dimension(18, 18));
        btn15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt24Update(evt);
            }
        });

        btn16.setText("...");
        btn16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn16.setMaximumSize(new java.awt.Dimension(18, 18));
        btn16.setMinimumSize(new java.awt.Dimension(18, 18));
        btn16.setName("btnField17"); // NOI18N
        btn16.setPreferredSize(new java.awt.Dimension(18, 18));
        btn16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt26Update(evt);
            }
        });

        lab48.setFont(frames.UGui.getFont(0,0));
        lab48.setText("Наимен.ручки");
        lab48.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab48.setMinimumSize(new java.awt.Dimension(15, 18));
        lab48.setPreferredSize(new java.awt.Dimension(80, 18));

        txt34.setEditable(false);
        txt34.setBackground(new java.awt.Color(255, 255, 255));
        txt34.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt34.setPreferredSize(new java.awt.Dimension(180, 18));

        btn24.setText("...");
        btn24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn24.setMaximumSize(new java.awt.Dimension(18, 18));
        btn24.setMinimumSize(new java.awt.Dimension(18, 18));
        btn24.setName("btnField17"); // NOI18N
        btn24.setPreferredSize(new java.awt.Dimension(18, 18));
        btn24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handlToStvorka(evt);
            }
        });

        javax.swing.GroupLayout pan16Layout = new javax.swing.GroupLayout(pan16);
        pan16.setLayout(pan16Layout);
        pan16Layout.setHorizontalGroup(
            pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lab46, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab45, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(txt30, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addComponent(txt16, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt31, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addComponent(lab42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 170, Short.MAX_VALUE))
                    .addGroup(pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lab37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab48, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                            .addComponent(lab39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt34, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                            .addComponent(txt25, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                            .addComponent(txt21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pan16Layout.setVerticalGroup(
            pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan16Layout.createSequentialGroup()
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        pan7.add(pan16, "card16");

        pan17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Соединения", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0, 1)));
        pan17.setPreferredSize(new java.awt.Dimension(300, 200));

        lab49.setFont(frames.UGui.getFont(0,0));
        lab49.setText("1  соединение");
        lab49.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab49.setIconTextGap(1);
        lab49.setPreferredSize(new java.awt.Dimension(80, 18));

        lab50.setFont(frames.UGui.getFont(0,0));
        lab50.setText("2  соединение");
        lab50.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab50.setPreferredSize(new java.awt.Dimension(80, 18));

        txt36.setEditable(false);
        txt36.setBackground(new java.awt.Color(255, 255, 255));
        txt36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt36.setPreferredSize(new java.awt.Dimension(180, 18));

        txt37.setEditable(false);
        txt37.setBackground(new java.awt.Color(255, 255, 255));
        txt37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt37.setPreferredSize(new java.awt.Dimension(180, 18));

        lab55.setFont(frames.UGui.getFont(0,0));
        lab55.setText("Вариант");
        lab55.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab55.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lab55.setIconTextGap(6);
        lab55.setPreferredSize(new java.awt.Dimension(80, 20));

        txt38.setEditable(false);
        txt38.setBackground(new java.awt.Color(255, 255, 255));
        txt38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt38.setPreferredSize(new java.awt.Dimension(180, 18));

        btn26.setText("...");
        btn26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn26.setMaximumSize(new java.awt.Dimension(18, 18));
        btn26.setMinimumSize(new java.awt.Dimension(18, 18));
        btn26.setName("btn26"); // NOI18N
        btn26.setPreferredSize(new java.awt.Dimension(18, 18));
        btn26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinToFrame(evt);
            }
        });

        btn27.setText("...");
        btn27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn27.setMaximumSize(new java.awt.Dimension(18, 18));
        btn27.setMinimumSize(new java.awt.Dimension(18, 18));
        btn27.setName("btn27"); // NOI18N
        btn27.setPreferredSize(new java.awt.Dimension(18, 18));
        btn27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinToFrame(evt);
            }
        });

        lab56.setFont(frames.UGui.getFont(0,0));
        lab56.setText("Вариант");
        lab56.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab56.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lab56.setIconTextGap(6);
        lab56.setPreferredSize(new java.awt.Dimension(80, 20));

        txt39.setEditable(false);
        txt39.setBackground(new java.awt.Color(255, 255, 255));
        txt39.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt39.setPreferredSize(new java.awt.Dimension(180, 18));

        lab54.setFont(frames.UGui.getFont(0,0));
        lab54.setText("3  соединение");
        lab54.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab54.setPreferredSize(new java.awt.Dimension(80, 18));

        txt40.setEditable(false);
        txt40.setBackground(new java.awt.Color(255, 255, 255));
        txt40.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt40.setPreferredSize(new java.awt.Dimension(180, 18));

        lab57.setFont(frames.UGui.getFont(0,0));
        lab57.setText("Вариант");
        lab57.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab57.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lab57.setIconTextGap(6);
        lab57.setPreferredSize(new java.awt.Dimension(80, 20));

        txt41.setEditable(false);
        txt41.setBackground(new java.awt.Color(255, 255, 255));
        txt41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt41.setPreferredSize(new java.awt.Dimension(180, 18));

        btn28.setText("...");
        btn28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn28.setMaximumSize(new java.awt.Dimension(18, 18));
        btn28.setMinimumSize(new java.awt.Dimension(18, 18));
        btn28.setName("btn28"); // NOI18N
        btn28.setPreferredSize(new java.awt.Dimension(18, 18));
        btn28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinToFrame(evt);
            }
        });

        javax.swing.GroupLayout pan17Layout = new javax.swing.GroupLayout(pan17);
        pan17.setLayout(pan17Layout);
        pan17Layout.setHorizontalGroup(
            pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan17Layout.createSequentialGroup()
                        .addComponent(lab55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt38, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .addGroup(pan17Layout.createSequentialGroup()
                        .addComponent(lab57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt41, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .addGroup(pan17Layout.createSequentialGroup()
                        .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan17Layout.createSequentialGroup()
                                .addComponent(lab49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt36, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                            .addGroup(pan17Layout.createSequentialGroup()
                                .addComponent(lab54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt40, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan17Layout.createSequentialGroup()
                        .addComponent(lab50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt37, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan17Layout.createSequentialGroup()
                        .addComponent(lab56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt39, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan17Layout.setVerticalGroup(
            pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        pan7.add(pan17, "card17");

        pan2.add(pan7);

        pan1.add(pan2);

        tabb1.setPreferredSize(new java.awt.Dimension(540, 250));
        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                Systree.this.stateChanged(evt);
            }
        });

        lab13.setFont(frames.UGui.getFont(0,0));
        lab13.setText("Заполн. по умолчанию");
        lab13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab13.setPreferredSize(new java.awt.Dimension(112, 18));

        lab14.setFont(frames.UGui.getFont(0,0));
        lab14.setText("Доступные толщины");
        lab14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab14.setPreferredSize(new java.awt.Dimension(120, 18));

        lab15.setFont(frames.UGui.getFont(0,0));
        lab15.setText("Основная текстура");
        lab15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab15.setPreferredSize(new java.awt.Dimension(112, 18));

        lab16.setFont(frames.UGui.getFont(0,0));
        lab16.setText("Внутр. текстура");
        lab16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab16.setPreferredSize(new java.awt.Dimension(112, 18));

        lab17.setFont(frames.UGui.getFont(0,0));
        lab17.setText("Внешняя текстура");
        lab17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab17.setPreferredSize(new java.awt.Dimension(112, 18));

        lab19.setFont(frames.UGui.getFont(0,0));
        lab19.setText("Признак системы");
        lab19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab19.setMaximumSize(new java.awt.Dimension(112, 18));
        lab19.setMinimumSize(new java.awt.Dimension(112, 18));
        lab19.setPreferredSize(new java.awt.Dimension(112, 18));

        lab20.setFont(frames.UGui.getFont(0,0));
        lab20.setText("Система");
        lab20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab20.setPreferredSize(new java.awt.Dimension(112, 18));

        lab24.setFont(frames.UGui.getFont(0,0));
        lab24.setText("Вид изделия по умолчанию");
        lab24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab24.setPreferredSize(new java.awt.Dimension(120, 18));

        lab18.setFont(frames.UGui.getFont(0,0));
        lab18.setText("Доступн.гр.текстур");
        lab18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab18.setPreferredSize(new java.awt.Dimension(112, 18));

        lab23.setFont(frames.UGui.getFont(0,0));
        lab23.setText("Префикс (замена/код)");
        lab23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab23.setPreferredSize(new java.awt.Dimension(120, 18));

        txt1.setEditable(false);
        txt1.setBackground(new java.awt.Color(255, 255, 255));
        txt1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt1.setPreferredSize(new java.awt.Dimension(70, 18));

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt2.setPreferredSize(new java.awt.Dimension(80, 18));

        txt3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt3.setPreferredSize(new java.awt.Dimension(80, 18));

        txt4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt4.setPreferredSize(new java.awt.Dimension(72, 18));

        txt5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt5.setPreferredSize(new java.awt.Dimension(72, 18));

        txt7.setEditable(false);
        txt7.setBackground(new java.awt.Color(255, 255, 255));
        txt7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt7.setPreferredSize(new java.awt.Dimension(70, 18));

        txt8.setEditable(false);
        txt8.setBackground(new java.awt.Color(255, 255, 255));
        txt8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt8.setPreferredSize(new java.awt.Dimension(450, 18));

        txt10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt10.setPreferredSize(new java.awt.Dimension(80, 18));

        txt11.setEditable(false);
        txt11.setBackground(new java.awt.Color(255, 255, 255));
        txt11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt11.setPreferredSize(new java.awt.Dimension(70, 18));

        txt15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt15.setPreferredSize(new java.awt.Dimension(80, 18));

        btn4.setText("...");
        btn4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn4.setMaximumSize(new java.awt.Dimension(18, 18));
        btn4.setMinimumSize(new java.awt.Dimension(18, 18));
        btn4.setPreferredSize(new java.awt.Dimension(18, 18));
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glasdefToSystree(evt);
            }
        });

        btn7.setText("...");
        btn7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn7.setMaximumSize(new java.awt.Dimension(18, 18));
        btn7.setMinimumSize(new java.awt.Dimension(18, 18));
        btn7.setPreferredSize(new java.awt.Dimension(18, 18));
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeToSystree(evt);
            }
        });

        btn11.setText("...");
        btn11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn11.setMaximumSize(new java.awt.Dimension(18, 18));
        btn11.setMinimumSize(new java.awt.Dimension(18, 18));
        btn11.setPreferredSize(new java.awt.Dimension(18, 18));
        btn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageviewToSystree(evt);
            }
        });

        lab47.setFont(frames.UGui.getFont(0,0));
        lab47.setText("Коэф. рентабельности");
        lab47.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txt35.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt35.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt35.setPreferredSize(new java.awt.Dimension(80, 18));

        javax.swing.GroupLayout pan6Layout = new javax.swing.GroupLayout(pan6);
        pan6.setLayout(pan6Layout);
        pan6Layout.setHorizontalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(lab20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt8, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pan6Layout.createSequentialGroup()
                                        .addComponent(lab15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(pan6Layout.createSequentialGroup()
                                        .addComponent(lab16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(pan6Layout.createSequentialGroup()
                                        .addComponent(lab18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(pan6Layout.createSequentialGroup()
                                        .addComponent(lab17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(18, 18, 18))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt7, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)))
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan6Layout.createSequentialGroup()
                                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                                        .addComponent(lab47, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt35, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                                    .addGroup(pan6Layout.createSequentialGroup()
                                        .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)))))
                .addGap(19, 19, 19))
        );
        pan6Layout.setVerticalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab47)
                    .addComponent(txt35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(133, Short.MAX_VALUE))
        );

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp\nОсновные\n&nbsp;&nbsp;&nbsp", pan6);

        pan3.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);
        scr2.setPreferredSize(new java.awt.Dimension(450, 300));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Применение", "Сторона", "Артикул", "Название", "Приоритет", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
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
                Systree.this.mousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(3).setMinWidth(200);
            tab2.getColumnModel().getColumn(4).setMaxWidth(40);
            tab2.getColumnModel().getColumn(5).setMaxWidth(40);
        }

        pan3.add(scr2, java.awt.BorderLayout.CENTER);

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp\nПрофили\n&nbsp;&nbsp;&nbsp", pan3);

        pan4.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(450, 300));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "№пп", "Название  фурнитуры", "Тип открывания", "Замена", "Установка ручки", "Артикул ручки", "Артикул подвеса", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Systree.this.mousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(0).setMaxWidth(80);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab3.getColumnModel().getColumn(3).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(3).setMaxWidth(80);
            tab3.getColumnModel().getColumn(7).setMaxWidth(40);
        }

        pan4.add(scr3, java.awt.BorderLayout.CENTER);

        tabb1.addTab("<html><font size=\"3\">&nbsp;&nbsp;&nbsp\nФурнитура\n&nbsp;&nbsp;&nbsp", pan4);

        pan5.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(null);
        scr4.setPreferredSize(new java.awt.Dimension(450, 300));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Параметр", "Значение по умолчанию", "Закреплено", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
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
                Systree.this.mousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(2).setPreferredWidth(60);
            tab4.getColumnModel().getColumn(2).setMaxWidth(80);
            tab4.getColumnModel().getColumn(3).setMaxWidth(40);
        }

        pan5.add(scr4, java.awt.BorderLayout.CENTER);

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp\nПараметры\n&nbsp;&nbsp;&nbsp", pan5);

        pan10.setLayout(new java.awt.BorderLayout());

        scr5.setBorder(null);

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab5.setFillsViewportHeight(true);
        tab5.setRowHeight(68);
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Systree.this.mousePressed(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(1).setMinWidth(68);
            tab5.getColumnModel().getColumn(1).setPreferredWidth(68);
            tab5.getColumnModel().getColumn(1).setMaxWidth(68);
        }

        pan10.add(scr5, java.awt.BorderLayout.CENTER);

        scr6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr6.setPreferredSize(new java.awt.Dimension(240, 324));
        scr6.setViewportView(winTree);

        pan10.add(scr6, java.awt.BorderLayout.EAST);

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp\nМодели\n&nbsp;&nbsp;&nbsp", pan10);

        pan1.add(tabb1);
        tabb1.getAccessibleContext().setAccessibleName("<html><font size=\"3\">&nbsp;&nbsp;&nbsp\nОсновные\n&nbsp;&nbsp;&nbsp");

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        filler1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.add(filler1);

        lab1.setText("___");
        south.add(lab1);

        filler2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.add(filler2);

        lab2.setText("___");
        south.add(lab2);

        filler3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.add(filler3);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, Arrays.asList(tab2, tab3, tab4, tab5));
        if (sysTree.isEditing()) {
            sysTree.getCellEditor().stopCellEditing();
        }
        sysTree.setBorder(null);
        UGui.updateBorderAndSql(table, Arrays.asList(tab2, tab3, tab4, tab5));
        filterTable.mousePressed((JTable) evt.getSource());
    }//GEN-LAST:event_mousePressed

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(sysTree, tab2, tab3, tab4, tab5);
        qSystree.execsql();
        Arrays.asList(tab2, tab3, tab4).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (models != null)
            models.dispose();
    }//GEN-LAST:event_windowClosed

    private void stateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_stateChanged

        UGui.stopCellEditing(sysTree);
        sysTree.setBorder(null);
        if (tabb1.getSelectedIndex() == 1) {
            UGui.updateBorderAndSql(tab2, Arrays.asList(tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 2) {
            UGui.updateBorderAndSql(tab3, Arrays.asList(tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 3) {
            UGui.updateBorderAndSql(tab4, Arrays.asList(tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 4) {
            UGui.updateBorderAndSql(tab5, Arrays.asList(tab2, tab3, tab4, tab5));
        }
    }//GEN-LAST:event_stateChanged

    private void sysTreeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sysTreeMousePressed
        Arrays.asList(tab2, tab3, tab4, tab5).forEach(tab -> tab.setBorder(null));
        sysTree.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
        UGui.stopCellEditing(tab2, tab3, tab4, tab5);
    }//GEN-LAST:event_sysTreeMousePressed

    private void glasdefToSystree(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glasdefToSystree

        new DicArtikl(this, (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            sysNode.rec().set(eSystree.glas, record.getStr(eArtikl.code));
            rsvSystree.load();
        }, 5);
    }//GEN-LAST:event_glasdefToSystree

    private void imageviewToSystree(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageviewToSystree

        new DicEnums(this, (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            sysNode.rec().set(eSystree.imgview, record.getInt(0));
            rsvSystree.load();

        }, LayoutProduct.values());
    }//GEN-LAST:event_imageviewToSystree

    private void typeToSystree(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeToSystree

        new DicEnums(this, (record) -> {
            UGui.stopCellEditing(tab2, tab3, tab4, tab5);
            sysNode.rec().set(eSystree.types, record.getInt(0));
            rsvSystree.load();
        }, TypeUse.values());
    }//GEN-LAST:event_typeToSystree

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (sysNode != null) {
            if (sysTree.getBorder() != null) {
                if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите добавить ветку в систему?", "Предупреждение",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
                    Record record = eSystree.up.newRecord(Query.INS);
                    int id = Conn.instanc().genId(eSystree.id);
                    record.setNo(eSystree.id, id);
                    int parent_id = (sysNode.rec().getInt(eSystree.id) == -1) ? id : sysNode.rec().getInt(eSystree.id);
                    record.setNo(eSystree.parent_id, parent_id);
                    record.setNo(eSystree.name, "P" + id + "." + parent_id);
                    qSystree.insert(record); //record сохраним в базе
                    record.set(eSystree.up, Query.SEL);
                    qSystree.add(record); //добавим record в список
                    DefMutableTreeNode newNode = new DefMutableTreeNode(record);
                    ((DefaultTreeModel) sysTree.getModel()).insertNodeInto(newNode, sysNode, sysNode.getChildCount()); //добавим node в tree
                    TreeNode[] nodes = ((DefaultTreeModel) sysTree.getModel()).getPathToRoot(newNode);
                    sysTree.scrollPathToVisible(new TreePath(nodes));
                    sysTree.setSelectionPath(new TreePath(nodes));
                }

            } else if (tab2.getBorder() != null) {
                UGui.insertRecord(tab2, eSysprof.up, (record) -> {
                    record.set(eSysprof.systree_id, systreeID);
                    Record record2 = eArtikl.up.newRecord();
                    qSysprof.table(eArtikl.up).add(record2);;
                });

            } else if (tab3.getBorder() != null) {
                UGui.insertRecord(tab3, eSysfurn.up, (record) -> {
                    record.set(eSysfurn.systree_id, systreeID);
                    record.setNo(eSysfurn.npp, 0);
                    record.setNo(eSysfurn.replac, 0);
                    Record record2 = eFurniture.up.newRecord();
                    qSysfurn.table(eFurniture.up).add(record2);;
                });
            } else if (tab4.getBorder() != null) {
                UGui.insertRecord(tab4, eSyspar1.up, (record) -> {
                    record.set(eSyspar1.systree_id, systreeID);
                });

            } else if (tab5.getBorder() != null) {
                if (sysNode != null && sysNode.isLeaf()) {
                    FrameProgress.create(Systree.this, new ListenerFrame() {
                        public void actionRequest(Object obj) {
                            models = new Models(Systree.this, listenerModel);
                            FrameToFile.setFrameSize(models);
                            models.setVisible(true);
                        }
                    });
                }
            }
        }
    }//GEN-LAST:event_btnInsert

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (sysTree.getBorder() != null) {
            if (sysTree.isSelectionEmpty() == false) {
                if (sysNode.getChildCount() != 0) {
                    JOptionPane.showMessageDialog(this, "Нельзя удалить текущий узел т. к. у него есть подчинённые записи", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                DefMutableTreeNode parentNode = (DefMutableTreeNode) sysNode.getParent();
                if (JOptionPane.showConfirmDialog(this, "Хотите удалить " + sysNode + "?", "Подтвердите удаление",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null) == 0) {
                    UGui.stopCellEditing(sysTree);
                    if (qSystree.delete(sysNode.rec())) {

                        qSystree.remove(sysNode.rec());
                        ((DefaultTreeModel) sysTree.getModel()).removeNodeFromParent(sysNode);
                        if (parentNode != null) {
                            TreeNode[] nodes = ((DefaultTreeModel) sysTree.getModel()).getPathToRoot(parentNode);
                            sysTree.scrollPathToVisible(new TreePath(nodes));
                            sysTree.setSelectionPath(new TreePath(nodes));
                        }
                    }
                }
            }
        } else if (tab2.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0 && tab2.getSelectedRow() != -1) {
                UGui.deleteRecord(tab2);
            }
        } else if (tab3.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0 && tab3.getSelectedRow() != -1) {
                UGui.deleteRecord(tab3);
            }
        } else if (tab4.getBorder() != null && tab4.getSelectedRow() != -1) {
            if (UGui.isDeleteRecord(this) == 0) {
                UGui.deleteRecord(tab4);
            }
        } else if (tab5.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0 && tab5.getSelectedRow() != -1) {
                UGui.deleteRecord(tab5);
            }
//          else {    JOptionPane.showMessageDialog(null, "Ни одна из текущих записей не выбрана", "Предупреждение", JOptionPane.NO_OPTION);     }
        }
    }//GEN-LAST:event_btnDelete

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Query.listOpenTable.forEach(q -> q.clear());
        int row[] = winTree.getSelectionRows();
        loadingData();
        selectionSys();
        winTree.setSelectionRows(row);
    }//GEN-LAST:event_btnRefresh

    private void findFromArtikl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findFromArtikl
        Record record = qSysprof.get(UGui.getIndexRec(tab2));
        Record record2 = eArtikl.find(record.getInt(eSysprof.artikl_id), false);
        FrameProgress.create(this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Artikles.createFrame(Systree.this, record2);
            }
        });
    }//GEN-LAST:event_findFromArtikl

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void sysprofToFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysprofToFrame
        try {
            if (winNode != null) {
                float selectID = winNode.com5t().id(); //id элемента который уже есть в конструкции, это либо виртуал. либо найденный по приоритету при построении модели
                Query qSysprofFilter = new Query(eSysprof.values(), eArtikl.values()); //тут будет список допустимых профилей из ветки системы
                //Цикл по профилям ветки 
                for (int index = 0; index < qSysprof.size(); ++index) {
                    Record sysprofRec = qSysprof.get(index);

                    //Отфильтруем подходящие по параметрам
                    if (winNode.com5t().type().id2 == sysprofRec.getInt(eSysprof.use_type)) {
                        if (sysprofRec.getInt(eSysprof.use_side) == winNode.com5t().layout().id
                                || sysprofRec.getInt(eSysprof.use_side) == UseSide.ANY.id
                                || sysprofRec.getInt(eSysprof.use_side) == UseSide.MANUAL.id) {

                            qSysprofFilter.add(sysprofRec);
                            qSysprofFilter.table(eArtikl.up).add(qSysprof.table(eArtikl.up).get(index));
                        }
                    }
                }
                new DicSysprof(this, (sysprofRec) -> {

                    Wincalc iwin = iwin();
                    if (winNode.com5t().type() == enums.Type.FRAME_SIDE) { //рама окна
                        float elemId = winNode.com5t().id();
                        GsonElem gsonRama = iwin.rootGson.find(elemId);
                        String paramStr = gsonRama.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        paramObj.addProperty(PKjson.sysprofID, sysprofRec.getInt(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        gsonRama.param(paramStr);
                        updateScript(selectID);

                    } else if (winNode.com5t().type() == enums.Type.STVORKA_SIDE) { //рама створки
                        float stvId = ((DefMutableTreeNode) winNode.getParent()).com5t().id();
                        GsonElem stvArea = (GsonElem) iwin.rootGson.find(stvId);
                        String paramStr = stvArea.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        String stvKey = null;
                        if (winNode.com5t().layout() == Layout.BOTT) {
                            stvKey = PKjson.stvorkaBottom;
                        } else if (winNode.com5t().layout() == Layout.RIGHT) {
                            stvKey = PKjson.stvorkaRight;
                        } else if (winNode.com5t().layout() == Layout.TOP) {
                            stvKey = PKjson.stvorkaTop;
                        } else if (winNode.com5t().layout() == Layout.LEFT) {
                            stvKey = PKjson.stvorkaLeft;
                        }
                        JsonObject jso = UJson.getAsJsonObject(paramObj, stvKey);
                        jso.addProperty(PKjson.sysprofID, sysprofRec.getStr(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        stvArea.param(paramStr);
                        updateScript(selectID);

                    } else {  //импост
                        float elemId = winNode.com5t().id();
                        GsonElem gsonElem = iwin.rootGson.find(elemId);
                        String paramStr = gsonElem.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        paramObj.addProperty(PKjson.sysprofID, sysprofRec.getInt(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        gsonElem.param(paramStr);
                        updateScript(selectID);
                    }

                }, qSysprofFilter);
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_sysprofToFrame

    private void colorToFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToFrame
        try {
            float selectID = winNode.com5t().id();
            HashSet<Record> colorSet = new HashSet();
            Query artdetList = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.artikl_id, "=", winNode.com5t().artiklRec.getInt(eArtikl.id));
            artdetList.forEach(rec -> {

                if (rec.getInt(eArtdet.color_fk) < 0) {
                    eColor.query().forEach(rec2 -> {
                        if (rec2.getInt(eColor.colgrp_id) == Math.abs(rec.getInt(eArtdet.color_fk))) {
                            colorSet.add(rec2);
                        }
                    });
                } else {
                    colorSet.add(eColor.find(rec.getInt(eArtdet.color_fk)));
                }
            });
            DicColor frame = new DicColor(this, (colorRec) -> {

                String colorID = (evt.getSource() == btn18) ? PKjson.colorID1 : (evt.getSource() == btn19) ? PKjson.colorID2 : PKjson.colorID3;
                float parentId = ((DefMutableTreeNode) winNode.getParent()).com5t().id();
                GsonElem parentArea = (GsonElem) iwin().rootGson.find(parentId);

                if (winNode.com5t().type() == enums.Type.STVORKA_SIDE) {
                    String paramStr = parentArea.param();
                    JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                    String stvKey = null;
                    if (winNode.com5t().layout() == Layout.BOTT) {
                        stvKey = PKjson.stvorkaBottom;
                    } else if (winNode.com5t().layout() == Layout.RIGHT) {
                        stvKey = PKjson.stvorkaRight;
                    } else if (winNode.com5t().layout() == Layout.TOP) {
                        stvKey = PKjson.stvorkaTop;
                    } else if (winNode.com5t().layout() == Layout.LEFT) {
                        stvKey = PKjson.stvorkaLeft;
                    }
                    JsonObject jso = UJson.getAsJsonObject(paramObj, stvKey);
                    jso.addProperty(colorID, colorRec.getStr(eColor.id));
                    paramStr = gson.toJson(paramObj);
                    parentArea.param(paramStr);
                    updateScript(selectID);

                } else if (winNode.com5t().type() == enums.Type.FRAME_SIDE) {
                    for (GsonElem elem : parentArea.elems()) {
                        if (elem.id() == ((DefMutableTreeNode) winNode).com5t().id()) {
                            String paramStr = elem.param();
                            JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                            paramObj.addProperty(colorID, colorRec.getStr(eColor.id));
                            paramStr = gson.toJson(paramObj);
                            elem.param(paramStr);
                            updateScript(selectID);
                        }
                    }
                } else if (winNode.com5t().type() == enums.Type.IMPOST
                        || winNode.com5t().type() == enums.Type.STOIKA
                        || winNode.com5t().type() == enums.Type.SHTULP) {
                    for (GsonElem elem : parentArea.elems()) {
                        if (elem.id() == ((DefMutableTreeNode) winNode).com5t().id()) {
                            String paramStr = elem.param();
                            JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                            paramObj.addProperty(colorID, colorRec.getStr(eColor.id));
                            paramStr = gson.toJson(paramObj);
                            elem.param(paramStr);
                            updateScript(selectID);
                        }
                    }
                }

            }, colorSet);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToFrame

    private void colorToWindows(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToWindows
        try {
            float selectID = winNode.com5t().id();
            HashSet<Record> set = new HashSet();
            String[] arr1 = (txt15.getText().isEmpty() == false) ? txt15.getText().split(";") : null;
            String jfield = (evt.getSource() == btn9) ? txt3.getText() : (evt.getSource() == btn13) ? txt4.getText() : txt5.getText();
            Integer[] arr2 = UCom.parserInt(jfield);
            if (arr1 != null) {
                for (String s1 : arr1) { //группы
                    HashSet<Record> se2 = new HashSet();
                    boolean b = false;
                    for (Record rec : eColor.query()) {

                        if (rec.getStr(eColor.colgrp_id).equals(s1)) {
                            se2.add(rec); //текстуры группы

                            for (int i = 0; i < arr2.length; i = i + 2) { //тестуры
                                if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                    b = true;
                                }
                            }
                        }
                    }
                    if (b == false) { //если небыло пападаний то добавляем всю группу
                        set.addAll(se2);
                    }
                }
            }
            if (arr2.length != 0) {
                for (Record rec : eColor.query()) {
                    if (arr1 != null) {

                        for (String s1 : arr1) { //группы
                            if (rec.getStr(eColor.colgrp_id).equals(s1)) {
                                for (int i = 0; i < arr2.length; i = i + 2) { //текстуры
                                    if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                        set.add(rec);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < arr2.length; i = i + 2) { //тестуры
                            if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                set.add(rec);
                            }
                        }
                    }
                }
            }

            ListenerRecord listenerColor = (colorRec) -> {

                Wincalc iwin = iwin();
                builder.script.GsonElem rootArea = iwin.rootGson.find(selectID);
                if (rootArea != null) {
                    if (evt.getSource() == btn9) {
                        iwin.rootGson.color1 = colorRec.getInt(eColor.id);
                    } else if (evt.getSource() == btn13) {
                        iwin.rootGson.color2 = colorRec.getInt(eColor.id);
                    } else {
                        iwin.rootGson.color3 = colorRec.getInt(eColor.id);
                    }
                    updateScript(selectID);
                }
            };
            if (arr1 == null && arr2.length == 0) {
                new DicColor(this, listenerColor);
            } else {
                new DicColor(this, listenerColor, set);
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToWindows

    private void artiklToGlass(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_artiklToGlass
        try {
            float selectID = winNode.com5t().id();
            //Список доступных толщин в ветке системы например 4;5;8
            String depth = sysNode.rec().getStr(eSystree.depth);
            if (depth != null && depth.isEmpty() == false) {
                depth = depth.replace(";", ",");
                if (depth.charAt(depth.length() - 1) == ',') {
                    depth = depth.substring(0, depth.length() - 1);
                }
            }
            //Список стеклопакетов
            depth = (depth != null && depth.isEmpty() == false) ? " and " + eArtikl.depth.name() + " in (" + depth + ")" : "";
            Query qArtikl = new Query(eArtikl.values()).select(eArtikl.up,
                    "where", eArtikl.level1, "= 5 and", eArtikl.level2, "in (1,2,3)", depth);

            new DicArtikl(this, (artiklRec) -> {

                GsonElem glassElem = (GsonElem) iwin().rootGson.find(selectID);
                String paramStr = glassElem.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.artglasID, artiklRec.getStr(eArtikl.id));
                paramStr = gson.toJson(paramObj);
                glassElem.param(paramStr);
                updateScript(selectID);

            }, qArtikl);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_artiklToGlass

    private void sysfurnToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysfurnToStvorka
        try {
            float windowsID = winNode.com5t().id();
            String systreeID = sysNode.rec().getStr(eSystree.id);
            Query qSysfurn = new Query(eSysfurn.values(), eFurniture.values()).select(eSysfurn.up, "left join", eFurniture.up, "on",
                    eSysfurn.furniture_id, "=", eFurniture.id, "where", eSysfurn.systree_id, "=", systreeID);

            new DicName(this, (sysfurnRec) -> {

                GsonElem stvArea = (GsonElem) iwin().rootGson.find(windowsID);
                String paramStr = stvArea.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.sysfurnID, sysfurnRec.getStr(eSysfurn.id));
                paramStr = gson.toJson(paramObj);
                stvArea.param(paramStr);
                updateScript(windowsID);

            }, qSysfurn, eFurniture.name);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_sysfurnToStvorka

    private void typeOpenToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeOpenToStvorka
        try {
            new DicEnums(this, (typeopenRec) -> {

                float elemID = winNode.com5t().id();
                GsonElem jsonStv = (GsonElem) iwin().rootGson.find(elemID);
                String paramStr = jsonStv.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.typeOpen, typeopenRec.getInt(0));
                paramStr = gson.toJson(paramObj);
                jsonStv.param(paramStr);
                updateScript(elemID);

            }, TypeOpen1.INVALID, TypeOpen1.LEFT, TypeOpen1.LEFTUP, TypeOpen1.LEFTMOV,
                    TypeOpen1.RIGHT, TypeOpen1.RIGHTUP, TypeOpen1.RIGHTMOV, TypeOpen1.UPPER, TypeOpen1.FIXED);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_typeOpenToStvorka

    private void handlToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handlToStvorka
        try {
            HashSet<Integer> set = new HashSet();
            float selectID = winNode.com5t().id();
            int furnitureID = ((AreaStvorka) winNode.com5t()).sysfurnRec.getInt(eSysfurn.furniture_id);
            Query qFurndetAll = new Query(eFurndet.values()).select(eFurndet.up);
            ArrayList<Record> qFurndet = (ArrayList<Record>) qFurndetAll.stream().filter(rec -> rec.getInt(eFurndet.furniture_id1) == furnitureID).collect(toList());
            Query qArtikl = new Query(eArtikl.values()).select(eArtikl.up, "where", eArtikl.level1, "= 2 and", eArtikl.level2, " = 11");
            qArtikl.forEach(rec -> set.add(rec.getInt(eArtikl.id)));
            Query qResult = new Query(eArtikl.values());
            for (Record furndetRec : qFurndet) { //первый уровень
                if (furndetRec.get(eFurndet.furniture_id2) == null) {
                    if (set.contains(furndetRec.getInt(eFurndet.artikl_id))) {
                        for (Record artiklRec : qArtikl) { //все ручки первого уровня
                            if (furndetRec.getInt(eFurndet.artikl_id) == artiklRec.getInt(eArtikl.id)) {
                                if (artiklRec.getStr(eArtikl.code).charAt(0) != '@') {
                                    qResult.add(artiklRec);
                                }
                            }
                            for (Record furndetRec3 : qFurndetAll) {
                                if (furndetRec3.getInt(eFurndet.furndet_id) == furndetRec.getInt(eFurndet.id)
                                        && furndetRec3.getInt(eFurndet.furndet_id) != furndetRec3.getInt(eFurndet.id)) {
                                    if (set.contains(furndetRec3.getInt(eFurndet.artikl_id))) {
                                        for (Record artiklRec3 : qArtikl) { //все ручки второго уровня
                                            if (furndetRec3.getInt(eFurndet.artikl_id) == artiklRec3.getInt(eArtikl.id)) {
                                                if (artiklRec3.getStr(eArtikl.code).charAt(0) != '@') {
                                                    qResult.add(artiklRec3);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else { //набор в первом уровне т. к. во втором нет смысла
                    for (Record furndetRec2 : qFurndetAll) {
                        if (furndetRec2.getInt(eFurndet.furniture_id2) == furndetRec.getInt(eFurndet.furniture_id2)) {
                            if (set.contains(furndetRec2.getInt(eFurndet.artikl_id))) {
                                for (Record artiklRec2 : qArtikl) { //все ручки первого уровня в наборе
                                    if (furndetRec2.getInt(eFurndet.artikl_id) == artiklRec2.getInt(eArtikl.id)) {
                                        if (artiklRec2.getStr(eArtikl.code).charAt(0) != '@') {
                                            qResult.add(artiklRec2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            new DicArtikl(this, (artiklRec) -> {

                GsonElem stvArea = (GsonElem) iwin().rootGson.find(selectID);
                String paramStr = stvArea.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.artiklHandl, artiklRec.getStr(eArtikl.id));
                paramStr = gson.toJson(paramObj);
                stvArea.param(paramStr);
                updateScript(selectID);

            }, qResult);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_handlToStvorka

    private void heightHandlToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightHandlToStvorka

        AreaStvorka areaStv = (AreaStvorka) winNode.com5t();
        int indexLayoutHandl = 0;
        if (LayoutHandle.CONST.name.equals(txt16.getText())) {
            indexLayoutHandl = 1;
        } else if (LayoutHandle.VARIAT.name.equals(txt16.getText())) {
            indexLayoutHandl = 2;
        }
        new DicHandl(this, (record) -> {
            try {
                float selectID = areaStv.id();
                GsonElem stvArea = (GsonElem) iwin().rootGson.find(selectID);
                String paramStr = stvArea.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);

                if (record.getInt(0) == 0) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.MIDL.id);
                    txt31.setEditable(false);

                } else if (record.getInt(0) == 1) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.CONST.id);
                    txt31.setEditable(false);

                } else if (record.getInt(0) == 2) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.VARIAT.id);
                    paramObj.addProperty(PKjson.heightHandl, record.getInt(1));
                    txt31.setEditable(true);
                }
                paramStr = gson.toJson(paramObj);
                stvArea.param(paramStr);
                updateScript(selectID);

            } catch (Exception e) {
                System.err.println("Ошибка: " + e);
            }

        }, indexLayoutHandl);
    }//GEN-LAST:event_heightHandlToStvorka

    private void colorToHandl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToHandl
        try {
            float selectID = winNode.com5t().id();
            HashSet<Record> colorSet = new HashSet();
            Query artdetList = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.artikl_id, "=", winNode.com5t().artiklRec.getInt(eArtikl.id));
            artdetList.forEach(rec -> {

                if (rec.getInt(eArtdet.color_fk) < 0) {
                    eColor.query().forEach(rec2 -> {
                        if (rec2.getInt(eColor.colgrp_id) == Math.abs(rec.getInt(eArtdet.color_fk))) {
                            colorSet.add(rec2);
                        }
                    });
                } else {
                    colorSet.add(eColor.find(rec.getInt(eArtdet.color_fk)));
                }
            });
            DicColor frame = new DicColor(this, (colorRec) -> {

                GsonElem stvArea = (GsonElem) iwin().rootGson.find(selectID);
                String paramStr = stvArea.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.colorHandl, colorRec.getStr(eColor.id));
                paramStr = gson.toJson(paramObj);
                stvArea.param(paramStr);
                updateScript(selectID);

            }, colorSet);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToHandl

    private void txt24Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt24Update
//        float windowsID = winNode.com5t().id();
//        GsonElem jsonStv = (GsonElem) iwin.rootGson.find(windowsID);
//        jsonStv.widthUp(UCom.getFloat(txt24.getText()));
//        updateScript(windowsID);
    }//GEN-LAST:event_txt24Update

    private void txt26Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt26Update
//        float windowsID = winNode.com5t().id();
//        GsonElem jsonStv = (GsonElem) iwin.rootGson.find(windowsID);
//        jsonStv.heightUp(UCom.getFloat(txt26.getText()));
//        updateScript(windowsID);
    }//GEN-LAST:event_txt26Update

    private void txt17Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt17Update
//        float windowsID = winNode.com5t().id();
//        float dx = UCom.getFloat(txt17.getText()) / iwin.rootGson.width();
//        iwin.rootGson.width(UCom.getFloat(txt17.getText()));
//        iwin.rootGson.widthDown(iwin.rootGson, dx);
//        updateScript(windowsID);
    }//GEN-LAST:event_txt17Update

    private void txt22Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt22Update
//        float windowsID = winNode.com5t().id();
//        float dy = UCom.getFloat(txt22.getText()) / iwin.rootGson.height();
//        iwin.rootGson.height(UCom.getFloat(txt22.getText()));
////        if (iwin.rootGson.heightAdd() != null) {
////            iwin.rootGson.heightAdd(Util.getFloat(txt22.getText()));
////        }
//        iwin.rootGson.heightDown(iwin.rootGson, dy);
//        iwin.rootGson.resizeAll(UCom.getFloat(txt22.getText()), Layout.VERT);
//        updateScript(windowsID);
    }//GEN-LAST:event_txt22Update

    private void txtKeyEnter(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeyEnter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (evt.getSource() == txt17) {
                txt17Update(null);
            } else if (evt.getSource() == txt22) {
                txt22Update(null);
            } else if (evt.getSource() == txt23) {
                txt23Update(null);
            } else if (evt.getSource() == txt24) {
                txt24Update(null);
            } else if (evt.getSource() == txt26) {
                txt26Update(null);
            }
        }
    }//GEN-LAST:event_txtKeyEnter

    private void txt23Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt23Update
//            float windowsID = windowsNode.com5t().id();
//            float dy = Util.getFloat(txt22.getText()) / iwin.rootGson.height();
//            iwin.rootGson.height(Util.getFloat(txt22.getText()));
//            iwin.rootGson.heightAdd(Util.getFloat(txt22.getText()));
//            iwin.rootGson.heightDown(iwin.rootGson, dy);
//            updateScript(windowsID);
    }//GEN-LAST:event_txt23Update

    private void btnTest(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest

    }//GEN-LAST:event_btnTest

    private void joinToFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinToFrame
        try {
            if (winNode != null) {
                Wincalc iwin = iwin();
                DefMutableTreeNode nodeParent = (DefMutableTreeNode) winNode.getParent();
                ElemSimple elem5e = (ElemSimple) nodeParent.com5t();
                JButton btn = (JButton) evt.getSource();
                int k = (btn.getName().equals("btn26")) ? 0 : (btn.getName().equals("btn27")) ? 1 : 2;
                ElemJoining elemJoin = iwin.mapJoin.get(elem5e.joinPoint(k));
                Record joiningRec = eJoining.find(elemJoin.elem1.artiklRecAn, elemJoin.elem2.artiklRecAn);
                Joining joining = new Joining(iwin);
                List<Record> list = joining.varList(elemJoin);
                new DicJoinvar(this, (record) -> {
                    System.out.println(record);
                }, list);
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_joinToFrame

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn15;
    private javax.swing.JButton btn16;
    private javax.swing.JButton btn17;
    private javax.swing.JButton btn18;
    private javax.swing.JButton btn19;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn20;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn24;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn26;
    private javax.swing.JButton btn27;
    private javax.swing.JButton btn28;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JToggleButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport1;
    private javax.swing.JButton btnTest;
    private javax.swing.JPanel centr;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab13;
    private javax.swing.JLabel lab14;
    private javax.swing.JLabel lab15;
    private javax.swing.JLabel lab16;
    private javax.swing.JLabel lab17;
    private javax.swing.JLabel lab18;
    private javax.swing.JLabel lab19;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab20;
    private javax.swing.JLabel lab23;
    private javax.swing.JLabel lab24;
    private javax.swing.JLabel lab27;
    private javax.swing.JLabel lab29;
    private javax.swing.JLabel lab30;
    private javax.swing.JLabel lab31;
    private javax.swing.JLabel lab32;
    private javax.swing.JLabel lab33;
    private javax.swing.JLabel lab34;
    private javax.swing.JLabel lab35;
    private javax.swing.JLabel lab36;
    private javax.swing.JLabel lab37;
    private javax.swing.JLabel lab38;
    private javax.swing.JLabel lab39;
    private javax.swing.JLabel lab40;
    private javax.swing.JLabel lab41;
    private javax.swing.JLabel lab42;
    private javax.swing.JLabel lab45;
    private javax.swing.JLabel lab46;
    private javax.swing.JLabel lab47;
    private javax.swing.JLabel lab48;
    private javax.swing.JLabel lab49;
    private javax.swing.JLabel lab50;
    private javax.swing.JLabel lab51;
    private javax.swing.JLabel lab52;
    private javax.swing.JLabel lab53;
    private javax.swing.JLabel lab54;
    private javax.swing.JLabel lab55;
    private javax.swing.JLabel lab56;
    private javax.swing.JLabel lab57;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan12;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan15;
    private javax.swing.JPanel pan16;
    private javax.swing.JPanel pan17;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan20;
    private javax.swing.JPanel pan21;
    private javax.swing.JPanel pan22;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel panDesign;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JScrollPane scr7;
    private javax.swing.JPanel south;
    private javax.swing.JTree sysTree;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab7;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JPanel tool;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt10;
    private javax.swing.JTextField txt11;
    private javax.swing.JTextField txt13;
    private javax.swing.JTextField txt14;
    private javax.swing.JTextField txt15;
    private javax.swing.JTextField txt16;
    private javax.swing.JTextField txt17;
    private javax.swing.JTextField txt18;
    private javax.swing.JTextField txt19;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt20;
    private javax.swing.JTextField txt21;
    private javax.swing.JTextField txt22;
    private javax.swing.JTextField txt23;
    private javax.swing.JTextField txt24;
    private javax.swing.JTextField txt25;
    private javax.swing.JTextField txt26;
    private javax.swing.JTextField txt27;
    private javax.swing.JTextField txt28;
    private javax.swing.JTextField txt29;
    private javax.swing.JTextField txt3;
    private javax.swing.JTextField txt30;
    private javax.swing.JTextField txt31;
    private javax.swing.JTextField txt32;
    private javax.swing.JTextField txt33;
    private javax.swing.JTextField txt34;
    private javax.swing.JTextField txt35;
    private javax.swing.JTextField txt36;
    private javax.swing.JTextField txt37;
    private javax.swing.JTextField txt38;
    private javax.swing.JTextField txt39;
    private javax.swing.JTextField txt4;
    private javax.swing.JTextField txt40;
    private javax.swing.JTextField txt41;
    private javax.swing.JTextField txt5;
    private javax.swing.JTextField txt7;
    private javax.swing.JTextField txt8;
    private javax.swing.JTextField txt9;
    private javax.swing.JTree winTree;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {

        new FrameToFile(this, btnClose);
        new UColor();
        south.add(filterTable, 0);
        UGui.documentFilter(1, txt2, txt15);
        UGui.documentFilter(2, txt3, txt4, txt5);
        UGui.documentFilter(3, txt17, txt22, txt23, txt24, txt26, txt35);
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> UGui.stopCellEditing(tab2, tab3, tab4, tab5)));
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) sysTree.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b038.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        DefaultTreeCellRenderer rnd2 = (DefaultTreeCellRenderer) winTree.getCellRenderer();
        rnd2.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b038.gif")));
        rnd2.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd2.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        sysTree.getSelectionModel().addTreeSelectionListener(tse -> selectionSys());
        winTree.getSelectionModel().addTreeSelectionListener(tse -> selectionWin());
        tab5.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab5();
                }
            }
        });
        DefaultTreeModel model = (DefaultTreeModel) winTree.getModel();
        ((DefaultMutableTreeNode) model.getRoot()).removeAllChildren();
        model.reload();
    }
}
