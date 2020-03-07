package forms;

import common.FrameListener;
import common.FrameProgress;
import common.FrameToFile;
import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eColor;
import domain.eColgrp;
import domain.eColpar1;
import domain.eParams;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import swing.DefFieldEditor;
import swing.DefTableModel;

public class Color extends javax.swing.JFrame
        implements FrameListener<DefTableModel, Object> {

    private Component focusComp = null;
    private Query qСolgrup = new Query(eColgrp.id, eColgrp.name, eColgrp.coeff).select(eColgrp.up, "order by", eColgrp.name);
    private Query qColor = new Query(eColor.values());
    private Query qColpar1 = new Query(eColpar1.values(), eParams.values());

    private FocusListener listenerFocus = new FocusListener() {
        javax.swing.border.Border border
                = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

        public void focusGained(FocusEvent e) {
            focusComp = (Component) e.getSource();
            JTable table = (JTable) e.getSource();
            ((JComponent) e.getSource()).setBorder(border);

            btnIns.setEnabled(true);
            btnSave.setEnabled(true);
            if (table == tab1 && tab2.getModel().getRowCount() > 0) {
                btnDel.setEnabled(false);
            } else {
                btnDel.setEnabled(true);
            }
        }

        public void focusLost(FocusEvent e) {
            ((JComponent) e.getSource()).setBorder(null);
        }
    };
    private FrameListener<Object, Object> listenerModify = new FrameListener() {

        Icon[] btnIM = {new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c020.gif")),
            new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"))};

        public void actionRequest(Object obj) {
            btnSave.setIcon(btnIM[0]);
        }

        public void actionResponse(Object obj) {
            btnSave.setIcon(btnIM[1]);
        }
    };
    private FrameListener<Object, Object> listenerDictionary = new FrameListener() {

        public void actionRequest(Object obj) {
            System.out.println(".request()");
        }
    };

    public Color() {
        initComponents();
        initElements();
        initDatamodel();
    }

    private void initDatamodel() {

        new DefTableModel(tab1, qСolgrup, eColgrp.name).addFrameListener(listenerModify);
        new DefTableModel(tab2, qColor, eColor.name, eColor.suffix1, eColor.suffix2, eColor.suffix3).addFrameListener(listenerModify);
        new DefTableModel(tab3, qColpar1, eParams.text, eColpar1.text).addFrameListener(listenerModify);

        JButton btnT3C0 = new JButton("...");
        btnT3C0.addActionListener(event -> remoteForms(event));
        tab3.getColumnModel().getColumn(0).setCellEditor(new DefFieldEditor(this, btnT3C0));

        if (tab1.getRowCount() > 0) {
            tab1.setRowSelectionInterval(0, 0);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        qColor.execsql();
        listenerModify.actionResponse(null);
        int row = tab1.getSelectedRow();
        if (row != -1) {
            Record record = qСolgrup.table(eColgrp.up.tname()).get(row);
            Integer cgrup = record.getInt(eColgrp.id);
            qColor.select(eColor.up, "where", eColor.colgrp_id, "=" + cgrup + "order by", eColor.name);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            if (tab2.getRowCount() > 0) {
                tab2.setRowSelectionInterval(0, 0);
                btnDel.setEnabled(false);
            } else {
                btnDel.setEnabled(true);
            }
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = tab2.getSelectedRow();
        if (row != -1) {
            Record record = qColor.table(eColor.up.tname()).get(row);
            int id = record.getInt(eColor.id);
            qColpar1.select(eColpar1.up, "left join", eParams.up.tname(), "on", eParams.grup, "=",
                    eColpar1.grup, "and", eParams.numb, "= 0", "where", eColpar1.numb, "=", id);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            if (tab3.getRowCount() > 0) {
                tab3.setRowSelectionInterval(0, 0);
                btnDel.setEnabled(false);
            } else {
                btnDel.setEnabled(true);
            }
        }
    }

    public void remoteForms(java.awt.event.ActionEvent evt) {
        
        Query query = new Query(eParams.values()).select(eParams.up,
                "where", eParams.color, "= 1 order by", eParams.text).table(eParams.up.tname());
        eParams.text.meta().descr("Название параметра");
        DicList frame = new DicList(this, listenerDictionary, query, eParams.text);
        FrameToFile.setFrameSize(frame);
        frame.setVisible(true);
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
        btnFilter = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        panWest = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        panCentr = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Текстура");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(740, 600));

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(740, 29));

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
        btnSave.setEnabled(false);
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
        btnDel.setEnabled(false);
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
        btnIns.setEnabled(false);
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

        btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btnFilter.setToolTipText(bundle.getString("Поиск")); // NOI18N
        btnFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFilter.setFocusable(false);
        btnFilter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFilter.setMaximumSize(new java.awt.Dimension(25, 25));
        btnFilter.setMinimumSize(new java.awt.Dimension(25, 25));
        btnFilter.setPreferredSize(new java.awt.Dimension(25, 25));
        btnFilter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterChoice(evt);
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
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportChoice(evt);
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
                .addGap(94, 94, 94)
                .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 456, Short.MAX_VALUE)
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
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panWest.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panWest.setPreferredSize(new java.awt.Dimension(200, 600));
        panWest.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxxx"},
                {"zzzzzzz"}
            },
            new String [] {
                "Название групп"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);

        panWest.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panWest, java.awt.BorderLayout.WEST);

        panCentr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panCentr.setPreferredSize(new java.awt.Dimension(458, 480));
        panCentr.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);
        scr2.setPreferredSize(new java.awt.Dimension(450, 360));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111",  new Integer(1),  new Integer(1),  new Integer(1)},
                {"222",  new Integer(2),  new Integer(2),  new Integer(2)}
            },
            new String [] {
                "Название", "Основная", "Внутрення", "Внешняя"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(260);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(80);
        }

        panCentr.add(scr2, java.awt.BorderLayout.CENTER);

        scr3.setBorder(null);
        scr3.setPreferredSize(new java.awt.Dimension(454, 200));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxx", "xxxxxx"},
                {"zzzzzz", "zzzzzzz"}
            },
            new String [] {
                "Параметр", "Текстура"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        panCentr.add(scr3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(740, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 773, Short.MAX_VALUE)
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
        qСolgrup.select(eColgrp.up, "order by", eColgrp.name);
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        if (tab1.getRowCount() > 0) {
            tab1.setRowSelectionInterval(0, 0);
        }

    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave
        qСolgrup.execsql();
        qColor.execsql();
        listenerModify.actionResponse(null);
    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?", "Предупреждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            try {
                if (focusComp == tab1) {
                    Query query = qСolgrup.table(eColgrp.up.tname());
                    Record record = query.get(tab1.getSelectedRow());
                    query.delete(record);
                    qСolgrup.select(eColgrp.up, "order by", eColgrp.name);
                    ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();

                } else if (focusComp == tab2) {
                    Query query = qColor.table(eColor.up.tname());
                    Record record = query.get(tab2.getSelectedRow());
                    query.delete(record);
                    qColor.select(eColgrp.up, "order by", eColgrp.name);
                    ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (focusComp == tab1) {
            Query query = qСolgrup.table(eColgrp.up.tname());
            Record record = query.newRecord(Query.INS);
            int id = ConnApp.ins().generatorId(eColgrp.up.tname());
            record.setNo(eColgrp.id, id);
            //record.setNo(eTextGrp.groups, id);
            //record.setNo(eTextGrp.gunic, id);
            record.setNo(eColgrp.coeff, 1);
            query.add(record);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();

        } else if (focusComp == tab2) {
            int row = tab1.getSelectedRow();
            Query query1 = qСolgrup.table(eColgrp.up.tname());
            Query query2 = qColor.table(eColor.up.tname());
            Record record1 = query1.get(row);
            Record record2 = query2.newRecord(Query.INS);
            int id = ConnApp.ins().generatorId(eColor.up.tname());
            record2.setNo(eColor.id, id);
            query2.add(record2);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        }
    }//GEN-LAST:event_btnInsert

    private void btnFilterChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterChoice

    }//GEN-LAST:event_btnFilterChoice

    private void btnReportChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportChoice

    }//GEN-LAST:event_btnReportChoice
// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JPanel panWest;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    // End of variables declaration//GEN-END:variables

    private void initElements() {

        new FrameToFile(this, btnClose);
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
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
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Группы текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Описание текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
    }
// </editor-fold> 
}
