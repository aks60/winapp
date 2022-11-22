package frames.swing;

import common.UCom;
import common.eProp;
import dataset.Field;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import common.listener.ListenerObject;

public class DefCellEditor extends DefaultCellEditor {

//    private int check = 0;
    private ListenerObject listenerCell = null;
    private JComponent panel = new javax.swing.JPanel();
    private JButton button = null;

//    public DefCellEditor(int check) {
//        super(new JTextField());
//        this.check = check;
//        init(true);
//        filter();
//        this.getComponent().setLocale(eProp.locale);
//    }

    public DefCellEditor(JButton button) {
        super(new JTextField());
        init(false);
        button(button);
        this.getComponent().setLocale(eProp.locale);
    }

    public DefCellEditor(ListenerObject listener, JButton button) {
        super(new JTextField());
        this.listenerCell = listener;
        init(false);
        button(button);
        filter();
        this.getComponent().setLocale(eProp.locale);
    }

    private void init(boolean editable) {
        JTextField editorText = (JTextField) editorComponent;
        panel.setBorder(null);
        panel.setBackground(new java.awt.Color(240, 240, 240));
        panel.setLayout(new java.awt.BorderLayout());
        editorText.setFont(frames.UGui.getFont(0,0));
        editorText.setPreferredSize(new java.awt.Dimension(60, 18));
        editorText.setEditable(editable);
        editorText.setBorder(null);
        editorText.setBackground(new java.awt.Color(255, 255, 255));
        panel.add(editorText, java.awt.BorderLayout.CENTER);
    }

    private void button(JButton button) {
        setClickCountToStart(2);
        this.button = button;
        button.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        button.setFocusable(false);
        button.setPreferredSize(new java.awt.Dimension(24, 18));
        panel.add(button, java.awt.BorderLayout.EAST);
    }

    private void filter() {

        JTextField editorText = (JTextField) editorComponent;
        PlainDocument doc = (PlainDocument) editorText.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.length() > 1 || check(string)) { //проверка на коррекность ввода
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                if (string.length() > 1 || check(string)) {  //проверка на коррекность ввода
                    super.replace(fb, offset, length, string, attrs);
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        if (((JTextField) editorComponent).isEditable() == false) {
            Field field = ((DefTableModel) table.getModel()).columns[column];
            ((JTextField) editorComponent).setEditable(field.meta().type() == Field.TYPE.STR); //разрешить редактирование стрингу
        }
        delegate.setValue(value);
        return panel;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent == true) {
            if (listenerCell != null && ((MouseEvent) anEvent).getClickCount() == 2) {
                listenerCell.action(DefCellEditor.this);
            }
        }
        return delegate.isCellEditable(anEvent);
    }

    private boolean check(String s) {
//        if (check == 0) {
            return listenerCell.action(s);
//        } else if (check == 3 && "0123456789.,".indexOf(s) != -1) {
//            return true;
//        } else if (check == 4 && "0123456789;".indexOf(s) != -1) {
//            return true;
//        } else if (check == 5 && "0123456789-;".indexOf(s) != -1) {
//            return true;
//        } else if (check == 6 && "0123456789,-;".indexOf(s) != -1) {
//            return true;
//        }
//        return false;
    }

    public JButton getButton() {
        return button;
    }

    public JTextField getTextField() {
        return (JTextField) editorComponent;
    }
}
