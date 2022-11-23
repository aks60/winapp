package frames.swing;

import common.UCom;
import common.eProp;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class DefCellEditorNumb extends DefaultCellEditor {

    private int scale = 0;
    private String pattern = null;
    private DecimalFormat df = null;

    public DefCellEditorNumb(int scale) {
        super(new JTextField());
        this.scale = scale;
        editorComponent.setFont(frames.UGui.getFont(0, 0));
        editorComponent.setPreferredSize(new java.awt.Dimension(60, 18));
        editorComponent.setBorder(null);
    }

    public DefCellEditorNumb(String pattern) {
        super(new JTextField());
        this.pattern = pattern;
        editorComponent.setFont(frames.UGui.getFont(0, 0));
        editorComponent.setPreferredSize(new java.awt.Dimension(60, 18));
        editorComponent.setBorder(null);
    }
    
    public DefCellEditorNumb(DecimalFormat df) {
        super(new JTextField());
        this.df = df;
        editorComponent.setFont(frames.UGui.getFont(0, 0));
        editorComponent.setPreferredSize(new java.awt.Dimension(60, 18));
        editorComponent.setBorder(null);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        try {
            if (value instanceof Float || value instanceof Double) {
                String val = (df != null) ? df.format(value) :(pattern != null) ? UCom.format(value, pattern) : UCom.format(value, scale);
                return super.getTableCellEditorComponent(table, val, isSelected, row, column);
            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);

        } catch (Exception e) {
            System.err.println("Ошибка:DefCellEditor.getTableCellEditorComponent() " + e);
            return null;
        }
    }
}
