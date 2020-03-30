package swing;

import common.Util;
import dataset.Field;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ButtonCellEditor extends DefaultCellEditor {

    protected JComponent panel = new javax.swing.JPanel();;    
    protected JButton button = null;   
    protected final JTextField textField = new JTextField(); 

    public ButtonCellEditor(JButton button) {
        super(new JTextField());
        this.button = button; 
        button.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        button.setFocusable(false);
        button.setPreferredSize(new java.awt.Dimension(24, 18));
        panel.setBorder(null);
        panel.setBackground(new java.awt.Color(240, 240, 240));
        panel.setLayout(new java.awt.BorderLayout());
        editorComponent.setPreferredSize(new java.awt.Dimension(60, 18));
        editorComponent.setBorder(null);
        editorComponent.setBackground(new java.awt.Color(255, 255, 255));
        panel.add(editorComponent, java.awt.BorderLayout.CENTER);
        panel.add(button, java.awt.BorderLayout.EAST);        
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        DefTableModel rsm = (DefTableModel) table.getModel();
        Field field = rsm.getColumn(column);
        if (field.meta().type().equals(Field.TYPE.DATE)) {
            delegate.setValue(Util.DateToStr(value));
//        } else if (field.meta().type().equals(eField.TYPE.DBL) || field.meta().type().equals(eField.TYPE.FLT)) {
//            String val = String.valueOf(value).replace(',', '.');
//            delegate.setValue(val);
        //} else if(field.meta().type() == Field.TYPE.STR) { 
            
        } else {
            delegate.setValue(value);            
        }
        return panel;
    }

}
