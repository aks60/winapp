package forms;

import common.FrameAdapter;
import common.FrameListener;
import common.FrameToFile;
import common.Util;
import static common.Util.scrollRectToVisible;
import dataset.ConnApp;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlaspar2;
import domain.eGlasprof;
import domain.eJoinvar;
import domain.eSysprof;
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

public class Glass extends javax.swing.JFrame {

    private Query qGlasgrp = new Query(eGlasgrp.values());
    private Query qGlasdet = new Query(eGlasdet.values(), eArtikl.values());
    private Query qGlasprof = new Query(eGlasprof.values(), eArtikl.values()).select(eGlasprof.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eGlasprof.artikl_id);
    private Query qGlaspar1 = new Query(eGlaspar1.values()).select(eGlaspar1.up, "order by", eGlaspar1.id);
    private Query qGlaspar2 = new Query(eGlaspar2.values()).select(eGlaspar2.up, "order by", eGlaspar2.id);
    private FrameListener listenerFrame = null;
    private String subsql = "";
    private int nuni = -1;
    private Window owner = null;

    private FocusListener listenerFocus = new FocusListener() {

        javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

        public void focusGained(FocusEvent e) {
            FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            tab1.setBorder(null);
            tab2.setBorder(null);
            tab3.setBorder(null);
            tab4.setBorder(null);
            tab5.setBorder(null);
            if (e.getSource() instanceof JTable) {
                ((JComponent) e.getSource()).setBorder(border);
            }
        }

        public void focusLost(FocusEvent e) {
        }
    };

    public Glass() {
        initComponents();
        initElements();
        loadingQuery();
        initDatamodel();
    }

    public Glass(java.awt.Window owner, int nuni) {
        initComponents();
        initElements();
        this.nuni = nuni;
        this.owner = owner;
        listenerFrame = (FrameListener) owner;
        owner.setEnabled(false);
        loadingQuery();
        initDatamodel();
    }

    private void initDatamodel() {
        new DefTableModel(tab1, qGlasgrp, eGlasgrp.name, eGlasgrp.gap, eGlasgrp.thick);
        new DefTableModel(tab2, qGlasdet, eGlasdet.depth, eArtikl.code, eArtikl.name, eGlasdet.id, eGlasdet.id);
        new DefTableModel(tab3, qGlaspar1, eGlaspar1.grup, eGlaspar1.text);
        new DefTableModel(tab4, qGlaspar2, eGlaspar2.grup, eGlaspar2.text);
        new DefTableModel(tab5, qGlasprof, eGlasprof.sizeax, eArtikl.code, eArtikl.name, eGlasprof.id, eGlasprof.id);
        if (tab1.getRowCount() > 0) {
            tab1.setRowSelectionInterval(0, 0);
        }
    }

    private void loadingQuery() {
        if (owner == null) {
            qGlasgrp.select(eGlasgrp.up, "order by", eGlasgrp.name);
            qGlasdet.select(eGlasdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eGlasdet.artikl_id);
        } else {
            Query query = new Query(eSysprof.artikl_id).select(eSysprof.up, "where", eSysprof.systree_id, "=", nuni).table(eSysprof.up);
            query.stream().forEach(rec -> subsql = subsql + "," + rec.getStr(eSysprof.artikl_id));
            subsql = "(" + subsql.substring(1) + ")";
            qGlasgrp.select(eGlasgrp.up, ",", eGlasprof.up.tname(),
                    "where", eGlasgrp.id, "=", eGlasprof.glasgrp_id, "and", eGlasprof.artikl_id, "in", subsql, "order by", eGlasgrp.name);
            qGlasdet.select(eGlasdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eGlasdet.artikl_id);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        int row = tab1.getSelectedRow();
        if (row != -1) {
            Record record = qGlasgrp.table(eGlasgrp.up).get(row);
            Integer id = record.getInt(eGlasgrp.id);
            qGlasdet.select(eGlasdet.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eGlasdet.artikl_id, "where", eGlasdet.glasgrp_id, "=", id);
            qGlaspar1.select(eGlaspar1.up, "where", eGlaspar1.glasgrp_id, "=", id, "order by", eGlaspar1.id);
            qGlasprof.select(eGlasprof.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eGlasprof.artikl_id, "where", eGlasprof.glasgrp_id, "=", id);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.selectRecord(tab2, 0);
            Util.selectRecord(tab3, 0);
            Util.selectRecord(tab4, 0);
            Util.selectRecord(tab5, 0);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        int row = tab2.getSelectedRow();
        if (row != -1) {
            Record record = qGlasdet.table(eGlasdet.up).get(row);
            Integer id = record.getInt(eJoinvar.id);
            qGlaspar2.select(eGlaspar2.up, "where", eGlaspar2.glasdet_id, "=", id, "order by", eGlaspar2.id);
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            if (tab4.getRowCount() > 0) {
                tab4.setRowSelectionInterval(0, 0);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        panCentr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        tabb1 = new javax.swing.JTabbedPane();
        pan4 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Заполнения");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(800, 29));

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
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 765, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCentr.setPreferredSize(new java.awt.Dimension(800, 560));
        panCentr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(700, 200));
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr1.setPreferredSize(new java.awt.Dimension(400, 200));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Мммммммммм", "1", "111"},
                {"Ррррррррррр", "2", "222"}
            },
            new String [] {
                "Название", "Зазор", "Толщины доступные"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setMaxWidth(60);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(2).setMaxWidth(160);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        scr3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr3.setPreferredSize(new java.awt.Dimension(300, 200));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"kkkkkkkkkkk", "77"},
                {"hhhhhhhhh", "88"}
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
            tab3.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        pan1.add(scr3, java.awt.BorderLayout.EAST);

        panCentr.add(pan1, java.awt.BorderLayout.CENTER);

        tabb1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tabb1.setToolTipText("");
        tabb1.setPreferredSize(new java.awt.Dimension(800, 300));

        pan4.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr2.setPreferredSize(new java.awt.Dimension(500, 300));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"eeeeeeeeee", "22", null, null, null},
                {"mmmmmmm", "44", null, null, null}
            },
            new String [] {
                "Толщина", "Артикул", "Название", "Текстура", "Подбор"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(0).setMaxWidth(120);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(1).setMaxWidth(120);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(3).setMaxWidth(120);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(4).setMaxWidth(120);
        }

        pan4.add(scr2, java.awt.BorderLayout.CENTER);

        scr4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr4.setPreferredSize(new java.awt.Dimension(300, 300));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzz", "aaaaa"},
                {"ccccc", "vvvvv"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ));
        tab4.setFillsViewportHeight(true);
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab4.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        pan4.add(scr4, java.awt.BorderLayout.EAST);

        tabb1.addTab("Спецификация", pan4);

        pan3.setPreferredSize(new java.awt.Dimension(454, 304));
        pan3.setLayout(new java.awt.BorderLayout());

        scr5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr5.setPreferredSize(new java.awt.Dimension(454, 304));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"yyyyyyyy", "fffffffffffffff", "44", "7", "2"},
                {"rrrrrrrrrrr", "pppppppppp", "77", "2", "4"}
            },
            new String [] {
                "Артикул", "Название", "Размер от оси", "Внутреннее", "Внешнее"
            }
        ));
        tab5.setFillsViewportHeight(true);
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(3).setMaxWidth(120);
            tab5.getColumnModel().getColumn(4).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(4).setMaxWidth(120);
        }

        pan3.add(scr5, java.awt.BorderLayout.CENTER);

        tabb1.addTab("Прфили в группе", pan3);

        panCentr.add(tabb1, java.awt.BorderLayout.SOUTH);
        tabb1.getAccessibleContext().setAccessibleName("");

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        loadingQuery();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.selectRecord(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?", "Предупреждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                Record glasgrpRec = qGlasgrp.get(tab1.getSelectedRow());
                glasgrpRec.set(eGlasgrp.up, Query.DEL);
                qGlasgrp.delete(glasgrpRec);
                qGlasgrp.removeRec(tab1.getSelectedRow());
                ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
                Util.selectRecord(tab1, 0);

            } else if (tab2.getBorder() != null) {
                Record glasdetRec = qGlasdet.get(tab2.getSelectedRow());
                glasdetRec.set(eGlasdet.up, Query.DEL);
                qGlasdet.delete(glasdetRec);
                qGlasdet.removeRec(tab2.getSelectedRow());
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.selectRecord(tab2, 0);

            } else if (tab3.getBorder() != null) {
                Record glaspar1Rec = qGlaspar1.get(tab3.getSelectedRow());
                glaspar1Rec.set(eGlaspar1.up, Query.DEL);
                qGlaspar1.delete(glaspar1Rec);
                qGlaspar1.removeRec(tab3.getSelectedRow());
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.selectRecord(tab3, 0);

            } else if (tab4.getBorder() != null) {
                Record glaspar2Rec = qGlaspar2.get(tab4.getSelectedRow());
                glaspar2Rec.set(eGlaspar2.up, Query.DEL);
                qGlaspar2.delete(glaspar2Rec);
                qGlaspar2.removeRec(tab4.getSelectedRow());
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.selectRecord(tab4, 0);

            } else if (tab5.getBorder() != null) {
                Record glasprofRec = qGlasprof.get(tab4.getSelectedRow());
                glasprofRec.set(eGlasprof.up, Query.DEL);
                qGlasprof.delete(glasprofRec);
                qGlasprof.removeRec(tab5.getSelectedRow());
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                Util.selectRecord(tab5, 0);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            Record glasgrpRec = qGlasgrp.newRecord(Query.INS);
            glasgrpRec.setNo(eGlasgrp.id, ConnApp.instanc().genId(eGlasgrp.up));
            qGlasgrp.add(glasgrpRec);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            scrollRectToVisible(qGlasgrp, tab1);

        } else if (tab2.getBorder() != null) {
            int row = tab1.getSelectedRow();
            if (row != -1) {
                Record glasgrpRec = qGlasgrp.get(row);
                Record glasdetRec = qGlasdet.newRecord(Query.INS);
                Record artiklRec = eArtikl.up.newRecord(Query.SEL);
                glasdetRec.setNo(eGlasdet.id, ConnApp.instanc().genId(eGlasdet.up));
                glasdetRec.setNo(eGlasdet.glasgrp_id, glasgrpRec.getInt(eGlasgrp.id));
                qGlasdet.add(glasdetRec);
                qGlasdet.table(eArtikl.up).add(artiklRec);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                scrollRectToVisible(qGlasdet, tab2);
            }
        } else if (tab3.getBorder() != null) {
            int row = tab1.getSelectedRow();
            if (row != -1) {
                Record glasgrpRec = qGlasgrp.get(row);
                Record glaspar1Rec = qGlaspar1.newRecord(Query.INS);
                glaspar1Rec.setNo(eGlaspar1.id, ConnApp.instanc().genId(eGlaspar1.up));
                glaspar1Rec.setNo(eGlaspar1.glasgrp_id, glasgrpRec.getInt(eGlasgrp.id));
                qGlaspar1.add(glaspar1Rec);
                ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qGlaspar1, tab3);
            }
        } else if (tab4.getBorder() != null) {
            int row = tab2.getSelectedRow();
            if (row != -1) {
                Record glasdetRec = qGlasdet.get(row);
                Record glaspar2Rec = qGlaspar2.newRecord(Query.INS);
                glaspar2Rec.setNo(eGlaspar2.id, ConnApp.instanc().genId(eGlaspar2.up));
                glaspar2Rec.setNo(eGlaspar2.glasdet_id, glasdetRec.getInt(eGlasdet.id));
                qGlaspar2.add(glaspar2Rec);
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qGlaspar2, tab4);
            }
        } else if (tab5.getBorder() != null) {
            int row = tab1.getSelectedRow();
            if (row != -1) {
                Record glasgrpRec = qGlasgrp.get(row);
                Record glasprofRec = qGlasprof.newRecord(Query.INS);
                Record artiklRec = eArtikl.up.newRecord(Query.SEL);
                glasprofRec.setNo(eGlasprof.id, ConnApp.instanc().genId(eGlasprof.up));
                glasprofRec.setNo(eGlasprof.glasgrp_id, glasgrpRec.getInt(eGlasgrp.id));
                qGlasprof.add(glasprofRec);
                qGlasprof.table(eArtikl.up).add(artiklRec);
                ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
                scrollRectToVisible(qGlasprof, tab5);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(qGlasgrp, qGlasdet, qGlasprof, qGlaspar1, qGlaspar2, qGlasprof).forEach(q -> q.execsql());
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panNorth;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTabbedPane tabb1;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    
    private void initElements() {

        new FrameToFile(this, btnClose);
        btnIns.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        btnDel.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        btnRef.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2, tab3, tab4, tab5));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Группы заполнений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Спецификация групп заполнений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Профили в группе заполнения", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
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
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
        tab3.addFocusListener(listenerFocus);
        tab4.addFocusListener(listenerFocus);
        tab5.addFocusListener(listenerFocus);
    }
}
