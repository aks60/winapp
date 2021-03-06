package startup;

import frames.FrameToFile;
import frames.swing.listener.ListenerFrame;
import common.eProfile;
import common.eProperty;
import convert.Profstroy;
import dataset.Conn;
import dataset.Field;
import dataset.Query;
import static dataset.Query.connection;
import dataset.eExcep;
import frames.PathToDb;
import frames.Uti5;
import frames.swing.FilterFile;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Adm extends javax.swing.JFrame {

    private Thread thread = null;
    private Queue<Object[]> listQue = new ConcurrentLinkedQueue<Object[]>();
    private ListenerFrame listenerMenu;
    private HashMap<String, JCheckBoxMenuItem> hmLookAndFill = new HashMap();
    javax.swing.Timer timer = new Timer(100, new ActionListener() {

        public void actionPerformed(ActionEvent ev) {
            if (listQue.isEmpty()) {
                Thread.yield();
            } else {
                clearListQue();
            }
        }
    });

    public Adm() {
        initComponents();
        initElements();
        //loadingModel();
    }

    private void mnLookAndFeel(java.awt.event.ActionEvent evt) {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if (((JCheckBoxMenuItem) evt.getSource()).getText().equals(laf.getName()) == true) {
                eProperty.lookandfeel.write(laf.getName());
                eProperty.save();
            }
        }
    }

    private void loadingPath() {
        
        if (eProperty.base_num.read().equals("1")) {
            edPath.setText("D:\\Okna\\Database\\ps4\\ITEST.FDB");
            labPath2.setText(eProperty.server1.read() + "/" + eProperty.port1.read() + "\\" + eProperty.base1.read());
            
        } else if (eProperty.base_num.read().equals("2")) {
            edPath.setText("D:\\Okna\\Database\\ps3\\sial3.fdb");
            labPath2.setText(eProperty.server2.read() + "/" + eProperty.port2.read() + "\\" + eProperty.base2.read());
            
        } else if (eProperty.base_num.read().equals("3")) {
            //edPath.setText("D:\\Okna\\Database\\ps4\\krauss.fdb");
            edPath.setText("D:\\Okna\\Database\\ps4\\vidnal.fdb");
            labPath2.setText(eProperty.server3.read() + "/" + eProperty.port3.read() + "\\" + eProperty.base3.read());
            
        }

        edPort.setText((eProperty.base_num.read().equals("2")) ? "3055" : "3050");
        edServer.setText("localhost");
        edUser.setText("sysdba");
        edPass.setText("masterkey");
    }

    private void loadingTab2() {

        DefaultTableModel dm = (DefaultTableModel) tab2.getModel();
        dm.getDataVector().clear();
        int npp = 0;
        for (Field up : App.db) {
            List rec = Arrays.asList(++npp, up.tname(), up.meta().descr());
            Vector vec = new Vector(rec);
            dm.getDataVector().add(vec);
        }
        ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
        Uti5.setSelectedRow(tab2);
    }

    private void loadingTab3() {
        try {
            int row = tab2.getSelectedRow();
            Field fieldUp = App.db[row];
            Query qTable = new Query(fieldUp.fields()).select(fieldUp);

            String[] columnArr = new String[fieldUp.fields().length - 1];
            for (int k = 1; k < fieldUp.fields().length; k++) {
                columnArr[k - 1] = fieldUp.fields()[k].name();
            }
            Object dataArr[][] = new Object[qTable.size()][fieldUp.fields().length - 1];
            for (int i = 0; i < qTable.size(); ++i) {
                for (int k = 1; k < fieldUp.fields().length; ++k) {
                    dataArr[i][k - 1] = qTable.get(i).get(k);
                }
            }
            tab3.setModel(new DefaultTableModel(dataArr, columnArr) {
                public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                    super.setValueAt(aValue, rowIndex, columnIndex);
                    qTable.get(rowIndex).set(columnIndex + 1, aValue);
                    qTable.update(qTable.get(rowIndex));
                }
            });

        } catch (Exception e) {
            System.err.println("Adm.loadingTab3() " + e);
        }
    }

    private void loadingTab4() {
        try {
            DefaultTableModel dm = (DefaultTableModel) tab4.getModel();
            dm.getDataVector().clear();
            String sql = "SELECT DISTINCT a.rdb$role_name , b.rdb$user FROM rdb$roles a, "
                    + "rdb$user_privileges b WHERE a.rdb$role_name = b.rdb$relation_name AND  "
                    + "a.rdb$role_name != 'DEFROLE' AND b.rdb$user != 'SYSDBA' AND NOT EXISTS "
                    + "(SELECT * FROM rdb$roles c WHERE c.rdb$role_name = b.rdb$user)";
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(sql);
            int npp = 0;
            while (rs.next()) {
                Object role = ("TEXNOLOG_RW".equals(rs.getString(1).trim()) || "MANAGER_RW".equals(rs.getString(1).trim())) ? "чтение-запись" : "только чтение";
                Object profile = ("TEXNOLOG_RW".equals(rs.getString(1).trim()) || "TEXNOLOG_RO".equals(rs.getString(1).trim())) ? "Технолог" : "Менеджер";
                List rec = Arrays.asList(++npp, rs.getObject(2), role, profile);
                Vector vec = new Vector(rec);
                dm.getDataVector().add(vec);
            }
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Uti5.setSelectedRow(tab4);

        } catch (Exception e) {
            System.out.println("Ошибка: Adm.loadingTab4() " + e);
        }
    }

    private void clearListQue() {

        if (listQue.isEmpty() == false) {
            for (int i = 0; i < listQue.size(); ++i) {
                Object obj[] = listQue.poll();
                if (obj.length == 2) {
                    if (obj[0] instanceof Color && obj[1] instanceof String) {
                        appendToPane(obj[1].toString() + "\n", (Color) obj[0]);
                    }
                } else {
                    if (obj[0] instanceof Color && obj[1] instanceof String && obj[2] instanceof Color && obj[3] instanceof String) {
                        appendToPane(obj[1].toString(), (Color) obj[0]);
                        appendToPane(obj[3].toString() + "\n", (Color) obj[2]);
                    }
                }
            }
        }
    }

    private void appendToPane(String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = txtPane.getDocument().getLength();
        txtPane.setCaretPosition(len);
        txtPane.setCharacterAttributes(aset, false);
        txtPane.replaceSelection(msg);
    }

    private void connectBaseNumb(String num_base) {
        PathToDb frame = new PathToDb(this, num_base);
        FrameToFile.setFrameSize(frame);
        frame.setVisible(true);

        if (eProperty.base_num.read().equals("1")) {
            btnT7.setSelected(true);
            mn631.setSelected(true);

        } else if (eProperty.base_num.read().equals("2")) {
            btnT8.setSelected(true);
            mn632.setSelected(true);

        } else if (eProperty.base_num.read().equals("3")) {
            btnT9.setSelected(true);
            mn633.setSelected(true);
        }
        loadingPath();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonLookAndFiilGroup = new javax.swing.ButtonGroup();
        buttonBaseGroup1 = new javax.swing.ButtonGroup();
        buttonBaseGroup2 = new javax.swing.ButtonGroup();
        ppmMain = new javax.swing.JPopupMenu();
        mn20 = new javax.swing.JMenuItem();
        mn10 = new javax.swing.JMenuItem();
        mn40 = new javax.swing.JMenuItem();
        sep2 = new javax.swing.JPopupMenu.Separator();
        mn63 = new javax.swing.JMenu();
        mn631 = new javax.swing.JCheckBoxMenuItem();
        mn632 = new javax.swing.JCheckBoxMenuItem();
        mn633 = new javax.swing.JCheckBoxMenuItem();
        mn62 = new javax.swing.JMenu();
        sep1 = new javax.swing.JPopupMenu.Separator();
        mn30 = new javax.swing.JMenuItem();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnT7 = new javax.swing.JToggleButton();
        btnT8 = new javax.swing.JToggleButton();
        btnT9 = new javax.swing.JToggleButton();
        btnMenu = new javax.swing.JButton();
        btnConv = new javax.swing.JButton();
        btnBaseEdit = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        pan8 = new javax.swing.JPanel();
        pan5 = new javax.swing.JPanel();
        pan6 = new javax.swing.JPanel();
        lab1 = new javax.swing.JLabel();
        lab2 = new javax.swing.JLabel();
        lab3 = new javax.swing.JLabel();
        lab4 = new javax.swing.JLabel();
        edServer = new javax.swing.JTextField();
        edPath = new javax.swing.JTextField();
        edUser = new javax.swing.JTextField();
        lab5 = new javax.swing.JLabel();
        edPort = new javax.swing.JTextField();
        edPass = new javax.swing.JTextField();
        labPath2 = new javax.swing.JLabel();
        lab6 = new javax.swing.JLabel();
        btn10 = new javax.swing.JButton();
        btnTest = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        pn7 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        pan4 = new javax.swing.JPanel();
        txtPane = new javax.swing.JTextPane();
        pan2 = new javax.swing.JPanel();
        pan9 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        pan10 = new javax.swing.JPanel();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan3 = new javax.swing.JPanel();
        pan11 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        pan14 = new javax.swing.JPanel();
        btnIns = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnSysdba = new javax.swing.JButton();
        pan13 = new javax.swing.JPanel();
        pan12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        box1 = new javax.swing.JComboBox<>();
        txt1 = new javax.swing.JTextField();
        box2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        txt2 = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        south = new javax.swing.JPanel();

        ppmMain.setFont(frames.Uti5.getFont(1,1));

        mn20.setFont(frames.Uti5.getFont(1,1));
        mn20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b059.gif"))); // NOI18N
        mn20.setActionCommand("sa-okna <= ПрофСтрой(3,4)");
        mn20.setLabel("БД <= ПрофСтрой(3,4)");
        mn20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCard(evt);
            }
        });
        ppmMain.add(mn20);

        mn10.setFont(frames.Uti5.getFont(1,1));
        mn10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b051.gif"))); // NOI18N
        mn10.setText("Правка БД");
        mn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCard(evt);
            }
        });
        ppmMain.add(mn10);

        mn40.setFont(frames.Uti5.getFont(1,1));
        mn40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b032.gif"))); // NOI18N
        mn40.setText("Пользователи БД");
        mn40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCard(evt);
            }
        });
        ppmMain.add(mn40);
        ppmMain.add(sep2);

        mn63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b028.gif"))); // NOI18N
        mn63.setText("База данных");
        mn63.setFont(frames.Uti5.getFont(1,1));

        buttonBaseGroup1.add(mn631);
        mn631.setFont(frames.Uti5.getFont(1,1));
        mn631.setText("База 1");
        mn631.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn631);

        buttonBaseGroup1.add(mn632);
        mn632.setFont(frames.Uti5.getFont(1,1));
        mn632.setText("База 2");
        mn632.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn632);

        buttonBaseGroup1.add(mn633);
        mn633.setFont(frames.Uti5.getFont(1,1));
        mn633.setText("База 3");
        mn633.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn633);

        ppmMain.add(mn63);

        mn62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b061.gif"))); // NOI18N
        mn62.setText("Вид интерфейса");
        mn62.setFont(frames.Uti5.getFont(1,1));
        ppmMain.add(mn62);
        ppmMain.add(sep1);

        mn30.setFont(frames.Uti5.getFont(1,1));
        mn30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b009.gif"))); // NOI18N
        mn30.setText("Выход");
        mn30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn30mnExit(evt);
            }
        });
        ppmMain.add(mn30);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(900, 503));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Adm.this.windowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Adm.this.windowClosing(evt);
            }
        });

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
                mnExit(evt);
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

        buttonBaseGroup2.add(btnT7);
        btnT7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c057.gif"))); // NOI18N
        btnT7.setSelected(true);
        btnT7.setFocusable(false);
        btnT7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnT7.setMaximumSize(new java.awt.Dimension(28, 25));
        btnT7.setMinimumSize(new java.awt.Dimension(28, 25));
        btnT7.setPreferredSize(new java.awt.Dimension(28, 25));
        btnT7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnT7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBase(evt);
            }
        });

        buttonBaseGroup2.add(btnT8);
        btnT8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c058.gif"))); // NOI18N
        btnT8.setFocusable(false);
        btnT8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnT8.setMaximumSize(new java.awt.Dimension(28, 25));
        btnT8.setMinimumSize(new java.awt.Dimension(28, 25));
        btnT8.setPreferredSize(new java.awt.Dimension(28, 25));
        btnT8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnT8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBase(evt);
            }
        });

        buttonBaseGroup2.add(btnT9);
        btnT9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c059.gif"))); // NOI18N
        btnT9.setFocusable(false);
        btnT9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnT9.setMaximumSize(new java.awt.Dimension(28, 25));
        btnT9.setMinimumSize(new java.awt.Dimension(28, 25));
        btnT9.setPreferredSize(new java.awt.Dimension(28, 25));
        btnT9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnT9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBase(evt);
            }
        });

        btnMenu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c032.gif"))); // NOI18N
        btnMenu.setText("Гл. меню");
        btnMenu.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnMenu.setFocusable(false);
        btnMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMenu.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMenu.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMenu.setPreferredSize(new java.awt.Dimension(96, 25));
        btnMenu.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu(evt);
            }
        });

        btnConv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c070.gif"))); // NOI18N
        btnConv.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnConv.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnConv.setFocusable(false);
        btnConv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConv.setMaximumSize(new java.awt.Dimension(25, 25));
        btnConv.setMinimumSize(new java.awt.Dimension(25, 25));
        btnConv.setPreferredSize(new java.awt.Dimension(25, 25));
        btnConv.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnConv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard(evt);
            }
        });

        btnBaseEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c068.gif"))); // NOI18N
        btnBaseEdit.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnBaseEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnBaseEdit.setFocusable(false);
        btnBaseEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBaseEdit.setMaximumSize(new java.awt.Dimension(25, 25));
        btnBaseEdit.setMinimumSize(new java.awt.Dimension(25, 25));
        btnBaseEdit.setPreferredSize(new java.awt.Dimension(25, 25));
        btnBaseEdit.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnBaseEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBaseEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard(evt);
            }
        });

        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c069.gif"))); // NOI18N
        btnLogin.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnLogin.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnLogin.setFocusable(false);
        btnLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogin.setMaximumSize(new java.awt.Dimension(25, 25));
        btnLogin.setMinimumSize(new java.awt.Dimension(25, 25));
        btnLogin.setPreferredSize(new java.awt.Dimension(25, 25));
        btnLogin.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnLogin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBaseEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(btnT7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnT9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 392, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, northLayout.createSequentialGroup()
                        .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBaseEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        center.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        center.setPreferredSize(new java.awt.Dimension(865, 500));
        center.setLayout(new java.awt.CardLayout());

        pan8.setPreferredSize(new java.awt.Dimension(861, 500));

        javax.swing.GroupLayout pan8Layout = new javax.swing.GroupLayout(pan8);
        pan8.setLayout(pan8Layout);
        pan8Layout.setHorizontalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 845, Short.MAX_VALUE)
        );
        pan8Layout.setVerticalGroup(
            pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
        );

        center.add(pan8, "pan8");

        pan5.setPreferredSize(new java.awt.Dimension(500, 500));
        pan5.setLayout(new java.awt.BorderLayout());

        pan6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan6.setPreferredSize(new java.awt.Dimension(500, 72));

        lab1.setFont(frames.Uti5.getFont(0,0));
        lab1.setText("Cервер (host)");
        lab1.setAlignmentX(0.5F);
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab1.setMaximumSize(new java.awt.Dimension(100, 18));
        lab1.setMinimumSize(new java.awt.Dimension(0, 18));
        lab1.setPreferredSize(new java.awt.Dimension(80, 18));

        lab2.setFont(frames.Uti5.getFont(0,0));
        lab2.setText("База источник");
        lab2.setAlignmentX(0.5F);
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab2.setMinimumSize(new java.awt.Dimension(100, 18));
        lab2.setPreferredSize(new java.awt.Dimension(84, 18));

        lab3.setFont(frames.Uti5.getFont(0,0));
        lab3.setText("Пользователь");
        lab3.setAlignmentX(0.5F);
        lab3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab3.setMaximumSize(new java.awt.Dimension(100, 18));
        lab3.setMinimumSize(new java.awt.Dimension(0, 18));
        lab3.setPreferredSize(new java.awt.Dimension(80, 18));

        lab4.setFont(frames.Uti5.getFont(0,0));
        lab4.setText("Пароль");
        lab4.setAlignmentX(0.5F);
        lab4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab4.setPreferredSize(new java.awt.Dimension(46, 18));

        edServer.setFont(frames.Uti5.getFont(0,0));
        edServer.setText("localhost");
        edServer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edServer.setMinimumSize(new java.awt.Dimension(0, 0));
        edServer.setPreferredSize(new java.awt.Dimension(72, 18));

        edPath.setFont(frames.Uti5.getFont(0,0));
        edPath.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        edPath.setMinimumSize(new java.awt.Dimension(0, 0));
        edPath.setPreferredSize(new java.awt.Dimension(200, 18));

        edUser.setFont(frames.Uti5.getFont(0,0));
        edUser.setText("sysdba");
        edUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edUser.setFocusable(false);
        edUser.setMinimumSize(new java.awt.Dimension(0, 0));
        edUser.setPreferredSize(new java.awt.Dimension(72, 18));

        lab5.setFont(frames.Uti5.getFont(0,0));
        lab5.setText("Порт");
        lab5.setAlignmentX(0.5F);
        lab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab5.setMaximumSize(new java.awt.Dimension(40, 18));
        lab5.setMinimumSize(new java.awt.Dimension(40, 18));
        lab5.setPreferredSize(new java.awt.Dimension(46, 18));

        edPort.setFont(frames.Uti5.getFont(0,0));
        edPort.setText("3050");
        edPort.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPort.setMinimumSize(new java.awt.Dimension(0, 0));
        edPort.setPreferredSize(new java.awt.Dimension(72, 18));

        edPass.setFont(frames.Uti5.getFont(0,0));
        edPass.setText("masterkey");
        edPass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPass.setMinimumSize(new java.awt.Dimension(0, 0));
        edPass.setPreferredSize(new java.awt.Dimension(72, 18));

        labPath2.setBackground(new java.awt.Color(255, 255, 255));
        labPath2.setFont(frames.Uti5.getFont(0,0));
        labPath2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labPath2.setFocusable(false);
        labPath2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        labPath2.setOpaque(true);
        labPath2.setPreferredSize(new java.awt.Dimension(200, 18));

        lab6.setFont(frames.Uti5.getFont(0,0));
        lab6.setText("База приемник");
        lab6.setAlignmentX(0.5F);
        lab6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab6.setPreferredSize(new java.awt.Dimension(84, 18));

        btn10.setText("...");
        btn10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn10.setMaximumSize(new java.awt.Dimension(18, 18));
        btn10.setMinimumSize(new java.awt.Dimension(18, 18));
        btn10.setName("btnField17"); // NOI18N
        btn10.setPreferredSize(new java.awt.Dimension(18, 18));
        btn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn10btnAction(evt);
            }
        });

        btnTest.setFont(frames.Uti5.getFont(0,0));
        btnTest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b028.gif"))); // NOI18N
        btnTest.setText("Тест");
        btnTest.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTest.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btnTest.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTest.setMinimumSize(new java.awt.Dimension(0, 0));
        btnTest.setPreferredSize(new java.awt.Dimension(54, 25));
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestBtnStartClick(evt);
            }
        });

        btnStart.setFont(frames.Uti5.getFont(0,0));
        btnStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b059.gif"))); // NOI18N
        btnStart.setText("Конвертировать");
        btnStart.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnStart.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnStart.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btnStart.setMaximumSize(new java.awt.Dimension(120, 25));
        btnStart.setMinimumSize(new java.awt.Dimension(0, 0));
        btnStart.setPreferredSize(new java.awt.Dimension(80, 25));
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStart(evt);
            }
        });

        javax.swing.GroupLayout pan6Layout = new javax.swing.GroupLayout(pan6);
        pan6.setLayout(pan6Layout);
        pan6Layout.setHorizontalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lab1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPath, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan6Layout.createSequentialGroup()
                        .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labPath2, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pan6Layout.setVerticalGroup(
            pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(pan6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labPath2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pan5.add(pan6, java.awt.BorderLayout.PAGE_START);

        pn7.setPreferredSize(new java.awt.Dimension(20, 20));
        pn7.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        pan4.setLayout(new java.awt.BorderLayout());

        txtPane.setBackground(new java.awt.Color(232, 233, 236));
        txtPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 6, 1, 1));
        pan4.add(txtPane, java.awt.BorderLayout.CENTER);

        scr1.setViewportView(pan4);

        pn7.add(scr1, java.awt.BorderLayout.CENTER);

        pan5.add(pn7, java.awt.BorderLayout.CENTER);

        center.add(pan5, "pan5");

        pan2.setPreferredSize(new java.awt.Dimension(654, 504));
        pan2.setLayout(new java.awt.BorderLayout());

        pan9.setPreferredSize(new java.awt.Dimension(360, 500));
        pan9.setLayout(new java.awt.BorderLayout());

        scr2.setPreferredSize(new java.awt.Dimension(360, 404));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "№пп", "Таблица", "Описание"
            }
        ));
        tab2.setFillsViewportHeight(true);
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setMaxWidth(40);
            tab2.getColumnModel().getColumn(1).setMaxWidth(80);
        }

        pan9.add(scr2, java.awt.BorderLayout.CENTER);

        pan2.add(pan9, java.awt.BorderLayout.WEST);

        pan10.setPreferredSize(new java.awt.Dimension(400, 500));
        pan10.setLayout(new java.awt.BorderLayout());

        scr3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

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
        tab3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr3.setViewportView(tab3);

        pan10.add(scr3, java.awt.BorderLayout.CENTER);

        pan2.add(pan10, java.awt.BorderLayout.CENTER);

        center.add(pan2, "pan2");

        pan3.setLayout(new java.awt.CardLayout());

        pan11.setPreferredSize(new java.awt.Dimension(348, 496));
        pan11.setRequestFocusEnabled(false);
        pan11.setLayout(new java.awt.BorderLayout());

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "№пп", "Пользователь", "Права доступа", "Профиль"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab4.setFillsViewportHeight(true);
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        pan11.add(scr4, java.awt.BorderLayout.CENTER);

        pan14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pan14.setPreferredSize(new java.awt.Dimension(548, 29));

        btnIns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b042.gif"))); // NOI18N
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
                addUser(evt);
            }
        });

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b044.gif"))); // NOI18N
        btnUp.setToolTipText(bundle.getString("Удалить")); // NOI18N
        btnUp.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnUp.setFocusable(false);
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setMaximumSize(new java.awt.Dimension(25, 25));
        btnUp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnUp.setPreferredSize(new java.awt.Dimension(25, 25));
        btnUp.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userUpdate(evt);
            }
        });

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b043.gif"))); // NOI18N
        btnDel.setToolTipText(bundle.getString("Обновить")); // NOI18N
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
                userDel(evt);
            }
        });

        btnSysdba.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c071.gif"))); // NOI18N
        btnSysdba.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnSysdba.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSysdba.setFocusable(false);
        btnSysdba.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSysdba.setMaximumSize(new java.awt.Dimension(25, 25));
        btnSysdba.setMinimumSize(new java.awt.Dimension(25, 25));
        btnSysdba.setPreferredSize(new java.awt.Dimension(25, 25));
        btnSysdba.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnSysdba.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSysdba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSysdba(evt);
            }
        });

        javax.swing.GroupLayout pan14Layout = new javax.swing.GroupLayout(pan14);
        pan14.setLayout(pan14Layout);
        pan14Layout.setHorizontalGroup(
            pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnSysdba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(671, Short.MAX_VALUE))
        );
        pan14Layout.setVerticalGroup(
            pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnIns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSysdba, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pan11.add(pan14, java.awt.BorderLayout.PAGE_START);

        pan3.add(pan11, "pan11");

        pan13.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Профиль");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel1.setPreferredSize(new java.awt.Dimension(140, 18));

        jLabel2.setText("Пользователь  (english)");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setPreferredSize(new java.awt.Dimension(140, 18));

        jLabel3.setText("Права");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel3.setPreferredSize(new java.awt.Dimension(140, 18));

        jLabel4.setText("Пароль  (english)");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel4.setPreferredSize(new java.awt.Dimension(140, 18));

        box1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Технолог", "Менеджер" }));
        box1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        box1.setPreferredSize(new java.awt.Dimension(140, 18));

        txt1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt1.setPreferredSize(new java.awt.Dimension(140, 18));

        box2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "чтение-запись", "только запись", " " }));
        box2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        box2.setPreferredSize(new java.awt.Dimension(140, 18));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b044.gif"))); // NOI18N
        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(140, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminOk(evt);
            }
        });

        txt2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt2.setPreferredSize(new java.awt.Dimension(140, 20));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b044.gif"))); // NOI18N
        jButton2.setText("Отмена");
        jButton2.setPreferredSize(new java.awt.Dimension(140, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                admCancel(evt);
            }
        });

        javax.swing.GroupLayout pan12Layout = new javax.swing.GroupLayout(pan12);
        pan12.setLayout(pan12Layout);
        pan12Layout.setHorizontalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan12Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pan12Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(box2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan12Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan12Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan12Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan12Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(519, Short.MAX_VALUE))
        );
        pan12Layout.setVerticalGroup(
            pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan12Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(box1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(box2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(294, Short.MAX_VALUE))
        );

        pan13.add(pan12, java.awt.BorderLayout.CENTER);

        pan3.add(pan13, "pan13");

        center.add(pan3, "pan3");

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 845, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnExit
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.dispose());
    }//GEN-LAST:event_mnExit

    private void userDel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userDel
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить текущего пользователя?", "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
            int row = tab4.getSelectedRow();
            if (row != -1) {
                String user = String.valueOf(tab4.getValueAt(row, 1));
                Conn.instanc().deleteUser(user);
                loadingTab4();
            }
        }
    }//GEN-LAST:event_userDel

    private void userUpdate(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userUpdate
        int row = tab4.getSelectedRow();
        if (row != -1) {
            box1.setEnabled(false);
            box2.setEnabled(false);
            txt1.setEditable(false);

            int index1 = ("Технолог".equals(tab4.getValueAt(row, 3))) ? 1 : 2;
            box1.setSelectedIndex(index1);
            int index2 = ("чтение-запись".equals(tab4.getValueAt(row, 2))) ? 0 : 1;
            box2.setSelectedIndex(index2);
            String user = String.valueOf(tab4.getValueAt(row, 1));
            txt1.setText(user);
            ((CardLayout) pan3.getLayout()).show(pan3, "pan13");
        }
    }//GEN-LAST:event_userUpdate

    private void addUser(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUser
        box1.setEnabled(true);
        box1.setSelectedIndex(0);
        box2.setEnabled(true);
        box2.setSelectedIndex(0);
        txt1.setEditable(true);
        txt1.setText("");
        ((CardLayout) pan3.getLayout()).show(pan3, "pan13");
    }//GEN-LAST:event_addUser

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport
        try {
            Conn.instanc().getConnection().createStatement().executeUpdate("create user AKS2 password '1'");
            Conn.instanc().getConnection().createStatement().executeUpdate("grant DEFROLE to AKS2");
            Conn.instanc().getConnection().createStatement().executeUpdate("grant TEXNOLOG_RW to AKS2");

        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnReport

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        timer.stop();
        if (thread != null)
            thread.stop();
    }//GEN-LAST:event_windowClosed

    private void windowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosing
        mnExit(null);
    }//GEN-LAST:event_windowClosing

    private void btnBase(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBase
        String num_base = (btnT7.isSelected()) ? "1" : (btnT8.isSelected()) ? "2" : "3";
        connectBaseNumb(num_base);
    }//GEN-LAST:event_btnBase

    private void btnMenu(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu
        ppmMain.show(btnMenu, center.getX(), center.getY());
    }//GEN-LAST:event_btnMenu

    private void btnCard(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCard
        JButton button = (JButton) evt.getSource();
        if (button == btnConv) {
            ((CardLayout) center.getLayout()).show(center, "pan5");
            timer.start();
            loadingPath();

        } else if (button == btnBaseEdit) {
            ((CardLayout) center.getLayout()).show(center, "pan2");
            loadingTab2();

        } else if (button == btnLogin) {
            ((CardLayout) center.getLayout()).show(center, "pan3");
            loadingTab4();
        }
    }//GEN-LAST:event_btnCard

    private void btn10btnAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn10btnAction
        FilterFile filter = new FilterFile();
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(filter);
        int result = chooser.showDialog(this, "Выбрать");
        if (result == JFileChooser.APPROVE_OPTION) {
            edPath.setText(chooser.getSelectedFile().getPath());
        }        
    }//GEN-LAST:event_btn10btnAction

    private void btnStart(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStart
        try {
            Query.listOpenTable.clear();
            eProperty.user.write("sysdba");
            eProperty.password = String.valueOf("masterkey");
            String num_base = eProperty.base_num.read();
            Conn con2 = Conn.initConnect();
            con2.createConnection(eProperty.server(num_base), eProperty.port(num_base), eProperty.base(num_base), eProperty.user.read(), eProperty.password.toCharArray(), null);
            Connection c2 = con2.getConnection();

            Conn con1 = new Conn();
            con1.createConnection(edServer.getText().trim(), edPort.getText().trim(), edPath.getText().trim(), edUser.getText().trim(), edPass.getText().toCharArray(), null);
            Connection c1 = con1.getConnection();

            txtPane.setText("");
            thread = new Thread(new Runnable() {
                public void run() {
                    Profstroy.exec(listQue, c1, c2);
                }

            });
            thread.start();

        } catch (Exception e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_btnStart

    private void btnTestBtnStartClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestBtnStartClick
        Conn Src = new Conn();
        eExcep excep = Src.createConnection(edServer.getText().trim(), edPort.getText().trim(),
                edPath.getText().trim(), edUser.getText().trim(), edPass.getText().toCharArray(), null);
        JOptionPane.showMessageDialog(this, edPath.getText().trim() + "  \n" + excep.mes, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnTestBtnStartClick

    private void mn30mnExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn30mnExit
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.dispose());
    }//GEN-LAST:event_mn30mnExit

    private void mnBase(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnBase
        String num_base = (mn631.isSelected()) ? "1" : (mn632.isSelected()) ? "2" : "3";
        connectBaseNumb(num_base);
    }//GEN-LAST:event_mnBase

    private void mnCard(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCard
        JMenuItem button = (JMenuItem) evt.getSource();
        if (button == mn20) {
            ((CardLayout) center.getLayout()).show(center, "pan5");
            timer.start();
            loadingPath();

        } else if (button == mn10) {
            ((CardLayout) center.getLayout()).show(center, "pan2");
            loadingTab2();

        } else if (button == mn40) {
            ((CardLayout) center.getLayout()).show(center, "pan3");
            loadingTab4();
        }
    }//GEN-LAST:event_mnCard

    private void adminOk(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminOk

        if (txt1.isEditable() == true) {

            if (box1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Укажите профиль пользователя", "Предупреждение", JOptionPane.NO_OPTION);
            } else if (txt1.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Укажите имя пользователя", "Предупреждение", JOptionPane.NO_OPTION);
            } else if (txt2.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Укажите пароль пользователя", "Предупреждение", JOptionPane.NO_OPTION);
            } else {
                for (char c : txt1.getText().toCharArray()) {
                    if (((c >= 'а') && (c <= 'я')) || ((c >= 'А') && (c <= 'Я'))) {
                        JOptionPane.showMessageDialog(this, "В имени пользователя есть символы  принадлежащие русскому алфавиту", "Предупреждение", JOptionPane.NO_OPTION);
                        return;
                    }
                }
                for (char c : txt2.getPassword()) {
                    if (((c >= 'а') && (c <= 'я')) || ((c >= 'А') && (c <= 'Я'))) {
                        JOptionPane.showMessageDialog(this, "В пароле есть символы принадлежащие русскому алфавиту", "Предупреждение", JOptionPane.NO_OPTION);
                        return;
                    }
                }
                String role = (box1.getSelectedIndex() == 1) ? "TEXNOLOG" : "MANAGER";
                role = (box2.getSelectedIndex() == 0) ? role + "_RW" : role + "_RO";
                Conn.instanc().addUser(txt1.getText().trim(), txt2.getPassword(), role);
                loadingTab4();
            }
        } else {
            Conn.instanc().modifyPassword(txt1.getText().trim(), txt2.getPassword());
        }
        ((CardLayout) pan3.getLayout()).show(pan3, "pan11");
    }//GEN-LAST:event_adminOk

    private void admCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_admCancel
        ((CardLayout) pan3.getLayout()).show(pan3, "pan11");
    }//GEN-LAST:event_admCancel

    private void btnSysdba(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSysdba

        JTextField pass1 = new JPasswordField();
        JPasswordField pass2 = new JPasswordField();
        Object[] ob = {"Изменение пароля SYSDBA", pass1, "Подтвердите новый пароль", pass2};
        int result = JOptionPane.showConfirmDialog(null, ob, "Изменение пароля", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {            
            if (pass1.getText().equals(pass2.getText()))  {
                
                Conn.instanc().modifyPassword("sysdba", pass2.getPassword());
                JOptionPane.showMessageDialog(this, "Операция выполнена успешно!", "Изменение паспорта SYSDBA", JOptionPane.NO_OPTION);
            } else {
                JOptionPane.showMessageDialog(this, "Имена не совпали", "Неудача", JOptionPane.NO_OPTION);
            }
        }

    }//GEN-LAST:event_btnSysdba

// <editor-fold defaultstate="collapsed" desc="Generated Code">    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box1;
    private javax.swing.JComboBox<String> box2;
    private javax.swing.JButton btn10;
    private javax.swing.JButton btnBaseEdit;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConv;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnSysdba;
    private javax.swing.JToggleButton btnT7;
    private javax.swing.JToggleButton btnT8;
    private javax.swing.JToggleButton btnT9;
    private javax.swing.JButton btnTest;
    private javax.swing.JButton btnUp;
    private javax.swing.ButtonGroup buttonBaseGroup1;
    private javax.swing.ButtonGroup buttonBaseGroup2;
    private javax.swing.ButtonGroup buttonLookAndFiilGroup;
    private javax.swing.JPanel center;
    private javax.swing.JTextField edPass;
    private javax.swing.JTextField edPath;
    private javax.swing.JTextField edPort;
    private javax.swing.JTextField edServer;
    private javax.swing.JTextField edUser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab3;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab5;
    private javax.swing.JLabel lab6;
    private javax.swing.JLabel labPath2;
    private javax.swing.JMenuItem mn10;
    private javax.swing.JMenuItem mn20;
    private javax.swing.JMenuItem mn30;
    private javax.swing.JMenuItem mn40;
    private javax.swing.JMenu mn62;
    private javax.swing.JMenu mn63;
    private javax.swing.JCheckBoxMenuItem mn631;
    private javax.swing.JCheckBoxMenuItem mn632;
    private javax.swing.JCheckBoxMenuItem mn633;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan10;
    private javax.swing.JPanel pan11;
    private javax.swing.JPanel pan12;
    private javax.swing.JPanel pan13;
    private javax.swing.JPanel pan14;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JPanel pan6;
    private javax.swing.JPanel pan8;
    private javax.swing.JPanel pan9;
    private javax.swing.JPanel pn7;
    private javax.swing.JPopupMenu ppmMain;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JPopupMenu.Separator sep1;
    private javax.swing.JPopupMenu.Separator sep2;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTextField txt1;
    private javax.swing.JPasswordField txt2;
    private javax.swing.JTextPane txtPane;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {
        setTitle(eProfile.profile.title);
        new FrameToFile(this, btnClose);
        appendToPane("\n", Color.GRAY);
        appendToPane("    Внимание!!! Перенос данных из ПрофСтрой-3 должен\n", Color.GRAY);
        appendToPane("    выполняться под управлением Firebird 2.1 НЕ ВЫШЕ.\n", Color.GRAY);
        appendToPane("    Если версия выше чем 2.1 переустановите Firebird.\n", Color.GRAY);
        appendToPane("\n", Color.GRAY);
        appendToPane("    PS. У Вас установлена версия Firebird " + Conn.instanc().version() + "\n", Color.GRAY);

        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            JCheckBoxMenuItem mnIt = new javax.swing.JCheckBoxMenuItem();
            buttonLookAndFiilGroup.add(mnIt);
            hmLookAndFill.put(laf.getName(), mnIt);
            mn62.add(mnIt);
            mnIt.setFont(frames.Uti5.getFont(1, 1));
            mnIt.setText(laf.getName());
            mnIt.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    mnLookAndFeel(evt);
                }
            });
            if (lookAndFeel.getName().equals(laf.getName())) {
                mnIt.setSelected(true);
            }
        }
        if (eProperty.base_num.read().equals("1")) {
            btnT7.setSelected(true);
            mn631.setSelected(true);

        } else if (eProperty.base_num.read().equals("2")) {
            btnT8.setSelected(true);
            mn632.setSelected(true);

        } else if (eProperty.base_num.read().equals("3")) {
            btnT9.setSelected(true);
            mn633.setSelected(true);
        }
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    loadingTab3();
                }
            }
        });
    }
}
