package frames;

import common.FrameToFile;
import dataset.Query;
import dataset.Record;
import domain.eGroups;
import enums.TypeGroups;
import frames.swing.DefCellEditor;
import frames.swing.DefTableModel;
import java.awt.CardLayout;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;

public class Groups extends javax.swing.JFrame {

    private Query qArtSeri = new Query(eGroups.values());
    private Query qArtIncr = new Query(eGroups.values());
    private Query qArtDecr = new Query(eGroups.values());
    private Query qCategProf = new Query(eGroups.values());
    private Query qColgrp = new Query(eGroups.values());
    private Query qCategVst = new Query(eGroups.values());

    public Groups(Object... page) {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        for (int i = page.length; i > 0; --i) {
            int p = (int) page[i - 1];
            System.out.println(p);
            tabb.remove(p);
        }
    }

    private void loadingData() {
        qColgrp.select(eGroups.up, "where", eGroups.grup, "= 2", "order by", eGroups.name);
        qArtSeri.select(eGroups.up, "where", eGroups.grup, "= 3", "order by", eGroups.name);
        qArtIncr.select(eGroups.up, "where", eGroups.grup, "= 4", "order by", eGroups.name);
        qArtDecr.select(eGroups.up, "where", eGroups.grup, "= 5", "order by", eGroups.name);
        qCategProf.select(eGroups.up, "where", eGroups.grup, "= 6", "order by", eGroups.name);
        qCategVst.select(eGroups.up, "where", eGroups.grup, "= 8", "order by", eGroups.name);
    }

    private void loadingModel() {
        new DefTableModel(tab1, qArtIncr, eGroups.name, eGroups.val);
        new DefTableModel(tab2, qArtDecr, eGroups.name, eGroups.val);
        new DefTableModel(tab3, qArtSeri, eGroups.name);
        new DefTableModel(tab4, qCategProf, eGroups.name);
        new DefTableModel(tab5, qColgrp, eGroups.name, eGroups.val);
        new DefTableModel(tab6, qCategVst, eGroups.name);
        tab1.getColumnModel().getColumn(1).setCellEditor(new DefCellEditor(3));
        tab2.getColumnModel().getColumn(1).setCellEditor(new DefCellEditor(3));
        tab5.getColumnModel().getColumn(1).setCellEditor(new DefCellEditor(3));
        Util.setSelectedRow(tab1);
        Util.setSelectedRow(tab2);
        Util.setSelectedRow(tab3);
        Util.setSelectedRow(tab4);
        Util.setSelectedRow(tab5);
        Util.setSelectedRow(tab6);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        centr = new javax.swing.JPanel();
        tabb = new javax.swing.JTabbedPane();
        pan7 = new javax.swing.JPanel();
        lab13 = new javax.swing.JLabel();
        lab16 = new javax.swing.JLabel();
        lab17 = new javax.swing.JLabel();
        lab18 = new javax.swing.JLabel();
        lab19 = new javax.swing.JLabel();
        lab20 = new javax.swing.JLabel();
        lab21 = new javax.swing.JLabel();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        panl1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan4 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan6 = new javax.swing.JPanel();
        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Группы");
        setPreferredSize(new java.awt.Dimension(600, 508));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        centr.setPreferredSize(new java.awt.Dimension(600, 459));
        centr.setLayout(new java.awt.BorderLayout());

        tabb.setPreferredSize(new java.awt.Dimension(600, 459));
        tabb.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbStateChanged(evt);
            }
        });

        pan7.setPreferredSize(new java.awt.Dimension(450, 433));

        lab13.setFont(frames.Util.getFont(0,0));
        lab13.setText("Коэффициент наценки на материал");
        lab13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab13.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab13.setMinimumSize(new java.awt.Dimension(34, 14));
        lab13.setPreferredSize(new java.awt.Dimension(240, 18));

        lab16.setFont(frames.Util.getFont(0,0));
        lab16.setText("Коэффициент наценки на заполнения");
        lab16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab16.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab16.setMinimumSize(new java.awt.Dimension(34, 14));
        lab16.setPreferredSize(new java.awt.Dimension(240, 18));

        lab17.setFont(frames.Util.getFont(0,0));
        lab17.setText("Наценка на изделие арочной формы");
        lab17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab17.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab17.setMinimumSize(new java.awt.Dimension(34, 14));
        lab17.setPreferredSize(new java.awt.Dimension(240, 18));

        lab18.setFont(frames.Util.getFont(0,0));
        lab18.setText("Наценка на изделие с непр-ной формы");
        lab18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab18.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab18.setMinimumSize(new java.awt.Dimension(34, 14));
        lab18.setPreferredSize(new java.awt.Dimension(240, 18));

        lab19.setFont(frames.Util.getFont(0,0));
        lab19.setText("Максимальная скидка общая");
        lab19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab19.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab19.setMinimumSize(new java.awt.Dimension(34, 14));
        lab19.setPreferredSize(new java.awt.Dimension(240, 18));

        lab20.setFont(frames.Util.getFont(0,0));
        lab20.setText("Максимальная скидка на конструкцию");
        lab20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab20.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab20.setMinimumSize(new java.awt.Dimension(34, 14));
        lab20.setPreferredSize(new java.awt.Dimension(240, 18));

        lab21.setFont(frames.Util.getFont(0,0));
        lab21.setText("Максимальная скидка на комплектацию");
        lab21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab21.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab21.setMinimumSize(new java.awt.Dimension(34, 14));
        lab21.setPreferredSize(new java.awt.Dimension(240, 18));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(251, Short.MAX_VALUE))
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        tabb.addTab("<html><font size=\"3\">Скидки/наценки", pan7);

        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", null, null},
                {"222", null, null}
            },
            new String [] {
                "Название группы", "Скидка %", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
            tab2.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(1).setMaxWidth(80);
            tab2.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        tabb.addTab("<html><font size=\"3\">Скидки групп МЦ", pan2);

        panl1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", null, null},
                {"222", null, null}
            },
            new String [] {
                "Название группы", "Наценка (коэф)", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
            tab1.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(1).setMaxWidth(80);
            tab1.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        panl1.add(scr1, java.awt.BorderLayout.CENTER);

        tabb.addTab("<html><font size=\"3\">Наценки групп МЦ", panl1);

        pan5.setLayout(new java.awt.BorderLayout());

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Наименование групп", "Коэффициент", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab5.setFillsViewportHeight(true);
        tab5.setName("tab5"); // NOI18N
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab5.getColumnModel().getColumn(1).setMaxWidth(80);
            tab5.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        pan5.add(scr5, java.awt.BorderLayout.CENTER);

        tabb.addTab("<html><font size=\"3\">Коэффициенты текстур", pan5);

        pan3.setLayout(new java.awt.BorderLayout());

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1111", null},
                {"2222", null}
            },
            new String [] {
                "Наименование", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setMaxWidth(40);
        }

        pan3.add(scr3, java.awt.BorderLayout.CENTER);

        tabb.addTab("<html><font size=\"3\">Серии профилей", pan3);

        pan4.setLayout(new java.awt.BorderLayout());

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Наименование", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
            tab4.getColumnModel().getColumn(1).setMaxWidth(40);
        }

        pan4.add(scr4, java.awt.BorderLayout.CENTER);

        tabb.addTab("<html><font size=\"3\">Категории профилей", pan4);

        pan6.setLayout(new java.awt.BorderLayout());

        tab6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Наименование", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab6.setFillsViewportHeight(true);
        tab6.setName("tab4"); // NOI18N
        tab6.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr6.setViewportView(tab6);
        if (tab6.getColumnModel().getColumnCount() > 0) {
            tab6.getColumnModel().getColumn(1).setMaxWidth(40);
        }

        pan6.add(scr6, java.awt.BorderLayout.CENTER);

        tabb.addTab("<html><font size=\"3\">Категории вставок", pan6);

        centr.add(tabb, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(10, 29));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 296, Short.MAX_VALUE)
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

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(10, 20));
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
                txtFilterfilterCaretUpdate(evt);
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
        Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6).forEach(tab -> ((DefaultTableModel) tab.getModel()).fireTableDataChanged());
        Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6).forEach(tab -> Util.setSelectedRow(tab));
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (Util.isDeleteRecord(this) == 0) {
            if (tab1.getBorder() != null) {
                Util.deleteRecord(tab1);
            } else if (tab2.getBorder() != null) {
                Util.deleteRecord(tab2);
            } else if (tab3.getBorder() != null) {
                Util.deleteRecord(tab3);
            } else if (tab4.getBorder() != null) {
                Util.deleteRecord(tab4);
            } else if (tab5.getBorder() != null) {
                Util.deleteRecord(tab5);
            } else if (tab6.getBorder() != null) {
                Util.deleteRecord(tab6);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.PRICE_INC.id);
            });

        } else if (tab2.getBorder() != null) {
            Util.insertRecord(tab2, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.PRICE_DEC.id);
            });

        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab3, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.SERI_PROF.id);
            });

        } else if (tab4.getBorder() != null) {
            Util.insertRecord(tab4, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.CATEG_PRF.id);
            });

        } else if (tab5.getBorder() != null) {
            Util.insertRecord(tab5, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.COLOR.id);
            });

        } else if (tab6.getBorder() != null) {
            Util.insertRecord(tab6, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.CATEG_VST.id);
            });
        }
    }//GEN-LAST:event_btnInsert

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void txtFilterfilterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilterfilterCaretUpdate

        JTable table = Stream.of(tab1, tab2, tab3, tab4, tab5, tab6).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_txtFilterfilterCaretUpdate

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.updateBorderAndSql(table, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6);
        Arrays.asList(qArtIncr, qArtDecr, qArtSeri, qCategProf, qColgrp, qCategVst).forEach(q -> q.execsql());
    }//GEN-LAST:event_formWindowClosed

    private void tabbStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbStateChanged
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6);
        Arrays.asList(qArtIncr, qArtDecr, qArtSeri, qCategProf, qColgrp, qCategVst).forEach(q -> q.execsql());
        if (tabb.getSelectedIndex() == 0) {
            //Util.updateBorderAndSql(tab7, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        } else if (tabb.getSelectedIndex() == 1) {
            Util.updateBorderAndSql(tab2, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        } else if (tabb.getSelectedIndex() == 2) {
            Util.updateBorderAndSql(tab1, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        } else if (tabb.getSelectedIndex() == 3) {
            Util.updateBorderAndSql(tab5, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        } else if (tabb.getSelectedIndex() == 4) {
            Util.updateBorderAndSql(tab3, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        } else if (tabb.getSelectedIndex() == 5) {
            Util.updateBorderAndSql(tab4, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        } else if (tabb.getSelectedIndex() == 6) {
            Util.updateBorderAndSql(tab6, Arrays.asList(tab1, tab2, tab3, tab4, tab5, tab6));
        }
    }//GEN-LAST:event_tabbStateChanged

    // <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel lab13;
    private javax.swing.JLabel lab16;
    private javax.swing.JLabel lab17;
    private javax.swing.JLabel lab18;
    private javax.swing.JLabel lab19;
    private javax.swing.JLabel lab20;
    private javax.swing.JLabel lab21;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel panl1;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab6;
    private javax.swing.JTabbedPane tabb;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        Arrays.asList(btnIns, btnDel, btnRef).forEach(btn -> btn.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6)));
    }
}
