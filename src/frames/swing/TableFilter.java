package frames.swing;

import frames.Uti4;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableFilter extends javax.swing.JPanel {

    private JTable table = null;
    private int indexColumn = 0;

    public TableFilter() {
        initComponents();
    }

    public void tabMousePressed(JTable table, JTable... tab) {
        this.table = table;
        Uti4.updateBorderAndSql(table, Arrays.asList(tab));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }

    public JLabel getLabFilter() {
        return labFilter;
    }

    public JTextField getTextField() {
        return txtFilter;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setMaximumSize(new java.awt.Dimension(380, 20));
        setMinimumSize(new java.awt.Dimension(380, 20));
        setPreferredSize(new java.awt.Dimension(380, 20));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
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
                txtFilterCaretUpdate(evt);
            }
        });
        add(txtFilter);

        checkFilter.setText("в конце строки");
        checkFilter.setMaximumSize(new java.awt.Dimension(103, 18));
        checkFilter.setMinimumSize(new java.awt.Dimension(103, 18));
        checkFilter.setPreferredSize(new java.awt.Dimension(103, 18));
        add(checkFilter);
    }// </editor-fold>//GEN-END:initComponents

    private void txtFilterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilterCaretUpdate
        if (txtFilter.getText().length() == 0) {
            ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(null);
        } else {
            indexColumn = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(RowFilter.regexFilter(text, indexColumn));
        }
        Uti4.setSelectedRow(table);
    }//GEN-LAST:event_txtFilterCaretUpdate

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
