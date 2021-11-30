package frames;

import dataset.Query;
import dataset.Record;
import domain.ePropart;
import frames.swing.DefFieldEditor;
import javax.swing.JTable;
import frames.swing.DefTableModel;
import frames.swing.FilterTable;
import java.awt.Frame;
import java.awt.Window;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import common.listener.ListenerRecord;
import dataset.Conn;
import java.sql.ResultSet;
import javax.swing.event.ListSelectionListener;

public class Partner extends javax.swing.JFrame {

    private Window owner = null;
    private ListenerRecord listener = null;
    private FilterTable filterTable = null;
    private Query qPrjcontr = new Query(ePropart.values());
    private DefFieldEditor rsv = null;
    private String arrCateg[] = {"заказчик", "поставшик", "офис", "дилер", "специальный"};

    public Partner() {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
    }

    public Partner(Frame owner, ListenerRecord listener) {
        initComponents();
        initElements();
        this.listener = listener;
        this.owner = owner;
        owner.setEnabled(false);
        loadingData();
        loadingModel();
        setVisible(true);
    }

    public void loadingData() {
        qPrjcontr.select(ePropart.up, "order by", ePropart.category, ",", ePropart.partner);
    }

    public void loadingModel() {
        new DefTableModel(tab1, qPrjcontr, ePropart.category, ePropart.partner, ePropart.manager, ePropart.flag2);

        UGui.buttonCellEditor(tab1, 0).addActionListener(event -> {
            Object result = JOptionPane.showInputDialog(Partner.this, "Выберите категорию",
                    "Изменение категории контрагента", JOptionPane.QUESTION_MESSAGE, null, arrCateg, arrCateg[0]);
            if (result != null) {
                UGui.stopCellEditing(tab1);
                qPrjcontr.set(result, UGui.getIndexRec(tab1), ePropart.category);
                ((DefTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
            }
        });

        rsv.add(ePropart.addr_leve1, txt12);
        rsv.add(ePropart.addr_leve2, txt14);
        rsv.add(ePropart.addr_phone, txt13);
        rsv.add(ePropart.note, txt15);

        rsv.add(ePropart.org_name, txt8);
        rsv.add(ePropart.org_leve1, txt9);
        rsv.add(ePropart.org_leve2, txt17);
        rsv.add(ePropart.org_phone, txt10);
        rsv.add(ePropart.org_fax, txt11);
        rsv.add(ePropart.bank_name, txt1);
        rsv.add(ePropart.bank_inn, txt2);
        rsv.add(ePropart.bank_rs, txt3);
        rsv.add(ePropart.bank_bik, txt4);
        rsv.add(ePropart.bank_ks, txt5);
        rsv.add(ePropart.bank_kpp, txt6);
        rsv.add(ePropart.bank_ogrn, txt7);
        rsv.add(ePropart.note, txt16);

        UGui.setSelectedRow(tab1);
    }

    public void selectionTab1(ListSelectionEvent event) {
        UGui.stopCellEditing(tab1);
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            int flag = qPrjcontr.getAs(index, ePropart.flag2);
            int i = (flag == 1) ? 1 : 0;
            tabb1.setSelectedIndex(i);
        }
        rsv.load(index);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        tabb1 = new javax.swing.JTabbedPane();
        pan4 = new javax.swing.JPanel();
        lab47 = new javax.swing.JLabel();
        txt12 = new javax.swing.JTextField();
        lab48 = new javax.swing.JLabel();
        txt13 = new javax.swing.JTextField();
        lab49 = new javax.swing.JLabel();
        txt14 = new javax.swing.JTextField();
        lab50 = new javax.swing.JLabel();
        txt15 = new javax.swing.JTextField();
        pan3 = new javax.swing.JPanel();
        lab36 = new javax.swing.JLabel();
        txt8 = new javax.swing.JTextField();
        lab37 = new javax.swing.JLabel();
        txt9 = new javax.swing.JTextField();
        lab38 = new javax.swing.JLabel();
        txt10 = new javax.swing.JTextField();
        lab39 = new javax.swing.JLabel();
        txt11 = new javax.swing.JTextField();
        lab40 = new javax.swing.JLabel();
        txt1 = new javax.swing.JTextField();
        lab41 = new javax.swing.JLabel();
        txt2 = new javax.swing.JTextField();
        lab42 = new javax.swing.JLabel();
        txt3 = new javax.swing.JTextField();
        lab45 = new javax.swing.JLabel();
        txt4 = new javax.swing.JTextField();
        lab46 = new javax.swing.JLabel();
        txt5 = new javax.swing.JTextField();
        lab43 = new javax.swing.JLabel();
        txt6 = new javax.swing.JTextField();
        lab44 = new javax.swing.JLabel();
        txt7 = new javax.swing.JTextField();
        lab51 = new javax.swing.JLabel();
        txt16 = new javax.swing.JTextField();
        lab52 = new javax.swing.JLabel();
        txt17 = new javax.swing.JTextField();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Контрагенты");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Partner.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));

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
        btnChoice.setToolTipText(bundle.getString("Добавить")); // NOI18N
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

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btnRemove.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnRemove.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemove.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 524, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setPreferredSize(new java.awt.Dimension(800, 404));
        center.setLayout(new java.awt.GridLayout(1, 0));

        pan1.setLayout(new java.awt.BorderLayout());

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Категория", "Контрагент", "User", "", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false, false
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
                Partner.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(50);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(140);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(3).setResizable(false);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(16);
            tab1.getColumnModel().getColumn(4).setMaxWidth(40);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        center.add(pan1);

        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabb1StateChanged(evt);
            }
        });

        pan4.setName(""); // NOI18N

        lab47.setFont(frames.UGui.getFont(0,0));
        lab47.setText("Адрес 1го уровня");
        lab47.setToolTipText("");
        lab47.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab47.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab47.setMinimumSize(new java.awt.Dimension(34, 14));
        lab47.setPreferredSize(new java.awt.Dimension(98, 18));

        txt12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt12.setPreferredSize(new java.awt.Dimension(300, 18));

        lab48.setFont(frames.UGui.getFont(0,0));
        lab48.setText("Телефон");
        lab48.setToolTipText("");
        lab48.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab48.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab48.setMinimumSize(new java.awt.Dimension(34, 14));
        lab48.setPreferredSize(new java.awt.Dimension(98, 18));

        txt13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt13.setPreferredSize(new java.awt.Dimension(300, 18));

        lab49.setFont(frames.UGui.getFont(0,0));
        lab49.setText("Адрес 2го уровня");
        lab49.setToolTipText("");
        lab49.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab49.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab49.setMinimumSize(new java.awt.Dimension(34, 14));
        lab49.setPreferredSize(new java.awt.Dimension(98, 18));

        txt14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt14.setPreferredSize(new java.awt.Dimension(300, 18));

        lab50.setFont(frames.UGui.getFont(0,0));
        lab50.setText("Примечание");
        lab50.setToolTipText("");
        lab50.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab50.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab50.setMinimumSize(new java.awt.Dimension(34, 14));
        lab50.setPreferredSize(new java.awt.Dimension(98, 18));

        txt15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt15.setPreferredSize(new java.awt.Dimension(300, 18));

        javax.swing.GroupLayout pan4Layout = new javax.swing.GroupLayout(pan4);
        pan4.setLayout(pan4Layout);
        pan4Layout.setHorizontalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan4Layout.createSequentialGroup()
                        .addComponent(lab47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan4Layout.createSequentialGroup()
                        .addComponent(lab49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan4Layout.createSequentialGroup()
                        .addComponent(lab48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan4Layout.createSequentialGroup()
                        .addComponent(lab50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan4Layout.setVerticalGroup(
            pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(319, Short.MAX_VALUE))
        );

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\nЧастное лицо\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", pan4);

        pan3.setName(""); // NOI18N

        lab36.setFont(frames.UGui.getFont(0,0));
        lab36.setText("Организация");
        lab36.setToolTipText("");
        lab36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab36.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab36.setMinimumSize(new java.awt.Dimension(34, 14));
        lab36.setPreferredSize(new java.awt.Dimension(96, 18));

        txt8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt8.setPreferredSize(new java.awt.Dimension(300, 18));

        lab37.setFont(frames.UGui.getFont(0,0));
        lab37.setText("Адрес 1го уровн...");
        lab37.setToolTipText("");
        lab37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab37.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab37.setMinimumSize(new java.awt.Dimension(34, 14));
        lab37.setPreferredSize(new java.awt.Dimension(96, 18));

        txt9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt9.setPreferredSize(new java.awt.Dimension(300, 18));

        lab38.setFont(frames.UGui.getFont(0,0));
        lab38.setText("Телефон");
        lab38.setToolTipText("");
        lab38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab38.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab38.setMinimumSize(new java.awt.Dimension(34, 14));
        lab38.setPreferredSize(new java.awt.Dimension(96, 18));

        txt10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt10.setPreferredSize(new java.awt.Dimension(300, 18));

        lab39.setFont(frames.UGui.getFont(0,0));
        lab39.setText("Факс");
        lab39.setToolTipText("");
        lab39.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab39.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab39.setMinimumSize(new java.awt.Dimension(34, 14));
        lab39.setPreferredSize(new java.awt.Dimension(96, 18));

        txt11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt11.setPreferredSize(new java.awt.Dimension(300, 18));

        lab40.setFont(frames.UGui.getFont(0,0));
        lab40.setText("Банк");
        lab40.setToolTipText("");
        lab40.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab40.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab40.setMinimumSize(new java.awt.Dimension(34, 14));
        lab40.setPreferredSize(new java.awt.Dimension(96, 18));

        txt1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt1.setPreferredSize(new java.awt.Dimension(300, 18));

        lab41.setFont(frames.UGui.getFont(0,0));
        lab41.setText("ИНН");
        lab41.setToolTipText("");
        lab41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab41.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab41.setMinimumSize(new java.awt.Dimension(34, 14));
        lab41.setPreferredSize(new java.awt.Dimension(96, 18));

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setPreferredSize(new java.awt.Dimension(300, 18));

        lab42.setFont(frames.UGui.getFont(0,0));
        lab42.setText("Р/С");
        lab42.setToolTipText("");
        lab42.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab42.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab42.setMinimumSize(new java.awt.Dimension(34, 14));
        lab42.setPreferredSize(new java.awt.Dimension(96, 18));

        txt3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt3.setPreferredSize(new java.awt.Dimension(300, 18));

        lab45.setFont(frames.UGui.getFont(0,0));
        lab45.setText("БИК");
        lab45.setToolTipText("");
        lab45.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab45.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab45.setMinimumSize(new java.awt.Dimension(34, 14));
        lab45.setPreferredSize(new java.awt.Dimension(96, 18));

        txt4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt4.setPreferredSize(new java.awt.Dimension(300, 18));

        lab46.setFont(frames.UGui.getFont(0,0));
        lab46.setText("К/С");
        lab46.setToolTipText("");
        lab46.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab46.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab46.setMinimumSize(new java.awt.Dimension(34, 14));
        lab46.setPreferredSize(new java.awt.Dimension(96, 18));

        txt5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt5.setPreferredSize(new java.awt.Dimension(300, 18));

        lab43.setFont(frames.UGui.getFont(0,0));
        lab43.setText("КПП");
        lab43.setToolTipText("");
        lab43.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab43.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab43.setMinimumSize(new java.awt.Dimension(34, 14));
        lab43.setPreferredSize(new java.awt.Dimension(96, 18));

        txt6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt6.setPreferredSize(new java.awt.Dimension(300, 18));

        lab44.setFont(frames.UGui.getFont(0,0));
        lab44.setText("ОГРН");
        lab44.setToolTipText("");
        lab44.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab44.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab44.setMinimumSize(new java.awt.Dimension(34, 14));
        lab44.setPreferredSize(new java.awt.Dimension(96, 18));

        txt7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt7.setPreferredSize(new java.awt.Dimension(300, 18));

        lab51.setFont(frames.UGui.getFont(0,0));
        lab51.setText("Примечание");
        lab51.setToolTipText("");
        lab51.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab51.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab51.setMinimumSize(new java.awt.Dimension(34, 14));
        lab51.setPreferredSize(new java.awt.Dimension(96, 18));

        txt16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt16.setPreferredSize(new java.awt.Dimension(300, 18));

        lab52.setFont(frames.UGui.getFont(0,0));
        lab52.setText("Адрес 2го уровн...");
        lab52.setToolTipText("");
        lab52.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab52.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab52.setMinimumSize(new java.awt.Dimension(34, 14));
        lab52.setPreferredSize(new java.awt.Dimension(96, 18));

        txt17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt17.setPreferredSize(new java.awt.Dimension(300, 18));

        javax.swing.GroupLayout pan3Layout = new javax.swing.GroupLayout(pan3);
        pan3.setLayout(pan3Layout);
        pan3Layout.setHorizontalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan3Layout.createSequentialGroup()
                        .addComponent(lab52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan3Layout.setVerticalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\nОрганизация\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", pan3);

        center.add(tabb1);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Arrays.asList(tab1).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(tab1, this) == 0) {
                UGui.deleteRecord(tab1);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            UGui.insertRecordEnd(tab1, ePropart.up, (prjpartRec) -> {
                try {
                    ResultSet rs = Conn.instanc().getConnection().createStatement().executeQuery("SELECT current_user FROM rdb$database");
                    rs.next();
                    prjpartRec.setNo(ePropart.manager, rs.getString(1));
                    prjpartRec.setNo(ePropart.category, arrCateg[0]);

                } catch (Exception e) {
                    System.err.println("frames.Partner.btnInsert()");
                }
            });
        }
    }//GEN-LAST:event_btnInsert

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        UGui.updateBorderAndSql(tab1, Arrays.asList(tab1));
        filterTable.mousePressed((JTable) evt.getSource());
    }//GEN-LAST:event_mousePressed

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice
        int index = UGui.getIndexRec(tab1);
        if (index != -1 && listener != null) {
            Record record = qPrjcontr.get(index);
            listener.action(record);
        }
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void btnRemove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove

    }//GEN-LAST:event_btnRemove

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1);
        Arrays.asList(tab1).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked

    }//GEN-LAST:event_btnRemoveMouseClicked

    private void tabb1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabb1StateChanged
        if (UGui.getIndexRec(tab1) != -1) {
            if (tabb1.getSelectedIndex() == 0) {
                qPrjcontr.set(0, UGui.getIndexRec(tab1), ePropart.flag2);
            } else {
                qPrjcontr.set(1, UGui.getIndexRec(tab1), ePropart.flag2);
            }
            ((DefTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
        }
    }//GEN-LAST:event_tabb1StateChanged

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnRemove;
    private javax.swing.JPanel center;
    private javax.swing.JLabel lab36;
    private javax.swing.JLabel lab37;
    private javax.swing.JLabel lab38;
    private javax.swing.JLabel lab39;
    private javax.swing.JLabel lab40;
    private javax.swing.JLabel lab41;
    private javax.swing.JLabel lab42;
    private javax.swing.JLabel lab43;
    private javax.swing.JLabel lab44;
    private javax.swing.JLabel lab45;
    private javax.swing.JLabel lab46;
    private javax.swing.JLabel lab47;
    private javax.swing.JLabel lab48;
    private javax.swing.JLabel lab49;
    private javax.swing.JLabel lab50;
    private javax.swing.JLabel lab51;
    private javax.swing.JLabel lab52;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt10;
    private javax.swing.JTextField txt11;
    private javax.swing.JTextField txt12;
    private javax.swing.JTextField txt13;
    private javax.swing.JTextField txt14;
    private javax.swing.JTextField txt15;
    private javax.swing.JTextField txt16;
    private javax.swing.JTextField txt17;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt3;
    private javax.swing.JTextField txt4;
    private javax.swing.JTextField txt5;
    private javax.swing.JTextField txt6;
    private javax.swing.JTextField txt7;
    private javax.swing.JTextField txt8;
    private javax.swing.JTextField txt9;
    // End of variables declaration//GEN-END:variables
   // </editor-fold> 

    public void initElements() {
        new FrameToFile(this, btnClose);
        FrameToFile.setFrameSize(this);
        filterTable = new FilterTable(0, tab1);
        south.add(filterTable, 0);        
        filterTable.getTxt().grabFocus();   
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> UGui.stopCellEditing(tab1)));
        rsv = new DefFieldEditor(tab1);
    }
}
