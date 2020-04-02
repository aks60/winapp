package frame;

import common.Util;
import common.FrameToFile;
import common.Util;
import dataset.ConnApp;
import dataset.Query;
import dataset.Record;
import domain.eColor;
import domain.eColpar1;
import domain.eKitdet;
import domain.eKitpar1;
import domain.eKits;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefTableModel;
import static common.Util.getSelectedRec;

public class Kits extends javax.swing.JFrame {

    private Query qKits = new Query(eKits.values()).select(eKits.up, "order by", eKits.name);
    private Query qKitdet = new Query(eKitdet.values());
    private Query qKitpar1 = new Query(eKitpar1.values());

    public Kits() {
        initComponents();
        initElements();
        initDatamodel();
    }

    private void initDatamodel() {
        new DefTableModel(tab1, qKits, eKits.name, eKits.artikl_id, eKits.color_id, eKits.quant, eKits.hide, eKits.categ);
        new DefTableModel(tab2, qKitdet, eKitdet.artikl_id, eKitdet.artikl_id, eKitdet.color1_id, eKitdet.color2_id, eKitdet.color3_id, eKitdet.flag);
        new DefTableModel(tab3, qKitpar1, eKitpar1.kitdet_id, eKitpar1.text);
        Util.setSelectedRow(tab1, 0);
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = getSelectedRec(tab1);
        if (row != -1) {
            Record record = qKits.get(row);
            Integer id = record.getInt(eKits.id);
            qKitdet.select(eKitdet.up, "where", eKitdet.kits_id, "=", id, "order by", eKitdet.artikl_id);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, 0);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = getSelectedRec(tab2);
        if (row != -1) {
            Record record = qKitdet.get(row);
            Integer id = record.getInt(eKitdet.id);
            qKitpar1.select(eKitpar1.up, "where", eKitpar1.kitdet_id, "=", id, "order by", eKitpar1.grup);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, 0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup1 = new javax.swing.ButtonGroup();
        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        rbtn1 = new javax.swing.JRadioButton();
        rbtn2 = new javax.swing.JRadioButton();
        rbn3 = new javax.swing.JRadioButton();
        rbtn4 = new javax.swing.JRadioButton();
        panCentr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Комплекты");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(700, 29));

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

        btnGroup1.add(rbtn1);
        rbtn1.setFont(Util.getFont(0, 0));
        rbtn1.setSelected(true);
        rbtn1.setText("Продажа");
        rbtn1.setMaximumSize(new java.awt.Dimension(96, 25));
        rbtn1.setMinimumSize(new java.awt.Dimension(96, 25));
        rbtn1.setPreferredSize(new java.awt.Dimension(96, 25));

        btnGroup1.add(rbtn2);
        rbtn2.setFont(Util.getFont(0, 0));
        rbtn2.setText("Скатка");
        rbtn2.setMaximumSize(new java.awt.Dimension(96, 25));
        rbtn2.setMinimumSize(new java.awt.Dimension(96, 25));
        rbtn2.setPreferredSize(new java.awt.Dimension(96, 25));

        btnGroup1.add(rbn3);
        rbn3.setFont(Util.getFont(0, 0));
        rbn3.setText("Стеклопакет");
        rbn3.setMaximumSize(new java.awt.Dimension(96, 25));
        rbn3.setMinimumSize(new java.awt.Dimension(96, 25));
        rbn3.setPreferredSize(new java.awt.Dimension(96, 25));

        btnGroup1.add(rbtn4);
        rbtn4.setFont(Util.getFont(0, 0));
        rbtn4.setText("Ламинация");
        rbtn4.setMaximumSize(new java.awt.Dimension(96, 25));
        rbtn4.setMinimumSize(new java.awt.Dimension(96, 25));
        rbtn4.setPreferredSize(new java.awt.Dimension(96, 25));

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
                .addGap(91, 91, 91)
                .addComponent(rbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtn4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 345, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panNorthLayout.createSequentialGroup()
                        .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rbtn4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCentr.setPreferredSize(new java.awt.Dimension(700, 560));
        panCentr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Название комплекта", "Артикул", "Текстура", "Количество", "Скрыт", "Категория"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(3).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(3).setMaxWidth(80);
            tab1.getColumnModel().getColumn(4).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(4).setMaxWidth(40);
        }

        panCentr.add(scr1, java.awt.BorderLayout.CENTER);

        pan2.setPreferredSize(new java.awt.Dimension(700, 200));
        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);
        scr2.setPreferredSize(new java.awt.Dimension(450, 200));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Артикул", "Название", "Основная текстура", "Внутренняя текстура", "Внешняя текстура", "Основной элемент"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(5).setMaxWidth(60);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(260, 200));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        pan2.add(scr3, java.awt.BorderLayout.EAST);

        panCentr.add(pan2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(700, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 952, Short.MAX_VALUE)
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        qKits.select(eKits.up, "order by", eKits.name);
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?", "Предупреждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                Record kitsRec = qKits.get(getSelectedRec(tab1));
                kitsRec.set(eKits.up, Query.DEL);
                qKits.delete(kitsRec);
                qKits.removeRec(getSelectedRec(tab1));
                ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab1, 0);

            } else if (tab2.getBorder() != null) {
                Record kitdetRc = qKitdet.get(getSelectedRec(tab2));
                kitdetRc.set(eColor.up, Query.DEL);
                qKitdet.delete(kitdetRc);
                qKitdet.removeRec(getSelectedRec(tab2));
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab2, 0);

            } else if (tab3.getBorder() != null) {
                Record kitpar1Rec = qKitpar1.get(getSelectedRec(tab3));
                kitpar1Rec.set(eColpar1.up, Query.DEL);
                qKitpar1.delete(kitpar1Rec);
                qKitpar1.removeRec(getSelectedRec(tab3));
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab3, 0);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            Record record = qKits.newRecord(Query.INS);
            int id = ConnApp.instanc().genId(eKits.up);
            record.setNo(eKits.id, id);
            qKits.add(record);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();

        } else if (tab2.getBorder() != null) {
            int row = getSelectedRec(tab1);
            Record kitsRec = qKits.get(row);
            Record kitdetRec = qKitdet.newRecord(Query.INS);
            kitdetRec.setNo(eKitdet.id, ConnApp.instanc().genId(eKitdet.up));
            kitdetRec.setNo(eKitdet.kits_id, kitsRec.getInt(eKits.id));
            qKitdet.add(kitdetRec);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qKitdet, tab2);

        } else if (tab3.getBorder() != null) {
            int row = getSelectedRec(tab2);
            Record kitdetRec = qKitdet.get(row);
            Record kitpar1Rec = qKitpar1.newRecord(Query.INS);
            kitpar1Rec.setNo(eColpar1.id, ConnApp.instanc().genId(eColpar1.up));
            kitpar1Rec.setNo(eKitpar1.kitdet_id, kitdetRec.getInt(eKitdet.id));
            qKitpar1.add(kitpar1Rec);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qKitpar1, tab3);
        }
    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Util.stopCellEditing(tab1, tab2, tab3);
        Arrays.asList(qKits, qKitdet, qKitpar1).forEach(q -> q.execsql());
    }//GEN-LAST:event_formWindowClosed

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.ButtonGroup btnGroup1;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JRadioButton rbn3;
    private javax.swing.JRadioButton rbtn1;
    private javax.swing.JRadioButton rbtn2;
    private javax.swing.JRadioButton rbtn4;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {
        btnIns.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3));
        btnDel.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3));
        btnRef.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3));
        new FrameToFile(this, btnClose);
        FocusListener listenerFocus = new FocusListener() {

            javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

            public void focusGained(FocusEvent e) {
                Util.stopCellEditing(tab1, tab2, tab3);
                tab1.setBorder(null);
                tab2.setBorder(null);
                tab3.setBorder(null);
                if (e.getSource() instanceof JTable) {
                    ((JTable) e.getSource()).setBorder(border);
                }
            }

            public void focusLost(FocusEvent e) {
            }
        };
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки комплектов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация комплектов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                selectionTab2(event);
            }
        });
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
        tab3.addFocusListener(listenerFocus);
    }
}
