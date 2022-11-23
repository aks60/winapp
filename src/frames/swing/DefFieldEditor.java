/*
 * Связывание полей JTextField с моделью данных
 */
package frames.swing;

import frames.UGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import dataset.Field;
import dataset.Query;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTree;

/**
 * <p>
 * Визуализация полей </p>
 */
public class DefFieldEditor {

    private JComponent comp = null;
    private Query query = null;
    private HashMap<JTextComponent, Field> mapTxt = new HashMap(16);
    private static boolean update = false;

    //Конструктор
    public DefFieldEditor(JTree comp) {
        this.comp = comp;
    }

    //Конструктор
    public DefFieldEditor(JTable comp) {
        this.comp = comp;
        this.query = ((DefTableModel) comp.getModel()).getQuery();
    }

    //Конструктор
    public DefFieldEditor(JTable comp, Query query) {
        this.comp = comp;
        this.query = query;
    }

    //Добавить компонент отображения
    public void add(Field field, JTextComponent jtxt) {
        mapTxt.put(jtxt, field);
        //jtxt.setLocale(eProp.locale);
        if (field.meta().edit() == false) { //если редактирование запрещено
            jtxt.setEditable(false);
            jtxt.setBackground(new java.awt.Color(255, 255, 255));
        }
        jtxt.getDocument().addDocumentListener(new DocListiner(jtxt));
    }

    //Очистить текст
    public void clear() {
        for (Map.Entry<JTextComponent, Field> me : mapTxt.entrySet()) {
            JTextComponent textcomp = me.getKey();
            Field field = me.getValue();
            textcomp.setText(null);
            if (field.meta().type().equals(Field.TYPE.STR)) {
                textcomp.setText("");
            } else {
                textcomp.setText("0");
            }
        }
    }

    //Загрузить данные в компоненты
    public void load() {
        if (comp instanceof JTable) {
            load(UGui.getIndexRec((JTable) comp));

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
                    Object val = query.table(field).get(index, field);
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
            jtxt.setText(UGui.DateToStr(val));

        } else {
            if (jtxt instanceof JFormattedTextField) {
                ((JFormattedTextField) jtxt).setValue(val);
            } else {
                jtxt.setText(val.toString());
            }
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
                        int index = UGui.getIndexRec((JTable) comp);
                        if (index != -1) {
                            Field field = mapTxt.get(jtxt);
                            String str = jtxt.getText();
                            if (((JTable) comp).getRowCount() > 0) {
                                if (List.of(Field.TYPE.FLT, Field.TYPE.DBL).contains(field.meta().type())) {
                                    str = String.valueOf(str).replace(',', '.');
                                }
                                query.table(field).set(str, index, field);
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
