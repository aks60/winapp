package frames;

import dataset.Query;
import dataset.Record;
import domain.eSysmodel;
import java.awt.Component;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import builder.Wincalc;
import builder.script.GsonRoot;
import com.google.gson.Gson;
import frames.swing.DefMutableTreeNode;
import frames.swing.Canvas;
import java.awt.CardLayout;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import frames.swing.listener.ListenerRecord;
import frames.swing.listener.ListenerFrame;
import dataset.Conn;
import frames.swing.DefTableModel;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

public class Models extends javax.swing.JFrame implements ListenerFrame<Object, Object> {

    public Wincalc iwin = new Wincalc();
    private Window owner = null;
    private ListenerRecord listenet = null;
    private Canvas canvas = new Canvas();
    private Query qModels = new Query(eSysmodel.values());

    public Models() {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        btnChoice.setVisible(false);
        btnRemov.setVisible(false);
        loadingTab(tab1, 1001);
    }

    public Models(java.awt.Window owner, ListenerRecord listener) {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        this.owner = owner;
        this.listenet = listener;
        owner.setEnabled(false);
        loadingTab(tab1, 1001);
    }

    private void loadingData() {
        //
    }

    private void loadingModel() {
        panDesign.add(canvas, java.awt.BorderLayout.CENTER);
        canvas.setVisible(true);
        new DefTableModel(tab1, qModels, eSysmodel.npp, eSysmodel.name, eSysmodel.id);
        tab1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Object v = qModels.get(row).get(eSysmodel.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
        new DefTableModel(tab2, qModels, eSysmodel.npp, eSysmodel.name, eSysmodel.id);
        tab2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Object v = qModels.get(row).get(eSysmodel.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
        new DefTableModel(tab3, qModels, eSysmodel.npp, eSysmodel.name, eSysmodel.id);
        tab3.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Object v = qModels.get(row).get(eSysmodel.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
    }

    private void loadingTab(JTable tab, int form) {
        qModels.select(eSysmodel.up, "where", eSysmodel.form, "=", form);
        DefaultTableModel dm = (DefaultTableModel) tab.getModel();
        dm.getDataVector().removeAllElements();
        for (Record record : qModels.table(eSysmodel.up)) {
            try {
                String script = record.getStr(eSysmodel.script);
                Wincalc iwin2 = new Wincalc(script);
                iwin2.imageIcon = Canvas.createIcon(iwin2, 68);
                record.add(iwin2);

            } catch (Exception e) {
                System.err.println("Ошибка:Models.loadingTab() " + e);
            }
        }
        ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
        UGui.updateBorderAndSql(tab, Arrays.asList(tab1, tab2, tab3));
        UGui.setSelectedRow(tab);
    }

    private void loadingWin() {
        try {
            DefMutableTreeNode root = UGui.loadWinTree(iwin);
            tree.setModel(new DefaultTreeModel(root));
            UGui.expandTree(tree, new TreePath(root), true);
            tree.setSelectionRow(0);

        } catch (Exception e) {
            System.err.println("Ошибка: Systree.loadingWin() " + e);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record record = qModels.get(index);
            Object v = record.get(eSysmodel.values().length);
            if (v instanceof Wincalc) { //прорисовка окна               
                iwin = (Wincalc) v;
                canvas.init(iwin);
                canvas.draw();
            }
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int index = UGui.getIndexRec(tab2);
        if (index != -1) {
            Record record = qModels.get(index);
            Object v = record.get(eSysmodel.values().length);
            if (v instanceof Wincalc) { //прорисовка окна               
                iwin = (Wincalc) v;
                canvas.init(iwin);
                canvas.draw();
            }
        }
    }

    private void selectionTab3(ListSelectionEvent event) {
        int index = UGui.getIndexRec(tab3);
        if (index != -1) {
            Record record = qModels.get(index);
            Object v = record.get(eSysmodel.values().length);
            if (v instanceof Wincalc) { //прорисовка окна               
                iwin = (Wincalc) v;
                canvas.init(iwin);
                canvas.draw();
            }
        }
    }

    private void selectionTree() {

//        DefMutableTreeNode selectedNode = (DefMutableTreeNode) tree.getLastSelectedPathComponent();
//        if (selectedNode != null) {
//            if (selectedNode.com5t().type() == enums.Type.RECTANGL || selectedNode.com5t().type() == enums.Type.ARCH) {
//                ((CardLayout) pan6.getLayout()).show(pan6, "pan19");
//
//            } else if (selectedNode.com5t().type() == enums.Type.AREA) {
//                ((CardLayout) pan6.getLayout()).show(pan6, "pan20");
//
//            } else if (selectedNode.com5t().type() == enums.Type.FRAME_SIDE) {
//                ((CardLayout) pan6.getLayout()).show(pan6, "pan21");
//
//            } else if (selectedNode.com5t().type() == enums.Type.STVORKA) {
//                ((CardLayout) pan6.getLayout()).show(pan6, "pan22");
//
//            } else if (selectedNode.com5t().type() == enums.Type.IMPOST
//                    || selectedNode.com5t().type() == enums.Type.STOIKA
//                    || selectedNode.com5t().type() == enums.Type.SHTULP) {
//                ((CardLayout) pan6.getLayout()).show(pan6, "pan23");
//
//            } else if (selectedNode.com5t().type() == enums.Type.GLASS) {
//                ((CardLayout) pan6.getLayout()).show(pan6, "pan24");
//            }
//        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemov = new javax.swing.JButton();
        btnT1 = new javax.swing.JToggleButton();
        btnT2 = new javax.swing.JToggleButton();
        btnT3 = new javax.swing.JToggleButton();
        btnT4 = new javax.swing.JToggleButton();
        west = new javax.swing.JPanel();
        pan13 = new javax.swing.JPanel();
        pan16 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan14 = new javax.swing.JPanel();
        pan25 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan15 = new javax.swing.JPanel();
        pan26 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan18 = new javax.swing.JPanel();
        scrTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        centr = new javax.swing.JPanel();
        pan17 = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        panDesign = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        lab4 = new javax.swing.JLabel();
        txtField4 = new javax.swing.JFormattedTextField();
        lab5 = new javax.swing.JLabel();
        txtField5 = new javax.swing.JFormattedTextField();
        pan8 = new javax.swing.JPanel();
        pan9 = new javax.swing.JPanel();
        pan10 = new javax.swing.JPanel();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Модели конструкций");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Models.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));
        north.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panMouseClicked(evt);
            }
        });

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

        btnChoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c044.gif"))); // NOI18N
        btnChoice.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnChoice.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnChoice.setFocusable(false);
        btnChoice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChoice.setMaximumSize(new java.awt.Dimension(25, 25));
        btnChoice.setMinimumSize(new java.awt.Dimension(25, 25));
        btnChoice.setPreferredSize(new java.awt.Dimension(25, 25));
        btnChoice.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnChoice.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoice(evt);
            }
        });

        btnRemov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btnRemov.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnRemov.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemov.setFocusable(false);
        btnRemov.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemov.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemov.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemov.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemov.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnRemov.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovresh(evt);
            }
        });

        buttonGroup.add(btnT1);
        btnT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c003.gif"))); // NOI18N
        btnT1.setSelected(true);
        btnT1.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT1.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT1.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnT2);
        btnT2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c004.gif"))); // NOI18N
        btnT2.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT2.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT2.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnT3);
        btnT3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c005.gif"))); // NOI18N
        btnT3.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT3.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT3.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnT4);
        btnT4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c056.gif"))); // NOI18N
        btnT4.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT4.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT4.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
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
                .addGap(67, 67, 67)
                .addComponent(btnT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 319, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChoice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemov, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        west.setPreferredSize(new java.awt.Dimension(200, 560));
        west.setLayout(new java.awt.CardLayout());

        pan13.setName(""); // NOI18N
        pan13.setPreferredSize(new java.awt.Dimension(200, 560));
        pan13.setLayout(new java.awt.BorderLayout());

        pan16.setPreferredSize(new java.awt.Dimension(200, 560));
        pan16.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scr1.setPreferredSize(new java.awt.Dimension(200, 560));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх1", "123"},
                {"99", "мммммммммм1", "321"}
            },
            new String [] {
                "№", "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setRowHeight(68);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setResizable(false);
            tab1.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(2).setResizable(false);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(68);
        }

        pan16.add(scr1, java.awt.BorderLayout.CENTER);

        pan13.add(pan16, java.awt.BorderLayout.CENTER);

        west.add(pan13, "pan13");

        pan14.setName(""); // NOI18N
        pan14.setPreferredSize(new java.awt.Dimension(200, 560));
        pan14.setLayout(new java.awt.BorderLayout());

        pan25.setPreferredSize(new java.awt.Dimension(200, 560));
        pan25.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scr2.setPreferredSize(new java.awt.Dimension(200, 560));
        scr2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "№", "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setRowHeight(68);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setResizable(false);
            tab2.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(2).setResizable(false);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(68);
        }

        pan25.add(scr2, java.awt.BorderLayout.CENTER);

        pan14.add(pan25, java.awt.BorderLayout.CENTER);

        west.add(pan14, "pan14");

        pan15.setName(""); // NOI18N
        pan15.setPreferredSize(new java.awt.Dimension(200, 560));
        pan15.setLayout(new java.awt.BorderLayout());

        pan26.setPreferredSize(new java.awt.Dimension(200, 560));
        pan26.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scr3.setPreferredSize(new java.awt.Dimension(200, 560));
        scr3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "№", "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setRowHeight(68);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setResizable(false);
            tab3.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab3.getColumnModel().getColumn(2).setResizable(false);
            tab3.getColumnModel().getColumn(2).setPreferredWidth(68);
        }

        pan26.add(scr3, java.awt.BorderLayout.CENTER);

        pan15.add(pan26, java.awt.BorderLayout.CENTER);

        west.add(pan15, "pan15");

        pan18.setPreferredSize(new java.awt.Dimension(200, 560));
        pan18.setLayout(new java.awt.BorderLayout());

        scrTree.setBorder(null);
        scrTree.setPreferredSize(new java.awt.Dimension(200, 560));
        scrTree.setViewportView(tree);

        pan18.add(scrTree, java.awt.BorderLayout.CENTER);

        west.add(pan18, "pan18");

        getContentPane().add(west, java.awt.BorderLayout.WEST);

        centr.setPreferredSize(new java.awt.Dimension(600, 560));
        centr.setLayout(new java.awt.BorderLayout());

        pan17.setPreferredSize(new java.awt.Dimension(600, 500));
        pan17.setLayout(new java.awt.BorderLayout());

        pan4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan4.setPreferredSize(new java.awt.Dimension(400, 430));
        pan4.setLayout(new java.awt.BorderLayout());

        panDesign.setLayout(new java.awt.BorderLayout());
        pan4.add(panDesign, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(700, 40));

        lab4.setText("INDEX");
        lab4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab4.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        lab5.setText("INDEX");
        lab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab5.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(321, Short.MAX_VALUE))
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan4.add(pan7, java.awt.BorderLayout.NORTH);

        pan8.setPreferredSize(new java.awt.Dimension(744, 10));

        javax.swing.GroupLayout pan8Layout = new javax.swing.GroupLayout(pan8);
        pan8.setLayout(pan8Layout);
        pan8Layout.setHorizontalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );
        pan8Layout.setVerticalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        pan4.add(pan8, java.awt.BorderLayout.SOUTH);

        pan9.setPreferredSize(new java.awt.Dimension(10, 376));

        javax.swing.GroupLayout pan9Layout = new javax.swing.GroupLayout(pan9);
        pan9.setLayout(pan9Layout);
        pan9Layout.setHorizontalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan9Layout.setVerticalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 433, Short.MAX_VALUE)
        );

        pan4.add(pan9, java.awt.BorderLayout.EAST);

        pan10.setPreferredSize(new java.awt.Dimension(10, 336));

        javax.swing.GroupLayout pan10Layout = new javax.swing.GroupLayout(pan10);
        pan10.setLayout(pan10Layout);
        pan10Layout.setHorizontalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan10Layout.setVerticalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 433, Short.MAX_VALUE)
        );

        pan4.add(pan10, java.awt.BorderLayout.WEST);

        pan17.add(pan4, java.awt.BorderLayout.CENTER);

        centr.add(pan17, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 701, Short.MAX_VALUE)
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

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        loadingTab(tab1, 1001);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0 && tab1.getSelectedRow() != -1) {
                //iwin.rootArea = null;
                //canvas.paint(canvas.getGraphics());
                UGui.deleteRecord(tab1);
            }
        } else if (tab2.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0 && tab2.getSelectedRow() != -1) {
                //iwin.rootArea = null;
                //canvas.paint(canvas.getGraphics());
                UGui.deleteRecord(tab2);
            }
        } else if (tab3.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0 && tab3.getSelectedRow() != -1) {
                //iwin.rootArea = null;
                //canvas.paint(canvas.getGraphics());
                UGui.deleteRecord(tab3);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        try {
            Object prj = JOptionPane.showInputDialog(Models.this, "Номер проекта", "Проект", JOptionPane.QUESTION_MESSAGE);
            String json = builder.script.Winscript.test(Integer.valueOf(prj.toString()), true);
            GsonRoot win = new Gson().fromJson(json, GsonRoot.class);

//            JFileChooser chooser = new JFileChooser();
//            chooser.setCurrentDirectory(new File("."));
//            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//            chooser.setAcceptAllFileFilterUsed(false);
//            FileNameExtensionFilter filter = new FileNameExtensionFilter("Json (формат json)", "json");
//            chooser.setFileFilter(filter);
//            if (chooser.showDialog(this, "Выбрать") == JFileChooser.CANCEL_OPTION) {
//                return;
//            }
//            String path = chooser.getSelectedFile().getPath();
//            JsonReader reader = new JsonReader(new FileReader(path));
//            GsonRoot win = new Gson().fromJson(reader, GsonRoot.class);
//            String json = new Gson().toJson(win);
//            reader.close();            
            Record record = eSysmodel.up.newRecord(Query.INS);
            record.set(eSysmodel.id, Conn.instanc().genId(eSysmodel.up));
            record.set(eSysmodel.npp, qModels.size());
            record.set(eSysmodel.name, "<html>" + win.prj + " " + win.name);
            record.set(eSysmodel.script, json);

            if (tab1.getBorder() != null) {
                record.set(eSysmodel.form, 1001);
            } else if (tab2.getBorder() != null) {
                record.set(eSysmodel.form, 1004);
            } else if (tab3.getBorder() != null) {
                record.set(eSysmodel.form, 1002);
            }
            qModels.insert(record);
            if (tab1.getBorder() != null) {
                loadingTab(tab1, 1001);
                UGui.setSelectedIndex(tab1, qModels.size() - 1);
                UGui.scrollRectToIndex(qModels.size() - 1, tab1);
            } else if (tab2.getBorder() != null) {
                loadingTab(tab2, 1004);
                UGui.setSelectedIndex(tab2, qModels.size() - 1);
                UGui.scrollRectToIndex(qModels.size() - 1, tab2);
            } else if (tab3.getBorder() != null) {
                loadingTab(tab3, 1002);
                UGui.setSelectedIndex(tab3, qModels.size() - 1);
                UGui.scrollRectToIndex(qModels.size() - 1, tab3);
            }
        } catch (Exception e) {
            System.out.println("Ошибка:Models.btnInsert()");
        }
    }//GEN-LAST:event_btnInsert

    private void panMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panMouseClicked
        System.out.println(evt.getX() + " " + evt.getY());
    }//GEN-LAST:event_panMouseClicked

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice
        JTable table = null;
        if (btnT1.isSelected()) {
            table = tab1;
        } else if (btnT2.isSelected()) {
            table = tab2;
        } else if (btnT3.isSelected()) {
            table = tab3;
        }
        int index = UGui.getIndexRec(table);
        if (index != -1) {
            Record record = new Record();
            record.add(qModels.get(index, eSysmodel.id));
            record.add(qModels.get(index, eSysmodel.name));
            record.add(qModels.get(index, eSysmodel.script));
            listenet.action(record);
        }
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

    private void btnRemovresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovresh

    }//GEN-LAST:event_btnRemovresh

    private void btnToggl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToggl
        if (btnT1.isSelected()) {
            loadingTab(tab1, 1001);
            ((CardLayout) west.getLayout()).show(west, "pan13");
            UGui.updateBorderAndSql(tab1, Arrays.asList(tab1, tab2, tab3));
            UGui.setSelectedRow(tab1);
        } else if (btnT2.isSelected()) {
            loadingTab(tab2, 1004);
            ((CardLayout) west.getLayout()).show(west, "pan14");
            UGui.updateBorderAndSql(tab2, Arrays.asList(tab1, tab2, tab3));
            UGui.setSelectedRow(tab2);
        } else if (btnT3.isSelected()) {
            loadingTab(tab3, 1002);
            ((CardLayout) west.getLayout()).show(west, "pan15");
            UGui.updateBorderAndSql(tab3, Arrays.asList(tab1, tab2, tab3));
            UGui.setSelectedRow(tab3);
        } else {
            loadingWin();
            ((CardLayout) west.getLayout()).show(west, "pan18");
        }
    }//GEN-LAST:event_btnToggl

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, Arrays.asList(tab1, tab2, tab3));
    }//GEN-LAST:event_tabMousePressed

    private void tabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMouseClicked
        if (evt.getClickCount() == 2) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tabMouseClicked

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnRemov;
    private javax.swing.JToggleButton btnT1;
    private javax.swing.JToggleButton btnT2;
    private javax.swing.JToggleButton btnT3;
    private javax.swing.JToggleButton btnT4;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel centr;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab5;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan14;
    private javax.swing.JPanel pan15;
    private javax.swing.JPanel pan16;
    private javax.swing.JPanel pan17;
    private javax.swing.JPanel pan18;
    private javax.swing.JPanel pan25;
    private javax.swing.JPanel pan26;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JPanel panDesign;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scrTree;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTree tree;
    private javax.swing.JFormattedTextField txtField4;
    private javax.swing.JFormattedTextField txtField5;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    private void initElements() {

        new FrameToFile(this, btnClose);
        DefaultTreeCellRenderer rnd = (DefaultTreeCellRenderer) tree.getCellRenderer();
        rnd.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b037.gif")));
        rnd.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b007.gif")));
        rnd.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b006.gif")));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab2(event);
                }
            }
        });
        tab3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab3(event);
                }
            }
        });
        tree.getSelectionModel().addTreeSelectionListener(tse -> selectionTree());
    }
}
