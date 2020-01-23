package forms;

import common.FrameListener;
import dataset.Query;
import dataset.Record;
import domain.eSystree;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import wincalc.script.AreaRoot;
import swing.DefTableModel;

public class Design extends javax.swing.JFrame {

    private Query qSystree = new Query(eSystree.values()).select(eSystree.up);
    private FrameListener<Object, Object> listenerModify = new FrameListener() {

        Icon[] btnIM = {new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c020.gif")),
            new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"))};

        public void request(Object obj) {
            btnSave.setIcon(btnIM[0]);
        }

        public void response(Object obj) {
            btnSave.setIcon(btnIM[1]);
        }
    };
    private AreaRoot rootArea;

    public Design() {
        initComponents();
        initElements();
        
        DefTableModel rsmSystree = new DefTableModel(new JTable(), qSystree, eSystree.id);
        //loadTree();
    }

    public void loadTree(String name) {
        
        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("Дерево системы профилей");
        ArrayList<DefaultMutableTreeNode> treeList = new ArrayList();
        Query q = qSystree.table(eSystree.up.tname());
        for (Record record : q) {
            if (record.getInt(eSystree.parent_id) == record.getInt(eSystree.id)) {
                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(new Design.UserNode(record));
                treeList.add(node2);
                treeNode1.add(node2);
            }
        }
        ArrayList<DefaultMutableTreeNode> treeList2 = addChild(treeList, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList3 = addChild(treeList2, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList4 = addChild(treeList3, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList5 = addChild(treeList4, new ArrayList());
        ArrayList<DefaultMutableTreeNode> treeList6 = addChild(treeList5, new ArrayList());
        tree1.setModel(new DefaultTreeModel(treeNode1));
        scr1.setViewportView(tree1);
        tree1.setSelectionRow(0);
    }

    private ArrayList<DefaultMutableTreeNode> addChild(ArrayList<DefaultMutableTreeNode> nodeList1, ArrayList<DefaultMutableTreeNode> nodeList2) {

        Query q = qSystree.table(eSystree.up.tname());
        for (DefaultMutableTreeNode node : nodeList1) {
            Design.UserNode userNode = (Design.UserNode) node.getUserObject();
            for (Record record2 : q) {
                if (record2.getInt(eSystree.parent_id) == userNode.record.getInt(eSystree.id)
                        && record2.getInt(eSystree.parent_id) != record2.getInt(eSystree.id)) {
                    DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(new Design.UserNode(record2));
                    node.add(node2);
                    nodeList2.add(node2);
                }
            }
        }
        return nodeList2;
    }

    private void selectioTree(ListSelectionEvent event) {

    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        centr = new javax.swing.JPanel();
        south = new javax.swing.JPanel();
        west = new javax.swing.JPanel();
        east = new javax.swing.JPanel();
        pan1a = new javax.swing.JTabbedPane();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tree1 = new javax.swing.JTree();
        pan13 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tree2 = new javax.swing.JTree();
        pan2 = new javax.swing.JPanel();
        pan3 = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        design = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        pan8 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        txt1 = new javax.swing.JFormattedTextField();
        lab2 = new javax.swing.JLabel();
        txt2 = new javax.swing.JFormattedTextField();
        pan9 = new javax.swing.JPanel();
        pan10 = new javax.swing.JPanel();
        pan6 = new javax.swing.JPanel();
        btnSquare = new javax.swing.JButton();
        btnArch3 = new javax.swing.JButton();
        btnArch2 = new javax.swing.JButton();
        btnTrapeze = new javax.swing.JButton();
        btnTrapeze2 = new javax.swing.JButton();
        btnSquare1 = new javax.swing.JButton();
        pan12 = new javax.swing.JPanel();
        btnImpostVert = new javax.swing.JButton();
        btnImpostGoriz = new javax.swing.JButton();
        btnSquare4 = new javax.swing.JButton();
        btnSquare5 = new javax.swing.JButton();
        pan11 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout centrLayout = new javax.swing.GroupLayout(centr);
        centr.setLayout(centrLayout);
        centrLayout.setHorizontalGroup(
            centrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
        centrLayout.setVerticalGroup(
            centrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout westLayout = new javax.swing.GroupLayout(west);
        west.setLayout(westLayout);
        westLayout.setHorizontalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        westLayout.setVerticalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout eastLayout = new javax.swing.GroupLayout(east);
        east.setLayout(eastLayout);
        eastLayout.setHorizontalGroup(
            eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        eastLayout.setVerticalGroup(
            eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Конструктор окна");
        setPreferredSize(new java.awt.Dimension(800, 600));

        pan1a.setPreferredSize(new java.awt.Dimension(260, 5));

        pan1.setPreferredSize(new java.awt.Dimension(240, 654));
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setViewportView(tree1);

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        pan1a.addTab("Дерево системы", pan1);

        pan13.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);
        scr2.setViewportView(tree2);

        pan13.add(scr2, java.awt.BorderLayout.CENTER);

        pan1a.addTab("Дерево окна", pan13);

        getContentPane().add(pan1a, java.awt.BorderLayout.WEST);

        pan2.setPreferredSize(new java.awt.Dimension(560, 585));
        pan2.setLayout(new java.awt.BorderLayout());

        pan3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan3.setMinimumSize(new java.awt.Dimension(100, 20));
        pan3.setPreferredSize(new java.awt.Dimension(800, 40));

        javax.swing.GroupLayout pan3Layout = new javax.swing.GroupLayout(pan3);
        pan3.setLayout(pan3Layout);
        pan3Layout.setHorizontalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 624, Short.MAX_VALUE)
        );
        pan3Layout.setVerticalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        pan2.add(pan3, java.awt.BorderLayout.SOUTH);

        pan4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan4.setLayout(new java.awt.BorderLayout());

        design.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 255, 255)));

        javax.swing.GroupLayout designLayout = new javax.swing.GroupLayout(design);
        design.setLayout(designLayout);
        designLayout.setHorizontalGroup(
            designLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );
        designLayout.setVerticalGroup(
            designLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 461, Short.MAX_VALUE)
        );

        pan4.add(design, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(700, 60));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        pan4.add(pan7, java.awt.BorderLayout.NORTH);

        pan8.setPreferredSize(new java.awt.Dimension(744, 60));

        lab1.setText("Ширина");
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txt1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txt1.setText("1200");

        lab2.setText("Высота");
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txt2.setText("1800");

        javax.swing.GroupLayout pan8Layout = new javax.swing.GroupLayout(pan8);
        pan8.setLayout(pan8Layout);
        pan8Layout.setHorizontalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan8Layout.createSequentialGroup()
                        .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan8Layout.createSequentialGroup()
                        .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(464, 464, 464))
        );
        pan8Layout.setVerticalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab1)
                    .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab2)
                    .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan4.add(pan8, java.awt.BorderLayout.SOUTH);

        pan9.setPreferredSize(new java.awt.Dimension(60, 376));

        javax.swing.GroupLayout pan9Layout = new javax.swing.GroupLayout(pan9);
        pan9.setLayout(pan9Layout);
        pan9Layout.setHorizontalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        pan9Layout.setVerticalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
        );

        pan4.add(pan9, java.awt.BorderLayout.EAST);

        pan10.setPreferredSize(new java.awt.Dimension(60, 336));

        javax.swing.GroupLayout pan10Layout = new javax.swing.GroupLayout(pan10);
        pan10.setLayout(pan10Layout);
        pan10Layout.setHorizontalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        pan10Layout.setVerticalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
        );

        pan4.add(pan10, java.awt.BorderLayout.WEST);

        pan2.add(pan4, java.awt.BorderLayout.CENTER);

        pan6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan6.setPreferredSize(new java.awt.Dimension(38, 500));
        pan6.setLayout(new javax.swing.BoxLayout(pan6, javax.swing.BoxLayout.Y_AXIS));

        btnSquare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare.setName("areaSquare"); // NOI18N
        btnSquare.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        pan6.add(btnSquare);

        btnArch3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d030.gif"))); // NOI18N
        btnArch3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch3.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch3.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch3.setName("areaArch"); // NOI18N
        btnArch3.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        pan6.add(btnArch3);

        btnArch2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d031.gif"))); // NOI18N
        btnArch2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch2.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch2.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch2.setName("areaArch2"); // NOI18N
        btnArch2.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        pan6.add(btnArch2);

        btnTrapeze.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d028.gif"))); // NOI18N
        btnTrapeze.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze.setName("areaTrapeze"); // NOI18N
        btnTrapeze.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        pan6.add(btnTrapeze);

        btnTrapeze2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d027.gif"))); // NOI18N
        btnTrapeze2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze2.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.setName("areaTrapeze2"); // NOI18N
        btnTrapeze2.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        pan6.add(btnTrapeze2);

        btnSquare1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d024.gif"))); // NOI18N
        btnSquare1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare1.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare1.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare1.setName("areaSquare"); // NOI18N
        btnSquare1.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        pan6.add(btnSquare1);

        pan2.add(pan6, java.awt.BorderLayout.EAST);

        pan12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan12.setPreferredSize(new java.awt.Dimension(38, 500));
        pan12.setLayout(new javax.swing.BoxLayout(pan12, javax.swing.BoxLayout.Y_AXIS));

        btnImpostVert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d023.gif"))); // NOI18N
        btnImpostVert.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostVert.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostVert.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostVert.setName("impostVert"); // NOI18N
        btnImpostVert.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostVert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElem(evt);
            }
        });
        pan12.add(btnImpostVert);

        btnImpostGoriz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d022.gif"))); // NOI18N
        btnImpostGoriz.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostGoriz.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.setName("impostGoriz"); // NOI18N
        btnImpostGoriz.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElem(evt);
            }
        });
        pan12.add(btnImpostGoriz);

        btnSquare4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare4.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare4.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare4.setName("areaSquare"); // NOI18N
        btnSquare4.setPreferredSize(new java.awt.Dimension(32, 32));
        pan12.add(btnSquare4);

        btnSquare5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare5.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare5.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare5.setName("areaSquare"); // NOI18N
        btnSquare5.setPreferredSize(new java.awt.Dimension(32, 32));
        pan12.add(btnSquare5);

        pan2.add(pan12, java.awt.BorderLayout.WEST);

        getContentPane().add(pan2, java.awt.BorderLayout.CENTER);

        pan11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan11.setMaximumSize(new java.awt.Dimension(32767, 31));
        pan11.setPreferredSize(new java.awt.Dimension(800, 29));

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

        javax.swing.GroupLayout pan11Layout = new javax.swing.GroupLayout(pan11);
        pan11.setLayout(pan11Layout);
        pan11Layout.setHorizontalGroup(
            pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 721, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan11Layout.setVerticalGroup(
            pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan11Layout.createSequentialGroup()
                .addGroup(pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(pan11, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
             pan10.setPreferredSize(new Dimension(100, 400));
             pan4.repaint();
             this.setVisible(true);
    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave
      pan4.paintComponents(pan4.getGraphics());
      this.setVisible(true);
    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArea
        JButton btn = (JButton) evt.getSource();
        loadTree(btn.getName());
    }//GEN-LAST:event_btnArea

    private void btnElem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElem
        JButton btn = (JButton) evt.getSource();
        //addTree(btn.getName());
    }//GEN-LAST:event_btnElem

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArch2;
    private javax.swing.JButton btnArch3;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnImpostGoriz;
    private javax.swing.JButton btnImpostVert;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSquare;
    private javax.swing.JButton btnSquare1;
    private javax.swing.JButton btnSquare4;
    private javax.swing.JButton btnSquare5;
    private javax.swing.JButton btnTrapeze;
    private javax.swing.JButton btnTrapeze2;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel design;
    private javax.swing.JPanel east;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan12;
    private javax.swing.JPanel pan13;
    private javax.swing.JTabbedPane pan1a;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JPanel south;
    private javax.swing.JTree tree1;
    private javax.swing.JTree tree2;
    private javax.swing.JFormattedTextField txt1;
    private javax.swing.JFormattedTextField txt2;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables

    private void initElements() {

        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree1.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
    } 
// </editor-fold> 
    
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
