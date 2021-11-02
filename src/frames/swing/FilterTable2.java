package frames.swing;

import dataset.Field;
import dataset.Query;
import frames.UGui;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class FilterTable2 extends javax.swing.JPanel {

    private JTable table = null;
    private JTable[] tableList = null;
    private int indexColumn = 0;
    private int indexBegin = 0;
    private boolean search = false;

    public FilterTable2() {
        initComponents();
    }

    public FilterTable2(int index, JTable... table) {
        initComponents();
        tableList = table;
        mousePressed(table[0]);
        labFilter.setText(table[0].getColumnName(index));
        txtFilter.setName(table[0].getName());
        this.indexBegin = index;
    }

    public JLabel getLab() {
        return labFilter;
    }

    public JTextField getTxt() {
        return null; //txtFilter;
    }

    public void mousePressed(JTable table) {
//        try {
//            if (txtFilter.getText().length() == 0) {
//                this.table = table;
//                labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
//                txtFilter.setName(table.getName());
//            }
//        } catch (Exception e) {
//            System.err.println("ОШИБКА:swing.FilterTable.mousePressed() " + e);
//        }
    }

    //https://coderoad.ru/9549108/%D0%A1%D0%B3%D0%B5%D0%BD%D0%B5%D1%80%D0%B8%D1%80%D1%83%D0%B9%D1%82%D0%B5-%D1%81%D0%BE%D0%B1%D1%8B%D1%82%D0%B8%D0%B5-%D0%B4%D0%B2%D0%BE%D0%B9%D0%BD%D0%BE%D0%B3%D0%BE-%D1%89%D0%B5%D0%BB%D1%87%D0%BA%D0%B0-%D0%BC%D1%8B%D1%88%D0%B8-%D0%B2-Java-Swing
    public void genericTableColumnClick(Component comp_sender, Component comp_receiver) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                comp_receiver //получатель
                        .dispatchEvent(new MouseEvent(comp_sender, //отправитель
                                MouseEvent.MOUSE_CLICKED, 1, MouseEvent.BUTTON1, 0, 0, 1, false
                        ));
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn1 = new javax.swing.JButton();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JComboBox<>();
        btn2 = new javax.swing.JButton();
        checkFilter = new javax.swing.JCheckBox();

        setMaximumSize(new java.awt.Dimension(460, 20));
        setMinimumSize(new java.awt.Dimension(360, 20));
        setPreferredSize(new java.awt.Dimension(460, 20));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn1.setBorder(null);
        btn1.setMaximumSize(new java.awt.Dimension(30, 23));
        btn1.setMinimumSize(new java.awt.Dimension(30, 23));
        btn1.setPreferredSize(new java.awt.Dimension(30, 23));
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActiPerf(evt);
            }
        });
        add(btn1);

        labFilter.setText("Поле не выбрано");
        labFilter.setMaximumSize(new java.awt.Dimension(140, 18));
        labFilter.setMinimumSize(new java.awt.Dimension(140, 18));
        labFilter.setPreferredSize(new java.awt.Dimension(140, 18));
        add(labFilter);

        txtFilter.setEditable(true);
        txtFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(120, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(120, 20));
        txtFilter.setPreferredSize(new java.awt.Dimension(120, 20));
        add(txtFilter);

        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b041.gif"))); // NOI18N
        btn2.setBorder(null);
        btn2.setMaximumSize(new java.awt.Dimension(26, 23));
        btn2.setMinimumSize(new java.awt.Dimension(26, 23));
        btn2.setPreferredSize(new java.awt.Dimension(26, 23));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActiPerf(evt);
            }
        });
        add(btn2);

        checkFilter.setText("в конце строки");
        checkFilter.setMaximumSize(new java.awt.Dimension(120, 18));
        checkFilter.setMinimumSize(new java.awt.Dimension(103, 18));
        checkFilter.setPreferredSize(new java.awt.Dimension(120, 18));
        add(checkFilter);
    }// </editor-fold>//GEN-END:initComponents

    private void btn1ActiPerf(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActiPerf
//        try {
//            if (search == true) {
//                btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif")));
//                search = !search;
//                txtCaretUpdate(null);
//            } else {
//                btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c043.gif")));
//                search = !search;
//                String txt = txtFilter.getText();
//                txtFilter.setText("");
//                txtCaretUpdate(null);
//                txtFilter.setText(txt);
//            }
//            UGui.scrollRectToRow(table.getSelectedRow() - 1, table);
//        } catch (Exception e) {
//            System.err.println("ERROR");
//        }
    }//GEN-LAST:event_btn1ActiPerf

    private void btn2ActiPerf(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActiPerf
//        try {
//            if (txtFilter.getText().isEmpty()) {
//                btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b064.gif")));
//                try {
//                    Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
//                    Transferable t = cb.getContents(null);
//                    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//                        txtFilter.setText(t.getTransferData(DataFlavor.stringFlavor).toString());
//                    }
//                } catch (UnsupportedFlavorException | IOException ex) {
//                    System.out.println("frames.swing.TableFilter.btn2ActiPerf()");
//                }
//            } else {
//                btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b041.gif")));
//                txtFilter.setText("");
//            }
//        } catch (Exception e) {
//            System.err.println("ERROR");
//        }
    }//GEN-LAST:event_btn2ActiPerf

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JComboBox<String> txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
