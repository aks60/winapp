package swing;

import common.EditorListener;
import common.Util;
import dataset.Field;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;

public class DefFieldEditor extends DefaultCellEditor {

    private EditorListener listener = null;
    protected JComponent panel = new javax.swing.JPanel();
    protected JButton button = null;

    public DefFieldEditor(JButton button) {
        super(new JTextField());
        init(button);
    }

    public DefFieldEditor(EditorListener listener, JButton button) {
        super(new JTextField());
        this.listener = listener;
        init(button);
    }

    private void init(JButton button) {
        this.button = button;
        button.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        button.setFocusable(false);
        button.setPreferredSize(new java.awt.Dimension(24, 18));

        panel = new javax.swing.JPanel();
        panel.setBorder(null);
        panel.setBackground(new java.awt.Color(240, 240, 240));
        panel.setLayout(new java.awt.BorderLayout());

        JTextField editorText = (JTextField) editorComponent;
        editorText.setPreferredSize(new java.awt.Dimension(60, 18));
        editorText.setEditable(false);
        editorText.setBorder(null);
        editorText.setBackground(new java.awt.Color(255, 255, 255));
        panel.add(editorText, java.awt.BorderLayout.CENTER);
        panel.add(button, java.awt.BorderLayout.EAST);
    }
     
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        Field field = ((DefTableModel) table.getModel()).getColumn(column);
        ((JTextField) editorComponent).setEditable(field.meta().type() == Field.TYPE.STR); //разрешить редактирование стрингу
        delegate.setValue(value);
        return panel;
    }

    public boolean stopCellEditing() {

        System.out.println("javaapplication8.IntegerEditor.stopCellEditing()");

        JTextField ftf = (JTextField) getComponent();
        if (ftf.getText().equals("777")) {
            System.out.println(ftf.getText());
            return false;
        }
        return super.stopCellEditing();
    }

    public JButton getButton() {
        return button;
    }

    public JTextField getTextField() {
        return (JTextField) editorComponent;
    }
}
