package common;

import dataset.Field;
import winapp.Main;
import dataset.Record;
import dataset.Table;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ArrayList;

/**
 * <p>
 * Параметры приложения </p>
 */
public class Utils {

    public static boolean progressFrame = true;
    /**
     * Календарь программы
     */
    private static GregorianCalendar appCalendar = new GregorianCalendar();
    /**
     * Формат даты
     */
    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    //"yyyy-MM-dd" формат только для баз где даты utf8
    private static SimpleDateFormat simpledateFormat = null;

    /**
     * Разврашает ввод данных через карточку ввода
     */
    private static boolean cardFormat = true;
    /**
     * Для тестирования
     */
    //public static boolean dev = false;
    private static int mes = 0;

    /**
     * Font по умолчанию
     */
    private enum font {

        name("Dialog"),
        size("11");
        String value;

        font(String value) {
            this.value = value;
        }
    }

    public static void setSimpleDateFormat(SimpleDateFormat _simpledateFormat) {
        simpledateFormat = _simpledateFormat;
    }

    /**
     * message
     */
    public static void D(Object... obj) {
        if (Main.dev == false) {
            return;
        }
        if (obj == null) {
            System.out.println(String.valueOf(++mes));
        } else {
            for (Object obj2 : obj) {
                System.out.println(obj2);
            }
        }
    }

    public static boolean isDataCardFormat() {
        return cardFormat;
    }

    public static DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * Преобразование даты в строку
     */
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

    /**
     * Преобразование текущей даты в строку
     */
    public static int getDateField(Object obj, int field) {
        if (obj instanceof Date) {
            GregorianCalendar gk = new GregorianCalendar();
            gk.setTime((Date) obj);
            return gk.get(field);
        } else {
            return 0;
        }
    }

    /**
     * Текущая дата
     */
    public static Date getDateCur() {
        return appCalendar.getTime();
    }

    /**
     * Дата перехода на другой уч. год
     */
    public static Date getDatePass(int dxYear) {
        /*int index = eSystem.query().locate(eSystem.sp.meta().push(1));
        int year = getYearSchool() + dxYear;
        int month = eSystem.query().getInt(index, eSystem.val1);
        int day = eSystem.query().getInt(index, eSystem.val2);
        GregorianCalendar schoolCalendar = new GregorianCalendar(year, month - 1, day);
        return schoolCalendar.getTime();*/
        return null;
    }

    /**
     * Преобразование string в date
     */
    public static Date StrToDate(String str) {
        try {
            return (Date) dateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Преобразование date в string
     */
    public static String DateToStr(Object date) {
        return (date instanceof java.util.Date) ? dateFormat.format(date) : "";
    }

    /**
     * Преобразование date в string
     */
    public static String DateToSql(Object date) {
        if (date == null) {
            return simpledateFormat.format(appCalendar.getTime());
        }
        if (date instanceof java.util.Date) {
            return simpledateFormat.format(date);
        }
        return "";
    }

    /**
     * Текущий год
     */
    public static int getYearCur() {
        return appCalendar.get(Calendar.YEAR);
    }

    public static void setGregorianCalendar(Object obj) {
        appCalendar = (java.util.GregorianCalendar) obj;
    }

    public static GregorianCalendar getGregorianCalendar() {
        return appCalendar;
    }

    public static double ObjToDouble(Object obj) {
        if (obj == null) {
            return 0;
        }
        return Double.valueOf(obj.toString());
    }

    public static Font getFont(int size, int bold) {
        return new Font(eProp.fontname.read(), bold, Integer.valueOf(eProp.fontsize.read()) + size);
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
                    return " '" + value + "' ";
                }
            }
            return value;

        } catch (Exception e) {
            System.out.println("Query.vrapper() " + e);
            return null;
        }
    }

    public static void println(Object obj) {
        if (Main.dev == true) {
            System.out.println(obj);
        }
    }

    public static void println(Table table) {
        if (Main.dev == true) {
            for (Record record : table) {
                System.out.println(record);
            }
        }
    }
}
