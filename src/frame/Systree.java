package frame;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import common.DialogListener;
import common.FrameListener;
import common.FrameProgress;
import common.FrameToFile;
import common.Util;
import common.eProperty;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import dialog.DicArtikl;
import dialog.DicEnums;
import domain.eArtikl;
import domain.eElemgrp;
import domain.eFurniture;
import domain.eFurnside1;
import domain.eParams;
import domain.eSysfurn;
import domain.eSyspar1;
import domain.eSysprod;
import domain.eSysprof;
import domain.eSystree;
import enums.Enam;
import enums.LayoutFurn1;
import enums.LayoutProfile;
import enums.UserArtikl;
import enums.TypeUse;
import enums.UseFurn2;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import main.App1;
import swing.DefFieldRenderer;
import swing.DefTableModel;
import wincalc.Wincalc;
import wincalc.model.PaintPanel;

public class Systree extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.values()).select(eParams.up, "where", eParams.grup, "< 0").table(eParams.up);
    private Query qSystree = new Query(eSystree.values()).select(eSystree.up);
    private Query qSysprof = new Query(eSysprof.values(), eArtikl.values());
    private Query qSysfurn = new Query(eSysfurn.values(), eFurniture.values());
    private Query qSyspar1 = new Query(eSyspar1.values());
    private DialogListener listenerArtikl, listenerUsetyp, listenetNuni, listenerModify, listenerSide;
    private DefaultMutableTreeNode root = null;
    private DefFieldRenderer rsvSystree;
    private Wincalc iwin = new Wincalc();
    private int nuni = -1;
    private TreeNode[] treeNode = null;
    private PaintPanel paintPanel = new PaintPanel(iwin) {

        public void actionResponse(MouseEvent evt) {
//            ElemSimple elem = iwin.listElem.stream().filter(el -> el.contains(evt.getX(), evt.getY())).findFirst().orElse(null);
//            if (elem != null) {
//                txtField5.setText(String.valueOf(elem.getId()));
//                repaint();
//            }
        }
    };

    public Systree() {
        initComponents();
        initElements();
        listenerDict();
        loadingData();
        loadingModel();        
    }

    private void loadingData() {

        nuni = Integer.valueOf(eProperty.systree_nuni.read());
        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("Дерево системы профилей");
        ArrayList<DefaultMutableTreeNode> treeList = new ArrayList();
        Query q = qSystree.table(eSystree.up);
        for (Record record : q) {
            if (record.getInt(eSystree.parent_id) == record.getInt(eSystree.id)) {
                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(new UserNode(record));
                treeList.add(node2);
                treeNode1.add(node2);
            }
        }
        ArrayList<DefaultMutableTreeNode> treeList2 = addChild(treeList, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList3 = addChild(treeList2, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList4 = addChild(treeList3, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList5 = addChild(treeList4, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList6 = addChild(treeList5, new ArrayList());
        tree.setModel(new DefaultTreeModel(treeNode1));
        scr1.setViewportView(tree);
    }

    private void loadingModel() {

        DefTableModel rsmSystree = new DefTableModel(new JTable(), qSystree, eSystree.id);
        DefTableModel rsmSysprof = new DefTableModel(tab2, qSysprof, eSysprof.id, eSysprof.use_type, eSysprof.use_side, eSysprof.prio, eArtikl.code, eArtikl.name) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eSysprof.use_side && val != null) {
                    LayoutProfile en = LayoutProfile.get(Integer.valueOf(val.toString()));
                    if (en != null) {
                        return en.text();
                    }
                } else if (field == eSysprof.use_type && val != null) {
                    UserArtikl en = UserArtikl.get(Integer.valueOf(val.toString()));
                    if (en != null) {
                        return en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab3, qSysfurn, eSysfurn.npp, eFurniture.name, eSysfurn.side_open, eSysfurn.replac, eSysfurn.hand_pos);
        new DefTableModel(tab4, qSyspar1, eSyspar1.grup, eSyspar1.text, eSyspar1.fixed) {
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && field == eSyspar1.grup) {
                    return qParams.stream().filter(rec -> (rec.get(eParams.grup).equals(val)
                            && rec.getInt(eParams.numb) == 0)).findFirst().orElse(qParams.newRecord(Query.SEL)).getStr(eParams.text);
                }
                return val;
            }
        };

        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            new DicEnums(this, listenerUsetyp, UserArtikl.values());
        });

        Util.buttonEditorCell(tab2, 2).addActionListener(event -> {
            new DicEnums(this, listenerSide, LayoutProfile.values());
        });

        Util.buttonEditorCell(tab2, 4).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });
        Util.buttonEditorCell(tab2, 5).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        rsmSysprof.setFrameListener(listenerModify);
        rsvSystree = new DefFieldRenderer(rsmSystree);
        rsvSystree.add(eSystree.name, txtField8);
        rsvSystree.add(eSystree.types, txtField7, TypeUse.values());
        rsvSystree.add(eSystree.glas, txtField1);
        rsvSystree.add(eSystree.size, txtField2);
        rsvSystree.add(eSystree.col1, txtField3);
        rsvSystree.add(eSystree.col2, txtField4);
        rsvSystree.add(eSystree.col3, txtField5);
        rsvSystree.add(eSystree.id, txtField6);

        panDesign.add(paintPanel, java.awt.BorderLayout.CENTER);
        paintPanel.setVisible(true);

        if (treeNode != null) {
            tree.setSelectionPath(new TreePath(treeNode));
        } else {
            tree.setSelectionRow(0);
        }
    }

    private void selectionTree() {

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (selectedNode.getUserObject() instanceof UserNode) {
                UserNode node = (UserNode) selectedNode.getUserObject();
                nuni = node.record.getInt(eSystree.id);
                eProperty.systree_nuni.write(String.valueOf(nuni));
                int sysprod_id = node.record.getInt(eSystree.sysprod_id);
                //if (sysprod_id != -1) {
                    createWincalc(sysprod_id);
                //}
                Query q = qSystree.table(eSystree.up);
                for (int i = 0; i < q.size(); i++) {
                    if (nuni == q.get(i).getInt(eSystree.id)) {
                        rsvSystree.load(i);
                    }
                }
                qSysprof.select(eSysprof.up, "left join", eArtikl.up, "on", eArtikl.id, "=",
                        eSysprof.artikl_id, "where", eSysprof.systree_id, "=", node.record.getInt(eSystree.id));
                qSysfurn.select(eSysfurn.up, "left join", eFurniture.up, "on", eFurniture.id, "=",
                        eSysfurn.furniture_id, "where", eSysfurn.systree_id, "=", node.record.getInt(eSystree.id));
                qSyspar1.select(eSyspar1.up, "where", eSyspar1.systree_id, "=", node.record.getInt(eSystree.id));
                //"and", eSyspar1.par2, "= 0");

                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, 0);
                Util.setSelectedRow(tab3, 0);
                Util.setSelectedRow(tab4, 0);
            }
        } else {
            createWincalc(-1);
        }
    }

    private void listenerDict() {

        listenerUsetyp = (record) -> {
            Util.listenerEnums(record, tab2, eSysprof.use_type, tab2, tab3, tab4);
        };

        listenerSide = (record) -> {
            Util.listenerEnums(record, tab2, eSysprof.use_side, tab2, tab3, tab4);
        };

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab2, tab3, tab4);
            if (tab2.getBorder() != null) {
                int row = tab2.getSelectedRow();
                qSysprof.set(record.getInt(eArtikl.id), Util.getSelectedRec(tab2), eSysprof.artikl_id);
                qSysprof.table(eArtikl.up).set(record.get(eArtikl.name), Util.getSelectedRec(tab2), eArtikl.name);
                qSysprof.table(eArtikl.up).set(record.get(eArtikl.code), Util.getSelectedRec(tab2), eArtikl.code);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, row);
            }
        };

        listenetNuni = (record) -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                if (selectedNode.getUserObject() instanceof UserNode) {
                    UserNode node = (UserNode) selectedNode.getUserObject();
                    Record record2 = node.record;
                    int id = record.getInt(0);
                    record2.set(eSystree.sysprod_id, record.getInt(0));
                    qSystree.update(record2);
                    createWincalc(record.getInt(0));
                }
            }
        };

        listenerModify = (record) -> {
            System.out.println("frame.Systree.listenerDict()");
        };
    }

    private ArrayList<DefaultMutableTreeNode> addChild(ArrayList<DefaultMutableTreeNode> nodeList1, ArrayList<DefaultMutableTreeNode> nodeList2) {

        Query systreeList = qSystree.table(eSystree.up);
        for (DefaultMutableTreeNode node : nodeList1) {
            UserNode userNode = (UserNode) node.getUserObject();
            for (Record record2 : systreeList) {
                if (record2.getInt(eSystree.parent_id) == userNode.record.getInt(eSystree.id)
                        && record2.getInt(eSystree.parent_id) != record2.getInt(eSystree.id)) {
                    DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(new UserNode(record2));
                    node.add(node2);
                    nodeList2.add(node2);
                    if (record2.getInt(eSystree.id) == nuni) {
                        treeNode = node2.getPath();
                    }
                }
            }
        }
        return nodeList2;
    }

    private void createWincalc(int sysprod_id) {
        if (sysprod_id != -1) {
            String script = new Query(eSysprod.script).select(eSysprod.up, "where", eSysprod.id, "=", sysprod_id).getAs(0, eSysprod.script);
            if (script != null && script.isEmpty() == false) {
                JsonElement je = new Gson().fromJson(script, JsonElement.class);
                je.getAsJsonObject().addProperty("nuni", nuni);
                iwin.create(je.toString());
                paintPanel.repaint(true, 12);
            }
        } else {
            Graphics2D g = (Graphics2D) paintPanel.getGraphics();
            g.clearRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnNon = new javax.swing.JToggleButton();
        btnFill = new javax.swing.JToggleButton();
        btnJoin = new javax.swing.JToggleButton();
        btnElem = new javax.swing.JToggleButton();
        btnFurn = new javax.swing.JToggleButton();
        btnSpec = new javax.swing.JToggleButton();
        panCentr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        pan1 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        panDesign = new javax.swing.JPanel();
        btnTypicalOkna = new javax.swing.JButton();
        pam9 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        tabb1 = new javax.swing.JTabbedPane();
        pan6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtField1 = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        txtField2 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        txtField3 = new javax.swing.JFormattedTextField();
        txtField4 = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        txtField5 = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtField6 = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        txtField7 = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        txtField8 = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        txtField9 = new javax.swing.JFormattedTextField();
        pan3 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan4 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Системы профилей");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(800, 29));

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
                btnCloseClose(evt);
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"))); // NOI18N
        btnSave.setToolTipText(bundle.getString("Сохранить")); // NOI18N
        btnSave.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setMaximumSize(new java.awt.Dimension(25, 25));
        btnSave.setMinimumSize(new java.awt.Dimension(25, 25));
        btnSave.setPreferredSize(new java.awt.Dimension(25, 25));
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave(evt);
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

        btnNon.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c028.gif"))); // NOI18N
        btnNon.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnNon.setMaximumSize(new java.awt.Dimension(25, 25));
        btnNon.setMinimumSize(new java.awt.Dimension(25, 25));
        btnNon.setPreferredSize(new java.awt.Dimension(25, 25));
        btnNon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructive(evt);
            }
        });

        btnFill.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c026.gif"))); // NOI18N
        btnFill.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFill.setMaximumSize(new java.awt.Dimension(25, 25));
        btnFill.setMinimumSize(new java.awt.Dimension(25, 25));
        btnFill.setPreferredSize(new java.awt.Dimension(25, 25));
        btnFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructive(evt);
            }
        });

        btnJoin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c024.gif"))); // NOI18N
        btnJoin.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnJoin.setMaximumSize(new java.awt.Dimension(25, 25));
        btnJoin.setMinimumSize(new java.awt.Dimension(25, 25));
        btnJoin.setPreferredSize(new java.awt.Dimension(25, 25));
        btnJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructive(evt);
            }
        });

        btnElem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c025.gif"))); // NOI18N
        btnElem.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnElem.setMaximumSize(new java.awt.Dimension(25, 25));
        btnElem.setMinimumSize(new java.awt.Dimension(25, 25));
        btnElem.setPreferredSize(new java.awt.Dimension(25, 25));
        btnElem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructive(evt);
            }
        });

        btnFurn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFurn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c027.gif"))); // NOI18N
        btnFurn.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFurn.setMaximumSize(new java.awt.Dimension(25, 25));
        btnFurn.setMinimumSize(new java.awt.Dimension(25, 25));
        btnFurn.setPreferredSize(new java.awt.Dimension(25, 25));
        btnFurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructive(evt);
            }
        });

        btnSpec.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSpec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c016.gif"))); // NOI18N
        btnSpec.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSpec.setMaximumSize(new java.awt.Dimension(25, 25));
        btnSpec.setMinimumSize(new java.awt.Dimension(25, 25));
        btnSpec.setPreferredSize(new java.awt.Dimension(25, 25));
        btnSpec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructive(evt);
            }
        });

        javax.swing.GroupLayout panNorthLayout = new javax.swing.GroupLayout(panNorth);
        panNorth.setLayout(panNorthLayout);
        panNorthLayout.setHorizontalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167)
                .addComponent(btnJoin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnElem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFurn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSpec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 302, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panNorthLayout.createSequentialGroup()
                        .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnFill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnJoin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnElem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFurn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSpec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCentr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panCentr.setPreferredSize(new java.awt.Dimension(896, 409));
        panCentr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(260, 564));
        scr1.setViewportView(tree);

        panCentr.add(scr1, java.awt.BorderLayout.WEST);

        pan1.setPreferredSize(new java.awt.Dimension(602, 565));
        pan1.setLayout(new java.awt.BorderLayout());

        pan2.setPreferredSize(new java.awt.Dimension(692, 200));
        pan2.setLayout(new java.awt.BorderLayout());

        pan7.setLayout(new java.awt.BorderLayout());

        panDesign.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panDesign.setPreferredSize(new java.awt.Dimension(200, 200));
        panDesign.setLayout(new java.awt.BorderLayout());

        btnTypicalOkna.setText("Типовая конструкция");
        btnTypicalOkna.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnTypicalOkna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTypicalOkna(evt);
            }
        });
        panDesign.add(btnTypicalOkna, java.awt.BorderLayout.SOUTH);

        pan7.add(panDesign, java.awt.BorderLayout.WEST);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel22.setFont(common.Util.getFont(0,0));
        jLabel22.setText("Артикул профиля");
        jLabel22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel22.setPreferredSize(new java.awt.Dimension(160, 18));

        javax.swing.GroupLayout pam9Layout = new javax.swing.GroupLayout(pam9);
        pam9.setLayout(pam9Layout);
        pam9Layout.setHorizontalGroup(
            pam9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pam9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, 0, 192, Short.MAX_VALUE)
                .addContainerGap())
        );
        pam9Layout.setVerticalGroup(
            pam9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pam9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pam9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(167, Short.MAX_VALUE))
        );

        pan7.add(pam9, java.awt.BorderLayout.CENTER);

        pan2.add(pan7, java.awt.BorderLayout.CENTER);

        pan1.add(pan2, java.awt.BorderLayout.NORTH);

        tabb1.setPreferredSize(new java.awt.Dimension(455, 325));

        pan6.setPreferredSize(new java.awt.Dimension(360, 208));

        jLabel13.setFont(common.Util.getFont(0,0));
        jLabel13.setText("Заполнение по умолчанию");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel13.setPreferredSize(new java.awt.Dimension(160, 18));

        txtField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField1.setPreferredSize(new java.awt.Dimension(240, 18));

        jLabel14.setFont(common.Util.getFont(0,0));
        jLabel14.setText("Доступные толщины");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel14.setPreferredSize(new java.awt.Dimension(160, 18));

        txtField2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField2.setPreferredSize(new java.awt.Dimension(240, 18));

        jLabel15.setFont(common.Util.getFont(0,0));
        jLabel15.setText("Основная текстура");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel15.setPreferredSize(new java.awt.Dimension(160, 18));

        txtField3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField3.setPreferredSize(new java.awt.Dimension(240, 18));

        txtField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField4.setPreferredSize(new java.awt.Dimension(240, 18));

        jLabel16.setFont(common.Util.getFont(0,0));
        jLabel16.setText("Внутренняя текстура");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel16.setPreferredSize(new java.awt.Dimension(160, 18));

        txtField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField5.setPreferredSize(new java.awt.Dimension(240, 18));

        jLabel17.setFont(common.Util.getFont(0,0));
        jLabel17.setText("Внешняя текстура");
        jLabel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel17.setPreferredSize(new java.awt.Dimension(160, 18));

        jLabel18.setFont(common.Util.getFont(0,0));
        jLabel18.setText("NUNI");
        jLabel18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel18.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField6.setPreferredSize(new java.awt.Dimension(20, 18));

        jLabel19.setFont(common.Util.getFont(0,0));
        jLabel19.setText("Тип изделия");
        jLabel19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel19.setPreferredSize(new java.awt.Dimension(160, 18));

        txtField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField7.setPreferredSize(new java.awt.Dimension(240, 18));

        jLabel20.setFont(common.Util.getFont(0,0));
        jLabel20.setText("Название изделия");
        jLabel20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel20.setPreferredSize(new java.awt.Dimension(160, 18));

        txtField8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField8.setPreferredSize(new java.awt.Dimension(240, 18));

        jLabel21.setFont(common.Util.getFont(0,0));
        jLabel21.setText("INDEX");
        jLabel21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel21.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField9.setPreferredSize(new java.awt.Dimension(20, 18));

        javax.swing.GroupLayout pan6Layout = new javax.swing.GroupLayout(pan6);
        pan6.setLayout(pan6Layout);
        pan6Layout.setHorizontalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan6Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(126, 126, 126))
        );
        pan6Layout.setVerticalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(95, Short.MAX_VALUE))
        );

        tabb1.addTab("Основные параметры", pan6);

        pan3.setPreferredSize(new java.awt.Dimension(450, 300));
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
                "ID", "Применение", "Сторона", "Приоритет", "Артикул", "Название"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(40);
        }

        pan3.add(scr2, java.awt.BorderLayout.CENTER);

        tabb1.addTab("Профили в системе", pan3);

        pan4.setPreferredSize(new java.awt.Dimension(450, 300));
        pan4.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(450, 300));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "№пп", "Название  фурнитуры", "Тип открывания", "Замена", "Установка ручки"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(0).setMaxWidth(80);
            tab3.getColumnModel().getColumn(3).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(3).setMaxWidth(80);
        }

        pan4.add(scr3, java.awt.BorderLayout.CENTER);

        tabb1.addTab("Фурнитура в системе", pan4);

        pan5.setPreferredSize(new java.awt.Dimension(450, 300));
        pan5.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(null);
        scr4.setPreferredSize(new java.awt.Dimension(450, 300));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Параметр", "Значение по умолчанию", "Закреплено"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        tab4.setName("tab4"); // NOI18N
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(2).setPreferredWidth(60);
            tab4.getColumnModel().getColumn(2).setMaxWidth(80);
        }

        pan5.add(scr4, java.awt.BorderLayout.CENTER);

        tabb1.addTab("Параметры по умолчанию", pan5);

        pan1.add(tabb1, java.awt.BorderLayout.CENTER);

        panCentr.add(pan1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(1000, 20));
        panSouth.setLayout(new javax.swing.BoxLayout(panSouth, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
        labFilter.setMaximumSize(new java.awt.Dimension(100, 14));
        labFilter.setMinimumSize(new java.awt.Dimension(100, 14));
        labFilter.setPreferredSize(new java.awt.Dimension(100, 14));
        panSouth.add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(80, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(80, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(80, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                filterCaretUpdate(evt);
            }
        });
        panSouth.add(txtFilter);

        checkFilter.setText("в конце строки");
        panSouth.add(checkFilter);

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        //iwin.create(Winscript.test(Winscript.prj, null));
        //paintPanel.repaint(true, 12);
    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave
        FrameProgress.create(this, new FrameListener() {
            public void actionRequest(Object obj) {
                App1.eApp1.Element.createFrame(Systree.this);
            }
        });
    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnConstructive(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConstructive

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (selectedNode.getUserObject() instanceof UserNode) {
                UserNode node = (UserNode) selectedNode.getUserObject();
                int nuni = node.record.getInt(eSystree.id);
                JToggleButton btn = (JToggleButton) evt.getSource();
                FrameProgress.create(Systree.this, new FrameListener() {
                    public void actionRequest(Object obj) {

                        java.awt.Frame frame = null;
                        if (btn == btnJoin) {
                            frame = new Joining(Systree.this, nuni);
                        } else if (btn == btnElem) {
                            frame = new Element(Systree.this, nuni);
                        } else if (btn == btnFill) {
                            frame = new Filling(Systree.this, nuni);
                        } else if (btn == btnFurn) {
                            frame = new Furniture(Systree.this, nuni);
                        } else if (btn == btnSpec) {
                            frame = new Specific(Systree.this, iwin);
                        }
                        FrameToFile.setFrameSize(frame);
                        frame.setVisible(true);
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, "Выберите систему профилей", "Предупреждение", JOptionPane.OK_OPTION);
            }
        }
    }//GEN-LAST:event_btnConstructive

    private void btnTypicalOkna(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTypicalOkna
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null && selectedNode.isLeaf()) {
            FrameProgress.create(Systree.this, new FrameListener() {
                public void actionRequest(Object obj) {
                    BoxTypical frame = new BoxTypical(Systree.this, listenetNuni);
                    FrameToFile.setFrameSize(frame);
                    frame.setVisible(true);
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Выберите систему профилей", "Предупреждение", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_btnTypicalOkna

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab2, tab3, tab4));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab2, tab3, tab4).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab2);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Util.stopCellEditing(tab2, tab3, tab4);
        Arrays.asList(tab2, tab3, tab4).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
    }//GEN-LAST:event_formWindowClosed

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JToggleButton btnElem;
    private javax.swing.JToggleButton btnFill;
    private javax.swing.JToggleButton btnFurn;
    private javax.swing.JButton btnIns;
    private javax.swing.JToggleButton btnJoin;
    private javax.swing.JToggleButton btnNon;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnSave;
    private javax.swing.JToggleButton btnSpec;
    private javax.swing.JButton btnTypicalOkna;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel pam9;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panDesign;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JTree tree;
    private javax.swing.JFormattedTextField txtField1;
    private javax.swing.JFormattedTextField txtField2;
    private javax.swing.JFormattedTextField txtField3;
    private javax.swing.JFormattedTextField txtField4;
    private javax.swing.JFormattedTextField txtField5;
    private javax.swing.JFormattedTextField txtField6;
    private javax.swing.JFormattedTextField txtField7;
    private javax.swing.JFormattedTextField txtField8;
    private javax.swing.JFormattedTextField txtField9;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab2, tab3, tab4)));
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent tse) {
                selectionTree();
            }
        });
    }

    private class UserNode {

        Record record;

        UserNode(Record record) {
            this.record = record;
        }

        public String toString() {
            return record.getStr(eSystree.name);
        }
    }
}
