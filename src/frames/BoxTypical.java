package frames;

import common.DialogListener;
import common.FrameListener;
import common.FrameToFile;
import dataset.Query;
import dataset.Record;
import domain.eSysprod;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import estimate.Wincalc;
import estimate.model.ElemSimple;
import estimate.model.PaintPanel;

public class BoxTypical extends javax.swing.JFrame implements FrameListener<Object, Object> {

    public Wincalc iwinMax = new Wincalc();
    public Wincalc iwinMin = new Wincalc();
    private Window owner = null;
    private ArrayList<Icon> listIcon1 = new ArrayList<Icon>();
    private ArrayList<Icon> listIcon2 = new ArrayList<Icon>();
    private DialogListener listenet = null;
    private PaintPanel paintPanel1 = new PaintPanel(iwinMax) {

        public void actionResponse(MouseEvent evt) {
            ElemSimple elem5e = iwinMax.listElem.stream().filter(el -> el.mouseClick(evt.getX(), evt.getY())).findFirst().orElse(null);
            if (elem5e != null) {
                txtField5.setText(String.valueOf(elem5e.id()));
                repaint();
            }
        }
    };
    private PaintPanel paintPanel2 = new PaintPanel(iwinMax) {

        public void actionResponse(MouseEvent evt) {
            ElemSimple elem5e = iwinMax.listElem.stream().filter(el -> el.mouseClick(evt.getX(), evt.getY())).findFirst().orElse(null);
            if (elem5e != null) {
                txtField10.setText(String.valueOf(elem5e.id()));
                repaint();
            }
        }
    };
    private Query qSysprod1 = new Query(eSysprod.values());
    private Query qSysprod2 = new Query(eSysprod.values());

    public BoxTypical() {
        initComponents();
        initElements();
        loadingModel();
        btnChoice.setVisible(false);
        btnRemov.setVisible(false);
        loadingData();
    }

    public BoxTypical(java.awt.Window owner, DialogListener listener) {
        initComponents();
        initElements();
        loadingModel();
        this.owner = owner;
        this.listenet = listener;
        owner.setEnabled(false);
        loadingData();
    }

    private void loadingData() {

        qSysprod1.select(eSysprod.up, "where", eSysprod.form, "=3", "order by", eSysprod.npp);
        qSysprod2.select(eSysprod.up, "where", eSysprod.form, "=4", "order by", eSysprod.npp);
        DefaultTableModel dm1 = (DefaultTableModel) tab1.getModel();
        DefaultTableModel dm2 = (DefaultTableModel) tab2.getModel();
        dm1.getDataVector().removeAllElements();
        dm2.getDataVector().removeAllElements();
        int length = 70;
        for (Record record : qSysprod1) {
            try {
                Object obj[] = {record.get(eSysprod.npp), record.get(eSysprod.name), ""};
                Object script = record.get(eSysprod.script);
                iwinMin.build(script.toString());
                BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
                iwinMin.gc2d = bi.createGraphics();
                iwinMin.rootArea.draw(length, length);
                ImageIcon image = new ImageIcon(bi);
                listIcon1.add(image);
                dm1.addRow(obj);
            } catch (Exception e) {
                System.out.println("Ошибка " + e);
            }
        }
        for (Record record : qSysprod2) {
            try {
                Object obj[] = {record.get(eSysprod.npp), record.get(eSysprod.name), ""};
                Object script = record.get(eSysprod.script);
                iwinMin.build(script.toString());
                BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
                iwinMin.gc2d = bi.createGraphics();
                iwinMin.rootArea.draw(length, length);
                ImageIcon image = new ImageIcon(bi);
                listIcon2.add(image);
                dm2.addRow(obj);
            } catch (Exception e) {
                System.out.println("Ошибка " + e);
            }
        }
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab2);
        Util.setSelectedRow(tab1);
    }

    private void loadingModel() {
        panDesign1.add(paintPanel1, java.awt.BorderLayout.CENTER);
        panDesign2.add(paintPanel2, java.awt.BorderLayout.CENTER);
        paintPanel1.setVisible(true);
        paintPanel2.setVisible(true);
        iwinMin.scale2 = 25;
        tab1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Icon icon = listIcon1.get(row);
                    label.setIcon(icon);
                } else {
                    label.setIcon(null);
                }
                return label;
            }
        });
        tab2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    Icon icon = listIcon2.get(row);
                    label.setIcon(icon);
                } else {
                    label.setIcon(null);
                }
                return label;
            }
        });
    }

    private void selectionTab1(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Object script = qSysprod1.get(row, eSysprod.script);
            iwinMax.build(script.toString());
            paintPanel1.repaint(true, 1);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab2);
        if (row != -1) {
            Object script = qSysprod2.get(row, eSysprod.script);
            iwinMax.build(script.toString());
            paintPanel2.repaint(true, 1);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnChoice = new javax.swing.JButton();
        btnRemov = new javax.swing.JButton();
        btnTog1 = new javax.swing.JToggleButton();
        btnTog2 = new javax.swing.JToggleButton();
        btnTog3 = new javax.swing.JToggleButton();
        centr = new javax.swing.JPanel();
        tabbed = new javax.swing.JTabbedPane();
        pan13 = new javax.swing.JPanel();
        pan17 = new javax.swing.JPanel();
        pan3 = new javax.swing.JPanel();
        lab2 = new javax.swing.JLabel();
        txtField2 = new javax.swing.JFormattedTextField();
        lab1 = new javax.swing.JLabel();
        txtField1 = new javax.swing.JFormattedTextField();
        pan4 = new javax.swing.JPanel();
        panDesign1 = new javax.swing.JPanel();
        pan7 = new javax.swing.JPanel();
        lab3 = new javax.swing.JLabel();
        txtField3 = new javax.swing.JFormattedTextField();
        lab4 = new javax.swing.JLabel();
        txtField4 = new javax.swing.JFormattedTextField();
        lab5 = new javax.swing.JLabel();
        txtField5 = new javax.swing.JFormattedTextField();
        pan8 = new javax.swing.JPanel();
        pan9 = new javax.swing.JPanel();
        pan10 = new javax.swing.JPanel();
        pan6 = new javax.swing.JPanel();
        btnSquare = new javax.swing.JButton();
        btnArch3 = new javax.swing.JButton();
        btnArch2 = new javax.swing.JButton();
        btnTrapeze = new javax.swing.JButton();
        btnTrapeze2 = new javax.swing.JButton();
        btnSquare1 = new javax.swing.JButton();
        pan12 = new javax.swing.JPanel();
        btnImpostVert = new javax.swing.JButton();
        btnImpostGoriz = new javax.swing.JButton();
        btnSquare4 = new javax.swing.JButton();
        btnSquare5 = new javax.swing.JButton();
        pan16 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan14 = new javax.swing.JPanel();
        pan18 = new javax.swing.JPanel();
        pan5 = new javax.swing.JPanel();
        lab6 = new javax.swing.JLabel();
        txtField6 = new javax.swing.JFormattedTextField();
        lab7 = new javax.swing.JLabel();
        txtField7 = new javax.swing.JFormattedTextField();
        pan11 = new javax.swing.JPanel();
        panDesign2 = new javax.swing.JPanel();
        pan19 = new javax.swing.JPanel();
        lab8 = new javax.swing.JLabel();
        txtField8 = new javax.swing.JFormattedTextField();
        lab9 = new javax.swing.JLabel();
        txtField9 = new javax.swing.JFormattedTextField();
        lab10 = new javax.swing.JLabel();
        txtField10 = new javax.swing.JFormattedTextField();
        pan20 = new javax.swing.JPanel();
        pan21 = new javax.swing.JPanel();
        pan22 = new javax.swing.JPanel();
        pan23 = new javax.swing.JPanel();
        btnSquare2 = new javax.swing.JButton();
        btnArch4 = new javax.swing.JButton();
        btnArch5 = new javax.swing.JButton();
        btnTrapeze1 = new javax.swing.JButton();
        btnTrapeze3 = new javax.swing.JButton();
        btnSquare3 = new javax.swing.JButton();
        pan24 = new javax.swing.JPanel();
        btnImpostVert1 = new javax.swing.JButton();
        btnImpostGoriz1 = new javax.swing.JButton();
        btnSquare6 = new javax.swing.JButton();
        btnSquare7 = new javax.swing.JButton();
        pan25 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan15 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Типовые конструкции");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(900, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                BoxTypical.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(900, 29));
        north.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panMouseClicked(evt);
            }
        });

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

        btnRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c038.gif"))); // NOI18N
        btnRef.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnRef.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRef.setFocusable(false);
        btnRef.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRef.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRef.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRef.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRef.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c036.gif"))); // NOI18N
        btnSave.setToolTipText(bundle.getString("Сохранить")); // NOI18N
        btnSave.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setMaximumSize(new java.awt.Dimension(25, 25));
        btnSave.setMinimumSize(new java.awt.Dimension(25, 25));
        btnSave.setPreferredSize(new java.awt.Dimension(25, 25));
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave(evt);
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
        btnIns.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsert(evt);
            }
        });

        btnChoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c044.gif"))); // NOI18N
        btnChoice.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnChoice.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnChoice.setFocusable(false);
        btnChoice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChoice.setMaximumSize(new java.awt.Dimension(25, 25));
        btnChoice.setMinimumSize(new java.awt.Dimension(25, 25));
        btnChoice.setPreferredSize(new java.awt.Dimension(25, 25));
        btnChoice.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoiceresh(evt);
            }
        });

        btnRemov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btnRemov.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnRemov.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemov.setFocusable(false);
        btnRemov.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemov.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemov.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemov.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemov.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovresh(evt);
            }
        });

        buttonGroup.add(btnTog1);
        btnTog1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c032.gif"))); // NOI18N
        btnTog1.setSelected(true);
        btnTog1.setMaximumSize(new java.awt.Dimension(25, 25));
        btnTog1.setMinimumSize(new java.awt.Dimension(25, 25));
        btnTog1.setPreferredSize(new java.awt.Dimension(25, 25));
        btnTog1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnTog2);
        btnTog2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c032.gif"))); // NOI18N
        btnTog2.setMaximumSize(new java.awt.Dimension(25, 25));
        btnTog2.setMinimumSize(new java.awt.Dimension(25, 25));
        btnTog2.setPreferredSize(new java.awt.Dimension(25, 25));
        btnTog2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
            }
        });

        buttonGroup.add(btnTog3);
        btnTog3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c032.gif"))); // NOI18N
        btnTog3.setMaximumSize(new java.awt.Dimension(25, 25));
        btnTog3.setMinimumSize(new java.awt.Dimension(25, 25));
        btnTog3.setPreferredSize(new java.awt.Dimension(25, 25));
        btnTog3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggl(evt);
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
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnTog1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnTog2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnTog3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(btnChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 558, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnTog1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTog2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTog3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(900, 560));
        centr.setLayout(new java.awt.BorderLayout());

        tabbed.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tabbed.setPreferredSize(new java.awt.Dimension(900, 560));
        tabbed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedStateChanged(evt);
            }
        });

        pan13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pan13.setPreferredSize(new java.awt.Dimension(900, 560));
        pan13.setLayout(new java.awt.BorderLayout());

        pan17.setPreferredSize(new java.awt.Dimension(600, 540));
        pan17.setLayout(new java.awt.BorderLayout());

        pan3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan3.setMinimumSize(new java.awt.Dimension(100, 20));
        pan3.setPreferredSize(new java.awt.Dimension(500, 40));

        lab2.setText("Высота");
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtField2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField2.setText("1800");

        lab1.setText("Ширина");
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField1.setText("1200");

        javax.swing.GroupLayout pan3Layout = new javax.swing.GroupLayout(pan3);
        pan3.setLayout(pan3Layout);
        pan3Layout.setHorizontalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(397, Short.MAX_VALUE))
        );
        pan3Layout.setVerticalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab2)
                    .addComponent(txtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab1)
                    .addComponent(txtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan17.add(pan3, java.awt.BorderLayout.SOUTH);

        pan4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan4.setPreferredSize(new java.awt.Dimension(400, 430));
        pan4.setLayout(new java.awt.BorderLayout());

        panDesign1.setLayout(new java.awt.BorderLayout());
        pan4.add(panDesign1, java.awt.BorderLayout.CENTER);

        pan7.setPreferredSize(new java.awt.Dimension(700, 40));

        lab3.setText("Тип изделия");
        lab3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab3.setPreferredSize(new java.awt.Dimension(80, 18));

        txtField3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField3.setPreferredSize(new java.awt.Dimension(120, 18));

        lab4.setText("INDEX");
        lab4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab4.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        lab5.setText("INDEX");
        lab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab5.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout pan7Layout = new javax.swing.GroupLayout(pan7);
        pan7.setLayout(pan7Layout);
        pan7Layout.setHorizontalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );
        pan7Layout.setVerticalGroup(
            pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan4.add(pan7, java.awt.BorderLayout.NORTH);

        pan8.setPreferredSize(new java.awt.Dimension(744, 10));

        javax.swing.GroupLayout pan8Layout = new javax.swing.GroupLayout(pan8);
        pan8.setLayout(pan8Layout);
        pan8Layout.setHorizontalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        pan8Layout.setVerticalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        pan4.add(pan8, java.awt.BorderLayout.SOUTH);

        pan9.setPreferredSize(new java.awt.Dimension(10, 376));

        javax.swing.GroupLayout pan9Layout = new javax.swing.GroupLayout(pan9);
        pan9.setLayout(pan9Layout);
        pan9Layout.setHorizontalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan9Layout.setVerticalGroup(
            pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );

        pan4.add(pan9, java.awt.BorderLayout.EAST);

        pan10.setPreferredSize(new java.awt.Dimension(10, 336));

        javax.swing.GroupLayout pan10Layout = new javax.swing.GroupLayout(pan10);
        pan10.setLayout(pan10Layout);
        pan10Layout.setHorizontalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan10Layout.setVerticalGroup(
            pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );

        pan4.add(pan10, java.awt.BorderLayout.WEST);

        pan17.add(pan4, java.awt.BorderLayout.CENTER);

        pan6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan6.setPreferredSize(new java.awt.Dimension(38, 500));
        pan6.setLayout(new javax.swing.BoxLayout(pan6, javax.swing.BoxLayout.Y_AXIS));

        btnSquare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare.setName("areaSquare"); // NOI18N
        btnSquare.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquarebtnArea(evt);
            }
        });
        pan6.add(btnSquare);

        btnArch3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d030.gif"))); // NOI18N
        btnArch3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch3.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch3.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch3.setName("areaArch"); // NOI18N
        btnArch3.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArch3btnArea(evt);
            }
        });
        pan6.add(btnArch3);

        btnArch2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d031.gif"))); // NOI18N
        btnArch2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch2.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch2.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch2.setName("areaArch2"); // NOI18N
        btnArch2.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArch2btnArea(evt);
            }
        });
        pan6.add(btnArch2);

        btnTrapeze.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d028.gif"))); // NOI18N
        btnTrapeze.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze.setName("areaTrapeze"); // NOI18N
        btnTrapeze.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrapezebtnArea(evt);
            }
        });
        pan6.add(btnTrapeze);

        btnTrapeze2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d027.gif"))); // NOI18N
        btnTrapeze2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze2.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.setName("areaTrapeze2"); // NOI18N
        btnTrapeze2.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrapeze2btnArea(evt);
            }
        });
        pan6.add(btnTrapeze2);

        btnSquare1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d024.gif"))); // NOI18N
        btnSquare1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare1.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare1.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare1.setName("areaSquare"); // NOI18N
        btnSquare1.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquare1btnArea(evt);
            }
        });
        pan6.add(btnSquare1);

        pan17.add(pan6, java.awt.BorderLayout.EAST);

        pan12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan12.setPreferredSize(new java.awt.Dimension(38, 500));
        pan12.setLayout(new javax.swing.BoxLayout(pan12, javax.swing.BoxLayout.Y_AXIS));

        btnImpostVert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d023.gif"))); // NOI18N
        btnImpostVert.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostVert.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostVert.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostVert.setName("impostVert"); // NOI18N
        btnImpostVert.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostVert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpostVertbtnElem(evt);
            }
        });
        pan12.add(btnImpostVert);

        btnImpostGoriz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d022.gif"))); // NOI18N
        btnImpostGoriz.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostGoriz.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.setName("impostGoriz"); // NOI18N
        btnImpostGoriz.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpostGorizbtnElem(evt);
            }
        });
        pan12.add(btnImpostGoriz);

        btnSquare4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare4.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare4.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare4.setName("areaSquare"); // NOI18N
        btnSquare4.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquare4ActionPerformed(evt);
            }
        });
        pan12.add(btnSquare4);

        btnSquare5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare5.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare5.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare5.setName("areaSquare"); // NOI18N
        btnSquare5.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquare5ActionPerformed(evt);
            }
        });
        pan12.add(btnSquare5);

        pan17.add(pan12, java.awt.BorderLayout.WEST);

        pan13.add(pan17, java.awt.BorderLayout.CENTER);

        pan16.setPreferredSize(new java.awt.Dimension(300, 560));
        pan16.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "Ном.п/п", "Наименование конструкции", "Рисунок конструкции"
            }
        ));
        tab1.setRowHeight(80);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab1.getColumnModel().getColumn(0).setMaxWidth(20);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab1.getColumnModel().getColumn(2).setMaxWidth(68);
        }

        pan16.add(scr1, java.awt.BorderLayout.CENTER);

        pan13.add(pan16, java.awt.BorderLayout.WEST);

        tabbed.addTab("Простые", pan13);

        pan14.setPreferredSize(new java.awt.Dimension(900, 560));
        pan14.setLayout(new java.awt.BorderLayout());

        pan18.setPreferredSize(new java.awt.Dimension(600, 540));
        pan18.setLayout(new java.awt.BorderLayout());

        pan5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan5.setMinimumSize(new java.awt.Dimension(100, 20));
        pan5.setPreferredSize(new java.awt.Dimension(500, 40));

        lab6.setText("Высота");
        lab6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtField6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField6.setText("1800");

        lab7.setText("Ширина");
        lab7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField7.setText("1200");

        javax.swing.GroupLayout pan5Layout = new javax.swing.GroupLayout(pan5);
        pan5.setLayout(pan5Layout);
        pan5Layout.setHorizontalGroup(
            pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(399, Short.MAX_VALUE))
        );
        pan5Layout.setVerticalGroup(
            pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab6)
                    .addComponent(txtField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab7)
                    .addComponent(txtField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan18.add(pan5, java.awt.BorderLayout.SOUTH);

        pan11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan11.setPreferredSize(new java.awt.Dimension(400, 430));
        pan11.setLayout(new java.awt.BorderLayout());

        panDesign2.setLayout(new java.awt.BorderLayout());
        pan11.add(panDesign2, java.awt.BorderLayout.CENTER);

        pan19.setPreferredSize(new java.awt.Dimension(700, 40));

        lab8.setText("Тип изделия");
        lab8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab8.setPreferredSize(new java.awt.Dimension(80, 18));

        txtField8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtField8.setPreferredSize(new java.awt.Dimension(120, 18));

        lab9.setText("INDEX");
        lab9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab9.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        lab10.setText("INDEX");
        lab10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab10.setPreferredSize(new java.awt.Dimension(40, 18));

        txtField10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtField10.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout pan19Layout = new javax.swing.GroupLayout(pan19);
        pan19.setLayout(pan19Layout);
        pan19Layout.setHorizontalGroup(
            pan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lab10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtField10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(161, Short.MAX_VALUE))
        );
        pan19Layout.setVerticalGroup(
            pan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan11.add(pan19, java.awt.BorderLayout.NORTH);

        pan20.setPreferredSize(new java.awt.Dimension(744, 10));

        javax.swing.GroupLayout pan20Layout = new javax.swing.GroupLayout(pan20);
        pan20.setLayout(pan20Layout);
        pan20Layout.setHorizontalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 563, Short.MAX_VALUE)
        );
        pan20Layout.setVerticalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        pan11.add(pan20, java.awt.BorderLayout.SOUTH);

        pan21.setPreferredSize(new java.awt.Dimension(10, 376));

        javax.swing.GroupLayout pan21Layout = new javax.swing.GroupLayout(pan21);
        pan21.setLayout(pan21Layout);
        pan21Layout.setHorizontalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan21Layout.setVerticalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );

        pan11.add(pan21, java.awt.BorderLayout.EAST);

        pan22.setPreferredSize(new java.awt.Dimension(10, 336));

        javax.swing.GroupLayout pan22Layout = new javax.swing.GroupLayout(pan22);
        pan22.setLayout(pan22Layout);
        pan22Layout.setHorizontalGroup(
            pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pan22Layout.setVerticalGroup(
            pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );

        pan11.add(pan22, java.awt.BorderLayout.WEST);

        pan18.add(pan11, java.awt.BorderLayout.CENTER);

        pan23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan23.setPreferredSize(new java.awt.Dimension(38, 500));
        pan23.setLayout(new javax.swing.BoxLayout(pan23, javax.swing.BoxLayout.Y_AXIS));

        btnSquare2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare2.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare2.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare2.setName("areaSquare"); // NOI18N
        btnSquare2.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquare2btnArea(evt);
            }
        });
        pan23.add(btnSquare2);

        btnArch4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d030.gif"))); // NOI18N
        btnArch4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch4.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch4.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch4.setName("areaArch"); // NOI18N
        btnArch4.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArch4btnArea(evt);
            }
        });
        pan23.add(btnArch4);

        btnArch5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d031.gif"))); // NOI18N
        btnArch5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch5.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch5.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch5.setName("areaArch2"); // NOI18N
        btnArch5.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArch5btnArea(evt);
            }
        });
        pan23.add(btnArch5);

        btnTrapeze1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d028.gif"))); // NOI18N
        btnTrapeze1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze1.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze1.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze1.setName("areaTrapeze"); // NOI18N
        btnTrapeze1.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrapeze1btnArea(evt);
            }
        });
        pan23.add(btnTrapeze1);

        btnTrapeze3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d027.gif"))); // NOI18N
        btnTrapeze3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze3.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze3.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze3.setName("areaTrapeze2"); // NOI18N
        btnTrapeze3.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrapeze3btnArea(evt);
            }
        });
        pan23.add(btnTrapeze3);

        btnSquare3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d024.gif"))); // NOI18N
        btnSquare3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare3.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare3.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare3.setName("areaSquare"); // NOI18N
        btnSquare3.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquare3btnArea(evt);
            }
        });
        pan23.add(btnSquare3);

        pan18.add(pan23, java.awt.BorderLayout.EAST);

        pan24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan24.setPreferredSize(new java.awt.Dimension(38, 500));
        pan24.setLayout(new javax.swing.BoxLayout(pan24, javax.swing.BoxLayout.Y_AXIS));

        btnImpostVert1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d023.gif"))); // NOI18N
        btnImpostVert1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostVert1.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostVert1.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostVert1.setName("impostVert"); // NOI18N
        btnImpostVert1.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostVert1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpostVert1btnElem(evt);
            }
        });
        pan24.add(btnImpostVert1);

        btnImpostGoriz1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d022.gif"))); // NOI18N
        btnImpostGoriz1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostGoriz1.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz1.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz1.setName("impostGoriz"); // NOI18N
        btnImpostGoriz1.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpostGoriz1btnElem(evt);
            }
        });
        pan24.add(btnImpostGoriz1);

        btnSquare6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare6.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare6.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare6.setName("areaSquare"); // NOI18N
        btnSquare6.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquare6ActionPerformed(evt);
            }
        });
        pan24.add(btnSquare6);

        btnSquare7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare7.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare7.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare7.setName("areaSquare"); // NOI18N
        btnSquare7.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSquare7ActionPerformed(evt);
            }
        });
        pan24.add(btnSquare7);

        pan18.add(pan24, java.awt.BorderLayout.WEST);

        pan14.add(pan18, java.awt.BorderLayout.CENTER);

        pan25.setPreferredSize(new java.awt.Dimension(300, 560));
        pan25.setLayout(new java.awt.BorderLayout());

        scr2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "Ном.п/п", "Наименование конструкции", "Рисунок конструкции"
            }
        ));
        tab2.setRowHeight(80);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab2.getColumnModel().getColumn(0).setMaxWidth(20);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab2.getColumnModel().getColumn(2).setMaxWidth(68);
        }

        pan25.add(scr2, java.awt.BorderLayout.CENTER);

        pan14.add(pan25, java.awt.BorderLayout.WEST);

        tabbed.addTab("   Арки   ", pan14);

        pan15.setPreferredSize(new java.awt.Dimension(900, 560));
        pan15.setLayout(new java.awt.BorderLayout());

        scr3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "хххххххххх", "123"},
                {"99", "мммммммммм", "321"}
            },
            new String [] {
                "Ном.п/п", "Наименование конструкции", "Рисунок конструкции"
            }
        ));
        tab3.setRowHeight(80);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(0).setPreferredWidth(20);
            tab3.getColumnModel().getColumn(0).setMaxWidth(20);
            tab3.getColumnModel().getColumn(2).setPreferredWidth(68);
            tab3.getColumnModel().getColumn(2).setMaxWidth(68);
        }

        pan15.add(scr3, java.awt.BorderLayout.CENTER);

        tabbed.addTab("Трапеции", pan15);

        centr.add(tabbed, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 946, Short.MAX_VALUE)
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

    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave

    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnSquarebtnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquarebtnArea

    }//GEN-LAST:event_btnSquarebtnArea

    private void btnArch3btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArch3btnArea

    }//GEN-LAST:event_btnArch3btnArea

    private void btnArch2btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArch2btnArea

    }//GEN-LAST:event_btnArch2btnArea

    private void btnTrapezebtnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrapezebtnArea

    }//GEN-LAST:event_btnTrapezebtnArea

    private void btnTrapeze2btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrapeze2btnArea

    }//GEN-LAST:event_btnTrapeze2btnArea

    private void btnSquare1btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquare1btnArea

    }//GEN-LAST:event_btnSquare1btnArea

    private void btnImpostVertbtnElem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpostVertbtnElem
        iwinMax.listElem.stream().forEach(el -> el.print());
    }//GEN-LAST:event_btnImpostVertbtnElem

    private void btnImpostGorizbtnElem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpostGorizbtnElem

    }//GEN-LAST:event_btnImpostGorizbtnElem

    private void btnSquare4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquare4ActionPerformed

    }//GEN-LAST:event_btnSquare4ActionPerformed

    private void btnSquare5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquare5ActionPerformed

    }//GEN-LAST:event_btnSquare5ActionPerformed

    private void panMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panMouseClicked
        System.out.println(evt.getX() + " " + evt.getY());
    }//GEN-LAST:event_panMouseClicked

    private void btnChoiceresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoiceresh
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record record = new Record();
            record.add(qSysprod1.get(row, eSysprod.id));
            listenet.action(record);
            this.dispose();
        }
    }//GEN-LAST:event_btnChoiceresh

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

    private void btnRemovresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovresh

    }//GEN-LAST:event_btnRemovresh

    private void btnSquare2btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquare2btnArea
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSquare2btnArea

    private void btnArch4btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArch4btnArea
        // TODO add your handling code here:
    }//GEN-LAST:event_btnArch4btnArea

    private void btnArch5btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArch5btnArea
        // TODO add your handling code here:
    }//GEN-LAST:event_btnArch5btnArea

    private void btnTrapeze1btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrapeze1btnArea
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTrapeze1btnArea

    private void btnTrapeze3btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrapeze3btnArea
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTrapeze3btnArea

    private void btnSquare3btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquare3btnArea
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSquare3btnArea

    private void btnImpostVert1btnElem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpostVert1btnElem
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImpostVert1btnElem

    private void btnImpostGoriz1btnElem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpostGoriz1btnElem
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImpostGoriz1btnElem

    private void btnSquare6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquare6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSquare6ActionPerformed

    private void btnSquare7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSquare7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSquare7ActionPerformed

    private void tabbedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedStateChanged
        if (tabbed.getSelectedIndex() == 0) {
            selectionTab1(null);
        } else if (tabbed.getSelectedIndex() == 1) {
            selectionTab2(null);
        } else {
        }     
    }//GEN-LAST:event_tabbedStateChanged

    private void btnToggl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToggl
        if (btnTog1.isSelected()) {
            selectionTab1(null);
        } else if (btnTog2.isSelected()) {
            selectionTab2(null);
        } else {
        }
    }//GEN-LAST:event_btnToggl

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArch2;
    private javax.swing.JButton btnArch3;
    private javax.swing.JButton btnArch4;
    private javax.swing.JButton btnArch5;
    private javax.swing.JButton btnChoice;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnImpostGoriz;
    private javax.swing.JButton btnImpostGoriz1;
    private javax.swing.JButton btnImpostVert;
    private javax.swing.JButton btnImpostVert1;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnRemov;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSquare;
    private javax.swing.JButton btnSquare1;
    private javax.swing.JButton btnSquare2;
    private javax.swing.JButton btnSquare3;
    private javax.swing.JButton btnSquare4;
    private javax.swing.JButton btnSquare5;
    private javax.swing.JButton btnSquare6;
    private javax.swing.JButton btnSquare7;
    private javax.swing.JToggleButton btnTog1;
    private javax.swing.JToggleButton btnTog2;
    private javax.swing.JToggleButton btnTog3;
    private javax.swing.JButton btnTrapeze;
    private javax.swing.JButton btnTrapeze1;
    private javax.swing.JButton btnTrapeze2;
    private javax.swing.JButton btnTrapeze3;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel centr;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab10;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab3;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab5;
    private javax.swing.JLabel lab6;
    private javax.swing.JLabel lab7;
    private javax.swing.JLabel lab8;
    private javax.swing.JLabel lab9;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan12;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan14;
    private javax.swing.JPanel pan15;
    private javax.swing.JPanel pan16;
    private javax.swing.JPanel pan17;
    private javax.swing.JPanel pan18;
    private javax.swing.JPanel pan19;
    private javax.swing.JPanel pan20;
    private javax.swing.JPanel pan21;
    private javax.swing.JPanel pan22;
    private javax.swing.JPanel pan23;
    private javax.swing.JPanel pan24;
    private javax.swing.JPanel pan25;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JPanel panDesign1;
    private javax.swing.JPanel panDesign2;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTabbedPane tabbed;
    private javax.swing.JFormattedTextField txtField1;
    private javax.swing.JFormattedTextField txtField10;
    private javax.swing.JFormattedTextField txtField2;
    private javax.swing.JFormattedTextField txtField3;
    private javax.swing.JFormattedTextField txtField4;
    private javax.swing.JFormattedTextField txtField5;
    private javax.swing.JFormattedTextField txtField6;
    private javax.swing.JFormattedTextField txtField7;
    private javax.swing.JFormattedTextField txtField8;
    private javax.swing.JFormattedTextField txtField9;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
    private void initElements() {

        new FrameToFile(this, btnClose);
        FocusListener listenerFocus = new FocusListener() {

            javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

            public void focusGained(FocusEvent e) {
                if (e.getSource() instanceof JTable) {
                    ((JTable) e.getSource()).setBorder(border);
                }
            }

            public void focusLost(FocusEvent e) {
                if (e.getSource() instanceof JTable) {
                    ((JTable) e.getSource()).setBorder(null);
                }
            }
        };
        //scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0), "Типовые конструкции", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, frames.Util.getFont(0, 0)));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab2(event);
                }
            }
        });
        tab1.addFocusListener(listenerFocus);
        tab2.addFocusListener(listenerFocus);
    }
}
