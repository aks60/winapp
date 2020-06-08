package frames;

import common.FrameToFile;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import frames.swing.DefTableModel;

public class TestFrame extends javax.swing.JFrame {

    private String src = "jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251";
    private Connection cn;

    public TestFrame() {
        initComponents();
        initElements();;
        loadingTab1();
    }

    private void loadingTab1() {
        try {
            cn = java.sql.DriverManager.getConnection(src, "sysdba", "masterkey");
            sql1.setText("select * from furnlst order by fname");
            ResultSet rs = cn.createStatement().executeQuery(sql1.getText());
            ResultSetMetaData rsmd = rs.getMetaData();

            Vector column = new Vector();
            for (int i = 0; i < rsmd.getColumnCount(); ++i) {
                column.add(rsmd.getColumnName(i + 1));
            }
            Vector<Vector> data = new Vector();
            while (rs.next()) {
                Vector vector = new Vector();
                for (int i = 0; i < column.size(); i++) {
                    vector.add(rs.getObject(i + 1));
                }
                data.add(vector);
            }
            tab1.setModel(new DefaultTableModel(data, column));

        } catch (Exception e) {
            System.out.println("frames.TestFrame.selectionTab1() " + e);
        }
    }

    private void selectionTab2() {
        try {
            int row = tab1.getSelectedRow();
            if (row != -1) {
                Object id = tab1.getValueAt(row, 0);
                sql2.setText("select * from furnspc where funic = " + id);
                ResultSet rs = cn.createStatement().executeQuery(sql2.getText());
                ResultSetMetaData rsmd = rs.getMetaData();

                Vector column = new Vector();
                for (int i = 0; i < rsmd.getColumnCount(); ++i) {
                    column.add(rsmd.getColumnName(i + 1));
                }
                Vector<Vector> data = new Vector();
                while (rs.next()) {
                    Vector vector = new Vector();
                    for (int i = 0; i < column.size(); i++) {
                        vector.add(rs.getObject(i + 1));
                    }
                    data.add(vector);
                }
                tab2.setModel(new DefaultTableModel(data, column));
            }

        } catch (Exception e) {
            System.out.println("frames.TestFrame.selectionTab2() " + e);
        }
    }

    private void selectionTab3() {
        try {
//            int row = tab2.getSelectedRow();
//            if (row != -1) {
//                Object id = tab2.getValueAt(row, 3);
//                sql3.setText("select * from furnspc where fincs = " + id);
//                ResultSet rs = cn.createStatement().executeQuery(sql3.getText());
//                ResultSetMetaData rsmd = rs.getMetaData();
//
//                Vector column = new Vector();
//                for (int i = 0; i < rsmd.getColumnCount(); ++i) {
//                    column.add(rsmd.getColumnName(i + 1));
//                }
//                Vector<Vector> data = new Vector();
//                while (rs.next()) {
//                    Vector vector = new Vector();
//                    for (int i = 0; i < column.size(); i++) {
//                        vector.add(rs.getObject(i + 1));
//                    }
//                    data.add(vector);
//                }
//                tab3.setModel(new DefaultTableModel(data, column));
//            }
        } catch (Exception e) {
            System.out.println("frames.TestFrame.selectionTab3() " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        pan1 = new javax.swing.JPanel();
        sql1 = new javax.swing.JTextField();
        pan11 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        txt1 = new javax.swing.JTextField();
        btn1 = new javax.swing.JButton();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        pan22 = new javax.swing.JPanel();
        lab2 = new javax.swing.JLabel();
        txt2 = new javax.swing.JTextField();
        sql2 = new javax.swing.JTextField();
        btn2 = new javax.swing.JButton();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        pan33 = new javax.swing.JPanel();
        lab3 = new javax.swing.JLabel();
        txt3 = new javax.swing.JTextField();
        sql3 = new javax.swing.JTextField();
        btn3 = new javax.swing.JButton();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TEST");

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(900, 29));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/prop/hint"); // NOI18N
        btnClose.setToolTipText(bundle.getString("Закрыть")); // NOI18N
        btnClose.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnClose.setFocusable(false);
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setMaximumSize(new java.awt.Dimension(25, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(25, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(25, 25));
        btnClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap(649, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setLayout(new javax.swing.BoxLayout(center, javax.swing.BoxLayout.PAGE_AXIS));

        pan1.setPreferredSize(new java.awt.Dimension(800, 24));
        pan1.setLayout(new java.awt.BorderLayout());

        sql1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sql1.setMinimumSize(new java.awt.Dimension(6, 20));
        sql1.setPreferredSize(new java.awt.Dimension(600, 20));
        pan1.add(sql1, java.awt.BorderLayout.CENTER);

        pan11.setLayout(new java.awt.BorderLayout());

        lab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lab1.setPreferredSize(new java.awt.Dimension(80, 18));
        pan11.add(lab1, java.awt.BorderLayout.WEST);

        txt1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt1.setPreferredSize(new java.awt.Dimension(61, 20));
        txt1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt1CaretUpdate(evt);
            }
        });
        pan11.add(txt1, java.awt.BorderLayout.CENTER);

        pan1.add(pan11, java.awt.BorderLayout.WEST);

        btn1.setText("...");
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });
        pan1.add(btn1, java.awt.BorderLayout.EAST);

        center.add(pan1);

        scr1.setPreferredSize(new java.awt.Dimension(800, 304));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);

        center.add(scr1);

        pan2.setPreferredSize(new java.awt.Dimension(800, 24));
        pan2.setLayout(new java.awt.BorderLayout());

        pan22.setLayout(new java.awt.BorderLayout());

        lab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lab2.setPreferredSize(new java.awt.Dimension(80, 18));
        pan22.add(lab2, java.awt.BorderLayout.WEST);

        txt2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt2.setPreferredSize(new java.awt.Dimension(61, 20));
        txt2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt2CaretUpdate(evt);
            }
        });
        pan22.add(txt2, java.awt.BorderLayout.CENTER);

        pan2.add(pan22, java.awt.BorderLayout.WEST);

        sql2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pan2.add(sql2, java.awt.BorderLayout.CENTER);

        btn2.setText("...");
        pan2.add(btn2, java.awt.BorderLayout.EAST);

        center.add(pan2);

        scr2.setPreferredSize(new java.awt.Dimension(800, 304));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);

        center.add(scr2);

        pan3.setPreferredSize(new java.awt.Dimension(800, 24));
        pan3.setLayout(new java.awt.BorderLayout());

        pan33.setLayout(new java.awt.BorderLayout());

        lab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lab3.setPreferredSize(new java.awt.Dimension(80, 18));
        pan33.add(lab3, java.awt.BorderLayout.WEST);

        txt3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt3.setPreferredSize(new java.awt.Dimension(61, 20));
        txt3.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt3CaretUpdate(evt);
            }
        });
        pan33.add(txt3, java.awt.BorderLayout.CENTER);

        pan3.add(pan33, java.awt.BorderLayout.WEST);

        sql3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pan3.add(sql3, java.awt.BorderLayout.CENTER);

        btn3.setText("...");
        pan3.add(btn3, java.awt.BorderLayout.EAST);

        center.add(pan3);

        scr3.setPreferredSize(new java.awt.Dimension(800, 300));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);

        center.add(scr3);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void txt1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt1CaretUpdate

        lab1.setText(tab1.getColumnName(tab1.getSelectedColumn()));
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tab1.getRowSorter();

        if (txt1.getText().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("^" + txt1.getText(), tab1.getSelectedColumn()));
        }
    }//GEN-LAST:event_txt1CaretUpdate

    private void txt2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt2CaretUpdate

        lab2.setText(tab2.getColumnName(tab2.getSelectedColumn()));
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tab2.getRowSorter();

        if (txt2.getText().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("^" + txt2.getText(), tab2.getSelectedColumn()));
        }
    }//GEN-LAST:event_txt2CaretUpdate

    private void txt3CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt3CaretUpdate
        lab3.setText(tab3.getColumnName(tab3.getSelectedColumn()));
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tab3.getRowSorter();

        if (txt3.getText().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("^" + txt3.getText(), tab3.getSelectedColumn()));
        }
    }//GEN-LAST:event_txt3CaretUpdate

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed

    }//GEN-LAST:event_btn1ActionPerformed
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel center;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab3;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan22;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan33;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JTextField sql1;
    private javax.swing.JTextField sql2;
    private javax.swing.JTextField sql3;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt3;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {
        new FrameToFile(this, btnClose);
        tab1.setAutoCreateRowSorter(true);
        tab2.setAutoCreateRowSorter(true);
        tab3.setAutoCreateRowSorter(true);

        tab1.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting() == false) {
                selectionTab2();
            }
        });
        tab2.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting() == false) {
                selectionTab3();
            }
        });
    }
}
