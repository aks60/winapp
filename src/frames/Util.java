package frames;

import builder.Wincalc;
import common.eProperty;
import dataset.Conn;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eGroups;
import domain.eParams;
import domain.eSysprod;
import domain.eSystree;
import enums.Enam;
import enums.ParamList;
import enums.UseColor;
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
import frames.swing.DefCellEditor;
import frames.swing.DefTableModel;
import java.util.Enumeration;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import frames.swing.listener.ListenerSQL;
import frames.swing.listener.ListenerObject;
import common.eProfile;
import domain.ePrjprod;
import enums.TypeElem;
import enums.UseArtiklTo;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.ImageIcon;

/**
 * <p>
 * Параметры приложения </p>
 */
public class Util {

    public static DecimalFormat df = new DecimalFormat("0.0#", new DecimalFormatSymbols(Locale.ENGLISH));
    private static GregorianCalendar appCalendar = new GregorianCalendar(); //календарь программы    
    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM); //формат даты
    public static SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy"); //"yyyy-MM-dd" формат для баз где даты utf8
    private static int mes = 0;

    // <editor-fold defaultstate="collapsed" desc="Работа с датой..."> 
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
            return simpleFormat.format(appCalendar.getTime());
        }
        if (date instanceof java.util.Date) {
            return simpleFormat.format(date);
        }
        return "";
    }

    //Текущий год
    public static int getYearCur() {
        return appCalendar.get(Calendar.YEAR);
    }

    public static GregorianCalendar сalendar() {
        return appCalendar;
    }
// </editor-fold> 

    public static Font getFont(int size, int bold) {
        return new Font(eProperty.fontname.read(), bold, Integer.valueOf(eProperty.fontsize.read()) + size);
    }

    public static void expandTree(JTree tree, TreePath path, boolean expand) {
        TreeNode node = (TreeNode) path.getLastPathComponent();

        if (node.getChildCount() >= 0) {
            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                TreeNode n = (TreeNode) enumeration.nextElement();
                TreePath p = path.pathByAddingChild(n);

                expandTree(tree, p, expand);
            }
        }

        if (expand) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    }

    public static String designTitle() {
        try {
            if (eProfile.profile == eProfile.P02) {
                int productID = Integer.valueOf(eProperty.sysprodID.read());
                Record productRec = eSysprod.find(productID);
                if (productRec != null) {

                    String str = productRec.getStr(eSysprod.name);
                    if (str.length() > 6) {
                        if (str.length() < 128) {
                            str = str.substring(6, str.length());
                        } else {
                            str = str.substring(6, 128);
                        }
                    }
                    return "   Изделие: " + eSystree.patch(productRec.getInt(eSysprod.systree_id), "") + "/" + str;
                }

            } else if (eProfile.profile == eProfile.P03) {
                int productID = Integer.valueOf(eProperty.prjprodID.read());
                Record productRec = ePrjprod.find(productID);
                if (productRec != null) {

                    String str = productRec.getStr(ePrjprod.name);
                    if (str.length() > 6) {
                        if (str.length() < 128) {
                            str = str.substring(6, str.length());
                        } else {
                            str = str.substring(6, 128);
                        }
                    }
                    return "   Изделие: " + eSystree.patch(productRec.getInt(ePrjprod.systree_id), "") + "/" + str;
                }
            }
            return "";

        } catch (Exception e) {
            System.err.println("frames.Util.designName() " + e);
            return "";
        }
    }

    public static String designProject() {
        try {
            if (eProfile.profile == eProfile.P02) {
                int productID = Integer.valueOf(eProperty.sysprodID.read());
                Record productRec = eSysprod.find(productID);
                if (productRec != null) {

                    String str = productRec.getStr(eSysprod.name);
                    if (str.length() > 6) {
                        if (str.length() < 128) {
                            str = str.substring(6, str.length());
                        } else {
                            str = str.substring(6, 128);
                        }
                    }
                    return "<html><font size='3' color='blue'>Проект: " + str;
                }

            } else if (eProfile.profile == eProfile.P03) {
                int productID = Integer.valueOf(eProperty.prjprodID.read());
                Record productRec = ePrjprod.find(productID);
                if (productRec != null) {

                    String str = productRec.getStr(ePrjprod.name);
                    if (str.length() > 6) {
                        if (str.length() < 128) {
                            str = str.substring(6, str.length());
                        } else {
                            str = str.substring(6, 128);
                        }
                    }
                    return "   Изделие: " + eSystree.patch(productRec.getInt(ePrjprod.systree_id), "") + "/" + str;
                }
            }
            return "";

        } catch (Exception e) {
            System.err.println("frames.Util.designName() " + e);
            return "";
        }
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

    //Установить бордер
    public static void createEmptyBorder(final Container c) {
        List<Component> comps = getAllComponents(c);
        for (Component comp : comps) {
            if (comp instanceof JPanel) {
                ((JPanel) comp).setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 1, 1, 1));
            }
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
            System.err.println("Query.vrapper() " + e);
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
    public static void scrollRectToIndex(int index, JTable table) {
        int row = table.convertRowIndexToView(index);
        scrollRectToRow(row, table);
    }

    //Прокрутить скроллинг и сделать ячейку видимой
    public static void scrollRectToRow(int row, JTable table) {
        if (table.getRowCount() > row + 4) {
            Rectangle cellRect = table.getCellRect(row + 4, 0, false);
            table.scrollRectToVisible(cellRect);
        } else if (table.getRowCount() > row) {
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
    public static void setSelectedRow(JTable table, int index) {
        if (table.getRowCount() > 0) {

            int row = table.convertRowIndexToView(index);
            if (row < table.getRowCount()) {

                table.setRowSelectionInterval(row, row);
            } else {
                table.setRowSelectionInterval(0, 0);
            }
        }
    }

    //Получить convertRowIndexToModel
    public static int getIndexRec(JTable table) {
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
    public static void insertRecord(JTable table, Field field, ListenerSQL listener) {

        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record record = field.newRecord(Query.INS);
        record.setNo(field.fields()[1], Conn.instanc().genId(field));
        query.add(record);
        listener.action(record);
        ((DefaultTableModel) table.getModel()).fireTableRowsInserted(query.size() - 1, query.size() - 1);
        Util.scrollRectToIndex(query.size() - 1, table);
    }

    //Изменить запись
    public static void updateRecord(JTable table, ListenerSQL listener) {
        Record record = ((DefTableModel) table.getModel()).getQuery().get(Util.getIndexRec(table));
        listener.action(record);
        ((DefaultTableModel) table.getModel()).fireTableRowsUpdated(table.getSelectedRow(), table.getSelectedRow());
    }

    //Удалить запись
    public static void deleteRecord(JTable table) {
        if (table.getSelectedRow() != -1) {
            Query query = ((DefTableModel) table.getModel()).getQuery();
            int row = table.getSelectedRow();
            int index = getIndexRec(table);
            Record record = query.get(index);
            record.set(0, Query.DEL);

            query.delete(record);
            query.removeRec(index);
            ((DefTableModel) table.getModel()).fireTableRowsDeleted(row, row);

            row = (row > 0) ? --row : 0;
            if (query.size() > 0) {
                index = table.convertRowIndexToModel(row);
                Util.setSelectedRow(table, index);
            }
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
                JOptionPane.showMessageDialog(owner, "Перед удалением записи, удалите данные в зависимых таблицах", "Предупреждение", JOptionPane.NO_OPTION);
                return 1;
            }
        }
        return JOptionPane.showConfirmDialog(owner, "Вы действительно хотите удалить текущую запись?", "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Проверка допустимости удаления таблицы
    public static int isDeleteRecord(java.awt.Window owner, JTable... tables) {
        for (JTable tab : tables) {
            if (tab.getRowCount() != 0) {
                JOptionPane.showMessageDialog(owner, "Перед удалением записи, удалите данные в зависимых таблицах", "Предупреждение", JOptionPane.NO_OPTION);
                return 1;
            }
        }
        return JOptionPane.showConfirmDialog(owner, "Вы действительно хотите удалить текущую запись?", "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Установить border и выполнить sql
    public static void updateBorderAndSql(JTable table, List<JTable> tabList) {
        if (tabList != null) {
            tabList.forEach(tab -> tab.setBorder(null));
            tabList.forEach(tab -> {
                if (tab != table) {
                    Util.stopCellEditing(tab);
                    if (tab.getModel() instanceof DefTableModel) {
                        ((DefTableModel) tab.getModel()).getQuery().execsql();
                    }
                }
            });
        }
        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
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
    public static JButton buttonCellEditor(JTable table, int column) {
        JButton btn = new JButton("...");
        table.getColumnModel().getColumn(column).setCellEditor(new DefCellEditor(btn));
        return btn;
    }

    //Инкапсуляция кнопки в ячейку таблицы
    public static JButton buttonCellEditor(JTable table, int column, ListenerObject listener) {
        JButton btn = new JButton("...");
        table.getColumnModel().getColumn(column).setCellEditor(new DefCellEditor(listener, btn));
        return btn;
    }

    //Выключить режим редактирования
    public static void stopCellEditing(JComponent... compList) {
        for (JComponent comp : compList) {
            if (comp instanceof JTable) {
                if (((JTable) comp).isEditing()) {
                    ((JTable) comp).getCellEditor().stopCellEditing();
                }
            } else if (comp instanceof JTree) {
                if (((JTree) comp).isEditing()) {
                    ((JTree) comp).getCellEditor().stopCellEditing();
                }
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

    //Редактирование типа данных и вида ячейки таблицы 
    public static boolean listenerCell(JTable table, Object component, Field params_id) {
        Query qParam = ((DefTableModel) table.getModel()).getQuery();

        if (component instanceof DefCellEditor) { //установим вид и тип ячейки
            DefCellEditor editor = (DefCellEditor) component;
            int paramsID = qParam.getAs(getIndexRec(table), params_id);

            if (paramsID < 0) { //пользовательский список параметров
                editor.getButton().setVisible(true);
                editor.getTextField().setEnabled(false);
            } else {
                Enam enam = ParamList.find(paramsID);
                if (enam.dict() != null) { //системный список параметров
                    editor.getButton().setVisible(true);
                    editor.getTextField().setEnabled(false);

                } else { //системные вводимые пользователем
                    editor.getButton().setVisible(false);
                    editor.getTextField().setEnabled(true);
                    editor.getTextField().setEditable(true);
                }
            }

        } else if (component != null && component instanceof String) {  //проверка на коррекность ввода
            String txt = (String) component;
            return ParamList.find(qParam.getAs(Util.getIndexRec(table), params_id)).check(txt);
        }
        return true;
    }

    //Слушатель редактирование параметров
    public static void listenerParam(Record record, JTable table, Field paramsID, Field text, JTable... tables) {
        Util.stopCellEditing(tables);
        int index = getIndexRec(table);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record record2 = query.get(Util.getIndexRec(table));

        if (eParams.values().length == record.size()) {
            record2.set(paramsID, record.getInt(eParams.id));
            record2.set(text, ParamList.find(record.getInt(eParams.id)).def());

        } else if (record.size() == 2) {
            record2.set(paramsID, record.getInt(0));
            record2.set(text, ParamList.find(record.getInt(0)).def());

        } else if (record.size() == 1) {
            System.out.println("УРА!!! Я НАШОЛ ТЕБЯ.");
            record2.set(text, record.getStr(0));
        }
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.setSelectedRow(table, index);
    }

    //Слушатель редактирование палитры
    public static void listenerColor(Record record, JTable table, Field color_fk, Field types, JTable... tables) {
        Util.stopCellEditing(tables);
        int index = getIndexRec(table);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record elemdetRec = query.get(index);
        int group = (eGroups.values().length == record.size()) ? (-1 * record.getInt(eGroups.id)) : record.getInt(0);
        elemdetRec.set(color_fk, group);
        if (group == 0 || group == 100000) {
            int val = UseColor.PROF.id + (UseColor.PROF.id << 4) + (UseColor.PROF.id << 8);
            elemdetRec.set(types, val);
        } else if (group > 0) {
            elemdetRec.set(types, 0);
        } else {
            int val = UseColor.PROF.id + (UseColor.PROF.id << 4) + (UseColor.PROF.id << 8);
            elemdetRec.set(types, val);
        }
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.setSelectedRow(table, index);
    }

    public static void listenerEnums(Record record, JTable table, Field field_fk, JTable... tables) {
        Util.stopCellEditing(tables);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        int index = getIndexRec(table);
        query.set(record.getInt(0), getIndexRec(table), field_fk);
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        Util.setSelectedRow(table, index);
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
    public static void documentFilter(int numb, JTextField... txtField) {
        for (JTextField txtField2 : txtField) {
            ((PlainDocument) txtField2.getDocument()).setDocumentFilter(new DocumentFilter() {
                public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                    if (numb == 1 && (string.length() > 1 || "0123456789;-".indexOf(string) != -1)) {
                        super.replace(fb, offset, length, string, attrs);
                    } else if (numb == 2 && (string.length() > 1 || "0123456789;-".indexOf(string) != -1)) {
                        super.replace(fb, offset, length, string, attrs);
                    } else if (numb == 3 && (string.length() > 1 || "0123456789.".indexOf(string) != -1)) {
                        super.replace(fb, offset, length, string, attrs);
                    }
                }
            });
        }
    }

    public static ImageIcon createWindraw(Wincalc iwin, Object script, int length) {

        iwin.build(script.toString());
        BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
        iwin.gc2d = bi.createGraphics();
        iwin.gc2d.fillRect(0, 0, length, length);
        iwin.scale = (length / iwin.width > length / iwin.heightAdd) ? length / (iwin.heightAdd + 200) : length / (iwin.width + 200);
        iwin.gc2d.translate(2, 2);
        iwin.gc2d.scale(iwin.scale, iwin.scale);
        iwin.rootArea.draw(length, length);
        ImageIcon image = new ImageIcon(bi);
        return image;
    }
}
