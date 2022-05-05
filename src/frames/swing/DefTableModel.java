package frames.swing;

import frames.UGui;
import common.eProp;
import dataset.Field;
import dataset.Query;
import dataset.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import startup.App;
import common.listener.ListenerFrame;

public class DefTableModel extends DefaultTableModel implements ListenerFrame {

    private JTable table = null;
    private DefaultTableModel model;
    private Query query = null;
    public Field[] columns = null;
    private Boolean[] editable = null;
    private TableRowSorter<DefTableModel> sorter = null;

    public DefTableModel(JTable table, Query query, Field... columns) {
        this.table = table;
        this.model = (DefaultTableModel) table.getModel();
        this.query = query;

        Field[] newArray = Arrays.copyOf(columns, columns.length + 1);
        newArray[newArray.length - 1] = query.fields().get(0).fields()[1];
        this.columns = newArray; //последний столбец всегда = ID

        editable = new Boolean[model.getColumnCount()];
        for (int index = 0; index < model.getColumnCount(); index++) {
            editable[index] = model.isCellEditable(0, index);
        }
        ArrayList<Boolean> resizableList = new ArrayList();
        ArrayList<Integer> prefWidthList = new ArrayList();
        ArrayList<Integer> maxWidthList = new ArrayList();
        ArrayList<Integer> minWidthList = new ArrayList();
        DefaultTableColumnModel columnModel = (DefaultTableColumnModel) table.getColumnModel();

        for (int index = 0; index < columnModel.getColumnCount(); index++) {
            resizableList.add(columnModel.getColumn(index).getResizable());
            prefWidthList.add(columnModel.getColumn(index).getPreferredWidth());
            maxWidthList.add(columnModel.getColumn(index).getMaxWidth());
            minWidthList.add(columnModel.getColumn(index).getMinWidth());
        }
        table.setModel(this);
        sorter = new TableRowSorter<DefTableModel>((DefTableModel) table.getModel());
        table.setRowSorter(sorter);
        JTableHeader header = table.getTableHeader();
        header.setFont(UGui.getFont(0, 0));

        for (int index = 0; index < columnModel.getColumnCount(); index++) {
            columnModel.getColumn(index).setResizable(resizableList.get(index));
            columnModel.getColumn(index).setPreferredWidth(prefWidthList.get(index));
            columnModel.getColumn(index).setMinWidth(minWidthList.get(index));
            columnModel.getColumn(index).setMaxWidth(maxWidthList.get(index));
        }

        for (int index = 0; index < columnModel.getColumnCount(); index++) {
            if (eProp.dev == false && "ID".equals(table.getColumnName(index))
                    || "id".equals(table.getColumnName(index))) { //id - Artikles фильтр
                TableColumn col = columnModel.getColumn(index);
                col.setMinWidth(0);
                col.setMaxWidth(0);
            }
        }
        ((DefaultCellEditor) table.getDefaultEditor(Boolean.class)).setClickCountToStart(2);
    }

    public Query getQuery() {
        return query;
    }

    public TableRowSorter<DefTableModel> getSorter() {
        return sorter;
    }

    @Override
    public int getColumnCount() {
        return model.getColumnCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return model.getColumnName(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return model.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return (columns != null) ? query.size() : 0;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columns != null) {
            Table query2 = query.table(columns[columnIndex]);
            Object val = query2.get(rowIndex, columns[columnIndex]);
            if (getColumnClass(columnIndex) == Boolean.class) {
                return (val == null || val.equals(0)) ? false : true;
            }
            return getValueAt(columnIndex, rowIndex, val);
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (table.getColumnModel().getColumn(columnIndex).getCellEditor() instanceof DefCellEditor) {
            if (((DefCellEditor) table.getColumnModel().getColumn(columnIndex)
                    .getCellEditor()).getTextField().isEditable() == false) {
                return; //если DefCellEditor и редактирокание запрещено, всё остальное стандартно
            }
            setValueAt(aValue, rowIndex, columns[columnIndex]);
        } else {
            setValueAt(aValue, rowIndex, columns[columnIndex]);
        }
    }

    //Записать значение элемента от row и field, тут делаются проверки на ввод данных расширенного типа.
    public void setValueAt(Object value, int row, Field field) {
        try {
            if (field.meta().edit() == true) {
                if (value != null && String.valueOf(value).isEmpty() == false) {
                    if (field.meta().type().equals(Field.TYPE.DATE)) {
                        Date d = UGui.StrToDate(value.toString());
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
                        value = (Boolean.valueOf(String.valueOf(value))) ? 1 : 0;
                    }
                }
                query.table(field).set(value, row, field);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(App.Top.frame, "Неверный формат ввода данных", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
