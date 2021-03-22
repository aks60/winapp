package startup;

import common.FrameToFile;
import common.ListenerFrame;
import common.eProfile;
import common.eProperty;
import convert.Profstroy;
import dataset.Confb;
import dataset.Field;
import dataset.Query;
import dataset.eExcep;
import frames.PathToDb;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Adm extends javax.swing.JFrame {

    private Locale locale;
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
        loadingModel();

        locale = this.getLocale();
        Locale loc = new Locale("ru", "RU");
        this.setLocale(loc);
        this.getInputContext().selectInputMethod(loc);
    }

    private void mnLookAndFeel(java.awt.event.ActionEvent evt) {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if (((JCheckBoxMenuItem) evt.getSource()).getText().equals(laf.getName()) == true) {
                eProperty.lookandfeel.write(laf.getName());
                eProperty.save();
            }
        }
    }

    private void loadingModel() {
        if (eProperty.base_num.read().equals("1")) {
            labPath2.setText(eProperty.server1.read() + "/" + eProperty.port1.read() + "\\" + eProperty.base1.read());
            edPath.setText("D:\\Okna\\Database\\Profstroy4\\ITEST.FDB");
        } else if (eProperty.base_num.read().equals("2")) {
            labPath2.setText(eProperty.server2.read() + "/" + eProperty.port2.read() + "\\" + eProperty.base2.read());
            edPath.setText("D:\\Okna\\Database\\Sialbase3\\sial3.fdb");
        } else if (eProperty.base_num.read().equals("3")) {
            labPath2.setText(eProperty.server3.read() + "/" + eProperty.port3.read() + "\\" + eProperty.base3.read());
            edPath.setText("D:\\Okna\\Database\\Alutex3\\alutech3x.fdb");
        }
        edPort.setText((eProperty.base_num.read().equals("1")) ? "3050" : "3055");
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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonLookAndFiilGroup = new javax.swing.ButtonGroup();
        buttonBaseGroup1 = new javax.swing.ButtonGroup();
        buttonBaseGroup2 = new javax.swing.ButtonGroup();
        ppmMain = new javax.swing.JPopupMenu();
        mn10 = new javax.swing.JMenu();
        mn11 = new javax.swing.JMenuItem();
        mn12 = new javax.swing.JMenuItem();
        mn20 = new javax.swing.JMenuItem();
        mn63 = new javax.swing.JMenu();
        mn631 = new javax.swing.JCheckBoxMenuItem();
        mn632 = new javax.swing.JCheckBoxMenuItem();
        mn633 = new javax.swing.JCheckBoxMenuItem();
        mn62 = new javax.swing.JMenu();
        sep1 = new javax.swing.JPopupMenu.Separator();
        mn30 = new javax.swing.JMenuItem();
        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
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
        jLabel2 = new javax.swing.JLabel();
        south = new javax.swing.JPanel();

        ppmMain.setFont(frames.Util.getFont(1,1));

        mn10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn10.setText("Установки");
        mn10.setToolTipText("");
        mn10.setFont(frames.Util.getFont(1,1));

        mn11.setFont(frames.Util.getFont(1,1));
        mn11.setText("Текст ячейки");
        mn11.setName("1"); // NOI18N
        mn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn11cellValueType(evt);
            }
        });
        mn10.add(mn11);

        mn12.setFont(frames.Util.getFont(1,1));
        mn12.setText("Целое число");
        mn12.setName("2"); // NOI18N
        mn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn12cellValueType(evt);
            }
        });
        mn10.add(mn12);

        ppmMain.add(mn10);

        mn20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b059.gif"))); // NOI18N
        mn20.setText("SA-OKNA <= ПрофСтрой(3,4)");
        mn20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCard(evt);
            }
        });
        ppmMain.add(mn20);

        mn63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b028.gif"))); // NOI18N
        mn63.setText("База данных");
        mn63.setFont(frames.Util.getFont(1,1));

        buttonBaseGroup1.add(mn631);
        mn631.setFont(frames.Util.getFont(1,1));
        mn631.setText("База 1");
        mn631.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn631);

        buttonBaseGroup1.add(mn632);
        mn632.setFont(frames.Util.getFont(1,1));
        mn632.setText("База 2");
        mn632.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn632);

        buttonBaseGroup1.add(mn633);
        mn633.setFont(frames.Util.getFont(1,1));
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
        mn62.setFont(frames.Util.getFont(1,1));
        ppmMain.add(mn62);
        ppmMain.add(sep1);

        mn30.setFont(frames.Util.getFont(1,1));
        mn30.setText("Выход");
        mn30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn30mnExit(evt);
            }
        });
        ppmMain.add(mn30);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c066.gif"))); // NOI18N
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
                .addGap(49, 49, 49)
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnConv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBaseEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnT7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnT8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnT9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addGap(0, 896, Short.MAX_VALUE)
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

        lab1.setFont(frames.Util.getFont(0,0));
        lab1.setText("Cервер (host)");
        lab1.setAlignmentX(0.5F);
        lab1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab1.setMaximumSize(new java.awt.Dimension(100, 18));
        lab1.setMinimumSize(new java.awt.Dimension(0, 18));
        lab1.setPreferredSize(new java.awt.Dimension(80, 18));

        lab2.setFont(frames.Util.getFont(0,0));
        lab2.setText("Путь к базе источника");
        lab2.setAlignmentX(0.5F);
        lab2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab2.setMinimumSize(new java.awt.Dimension(100, 18));
        lab2.setPreferredSize(new java.awt.Dimension(126, 18));

        lab3.setFont(frames.Util.getFont(0,0));
        lab3.setText("Пользователь");
        lab3.setAlignmentX(0.5F);
        lab3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab3.setMaximumSize(new java.awt.Dimension(100, 18));
        lab3.setMinimumSize(new java.awt.Dimension(0, 18));
        lab3.setPreferredSize(new java.awt.Dimension(80, 18));

        lab4.setFont(frames.Util.getFont(0,0));
        lab4.setText("Пароль");
        lab4.setAlignmentX(0.5F);
        lab4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab4.setPreferredSize(new java.awt.Dimension(46, 18));

        edServer.setFont(frames.Util.getFont(0,0));
        edServer.setText("localhost");
        edServer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edServer.setMinimumSize(new java.awt.Dimension(0, 0));
        edServer.setPreferredSize(new java.awt.Dimension(80, 18));

        edPath.setFont(frames.Util.getFont(0,0));
        edPath.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        edPath.setMinimumSize(new java.awt.Dimension(0, 0));
        edPath.setPreferredSize(new java.awt.Dimension(200, 18));

        edUser.setFont(frames.Util.getFont(0,0));
        edUser.setText("sysdba");
        edUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edUser.setFocusable(false);
        edUser.setMinimumSize(new java.awt.Dimension(0, 0));
        edUser.setPreferredSize(new java.awt.Dimension(80, 18));

        lab5.setFont(frames.Util.getFont(0,0));
        lab5.setText("Порт");
        lab5.setAlignmentX(0.5F);
        lab5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab5.setMaximumSize(new java.awt.Dimension(40, 18));
        lab5.setMinimumSize(new java.awt.Dimension(40, 18));
        lab5.setPreferredSize(new java.awt.Dimension(46, 18));

        edPort.setFont(frames.Util.getFont(0,0));
        edPort.setText("3050");
        edPort.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPort.setMinimumSize(new java.awt.Dimension(0, 0));
        edPort.setPreferredSize(new java.awt.Dimension(80, 18));

        edPass.setFont(frames.Util.getFont(0,0));
        edPass.setText("masterkey");
        edPass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edPass.setMinimumSize(new java.awt.Dimension(0, 0));
        edPass.setPreferredSize(new java.awt.Dimension(80, 18));

        labPath2.setBackground(new java.awt.Color(255, 255, 255));
        labPath2.setFont(frames.Util.getFont(0,0));
        labPath2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labPath2.setFocusable(false);
        labPath2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        labPath2.setOpaque(true);
        labPath2.setPreferredSize(new java.awt.Dimension(200, 18));

        lab6.setFont(frames.Util.getFont(0,0));
        lab6.setText("Пкть к базе приемника");
        lab6.setAlignmentX(0.5F);
        lab6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lab6.setPreferredSize(new java.awt.Dimension(126, 18));

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

        btnTest.setFont(frames.Util.getFont(0,0));
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

        btnStart.setFont(frames.Util.getFont(0,0));
        btnStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b059.gif"))); // NOI18N
        btnStart.setText("Конвертировать");
        btnStart.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnStart.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnStart.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btnStart.setMaximumSize(new java.awt.Dimension(120, 25));
        btnStart.setMinimumSize(new java.awt.Dimension(0, 0));
        btnStart.setPreferredSize(new java.awt.Dimension(116, 25));
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
                        .addComponent(edPath, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan6Layout.createSequentialGroup()
                        .addComponent(lab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lab6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labPath2, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
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
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setMaxWidth(40);
            tab2.getColumnModel().getColumn(1).setMaxWidth(80);
        }

        pan9.add(scr2, java.awt.BorderLayout.CENTER);

        pan2.add(pan9, java.awt.BorderLayout.WEST);

        pan10.setPreferredSize(new java.awt.Dimension(400, 500));
        pan10.setLayout(new java.awt.BorderLayout());

        scr3.setPreferredSize(new java.awt.Dimension(400, 500));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tab3.setFillsViewportHeight(true);
        tab3.setPreferredSize(new java.awt.Dimension(400, 500));
        scr3.setViewportView(tab3);

        pan10.add(scr3, java.awt.BorderLayout.CENTER);

        pan2.add(pan10, java.awt.BorderLayout.CENTER);

        center.add(pan2, "pan2");

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout pan3Layout = new javax.swing.GroupLayout(pan3);
        pan3.setLayout(pan3Layout);
        pan3Layout.setHorizontalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(844, Short.MAX_VALUE))
        );
        pan3Layout.setVerticalGroup(
            pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel2)
                .addContainerGap(451, Short.MAX_VALUE))
        );

        center.add(pan3, "pan3");

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(900, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 896, Short.MAX_VALUE)
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

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh

    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        this.setLocale(locale);
        this.getInputContext().selectInputMethod(locale);
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
            south.setVisible(false);

        } else if (button == btnBaseEdit) {
            ((CardLayout) center.getLayout()).show(center, "pan2");
            loadingTab2();
            south.setVisible(true);

        } else if (button == btnLogin) {
            ((CardLayout) center.getLayout()).show(center, "pan3");
            south.setVisible(true);
        }
    }//GEN-LAST:event_btnCard

    private void btn10btnAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn10btnAction
        //        PathToDb.FilesFilter filter = new PathToDb.FilesFilter();
        //        JFileChooser chooser = new JFileChooser();
        //        chooser.setCurrentDirectory(new File("."));
        //        chooser.setFileFilter(filter);
        //        int result = chooser.showDialog(this, "Выбрать");
        //        if (result == JFileChooser.APPROVE_OPTION) {
        //            edPath.setText(chooser.getSelectedFile().getPath());
        //        }
    }//GEN-LAST:event_btn10btnAction

    private void btnStart(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStart
        try {
            Query.listOpenTable.clear();
            eProperty.user.write("sysdba");
            eProperty.password = String.valueOf("masterkey");
            String num_base = eProperty.base_num.read();
            Confb con2 = Confb.initConnect();
            con2.createConnection(eProperty.server(num_base), eProperty.port(num_base), eProperty.base(num_base), eProperty.user.read(), eProperty.password.toCharArray(), null);
            Connection c2 = con2.getConnection();

            Confb con1 = new Confb();
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
        Confb Src = new Confb();
        eExcep excep = Src.createConnection(edServer.getText().trim(), edPort.getText().trim(),
                edPath.getText().trim(), edUser.getText().trim(), edPass.getText().toCharArray(), null);
        JOptionPane.showMessageDialog(this, edPath.getText().trim() + "  \n" + excep.mes, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnTestBtnStartClick

    private void mn11cellValueType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn11cellValueType

    }//GEN-LAST:event_mn11cellValueType

    private void mn12cellValueType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn12cellValueType

    }//GEN-LAST:event_mn12cellValueType

    private void mn30mnExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn30mnExit
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.dispose());
    }//GEN-LAST:event_mn30mnExit

    private void mnBase(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnBase
        String num_base = (mn631.isSelected()) ? "1" : (mn632.isSelected()) ? "2" : "3";
        connectBaseNumb(num_base);
    }//GEN-LAST:event_mnBase

    private void mnCard(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCard
        ((CardLayout) center.getLayout()).show(center, "pan5");
        timer.start();
        south.setVisible(false);
    }//GEN-LAST:event_mnCard

// <editor-fold defaultstate="collapsed" desc="Generated Code">    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn10;
    private javax.swing.JButton btnBaseEdit;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConv;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnStart;
    private javax.swing.JToggleButton btnT7;
    private javax.swing.JToggleButton btnT8;
    private javax.swing.JToggleButton btnT9;
    private javax.swing.JButton btnTest;
    private javax.swing.ButtonGroup buttonBaseGroup1;
    private javax.swing.ButtonGroup buttonBaseGroup2;
    private javax.swing.ButtonGroup buttonLookAndFiilGroup;
    private javax.swing.JPanel center;
    private javax.swing.JTextField edPass;
    private javax.swing.JTextField edPath;
    private javax.swing.JTextField edPort;
    private javax.swing.JTextField edServer;
    private javax.swing.JTextField edUser;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lab1;
    private javax.swing.JLabel lab2;
    private javax.swing.JLabel lab3;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab5;
    private javax.swing.JLabel lab6;
    private javax.swing.JLabel labPath2;
    private javax.swing.JMenu mn10;
    private javax.swing.JMenuItem mn11;
    private javax.swing.JMenuItem mn12;
    private javax.swing.JMenuItem mn20;
    private javax.swing.JMenuItem mn30;
    private javax.swing.JMenu mn62;
    private javax.swing.JMenu mn63;
    private javax.swing.JCheckBoxMenuItem mn631;
    private javax.swing.JCheckBoxMenuItem mn632;
    private javax.swing.JCheckBoxMenuItem mn633;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan10;
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
    private javax.swing.JPopupMenu.Separator sep1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
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
        appendToPane("    PS. У Вас установлена версия Firebird " + Confb.instanc().version() + "\n", Color.GRAY);

        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            JCheckBoxMenuItem mnIt = new javax.swing.JCheckBoxMenuItem();
            buttonLookAndFiilGroup.add(mnIt);
            hmLookAndFill.put(laf.getName(), mnIt);
            mn62.add(mnIt);
            mnIt.setFont(frames.Util.getFont(1, 1));
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
    }
}
