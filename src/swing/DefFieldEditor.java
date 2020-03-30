package swing;

import common.EditorListener;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import dataset.Field;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;

public class DefFieldEditor extends DefaultCellEditor {

    private EditorListener listener = null;
    protected JComponent panel = new javax.swing.JPanel();
    protected JButton button = null;

    public DefFieldEditor(JButton button) {
        super(new JFormattedTextField());
        init(button);
    }

    public DefFieldEditor(EditorListener listener, JButton button) {
        super(new JFormattedTextField());
        this.listener = listener;
        init(button);
    }

    private void init(JButton button) {
        this.button = button;
        button.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
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
//        button.setFocusable(false);
//        button.setPreferredSize(new java.awt.Dimension(24, 18));
//        panel.setBorder(null);
//        panel.setBackground(new java.awt.Color(240, 240, 240));
//        panel.setLayout(new java.awt.BorderLayout());
//        editorComponent.setPreferredSize(new java.awt.Dimension(60, 18));
//        editorComponent.setBorder(null);
//        editorComponent.setBackground(new java.awt.Color(255, 255, 255));
//        panel.add(editorComponent, java.awt.BorderLayout.CENTER);
//        panel.add(button, java.awt.BorderLayout.EAST);
//        ((JFormattedTextField) editorComponent).addActionListener(event -> {
//            if (listener != null) {
//                listener.action(editorComponent);
//            }
//        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        Field field = ((DefTableModel) table.getModel()).getColumn(column);

//        if (field.meta().type().equals(Field.TYPE.DATE)) {
//            delegate.setValue(Util.DateToStr(value));
//            
//        } else if (field.meta().type().equals(Field.TYPE.DBL) || field.meta().type().equals(Field.TYPE.FLT)) {
//            String val = String.valueOf(value).replace(',', '.');
//            delegate.setValue(val);
//        } else {

        ((JTextField)editorComponent).setEditable(field.meta().type() == Field.TYPE.STR);
        delegate.setValue(value);
        return panel;
        
//        Field field = ((DefTableModel) table.getModel()).getColumn(column);
//
////        if (field.meta().type().equals(Field.TYPE.DATE)) {
////            delegate.setValue(Util.DateToStr(value));
////            
////        } else if (field.meta().type().equals(Field.TYPE.DBL) || field.meta().type().equals(Field.TYPE.FLT)) {
////            String val = String.valueOf(value).replace(',', '.');
////            delegate.setValue(val);
////        } else {
//        ((JTextField) editorComponent).setEditable(field.meta().type() == Field.TYPE.STR);
//        delegate.setValue(value);
//        return panel;
    }

    public JButton getButton() {
        return button;
    }

    public JFormattedTextField getFormatTextField() {
        return (JFormattedTextField) editorComponent;
    }
}
