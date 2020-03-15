/*
 * Связывание полей JTextField с моделью данных
 */
package swing;

import common.Util;
import dataset.Enam;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import dataset.Field;
import java.awt.Component;

/**
 * <p>
 * Визуализация полей </p>
 */
public class DefFieldRenderer {

    private int row;
    private DefTableModel tableModel = null;
    private HashMap<Field, Enam[]> mapEnam = new HashMap(8);
    private HashMap<JTextComponent, Field> mapTxt = new HashMap(16);
    private HashMap<Component, Field> mapBtn = new HashMap(16);
    private static boolean update = false;

    //Конструктор 1
    public DefFieldRenderer(DefTableModel tableModel) {
        this.tableModel = tableModel;
    }

    //Добавить компонент отображения
    public void add(Field field, JTextComponent comp) {

        mapTxt.put(comp, field);
        //если редактирование запрещено
        if (field instanceof Field) {
            if (field.meta().edit() == false) {
                comp.setEditable(false);
                comp.setBackground(new java.awt.Color(255, 255, 255));
            }
        }
        comp.getDocument().addDocumentListener(new DocListiner(comp));
    }

    //Данные для отображения и слушатель DocListiner для отслеживания редактирования
    public void add(Field field, JTextComponent comp, Component btn) {

        add(field, comp);
        mapBtn.put(btn, field);

    }

    public void add(Field field, JTextComponent comp, Enam[] enam) {

        add(field, comp);
        mapEnam.put(field, enam);
    }

    public void add(Field field, JTextComponent comp, Component btn, Enam[] enam) {
        add(field, comp, btn);
        mapEnam.put(field, enam);
    }

    //Записать в компоненты из модели данных
    public void write(Integer row) {

        update = false;
        this.row = row;
        for (Map.Entry<JTextComponent, Field> me : mapTxt.entrySet()) {
            JTextComponent comp = me.getKey();
            Field field = me.getValue();
            Object val = tableModel.query.table(field).get(row, field);

            if (val == null || row == -1) {
                if (field.meta().type().equals(Field.TYPE.STR)) {
                    comp.setText("");
                } 
                else {                    
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

        //При редактированиии одного из полей
        public void fieldUpdate() {
            if (update == true && row != -1) {
                if (tableModel.getRowCount() > 0) {
                    tableModel.setValueAt(comp.getText(), row, mapTxt.get(comp));
                }
            }
        }
    }
}
