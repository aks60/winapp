package swing;

import java.awt.Component;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.EventObject;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import common.FrameListener;
import common.Util;
import dataset.Field;

/**
 * Специальный редактор сложных компонентов. Конструктор передаёт объект в
 * FrameListener. После чего передаётся событие клика на кнопке в FrameListener
 * (listenerFrame.request(rsm)).
 */
public class DefFieldEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    protected FrameListener listenerFrame = null;
    protected JTable editorTable = null;  //таблица редактирования    
    protected JComponent editorComponent;  //компонента отображения    
    protected JButton editorButton;  //кнопка выбора из справочника    
    protected JTextField editorText; //компонента редактирования
    private static int clickCountToStart = 2; //количество кликов для перехода в режим редактирования.
    protected RsEditorDelegate delegate; //делегат редактора

    //Конструктор 1 редактора JTextField.
    public DefFieldEditor(final JTextField textField) {
        textField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        editorComponent = textField;
        delegate = new RsEditorDelegate() {

            public void setValue(Object value) {
                textField.setText((value != null) ? value.toString() : "");
            }

            public Object getCellEditorValue() {
                return textField.getText();
            }
        };
        textField.addActionListener(delegate);
    }

    //Конструктор 2 редактора JCheckBox.
    public DefFieldEditor(final JCheckBox checkBox) {
        editorComponent = checkBox;
        delegate = new RsEditorDelegate() {

            public void setValue(Object value) {
                boolean selected = false;
                if (value instanceof Boolean) {
                    selected = ((Boolean) value).booleanValue();
                } else if (value instanceof String) {
                    selected = value.equals("true");
                }
                checkBox.setSelected(selected);
            }

            public Object getCellEditorValue() {
                return Boolean.valueOf(checkBox.isSelected());
            }
        };
        checkBox.addActionListener(delegate);
        checkBox.setRequestFocusEnabled(false);
    }

    //Конструктор 3 редактора JComboBox.
    public DefFieldEditor(final JComboBox comboBox) {
        editorComponent = comboBox;
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        delegate = new RsEditorDelegate() {

            public void setValue(Object value) {
                comboBox.setSelectedItem(value);
            }

            public Object getCellEditorValue() {
                return comboBox.getSelectedItem();
            }

            public boolean shouldSelectCell(EventObject anEvent) {
                if (anEvent instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent) anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }

            public boolean stopCellEditing() {
                if (comboBox.isEditable()) {
                    // Commit edited value.
                    comboBox.actionPerformed(new ActionEvent(DefFieldEditor.this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        comboBox.addActionListener(delegate);
    }

    //Конструктор 4 редактора JButton.
    public DefFieldEditor(FrameListener listener, JButton editorButton) {
        listenerFrame = listener;
        this.editorButton = editorButton;
        editorButton.addActionListener(this);
        editorButton.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        editorButton.setFocusable(false);
        editorButton.setPreferredSize(new java.awt.Dimension(24, 18));

        this.editorComponent = new javax.swing.JPanel();
        this.editorComponent.setBorder(null);
        this.editorComponent.setBackground(new java.awt.Color(240, 240, 240));
        this.editorComponent.setLayout(new java.awt.BorderLayout());

        editorText = new javax.swing.JTextField();
        editorText.setPreferredSize(new java.awt.Dimension(60, 18));
        editorText.setBorder(null);
        editorText.setBackground(new java.awt.Color(255, 255, 255));
        this.editorComponent.add(editorText, java.awt.BorderLayout.CENTER);
        this.editorComponent.add(editorButton, java.awt.BorderLayout.EAST);

        delegate = new RsEditorDelegate() {

            public void setValue(Object value) {
                editorText.setText((value != null) ? value.toString() : "");
            }

            public Object getCellEditorValue() {
                return editorText.getText();
            }
        };

    }

    //Устанавливает редактор ячеек. см. FrameAdapter.initTable()
    public static void initCellEditor(FrameListener listener, JTable table) {

        DefTableModel model = (DefTableModel) table.getModel();
        TableColumnModel columnModel = table.getColumnModel();
        for (int index = 0; index < columnModel.getColumnCount(); index++) {

            Field field = model.getColumn(index);
            if (field.meta().type().equals(Field.TYPE.INTsp)) {
                columnModel.getColumn(index).setCellEditor(new DefFieldEditor(listener, new JButton("...")));

            } else if (field.meta().type().equals(Field.TYPE.DATE)) {
                columnModel.getColumn(index).setCellEditor(new DefFieldEditor(listener, new JButton("...")));

            } else if (field.meta().type().equals(Field.TYPE.BOOL)) {
                String str[] = {"+", "-"};
                columnModel.getColumn(index).setCellEditor(new DefFieldEditor(new JComboBox(str)));

            } else {
                JTextField jTextField = new JTextField();
                columnModel.getColumn(index).setCellEditor(new DefFieldEditor(jTextField));
            }
        }
    }

    // Определение количество кликов чтобы начать редактировать ячейку.
    public static void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    //Получить результат редактирования.
    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    //Признак редактирования.
    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    //Передаёт в фрейм событие клика на ячейке с кнопкой.
    public void actionPerformed(ActionEvent e) {
        if (listenerFrame == null) {
            return;
        }
        if (editorTable.getModel() instanceof TableModel) {
            TableModel rsm = (TableModel) editorTable.getModel();
            listenerFrame.actionRequest(rsm);
        } else {
            listenerFrame.actionRequest(null);
        }
    }

    //Выбор ячейки при редактировании.
    public boolean shouldSelectCell(EventObject anEvent) {
        return delegate.shouldSelectCell(anEvent);
    }

    //Остановить редактирование
    public boolean stopCellEditing() {
        return delegate.stopCellEditing();
    }

    //Отменить редактирование.
    public void cancelCellEditing() {
        delegate.cancelCellEditing();
    }

    //Главная функция редактирования.
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        editorTable = table;
        DefTableModel rsm = (DefTableModel) table.getModel();
        Field field = rsm.getColumn(column);
        if (field.meta().type().equals(Field.TYPE.DATE)) {
            delegate.setValue(Util.DateToStr(value));
//        } else if (field.meta().type().equals(eField.TYPE.DBL) || field.meta().type().equals(eField.TYPE.FLT)) {
//            String val = String.valueOf(value).replace(',', '.');
//            delegate.setValue(val);
        } else {
            delegate.setValue(value);
        }
        return editorComponent;
    }

    //Делегирование редактора компонентов.
    protected class RsEditorDelegate implements ActionListener, ItemListener, Serializable {

        protected Object value;

        /**
         * Возвращает текущее значение в редакторе.
         */
        public Object getCellEditorValue() {
            return value;
        }

        /**
         * Устанавливаем значение в value.
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Признак редактирования.
         */
        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent == true) {
                boolean editable = ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
//                if (editable == true) {
//                    clickCountToStart = 1;
//                    startCellEditing(null);
//                }
                return editable;
            }
            return true;
        }

        /**
         * Выбор ячейки при редактировании.
         */
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        /**
         * Начать редактирование.
         */
        //public boolean startCellEditing(EventObject anEvent) {
        public void startCellEditing(EventObject anEvent) {
            //listenerEditor.initStartEditing(null);
        }

        /**
         * Остановить редактирование
         */
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        /**
         * Отменить редактирование.
         */
        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        /**
         * Событие прекращ. редактирования.
         */
        public void actionPerformed(ActionEvent e) {
            DefFieldEditor.this.stopCellEditing();
        }

        /**
         * Событие изменения статуса редатирования.
         */
        public void itemStateChanged(ItemEvent e) {
            DefFieldEditor.this.stopCellEditing();
        }
    }
}
