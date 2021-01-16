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
import frames.dialog.DicFurniture;
import frames.dialog.ParDefault;
import domain.eArtikl;
import domain.eFurniture;
import domain.eParams;
import domain.eSysfurn;
import domain.eSyspar1;
import domain.eModels;
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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import frames.swing.BooleanRenderer;
import frames.swing.DefFieldEditor;
import frames.swing.DefTableModel;
import builder.Wincalc;
import frames.swing.Canvas;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import startup.App1;
import startup.Main;

public class Systree extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.values());
    private Query qArtikl = new Query(eArtikl.id, eArtikl.code, eArtikl.name);
    private Query qSystree = new Query(eSystree.values()).select(eSystree.up);
    private Query qSysprod = new Query(eSysprod.values());
    private Query qSysprof = new Query(eSysprof.values(), eArtikl.values());
    private Query qSysfurn = new Query(eSysfurn.values(), eFurniture.values());
    private Query qSyspar1 = new Query(eSyspar1.values());
    public Wincalc iwinMin = new Wincalc();
    private JTable tab1 = new JTable();
    private DialogListener listenerArtikl, listenerUsetyp, listenerProd, listenerModify, listenerTree,
            listenerSide, listenerFurn, listenerTypeopen, listenerHandle, listenerParam1, listenerParam2,
            listenerBtn1, listenerBtn7, listenerBtn11, listenerArt211, listenerArt212;
    private DefMutableTreeNode rootTree = null;
    private DefFieldEditor rsvSystree;
    private Wincalc iwin = new Wincalc();
    private java.awt.Frame frame = null;
    private int nuni = -1;
    private TreeNode[] nuniNode = null;
    private Canvas paintPanel = new Canvas(iwin) {

//        public void actionResponse(MouseEvent evt) {
//            ElemSimple elem = iwin.listElem.stream().filter(el -> el.contains(evt.getX(), evt.getY())).findFirst().orElse(null);
//            if (elem != null) {
//                txtField5.setText(String.valueOf(elem.id()));
//                repaint();
//            }
//        }
    };

    public Systree() {
        initComponents();
        initElements();
        loadingData();
        loadingTree();
        loadingModel();
        listenerAdd();
        listenerSet();
    }

    public Systree(int artiklID) {
        initComponents();
        initElements();
        loadingData();
        loadingTree();
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
        qParams.select(eParams.up, "where", eParams.id, "< 0").table(eParams.up);
        qArtikl.select(eArtikl.up, "where", eArtikl.level1, "= 2 and", eArtikl.level2, "in (11,12)");

        ((DefaultTreeCellEditor) tree.getCellEditor()).addCellEditorListener(new CellEditorListener() {

            public void editingStopped(ChangeEvent e) {
                DefMutableTreeNode node = (DefMutableTreeNode) tree.getLastSelectedPathComponent();
                String str = ((DefaultTreeCellEditor) tree.getCellEditor()).getCellEditorValue().toString();
                node.record.set(eSystree.name, str);
                node.setUserObject(str);
                txtField8.setText(str);
                qSystree.update(node.record); //сохраним в базе
            }

            public void editingCanceled(ChangeEvent e) {
                editingStopped(e);
            }
        });
    }

    private void loadingTree() {
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
        tree.setModel(new DefaultTreeModel(rootTree));
        scr1.setViewportView(tree);
    }

    private void loadingTab5() {

        qSysprod.select(eSysprod.up, "where", eSysprod.systree_id, "=", nuni);
        DefaultTableModel dtm5 = (DefaultTableModel) tab5.getModel();
        dtm5.getDataVector().removeAllElements();
        int length = 68;
        for (Record record : qSysprod.table(eSysprod.up)) {
            try {
                Object arrayRec[] = {record.get(eSysprod.name), null};
                Object script = record.get(eSysprod.script);
                iwinMin.build(script.toString());
                BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
                iwinMin.gc2d = bi.createGraphics();
                iwinMin.gc2d.fillRect(0, 0, length, length);
                iwinMin.scale = (length / iwinMin.width > length / iwinMin.heightAdd) ? length / (iwinMin.heightAdd + 200) : length / (iwinMin.width + 200);
                iwinMin.gc2d.translate(2, 2);
                iwinMin.gc2d.scale(iwinMin.scale, iwinMin.scale);
                iwinMin.rootArea.draw(length, length);
                ImageIcon image = new ImageIcon(bi);
                arrayRec[1] = image;
                dtm5.addRow(arrayRec);

            } catch (Exception e) {
                System.out.println("Ошибка " + e);
            }
        }
    }

    private void loadingModel() {

        DefTableModel rsmSystree = new DefTableModel(tab1, qSystree, eSystree.values());
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
        rsvSystree = new DefFieldEditor(tab1);
        rsvSystree.add(eSystree.name, txtField8);
        rsvSystree.add(eSystree.types, txtField7, TypeUse.values());
        rsvSystree.add(eSystree.glas, txtField1);
        rsvSystree.add(eSystree.depth, txtField2);
        rsvSystree.add(eSystree.col1, txtField3);
        rsvSystree.add(eSystree.col2, txtField4);
        rsvSystree.add(eSystree.col3, txtField5);
        rsvSystree.add(eSystree.id, txtField6);
        rsvSystree.add(eSystree.pref, txtField10);
        rsvSystree.add(eSystree.imgview, txtField11, LayoutProduct.values());
        rsvSystree.add(eSystree.nuni, txtField12);

        panDesign.add(paintPanel, java.awt.BorderLayout.CENTER);
        paintPanel.setVisible(true);

        if (nuniNode != null) {
            tree.setSelectionPath(new TreePath(nuniNode));
        } else {
            tree.setSelectionRow(0);
        }
    }

    private void listenerAdd() {
        Util.buttonEditorCell(tab2, 0).addActionListener(event -> {
            new DicEnums(this, listenerUsetyp, UseArtiklTo.values());
        });

        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            new DicEnums(this, listenerSide, UseSide.values());
        });

        Util.buttonEditorCell(tab2, 2).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab2, 3).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab3, 1).addActionListener(event -> {
            DicFurniture frame = new DicFurniture(this, listenerFurn);
        });

        Util.buttonEditorCell(tab3, 2).addActionListener(event -> {
            DicEnums frame = new DicEnums(this, listenerTypeopen, TypeOpen2.values());
        });

        Util.buttonEditorCell(tab3, 4).addActionListener(event -> {
            DicEnums frame = new DicEnums(this, listenerHandle, LayoutHandle.values());
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

        listenerUsetyp = (record) -> {
            Util.listenerEnums(record, tab2, eSysprof.use_type, tab1, tab2, tab3, tab4);
        };

        listenerSide = (record) -> {
            Util.listenerEnums(record, tab2, eSysprof.use_side, tab1, tab2, tab3, tab4);
        };

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab2);
            qSysprof.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab2), eSysprof.artikl_id);
            qSysprof.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab2), eArtikl.name);
            qSysprof.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab2), eArtikl.code);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, row);
        };

        listenerProd = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int models_id = record.getInt(0);
            Record record2 = eSysprod.up.newRecord(Query.INS);
            record2.setNo(eSysprod.id, ConnApp.instanc().genId(eSysprod.id));
            record2.setNo(eSysprod.systree_id, nuni);
            record2.setNo(eSysprod.name, record.get(1));
            record2.setNo(eSysprod.script, record.get(2));
            qSysprod.table(eSysprod.up).insert(record2);
            loadingTab5();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qSysprod, tab5);
        };

        listenerFurn = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            qSysfurn.set(record.getInt(eFurniture.id), Util.getSelectedRec(tab3), eSysfurn.furniture_id);
            qSysfurn.table(eFurniture.up).set(record.get(eFurniture.name), Util.getSelectedRec(tab3), eFurniture.name);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerTypeopen = (record) -> {
            Util.listenerEnums(record, tab3, eSysfurn.side_open, tab1, tab2, tab3, tab4, tab5);
        };

        listenerHandle = (record) -> {
            Util.listenerEnums(record, tab3, eSysfurn.hand_pos, tab1, tab2, tab3, tab4, tab5);
        };

        listenerArt211 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            qSysfurn.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab3), eSysfurn.artikl_id1);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerArt212 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab3);
            qSysfurn.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab3), eSysfurn.artikl_id2);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);
        };

        listenerParam1 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab4);
            qSyspar1.set(record.getInt(eParams.id), Util.getSelectedRec(tab4), eSyspar1.params_id);
            qSyspar1.set(null, Util.getSelectedRec(tab4), eSyspar1.text);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, row);
        };

        listenerParam2 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab4);
            qSyspar1.set(record.getStr(eParams.text), Util.getSelectedRec(tab4), eSyspar1.text);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, row);
        };
    }

    private void selectionTree() {
        DefMutableTreeNode node = (DefMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node != null) {
            nuni = node.record.getInt(eSystree.id);
            eProperty.systree_nuni.write(String.valueOf(nuni));
            for (int i = 0; i < qSystree.size(); i++) {
                if (nuni == qSystree.get(i).getInt(eSystree.id)) {
                    rsvSystree.load(i);
                }
            }
            qSysprod.select(eSysprod.up, "where", eSysprod.systree_id, "=", node.record.getInt(eSystree.id), "order by", eSysprod.name);
            qSysprof.select(eSysprof.up, "left join", eArtikl.up, "on", eArtikl.id, "=",
                    eSysprof.artikl_id, "where", eSysprof.systree_id, "=", node.record.getInt(eSystree.id), "order by", eSysprof.use_type, ",", eSysprof.prio);
            qSysfurn.select(eSysfurn.up, "left join", eFurniture.up, "on", eFurniture.id, "=",
                    eSysfurn.furniture_id, "where", eSysfurn.systree_id, "=", node.record.getInt(eSystree.id), "order by", eSysfurn.npp);
            qSyspar1.select(eSyspar1.up, "where", eSyspar1.systree_id, "=", node.record.getInt(eSystree.id));

            loadingTab5();

            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
            Util.setSelectedRow(tab3);
            Util.setSelectedRow(tab4);
            Util.setSelectedRow(tab5);
        } else {
            //createWincalc(-1); //рисуем виртуалку
        }
    }

    private void selectionTab5() {
        int row = Util.getSelectedRec(tab5);
        if (row != -1) {
            Record record = qSysprod.table(eSysprod.up).get(row);
            String script = record.getStr(eSysprod.script);
            createWincalc(script); //калькуляция и прорисовка окна
        }
    }

    private ArrayList<DefMutableTreeNode> addChild(ArrayList<DefMutableTreeNode> nodeList1, ArrayList<DefMutableTreeNode> nodeList2) {

        Query systreeList = qSystree.table(eSystree.up);
        for (DefMutableTreeNode node : nodeList1) {
            String userNode = (String) node.getUserObject();
            for (Record record2 : systreeList) {
                if (record2.getInt(eSystree.parent_id) == node.record.getInt(eSystree.id)
                        && record2.getInt(eSystree.parent_id) != record2.getInt(eSystree.id)) {
                    DefMutableTreeNode node2 = new DefMutableTreeNode(record2);
                    node.add(node2);
                    nodeList2.add(node2);
                    if (record2.getInt(eSystree.id) == nuni) {
                        nuniNode = node2.getPath(); //запомним path для nuni
                    }
                }
            }
        }
        return nodeList2;
    }

    private void createWincalc(String script) {
        
        if (script != null && script.isEmpty() == false) {
            JsonElement script2 = new Gson().fromJson(script, JsonElement.class);
            script2.getAsJsonObject().addProperty("nuni", nuni); //запишем nuni в script
            iwin.build(script2.toString()); //калькуляция изделия
            paintPanel.repaint(true);

        } else {
            Graphics2D g = (Graphics2D) paintPanel.getGraphics();
            g.clearRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
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
        pan9 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        btnArtikl = new javax.swing.JToggleButton();
        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        pan1 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        panDesign = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtField12 = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        txtField6 = new javax.swing.JFormattedTextField();
        pan8 = new javax.swing.JPanel();
        tabb1 = new javax.swing.JTabbedPane();
        pan10 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        pan11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        pan6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtField1 = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtField7 = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        txtField8 = new javax.swing.JTextField();
        btnField1 = new javax.swing.JButton();
        txtField2 = new javax.swing.JTextField();
        txtField3 = new javax.swing.JTextField();
        txtField4 = new javax.swing.JTextField();
        txtField5 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtField10 = new javax.swing.JTextField();
        txtField11 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        btnField11 = new javax.swing.JButton();
        btnField7 = new javax.swing.JButton();
        pan3 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan4 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Системы профилей");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Systree.this.windowClosed(evt);
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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel22.setFont(frames.Util.getFont(0,0));
        jLabel22.setText("Артикул профиля");
        jLabel22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel22.setPreferredSize(new java.awt.Dimension(160, 18));

        javax.swing.GroupLayout pan9Layout = new javax.swing.GroupLayout(pan9);
        pan9.setLayout(pan9Layout);
        pan9Layout.setHorizontalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, 0, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        pan9Layout.setVerticalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(167, Short.MAX_VALUE))
        );

        btnArtikl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnArtikl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c055.gif"))); // NOI18N
        btnArtikl.setToolTipText("Артикулы в системе...");
        btnArtikl.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArtikl.setMaximumSize(new java.awt.Dimension(25, 25));
        btnArtikl.setMinimumSize(new java.awt.Dimension(25, 25));
        btnArtikl.setPreferredSize(new java.awt.Dimension(25, 25));
        btnArtikl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtikl(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(northLayout.createSequentialGroup()
                        .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnArtikl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, northLayout.createSequentialGroup()
                        .addGap(0, 359, Short.MAX_VALUE)
                        .addComponent(pan9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnArtikl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(260, 564));

        tree.setEditable(true);
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                treeMousePressed(evt);
            }
        });
        scr1.setViewportView(tree);

        centr.add(scr1, java.awt.BorderLayout.WEST);

        pan1.setPreferredSize(new java.awt.Dimension(602, 565));
        pan1.setLayout(new java.awt.GridLayout(2, 1));

        pan2.setPreferredSize(new java.awt.Dimension(692, 200));
        pan2.setLayout(new java.awt.BorderLayout());

        panDesign.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panDesign.setLayout(new java.awt.BorderLayout());
        pan2.add(panDesign, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(300, 200));

        jLabel25.setFont(frames.Util.getFont(0,0));
        jLabel25.setText("NUNI-PS4");
        jLabel25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel25.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField12.setPreferredSize(new java.awt.Dimension(20, 18));

        jLabel26.setFont(frames.Util.getFont(0,0));
        jLabel26.setText("NUNI-PS5");
        jLabel26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel26.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField6.setPreferredSize(new java.awt.Dimension(20, 18));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan7Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan7Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(240, Short.MAX_VALUE))
        );

        pan2.add(pan7, java.awt.BorderLayout.EAST);

        pan8.setPreferredSize(new java.awt.Dimension(8, 208));

        javax.swing.GroupLayout pan8Layout = new javax.swing.GroupLayout(pan8);
        pan8.setLayout(pan8Layout);
        pan8Layout.setHorizontalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        pan8Layout.setVerticalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
        );

        pan2.add(pan8, java.awt.BorderLayout.WEST);

        pan1.add(pan2);

        tabb1.setPreferredSize(new java.awt.Dimension(600, 275));
        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabb1StateChanged(evt);
            }
        });

        pan10.setPreferredSize(new java.awt.Dimension(0, 0));
        pan10.setLayout(new java.awt.GridLayout(1, 2));

        scr5.setBorder(null);
        scr5.setPreferredSize(new java.awt.Dimension(0, 0));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
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

        pan10.add(scr5);

        pan11.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportView(jTree1);

        pan11.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pan10.add(pan11);

        tabb1.addTab("Конструкции системы", pan10);

        pan6.setPreferredSize(new java.awt.Dimension(300, 275));

        jLabel13.setFont(frames.Util.getFont(0,0));
        jLabel13.setText("Зап-ие по умолчанию");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel13.setPreferredSize(new java.awt.Dimension(112, 18));

        txtField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField1.setPreferredSize(new java.awt.Dimension(156, 18));

        jLabel14.setFont(frames.Util.getFont(0,0));
        jLabel14.setText("Доступные толщины");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel14.setPreferredSize(new java.awt.Dimension(120, 18));

        jLabel15.setFont(frames.Util.getFont(0,0));
        jLabel15.setText("Основная текстура");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel15.setPreferredSize(new java.awt.Dimension(112, 18));

        jLabel16.setFont(frames.Util.getFont(0,0));
        jLabel16.setText("Внутр. текстура");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel16.setPreferredSize(new java.awt.Dimension(112, 18));

        jLabel17.setFont(frames.Util.getFont(0,0));
        jLabel17.setText("Внешняя текстура");
        jLabel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel17.setPreferredSize(new java.awt.Dimension(112, 18));

        jLabel19.setFont(frames.Util.getFont(0,0));
        jLabel19.setText("Признак системы");
        jLabel19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel19.setMaximumSize(new java.awt.Dimension(112, 18));
        jLabel19.setMinimumSize(new java.awt.Dimension(112, 18));
        jLabel19.setPreferredSize(new java.awt.Dimension(112, 18));

        txtField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField7.setPreferredSize(new java.awt.Dimension(156, 18));

        jLabel20.setFont(frames.Util.getFont(0,0));
        jLabel20.setText("Система");
        jLabel20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel20.setPreferredSize(new java.awt.Dimension(112, 18));

        txtField8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtField8.setFocusable(false);
        txtField8.setPreferredSize(new java.awt.Dimension(450, 18));

        btnField1.setText("...");
        btnField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField1.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField1.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField1.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField1(evt);
            }
        });

        txtField2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtField2.setPreferredSize(new java.awt.Dimension(134, 18));

        txtField3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtField3.setPreferredSize(new java.awt.Dimension(180, 18));

        txtField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtField4.setPreferredSize(new java.awt.Dimension(180, 18));

        txtField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtField5.setPreferredSize(new java.awt.Dimension(180, 18));

        jLabel23.setFont(frames.Util.getFont(0,0));
        jLabel23.setText("Префикс (замена/код)");
        jLabel23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel23.setPreferredSize(new java.awt.Dimension(120, 18));

        txtField10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtField10.setPreferredSize(new java.awt.Dimension(134, 18));

        txtField11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtField11.setPreferredSize(new java.awt.Dimension(110, 18));

        jLabel24.setFont(frames.Util.getFont(0,0));
        jLabel24.setText("Вид изделия по умолчанию");
        jLabel24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel24.setPreferredSize(new java.awt.Dimension(120, 18));

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

        btnField7.setText("...");
        btnField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnField7.setMaximumSize(new java.awt.Dimension(18, 18));
        btnField7.setMinimumSize(new java.awt.Dimension(18, 18));
        btnField7.setPreferredSize(new java.awt.Dimension(18, 18));
        btnField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnField7(evt);
            }
        });

        javax.swing.GroupLayout pan6Layout = new javax.swing.GroupLayout(pan6);
        pan6.setLayout(pan6Layout);
        pan6Layout.setHorizontalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        pan6Layout.setVerticalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(119, Short.MAX_VALUE))
        );

        tabb1.addTab("Основные", pan6);

        pan3.setPreferredSize(new java.awt.Dimension(0, 0));
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

        tabb1.addTab("Профили", pan3);

        pan4.setPreferredSize(new java.awt.Dimension(0, 0));
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

        tabb1.addTab("Фурнитура", pan4);

        pan5.setPreferredSize(new java.awt.Dimension(0, 0));
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

        tabb1.addTab("Параметры", pan5);

        pan1.add(tabb1);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

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
        Wincalc iwin = new Wincalc();
        int nuni = Integer.valueOf(eProperty.systree_nuni.read());
        Record record = eSystree.find(nuni);
        int models_id = record.getInt(eSystree.models_id);
        Record record2 = eModels.find(models_id);
        String script = record2.getStr(eModels.script);
        JsonElement je = new Gson().fromJson(script, JsonElement.class);
        //je.getAsJsonObject().addProperty("nuni", nuni);
        iwin.build(je.toString());
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tree.getBorder() != null) {
            if (tree.isSelectionEmpty() == false) {
                DefMutableTreeNode removeNode = (DefMutableTreeNode) tree.getLastSelectedPathComponent();
                if (removeNode.getChildCount() != 0) {
                    JOptionPane.showMessageDialog(this, "Нельзя удалить текущий узел т. к. у него есть подчинённые записи", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                DefMutableTreeNode parentNode = (DefMutableTreeNode) removeNode.getParent();
                if (JOptionPane.showConfirmDialog(this, "Хотите удалить " + removeNode + "?", "Подтвердите удаление",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null) == 0) {
                    removeNode.record.set(eSystree.up, Query.DEL);
                    qSystree.delete(removeNode.record);
                    qSystree.remove(removeNode.record);
                    ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(removeNode);
                    if (parentNode != null) {
                        TreeNode[] nodes = ((DefaultTreeModel) tree.getModel()).getPathToRoot(parentNode);
                        tree.scrollPathToVisible(new TreePath(nodes));
                        tree.setSelectionPath(new TreePath(nodes));
                    }
                }
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        DefMutableTreeNode node = (DefMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node != null) {
            if (tree.getBorder() != null) {
                Record record = eSystree.up.newRecord(Query.INS);
                record.setNo(eSystree.id, ConnApp.instanc().genId(eSystree.id));
                int parent_id = (node.record.getInt(eSystree.id) == node.record.getInt(eSystree.parent_id)) ? record.getInt(eSystree.id) : node.record.getInt(eSystree.id);
                record.setNo(eSystree.parent_id, parent_id);
                record.setNo(eSystree.name, "P" + record.getStr(eSystree.id));
                qSystree.insert(record); //record сохраним в базе
                record.set(eSystree.up, Query.SEL);
                qSystree.add(record); //добавим record в список
                DefMutableTreeNode newNode = new DefMutableTreeNode(record);
                ((DefaultTreeModel) tree.getModel()).insertNodeInto(newNode, node, node.getChildCount()); //добавим node в tree
                TreeNode[] nodes = ((DefaultTreeModel) tree.getModel()).getPathToRoot(newNode);
                tree.scrollPathToVisible(new TreePath(nodes));
                tree.setSelectionPath(new TreePath(nodes));

            } else if (tab2.getBorder() != null) {
                Record record1 = eSysprof.up.newRecord(Query.INS);
                Record record2 = eArtikl.up.newRecord();
                record1.setNo(eSysprof.id, ConnApp.instanc().genId(eSysprof.id));
                record1.setNo(eSysprof.systree_id, nuni);
                qSysprof.add(record1);
                qSysprof.table(eArtikl.up).add(record2);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qSysprof, tab2);

            } else if (tab3.getBorder() != null) {
                Record record1 = eSysfurn.up.newRecord(Query.INS);
                Record record2 = eFurniture.up.newRecord();
                record1.setNo(eSysfurn.id, ConnApp.instanc().genId(eSysfurn.id));
                record1.setNo(eSysfurn.systree_id, nuni);
                record1.setNo(eSysfurn.npp, 0);
                record1.setNo(eSysfurn.replac, 0);
                qSysfurn.add(record1);
                qSysfurn.table(eFurniture.up).add(record2);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qSysfurn, tab3);

            } else if (tab4.getBorder() != null) {
                Record record1 = eSyspar1.up.newRecord(Query.INS);
                record1.setNo(eSyspar1.id, ConnApp.instanc().genId(eSyspar1.id));
                record1.setNo(eSyspar1.systree_id, nuni);
                qSyspar1.add(record1);
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qSyspar1, tab4);

            } else if (tab5.getBorder() != null) {
                DefMutableTreeNode selectedNode = (DefMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.isLeaf()) {
                    FrameProgress.create(Systree.this, new FrameListener() {
                        public void actionRequest(Object obj) {
                            frame = new BoxTypical(Systree.this, listenerProd);
                            FrameToFile.setFrameSize(frame);
                            frame.setVisible(true);
                        }
                    });
                }
            }
        }
    }//GEN-LAST:event_btnInsert

    private void btnArtikl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtikl
        Record record = qSysprof.get(Util.getSelectedRec(tab2));
        Record record2 = eArtikl.find(record.getInt(eSysprof.artikl_id), false);
        FrameProgress.create(this, new FrameListener() {
            public void actionRequest(Object obj) {
                App1.eApp1.Artikles.createFrame(Systree.this, record2);
            }
        });
    }//GEN-LAST:event_btnArtikl

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        if (tree.isEditing()) {
            tree.getCellEditor().stopCellEditing();
        }
        tree.setBorder(null);
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
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        if (tree.isEditing()) {
            tree.getCellEditor().stopCellEditing();
        }
        eProperty.systree_nuni.write(String.valueOf(nuni)); //запишем текущий nuni в файл
        Arrays.asList(tab1, tab2, tab3, tab4).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (frame != null)
            frame.dispose();
    }//GEN-LAST:event_windowClosed

    private void tabb1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabb1StateChanged
        if (tree.isEditing()) {
            tree.getCellEditor().stopCellEditing();
        }
        tree.setBorder(null);
        if (tabb1.getSelectedIndex() == 1) {
            Util.listenerClick(tab2, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 2) {
            Util.listenerClick(tab3, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 3) {
            Util.listenerClick(tab4, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        } else if (tabb1.getSelectedIndex() == 4) {
            Util.listenerClick(tab5, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        }
    }//GEN-LAST:event_tabb1StateChanged

    private void treeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeMousePressed
        Arrays.asList(tab1, tab2, tab3, tab4, tab5).forEach(tab -> tab.setBorder(null));
        tree.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
    }//GEN-LAST:event_treeMousePressed

    private void btnField1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField1

        listenerBtn1 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            for (int i = 0; i < qSystree.size(); i++) {
                if (nuni == qSystree.get(i).getInt(eSystree.id)) {
                    qSystree.set(record.getStr(eArtikl.code), i, eSystree.glas);
                    rsvSystree.load(i);
                }
            }
        };
        new DicArtikl(this, listenerBtn1, 5);
    }//GEN-LAST:event_btnField1

    private void btnField11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField11

        listenerBtn11 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            for (int i = 0; i < qSystree.size(); i++) {
                if (nuni == qSystree.get(i).getInt(eSystree.id)) {
                    qSystree.set(record.getInt(0), i, eSystree.imgview);
                    rsvSystree.load(i);
                }
            }
        };
        new DicEnums(this, listenerBtn11, LayoutProduct.values());
    }//GEN-LAST:event_btnField11

    private void btnField7(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField7

        listenerBtn7 = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            for (int i = 0; i < qSystree.size(); i++) {
                if (nuni == qSystree.get(i).getInt(eSystree.id)) {
                    qSystree.set(record.getInt(0), i, eSystree.types);
                    rsvSystree.load(i);
                }
            }
        };
        new DicEnums(this, listenerBtn7, TypeUse.values());
    }//GEN-LAST:event_btnField7

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnArtikl;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnField1;
    private javax.swing.JButton btnField11;
    private javax.swing.JButton btnField7;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JPanel panDesign;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JTree tree;
    private javax.swing.JFormattedTextField txtField1;
    private javax.swing.JTextField txtField10;
    private javax.swing.JTextField txtField11;
    private javax.swing.JFormattedTextField txtField12;
    private javax.swing.JTextField txtField2;
    private javax.swing.JTextField txtField3;
    private javax.swing.JTextField txtField4;
    private javax.swing.JTextField txtField5;
    private javax.swing.JFormattedTextField txtField6;
    private javax.swing.JFormattedTextField txtField7;
    private javax.swing.JTextField txtField8;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        Util.documentFilter1(txtField2, txtField3, txtField4, txtField5);
        nuni = Integer.valueOf(eProperty.systree_nuni.read());
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5)));
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        tree.getSelectionModel().addTreeSelectionListener(tse -> selectionTree());
        tab5.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab5();
                }
            }
        });
    }

    private class DefMutableTreeNode extends DefaultMutableTreeNode {

        public Record record = null;

        public DefMutableTreeNode(Record record) {
            super(record.getStr(eSystree.name));
            this.record = record;
        }
    }
}
