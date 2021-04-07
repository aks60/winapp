package frames.dialog;

import common.FrameToFile;
import frames.Util;
import enums.Enam;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eParams;
import enums.ParamList;
import java.awt.CardLayout;
import java.awt.Frame;
import java.util.List;
import java.util.Vector;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import common.ListenerRecord;

public class ParGrup2 extends javax.swing.JDialog {

    private static final int serialVersionUID = 343749179;
    private Frame parent = null;
    private ListenerRecord listener;
    private Query qParams = new Query(eParams.values());
    private Field filter = null;

    public ParGrup2(Frame parent, ListenerRecord listener, Field filter, int... part) {
        super(parent, true);
        initComponents();
        initElements();
        this.parent = parent;
        this.listener = listener;
        this.filter = filter;
        loadingData();
        loadingModel(part);
        setVisible(true);
    }

    private void loadingData() {
        qParams.select(eParams.up, "where", filter.name(), "= 1 and", eParams.id, "=", eParams.params_id, "order by", eParams.text);
    }

    private void loadingModel(int... part) {
        DefaultTableModel dm = (DefaultTableModel) tab1.getModel();
        dm.getDataVector().clear();
        List<List> recordList = new Vector();
        for (Enam el : ParamList.values()) {
            for (int it : part) {
                if (el.pass() == 1 && el.numb() >= it && el.numb() < it + 1000) {

                    List record = new Vector();
                    record.add(el.numb());
                    record.add(el.text());
                    recordList.add(record);
                }
            }
        }
        for (List record : recordList) {
            dm.addRow((Vector) record);
        }
        tab2.setModel(new DefTableModel(tab2, qParams, eParams.id, eParams.id, eParams.text));
        ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
        Util.setSelectedRow(tab2);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnCard1 = new javax.swing.JToggleButton();
        btnCard2 = new javax.swing.JToggleButton();
        south = new javax.swing.JPanel();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Группы параметров");

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(350, 29));

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

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btnRemove.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnRemove.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemov(evt);
            }
        });

        buttonGroup1.add(btnCard1);
        btnCard1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCard1.setSelected(true);
        btnCard1.setText("1");
        btnCard1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnCard1.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCard1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard(evt);
            }
        });

        buttonGroup1.add(btnCard2);
        btnCard2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCard2.setText("2");
        btnCard2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnCard2.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCard2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(btnCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(350, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        centr.setLayout(new java.awt.CardLayout());

        pan1.setName("pan1"); // NOI18N
        pan1.setPreferredSize(new java.awt.Dimension(350, 400));
        pan1.setLayout(new java.awt.BorderLayout());

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "2"},
                {"1", "2"}
            },
            new String [] {
                "numb", "Название"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        centr.add(pan1, "card1");

        pan2.setName("pan2"); // NOI18N
        pan2.setPreferredSize(new java.awt.Dimension(350, 180));
        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "2", "3"},
                {"1", "2", "3"}
            },
            new String [] {
                "grup", "num", "Название "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(0).setMaxWidth(60);
            tab2.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        centr.add(pan2, "card2");

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice
        if (btnCard1.isSelected() == true) {
            Record record = new Record(2);
            record.add(tab1.getModel().getValueAt(Util.getIndexRec(tab1), 0));
            record.add(tab1.getModel().getValueAt(Util.getIndexRec(tab1), 1));
            listener.action(record);
        } else {
            listener.action(qParams.get(Util.getIndexRec(tab2)));
        }
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void btnRemov(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemov
        if (btnCard1.isSelected() == true) {
            Record record = new Record(2);
            record.add(null);
            record.add(null);
            listener.action(record);
        } else {
            listener.action(eParams.up.newRecord());
        }
        this.dispose();
    }//GEN-LAST:event_btnRemov

    private void btnCard(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCard
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn == btnCard1) {
            ((CardLayout) centr.getLayout()).show(centr, "card1");
        } else if (btn == btnCard2) {
            ((CardLayout) centr.getLayout()).show(centr, "card2");
        }
    }//GEN-LAST:event_btnCard

    private void tabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMouseClicked
        if (evt.getClickCount() == 2) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tabMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnCard1;
    private javax.swing.JToggleButton btnCard2;
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRemove;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    // End of variables declaration//GEN-END:variables
// </editor-fold>     
    private void initElements() {

        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры системы", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры пользователя", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
    }
}
