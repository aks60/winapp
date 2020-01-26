/*
 * Класс для связывания полей JTextField
 * с моделью данных
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
import java.util.ArrayList;

/**
 * <p>
 * Визуализация полей </p>
 *
 */
public class DefFieldRenderer {

    private int index;
    private DefTableModel tableModel = null;
    private ArrayList<Enam> listEnam = new ArrayList(8);
    private HashMap<JTextComponent, Enam> mapTxt = new HashMap(16);
    private HashMap<Component, Enam> mapBtn = new HashMap(16);
    private static boolean update = false;

    //Конструктор 1
    public DefFieldRenderer(DefTableModel tableModel) {
        this.tableModel = tableModel;
    }

    //Конструктор 2
    public DefFieldRenderer(DefTableModel tableModel, Enam[]... enams) {
        this.tableModel = tableModel;
        for (Enam[] en : enams) {
            for (Enam it : en) {
                listEnam.add(it);
            }
        }
    }

    //Добавить компонент отображения
    public void add(Enam field, JTextComponent comp) {
        add(field, comp, null);
    }

    //Данные для отображения и слушатель DocListiner для отслеживания редактирования
    public void add(Enam field, JTextComponent comp, Component btn) {
        mapTxt.put(comp, field);
        if (btn != null) {
            mapBtn.put(btn, field);
        }
        //если редактирование запрещено
        if (field instanceof Field) {
            if (((Field) field).meta().edit() == false) {
                comp.setEditable(false);
                comp.setBackground(new java.awt.Color(255, 255, 255));
            }            
        }
        comp.getDocument().addDocumentListener(new DocListiner(comp));
    }
    
    //Записать данные в компоненты из модели данных
    public void write(Integer index) {
        update = false;
        this.index = index;
        for (Map.Entry<JTextComponent, Enam> me : mapTxt.entrySet()) {
            JTextComponent comp = me.getKey();
            if (me.getValue() instanceof Field) {
                
                Field field = (Field) me.getValue();
                Object val = tableModel.query.table(field.tname()).getAs(index, field, "");
                if (index == null || val == null) {
                    comp.setText("");
                } else if (field.meta().type().equals(Field.TYPE.DATE)) {
                    comp.setText(Util.DateToStr(val));
                } else {
                    comp.setText(val.toString());
                }                
            } else {
                
                comp.setText("aksenov777");
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
            if (mapTxt.get(comp) instanceof Field) {
                if (update == true && index != -1) {
                    if (tableModel.getRowCount() > 0) {
                        tableModel.setValueAt(comp.getText(), index, (Field) mapTxt.get(comp));
                    }
                }
            }
        }
    }
}
