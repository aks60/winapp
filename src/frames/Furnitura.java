package frames;

import common.FrameListener;
import dataset.Query;
import domain.eFurnitura;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import swing.DefTableModel;

public class Furnitura extends javax.swing.JFrame {

    private Query qFurnitura = new Query(eFurnitura.values()).select(eFurnitura.up, "order by", eFurnitura.name);

    private FocusListener listenerFocus = new FocusListener() {

        javax.swing.border.Border border
                = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

        public void focusGained(FocusEvent e) {
            JTable table = (JTable) e.getSource();
            table.setBorder(border);
//            tabList.add(table);
//            tabActive = table;
//            tmActive = (TableModel) table.getModel();
            btnIns.setEnabled(true);
//            if (table != treeMat) {
//                btnDel.setEnabled(true);
//            }
        }

        public void focusLost(FocusEvent e) {
            JTable table = (JTable) e.getSource();
            table.setBorder(null);
            btnIns.setEnabled(false);
            btnDel.setEnabled(false);
        }
    };
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

    public Furnitura() {
        initComponents();
        initElements();
        
      Object obj = qFurnitura.query(eFurnitura.up.tname());
      
        DefTableModel rsmFurnitura = new DefTableModel(tab1, qFurnitura, eFurnitura.name, eFurnitura.view_open, eFurnitura.view_open, eFurnitura.p2_max, 
                eFurnitura.width_max, eFurnitura.height_max, eFurnitura.weight_max, eFurnitura.types, eFurnitura.pars, eFurnitura.coord_lim);
//        rsmFurnitura.addFrameListener(listenerModify);
    }

    public void selectionTab1(ListSelectionEvent event) {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grour1 = new javax.swing.ButtonGroup();
        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
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
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan7 = new javax.swing.JPanel();
        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        scr7 = new javax.swing.JScrollPane();
        tab7 = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Фурнитура");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(600, 29));

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
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rdb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(rdb2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(rdb3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 394, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        panSentr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(800, 200));
        pan1.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(500, 400));
        pan4.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"ddddddd", "3", "3", "3", null, null, null, null, null, null},
                {"ffffffffffff", "3", "4", "5", null, null, null, null, null, null}
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

        pan5.setPreferredSize(new java.awt.Dimension(300, 400));
        pan5.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(null);
        scr4.setPreferredSize(new java.awt.Dimension(300, 100));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "333", "5544"},
                {"2", "44", "erere"}
            },
            new String [] {
                "Номер", "Вид", "Назваание"
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
        scr5.setPreferredSize(new java.awt.Dimension(300, 400));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzzzzzzzzzzzzzz", "11"},
                {"vvvvvvvvvvvvvv", "22"}
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

        pan2.setPreferredSize(new java.awt.Dimension(800, 402));
        pan2.setLayout(new java.awt.BorderLayout());

        pan6.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);
        scr2.setPreferredSize(new java.awt.Dimension(500, 400));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"фывфыв", "мммммммммммммм", "11",  new Double(22.0)},
                {"ываыва", "иииииииииииии", "22",  new Double(33.0)}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);

        pan6.add(scr2, java.awt.BorderLayout.CENTER);

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(300, 400));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"ffffffffffffffffff", "11"},
                {"eeeeeeeeeeee", "22"}
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

        pan2.add(pan6, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(807, 120));
        pan7.setLayout(new java.awt.BorderLayout());

        scr6.setBorder(null);
        scr6.setPreferredSize(new java.awt.Dimension(300, 400));

        tab6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"sssssssssssssssss", "55"},
                {"kkkkkkkkkkkkkkkkk", "44"}
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
        tab6.setFillsViewportHeight(true);
        tab6.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr6.setViewportView(tab6);
        if (tab6.getColumnModel().getColumnCount() > 0) {
            tab6.getColumnModel().getColumn(0).setPreferredWidth(220);
            tab6.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan7.add(scr6, java.awt.BorderLayout.EAST);

        scr7.setBorder(null);

        tab7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", "1", "1"},
                {"2", "2", "2", "2", "2"}
            },
            new String [] {
                "Номер", "Мин. длина", "Макс. длина", "Мин. угол", "Макс. угол"
            }
        ));
        tab7.setFillsViewportHeight(true);
        tab7.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr7.setViewportView(tab7);

        pan7.add(scr7, java.awt.BorderLayout.CENTER);

        pan2.add(pan7, java.awt.BorderLayout.SOUTH);

        panSentr.add(pan2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panSentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 882, Short.MAX_VALUE)
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

    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave

    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

//        if (qRate.isUpdate() && JOptionPane.showConfirmDialog(this, "Данные были изменены.Сохранить изменения?", "Предупреждение",
//                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
//            qRate.execsql();
//            listenerModify.response(null);
//        }
    }//GEN-LAST:event_formWindowClosed
// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup grour1;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
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
    private javax.swing.JScrollPane scr7;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab6;
    private javax.swing.JTable tab7;
    // End of variables declaration//GEN-END:variables

    private void initElements() {
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                selectionTab1(event);
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                //selectionTab2(event);
            }
        });
        tab4.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                //selectionTab4(event);
            }
        });
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
        tab3.addFocusListener(listenerFocus);
        tab4.addFocusListener(listenerFocus);
        tab5.addFocusListener(listenerFocus);
        tab6.addFocusListener(listenerFocus);
        tab7.addFocusListener(listenerFocus);
    }
// </editor-fold> 
}
