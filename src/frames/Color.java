package frames;

import common.DialogListener;
import common.FrameToFile;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import frames.dialog.ParGrup2v;
import domain.eColor;
import domain.eColpar1;
import domain.eElempar2;
import domain.eGroups;
import domain.eParams;
import enums.TypeGroups;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import startup.Main;
import frames.swing.BooleanRenderer;
import frames.swing.DefTableModel;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

public class Color extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.id, eParams.id, eParams.text);
    private Query qСolgrup = new Query(eGroups.id, eGroups.name);
    private Query qColor = new Query(eColor.values());
    private Query qColpar1 = new Query(eColpar1.values());
    private DialogListener listenerColor;

    public Color() {
        initComponents();
        initElements();
        listenerSet();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    private void loadingData() {
        qParams.select(eParams.up, "where", eParams.color, "= 1 and", eParams.id, "=", eParams.params_id, "order by", eParams.text);
        qСolgrup.select(eGroups.up, "where grup=" + TypeGroups.COLOR.id, "order by", eGroups.name);
    }

    private void loadingModel() {

        new DefTableModel(tab1, qСolgrup, eGroups.name);
        new DefTableModel(tab2, qColor, eColor.id, eColor.name, eColor.coef1, eColor.coef2, eColor.coef3, eColor.is_prod);
        new DefTableModel(tab3, qColpar1, eColpar1.params_id, eColpar1.text, eColpar1.color_id2) {
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eColpar1.params_id) {
                    Record record = qParams.stream().filter(rec -> rec.get(eParams.id).equals(val)).findFirst().orElse(eParams.up.newRecord());
                    return (Main.dev) ? record.getStr(eElempar2.id) + "-" + record.getStr(eElempar2.text) : record.getStr(eElempar2.text);
                } 
                return val;
            }
        };

        tab2.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                int rgb = qColor.getAs(row, eColor.rgb);
                lab.setBackground(new java.awt.Color(rgb));
                return lab;
            }
        });
        tab2.getColumnModel().getColumn(5).setCellRenderer(new BooleanRenderer());

        Util.setSelectedRow(tab1);
    }

    private void listenerAdd() {
        
        Util.buttonEditorCell(tab3, 0).addActionListener(event -> {
            ParGrup2v frame = new ParGrup2v(this, listenerColor, eParams.color);
        });
        
        Util.buttonEditorCell(tab3, 1).addActionListener(event -> {
            ParGrup2v frame = new ParGrup2v(this, listenerColor, eParams.color);
        });
    }

    public void listenerSet() {

        listenerColor = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3);
            int row = Util.getSelectedRec(tab3);            
            Record record2 = qColpar1.get(row);
            record2.set(eColpar1.params_id, record.getInt(eParams.params_id));
            record2.set(eColpar1.text, record.getStr(eParams.text)); 
            qColpar1.update(record2);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3, row);            
        };
    }
    
    public static void setDefaultTableEditorsClicks(JTable table, int clickCountToStart) {
        TableCellEditor editor;
        editor = table.getDefaultEditor(Object.class);
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor) editor).setClickCountToStart(clickCountToStart);
        }
        editor = table.getDefaultEditor(Number.class);
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor) editor).setClickCountToStart(clickCountToStart);
        }
        editor = table.getDefaultEditor(Boolean.class);
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor) editor).setClickCountToStart(clickCountToStart);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        Util.stopCellEditing(tab1, tab2, tab3);
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record record = qСolgrup.table(eGroups.up).get(row);
            Integer cgrup = record.getInt(eGroups.id);
            qColor.select(eColor.up, "where", eColor.colgrp_id, "=" + cgrup + "order by", eColor.name);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        Util.stopCellEditing(tab1, tab2, tab3);
        int row = Util.getSelectedRec(tab2);
        if (row != -1) {
            Record colorRec = qColor.table(eColor.up).get(row);
            int id = colorRec.getInt(eColor.id);
            qColpar1.select(eColpar1.up, "where", eColpar1.color_id1, "=", id);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        west = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        centr = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        soutch = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        south2 = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();
        west2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Текстура");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Color.this.windowClosed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 697, Short.MAX_VALUE)
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
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        west.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        west.setPreferredSize(new java.awt.Dimension(200, 400));
        west.setLayout(new java.awt.BorderLayout());

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
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);

        west.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(west, java.awt.BorderLayout.WEST);

        centr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        centr.setPreferredSize(new java.awt.Dimension(700, 400));
        centr.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "111", null, null, null, null, null},
                {null, "222", null, null, null, null, null}
            },
            new String [] {
                "Код текстуры", "Название", "Коэф.(основн.текстура)", "Коэф.(внутр.текстура)", "Коэф.(внешн.текстура)", "Для изделий", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
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
            tab2.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(0).setMaxWidth(120);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(260);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(4).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(5).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(5).setMaxWidth(120);
            tab2.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        centr.add(scr2, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        soutch.setPreferredSize(new java.awt.Dimension(900, 200));
        soutch.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(null);

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"xxxxx", "xxxxxx", null, null},
                {"zzzzzz", "zzzzzzz", null, null}
            },
            new String [] {
                "Параметр", "Текстура", "Цвет", "ID"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(200);
            tab3.getColumnModel().getColumn(3).setMaxWidth(40);
        }

        soutch.add(scr3, java.awt.BorderLayout.CENTER);

        south2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south2.setMinimumSize(new java.awt.Dimension(100, 20));
        south2.setPreferredSize(new java.awt.Dimension(900, 20));
        south2.setLayout(new javax.swing.BoxLayout(south2, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
        labFilter.setMaximumSize(new java.awt.Dimension(100, 14));
        labFilter.setMinimumSize(new java.awt.Dimension(100, 14));
        labFilter.setPreferredSize(new java.awt.Dimension(100, 14));
        south2.add(labFilter);

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
        south2.add(txtFilter);

        checkFilter.setText("в конце строки");
        south2.add(checkFilter);

        soutch.add(south2, java.awt.BorderLayout.SOUTH);

        west2.setPreferredSize(new java.awt.Dimension(300, 180));

        javax.swing.GroupLayout west2Layout = new javax.swing.GroupLayout(west2);
        west2.setLayout(west2Layout);
        west2Layout.setHorizontalGroup(
            west2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        west2Layout.setVerticalGroup(
            west2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        soutch.add(west2, java.awt.BorderLayout.WEST);

        getContentPane().add(soutch, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab2) == 0) {
                Util.deleteRecord(tab1, eGroups.up);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab3) == 0) {
                Util.deleteRecord(tab2, eColor.up);
            }
        } else if (tab3.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab3, eColpar1.up);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Record record = Util.insertRecord(tab1, eGroups.up);
            record.set(eGroups.val, 1);

        } else if (tab2.getBorder() != null) {
            Util.insertRecord(tab1, tab2, eGroups.up, eColor.up, eColor.colgrp_id);

        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab2, tab3, eColor.up, eColpar1.up, eColpar1.color_id1);
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2, tab3);
        Arrays.asList(tab1, tab2, tab3).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
    }//GEN-LAST:event_windowClosed

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1, tab2, tab3));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab1, tab2, tab3).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JPanel soutch;
    private javax.swing.JPanel south2;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTextField txtFilter;
    private javax.swing.JPanel west;
    private javax.swing.JPanel west2;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    
    private void initElements() {

        new FrameToFile(this, btnClose);
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
                "Группы текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Описание текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры текстур", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
    }
}
