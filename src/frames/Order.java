package frames;

import builder.Wincalc;
import builder.model.AreaStvorka;
import builder.script.GsonElem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.FrameToFile;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eProject;
import domain.ePrjpart;
import frames.dialog.DicDate;
import frames.dialog.DicName;
import frames.swing.DefCellRenderer;
import javax.swing.JTable;
import frames.swing.DefTableModel;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefFieldEditor;
import common.ListenerObject;
import common.ListenerRecord;
import common.eProfile;
import common.eProperty;
import dataset.Confb;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eCurrenc;
import domain.eFurndet;
import domain.eFurniture;
import domain.ePrjprod;
import domain.eSysfurn;
import domain.eSysprod;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.LayoutHandle;
import enums.PKjson;
import enums.TypeElem;
import enums.TypeOpen1;
import enums.UseArtiklTo;
import enums.UseSide;
import frames.dialog.DicArtikl;
import frames.dialog.DicColor;
import frames.dialog.DicEnums;
import frames.dialog.DicHandl;
import frames.dialog.DicSyspod;
import frames.dialog.DicSysprof;
import frames.swing.Canvas;
import frames.swing.DefMutableTreeNode;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import startup.App;

public class Order extends javax.swing.JFrame {

    private Query qCurrenc = new Query(eCurrenc.values());
    private Query qPrjpart = new Query(ePrjpart.values());
    private Query qProject = new Query(eProject.values());
    private Query qPrjprod = new Query(ePrjprod.values());
    private Wincalc iwin = new Wincalc();
    private DefMutableTreeNode windowsNode = null;
    private Canvas paintPanel = new Canvas(iwin);
    private ListenerObject listenerDate;
    private DefFieldEditor rsvPrj;
    private Gson gson = new GsonBuilder().create();

    public Order() {
        initComponents();
        initElements();
        loadingData();
        loadingModel();
        listenerAdd();
    }

    private void loadingData() {
        qCurrenc.select(eCurrenc.up, "order by", eCurrenc.name);
        qPrjpart.select(ePrjpart.up);
        qProject.select(eProject.up, "order by", eProject.num_ord);
        qPrjprod.select(ePrjprod.up);
    }

    private void loadingModel() {
        new DefTableModel(tab1, qProject, eProject.num_ord, eProject.date4, eProject.date6, eProject.prjpart_id, eProject.manager, eProject.categ) {
            @Override
            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (field == eProject.prjpart_id) {
                    Record record = qPrjpart.stream().filter(rec -> rec.get(ePrjpart.id).equals(val)).findFirst().orElse(ePrjpart.up.newRecord());
                    return record.get(ePrjpart.partner);
                }
                return val;
            }
        };
        new DefTableModel(tab2, qPrjprod, ePrjprod.name, ePrjprod.id);
        new DefTableModel(tab4, qPrjprod, ePrjprod.name, ePrjprod.id);

        tab1.getColumnModel().getColumn(1).setCellRenderer(new DefCellRenderer());
        tab1.getColumnModel().getColumn(2).setCellRenderer(new DefCellRenderer());
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1) {
                    int index = table.convertRowIndexToModel(row);
                    Object v = qPrjprod.get(index).get(ePrjprod.values().length);
                    if (v instanceof Icon) {
                        Icon icon = (Icon) v;
                        label.setIcon(icon);
                    }
                } else {
                    label.setIcon(null);
                }
                return label;
            }
        };
        tab2.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        tab4.setDefaultRenderer(Object.class, defaultTableCellRenderer);

        rsvPrj = new DefFieldEditor(tab1) {

            public Set<JTextField> set = new HashSet();

            public void setText(JTextField jtf, String str) {
                set.add(jtf);
                jtf.setText(str);
            }

            @Override
            public void load(Integer index) {
                super.load(index);
                Record projectRec = qProject.get(Util.getIndexRec(tab1));
                Record currencRec = qCurrenc.stream().filter(rec -> rec.get(eCurrenc.id).equals(projectRec.get(eProject.currenc_id))).findFirst().orElse(eCurrenc.up.newRecord());
                setText(txt3, currencRec.getStr(eCurrenc.name));
            }

            @Override
            public void clear() {
                super.clear();
                set.forEach(s -> s.setText(null));
            }
        };
        rsvPrj.add(eProject.num_acc, txt2);
        rsvPrj.add(eProject.pric1, txt4);
        rsvPrj.add(eProject.pric5, txt5);
        rsvPrj.add(eProject.cost4, txt6);
        rsvPrj.add(eProject.weight, txt7);
        rsvPrj.add(eProject.square, txt8);

        panDesign.add(paintPanel, java.awt.BorderLayout.CENTER);
        paintPanel.setVisible(true);
        int index = -1;
        int orderID = Integer.valueOf(eProperty.orderID.read());
        for (int index2 = 0; index2 < qProject.size(); ++index2) {
            if (qProject.get(index2).getInt(eSysprod.id) == orderID) {
                index = index2;
            }
        }
        if (index != -1) {
            Util.setSelectedRow(tab1, index);
        } else {
            Util.setSelectedRow(tab1);
        }
        Util.scrollRectToRow(index, tab1);
    }

    private void listenerAdd() {

        Util.buttonCellEditor(tab1, 1).addActionListener(event -> {
            new DicDate(this, (obj) -> {
                GregorianCalendar calendar = (GregorianCalendar) obj;
                Util.stopCellEditing(tab1);
                Record record2 = qProject.get(Util.getIndexRec(tab1));
                record2.set(eProject.date4, calendar.getTime());
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
                return true;
            }, 0);
        });

        Util.buttonCellEditor(tab1, 2).addActionListener(event -> {
            new DicDate(this, (obj) -> {
                GregorianCalendar calendar = (GregorianCalendar) obj;
                Util.stopCellEditing(tab1);
                Record record2 = qProject.get(Util.getIndexRec(tab1));
                record2.set(eProject.date6, calendar.getTime());
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
                return true;
            }, 0);
        });

        Util.buttonCellEditor(tab1, 3).addActionListener(event -> {
            new Partner(this, (record) -> {
                Util.stopCellEditing(tab1);
                Record record2 = qProject.get(Util.getIndexRec(tab1));
                record2.set(eProject.prjpart_id, record.getInt(ePrjpart.id));
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
            });
        });

        Util.buttonCellEditor(tab1, 4).addActionListener(event -> {
            Set set = new HashSet();
            Query q = new Query(eProject.categ);
            qProject.forEach(rec -> set.add(rec.get(eProject.categ)));

            new DicName(this, (record) -> {
                Util.stopCellEditing(tab1);
                Record record2 = qProject.get(Util.getIndexRec(tab1));
                record2.set(eProject.categ, record.getStr(0));
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
            }, set);
        });

        Util.buttonCellEditor(tab1, 5).addActionListener(event -> {
            Set set = new HashSet();
            qProject.forEach(rec -> set.add(rec.get(eProject.categ)));
            new DicName(this, (record) -> {
                Util.stopCellEditing(tab1);
                Record record2 = qProject.get(Util.getIndexRec(tab1));
                record2.set(eProject.categ, record.getStr(0));
                ((DefaultTableModel) tab1.getModel()).fireTableRowsUpdated(tab1.getSelectedRow(), tab1.getSelectedRow());
            }, set);
        });
    }

    public void listenerSet() {

        listenerDate = (obj) -> {
            return true;
        };
    }

    private void loadingTab2() {

        Util.stopCellEditing(tab1, tab2);
        Arrays.asList(qProject, qPrjprod).forEach(q -> q.execsql());
        Record projectRec = qProject.get(Util.getIndexRec(tab1));
        int id = projectRec.getInt(eProject.id);
        qPrjprod.select(ePrjprod.up, "where", ePrjprod.order_id, "=", id);

        int length = 68;
        for (Record record : qPrjprod) {
            try {
                Object script = record.get(ePrjprod.script);
                ImageIcon image = Util.createWindraw(iwin, script, length);
                record.add(image);

            } catch (Exception e) {
                System.err.println("Ошибка:Order.loadingTab2() " + e);
            }
        }
        ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
    }

    private void loadingWin() {
        try {
            int row[] = windowsTree.getSelectionRows();
            DefMutableTreeNode root = iwin.rootArea.treeWin(iwin);
            windowsTree.setModel(new DefaultTreeModel(root));
            windowsTree.setSelectionRows(row);

        } catch (Exception e) {
            System.err.println("Ошибка: Systree.loadingWin() " + e);
        }
    }

    private void selectionTab1(ListSelectionEvent event) {
        Util.stopCellEditing(tab1);
        int index = Util.getIndexRec(tab1);
        int orderID = qProject.getAs(index, eProject.id);
        eProperty.orderID.write(String.valueOf(orderID));
        rsvPrj.load();
        loadingTab2();
        index = -1;
        int prjprodID = Integer.valueOf(eProperty.prjprodID.read());
        for (int i = 0; i < qPrjprod.size(); ++i) {
            if (qPrjprod.get(i).getInt(ePrjprod.id) == prjprodID) {
                index = i;
            }
        }
        if (index != -1) {
            Util.setSelectedRow(tab2, index);
            Util.setSelectedRow(tab4, index);
        } else {
            Util.setSelectedRow(tab2);
            Util.setSelectedRow(tab4);
        }
    }

    private void selectionTab24() {
        int index = Util.getIndexRec(tab2);
        if (index != -1) {
            Record prjprodRec = qPrjprod.get(index);
            String script = prjprodRec.getStr(ePrjprod.script);
            eProperty.prjprodID.write(prjprodRec.getStr(ePrjprod.id)); //запишем текущий prjprodID в файл
            App.Top.frame.setTitle(eProfile.profile.title + Util.designTitle());

            //Калькуляция и прорисовка окна
            if (script != null && script.isEmpty() == false) {
                JsonElement script2 = gson.fromJson(script, JsonElement.class);
                iwin.build(script2.toString()); //построение изделия
                paintPanel.repaint(true);
                loadingWin();
                windowsTree.setSelectionRow(0);

            } else {
                Graphics2D g = (Graphics2D) paintPanel.getGraphics();
                g.clearRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
            }
        }
    }

    private void selectionWin() {
        windowsNode = (DefMutableTreeNode) windowsTree.getLastSelectedPathComponent();
        if (windowsNode != null) {

            //Основные
            if (windowsNode.com5t().type() == TypeElem.RECTANGL || windowsNode.com5t().type() == TypeElem.ARCH) {
                ((CardLayout) pan8.getLayout()).show(pan8, "card12");
                ((TitledBorder) pan12.getBorder()).setTitle(iwin.rootArea.type().name);
                pan12.repaint();
                txt9.setText(eColor.find(iwin.colorID1).getStr(eColor.name));
                txt13.setText(eColor.find(iwin.colorID2).getStr(eColor.name));
                txt14.setText(eColor.find(iwin.colorID3).getStr(eColor.name));
                txt17.setText(String.valueOf(iwin.rootGson.width()));
                txt22.setText(String.valueOf(iwin.rootGson.height()));
                txt23.setText(String.valueOf(iwin.rootGson.heightAdd()));
                txt23.setEditable(windowsNode.com5t().type() == TypeElem.ARCH);

                //Рама, импост...
            } else if (windowsNode.com5t().type() == TypeElem.FRAME_SIDE
                    || windowsNode.com5t().type() == TypeElem.STVORKA_SIDE || windowsNode.com5t().type() == TypeElem.IMPOST) {
                ((CardLayout) pan8.getLayout()).show(pan8, "card13");
                ((TitledBorder) pan13.getBorder()).setTitle(windowsNode.toString());
                txt32.setText(windowsNode.com5t().artiklRec.getStr(eArtikl.code));
                txt33.setText(windowsNode.com5t().artiklRec.getStr(eArtikl.name));
                txt27.setText(eColor.find(windowsNode.com5t().colorID1).getStr(eColor.name));
                txt28.setText(eColor.find(windowsNode.com5t().colorID2).getStr(eColor.name));
                txt29.setText(eColor.find(windowsNode.com5t().colorID3).getStr(eColor.name));

                //Стеклопакет
            } else if (windowsNode.com5t().type() == TypeElem.GLASS) {
                ((CardLayout) pan8.getLayout()).show(pan8, "card15");
                Record artiklRec = eArtikl.find(windowsNode.com5t().artiklRec.getInt(eArtikl.id), false);
                txt19.setText(artiklRec.getStr(eArtikl.code));
                txt18.setText(artiklRec.getStr(eArtikl.name));

                //Створка
            } else if (windowsNode.com5t().type() == TypeElem.STVORKA) {
                ((CardLayout) pan8.getLayout()).show(pan8, "card16");
                AreaStvorka stv = (AreaStvorka) windowsNode.com5t();
                int id = stv.sysfurnRec.getInt(eSysfurn.furniture_id);
                txt20.setText(eFurniture.find(id).getStr(eFurniture.name));
                txt30.setText(stv.typeOpen.name2);
                txt16.setText(stv.handlLayout.name);
                if (stv.handlLayout == LayoutHandle.SET) {
                    txt31.setEditable(true);
                    txt31.setText(String.valueOf(stv.handlHeight));
                } else {
                    txt31.setEditable(false);
                    txt31.setText("");
                }
                iwin.calcFurniture = new builder.specif.Furniture(iwin, true); //фурнитура 
                iwin.calcFurniture.calc();
                txt21.setText(stv.handlRec.getStr(eArtikl.name));
                txt24.setText(Util.df.format(iwin.rootGson.find(stv.id()).width()));
                txt26.setText(Util.df.format(iwin.rootGson.find(stv.id()).height()));
                txt25.setText(eColor.find(stv.handlColor).getStr(eColor.name));
            }
            Arrays.asList(txt9, txt13, txt14, txt27, txt28,
                    txt29, txt19, txt20, txt30).forEach(it -> it.setCaretPosition(0));
            Arrays.asList(pan12, pan13, pan15, pan16).forEach(it -> it.repaint());
        }
    }

    private void updateScript(float selectID) {
        String script = gson.toJson(iwin.rootGson);
        Record prjprodRec = qPrjprod.get(Util.getIndexRec(tab2));
        prjprodRec.set(ePrjprod.script, script);
        qPrjprod.update(prjprodRec);
        selectionTab24();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) windowsTree.getModel().getRoot();
        do {
            if (selectID == ((DefMutableTreeNode) node).com5t().id()) {
                TreePath path = new TreePath(node.getPath());
                windowsTree.setSelectionPath(path);
                windowsTree.scrollPathToVisible(path);
            }
            node = node.getNextNode();
        } while (node != null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnCalc = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        tabb1 = new javax.swing.JTabbedPane();
        pan1 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan11 = new javax.swing.JPanel();
        pan2 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        lab2 = new javax.swing.JLabel();
        lab3 = new javax.swing.JLabel();
        lab4 = new javax.swing.JLabel();
        lab5 = new javax.swing.JLabel();
        txt2 = new javax.swing.JTextField();
        txt3 = new javax.swing.JTextField();
        txt4 = new javax.swing.JTextField();
        txt5 = new javax.swing.JTextField();
        btn1 = new javax.swing.JButton();
        lab6 = new javax.swing.JLabel();
        txt6 = new javax.swing.JTextField();
        lab7 = new javax.swing.JLabel();
        txt7 = new javax.swing.JTextField();
        lab8 = new javax.swing.JLabel();
        txt8 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        pan7 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        pan8 = new javax.swing.JPanel();
        pan14 = new javax.swing.JPanel();
        pan12 = new javax.swing.JPanel();
        pan21 = new javax.swing.JPanel();
        lab27 = new javax.swing.JLabel();
        lab31 = new javax.swing.JLabel();
        lab32 = new javax.swing.JLabel();
        btn9 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        txt9 = new javax.swing.JTextField();
        txt13 = new javax.swing.JTextField();
        txt14 = new javax.swing.JTextField();
        lab35 = new javax.swing.JLabel();
        txt17 = new javax.swing.JTextField();
        btn17 = new javax.swing.JButton();
        lab38 = new javax.swing.JLabel();
        txt22 = new javax.swing.JTextField();
        btn23 = new javax.swing.JButton();
        lab40 = new javax.swing.JLabel();
        btn25 = new javax.swing.JButton();
        txt23 = new javax.swing.JTextField();
        pan13 = new javax.swing.JPanel();
        pan20 = new javax.swing.JPanel();
        lab28 = new javax.swing.JLabel();
        lab43 = new javax.swing.JLabel();
        lab44 = new javax.swing.JLabel();
        btn18 = new javax.swing.JButton();
        btn19 = new javax.swing.JButton();
        btn20 = new javax.swing.JButton();
        txt27 = new javax.swing.JTextField();
        txt28 = new javax.swing.JTextField();
        txt29 = new javax.swing.JTextField();
        lab33 = new javax.swing.JLabel();
        lab34 = new javax.swing.JLabel();
        txt32 = new javax.swing.JTextField();
        txt33 = new javax.swing.JTextField();
        btn22 = new javax.swing.JButton();
        pan15 = new javax.swing.JPanel();
        lab29 = new javax.swing.JLabel();
        lab36 = new javax.swing.JLabel();
        btn3 = new javax.swing.JButton();
        txt19 = new javax.swing.JTextField();
        txt18 = new javax.swing.JTextField();
        pan16 = new javax.swing.JPanel();
        lab30 = new javax.swing.JLabel();
        lab37 = new javax.swing.JLabel();
        lab39 = new javax.swing.JLabel();
        lab46 = new javax.swing.JLabel();
        lab45 = new javax.swing.JLabel();
        btn10 = new javax.swing.JButton();
        btn12 = new javax.swing.JButton();
        btn14 = new javax.swing.JButton();
        btn21 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        txt20 = new javax.swing.JTextField();
        txt30 = new javax.swing.JTextField();
        txt25 = new javax.swing.JTextField();
        txt21 = new javax.swing.JTextField();
        txt31 = new javax.swing.JTextField();
        txt16 = new javax.swing.JTextField();
        lab41 = new javax.swing.JLabel();
        txt24 = new javax.swing.JTextField();
        btn15 = new javax.swing.JButton();
        lab42 = new javax.swing.JLabel();
        txt26 = new javax.swing.JTextField();
        btn16 = new javax.swing.JButton();
        pan9 = new javax.swing.JPanel();
        panDesign = new javax.swing.JPanel();
        scr6 = new javax.swing.JScrollPane();
        windowsTree = new javax.swing.JTree();
        pan6 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Заказы");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Order.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));

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

        btnCalc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCalc.setText("Пересчитать заказ");
        btnCalc.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnCalc.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnCalc.setFocusable(false);
        btnCalc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCalc.setMaximumSize(new java.awt.Dimension(25, 25));
        btnCalc.setMinimumSize(new java.awt.Dimension(25, 25));
        btnCalc.setPreferredSize(new java.awt.Dimension(120, 25));
        btnCalc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcresh(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCalc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 591, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCalc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(800, 450));
        centr.setLayout(new java.awt.GridLayout(1, 2));

        tabb1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                Order.this.stateChanged(evt);
            }
        });

        pan1.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scr1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Номер заказа", "Дата от", "Дата до", "Контрагент", "Менеджер", "Фильтр"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tab1.setFillsViewportHeight(true);
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(60);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(4).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(5).setPreferredWidth(60);
        }

        pan1.add(scr1, java.awt.BorderLayout.CENTER);

        pan11.setPreferredSize(new java.awt.Dimension(286, 470));
        pan11.setLayout(new java.awt.BorderLayout());

        pan2.setPreferredSize(new java.awt.Dimension(300, 214));

        lab1.setFont(frames.Util.getFont(0,0));
        lab1.setText("Тип расчтета");
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab1.setMinimumSize(new java.awt.Dimension(34, 14));
        lab1.setPreferredSize(new java.awt.Dimension(120, 18));

        lab2.setFont(frames.Util.getFont(0,0));
        lab2.setText("Номер счета");
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab2.setMinimumSize(new java.awt.Dimension(34, 14));
        lab2.setPreferredSize(new java.awt.Dimension(120, 18));

        lab3.setFont(frames.Util.getFont(0,0));
        lab3.setText("Валюта название");
        lab3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab3.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab3.setMinimumSize(new java.awt.Dimension(34, 14));
        lab3.setPreferredSize(new java.awt.Dimension(120, 18));

        lab4.setFont(frames.Util.getFont(0,0));
        lab4.setText("Cебестоимость");
        lab4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab4.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab4.setMinimumSize(new java.awt.Dimension(34, 14));
        lab4.setPreferredSize(new java.awt.Dimension(120, 18));

        lab5.setFont(frames.Util.getFont(0,0));
        lab5.setText("Cтоимость без скидки");
        lab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab5.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab5.setMinimumSize(new java.awt.Dimension(34, 14));
        lab5.setPreferredSize(new java.awt.Dimension(120, 18));

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setPreferredSize(new java.awt.Dimension(120, 18));

        txt3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt3.setFocusable(false);
        txt3.setPreferredSize(new java.awt.Dimension(120, 18));

        txt4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt4.setFocusable(false);
        txt4.setPreferredSize(new java.awt.Dimension(120, 18));

        txt5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt5.setFocusable(false);
        txt5.setPreferredSize(new java.awt.Dimension(120, 18));

        btn1.setText("...");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(18, 18));
        btn1.setMinimumSize(new java.awt.Dimension(18, 18));
        btn1.setName("btn1"); // NOI18N
        btn1.setPreferredSize(new java.awt.Dimension(18, 18));
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        lab6.setFont(frames.Util.getFont(0,0));
        lab6.setText("Cтоимость со скидкой");
        lab6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab6.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab6.setMinimumSize(new java.awt.Dimension(34, 14));
        lab6.setPreferredSize(new java.awt.Dimension(120, 18));

        txt6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt6.setFocusable(false);
        txt6.setPreferredSize(new java.awt.Dimension(120, 18));

        lab7.setFont(frames.Util.getFont(0,0));
        lab7.setText("Общий вес");
        lab7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab7.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab7.setMinimumSize(new java.awt.Dimension(34, 14));
        lab7.setPreferredSize(new java.awt.Dimension(120, 18));

        txt7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt7.setFocusable(false);
        txt7.setPreferredSize(new java.awt.Dimension(120, 18));

        lab8.setFont(frames.Util.getFont(0,0));
        lab8.setText("Общая площадь");
        lab8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab8.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab8.setMinimumSize(new java.awt.Dimension(34, 14));
        lab8.setPreferredSize(new java.awt.Dimension(120, 18));

        txt8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt8.setFocusable(false);
        txt8.setPreferredSize(new java.awt.Dimension(120, 18));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "По спецификации", "По целым хлыстам", " " }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jComboBox1.setMinimumSize(new java.awt.Dimension(12, 12));
        jComboBox1.setPreferredSize(new java.awt.Dimension(146, 18));

        javax.swing.GroupLayout pan2Layout = new javax.swing.GroupLayout(pan2);
        pan2.setLayout(pan2Layout);
        pan2Layout.setHorizontalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createSequentialGroup()
                        .addComponent(lab7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                            .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan2Layout.createSequentialGroup()
                            .addComponent(lab8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan2Layout.setVerticalGroup(
            pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan11.add(pan2, java.awt.BorderLayout.NORTH);

        pan7.setLayout(new java.awt.BorderLayout());

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        tab4.setRowHeight(68);
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab4.getColumnModel().getColumn(1).setMinWidth(68);
            tab4.getColumnModel().getColumn(1).setPreferredWidth(68);
            tab4.getColumnModel().getColumn(1).setMaxWidth(68);
        }

        pan7.add(scr4, java.awt.BorderLayout.CENTER);

        pan11.add(pan7, java.awt.BorderLayout.CENTER);

        pan1.add(pan11, java.awt.BorderLayout.EAST);

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\nЗаказы\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", pan1);

        pan3.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(400, 404));
        pan4.setLayout(new java.awt.BorderLayout());

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Наименование", "Рисунок"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setRowHeight(68);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(80);
            tab2.getColumnModel().getColumn(1).setMinWidth(68);
            tab2.getColumnModel().getColumn(1).setPreferredWidth(68);
            tab2.getColumnModel().getColumn(1).setMaxWidth(68);
        }

        pan4.add(scr2, java.awt.BorderLayout.CENTER);

        pan3.add(pan4, java.awt.BorderLayout.WEST);

        pan5.setLayout(new java.awt.BorderLayout());

        pan8.setPreferredSize(new java.awt.Dimension(10, 200));
        pan8.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout pan14Layout = new javax.swing.GroupLayout(pan14);
        pan14.setLayout(pan14Layout);
        pan14Layout.setHorizontalGroup(
            pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );
        pan14Layout.setVerticalGroup(
            pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        pan8.add(pan14, "card14");

        pan12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Основные", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan12.setToolTipText("");
        pan12.setPreferredSize(new java.awt.Dimension(300, 200));

        pan21.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Текстура изделия", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 0)));
        pan21.setPreferredSize(new java.awt.Dimension(308, 104));

        lab27.setFont(frames.Util.getFont(0,0));
        lab27.setText("Основная");
        lab27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab27.setPreferredSize(new java.awt.Dimension(80, 18));

        lab31.setFont(frames.Util.getFont(0,0));
        lab31.setText("Внутренняя");
        lab31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab31.setPreferredSize(new java.awt.Dimension(80, 18));

        lab32.setFont(frames.Util.getFont(0,0));
        lab32.setText("Внешняя");
        lab32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab32.setPreferredSize(new java.awt.Dimension(80, 18));

        btn9.setText("...");
        btn9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn9.setMaximumSize(new java.awt.Dimension(18, 18));
        btn9.setMinimumSize(new java.awt.Dimension(18, 18));
        btn9.setName("btn9"); // NOI18N
        btn9.setPreferredSize(new java.awt.Dimension(18, 18));
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        btn13.setText("...");
        btn13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn13.setMaximumSize(new java.awt.Dimension(18, 18));
        btn13.setMinimumSize(new java.awt.Dimension(18, 18));
        btn13.setName("btn13"); // NOI18N
        btn13.setPreferredSize(new java.awt.Dimension(18, 18));
        btn13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        btn2.setText("...");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(18, 18));
        btn2.setMinimumSize(new java.awt.Dimension(18, 18));
        btn2.setName("btn2"); // NOI18N
        btn2.setPreferredSize(new java.awt.Dimension(18, 18));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToWindows(evt);
            }
        });

        txt9.setEditable(false);
        txt9.setBackground(new java.awt.Color(255, 255, 255));
        txt9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt9.setPreferredSize(new java.awt.Dimension(180, 18));

        txt13.setEditable(false);
        txt13.setBackground(new java.awt.Color(255, 255, 255));
        txt13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt13.setPreferredSize(new java.awt.Dimension(180, 18));

        txt14.setEditable(false);
        txt14.setBackground(new java.awt.Color(255, 255, 255));
        txt14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt14.setPreferredSize(new java.awt.Dimension(180, 18));

        javax.swing.GroupLayout pan21Layout = new javax.swing.GroupLayout(pan21);
        pan21.setLayout(pan21Layout);
        pan21Layout.setHorizontalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan21Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt14, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan21Layout.createSequentialGroup()
                        .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        pan21Layout.setVerticalGroup(
            pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan21Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lab35.setFont(frames.Util.getFont(0,0));
        lab35.setText("Ширина");
        lab35.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab35.setPreferredSize(new java.awt.Dimension(80, 18));

        txt17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt17.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt17.setPreferredSize(new java.awt.Dimension(60, 18));
        txt17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        btn17.setText("...");
        btn17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn17.setMaximumSize(new java.awt.Dimension(18, 18));
        btn17.setMinimumSize(new java.awt.Dimension(18, 18));
        btn17.setName("btnField17"); // NOI18N
        btn17.setPreferredSize(new java.awt.Dimension(18, 18));
        btn17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt17Update(evt);
            }
        });

        lab38.setFont(frames.Util.getFont(0,0));
        lab38.setText("Высота1");
        lab38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab38.setPreferredSize(new java.awt.Dimension(80, 18));

        txt22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt22.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt22.setPreferredSize(new java.awt.Dimension(60, 18));
        txt22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        btn23.setText("...");
        btn23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn23.setMaximumSize(new java.awt.Dimension(18, 18));
        btn23.setMinimumSize(new java.awt.Dimension(18, 18));
        btn23.setName("btnField17"); // NOI18N
        btn23.setPreferredSize(new java.awt.Dimension(18, 18));
        btn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt22Update(evt);
            }
        });

        lab40.setFont(frames.Util.getFont(0,0));
        lab40.setText("Высота2");
        lab40.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab40.setPreferredSize(new java.awt.Dimension(80, 18));

        btn25.setText("...");
        btn25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn25.setMaximumSize(new java.awt.Dimension(18, 18));
        btn25.setMinimumSize(new java.awt.Dimension(18, 18));
        btn25.setName("btnField17"); // NOI18N
        btn25.setPreferredSize(new java.awt.Dimension(18, 18));
        btn25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt23Update(evt);
            }
        });

        txt23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt23.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt23.setPreferredSize(new java.awt.Dimension(60, 18));
        txt23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        javax.swing.GroupLayout pan12Layout = new javax.swing.GroupLayout(pan12);
        pan12.setLayout(pan12Layout);
        pan12Layout.setHorizontalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan21, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                    .addGroup(pan12Layout.createSequentialGroup()
                        .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan12Layout.createSequentialGroup()
                                .addComponent(lab38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan12Layout.createSequentialGroup()
                                .addComponent(lab35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan12Layout.createSequentialGroup()
                                .addComponent(lab40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan12Layout.setVerticalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan12Layout.createSequentialGroup()
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(pan21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pan8.add(pan12, "card12");

        pan13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Рама, импост..", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan13.setPreferredSize(new java.awt.Dimension(300, 200));

        pan20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Текстура элемента", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 0)));
        pan20.setPreferredSize(new java.awt.Dimension(308, 104));

        lab28.setFont(frames.Util.getFont(0,0));
        lab28.setText("Основная");
        lab28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab28.setPreferredSize(new java.awt.Dimension(80, 18));

        lab43.setFont(frames.Util.getFont(0,0));
        lab43.setText("Внутренняя");
        lab43.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab43.setPreferredSize(new java.awt.Dimension(80, 18));

        lab44.setFont(frames.Util.getFont(0,0));
        lab44.setText("Внешняя");
        lab44.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab44.setPreferredSize(new java.awt.Dimension(80, 18));

        btn18.setText("...");
        btn18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn18.setMaximumSize(new java.awt.Dimension(18, 18));
        btn18.setMinimumSize(new java.awt.Dimension(18, 18));
        btn18.setName("btnField17"); // NOI18N
        btn18.setPreferredSize(new java.awt.Dimension(18, 18));
        btn18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToFrame(evt);
            }
        });

        btn19.setText("...");
        btn19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn19.setMaximumSize(new java.awt.Dimension(18, 18));
        btn19.setMinimumSize(new java.awt.Dimension(18, 18));
        btn19.setName("btnField17"); // NOI18N
        btn19.setPreferredSize(new java.awt.Dimension(18, 18));
        btn19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToFrame(evt);
            }
        });

        btn20.setText("...");
        btn20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn20.setMaximumSize(new java.awt.Dimension(18, 18));
        btn20.setMinimumSize(new java.awt.Dimension(18, 18));
        btn20.setName("btnField17"); // NOI18N
        btn20.setPreferredSize(new java.awt.Dimension(18, 18));
        btn20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToFrame(evt);
            }
        });

        txt27.setEditable(false);
        txt27.setBackground(new java.awt.Color(255, 255, 255));
        txt27.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt27.setPreferredSize(new java.awt.Dimension(180, 18));

        txt28.setEditable(false);
        txt28.setBackground(new java.awt.Color(255, 255, 255));
        txt28.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt28.setPreferredSize(new java.awt.Dimension(180, 18));

        txt29.setEditable(false);
        txt29.setBackground(new java.awt.Color(255, 255, 255));
        txt29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt29.setPreferredSize(new java.awt.Dimension(180, 18));

        javax.swing.GroupLayout pan20Layout = new javax.swing.GroupLayout(pan20);
        pan20.setLayout(pan20Layout);
        pan20Layout.setHorizontalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan20Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan20Layout.createSequentialGroup()
                        .addComponent(lab44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan20Layout.createSequentialGroup()
                        .addComponent(lab28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(pan20Layout.createSequentialGroup()
                        .addComponent(lab43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        pan20Layout.setVerticalGroup(
            pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan20Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lab33.setFont(frames.Util.getFont(0,0));
        lab33.setText("  Артикул");
        lab33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab33.setPreferredSize(new java.awt.Dimension(80, 18));

        lab34.setFont(frames.Util.getFont(0,0));
        lab34.setText("  Название");
        lab34.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab34.setPreferredSize(new java.awt.Dimension(80, 18));

        txt32.setEditable(false);
        txt32.setBackground(new java.awt.Color(255, 255, 255));
        txt32.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt32.setPreferredSize(new java.awt.Dimension(180, 18));

        txt33.setEditable(false);
        txt33.setBackground(new java.awt.Color(255, 255, 255));
        txt33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt33.setPreferredSize(new java.awt.Dimension(180, 18));

        btn22.setText("...");
        btn22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn22.setMaximumSize(new java.awt.Dimension(18, 18));
        btn22.setMinimumSize(new java.awt.Dimension(18, 18));
        btn22.setName("btnField17"); // NOI18N
        btn22.setPreferredSize(new java.awt.Dimension(18, 18));
        btn22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysprofToFrame(evt);
            }
        });

        javax.swing.GroupLayout pan13Layout = new javax.swing.GroupLayout(pan13);
        pan13.setLayout(pan13Layout);
        pan13Layout.setHorizontalGroup(
            pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan20, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                    .addGroup(pan13Layout.createSequentialGroup()
                        .addComponent(lab33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt32, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan13Layout.createSequentialGroup()
                        .addComponent(lab34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt33, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan13Layout.setVerticalGroup(
            pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pan8.add(pan13, "card13");

        pan15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Стеклопакет", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan15.setPreferredSize(new java.awt.Dimension(300, 200));

        lab29.setFont(frames.Util.getFont(0,0));
        lab29.setText("Артикул");
        lab29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab29.setPreferredSize(new java.awt.Dimension(80, 18));

        lab36.setFont(frames.Util.getFont(0,0));
        lab36.setText("Название");
        lab36.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab36.setPreferredSize(new java.awt.Dimension(80, 18));

        btn3.setText("...");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(18, 18));
        btn3.setMinimumSize(new java.awt.Dimension(18, 18));
        btn3.setName("btnField17"); // NOI18N
        btn3.setPreferredSize(new java.awt.Dimension(18, 18));
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        txt19.setEditable(false);
        txt19.setBackground(new java.awt.Color(255, 255, 255));
        txt19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt19.setPreferredSize(new java.awt.Dimension(180, 18));

        txt18.setEditable(false);
        txt18.setBackground(new java.awt.Color(255, 255, 255));
        txt18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt18.setPreferredSize(new java.awt.Dimension(180, 18));

        javax.swing.GroupLayout pan15Layout = new javax.swing.GroupLayout(pan15);
        pan15.setLayout(pan15Layout);
        pan15Layout.setHorizontalGroup(
            pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan15Layout.createSequentialGroup()
                        .addComponent(lab29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt19, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan15Layout.createSequentialGroup()
                        .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt18, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan15Layout.setVerticalGroup(
            pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );

        pan8.add(pan15, "card15");

        pan16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Створка", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, frames.Util.getFont(0, 1)));
        pan16.setPreferredSize(new java.awt.Dimension(3100, 200));

        lab30.setFont(frames.Util.getFont(0,0));
        lab30.setText("Фурнитура");
        lab30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab30.setPreferredSize(new java.awt.Dimension(80, 18));

        lab37.setFont(frames.Util.getFont(0,0));
        lab37.setText("Ручка");
        lab37.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab37.setPreferredSize(new java.awt.Dimension(80, 18));

        lab39.setFont(frames.Util.getFont(0,0));
        lab39.setText("Текстура");
        lab39.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab39.setPreferredSize(new java.awt.Dimension(80, 18));

        lab46.setFont(frames.Util.getFont(0,0));
        lab46.setText("Высота ручки");
        lab46.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab46.setPreferredSize(new java.awt.Dimension(80, 18));

        lab45.setFont(frames.Util.getFont(0,0));
        lab45.setText("Напр. откр.");
        lab45.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab45.setPreferredSize(new java.awt.Dimension(80, 18));

        btn10.setText("...");
        btn10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn10.setMaximumSize(new java.awt.Dimension(18, 18));
        btn10.setMinimumSize(new java.awt.Dimension(18, 18));
        btn10.setName("btnField17"); // NOI18N
        btn10.setPreferredSize(new java.awt.Dimension(18, 18));
        btn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysfurnToStvorka(evt);
            }
        });

        btn12.setText("...");
        btn12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn12.setMaximumSize(new java.awt.Dimension(18, 18));
        btn12.setMinimumSize(new java.awt.Dimension(18, 18));
        btn12.setName("btnField17"); // NOI18N
        btn12.setPreferredSize(new java.awt.Dimension(18, 18));
        btn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handkToStvorka(evt);
            }
        });

        btn14.setText("...");
        btn14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn14.setMaximumSize(new java.awt.Dimension(18, 18));
        btn14.setMinimumSize(new java.awt.Dimension(18, 18));
        btn14.setName("btnField17"); // NOI18N
        btn14.setPreferredSize(new java.awt.Dimension(18, 18));
        btn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorToHandl(evt);
            }
        });

        btn21.setText("...");
        btn21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn21.setMaximumSize(new java.awt.Dimension(18, 18));
        btn21.setMinimumSize(new java.awt.Dimension(18, 18));
        btn21.setName("btnField17"); // NOI18N
        btn21.setPreferredSize(new java.awt.Dimension(18, 18));
        btn21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeOpenToStvorka(evt);
            }
        });

        btn6.setText("...");
        btn6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn6.setMaximumSize(new java.awt.Dimension(18, 18));
        btn6.setMinimumSize(new java.awt.Dimension(18, 18));
        btn6.setName("btnField17"); // NOI18N
        btn6.setPreferredSize(new java.awt.Dimension(18, 18));
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightHandlToStvorka(evt);
            }
        });

        txt20.setEditable(false);
        txt20.setBackground(new java.awt.Color(255, 255, 255));
        txt20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt20.setPreferredSize(new java.awt.Dimension(180, 18));

        txt30.setEditable(false);
        txt30.setBackground(new java.awt.Color(255, 255, 255));
        txt30.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt30.setPreferredSize(new java.awt.Dimension(180, 18));

        txt25.setEditable(false);
        txt25.setBackground(new java.awt.Color(255, 255, 255));
        txt25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt25.setPreferredSize(new java.awt.Dimension(180, 18));

        txt21.setEditable(false);
        txt21.setBackground(new java.awt.Color(255, 255, 255));
        txt21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt21.setPreferredSize(new java.awt.Dimension(180, 18));

        txt31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt31.setPreferredSize(new java.awt.Dimension(180, 18));

        txt16.setEditable(false);
        txt16.setBackground(new java.awt.Color(255, 255, 255));
        txt16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt16.setPreferredSize(new java.awt.Dimension(180, 18));

        lab41.setFont(frames.Util.getFont(0,0));
        lab41.setText("Ширина");
        lab41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab41.setPreferredSize(new java.awt.Dimension(80, 18));

        txt24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt24.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt24.setPreferredSize(new java.awt.Dimension(60, 18));
        txt24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        btn15.setText("...");
        btn15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn15.setMaximumSize(new java.awt.Dimension(18, 18));
        btn15.setMinimumSize(new java.awt.Dimension(18, 18));
        btn15.setName("btnField17"); // NOI18N
        btn15.setPreferredSize(new java.awt.Dimension(18, 18));
        btn15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt24Update(evt);
            }
        });

        lab42.setFont(frames.Util.getFont(0,0));
        lab42.setText("Высота");
        lab42.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab42.setPreferredSize(new java.awt.Dimension(80, 18));

        txt26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt26.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt26.setPreferredSize(new java.awt.Dimension(60, 18));
        txt26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyEnter(evt);
            }
        });

        btn16.setText("...");
        btn16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn16.setMaximumSize(new java.awt.Dimension(18, 18));
        btn16.setMinimumSize(new java.awt.Dimension(18, 18));
        btn16.setName("btnField17"); // NOI18N
        btn16.setPreferredSize(new java.awt.Dimension(18, 18));
        btn16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt26Update(evt);
            }
        });

        javax.swing.GroupLayout pan16Layout = new javax.swing.GroupLayout(pan16);
        pan16.setLayout(pan16Layout);
        pan16Layout.setHorizontalGroup(
            pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lab39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt21, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                            .addComponent(txt25, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lab46, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab45, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lab30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(txt30, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addComponent(txt16, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt31, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pan16Layout.createSequentialGroup()
                        .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addComponent(lab42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pan16Layout.createSequentialGroup()
                                .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pan16Layout.setVerticalGroup(
            pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan8.add(pan16, "card16");

        pan5.add(pan8, java.awt.BorderLayout.NORTH);

        pan9.setLayout(new java.awt.BorderLayout());

        panDesign.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panDesign.setLayout(new java.awt.BorderLayout());
        pan9.add(panDesign, java.awt.BorderLayout.CENTER);

        scr6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr6.setPreferredSize(new java.awt.Dimension(240, 324));
        scr6.setViewportView(windowsTree);

        pan9.add(scr6, java.awt.BorderLayout.WEST);

        pan5.add(pan9, java.awt.BorderLayout.CENTER);

        pan3.add(pan5, java.awt.BorderLayout.CENTER);

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\nИзделия\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", pan3);

        pan6.setLayout(new java.awt.BorderLayout());

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Изд.№", "Артикул", "Название", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Угол 1", "Угол 2", "Кол-во", "Погонаж", "Скидка %", "Примечание"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);

        pan6.add(scr3, java.awt.BorderLayout.CENTER);

        tabb1.addTab("<html><font size=\"3\">\n&nbsp;&nbsp;&nbsp;\nКомплектация\n&nbsp;&nbsp;&nbsp;", pan6);

        centr.add(tabb1);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
        labFilter.setMaximumSize(new java.awt.Dimension(100, 14));
        labFilter.setMinimumSize(new java.awt.Dimension(100, 14));
        labFilter.setPreferredSize(new java.awt.Dimension(100, 14));
        south.add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(180, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(180, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(180, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFilter(evt);
            }
        });
        south.add(txtFilter);

        checkFilter.setText("в конце строки");
        south.add(checkFilter);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Arrays.asList(qProject, qPrjprod).forEach(q -> q.execsql());
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(tab1, this, tab2) == 0 && tab1.getSelectedRow() != -1) {
                Util.deleteRecord(tab1);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0 && tab2.getSelectedRow() != -1) {
                Util.deleteRecord(tab2);
            } else {
                JOptionPane.showMessageDialog(null, "Ни одна из текущих записей не выбрана", "Предупреждение", JOptionPane.NO_OPTION);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, eProject.up, (record) -> {
                record.set(eProject.manager, eProfile.user);
            });

        } else if (tab2.getBorder() != null) {
            new DicSyspod(this, (record) -> {
                Record record2 = ePrjprod.up.newRecord();
                record2.set(ePrjprod.id, Confb.instanc().genId(ePrjprod.up));
                record2.set(ePrjprod.name, record.getStr(eSysprod.name));
                record2.set(ePrjprod.script, record.getStr(eSysprod.script));
                record2.set(ePrjprod.systree_id, record.getStr(eSysprod.systree_id));
                record2.set(ePrjprod.order_id, qProject.getAs(Util.getIndexRec(tab1), eProject.id));
                qPrjprod.insert(record2);
                loadingTab2();
                ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
                for (int index = 0; index < qPrjprod.size(); ++index) {
                    if (qPrjprod.get(index, eSysprod.id) == record2.get(eSysprod.id)) {
                        Util.setSelectedRow(tab2, index);
                        Util.scrollRectToRow(index, tab2);
                        windowsTree.setSelectionRow(0);
                    }
                }
            });
        }
    }//GEN-LAST:event_btnInsert

    private void txtFilter(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFilter
        JTable table = Stream.of(tab1).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_txtFilter

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2);
        eProperty.save(); //запишем текущий ordersId в файл
        Arrays.asList(qProject, qPrjprod).forEach(q -> q.execsql());
    }//GEN-LAST:event_windowClosed

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        Util.updateBorderAndSql(tab1, Arrays.asList(tab1, tab2, tab3));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(tab1.getColumnName((tab1.getSelectedColumn() == -1 || tab1.getSelectedColumn() == 0) ? 0 : tab1.getSelectedColumn()));
            txtFilter.setName(tab1.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void stateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_stateChanged
        Util.stopCellEditing(tab1, tab2, tab3);
        if (tabb1.getSelectedIndex() == 0) {
            Util.updateBorderAndSql(tab1, Arrays.asList(tab1, tab2, tab3));
        } else if (tabb1.getSelectedIndex() == 1) {
            Util.updateBorderAndSql(tab2, Arrays.asList(tab1, tab2, tab3));
        } else if (tabb1.getSelectedIndex() == 2) {
            Util.updateBorderAndSql(tab3, Arrays.asList(tab1, tab2, tab3));
        }
    }//GEN-LAST:event_stateChanged

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        Currenc frame = new Currenc(this, (record) -> {
            int index = Util.getIndexRec(tab1);
            if (index != -1) {
                Record record2 = qProject.get(index);
                record2.set(eProject.currenc_id, record.get(eCurrenc.id));
                rsvPrj.load();
            }
            Util.stopCellEditing(tab1, tab2);
        });
    }//GEN-LAST:event_btn1ActionPerformed

    private void colorToWindows(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToWindows
        try {
            if (windowsNode != null) {
                float selectID = windowsNode.com5t().id();
                HashSet<Record> set = new HashSet();
                int systreeID = qPrjprod.getAs(Util.getIndexRec(tab2), ePrjprod.systree_id);
                Record systreeRec = eSystree.find(systreeID);
                String[] arr1 = (systreeRec.getStr(eSystree.cgrp).isEmpty() == false) ? systreeRec.getStr(eSystree.cgrp).split(";") : null;
                eSystree col = (evt.getSource() == btn9) ? eSystree.col1 : (evt.getSource() == btn13) ? eSystree.col2 : eSystree.col3;
                Integer[] arr2 = builder.specif.Util.parserInt(systreeRec.getStr(col));
                if (arr1 != null) {
                    for (String s1 : arr1) { //группы
                        HashSet<Record> se2 = new HashSet();
                        boolean b = false;
                        for (Record rec : eColor.query()) {

                            if (rec.getStr(eColor.colgrp_id).equals(s1)) {
                                se2.add(rec); //текстуры группы

                                for (int i = 0; i < arr2.length; i = i + 2) { //тестуры
                                    if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                        b = true;
                                    }
                                }
                            }
                        }
                        if (b == false) { //если небыло пападаний то добавляем всю группу
                            set.addAll(se2);
                        }
                    }
                }
                if (arr2.length != 0) {
                    for (Record rec : eColor.query()) {
                        if (arr1 != null) {

                            for (String s1 : arr1) { //группы
                                if (rec.getStr(eColor.colgrp_id).equals(s1)) {
                                    for (int i = 0; i < arr2.length; i = i + 2) { //текстуры
                                        if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                            set.add(rec);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < arr2.length; i = i + 2) { //тестуры
                                if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                    set.add(rec);
                                }
                            }
                        }
                    }
                }

                ListenerRecord listenerColor = (colorRec) -> {

                    GsonElem jsonElem = iwin.rootGson.find(selectID);
                    if (jsonElem != null) {
                        String paramStr = (jsonElem.param().isEmpty()) ? "{}" : jsonElem.param();
                        JsonObject jsonObject = gson.fromJson(paramStr, JsonObject.class);

                        if (evt.getSource() == btn9) {
                            jsonObject.addProperty(PKjson.colorID1, colorRec.getStr(eColor.id));
                        } else if (evt.getSource() == btn13) {
                            jsonObject.addProperty(PKjson.colorID2, colorRec.getStr(eColor.id));
                        } else if (evt.getSource() == btn2) {
                            jsonObject.addProperty(PKjson.colorID3, colorRec.getStr(eColor.id));
                        }
                        paramStr = gson.toJson(jsonObject);
                        jsonElem.param(paramStr);
                        updateScript(selectID);
                    }
                };
                if (arr1 == null && arr2.length == 0) {
                    new DicColor(this, listenerColor);
                } else {
                    new DicColor(this, listenerColor, set);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToWindows

    private void sysprofToFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysprofToFrame
        try {
            if (windowsNode != null) {
                float selectID = windowsNode.com5t().id();
                int systreeID = qPrjprod.getAs(Util.getIndexRec(tab2), ePrjprod.systree_id);
                Query qSysprof = new Query(eSysprof.values(), eArtikl.values()).select(eSysprof.up, "left join",
                        eArtikl.up, "on", eArtikl.id, "=", eSysprof.artikl_id, "where", eSysprof.systree_id, "=", systreeID);
                Query qSysprof2 = new Query(eSysprof.values(), eArtikl.values());
                UseArtiklTo useArtiklTo = (windowsNode.com5t().type() == TypeElem.IMPOST) ? UseArtiklTo.IMPOST
                        : (windowsNode.com5t().type() == TypeElem.FRAME_SIDE) ? UseArtiklTo.FRAME : UseArtiklTo.STVORKA;

                for (int index = 0; index < qSysprof.size(); ++index) {
                    Record sysprofRec = qSysprof.get(index);
                    if (sysprofRec.getInt(eSysprof.use_type) == useArtiklTo.id) {
                        if (sysprofRec.getInt(eSysprof.use_side) == windowsNode.com5t().layout().id
                                || sysprofRec.getInt(eSysprof.use_side) == UseSide.ANY.id
                                || sysprofRec.getInt(eSysprof.use_side) == UseSide.MANUAL.id) {
                            qSysprof2.add(sysprofRec);
                            qSysprof2.table(eArtikl.up).add(qSysprof.table(eArtikl.up).get(index));
                        }
                    }
                }
                new DicSysprof(this, (sysprofRec) -> {

                    if (windowsNode.com5t().type() == TypeElem.FRAME_SIDE) { //рама окна
                        float gsonId = windowsNode.com5t().id();
                        GsonElem gsonRama = iwin.rootGson.find(gsonId);
                        String paramStr = gsonRama.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        paramObj.addProperty(PKjson.sysprofID, sysprofRec.getInt(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        gsonRama.param(paramStr);
                        updateScript(selectID);

                    } else if (windowsNode.com5t().type() == TypeElem.STVORKA_SIDE) { //рама створки
                        float stvId = ((DefMutableTreeNode) windowsNode.getParent()).com5t().id();
                        GsonElem stvArea = (GsonElem) iwin.rootGson.find(stvId);
                        String paramStr = stvArea.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        String stvKey = null;
                        if (windowsNode.com5t().layout() == LayoutArea.BOTTOM) {
                            stvKey = PKjson.stvorkaBottom;
                        } else if (windowsNode.com5t().layout() == LayoutArea.RIGHT) {
                            stvKey = PKjson.stvorkaRight;
                        } else if (windowsNode.com5t().layout() == LayoutArea.TOP) {
                            stvKey = PKjson.stvorkaTop;
                        } else if (windowsNode.com5t().layout() == LayoutArea.LEFT) {
                            stvKey = PKjson.stvorkaLeft;
                        }
                        JsonObject jso = Ujson.getAsJsonObject(paramObj, stvKey);
                        jso.addProperty(PKjson.sysprofID, sysprofRec.getStr(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        stvArea.param(paramStr);
                        updateScript(selectID);

                    } else {  //импост
                        float elemId = windowsNode.com5t().id();
                        GsonElem gsonElem = iwin.rootGson.find(elemId);
                        String paramStr = gsonElem.param();
                        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                        paramObj.addProperty(PKjson.sysprofID, sysprofRec.getInt(eSysprof.id));
                        paramStr = gson.toJson(paramObj);
                        gsonElem.param(paramStr);
                        updateScript(selectID);
                    }

                }, qSysprof2);
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_sysprofToFrame

    private void colorToFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToFrame
        try {
            float selectID = windowsNode.com5t().id();
            HashSet<Record> colorSet = new HashSet();
            Query artdetList = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.artikl_id, "=", windowsNode.com5t().artiklRec.getInt(eArtikl.id));
            artdetList.forEach(rec -> {

                if (rec.getInt(eArtdet.color_fk) < 0) {
                    eColor.query().forEach(rec2 -> {
                        if (rec2.getInt(eColor.colgrp_id) == Math.abs(rec.getInt(eArtdet.color_fk))) {
                            colorSet.add(rec2);
                        }
                    });
                } else {
                    colorSet.add(eColor.find(rec.getInt(eArtdet.color_fk)));
                }
            });
            DicColor frame = new DicColor(this, (colorRec) -> {

                String colorID = (evt.getSource() == btn18) ? PKjson.colorID1 : (evt.getSource() == btn19) ? PKjson.colorID2 : PKjson.colorID3;
                float parentId = ((DefMutableTreeNode) windowsNode.getParent()).com5t().id();
                GsonElem jsonArea = (GsonElem) iwin.rootGson.find(parentId);

                if (windowsNode.com5t().type() == TypeElem.STVORKA_SIDE) {
                    String paramStr = jsonArea.param();
                    JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                    String stvKey = null;
                    if (windowsNode.com5t().layout() == LayoutArea.BOTTOM) {
                        stvKey = PKjson.stvorkaBottom;
                    } else if (windowsNode.com5t().layout() == LayoutArea.RIGHT) {
                        stvKey = PKjson.stvorkaRight;
                    } else if (windowsNode.com5t().layout() == LayoutArea.TOP) {
                        stvKey = PKjson.stvorkaTop;
                    } else if (windowsNode.com5t().layout() == LayoutArea.LEFT) {
                        stvKey = PKjson.stvorkaLeft;
                    }
                    JsonObject jso = Ujson.getAsJsonObject(paramObj, stvKey);
                    jso.addProperty(colorID, colorRec.getStr(eColor.id));
                    paramStr = gson.toJson(paramObj);
                    jsonArea.param(paramStr);
                    updateScript(selectID);

                } else if (windowsNode.com5t().type() == TypeElem.FRAME_SIDE) {
                    for (GsonElem elem : jsonArea.elements()) {
                        if (elem.id() == ((DefMutableTreeNode) windowsNode).com5t().id()) {
                            String paramStr = elem.param();
                            JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                            paramObj.addProperty(colorID, colorRec.getStr(eColor.id));
                            paramStr = gson.toJson(paramObj);
                            elem.param(paramStr);
                            updateScript(selectID);
                        }
                    }
                }

            }, colorSet);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToFrame

    private void sysfurnToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysfurnToStvorka
        try {
            float windowsID = windowsNode.com5t().id();
            int systreeID = qPrjprod.getAs(Util.getIndexRec(tab2), ePrjprod.systree_id);

            Query qSysfurn = new Query(eSysfurn.values(), eFurniture.values()).select(eSysfurn.up, "left join", eFurniture.up, "on",
                    eSysfurn.furniture_id, "=", eFurniture.id, "where", eSysfurn.systree_id, "=", systreeID);

            new DicName(this, (sysfurnRec) -> {

                GsonElem jsonStv = (GsonElem) iwin.rootGson.find(windowsID);
                String paramStr = jsonStv.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.sysfurnID, sysfurnRec.getStr(eSysfurn.id));
                paramStr = gson.toJson(paramObj);
                jsonStv.param(paramStr);
                updateScript(windowsID);

            }, qSysfurn, eFurniture.name);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_sysfurnToStvorka

    private void typeOpenToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeOpenToStvorka
        try {
            new DicEnums(this, (typeopenRec) -> {

                float elemID = windowsNode.com5t().id();
                GsonElem jsonStv = (GsonElem) iwin.rootGson.find(elemID);
                String paramStr = jsonStv.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.typeOpen, typeopenRec.getInt(0));
                paramStr = gson.toJson(paramObj);
                jsonStv.param(paramStr);
                updateScript(elemID);

            }, TypeOpen1.LEFT, TypeOpen1.LEFTUP, TypeOpen1.LEFTSHIFT,
                    TypeOpen1.RIGHT, TypeOpen1.RIGHTUP, TypeOpen1.RIGHTSHIFT, TypeOpen1.UPPER, TypeOpen1.FIXED);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_typeOpenToStvorka

    private void colorToHandl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorToHandl
        try {
            float selectID = windowsNode.com5t().id();
            HashSet<Record> colorSet = new HashSet();
            Query artdetList = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.artikl_id, "=", windowsNode.com5t().artiklRec.getInt(eArtikl.id));
            artdetList.forEach(rec -> {

                if (rec.getInt(eArtdet.color_fk) < 0) {
                    eColor.query().forEach(rec2 -> {
                        if (rec2.getInt(eColor.colgrp_id) == Math.abs(rec.getInt(eArtdet.color_fk))) {
                            colorSet.add(rec2);
                        }
                    });
                } else {
                    colorSet.add(eColor.find(rec.getInt(eArtdet.color_fk)));
                }
            });
            DicColor frame = new DicColor(this, (colorRec) -> {

                GsonElem jsonStv = (GsonElem) iwin.rootGson.find(selectID);
                String paramStr = jsonStv.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.colorHandl, colorRec.getStr(eColor.id));
                paramStr = gson.toJson(paramObj);
                jsonStv.param(paramStr);
                updateScript(selectID);

            }, colorSet);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_colorToHandl

    private void handkToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handkToStvorka
        try {
            float selectID = windowsNode.com5t().id();
            int furnitureID = ((AreaStvorka) windowsNode.com5t()).sysfurnRec.getInt(eSysfurn.furniture_id);
            Query qFurndet = new Query(eFurndet.values()).select(eFurndet.up, "where", eFurndet.furniture_id1, "=", furnitureID);
            Query qArtikl = new Query(eArtikl.values()).select(eArtikl.up, "where", eArtikl.level1, "= 2 and", eArtikl.level2, " = 11");
            Query qArtikl2 = new Query(eArtikl.values());
            for (Record furndetRec : qFurndet) { //первый уровень
                for (Record artiklRec : qArtikl) {
                    if (furndetRec.getInt(eFurndet.artikl_id) == artiklRec.getInt(eArtikl.id)) {
                        qArtikl2.add(artiklRec);
                    }
                }
                Query qFurndet2 = new Query(eFurndet.values()).select(eFurndet.up, "where",
                        eFurndet.furndet_id, "=", furndetRec.getInt(eFurndet.id), "and", eFurndet.furndet_id, "!=", eFurndet.id);
                for (Record furndet2Rec : qFurndet2) { //второй уровень
                    for (Record artiklRec : qArtikl) {
                        if (furndet2Rec.getInt(eFurndet.artikl_id) == artiklRec.getInt(eArtikl.id)) {
                            qArtikl2.add(artiklRec);
                        }
                    }
                }
            }
            new DicArtikl(this, (artiklRec) -> {

                GsonElem jsonStv = (GsonElem) iwin.rootGson.find(selectID);
                String paramStr = jsonStv.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.artiklHandl, artiklRec.getStr(eArtikl.id));
                paramStr = gson.toJson(paramObj);
                jsonStv.param(paramStr);
                updateScript(selectID);

            }, qArtikl2);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_handkToStvorka

    private void heightHandlToStvorka(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightHandlToStvorka

        AreaStvorka areaStv = (AreaStvorka) windowsNode.com5t();
        int indexLayoutHandl = 0;
        if (LayoutHandle.CONST.name.equals(txt16.getText())) {
            indexLayoutHandl = 1;
        } else if (LayoutHandle.SET.name.equals(txt16.getText())) {
            indexLayoutHandl = 2;
        }
        new DicHandl(this, (record) -> {
            try {
                float selectID = areaStv.id();
                GsonElem jsonStv = (GsonElem) iwin.rootGson.find(selectID);
                String paramStr = jsonStv.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);

                if (record.getInt(0) == 0) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.MIDL.id);
                    txt31.setEditable(false);

                } else if (record.getInt(0) == 1) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.CONST.id);
                    txt31.setEditable(false);

                } else if (record.getInt(0) == 2) {
                    paramObj.addProperty(PKjson.positionHandl, LayoutHandle.SET.id);
                    paramObj.addProperty(PKjson.heightHandl, record.getInt(1));
                    txt31.setEditable(true);
                }
                paramStr = gson.toJson(paramObj);
                jsonStv.param(paramStr);
                updateScript(selectID);

            } catch (Exception e) {
                System.err.println("Ошибка: " + e);
            }

        }, indexLayoutHandl);
    }//GEN-LAST:event_heightHandlToStvorka

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        try {
            float selectID = windowsNode.com5t().id();
            int systreeID = qPrjprod.getAs(Util.getIndexRec(tab2), ePrjprod.systree_id);
            Record systreeRec = eSystree.find(systreeID);
            String depth = systreeRec.getStr(eSystree.depth);
            if (depth != null && depth.isEmpty() == false) {
                depth = depth.replace(";", ",");
                if (depth.charAt(depth.length() - 1) == ',') {
                    depth = depth.substring(0, depth.length() - 1);
                }
            }
            depth = (depth != null && depth.isEmpty() == false) ? " and " + eArtikl.depth.name() + " in (" + depth + ")" : "";
            Query qArtikl = new Query(eArtikl.values()).select(eArtikl.up, "where",
                    eArtikl.level1, "= 5 and", eArtikl.level2, "in (1,2,3)", depth);

            new DicArtikl(this, (artiklRec) -> {

                GsonElem glassElem = (GsonElem) iwin.rootGson.find(selectID);
                String paramStr = glassElem.param();
                JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
                paramObj.addProperty(PKjson.artglasID, artiklRec.getStr(eArtikl.id));
                paramStr = gson.toJson(paramObj);
                glassElem.param(paramStr);
                updateScript(selectID);

            }, qArtikl);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
    }//GEN-LAST:event_btn3ActionPerformed

    private void txtKeyEnter(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeyEnter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (evt.getSource() == txt17) {
                txt17Update(null);
            } else if (evt.getSource() == txt22) {
                txt22Update(null);
            } else if (evt.getSource() == txt23) {
                txt23Update(null);
            } else if (evt.getSource() == txt24) {
                txt24Update(null);
            } else if (evt.getSource() == txt26) {
                txt26Update(null);
            }
        }
    }//GEN-LAST:event_txtKeyEnter

    private void txt17Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt17Update
        float windowsID = windowsNode.com5t().id();
        float dx = Float.valueOf(txt17.getText()) / iwin.rootGson.width();
        iwin.rootGson.width(Float.valueOf(txt17.getText()));
        iwin.rootGson.widthDown(iwin.rootGson, dx);
        updateScript(windowsID);
    }//GEN-LAST:event_txt17Update

    private void txt22Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt22Update
        float windowsID = windowsNode.com5t().id();
        float dy = Float.valueOf(txt22.getText()) / iwin.rootGson.height();
        iwin.rootGson.height(Float.valueOf(txt22.getText()));
        //        if (iwin.rootGson.heightAdd() != null) {
        //            iwin.rootGson.heightAdd(Float.valueOf(txt22.getText()));
        //        }
        iwin.rootGson.heightDown(iwin.rootGson, dy);
        updateScript(windowsID);
    }//GEN-LAST:event_txt22Update

    private void txt23Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt23Update
        //            float windowsID = windowsNode.com5t().id();
        //            float dy = Float.valueOf(txt22.getText()) / iwin.rootGson.height();
        //            iwin.rootGson.height(Float.valueOf(txt22.getText()));
        //            iwin.rootGson.heightAdd(Float.valueOf(txt22.getText()));
        //            iwin.rootGson.heightDown(iwin.rootGson, dy);
        //            updateScript(windowsID);
    }//GEN-LAST:event_txt23Update

    private void txt24Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt24Update
        float windowsID = windowsNode.com5t().id();
        GsonElem jsonStv = (GsonElem) iwin.rootGson.find(windowsID);
        jsonStv.widthUp(Float.valueOf(txt24.getText()));
        updateScript(windowsID);
    }//GEN-LAST:event_txt24Update

    private void txt26Update(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt26Update
        float windowsID = windowsNode.com5t().id();
        GsonElem jsonStv = (GsonElem) iwin.rootGson.find(windowsID);
        jsonStv.heightUp(Float.valueOf(txt26.getText()));
        updateScript(windowsID);
    }//GEN-LAST:event_txt26Update

    private void btnCalcresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcresh
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCalcresh

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn15;
    private javax.swing.JButton btn16;
    private javax.swing.JButton btn17;
    private javax.swing.JButton btn18;
    private javax.swing.JButton btn19;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn20;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnCalc;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab27;
    private javax.swing.JLabel lab28;
    private javax.swing.JLabel lab29;
    private javax.swing.JLabel lab3;
    private javax.swing.JLabel lab30;
    private javax.swing.JLabel lab31;
    private javax.swing.JLabel lab32;
    private javax.swing.JLabel lab33;
    private javax.swing.JLabel lab34;
    private javax.swing.JLabel lab35;
    private javax.swing.JLabel lab36;
    private javax.swing.JLabel lab37;
    private javax.swing.JLabel lab38;
    private javax.swing.JLabel lab39;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab40;
    private javax.swing.JLabel lab41;
    private javax.swing.JLabel lab42;
    private javax.swing.JLabel lab43;
    private javax.swing.JLabel lab44;
    private javax.swing.JLabel lab45;
    private javax.swing.JLabel lab46;
    private javax.swing.JLabel lab5;
    private javax.swing.JLabel lab6;
    private javax.swing.JLabel lab7;
    private javax.swing.JLabel lab8;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan12;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan14;
    private javax.swing.JPanel pan15;
    private javax.swing.JPanel pan16;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan20;
    private javax.swing.JPanel pan21;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan7;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JPanel panDesign;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr6;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTabbedPane tabb1;
    private javax.swing.JTextField txt13;
    private javax.swing.JTextField txt14;
    private javax.swing.JTextField txt16;
    private javax.swing.JTextField txt17;
    private javax.swing.JTextField txt18;
    private javax.swing.JTextField txt19;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt20;
    private javax.swing.JTextField txt21;
    private javax.swing.JTextField txt22;
    private javax.swing.JTextField txt23;
    private javax.swing.JTextField txt24;
    private javax.swing.JTextField txt25;
    private javax.swing.JTextField txt26;
    private javax.swing.JTextField txt27;
    private javax.swing.JTextField txt28;
    private javax.swing.JTextField txt29;
    private javax.swing.JTextField txt3;
    private javax.swing.JTextField txt30;
    private javax.swing.JTextField txt31;
    private javax.swing.JTextField txt32;
    private javax.swing.JTextField txt33;
    private javax.swing.JTextField txt4;
    private javax.swing.JTextField txt5;
    private javax.swing.JTextField txt6;
    private javax.swing.JTextField txt7;
    private javax.swing.JTextField txt8;
    private javax.swing.JTextField txt9;
    private javax.swing.JTextField txtFilter;
    private javax.swing.JTree windowsTree;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {
        new FrameToFile(this, btnClose);
        FrameToFile.setFrameSize(this);
        Util.documentFilter(3, txt4, txt5, txt6, txt7, txt8);
        labFilter.setText(tab1.getColumnName(0));
        txtFilter.setName(tab1.getName());
        tab1.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting() == false) {
                selectionTab1(event);
            }
        });
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1)));
        windowsTree.getSelectionModel().addTreeSelectionListener(tse -> selectionWin());
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false && tab2.getSelectedRow() != -1) {
                    tab4.setRowSelectionInterval(tab2.getSelectedRow(), tab2.getSelectedRow());
                    selectionTab24();
                }
            }
        });
        tab4.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false && tab4.getSelectedRow() != -1) {
                    tab2.setRowSelectionInterval(tab4.getSelectedRow(), tab4.getSelectedRow());
                    selectionTab24();
                }
            }
        });
        DefaultTreeModel model = (DefaultTreeModel) windowsTree.getModel();
        ((DefaultMutableTreeNode) model.getRoot()).removeAllChildren();
        model.reload();
        txt3.setEditable(false);
        txt3.setBackground(new java.awt.Color(255, 255, 255));
    }
}
