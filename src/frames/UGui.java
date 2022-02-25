package frames;

import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.AreaStvorka;
import builder.model.Com5t;
import builder.model.ElemSimple;
import builder.script.GsonRoot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import builder.param.ParamList;
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
import common.listener.ListenerObject;
import common.eProfile;
import common.listener.ListenerRecord;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eFurndet;
import domain.eProprod;
import enums.Layout;
import enums.PKjson;
import enums.Type;
import frames.swing.DefMutableTreeNode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * Параметры приложения </p>
 */
public class UGui {

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

    public static DefMutableTreeNode loadWinTree(Wincalc iwin) {
        DefMutableTreeNode root = new DefMutableTreeNode(iwin.rootArea);
        root.add(new DefMutableTreeNode(new Com5t(Type.PARAM) {
        }));
        DefMutableTreeNode frm = root.add(new DefMutableTreeNode(new Com5t(Type.FRAME) {
        }));
        frm.add(new DefMutableTreeNode(iwin.rootArea.frames.get(Layout.BOTT)));
        ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        frm.add(new DefMutableTreeNode(iwin.rootArea.frames.get(Layout.RIGHT)));
        ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        frm.add(new DefMutableTreeNode(iwin.rootArea.frames.get(Layout.TOP)));
        ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        frm.add(new DefMutableTreeNode(iwin.rootArea.frames.get(Layout.LEFT)));
        ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        for (Com5t com : iwin.rootArea.childs) {
            if (com.type != Type.STVORKA) {
                if (com instanceof ElemSimple) {
                    frm.add(new DefMutableTreeNode(com));
                    if (com.type != Type.GLASS) {
                        ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
                        }));
                    }
                } else {
                    for (Com5t com2 : ((AreaSimple) com).childs) {
                        if (com2.type != Type.STVORKA) {
                            if (com2 instanceof ElemSimple) {
                                frm.add(new DefMutableTreeNode(com2));
                                if (com2.type != Type.GLASS) {
                                    ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
                                    }));
                                }
                            } else {
                                for (Com5t com3 : ((AreaSimple) com2).childs) {
                                    if (com3.type != Type.STVORKA) {
                                        if (com3 instanceof ElemSimple) {
                                            frm.add(new DefMutableTreeNode(com3));
                                            if (com3.type != Type.GLASS) {
                                                ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
                                                }));
                                            }
                                        } else {
                                            for (Com5t com4 : ((AreaSimple) com3).childs) {
                                                if (com4.type != Type.STVORKA) {
                                                    if (com4 instanceof ElemSimple) {
                                                        frm.add(new DefMutableTreeNode(com4));
                                                        if (com4.type != Type.GLASS) {
                                                            ((DefMutableTreeNode) frm.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
                                                            }));
                                                        }
                                                    }
                                                } else {
                                                    loadWinTree(iwin, root, com4);
                                                }
                                            }
                                        }
                                    } else {
                                        loadWinTree(iwin, root, com3);
                                    }
                                }
                            }
                        } else {
                            loadWinTree(iwin, root, com2);
                        }
                    }
                }
            } else {
                loadWinTree(iwin, root, com);
            }
        }
        return root;
    }

    public static void loadWinTree(Wincalc iwin, DefMutableTreeNode root, Com5t com) {
        DefMutableTreeNode nodeStv = root.add(new DefMutableTreeNode(com));
        AreaStvorka stv = (AreaStvorka) com;
        nodeStv.add(new DefMutableTreeNode(stv.frames.get(Layout.BOTT)));
        ((DefMutableTreeNode) nodeStv.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        nodeStv.add(new DefMutableTreeNode(stv.frames.get(Layout.RIGHT)));
        ((DefMutableTreeNode) nodeStv.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        nodeStv.add(new DefMutableTreeNode(stv.frames.get(Layout.TOP)));
        ((DefMutableTreeNode) nodeStv.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        nodeStv.add(new DefMutableTreeNode(stv.frames.get(Layout.LEFT)));
        ((DefMutableTreeNode) nodeStv.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
        }));
        for (Com5t com2 : ((AreaSimple) com).childs) {
            if (com2 instanceof ElemSimple) {
                nodeStv.add(new DefMutableTreeNode(com2));
                if (com2.type != Type.GLASS) {
                    ((DefMutableTreeNode) nodeStv.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
                    }));
                }
            } else {
                for (Com5t com3 : ((AreaSimple) com2).childs) {
                    if (com3 instanceof ElemSimple) {
                        nodeStv.add(new DefMutableTreeNode(com3));
                        if (com3.type != Type.GLASS) {
                            ((DefMutableTreeNode) nodeStv.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
                            }));
                        }
                    } else {
                        for (Com5t com4 : ((AreaSimple) com3).childs) {
                            if (com4 instanceof ElemSimple) {
                                nodeStv.add(new DefMutableTreeNode(com4));
                                if (com4.type != Type.GLASS) {
                                    ((DefMutableTreeNode) nodeStv.getLastChild()).add(new DefMutableTreeNode(new Com5t(Type.JOINING) {
                                    }));
                                }
                            }
                        }
                    }
                }
            }
        }
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

    public static String paramdefAdd(String script, int paramDef, Query qParams) {
        Gson gson = new GsonBuilder().create();
        GsonRoot gsonRoot = gson.fromJson(script, GsonRoot.class);
        JsonObject jsonObj = gson.fromJson(gsonRoot.param(), JsonObject.class);
        JsonArray jsonArr = jsonObj.getAsJsonArray(PKjson.ioknaParam);
        jsonArr = (jsonArr == null) ? new JsonArray() : jsonArr;
        int indexRemov = -1;        
        int titleID1 = qParams.stream().filter(rec -> paramDef == rec.getInt(eParams.id))
                .findFirst().orElse(eParams.newRecord2()).getInt(eParams.params_id);
        for (int i = 0; i < jsonArr.size(); i++) { 
            
            int it = jsonArr.get(i).getAsInt();
            int titleID2 = qParams.stream().filter(rec -> (it == rec.getInt(eParams.id))).findFirst().orElse(eParams.newRecord2()).getInt(eParams.params_id);
            if (titleID1 == titleID2) {
                indexRemov = i;
            }
        }
        if (indexRemov != -1) {
            jsonArr.remove(indexRemov);
        }
        jsonArr.add(paramDef);
        jsonObj.add(PKjson.ioknaParam, jsonArr);
        gsonRoot.param(jsonObj);
        return gson.toJson(gsonRoot);
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
                Record productRec = eProprod.find(productID);
                if (productRec != null) {

                    String str = productRec.getStr(eProprod.name);
                    if (str.length() > 6) {
                        if (str.length() < 128) {
                            str = str.substring(6, str.length());
                        } else {
                            str = str.substring(6, 128);
                        }
                    }
                    return "   Изделие: " + eSystree.patch(productRec.getInt(eProprod.systree_id), "") + "/" + str;
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
                Record productRec = eProprod.find(productID);
                if (productRec != null) {

                    String str = productRec.getStr(eProprod.name);
                    if (str.length() > 6) {
                        if (str.length() < 128) {
                            str = str.substring(6, str.length());
                        } else {
                            str = str.substring(6, 128);
                        }
                    }
                    return "   Изделие: " + eSystree.patch(productRec.getInt(eProprod.systree_id), "") + "/" + str;
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
        try {
            int row = table.convertRowIndexToView(index);
            scrollRectToRow(row, table);
        } catch (Exception e) {
            System.err.println("Ошибка:UGui.scrollRectToIndex() " + e);
        }
    }

    //Прокрутить скроллинг и сделать ячейку видимой
    public static void scrollRectToRow(int row, JTable table) {
        try {
            if (table.getRowCount() > row + 4) {
                Rectangle cellRect = table.getCellRect(row + 4, 0, false);
                table.scrollRectToVisible(cellRect);
            } else if (table.getRowCount() > row) {
                Rectangle cellRect = table.getCellRect(row, 0, false);
                table.scrollRectToVisible(cellRect);
            }
        } catch (Exception e) {
            System.err.println("Ошибка:UGui.scrollRectToRow() " + e);
        }
    }

    //Выделить запись
    public static void setSelectedRow(JTable table) {
        
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            
            int column = table.getSelectedColumn();
            if (column != -1) {
                table.setColumnSelectionInterval(column, column);
            }
        }
    }

    //Выделить запись
    public static void setSelectedIndex(JTable table, int index) {
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

    //Получить convertRowIndexToModel
    public static int getIndexRec(JTable table, int def) {
        if (table.getSelectedRow() != -1) {
            return table.convertRowIndexToModel(table.getSelectedRow());
        }
        return def;
    }

    //Поиск Record в модели по row table
    public static Record findRecordModel(Query q, JTable table, int row) {
        int id = (int) table.getValueAt(row, table.getColumnCount() - 1);
        return q.stream().filter(rec -> rec.getInt(1) == 1).findFirst().orElse(null);
    }

    //Отменить сортировку
    public static void stopSorting(JTable... table) {
        for (JTable tab : table) {
            tab.getRowSorter().setSortKeys(null);
        }
    }

    //Вставить запись
    public static void insertRecordEnd(JTable table, Field field, ListenerRecord listener) {

        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record record = field.newRecord(Query.INS);
        record.setNo(field.fields()[1], Conn.genId(field));
        query.add(record);
        listener.action(record);
        ((DefaultTableModel) table.getModel()).fireTableRowsInserted(query.size() - 1, query.size() - 1);
        UGui.setSelectedIndex(table, query.size() - 1);
        UGui.scrollRectToIndex(query.size() - 1, table);
    }

    //Вставить запись
    public static void insertRecordCur(JTable table, Field field, ListenerRecord listener) {

        int index = UGui.getIndexRec(table);
        index = (index == -1) ? 0 : index;
        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record record = field.newRecord(Query.INS);
        record.setNo(field.fields()[1], Conn.genId(field));
        query.add(index, record);
        listener.action(record);
        ((DefaultTableModel) table.getModel()).fireTableRowsInserted(index, index);
        UGui.setSelectedIndex(table, index);
    }

    //Изменить запись
    public static void updateRecord(JTable table, ListenerRecord listener) {
        Record record = ((DefTableModel) table.getModel()).getQuery().get(UGui.getIndexRec(table));
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
                UGui.setSelectedIndex(table, index);
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

    //Обновление записи в таблице JTable
    public static void fireTableRowUpdated(JTable table) {
        ((DefaultTableModel) table.getModel()).fireTableRowsUpdated(table.getSelectedRow(), table.getSelectedRow());
    }

    //Установить border и выполнить sql
    public static void updateBorderAndSql(JTable table, List<JTable> tabList) {
        if (tabList != null) {
            tabList.forEach(tab -> tab.setBorder(null));
            tabList.forEach(tab -> {
                if (tab != table) {
                    UGui.stopCellEditing(tab);
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
            return ParamList.find(qParam.getAs(UGui.getIndexRec(table), params_id)).check(txt);
        }
        return true;
    }

    //Слушатель редактирование параметров
    public static void listenerParam(Record record, JTable table, Field paramsID, Field text, JTable... tables) {
        UGui.stopCellEditing(tables);
        int index = getIndexRec(table);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        Record record2 = query.get(UGui.getIndexRec(table));

        if (eParams.values().length == record.size()) {
            record2.set(paramsID, record.getInt(eParams.id));
            record2.set(text, ParamList.find(record.getInt(eParams.id)).def());

        } else if (record.size() == 2) {
            record2.set(paramsID, record.getInt(0));
            record2.set(text, ParamList.find(record.getInt(0)).def());

        } else if (record.size() == 1) {
            String val = record2.getStr(text);

            if (record.get(0) == null) {
                record2.set(text, null);

            } else if (val != null && val.isEmpty() == false) {
                record2.set(text, val + ";" + record.getStr(0));

            } else {
                record2.set(text, record.getStr(0));
            }
        }
        ((DefaultTableModel) table.getModel()).fireTableRowsUpdated(index, index);
    }

    //Слушатель редактирование палитры
    public static void listenerColor(Record record, JTable table, Field color_fk, Field types, JTable... tables) {
        UGui.stopCellEditing(tables);
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
        ((DefaultTableModel) table.getModel()).fireTableRowsUpdated(index, index);
        UGui.setSelectedIndex(table, index);
    }

    public static void listenerEnums(Record record, JTable table, Field field_fk, JTable... tables) {
        UGui.stopCellEditing(tables);
        Query query = ((DefTableModel) table.getModel()).getQuery();
        int index = getIndexRec(table);
        query.set(record.getInt(0), getIndexRec(table), field_fk);
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        UGui.setSelectedIndex(table, index);
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

    //Список для выбора ручек, подвесов, накладок в створке   
    public static Query artTypeToFurndetList(int furnitureID, Query qArtikl) {
        HashSet<Integer> setPk = new HashSet();
        qArtikl.stream().forEach(rec -> setPk.add(rec.getInt(eArtikl.id)));
        HashSet<Integer> setFilter = new HashSet();
        Query qResult = new Query(eArtikl.values());
        Query qFurndetAll = new Query(eFurndet.values()).select(eFurndet.up);
        List<Record> furndetList1 = qFurndetAll.stream().filter(rec -> setPk.contains(rec.getInt(eFurndet.artikl_id)) == true).collect(toList());
        List<Record> furndetList2 = furndetList1.stream().filter(rec -> rec.getInt(eFurndet.furniture_id1) == furnitureID).collect(toList());

        //Цикл детализации конкретной записи фурнитуры
        for (Record furndetRec2 : furndetList2) {

            //Цикл по детализации определённого typeArtikl для конкретной фурнитуры
            if (furndetRec2.get(eFurndet.furniture_id2) == null) { //не набор
                int ID = setPk.stream().filter(it -> it == furndetRec2.getInt(eFurndet.artikl_id)).findFirst().orElse(-1);
                if (ID != -1) {
                    setFilter.add(ID);
                }
            } else { //это набор
                int naborID = furndetRec2.getInt(eFurndet.furniture_id2);
                //Цикл по всей детализации определённого typeArtikl
                for (Record furndetRec1 : furndetList1) {

                    int ID = setPk.stream().filter(it -> it == furndetRec1.getInt(eFurndet.artikl_id)).findFirst().orElse(-1);
                    if (ID != -1) {
                        setFilter.add(ID);
                    }
                }
            }
        }
        for (Integer id : setFilter) {
            Record record = qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == id).findFirst().get();
            //if (record.getStr(eArtikl.code).charAt(0) != '@') {
            qResult.add(record);
            //}
        }
        return qResult;
    }

    public static HashSet<Record> artiklToColorSet(int artiklID) {
        HashSet<Record> colorSet = new HashSet();
        Query artdetList = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.artikl_id, "=", artiklID);
        artdetList.stream().forEach(rec -> {

            if (rec.getInt(eArtdet.color_fk) < 0) {
                eColor.query().forEach(rec2 -> {
                    if (rec2.getInt(eColor.colgrp_id) == Math.abs(rec.getInt(eArtdet.color_fk))) {
                        colorSet.add(rec2);
                    }
                });
            } else {
                colorSet.add(eColor.find(rec.getInt(eArtdet.color_fk)));
            }
        });
        return colorSet;
    }
}
