package frames.dialog;

import frames.FrameToFile;
import frames.UGui;
import dataset.Query;
import dataset.Record;
import domain.eGroups;
import enums.Enam;
import enums.TypeGroups;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import frames.swing.DefTableModel;
import java.awt.CardLayout;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JToggleButton;
import frames.swing.listener.ListenerRecord;

public class DicGroups extends javax.swing.JDialog {

    private static final int serialVersionUID = 1095563562;
    private Enam grup = null;
    private ListenerRecord listener = null;
    private Query qGroups = new Query(eGroups.values());
    private int ID = 0;

    public DicGroups(java.awt.Frame parent, ListenerRecord listenet, Enam grup, int id) {
        super(parent, true);
        this.grup = grup;
        this.ID = id;
        this.listener = listenet;
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        setVisible(true);
    }

    public void loadingData() {
        qGroups.select(eGroups.up, "where grup =", grup.numb(), "order by", eGroups.name);
    }

    public void loadingModel() {
        if (grup.numb() == TypeGroups.SERI_PROF.id) {
            setTitle("Серии профилей");
            ((CardLayout) centr.getLayout()).show(centr, "pan1");
            tab1.setModel(new DefTableModel(tab1, qGroups, eGroups.name));
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            setSelectedRow(tab1);
        } else if (grup.numb() == TypeGroups.PRICE_INC.id) {
            setTitle("Группы наценок");
            ((CardLayout) centr.getLayout()).show(centr, "pan2");
            tab2.setModel(new DefTableModel(tab2, qGroups, eGroups.name, eGroups.val));
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            setSelectedRow(tab2);
        } else if (grup.numb() == TypeGroups.PRICE_DEC.id) {
            setTitle("Группы скидок");
            ((CardLayout) centr.getLayout()).show(centr, "pan3");
            tab3.setModel(new DefTableModel(tab3, qGroups, eGroups.name, eGroups.val));
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            setSelectedRow(tab3);
        } else if (grup.numb() == TypeGroups.CATEG_PRF.id) {
            setTitle("Группы фильтров");
            ((CardLayout) centr.getLayout()).show(centr, "pan4");
            tab4.setModel(new DefTableModel(tab4, qGroups, eGroups.name));
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            setSelectedRow(tab4);
        }
    }

    public void setSelectedRow(JTable tab) {
        if (ID > 0) {
            for (int i = 0; i < qGroups.size(); ++i) {
                if (qGroups.get(i).getInt(eGroups.id) == ID) {
                    UGui.setSelectedIndex(tab, i);
                    UGui.scrollRectToRow(i, tab);
                }
            }
        } else {
            UGui.setSelectedRow(tab);
        }
        UGui.updateBorderAndSql(tab, null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan4 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Справочник");

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(300, 29));

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
                btnRemove(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 211, Short.MAX_VALUE)
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
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(300, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setMaximumSize(new java.awt.Dimension(20, 14));
        labFilter.setMinimumSize(new java.awt.Dimension(20, 14));
        labFilter.setPreferredSize(new java.awt.Dimension(20, 14));
        south.add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(80, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(80, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(80, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFilterCaretUpdate(evt);
            }
        });
        south.add(txtFilter);

        checkFilter.setText("в конце строки");
        south.add(checkFilter);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        centr.setName("card"); // NOI18N
        centr.setPreferredSize(new java.awt.Dimension(300, 440));
        centr.setLayout(new java.awt.CardLayout());

        pan1.setName(""); // NOI18N
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Nmae 0"},
                {"Name 0"}
            },
            new String [] {
                "Наименование"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

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
        });
        scr1.setViewportView(tab1);

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        centr.add(pan1, "pan1");

        pan2.setName(""); // NOI18N
        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Nmae 0", null},
                {"Name 0", null}
            },
            new String [] {
                "Наименование", "Наценка коеф."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab2mouse2Clicked(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        centr.add(pan2, "pan2");

        pan3.setName(""); // NOI18N
        pan3.setLayout(new java.awt.BorderLayout());

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Наименование", "Скидка %"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        pan3.add(scr3, java.awt.BorderLayout.CENTER);

        centr.add(pan3, "pan3");

        pan4.setName("pan4"); // NOI18N
        pan4.setLayout(new java.awt.BorderLayout());

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Наименование"
            }
        ));
        tab4.setFillsViewportHeight(true);
        tab4.setName("tab4"); // NOI18N
        scr4.setViewportView(tab4);

        pan4.add(scr4, java.awt.BorderLayout.CENTER);

        centr.add(pan4, "pan4");

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);
        centr.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose

        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice

        int index = -1;
        if (tab1.getBorder() != null) {
            index = UGui.getIndexRec(tab1);
        } else if (tab2.getBorder() != null) {
            index = UGui.getIndexRec(tab2);
        } else if (tab3.getBorder() != null) {
            index = UGui.getIndexRec(tab3);
        } else if (tab4.getBorder() != null) {
            index = UGui.getIndexRec(tab4);
        }
        if (index != -1) {
            listener.action(qGroups.get(index));
            this.dispose();
        }

    }//GEN-LAST:event_btnChoice

    private void btnRemove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove
        Record record = eGroups.up.newRecord();
        listener.action(record);
        this.dispose();
    }//GEN-LAST:event_btnRemove

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        if (evt.getClickCount() == 2) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tab1MouseClicked

    private void txtFilterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilterCaretUpdate

        JTable table = Stream.of(tab1, tab2, tab2, tab3).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
        if (txtFilter.getText().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            sorter.setRowFilter(RowFilter.regexFilter(text, 0));
        }
    }//GEN-LAST:event_txtFilterCaretUpdate

    private void tab2mouse2Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2mouse2Clicked
        if (evt.getClickCount() == 2) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tab2mouse2Clicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRemove;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
    
    public void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
    }
}
