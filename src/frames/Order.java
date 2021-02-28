package frames;

import common.DialogListener;
import common.EditorListener;
import common.FrameToFile;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eGroups;
import domain.eOrders;
import domain.eOrdcontr;
import frames.dialog.DicDate;
import frames.dialog.DicName;
import frames.swing.DefCellRenderer;
import javax.swing.JTable;
import frames.swing.DefTableModel;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class Order extends javax.swing.JFrame {

    private Query qOrdconr = new Query(eOrdcontr.values());
    private Query qOrders = new Query(eOrders.values());
    private EditorListener listenerDate;

    public Order() {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    private void loadingData() {
        qOrdconr.select(eOrdcontr.up);
        qOrders.select(eOrders.up, "order by", eOrders.num_ord);
    }

    private void loadingModel() {
        new DefTableModel(tab1, qOrders, eOrders.num_ord, eOrders.date4, eOrders.date6, eOrders.contractor_id, eOrders.manager, eOrders.categ) {
            @Override
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eOrders.contractor_id) {
                    Record record = qOrdconr.stream().filter(rec -> rec.get(eOrdcontr.id).equals(val)).findFirst().orElse(eOrdcontr.up.newRecord());
                    return record.get(eOrdcontr.contractor);
                }
                return val;
            }
        };
        tab1.getColumnModel().getColumn(1).setCellRenderer(new DefCellRenderer());
        tab1.getColumnModel().getColumn(2).setCellRenderer(new DefCellRenderer());
    }

    private void listenerAdd() {

        Util.buttonCellEditor(tab1, 1).addActionListener(event -> {
            new DicDate(this, (obj) -> {
                GregorianCalendar calendar = (GregorianCalendar) obj;
                Util.stopCellEditing(tab1);
                Record record2 = qOrders.get(Util.getIndexRec(tab1));
                record2.set(eOrders.date4, calendar.getTime());
                qOrders.update(record2);
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
                System.out.println(calendar.getTime());
                return true;
            }, 0);
        });
        
        Util.buttonCellEditor(tab1, 1).addActionListener(event -> {
            new DicDate(this, (obj) -> {
                GregorianCalendar calendar = (GregorianCalendar) obj;
                Util.stopCellEditing(tab1);
                Record record2 = qOrders.get(Util.getIndexRec(tab1));
                record2.set(eOrders.date4, calendar.getTime());
                qOrders.update(record2);
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
                System.out.println(calendar.getTime());
                return true;
            }, 0);
        });

        Util.buttonCellEditor(tab1, 2).addActionListener(event -> {
            new Partner(this, (record) -> {
                Util.stopCellEditing(tab1);
                Record record2 = qOrders.get(Util.getIndexRec(tab1));
                record2.set(eOrders.contractor_id, record.getInt(eOrdcontr.id));
                qOrders.update(record2);
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
            });
        });

        Util.buttonCellEditor(tab1, 4).addActionListener(event -> {
            Set set = new HashSet();
            qOrders.forEach(rec -> set.add(rec.get(eOrders.categ)));
            new DicName(this, (record) -> {
                Util.stopCellEditing(tab1);
                Record record2 = qOrders.get(Util.getIndexRec(tab1));
                record2.set(eOrders.categ, record.getStr(0));
                qOrders.update(record2);
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
            }, set);
        });

    }

    private void selectionTab1(ListSelectionEvent event) {
//        Util.stopCellEditing(tab1);
//        int index = Util.getIndexRec(tab1);
//        if (index != -1) {
//            int flag = qOrdcontr.getAs(index, eOrdcontr.flag2);
//            String name = (flag == 1) ? "pan3" : "pan4";
//            ((CardLayout) pan2.getLayout()).show(pan2, name);
//        }
//        rsv.load(index);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        lab2 = new javax.swing.JLabel();
        lab3 = new javax.swing.JLabel();
        lab4 = new javax.swing.JLabel();
        lab5 = new javax.swing.JLabel();
        txt1 = new javax.swing.JTextField();
        txt2 = new javax.swing.JTextField();
        txt3 = new javax.swing.JTextField();
        txt4 = new javax.swing.JTextField();
        txt5 = new javax.swing.JTextField();
        btn1 = new javax.swing.JButton();
        lab6 = new javax.swing.JLabel();
        txt6 = new javax.swing.JTextField();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Заказы");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Order.this.windowClosed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 718, Short.MAX_VALUE)
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
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(800, 450));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(400, 370));
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scr1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Номер заказа", "Дата 1", "Дата 2", "Контрагент", "Менеджер", "Фильтр"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
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
            tab1.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(4).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(5).setPreferredWidth(60);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        pan2.setPreferredSize(new java.awt.Dimension(400, 470));

        lab1.setFont(frames.Util.getFont(0,0));
        lab1.setText("Номер отдела");
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab1.setMinimumSize(new java.awt.Dimension(34, 14));
        lab1.setPreferredSize(new java.awt.Dimension(120, 18));

        lab2.setFont(frames.Util.getFont(0,0));
        lab2.setText("Номер счета");
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab2.setMinimumSize(new java.awt.Dimension(34, 14));
        lab2.setPreferredSize(new java.awt.Dimension(120, 18));

        lab3.setFont(frames.Util.getFont(0,0));
        lab3.setText("Валюта название");
        lab3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab3.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab3.setMinimumSize(new java.awt.Dimension(34, 14));
        lab3.setPreferredSize(new java.awt.Dimension(120, 18));

        lab4.setFont(frames.Util.getFont(0,0));
        lab4.setText("Cебестоимость");
        lab4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab4.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab4.setMinimumSize(new java.awt.Dimension(34, 14));
        lab4.setPreferredSize(new java.awt.Dimension(120, 18));

        lab5.setFont(frames.Util.getFont(0,0));
        lab5.setText("Cтоимость без скидки");
        lab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab5.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab5.setMinimumSize(new java.awt.Dimension(34, 14));
        lab5.setPreferredSize(new java.awt.Dimension(120, 18));

        txt1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt1.setPreferredSize(new java.awt.Dimension(120, 18));
        txt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt1ActionPerformed(evt);
            }
        });

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setPreferredSize(new java.awt.Dimension(120, 18));
        txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt2ActionPerformed(evt);
            }
        });

        txt3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt3.setPreferredSize(new java.awt.Dimension(120, 18));
        txt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt3ActionPerformed(evt);
            }
        });

        txt4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt4.setPreferredSize(new java.awt.Dimension(120, 18));
        txt4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt4ActionPerformed(evt);
            }
        });

        txt5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt5.setPreferredSize(new java.awt.Dimension(120, 18));
        txt5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt5ActionPerformed(evt);
            }
        });

        btn1.setText("...");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(18, 18));
        btn1.setMinimumSize(new java.awt.Dimension(18, 18));
        btn1.setName("btn1"); // NOI18N
        btn1.setPreferredSize(new java.awt.Dimension(18, 18));

        lab6.setFont(frames.Util.getFont(0,0));
        lab6.setText("Cтоимость со скидкой");
        lab6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab6.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab6.setMinimumSize(new java.awt.Dimension(34, 14));
        lab6.setPreferredSize(new java.awt.Dimension(120, 18));

        txt6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt6.setPreferredSize(new java.awt.Dimension(120, 18));
        txt6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan2Layout = new javax.swing.GroupLayout(pan2);
        pan2.setLayout(pan2Layout);
        pan2Layout.setHorizontalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(122, Short.MAX_VALUE))
        );
        pan2Layout.setVerticalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(319, Short.MAX_VALUE))
        );

        centr.add(pan2, java.awt.BorderLayout.EAST);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

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
        txtFilter.setMaximumSize(new java.awt.Dimension(180, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(180, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(180, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFilter(evt);
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
        Arrays.asList(tab1).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(tab1, this) == 0) {
                Util.deleteRecord(tab1);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, eOrders.up, (record) -> {
            });
        }
    }//GEN-LAST:event_btnInsert

    private void txtFilter(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilter
        JTable table = Stream.of(tab1).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_txtFilter

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1);
        Arrays.asList(tab1).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
    }//GEN-LAST:event_windowClosed

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        Util.updateBorderAndSql(tab1, Arrays.asList(tab1));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(tab1.getColumnName((tab1.getSelectedColumn() == -1 || tab1.getSelectedColumn() == 0) ? 0 : tab1.getSelectedColumn()));
            txtFilter.setName(tab1.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void txt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt1ActionPerformed

    private void txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt2ActionPerformed

    private void txt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt3ActionPerformed

    private void txt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt4ActionPerformed

    private void txt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt5ActionPerformed

    private void txt6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt6ActionPerformed

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab3;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab5;
    private javax.swing.JLabel lab6;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt3;
    private javax.swing.JTextField txt4;
    private javax.swing.JTextField txt5;
    private javax.swing.JTextField txt6;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {
        new FrameToFile(this, btnClose);
        FrameToFile.setFrameSize(this);
        setTitle(getTitle() + Util.designName());
        labFilter.setText(tab1.getColumnName(0));
        txtFilter.setName(tab1.getName());
        tab1.getSelectionModel().addListSelectionListener(event -> selectionTab1(event));
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1)));
    }
}
