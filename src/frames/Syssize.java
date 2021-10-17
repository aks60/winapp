package frames;

import dataset.Conn;
import dataset.Query;
import dataset.Record;
import domain.eCurrenc;
import domain.eSyssize;
import frames.swing.DefCellEditor;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import java.util.Arrays;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import frames.swing.listener.ListenerRecord;

public class Syssize extends javax.swing.JFrame {

    private ListenerRecord listener = null;
    private Window owner = null;
    private Query qSyssize = new Query(eSyssize.values());

    public Syssize() {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        btnChoice.setVisible(false);
    }

    public Syssize(Frame owner, ListenerRecord listener, int id) {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        this.owner = owner;
        owner.setEnabled(false);
        this.listener = listener;
        for (int i = 0; i < qSyssize.size(); i++) {
            if (qSyssize.get(i).getInt(eSyssize.id) == id) {
                UGui.setSelectedIndex(tab1, i);
            }
        }
        btnChoice.setVisible(true);
        setVisible(true);
    }

    public void loadingData() {
        qSyssize.select(eSyssize.up, "order by", eSyssize.name);
    }

    public void loadingModel() {
        new DefTableModel(tab1, qSyssize, eSyssize.name, eSyssize.prip, eSyssize.naxl, eSyssize.zax, eSyssize.falz);
        tab1.getColumnModel().getColumn(1).setCellEditor(new DefCellEditor(3));
        tab1.getColumnModel().getColumn(2).setCellEditor(new DefCellEditor(3));
        tab1.getColumnModel().getColumn(3).setCellEditor(new DefCellEditor(3));
        tab1.getColumnModel().getColumn(4).setCellEditor(new DefCellEditor(3));
        UGui.setSelectedRow(tab1);
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
        south = new javax.swing.JPanel();
        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Системные константы");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Syssize.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(700, 29));

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
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveert(evt);
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
                .addGap(103, 103, 103)
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 399, Short.MAX_VALUE)
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

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        centr.setPreferredSize(new java.awt.Dimension(700, 400));
        centr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Система артикулов", "Припуск на сварку", "Нахлест створки", "Заход импоста", "Наплав системы", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Syssize.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setMinWidth(200);
            tab1.getColumnModel().getColumn(0).setPreferredWidth(200);
            tab1.getColumnModel().getColumn(5).setMaxWidth(40);
        }

        centr.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        qSyssize.execsql();
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(this) == 0) {
                UGui.deleteRecord(tab1);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab1.getBorder() != null) {
            UGui.insertRecord(tab1, eSyssize.up, (record) -> {
            });
        }
    }//GEN-LAST:event_btnInsert

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            listener.action(qSyssize.get(index));
        }
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        UGui.stopCellEditing(tab1);
        qSyssize.execsql();
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

    private void btnRemoveert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveert
        listener.action(eSyssize.up.newRecord());
    }//GEN-LAST:event_btnRemoveert

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, Arrays.asList(tab1));
    }//GEN-LAST:event_mousePressed

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnRemove;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    // End of variables declaration//GEN-END:variables
// </editor-fold>    

    public void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> UGui.stopCellEditing(tab1)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Системные константы", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.UGui.getFont(0, 0)));
    }
}
