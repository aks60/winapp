package swing;

import common.FrameListener;
import common.Util;
import dataset.Field;
import dataset.Query;
import dataset.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class DefTableModel extends DefaultTableModel implements FrameListener {

    private DefaultTableModel model;
    public Query query = null;
    private Field[] columns = null;
    private Boolean[] editable = null;
    private TableRowSorter<DefTableModel> sorter = new TableRowSorter(this);
    private FrameListener<Object, Object> listenerModify = null;

    public DefTableModel(JTable table, Query query, Field... columns) {
        this.model = (DefaultTableModel) table.getModel();
        this.query = query;
        this.columns = columns;
        editable = new Boolean[model.getColumnCount()];
        for (int index = 0; index < model.getColumnCount(); index++) {
            editable[index] = model.isCellEditable(0, index);
        }
        ArrayList<Boolean> resizableList = new ArrayList();
        ArrayList<Integer> prefWidthList = new ArrayList();
        ArrayList<Integer> maxWidthList = new ArrayList();
        for (int index = 0; index < table.getColumnModel().getColumnCount(); index++) {
            resizableList.add(table.getColumnModel().getColumn(index).getResizable());
            prefWidthList.add(table.getColumnModel().getColumn(index).getPreferredWidth());
            maxWidthList.add(table.getColumnModel().getColumn(index).getMaxWidth());
        }
        table.setModel(this);
        table.setRowSorter(sorter);
        JTableHeader header = table.getTableHeader();
        header.setFont(Util.getFont(0, 0));
        for (int index = 0; index < resizableList.size(); index++) {
            table.getColumnModel().getColumn(index).setResizable(resizableList.get(index));
            table.getColumnModel().getColumn(index).setPreferredWidth(prefWidthList.get(index));
            table.getColumnModel().getColumn(index).setMaxWidth(maxWidthList.get(index));
        }
    }

    public int getColumnCount() {
        return model.getColumnCount();
    }

    public String getColumnName(int columnIndex) {
        return model.getColumnName(columnIndex);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return model.getColumnClass(columnIndex);
    }

    public int getRowCount() {
        return (columns != null) ? query.table(columns[0].tname()).size() : 0;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object val = null;
        if (columns != null) {
            Table table = query.table(columns[columnIndex].tname());
            if (getColumnClass(columnIndex) == Boolean.class) {
                val = (table.get(rowIndex, columns[columnIndex]).equals(0)) ? false : true;
                return val;
            } else {
                val = table.get(rowIndex, columns[columnIndex]);
                return preview(columns[columnIndex], rowIndex, val);
            }            
        }
        return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        setValueAt(aValue, rowIndex, columns[columnIndex]);
    }

    /**
     * Записать значение элемента от row и field, тут делаются проверки на ввод
     * данных расширенного типа.
     */
    public void setValueAt(Object value, int row, Field field) {

        Table table = query.table(field.tname());
        if (field.meta().edit() == false || value.equals(table.get(row, field))) {
            return;
        }
        try {
            if (field.meta().type().equals(Field.TYPE.DATE)) {
                Date d = Util.StrToDate(value.toString());
                if (d != null) {
                    GregorianCalendar d1 = new GregorianCalendar(1917, 01, 01);
                    GregorianCalendar d2 = new GregorianCalendar(2040, 01, 01);
                    if (d.after(d2.getTime()) || d.before(d1.getTime())) {
                        return;
                    }
                }
                value = d;
            } else if (field.meta().type().equals(Field.TYPE.INT)) {
                value = Integer.valueOf(String.valueOf(value));
            } else if (field.meta().type().equals(Field.TYPE.DBL)) {
                String str = String.valueOf(value).replace(',', '.');
                value = Double.valueOf(str);
            } else if (field.meta().type().equals(Field.TYPE.FLT)) {
                String str = String.valueOf(value).replace(',', '.');
                value = Float.valueOf(str);
            } else if (field.meta().type().equals(Field.TYPE.BOOL)) {
                value = Boolean.valueOf(String.valueOf(value));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Неверный формат ввода данных", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        table.set(value, row, field);
        if (listenerModify != null) {
            listenerModify.request(null);
        }
    }

    public void addFrameListener(FrameListener listenerModify) {
        this.listenerModify = listenerModify;
    }
}
