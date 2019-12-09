package swing;

import java.awt.Component;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.EventObject;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import common.Util;
import common.FrameListener;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import dataset.Field;

/**
 * <p>
 * Специальный редактор сложных компонентов. </p>
 * <p>
 * Конструктор передаёт в объект FrameListener. После чего передаётся событие
 * клика на кнопке в FrameListener (listenerFrame.request(rsm)).</p>
 */
public class DefFieldEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    protected FrameListener listenerFrame = null;
    // Таблица редактирования
    protected JTable editorTable = null;
    // Компонента отображения
    protected JComponent editorComponent;
    // Кнопка выбора из справочника
    protected JButton editorButton;
    // Компонента редактирования
    protected JTextField editorText;
    // Делегат редактора
    protected RsEditorDelegate delegate;

    /**
     * Конструктор 1 редактора JTextField.
     */
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

    /**
     * Конструктор 2 редактора JCheckBox.
     */
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

    /**
     * Конструктор 3 редактора JComboBox.
     */
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

    /**
     * Конструктор 4 редактора JButton.
     */
    public DefFieldEditor(FrameListener listener, JButton editorButton) {
        listenerFrame = listener;
        this.editorButton = editorButton;
        editorButton.addActionListener(this);
        editorButton.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        editorButton.setFocusable(false);
        editorButton.setPreferredSize(new java.awt.Dimension(20, 18));

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

    /**
     * Получить результат редактирования.
     */
    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    /**
     * Признак редактирования.
     */
    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    /**
     * Передаёт в фрейм событие клика на ячейке с кнопкой.
     */
    public void actionPerformed(ActionEvent e) {
        /*if (listenerFrame == null) {
            return;
        }
        if (editorTable.getModel() instanceof DefTableModel) {
            TableModel rsm = (TableModel) editorTable.getModel();
            listenerFrame.request(rsm);
        } else {
            listenerFrame.request(null);
        }*/
    }

    /**
     * Главная функция редактирования.
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
//        editorTable = table;
//        TableModel rsm = (TableModel) table.getModel();
//        Field field = rsm.getColumns()[column];
//        if (field.meta().type().equals(Field.TYPE.DATE)) {
//            delegate.setValue(Utils.DateToStr(value));
////        } else if (field.meta().type().equals(eField.TYPE.DBL) || field.meta().type().equals(eField.TYPE.FLT)) {
////            String val = String.valueOf(value).replace(',', '.');
////            delegate.setValue(val);
//        } else {
            delegate.setValue(value);
//        }
        return editorComponent;
    }

    /**
     * Делегирование редактора компонентов.
     */
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
                boolean editable = ((MouseEvent) anEvent).getClickCount() >= 2;
                return editable;
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
