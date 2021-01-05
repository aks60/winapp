package frames;

import common.EditorListener;
import common.FrameToFile;
import dataset.ConnApp;
import dataset.Query;
import dataset.Record;
import domain.eParams;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.BooleanRenderer;
import frames.swing.DefCellEditor;
import frames.swing.DefTableModel;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.RowFilter;

public class Param extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.values());
    private Query qPardet = new Query(eParams.values());
    private EditorListener listenerEditor;

    public Param() {
        initComponents();
        initElements();
        loadData();
        loadingModel();
        lstenerAdd();
    }

    private void loadData() {
        qParams.select(eParams.up, "where", eParams.id, "=", eParams.params_id, "order by", eParams.text);
    }

    private void loadingModel() {

        new DefTableModel(tab1, qParams, eParams.text, eParams.komp,
                eParams.joint, eParams.elem, eParams.glas, eParams.furn, eParams.otkos, eParams.color);
        new DefTableModel(tab2, qPardet, eParams.text, eParams.komp,
                eParams.joint, eParams.elem, eParams.glas, eParams.furn, eParams.otkos, eParams.label);

        BooleanRenderer br = new BooleanRenderer();
        Arrays.asList(1, 2, 3, 4, 5, 6, 7).forEach(index -> tab1.getColumnModel().getColumn(index).setCellRenderer(br));
        Arrays.asList(1, 2, 3, 4, 5, 6).forEach(index -> tab2.getColumnModel().getColumn(index).setCellRenderer(br));

        if (tab1.getRowCount() > 0) {
            tab1.setRowSelectionInterval(0, 0);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record record = qParams.table(eParams.up).get(row);
            Integer p1 = record.getInt(eParams.id);
            qPardet.select(eParams.up, "where", eParams.params_id, "=", p1, "and", eParams.id, "!=", eParams.params_id, "order by", eParams.text);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            if (tab2.getRowCount() > 0) {
                tab2.setRowSelectionInterval(0, 0);
            }
        }
    }

    private void lstenerAdd() {
        
        listenerEditor = (component) -> {
            DefCellEditor editor = (DefCellEditor) component;
            editor.getButton().setVisible(true);
            System.out.println("XXXXXXXXXXXXXX");
            return false;
        };
        
        JButton btn = new JButton("...");
        DefCellEditor editor = new DefCellEditor(listenerEditor, btn);
        tab2.getColumnModel().getColumn(0).setCellEditor(editor);

        btn.addActionListener(event -> {
            System.out.println("VVVVVVVVVVVV");
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        checkFilter = new javax.swing.JCheckBox();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(454, 563));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Параметры");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Param.this.windowClosed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 788, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(900, 550));
        centr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(454, 304));
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setPreferredSize(new java.awt.Dimension(454, 304));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzzz", null, null, null, null, null, null, null, null},
                {"xxxxx", null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Название параметра", "Комплекты", "Соединения", "Составы", "Заполнения", "Фурнитура", "Откосы", "Текстура", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setMinWidth(300);
            tab1.getColumnModel().getColumn(0).setPreferredWidth(400);
            tab1.getColumnModel().getColumn(8).setMaxWidth(40);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        centr.add(pan1, java.awt.BorderLayout.CENTER);

        pan2.setPreferredSize(new java.awt.Dimension(454, 204));
        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setPreferredSize(new java.awt.Dimension(454, 204));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzzz", null, null, null, null, null, null, "1", null},
                {"xxxxx", null, null, null, null, null, null, "1", null}
            },
            new String [] {
                "Значение параметра", "Комплекты", "Соединения", "Составы", "Заполнения", "Фурнитура", "Откосы", "Надпись", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setMinWidth(300);
            tab2.getColumnModel().getColumn(0).setPreferredWidth(400);
            tab2.getColumnModel().getColumn(8).setMaxWidth(40);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        centr.add(pan2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

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

        checkFilter.setText("в конце строки");
        south.add(checkFilter);

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

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        qParams.select(eParams.up);
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?",
                "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                int row = Util.getSelectedRec(tab1);
                if (row != -1) {
                    Record record = qParams.get(row);
                    record.set(eParams.up, Query.DEL);
                    qParams.delete(record);
                    qParams.removeRec(row);
                    ((DefTableModel) tab1.getModel()).fireTableDataChanged();
                    Util.setSelectedRow(tab1);
                }
            } else if (tab2.getBorder() != null) {
                int row = Util.getSelectedRec(tab2);
                if (row != -1) {
                    Record record = qPardet.get(row);
                    record.set(eParams.up, Query.DEL);
                    qPardet.delete(record);
                    qPardet.removeRec(row);
                    ((DefTableModel) tab2.getModel()).fireTableDataChanged();
                    Util.setSelectedRow(tab2);
                }
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Record paramlRec = eParams.up.newRecord(Query.INS);
            int id = ConnApp.instanc().genId(eParams.up);
            paramlRec.setNo(eParams.id, id);
            paramlRec.setNo(eParams.id, -1 * id);
            //paramlRec.setNo(eParams.numb, 0);
            Arrays.asList(eParams.komp.ordinal(), eParams.joint.ordinal(), eParams.elem.ordinal(), eParams.glas.ordinal(),
                    eParams.furn.ordinal(), eParams.otkos.ordinal(), eParams.color.ordinal()).forEach(index -> paramlRec.set(index, 0));
            qParams.add(paramlRec);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qParams, tab1);

        } else if (tab2.getBorder() != null) {
            int row = Util.getSelectedRec(tab1);
            if (row != -1) {
                Record paramRec = qParams.get(row);
                Record pardetRec = eParams.up.newRecord(Query.INS);
                int grup = paramRec.getInt(eParams.id);
                int id = ConnApp.instanc().genId(eParams.up);
                pardetRec.setNo(eParams.id, id);
                pardetRec.setNo(eParams.id, grup);
                //pardetRec.setNo(eParams.numb, id);
                Arrays.asList(eParams.komp.ordinal(), eParams.joint.ordinal(), eParams.elem.ordinal(), eParams.glas.ordinal(),
                        eParams.furn.ordinal(), eParams.otkos.ordinal(), eParams.color.ordinal()).forEach(index -> pardetRec.set(index, 0));
                qPardet.add(pardetRec);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qPardet, tab2);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2);
        Arrays.asList(qParams, qPardet).forEach(q -> q.execsql());
    }//GEN-LAST:event_windowClosed

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1, tab2));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab1, tab2).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
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
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {
        btnIns.addActionListener(l -> Util.stopCellEditing(tab1, tab2));
        btnDel.addActionListener(l -> Util.stopCellEditing(tab1, tab2));
        btnRef.addActionListener(l -> Util.stopCellEditing(tab1, tab2));
        new FrameToFile(this, btnClose);
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Список параметров", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Значение параметров", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    //selectionTab1(event);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    //selectionTab2(event);
                }
            }
        });
    }
}
