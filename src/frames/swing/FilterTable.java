package frames.swing;

import dataset.Field;
import dataset.Query;
import frames.Uti5;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class FilterTable extends javax.swing.JPanel {

    private JTable table = null;
    private JTable[] tableList = null;
    private int indexColumn = 0;
    private boolean search = false;

    public FilterTable() {
        initComponents();
    }

    public FilterTable(int index, JTable... table) {
        initComponents();
        tableList = table;
        mousePressed(table[0]);
        labFilter.setText(table[0].getColumnName(index));
        txtFilter.setName(table[0].getName());
        table[0].setColumnSelectionInterval(index, index);
    }

    public JLabel getLab() {
        return labFilter;
    }

    public JTextField getTxt() {
        return txtFilter;
    }

    public void mousePressed(JTable table) {
        if (txtFilter.getText().length() == 0) {
            this.table = table;
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn1 = new javax.swing.JButton();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        btn2 = new javax.swing.JButton();
        checkFilter = new javax.swing.JCheckBox();

        setMaximumSize(new java.awt.Dimension(420, 20));
        setMinimumSize(new java.awt.Dimension(360, 20));
        setPreferredSize(new java.awt.Dimension(420, 20));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn1.setBorder(null);
        btn1.setMaximumSize(new java.awt.Dimension(60, 25));
        btn1.setMinimumSize(new java.awt.Dimension(32, 20));
        btn1.setPreferredSize(new java.awt.Dimension(36, 23));
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActiPerf(evt);
            }
        });
        add(btn1);

        labFilter.setText("Поле не выбрано");
        labFilter.setMaximumSize(new java.awt.Dimension(140, 18));
        labFilter.setMinimumSize(new java.awt.Dimension(140, 18));
        labFilter.setPreferredSize(new java.awt.Dimension(140, 18));
        add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(120, 18));
        txtFilter.setMinimumSize(new java.awt.Dimension(120, 18));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(120, 18));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtCaretUpdate(evt);
            }
        });
        add(txtFilter);

        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b041.gif"))); // NOI18N
        btn2.setBorder(null);
        btn2.setMaximumSize(new java.awt.Dimension(26, 23));
        btn2.setMinimumSize(new java.awt.Dimension(26, 23));
        btn2.setPreferredSize(new java.awt.Dimension(26, 23));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActiPerf(evt);
            }
        });
        add(btn2);

        checkFilter.setText("в конце строки");
        checkFilter.setMaximumSize(new java.awt.Dimension(103, 18));
        checkFilter.setMinimumSize(new java.awt.Dimension(103, 18));
        checkFilter.setPreferredSize(new java.awt.Dimension(103, 18));
        add(checkFilter);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtCaretUpdate
        if (table != null) {
            if (txtFilter.getText().length() == 0) {
                btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b041.gif")));
                ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(null);

            } else if (search == true) {
                btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b064.gif")));
                indexColumn = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
                if (table.getModel() instanceof DefTableModel) {
                    Query query = ((DefTableModel) table.getModel()).getQuery();
                    Field field = ((DefTableModel) table.getModel()).columns[indexColumn];
                    for (int index = 0; index < query.size(); ++index) {

                        if (query.table(field).get(index).getStr(field).startsWith(txtFilter.getText())) {
                            Uti5.setSelectedRow(table, index);
                            Uti5.scrollRectToIndex(index, table);
                            return;
                        }
                    }
                } else {
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    for (int index = 0; index < dtm.getDataVector().size(); ++index) {
                        Vector vector = dtm.getDataVector().get(index);
                        if (String.valueOf(vector.get(indexColumn)).startsWith(txtFilter.getText())) {
                            Uti5.setSelectedRow(table, index);
                            Uti5.scrollRectToIndex(index, table);
                            return;
                        }
                    }
                }
            } else if (search == false) {
                btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b064.gif")));
                indexColumn = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
                String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
                ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(RowFilter.regexFilter(text, indexColumn));
                Uti5.setSelectedRow(table);
            }
        }
    }//GEN-LAST:event_txtCaretUpdate

    private void btn1ActiPerf(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActiPerf
        if (search == true) {
            btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif")));
            search = !search;
            txtCaretUpdate(null);
        } else {
            btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c043.gif")));
            search = !search;
            String txt = txtFilter.getText();
            txtFilter.setText("");
            txtCaretUpdate(null);
            txtFilter.setText(txt);
        }
        Uti5.scrollRectToRow(table.getSelectedRow() - 1, table);
    }//GEN-LAST:event_btn1ActiPerf

    private void btn2ActiPerf(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActiPerf
        if (txtFilter.getText().isEmpty()) {
            btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b064.gif")));
            try {
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = cb.getContents(null);
                if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    txtFilter.setText(t.getTransferData(DataFlavor.stringFlavor).toString());
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println("frames.swing.TableFilter.btn2ActiPerf()");
            }
        } else {
            btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b041.gif")));
            txtFilter.setText("");
        }
    }//GEN-LAST:event_btn2ActiPerf

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
