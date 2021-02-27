package frames.swing;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DefCellRenderer extends DefaultTableCellRenderer {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (value instanceof java.util.Date) {
            return super.getTableCellRendererComponent(table, DATE_FORMAT.format(value), isSelected, hasFocus, row, column);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
