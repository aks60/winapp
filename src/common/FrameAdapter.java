package common;

import dataset.Field;
import dataset.Query;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import swing.DefTableModel;
import swing.DefCellEditor;

/**
 * <p>
 * Адаптер фреймов </p>
 */
public class FrameAdapter {

    private static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            setBackground(new java.awt.Color(212, 208, 200));
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    };
    public static Timer timeFrame = new Timer(600, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(eProfile.appframe, "Размеры формы сохранены в файле");
        }
    });

    /**
     * Инициализация таблицы
     */
    public static void initTable(JTable table,  Object... obj) {

        DefTableModel tableModel = (DefTableModel) table.getModel();
        //установим сортировку колонок
        //table.setRowSorter(tableModel.sorter);
        //добавим слухача выделения строки таблицы
        //table.getSelectionModel().addListSelectionListener(tableModel.rowSL);
        //добавим слухача выделения столбца таблицы
        //table.getColumnModel().getSelectionModel().addListSelectionListener(tableModel.colSL);
        if (obj != null) {
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] instanceof FrameListener) {
                    //добавим слухача режима редактирования, создадим редакторы ячеек
                    //ActionListener actionListener = (ActionListener) obj[i];
                    //tableModel.actionListener = actionListener;
                    //FieldEditor.initCellEditor(frameListener, table);
                } else if (obj[i] instanceof FocusListener) {
                    //добавим слухача фокуса компонентов
                    FocusListener focusListener = (FocusListener) obj[i];
                    table.addFocusListener(focusListener);
                } else if (obj[i] instanceof ListSelectionListener) {
                    //добавим слухача выделения строки таблицы
                    ListSelectionListener listSelectionListener = (ListSelectionListener) obj[i];
                    table.getSelectionModel().addListSelectionListener(listSelectionListener);
                }
            }
        }
    }
    
    public static void setFocusable(Container container, FocusListener listener) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                setFocusable((Container) comp, listener);
            }
            if (comp instanceof JTable) {
                ((JTable) comp).addFocusListener(listener);
            }
        }
    }  
}
