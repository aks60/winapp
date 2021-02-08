package frames;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import common.DialogListener;
import common.FrameListener;
import common.FrameProgress;
import common.FrameToFile;
import common.eProperty;
import dataset.ConnApp;
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
import javax.swing.RowFilter;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import frames.swing.BooleanRenderer;
import frames.swing.DefFieldEditor;
import frames.swing.DefTableModel;
import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.AreaStvorka;
import builder.model.ElemSimple;
import builder.script.JsonArea;
import builder.script.JsonElem;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import domain.eArtdet;
import domain.eColor;
import domain.eFurndet;
import enums.LayoutArea;
import enums.PKjson;
import enums.TypeElem;
import enums.TypeOpen1;
import frames.dialog.DicColor2;
import frames.dialog.DicHandl;
import frames.dialog.DicSysprof;
import frames.swing.Canvas;
import frames.swing.DefMutableTreeNode;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import startup.App1;
import startup.App1.eApp1;
import startup.Main;

public class Systree extends javax.swing.JFrame {

    private Wincalc iwin = new Wincalc();
    private int systreeID = -1; //выьранная система
    private int sysprodID = -1; //выбранная конструкция

    private Query qParams = new Query(eParams.values());
    private Query qArtikl = new Query(eArtikl.id, eArtikl.code, eArtikl.name);
    private Query qSystree = new Query(eSystree.values()).select(eSystree.up);
    private Query qSysprod = new Query(eSysprod.values());
    private Query qSysprof = new Query(eSysprof.values(), eArtikl.values());
    private Query qSysfurn = new Query(eSysfurn.values(), eFurniture.values());
    private Query qSyspar1 = new Query(eSyspar1.values());
    private DialogListener listenerArtikl, listenerModel, listenerModify, listenerFurn,
            listenerParam1, listenerParam2, listenerArt211, listenerArt212;
    private Canvas paintPanel = new Canvas(iwin);
    private DefMutableTreeNode rootTree = null;
    private DefFieldEditor rsvSystree;
    private java.awt.Frame frame = null;
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
        for (int i = 0; i < qSysprof.size(); i++) {
            if (qSysprof.get(i).getInt(eSysprof.artikl_id) == artiklID) {
                Util.setSelectedRow(tab2, i);
            }
        }
    }

    private void loadingData() {

        systreeID = Integer.valueOf(eProperty.systreeID.read());
        sysprodID = Integer.valueOf(eProperty.sysprodID.read());
        qParams.select(eParams.up, "where", eParams.id, "< 0").table(eParams.up);
        qArtikl.select(eArtikl.up, "where", eArtikl.level1, "= 2 and", eArtikl.level2, "in (11,12)");

        ((DefaultTreeCellEditor) treeSys.getCellEditor()).addCellEditorListener(new CellEditorListener() {

            public void editingStopped(ChangeEvent e) {
                DefMutableTreeNode node = (DefMutableTreeNode) treeSys.getLastSelectedPathComponent();
                String str = ((DefaultTreeCellEditor) treeSys.getCellEditor()).getCellEditorValue().toString();
                node.rec().set(eSystree.name, str);
                node.setUserObject(str);
                txt08.setText(str);
                qSystree.update(node.rec()); //сохраним в базе
            }

            public void editingCanceled(ChangeEvent e) {
                editingStopped(e);
            }
        });
    }

    private void loadingSys() {
        Record recordRoot = eSystree.up.newRecord(Query.SEL);
        recordRoot.set(eSystree.id, 0);
        recordRoot.set(eSystree.parent_id, 0);
        recordRoot.set(eSystree.name, "Дерево системы профилей");
        rootTree = new DefMutableTreeNode(recordRoot);
        ArrayList<DefMutableTreeNode> treeList = new ArrayList();
        Query q = qSystree.table(eSystree.up);
        for (Record record : q) {
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
        treeSys.setModel(new DefaultTreeModel(rootTree));
        scr1.setViewportView(treeSys);
    }

    private void loadingModel() {

        DefTableModel rsmSysprof = new DefTableModel(tab2, qSysprof, eSysprof.use_type, eSysprof.use_side, eArtikl.code, eArtikl.name, eSysprof.prio) {

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
        tab4.getColumnModel().getColumn(2).setCellRenderer(new BooleanRenderer());
        tab5.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1) {
                    Icon icon = (Icon) value;
                    label.setIcon(icon);
                } else {
                    label.setIcon(null);
                }
                return label;
            }
        });

        rsmSysprof.setFrameListener(listenerModify);
        rsvSystree = new DefFieldEditor(treeSys);
        rsvSystree.add(eSystree.name, txt08);
        rsvSystree.add(eSystree.types, txt07, TypeUse.values());
        rsvSystree.add(eSystree.glas, txt01);
        rsvSystree.add(eSystree.depth, txt02);
        rsvSystree.add(eSystree.col1, txt03);
        rsvSystree.add(eSystree.col2, txt04);
        rsvSystree.add(eSystree.col3, txt05);
        rsvSystree.add(eSystree.id, txt06);
        rsvSystree.add(eSystree.pref, txt10);
        rsvSystree.add(eSystree.imgview, txt11, LayoutProduct.values());
        rsvSystree.add(eSystree.nuni, txt12);
        rsvSystree.add(eSystree.cgrp, txt15);
        panDesign.add(paintPanel, java.awt.BorderLayout.CENTER);
        paintPanel.setVisible(true);
        if (selectedPath != null) {
            treeSys.setSelectionPath(new TreePath(selectedPath));
        } else {
            treeSys.setSelectionRow(0);
        }
    }

    private void loadingWin() {
        try {
            int row[] = treeWin.getSelectionRows();
            DefMutableTreeNode root = new DefMutableTreeNode(iwin.rootArea);
            Set<AreaSimple> set = new HashSet();
            for (ElemSimple elem5e : iwin.listElem) {
                if (elem5e.owner().type() != TypeElem.STVORKA) {
                    root.add(new DefMutableTreeNode(elem5e));
                } else {
                    set.add(elem5e.owner());
                }
            }
            for (AreaSimple areaStv : set) {
                DefMutableTreeNode nodeStv = new DefMutableTreeNode(areaStv);
                root.add(nodeStv);
                for (ElemSimple elemStv : iwin.listElem) {
                    if (elemStv.owner() == areaStv) {
                        nodeStv.add(new DefMutableTreeNode(elemStv));
                    }
                }
            }
            treeWin.setModel(new DefaultTreeModel(root));
            treeWin.setSelectionRows(row);

        } catch (Exception e) {
            System.err.println("Ошибка: Systree.loadingWin() " + e);
        }
    }

    private void loadingTab5() {

        qSysprod.select(eSysprod.up, "where", eSysprod.systree_id, "=", systreeID);
        DefaultTableModel dtm5 = (DefaultTableModel) tab5.getModel();
        dtm5.getDataVector().removeAllElements();
        int length = 68;
        for (Record record : qSysprod.table(eSysprod.up)) {
            try {
                Object arrayRec[] = {record.get(eSysprod.name), null};
                Object script = record.get(eSysprod.script);
                iwin.build(script.toString());
                BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
                iwin.gc2d = bi.createGraphics();
                iwin.gc2d.fillRect(0, 0, length, length);
                iwin.scale = (length / iwin.width > length / iwin.heightAdd) ? length / (iwin.heightAdd + 200) : length / (iwin.width + 200);
                iwin.gc2d.translate(2, 2);
                iwin.gc2d.scale(iwin.scale, iwin.scale);
                iwin.rootArea.draw(length, length);
                ImageIcon image = new ImageIcon(bi);
                arrayRec[1] = image;
                dtm5.addRow(arrayRec);

            } catch (Exception e) {
                System.err.println("Ошибка:Systree.loadingTab5() " + e);
            }
        }
    }

    private void listenerAdd() {
        Util.buttonEditorCell(tab2, 0).addActionListener(event -> {
            new DicEnums(this, (record) -> {
                Util.listenerEnums(record, tab2, eSysprof.use_type, tab2, tab3, tab4);
            }, UseArtiklTo.values());
        });

        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            new DicEnums(this, (record) -> {
                Util.listenerEnums(record, tab2, eSysprof.use_side, tab2, tab3, tab4);
            }, UseSide.values());
        });

        Util.buttonEditorCell(tab2, 2).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab2, 3).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab3, 1).addActionListener(event -> {
            DicName frame = new DicName(this, listenerFurn, new Query(eFurniture.values()).select(eFurniture.up, "order by", eFurniture.name), eFurniture.name);
        });

        Util.buttonEditorCell(tab3, 2).addActionListener(event -> {
            DicEnums frame = new DicEnums(this, (record) -> {
                Util.listenerEnums(record, tab3, eSysfurn.side_open, tab2, tab3, tab4, tab5);
            }, TypeOpen2.values());
        });

        Util.buttonEditorCell(tab3, 4).addActionListener(event -> {
            DicEnums frame = new DicEnums(this, (record) -> {
                Util.listenerEnums(record, tab3, eSysfurn.hand_pos, tab2, tab3, tab4, tab5);
            }, LayoutHandle.values());
        });

        Util.buttonEditorCell(tab3, 5).addActionListener(event -> {
            int furnityreId = qSysfurn.getAs(Util.getSelectedRec(tab3), eSysfurn.furniture_id);
            DicArtikl artikl = new DicArtikl(this, listenerArt211, furnityreId, TypeArtikl.FURNRUCHKA.id1, TypeArtikl.FURNRUCHKA.id2);
        });

        Util.buttonEditorCell(tab3, 6).addActionListener(event -> {
            int furnityreId = qSysfurn.getAs(Util.getSelectedRec(tab3), eSysfurn.furniture_id);
            DicArtikl artikl = new DicArtikl(this, listenerArt212, furnityreId, TypeArtikl.FURNLOOP.id1, TypeArtikl.FURNLOOP.id2);
        });

        Util.buttonEditorCell(tab4, 0).addActionListener(event -> {
            ParDefault frame = new ParDefault(this, listenerParam1);
        });

        Util.buttonEditorCell(tab4, 1).addActionListener(event -> {
            Integer grup = qSyspar1.getAs(Util.getSelectedRec(tab4), eSyspar1.params_id);
            ParDefault frame = new ParDefault(this, listenerParam2, grup);
        });
    }

    private void listenerSet() {

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab2);
            qSysprof.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab2), eSysprof.artikl_id);
            qSysprof.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab2), eArtikl.name);
            qSysprof.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab2), eArtikl.code);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, row);
        };
        listenerModel = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            Record record2 = eSysprod.up.newRecord(Query.INS);
            record2.setNo(eSysprod.id, ConnApp.instanc().genId(eSysprod.id));
            record2.setNo(eSysprod.systree_id, systreeID);
            record2.setNo(eSysprod.name, record.get(1));
            record2.setNo(eSysprod.script, record.get(2));
            qSysprod.table(eSysprod.up).insert(record2);
            loadingTab5();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qSysprod, tab5);
        };

        listenerFurn = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            qSysfurn.set(record.getInt(eFurniture.id), Util.getSelectedRec(tab3), eSysfurn.furniture_id);
            qSysfurn.table(eFurniture.up).set(record.get(eFurniture.name), Util.getSelectedRec(tab3), eFurniture.name);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerArt211 = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            qSysfurn.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab3), eSysfurn.artikl_id1);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerArt212 = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            qSysfurn.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab3), eSysfurn.artikl_id2);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerParam1 = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab4);
            qSyspar1.set(record.getInt(eParams.id), Util.getSelectedRec(tab4), eSyspar1.params_id);
            qSyspar1.set(null, Util.getSelectedRec(tab4), eSyspar1.text);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, row);
        };

        listenerParam2 = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab4);
            qSyspar1.set(record.getStr(eParams.text), Util.getSelectedRec(tab4), eSyspar1.text);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, row);
        };
    }

    private void selectionSys() {
        DefMutableTreeNode node = (DefMutableTreeNode) treeSys.getLastSelectedPathComponent();
        if (node != null) {

            systreeID = node.rec().getInt(eSystree.id);
            eProperty.systreeID.write(String.valueOf(systreeID));

            rsvSystree.load();

            qSysprof.select(eSysprof.up, "left join", eArtikl.up, "on", eArtikl.id, "=",
                    eSysprof.artikl_id, "where", eSysprof.systree_id, "=", node.rec().getInt(eSystree.id), "order by", eSysprof.use_type, ",", eSysprof.prio);
            qSysfurn.select(eSysfurn.up, "left join", eFurniture.up, "on", eFurniture.id, "=",
                    eSysfurn.furniture_id, "where", eSysfurn.systree_id, "=", node.rec().getInt(eSystree.id), "order by", eSysfurn.npp);
            qSyspar1.select(eSyspar1.up, "where", eSyspar1.systree_id, "=", node.rec().getInt(eSystree.id));

            loadingTab5();

            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
            Util.setSelectedRow(tab3);
            Util.setSelectedRow(tab4);

            int index = -1;
            for (int row = 0; row < qSysprod.size(); ++row) {
                if (qSysprod.get(row).getInt(eSysprod.id) == sysprodID) {
                    index = row;
                }
            }
            if (index != -1) {
                Util.setSelectedRow(tab5, index);
            } else {
                Util.setSelectedRow(tab5);
            }

        } else {
            //createWincalc(-1); //рисуем виртуалку
        }
    }

    private void selectionWin() {
        DefMutableTreeNode node = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
        if (node != null) {

            //Основные
            if (node.com5t().type() == TypeElem.RECTANGL || node.com5t().type() == TypeElem.ARCH) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card12");
                ((TitledBorder) pan12.getBorder()).setTitle(iwin.rootArea.type().name);
                pan12.repaint();
                txt09.setText(eColor.find(iwin.colorID1).getStr(eColor.name));
                txt13.setText(eColor.find(iwin.colorID2).getStr(eColor.name));
                txt14.setText(eColor.find(iwin.colorID3).getStr(eColor.name));

                //Рама, импост...
            } else if (node.com5t().type() == TypeElem.FRAME_SIDE
                    || node.com5t().type() == TypeElem.STVORKA_SIDE || node.com5t().type() == TypeElem.IMPOST) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card13");
                ((TitledBorder) pan13.getBorder()).setTitle(node.toString());
                txt32.setText(node.com5t().artiklRec.getStr(eArtikl.code));
                txt33.setText(node.com5t().artiklRec.getStr(eArtikl.name));
                txt27.setText(eColor.find(node.com5t().colorID1).getStr(eColor.name));
                txt28.setText(eColor.find(node.com5t().colorID2).getStr(eColor.name));
                txt29.setText(eColor.find(node.com5t().colorID3).getStr(eColor.name));

                //Стеклопакет
            } else if (node.com5t().type() == TypeElem.GLASS) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card15");
                Record artiklRec = eArtikl.find(node.com5t().artiklRec.getInt(eArtikl.id), false);
                txt19.setText(artiklRec.getStr(eArtikl.code));
                txt18.setText(artiklRec.getStr(eArtikl.name));

                //Створка
            } else if (node.com5t().type() == TypeElem.STVORKA) {
                ((CardLayout) pan7.getLayout()).show(pan7, "card16");
                AreaStvorka stv = (AreaStvorka) node.com5t();
                int id = stv.sysfurnRec.getInt(eSysfurn.furniture_id);
                txt20.setText(eFurniture.find(id).getStr(eFurniture.name));
                txt30.setText(stv.typeOpen.name2);
                txt16.setText(stv.handlLayout.name);
                if (stv.handlLayout == LayoutHandle.SET) {
                    txt31.setEditable(true);
                    txt31.setText(String.valueOf(stv.handlHeight));
                } else {
                    txt31.setEditable(false);
                    txt31.setText("");
                }
                iwin.calcFurniture = new builder.specif.Furniture(iwin, true); //фурнитура 
                iwin.calcFurniture.calc();
                txt21.setText(stv.handlRec.getStr(eArtikl.name));
                txt25.setText(eColor.find(stv.handlColor).getStr(eColor.name));
            }
            Arrays.asList(txt09, txt13, txt14, txt27, txt28,
                    txt29, txt19, txt20, txt30).forEach(it -> it.setCaretPosition(0));
            Arrays.asList(pan12, pan13, pan15, pan16).forEach(it -> it.repaint());
        }
    }

    private void selectionTab5() {
        int row = Util.getSelectedRec(tab5);
        if (row != -1) {
            Record sysprodRec = qSysprod.table(eSysprod.up).get(row);
            String script = sysprodRec.getStr(eSysprod.script);
            eProperty.sysprodID.write(sysprodRec.getStr(eSysprod.id));
            eApp1.App1.frame.setTitle(getTitle() + Util.designName());

            //Калькуляция и прорисовка окна
            if (script != null && script.isEmpty() == false) {
                JsonElement script2 = new Gson().fromJson(script, JsonElement.class);
                script2.getAsJsonObject().addProperty("nuni", systreeID); //запишем nuni в script
                iwin.build(script2.toString()); //калькуляция изделия
                paintPanel.repaint(true);
                loadingWin();

            } else {
                Graphics2D g = (Graphics2D) paintPanel.getGraphics();
                g.clearRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
            }
        }
    }

    private ArrayList<DefMutableTreeNode> addChild(ArrayList<DefMutableTreeNode> nodeList1, ArrayList<DefMutableTreeNode> nodeList2) {

        Query systreeList = qSystree.table(eSystree.up);
        for (DefMutableTreeNode node : nodeList1) {
            String userNode = (String) node.getUserObject();
            for (Record record2 : systreeList) {
                if (record2.getInt(eSystree.parent_id) == node.rec().getInt(eSystree.id)
                        && record2.getInt(eSystree.parent_id) != record2.getInt(eSystree.id)) {
                    DefMutableTreeNode node2 = new DefMutableTreeNode(record2);
                    node.add(node2);
                    nodeList2.add(node2);
                    if (record2.getInt(eSystree.id) == systreeID) {
                        selectedPath = node2.getPath(); //запомним path для nuni
                    }
                }
            }
        }
        return nodeList2;
    }

    private void updateScript(float selectID) {
        String script = gson.toJson(iwin.jsonRoot);
        Record sysprodRec = qSysprod.get(Util.getSelectedRec(tab5));
        sysprodRec.set(eSysprod.script, script);
        qSysprod.update(sysprodRec);
        selectionTab5();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeWin.getModel().getRoot();
        do {
            if (selectID == ((DefMutableTreeNode) node).com5t().id()) {
                TreePath path = new TreePath(node.getPath());
                treeWin.setSelectionPath(path);
                treeWin.scrollPathToVisible(path);
            }
            node = node.getNextNode();
        } while (node != null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        treeSys = new javax.swing.JTree();
        pan1 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        panDesign = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        pan12 = new javax.swing.JPanel();
        lab25 = new javax.swing.JLabel();
        txt12 = new javax.swing.JFormattedTextField();
        lab26 = new javax.swing.JLabel();
        txt06 = new javax.swing.JFormattedTextField();
        pan21 = new javax.swing.JPanel();
        lab27 = new javax.swing.JLabel();
        lab31 = new javax.swing.JLabel();
        lab32 = new javax.swing.JLabel();
        btn09 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn02 = new javax.swing.JButton();
        txt09 = new javax.swing.JTextField();
        txt13 = new javax.swing.JTextField();
        txt14 = new javax.swing.JTextField();
        pan13 = new javax.swing.JPanel();
        pan20 = new javax.swing.JPanel();
        lab28 = new javax.swing.JLabel();
        lab43 = new javax.swing.JLabel();
        lab44 = new javax.swing.JLabel();
        btn18 = new javax.swing.JButton();
        btn19 = new javax.swing.JButton();
        btn20 = new javax.swing.JButton();
        txt27 = new javax.swing.JTextField();
        txt28 = new javax.swing.JTextField();
        txt29 = new javax.swing.JTextField();
        lab33 = new javax.swing.JLabel();
        lab34 = new javax.swing.JLabel();
        txt32 = new javax.swing.JTextField();
        txt33 = new javax.swing.JTextField();
        btn22 = new javax.swing.JButton();
        pan15 = new javax.swing.JPanel();
        lab29 = new javax.swing.JLabel();
        lab36 = new javax.swing.JLabel();
        btn03 = new javax.swing.JButton();
        txt19 = new javax.swing.JTextField();
        txt18 = new javax.swing.JTextField();
        pan16 = new javax.swing.JPanel();
        lab30 = new javax.swing.JLabel();
        lab37 = new javax.swing.JLabel();
        btn10 = new javax.swing.JButton();
        lab38 = new javax.swing.JLabel();
        lab39 = new javax.swing.JLabel();
        btn12 = new javax.swing.JButton();
        btn01 = new javax.swing.JButton();
        lab40 = new javax.swing.JLabel();
        lab41 = new javax.swing.JLabel();
        lab42 = new javax.swing.JLabel();
        btn14 = new javax.swing.JButton();
        btn15 = new javax.swing.JButton();
        btn16 = new javax.swing.JButton();
        btn17 = new javax.swing.JButton();
        lab45 = new javax.swing.JLabel();
        btn21 = new javax.swing.JButton();
        txt20 = new javax.swing.JTextField();
        txt30 = new javax.swing.JTextField();
        txt25 = new javax.swing.JTextField();
        txt21 = new javax.swing.JTextField();
        txt22 = new javax.swing.JTextField();
        txt24 = new javax.swing.JTextField();
        txt23 = new javax.swing.JTextField();
        txt26 = new javax.swing.JTextField();
        lab46 = new javax.swing.JLabel();
        txt31 = new javax.swing.JTextField();
        txt16 = new javax.swing.JTextField();
        btn06 = new javax.swing.JButton();
        tabb1 = new javax.swing.JTabbedPane();
        pan6 = new javax.swing.JPanel();
        lab13 = new javax.swing.JLabel();
        txt01 = new javax.swing.JFormattedTextField();
        lab14 = new javax.swing.JLabel();
        lab15 = new javax.swing.JLabel();
        lab16 = new javax.swing.JLabel();
        lab17 = new javax.swing.JLabel();
        lab19 = new javax.swing.JLabel();
        txt07 = new javax.swing.JFormattedTextField();
        lab20 = new javax.swing.JLabel();
        txt08 = new javax.swing.JTextField();
        btn04 = new javax.swing.JButton();
        txt02 = new javax.swing.JTextField();
        txt03 = new javax.swing.JTextField();
        txt04 = new javax.swing.JTextField();
        txt05 = new javax.swing.JTextField();
        lab23 = new javax.swing.JLabel();
        txt10 = new javax.swing.JTextField();
        txt11 = new javax.swing.JTextField();
        lab24 = new javax.swing.JLabel();
        btn11 = new javax.swing.JButton();
        btn07 = new javax.swing.JButton();
        lab18 = new javax.swing.JLabel();
        txt15 = new javax.swing.JTextField();
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
        treeWin = new javax.swing.JTree();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();
        tool = new javax.swing.JPanel();
        btnIns = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btn05 = new javax.swing.JToggleButton();
        btnReport1 = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Системы профилей.");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Systree.this.windowClosed(evt);
            }
        });

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setPreferredSize(new java.awt.Dimension(800, 450));
        centr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(260, 550));

        treeSys.setEditable(true);
        treeSys.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                treeSysMousePressed(evt);
            }
        });
        scr1.setViewportView(treeSys);

        centr.add(scr1, java.awt.BorderLayout.WEST);

        pan1.setPreferredSize(new java.awt.Dimension(540, 550));
        pan1.setLayout(new java.awt.GridLayout(2, 1));

        pan2.setPreferredSize(new java.awt.Dimension(540, 200));
        pan2.setLayout(new java.awt.GridLayout(1, 2));

        panDesign.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panDesign.setLayout(new java.awt.BorderLayout());
        pan2.add(panDesign);

        pan7.setPreferredSize(new java.awt.Dimension(300, 200));
        pan7.setLayout(new java.awt.CardLayout());

        pan12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Основные", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan12.setToolTipText("");
        pan12.setPreferredSize(new java.awt.Dimension(300, 200));

        lab25.setFont(frames.Util.getFont(0,0));
        lab25.setText("NUNI-PS4");
        lab25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab25.setPreferredSize(new java.awt.Dimension(40, 18));

        txt12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txt12.setPreferredSize(new java.awt.Dimension(40, 18));

        lab26.setFont(frames.Util.getFont(0,0));
        lab26.setText("NUNI-PS5");
        lab26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab26.setPreferredSize(new java.awt.Dimension(40, 18));

        txt06.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt06.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txt06.setPreferredSize(new java.awt.Dimension(40, 18));

        pan21.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Текстура изделия", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 0)));
        pan21.setPreferredSize(new java.awt.Dimension(308, 104));

        lab27.setFont(frames.Util.getFont(0,0));
        lab27.setText("Основная");
        lab27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab27.setPreferredSize(new java.awt.Dimension(80, 18));

        lab31.setFont(frames.Util.getFont(0,0));
        lab31.setText("Внутренняя");
        lab31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab31.setPreferredSize(new java.awt.Dimension(80, 18));

        lab32.setFont(frames.Util.getFont(0,0));
        lab32.setText("Внешняя");
        lab32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab32.setPreferredSize(new java.awt.Dimension(80, 18));

        btn09.setText("...");
        btn09.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn09.setMaximumSize(new java.awt.Dimension(18, 18));
        btn09.setMinimumSize(new java.awt.Dimension(18, 18));
        btn09.setName("btn09"); // NOI18N
        btn09.setPreferredSize(new java.awt.Dimension(18, 18));
        btn09.addActionListener(new java.awt.event.ActionListener() {
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

        btn02.setText("...");
        btn02.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn02.setMaximumSize(new java.awt.Dimension(18, 18));
        btn02.setMinimumSize(new java.awt.Dimension(18, 18));
        btn02.setName("btn02"); // NOI18N
        btn02.setPreferredSize(new java.awt.Dimension(18, 18));
        btn02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        txt09.setEditable(false);
        txt09.setBackground(new java.awt.Color(255, 255, 255));
        txt09.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt09.setPreferredSize(new java.awt.Dimension(180, 18));

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
                        .addComponent(txt14, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt09, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt13, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn09, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        pan21Layout.setVerticalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan21Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn09, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt09, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pan12Layout = new javax.swing.GroupLayout(pan12);
        pan12.setLayout(pan12Layout);
        pan12Layout.setHorizontalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pan21, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
            .addGroup(pan12Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(lab25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan12Layout.setVerticalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan12Layout.createSequentialGroup()
                .addComponent(pan21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        pan7.add(pan12, "card12");

        pan13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Рама, импост..", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan13.setPreferredSize(new java.awt.Dimension(300, 200));

        pan20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Текстура элемента", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 0)));
        pan20.setPreferredSize(new java.awt.Dimension(308, 104));

        lab28.setFont(frames.Util.getFont(0,0));
        lab28.setText("Основная");
        lab28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab28.setPreferredSize(new java.awt.Dimension(80, 18));

        lab43.setFont(frames.Util.getFont(0,0));
        lab43.setText("Внутренняя");
        lab43.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab43.setPreferredSize(new java.awt.Dimension(80, 18));

        lab44.setFont(frames.Util.getFont(0,0));
        lab44.setText("Внешняя");
        lab44.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab44.setPreferredSize(new java.awt.Dimension(80, 18));

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

        txt27.setEditable(false);
        txt27.setBackground(new java.awt.Color(255, 255, 255));
        txt27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt27.setPreferredSize(new java.awt.Dimension(180, 18));

        txt28.setEditable(false);
        txt28.setBackground(new java.awt.Color(255, 255, 255));
        txt28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt28.setPreferredSize(new java.awt.Dimension(180, 18));

        txt29.setEditable(false);
        txt29.setBackground(new java.awt.Color(255, 255, 255));
        txt29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt29.setPreferredSize(new java.awt.Dimension(180, 18));

        javax.swing.GroupLayout pan20Layout = new javax.swing.GroupLayout(pan20);
        pan20.setLayout(pan20Layout);
        pan20Layout.setHorizontalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan20Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan20Layout.createSequentialGroup()
                        .addComponent(lab44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan20Layout.createSequentialGroup()
                        .addComponent(lab28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan20Layout.createSequentialGroup()
                        .addComponent(lab43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        pan20Layout.setVerticalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan20Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lab33.setFont(frames.Util.getFont(0,0));
        lab33.setText("  Артикул");
        lab33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab33.setPreferredSize(new java.awt.Dimension(80, 18));

        lab34.setFont(frames.Util.getFont(0,0));
        lab34.setText("  Название");
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

        javax.swing.GroupLayout pan13Layout = new javax.swing.GroupLayout(pan13);
        pan13.setLayout(pan13Layout);
        pan13Layout.setHorizontalGroup(
            pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan20, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addGroup(pan13Layout.createSequentialGroup()
                        .addComponent(lab33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt32, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan13Layout.createSequentialGroup()
                        .addComponent(lab34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        pan7.add(pan13, "card13");

        pan15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Стеклопакет", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan15.setPreferredSize(new java.awt.Dimension(300, 200));

        lab29.setFont(frames.Util.getFont(0,0));
        lab29.setText("Артикул");
        lab29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab29.setPreferredSize(new java.awt.Dimension(80, 18));

        lab36.setFont(frames.Util.getFont(0,0));
        lab36.setText("Название");
        lab36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab36.setPreferredSize(new java.awt.Dimension(80, 18));

        btn03.setText("...");
        btn03.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn03.setMaximumSize(new java.awt.Dimension(18, 18));
        btn03.setMinimumSize(new java.awt.Dimension(18, 18));
        btn03.setName("btnField17"); // NOI18N
        btn03.setPreferredSize(new java.awt.Dimension(18, 18));
        btn03.addActionListener(new java.awt.event.ActionListener() {
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
                        .addComponent(txt19, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan15Layout.createSequentialGroup()
                        .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan15Layout.setVerticalGroup(
            pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(186, Short.MAX_VALUE))
        );

        pan7.add(pan15, "card15");

        pan16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Створка", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan16.setPreferredSize(new java.awt.Dimension(3100, 200));

        lab30.setFont(frames.Util.getFont(0,0));
        lab30.setText("Фурнитура");
        lab30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab30.setPreferredSize(new java.awt.Dimension(80, 18));

        lab37.setFont(frames.Util.getFont(0,0));
        lab37.setText("Ручка");
        lab37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab37.setPreferredSize(new java.awt.Dimension(80, 18));

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

        lab38.setFont(frames.Util.getFont(0,0));
        lab38.setText("Подвес");
        lab38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab38.setPreferredSize(new java.awt.Dimension(80, 18));

        lab39.setFont(frames.Util.getFont(0,0));
        lab39.setText("Текстура");
        lab39.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab39.setPreferredSize(new java.awt.Dimension(80, 18));

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

        btn01.setText("...");
        btn01.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn01.setMaximumSize(new java.awt.Dimension(18, 18));
        btn01.setMinimumSize(new java.awt.Dimension(18, 18));
        btn01.setName("btnField17"); // NOI18N
        btn01.setPreferredSize(new java.awt.Dimension(18, 18));

        lab40.setFont(frames.Util.getFont(0,0));
        lab40.setText("Замок");
        lab40.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab40.setPreferredSize(new java.awt.Dimension(80, 18));

        lab41.setFont(frames.Util.getFont(0,0));
        lab41.setText("Текстура");
        lab41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab41.setPreferredSize(new java.awt.Dimension(80, 18));

        lab42.setFont(frames.Util.getFont(0,0));
        lab42.setText("Текстура");
        lab42.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab42.setPreferredSize(new java.awt.Dimension(80, 18));

        btn14.setText("...");
        btn14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn14.setMaximumSize(new java.awt.Dimension(18, 18));
        btn14.setMinimumSize(new java.awt.Dimension(18, 18));
        btn14.setName("btnField17"); // NOI18N
        btn14.setPreferredSize(new java.awt.Dimension(18, 18));

        btn15.setText("...");
        btn15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn15.setMaximumSize(new java.awt.Dimension(18, 18));
        btn15.setMinimumSize(new java.awt.Dimension(18, 18));
        btn15.setName("btnField17"); // NOI18N
        btn15.setPreferredSize(new java.awt.Dimension(18, 18));

        btn16.setText("...");
        btn16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn16.setMaximumSize(new java.awt.Dimension(18, 18));
        btn16.setMinimumSize(new java.awt.Dimension(18, 18));
        btn16.setName("btnField17"); // NOI18N
        btn16.setPreferredSize(new java.awt.Dimension(18, 18));

        btn17.setText("...");
        btn17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn17.setMaximumSize(new java.awt.Dimension(18, 18));
        btn17.setMinimumSize(new java.awt.Dimension(18, 18));
        btn17.setName("btn17"); // NOI18N
        btn17.setPreferredSize(new java.awt.Dimension(18, 18));

        lab45.setFont(frames.Util.getFont(0,0));
        lab45.setText("Напр. откр.");
        lab45.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab45.setPreferredSize(new java.awt.Dimension(80, 18));

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

        txt22.setEditable(false);
        txt22.setBackground(new java.awt.Color(204, 204, 204));
        txt22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt22.setPreferredSize(new java.awt.Dimension(180, 18));

        txt24.setEditable(false);
        txt24.setBackground(new java.awt.Color(204, 204, 204));
        txt24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt24.setPreferredSize(new java.awt.Dimension(180, 18));

        txt23.setEditable(false);
        txt23.setBackground(new java.awt.Color(204, 204, 204));
        txt23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt23.setPreferredSize(new java.awt.Dimension(180, 18));

        txt26.setEditable(false);
        txt26.setBackground(new java.awt.Color(204, 204, 204));
        txt26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt26.setPreferredSize(new java.awt.Dimension(180, 18));

        lab46.setFont(frames.Util.getFont(0,0));
        lab46.setText("Высота ручки");
        lab46.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab46.setPreferredSize(new java.awt.Dimension(80, 18));

        txt31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt31.setPreferredSize(new java.awt.Dimension(180, 18));

        txt16.setEditable(false);
        txt16.setBackground(new java.awt.Color(255, 255, 255));
        txt16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt16.setPreferredSize(new java.awt.Dimension(180, 18));

        btn06.setText("...");
        btn06.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn06.setMaximumSize(new java.awt.Dimension(18, 18));
        btn06.setMinimumSize(new java.awt.Dimension(18, 18));
        btn06.setName("btnField17"); // NOI18N
        btn06.setPreferredSize(new java.awt.Dimension(18, 18));
        btn06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightHandlToStvorka(evt);
            }
        });

        javax.swing.GroupLayout pan16Layout = new javax.swing.GroupLayout(pan16);
        pan16.setLayout(pan16Layout);
        pan16Layout.setHorizontalGroup(
            pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lab39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt21, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                            .addComponent(txt25, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(txt22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn01, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan16Layout.createSequentialGroup()
                        .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(btn06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lab40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan16Layout.createSequentialGroup()
                                .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        pan16Layout.setVerticalGroup(
            pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan16Layout.createSequentialGroup()
                .addContainerGap()
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
                    .addComponent(btn06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pan7.add(pan16, "card16");

        pan2.add(pan7);

        pan1.add(pan2);

        tabb1.setPreferredSize(new java.awt.Dimension(540, 250));
        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabb1StateChanged(evt);
            }
        });

        lab13.setFont(frames.Util.getFont(0,0));
        lab13.setText("Зап-ие по умолчанию");
        lab13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab13.setPreferredSize(new java.awt.Dimension(112, 18));

        txt01.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt01.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txt01.setPreferredSize(new java.awt.Dimension(156, 18));

        lab14.setFont(frames.Util.getFont(0,0));
        lab14.setText("Доступные толщины");
        lab14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab14.setPreferredSize(new java.awt.Dimension(120, 18));

        lab15.setFont(frames.Util.getFont(0,0));
        lab15.setText("Основная текстура");
        lab15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab15.setPreferredSize(new java.awt.Dimension(112, 18));

        lab16.setFont(frames.Util.getFont(0,0));
        lab16.setText("Внутр. текстура");
        lab16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab16.setPreferredSize(new java.awt.Dimension(112, 18));

        lab17.setFont(frames.Util.getFont(0,0));
        lab17.setText("Внешняя текстура");
        lab17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab17.setPreferredSize(new java.awt.Dimension(112, 18));

        lab19.setFont(frames.Util.getFont(0,0));
        lab19.setText("Признак системы");
        lab19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab19.setMaximumSize(new java.awt.Dimension(112, 18));
        lab19.setMinimumSize(new java.awt.Dimension(112, 18));
        lab19.setPreferredSize(new java.awt.Dimension(112, 18));

        txt07.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt07.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txt07.setPreferredSize(new java.awt.Dimension(156, 18));

        lab20.setFont(frames.Util.getFont(0,0));
        lab20.setText("Система");
        lab20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab20.setPreferredSize(new java.awt.Dimension(112, 18));

        txt08.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt08.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt08.setFocusable(false);
        txt08.setPreferredSize(new java.awt.Dimension(450, 18));

        btn04.setText("...");
        btn04.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn04.setMaximumSize(new java.awt.Dimension(18, 18));
        btn04.setMinimumSize(new java.awt.Dimension(18, 18));
        btn04.setPreferredSize(new java.awt.Dimension(18, 18));
        btn04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glasdefToSystree(evt);
            }
        });

        txt02.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt02.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt02.setPreferredSize(new java.awt.Dimension(180, 18));

        txt03.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt03.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt03.setPreferredSize(new java.awt.Dimension(180, 18));

        txt04.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt04.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt04.setPreferredSize(new java.awt.Dimension(180, 18));

        txt05.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt05.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt05.setPreferredSize(new java.awt.Dimension(180, 18));

        lab23.setFont(frames.Util.getFont(0,0));
        lab23.setText("Префикс (замена/код)");
        lab23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab23.setPreferredSize(new java.awt.Dimension(120, 18));

        txt10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt10.setPreferredSize(new java.awt.Dimension(180, 18));

        txt11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt11.setPreferredSize(new java.awt.Dimension(156, 18));

        lab24.setFont(frames.Util.getFont(0,0));
        lab24.setText("Вид изделия по умолчанию");
        lab24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab24.setPreferredSize(new java.awt.Dimension(120, 18));

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

        btn07.setText("...");
        btn07.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn07.setMaximumSize(new java.awt.Dimension(18, 18));
        btn07.setMinimumSize(new java.awt.Dimension(18, 18));
        btn07.setPreferredSize(new java.awt.Dimension(18, 18));
        btn07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeToSystree(evt);
            }
        });

        lab18.setFont(frames.Util.getFont(0,0));
        lab18.setText("Доступн.гр.текстур");
        lab18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab18.setPreferredSize(new java.awt.Dimension(112, 18));

        txt15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt15.setPreferredSize(new java.awt.Dimension(180, 18));

        javax.swing.GroupLayout pan6Layout = new javax.swing.GroupLayout(pan6);
        pan6.setLayout(pan6Layout);
        pan6Layout.setHorizontalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(pan6Layout.createSequentialGroup()
                            .addComponent(lab19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txt01, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                            .addComponent(lab20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt08, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt11, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan6Layout.createSequentialGroup()
                                .addComponent(lab23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan6Layout.setVerticalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt08, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        tabb1.addTab("<html><font size=\"3\">Основные", pan6);

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
                tabMousePressed(evt);
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

        tabb1.addTab("<html><font size=\"3\">Профили", pan3);

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
                tabMousePressed(evt);
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

        tabb1.addTab("<html><font size=\"3\">Фурнитура", pan4);

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
                tabMousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(2).setPreferredWidth(60);
            tab4.getColumnModel().getColumn(2).setMaxWidth(80);
            tab4.getColumnModel().getColumn(3).setMaxWidth(40);
        }

        pan5.add(scr4, java.awt.BorderLayout.CENTER);

        tabb1.addTab("<html><font size=\"3\">Параметры", pan5);

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
                false, false
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
                tabMousePressed(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(0).setResizable(false);
            tab5.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(1).setMinWidth(68);
            tab5.getColumnModel().getColumn(1).setPreferredWidth(68);
            tab5.getColumnModel().getColumn(1).setMaxWidth(68);
        }

        pan10.add(scr5, java.awt.BorderLayout.CENTER);

        scr6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr6.setPreferredSize(new java.awt.Dimension(240, 324));
        scr6.setViewportView(treeWin);

        pan10.add(scr6, java.awt.BorderLayout.EAST);

        tabb1.addTab("<html><font size=\"3\">Модели", pan10);

        pan1.add(tabb1);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));
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

        tool.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tool.setPreferredSize(new java.awt.Dimension(916, 29));

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

        btn05.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn05.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c055.gif"))); // NOI18N
        btn05.setToolTipText("Артикулы в системе...");
        btn05.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn05.setMaximumSize(new java.awt.Dimension(25, 25));
        btn05.setMinimumSize(new java.awt.Dimension(25, 25));
        btn05.setPreferredSize(new java.awt.Dimension(25, 25));
        btn05.addActionListener(new java.awt.event.ActionListener() {
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
                .addComponent(btn05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(btnReport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 615, Short.MAX_VALUE)
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
                    .addComponent(btn05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(tool, java.awt.BorderLayout.PAGE_START);

        getAccessibleContext().setAccessibleName("Системы профилей");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab2, tab3, tab4, tab5));
        if (treeSys.isEditing()) {
            treeSys.getCellEditor().stopCellEditing();
        }
        treeSys.setBorder(null);
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab2, tab3, tab4, tab5).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab2);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(treeSys, tab2, tab3, tab4, tab5);
        eProperty.save(); //запишем текущий systreeID и sysprodID в файл
        qSystree.execsql();
        Arrays.asList(tab2, tab3, tab4).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (frame != null)
            frame.dispose();
    }//GEN-LAST:event_windowClosed

    private void tabb1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabb1StateChanged
        if (treeSys.isEditing()) {
            treeSys.getCellEditor().stopCellEditing();
        }
        treeSys.setBorder(null);
        if (tabb1.getSelectedIndex() == 1) {
            Util.listenerClick(tab3, Arrays.asList(tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 2) {
            Util.listenerClick(tab4, Arrays.asList(tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 3) {
            Util.listenerClick(tab5, Arrays.asList(tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 4) {
            Util.listenerClick(tab2, Arrays.asList(tab2, tab3, tab4, tab5));
        }
    }//GEN-LAST:event_tabb1StateChanged

    private void treeSysMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeSysMousePressed
        Arrays.asList(tab2, tab3, tab4, tab5).forEach(tab -> tab.setBorder(null));
        treeSys.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
        Util.stopCellEditing(tab2, tab3, tab4, tab5);
    }//GEN-LAST:event_treeSysMousePressed

    private void glasdefToSystree(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glasdefToSystree

        new DicArtikl(this, (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            for (int i = 0; i < qSystree.size(); i++) {
                if (systreeID == qSystree.get(i).getInt(eSystree.id)) {
                    qSystree.set(record.getStr(eArtikl.code), i, eSystree.glas);
                    rsvSystree.load(i);
                }
            }
        }, 5);
    }//GEN-LAST:event_glasdefToSystree

    private void imageviewToSystree(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageviewToSystree

        new DicEnums(this, (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            for (int i = 0; i < qSystree.size(); i++) {
                if (systreeID == qSystree.get(i).getInt(eSystree.id)) {
                    qSystree.set(record.getInt(0), i, eSystree.imgview);
                    rsvSystree.load(i);
                }
            }
        }, LayoutProduct.values());
    }//GEN-LAST:event_imageviewToSystree

    private void typeToSystree(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeToSystree

        new DicEnums(this, (record) -> {

            Util.stopCellEditing(tab2, tab3, tab4, tab5);
            for (int i = 0; i < qSystree.size(); i++) {
                if (systreeID == qSystree.get(i).getInt(eSystree.id)) {
                    qSystree.set(record.getInt(0), i, eSystree.types);
                    rsvSystree.load(i);
                }
            }
        }, TypeUse.values());
    }//GEN-LAST:event_typeToSystree

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        DefMutableTreeNode node = (DefMutableTreeNode) treeSys.getLastSelectedPathComponent();
        if (node != null) {
            if (treeSys.getBorder() != null) {
                Record record = eSystree.up.newRecord(Query.INS);
                record.setNo(eSystree.id, ConnApp.instanc().genId(eSystree.id));
                int parent_id = (node.rec().getInt(eSystree.id) == node.rec().getInt(eSystree.parent_id)) ? record.getInt(eSystree.id) : node.rec().getInt(eSystree.id);
                record.setNo(eSystree.parent_id, parent_id);
                record.setNo(eSystree.name, "P" + record.getStr(eSystree.id));
                qSystree.insert(record); //record сохраним в базе
                record.set(eSystree.up, Query.SEL);
                qSystree.add(record); //добавим record в список
                DefMutableTreeNode newNode = new DefMutableTreeNode(record);
                ((DefaultTreeModel) treeSys.getModel()).insertNodeInto(newNode, node, node.getChildCount()); //добавим node в tree
                TreeNode[] nodes = ((DefaultTreeModel) treeSys.getModel()).getPathToRoot(newNode);
                treeSys.scrollPathToVisible(new TreePath(nodes));
                treeSys.setSelectionPath(new TreePath(nodes));

            } else if (tab2.getBorder() != null) {
                Record record1 = eSysprof.up.newRecord(Query.INS);
                Record record2 = eArtikl.up.newRecord();
                record1.setNo(eSysprof.id, ConnApp.instanc().genId(eSysprof.id));
                record1.setNo(eSysprof.systree_id, systreeID);
                qSysprof.add(record1);
                qSysprof.table(eArtikl.up).add(record2);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qSysprof, tab2);

            } else if (tab3.getBorder() != null) {
                Record record1 = eSysfurn.up.newRecord(Query.INS);
                Record record2 = eFurniture.up.newRecord();
                record1.setNo(eSysfurn.id, ConnApp.instanc().genId(eSysfurn.id));
                record1.setNo(eSysfurn.systree_id, systreeID);
                record1.setNo(eSysfurn.npp, 0);
                record1.setNo(eSysfurn.replac, 0);
                qSysfurn.add(record1);
                qSysfurn.table(eFurniture.up).add(record2);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qSysfurn, tab3);

            } else if (tab4.getBorder() != null) {
                Record record1 = eSyspar1.up.newRecord(Query.INS);
                record1.setNo(eSyspar1.id, ConnApp.instanc().genId(eSyspar1.id));
                record1.setNo(eSyspar1.systree_id, systreeID);
                qSyspar1.add(record1);
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qSyspar1, tab4);

            } else if (tab5.getBorder() != null) {
                DefMutableTreeNode selectedNode = (DefMutableTreeNode) treeSys.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.isLeaf()) {
                    FrameProgress.create(Systree.this, new FrameListener() {
                        public void actionRequest(Object obj) {
                            frame = new Models(Systree.this, listenerModel);
                            FrameToFile.setFrameSize(frame);
                            frame.setVisible(true);
                        }
                    });
                }
            }
        }
    }//GEN-LAST:event_btnInsert

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (treeSys.getBorder() != null) {
            if (treeSys.isSelectionEmpty() == false) {
                DefMutableTreeNode removeNode = (DefMutableTreeNode) treeSys.getLastSelectedPathComponent();
                if (removeNode.getChildCount() != 0) {
                    JOptionPane.showMessageDialog(this, "Нельзя удалить текущий узел т. к. у него есть подчинённые записи", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                DefMutableTreeNode parentNode = (DefMutableTreeNode) removeNode.getParent();
                if (JOptionPane.showConfirmDialog(this, "Хотите удалить " + removeNode + "?", "Подтвердите удаление",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null) == 0) {
                    removeNode.rec().set(eSystree.up, Query.DEL);
                    qSystree.delete(removeNode.rec());
                    qSystree.remove(removeNode.rec());
                    ((DefaultTreeModel) treeSys.getModel()).removeNodeFromParent(removeNode);
                    if (parentNode != null) {
                        TreeNode[] nodes = ((DefaultTreeModel) treeSys.getModel()).getPathToRoot(parentNode);
                        treeSys.scrollPathToVisible(new TreePath(nodes));
                        treeSys.setSelectionPath(new TreePath(nodes));
                    }
                }
            }
        } else if (tab5.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0 && tab5.getSelectedRow() != -1) {
                int rowTable = tab5.getSelectedRow();
                int rowModel = Util.getSelectedRec(tab5);
                Record record = qSysprod.get(rowModel);
                record.set(eSysprod.up, Query.DEL);

                qSysprod.delete(record);
                qSysprod.removeRec(rowModel);
                ((DefaultTableModel) tab5.getModel()).removeRow(rowTable);

                rowTable = (qSysprod.size() > 0) ? --rowTable : 0;
                rowModel = tab5.convertRowIndexToModel(rowTable);
                Util.setSelectedRow(tab5, rowModel);
            } else {
                JOptionPane.showMessageDialog(null, "Ни одна из текущих записей не выбрана", "Предупреждение", JOptionPane.NO_OPTION);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Query.listOpenTable.forEach(q -> q.clear());
        loadingWin();
    }//GEN-LAST:event_btnRefresh

    private void findFromArtikl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findFromArtikl
        Record record = qSysprof.get(Util.getSelectedRec(tab2));
        Record record2 = eArtikl.find(record.getInt(eSysprof.artikl_id), false);
        FrameProgress.create(this, new FrameListener() {
            public void actionRequest(Object obj) {
                App1.eApp1.Artikles.createFrame(Systree.this, record2);
            }
        });
    }//GEN-LAST:event_findFromArtikl

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        Gson gs = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gs.toJson(iwin.jsonRoot));
    }//GEN-LAST:event_btnReport

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void sysprofToFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysprofToFrame
        try {
            DefMutableTreeNode node = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
            float selectID = node.com5t().id();
            if (node != null) {
                Query query = new Query(eSysprof.values(), eArtikl.values());
                UseArtiklTo useArtiklTo = (node.com5t().type() == TypeElem.FRAME_SIDE) ? UseArtiklTo.FRAME : UseArtiklTo.STVORKA;

                for (int index = 0; index < qSysprof.size(); ++index) {
                    Record sysprofRec = qSysprof.get(index);
                    if (sysprofRec.getInt(eSysprof.use_type) == useArtiklTo.id) {
                        if (sysprofRec.getInt(eSysprof.use_side) == node.com5t().layout().id
                                || sysprofRec.getInt(eSysprof.use_side) == UseSide.ANY.id) {
                            query.add(sysprofRec);
                            query.table(eArtikl.up).add(qSysprof.table(eArtikl.up).get(index));
                        }
                    }
                }
                new DicSysprof(this, (sysprofRec) -> {

                    float ramaId = node.com5t().id();
                    JsonElem elemRama = iwin.jsonRoot.find(ramaId);

                    if (node.com5t().type() == TypeElem.FRAME_SIDE) { //рама окна
                        String paramStr = elemRama.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        paramObj.addProperty(PKjson.sysprofID, sysprofRec.getInt(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        elemRama.param(paramStr);
                        updateScript(selectID);

                    } else { //рама створки
                        float stvId = ((DefMutableTreeNode) node.getParent()).com5t().id();
                        JsonArea stvArea = (JsonArea) iwin.jsonRoot.find(stvId);
                        String paramStr = stvArea.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        String stvKey = null;
                        if (node.com5t().layout() == LayoutArea.BOTTOM) {
                            stvKey = PKjson.stvorkaBottom;
                        } else if (node.com5t().layout() == LayoutArea.RIGHT) {
                            stvKey = PKjson.stvorkaRight;
                        } else if (node.com5t().layout() == LayoutArea.TOP) {
                            stvKey = PKjson.stvorkaTop;
                        } else if (node.com5t().layout() == LayoutArea.LEFT) {
                            stvKey = PKjson.stvorkaLeft;
                        }
                        JsonObject jso = Ujson.getAsJsonObject(paramObj, stvKey);
                        jso.addProperty(PKjson.sysprofID, sysprofRec.getStr(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        stvArea.param(paramStr);
                        updateScript(selectID);
                    }

                }, query);
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_sysprofToFrame

    private void colorToFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToFrame
        try {
            DefMutableTreeNode node = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
            float selectID = node.com5t().id();
            HashSet<Record> colorSet = new HashSet();
            Query artdetList = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.artikl_id, "=", node.com5t().artiklRec.getInt(eArtikl.id));
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
            DicColor2 frame = new DicColor2(this, (colorRec) -> {

                String colorID = (evt.getSource() == btn18) ? PKjson.colorID1 : (evt.getSource() == btn19) ? PKjson.colorID2 : PKjson.colorID3;
                float parentId = ((DefMutableTreeNode) node.getParent()).com5t().id();
                JsonArea parentArea = (JsonArea) iwin.jsonRoot.find(parentId);

                if (node.com5t().type() == TypeElem.STVORKA_SIDE) {
                    String paramStr = parentArea.param();
                    JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                    String stvKey = null;
                    if (node.com5t().layout() == LayoutArea.BOTTOM) {
                        stvKey = PKjson.stvorkaBottom;
                    } else if (node.com5t().layout() == LayoutArea.RIGHT) {
                        stvKey = PKjson.stvorkaRight;
                    } else if (node.com5t().layout() == LayoutArea.TOP) {
                        stvKey = PKjson.stvorkaTop;
                    } else if (node.com5t().layout() == LayoutArea.LEFT) {
                        stvKey = PKjson.stvorkaLeft;
                    }
                    JsonObject jso = Ujson.getAsJsonObject(paramObj, stvKey);
                    jso.addProperty(colorID, colorRec.getStr(eColor.id));
                    paramStr = gson.toJson(paramObj);
                    parentArea.param(paramStr);
                    updateScript(selectID);

                } else if (node.com5t().type() == TypeElem.FRAME_SIDE) {
                    for (JsonElem elem : parentArea.elements()) {
                        if (elem.id() == ((DefMutableTreeNode) node).com5t().id()) {
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
            float selectID = ((DefMutableTreeNode) treeWin.getLastSelectedPathComponent()).com5t().id();
            HashSet<Record> set = new HashSet();
            String[] arr1 = (txt15.getText().isEmpty() == false) ? txt15.getText().split(";") : null;
            String jfield = (evt.getSource() == btn09) ? txt03.getText() : (evt.getSource() == btn13) ? txt04.getText() : txt05.getText();
            Integer[] arr2 = builder.specif.Util.parserInt(jfield);
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

            DialogListener listenerColor = (colorRec) -> {

                builder.script.JsonElem rootArea = iwin.jsonRoot.find(selectID);
                if (rootArea != null) {
                    String paramStr = (rootArea.param().isEmpty()) ? "{}" : rootArea.param();
                    JsonObject jsonObject = gson.fromJson(paramStr, JsonObject.class);

                    if (evt.getSource() == btn09) {
                        jsonObject.addProperty(PKjson.colorID1, colorRec.getStr(eColor.id));
                    } else if (evt.getSource() == btn13) {
                        jsonObject.addProperty(PKjson.colorID2, colorRec.getStr(eColor.id));
                    } else if (evt.getSource() == btn02) {
                        jsonObject.addProperty(PKjson.colorID3, colorRec.getStr(eColor.id));
                    }
                    paramStr = gson.toJson(jsonObject);
                    rootArea.param(paramStr);
                    updateScript(selectID);
                }
            };
            if (arr1 == null && arr2.length == 0) {
                new DicColor2(this, listenerColor);
            } else {
                new DicColor2(this, listenerColor, set);
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToWindows

    private void artiklToGlass(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_artiklToGlass
        try {
            DefMutableTreeNode nodeWin = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
            float selectID = nodeWin.com5t().id();
            DefMutableTreeNode nodeSys = (DefMutableTreeNode) treeSys.getLastSelectedPathComponent();
            String depth = nodeSys.rec().getStr(eSystree.depth);
            if (depth != null && depth.isEmpty() == false) {
                depth = depth.replace(";", ",");
                if (depth.charAt(depth.length() - 1) == ',') {
                    depth = depth.substring(0, depth.length() - 1);
                }
            }
            depth = (depth != null && depth.isEmpty() == false) ? " and " + eArtikl.depth.name() + " in (" + depth + ")" : "";
            Query qArtikl = new Query(eArtikl.values()).select(eArtikl.up, "where",
                    eArtikl.level1, "= 5 and", eArtikl.level2, "in (1,2,3)", depth);

            new DicArtikl(this, (artiklRec) -> {

                JsonElem glassElem = (JsonElem) iwin.jsonRoot.find(selectID);
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
            DefMutableTreeNode nodeWin = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
            float selectID = nodeWin.com5t().id();
            DefMutableTreeNode nodeSys = (DefMutableTreeNode) treeSys.getLastSelectedPathComponent();
            String systreeID = nodeSys.rec().getStr(eSystree.id);
            Query qSysfurn = new Query(eSysfurn.values(), eFurniture.values()).select(eSysfurn.up, "left join", eFurniture.up, "on",
                    eSysfurn.furniture_id, "=", eFurniture.id, "where", eSysfurn.systree_id, "=", systreeID);         
            new DicName(this, (sysfurnRec) -> {

                JsonArea stvArea = (JsonArea) iwin.jsonRoot.find(selectID);
                String paramStr = stvArea.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.sysfurnID, sysfurnRec.getStr(eSysfurn.id));
                paramStr = gson.toJson(paramObj);
                stvArea.param(paramStr);
                updateScript(selectID);

            }, qSysfurn, eFurniture.name);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_sysfurnToStvorka

    private void typeOpenToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeOpenToStvorka
        try {
            new DicEnums(this, (record) -> {

                System.out.println(record);
            }, TypeOpen1.LEFT, TypeOpen1.LEFTUP, TypeOpen1.LEFTSHIFT,
                    TypeOpen1.RIGHT, TypeOpen1.RIGHTUP, TypeOpen1.RIGHTSHIFT, TypeOpen1.UPPER, TypeOpen1.FIXED);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_typeOpenToStvorka

    private void handlToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handlToStvorka
        try {
            DefMutableTreeNode nodeWin = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
            float selectID = nodeWin.com5t().id();
            DefMutableTreeNode node = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
            int furnitureID = ((AreaStvorka) node.com5t()).sysfurnRec.getInt(eSysfurn.furniture_id);
            Query qFurndet = new Query(eFurndet.values()).select(eFurndet.up, "where", eFurndet.furniture_id1, "=", furnitureID);
            Query qArtikl = new Query(eArtikl.values()).select(eArtikl.up, "where", eArtikl.level1, "= 2 and", eArtikl.level2, "in (11)");
            Query qArtikl2 = new Query(eArtikl.values());
            for (Record furndetRec : qFurndet) { //первый уровень
                for (Record artiklRec : qArtikl) {
                    if (furndetRec.getInt(eFurndet.artikl_id) == artiklRec.getInt(eArtikl.id)) {
                        qArtikl2.add(artiklRec);
                    }
                }
                Query qFurndet2 = new Query(eFurndet.values()).select(eFurndet.up, "where",
                        eFurndet.furndet_id, "=", furndetRec.getInt(eFurndet.id), "and", eFurndet.furndet_id, "!=", eFurndet.id);
                for (Record furndet2Rec : qFurndet2) { //второй уровень
                    for (Record artiklRec : qArtikl) {
                        if (furndet2Rec.getInt(eFurndet.artikl_id) == artiklRec.getInt(eArtikl.id)) {
                            qArtikl2.add(artiklRec);
                        }
                    }
                }
            }
            new DicArtikl(this, (artiklRec) -> {

                JsonArea stvArea = (JsonArea) iwin.jsonRoot.find(selectID);
                String paramStr = stvArea.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.artiklHandl, artiklRec.getStr(eArtikl.id));
                paramStr = gson.toJson(paramObj);
                stvArea.param(paramStr);
                updateScript(selectID);

            }, qArtikl2);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_handlToStvorka

    private void heightHandlToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightHandlToStvorka
        DefMutableTreeNode nodeWin = (DefMutableTreeNode) treeWin.getLastSelectedPathComponent();
        AreaStvorka areaStv = (AreaStvorka) nodeWin.com5t();
        int indexLayoutHandl = 0;
        if (LayoutHandle.CONST.name.equals(txt16.getText())) {
            indexLayoutHandl = 1;
        } else if (LayoutHandle.SET.name.equals(txt16.getText())) {
            indexLayoutHandl = 2;
        }
        new DicHandl(this, (record) -> {
            try {
                float selectID = areaStv.id();
                JsonArea stvArea = (JsonArea) iwin.jsonRoot.find(selectID);
                String paramStr = stvArea.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);

                if (record.getInt(0) == 0) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.MIDL.id);
                    txt31.setEditable(false);

                } else if (record.getInt(0) == 1) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.CONST.id);
                    txt31.setEditable(false);

                } else if (record.getInt(0) == 2) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.SET.id);
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

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn01;
    private javax.swing.JButton btn02;
    private javax.swing.JButton btn03;
    private javax.swing.JButton btn04;
    private javax.swing.JToggleButton btn05;
    private javax.swing.JButton btn06;
    private javax.swing.JButton btn07;
    private javax.swing.JButton btn09;
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
    private javax.swing.JButton btn20;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport1;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel lab13;
    private javax.swing.JLabel lab14;
    private javax.swing.JLabel lab15;
    private javax.swing.JLabel lab16;
    private javax.swing.JLabel lab17;
    private javax.swing.JLabel lab18;
    private javax.swing.JLabel lab19;
    private javax.swing.JLabel lab20;
    private javax.swing.JLabel lab23;
    private javax.swing.JLabel lab24;
    private javax.swing.JLabel lab25;
    private javax.swing.JLabel lab26;
    private javax.swing.JLabel lab27;
    private javax.swing.JLabel lab28;
    private javax.swing.JLabel lab29;
    private javax.swing.JLabel lab30;
    private javax.swing.JLabel lab31;
    private javax.swing.JLabel lab32;
    private javax.swing.JLabel lab33;
    private javax.swing.JLabel lab34;
    private javax.swing.JLabel lab36;
    private javax.swing.JLabel lab37;
    private javax.swing.JLabel lab38;
    private javax.swing.JLabel lab39;
    private javax.swing.JLabel lab40;
    private javax.swing.JLabel lab41;
    private javax.swing.JLabel lab42;
    private javax.swing.JLabel lab43;
    private javax.swing.JLabel lab44;
    private javax.swing.JLabel lab45;
    private javax.swing.JLabel lab46;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan12;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan15;
    private javax.swing.JPanel pan16;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan20;
    private javax.swing.JPanel pan21;
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
    private javax.swing.JPanel south;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JPanel tool;
    private javax.swing.JTree treeSys;
    private javax.swing.JTree treeWin;
    private javax.swing.JFormattedTextField txt01;
    private javax.swing.JTextField txt02;
    private javax.swing.JTextField txt03;
    private javax.swing.JTextField txt04;
    private javax.swing.JTextField txt05;
    private javax.swing.JFormattedTextField txt06;
    private javax.swing.JFormattedTextField txt07;
    private javax.swing.JTextField txt08;
    private javax.swing.JTextField txt09;
    private javax.swing.JTextField txt10;
    private javax.swing.JTextField txt11;
    private javax.swing.JFormattedTextField txt12;
    private javax.swing.JTextField txt13;
    private javax.swing.JTextField txt14;
    private javax.swing.JTextField txt15;
    private javax.swing.JTextField txt16;
    private javax.swing.JTextField txt18;
    private javax.swing.JTextField txt19;
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
    private javax.swing.JTextField txt30;
    private javax.swing.JTextField txt31;
    private javax.swing.JTextField txt32;
    private javax.swing.JTextField txt33;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        Util.documentFilter1(txt02, txt15);
        Util.documentFilter2(txt03, txt04, txt05);
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab2, tab3, tab4, tab5)));
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) treeSys.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        treeSys.getSelectionModel().addTreeSelectionListener(tse -> selectionSys());
        treeWin.getSelectionModel().addTreeSelectionListener(tse -> selectionWin());
        tab5.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab5();
                }
            }
        });
        DefaultTreeModel model = (DefaultTreeModel) treeWin.getModel();
        ((DefaultMutableTreeNode) model.getRoot()).removeAllChildren();
        model.reload();
    }

    private void path(float id1, float id2, DefaultMutableTreeNode node) {
        if (id1 == id2) {
            selectedPath = node.getPath();
        }
    }
}
