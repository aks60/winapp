package frame;

import dialog.DicColor2;
import common.DialogListener;
import common.FrameToFile;
import common.Util;
import dataset.ConnApp;
import dataset.Field;
import swing.DefCellEditor;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eArtdet;
import domain.eColor;
import domain.eCurrenc;
import domain.eColgrp;
import enums.TypeArtikl1;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import swing.DefTableModel;
import swing.DefFieldEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import dialog.DicArtikl;
import dialog.DicGroups;
import dialog.ParGrup1;
import domain.eElement;
import domain.eGroups;
import domain.eParams;
import domain.eSysprof;
import enums.TypeGroups;
import java.awt.Window;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.swing.JPanel;
import javax.swing.RowFilter;

/**
 * Материальные ценности
 */
public class Artikles extends javax.swing.JFrame {

    private Query qColgrp = new Query(eColgrp.values()).select(eColgrp.up);
    private Query qColor = new Query(eColor.values()).select(eColor.up);
    private Query qGroups = new Query(eGroups.values());
    private Query qCurrenc = new Query(eCurrenc.values()).select(eCurrenc.up);
    private Query qArtikl = new Query(eArtikl.values());
    private Query qArtdet = new Query(eArtdet.values());
    private DefFieldEditor rsvArtikl;
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;
    private DialogListener listenerSeries, listenerColor, listenerCurrenc, listenerAnalog;

    public Artikles() {
        initComponents();
        initElements();
        listenerDict();
        loadingData();
        loadingModel();
        loadingTree();
    }

    public Artikles(java.awt.Window owner, int nuni) {
        initComponents();
        this.owner = owner;
        this.nuni = nuni;
        initElements();
        listenerDict();
        loadingData();
        loadingModel();
        loadingTree();
    }

    private void loadingData() {
        if (owner != null) {
            new Query(eSysprof.artikl_id).select(eSysprof.up, "where", eSysprof.systree_id, "=", nuni).forEach(record -> {
                subsql = subsql + "," + record.getStr(eSysprof.artikl_id);
                new Query(eElement.artikl_id).select(eElement.up, "where", eElement.artikl_id, "=", record.getInt(eSysprof.artikl_id)).forEach(record2 -> {
                    subsql = subsql + "," + record2.getStr(eElement.artikl_id);
                });
            });
            subsql = "(" + subsql.substring(1) + ")";
        }
        qGroups.select(eGroups.up, "where grup =" + TypeGroups.SERIES.id);
    }

    private void loadingModel() {

        DefTableModel rsmArtikl = new DefTableModel(tab1, qArtikl, eArtikl.code, eArtikl.name);
        DefTableModel rsmArtdet = new DefTableModel(tab2, qArtdet, eArtdet.id, eArtdet.color_fk, eArtdet.cost_cl1, eArtdet.cost_cl2, eArtdet.cost_cl3, eArtdet.cost_unit) {
            @Override
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eArtdet.id || field == eArtdet.color_fk) {
                    Record artdetRec = qArtdet.get(row);
                    Integer color_fk = artdetRec.getInt(eArtdet.color_fk);
                    if (color_fk == null || (color_fk == -1 && artdetRec.getStr(eArtdet.up).equals("INS"))) {
                        return null;
                    }
                    if (field == eArtdet.id) {
                        if (color_fk >= 0) {
                            Record colorRec = qColor.stream().filter(rec -> rec.getInt(eColor.id) == color_fk).findFirst().orElse(null);
                            int colgrp_id = colorRec.getInt(eColor.colgrp_id);
                            Record colgrpRec = qColgrp.stream().filter(rec -> rec.getInt(eColgrp.id) == colgrp_id).findFirst().orElse(null);
                            return colgrpRec.getStr(eColgrp.name);

                        } else if (color_fk < 0) {
                            Record colgrpRec = qColgrp.stream().filter(rec -> rec.getInt(eColgrp.id) == Math.abs(color_fk)).findFirst().orElse(null);
                            return colgrpRec.getStr(eColgrp.name);
                        }
                    } else if (field == eArtdet.color_fk) {
                        if (color_fk >= 0) {
                            Record colorRec = qColor.stream().filter(rec -> rec.getInt(eColor.id) == color_fk).findFirst().orElse(null);
                            return colorRec.getStr(eColor.name);

                        } else if (color_fk < 0) {
                            return "Все текстуры группы";
                        }
                    }
                }
                return val;
            }
        };

        rsvArtikl = new DefFieldEditor(tab1) {
            @Override
            public void load(Integer row) {
                super.load(row);
                Record artiklRec = qArtikl.get(Util.getSelectedRec(tab1));
                Record groupsRec = qGroups.stream().filter(rec -> rec.getInt(eGroups.id) == artiklRec.getInt(eArtikl.series_id)).findFirst().orElse(null);
                Record currencRec = qCurrenc.stream().filter(rec -> rec.get(eCurrenc.id).equals(artiklRec.get(eArtikl.currenc_id))).findFirst().orElse(null);
                String name = (currencRec != null) ? currencRec.getStr(eCurrenc.name) : null;
                txtField7.setText(name);
                name = (groupsRec != null) ? groupsRec.getStr(eGroups.name) : null;
                txtField10.setText(name);
                if (artiklRec.getInt(eArtikl.analog_id) != -1) {
                    Record analogRec = qArtikl.stream().filter(rec -> rec.get(eArtikl.id).equals(artiklRec.get(eArtikl.analog_id))).findFirst().orElse(null);
                    txtField11.setText(analogRec.getStr(eArtikl.code));
                } else {
                    txtField11.setText(null);
                }

            }
        };
        rsvArtikl.add(eArtikl.len_unit, txtField1);
        rsvArtikl.add(eArtikl.height, txtField2);
        rsvArtikl.add(eArtikl.depth, txtField3);
        rsvArtikl.add(eArtikl.depth, txtField4);
        rsvArtikl.add(eArtikl.depth, txtField5);
        rsvArtikl.add(eArtikl.otx_norm, txtField6);
        rsvArtikl.add(eArtikl.size_centr, txtField8);
        rsvArtikl.add(eArtikl.min_rad, txtField12);

        Util.buttonEditorCell(tab2, 0).addActionListener(event -> {
            DicColor2 frame = new DicColor2(this, listenerColor);
        });
        
        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            DicColor2 frame = new DicColor2(this, listenerColor);
        });
    }

    private void loadingTree() {

        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("Мат. ценности");
        DefaultMutableTreeNode treeNode2 = null;
        for (TypeArtikl1 it : TypeArtikl1.values()) {
            if (it.id1 == 1 && it.id2 == 0) {
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl1.PPROFIL); //"Профили"

            } else if (it.id1 == 2 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl1.ACSESYAR); //"Аксессуары"

            } else if (it.id1 == 3 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl1.POGONAG); //"Погонаж"

            } else if (it.id1 == 4 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl1.INSTRYMENT); //"Инструмент"

            } else if (it.id1 == 5 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl1.ZAPOLNEN); //"Заполнения"

            } else if (it.id2 > 0) {   //остальное       
                treeNode1.add(treeNode2);
                treeNode2.add(new javax.swing.tree.DefaultMutableTreeNode(it));
            }
        }
        treeNode1.add(treeNode2);
        tree.setModel(new DefaultTreeModel(treeNode1));
        scrTree.setViewportView(tree);
        tree.setSelectionRow(0);
    }

    private void selectionTree() {

        Util.stopCellEditing(tab1, tab2);
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (selectedNode.getUserObject() instanceof TypeArtikl1 == false) {
                if (owner == null) {
                    qArtikl.select(eArtikl.up, "order by", eArtikl.level1, ",", eArtikl.code);
                } else {
                    qArtikl.select(eArtikl.up, "where", eArtikl.id, "in", subsql, "order by", eArtikl.level1, ",", eArtikl.code);
                }

            } else if (selectedNode.isLeaf()) {
                TypeArtikl1 e = (TypeArtikl1) selectedNode.getUserObject();
                if (owner == null) {
                    qArtikl.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1 + "and", eArtikl.level2, "=", e.id2, "order by", eArtikl.level1, ",", eArtikl.code);
                } else {
                    qArtikl.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1 + "and", eArtikl.level2, "=", e.id2, "and", eArtikl.id, "in", subsql, "order by", eArtikl.level1, ",", eArtikl.code);
                }
            } else {
                TypeArtikl1 e = (TypeArtikl1) selectedNode.getUserObject();
                if (owner == null) {
                    qArtikl.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1, "order by", eArtikl.level1, ",", eArtikl.code);
                } else {
                    qArtikl.select(eArtikl.up, "where", eArtikl.level1, "=", e.id1, "and", eArtikl.id, "in", subsql, "order by", eArtikl.level1, ",", eArtikl.code);
                }
            }
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab1);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {

        Util.stopCellEditing(tab1, tab2);
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record record = qArtikl.get(row);
            int id = record.getInt(eArtikl.id);
            qArtdet.select(eArtdet.up, "where", eArtdet.artikl_id, "=", id);
            rsvArtikl.load();
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
        }
    }

    public void listenerDict() {

        listenerSeries = (record) -> {
            int row = Util.getSelectedRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                artiklRec.set(eArtikl.series_id, record.get(eGroups.id));
                rsvArtikl.load();
            }
            Util.stopCellEditing(tab1, tab2);
        };

        listenerAnalog = (record) -> {
            int row = Util.getSelectedRec(tab1);
            if (row != -1) {
                System.out.println(record);
                Record artiklRec = qArtikl.get(row);
                artiklRec.set(eArtikl.analog_id, record.get(eArtikl.id));
                rsvArtikl.load();
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerColor = (record) -> {
            if (tab2.getBorder() != null) {
                if (eColgrp.values().length == record.size()) {
                    qArtdet.set(-1 * record.getInt(eColgrp.id), Util.getSelectedRec(tab2), eArtdet.color_fk);

                } else if (eColor.values().length == record.size()) {
                    qArtdet.set(record.getInt(eColor.id), Util.getSelectedRec(tab2), eArtdet.color_fk);
                }
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.stopCellEditing(tab1, tab2);
            }
        };

        listenerCurrenc = (record) -> {
            if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab1);
                if (row != -1) {
                    Record artiklRec = qArtikl.get(row);
                    artiklRec.set(eArtikl.currenc_id, record.get(eCurrenc.id));
                }
                Util.stopCellEditing(tab1, tab2);
            }
        };
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
        center = new javax.swing.JPanel();
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
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtField7 = new javax.swing.JTextField();
        txtField8 = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtField9 = new javax.swing.JFormattedTextField();
        txtField6 = new javax.swing.JFormattedTextField();
        btnField7 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtField10 = new javax.swing.JTextField();
        btnField10 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtField11 = new javax.swing.JFormattedTextField();
        btnField11 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtField12 = new javax.swing.JFormattedTextField();
        pan4 = new javax.swing.JPanel();
        scrTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        pan5 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Материальные ценности");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(900, 550));
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
                .addGap(49, 49, 49)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 690, Short.MAX_VALUE)
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
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setPreferredSize(new java.awt.Dimension(900, 550));
        center.setLayout(new java.awt.BorderLayout());

        pan2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan2.setPreferredSize(new java.awt.Dimension(300, 500));

        jLabel13.setFont(common.Util.getFont(0,0));
        jLabel13.setText("Длина");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel13.setPreferredSize(new java.awt.Dimension(108, 18));

        jLabel14.setFont(common.Util.getFont(0,0));
        jLabel14.setText("Ширина");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel14.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField5.setFont(common.Util.getFont(0,0));
        txtField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField5.setPreferredSize(new java.awt.Dimension(60, 18));

        jLabel15.setFont(common.Util.getFont(0,0));
        jLabel15.setText("Толщина");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel15.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField1.setPreferredSize(new java.awt.Dimension(60, 18));

        txtField2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField2.setPreferredSize(new java.awt.Dimension(60, 18));

        txtField3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField3.setPreferredSize(new java.awt.Dimension(60, 18));

        jLabel16.setFont(common.Util.getFont(0,0));
        jLabel16.setText("Уд.вес кг/ед");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel16.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField4.setPreferredSize(new java.awt.Dimension(60, 18));

        jLabel17.setFont(common.Util.getFont(0,0));
        jLabel17.setText("Ед.измерения");
        jLabel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel17.setPreferredSize(new java.awt.Dimension(108, 18));

        jLabel18.setFont(common.Util.getFont(0,0));
        jLabel18.setText("Норма отхода");
        jLabel18.setToolTipText("");
        jLabel18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel18.setPreferredSize(new java.awt.Dimension(108, 18));

        jLabel19.setFont(common.Util.getFont(0,0));
        jLabel19.setText("Валюта");
        jLabel19.setToolTipText("");
        jLabel19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel19.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField7.setFont(common.Util.getFont(0,0));
        txtField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField7.setFocusable(false);
        txtField7.setPreferredSize(new java.awt.Dimension(60, 18));

        txtField8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField8.setPreferredSize(new java.awt.Dimension(60, 18));

        jLabel20.setFont(common.Util.getFont(0,0));
        jLabel20.setText("От края до оси (В)");
        jLabel20.setToolTipText("");
        jLabel20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel20.setPreferredSize(new java.awt.Dimension(108, 18));

        jLabel21.setFont(common.Util.getFont(0,0));
        jLabel21.setText("Фурн. паз (F)");
        jLabel21.setToolTipText("");
        jLabel21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel21.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField9.setPreferredSize(new java.awt.Dimension(60, 18));

        txtField6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField6.setPreferredSize(new java.awt.Dimension(60, 18));

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

        jLabel22.setFont(common.Util.getFont(0,0));
        jLabel22.setText("Серия профилей");
        jLabel22.setToolTipText("");
        jLabel22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel22.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField10.setFocusable(false);
        txtField10.setPreferredSize(new java.awt.Dimension(120, 18));

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

        jLabel23.setFont(common.Util.getFont(0,0));
        jLabel23.setText("Аналог профиля");
        jLabel23.setToolTipText("");
        jLabel23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel23.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField11.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField11.setFocusable(false);
        txtField11.setPreferredSize(new java.awt.Dimension(120, 18));

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

        jLabel24.setFont(common.Util.getFont(0,0));
        jLabel24.setText("Мин. радиус гиба");
        jLabel24.setToolTipText("");
        jLabel24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel24.setPreferredSize(new java.awt.Dimension(108, 18));

        txtField12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtField12.setPreferredSize(new java.awt.Dimension(60, 18));

        javax.swing.GroupLayout pan2Layout = new javax.swing.GroupLayout(pan2);
        pan2.setLayout(pan2Layout);
        pan2Layout.setHorizontalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pan2Layout.createSequentialGroup()
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtField8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtField7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        pan2Layout.setVerticalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        center.add(pan2, java.awt.BorderLayout.EAST);

        pan4.setPreferredSize(new java.awt.Dimension(200, 500));
        pan4.setLayout(new java.awt.BorderLayout());

        scrTree.setBorder(null);
        scrTree.setPreferredSize(new java.awt.Dimension(200, 400));

        tree.setFont(common.Util.getFont(0,0));
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

        pan5.setPreferredSize(new java.awt.Dimension(400, 500));
        pan5.setLayout(new java.awt.BorderLayout());

        tab1.setFont(common.Util.getFont(0,0));
        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "111"},
                {"2", "222"}
            },
            new String [] {
                "Актикул", "Наименование"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
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
            tab1.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        pan5.add(scr1, java.awt.BorderLayout.CENTER);

        center.add(pan5, java.awt.BorderLayout.CENTER);

        pan3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan3.setPreferredSize(new java.awt.Dimension(800, 130));
        pan3.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setFont(common.Util.getFont(0,0));
        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Группа", "Название", "Основная", "Внутренняя", "Внешняя", "За ед. веса"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class
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

        pan3.add(scr2, java.awt.BorderLayout.CENTER);

        center.add(pan3, java.awt.BorderLayout.SOUTH);

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
                txtFilterCaretUpdate(evt);
            }
        });
        south.add(txtFilter);

        checkFilter.setText("в конце строки");
        south.add(checkFilter);

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
                TypeArtikl1 typeArtikl = (TypeArtikl1) selectedNode.getUserObject();
                Record artiklRec = qArtikl.newRecord(Query.INS);
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
            int row = Util.getSelectedRec(tab1);
            if (row != -1) {
                Record artiklRec = qArtikl.get(row);
                Record artdetRec = qArtdet.newRecord(Query.INS);
                artdetRec.setNo(eArtdet.id, ConnApp.instanc().genId(eArtdet.up));
                artdetRec.setNo(eArtdet.artikl_id, artiklRec.get(eArtikl.id));
                qArtdet.add(artdetRec);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qArtdet, tab2);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?",
                "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                int row = Util.getSelectedRec(tab1);
                if (row != -1) {
                    Record record = qArtikl.get(row);
                    record.set(eArtikl.up, Query.DEL);
                    qArtikl.delete(record);
                    qArtikl.removeRec(row);
                    ((DefTableModel) tab1.getModel()).fireTableDataChanged();
                    Util.setSelectedRow(tab1);
                }
            } else if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                if (row != -1) {
                    Record record = qArtdet.get(row);
                    record.set(eArtdet.up, Query.DEL);
                    qArtdet.delete(record);
                    qArtdet.removeRec(row);
                    ((DefTableModel) tab2.getModel()).fireTableDataChanged();
                    Util.setSelectedRow(tab1);
                }
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        qColgrp.select(eColgrp.up);
        qColor.select(eColor.up);
        qCurrenc.select(eCurrenc.up);
        selectionTree();
    }//GEN-LAST:event_btnRefresh

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        tab1.setRowSelectionInterval(1, 1);
    }//GEN-LAST:event_btnReport

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnField7(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField7
        Currenc frame = new Currenc(this, listenerCurrenc);
    }//GEN-LAST:event_btnField7

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
    }//GEN-LAST:event_txtFilterCaretUpdate

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1, tab2));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void btnField10(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField10
        DicGroups groups = new DicGroups(this, TypeGroups.SERIES, listenerSeries);
    }//GEN-LAST:event_btnField10

    private void btnField11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnField11
        DicArtikl artikl = new DicArtikl(this, listenerAnalog, 1);
    }//GEN-LAST:event_btnField11
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnField10;
    private javax.swing.JButton btnField11;
    private javax.swing.JButton btnField7;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox checkFilter;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
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
    private javax.swing.JFormattedTextField txtField2;
    private javax.swing.JFormattedTextField txtField3;
    private javax.swing.JFormattedTextField txtField4;
    private javax.swing.JTextField txtField5;
    private javax.swing.JFormattedTextField txtField6;
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
                "Типы артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Свойства артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Текстура артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        tree.getSelectionModel().addTreeSelectionListener(tse -> selectionTree());
        tab1.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting() == false) {
                selectionTab1(event);
            }
        });
        txtField7.setEditable(false);
        txtField7.setBackground(new java.awt.Color(255, 255, 255));
    }
}
