/*
 * Связывание полей JTextField с моделью данных
 */
package jframe.swing;

import common.Util;
import enums.Enam;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import dataset.Field;
import java.awt.Component;
import javax.swing.JTable;

/**
 * <p>
 * Визуализация полей </p>
 */
public class DefFieldEditor {

    private JTable table = null;
    private HashMap<Field, Enam[]> mapEnam = new HashMap(8);
    private HashMap<JTextComponent, Field> mapTxt = new HashMap(16);
    private static boolean update = false;

    //Конструктор
    public DefFieldEditor(JTable table) {
        this.table = table;
    }

    //Добавить компонент отображения
    public void add(Field field, JTextComponent comp) {

        mapTxt.put(comp, field);
        if (field.meta().edit() == false) { //если редактирование запрещено
            comp.setEditable(false);
            comp.setBackground(new java.awt.Color(255, 255, 255));
        }
        comp.getDocument().addDocumentListener(new DocListiner(comp));
    }

    //Добавить компонент отображения 
    public void add(Field field, JTextComponent comp, Enam[] enam) {

        add(field, comp);
        mapEnam.put(field, enam);
    }

    //Очистить текст
    public void clear() {
        for (Map.Entry<JTextComponent, Field> me : mapTxt.entrySet()) {
            JTextComponent comp = me.getKey();
            Field field = me.getValue();
            comp.setText(null);
            if (field.meta().type().equals(Field.TYPE.STR)) {
                comp.setText("");
            } else {
                comp.setText("0");
            }
        }
    }

    //Загрузить данные в компоненты из модели данных
    public void load() {
        load(Util.getSelectedRec(table));
    }

    //Загрузить данные в компоненты из модели данных
    public void load(Integer row) {
        update = false;
        try {
            if (row != null) {
                for (Map.Entry<JTextComponent, Field> me : mapTxt.entrySet()) {
                    JTextComponent comp = me.getKey();
                    Field field = me.getValue();
                    Object val = ((DefTableModel) table.getModel()).getQuery().table(field).get(row, field);

                    if (val == null || row == -1) {
                        if (field.meta().type().equals(Field.TYPE.STR)) {
                            comp.setText("");
                        } else {
                            comp.setText("0");
                        }
                    } else if (field.meta().type().equals(Field.TYPE.DATE)) {
                        comp.setText(Util.DateToStr(val));

                    } else if (mapEnam.containsKey(field)) {
                        for (Enam enam : mapEnam.get(field)) {
                            if (val.equals(enam.numb())) {
                                comp.setText(enam.text());
                            }
                        }
                    } else {
                        comp.setText(val.toString());
                    }

                    comp.getCaret().setDot(1);
                }
            }
        } finally {
            update = true;
        }
    }

    //Редактирование
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

        //При редактированиии одного из полей
        public void fieldUpdate() {
            int row = table.getSelectedRow();
            if (update == true && row != -1) {
                if (table.getRowCount() > 0) {
                    ((DefTableModel) table.getModel()).setValueAt(comp.getText(), row, mapTxt.get(comp));
                }
            }
        }
    }
}
