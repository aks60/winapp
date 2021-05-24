package frames.swing;

import frames.Uti4;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableFilter extends javax.swing.JPanel {


    public TableFilter() {
        initComponents();
    }

    private void txtFilterCaretUpdate(JTable table) {
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
        Uti4.setSelectedRow(table);        
    }
    
    private void filterUpdate(JTable table) {                              
        if (txtFilter.getText().length() == 0) {
            ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(RowFilter.regexFilter(text, index));
        }
    }     

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(445, 20));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
        labFilter.setMaximumSize(new java.awt.Dimension(140, 18));
        labFilter.setMinimumSize(new java.awt.Dimension(140, 18));
        labFilter.setPreferredSize(new java.awt.Dimension(140, 18));
        add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(180, 18));
        txtFilter.setMinimumSize(new java.awt.Dimension(180, 18));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(180, 18));
        add(txtFilter);

        checkFilter.setText("в конце строки");
        checkFilter.setMaximumSize(new java.awt.Dimension(103, 18));
        checkFilter.setMinimumSize(new java.awt.Dimension(103, 18));
        checkFilter.setPreferredSize(new java.awt.Dimension(103, 18));
        add(checkFilter);
    }// </editor-fold>//GEN-END:initComponents

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
