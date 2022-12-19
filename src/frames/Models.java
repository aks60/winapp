package frames;

import dataset.Query;
import dataset.Record;
import domain.eSysmodel;
import java.awt.Component;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import builder.Wincalc;
import builder.making.Cal5e;
import builder.making.Joining;
import builder.script.GsonRoot;
import builder.script.GsonScript;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import common.eProp;
import frames.swing.draw.Canvas;
import java.awt.CardLayout;
import common.listener.ListenerRecord;
import common.listener.ListenerFrame;
import common.listener.ListenerReload;
import dataset.Conn;
import domain.eSysprod;
import frames.swing.DefTableModel;
import frames.swing.draw.Scene;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class Models extends javax.swing.JFrame implements ListenerFrame<Object, Object>, ListenerReload {

    private Window owner = null;
    private ListenerRecord listenet = null;
    private Canvas canvas = new Canvas();
    private Scene scene = null;
    private Query qSysmodel = new Query(eSysmodel.values());

    public Models() {
        initComponents();
        scene = new Scene(canvas, spinner, this);
        initElements();
        loadingModel();
        btnChoice.setVisible(false);
        btnRemov.setVisible(false);
        loadingTab1(tab1, 1001);
    }

    public Models(java.awt.Window owner, ListenerRecord listener) {
        initComponents();
        scene = new Scene(canvas, spinner, this);
        initElements();
        loadingModel();
        this.owner = owner;
        this.listenet = listener;
        owner.setEnabled(false);
        loadingTab1(tab1, 1001);
    }

    public void loadingModel() {
        new DefTableModel(tab1, qSysmodel, eSysmodel.npp, eSysmodel.name, eSysmodel.id);
        tab1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Object v = qSysmodel.get(row).get(eSysmodel.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
        new DefTableModel(tab2, qSysmodel, eSysmodel.npp, eSysmodel.name, eSysmodel.id);
        tab2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Object v = qSysmodel.get(row).get(eSysmodel.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
        new DefTableModel(tab3, qSysmodel, eSysmodel.npp, eSysmodel.name, eSysmodel.id);
        tab3.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Object v = qSysmodel.get(row).get(eSysmodel.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
        new DefTableModel(tab4, qSysmodel, eSysmodel.npp, eSysmodel.name, eSysmodel.id);
        tab4.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Object v = qSysmodel.get(row).get(eSysmodel.values().length);
                    if (v instanceof Wincalc) {
                        label.setIcon(((Wincalc) v).imageIcon);
                    }
                } else {
                    label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    label.setIcon(null);
                }
                return label;
            }
        });
        canvas.setVisible(true);
    }

    public void loadingTab1(JTable tab, int form) {
        qSysmodel.select(eSysmodel.up, "where", eSysmodel.form, "=", form, "order by npp");
        DefaultTableModel dm = (DefaultTableModel) tab.getModel();
        dm.getDataVector().removeAllElements();
        for (Record record : qSysmodel.table(eSysmodel.up)) {
            try {
                String script = record.getStr(eSysmodel.script);
                Wincalc iwin2 = new Wincalc(script);
                Cal5e joining = new Joining(iwin2, true);//заполним соединения из конструктива
                joining.calc();
                iwin2.imageIcon = Canvas.createIcon(iwin2, 68);
                record.add(iwin2);

            } catch (Exception e) {
                System.err.println("Ошибка:Models.loadingTab() " + e);
            }
        }
        ((DefaultTableModel) tab.getModel()).fireTableDataChanged();
        //UGui.updateBorderAndSql(tab, List.of(tab1, tab2, tab3, tab4));
        UGui.setSelectedRow(tab);
    }

    public void selectionTab1(ListSelectionEvent event, JTable tab) {
        int index = UGui.getIndexRec(tab);
        if (index != -1) {
            Record sysmodelRec = qSysmodel.get(index);
            Object w = sysmodelRec.get(eSysmodel.values().length);
            if (w instanceof Wincalc) { //прорисовка окна               
                Wincalc win = (Wincalc) w;
                scene.init(win);
                canvas.draw();
                scene.draw();
            }
        }
    }

    @Override
    public void reload() {
        try {
            int index = UGui.getIndexRec(tab1);
            if (index != -1) {
                Wincalc win = winc();
                String script = win.rootGson.toJson();
                win.build(script);
                win.imageIcon = Canvas.createIcon(win, 68);
                Record sysmodelRec = qSysmodel.get(index);
                sysmodelRec.set(eSysmodel.script, script);
                sysmodelRec.set(eSysmodel.values().length, win);
                canvas.draw();
                scene.lineHoriz.forEach(e -> e.init());
                scene.lineVert.forEach(e -> e.init());
                scene.draw();
                //selectionWinTree();
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Models.reload() " + e);
        }
    }

    private Wincalc winc() {
        int index = UGui.getIndexRec(tab1);
        if (index != -1) {
            Record sysmodelRec = qSysmodel.table(eSysmodel.up).get(index);
            Object v = sysmodelRec.get(eSysmodel.values().length);
            if (v instanceof Wincalc) {
                return (Wincalc) v;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemov = new javax.swing.JButton();
        btnT1 = new javax.swing.JToggleButton();
        btnT2 = new javax.swing.JToggleButton();
        btnT3 = new javax.swing.JToggleButton();
        btnT4 = new javax.swing.JToggleButton();
        btnMoveU = new javax.swing.JButton();
        btnMoveD = new javax.swing.JButton();
        panSspinner = new javax.swing.JPanel();
        spinner = new javax.swing.JSpinner();
        west = new javax.swing.JPanel();
        pan13 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan14 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan15 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan18 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        centr = new javax.swing.JPanel();
        pan17 = new javax.swing.JPanel();
        panDesign = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        pan8 = new javax.swing.JPanel();
        pan9 = new javax.swing.JPanel();
        pan10 = new javax.swing.JPanel();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Модели конструкций");
        setFont(frames.UGui.getFont(0,1));
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Models.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));
        north.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panMouseClicked(evt);
            }
        });

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

        btnChoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c044.gif"))); // NOI18N
        btnChoice.setToolTipText(bundle.getString("Выбрать")); // NOI18N
        btnChoice.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnChoice.setFocusable(false);
        btnChoice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChoice.setMaximumSize(new java.awt.Dimension(25, 25));
        btnChoice.setMinimumSize(new java.awt.Dimension(25, 25));
        btnChoice.setPreferredSize(new java.awt.Dimension(25, 25));
        btnChoice.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnChoice.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoice(evt);
            }
        });

        btnRemov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btnRemov.setToolTipText(bundle.getString("Очистить")); // NOI18N
        btnRemov.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemov.setFocusable(false);
        btnRemov.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemov.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemov.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemov.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemov.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnRemov.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemov(evt);
            }
        });

        buttonGroup.add(btnT1);
        btnT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c003.gif"))); // NOI18N
        btnT1.setSelected(true);
        btnT1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnT1.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT1.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT1.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnT2);
        btnT2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c004.gif"))); // NOI18N
        btnT2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnT2.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT2.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT2.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnT3);
        btnT3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c005.gif"))); // NOI18N
        btnT3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnT3.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT3.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT3.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnT4);
        btnT4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c079.gif"))); // NOI18N
        btnT4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnT4.setMaximumSize(new java.awt.Dimension(25, 25));
        btnT4.setMinimumSize(new java.awt.Dimension(25, 25));
        btnT4.setPreferredSize(new java.awt.Dimension(25, 25));
        btnT4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
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

        panSspinner.setMinimumSize(new java.awt.Dimension(50, 20));
        panSspinner.setPreferredSize(new java.awt.Dimension(100, 24));
        panSspinner.setLayout(new java.awt.BorderLayout());

        spinner.setFont(frames.UGui.getFont(1,0));
        spinner.setModel(new javax.swing.SpinnerNumberModel(0.0f, null, null, 1.0f));
        spinner.setBorder(null);
        spinner.setPreferredSize(new java.awt.Dimension(50, 24));
        panSspinner.add(spinner, java.awt.BorderLayout.CENTER);

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
                .addGap(18, 18, 18)
                .addComponent(btnMoveU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnMoveD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addComponent(btnT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112)
                .addComponent(panSspinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMoveU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMoveD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panSspinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        west.setPreferredSize(new java.awt.Dimension(360, 560));
        west.setLayout(new java.awt.CardLayout());

        pan13.setName(""); // NOI18N
        pan13.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
        tab1.setFont(frames.UGui.getFont(0,0));
        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Integer(1), "xxxxxxxxxxxxxxx", "XXX"},
                { new Integer(2), "zzzzzzzzzzzzzzzzz", "XXX"}
            },
            new String [] {
                "№", "Наименование", "Рисунок"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setRowHeight(68);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(260);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab1.getColumnModel().getColumn(2).setMaxWidth(68);
        }

        pan13.add(scr1, java.awt.BorderLayout.CENTER);

        west.add(pan13, "pan13");

        pan14.setName(""); // NOI18N
        pan14.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "№", "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setRowHeight(68);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(260);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab2.getColumnModel().getColumn(2).setMaxWidth(68);
        }

        pan14.add(scr2, java.awt.BorderLayout.CENTER);

        west.add(pan14, "pan14");

        pan15.setName(""); // NOI18N
        pan15.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "№", "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setRowHeight(68);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab3.getColumnModel().getColumn(1).setPreferredWidth(260);
            tab3.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab3.getColumnModel().getColumn(2).setMaxWidth(68);
        }

        pan15.add(scr3, java.awt.BorderLayout.CENTER);

        west.add(pan15, "pan15");

        pan18.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "№", "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        tab4.setRowHeight(68);
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab4.getColumnModel().getColumn(1).setPreferredWidth(260);
            tab4.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab4.getColumnModel().getColumn(2).setMaxWidth(68);
        }

        pan18.add(scr4, java.awt.BorderLayout.CENTER);

        west.add(pan18, "pan18");

        getContentPane().add(west, java.awt.BorderLayout.WEST);

        centr.setPreferredSize(new java.awt.Dimension(600, 560));
        centr.setLayout(new java.awt.BorderLayout());

        pan17.setPreferredSize(new java.awt.Dimension(600, 500));
        pan17.setLayout(new java.awt.BorderLayout());

        panDesign.setLayout(new java.awt.BorderLayout());
        pan17.add(panDesign, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(700, 40));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        pan17.add(pan7, java.awt.BorderLayout.NORTH);

        pan8.setPreferredSize(new java.awt.Dimension(744, 10));

        javax.swing.GroupLayout pan8Layout = new javax.swing.GroupLayout(pan8);
        pan8.setLayout(pan8Layout);
        pan8Layout.setHorizontalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );
        pan8Layout.setVerticalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        pan17.add(pan8, java.awt.BorderLayout.SOUTH);

        pan9.setPreferredSize(new java.awt.Dimension(10, 376));

        javax.swing.GroupLayout pan9Layout = new javax.swing.GroupLayout(pan9);
        pan9.setLayout(pan9Layout);
        pan9Layout.setHorizontalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan9Layout.setVerticalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
        );

        pan17.add(pan9, java.awt.BorderLayout.EAST);

        pan10.setPreferredSize(new java.awt.Dimension(10, 336));

        javax.swing.GroupLayout pan10Layout = new javax.swing.GroupLayout(pan10);
        pan10.setLayout(pan10Layout);
        pan10Layout.setHorizontalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan10Layout.setVerticalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
        );

        pan17.add(pan10, java.awt.BorderLayout.WEST);

        centr.add(pan17, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 782, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        loadingTab1(tab1, 1001);
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
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert
        try {
            String json = null;
            Record record = eSysmodel.up.newRecord(Query.INS);
            record.set(eSysmodel.id, Conn.genId(eSysmodel.up));
            record.set(eSysmodel.npp, record.get(eSysmodel.id));

            //Для загрузки скрипта из программы 
            if (eProp.dev == true) {
                Object prj = JOptionPane.showInputDialog(Models.this, "Номер проекта", "Проект", JOptionPane.QUESTION_MESSAGE);
                if (prj != null) {
                    GsonRoot gsonRoot = new Gson().fromJson(json, GsonRoot.class);
                    json = GsonScript.modelJson(Integer.valueOf(prj.toString()));
                    record.set(eSysmodel.name, "<html> Kod:" + prj + "* " + gsonRoot.name());
                    record.set(eSysmodel.script, json);
                }
            } else {
                //Для загрузки скрипта с диска              
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Json (формат json)", "json");
                chooser.setFileFilter(filter);
                if (chooser.showDialog(this, "Выбрать") != JFileChooser.CANCEL_OPTION) {
                    String path = chooser.getSelectedFile().getPath();
                    JsonReader reader = new JsonReader(new FileReader(path));
                    GsonRoot gsonRoot = new Gson().fromJson(reader, GsonRoot.class);
                    json = gsonRoot.toJson();
                    reader.close();
                    record.set(eSysmodel.name, "<html>" + gsonRoot.name());
                    record.set(eSysmodel.script, json);
                }
            }
            if (json != null) {
                if (tab1.getBorder() != null) {
                    record.set(eSysmodel.form, 1001);
                    qSysmodel.insert(record);
                    loadingTab1(tab1, 1001);
                    UGui.setSelectedIndex(tab1, qSysmodel.size() - 1);
                    UGui.scrollRectToIndex(qSysmodel.size() - 1, tab1);

                } else if (tab2.getBorder() != null) {
                    record.set(eSysmodel.form, 1004);
                    qSysmodel.insert(record);
                    loadingTab1(tab2, 1004);
                    UGui.setSelectedIndex(tab2, qSysmodel.size() - 1);
                    UGui.scrollRectToIndex(qSysmodel.size() - 1, tab2);

                } else if (tab3.getBorder() != null) {
                    record.set(eSysmodel.form, 1002);
                    qSysmodel.insert(record);
                    loadingTab1(tab3, 1002);
                    UGui.setSelectedIndex(tab3, qSysmodel.size() - 1);
                    UGui.scrollRectToIndex(qSysmodel.size() - 1, tab3);

                } else if (tab4.getBorder() != null) {
                    record.set(eSysmodel.form, 1007);
                    qSysmodel.insert(record);
                    loadingTab1(tab4, 1007);
                    UGui.setSelectedIndex(tab4, qSysmodel.size() - 1);
                    UGui.scrollRectToIndex(qSysmodel.size() - 1, tab4);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Models.btnInsert()");
        }
    }//GEN-LAST:event_btnInsert

    private void panMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panMouseClicked
        System.out.println(evt.getX() + " " + evt.getY());
    }//GEN-LAST:event_panMouseClicked

    private void btnChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoice
        JTable table = null;
        if (btnT1.isSelected()) {
            table = tab1;
        } else if (btnT2.isSelected()) {
            table = tab2;
        } else if (btnT3.isSelected()) {
            table = tab3;
        } else if (btnT4.isSelected()) {
            table = tab4;
        }
        int index = UGui.getIndexRec(table);
        if (index != -1) {
            Record record = new Record();
            record.add(qSysmodel.get(index, eSysmodel.id));
            record.add(qSysmodel.get(index, eSysmodel.name));
            record.add(qSysmodel.get(index, eSysmodel.script));
            listenet.action(record);
        }
        this.dispose();
    }//GEN-LAST:event_btnChoice

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

    private void btnRemov(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemov

    }//GEN-LAST:event_btnRemov

    private void btnToggl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToggl
        if (btnT1.isSelected()) {
            loadingTab1(tab1, 1001);
            ((CardLayout) west.getLayout()).show(west, "pan13");
            UGui.updateBorderAndSql(tab1, List.of(tab1, tab2, tab3, tab4));
            UGui.setSelectedRow(tab1);
        } else if (btnT2.isSelected()) {
            loadingTab1(tab2, 1004);
            ((CardLayout) west.getLayout()).show(west, "pan14");
            UGui.updateBorderAndSql(tab2, List.of(tab1, tab2, tab3, tab4));
            UGui.setSelectedRow(tab2);
        } else if (btnT3.isSelected()) {
            loadingTab1(tab3, 1002);
            ((CardLayout) west.getLayout()).show(west, "pan15");
            UGui.updateBorderAndSql(tab3, List.of(tab1, tab2, tab3, tab4));
            UGui.setSelectedRow(tab3);
        } else {
            loadingTab1(tab4, 1007);
            ((CardLayout) west.getLayout()).show(west, "pan18");
            UGui.updateBorderAndSql(tab4, List.of(tab1, tab2, tab3, tab4));
            UGui.setSelectedRow(tab4);
        }
    }//GEN-LAST:event_btnToggl

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        UGui.updateBorderAndSql(table, List.of(tab1, tab2, tab3, tab4));
    }//GEN-LAST:event_tabMousePressed

    private void tabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMouseClicked
        if (evt.getClickCount() == 2) {
            btnChoice(null);
        }
    }//GEN-LAST:event_tabMouseClicked

    private void btnMove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove
        JTable table = UGui.tableFromBorder(tab1, tab2, tab3, tab4);
        int index = UGui.getIndexRec(table), index2 = index;
        JButton btn = (JButton) evt.getSource();
        if (index != -1) {
            if (table.getBorder() != null) {
                if (btn == btnMoveD && table.getSelectedRow() < table.getRowCount() - 1) {
                    Collections.swap(qSysmodel, index, ++index2);

                } else if (btn == btnMoveU && table.getSelectedRow() > 0) {
                    Collections.swap(qSysmodel, index, --index2);
                }
                IntStream.range(0, qSysmodel.size() - 1).forEach(i -> qSysmodel.set(i + 1, i, eSysmodel.npp));
                ((DefaultTableModel) table.getModel()).fireTableDataChanged();
                UGui.setSelectedIndex(table, index2);
                qSysmodel.execsql();
            }
        }
    }//GEN-LAST:event_btnMove

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnMoveD;
    private javax.swing.JButton btnMoveU;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnRemov;
    private javax.swing.JToggleButton btnT1;
    private javax.swing.JToggleButton btnT2;
    private javax.swing.JToggleButton btnT3;
    private javax.swing.JToggleButton btnT4;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan14;
    private javax.swing.JPanel pan15;
    private javax.swing.JPanel pan17;
    private javax.swing.JPanel pan18;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JPanel panDesign;
    private javax.swing.JPanel panSspinner;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JPanel south;
    private javax.swing.JSpinner spinner;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    private void initElements() {

        new FrameToFile(this, btnClose);
        panDesign.add(scene, java.awt.BorderLayout.CENTER);
        btnRemov.setVisible(false);
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event, tab1);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event, tab2);
                }
            }
        });
        tab3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event, tab3);
                }
            }
        });
        tab4.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event, tab4);
                }
            }
        });
    }
}
