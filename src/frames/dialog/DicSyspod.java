package frames.dialog;

import builder.Wincalc;
import common.FrameToFile;
import common.ListenerRecord;
import common.eProperty;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eFurniture;
import domain.eSysfurn;
import domain.eSyspar1;
import domain.eSysprod;
import domain.eSysprof;
import domain.eSystree;
import frames.Util;
import frames.swing.DefMutableTreeNode;
import frames.swing.DefTableModel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class DicSyspod extends javax.swing.JDialog {

    private Wincalc iwin = new Wincalc();
    private ListenerRecord listener = null;
    private int systreeID = -1; //выбранная система
    private Query qSystree = new Query(eSystree.values());
    private Query qSysprod = new Query(eSysprod.values());
    private DefMutableTreeNode rootTree = null;
    private TreeNode[] selectedPath = null;
    private DefMutableTreeNode systreeNode = null;

    public DicSyspod(java.awt.Frame parent, ListenerRecord listener) {
        super(parent, true);
        initComponents();
        initElements();
        loadingData();
        loadingSys();
        setVisible(true);
    }

    private void loadingData() {

        //Получим сохр. ID системы при выходе из программы
        Record sysprodRec = eSysprod.find(Integer.valueOf(eProperty.sysprodID.read()));
        if (sysprodRec != null) {
            systreeID = sysprodRec.getInt(eSysprod.systree_id);
        }
        qSystree.select(eSystree.up);
//        ((DefaultTreeCellEditor) tree1.getCellEditor()).addCellEditorListener(new CellEditorListener() {
//
//            public void editingStopped(ChangeEvent e) {
//                String str = ((DefaultTreeCellEditor) tree1.getCellEditor()).getCellEditorValue().toString();
//                systreeNode.rec().set(eSystree.name, str);
//                systreeNode.setUserObject(str);
//                txt8.setText(str);
//                qSystree.update(systreeNode.rec()); //сохраним в базе
//            }
//
//            public void editingCanceled(ChangeEvent e) {
//                editingStopped(e);
//            }
//        });
    }

    private void loadingSys() {
        Record recordRoot = eSystree.up.newRecord(Query.SEL);
        recordRoot.set(eSystree.id, -1);
        recordRoot.set(eSystree.parent_id, -1);
        recordRoot.set(eSystree.name, "Дерево системы профилей");
        rootTree = new DefMutableTreeNode(recordRoot);
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
        tree1.setModel(new DefaultTreeModel(rootTree));
        scr1.setViewportView(tree1);
    }

    private void loadingTab2() {
        qSysprod.select(eSysprod.up, "where", eSysprod.systree_id, "=", systreeID);
        DefaultTableModel dm = (DefaultTableModel) tab2.getModel();
        dm.getDataVector().removeAllElements();
        ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
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
                dm.addRow(arrayRec);

            } catch (Exception e) {
                System.err.println("Ошибка:DicSyspod.loadingTab2() " + e);
            }
        }        
    }   
    
    private void selectionSys() {

        systreeNode = (DefMutableTreeNode) tree1.getLastSelectedPathComponent();
        if (systreeNode != null) {
            systreeID = systreeNode.rec().getInt(eSystree.id);

            loadingTab2();

            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            int index = -1;
            int sysprodID = Integer.valueOf(eProperty.sysprodID.read());
            for (int i = 0; i < qSysprod.size(); ++i) {
                if (qSysprod.get(i).getInt(eSysprod.id) == sysprodID) {
                    index = i;
                }
            }
            if (index != -1) {
                Util.setSelectedRow(tab2, index);
            } else {
                Util.setSelectedRow(tab2);
            }
        } else {
            //createWincalc(-1); //рисуем виртуалку
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tree1 = new javax.swing.JTree();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Изделия");

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(460, 29));

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

        btnChoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c044.gif"))); // NOI18N
        btnChoice.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnChoice.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnChoice.setFocusable(false);
        btnChoice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChoice.setMaximumSize(new java.awt.Dimension(25, 25));
        btnChoice.setMinimumSize(new java.awt.Dimension(25, 25));
        btnChoice.setPreferredSize(new java.awt.Dimension(25, 25));
        btnChoice.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoice(evt);
            }
        });

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btnRemove.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnRemove.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovebtnRemov(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 555, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(240, 601));
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setViewportView(tree1);

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        centr.add(pan1, java.awt.BorderLayout.WEST);

        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
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
        tab2.setFillsViewportHeight(true);
        tab2.setRowHeight(68);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab2tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(1).setMinWidth(68);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(68);
            tab2.getColumnModel().getColumn(1).setMaxWidth(68);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        centr.add(pan2, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(460, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice

    }//GEN-LAST:event_btnChoice

    private void btnRemovebtnRemov(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovebtnRemov

    }//GEN-LAST:event_btnRemovebtnRemov

    private void tab2tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2tabMousePressed

    }//GEN-LAST:event_tab2tabMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRemove;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab2;
    private javax.swing.JTree tree1;
    // End of variables declaration//GEN-END:variables

    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree1.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        tree1.getSelectionModel().addTreeSelectionListener(tse -> selectionSys());
//        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent event) {
//                if (event.getValueIsAdjusting() == false) {
//                    selectionTab2();
//                }
//            }
//        });
    }
}
