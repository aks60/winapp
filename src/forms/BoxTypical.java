package forms;

import common.FrameListener;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eElement;
import domain.eElemgrp;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class BoxTypical extends javax.swing.JFrame {

    private FocusListener listenerFocus = new FocusListener() {
    
        javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof JTable) {
                ((JTable) e.getSource()).setBorder(border);
            }
        }

        public void focusLost(FocusEvent e) {
            if (e.getSource() instanceof JTable) {
                ((JTable) e.getSource()).setBorder(null);
            }
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

    public BoxTypical() {
        initComponents();
        initElements();
        loadDataTab1();
    }

    private void loadDataTab1() {

//        Record record = qElemgrp.table(eElemgrp.up.tname()).newRecord(Query.SEL);
//        record.setNo(eElemgrp.id, -1);
//        record.setNo(eElemgrp.name, "<html><font size='3' color='red'>&nbsp;&nbsp;&nbsp;ПРОФИЛИ</font>");
//        qElemgrp.table(eElemgrp.up.tname()).add(0, record);
//        for (int index = 0; index < qElemgrp.table(eElemgrp.up.tname()).size(); ++index) {
//            int level = qElemgrp.table(eElemgrp.up.tname()).getAs(index, eElemgrp.level, -1);
//            if (level == 5) {
//                Record record2 = qElemgrp.table(eElemgrp.up.tname()).newRecord(Query.SEL);
//                record2.setNo(eElemgrp.id, -5);
//                record2.setNo(eElemgrp.name, "<html><font size='3' color='red'>&nbsp;&nbsp;ЗАПОЛНЕНИЯ</font>");
//                qElemgrp.table(eElemgrp.up.tname()).add(index, record2);
//                break;
//            }
//        }
    }

    private void selectionTab1(ListSelectionEvent event) {
//        qElement.execsql();
//        qElemdet.execsql();
//        listenerModify.response(null);
//        int row = tab1.getSelectedRow();
//        if (row != -1) {
//            Record record = qElemgrp.table(eElemgrp.up.tname()).get(row);
//            Integer id = record.getInt(eElemgrp.id);
//            if (id == -1) {
//                qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
//                        "left join", eElemgrp.up, "on", eElemgrp.id, "=", eElement.elemgrp_id, "where", eElemgrp.level, "=1");
//            } else if (id == -5) {
//                qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
//                        "left join", eElemgrp.up, "on", eElemgrp.id, "=", eElement.elemgrp_id, "where", eElemgrp.level, "=5");
//            } else {
//                qElement.select(eElement.up, "left join", eArtikl.up, "on", eElement.artikl_id, "=", eArtikl.id,
//                        "where", eElement.elemgrp_id, "=", id, "order by", eElement.name);
//            }
//            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
//            if (tab2.getRowCount() > 0) {
//                tab2.setRowSelectionInterval(0, 0);
//            }
//        }
    }    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        panSouth = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Типовые конструкции фиртуальных профилей");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(800, 600));

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
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 591, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 754, Short.MAX_VALUE)
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

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    // End of variables declaration//GEN-END:variables
    private void initElements() {
//        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
//                "Категории составов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
//        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
//                "Списки составов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
//        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
//                "Спецификация составов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
//        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
//                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
//        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
//                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
//        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent event) {
//                selectionTab1(event);
//            }
//        });
//        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent event) {
//                selectionTab2(event);
//            }
//        });
//        tab3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent event) {
//                selectionTab3(event);
//            }
//        });
//        tab1.addFocusListener(listenerFocus);
//        tab2.addFocusListener(listenerFocus);
//        tab3.addFocusListener(listenerFocus);
//        tab4.addFocusListener(listenerFocus);
//        tab5.addFocusListener(listenerFocus);        
    }
// </editor-fold>
}
