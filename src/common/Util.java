package common;

import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eJoinpar1;
import domain.eSystree;
import enums.Enam;
import enums.ParamList;
import java.awt.Font;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import swing.DefFieldEditor;
import swing.DefTableModel;

/**
 * <p>
 * Параметры приложения </p>
 */
public class Util {

    public static boolean progressFrame = true;
    private static GregorianCalendar appCalendar = new GregorianCalendar(); //календарь программы    
    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM); //формат даты
    private static SimpleDateFormat simpledateFormat = null; //"yyyy-MM-dd" формат только для баз где даты utf8
    private static int mes = 0;

    public static void setSimpleDateFormat(SimpleDateFormat _simpledateFormat) {
        simpledateFormat = _simpledateFormat;
    }

    public static DateFormat getDateFormat() {
        return dateFormat;
    }

    // Преобразование даты в строку
    public static String getDateStr(Object obj) {
        if (obj == null) {
            return dateFormat.format(appCalendar.getTime());
        }
        if (obj instanceof Date) {
            GregorianCalendar gk = new GregorianCalendar();
            gk.setTime((Date) obj);
            int index = gk.get(GregorianCalendar.MONTH);
            String monthName[] = {"января", "февраля", "марта", "апреля", "мая",
                "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
            return "\" " + String.valueOf(gk.get(GregorianCalendar.DAY_OF_MONTH)) + " \"   "
                    + monthName[index] + "    " + String.valueOf(gk.get(GregorianCalendar.YEAR)) + " г.";
        } else {
            return "";
        }
    }

    //Преобразование текущей даты в строку
    public static int getDateField(Object obj, int field) {
        if (obj instanceof Date) {
            GregorianCalendar gk = new GregorianCalendar();
            gk.setTime((Date) obj);
            return gk.get(field);
        } else {
            return 0;
        }
    }

    // Текущая дата
    public static Date getDateCur() {
        return appCalendar.getTime();
    }

    //Преобразование string в date
    public static Date StrToDate(String str) {
        try {
            return (Date) dateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    // Преобразование date в string
    public static String DateToStr(Object date) {
        return (date instanceof java.util.Date) ? dateFormat.format(date) : "";
    }

    //Преобразование date в string
    public static String DateToSql(Object date) {
        if (date == null) {
            return simpledateFormat.format(appCalendar.getTime());
        }
        if (date instanceof java.util.Date) {
            return simpledateFormat.format(date);
        }
        return "";
    }

    //Текущий год
    public static int getYearCur() {
        return appCalendar.get(Calendar.YEAR);
    }

    public static void setGregorianCalendar(Object obj) {
        appCalendar = (java.util.GregorianCalendar) obj;
    }

    public static GregorianCalendar getGregorianCalendar() {
        return appCalendar;
    }

    public static Font getFont(int size, int bold) {
        return new Font(eProperty.fontname.read(), bold, Integer.valueOf(eProperty.fontsize.read()) + size);
    }

    public static String typeSql(Field.TYPE type, Object size) {

        if (type == Field.TYPE.INT) {
            return "INTEGER";
        } else if (type == Field.TYPE.DBL) {
            return "DOUBLE PRECISION";
        } else if (type == Field.TYPE.FLT) {
            return "FLOAT";
        } else if (type == Field.TYPE.STR) {
            return "VARCHAR(" + size + ")";
        } else if (type == Field.TYPE.DATE) {
            return "DATE";
        } else if (type == Field.TYPE.BLOB) {
            return "BLOB SUB_TYPE 1 SEGMENT SIZE " + size;
        } else if (type == Field.TYPE.BOOL) {
            return "SMALLINT";
        }
        return "";
    }

    public static Object wrapperSql(Object value, Field.TYPE type) {
        try {
            if (value == null) {
                return null;
            } else if (Field.TYPE.STR.equals(type)) {
                return "'" + value + "'";
            } else if (Field.TYPE.BLOB.equals(type)) {
                return "'" + value + "'";
            } else if (Field.TYPE.BOOL.equals(type)) {
                return "'" + value + "'";
            } else if (Field.TYPE.DATE.equals(type)) {
                if (value instanceof java.util.Date) {
                    return " '" + new SimpleDateFormat("dd.MM.yyyy").format(value) + "' ";
                } else {
                    //return " '" + value + "' ";
                    return null;
                }
            }
            return value;

        } catch (Exception e) {
            System.out.println("Query.vrapper() " + e);
            return null;
        }
    }

    //Рекурсия поиска родителя
    public static Record findParent(Query table, int key) {
        for (Record record : table) {
            if (key == record.getInt(eSystree.id)) {
                if (record.getInt(eSystree.id) == record.getInt(eSystree.parent_id)) {
                    return record;
                } else {
                    return findParent(table, record.getInt(eSystree.parent_id));
                }
            }
        }
        return null;
    }

    public static void scrollRectToVisible(Query query, JTable table) {
        if (table.getRowCount() > 1) {
            Rectangle cellRect = table.getCellRect(query.size() - 1, 0, false);
            table.scrollRectToVisible(cellRect);
        }
    }

    public static void formatterCell(Query query, JTable table, DefFieldEditor editor) {

//        JTextField formatText = editor.getTextField();
//        int grup = query.getAs(getSelectedRow(table), eJoinpar1.grup, -1);
//        if (grup < 0) { //пользовательский список параметров
//            editor.getButton().setVisible(true);
//            formatText.setEnabled(false);
//        } else {
//            Enam enam = ParamList.find(grup);
//            if (enam.dict() != null) { //системный список параметров
//                editor.getButton().setVisible(true);
//                formatText.setEnabled(false);
//
//            } else { //системные вводимые пользователем
//                editor.getButton().setVisible(false);
//                formatText.setEnabled(true);
//                formatText.setEditable(true);
//            }
//        }
    }

    public static void insertSql(JTable table, Query query, Field up) {

        Record record = query.newRecord(Query.INS);
        record.setNo(up.fields()[1], ConnApp.instanc().genId(up));
        query.add(record);
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.scrollRectToVisible(query, table);
    }

    public static void setSelectedRow(JTable table, int row) {
        if (table.getRowCount() > row) {
            table.setRowSelectionInterval(row, row);
        }
    }

    public static int getSelectedRow(JTable table) {
        if (table.getSelectedRow() != -1) {
            return table.convertRowIndexToModel(table.getSelectedRow());
        }
        return -1;
    }

    public static void insertRecord(JTable table1, JTable table2, Query query1, Query query2, Field up1, Field up2, Field fk2) {

        int row = getSelectedRow(table1);
        Record record1 = query1.get(row);
        Record record2 = query2.newRecord(Query.INS);
        record2.setNo(up2.fields()[1], ConnApp.instanc().genId(up2));
        record2.setNo(fk2, record1.getInt(up1.fields()[1]));
        query2.add(record2);
        ((DefaultTableModel) table2.getModel()).fireTableDataChanged();
        Util.scrollRectToVisible(query2, table2);
    }

    public static void deleteRecord(JTable table, Query query, Field field) {

        Record record = query.get(getSelectedRow(table));
        record.set(field, Query.DEL);
        query.delete(record);
        query.removeRec(getSelectedRow(table));
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.setSelectedRow(table, 0);
    }

    public static void clearTable(JTable... jTable) {
        for (JTable table : jTable) {
            if (table.getModel() instanceof DefTableModel) {
                ((DefTableModel) table.getModel()).getQuery().execsql();
                ((DefTableModel) table.getModel()).getQuery().clear();

            } else if (table.getModel() instanceof DefaultTableModel) {
                ((DefaultTableModel) table.getModel()).getDataVector().clear();
            }
            ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        }
    }

    public static JButton buttonEditorCell(JTable table, int column) {
        JButton btn = new JButton("...");
        table.getColumnModel().getColumn(column).setCellEditor(new DefFieldEditor(btn));
        return btn;
    }

    public static JButton buttonEditorCell(JTable table, int column, EditorListener listener) {
        JButton btn = new JButton("...");
        table.getColumnModel().getColumn(column).setCellEditor(new DefFieldEditor(listener, btn));
        return btn;
    }

    public static int isDeleteRecord(java.awt.Window owner, JTable... table) {
        for (JTable tab : table) {
            if (tab.getRowCount() != 0) {
                JOptionPane.showMessageDialog(owner, "Перед удалением записи удалите данные в зависимых таблицах", "Предупреждение", JOptionPane.NO_OPTION);
                return 1;
            }
        }
        return JOptionPane.showConfirmDialog(owner, "Вы действительно хотите удалить текущую запись?", "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}
