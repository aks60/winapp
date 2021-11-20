package frames.dialog;

import builder.param.KitDet;
import common.UCom;
import common.listener.ListenerObject;
import dataset.Conn;
import frames.FrameToFile;
import frames.UGui;
import dataset.Query;
import dataset.Record;
import domain.eColor;
import java.awt.Frame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import domain.eArtdet;
import domain.eArtikl;
import domain.eKitdet;
import domain.eKits;
import domain.eProkit;
import enums.UseUnit;
import java.util.HashMap;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

//Дополнительные комплекты
public class DicKits extends javax.swing.JDialog {

    private static final int serialVersionUID = 1343775792;
    private ListenerObject<Query> listener = null;
    private Query qKits = new Query(eKits.values());
    private Query qKitdet = new Query(eKitdet.values());
    private Query qProkit = new Query(eProkit.values());
    private int colorID[] = {-1, -1, -1};
    private int proprodID = -1;

    public DicKits(Frame parent, ListenerObject<Query> listener, int proprodID) {
        super(parent, true);
        initComponents();
        initElements();
        this.listener = listener;
        this.proprodID = proprodID;
        loadingData("0");
        loadingModel();
        setVisible(true);
    }

    public void loadingData(String type) {
        eArtikl.query();
        qKits.select(eKits.up, "where", eKits.types, "=", type, "order by", eKits.categ, ",", eKits.name);
    }

    private void loadingModel() {
        new DefTableModel(tab1, qKits, eKits.categ, eKits.name);
        new DefTableModel(tab2, qKitdet, eKitdet.artikl_id, eKitdet.artikl_id,
                eKitdet.color1_id, eKitdet.color2_id, eKitdet.color3_id, eKitdet.id, eKitdet.flag) {

            public Object getValueAt(int col, int row, Object val) {

                if (val != null && col == 0) {
                    return eArtikl.get((int) val).getStr(eArtikl.code);

                } else if (val != null && col == 1) {
                    return eArtikl.get((int) val).getStr(eArtikl.name);

                } else if (val != null && columns[col] == eKitdet.color1_id) {
                    return eColor.get((int) val).getStr(eColor.name);

                } else if (val != null && columns[col] == eKitdet.color2_id) {
                    return eColor.get((int) val).getStr(eColor.name);

                } else if (val != null && columns[col] == eKitdet.color3_id) {
                    return eColor.get((int) val).getStr(eColor.name);

                } else if (val != null && col == 5) { //columns[col] == eArtikl.unit) {
                    int index = tab2.convertRowIndexToModel(row);
                    int id = qKitdet.getAs(index, eKitdet.artikl_id);
                    Record record = eArtikl.get(id);
                    return UseUnit.getName(record.getInt(eArtikl.unit));
                }
                return val;
            }
        };
        UGui.setSelectedRow(tab1);
    }

    public void selectionTab1() {
        UGui.clearTable(tab2);
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record record = qKits.get(index);
            Integer id = record.getInt(eKits.id);
            qKitdet.select(eKitdet.up, "where", eKitdet.kits_id, "=", id, "order by", eKitdet.artikl_id);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab2);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        cbx1 = new javax.swing.JComboBox<>();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        lab30 = new javax.swing.JLabel();
        lab13 = new javax.swing.JLabel();
        txt1 = new javax.swing.JTextField();
        lab14 = new javax.swing.JLabel();
        txt2 = new javax.swing.JTextField();
        txt3 = new javax.swing.JTextField();
        lab27 = new javax.swing.JLabel();
        lab31 = new javax.swing.JLabel();
        lab32 = new javax.swing.JLabel();
        txt9 = new javax.swing.JTextField();
        txt13 = new javax.swing.JTextField();
        txt14 = new javax.swing.JTextField();
        btn9 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn14 = new javax.swing.JButton();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Справочник комплектов");

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(600, 29));

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

        cbx1.setBackground(new java.awt.Color(212, 208, 200));
        cbx1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbx1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Продажа", "Скатка", "Ламинация", "Стеклопакет" }));
        cbx1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cbx1.setPreferredSize(new java.awt.Dimension(160, 25));
        cbx1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAction(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 285, Short.MAX_VALUE)
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
                            .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(612, 560));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setLayout(new java.awt.BorderLayout());

        pan2.setPreferredSize(new java.awt.Dimension(513, 92));

        lab30.setFont(frames.UGui.getFont(0,0));
        lab30.setText("Кол. комп.");
        lab30.setToolTipText("");
        lab30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab30.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab30.setMinimumSize(new java.awt.Dimension(34, 14));
        lab30.setPreferredSize(new java.awt.Dimension(80, 18));

        lab13.setFont(frames.UGui.getFont(0,0));
        lab13.setText("Длина");
        lab13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab13.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab13.setMinimumSize(new java.awt.Dimension(34, 14));
        lab13.setPreferredSize(new java.awt.Dimension(80, 18));

        txt1.setText("0");
        txt1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt1.setPreferredSize(new java.awt.Dimension(60, 18));

        lab14.setFont(frames.UGui.getFont(0,0));
        lab14.setText("Ширина");
        lab14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab14.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab14.setMinimumSize(new java.awt.Dimension(34, 14));
        lab14.setPreferredSize(new java.awt.Dimension(80, 18));

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setPreferredSize(new java.awt.Dimension(60, 18));

        txt3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt3.setPreferredSize(new java.awt.Dimension(60, 18));

        lab27.setFont(frames.UGui.getFont(0,0));
        lab27.setText("Основная текстура");
        lab27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab27.setMaximumSize(new java.awt.Dimension(120, 18));
        lab27.setMinimumSize(new java.awt.Dimension(120, 18));
        lab27.setPreferredSize(new java.awt.Dimension(120, 18));

        lab31.setFont(frames.UGui.getFont(0,0));
        lab31.setText("Внутренняя текстура");
        lab31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab31.setMaximumSize(new java.awt.Dimension(120, 18));
        lab31.setMinimumSize(new java.awt.Dimension(120, 18));
        lab31.setPreferredSize(new java.awt.Dimension(120, 18));

        lab32.setFont(frames.UGui.getFont(0,0));
        lab32.setText("Внешняя текстура");
        lab32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab32.setMaximumSize(new java.awt.Dimension(120, 18));
        lab32.setMinimumSize(new java.awt.Dimension(120, 18));
        lab32.setPreferredSize(new java.awt.Dimension(120, 18));

        txt9.setEditable(false);
        txt9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt9.setPreferredSize(new java.awt.Dimension(180, 18));

        txt13.setEditable(false);
        txt13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt13.setPreferredSize(new java.awt.Dimension(180, 18));

        txt14.setEditable(false);
        txt14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt14.setPreferredSize(new java.awt.Dimension(180, 18));

        btn9.setText("...");
        btn9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn9.setMaximumSize(new java.awt.Dimension(18, 18));
        btn9.setMinimumSize(new java.awt.Dimension(18, 18));
        btn9.setName("btn9"); // NOI18N
        btn9.setPreferredSize(new java.awt.Dimension(18, 18));
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        btn13.setText("...");
        btn13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn13.setMaximumSize(new java.awt.Dimension(18, 18));
        btn13.setMinimumSize(new java.awt.Dimension(18, 18));
        btn13.setName("btn13"); // NOI18N
        btn13.setPreferredSize(new java.awt.Dimension(18, 18));
        btn13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        btn14.setText("...");
        btn14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn14.setMaximumSize(new java.awt.Dimension(18, 18));
        btn14.setMinimumSize(new java.awt.Dimension(18, 18));
        btn14.setName("btn14"); // NOI18N
        btn14.setPreferredSize(new java.awt.Dimension(18, 18));
        btn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        javax.swing.GroupLayout pan2Layout = new javax.swing.GroupLayout(pan2);
        pan2.setLayout(pan2Layout);
        pan2Layout.setHorizontalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt9, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pan2Layout.setVerticalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan1.add(pan2, java.awt.BorderLayout.NORTH);

        scr1.setPreferredSize(new java.awt.Dimension(412, 160));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", "name1", null},
                {"222", "name2", null}
            },
            new String [] {
                "Категория", "Название комплекта", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(300);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        scr2.setPreferredSize(new java.awt.Dimension(454, 304));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Артикул", "Название", "Основная текстура", "Внутренняя текстура", "Внешняя текстура", "Ед.измерения", "Основной элемент", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
            tab2.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(400);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(6).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(7).setPreferredWidth(60);
        }

        pan1.add(scr2, java.awt.BorderLayout.SOUTH);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(600, 20));
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

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice
        if (qKitdet.stream().anyMatch(rec -> "1".equals(eArtikl.get(rec.getInt(eKitdet.artikl_id)).getStr(eArtikl.unit))) == true) {
            if (txt3.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Укажите количество комплектов.", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else if (txt2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Укажите длину комплекта.", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else if (txt9.getText().isEmpty() || txt13.getText().isEmpty() || txt14.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Укажите текстуру комплекта.", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        HashMap<Integer, String> mapParam = new HashMap();
        KitDet kitDet = new KitDet(UCom.getFloat(txt3.getText()), UCom.getFloat(txt2.getText()), UCom.getFloat(txt1.getText()));
        for (Record record : qKitdet) {
            mapParam.clear();
            //ФИЛЬТР детализации, параметры накапливаются в mapParam
            if (kitDet.filter(mapParam, record) == true) {

                Record artiklRec = eArtikl.get(record.getInt(eKitdet.artikl_id));
                Record recordKit = eProkit.up.newRecord(Query.INS);
                recordKit.set(eProkit.id, Conn.instanc().genId(eProkit.up));
                recordKit.set(eProkit.proprod_id, proprodID);
                recordKit.set(eProkit.artikl_id, artiklRec.getInt(eArtikl.id));

                recordKit.set(eProkit.numb, get_7031_8061_9061(mapParam)); //количество                                
                Float width = get_8066_9066(mapParam);
                width = (width == null) ? 0 : width;
                recordKit.set(eProkit.width, width); //длина                
                Float height = get_8071_9071(mapParam);
                height = (height == null) ? artiklRec.getFloat(eArtikl.height) : height;
                recordKit.set(eProkit.height, height); //ширина               
                Float angl1 = get_8075(mapParam, 0);
                angl1 = (angl1 == null) ? 90 : angl1;
                recordKit.set(eProkit.angl1, angl1); //угол 1                
                Float angl2 = get_8075(mapParam, 1);
                angl1 = (angl2 == null) ? 90 : angl2;
                recordKit.set(eProkit.angl2, angl2); //угол 2
                recordKit.set(eProkit.color1_id, colorID[0]); //color1
                recordKit.set(eProkit.color2_id, colorID[1]); //color2
                recordKit.set(eProkit.color3_id, colorID[2]); //color3
                qProkit.insert(recordKit);
            }
        }

        listener.action(null);
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        if (evt.getClickCount() == 2 && tab2.getRowCount() == 0) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tab1MouseClicked

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate
        JTable table = Stream.of(tab1, tab2).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab2);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, Arrays.asList(tab1, tab2));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
//        if (evt.getClickCount() == 2) {
//            btnChoice(null);
//        }
    }//GEN-LAST:event_tabMousePressed

    private void comboBoxAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxAction
        UGui.stopCellEditing(tab1, tab2);
        Arrays.asList(tab1, tab2).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        int index = cbx1.getSelectedIndex();
        loadingData(String.valueOf(index));
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_comboBoxAction

    private void colorToWindows(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToWindows
        try {
            JTextField txt = (evt.getSource() == btn9) ? txt9 : (evt.getSource() == btn13) ? txt13 : txt14;
            int index = (evt.getSource() == btn9) ? 0 : (evt.getSource() == btn13) ? 1 : 2;
            Record record = qKitdet.stream().filter(rec -> 1 == rec.getInt(eKitdet.flag)).findFirst().orElse(null);
            if (record != null) {
                int id = record.getInt(eKitdet.artikl_id);
                HashSet<Record> colorSet = new HashSet();
                Query artdetList = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.artikl_id, "=", id);
                artdetList.forEach(rec -> {

                    if (rec.getInt(eArtdet.color_fk) < 0) {
                        eColor.query().forEach(rec2 -> {
                            if (rec2.getInt(eColor.colgrp_id) == Math.abs(rec.getInt(eArtdet.color_fk))) {
                                colorSet.add(rec2);
                            }
                        });
                    } else {
                        colorSet.add(eColor.find(rec.getInt(eArtdet.color_fk)));
                    }
                });
                DicColor frame = new DicColor(null, (rec) -> {
                    setColor(index, rec);
                }, colorSet);
            } else {
                DicColor frame = new DicColor(null, (rec) -> {
                    setColor(index, rec);
                });
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToWindows

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JComboBox<String> cbx1;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel lab13;
    private javax.swing.JLabel lab14;
    private javax.swing.JLabel lab27;
    private javax.swing.JLabel lab30;
    private javax.swing.JLabel lab31;
    private javax.swing.JLabel lab32;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt13;
    private javax.swing.JTextField txt14;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt3;
    private javax.swing.JTextField txt9;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1();
                }
            }
        });

        JTextField editorText1 = (JTextField) txt3;
        PlainDocument doc1 = (PlainDocument) editorText1.getDocument();
        doc1.setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ("0123456789.,".indexOf(string) != -1) { //проверка на коррекность ввода
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                if ("0123456789.,".indexOf(string) != -1) {  //проверка на коррекность ввода
                    super.replace(fb, offset, length, string, attrs);
                }
            }
        });

        JTextField editorText2 = (JTextField) txt2;
        PlainDocument doc2 = (PlainDocument) editorText2.getDocument();
        doc2.setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ("0123456789.,".indexOf(string) != -1) { //проверка на коррекность ввода
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                if ("0123456789.,".indexOf(string) != -1) {  //проверка на коррекность ввода
                    super.replace(fb, offset, length, string, attrs);
                }
            }
        });

        JTextField editorText3 = (JTextField) txt1;
        PlainDocument doc3 = (PlainDocument) editorText3.getDocument();
        doc3.setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ("0123456789.,".indexOf(string) != -1) { //проверка на коррекность ввода
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                if ("0123456789.,".indexOf(string) != -1) {  //проверка на коррекность ввода
                    super.replace(fb, offset, length, string, attrs);
                }
            }
        });
    }

    //Количество ед.
    private float get_7031_8061_9061(HashMap<Integer, String> mapParam) {
        String numb = getParam(mapParam, 7031, 8061, 9061);
        if (numb != null) {
            return Float.valueOf(numb);
        }
        return 1;
    }

    //Длина, мм
    private Float get_8066_9066(HashMap<Integer, String> mapParam) {
        String numb = getParam(mapParam, 8066, 9066);
        if (numb != null) {
            return Float.valueOf(numb);
        }
        return null;
    }

    //Ширина, мм
    private Float get_8071_9071(HashMap<Integer, String> mapParam) {
        String numb = getParam(mapParam, 8071, 9071);
        if (numb != null) {
            return Float.valueOf(numb);
        }
        return null;
    }

    //Углы реза "90х90", "90х45", "45х90", "45х45"
    private Float get_8075(HashMap<Integer, String> mapParam, int m) {
        String angl = getParam(mapParam, 8075);
        if (angl != null) {
            String s[] = angl.split("х");
            return Float.valueOf(s[m]);
        }
        return null;
    }

    private String getParam(HashMap<Integer, String> mapParam, int... p) {

        if (mapParam != null) {
            for (int index = 0; index < p.length; ++index) {
                int key = p[index];
                String str = mapParam.get(Integer.valueOf(key));
                if (str != null) {
                    return str;
                }
            }
        }
        return null;
    }

    private void setColor(int index, Record rec) {
        if (index == 0) {
            txt9.setText(rec.getStr(eColor.name));
            colorID[0] = rec.getInt(eColor.id);
            txt13.setText(rec.getStr(eColor.name));
            colorID[1] = rec.getInt(eColor.id);
            txt14.setText(rec.getStr(eColor.name));
            colorID[2] = rec.getInt(eColor.id);

        } else if (index == 1) {
            txt13.setText(rec.getStr(eColor.name));
            colorID[1] = rec.getInt(eColor.id);
            
        } else if (index == 2) {
            txt14.setText(rec.getStr(eColor.name));
            colorID[2] = rec.getInt(eColor.id);
        }
    }
}
