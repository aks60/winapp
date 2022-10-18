package frames;

import frames.dialog.DicColor;
import dataset.Conn;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eArtdet;
import domain.eColor;
import domain.eCurrenc;
import enums.TypeArtikl;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import frames.swing.DefTableModel;
import frames.swing.DefFieldEditor;
import domain.eGroups;
import domain.eSyssize;
import enums.TypeGroups;
import enums.UseUnit;
import frames.dialog.DicArtikl;
import frames.dialog.DicEnums;
import frames.dialog.DicGroups;
import frames.swing.DefCellBoolRenderer;
import frames.swing.FilterTable;
import java.awt.CardLayout;
import java.awt.Window;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import common.listener.ListenerRecord;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import report.ExecuteCmd;
import report.HtmlOfTable;

/**
 * Материальные ценности
 */
public class Artikles extends javax.swing.JFrame {

    private Query qGroups = new Query(eGroups.values());
    private Query qSyssize = new Query(eSyssize.values());
    private Query qColor = new Query(eColor.values());
    private Query qCurrenc = new Query(eCurrenc.values());
    private Query qArtikl = new Query(eArtikl.values());
    private Query qArtdet = new Query(eArtdet.values());

    private DefFieldEditor rsvArtikl;
    private FilterTable filterTable = null;
    private HashSet<JTextField> jtf = new HashSet();
    private DefaultMutableTreeNode nodeRoot = null;
    private Window owner = null;
    private ListenerRecord listenerSeries, listenerCateg, listenerColor, listenerUnit, listenerCurrenc1,
            listenerCurrenc2, listenerAnalog, listenerSyssize, listenerArtincr, listenerArtdecr, listenerSeriesFilter, listenerCategFilter;

    public Artikles() {
        initComponents();
        initElements();
        listenerSet();
        loadingData();
        loadingModel();
        loadingTree();
    }

    public Artikles(java.awt.Window owner, Record artiklRec) {
        initComponents();
        initElements();
        listenerSet();
        loadingData();
        loadingModel();
        loadingTree();
        setSelectionPath(artiklRec);
    }

    public void loadingData() {
        qSyssize.select(eSyssize.up, "order by", eSyssize.name);
        qGroups.select(eGroups.up, "order by", eGroups.name);
        qCurrenc.select(eCurrenc.up, "order by", eCurrenc.name);
        qColor.select(eColor.up, "order by", eColor.name);
    }

    public void loadingModel() {

        DefTableModel rsmArtikl = new DefTableModel(tab1, qArtikl, eArtikl.code, eArtikl.name, eArtikl.otx_norm, eArtikl.coeff, eArtikl.series_id, eArtikl.artgrp3_id) {
            @Override
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eArtikl.series_id) {
                    Record artiklRec = qArtikl.get(row);
                    Record groupRec = qGroups.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.series_id))).findFirst().orElse(eGroups.up.newRecord());
                    return groupRec.get(eGroups.name);
                } else if (field == eArtikl.artgrp3_id) {
                    Record artiklRec = qArtikl.get(row);
                    Record groupRec = qGroups.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp3_id))).findFirst().orElse(eGroups.up.newRecord());
                    return groupRec.get(eGroups.name);
                }
                return val;
            }
        };
        DefTableModel rsmArtdet = new DefTableModel(tab2, qArtdet, eArtdet.color_fk, eArtdet.color_fk, eArtdet.mark_c1, eArtdet.cost_c1,
                eArtdet.mark_c2, eArtdet.cost_c2, eArtdet.mark_c3, eArtdet.cost_c3, eArtdet.cost_c4, eArtdet.cost_unit, eArtdet.coef, eArtdet.cost_min, eArtdet.id) {

            @Override
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eArtdet.color_fk && val != null) {
                    Integer color_fk = Integer.valueOf(val.toString());

                    if (color_fk >= 0) {
                        Record colorRec = qColor.stream().filter(rec -> rec.getInt(eColor.id) == color_fk).findFirst().orElse(null);
                        if (col == 0) {
                            Record colgrpRec = qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == colorRec.getInt(eColor.colgrp_id)).findFirst().orElse(eGroups.up.newRecord());
                            return colgrpRec.getStr(eGroups.name);
                        } else {
                            return colorRec.getStr(eColor.name);
                        }

                    } else if (color_fk < 0) {
                        if (col == 0) {
                            Record colgrpRec = qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == Math.abs(color_fk)).findFirst().orElse(eGroups.up.newRecord());
                            return colgrpRec.getStr(eGroups.name);
                        } else {
                            return "Все текстуры группы";
                        }
                    }
                }
                return val;
            }
        };

        tab2.getColumnModel().getColumn(2).setCellRenderer(new DefCellBoolRenderer());
        tab2.getColumnModel().getColumn(4).setCellRenderer(new DefCellBoolRenderer());
        tab2.getColumnModel().getColumn(6).setCellRenderer(new DefCellBoolRenderer());

        rsvArtikl = new DefFieldEditor(tab1) {

            public Set<JTextField> set = new HashSet();

            public void setText(JTextField jtf, String str) {
                set.add(jtf);
                jtf.setText(str);
            }

            @Override
            public void load(Integer index) {
                super.load(index);
                Record artiklRec = qArtikl.get(UGui.getIndexRec(tab1));
                Record seriesRec = qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == artiklRec.getInt(eArtikl.series_id)).findFirst().orElse(eGroups.up.newRecord());
                Record currenc1Rec = qCurrenc.stream().filter(rec -> rec.get(eCurrenc.id).equals(artiklRec.get(eArtikl.currenc1_id))).findFirst().orElse(eCurrenc.up.newRecord());
                Record currenc2Rec = qCurrenc.stream().filter(rec -> rec.get(eCurrenc.id).equals(artiklRec.get(eArtikl.currenc2_id))).findFirst().orElse(eCurrenc.up.newRecord());
                Record artgrp1Rec = qGroups.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp1_id))).findFirst().orElse(eGroups.up.newRecord());
                Record artgrp2Rec = qGroups.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp2_id))).findFirst().orElse(eGroups.up.newRecord());
                Record artgrp3Rec = qGroups.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp3_id))).findFirst().orElse(eGroups.up.newRecord());
                Record syssizeRec = qSyssize.stream().filter(rec -> rec.getInt(eSyssize.id) == artiklRec.getInt(eArtikl.syssize_id)).findFirst().orElse(eSyssize.up.newRecord());

                setText(txt5, UseUnit.getName(artiklRec.getInt(eArtikl.unit)));
                setText(txt7, currenc1Rec.getStr(eCurrenc.name));
                setText(txt17, currenc2Rec.getStr(eCurrenc.name));
                setText(txt24, currenc2Rec.getStr(eCurrenc.name));
                setText(txt25, currenc1Rec.getStr(eCurrenc.name));
                setText(txt33, currenc2Rec.getStr(eCurrenc.name));
                setText(txt31, UseUnit.getName(artiklRec.getInt(eArtikl.unit)));
                setText(txt32, currenc1Rec.getStr(eCurrenc.name));
                setText(txt40, UseUnit.getName(artiklRec.getInt(eArtikl.unit)));

                if (artiklRec.getInt(eArtikl.analog_id) != -1) {
                    Record analogRec = qArtikl.stream().filter(rec -> rec.get(eArtikl.id).equals(artiklRec.get(eArtikl.analog_id))).findFirst().orElse(eArtikl.up.newRecord());
                    setText(txt11, analogRec.getStr(eArtikl.code));
                } else {
                    setText(txt11, null);
                }
                setText(txt10, seriesRec.getStr(eGroups.name));
                setText(txt18, syssizeRec.getStr(eSyssize.name));
                setText(txt19, artgrp1Rec.getStr(eGroups.val));
                setText(txt26, artgrp1Rec.getStr(eGroups.val));
                setText(txt20, artgrp2Rec.getStr(eGroups.val));
                setText(txt22, artgrp3Rec.getStr(eGroups.name));
                setText(txt26, artgrp1Rec.getStr(eGroups.val));
                setText(txt27, artgrp2Rec.getStr(eGroups.val));
                setText(txt29, artgrp3Rec.getStr(eGroups.name));
                setText(txt30, seriesRec.getStr(eGroups.name));
                setText(txt34, artgrp1Rec.getStr(eGroups.val));
                setText(txt35, artgrp2Rec.getStr(eGroups.val));
                setText(txt37, artgrp3Rec.getStr(eGroups.name));
                setText(txt38, seriesRec.getStr(eGroups.name));
            }

            @Override
            public void clear() {
                super.clear();
                set.forEach(s -> s.setText(null));
            }
        };

        rsvArtikl.add(eArtikl.len_unit, txt1);
        rsvArtikl.add(eArtikl.height, txt2);
        rsvArtikl.add(eArtikl.depth, txt3);
        rsvArtikl.add(eArtikl.density, txt4);
        rsvArtikl.add(eArtikl.density, txt6);
        rsvArtikl.add(eArtikl.size_centr, txt8);
        rsvArtikl.add(eArtikl.size_furn, txt9);
        rsvArtikl.add(eArtikl.min_rad, txt12);
        rsvArtikl.add(eArtikl.tech_code, txt14);
        rsvArtikl.add(eArtikl.size_falz, txt15);
        rsvArtikl.add(eArtikl.size_tech, txt16);
        rsvArtikl.add(eArtikl.size_frez, txt21);
        rsvArtikl.add(eArtikl.size_frez, txt23);
        rsvArtikl.add(eArtikl.density, txt39);
        rsvArtikl.add(eArtikl.len_unit, txt41);
        rsvArtikl.add(eArtikl.height, txt45);
        rsvArtikl.add(eArtikl.depth, txt46);
        rsvArtikl.add(eArtikl.len_unit, txt48);

        UGui.buttonCellEditor(tab2, 0).addActionListener(event -> {
            DicColor frame = new DicColor(this, listenerColor);
        });

        UGui.buttonCellEditor(tab2, 1).addActionListener(event -> {
            DicColor frame = new DicColor(this, listenerColor);
        });
    }

    public void listenerSet() {

        listenerSeries = (record) -> {
            int rowQuery = UGui.getIndexRec(tab1);
            if (rowQuery != -1) {
                Record artiklRec = qArtikl.get(rowQuery);
                artiklRec.set(eArtikl.series_id, record.get(eGroups.id));
                rsvArtikl.load();
            }
            UGui.stopCellEditing(tab1, tab2);
        };

        listenerSeriesFilter = (record) -> {
            tab1.setColumnSelectionInterval(4, 4);
            filterTable.getLab().setText("Серия профилей");
            filterTable.getTxt().setName("tab1");
            filterTable.getTxt().setText(record.getStr(eGroups.name));
        };

        listenerCategFilter = (record) -> {
            tab1.setColumnSelectionInterval(5, 5);
            filterTable.getLab().setText("Категоря профилей");
            filterTable.getTxt().setName("tab1");
            filterTable.getTxt().setText(record.getStr(eGroups.name));
        };

        listenerAnalog = (record) -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                artiklRec.set(eArtikl.analog_id, record.get(eArtikl.id));
                rsvArtikl.load();
                UGui.stopCellEditing(tab1, tab2);
            }
        };

        listenerColor = (record) -> {
            if (tab2.getBorder() != null) {
                if (eGroups.values().length == record.size()) {
                    qArtdet.set(-1 * record.getInt(eGroups.id), UGui.getIndexRec(tab2), eArtdet.color_fk);

                } else if (eColor.values().length == record.size()) {
                    qArtdet.set(record.getInt(eColor.id), UGui.getIndexRec(tab2), eArtdet.color_fk);
                }
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                UGui.stopCellEditing(tab1, tab2);
            }
        };

        listenerUnit = (record) -> {
            UGui.listenerEnums(record, tab1, eArtikl.unit, tab1, tab2);
        };

        listenerCurrenc1 = (record) -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                artiklRec.set(eArtikl.currenc1_id, record.get(eCurrenc.id));
                rsvArtikl.load();
            }
            UGui.stopCellEditing(tab1, tab2);
        };

        listenerCurrenc2 = (record) -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                artiklRec.set(eArtikl.currenc2_id, record.get(eCurrenc.id));
                rsvArtikl.load();
            }
            UGui.stopCellEditing(tab1, tab2);
        };

        listenerSyssize = (record) -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                artiklRec.set(eArtikl.syssize_id, record.get(eSyssize.id));
                if (artiklRec.getInt(eArtikl.size_falz) == -1) {
                    artiklRec.set(eArtikl.size_falz, record.get(eSyssize.falz));
                }
                rsvArtikl.load();
                UGui.stopCellEditing(tab1, tab2);
            }
        };

        listenerArtincr = (record) -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                artiklRec.set(eArtikl.artgrp1_id, record.get(eGroups.id));
                rsvArtikl.load();
                UGui.stopCellEditing(tab1, tab2);
            }
        };

        listenerArtdecr = (record) -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                artiklRec.set(eArtikl.artgrp2_id, record.get(eGroups.id));
                rsvArtikl.load();
                UGui.stopCellEditing(tab1, tab2);
            }
        };

        listenerCateg = (record) -> {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                artiklRec.set(eArtikl.artgrp3_id, record.get(eGroups.id));
                rsvArtikl.load();
                UGui.stopCellEditing(tab1, tab2);
            }
        };
    }

    public void loadingTree() {

        nodeRoot = new DefaultMutableTreeNode(TypeArtikl.ROOT);
        DefaultMutableTreeNode node = null;
        for (TypeArtikl it : TypeArtikl.values()) {
            if (it.id1 == 1 && it.id2 == 0) {
                node = new DefaultMutableTreeNode(TypeArtikl.X100); //"Профили"

            } else if (it.id1 == 2 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.X200); //"Аксессуары"

            } else if (it.id1 == 3 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.X300); //"Погонаж"

            } else if (it.id1 == 4 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.X400); //"Инструмент"

            } else if (it.id1 == 5 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.X500); //"Заполнения"

            } else if (it.id2 > 0) {   //остальное       
                nodeRoot.add(node);
                node.add(new javax.swing.tree.DefaultMutableTreeNode(it));
            }
        }
        nodeRoot.add(node);
        tree.setModel(new DefaultTreeModel(nodeRoot));
        scrTree.setViewportView(tree);
        tree.setSelectionRow(0);
    }

    public void selectionTree() {

        UGui.stopCellEditing(tab1, tab2);
        List.of(qArtikl, qArtdet).forEach(q -> q.execsql());
        rsvArtikl.clear();
        UGui.clearTable(tab1, tab2);
        UGui.stopCellEditing(tab1, tab2);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node != null) {
            TypeArtikl e = (TypeArtikl) node.getUserObject();
            String name = (e.id1 > 4) ? "pan8" : (e.id1 > 1) ? "pan7" : "pan2";
            ((CardLayout) pan6.getLayout()).show(pan6, name);

            if (e == TypeArtikl.ROOT) {
                qArtikl.select(eArtikl.up, "order by", eArtikl.level1, ",", eArtikl.code);
            } else if (node.isLeaf()) {
                qArtikl.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1 + "and", eArtikl.level2, "=", e.id2, "order by", eArtikl.level1, ",", eArtikl.code);
            } else {
                qArtikl.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1, "order by", eArtikl.level1, ",", eArtikl.code);
            }
            DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getParent();
            lab1.setText((node2 != null && node.getParent() != null) ? "Тип: " + ((TypeArtikl) node2.getUserObject()).id1
                    + "  подтип: " + ((TypeArtikl) node.getUserObject()).id2 : "");
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        }
        UGui.setSelectedRow(tab1);

    }

    public void selectionTab1(ListSelectionEvent event) {

        UGui.stopCellEditing(tab2);
        List.of(qArtdet).forEach(q -> q.execsql());
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record record = qArtikl.get(index);

            String name = (record.getInt(eArtikl.level1) > 4) ? "pan8" : (record.getInt(eArtikl.level1) > 1) ? "pan7" : "pan2";
            ((CardLayout) pan6.getLayout()).show(pan6, name);

            int id = record.getInt(eArtikl.id);
            lab2.setText("id: " + id);
            qArtdet.select(eArtdet.up, "where", eArtdet.artikl_id, "=", id);
            rsvArtikl.load();
            checkBox1.setSelected((record.getInt(eArtikl.with_seal) != 0));
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab2);
        }
    }

    public void setSelectionPath(Record artiklRec) {
        DefaultMutableTreeNode node = nodeRoot;
        node = node.getNextNode();
        do {
            TypeArtikl typeArt = (TypeArtikl) node.getUserObject();
            if (typeArt.id1 == artiklRec.getInt(eArtikl.level1)
                    && typeArt.id2 == artiklRec.getInt(eArtikl.level2)) {
                TreePath path = new TreePath(node.getPath());
                tree.setSelectionPath(path);
                tree.scrollPathToVisible(path);
            }
            node = node.getNextNode();
        } while (node != null);

        for (int index = 0; index < qArtikl.size(); ++index) {
            int id = qArtikl.getAs(index, eArtikl.id);
            if (id == artiklRec.getInt(eArtikl.id)) {
                UGui.setSelectedIndex(tab1, index);
                UGui.scrollRectToRow(index, tab1);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppmReport = new javax.swing.JPopupMenu();
        itReport1 = new javax.swing.JMenuItem();
        itReport2 = new javax.swing.JMenuItem();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnMove = new javax.swing.JButton();
        btnTest = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        scrTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        pan5 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan6 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        pan106 = new javax.swing.JPanel();
        pan91 = new javax.swing.JPanel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab13 = new javax.swing.JLabel();
        txt1 = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        lab14 = new javax.swing.JLabel();
        txt2 = new javax.swing.JTextField();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(12, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        lab15 = new javax.swing.JLabel();
        txt3 = new javax.swing.JTextField();
        pan98 = new javax.swing.JPanel();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab30 = new javax.swing.JLabel();
        txt18 = new javax.swing.JTextField();
        btn18 = new javax.swing.JButton();
        pan99 = new javax.swing.JPanel();
        filler14 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab28 = new javax.swing.JLabel();
        txt15 = new javax.swing.JTextField();
        filler16 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab21 = new javax.swing.JLabel();
        txt9 = new javax.swing.JTextField();
        pan100 = new javax.swing.JPanel();
        filler15 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab29 = new javax.swing.JLabel();
        txt16 = new javax.swing.JTextField();
        filler23 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab33 = new javax.swing.JLabel();
        txt8 = new javax.swing.JTextField();
        pan102 = new javax.swing.JPanel();
        filler17 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab16 = new javax.swing.JLabel();
        txt4 = new javax.swing.JTextField();
        filler24 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        labl17 = new javax.swing.JLabel();
        txt5 = new javax.swing.JTextField();
        btn5 = new javax.swing.JButton();
        pan103 = new javax.swing.JPanel();
        filler18 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab27 = new javax.swing.JLabel();
        checkBox1 = new javax.swing.JCheckBox();
        filler25 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(26, 0), new java.awt.Dimension(12, 32767));
        lab20 = new javax.swing.JLabel();
        txt21 = new javax.swing.JTextField();
        pan101 = new javax.swing.JPanel();
        filler22 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab24 = new javax.swing.JLabel();
        txt12 = new javax.swing.JTextField();
        pan92 = new javax.swing.JPanel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab19 = new javax.swing.JLabel();
        txt7 = new javax.swing.JTextField();
        btn7 = new javax.swing.JButton();
        filler21 = new javax.swing.Box.Filler(new java.awt.Dimension(12, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(12, 32767));
        txt17 = new javax.swing.JTextField();
        btn17 = new javax.swing.JButton();
        pan93 = new javax.swing.JPanel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab31 = new javax.swing.JLabel();
        txt19 = new javax.swing.JTextField();
        btn19 = new javax.swing.JButton();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(12, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(12, 32767));
        lab32 = new javax.swing.JLabel();
        txt20 = new javax.swing.JTextField();
        btn20 = new javax.swing.JButton();
        pan105 = new javax.swing.JPanel();
        filler20 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        pan94 = new javax.swing.JPanel();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab34 = new javax.swing.JLabel();
        txt22 = new javax.swing.JTextField();
        btn13 = new javax.swing.JButton();
        btn22 = new javax.swing.JButton();
        pan95 = new javax.swing.JPanel();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab22 = new javax.swing.JLabel();
        txt10 = new javax.swing.JTextField();
        btn8 = new javax.swing.JButton();
        btn37 = new javax.swing.JButton();
        pan96 = new javax.swing.JPanel();
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab23 = new javax.swing.JLabel();
        txt11 = new javax.swing.JTextField();
        btn11 = new javax.swing.JButton();
        pan97 = new javax.swing.JPanel();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab26 = new javax.swing.JLabel();
        txt14 = new javax.swing.JTextField();
        pan104 = new javax.swing.JPanel();
        filler19 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab52 = new javax.swing.JLabel();
        txt42 = new javax.swing.JTextField();
        btn35 = new javax.swing.JButton();
        pan7 = new javax.swing.JPanel();
        pan107 = new javax.swing.JPanel();
        pan11 = new javax.swing.JPanel();
        filler26 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab36 = new javax.swing.JLabel();
        txt25 = new javax.swing.JTextField();
        btn9 = new javax.swing.JButton();
        filler34 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        txt24 = new javax.swing.JTextField();
        btn21 = new javax.swing.JButton();
        pan12 = new javax.swing.JPanel();
        filler27 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab37 = new javax.swing.JLabel();
        txt26 = new javax.swing.JTextField();
        btn23 = new javax.swing.JButton();
        filler33 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        lab38 = new javax.swing.JLabel();
        txt27 = new javax.swing.JTextField();
        btn24 = new javax.swing.JButton();
        pan18 = new javax.swing.JPanel();
        filler31 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab18 = new javax.swing.JLabel();
        txt6 = new javax.swing.JTextField();
        filler38 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(12, 32767));
        lab42 = new javax.swing.JLabel();
        txt31 = new javax.swing.JTextField();
        btn6 = new javax.swing.JButton();
        pan109 = new javax.swing.JPanel();
        filler28 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        pan13 = new javax.swing.JPanel();
        filler29 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab40 = new javax.swing.JLabel();
        txt29 = new javax.swing.JTextField();
        btn14 = new javax.swing.JButton();
        btn25 = new javax.swing.JButton();
        pan17 = new javax.swing.JPanel();
        filler30 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab41 = new javax.swing.JLabel();
        txt30 = new javax.swing.JTextField();
        btn12 = new javax.swing.JButton();
        btn10 = new javax.swing.JButton();
        pan19 = new javax.swing.JPanel();
        filler32 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab39 = new javax.swing.JLabel();
        txt28 = new javax.swing.JTextField();
        btn36 = new javax.swing.JButton();
        pan8 = new javax.swing.JPanel();
        pan108 = new javax.swing.JPanel();
        pan20 = new javax.swing.JPanel();
        filler35 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab54 = new javax.swing.JLabel();
        txt48 = new javax.swing.JTextField();
        filler44 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(28, 0), new java.awt.Dimension(12, 32767));
        lab55 = new javax.swing.JLabel();
        txt45 = new javax.swing.JTextField();
        filler45 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        pan28 = new javax.swing.JPanel();
        filler43 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab56 = new javax.swing.JLabel();
        txt46 = new javax.swing.JTextField();
        filler49 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(28, 0), new java.awt.Dimension(12, 32767));
        lab49 = new javax.swing.JLabel();
        txt39 = new javax.swing.JTextField();
        pan21 = new javax.swing.JPanel();
        filler36 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab43 = new javax.swing.JLabel();
        txt32 = new javax.swing.JTextField();
        btn26 = new javax.swing.JButton();
        filler46 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        txt33 = new javax.swing.JTextField();
        btn16 = new javax.swing.JButton();
        pan22 = new javax.swing.JPanel();
        filler37 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab44 = new javax.swing.JLabel();
        txt34 = new javax.swing.JTextField();
        btn27 = new javax.swing.JButton();
        filler47 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        lab45 = new javax.swing.JLabel();
        txt35 = new javax.swing.JTextField();
        btn28 = new javax.swing.JButton();
        pan26 = new javax.swing.JPanel();
        filler41 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab50 = new javax.swing.JLabel();
        txt40 = new javax.swing.JTextField();
        btn33 = new javax.swing.JButton();
        filler48 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(12, 32767));
        lab35 = new javax.swing.JLabel();
        txt23 = new javax.swing.JTextField();
        pqn109 = new javax.swing.JPanel();
        pan24 = new javax.swing.JPanel();
        filler39 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab47 = new javax.swing.JLabel();
        txt37 = new javax.swing.JTextField();
        btn29 = new javax.swing.JButton();
        btn30 = new javax.swing.JButton();
        pan25 = new javax.swing.JPanel();
        filler40 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab48 = new javax.swing.JLabel();
        txt38 = new javax.swing.JTextField();
        btn31 = new javax.swing.JButton();
        btn32 = new javax.swing.JButton();
        pan27 = new javax.swing.JPanel();
        filler42 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(12, 32767));
        lab51 = new javax.swing.JLabel();
        txt41 = new javax.swing.JTextField();
        btn34 = new javax.swing.JButton();
        pan3 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        lab1 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        lab2 = new javax.swing.JLabel();

        itReport1.setText("Артикулы");
        itReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itReport1ppmCategAction(evt);
            }
        });
        ppmReport.add(itReport1);

        itReport2.setText("xxx");
        itReport2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itReport2ppmCategAction(evt);
            }
        });
        ppmReport.add(itReport2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Материальные ценности");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Artikles.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));

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

        btnMove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c050.gif"))); // NOI18N
        btnMove.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnMove.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnMove.setFocusable(false);
        btnMove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMove.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMove.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMove.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMove.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnMove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 555, Short.MAX_VALUE)
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
                    .addComponent(btnReport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMove, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setMinimumSize(new java.awt.Dimension(0, 0));
        center.setPreferredSize(new java.awt.Dimension(800, 550));
        center.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(180, 500));
        pan4.setLayout(new java.awt.BorderLayout());

        scrTree.setBorder(null);

        tree.setFont(frames.UGui.getFont(0,0));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Мат. ценности");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Профили");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Aксессуары");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("БлаБла");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tree.setMaximumSize(new java.awt.Dimension(200, 400));
        scrTree.setViewportView(tree);

        pan4.add(scrTree, java.awt.BorderLayout.CENTER);

        center.add(pan4, java.awt.BorderLayout.WEST);

        pan5.setPreferredSize(new java.awt.Dimension(280, 500));
        pan5.setLayout(new java.awt.BorderLayout());

        tab1.setFont(frames.UGui.getFont(0,0));
        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "111", null, null, null, null, null},
                {"2", "222", null, null, null, null, null}
            },
            new String [] {
                "Актикул", "Название", "Отход %", "Коэф. ценовой", "id", "id", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setMinimumSize(new java.awt.Dimension(0, 0));
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Artikles.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(26);
            tab1.getColumnModel().getColumn(2).setMaxWidth(120);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(26);
            tab1.getColumnModel().getColumn(3).setMaxWidth(120);
            tab1.getColumnModel().getColumn(4).setMaxWidth(0);
            tab1.getColumnModel().getColumn(5).setMaxWidth(0);
            tab1.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        pan5.add(scr1, java.awt.BorderLayout.CENTER);

        center.add(pan5, java.awt.BorderLayout.CENTER);

        pan6.setPreferredSize(new java.awt.Dimension(360, 500));
        pan6.setLayout(new java.awt.CardLayout());

        pan2.setPreferredSize(new java.awt.Dimension(360, 24));
        pan2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        pan106.setPreferredSize(new java.awt.Dimension(360, 12));

        javax.swing.GroupLayout pan106Layout = new javax.swing.GroupLayout(pan106);
        pan106.setLayout(pan106Layout);
        pan106Layout.setHorizontalGroup(
            pan106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        pan106Layout.setVerticalGroup(
            pan106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pan2.add(pan106);

        pan91.setPreferredSize(new java.awt.Dimension(360, 24));
        pan91.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan91.add(filler4);

        lab13.setFont(frames.UGui.getFont(0,0));
        lab13.setText("Длина");
        lab13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab13.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab13.setMinimumSize(new java.awt.Dimension(34, 14));
        lab13.setPreferredSize(new java.awt.Dimension(48, 18));
        pan91.add(lab13);

        txt1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt1.setPreferredSize(new java.awt.Dimension(44, 18));
        pan91.add(txt1);
        pan91.add(filler3);

        lab14.setFont(frames.UGui.getFont(0,0));
        lab14.setText("Ширина");
        lab14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab14.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab14.setMinimumSize(new java.awt.Dimension(34, 14));
        lab14.setPreferredSize(new java.awt.Dimension(48, 18));
        pan91.add(lab14);

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setPreferredSize(new java.awt.Dimension(44, 18));
        pan91.add(txt2);
        pan91.add(filler5);

        lab15.setFont(frames.UGui.getFont(0,0));
        lab15.setText("Толщина");
        lab15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab15.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab15.setMinimumSize(new java.awt.Dimension(34, 14));
        lab15.setPreferredSize(new java.awt.Dimension(54, 18));
        pan91.add(lab15);

        txt3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt3.setPreferredSize(new java.awt.Dimension(44, 18));
        pan91.add(txt3);

        pan2.add(pan91);

        pan98.setPreferredSize(new java.awt.Dimension(360, 24));
        pan98.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan98.add(filler13);

        lab30.setFont(frames.UGui.getFont(0,0));
        lab30.setText("Система констант");
        lab30.setToolTipText("");
        lab30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab30.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab30.setMinimumSize(new java.awt.Dimension(34, 14));
        lab30.setPreferredSize(new java.awt.Dimension(98, 18));
        pan98.add(lab30);

        txt18.setEditable(false);
        txt18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt18.setPreferredSize(new java.awt.Dimension(204, 18));
        pan98.add(txt18);

        btn18.setText("...");
        btn18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn18.setMaximumSize(new java.awt.Dimension(18, 18));
        btn18.setMinimumSize(new java.awt.Dimension(18, 18));
        btn18.setPreferredSize(new java.awt.Dimension(18, 18));
        btn18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn18(evt);
            }
        });
        pan98.add(btn18);

        pan2.add(pan98);

        pan99.setPreferredSize(new java.awt.Dimension(360, 24));
        pan99.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan99.add(filler14);

        lab28.setFont(frames.UGui.getFont(0,0));
        lab28.setText("Наплав, полка(N)");
        lab28.setToolTipText("");
        lab28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab28.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab28.setMinimumSize(new java.awt.Dimension(34, 14));
        lab28.setPreferredSize(new java.awt.Dimension(98, 18));
        pan99.add(lab28);

        txt15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt15.setPreferredSize(new java.awt.Dimension(44, 18));
        pan99.add(txt15);
        pan99.add(filler16);

        lab21.setFont(frames.UGui.getFont(0,0));
        lab21.setText("Фурн. паз (F)");
        lab21.setToolTipText("");
        lab21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab21.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab21.setMinimumSize(new java.awt.Dimension(34, 14));
        lab21.setPreferredSize(new java.awt.Dimension(98, 18));
        pan99.add(lab21);

        txt9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt9.setPreferredSize(new java.awt.Dimension(44, 18));
        pan99.add(txt9);

        pan2.add(pan99);

        pan100.setPreferredSize(new java.awt.Dimension(360, 24));
        pan100.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan100.add(filler15);

        lab29.setFont(frames.UGui.getFont(0,0));
        lab29.setText("Толщ. наплава(T) ");
        lab29.setToolTipText("");
        lab29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab29.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab29.setMinimumSize(new java.awt.Dimension(34, 14));
        lab29.setPreferredSize(new java.awt.Dimension(98, 18));
        pan100.add(lab29);

        txt16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt16.setPreferredSize(new java.awt.Dimension(44, 18));
        pan100.add(txt16);
        pan100.add(filler23);

        lab33.setFont(frames.UGui.getFont(0,0));
        lab33.setText("От края до оси (B)");
        lab33.setToolTipText("");
        lab33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab33.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab33.setMinimumSize(new java.awt.Dimension(34, 14));
        pan100.add(lab33);

        txt8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt8.setPreferredSize(new java.awt.Dimension(44, 18));
        pan100.add(txt8);

        pan2.add(pan100);

        pan102.setPreferredSize(new java.awt.Dimension(360, 24));
        pan102.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan102.add(filler17);

        lab16.setFont(frames.UGui.getFont(0,0));
        lab16.setText("Уд.вес кг/ед");
        lab16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab16.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab16.setMinimumSize(new java.awt.Dimension(34, 14));
        lab16.setPreferredSize(new java.awt.Dimension(98, 18));
        pan102.add(lab16);

        txt4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt4.setPreferredSize(new java.awt.Dimension(44, 18));
        pan102.add(txt4);
        pan102.add(filler24);

        labl17.setFont(frames.UGui.getFont(0,0));
        labl17.setText("Ед.измерения");
        labl17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        labl17.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        labl17.setMinimumSize(new java.awt.Dimension(34, 14));
        labl17.setPreferredSize(new java.awt.Dimension(98, 18));
        pan102.add(labl17);

        txt5.setEditable(false);
        txt5.setFont(frames.UGui.getFont(0,0));
        txt5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt5.setPreferredSize(new java.awt.Dimension(44, 18));
        pan102.add(txt5);

        btn5.setText("...");
        btn5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn5.setMaximumSize(new java.awt.Dimension(18, 18));
        btn5.setMinimumSize(new java.awt.Dimension(18, 18));
        btn5.setPreferredSize(new java.awt.Dimension(18, 18));
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5(evt);
            }
        });
        pan102.add(btn5);

        pan2.add(pan102);

        pan103.setPreferredSize(new java.awt.Dimension(360, 24));
        pan103.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan103.add(filler18);

        lab27.setFont(frames.UGui.getFont(0,0));
        lab27.setText("С уплотнением");
        lab27.setToolTipText("");
        lab27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab27.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab27.setMinimumSize(new java.awt.Dimension(34, 14));
        lab27.setPreferredSize(new java.awt.Dimension(98, 18));
        pan103.add(lab27);

        checkBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        checkBox1.setMaximumSize(new java.awt.Dimension(20, 20));
        checkBox1.setMinimumSize(new java.awt.Dimension(20, 20));
        checkBox1.setPreferredSize(new java.awt.Dimension(20, 20));
        checkBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox1Action(evt);
            }
        });
        pan103.add(checkBox1);
        pan103.add(filler25);

        lab20.setFont(frames.UGui.getFont(0,0));
        lab20.setText("Толщина фрезы");
        lab20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab20.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab20.setMinimumSize(new java.awt.Dimension(34, 14));
        lab20.setPreferredSize(new java.awt.Dimension(98, 18));
        pan103.add(lab20);

        txt21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt21.setPreferredSize(new java.awt.Dimension(44, 18));
        pan103.add(txt21);

        pan2.add(pan103);

        pan101.setPreferredSize(new java.awt.Dimension(360, 24));
        pan101.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan101.add(filler22);

        lab24.setFont(frames.UGui.getFont(0,0));
        lab24.setText("Мин. радиус гиба");
        lab24.setToolTipText("");
        lab24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab24.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab24.setMinimumSize(new java.awt.Dimension(34, 14));
        lab24.setPreferredSize(new java.awt.Dimension(98, 18));
        pan101.add(lab24);

        txt12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt12.setPreferredSize(new java.awt.Dimension(44, 18));
        pan101.add(txt12);

        pan2.add(pan101);

        pan92.setPreferredSize(new java.awt.Dimension(360, 24));
        pan92.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan92.add(filler6);

        lab19.setFont(frames.UGui.getFont(0,0));
        lab19.setText("Валюта");
        lab19.setToolTipText("");
        lab19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab19.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab19.setMinimumSize(new java.awt.Dimension(34, 14));
        lab19.setPreferredSize(new java.awt.Dimension(98, 18));
        pan92.add(lab19);

        txt7.setEditable(false);
        txt7.setFont(frames.UGui.getFont(0,0));
        txt7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt7.setPreferredSize(new java.awt.Dimension(80, 18));
        pan92.add(txt7);

        btn7.setText("...");
        btn7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn7.setMaximumSize(new java.awt.Dimension(18, 18));
        btn7.setMinimumSize(new java.awt.Dimension(18, 18));
        btn7.setName("btn7"); // NOI18N
        btn7.setPreferredSize(new java.awt.Dimension(18, 18));
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7(evt);
            }
        });
        pan92.add(btn7);
        pan92.add(filler21);

        txt17.setEditable(false);
        txt17.setFont(frames.UGui.getFont(0,0));
        txt17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt17.setPreferredSize(new java.awt.Dimension(80, 18));
        pan92.add(txt17);

        btn17.setText("...");
        btn17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn17.setMaximumSize(new java.awt.Dimension(18, 18));
        btn17.setMinimumSize(new java.awt.Dimension(18, 18));
        btn17.setName("btn17"); // NOI18N
        btn17.setPreferredSize(new java.awt.Dimension(18, 18));
        btn17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7(evt);
            }
        });
        pan92.add(btn17);

        pan2.add(pan92);

        pan93.setPreferredSize(new java.awt.Dimension(360, 24));
        pan93.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan93.add(filler7);

        lab31.setFont(frames.UGui.getFont(0,0));
        lab31.setText("Наценки (коэф)");
        lab31.setToolTipText("");
        lab31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab31.setMinimumSize(new java.awt.Dimension(34, 14));
        lab31.setPreferredSize(new java.awt.Dimension(98, 18));
        pan93.add(lab31);

        txt19.setEditable(false);
        txt19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt19.setPreferredSize(new java.awt.Dimension(44, 18));
        pan93.add(txt19);

        btn19.setText("...");
        btn19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn19.setMaximumSize(new java.awt.Dimension(18, 18));
        btn19.setMinimumSize(new java.awt.Dimension(18, 18));
        btn19.setPreferredSize(new java.awt.Dimension(18, 18));
        btn19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn19(evt);
            }
        });
        pan93.add(btn19);
        pan93.add(filler8);

        lab32.setFont(frames.UGui.getFont(0,0));
        lab32.setText("Скидка (%)");
        lab32.setToolTipText("");
        lab32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab32.setMinimumSize(new java.awt.Dimension(34, 14));
        lab32.setPreferredSize(new java.awt.Dimension(72, 18));
        pan93.add(lab32);

        txt20.setEditable(false);
        txt20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt20.setPreferredSize(new java.awt.Dimension(40, 18));
        pan93.add(txt20);

        btn20.setText("...");
        btn20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn20.setMaximumSize(new java.awt.Dimension(18, 18));
        btn20.setMinimumSize(new java.awt.Dimension(18, 18));
        btn20.setPreferredSize(new java.awt.Dimension(18, 18));
        btn20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn20(evt);
            }
        });
        pan93.add(btn20);

        pan2.add(pan93);

        pan105.setPreferredSize(new java.awt.Dimension(360, 12));
        pan105.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan105.add(filler20);

        pan2.add(pan105);

        pan94.setPreferredSize(new java.awt.Dimension(360, 24));
        pan94.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan94.add(filler9);

        lab34.setFont(frames.UGui.getFont(0,0));
        lab34.setText("Категория");
        lab34.setToolTipText("");
        lab34.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab34.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab34.setMinimumSize(new java.awt.Dimension(34, 14));
        lab34.setPreferredSize(new java.awt.Dimension(98, 18));
        pan94.add(lab34);

        txt22.setEditable(false);
        txt22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt22.setPreferredSize(new java.awt.Dimension(180, 18));
        pan94.add(txt22);

        btn13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn13.setMaximumSize(new java.awt.Dimension(18, 18));
        btn13.setMinimumSize(new java.awt.Dimension(18, 18));
        btn13.setPreferredSize(new java.awt.Dimension(18, 18));
        btn13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn13(evt);
            }
        });
        pan94.add(btn13);

        btn22.setText("...");
        btn22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn22.setMaximumSize(new java.awt.Dimension(18, 18));
        btn22.setMinimumSize(new java.awt.Dimension(18, 18));
        btn22.setPreferredSize(new java.awt.Dimension(18, 18));
        btn22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn22(evt);
            }
        });
        pan94.add(btn22);

        pan2.add(pan94);

        pan95.setPreferredSize(new java.awt.Dimension(360, 24));
        pan95.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan95.add(filler10);

        lab22.setFont(frames.UGui.getFont(0,0));
        lab22.setText("Серия профилей");
        lab22.setToolTipText("");
        lab22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab22.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab22.setMinimumSize(new java.awt.Dimension(34, 14));
        lab22.setPreferredSize(new java.awt.Dimension(98, 18));
        pan95.add(lab22);

        txt10.setEditable(false);
        txt10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt10.setPreferredSize(new java.awt.Dimension(180, 18));
        pan95.add(txt10);

        btn8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn8.setMaximumSize(new java.awt.Dimension(18, 18));
        btn8.setMinimumSize(new java.awt.Dimension(18, 18));
        btn8.setPreferredSize(new java.awt.Dimension(18, 18));
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8(evt);
            }
        });
        pan95.add(btn8);

        btn37.setText("...");
        btn37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn37.setMaximumSize(new java.awt.Dimension(18, 18));
        btn37.setMinimumSize(new java.awt.Dimension(18, 18));
        btn37.setPreferredSize(new java.awt.Dimension(18, 18));
        btn37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn37(evt);
            }
        });
        pan95.add(btn37);

        pan2.add(pan95);

        pan96.setPreferredSize(new java.awt.Dimension(360, 24));
        pan96.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan96.add(filler11);

        lab23.setFont(frames.UGui.getFont(0,0));
        lab23.setText("Аналог профиля");
        lab23.setToolTipText("");
        lab23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab23.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab23.setMinimumSize(new java.awt.Dimension(34, 14));
        lab23.setPreferredSize(new java.awt.Dimension(98, 18));
        pan96.add(lab23);

        txt11.setEditable(false);
        txt11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt11.setPreferredSize(new java.awt.Dimension(204, 18));
        pan96.add(txt11);

        btn11.setText("...");
        btn11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn11.setMaximumSize(new java.awt.Dimension(18, 18));
        btn11.setMinimumSize(new java.awt.Dimension(18, 18));
        btn11.setPreferredSize(new java.awt.Dimension(18, 18));
        btn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn11(evt);
            }
        });
        pan96.add(btn11);

        pan2.add(pan96);

        pan97.setPreferredSize(new java.awt.Dimension(360, 24));
        pan97.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan97.add(filler12);

        lab26.setFont(frames.UGui.getFont(0,0));
        lab26.setText("Технолог- ий код");
        lab26.setToolTipText("");
        lab26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab26.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab26.setMinimumSize(new java.awt.Dimension(34, 14));
        lab26.setPreferredSize(new java.awt.Dimension(98, 18));
        pan97.add(lab26);

        txt14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt14.setPreferredSize(new java.awt.Dimension(204, 18));
        pan97.add(txt14);

        pan2.add(pan97);

        pan104.setPreferredSize(new java.awt.Dimension(360, 24));
        pan104.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan104.add(filler19);

        lab52.setFont(frames.UGui.getFont(0,0));
        lab52.setText("Поставщик");
        lab52.setToolTipText("");
        lab52.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab52.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab52.setMinimumSize(new java.awt.Dimension(34, 14));
        lab52.setPreferredSize(new java.awt.Dimension(98, 18));
        pan104.add(lab52);

        txt42.setEditable(false);
        txt42.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt42.setFocusable(false);
        txt42.setPreferredSize(new java.awt.Dimension(204, 18));
        pan104.add(txt42);

        btn35.setText("...");
        btn35.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn35.setMaximumSize(new java.awt.Dimension(18, 18));
        btn35.setMinimumSize(new java.awt.Dimension(18, 18));
        btn35.setPreferredSize(new java.awt.Dimension(18, 18));
        pan104.add(btn35);

        pan2.add(pan104);

        pan6.add(pan2, "pan2");

        pan7.setPreferredSize(new java.awt.Dimension(360, 24));
        pan7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        pan107.setPreferredSize(new java.awt.Dimension(360, 12));

        javax.swing.GroupLayout pan107Layout = new javax.swing.GroupLayout(pan107);
        pan107.setLayout(pan107Layout);
        pan107Layout.setHorizontalGroup(
            pan107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        pan107Layout.setVerticalGroup(
            pan107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pan7.add(pan107);

        pan11.setPreferredSize(new java.awt.Dimension(360, 24));
        pan11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan11.add(filler26);

        lab36.setFont(frames.UGui.getFont(0,0));
        lab36.setText("Валюта");
        lab36.setToolTipText("");
        lab36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab36.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab36.setMinimumSize(new java.awt.Dimension(34, 14));
        lab36.setPreferredSize(new java.awt.Dimension(98, 18));
        pan11.add(lab36);

        txt25.setEditable(false);
        txt25.setFont(frames.UGui.getFont(0,0));
        txt25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt25.setPreferredSize(new java.awt.Dimension(80, 18));
        pan11.add(txt25);

        btn9.setText("...");
        btn9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn9.setMaximumSize(new java.awt.Dimension(18, 18));
        btn9.setMinimumSize(new java.awt.Dimension(18, 18));
        btn9.setName("btnField7"); // NOI18N
        btn9.setPreferredSize(new java.awt.Dimension(18, 18));
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7(evt);
            }
        });
        pan11.add(btn9);
        pan11.add(filler34);

        txt24.setEditable(false);
        txt24.setFont(frames.UGui.getFont(0,0));
        txt24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt24.setPreferredSize(new java.awt.Dimension(80, 18));
        pan11.add(txt24);

        btn21.setText("...");
        btn21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn21.setMaximumSize(new java.awt.Dimension(18, 18));
        btn21.setMinimumSize(new java.awt.Dimension(18, 18));
        btn21.setName("btnField17"); // NOI18N
        btn21.setPreferredSize(new java.awt.Dimension(18, 18));
        btn21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7(evt);
            }
        });
        pan11.add(btn21);

        pan7.add(pan11);

        pan12.setPreferredSize(new java.awt.Dimension(360, 24));
        pan12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan12.add(filler27);

        lab37.setFont(frames.UGui.getFont(0,0));
        lab37.setText("Наценки (коэф)");
        lab37.setToolTipText("");
        lab37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab37.setMinimumSize(new java.awt.Dimension(34, 14));
        lab37.setPreferredSize(new java.awt.Dimension(98, 18));
        pan12.add(lab37);

        txt26.setEditable(false);
        txt26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt26.setPreferredSize(new java.awt.Dimension(44, 18));
        pan12.add(txt26);

        btn23.setText("...");
        btn23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn23.setMaximumSize(new java.awt.Dimension(18, 18));
        btn23.setMinimumSize(new java.awt.Dimension(18, 18));
        btn23.setPreferredSize(new java.awt.Dimension(18, 18));
        btn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn19(evt);
            }
        });
        pan12.add(btn23);
        pan12.add(filler33);

        lab38.setFont(frames.UGui.getFont(0,0));
        lab38.setText("Скидка (%)");
        lab38.setToolTipText("");
        lab38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab38.setMinimumSize(new java.awt.Dimension(34, 14));
        lab38.setPreferredSize(new java.awt.Dimension(68, 18));
        pan12.add(lab38);

        txt27.setEditable(false);
        txt27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt27.setPreferredSize(new java.awt.Dimension(44, 18));
        pan12.add(txt27);

        btn24.setText("...");
        btn24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn24.setMaximumSize(new java.awt.Dimension(18, 18));
        btn24.setMinimumSize(new java.awt.Dimension(18, 18));
        btn24.setPreferredSize(new java.awt.Dimension(18, 18));
        btn24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn20(evt);
            }
        });
        pan12.add(btn24);

        pan7.add(pan12);

        pan18.setPreferredSize(new java.awt.Dimension(360, 24));
        pan18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan18.add(filler31);

        lab18.setFont(frames.UGui.getFont(0,0));
        lab18.setText("Уд.вес кг/ед");
        lab18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab18.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab18.setMinimumSize(new java.awt.Dimension(34, 14));
        lab18.setPreferredSize(new java.awt.Dimension(98, 18));
        pan18.add(lab18);

        txt6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt6.setPreferredSize(new java.awt.Dimension(44, 18));
        pan18.add(txt6);
        pan18.add(filler38);

        lab42.setFont(frames.UGui.getFont(0,0));
        lab42.setText("Ед.изм.");
        lab42.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab42.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab42.setMinimumSize(new java.awt.Dimension(34, 14));
        lab42.setPreferredSize(new java.awt.Dimension(68, 18));
        pan18.add(lab42);

        txt31.setEditable(false);
        txt31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt31.setPreferredSize(new java.awt.Dimension(44, 18));
        txt31.setRequestFocusEnabled(false);
        pan18.add(txt31);

        btn6.setText("...");
        btn6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn6.setMaximumSize(new java.awt.Dimension(18, 18));
        btn6.setMinimumSize(new java.awt.Dimension(18, 18));
        btn6.setPreferredSize(new java.awt.Dimension(18, 18));
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5(evt);
            }
        });
        pan18.add(btn6);

        pan7.add(pan18);

        pan109.setPreferredSize(new java.awt.Dimension(360, 12));
        pan109.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan109.add(filler28);

        pan7.add(pan109);

        pan13.setPreferredSize(new java.awt.Dimension(360, 24));
        pan13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan13.add(filler29);

        lab40.setFont(frames.UGui.getFont(0,0));
        lab40.setText("Категория");
        lab40.setToolTipText("");
        lab40.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab40.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab40.setMinimumSize(new java.awt.Dimension(34, 14));
        lab40.setPreferredSize(new java.awt.Dimension(98, 18));
        pan13.add(lab40);

        txt29.setEditable(false);
        txt29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt29.setPreferredSize(new java.awt.Dimension(180, 18));
        pan13.add(txt29);

        btn14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn14.setMaximumSize(new java.awt.Dimension(18, 18));
        btn14.setMinimumSize(new java.awt.Dimension(18, 18));
        btn14.setPreferredSize(new java.awt.Dimension(18, 18));
        btn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn13(evt);
            }
        });
        pan13.add(btn14);

        btn25.setText("...");
        btn25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn25.setMaximumSize(new java.awt.Dimension(18, 18));
        btn25.setMinimumSize(new java.awt.Dimension(18, 18));
        btn25.setPreferredSize(new java.awt.Dimension(18, 18));
        btn25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn22(evt);
            }
        });
        pan13.add(btn25);

        pan7.add(pan13);

        pan17.setPreferredSize(new java.awt.Dimension(360, 24));
        pan17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan17.add(filler30);

        lab41.setFont(frames.UGui.getFont(0,0));
        lab41.setText("Серия профилей");
        lab41.setToolTipText("");
        lab41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab41.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab41.setMinimumSize(new java.awt.Dimension(34, 14));
        lab41.setPreferredSize(new java.awt.Dimension(98, 18));
        pan17.add(lab41);

        txt30.setEditable(false);
        txt30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt30.setPreferredSize(new java.awt.Dimension(180, 18));
        pan17.add(txt30);

        btn12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn12.setMaximumSize(new java.awt.Dimension(18, 18));
        btn12.setMinimumSize(new java.awt.Dimension(18, 18));
        btn12.setPreferredSize(new java.awt.Dimension(18, 18));
        btn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8(evt);
            }
        });
        pan17.add(btn12);

        btn10.setText("...");
        btn10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn10.setMaximumSize(new java.awt.Dimension(18, 18));
        btn10.setMinimumSize(new java.awt.Dimension(18, 18));
        btn10.setPreferredSize(new java.awt.Dimension(18, 18));
        btn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn37(evt);
            }
        });
        pan17.add(btn10);

        pan7.add(pan17);

        pan19.setPreferredSize(new java.awt.Dimension(360, 24));
        pan19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan19.add(filler32);

        lab39.setFont(frames.UGui.getFont(0,0));
        lab39.setText("Поставщик");
        lab39.setToolTipText("");
        lab39.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab39.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab39.setMinimumSize(new java.awt.Dimension(34, 14));
        lab39.setPreferredSize(new java.awt.Dimension(98, 18));
        pan19.add(lab39);

        txt28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt28.setEnabled(false);
        txt28.setFocusable(false);
        txt28.setPreferredSize(new java.awt.Dimension(204, 18));
        pan19.add(txt28);

        btn36.setText("...");
        btn36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn36.setMaximumSize(new java.awt.Dimension(18, 18));
        btn36.setMinimumSize(new java.awt.Dimension(18, 18));
        btn36.setPreferredSize(new java.awt.Dimension(18, 18));
        pan19.add(btn36);

        pan7.add(pan19);

        pan6.add(pan7, "pan7");

        pan8.setPreferredSize(new java.awt.Dimension(360, 24));
        pan8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        pan108.setPreferredSize(new java.awt.Dimension(360, 12));

        javax.swing.GroupLayout pan108Layout = new javax.swing.GroupLayout(pan108);
        pan108.setLayout(pan108Layout);
        pan108Layout.setHorizontalGroup(
            pan108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        pan108Layout.setVerticalGroup(
            pan108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pan8.add(pan108);

        pan20.setPreferredSize(new java.awt.Dimension(360, 24));
        pan20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan20.add(filler35);

        lab54.setFont(frames.UGui.getFont(0,0));
        lab54.setText("Длина");
        lab54.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab54.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab54.setMinimumSize(new java.awt.Dimension(34, 14));
        lab54.setPreferredSize(new java.awt.Dimension(98, 18));
        pan20.add(lab54);

        txt48.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt48.setPreferredSize(new java.awt.Dimension(44, 18));
        pan20.add(txt48);
        pan20.add(filler44);

        lab55.setFont(frames.UGui.getFont(0,0));
        lab55.setText("Ширина");
        lab55.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab55.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab55.setMinimumSize(new java.awt.Dimension(34, 14));
        lab55.setPreferredSize(new java.awt.Dimension(74, 18));
        pan20.add(lab55);

        txt45.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt45.setPreferredSize(new java.awt.Dimension(44, 18));
        pan20.add(txt45);
        pan20.add(filler45);

        pan8.add(pan20);

        pan28.setPreferredSize(new java.awt.Dimension(360, 24));
        pan28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan28.add(filler43);

        lab56.setFont(frames.UGui.getFont(0,0));
        lab56.setText("Толщина");
        lab56.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab56.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab56.setMinimumSize(new java.awt.Dimension(34, 14));
        lab56.setPreferredSize(new java.awt.Dimension(98, 18));
        pan28.add(lab56);

        txt46.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt46.setPreferredSize(new java.awt.Dimension(44, 18));
        pan28.add(txt46);
        pan28.add(filler49);

        lab49.setFont(frames.UGui.getFont(0,0));
        lab49.setText("Уд.вес кг/ед");
        lab49.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab49.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab49.setMinimumSize(new java.awt.Dimension(34, 14));
        lab49.setPreferredSize(new java.awt.Dimension(74, 18));
        pan28.add(lab49);

        txt39.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt39.setPreferredSize(new java.awt.Dimension(44, 18));
        pan28.add(txt39);

        pan8.add(pan28);

        pan21.setPreferredSize(new java.awt.Dimension(360, 24));
        pan21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan21.add(filler36);

        lab43.setFont(frames.UGui.getFont(0,0));
        lab43.setText("Валюта");
        lab43.setToolTipText("");
        lab43.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab43.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab43.setMinimumSize(new java.awt.Dimension(34, 14));
        lab43.setPreferredSize(new java.awt.Dimension(98, 18));
        pan21.add(lab43);

        txt32.setEditable(false);
        txt32.setFont(frames.UGui.getFont(0,0));
        txt32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt32.setPreferredSize(new java.awt.Dimension(44, 18));
        pan21.add(txt32);

        btn26.setText("...");
        btn26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn26.setMaximumSize(new java.awt.Dimension(18, 18));
        btn26.setMinimumSize(new java.awt.Dimension(18, 18));
        btn26.setName("btnField17"); // NOI18N
        btn26.setPreferredSize(new java.awt.Dimension(18, 18));
        btn26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7(evt);
            }
        });
        pan21.add(btn26);
        pan21.add(filler46);

        txt33.setEditable(false);
        txt33.setFont(frames.UGui.getFont(0,0));
        txt33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt33.setPreferredSize(new java.awt.Dimension(50, 18));
        pan21.add(txt33);

        btn16.setText("...");
        btn16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn16.setMaximumSize(new java.awt.Dimension(18, 18));
        btn16.setMinimumSize(new java.awt.Dimension(18, 18));
        btn16.setName("btnField7"); // NOI18N
        btn16.setPreferredSize(new java.awt.Dimension(18, 18));
        btn16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7(evt);
            }
        });
        pan21.add(btn16);

        pan8.add(pan21);

        pan22.setPreferredSize(new java.awt.Dimension(360, 24));
        pan22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan22.add(filler37);

        lab44.setFont(frames.UGui.getFont(0,0));
        lab44.setText("Наценки (коэф)");
        lab44.setToolTipText("");
        lab44.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab44.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab44.setMinimumSize(new java.awt.Dimension(34, 14));
        lab44.setPreferredSize(new java.awt.Dimension(98, 18));
        pan22.add(lab44);

        txt34.setEditable(false);
        txt34.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt34.setPreferredSize(new java.awt.Dimension(44, 18));
        pan22.add(txt34);

        btn27.setText("...");
        btn27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn27.setMaximumSize(new java.awt.Dimension(18, 18));
        btn27.setMinimumSize(new java.awt.Dimension(18, 18));
        btn27.setPreferredSize(new java.awt.Dimension(18, 18));
        btn27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn19(evt);
            }
        });
        pan22.add(btn27);
        pan22.add(filler47);

        lab45.setFont(frames.UGui.getFont(0,0));
        lab45.setText("Скидка (%)");
        lab45.setToolTipText("");
        lab45.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab45.setMinimumSize(new java.awt.Dimension(34, 14));
        lab45.setPreferredSize(new java.awt.Dimension(74, 18));
        pan22.add(lab45);

        txt35.setEditable(false);
        txt35.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt35.setPreferredSize(new java.awt.Dimension(40, 18));
        pan22.add(txt35);

        btn28.setText("...");
        btn28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn28.setMaximumSize(new java.awt.Dimension(18, 18));
        btn28.setMinimumSize(new java.awt.Dimension(18, 18));
        btn28.setPreferredSize(new java.awt.Dimension(18, 18));
        btn28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn20(evt);
            }
        });
        pan22.add(btn28);

        pan8.add(pan22);

        pan26.setPreferredSize(new java.awt.Dimension(360, 24));
        pan26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan26.add(filler41);

        lab50.setFont(frames.UGui.getFont(0,0));
        lab50.setText("Ед.измерения");
        lab50.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab50.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab50.setMinimumSize(new java.awt.Dimension(34, 14));
        lab50.setPreferredSize(new java.awt.Dimension(98, 18));
        pan26.add(lab50);

        txt40.setEditable(false);
        txt40.setFont(frames.UGui.getFont(0,0));
        txt40.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt40.setPreferredSize(new java.awt.Dimension(44, 18));
        pan26.add(txt40);

        btn33.setText("...");
        btn33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn33.setMaximumSize(new java.awt.Dimension(18, 18));
        btn33.setMinimumSize(new java.awt.Dimension(18, 18));
        btn33.setPreferredSize(new java.awt.Dimension(18, 18));
        btn33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5(evt);
            }
        });
        pan26.add(btn33);
        pan26.add(filler48);

        lab35.setFont(frames.UGui.getFont(0,0));
        lab35.setText("Толщина фрезы");
        lab35.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab35.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab35.setMinimumSize(new java.awt.Dimension(34, 14));
        lab35.setPreferredSize(new java.awt.Dimension(98, 18));
        pan26.add(lab35);

        txt23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt23.setPreferredSize(new java.awt.Dimension(40, 18));
        pan26.add(txt23);

        pan8.add(pan26);

        pqn109.setPreferredSize(new java.awt.Dimension(360, 12));

        javax.swing.GroupLayout pqn109Layout = new javax.swing.GroupLayout(pqn109);
        pqn109.setLayout(pqn109Layout);
        pqn109Layout.setHorizontalGroup(
            pqn109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        pqn109Layout.setVerticalGroup(
            pqn109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pan8.add(pqn109);

        pan24.setPreferredSize(new java.awt.Dimension(360, 24));
        pan24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan24.add(filler39);

        lab47.setFont(frames.UGui.getFont(0,0));
        lab47.setText("Категория");
        lab47.setToolTipText("");
        lab47.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab47.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab47.setMinimumSize(new java.awt.Dimension(34, 14));
        lab47.setPreferredSize(new java.awt.Dimension(98, 18));
        pan24.add(lab47);

        txt37.setEditable(false);
        txt37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt37.setPreferredSize(new java.awt.Dimension(180, 18));
        pan24.add(txt37);

        btn29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn29.setMaximumSize(new java.awt.Dimension(18, 18));
        btn29.setMinimumSize(new java.awt.Dimension(18, 18));
        btn29.setPreferredSize(new java.awt.Dimension(18, 18));
        btn29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn13(evt);
            }
        });
        pan24.add(btn29);

        btn30.setText("...");
        btn30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn30.setMaximumSize(new java.awt.Dimension(18, 18));
        btn30.setMinimumSize(new java.awt.Dimension(18, 18));
        btn30.setPreferredSize(new java.awt.Dimension(18, 18));
        btn30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn22(evt);
            }
        });
        pan24.add(btn30);

        pan8.add(pan24);

        pan25.setPreferredSize(new java.awt.Dimension(360, 24));
        pan25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan25.add(filler40);

        lab48.setFont(frames.UGui.getFont(0,0));
        lab48.setText("Серия профилей");
        lab48.setToolTipText("");
        lab48.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab48.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab48.setMinimumSize(new java.awt.Dimension(34, 14));
        lab48.setPreferredSize(new java.awt.Dimension(98, 18));
        pan25.add(lab48);

        txt38.setEditable(false);
        txt38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt38.setPreferredSize(new java.awt.Dimension(180, 18));
        pan25.add(txt38);

        btn31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn31.setMaximumSize(new java.awt.Dimension(18, 18));
        btn31.setMinimumSize(new java.awt.Dimension(18, 18));
        btn31.setPreferredSize(new java.awt.Dimension(18, 18));
        btn31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8(evt);
            }
        });
        pan25.add(btn31);

        btn32.setText("...");
        btn32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn32.setMaximumSize(new java.awt.Dimension(18, 18));
        btn32.setMinimumSize(new java.awt.Dimension(18, 18));
        btn32.setPreferredSize(new java.awt.Dimension(18, 18));
        btn32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn37(evt);
            }
        });
        pan25.add(btn32);

        pan8.add(pan25);

        pan27.setPreferredSize(new java.awt.Dimension(360, 24));
        pan27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        pan27.add(filler42);

        lab51.setFont(frames.UGui.getFont(0,0));
        lab51.setText("Поставщик");
        lab51.setToolTipText("");
        lab51.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab51.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab51.setMinimumSize(new java.awt.Dimension(34, 14));
        lab51.setPreferredSize(new java.awt.Dimension(98, 18));
        pan27.add(lab51);

        txt41.setBackground(new java.awt.Color(212, 208, 200));
        txt41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt41.setFocusable(false);
        txt41.setPreferredSize(new java.awt.Dimension(204, 18));
        pan27.add(txt41);

        btn34.setText("...");
        btn34.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn34.setMaximumSize(new java.awt.Dimension(18, 18));
        btn34.setMinimumSize(new java.awt.Dimension(18, 18));
        btn34.setPreferredSize(new java.awt.Dimension(18, 18));
        btn34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn34(evt);
            }
        });
        pan27.add(btn34);

        pan8.add(pan27);

        pan6.add(pan8, "pan8");

        center.add(pan6, java.awt.BorderLayout.EAST);

        pan3.setPreferredSize(new java.awt.Dimension(600, 130));
        pan3.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setFont(frames.UGui.getFont(0,0));
        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Группа", "Название", "Применить", "Основная", "Применить", "Внутренняя", "Применить", "Внешняя", "Двухсторонняя", "За ед. веса", "Коэф. ценовой", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Object.class, java.lang.Object.class
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
                Artikles.this.mousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        tab2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(120);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(20);
            tab2.getColumnModel().getColumn(2).setMaxWidth(120);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(20);
            tab2.getColumnModel().getColumn(4).setMaxWidth(120);
            tab2.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(6).setPreferredWidth(20);
            tab2.getColumnModel().getColumn(6).setMaxWidth(120);
            tab2.getColumnModel().getColumn(7).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(8).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(9).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(10).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(11).setMaxWidth(40);
        }

        pan3.add(scr2, java.awt.BorderLayout.CENTER);

        center.add(pan3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        filler1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.add(filler1);

        lab1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lab1.setText("___");
        lab1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lab1.setMaximumSize(new java.awt.Dimension(100, 14));
        lab1.setPreferredSize(new java.awt.Dimension(100, 14));
        lab1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        south.add(lab1);

        filler2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.add(filler2);

        lab2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lab2.setText("___");
        lab2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lab2.setMaximumSize(new java.awt.Dimension(60, 14));
        lab2.setPreferredSize(new java.awt.Dimension(60, 14));
        lab2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        south.add(lab2);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1, tab2);
        List.of(qArtikl, qArtdet).forEach(q -> q.execsql());
    }//GEN-LAST:event_windowClosed

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null && node.isLeaf()) {
                TypeArtikl typeArtikl = (TypeArtikl) node.getUserObject();
                UGui.insertRecordEnd(tab1, eArtikl.up, (record) -> {
                    record.setNo(eArtikl.level1, typeArtikl.id1);
                    record.setNo(eArtikl.level2, typeArtikl.id2);
                });
                rsvArtikl.clear();
            }

        } else if (tab2.getBorder() != null) {

            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Record artiklRec = qArtikl.get(index);
                Record artdetRec = eArtdet.up.newRecord(Query.INS);
                artdetRec.setNo(eArtdet.id, Conn.genId(eArtdet.up));
                artdetRec.setNo(eArtdet.artikl_id, artiklRec.get(eArtikl.id));
                qArtdet.add(artdetRec);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                UGui.scrollRectToRow(tab2.getSelectedRow() - 1, tab2);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(this, tab2) == 0) {
                UGui.deleteRecord(tab1);
            }
        } else if (tab2.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0) {
                UGui.deleteRecord(tab2);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        loadingData();
        selectionTree();
    }//GEN-LAST:event_btnRefresh

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        HtmlOfTable.load("Список артикулов", tab1);
        ExecuteCmd.documentType(this);
    }//GEN-LAST:event_btnReport

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btn7(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7
        JButton btn = (JButton) evt.getSource();
        ListenerRecord listener = (btn == btn7 || btn == btn9 || btn == btn26) ? listenerCurrenc1 : listenerCurrenc2;
        Currenc frame = new Currenc(this, listener);
    }//GEN-LAST:event_btn7

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        UGui.updateBorderAndSql((JTable) evt.getSource(), List.of(tab1, tab2));
        filterTable.mousePressed((JTable) evt.getSource());
    }//GEN-LAST:event_mousePressed

    private void btn11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn11
        DicArtikl artikl = new DicArtikl(this, listenerAnalog, 1);
    }//GEN-LAST:event_btn11

    private void btn8(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            int id = qArtikl.getAs(index, eArtikl.series_id);
            new DicGroups(this, listenerSeriesFilter, TypeGroups.SERI_PROF, id);
        }
    }//GEN-LAST:event_btn8

    private void checkBox1Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox1Action
        int index = UGui.getIndexRec(tab1);
        Record artiklRec = qArtikl.get(index);
        int with_seal = (checkBox1.isSelected()) ? 1 : 0;
        artiklRec.set(eArtikl.with_seal, with_seal);
    }//GEN-LAST:event_checkBox1Action

    private void btn5(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5
        int index = UGui.getIndexRec(tab1);
        Record artiklRec = qArtikl.get(index);
        if (artiklRec.getInt(eArtikl.level1) == 1) {
            new DicEnums(this, listenerUnit, UseUnit.METR);

        } else if (artiklRec.getInt(eArtikl.level1) == 2) {
            if (artiklRec.getInt(eArtikl.level2) == 4) {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.ML, UseUnit.GRAM, UseUnit.KG, UseUnit.LITER, UseUnit.DOSE);
            } else {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.KIT, UseUnit.PAIR);
            }
        } else if (artiklRec.getInt(eArtikl.level1) == 3) {
            new DicEnums(this, listenerUnit, UseUnit.METR);

        } else if (artiklRec.getInt(eArtikl.level1) == 4) {
            if (artiklRec.getInt(eArtikl.level2) == 1) {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.KIT, UseUnit.PAIR, UseUnit.MONTH);
            } else {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.KIT, UseUnit.MONTH);
            }
        } else if (artiklRec.getInt(eArtikl.level1) == 5) {
            new DicEnums(this, listenerUnit, UseUnit.METR2);
        }
    }//GEN-LAST:event_btn5

    private void btn18(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn18
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record artiklRec = qArtikl.get(index);
            new Syssize(this, listenerSyssize, artiklRec.getInt(eArtikl.syssize_id));
        } else {
            new Syssize(this, listenerSyssize, -1);
        }
    }//GEN-LAST:event_btn18

    private void btn19(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn19
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            int id = qArtikl.getAs(index, eArtikl.artgrp1_id);
            new DicGroups(this, listenerArtincr, TypeGroups.PRICE_INC, id);
        }
    }//GEN-LAST:event_btn19

    private void btn20(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn20
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            int id = qArtikl.getAs(index, eArtikl.artgrp1_id);
            new DicGroups(this, listenerArtdecr, TypeGroups.PRICE_DEC, id);
        }
    }//GEN-LAST:event_btn20

    private void btn13(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn13
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            int id = qArtikl.getAs(index, eArtikl.series_id);
            new DicGroups(this, listenerCategFilter, TypeGroups.CATEG_PRF, id);
        }
    }//GEN-LAST:event_btn13

    private void btn22(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn22
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            int id = qArtikl.getAs(index, eArtikl.artgrp3_id);
            new DicGroups(this, listenerCateg, TypeGroups.CATEG_PRF, id);
        }
    }//GEN-LAST:event_btn22

    private void btnMove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record artiklRec = qArtikl.get(index);
            List list = new LinkedList();
            for (TypeArtikl typeArt : TypeArtikl.values()) {
                String str = (typeArt.id2 == 0) ? typeArt.name + ":" : "      " + typeArt.name;
                list.add(str);
            }
            Object result = JOptionPane.showInputDialog(Artikles.this, artiklRec.getStr(eArtikl.name),
                    "Изменение типа артикула", JOptionPane.QUESTION_MESSAGE, null, list.toArray(), list.toArray()[0]);

            if (result != null) {
                for (TypeArtikl enam : TypeArtikl.values()) {
                    if (enam instanceof TypeArtikl && enam.name.equals(result.toString().trim())) {
                        artiklRec.setNo(eArtikl.level1, enam.id1);
                        artiklRec.setNo(eArtikl.level2, enam.id2);
                        ((DefTableModel) tab1.getModel()).getQuery().update(artiklRec);
                        selectionTree();
                        UGui.setSelectedIndex(tab1, index);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnMove

    private void btn34(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn34

    }//GEN-LAST:event_btn34

    private void btn37(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn37
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            int id = qArtikl.getAs(index, eArtikl.series_id);
            new DicGroups(this, listenerSeries, TypeGroups.SERI_PROF, id);
        }
    }//GEN-LAST:event_btn37

    private void btnTest(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTest

    private void itReport1ppmCategAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itReport1ppmCategAction
        HtmlOfTable.load("Артикулы", qArtikl, eArtikl.values());
        ExecuteCmd.documentType(this); 
    }//GEN-LAST:event_itReport1ppmCategAction

    private void itReport2ppmCategAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itReport2ppmCategAction
    }//GEN-LAST:event_itReport2ppmCategAction

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn16;
    private javax.swing.JButton btn17;
    private javax.swing.JButton btn18;
    private javax.swing.JButton btn19;
    private javax.swing.JButton btn20;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn24;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn26;
    private javax.swing.JButton btn27;
    private javax.swing.JButton btn28;
    private javax.swing.JButton btn29;
    private javax.swing.JButton btn30;
    private javax.swing.JButton btn31;
    private javax.swing.JButton btn32;
    private javax.swing.JButton btn33;
    private javax.swing.JButton btn34;
    private javax.swing.JButton btn35;
    private javax.swing.JButton btn36;
    private javax.swing.JButton btn37;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnMove;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnTest;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox checkBox1;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler14;
    private javax.swing.Box.Filler filler15;
    private javax.swing.Box.Filler filler16;
    private javax.swing.Box.Filler filler17;
    private javax.swing.Box.Filler filler18;
    private javax.swing.Box.Filler filler19;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler20;
    private javax.swing.Box.Filler filler21;
    private javax.swing.Box.Filler filler22;
    private javax.swing.Box.Filler filler23;
    private javax.swing.Box.Filler filler24;
    private javax.swing.Box.Filler filler25;
    private javax.swing.Box.Filler filler26;
    private javax.swing.Box.Filler filler27;
    private javax.swing.Box.Filler filler28;
    private javax.swing.Box.Filler filler29;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler30;
    private javax.swing.Box.Filler filler31;
    private javax.swing.Box.Filler filler32;
    private javax.swing.Box.Filler filler33;
    private javax.swing.Box.Filler filler34;
    private javax.swing.Box.Filler filler35;
    private javax.swing.Box.Filler filler36;
    private javax.swing.Box.Filler filler37;
    private javax.swing.Box.Filler filler38;
    private javax.swing.Box.Filler filler39;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler40;
    private javax.swing.Box.Filler filler41;
    private javax.swing.Box.Filler filler42;
    private javax.swing.Box.Filler filler43;
    private javax.swing.Box.Filler filler44;
    private javax.swing.Box.Filler filler45;
    private javax.swing.Box.Filler filler46;
    private javax.swing.Box.Filler filler47;
    private javax.swing.Box.Filler filler48;
    private javax.swing.Box.Filler filler49;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JMenuItem itReport1;
    private javax.swing.JMenuItem itReport2;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab13;
    private javax.swing.JLabel lab14;
    private javax.swing.JLabel lab15;
    private javax.swing.JLabel lab16;
    private javax.swing.JLabel lab18;
    private javax.swing.JLabel lab19;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab20;
    private javax.swing.JLabel lab21;
    private javax.swing.JLabel lab22;
    private javax.swing.JLabel lab23;
    private javax.swing.JLabel lab24;
    private javax.swing.JLabel lab26;
    private javax.swing.JLabel lab27;
    private javax.swing.JLabel lab28;
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
    private javax.swing.JLabel lab43;
    private javax.swing.JLabel lab44;
    private javax.swing.JLabel lab45;
    private javax.swing.JLabel lab47;
    private javax.swing.JLabel lab48;
    private javax.swing.JLabel lab49;
    private javax.swing.JLabel lab50;
    private javax.swing.JLabel lab51;
    private javax.swing.JLabel lab52;
    private javax.swing.JLabel lab54;
    private javax.swing.JLabel lab55;
    private javax.swing.JLabel lab56;
    private javax.swing.JLabel labl17;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan100;
    private javax.swing.JPanel pan101;
    private javax.swing.JPanel pan102;
    private javax.swing.JPanel pan103;
    private javax.swing.JPanel pan104;
    private javax.swing.JPanel pan105;
    private javax.swing.JPanel pan106;
    private javax.swing.JPanel pan107;
    private javax.swing.JPanel pan108;
    private javax.swing.JPanel pan109;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan12;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan17;
    private javax.swing.JPanel pan18;
    private javax.swing.JPanel pan19;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan20;
    private javax.swing.JPanel pan21;
    private javax.swing.JPanel pan22;
    private javax.swing.JPanel pan24;
    private javax.swing.JPanel pan25;
    private javax.swing.JPanel pan26;
    private javax.swing.JPanel pan27;
    private javax.swing.JPanel pan28;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan91;
    private javax.swing.JPanel pan92;
    private javax.swing.JPanel pan93;
    private javax.swing.JPanel pan94;
    private javax.swing.JPanel pan95;
    private javax.swing.JPanel pan96;
    private javax.swing.JPanel pan97;
    private javax.swing.JPanel pan98;
    private javax.swing.JPanel pan99;
    private javax.swing.JPopupMenu ppmReport;
    private javax.swing.JPanel pqn109;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scrTree;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    public javax.swing.JTree tree;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt10;
    private javax.swing.JTextField txt11;
    private javax.swing.JTextField txt12;
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
    private javax.swing.JTextField txt37;
    private javax.swing.JTextField txt38;
    private javax.swing.JTextField txt39;
    private javax.swing.JTextField txt4;
    private javax.swing.JTextField txt40;
    private javax.swing.JTextField txt41;
    private javax.swing.JTextField txt42;
    private javax.swing.JTextField txt45;
    private javax.swing.JTextField txt46;
    private javax.swing.JTextField txt48;
    private javax.swing.JTextField txt5;
    private javax.swing.JTextField txt6;
    private javax.swing.JTextField txt7;
    private javax.swing.JTextField txt8;
    private javax.swing.JTextField txt9;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    public void initElements() {

        new FrameToFile(this, btnClose);
        filterTable = new FilterTable(0, tab1);
        south.add(filterTable, 0);        
        filterTable.getTxt().grabFocus();   
        
        List.of(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> UGui.stopCellEditing(tab1, tab2)));
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b038.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        scrTree.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Типы артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Свойства артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Текстура артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
        tree.getSelectionModel().addTreeSelectionListener(tse -> selectionTree());
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
    }
}
