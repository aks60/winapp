package frames.swing;

import common.UCom;
import frames.UGui;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DefCellRendererNumb extends DefaultTableCellRenderer {

    protected int scale = 2;
    private String pattern = null;
    
    public DefCellRendererNumb(int scale) {
        this.scale = scale;
    }
    
    public DefCellRendererNumb(String pattern) {
        this.pattern = pattern;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (value instanceof java.util.Date) {
            return super.getTableCellRendererComponent(table, UGui.simpleFormat.format(value), isSelected, hasFocus, row, column);
        }
        if (value instanceof Float || value instanceof Double) {
            String val = (pattern == null) ? UCom.format(value, scale) : UCom.format(value, pattern);
            return super.getTableCellRendererComponent(table, val, isSelected, hasFocus, row, column);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
