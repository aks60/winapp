package frames;

import common.UCom;
import dataset.Query;
import domain.eCurrenc;
import domain.eGroups;
import enums.TypeGroups;
import static frames.UGui.getIndexRec;
import frames.swing.DefCellEditorBtn;
import frames.swing.DefCellEditorNumb;
import frames.swing.DefCellRendererNumb;
import frames.swing.DefTableModel;
import frames.swing.TableFieldFilter;
import java.awt.Component;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import report.ExecuteCmd;
import report.HtmlOfTable;

public class Groups extends javax.swing.JFrame {

    private Query qCurrenc = new Query(eCurrenc.values());
    private Query qDecInc = new Query(eGroups.values());
    private Query qArtSeri = new Query(eGroups.values());
    private Query qArtIncr = new Query(eGroups.values());
    private Query qArtDecr = new Query(eGroups.values());
    private Query qCategProf = new Query(eGroups.values());
    private Query qColGrp = new Query(eGroups.values());
    private Query qColMap = new Query(eGroups.values());
    private Query qCategVst = new Query(eGroups.values());
    private Query qCategKit = new Query(eGroups.values());
    private int mode = 0;

    public Groups(int mode) {
        this.mode = mode;
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        List<Component> list = (mode == 1) ? List.of(pan1, pan2, pan5, pan6, pan7) : List.of(pan3, pan4);
        list.forEach(comp -> tabb.remove(comp));
        setTitle((mode == 1) ? "Справочники" : "Коэффициенты");
    }

    private void loadingData() {
        qCurrenc.select(eCurrenc.up, "order by", eCurrenc.name);
        qColGrp.select(eGroups.up, "where", eGroups.grup, "= 2", "order by", eGroups.name);
        qColMap.select(eGroups.up, "where", eGroups.grup, "= 7", "order by", eGroups.name);
        qArtSeri.select(eGroups.up, "where", eGroups.grup, "= 3", "order by", eGroups.name);
        qArtIncr.select(eGroups.up, "where", eGroups.grup, "= 4", "order by", eGroups.name);
        qArtDecr.select(eGroups.up, "where", eGroups.grup, "= 5", "order by", eGroups.name);
        qCategProf.select(eGroups.up, "where", eGroups.grup, "= 6", "order by", eGroups.name);
        qCategVst.select(eGroups.up, "where", eGroups.grup, "= 8", "order by", eGroups.name);
        qCategKit.select(eGroups.up, "where", eGroups.grup, "= 10", "order by", eGroups.name);
        qDecInc.select(eGroups.up, "where", eGroups.grup, "= 9 and", eGroups.id, "in (2101, 2104, 2073)", "order by", eGroups.name);
    }

    public void loadingModel() {
        new DefTableModel(tab1, qArtIncr, eGroups.name, eGroups.val);
        new DefTableModel(tab2, qArtDecr, eGroups.name, eGroups.val);
        new DefTableModel(tab3, qArtSeri, eGroups.name);
        new DefTableModel(tab4, qCategProf, eGroups.name);
        new DefTableModel(tab5, qColGrp, eGroups.name, eGroups.val);
        new DefTableModel(tab6, qCurrenc, eCurrenc.name, eCurrenc.par_case1, eCurrenc.par_case2, eCurrenc.cross_cour);
        new DefTableModel(tab7, qDecInc, eGroups.name, eGroups.val);

        tab6.getColumnModel().getColumn(3).setCellEditor(new DefCellEditorNumb(4));
        tab6.getColumnModel().getColumn(3).setCellRenderer(new DefCellRendererNumb(4));
        List.of(tab1, tab2, tab5).forEach(tab -> tab.getColumnModel().getColumn(1).setCellEditor(new DefCellEditorNumb(3)));
        tab7.getColumnModel().getColumn(1).setCellEditor(new DefCellEditorNumb(3));
        tab7.getColumnModel().getColumn(1).setCellRenderer(new DefCellRendererNumb(3) {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (column == 1) {
                    int id = qDecInc.getAs(row, eGroups.id);
                    if (id == 2073) {
                        if ((double) value == 3) {
                            value = "1 мм";
                        } else if ((double) value == 2) {
                            value = "0,5 мм";
                        } else if ((double) value == 1) {
                            value = "0,1 мм";
                        } else if ((double) value == 0) {
                            value = "точное";
                        }
                    } else if (id != -1 && (id == 2055 || id == 2056 || id == 2058 || id == 2101 || id == 2104)) {
                        value = UCom.format(value, scale) + "%";
                    }
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        List.of(tab1, tab2, tab3, tab4, tab5, tab6, tab7).forEach(tab -> UGui.setSelectedRow(tab));

        UGui.buttonCellEditor(tab7, 1, (component) -> {

            if (component instanceof DefCellEditorBtn) { //установим вид и тип ячейки
                DefCellEditorBtn editor = (DefCellEditorBtn) component;
                int groupsID = qDecInc.getAs(getIndexRec(tab7), eGroups.id);
                if (groupsID == 2073) {
                    editor.getButton().setVisible(true);
                    editor.getTextField().setEnabled(false);
                } else { //вводимые пользователем
                    editor.getButton().setVisible(false);
                    editor.getTextField().setEnabled(true);
                    editor.getTextField().setEditable(true);
                }
            } else {  //проверка на коррекность ввода
                String txt = (String) component;
                return ("0123456789,".indexOf(txt) != -1);
            }
            return true;

        }).addActionListener(event -> {
            String arrStr[] = {"точное", "0,1 мм", "0,5 мм", "1,0 мм"};
            double arrDbl[] = {0, 1, 2, 3};
            Object result = JOptionPane.showInputDialog(Groups.this, "Выберите точность рассчёта",
                    "Округление длины профилей", JOptionPane.QUESTION_MESSAGE, null, arrStr, arrStr[0]);
            for (int i = 0; i < arrStr.length; ++i) {
                if (arrStr[i].equals(result)) {
                    UGui.stopCellEditing(tab7);
                    qDecInc.set(arrDbl[i], UGui.getIndexRec(tab7), eGroups.val);
                    ((DefTableModel) tab7.getModel()).fireTableRowsUpdated(tab7.getSelectedRow(), tab7.getSelectedRow());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        centr = new javax.swing.JPanel();
        tabb = new javax.swing.JTabbedPane();
        pan6 = new javax.swing.JPanel();
        scr6 = new javax.swing.JScrollPane();
        tab6 = new javax.swing.JTable();
        pan7 = new javax.swing.JPanel();
        scr7 = new javax.swing.JScrollPane();
        tab7 = new javax.swing.JTable();
        pan2 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan4 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnMoveD = new javax.swing.JButton();
        btnMoveU = new javax.swing.JButton();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Группы");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        centr.setPreferredSize(new java.awt.Dimension(600, 459));
        centr.setLayout(new java.awt.BorderLayout());

        tabb.setFont(frames.UGui.getFont(0,0));
        tabb.setPreferredSize(new java.awt.Dimension(600, 459));
        tabb.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbStateChanged(evt);
            }
        });

        pan6.setLayout(new java.awt.BorderLayout());

        scr6.setBorder(null);

        tab6.setFont(frames.UGui.getFont(0,0));
        tab6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1111", "1", "1",  new Double(1.0), null},
                {"222", "2", "2",  new Double(2.0), null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Название", "Ед. число", "Мн. число", "Курс", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab6.setFillsViewportHeight(true);
        tab6.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr6.setViewportView(tab6);

        pan6.add(scr6, java.awt.BorderLayout.CENTER);

        tabb.addTab("Курсы валют", pan6);

        pan7.setName("pan7"); // NOI18N
        pan7.setLayout(new java.awt.BorderLayout());

        scr7.setBorder(null);

        tab7.setFont(frames.UGui.getFont(0,0));
        tab7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Название", "Параметр", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab7.setFillsViewportHeight(true);
        scr7.setViewportView(tab7);
        if (tab7.getColumnModel().getColumnCount() > 0) {
            tab7.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab7.getColumnModel().getColumn(1).setMaxWidth(80);
            tab7.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab7.getColumnModel().getColumn(2).setMaxWidth(60);
        }

        pan7.add(scr7, java.awt.BorderLayout.CENTER);

        tabb.addTab("Рассчётные параметры", pan7);

        pan2.setName("pan2"); // NOI18N
        pan2.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(null);

        tab2.setFont(frames.UGui.getFont(0,0));
        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", null, null},
                {"222", null, null}
            },
            new String [] {
                "Название группы", "Скидка %", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab2.getColumnModel().getColumn(1).setMaxWidth(80);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(2).setMaxWidth(60);
        }

        pan2.add(scr2, java.awt.BorderLayout.CENTER);

        tabb.addTab("Скидки групп МЦ", pan2);

        pan1.setName("pan1"); // NOI18N
        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setFont(frames.UGui.getFont(0,0));
        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"111", null, null},
                {"222", null, null}
            },
            new String [] {
                "Название группы", "Наценка (коэф)", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(1).setMaxWidth(120);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(2).setMaxWidth(60);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        tabb.addTab("Наценки групп МЦ", pan1);

        pan5.setName("pan5"); // NOI18N
        pan5.setLayout(new java.awt.BorderLayout());

        scr5.setBorder(null);

        tab5.setFont(frames.UGui.getFont(0,0));
        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1111",  new Double(3.0), null},
                {"2222",  new Double(3.0), null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Наименование групп", "Коэффициент", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab5.setFillsViewportHeight(true);
        tab5.setName("tab5"); // NOI18N
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab5.getColumnModel().getColumn(1).setMaxWidth(80);
            tab5.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab5.getColumnModel().getColumn(2).setMaxWidth(60);
        }

        pan5.add(scr5, java.awt.BorderLayout.CENTER);

        tabb.addTab("Коэф. групп текстур", pan5);

        pan3.setName("pan3"); // NOI18N
        pan3.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(null);

        tab3.setFont(frames.UGui.getFont(0,0));
        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1111", null},
                {"2222", null}
            },
            new String [] {
                "Наименование", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        pan3.add(scr3, java.awt.BorderLayout.CENTER);

        tabb.addTab("       Серии МЦ      ", pan3);

        pan4.setName("pan4"); // NOI18N
        pan4.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(null);

        tab4.setFont(frames.UGui.getFont(0,0));
        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11111", null},
                {"22222", null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Наименование", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        tab4.setName("tab4"); // NOI18N
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab4.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        pan4.add(scr4, java.awt.BorderLayout.CENTER);

        tabb.addTab("     Категории МЦ    ", pan4);

        centr.add(tabb, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(10, 29));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/hints/okno", common.eProp.locale); // NOI18N
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

        btnRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c038.gif"))); // NOI18N
        btnRef.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnRef.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRef.setFocusable(false);
        btnRef.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRef.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRef.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRef.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRef.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnRef.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh(evt);
            }
        });

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c034.gif"))); // NOI18N
        btnDel.setToolTipText(bundle.getString("Удалить")); // NOI18N
        btnDel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnDel.setFocusable(false);
        btnDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDel.setMaximumSize(new java.awt.Dimension(25, 25));
        btnDel.setMinimumSize(new java.awt.Dimension(25, 25));
        btnDel.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete(evt);
            }
        });

        btnIns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c033.gif"))); // NOI18N
        btnIns.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnIns.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnIns.setFocusable(false);
        btnIns.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIns.setMaximumSize(new java.awt.Dimension(25, 25));
        btnIns.setMinimumSize(new java.awt.Dimension(25, 25));
        btnIns.setPreferredSize(new java.awt.Dimension(25, 25));
        btnIns.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnIns.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsert(evt);
            }
        });

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport.setFocusable(false);
        btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
            }
        });

        btnMoveD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c052.gif"))); // NOI18N
        btnMoveD.setToolTipText(bundle.getString("Переместить вниз")); // NOI18N
        btnMoveD.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnMoveD.setFocusable(false);
        btnMoveD.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveD.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMoveD.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMoveD.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMoveD.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnMoveD.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoveD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove(evt);
            }
        });

        btnMoveU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c051.gif"))); // NOI18N
        btnMoveU.setToolTipText(bundle.getString("Переместить вверх")); // NOI18N
        btnMoveU.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnMoveU.setFocusable(false);
        btnMoveU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveU.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMoveU.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMoveU.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMoveU.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnMoveU.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoveU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnMoveD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 731, Short.MAX_VALUE)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnMoveU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnMoveD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(10, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6, tab7);
        List.of(tab1, tab2, tab3, tab4, tab5, tab6, tab7).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        List.of(tab1, tab2, tab3, tab4, tab5, tab6, tab7).forEach(tab -> ((DefaultTableModel) tab.getModel()).fireTableDataChanged());
        List.of(tab1, tab2, tab3, tab4, tab5, tab6, tab7).forEach(tab -> UGui.setSelectedRow(tab));
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete
        if (tab1.getBorder() != null) {
            if (UGui.isDeleteRecord(tab1, this) == 0) {
                UGui.deleteRecord(tab1);
            }
        } else if (tab2.getBorder() != null) {
            if (UGui.isDeleteRecord(tab2, this) == 0) {
                UGui.deleteRecord(tab2);
            }
        } else if (tab3.getBorder() != null) {
            if (UGui.isDeleteRecord(tab3, this) == 0) {
                UGui.deleteRecord(tab3);
            }
        } else if (tab4.getBorder() != null) {
            if (UGui.isDeleteRecord(tab4, this) == 0) {
                UGui.deleteRecord(tab4);
            }
        } else if (tab5.getBorder() != null) {
            if (UGui.isDeleteRecord(tab5, this) == 0) {
                UGui.deleteRecord(tab5);
            }
        } else if (tab6.getBorder() != null) {
            if (UGui.isDeleteRecord(tab6, this) == 0) {
                UGui.deleteRecord(tab6);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            UGui.insertRecordEnd(tab1, eGroups.up, (record) -> {
                record.set(eGroups.val, 0);
                record.setDev(eGroups.name, "Наценка.групп.МЦ");
                record.set(eGroups.grup, TypeGroups.PRICE_INC.id);
            });

        } else if (tab2.getBorder() != null) {
            UGui.insertRecordEnd(tab2, eGroups.up, (record) -> {
                record.set(eGroups.val, 0);
                record.setDev(eGroups.name, "Скидка.групп.МЦ");
                record.set(eGroups.grup, TypeGroups.PRICE_DEC.id);
            });

        } else if (tab3.getBorder() != null) {
            UGui.insertRecordEnd(tab3, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.SERI_ELEM.id);
                record.setDev(eGroups.name, "Серия.МЦ");
            });

        } else if (tab4.getBorder() != null) {
            UGui.insertRecordEnd(tab4, eGroups.up, (record) -> {
                record.set(eGroups.grup, TypeGroups.CATEG_ELEM.id);
                record.setDev(eGroups.name, "Категория.МЦ");
            });

        } else if (tab5.getBorder() != null) {
            UGui.insertRecordEnd(tab5, eGroups.up, (record) -> {
                record.set(eGroups.val, 1);
                record.set(eGroups.grup, TypeGroups.COLOR_GRP.id);
                record.setDev(eGroups.name, "Коэф.групп.текстур");
            });

        } else if (tab6.getBorder() != null) {
            UGui.insertRecordEnd(tab6, eCurrenc.up, (record) -> {
                record.setDev(eCurrenc.name, "Курс");
            });

        }
        eGroups.query().clear();
        eGroups.query();
    }//GEN-LAST:event_btnInsert

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        JTable table = null;
        if (tab1.getBorder() != null) {
            table = tab1;
        } else if (tab2.getBorder() != null) {
            table = tab2;
        } else if (tab3.getBorder() != null) {
            table = tab3;
        } else if (tab4.getBorder() != null) {
            table = tab4;
        } else if (tab5.getBorder() != null) {
            table = tab5;
        } else if (tab6.getBorder() != null) {
            table = tab6;
        } else if (tab7.getBorder() != null) {
            table = tab7;
        }
        if (table != null) {
            String title = tabb.getTitleAt(tabb.getSelectedIndex());
            HtmlOfTable.load(title, table);
            ExecuteCmd.documentType(this);
        }
    }//GEN-LAST:event_btnReport

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6, tab7);
        List.of(qArtIncr, qArtDecr, qArtSeri, qCategProf, qColGrp, qColMap, qCategVst, qCategKit, qDecInc).forEach(q -> q.execsql());
    }//GEN-LAST:event_formWindowClosed

    private void btnMove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove
        JTable table = UGui.tableFromBorder(tab1, tab2, tab3, tab4, tab5, tab7);
        int index = UGui.getIndexRec(table);
        int index2 = index;
        if (index != -1 && table != null) {
            JButton btn = (JButton) evt.getSource();
            Query query = ((DefTableModel) table.getModel()).getQuery();

            if (btn == btnMoveD && table.getSelectedRow() < table.getRowCount() - 1) {
                Collections.swap(query, index, ++index2);

            } else if (btn == btnMoveU && table.getSelectedRow() > 0) {
                Collections.swap(query, index, --index2);
            }
            for (int i = 0; i < query.size(); i++) {
                query.set(i + 1, i, eGroups.npp);
            }
            query.execsql();

            ((DefaultTableModel) table.getModel()).fireTableDataChanged();
            UGui.setSelectedIndex(table, index2);
        }
    }//GEN-LAST:event_btnMove

    private void tabbStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbStateChanged
        UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6, tab7);
        List.of(btnIns, btnDel, btnMoveU, btnMoveD).forEach(q -> q.setEnabled(true));
        JTable table = null;
        if (mode == 0) {
            if (tabb.getSelectedIndex() == 0) {
                table = tab6;
                btnMoveU.setEnabled(false);
                btnMoveD.setEnabled(false);
            } else if (tabb.getSelectedIndex() == 1) {
                table = tab7;
                btnIns.setEnabled(false);
                btnDel.setEnabled(false);
            } else if (tabb.getSelectedIndex() == 2) {
                table = tab2;
            } else if (tabb.getSelectedIndex() == 3) {
                table = tab1;
            } else if (tabb.getSelectedIndex() == 4) {
                table = tab5;
            }
        } else {
            if (tabb.getSelectedIndex() == 0) {
                table = tab3;
            } else if (tabb.getSelectedIndex() == 1) {
                table = tab4;
            }
        }
        UGui.updateBorderAndSql(table, List.of(tab1, tab2, tab3, tab4, tab5, tab6, tab7));
    }//GEN-LAST:event_tabbStateChanged

    // <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnMoveD;
    private javax.swing.JButton btnMoveU;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JScrollPane scr7;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTable tab6;
    private javax.swing.JTable tab7;
    private javax.swing.JTabbedPane tabb;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    public void initElements() {

        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);

        TableFieldFilter filterTable = new TableFieldFilter(0, tab1, tab2, tab3, tab4, tab5, tab6, tab7);
        south.add(filterTable, 0);
        filterTable.getTxt().grabFocus();

        List.of(btnIns, btnDel, btnRef).forEach(btn -> btn.addActionListener(l -> UGui.stopCellEditing(tab1, tab2, tab3, tab4, tab5, tab6, tab7)));
    }
}
