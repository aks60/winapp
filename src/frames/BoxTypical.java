package frames;

import common.DialogListener;
import common.FrameListener;
import common.FrameToFile;
import common.eProperty;
import dataset.Query;
import dataset.Record;
import domain.eModels;
import enums.TypeElem;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import builder.Wincalc;
import builder.model.ElemSimple;
import builder.model.PaintPanel;
import builder.script.Mediate;
import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class BoxTypical extends javax.swing.JFrame implements FrameListener<Object, Object> {

    public Wincalc iwinMax = new Wincalc();
    public Wincalc iwinMin = new Wincalc();
    private Window owner = null;
    private int nuni = -1;
    private ArrayList<Icon> listIcon1 = new ArrayList<Icon>();
    private ArrayList<Icon> listIcon2 = new ArrayList<Icon>();
    private DialogListener listenet = null;
    private PaintPanel paintPanel = new PaintPanel(iwinMax) {

        public void actionResponse(MouseEvent evt) {
            ElemSimple elem5e = iwinMax.listElem.stream().filter(el -> el.mouseClick(evt.getX(), evt.getY())).findFirst().orElse(null);
            if (elem5e != null) {
                txtField5.setText(String.valueOf(elem5e.id()));
                repaint();
            }
        }
    };
    private Query qModels1 = new Query(eModels.values());
    private Query qModels2 = new Query(eModels.values());

    public BoxTypical() {
        initComponents();
        initElements();
        loadingModel();
        btnChoice.setVisible(false);
        btnRemov.setVisible(false);
        loadingData();
        //loadingTree();
    }

    public BoxTypical(java.awt.Window owner, DialogListener listener) {
        initComponents();
        initElements();
        loadingModel();
        this.owner = owner;
        this.listenet = listener;
        owner.setEnabled(false);
        loadingData();
        //loadingTree();

    }

    private void loadingData() {

        qModels1.select(eModels.up, "where", eModels.form, "=", TypeElem.RECTANGL.id, "order by", eModels.npp);
        qModels2.select(eModels.up, "where", eModels.form, "=", TypeElem.ARCH.id, "order by", eModels.npp);
        DefaultTableModel dm1 = (DefaultTableModel) tab1.getModel();
        DefaultTableModel dm2 = (DefaultTableModel) tab2.getModel();
        dm1.getDataVector().removeAllElements();
        dm2.getDataVector().removeAllElements();
        int length = 68;
        for (Record record : qModels1) {
            try {
                Object obj[] = {record.get(eModels.npp), record.get(eModels.name), ""};
                Object script = record.get(eModels.script);
                iwinMin.build(script.toString());
                BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
                iwinMin.gc2d = bi.createGraphics();
                iwinMin.gc2d.fillRect(0, 0, length, length);
                iwinMin.scale = (length / iwinMin.width > length / iwinMin.heightAdd) ? length / (iwinMin.heightAdd + 200) : length / (iwinMin.width + 200);
                iwinMin.gc2d.translate(2, 2);
                iwinMin.gc2d.scale(iwinMin.scale, iwinMin.scale);
                iwinMin.rootArea.draw(length, length);
                ImageIcon image = new ImageIcon(bi);
                listIcon1.add(image);
                dm1.addRow(obj);
            } catch (Exception e) {
                System.err.println("Ошибка " + e);
            }
        }
        for (Record record : qModels2) {
            try {
                Object obj[] = {record.get(eModels.npp), record.get(eModels.name), ""};
                Object script = record.get(eModels.script);
                iwinMin.build(script.toString());
                BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
                iwinMin.gc2d = bi.createGraphics();
                iwinMin.gc2d.fillRect(0, 0, length, length);
                iwinMin.scale = (length / iwinMin.width > length / iwinMin.heightAdd) ? length / (iwinMin.heightAdd + 200) : length / (iwinMin.width + 200);
                iwinMin.gc2d.translate(2, 2);
                iwinMin.gc2d.scale(iwinMin.scale, iwinMin.scale);
                iwinMin.rootArea.draw(length, length);
                ImageIcon image = new ImageIcon(bi);
                listIcon2.add(image);
                dm2.addRow(obj);
            } catch (Exception e) {
                System.err.println("Ошибка " + e);
            }
        }
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab2);
        Util.setSelectedRow(tab1);
    }

    private void loadingTree() {
        try {
            Mediate mdtFirst = iwinMax.mediateList.getFirst();
            DefMutableTreeNode root = new DefMutableTreeNode(mdtFirst);
            DefMutableTreeNode node = null;
            for (Mediate mdt : iwinMax.mediateList) {
                Enumeration<TreeNode> e = root.depthFirstEnumeration();
                while (e.hasMoreElements()) {
                    DefMutableTreeNode node2 = (DefMutableTreeNode) e.nextElement();
                    if (mdt.owner != null && node2.record.id == mdt.owner.id) {
                        node = new DefMutableTreeNode(mdt);
                        node2.add(node);
                    }
                }
            }
            tree.setModel(new DefaultTreeModel(root));
            scrTree.setViewportView(tree);
            tree.setSelectionRow(0);

        } catch (Exception e) {
            System.err.println("Ошибка frames.BoxTypical.loadingTree() " + e);
        }
    }

    private void selectionTree() {

        DefMutableTreeNode selectedNode = (DefMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (selectedNode.record.type == TypeElem.RECTANGL || selectedNode.record.type == TypeElem.ARCH) {
                ((CardLayout) pan6.getLayout()).show(pan6, "pan19");

            } else if (selectedNode.record.type == TypeElem.AREA) {
                ((CardLayout) pan6.getLayout()).show(pan6, "pan20");

            } else if (selectedNode.record.type == TypeElem.FRAME_SIDE) {
                ((CardLayout) pan6.getLayout()).show(pan6, "pan21");

            } else if (selectedNode.record.type == TypeElem.STVORKA) {
                ((CardLayout) pan6.getLayout()).show(pan6, "pan22");

            } else if (selectedNode.record.type == TypeElem.IMPOST) {
                ((CardLayout) pan6.getLayout()).show(pan6, "pan23");

            } else if (selectedNode.record.type == TypeElem.GLASS) {
                ((CardLayout) pan6.getLayout()).show(pan6, "pan24");

            }
        }
    }

    private void loadingModel() {
        panDesign.add(paintPanel, java.awt.BorderLayout.CENTER);
        paintPanel.setVisible(true);
        tab1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Icon icon = listIcon1.get(row);
                    label.setIcon(icon);
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
        tab2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Icon icon = listIcon2.get(row);
                    label.setIcon(icon);
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Object script = qModels1.get(row, eModels.script);
            iwinMax.build(script.toString());
            paintPanel.repaint(true);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab2);
        if (row != -1) {
            Object script = qModels2.get(row, eModels.script);
            iwinMax.build(script.toString());
            paintPanel.repaint(true);
        }
    }

    private void selectionTab3(ListSelectionEvent event) {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
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
        pan3 = new javax.swing.JPanel();
        lab2 = new javax.swing.JLabel();
        txtField2 = new javax.swing.JFormattedTextField();
        lab1 = new javax.swing.JLabel();
        txtField1 = new javax.swing.JFormattedTextField();
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
        pan6 = new javax.swing.JPanel();
        pan19 = new javax.swing.JPanel();
        lab19 = new javax.swing.JLabel();
        pan20 = new javax.swing.JPanel();
        lab20 = new javax.swing.JLabel();
        pan21 = new javax.swing.JPanel();
        lab21 = new javax.swing.JLabel();
        pan22 = new javax.swing.JPanel();
        lab22 = new javax.swing.JLabel();
        pan23 = new javax.swing.JPanel();
        lab23 = new javax.swing.JLabel();
        pan24 = new javax.swing.JPanel();
        lab24 = new javax.swing.JLabel();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Модели конструкций");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(900, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                BoxTypical.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(900, 29));
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

        btnChoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c044.gif"))); // NOI18N
        btnChoice.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnChoice.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnChoice.setFocusable(false);
        btnChoice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChoice.setMaximumSize(new java.awt.Dimension(25, 25));
        btnChoice.setMinimumSize(new java.awt.Dimension(25, 25));
        btnChoice.setPreferredSize(new java.awt.Dimension(25, 25));
        btnChoice.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoiceresh(evt);
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
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 536, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
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
        tab1.setRowHeight(68);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setResizable(false);
            tab1.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab1.getColumnModel().getColumn(1).setResizable(false);
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

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "№", "Наименование конструкции", "Рисунок конструкции"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setRowHeight(68);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setResizable(false);
            tab2.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab2.getColumnModel().getColumn(1).setResizable(false);
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

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "Ном.п/п", "Наименование конструкции", "Рисунок конструкции"
            }
        ));
        tab3.setRowHeight(80);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab3.getColumnModel().getColumn(0).setMaxWidth(20);
            tab3.getColumnModel().getColumn(2).setMinWidth(68);
            tab3.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab3.getColumnModel().getColumn(2).setMaxWidth(68);
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

        centr.setPreferredSize(new java.awt.Dimension(700, 560));
        centr.setLayout(new java.awt.BorderLayout());

        pan17.setLayout(new java.awt.BorderLayout());

        pan3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan3.setMinimumSize(new java.awt.Dimension(100, 20));
        pan3.setPreferredSize(new java.awt.Dimension(700, 40));

        lab2.setText("Высота");
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtField2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField2.setText("1800");

        lab1.setText("Ширина");
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField1.setText("1200");

        javax.swing.GroupLayout pan3Layout = new javax.swing.GroupLayout(pan3);
        pan3.setLayout(pan3Layout);
        pan3Layout.setHorizontalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(478, Short.MAX_VALUE))
        );
        pan3Layout.setVerticalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab2)
                    .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab1)
                    .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan17.add(pan3, java.awt.BorderLayout.SOUTH);

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
                .addContainerGap(218, Short.MAX_VALUE))
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
            .addGap(0, 398, Short.MAX_VALUE)
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
            .addGap(0, 438, Short.MAX_VALUE)
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
            .addGap(0, 438, Short.MAX_VALUE)
        );

        pan4.add(pan10, java.awt.BorderLayout.WEST);

        pan17.add(pan4, java.awt.BorderLayout.CENTER);

        pan6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan6.setPreferredSize(new java.awt.Dimension(320, 500));
        pan6.setLayout(new java.awt.CardLayout());

        lab19.setText("jLabel19");

        javax.swing.GroupLayout pan19Layout = new javax.swing.GroupLayout(pan19);
        pan19.setLayout(pan19Layout);
        pan19Layout.setHorizontalGroup(
            pan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(pan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan19Layout.createSequentialGroup()
                    .addGap(0, 138, Short.MAX_VALUE)
                    .addComponent(lab19)
                    .addGap(0, 138, Short.MAX_VALUE)))
        );
        pan19Layout.setVerticalGroup(
            pan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
            .addGroup(pan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan19Layout.createSequentialGroup()
                    .addGap(0, 237, Short.MAX_VALUE)
                    .addComponent(lab19)
                    .addGap(0, 237, Short.MAX_VALUE)))
        );

        pan6.add(pan19, "pan19");

        lab20.setText("jLabel20");

        javax.swing.GroupLayout pan20Layout = new javax.swing.GroupLayout(pan20);
        pan20.setLayout(pan20Layout);
        pan20Layout.setHorizontalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan20Layout.createSequentialGroup()
                    .addGap(0, 138, Short.MAX_VALUE)
                    .addComponent(lab20)
                    .addGap(0, 138, Short.MAX_VALUE)))
        );
        pan20Layout.setVerticalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
            .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan20Layout.createSequentialGroup()
                    .addGap(0, 237, Short.MAX_VALUE)
                    .addComponent(lab20)
                    .addGap(0, 237, Short.MAX_VALUE)))
        );

        pan6.add(pan20, "pan20");

        lab21.setText("jLabel21");

        javax.swing.GroupLayout pan21Layout = new javax.swing.GroupLayout(pan21);
        pan21.setLayout(pan21Layout);
        pan21Layout.setHorizontalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan21Layout.createSequentialGroup()
                    .addGap(0, 138, Short.MAX_VALUE)
                    .addComponent(lab21)
                    .addGap(0, 138, Short.MAX_VALUE)))
        );
        pan21Layout.setVerticalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
            .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan21Layout.createSequentialGroup()
                    .addGap(0, 237, Short.MAX_VALUE)
                    .addComponent(lab21)
                    .addGap(0, 237, Short.MAX_VALUE)))
        );

        pan6.add(pan21, "pan21");

        lab22.setText("jLabel22");

        javax.swing.GroupLayout pan22Layout = new javax.swing.GroupLayout(pan22);
        pan22.setLayout(pan22Layout);
        pan22Layout.setHorizontalGroup(
            pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan22Layout.createSequentialGroup()
                    .addGap(0, 138, Short.MAX_VALUE)
                    .addComponent(lab22)
                    .addGap(0, 138, Short.MAX_VALUE)))
        );
        pan22Layout.setVerticalGroup(
            pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
            .addGroup(pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan22Layout.createSequentialGroup()
                    .addGap(0, 237, Short.MAX_VALUE)
                    .addComponent(lab22)
                    .addGap(0, 237, Short.MAX_VALUE)))
        );

        pan6.add(pan22, "pan22");

        lab23.setText("jLabel23");

        javax.swing.GroupLayout pan23Layout = new javax.swing.GroupLayout(pan23);
        pan23.setLayout(pan23Layout);
        pan23Layout.setHorizontalGroup(
            pan23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(pan23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan23Layout.createSequentialGroup()
                    .addGap(0, 138, Short.MAX_VALUE)
                    .addComponent(lab23)
                    .addGap(0, 138, Short.MAX_VALUE)))
        );
        pan23Layout.setVerticalGroup(
            pan23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
            .addGroup(pan23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan23Layout.createSequentialGroup()
                    .addGap(0, 237, Short.MAX_VALUE)
                    .addComponent(lab23)
                    .addGap(0, 237, Short.MAX_VALUE)))
        );

        pan6.add(pan23, "pan23");

        lab24.setText("jLabel24");

        javax.swing.GroupLayout pan24Layout = new javax.swing.GroupLayout(pan24);
        pan24.setLayout(pan24Layout);
        pan24Layout.setHorizontalGroup(
            pan24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(pan24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan24Layout.createSequentialGroup()
                    .addGap(0, 138, Short.MAX_VALUE)
                    .addComponent(lab24)
                    .addGap(0, 138, Short.MAX_VALUE)))
        );
        pan24Layout.setVerticalGroup(
            pan24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
            .addGroup(pan24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pan24Layout.createSequentialGroup()
                    .addGap(0, 237, Short.MAX_VALUE)
                    .addComponent(lab24)
                    .addGap(0, 237, Short.MAX_VALUE)))
        );

        pan6.add(pan24, "pan24");

        pan17.add(pan6, java.awt.BorderLayout.EAST);

        centr.add(pan17, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 918, Short.MAX_VALUE)
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
        System.out.println(paintPanel.getWidth() + "  -  " + paintPanel.getHeight());
    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave

    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void panMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panMouseClicked
        System.out.println(evt.getX() + " " + evt.getY());
    }//GEN-LAST:event_panMouseClicked

    private void btnChoiceresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoiceresh
        JTable table = null;
        Query query = null;
        if (btnT1.isSelected()) {
            query = qModels1;
            table = tab1;
        } else if (btnT2.isSelected()) {
            query = qModels2;
            table = tab2;
        }
        int row = Util.getSelectedRec(table);
        if (row != -1) {
            Record record = new Record();
            record.add(query.get(row, eModels.id));
            listenet.action(record);
        }
        this.dispose();
    }//GEN-LAST:event_btnChoiceresh

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

    private void btnRemovresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovresh

    }//GEN-LAST:event_btnRemovresh

    private void btnToggl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToggl
        if (btnT1.isSelected()) {
            selectionTab1(null);
            ((CardLayout) west.getLayout()).show(west, "pan13");
        } else if (btnT2.isSelected()) {
            selectionTab2(null);
            ((CardLayout) west.getLayout()).show(west, "pan14");
        } else if (btnT3.isSelected()) {
            selectionTab3(null);
            ((CardLayout) west.getLayout()).show(west, "pan15");
        } else {
            loadingTree();
            ((CardLayout) west.getLayout()).show(west, "pan18");
        }
    }//GEN-LAST:event_btnToggl

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnRemov;
    private javax.swing.JButton btnSave;
    private javax.swing.JToggleButton btnT1;
    private javax.swing.JToggleButton btnT2;
    private javax.swing.JToggleButton btnT3;
    private javax.swing.JToggleButton btnT4;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel centr;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab19;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab20;
    private javax.swing.JLabel lab21;
    private javax.swing.JLabel lab22;
    private javax.swing.JLabel lab23;
    private javax.swing.JLabel lab24;
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
    private javax.swing.JPanel pan19;
    private javax.swing.JPanel pan20;
    private javax.swing.JPanel pan21;
    private javax.swing.JPanel pan22;
    private javax.swing.JPanel pan23;
    private javax.swing.JPanel pan24;
    private javax.swing.JPanel pan25;
    private javax.swing.JPanel pan26;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan6;
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
    private javax.swing.JFormattedTextField txtField1;
    private javax.swing.JFormattedTextField txtField2;
    private javax.swing.JFormattedTextField txtField4;
    private javax.swing.JFormattedTextField txtField5;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    private void initElements() {

        new FrameToFile(this, btnClose);
        nuni = Integer.valueOf(eProperty.systree_nuni.read());
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
        tree.getSelectionModel().addTreeSelectionListener(tse -> selectionTree());
    }

    private class DefMutableTreeNode extends DefaultMutableTreeNode {

        public Mediate record = null;

        public DefMutableTreeNode(Mediate record) {
            super();
            this.record = record;
        }

        public String toString() {
            if (record.type == TypeElem.FRAME_SIDE) {
                return record.type.name + ", " + record.layout.name.toLowerCase();
            } else if (record.type == TypeElem.AREA) {
                return record.type.name + ". " + record.layout.name + " напр.";
            } else {
                return record.type.name;
            }
        }
    }
}
