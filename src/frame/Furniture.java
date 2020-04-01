package frame;

import common.Util;
import common.FrameListener;
import common.FrameToFile;
import common.Util;
import static common.Util.getSelectedRow;
import dataset.ConnApp;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnpar1;
import domain.eFurnpar2;
import domain.eFurnside1;
import domain.eColor;
import domain.eFurnside2;
import domain.eSysfurn;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefTableModel;

public class Furniture extends javax.swing.JFrame {

    private Query qFurniture = new Query(eFurniture.values());
    private Query qFurnside1 = new Query(eFurnside1.values());
    private Query qFurnside2 = new Query(eFurnside2.values());
    private Query qFurnpar1 = new Query(eFurnpar1.values());
    private Query qFurndet = new Query(eFurndet.values(), eArtikl.values(), eColor.values());
    private Query qFurnpar2 = new Query(eFurnpar2.values());
    private FrameListener listenerFrame = null;
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;

    public Furniture() {
        initComponents();
        initElements();
        initData();
        initModel();
    }

    public Furniture(java.awt.Window owner, int nuni) {
        initComponents();
        initElements();
        this.owner = owner;
        this.nuni = nuni;
        listenerFrame = (FrameListener) owner;
        owner.setEnabled(false);
        initData();
        initModel();
    }

    private void initModel() {
        new DefTableModel(tab1, qFurniture, eFurniture.name, eFurniture.view_open, eFurniture.view_open, eFurniture.p2_max, eFurniture.width_max,
                eFurniture.height_max, eFurniture.weight_max, eFurniture.types, eFurniture.pars, eFurniture.coord_lim);
        new DefTableModel(tab2, qFurndet, eArtikl.code, eArtikl.code, eArtikl.name, eColor.name, eFurndet.types);
        new DefTableModel(tab3, qFurnpar2, eFurnpar2.grup, eFurnpar2.text);
        new DefTableModel(tab4, qFurnside1, eFurnside1.npp, eFurnside1.furniture_id, eFurnside1.type_side);
        new DefTableModel(tab5, qFurnpar1, eFurnpar1.grup, eFurnpar1.text);
        new DefTableModel(tab6, qFurnside2, eFurnside2.side_num, eFurnside2.len_min, eFurnside2.len_max, eFurnside2.ang_min, eFurnside2.ang_max);
        Util.setSelectedRow(tab1, 0);
    }

    private void initData() {
        if (owner == null) {
            qFurniture.select(eFurniture.up, "order by", eFurniture.name);
        } else {
            Query query = new Query(eSysfurn.furniture_id).select(eSysfurn.up, "where", eSysfurn.systree_id, "=", nuni).table(eSysfurn.up);
            query.stream().forEach(rec -> subsql = subsql + "," + rec.getStr(eSysfurn.furniture_id));
            subsql = "(" + subsql.substring(1) + ")";
            qFurniture.select(eFurniture.up, "where", eFurniture.id, "in", subsql, "order by", eFurniture.name);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = getSelectedRow(tab1);
        if (row != -1) {
            Record record = qFurniture.table(eFurniture.up).get(row);
            Integer id = record.getInt(eFurniture.id);
            qFurnside1.select(eFurnside1.up, "where", eFurnside1.furniture_id, "=", id, "order by", eFurnside1.npp);
            qFurndet.select(eFurndet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eFurndet.artikl_id,
                    "left join", eColor.up, "on", eColor.id, "=", eFurndet.furniture_id,
                    "where", eFurndet.furniture_id, "=", id);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, 0);
            Util.setSelectedRow(tab4, 0);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = getSelectedRow(tab2);
        if (row != -1) {
            Record record = qFurndet.table(eFurndet.up).get(row);
            Integer id = record.getInt(eFurndet.id);
            qFurnpar2.select(eFurnpar2.up, "where", eFurnpar2.furndet_id, "=", id, "order by", eFurnpar2.grup);
            qFurnside2.select(eFurnside2.up, "where", eFurnside2.furndet_id, "=", id, "order by", eFurnside2.side_num);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, 0);
            Util.setSelectedRow(tab6, 0);
        }
    }

    private void selectionTab4(ListSelectionEvent event) {
        int row = getSelectedRow(tab4);
        if (row != -1) {
            Record record = qFurnside1.table(eFurnside1.up).get(row);
            Integer id = record.getInt(eFurnside1.id);
            qFurnpar1.select(eFurnpar1.up, "where", eFurnpar1.furnside_id, "=", id, "order by", eFurnpar1.grup);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab5, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grour1 = new javax.swing.ButtonGroup();
        panNorth = new javax.swing.JPanel();
        btnIns = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        rdb1 = new javax.swing.JRadioButton();
        rdb2 = new javax.swing.JRadioButton();
        rdb3 = new javax.swing.JRadioButton();
        panSentr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        pan6 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Фурнитура");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(800, 29));

        btnIns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c033.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/prop/hint"); // NOI18N
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

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
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

        grour1.add(rdb1);
        rdb1.setSelected(true);
        rdb1.setText("Основная");
        rdb1.setMaximumSize(new java.awt.Dimension(75, 18));
        rdb1.setMinimumSize(new java.awt.Dimension(75, 18));
        rdb1.setPreferredSize(new java.awt.Dimension(100, 18));

        grour1.add(rdb2);
        rdb2.setText("Дополнительная");
        rdb2.setMaximumSize(new java.awt.Dimension(75, 18));
        rdb2.setMinimumSize(new java.awt.Dimension(75, 18));
        rdb2.setPreferredSize(new java.awt.Dimension(100, 18));

        grour1.add(rdb3);
        rdb3.setText("Комплекты");
        rdb3.setMaximumSize(new java.awt.Dimension(75, 18));
        rdb3.setMinimumSize(new java.awt.Dimension(75, 18));
        rdb3.setPreferredSize(new java.awt.Dimension(100, 18));

        javax.swing.GroupLayout panNorthLayout = new javax.swing.GroupLayout(panNorth);
        panNorth.setLayout(panNorthLayout);
        panNorthLayout.setHorizontalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(rdb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(rdb2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(rdb3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panNorthLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdb2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdb3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panSentr.setPreferredSize(new java.awt.Dimension(800, 562));
        panSentr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(800, 260));
        pan1.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(500, 200));
        pan4.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setPreferredSize(new java.awt.Dimension(450, 200));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"333333333333333", "3", "3", "3", null, null, null, null, null, null},
                {"444444444444444", "3", "4", "5", null, null, null, null, null, null}
            },
            new String [] {
                "Название", "Вид", "Сторона ручки", "Р/2 максимальная", "Ширина максимальная", "Высота максимальная", "Вес максимальный", "Створка", "Использ. параметры", "Ограничения"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(200);
        }

        pan4.add(scr1, java.awt.BorderLayout.CENTER);

        pan1.add(pan4, java.awt.BorderLayout.CENTER);

        pan5.setPreferredSize(new java.awt.Dimension(300, 200));
        pan5.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(null);
        scr4.setPreferredSize(new java.awt.Dimension(300, 112));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "33", "3333"},
                {"2", "44", "4444"}
            },
            new String [] {
                "Номер", "Вид", "Назначение"
            }
        ));
        tab4.setFillsViewportHeight(true);
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab4.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab4.getColumnModel().getColumn(2).setPreferredWidth(220);
        }

        pan5.add(scr4, java.awt.BorderLayout.NORTH);

        scr5.setBorder(null);
        scr5.setPreferredSize(new java.awt.Dimension(300, 100));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111111111111111", "11"},
                {"222222222222222", "22"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab5.setFillsViewportHeight(true);
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab5.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan5.add(scr5, java.awt.BorderLayout.CENTER);

        pan1.add(pan5, java.awt.BorderLayout.EAST);

        panSentr.add(pan1, java.awt.BorderLayout.NORTH);

        pan2.setPreferredSize(new java.awt.Dimension(800, 302));
        pan2.setLayout(new java.awt.BorderLayout());

        pan6.setPreferredSize(new java.awt.Dimension(800, 300));
        pan6.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(300, 300));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11111111111", "11"},
                {"22222222222", "22"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan6.add(scr3, java.awt.BorderLayout.EAST);

        jPanel1.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);
        scr2.setPreferredSize(new java.awt.Dimension(500, 300));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "xxxxxxxxx", "11", "11"},
                {"22", "vvvvvvvvv", "22", "22"}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);

        jPanel1.add(scr2, java.awt.BorderLayout.CENTER);

        scr6.setPreferredSize(new java.awt.Dimension(454, 80));

        tab6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "0", "2600", "0", "360"},
                {"2", "0", "2600", "0", "360"}
            },
            new String [] {
                "Сторона", "Мин. длина", "Макс. длина", "Мин. угол", "Макс. угол"
            }
        ));
        tab6.setFillsViewportHeight(true);
        tab6.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr6.setViewportView(tab6);

        jPanel1.add(scr6, java.awt.BorderLayout.SOUTH);

        pan6.add(jPanel1, java.awt.BorderLayout.CENTER);

        pan2.add(pan6, java.awt.BorderLayout.CENTER);

        panSentr.add(pan2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panSentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(800, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 927, Short.MAX_VALUE)
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        initData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?", "Предупреждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                Record furnitureRec = qFurniture.get(getSelectedRow(tab1));
                furnitureRec.set(eFurniture.up, Query.DEL);
                qFurniture.delete(furnitureRec);
                qFurniture.removeRec(getSelectedRow(tab1));
                ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab1, 0);

            } else if (tab2.getBorder() != null) {
                Record furndetRec = qFurndet.get(getSelectedRow(tab2));
                furndetRec.set(eFurndet.up, Query.DEL);
                qFurndet.delete(furndetRec);
                qFurndet.removeRec(getSelectedRow(tab2));
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, 0);

            } else if (tab3.getBorder() != null) {
                Record urnpar2Rec = qFurnpar2.get(getSelectedRow(tab3));
                urnpar2Rec.set(eFurnpar2.up, Query.DEL);
                qFurnpar2.delete(urnpar2Rec);
                qFurnpar2.removeRec(getSelectedRow(tab3));
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab3, 0);

            } else if (tab4.getBorder() != null) {
                Record furnside1Rec = qFurnside1.get(getSelectedRow(tab3));
                furnside1Rec.set(eFurnside1.up, Query.DEL);
                qFurnside1.delete(furnside1Rec);
                qFurnside1.removeRec(getSelectedRow(tab4));
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab4, 0);

            } else if (tab5.getBorder() != null) {
                Record furnpar1Rec = qFurnpar1.get(getSelectedRow(tab5));
                furnpar1Rec.set(eFurnpar1.up, Query.DEL);
                qFurnpar1.delete(furnpar1Rec);
                qFurnpar1.removeRec(getSelectedRow(tab5));
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab5, 0);

            } else if (tab6.getBorder() != null) {
                Record furnside2Rec = qFurnside2.get(getSelectedRow(tab6));
                furnside2Rec.set(eFurnside2.up, Query.DEL);
                qFurnside2.delete(furnside2Rec);
                qFurnside2.removeRec(getSelectedRow(tab6));
                ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab6, 0);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            Record glasgrpRec = qFurniture.newRecord(Query.INS);
            glasgrpRec.setNo(eFurniture.id, ConnApp.instanc().genId(eFurniture.up));
            qFurniture.add(glasgrpRec);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qFurniture, tab1);

        } else if (tab2.getBorder() != null) {
            int row = getSelectedRow(tab1);
            if (row != -1) {
                Record furnityreRec = qFurniture.get(row);
                Record furndetRec = qFurndet.newRecord(Query.INS);
                furndetRec.setNo(eFurndet.id, ConnApp.instanc().genId(eFurndet.up));
                furndetRec.setNo(eFurndet.furniture_id, furnityreRec.getInt(eFurniture.id));
                qFurndet.add(furndetRec);
                qFurndet.table(eArtikl.up).add(eArtikl.up.newRecord());
                qFurndet.table(eColor.up).add(eColor.up.newRecord());
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qFurndet, tab2);
            }
        } else if (tab3.getBorder() != null) {
            int row = getSelectedRow(tab2);
            if (row != -1) {
                Record furndetRec = qFurndet.get(row);
                Record furnpar2Rec = qFurnpar2.newRecord(Query.INS);
                furnpar2Rec.setNo(eFurnpar2.id, ConnApp.instanc().genId(eFurnpar2.up));
                furnpar2Rec.setNo(eFurnpar2.furndet_id, furndetRec.getInt(eFurndet.id));
                qFurnpar2.add(furnpar2Rec);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qFurnpar2, tab3);
            }
        } else if (tab4.getBorder() != null) {
            int row = getSelectedRow(tab1);
            if (row != -1) {
                Record furnityreRec = qFurniture.get(row);
                Record furnside1Rec = qFurnside1.newRecord(Query.INS);
                furnside1Rec.setNo(eFurnside1.id, ConnApp.instanc().genId(eFurnside1.up));
                furnside1Rec.setNo(eFurnside1.furniture_id, furnityreRec.getInt(eFurniture.id));
                qFurnside1.add(furnside1Rec);
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qFurnside1, tab4);
            }
        } else if (tab5.getBorder() != null) {
            int row = getSelectedRow(tab4);
            if (row != -1) {
                Record furnside1Rec = qFurnside1.get(row);
                Record furnpar1Rec = qFurnpar1.newRecord(Query.INS);
                furnpar1Rec.setNo(eFurnpar1.id, ConnApp.instanc().genId(eFurnpar1.up));
                furnpar1Rec.setNo(eFurnpar1.furnside_id, furnside1Rec.getInt(eFurnside1.id));
                qFurnpar1.add(furnpar1Rec);
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qFurnpar1, tab5);
            }
        } else if (tab6.getBorder() != null) {
            int row = getSelectedRow(tab2);
            if (row != -1) {
                Record furndetRec = qFurndet.get(row);
                Record furnside2Rec = qFurnside2.newRecord(Query.INS);
                furnside2Rec.setNo(eFurnside2.id, ConnApp.instanc().genId(eFurnside2.up));
                furnside2Rec.setNo(eFurnside2.furndet_id, furndetRec.getInt(eFurndet.id));
                qFurnside2.add(furnside2Rec);
                ((DefaultTableModel) tab6.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qFurnside2, tab6);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6);
        Arrays.asList(qFurniture, qFurndet, qFurnside1, qFurnpar1, qFurnside2, qFurnpar2).forEach(q -> q.execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed
// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.ButtonGroup grour1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSentr;
    private javax.swing.JPanel panSouth;
    private javax.swing.JRadioButton rdb1;
    private javax.swing.JRadioButton rdb2;
    private javax.swing.JRadioButton rdb3;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab6;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        FocusListener listenerFocus = new FocusListener() {

            javax.swing.border.Border border
                    = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

            public void focusGained(FocusEvent e) {
                Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6);
                tab1.setBorder(null);
                tab2.setBorder(null);
                tab3.setBorder(null);
                tab4.setBorder(null);
                tab5.setBorder(null);
                tab6.setBorder(null);
                if (e.getSource() instanceof JTable) {
                    ((JComponent) e.getSource()).setBorder(border);
                }
            }

            public void focusLost(FocusEvent e) {
            }
        };
        btnIns.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6));
        btnDel.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6));
        btnRef.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Спмсок фурнитуры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Спецификация фурнитуры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Описание сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Ограничения сторон сторон", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));

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
        tab4.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab4(event);
                }
            }
        });
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
        tab3.addFocusListener(listenerFocus);
        tab4.addFocusListener(listenerFocus);
        tab5.addFocusListener(listenerFocus);
        tab6.addFocusListener(listenerFocus);
    }
}
