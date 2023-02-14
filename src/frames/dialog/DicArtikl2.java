package frames.dialog;

import common.listener.ListenerFrame;
import frames.FrameToFile;
import frames.UGui;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import enums.TypeArtikl;
import java.util.Arrays;
import frames.swing.DefTableModel;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import common.listener.ListenerRecord;
import frames.ProgressBar;
import frames.swing.TableFieldFilter;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import startup.App;
import startup.Tex;

//Справочник артикулов
public class DicArtikl2 extends javax.swing.JDialog {

    private ListenerRecord listener = null;
    private Query qArtikl = new Query(eArtikl.id, eArtikl.level1, eArtikl.level2, eArtikl.code, eArtikl.name);
    private Query qArtiklAll = new Query(eArtikl.values());
    private Record artiklRec = null;
    private TreeNode[] selectedPath = null;

    public DicArtikl2(java.awt.Frame parent, ListenerRecord listenet, int... level) {
        super(parent, true);
        initComponents();
        initElements();
        String p1 = Arrays.toString(level).split("[\\[\\]]")[1];
        qArtiklAll.select(eArtikl.up, "where", eArtikl.level1, "in (", p1, ") order by", eArtikl.level1, ",", eArtikl.level2, ",", eArtikl.code, ",", eArtikl.name);
        this.listener = listenet;
        loadingModel();
        loadingTree();
        setVisible(true);
    }

    public DicArtikl2(java.awt.Frame parent, int id, ListenerRecord listenet, int... level) {
        super(parent, true);
        initComponents();
        initElements();
        String p1 = Arrays.toString(level).split("[\\[\\]]")[1];
        qArtiklAll.select(eArtikl.up, "where", eArtikl.level1, "in (", p1, ") order by", eArtikl.level1, ",", eArtikl.level2, ",", eArtikl.code, ",", eArtikl.name);
        if (id != -1) {
            artiklRec = qArtiklAll.find(id, eArtikl.id);
        }
        this.listener = listenet;
        loadingModel();
        loadingTree();
        setVisible(true);
    }

    public void loadingModel() {

        new DefTableModel(tab1, qArtikl, eArtikl.code, eArtikl.name);
    }

    public void loadingTree() {

        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("Мат. ценности");
        DefaultMutableTreeNode treeNode2 = null;
        for (TypeArtikl it : TypeArtikl.values()) {
            if (it.id1 == 1 && it.id2 == 0) {
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl.X100); //"Профили"

            } else if (it.id1 == 2 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl.X200); //"Аксессуары"

            } else if (it.id1 == 3 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl.X300); //"Погонаж"

            } else if (it.id1 == 4 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl.X400); //"Инструмент"

            } else if (it.id1 == 5 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl.X500); //"Заполнения"

            } else if (it.id1 == 6 && it.id2 == 0) {
                treeNode1.add(treeNode2);
                treeNode2 = new DefaultMutableTreeNode(TypeArtikl.X600); //"Наборы"                  

            } else if (it.id2 > 0) {   //остальное       
                treeNode1.add(treeNode2);
                DefaultMutableTreeNode defaultMutableTreeNode = new javax.swing.tree.DefaultMutableTreeNode(it);
                treeNode2.add(defaultMutableTreeNode);
                if (artiklRec != null && it.id1 == artiklRec.getInt(eArtikl.level1) && it.id2 == artiklRec.getInt(eArtikl.level2)) {
                    selectedPath = defaultMutableTreeNode.getPath();
                }
            }
        }
        treeNode1.add(treeNode2);
        tree.setModel(new DefaultTreeModel(treeNode1));
        scrTree.setViewportView(tree);
        if (selectedPath != null) {
            tree.setSelectionPath(new TreePath(selectedPath));
            UGui.setSelectedKey(tab1, artiklRec.getInt(eArtikl.id));
        } else {
            tree.setSelectionRow(0);
            UGui.setSelectedRow(tab1);
        }

    }

    public void selectionTree() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node != null) {
            qArtikl.clear();
            if (node.getUserObject() instanceof TypeArtikl == false) {
                qArtikl.addAll(qArtiklAll);

            } else if (node.isLeaf()) {
                TypeArtikl e = (TypeArtikl) node.getUserObject();
                qArtikl.addAll(qArtiklAll.stream().filter(rec -> rec.getInt(eArtikl.level1) == e.id1 && rec.getInt(eArtikl.level2) == e.id2).collect(toList()));

            } else {
                TypeArtikl e = (TypeArtikl) node.getUserObject();
                qArtikl.addAll(qArtiklAll.stream().filter(rec -> rec.getInt(eArtikl.level1) == e.id1).collect(toList()));
            }
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnArt = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        scrTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Справочник артикулов");
        setPreferredSize(new java.awt.Dimension(600, 600));

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(460, 29));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/hints/okno", common.eProp.locale); // NOI18N
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
        btnChoice.setToolTipText(bundle.getString("Выбрать")); // NOI18N
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
        btnRemove.setToolTipText(bundle.getString("Очистить")); // NOI18N
        btnRemove.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemov(evt);
            }
        });

        btnArt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c021.gif"))); // NOI18N
        btnArt.setToolTipText("Артикулы");
        btnArt.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArt.setFocusable(false);
        btnArt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnArt.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnArt.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtmn94(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnArt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 450, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnArt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(460, 500));
        centr.setLayout(new java.awt.BorderLayout());

        scrTree.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0), "Типы артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0,0)));
        scrTree.setPreferredSize(new java.awt.Dimension(200, 600));

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

        centr.add(scrTree, java.awt.BorderLayout.WEST);

        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0), "Список артикулов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.UGui.getFont(0,0)));
        scr1.setPreferredSize(new java.awt.Dimension(350, 600));

        tab1.setFont(frames.UGui.getFont(0,0));
        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Name 1", "Value 1"},
                {"Name 2", "Value 2"}
            },
            new String [] {
                "Код арикула", "Наименование артикула"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DicArtikl2.this.mouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                DicArtikl2.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(0).setMaxWidth(800);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(240);
            tab1.getColumnModel().getColumn(1).setMaxWidth(1600);
        }

        centr.add(scr1, java.awt.BorderLayout.CENTER);

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

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice

        Record record;
        UGui.stopCellEditing(tab1);
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            record = qArtikl.get(index);
        } else {
            JOptionPane.showMessageDialog(null, "Ни одна из записей не выбрана", "Предупреждение", JOptionPane.NO_OPTION);
            return;
//            record = eArtikl.up.newRecord();
//            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//            if (selectedNode.getUserObject() instanceof TypeArtikl == true) {
//                TypeArtikl e = (TypeArtikl) selectedNode.getUserObject();
//                record.set(eArtikl.level1, e.id1);
//                record.set(eArtikl.level2, e.id2);
//            }
        }
        listener.action(record);
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void btnRemov(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemov
        listener.action(new Record(Arrays.asList(null, null)));
        this.dispose();
    }//GEN-LAST:event_btnRemov

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, List.of(tab1));
    }//GEN-LAST:event_mousePressed

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        if (evt.getClickCount() == 2) {
            btnChoice(null);
        }
    }//GEN-LAST:event_mouseClicked

    private void btnArtmn94(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtmn94
        dispose();
        ProgressBar.create(DicArtikl2.this.getOwner(), new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Artikles.createFrame(DicArtikl2.this.getOwner(), listener);
            }
        });
    }//GEN-LAST:event_btnArtmn94

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArt;
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRemove;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scrTree;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    public javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    public void initElements() {

        btnRemove.setVisible(false);
        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);

        TableFieldFilter filterTable = new TableFieldFilter(0, tab1);
        south.add(filterTable, 0);
        filterTable.getTxt().grabFocus();

        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        tree.getSelectionModel().addTreeSelectionListener(tse -> selectionTree());
    }
}
