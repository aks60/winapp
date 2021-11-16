package frames.dialog;

import common.listener.ListenerObject;
import frames.FrameToFile;
import frames.UGui;
import dataset.Query;
import dataset.Record;
import domain.eColor;
import domain.eGroups;
import enums.TypeGroups;
import java.awt.Frame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import common.listener.ListenerRecord;
import domain.eArtikl;
import domain.eKitdet;
import domain.eKits;
import domain.eParams;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Дополнительные комплекты
public class DicKits extends javax.swing.JDialog {

    private static final int serialVersionUID = 1343775792;
    private ListenerObject<Query> listener = null;
    private Query qKits = new Query(eKits.values());
    private Query qKitdet = new Query(eKitdet.values());

    public DicKits(Frame parent, ListenerObject<Query> listenerQuery) {
        super(parent, true);
        initComponents();
        initElements();
        this.listener = listenerQuery;
        loadingData("0");
        loadingModel();
        setVisible(true);
    }

    public void loadingData(String type) {
        eArtikl.query();
        qKits.select(eKits.up, "where", eKits.types, "=", type, "order by", eKits.categ, ",", eKits.name);
    }

    private void loadingModel() {
        new DefTableModel(tab1, qKits, eKits.categ, eKits.name);
        new DefTableModel(tab2, qKitdet, eKitdet.artikl_id, eKitdet.artikl_id, eKitdet.color1_id, eKitdet.color2_id, eKitdet.color3_id) {

            public Object getValueAt(int col, int row, Object val) {

                if (val != null && col == 0) {
                    return eArtikl.get((int) val).getStr(eArtikl.code);

                } else if (val != null && col == 1) {
                    return eArtikl.get((int) val).getStr(eArtikl.name);

                } else if (val != null && columns[col] == eKitdet.color1_id) {
                    return eColor.get((int) val).getStr(eColor.name);

                } else if (val != null && columns[col] == eKitdet.color2_id) {
                    return eColor.get((int) val).getStr(eColor.name);

                } else if (val != null && columns[col] == eKitdet.color3_id) {
                    return eColor.get((int) val).getStr(eColor.name);
                }
                return val;
            }
        };
        UGui.setSelectedRow(tab1);
    }

    public void selectionTab1() {
        UGui.clearTable(tab2);
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record record = qKits.get(index);
            Integer id = record.getInt(eKits.id);
            qKitdet.select(eKitdet.up, "where", eKitdet.kits_id, "=", id, "order by", eKitdet.artikl_id);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            UGui.setSelectedRow(tab2);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        cbx1 = new javax.swing.JComboBox<>();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Справочник текстур");
        setPreferredSize(new java.awt.Dimension(612, 609));

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(600, 29));

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

        cbx1.setBackground(new java.awt.Color(212, 208, 200));
        cbx1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbx1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Продажа", "Скатка", "Ламинация", "Стеклопакет" }));
        cbx1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cbx1.setPreferredSize(new java.awt.Dimension(160, 25));
        cbx1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAction(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 261, Short.MAX_VALUE)
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
                            .addComponent(cbx1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(612, 560));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setPreferredSize(new java.awt.Dimension(412, 160));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", "name1"},
                {"222", "name2"}
            },
            new String [] {
                "Категория", "Название комплекта"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        scr2.setPreferredSize(new java.awt.Dimension(454, 304));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Артикул", "Название", "Основная текстура", "Внутренняя текстура", "Внешняя текстура"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(15);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(15);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(15);
        }

        pan1.add(scr2, java.awt.BorderLayout.SOUTH);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(600, 20));
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
                filterCaretUpdate(evt);
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

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice
        listener.action(qKitdet);
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        if (evt.getClickCount() == 2 && tab2.getRowCount() == 0) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tab1MouseClicked

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate
        JTable table = Stream.of(tab1, tab2).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab2);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, Arrays.asList(tab1, tab2));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
        if (evt.getClickCount() == 2) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tabMousePressed

    private void comboBoxAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxAction
        UGui.stopCellEditing(tab1, tab2);
        Arrays.asList(tab1, tab2).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        int index = cbx1.getSelectedIndex();
        loadingData(String.valueOf(index));
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        UGui.setSelectedRow(tab1);
    }//GEN-LAST:event_comboBoxAction

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JComboBox<String> cbx1;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables

    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1();
                }
            }
        });
    }
}
