package frames;

import dataset.Conn;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eRulecalc;
import enums.TypeArtikl;
import enums.TypeForm;
import frames.dialog.DicArtikl2;
import frames.dialog.DicEnums;
import frames.swing.DefCellEditor;
import frames.swing.DefTableModel;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import frames.swing.listener.ListenerRecord;

public class Rulecalc extends javax.swing.JFrame {

    private Query qRulecalc = new Query(eRulecalc.values(), eArtikl.values());
    private ListenerRecord listenerArtikl, listenerForm;

    public Rulecalc() {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        listenerSet();
    }

    private void loadingData() {
        qRulecalc.select(eRulecalc.up, "left join", eArtikl.up, "on", eArtikl.id, "=", eRulecalc.artikl_id, "order by", eRulecalc.type);
    }

    private void loadingModel() {
        new DefTableModel(tab2, qRulecalc, eRulecalc.name, eRulecalc.type, eArtikl.code, eArtikl.name, eRulecalc.quant, eRulecalc.size,
                eRulecalc.coeff, eRulecalc.incr, eRulecalc.color1, eRulecalc.color2, eRulecalc.color3, eRulecalc.form) {

            public Object getValueAt(int col, int row, Object val) {

                if (val != null) {
                    Field field = columns[col];
                    if (eRulecalc.type == field) {
                        int val2 = Integer.valueOf(val.toString());
                        return TypeArtikl.find(val2 / 100, 0) + "." + TypeArtikl.find(val2 / 100, val2 % 10);

                    } else if (eRulecalc.form == field) {
                        int val2 = (val.equals(0) == true) ? 1 : Integer.valueOf(val.toString());
                        return TypeForm.P00.find(val2).text();
                    }
                }
                return val;
            }
        };

        tab2.getColumnModel().getColumn(4).setCellEditor(new DefCellEditor(6));
        tab2.getColumnModel().getColumn(5).setCellEditor(new DefCellEditor(5));
        tab2.getColumnModel().getColumn(6).setCellEditor(new DefCellEditor(3));
        tab2.getColumnModel().getColumn(7).setCellEditor(new DefCellEditor(3));
        tab2.getColumnModel().getColumn(8).setCellEditor(new DefCellEditor(5));
        tab2.getColumnModel().getColumn(9).setCellEditor(new DefCellEditor(5));
        tab2.getColumnModel().getColumn(10).setCellEditor(new DefCellEditor(5));

        Uti4.buttonCellEditor(tab2, 1).addActionListener(event -> {
            DicArtikl2 frame = new DicArtikl2(this, (artiklRec) -> {
                Uti4.stopCellEditing(tab2);
                int val = artiklRec.getInt(eArtikl.level1) * 100 + artiklRec.getInt(eArtikl.level2);
                qRulecalc.set(val, Uti4.getIndexRec(tab2), eRulecalc.type);
                ((DefaultTableModel) tab2.getModel()).fireTableRowsUpdated(tab2.getSelectedRow(), tab2.getSelectedRow());
            }, 1, 2, 3, 4, 5);
        });

        Uti4.buttonCellEditor(tab2, 2).addActionListener(event -> {
            int type = qRulecalc.getAs(tab2.getSelectedRow(), eRulecalc.type);
            int[] arr = (type == -1) ? new int[]{1, 2, 3, 4, 5} : new int[]{type / 100};
            new DicArtikl2(this, listenerArtikl, arr);
        });

        Uti4.buttonCellEditor(tab2, 3).addActionListener(event -> {
            int type = qRulecalc.getAs(tab2.getSelectedRow(), eRulecalc.type);
            int[] arr = (type == -1) ? new int[]{1, 2, 3, 4, 5} : new int[]{type / 100};
            new DicArtikl2(this, listenerArtikl, arr);
        });

        Uti4.buttonCellEditor(tab2, 11).addActionListener(event -> {
            new DicEnums(this, (record) -> Uti4.listenerEnums(record, tab2, eRulecalc.form, tab2), TypeForm.values());
        });

        Uti4.setSelectedRow(tab2);
    }

    private void listenerSet() {

        listenerArtikl = (arttiklRec) -> {
            int index = Uti4.getIndexRec(tab2);
            Uti4.stopCellEditing(tab2);
            qRulecalc.table(eRulecalc.up).set(arttiklRec.getInt(eArtikl.id), index, eRulecalc.artikl_id);
            qRulecalc.table(eArtikl.up).set(arttiklRec.get(eArtikl.code), index, eArtikl.code);
            qRulecalc.table(eArtikl.up).set(arttiklRec.get(eArtikl.name), index, eArtikl.name);
            if ("null".equals(qRulecalc.getAs(index, eRulecalc.name, "null"))) {
                qRulecalc.table(eRulecalc.up).set(arttiklRec.getStr(eArtikl.name), index, eRulecalc.name);
            }
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Правило расчёта");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Rulecalc.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(900, 29));

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

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport.setFocusable(false);
        btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
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
                .addGap(148, 148, 148)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 479, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));
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

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setPreferredSize(new java.awt.Dimension(900, 550));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(602, 565));
        pan1.setLayout(new java.awt.BorderLayout());

        scr2.setPreferredSize(new java.awt.Dimension(454, 320));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxx", null, "ttt", "erertet", "1", "1", null,  new Double(1.0), "1", "1", "1", null,  new Integer(1)},
                {"vvv", null, "reee", "ertewr", "1", "1", null,  new Double(1.0), "1", "1", "1", null,  new Integer(1)}
            },
            new String [] {
                "Название правила", "Использование", "Артикул", "Название", "Количество", "Габариты", "Коэффициент", "Надбавка", "Базовая текстура", "Внутр. текстура", "Внешн. текстура", "Форма позиции", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab2tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setMinWidth(102);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(160);
            tab2.getColumnModel().getColumn(6).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(7).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(8).setPreferredWidth(72);
            tab2.getColumnModel().getColumn(9).setPreferredWidth(72);
            tab2.getColumnModel().getColumn(10).setPreferredWidth(72);
            tab2.getColumnModel().getColumn(11).setPreferredWidth(180);
            tab2.getColumnModel().getColumn(12).setMaxWidth(40);
        }

        pan1.add(scr2, java.awt.BorderLayout.CENTER);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Uti4.stopCellEditing(tab2);
        qRulecalc.execsql();
        loadingData();
        ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        Uti4.setSelectedRow(tab2);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (Uti4.isDeleteRecord(tab2, this) == 0) {
            Uti4.deleteRecord(tab2);
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        if (tab2.getBorder() != null) {
            Record rulecalcRec = eRulecalc.up.newRecord(Query.INS);
            Record artiklRec = eArtikl.up.newRecord();
            rulecalcRec.setNo(eRulecalc.id, Conn.instanc().genId(eRulecalc.up));
            rulecalcRec.setNo(eRulecalc.id, Conn.instanc().genId(eRulecalc.up));
            qRulecalc.add(rulecalcRec);
            qRulecalc.table(eArtikl.up).add(artiklRec);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Uti4.scrollRectToIndex(qRulecalc.size() - 1, tab2);
        }
    }//GEN-LAST:event_btnInsert

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab2).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab2);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate

    private void tab2tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2tabMousePressed

        JTable table = (JTable) evt.getSource();
        Uti4.updateBorderAndSql(table, Arrays.asList(tab2));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tab2tabMousePressed

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Uti4.stopCellEditing(tab2);
        qRulecalc.execsql();
    }//GEN-LAST:event_windowClosed

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab2;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
        labFilter.setText(tab2.getColumnName(0));
        txtFilter.setName(tab2.getName());
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Uti4.stopCellEditing(tab2)));
    }
}
