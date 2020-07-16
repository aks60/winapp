package frames;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import common.FrameToFile;
import common.Util;
import common.eProperty;
import dataset.Record;
import domain.eSysprod;
import domain.eSystree;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import frames.swing.DefTableModel;
import estimate.Wincalc;
import estimate.constr.Specification;
import javax.swing.JOptionPane;

public class Specific extends javax.swing.JFrame {

    private estimate.Wincalc iwin = null;
    private Window owner = null;

    public Specific() {
        initComponents();
        initElements();
        if (createIwin()) {
            loadingData();
            loadingModel();
        }
    }

    public Specific(java.awt.Window owner, Wincalc iwin) {
        initComponents();
        initElements();
        this.owner = owner;
        this.iwin = iwin;
        loadingData();
        loadingModel();
        //owner.setEnabled(false);
    }
    
    private void loadingData() {
        iwin.constructiv();
    }

    private void loadingModel() {
        DefaultTableModel dtm = ((DefaultTableModel) tab1.getModel());
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tab1.getModel());
        tab1.setRowSorter(sorter);
        dtm.getDataVector().clear();
        int npp = -1;
        for (Specification specRec : iwin.listSpec) { //заполним спецификацию
            Vector vector = specRec.getVector();
//            for (int i = 0; i < vector.size(); ++i) {
//                if(vector.get(i).toString().equals("0.0")) {
//                    vector.set(i, "");
//                }
//            }
            vector.set(0, ++npp);
            dtm.addRow(vector);
            //System.out.println(vector);
        }
    }

    private boolean createIwin() {

        iwin = new Wincalc();
        int nuni = Integer.valueOf(eProperty.systree_nuni.read());
        setTitle(getTitle() + ".    Система: <<" + eSystree.patch(nuni) + ">>");
        Record record = eSystree.find(nuni);
        int sysprod_id = record.getInt(eSystree.sysprod_id);
        Record record2 = eSysprod.find(sysprod_id);
        if (record2 != null) {
            String script = record2.getStr(eSysprod.script);
            if (script.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Выберите конструкцию в системе профилей", "Предупреждение", JOptionPane.OK_OPTION);
                return false;
            } else {
                JsonElement je = new Gson().fromJson(script, JsonElement.class);
                je.getAsJsonObject().addProperty("nuni", nuni);
                iwin.build(je.toString());
            }
        }
        return true;
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
        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Спецификация");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Specific.this.windowClosed(evt);
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
                .addGap(18, 18, 18)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 709, Short.MAX_VALUE)
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
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(900, 500));
        centr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {"", "", "", "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nпп", "ID area", "ID elem", "Расположенние", "Артикул", "Наименование", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Масса", "Угол1", "Угол2", "Угол к горизонту", "Кол.единиц", "Ед.изм", "Кол. без отхода", "Процент отхода", "Кол. с отходом", "Собес-сть за ед. измерения", "Собест. с отх", "Стоим. без скидки", "Стоим. со скидкой", "Скидка"
            }
        ));
        tab1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tab1.setFillsViewportHeight(true);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Specific.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(30);
            tab1.getColumnModel().getColumn(0).setMaxWidth(60);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(30);
            tab1.getColumnModel().getColumn(1).setMaxWidth(60);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(30);
            tab1.getColumnModel().getColumn(2).setMaxWidth(60);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(46);
            tab1.getColumnModel().getColumn(3).setMaxWidth(60);
            tab1.getColumnModel().getColumn(4).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(5).setPreferredWidth(200);
            tab1.getColumnModel().getColumn(9).setMinWidth(40);
            tab1.getColumnModel().getColumn(10).setMinWidth(40);
        }

        centr.add(scr1, java.awt.BorderLayout.CENTER);

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

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(180, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(180, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(180, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                filterUpdate(evt);
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

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh

    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        if (owner != null) {
            owner.setEnabled(true);
            owner = null;
        }
    }//GEN-LAST:event_windowClosed

    private void filterUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterUpdate

        JTable table = tab1;
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((TableRowSorter<TableModel>) tab1.getRowSorter()).setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((TableRowSorter<TableModel>) tab1.getRowSorter()).setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterUpdate

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_mousePressed

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
    private javax.swing.JScrollPane scr1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        FocusListener listenerFocus = new FocusListener() {

            javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

            public void focusGained(FocusEvent e) {
                JTable table = (JTable) e.getSource();
                table.setBorder(border);
//            tabList.add(table);
//            tabActive = table;
//            tmActive = (TableModel) table.getModel();
                btnIns.setEnabled(true);
//            if (table != treeMat) {
//                btnDel.setEnabled(true);
//            }
            }

            public void focusLost(FocusEvent e) {
                JTable table = (JTable) e.getSource();
                table.setBorder(null);
                btnIns.setEnabled(false);
                btnDel.setEnabled(false);
            }
        };
        tab1.addFocusListener(listenerFocus);
    }
}
