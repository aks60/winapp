/*
 * Связывание полей JTextField с моделью данных
 */
package frames.swing;

import frames.Util;
import enums.Enam;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import dataset.Field;
import domain.eSystree;
import javax.swing.JTable;
import javax.swing.JTree;

/**
 * <p>
 * Визуализация полей </p>
 */
public class DefFieldEditor<E> {

    private E comp = null;
    private HashMap<Field, Enam[]> mapEnam = new HashMap(8);
    private HashMap<JTextComponent, Field> mapTxt = new HashMap(16);
    private static boolean update = false;

    //Конструктор
    public DefFieldEditor(E comp) {
        this.comp = comp;
    }

    //Добавить компонент отображения
    public void add(Field field, JTextComponent jtxt) {
        mapTxt.put(jtxt, field);
        if (field.meta().edit() == false) { //если редактирование запрещено
            jtxt.setEditable(false);
            jtxt.setBackground(new java.awt.Color(255, 255, 255));
        }
        jtxt.getDocument().addDocumentListener(new DocListiner(jtxt));
    }

    //Добавить компонент отображения 
    public void add(Field field, JTextComponent jtxt, Enam[] enam) {
        add(field, jtxt);
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

    //Загрузить данные в компоненты
    public void load() {
        if (comp instanceof JTable) {
            load(Util.getIndexRec((JTable) comp));

        } else if (comp instanceof JTree) {
            DefMutableTreeNode node = (DefMutableTreeNode) ((JTree) comp).getLastSelectedPathComponent();
            update = false;
            try {
                for (Map.Entry<JTextComponent, Field> me : mapTxt.entrySet()) {
                    JTextComponent jtxt = me.getKey();
                    Field field = me.getValue();
                    Object val = node.rec().get(field);
                    text(jtxt, field, val);
                }
            } catch (Exception e) {
                System.err.println("Oшибка:DefFieldEditor.load() " + e);
            } finally {
                update = true;
            }
        }
    }

    //Загрузить данные в компоненты
    public void load(Integer index) {
        update = false;
        try {
            if (index != null && index != -1) {
                for (Map.Entry<JTextComponent, Field> me : mapTxt.entrySet()) {
                    JTextComponent jtxt = me.getKey();
                    Field field = me.getValue();
                    Object val = ((DefTableModel) ((JTable) comp).getModel()).getQuery().table(field).get(index, field);
                    text(jtxt, field, val);
                }
            }
        } finally {
            update = true;
        }
    }

    private void text(JTextComponent jtxt, Field field, Object val) {
        if (val == null) {
            if (field.meta().type().equals(Field.TYPE.STR)) {
                jtxt.setText("");
            } else {
                jtxt.setText("0");
            }
        } else if (field.meta().type().equals(Field.TYPE.DATE)) {
            jtxt.setText(Util.DateToStr(val));

        } else if (mapEnam.containsKey(field)) {
            for (Enam enam : mapEnam.get(field)) {
                if (val.equals(enam.numb())) {
                    jtxt.setText(enam.text());
                }
            }
        } else {
            jtxt.setText(val.toString());
        }
        jtxt.getCaret().setDot(1);
    }

    //Редактирование
    class DocListiner implements DocumentListener, ActionListener {

        private JTextComponent jtxt;

        DocListiner(JTextComponent jtxt) {
            this.jtxt = jtxt;
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
            try {
                if (update == true) {

                    if (comp instanceof JTable) {
                        int row = ((JTable) comp).getSelectedRow();
                        if (row != -1) {
                            Field field = mapTxt.get(jtxt);
                            String str = jtxt.getText();
                            if (((JTable) comp).getRowCount() > 0) {
                                if (field.meta().type().equals(Field.TYPE.FLT) || field.meta().type().equals(Field.TYPE.DBL)) {
                                    str = String.valueOf(str).replace(',', '.');
                                }
                                ((DefTableModel) ((JTable) comp).getModel()).getQuery().set(str, row, field);
                            }
                        }

                    } else if (comp instanceof JTree) {
                        DefMutableTreeNode node = (DefMutableTreeNode) ((JTree) comp).getLastSelectedPathComponent();
                        node.rec().set(mapTxt.get(jtxt), jtxt.getText());
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка:DefFieldEditor.DocListiner.fieldUpdate() " + e);
            }
        }
    }
}
