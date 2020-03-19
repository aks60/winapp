package forms;

import common.FrameAdapter;
import common.FrameListener;
import common.FrameToFile;
import common.Util;
import dataset.ConnApp;
import dataset.Query;
import dataset.Record;
import domain.eParams;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import swing.BooleanRenderer;
import swing.DefTableModel;

public class Param extends javax.swing.JFrame {

    private Query qParams = new Query(eParams.values())
            .select(eParams.up, "where", eParams.grup, "< 0", "and", eParams.numb, "= 0", "order by", eParams.text);
    private Query qPardet = new Query(eParams.values());

    private FocusListener listenerFocus = new FocusListener() {

        javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

        public void focusGained(FocusEvent e) {

            FrameAdapter.stopCellEditing(tab1, tab2);
            tab1.setBorder(null);
            tab2.setBorder(null);
            ((JComponent) e.getSource()).setBorder(border);
        }

        public void focusLost(FocusEvent e) {
        }
    };

    public Param() {
        initComponents();
        initElements();
        initDatamodel();
    }

    private void initDatamodel() {

        new DefTableModel(tab1, qParams, eParams.text, eParams.komp,
                eParams.joint, eParams.elem, eParams.glas, eParams.furn, eParams.otkos, eParams.color);
        new DefTableModel(tab2, qPardet, eParams.text, eParams.komp,
                eParams.joint, eParams.elem, eParams.glas, eParams.furn, eParams.otkos, eParams.color);
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });

        BooleanRenderer br = new BooleanRenderer();
        Arrays.asList(1, 2, 3, 4, 5, 6, 7).forEach(index -> tab1.getColumnModel().getColumn(index).setCellRenderer(br));
        Arrays.asList(1, 2, 3, 4, 5, 6).forEach(index -> tab2.getColumnModel().getColumn(index).setCellRenderer(br));

        if (tab1.getRowCount() > 0) {
            tab1.setRowSelectionInterval(0, 0);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = tab1.getSelectedRow();
        if (row != -1) {
            Record record = qParams.table(eParams.up).get(row);
            Integer p1 = record.getInt(eParams.grup);
            qPardet.select(eParams.up, "where", eParams.grup, "=", p1, "and", eParams.numb, "!= 0", "order by", eParams.text);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            if (tab2.getRowCount() > 0) {
                tab2.setRowSelectionInterval(0, 0);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        panCentr = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        panSouth = new javax.swing.JPanel();

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(454, 563));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Параметры");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(740, 602));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 788, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panNorthLayout.createSequentialGroup()
                        .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panCentr.setPreferredSize(new java.awt.Dimension(454, 563));
        panCentr.setLayout(new java.awt.BorderLayout());

        pan1.setPreferredSize(new java.awt.Dimension(454, 304));
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setPreferredSize(new java.awt.Dimension(454, 304));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzzz", null, null, null, null, null, null, null},
                {"xxxxx", null, null, null, null, null, null, null}
            },
            new String [] {
                "Название параметра", "Комплекты", "Соединения", "Составы", "Заполнения", "Фурнитура", "Откосы", "Текстура"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setMinWidth(300);
            tab1.getColumnModel().getColumn(0).setPreferredWidth(400);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        panCentr.add(pan1, java.awt.BorderLayout.CENTER);

        pan2.setPreferredSize(new java.awt.Dimension(454, 204));
        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setPreferredSize(new java.awt.Dimension(454, 204));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzzz", null, null, null, null, null, null, "1"},
                {"xxxxx", null, null, null, null, null, null, "1"}
            },
            new String [] {
                "Значение параметра", "Комплекты", "Соединения", "Составы", "Заполнения", "Фурнитура", "Откосы", "Надпись"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setMinWidth(300);
            tab2.getColumnModel().getColumn(0).setPreferredWidth(400);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        panCentr.add(pan2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(740, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 920, Short.MAX_VALUE)
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
        qParams.select(eParams.up);
        Util.selectRecord(tab1, 0);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущую запись?",
                "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            if (tab1.getBorder() != null) {
                int row = tab1.getSelectedRow();
                if (row != -1) {
                    Record record = qParams.get(row);
                    record.set(eParams.up, Query.DEL);
                    qParams.delete(record);
                    qParams.removeRec(row);
                    ((DefTableModel) tab1.getModel()).fireTableDataChanged();
                    Util.selectRecord(tab1, 0);
                }
            } else if (tab2.getBorder() != null) {
                int row = tab2.getSelectedRow();
                if (row != -1) {
                    Record record = qPardet.get(row);
                    record.set(eParams.up, Query.DEL);
                    qPardet.delete(record);
                    qPardet.removeRec(row);
                    ((DefTableModel) tab2.getModel()).fireTableDataChanged();
                    Util.selectRecord(tab2, 0);
                }
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Record paramlRec = qParams.newRecord(Query.INS);
            int id = ConnApp.instanc().genId(eParams.up);
            paramlRec.setNo(eParams.id, id);
            paramlRec.setNo(eParams.grup, -1 * id);
            paramlRec.setNo(eParams.numb, 0);
            Arrays.asList(eParams.komp.ordinal(), eParams.joint.ordinal(), eParams.elem.ordinal(), eParams.glas.ordinal(),
                    eParams.furn.ordinal(), eParams.otkos.ordinal(), eParams.color.ordinal()).forEach(index -> paramlRec.set(index, 0));
            qParams.add(paramlRec);
            ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(qParams, tab1);
            
        } else if (tab2.getBorder() != null) {
            int row = tab1.getSelectedRow();
            if (row != -1) {
                Record paramRec = qParams.get(row);
                Record pardetRec = qPardet.newRecord(Query.INS);
                int grup = paramRec.getInt(eParams.grup);
                int id = ConnApp.instanc().genId(eParams.up);
                pardetRec.setNo(eParams.id, id);
                pardetRec.setNo(eParams.grup, grup);
                pardetRec.setNo(eParams.numb, id);
                Arrays.asList(eParams.komp.ordinal(), eParams.joint.ordinal(), eParams.elem.ordinal(), eParams.glas.ordinal(),
                        eParams.furn.ordinal(), eParams.otkos.ordinal(), eParams.color.ordinal()).forEach(index -> pardetRec.set(index, 0));
                qPardet.add(pardetRec);
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                Util.scrollRectToVisible(qPardet, tab2);
            }
        }
    }//GEN-LAST:event_btnInsert

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        FrameAdapter.stopCellEditing(tab1, tab2);
        Arrays.asList(qParams, qPardet).forEach(q -> q.execsql());
    }//GEN-LAST:event_formWindowClosed
// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {
        btnIns.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2));
        btnDel.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2));
        btnRef.addActionListener(l -> FrameAdapter.stopCellEditing(tab1, tab2));
        new FrameToFile(this, btnClose);
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Список параметров", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Значение параметров", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
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
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
    }

    public class CustomBooleanCellRenderer extends JCheckBox implements TableCellRenderer {

        private ImageIcon sad = new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c020.gif"));
        private ImageIcon happy = new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"));

        public CustomBooleanCellRenderer() {
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            if (selected) {
                setIcon(happy);
            } else {
                setIcon(sad);
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                boolean selected = (boolean) value;
                setSelected(selected);
            }
            return this;
        }
    }
}
