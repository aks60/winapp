package frames;

import common.EditorListener;
import common.eProperty;
import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eFurnpar2;
import domain.eJoinpar1;
import domain.eParams;
import domain.eSystree;
import enums.Enam;
import enums.ParamList;
import enums.UseColcalc;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import startup.Main;
import frames.swing.DefCellEditor;
import frames.swing.DefTableModel;

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

    public static String consoleColor(Object clr) {

        if (clr == java.awt.Color.RED) {
            return "\u001B[31m";
        } else if (clr == java.awt.Color.GREEN) {
            return "\u001B[32m";
        } else if (clr == java.awt.Color.BLUE) {
            return "\u001B[34m";
        } else {
            return "\u001B[0m";
        }
    }

    //Все компоненты формы
    public static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }

    //Типы данных в базе
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

    //Обернуть данные sql опратора
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

    //Прокрутить скроллинг и сделать ячейку видимой
    public static void scrollRectToVisible(Query query, JTable table) {
        if (table.getRowCount() > 1) {
            Rectangle cellRect = table.getCellRect(query.size() - 1, 0, false);
            table.scrollRectToVisible(cellRect);
        }
    }

    //Прокрутить скроллинг и сделать ячейку видимой
    public static void scrollRectToVisible(int row, JTable table) {
        if (table.getRowCount() > row) {
            Rectangle cellRect = table.getCellRect(row, 0, false);
            table.scrollRectToVisible(cellRect);
        }
    }

    //Выделить запись
    public static void setSelectedRow(JTable table) {
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    //Выделить запись
    public static void setSelectedRow(JTable table, int rowModel) {
        if (table.getRowCount() > 0) {

            int rowTable = table.convertRowIndexToView(rowModel);
            if (rowTable < table.getRowCount()) {

                table.setRowSelectionInterval(rowTable, rowTable);
            } else {
                table.setRowSelectionInterval(0, 0);
            }
        }
    }

    //Получить convertRowIndexToModel
    public static int getSelectedRec(JTable table) {
        if (table.getSelectedRow() != -1) {
            return table.convertRowIndexToModel(table.getSelectedRow());
        }
        return -1;
    }

    //Отменить сортировку
    public static void stopSorting(JTable... table) {
        for (JTable tab : table) {
            tab.getRowSorter().setSortKeys(null);
        }
    }

    //Вставить запись
    public static Record insertRecord(JTable table, Field up) {

        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record record = query.newRecord(Query.INS);
        record.setNo(up.fields()[1], ConnApp.instanc().genId(up));
        query.add(record);
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.scrollRectToVisible(query, table);
        return record;
    }

    //Вставить запись
    public static Record insertRecord(JTable table1, JTable table2, Field up1, Field up2, Field fk2) {

        int row = getSelectedRec(table1);
        if (row != -1) {
            Query query1 = ((DefTableModel) table1.getModel()).getQuery();
            Query query2 = ((DefTableModel) table2.getModel()).getQuery();
            Record record1 = query1.get(row);
            Record record2 = query2.newRecord(Query.INS);
            record2.setNo(up2.fields()[1], ConnApp.instanc().genId(up2));
            record2.setNo(fk2, record1.getInt(up1.fields()[1]));
            query2.add(record2);
            ((DefaultTableModel) table2.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(query2, table2);
            return record2;
        } else {
            JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            return null;
        }
    }

    //Вставить запись
    public static Record insertRecord(JTable table1, JTable table2, Field up1, Field up2, Field up3, Field fk2) {

        int row = getSelectedRec(table1);
        if (row != -1) {
            Query query1 = ((DefTableModel) table1.getModel()).getQuery();
            Query query2 = ((DefTableModel) table2.getModel()).getQuery();
            Record record1 = query1.get(row);
            Record record2 = ((DefTableModel) table2.getModel()).getQuery().newRecord(Query.INS);
            Record record3 = up3.newRecord();
            record2.setNo(up2.fields()[1], ConnApp.instanc().genId(up2));
            record2.setNo(fk2, record1.getInt(up1.fields()[1]));
            query2.add(record2);
            query2.table(up3).add(record3);
            ((DefaultTableModel) table2.getModel()).fireTableDataChanged();
            Util.scrollRectToVisible(query2, table2);
            return record2;
        } else {
            JOptionPane.showMessageDialog(null, "Сначала заполните основную таблицу", "Предупреждение", JOptionPane.NO_OPTION);
            return null;
        }
    }

    //Удалить запись
    public static void deleteRecord(JTable table, Field field) {

        if (table.getSelectedRow() != -1) {
            Query query = ((DefTableModel) table.getModel()).getQuery();
            int rowTable = table.getSelectedRow();
            int rowModel = getSelectedRec(table);
            Record record = query.get(rowModel);
            record.set(field, Query.DEL);
            query.delete(record);
            query.removeRec(rowModel);
            ((DefaultTableModel) table.getModel()).fireTableDataChanged();
            rowTable = (rowTable > 0) ? --rowTable : 0;
            Util.setSelectedRow(table, rowModel);
        } else {
            JOptionPane.showMessageDialog(null, "Ни одна из текущих записей не выбрана", "Предупреждение", JOptionPane.NO_OPTION);
        }
    }

    //Проверка допустимости удаления таблицы
    public static int isDeleteRecord(JTable table, java.awt.Window owner, JTable... tables) {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Ни одна из текущих записей не выбрана", "Предупреждение", JOptionPane.NO_OPTION);
            return 1;
        }
        for (JTable tab : tables) {
            if (tab.getRowCount() != 0) {
                JOptionPane.showMessageDialog(owner, "Перед удалением записи удалите данные в зависимых таблицах", "Предупреждение", JOptionPane.NO_OPTION);
                return 1;
            }
        }
        return JOptionPane.showConfirmDialog(owner, "Вы действительно хотите удалить текущую запись?", "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Проверка допустимости удаления таблицы
    public static int isDeleteRecord(java.awt.Window owner, JTable... tables) {
        for (JTable tab : tables) {
            if (tab.getRowCount() != 0) {
                JOptionPane.showMessageDialog(owner, "Перед удалением записи удалите данные в зависимых таблицах", "Предупреждение", JOptionPane.NO_OPTION);
                return 1;
            }
        }
        return JOptionPane.showConfirmDialog(owner, "Вы действительно хотите удалить текущую запись?", "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Очистить таблицу
    public static void clearTable(JTable... jTable) {
        for (JTable table : jTable) {
            if (table.getModel() instanceof DefTableModel) {
                ((DefTableModel) table.getModel()).getQuery().clear();

            } else if (table.getModel() instanceof DefaultTableModel) {
                ((DefaultTableModel) table.getModel()).getDataVector().clear();
            }
            ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        }
    }

    //Инкапсуляция кнопки в ячейку таблицы
    public static JButton buttonEditorCell(JTable table, int column) { //TODO Заменить индекс столбца на Field в пакете frame
        JButton btn = new JButton("...");
        table.getColumnModel().getColumn(column).setCellEditor(new DefCellEditor(btn));
        return btn;
    }

    //Инкапсуляция кнопки в ячейку таблицы
    public static JButton buttonEditorCell(JTable table, int column, EditorListener listener) {
        JButton btn = new JButton("...");
        table.getColumnModel().getColumn(column).setCellEditor(new DefCellEditor(listener, btn));
        return btn;
    }

    //Выключить режим редактирования
    public static void stopCellEditing(JTable... tableList) {
        for (JTable table : tableList) {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
        }
    }

    //Получить аблицу которая переведена в режим редактирования
    public static JTable getCellEditing(JTable... tableList) {
        for (JTable table : tableList) {
            if (table.isEditing()) {
                return table;
            }
        }
        return null;
    }

    //Слушатель редактирование типа данных и вида ячейки таблицы 
    public static boolean listenerCell(JTable table1, JTable table2, Object component, JTable... tabses) {
        Query qParam1 = ((DefTableModel) table1.getModel()).getQuery();
        Query qParam2 = ((DefTableModel) table2.getModel()).getQuery();

        if (component instanceof DefCellEditor) { //вид и тип ячейки
            DefCellEditor editor = (DefCellEditor) component;
            JTable tab = Util.getCellEditing(tabses);

            DefCellEditor editor2 = (DefCellEditor) table1.getColumnModel().getColumn(1).getCellEditor();
            if (editor.getButton() == editor2.getButton()) {
                Util.formatterCell(qParam1, table1, editor); //установим вид и тип ячейки
            }
            editor2 = (DefCellEditor) table2.getColumnModel().getColumn(1).getCellEditor();
            if (editor.getButton() == editor2.getButton()) {
                Util.formatterCell(qParam2, table2, editor); //установим вид и тип ячейки
            }

        } else if (component != null && component instanceof String) {  //проверка на коррекность ввода
            JTable tab = Util.getCellEditing(tabses);
            String txt = (String) component;
            if (tab == table1) {
                return ParamList.find(qParam1.getAs(Util.getSelectedRec(table1), eJoinpar1.grup)).check(txt);
            }
            if (tab == table2) {
                return ParamList.find(qParam2.getAs(Util.getSelectedRec(table2), eJoinpar1.grup)).check(txt);
            }
        }
        return true;
    }

    //Редактирование параметра ячейки таблицы
    public static void formatterCell(Query query, JTable table, DefCellEditor editor) {

        JTextField txt = editor.getTextField();
        int grup = query.getAs(getSelectedRec(table), eJoinpar1.grup, -1);
        if (grup < 0) { //пользовательский список параметров
            editor.getButton().setVisible(true);
            txt.setEnabled(false);
        } else {
            Enam enam = ParamList.find(grup);
            if (enam.dict() != null) { //системный список параметров
                editor.getButton().setVisible(true);
                txt.setEnabled(false);

            } else { //системные вводимые пользователем
                editor.getButton().setVisible(false);
                txt.setEnabled(true);
                txt.setEditable(true);
            }
        }
    }

    //Слушатель редактирование параметров
    public static void listenerParam(Record record, JTable table, Field grup, Field numb, Field text, JTable... tables) {
        Util.stopCellEditing(tables);
        int row = getSelectedRec(table);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record record2 = query.get(Util.getSelectedRec(table));

        if (eParams.values().length == record.size()) {
            record2.set(grup, record.getInt(eParams.grup));
            record2.set(numb, record.getInt(eParams.numb));
            record2.set(text, null);

        } else if (record.size() == 2) {
            record2.set(grup, record.get(0));
            record2.set(numb, -1);
            record2.set(text, null);

        } else if (record.size() == 1) {
            record2.set(text, record.getStr(0));
        }
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.setSelectedRow(table, row);
    }

    //Слушатель редактирование палитры
    public static void listenerColor(Record record, JTable table, Field color_fk, Field types, JTable... tables) {
        Util.stopCellEditing(tables);
        int row = getSelectedRec(table);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record elemdetRec = query.get(row);
        int group = (eParams.values().length == record.size()) ? record.getInt(eParams.grup) : record.getInt(0);
        elemdetRec.set(color_fk, group);
        if (group == 0 || group == 100000) {
            int val = UseColcalc.P11.id + (UseColcalc.P11.id << 4)  + (UseColcalc.P11.id << 8);
            elemdetRec.set(types, val);
        } else if(group > 0) {
            elemdetRec.set(types, 0);
        } else {
            int val = UseColcalc.P11.id + (UseColcalc.P11.id << 4)  + (UseColcalc.P11.id << 8);
            elemdetRec.set(types, val);
        }
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.setSelectedRow(table, row);
    }

    public static void listenerEnums(Record record, JTable table, Field field_fk, JTable... tables) {
        Util.stopCellEditing(tables);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        int row = getSelectedRec(table);
        query.set(record.getInt(0), Util.getSelectedRec(table), field_fk);
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.setSelectedRow(table, row);
    }

    //Слушатель клика на таблице
    public static void listenerClick(JTable table, List<JTable> tabList) {
        tabList.forEach(tab -> tab.setBorder(null));
        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
        tabList.forEach(tab -> {
            if (tab != table) {
                Util.stopCellEditing(tab);
                if (tab.getModel() instanceof DefTableModel) {
                    ((DefTableModel) tab.getModel()).getQuery().execsql();
                }
            }
        });
    }

    //Программный клик на компоненте
    public static void componentClick(JComponent comp) {
        try {
            Point p = comp.getLocationOnScreen();
            Robot r = new Robot();
            r.mouseMove(p.x + comp.getWidth() / 2, p.y + comp.getHeight() / 2);
            r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(0);
            r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //Проверка на коррекность ввода
    public static void documentFilter1(JTextField... txtField) {
        for (JTextField txtField2 : txtField) {
            ((PlainDocument) txtField2.getDocument()).setDocumentFilter(new DocumentFilter() {
                public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                    if (string.length() > 1 || "0123456789;".indexOf(string) != -1) {
                        super.replace(fb, offset, length, string, attrs);
                    }
                }
            });
        }
    }
}
