package frames.swing;

import common.EditorListener;
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

public class DefCellEditor extends DefaultCellEditor {

    private EditorListener listenerCell = null;
    protected JComponent panel = new javax.swing.JPanel();
    protected JButton button = null;

    public DefCellEditor(JButton button) {
        super(new JTextField());
        init(button);
    }

    public DefCellEditor(EditorListener listener, JButton button) {
        super(new JTextField());
        this.listenerCell = listener;
        init(button);
    }

    private void init(JButton button) {
        setClickCountToStart(2);
        this.button = button;
        button.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        button.setFocusable(false);
        button.setPreferredSize(new java.awt.Dimension(24, 18));

        panel = new javax.swing.JPanel();
        panel.setBorder(null);
        panel.setBackground(new java.awt.Color(240, 240, 240));
        panel.setLayout(new java.awt.BorderLayout());

        JTextField editorText = (JTextField) editorComponent;
        editorText.setPreferredSize(new java.awt.Dimension(60, 18));
        editorText.setEditable(false);
        editorText.setBorder(null);
        editorText.setBackground(new java.awt.Color(255, 255, 255));
        panel.add(editorText, java.awt.BorderLayout.CENTER);
        panel.add(button, java.awt.BorderLayout.EAST);

        PlainDocument doc = (PlainDocument) editorText.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (listenerCell == null) {
                    super.insertString(fb, offset, string, attr);

                } else {
                    if (listenerCell.action(string) == true) { //проверка на коррекность ввода
                        super.insertString(fb, offset, string, attr);
                    }
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                if (listenerCell == null) {
                    super.replace(fb, offset, length, string, attrs);

                } else {
                    if (listenerCell.action(string) == true) {  //проверка на коррекность ввода
                        super.replace(fb, offset, length, string, attrs);
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        Field field = ((DefTableModel) table.getModel()).columns[column];
        ((JTextField) editorComponent).setEditable(field.meta().type() == Field.TYPE.STR); //разрешить редактирование стрингу
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

//    @Override
//    public boolean stopCellEditing() {
//        JTextField txt = (JTextField) getComponent();
//        if (listenerCell != null) {
//            if (listenerCell.action(txt) == false) {
//                return false;
//            }
//        }
//        return super.stopCellEditing();
//    }

    public JButton getButton() {
        return button;
    }

    public JTextField getTextField() {
        return (JTextField) editorComponent;
    }
}
