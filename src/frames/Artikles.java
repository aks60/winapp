package frames;

import frames.dialog.DicColor2;
import common.DialogListener;
import common.FrameToFile;
import dataset.ConnApp;
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
import frames.swing.BooleanRenderer;
import java.awt.CardLayout;
import java.awt.Window;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * Материальные ценности
 */
public class Artikles extends javax.swing.JFrame {

    private Query qColgrp = new Query(eGroups.values());
    private Query qColor = new Query(eColor.values()).select(eColor.up);
    private Query qGroups = new Query(eGroups.values());
    private Query qCurrenc = new Query(eCurrenc.values()).select(eCurrenc.up);
    private Query qArtikl = new Query(eArtikl.values());
    private Query qArtikl2 = new Query(eArtikl.values());
    private Query qArtdet = new Query(eArtdet.values());
    private Query qSyssize = new Query(eSyssize.values());
    private Query qArtgrp = new Query(eGroups.values());
    private DefFieldEditor rsvArtikl;
    DefaultMutableTreeNode nodeRoot = null;
    private Window owner = null;
    private DialogListener listenerSeries, listenerCateg, listenerColor, listenerUnit, listenerCurrenc1,
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

    private void loadingData() {
        qSyssize.select(eSyssize.up, "order by", eSyssize.name);
        qGroups.select(eGroups.up, "where", eGroups.grup, "=" + TypeGroups.SERI_PROF.id, "order by", eGroups.name);
        qArtgrp.select(eGroups.up, "where", eGroups.grup, "=" + TypeGroups.FILTER.id, "order by", eGroups.name);
        qColgrp.select(eGroups.up, "where", eGroups.grup, "=" + TypeGroups.COLOR.id, "order by", eGroups.name);
    }

    private void loadingModel() {

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
                    Record groupRec = qArtgrp.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp3_id))).findFirst().orElse(eGroups.up.newRecord());
                    return groupRec.get(eGroups.name);
                }
                return val;
            }
        };
        DefTableModel rsmArtdet = new DefTableModel(tab2, qArtdet, eArtdet.color_fk, eArtdet.color_fk, eArtdet.mark_c1, eArtdet.cost_c1,
                eArtdet.mark_c2, eArtdet.cost_c2, eArtdet.mark_c3, eArtdet.cost_c3, eArtdet.cost_c4, eArtdet.cost_unit, eArtdet.price_coeff, eArtdet.id) {

            @Override
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eArtdet.color_fk && val != null) {
                    Integer color_fk = Integer.valueOf(val.toString());

                    if (color_fk >= 0) {
                        Record colorRec = qColor.stream().filter(rec -> rec.getInt(eColor.id) == color_fk).findFirst().orElse(null);
                        if (col == 0) {
                            Record colgrpRec = qColgrp.stream().filter(rec -> rec.getInt(eGroups.id) == colorRec.getInt(eColor.colgrp_id)).findFirst().orElse(eGroups.up.newRecord());
                            return colgrpRec.getStr(eGroups.name);
                        } else {
                            return colorRec.getStr(eColor.name);
                        }

                    } else if (color_fk < 0) {
                        if (col == 0) {
                            Record colgrpRec = qColgrp.stream().filter(rec -> rec.getInt(eGroups.id) == Math.abs(color_fk)).findFirst().orElse(eGroups.up.newRecord());
                            return colgrpRec.getStr(eGroups.name);
                        } else {
                            return "Все текстуры группы";
                        }
                    }
                }
                return val;
            }
        };

        tab2.getColumnModel().getColumn(2).setCellRenderer(new BooleanRenderer());
        tab2.getColumnModel().getColumn(4).setCellRenderer(new BooleanRenderer());
        tab2.getColumnModel().getColumn(6).setCellRenderer(new BooleanRenderer());

        rsvArtikl = new DefFieldEditor(tab1) {
            @Override
            public void load(Integer row) {
                super.load(row);
                Record artiklRec = qArtikl.get(Util.getIndexRec(tab1));
                Record seriesRec = qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == artiklRec.getInt(eArtikl.series_id)).findFirst().orElse(eGroups.up.newRecord());
                Record currenc1Rec = qCurrenc.stream().filter(rec -> rec.get(eCurrenc.id).equals(artiklRec.get(eArtikl.currenc1_id))).findFirst().orElse(eCurrenc.up.newRecord());
                Record currenc2Rec = qCurrenc.stream().filter(rec -> rec.get(eCurrenc.id).equals(artiklRec.get(eArtikl.currenc2_id))).findFirst().orElse(eCurrenc.up.newRecord());
                Record artgrp1Rec = qArtgrp.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp1_id))).findFirst().orElse(eGroups.up.newRecord());
                Record artgrp2Rec = qArtgrp.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp2_id))).findFirst().orElse(eGroups.up.newRecord());
                Record artgrp3Rec = qArtgrp.stream().filter(rec -> rec.get(eGroups.id).equals(artiklRec.get(eArtikl.artgrp3_id))).findFirst().orElse(eGroups.up.newRecord());
                Record syssizeRec = qSyssize.stream().filter(rec -> rec.getInt(eSyssize.id) == artiklRec.getInt(eArtikl.syssize_id)).findFirst().orElse(eSyssize.up.newRecord());;
                String name = UseUnit.getName(artiklRec.getInt(eArtikl.unit));
                txtField5.setText(name);
                name = (currenc1Rec != null) ? currenc1Rec.getStr(eCurrenc.name) : null;
                txtField7.setText(name);
                name = (currenc2Rec != null) ? currenc2Rec.getStr(eCurrenc.name) : null;
                txtField17.setText(name);
                //name = (groupsRec != null) ? groupsRec.getStr(eGroups.name) : null;
                //txtField10.setText(name);

                if (artiklRec.getInt(eArtikl.analog_id) != -1) {
                    Record analogRec = qArtikl.stream().filter(rec -> rec.get(eArtikl.id).equals(artiklRec.get(eArtikl.analog_id))).findFirst().orElse(null);
                    name = (analogRec != null) ? analogRec.getStr(eArtikl.code) : null;
                    txtField11.setText(name);
                } else {
                    txtField11.setText(null);
                }
                txtField10.setText(seriesRec.getStr(eGroups.name));
                txtField18.setText(syssizeRec.getStr(eSyssize.name));
                txtField19.setText(artgrp1Rec.getStr(eGroups.val));
                txtField20.setText(artgrp2Rec.getStr(eGroups.val));
                txtField22.setText(artgrp3Rec.getStr(eGroups.name));
                //rsvArtikl.add(eArtikl.price_coeff, txtField19);

            }
        };
        rsvArtikl.add(eArtikl.len_unit, txtField1);
        rsvArtikl.add(eArtikl.height, txtField2);
        rsvArtikl.add(eArtikl.depth, txtField3);
        rsvArtikl.add(eArtikl.depth, txtField4);
        rsvArtikl.add(eArtikl.size_centr, txtField8);
        rsvArtikl.add(eArtikl.size_furn, txtField9);
        rsvArtikl.add(eArtikl.min_rad, txtField12);
        rsvArtikl.add(eArtikl.id, txtField13);
        rsvArtikl.add(eArtikl.tech_code, txtField14);
        rsvArtikl.add(eArtikl.size_falz, txtField15);
        rsvArtikl.add(eArtikl.size_tech, txtField16);
        rsvArtikl.add(eArtikl.size_frez, txtField21);

        Util.buttonCellEditor(tab2, 0).addActionListener(event -> {
            DicColor2 frame = new DicColor2(this, listenerColor);
        });

        Util.buttonCellEditor(tab2, 1).addActionListener(event -> {
            DicColor2 frame = new DicColor2(this, listenerColor);
        });
    }

    public void listenerSet() {

        listenerSeries = (record) -> {
            int rowQuery = Util.getIndexRec(tab1);
            if (rowQuery != -1) {
                Record artiklRec = qArtikl.get(rowQuery);
                artiklRec.set(eArtikl.series_id, record.get(eGroups.id));
                rsvArtikl.load();
            }
            Util.stopCellEditing(tab1, tab2);
        };

        listenerSeriesFilter = (record) -> {
            tab1.setColumnSelectionInterval(4, 4);
            labFilter.setText("Серия профилей");
            txtFilter.setName("tab1");
            txtFilter.setText(record.getStr(eGroups.name));
        };

        listenerCategFilter = (record) -> {
            tab1.setColumnSelectionInterval(5, 5);
            labFilter.setText("Категоря профилей");
            txtFilter.setName("tab1");
            txtFilter.setText(record.getStr(eGroups.name));
        };

        listenerAnalog = (record) -> {
            int row = Util.getIndexRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                artiklRec.set(eArtikl.analog_id, record.get(eArtikl.id));
                rsvArtikl.load();
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerColor = (record) -> {
            if (tab2.getBorder() != null) {
                if (eGroups.values().length == record.size()) {
                    qArtdet.set(-1 * record.getInt(eGroups.id), Util.getIndexRec(tab2), eArtdet.color_fk);

                } else if (eColor.values().length == record.size()) {
                    qArtdet.set(record.getInt(eColor.id), Util.getIndexRec(tab2), eArtdet.color_fk);
                }
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerUnit = (record) -> {
            Util.listenerEnums(record, tab1, eArtikl.unit, tab1, tab2);
        };

        listenerCurrenc1 = (record) -> {
            if (tab1.getBorder() != null) {
                int row = Util.getIndexRec(tab1);
                if (row != -1) {
                    Record artiklRec = qArtikl.get(row);
                    artiklRec.set(eArtikl.currenc1_id, record.get(eCurrenc.id));
                    rsvArtikl.load();
                }
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerCurrenc2 = (record) -> {
            if (tab1.getBorder() != null) {
                int row = Util.getIndexRec(tab1);
                if (row != -1) {
                    Record artiklRec = qArtikl.get(row);
                    artiklRec.set(eArtikl.currenc2_id, record.get(eCurrenc.id));
                    rsvArtikl.load();
                }
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerSyssize = (record) -> {
            int row = Util.getIndexRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                artiklRec.set(eArtikl.syssize_id, record.get(eSyssize.id));
                if (artiklRec.getInt(eArtikl.size_falz) == -1) {
                    artiklRec.set(eArtikl.size_falz, record.get(eSyssize.falz));
                }
                rsvArtikl.load();
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerArtincr = (record) -> {
            int row = Util.getIndexRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                artiklRec.set(eArtikl.artgrp1_id, record.get(eGroups.id));
                rsvArtikl.load();
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerArtdecr = (record) -> {
            int row = Util.getIndexRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                artiklRec.set(eArtikl.artgrp2_id, record.get(eGroups.id));
                rsvArtikl.load();
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerCateg = (record) -> {
            int row = Util.getIndexRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                artiklRec.set(eArtikl.artgrp3_id, record.get(eGroups.id));
                rsvArtikl.load();
                Util.stopCellEditing(tab1, tab2);
            }
        };
    }

    private void loadingTree() {

        nodeRoot = new DefaultMutableTreeNode(TypeArtikl.ROOT);
        DefaultMutableTreeNode node = null;
        for (TypeArtikl it : TypeArtikl.values()) {
            if (it.id1 == 1 && it.id2 == 0) {
                node = new DefaultMutableTreeNode(TypeArtikl.PROFIL); //"Профили"

            } else if (it.id1 == 2 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.ACSESYAR); //"Аксессуары"

            } else if (it.id1 == 3 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.POGONAG); //"Погонаж"

            } else if (it.id1 == 4 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.INSTRYMENT); //"Инструмент"

            } else if (it.id1 == 5 && it.id2 == 0) {
                nodeRoot.add(node);
                node = new DefaultMutableTreeNode(TypeArtikl.ZAPOLNEN); //"Заполнения"

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

    private void selectionTree() {

        Arrays.asList(qArtikl, qArtdet).forEach(q -> q.execsql());
        Util.clearTable(tab1, tab2);
        Util.stopCellEditing(tab1, tab2);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        TypeArtikl e = (TypeArtikl) node.getUserObject();
        
        String name = (e.id1 > 4) ? "pan8" : (e.id1 > 1) ? "pan7" : "pan2";
        ((CardLayout) pan6.getLayout()).show(pan6, name);

        if (e == TypeArtikl.ROOT) {
            qArtikl2.select(eArtikl.up, "order by", eArtikl.level1, ",", eArtikl.code);
        } else if (node.isLeaf()) {
            qArtikl2.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1 + "and", eArtikl.level2, "=", e.id2, "order by", eArtikl.level1, ",", eArtikl.code);
        } else {
            qArtikl2.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1, "order by", eArtikl.level1, ",", eArtikl.code);
        }
        qArtikl.clear();
        qArtikl.addAll(qArtikl2);
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }

    private void selectionTab1(ListSelectionEvent event) {

        int index = Util.getIndexRec(tab1);
        if (index != -1) {
            Record record = qArtikl.get(index);
            
            String name = (record.getInt(eArtikl.level1) > 4) ? "pan8" : (record.getInt(eArtikl.level1) > 1) ? "pan7" : "pan2";
            ((CardLayout) pan6.getLayout()).show(pan6, name);
            
            int id = record.getInt(eArtikl.id);
            qArtdet.select(eArtdet.up, "where", eArtdet.artikl_id, "=", id);
            rsvArtikl.load();
            checkBox1.setSelected((record.getInt(eArtikl.with_seal) != 0));
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
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

        for (int index = 0; index < qArtikl2.size(); ++index) {
            int id = qArtikl2.getAs(index, eArtikl.id);
            if (id == artiklRec.getInt(eArtikl.id)) {
                Util.setSelectedRow(tab1, index);
                Util.scrollRectToVisible(index, tab1);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnMove = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        scrTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        pan5 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan6 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtField5 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtField1 = new javax.swing.JFormattedTextField();
        txtField2 = new javax.swing.JFormattedTextField();
        txtField3 = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        txtField4 = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtField7 = new javax.swing.JTextField();
        txtField8 = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        txtField9 = new javax.swing.JFormattedTextField();
        btnField7 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtField10 = new javax.swing.JTextField();
        btnField10 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtField11 = new javax.swing.JFormattedTextField();
        btnField11 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtField12 = new javax.swing.JFormattedTextField();
        txtField13 = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtField14 = new javax.swing.JFormattedTextField();
        checkBox1 = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtField15 = new javax.swing.JFormattedTextField();
        txtField16 = new javax.swing.JFormattedTextField();
        btnField5 = new javax.swing.JButton();
        txtField17 = new javax.swing.JTextField();
        btnField17 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtField18 = new javax.swing.JFormattedTextField();
        btnField18 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        txtField19 = new javax.swing.JFormattedTextField();
        btnField19 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        btnField20 = new javax.swing.JButton();
        txtField20 = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtField21 = new javax.swing.JFormattedTextField();
        btnField8 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        txtField22 = new javax.swing.JTextField();
        btnField13 = new javax.swing.JButton();
        btnField22 = new javax.swing.JButton();
        pan7 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        txtField24 = new javax.swing.JTextField();
        btnField9 = new javax.swing.JButton();
        txtField25 = new javax.swing.JTextField();
        btnField21 = new javax.swing.JButton();
        pan8 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        txtField23 = new javax.swing.JFormattedTextField();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(80, 0), new java.awt.Dimension(80, 0), new java.awt.Dimension(80, 32767));

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

        btnMove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c050.gif"))); // NOI18N
        btnMove.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnMove.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnMove.setFocusable(false);
        btnMove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMove.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMove.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMove.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove(evt);
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
                .addGap(49, 49, 49)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 659, Short.MAX_VALUE)
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
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setPreferredSize(new java.awt.Dimension(900, 550));
        center.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(200, 500));
        pan4.setLayout(new java.awt.BorderLayout());

        scrTree.setBorder(null);
        scrTree.setPreferredSize(new java.awt.Dimension(200, 400));

        tree.setFont(frames.Util.getFont(0,0));
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

        pan5.setPreferredSize(new java.awt.Dimension(340, 500));
        pan5.setLayout(new java.awt.BorderLayout());

        tab1.setFont(frames.Util.getFont(0,0));
        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "111", null, null, null, null, null},
                {"2", "222", null, null, null, null, null}
            },
            new String [] {
                "Актикул", "Наименование", "Отход %", "Коэф. ценовой", "id", "id", "ID"
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
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(1).setPreferredWidth(204);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(32);
            tab1.getColumnModel().getColumn(2).setMaxWidth(120);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(32);
            tab1.getColumnModel().getColumn(3).setMaxWidth(120);
            tab1.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        pan5.add(scr1, java.awt.BorderLayout.CENTER);

        center.add(pan5, java.awt.BorderLayout.CENTER);

        pan3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan3.setPreferredSize(new java.awt.Dimension(800, 130));
        pan3.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setFont(frames.Util.getFont(0,0));
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
                tabMousePressed(evt);
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

        pan6.setPreferredSize(new java.awt.Dimension(340, 500));
        pan6.setLayout(new java.awt.CardLayout());

        pan2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan2.setName(""); // NOI18N

        jLabel13.setFont(frames.Util.getFont(0,0));
        jLabel13.setText("Длина");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel13.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel13.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel13.setPreferredSize(new java.awt.Dimension(48, 18));

        jLabel14.setFont(frames.Util.getFont(0,0));
        jLabel14.setText("Ширина");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel14.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel14.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(48, 18));

        txtField5.setFont(frames.Util.getFont(0,0));
        txtField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField5.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel15.setFont(frames.Util.getFont(0,0));
        jLabel15.setText("Толщина");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel15.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel15.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel15.setPreferredSize(new java.awt.Dimension(54, 18));

        txtField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField1.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField2.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField3.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel16.setFont(frames.Util.getFont(0,0));
        jLabel16.setText("Уд.вес кг/ед");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel16.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel16.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel16.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField4.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel17.setFont(frames.Util.getFont(0,0));
        jLabel17.setText("Ед.измерения");
        jLabel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel17.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel17.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel17.setPreferredSize(new java.awt.Dimension(98, 18));

        jLabel19.setFont(frames.Util.getFont(0,0));
        jLabel19.setText("Валюта");
        jLabel19.setToolTipText("");
        jLabel19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel19.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel19.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel19.setPreferredSize(new java.awt.Dimension(92, 18));

        txtField7.setFont(frames.Util.getFont(0,0));
        txtField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField7.setFocusable(false);
        txtField7.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField8.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel21.setFont(frames.Util.getFont(0,0));
        jLabel21.setText("Фурн. паз (F)");
        jLabel21.setToolTipText("");
        jLabel21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel21.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel21.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel21.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField9.setPreferredSize(new java.awt.Dimension(40, 18));

        btnField7.setText("...");
        btnField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField7.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField7.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField7.setName("btnField7"); // NOI18N
        btnField7.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCurrenc(evt);
            }
        });

        jLabel22.setFont(frames.Util.getFont(0,0));
        jLabel22.setText("Серия профилей");
        jLabel22.setToolTipText("");
        jLabel22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel22.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel22.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel22.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField10.setFocusable(false);
        txtField10.setPreferredSize(new java.awt.Dimension(164, 18));
        txtField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtField10ActionPerformed(evt);
            }
        });

        btnField10.setText("...");
        btnField10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField10.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField10.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField10.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField10(evt);
            }
        });

        jLabel23.setFont(frames.Util.getFont(0,0));
        jLabel23.setText("Аналог профиля");
        jLabel23.setToolTipText("");
        jLabel23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel23.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel23.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel23.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField11.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField11.setFocusable(false);
        txtField11.setPreferredSize(new java.awt.Dimension(164, 18));

        btnField11.setText("...");
        btnField11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField11.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField11.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField11.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField11(evt);
            }
        });

        jLabel24.setFont(frames.Util.getFont(0,0));
        jLabel24.setText("Мин. радиус гиба");
        jLabel24.setToolTipText("");
        jLabel24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel24.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel24.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel24.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField12.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField13.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField13.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel25.setFont(frames.Util.getFont(0,0));
        jLabel25.setText("ID");
        jLabel25.setToolTipText("");
        jLabel25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel25.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel25.setMinimumSize(new java.awt.Dimension(34, 14));

        jLabel26.setFont(frames.Util.getFont(0,0));
        jLabel26.setText("Технолог- ий код");
        jLabel26.setToolTipText("");
        jLabel26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel26.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel26.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel26.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField14.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField14.setFocusable(false);
        txtField14.setPreferredSize(new java.awt.Dimension(164, 18));

        checkBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        checkBox1.setMaximumSize(new java.awt.Dimension(20, 20));
        checkBox1.setMinimumSize(new java.awt.Dimension(20, 20));
        checkBox1.setPreferredSize(new java.awt.Dimension(20, 20));
        checkBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox1Action(evt);
            }
        });

        jLabel27.setFont(frames.Util.getFont(0,0));
        jLabel27.setText("С уплотнением");
        jLabel27.setToolTipText("");
        jLabel27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel27.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel27.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel27.setPreferredSize(new java.awt.Dimension(98, 18));

        jLabel28.setFont(frames.Util.getFont(0,0));
        jLabel28.setText("Наплав, полка(N)");
        jLabel28.setToolTipText("");
        jLabel28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel28.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel28.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel28.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField15.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField15.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField16.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField16.setPreferredSize(new java.awt.Dimension(40, 18));

        btnField5.setText("...");
        btnField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField5.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField5.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField5.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField5(evt);
            }
        });

        txtField17.setFont(frames.Util.getFont(0,0));
        txtField17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField17.setFocusable(false);
        txtField17.setPreferredSize(new java.awt.Dimension(68, 18));

        btnField17.setText("...");
        btnField17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField17.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField17.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField17.setName("btnField17"); // NOI18N
        btnField17.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCurrenc(evt);
            }
        });

        jLabel29.setFont(frames.Util.getFont(0,0));
        jLabel29.setText("Толщ. наплава(T) ");
        jLabel29.setToolTipText("");
        jLabel29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel29.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel29.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel29.setPreferredSize(new java.awt.Dimension(98, 18));

        jLabel30.setFont(frames.Util.getFont(0,0));
        jLabel30.setText("Система констант");
        jLabel30.setToolTipText("");
        jLabel30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel30.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel30.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel30.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField18.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField18.setPreferredSize(new java.awt.Dimension(164, 18));

        btnField18.setText("...");
        btnField18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField18.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField18.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField18.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField18(evt);
            }
        });

        jLabel31.setFont(frames.Util.getFont(0,0));
        jLabel31.setText("Наценки (коэф)");
        jLabel31.setToolTipText("");
        jLabel31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel31.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel31.setPreferredSize(new java.awt.Dimension(92, 18));

        txtField19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField19.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField19.setPreferredSize(new java.awt.Dimension(40, 18));

        btnField19.setText("...");
        btnField19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField19.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField19.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField19.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField19(evt);
            }
        });

        jLabel32.setFont(frames.Util.getFont(0,0));
        jLabel32.setText("Скидка (%)");
        jLabel32.setToolTipText("");
        jLabel32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel32.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel32.setPreferredSize(new java.awt.Dimension(68, 18));

        btnField20.setText("...");
        btnField20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField20.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField20.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField20.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField20(evt);
            }
        });

        txtField20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField20.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField20.setPreferredSize(new java.awt.Dimension(40, 18));

        jLabel33.setFont(frames.Util.getFont(0,0));
        jLabel33.setText("От края до оси (B)");
        jLabel33.setToolTipText("");
        jLabel33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel33.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel33.setMinimumSize(new java.awt.Dimension(34, 14));

        jLabel20.setFont(frames.Util.getFont(0,0));
        jLabel20.setText("Толщина фрезы");
        jLabel20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel20.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel20.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel20.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField21.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField21.setPreferredSize(new java.awt.Dimension(40, 18));

        btnField8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btnField8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField8.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField8.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField8.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField8(evt);
            }
        });

        jLabel34.setFont(frames.Util.getFont(0,0));
        jLabel34.setText("Фильтр по...");
        jLabel34.setToolTipText("");
        jLabel34.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel34.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel34.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel34.setPreferredSize(new java.awt.Dimension(98, 18));

        txtField22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField22.setFocusable(false);
        txtField22.setPreferredSize(new java.awt.Dimension(164, 18));

        btnField13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btnField13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField13.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField13.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField13.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField13(evt);
            }
        });

        btnField22.setText("...");
        btnField22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField22.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField22.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField22.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField22(evt);
            }
        });

        javax.swing.GroupLayout pan2Layout = new javax.swing.GroupLayout(pan2);
        pan2.setLayout(pan2Layout);
        pan2Layout.setHorizontalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btnField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan2Layout.createSequentialGroup()
                                .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtField18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtField10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                            .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtField20, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtField22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pan2Layout.createSequentialGroup()
                            .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtField14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtField11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan2Layout.setVerticalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnField13, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnField8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(btnField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan2Layout.createSequentialGroup()
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6))))
                            .addGroup(pan2Layout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(txtField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)))
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57))
        );

        pan6.add(pan2, "pan2");

        pan7.setName(""); // NOI18N
        pan7.setPreferredSize(new java.awt.Dimension(14, 14));

        jLabel36.setFont(frames.Util.getFont(0,0));
        jLabel36.setText("Валюта");
        jLabel36.setToolTipText("");
        jLabel36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel36.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel36.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel36.setPreferredSize(new java.awt.Dimension(92, 18));

        txtField24.setFont(frames.Util.getFont(0,0));
        txtField24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField24.setFocusable(false);
        txtField24.setPreferredSize(new java.awt.Dimension(40, 18));

        btnField9.setText("...");
        btnField9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField9.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField9.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField9.setName("btnField7"); // NOI18N
        btnField9.setPreferredSize(new java.awt.Dimension(18, 18));

        txtField25.setFont(frames.Util.getFont(0,0));
        txtField25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField25.setFocusable(false);
        txtField25.setPreferredSize(new java.awt.Dimension(68, 18));

        btnField21.setText("...");
        btnField21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField21.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField21.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField21.setName("btnField17"); // NOI18N
        btnField21.setPreferredSize(new java.awt.Dimension(18, 18));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(391, Short.MAX_VALUE))
        );

        pan6.add(pan7, "pan7");

        pan8.setName(""); // NOI18N
        pan8.setPreferredSize(new java.awt.Dimension(14, 14));

        jLabel35.setFont(frames.Util.getFont(0,0));
        jLabel35.setText("Длина");
        jLabel35.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel35.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLabel35.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel35.setPreferredSize(new java.awt.Dimension(48, 18));

        txtField23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField23.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField23.setPreferredSize(new java.awt.Dimension(40, 18));

        javax.swing.GroupLayout pan8Layout = new javax.swing.GroupLayout(pan8);
        pan8.setLayout(pan8Layout);
        pan8Layout.setHorizontalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );
        pan8Layout.setVerticalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(391, Short.MAX_VALUE))
        );

        pan6.add(pan8, "pan8");

        center.add(pan6, java.awt.BorderLayout.EAST);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
        labFilter.setMaximumSize(new java.awt.Dimension(140, 14));
        labFilter.setMinimumSize(new java.awt.Dimension(140, 14));
        labFilter.setPreferredSize(new java.awt.Dimension(140, 14));
        south.add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(180, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(180, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(180, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFilterCaretUpdate(evt);
            }
        });
        south.add(txtFilter);

        checkFilter.setText("в конце строки");
        south.add(checkFilter);
        south.add(filler1);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2);
        Arrays.asList(qArtikl, qArtdet).forEach(q -> q.execsql());
    }//GEN-LAST:event_windowClosed

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.isLeaf()) {
                TypeArtikl typeArtikl = (TypeArtikl) selectedNode.getUserObject();
                Record artiklRec = eArtikl.up.newRecord(Query.INS);
                artiklRec.setNo(eArtikl.id, ConnApp.instanc().genId(eArtikl.up));
                artiklRec.setNo(eArtikl.level1, typeArtikl.id1);
                artiklRec.setNo(eArtikl.level2, typeArtikl.id2);
                qArtikl.add(artiklRec);
                ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qArtikl, tab1);
                rsvArtikl.clear();
            } else {
                JOptionPane.showMessageDialog(this, "Не выбран элемент артикула");
            }

        } else if (tab2.getBorder() != null) {
            int row = Util.getIndexRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                Record artdetRec = eArtdet.up.newRecord(Query.INS);
                artdetRec.setNo(eArtdet.id, ConnApp.instanc().genId(eArtdet.up));
                artdetRec.setNo(eArtdet.artikl_id, artiklRec.get(eArtikl.id));
                qArtdet.add(artdetRec);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qArtdet, tab2);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab2) == 0) {
                Util.deleteRecord(tab1);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab2);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        loadingData();
        selectionTree();
    }//GEN-LAST:event_btnRefresh

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        System.out.println(tab1.getSelectedColumn());
    }//GEN-LAST:event_btnReport

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnCurrenc(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCurrenc
        JButton btn = (JButton) evt.getSource();
        DialogListener listener = (btn.getName().equals("btnField7")) ? listenerCurrenc1 : listenerCurrenc2;
        Currenc frame = new Currenc(this, listener);
    }//GEN-LAST:event_btnCurrenc

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.updateBorderAndSql(table, Arrays.asList(tab1, tab2));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void btnField10(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField10
        DicGroups groups = new DicGroups(this, listenerSeries, TypeGroups.SERI_PROF);
    }//GEN-LAST:event_btnField10

    private void btnField11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField11
        DicArtikl artikl = new DicArtikl(this, listenerAnalog, 1);
    }//GEN-LAST:event_btnField11

    private void txtFilterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilterCaretUpdate

        JTable table = Stream.of(tab1, tab2).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
        Util.setSelectedRow(table);
    }//GEN-LAST:event_txtFilterCaretUpdate

    private void btnField8(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField8
        DicGroups groups = new DicGroups(this, listenerSeriesFilter, TypeGroups.SERI_PROF);
    }//GEN-LAST:event_btnField8

    private void checkBox1Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox1Action
        int row = Util.getIndexRec(tab1);
        Record artiklRec = qArtikl.get(row);
        int with_seal = (checkBox1.isSelected()) ? 1 : 0;
        artiklRec.set(eArtikl.with_seal, with_seal);
    }//GEN-LAST:event_checkBox1Action

    private void btnField5(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField5
        int row = Util.getIndexRec(tab1);
        Record artiklRec = qArtikl.get(row);
        if (artiklRec.getInt(eArtikl.level1) == 1) {
            new DicEnums(this, listenerUnit, UseUnit.METR);

        } else if (artiklRec.getInt(eArtikl.level1) == 2) {
            if (artiklRec.getInt(eArtikl.level2) == 4) {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.ML, UseUnit.GRAM, UseUnit.KG, UseUnit.LITER, UseUnit.DOSE);
            } else {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.SET, UseUnit.PAIR);
            }
        } else if (artiklRec.getInt(eArtikl.level1) == 3) {
            new DicEnums(this, listenerUnit, UseUnit.METR);

        } else if (artiklRec.getInt(eArtikl.level1) == 4) {
            if (artiklRec.getInt(eArtikl.level2) == 1) {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.SET, UseUnit.PAIR, UseUnit.MONTH);
            } else {
                new DicEnums(this, listenerUnit, UseUnit.PIE, UseUnit.SET, UseUnit.MONTH);
            }
        } else if (artiklRec.getInt(eArtikl.level1) == 5) {
            new DicEnums(this, listenerUnit, UseUnit.METR2);
        }
    }//GEN-LAST:event_btnField5

    private void btnField18(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField18
        new Syssize(this, listenerSyssize);
    }//GEN-LAST:event_btnField18

    private void btnField19(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField19
        new DicGroups(this, listenerArtincr, TypeGroups.PRICE_INC);
    }//GEN-LAST:event_btnField19

    private void btnField20(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField20
        new DicGroups(this, listenerArtdecr, TypeGroups.PRICE_DEC);
    }//GEN-LAST:event_btnField20

    private void btnField13(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField13
        DicGroups groups = new DicGroups(this, listenerCategFilter, TypeGroups.FILTER);
    }//GEN-LAST:event_btnField13

    private void btnField22(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField22
        DicGroups groups = new DicGroups(this, listenerCateg, TypeGroups.FILTER);
    }//GEN-LAST:event_btnField22

    private void txtField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtField10ActionPerformed

    private void btnMove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove
        int row = Util.getIndexRec(tab1);
        if (row != -1) {
            Record artiklRec = qArtikl.get(row);
            List list = new LinkedList();
            for (TypeArtikl typeArt : TypeArtikl.values()) {
                String str = (typeArt.id2 == 0) ? typeArt.name + ":" : "      " + typeArt.name;
                list.add(str);
            }
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif"));
            Object result = JOptionPane.showInputDialog(Artikles.this, artiklRec.getStr(eArtikl.name),
                    "Изменение типа артикла", JOptionPane.QUESTION_MESSAGE, null, list.toArray(), list.toArray()[0]);

            if (result != null) {
                for (TypeArtikl enam : TypeArtikl.values()) {
                    if (enam instanceof TypeArtikl && enam.name.equals(result.toString().trim())) {
                        artiklRec.setNo(eArtikl.level1, enam.id1);
                        artiklRec.setNo(eArtikl.level2, enam.id2);
                        ((DefTableModel) tab1.getModel()).getQuery().update(artiklRec);
                        selectionTree();
                        Util.setSelectedRow(tab1, row);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnMove

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnField10;
    private javax.swing.JButton btnField11;
    private javax.swing.JButton btnField13;
    private javax.swing.JButton btnField17;
    private javax.swing.JButton btnField18;
    private javax.swing.JButton btnField19;
    private javax.swing.JButton btnField20;
    private javax.swing.JButton btnField21;
    private javax.swing.JButton btnField22;
    private javax.swing.JButton btnField5;
    private javax.swing.JButton btnField7;
    private javax.swing.JButton btnField8;
    private javax.swing.JButton btnField9;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnMove;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox checkBox1;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scrTree;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    public javax.swing.JTree tree;
    private javax.swing.JFormattedTextField txtField1;
    private javax.swing.JTextField txtField10;
    private javax.swing.JFormattedTextField txtField11;
    private javax.swing.JFormattedTextField txtField12;
    private javax.swing.JFormattedTextField txtField13;
    private javax.swing.JFormattedTextField txtField14;
    private javax.swing.JFormattedTextField txtField15;
    private javax.swing.JFormattedTextField txtField16;
    private javax.swing.JTextField txtField17;
    private javax.swing.JFormattedTextField txtField18;
    private javax.swing.JFormattedTextField txtField19;
    private javax.swing.JFormattedTextField txtField2;
    private javax.swing.JFormattedTextField txtField20;
    private javax.swing.JFormattedTextField txtField21;
    private javax.swing.JTextField txtField22;
    private javax.swing.JFormattedTextField txtField23;
    private javax.swing.JTextField txtField24;
    private javax.swing.JTextField txtField25;
    private javax.swing.JFormattedTextField txtField3;
    private javax.swing.JFormattedTextField txtField4;
    private javax.swing.JTextField txtField5;
    private javax.swing.JTextField txtField7;
    private javax.swing.JFormattedTextField txtField8;
    private javax.swing.JFormattedTextField txtField9;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {

        new FrameToFile(this, btnClose);
        labFilter.setText(tab1.getColumnName(0));
        txtFilter.setName(tab1.getName());
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1, tab2)));
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        scrTree.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Типы артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Свойства артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Текстура артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        tree.getSelectionModel().addTreeSelectionListener(tse -> selectionTree());
        tab1.getSelectionModel().addListSelectionListener(event -> selectionTab1(event));
        txtField7.setEditable(false);
        txtField7.setBackground(new java.awt.Color(255, 255, 255));
    }
}
