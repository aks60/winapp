package frames.swing;

import common.DialogListener;
import common.FrameListener;
import frames.Util;
import common.eProfile;
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
import startup.Main;

public class DefTableModel extends DefaultTableModel implements FrameListener {

    private JTable table = null;
    private DefaultTableModel model;
    private Query query = null;
    public Field[] columns = null;
    private Boolean[] editable = null;
    private TableRowSorter<DefTableModel> sorter = null;
    private FrameListener<Object, Object> listenerModify = null;
    private DialogListener listenerModify2 = null;

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
        DefaultTableColumnModel columnModel = (DefaultTableColumnModel) table.getColumnModel();

        for (int index = 0; index < columnModel.getColumnCount(); index++) {
            resizableList.add(columnModel.getColumn(index).getResizable());
            prefWidthList.add(columnModel.getColumn(index).getPreferredWidth());
            maxWidthList.add(columnModel.getColumn(index).getMaxWidth());
        }
        table.setModel(this);
        sorter = new TableRowSorter<DefTableModel>((DefTableModel) table.getModel());
        table.setRowSorter(sorter);
        JTableHeader header = table.getTableHeader();
        header.setFont(Util.getFont(0, 0));

        for (int index = 0; index < columnModel.getColumnCount(); index++) {
            columnModel.getColumn(index).setResizable(resizableList.get(index));
            columnModel.getColumn(index).setPreferredWidth(prefWidthList.get(index));
            columnModel.getColumn(index).setMaxWidth(maxWidthList.get(index));
        }

        for (int index = 0; index < columnModel.getColumnCount(); index++) {
            if (Main.dev == false && "ID".equals(table.getColumnName(index))
                    || "id".equals(table.getColumnName(index))) {
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
            return (val != null) ? getValueAt(columnIndex, rowIndex, val) : null;
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        if (table.getColumnModel().getColumn(columnIndex).getCellEditor() instanceof DefCellEditor == false) {
            setValueAt(aValue, rowIndex, columns[columnIndex]);

        } else if (columns[columnIndex].meta().type() == Field.TYPE.STR) {
            setValueAt(aValue, rowIndex, columns[columnIndex]);
        }
    }

    //Записать значение элемента от row и field, тут делаются проверки на ввод данных расширенного типа.
    public void setValueAt(Object value, int row, Field field) {
        try {
            if (field.meta().edit() == true) {
                if (value != null && String.valueOf(value).isEmpty() == false) {
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
                        value = (Boolean.valueOf(String.valueOf(value))) ? 1 : 0;
                    }
                }
                query.table(field).set(value, row, field);
                if (listenerModify != null) {
                    listenerModify.actionRequest(null);
                } else if (listenerModify2 != null) {
                    listenerModify2.action(null);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(eProfile.appframe, "Неверный формат ввода данных", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public DefTableModel setFrameListener(FrameListener listenerModify) {
        this.listenerModify = listenerModify;
        return this;
    }

    public DefTableModel setFrameListener(DialogListener listenerModify) {
        this.listenerModify2 = listenerModify2;
        return this;
    }
}
