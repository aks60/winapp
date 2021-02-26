package frames.dialog;

import common.DialogListener;
import common.FrameToFile;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import frames.Util;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *Календарь
 */
public class DicDate extends javax.swing.JDialog {

    protected int rangeTopYear = 25;
    protected DialogListener listener;
    protected GregorianCalendar appCalendar = new GregorianCalendar();
    protected int overDay[] = new int[]{6, 0, 1, 2, 3, 4, 5};

    public DicDate(java.awt.Window owner, DialogListener listener, Integer dxYear) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        this.listener = listener;
        initComponents();

        //диапазон выбора
        jListYear.setModel(new javax.swing.AbstractListModel() {

            public int getSize() {
                return 100;
            }

            public Object getElementAt(int i) {
                return Util.getYearCur() + rangeTopYear - i;
            }
        });

        //Устанавливаем объект appCalendar дату
        int dxYear2 = (dxYear == null) ? 0 : dxYear;
        int year = appCalendar.get(appCalendar.YEAR) + dxYear2;
        appCalendar.set(appCalendar.YEAR, year);

        setDataModelDay();
        jTableDay.requestFocus();
        
        FrameToFile.setFrameSize(this);
        setVisible(true);
    }
    
    //Заполнение модели данных
    protected void setDataModelDay() {

        //Запоминаем текущую дату
        Date date = appCalendar.getTime();
        int month = appCalendar.get(Calendar.MONTH);
        //Очищаем DataModelDay
        jTableDay.clearSelection();
        for (int col = 0; col < jTableDay.getColumnCount(); col++) {
            for (int row = 0; row < jTableDay.getRowCount(); row++) {
                jTableDay.setValueAt(null, row, col);
            }
        }
        //Устанавливаем объект appCalendar на первую дату текущего месяца
        appCalendar.set(Calendar.DAY_OF_MONTH, 1);
        //Заполняем массив DataModelDay днями месяца
        do {
            int day_of_week = overDay[appCalendar.get(Calendar.DAY_OF_WEEK) - 1];
            int week_of_month = appCalendar.get(Calendar.WEEK_OF_MONTH) - 1;
            int day_of_month = appCalendar.get(Calendar.DAY_OF_MONTH);
            jTableDay.setValueAt(day_of_month, week_of_month, day_of_week);
            //Передвигаю объект appCalendar на новый день
            appCalendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (appCalendar.get(Calendar.MONTH) == month);
        //Возвращаем текущую дату
        appCalendar.setTime(date);
        //Выделяем элементы даты
        int week_of_moth = appCalendar.get(Calendar.WEEK_OF_MONTH) - 1;
        int day_of_week = overDay[appCalendar.get(Calendar.DAY_OF_WEEK) - 1];
        jTableDay.setRowSelectionInterval(week_of_moth, week_of_moth);
        jTableDay.setColumnSelectionInterval(day_of_week, day_of_week);
        jListMonth.setSelectedIndex(appCalendar.get(Calendar.MONTH));
        //jListYear.setSelectedValue(appCalendar.get(Calendar.YEAR), true); //��. formComponentShown()
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jListMonth = new javax.swing.JList();
        jTableDay = new javax.swing.JTable();
        jScrollPaneYear = new javax.swing.JScrollPane();
        jListYear = new javax.swing.JList();
        jPanelTool = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Календарь");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jListMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jListMonth.setFont(Util.getFont(-1,0));
        jListMonth.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "  1  Январь", "  2  Февраль", "  3  Март ", "  4  Апрель", "  5  Май", "  6  Июнь", "  7  Июль", "  8  Август", "  9  Сентябрь", "10  Октябрь", "11  Ноябрь", "12  Декабрь" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListMonth.setMaximumSize(new java.awt.Dimension(32767, 32767));
        jListMonth.setPreferredSize(new java.awt.Dimension(71, 190));
        jListMonth.setVisibleRowCount(12);
        jListMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jListMonth.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListMonthValueChanged(evt);
            }
        });

        jTableDay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableDay.setFont(Util.getFont(1,0));
        jTableDay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""}
            },
            new String [] {
                "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDay.setCellSelectionEnabled(true);
        jTableDay.setMaximumSize(new java.awt.Dimension(32767, 32767));
        jTableDay.setPreferredSize(new java.awt.Dimension(525, 192));
        jTableDay.setRowHeight(32);
        jTableDay.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableDay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jTableDay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableDayKeyPressed(evt);
            }
        });
        jTableDay.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                jTableDayValueChanged(event);
            }
        });
        jTableDay.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                jTableDayValueChanged(event);
            }
        });

        jScrollPaneYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPaneYear.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPaneYear.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneYear.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPaneYear.setPreferredSize(new java.awt.Dimension(48, 190));

        jListYear.setFont(Util.getFont(-1,0));
        jListYear.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982", "1981", "1980", "1979", "1978", "1977", "1976", "1975", "1974", "1973", "1972", "1971", "1970", "1969", "1968", "1967", "1966", "1965", "1964", "1963", "1962", "1961", "1960", "1959", "1958", "1957", "1956", "1955", "1954", "1953", "1952", "1951", "1950", "1949", "1948", "1947", "1946", "1945", "1944", "1943", "1942", "1941", "1940", "1939", "1938", "1937", "1936", "1935" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListYear.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListYear.setVisibleRowCount(22);
        jListYear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jListYear.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListYearValueChanged(evt);
            }
        });
        jScrollPaneYear.setViewportView(jListYear);

        jPanelTool.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanelTool.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanelTool.setPreferredSize(new java.awt.Dimension(289, 26));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c037.gif"))); // NOI18N
        btnClose.setText("Отмена");
        btnClose.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnClose.setFocusable(false);
        btnClose.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClose.setMaximumSize(new java.awt.Dimension(80, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(0, 0));
        btnClose.setPreferredSize(new java.awt.Dimension(80, 25));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c020.gif"))); // NOI18N
        btnOk.setText("OK");
        btnOk.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnOk.setFocusable(false);
        btnOk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOk.setMaximumSize(new java.awt.Dimension(80, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(0, 0));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 25));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelToolLayout = new javax.swing.GroupLayout(jPanelTool);
        jPanelTool.setLayout(jPanelToolLayout);
        jPanelToolLayout.setHorizontalGroup(
            jPanelToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelToolLayout.createSequentialGroup()
                .addContainerGap(162, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelToolLayout.setVerticalGroup(
            jPanelToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelToolLayout.createSequentialGroup()
                .addGroup(jPanelToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText(" Год");
        jLabel1.setAlignmentY(6.0F);
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel1.setMaximumSize(new java.awt.Dimension(30, 19));
        jLabel1.setPreferredSize(new java.awt.Dimension(30, 19));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("    Месяц");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel2.setMaximumSize(new java.awt.Dimension(54, 19));
        jLabel2.setPreferredSize(new java.awt.Dimension(54, 19));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText(" Пн    Вт   Ср   Чт     Пт");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel3.setMaximumSize(new java.awt.Dimension(54, 19));
        jLabel3.setPreferredSize(new java.awt.Dimension(54, 19));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Сб    Вс");
        jLabel4.setAlignmentY(6.0F);
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel4.setMaximumSize(new java.awt.Dimension(30, 19));
        jLabel4.setPreferredSize(new java.awt.Dimension(30, 19));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPaneYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jListMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                            .addComponent(jTableDay, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                    .addComponent(jPanelTool, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPaneYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jListMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTableDay, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTool, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
}//GEN-LAST:event_btnCloseActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        listener.action(null);
        dispose();
}//GEN-LAST:event_btnOkActionPerformed

    //Смена дня
    private void jTableDayValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting() == false) {
            int col = jTableDay.getSelectedColumn();
            int row = jTableDay.getSelectedRow();
            if (col != -1 && row != -1) {
                if (jTableDay.getValueAt(row, col) instanceof Integer) {
                    int day = ((Integer) jTableDay.getValueAt(row, col)).intValue();
                    if (day > 0) {
                        appCalendar.set(Calendar.DAY_OF_MONTH, day);
                    }
                }
            }
        }
    }

    //Смена года
    private void jListYearValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListYearValueChanged
        if (evt.getValueIsAdjusting() == false) {
            int index = jListYear.getSelectedIndex();
            //Integer val = Integer.valueOf(jListYear.getSelectedValue().toString());
            //appCalendar.set(Calendar.YEAR, val);            
            appCalendar.set(Calendar.YEAR, Util.getYearCur() + 25 - index);
            setDataModelDay();
        }
    }//GEN-LAST:event_jListYearValueChanged
    //Смена месяца
    private void jListMonthValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListMonthValueChanged
        if (evt.getValueIsAdjusting() == false) {
            int index = jListMonth.getSelectedIndex();
            appCalendar.set(Calendar.MONTH, index);
            setDataModelDay();
        }
    }//GEN-LAST:event_jListMonthValueChanged

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
//        if (evt.getClickCount() == 2) {
//            btnOkActionPerformed(null);
//        }
    }//GEN-LAST:event_jTableMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //mDic.DicDate.window = null;
    }//GEN-LAST:event_formWindowClosed

    private void jTableDayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableDayKeyPressed
        listener.action(null);
        dispose();
    }//GEN-LAST:event_jTableDayKeyPressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        int year = appCalendar.get(Calendar.YEAR);
        jListYear.setSelectedValue(year, true);
        int selectedIndex = jListYear.getSelectedIndex();
        int firstVisibleIndex = jListYear.getFirstVisibleIndex();
        int dx = firstVisibleIndex - selectedIndex + 6;
        if (year + dx < Util.getYearCur() + rangeTopYear) {
            jListYear.setSelectedValue(year + dx, true);
            jListYear.setSelectedValue(year, true);
        }
    }//GEN-LAST:event_formComponentShown

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jListMonth;
    private javax.swing.JList jListYear;
    private javax.swing.JPanel jPanelTool;
    private javax.swing.JScrollPane jScrollPaneYear;
    private javax.swing.JTable jTableDay;
    // End of variables declaration//GEN-END:variables
}
