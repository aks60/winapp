package common;

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
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import swing.DefFieldEditor;

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

    public static void selectRecord(JTable table, int row) {
        if (table.getRowCount() > row) {
            table.setRowSelectionInterval(row, row);
        }
    }

    public static void scrollRectToVisible(Query query, JTable table) {
        if (table.getRowCount() > 1) {
            Rectangle cellRect = table.getCellRect(query.size() - 1, 0, false);
            table.scrollRectToVisible(cellRect);
        }
    }

    public static void formatterCell(Query query, JTable table, DefFieldEditor editor) {

        JFormattedTextField formatText = editor.getFormatTextField();
        int grup = query.getAs(table.getSelectedRow(), eJoinpar1.grup, -1);
        if (grup < 0) { //пользовательские параметры 
            editor.getButton().setVisible(true);
            formatText.setEnabled(false);
            formatText.setFormatterFactory(ParamList.defaultFormatter());
        } else {
            Enam enam = ParamList.find(grup);
            if (enam.dict() != null) { //системные- список параметров
                editor.getButton().setVisible(true);
                formatText.setEnabled(false);
                formatText.setFormatterFactory(ParamList.defaultFormatter());

            } else { //системные- вводимые пользователем
                editor.getButton().setVisible(false);
                formatText.setEnabled(true);
                formatText.setFormatterFactory(enam.format());
            }
        }
    }
}
