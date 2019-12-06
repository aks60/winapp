/*
 * Класс для связывания полей JTextField
 * с моделью данных
 */
package swing;

import common.Utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import dataset.Field;
import static dataset.Query.SEL;
import dataset.Table;

/**
 * <p>
 * Визуализация полей </p>
 *
 */
public class DefFieldRenderer {

    private int index;
    private DefTableModel tableModel = null;
    private HashMap<JTextComponent, Field> mapTxt = new HashMap<JTextComponent, Field>(16);
    private HashMap<Object, Field> mapBtn = new HashMap<Object, Field>(16);
    private static boolean update = false;

    /**
     * Конструктор 1
     */
    public DefFieldRenderer(DefTableModel tableModel) {
        this.tableModel = tableModel;
    }

    /**
     * Добавить компонент отображения
     */
    public void add(Field field, JTextComponent comp) {
        add(field, comp, null);
    }

    /**
     * Добавляем данные для отображения и слушателей DocListiner для
     * отслеживания редактирования
     */
    public void add(Field field, JTextComponent comp, Object btn) {
        mapTxt.put(comp, field);
        if (btn != null) {
            mapBtn.put(btn, field);
        }
        //если редактирование запрещено
        if (field.meta().edit() == false) {
            comp.setEditable(false);
            comp.setBackground(new java.awt.Color(255, 255, 255));
        }
        comp.getDocument().addDocumentListener(new DocListiner(comp));
    }

    /**
     * Записать данные в компоненты из модели данных
     */
    public void write(Integer index) {
        update = false;
        this.index = index;
        for (Map.Entry<JTextComponent, Field> me : mapTxt.entrySet()) {
            JTextComponent comp = me.getKey();
            Field field = me.getValue();
            Object val = tableModel.query.query(field.tname()).getAs(index, field, "");
            if (index == null || val == null) {
                comp.setText("");
            } else if (field.meta().type().equals(Field.TYPE.DATE)) {
                comp.setText(Utils.DateToStr(val));
            } else {
                comp.setText(val.toString());
            }
            comp.getCaret().setDot(1);
        }
        update = true;
    }

    class DocListiner implements DocumentListener, ActionListener {

        private JTextComponent comp;

        DocListiner(JTextComponent field) {
            this.comp = field;
        }

        public void actionPerformed(ActionEvent e) {
            fieldUpdate();
        }

        public void insertUpdate(DocumentEvent e) {
            fieldUpdate();
        }

        public void removeUpdate(DocumentEvent e) {
            fieldUpdate();
        }

        public void changedUpdate(DocumentEvent e) {
            fieldUpdate();
        }

        /**
         * При редактированиии одного из полей
         */
        public void fieldUpdate() {
            if (update == true && index != -1) {
                if (tableModel.getRowCount() > 0) {
                    tableModel.setValueAt(comp.getText(), index, mapTxt.get(comp));
                }
            }
        }
    }
}
