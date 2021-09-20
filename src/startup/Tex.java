package startup;

import frames.PathToDb;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import frames.UGui;
import frames.FrameProgress;
import frames.FrameToFile;
import common.eProperty;
import dataset.Record;
import builder.Wincalc;
import domain.eSysprod;
import java.awt.Frame;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import frames.swing.listener.ListenerFrame;
import common.eProfile;
import dataset.Query;

/**
 * <p>
 * Технолог</p>
 */
public class Tex extends javax.swing.JFrame {

    private Wincalc iwin = new Wincalc();
    private javax.swing.Timer timer = null;
    private ListenerFrame listenerMenu;
    private HashMap<String, JCheckBoxMenuItem> hmLookAndFill = new HashMap();

    public Tex() {

        initComponents();
        initElements();
    }

    private void constructive() {
        int sysprodID = Integer.valueOf(eProperty.sysprodID.read());
        Record sysprodRec = eSysprod.find(sysprodID);
        String script = sysprodRec.getStr(eSysprod.script);
        if (script != null && script.isEmpty() == false) {
            JsonElement script2 = new Gson().fromJson(script, JsonElement.class);
            script2.getAsJsonObject().addProperty("nuni", sysprodRec.getInt(eSysprod.systree_id)); //запишем nuni в script
            iwin.build(script2.toString()); //калькуляция изделия                
        }
    }

    private void mnLookAndFeel(java.awt.event.ActionEvent evt) {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if (((JCheckBoxMenuItem) evt.getSource()).getText().equals(laf.getName()) == true) {
                eProperty.lookandfeel.write(laf.getName());
                eProperty.save();
            }
        }
    }

    private void connectBaseNumb(String num_base) {
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null && el != App.Top).forEach(el -> el.frame.dispose());
        Query.listOpenTable.forEach(q -> q.clear());
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
        buttonMenuGroup = new javax.swing.ButtonGroup();
        tb7 = new javax.swing.JToolBar();
        btn15 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        tb1 = new javax.swing.JToolBar();
        btn14 = new javax.swing.JButton();
        btn12 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn11 = new javax.swing.JButton();
        btn16 = new javax.swing.JButton();
        td5 = new javax.swing.JToolBar();
        btn52 = new javax.swing.JButton();
        btn51 = new javax.swing.JButton();
        btn53 = new javax.swing.JButton();
        tb2 = new javax.swing.JToolBar();
        btn1 = new javax.swing.JToggleButton();
        btn23 = new javax.swing.JButton();
        btn24 = new javax.swing.JButton();
        btn25 = new javax.swing.JButton();
        btn26 = new javax.swing.JButton();
        btn27 = new javax.swing.JButton();
        btn21 = new javax.swing.JButton();
        tb6 = new javax.swing.JToolBar();
        btnT7 = new javax.swing.JToggleButton();
        btnT8 = new javax.swing.JToggleButton();
        btnT9 = new javax.swing.JToggleButton();
        tb4 = new javax.swing.JToolBar();
        btnTest = new javax.swing.JButton();
        btn42 = new javax.swing.JButton();
        mn0 = new javax.swing.JMenuBar();
        mn01 = new javax.swing.JMenu();
        mn15 = new javax.swing.JMenuItem();
        mn11 = new javax.swing.JMenuItem();
        mn13 = new javax.swing.JSeparator();
        mn14 = new javax.swing.JMenuItem();
        mn02 = new javax.swing.JMenu();
        mn26 = new javax.swing.JMenuItem();
        mn21 = new javax.swing.JMenuItem();
        mn24 = new javax.swing.JMenuItem();
        mn23 = new javax.swing.JMenuItem();
        mn25 = new javax.swing.JPopupMenu.Separator();
        mn22 = new javax.swing.JMenuItem();
        mn09 = new javax.swing.JMenu();
        mn91 = new javax.swing.JMenuItem();
        mn94 = new javax.swing.JMenuItem();
        mn93 = new javax.swing.JPopupMenu.Separator();
        mn92 = new javax.swing.JMenuItem();
        mn03 = new javax.swing.JMenu();
        mn31 = new javax.swing.JMenuItem();
        mn32 = new javax.swing.JMenuItem();
        mn34 = new javax.swing.JMenuItem();
        mn35 = new javax.swing.JMenuItem();
        mn36 = new javax.swing.JMenuItem();
        mn38 = new javax.swing.JPopupMenu.Separator();
        mn37 = new javax.swing.JMenuItem();
        mn06 = new javax.swing.JMenu();
        mn63 = new javax.swing.JMenu();
        mn631 = new javax.swing.JCheckBoxMenuItem();
        mn632 = new javax.swing.JCheckBoxMenuItem();
        mn633 = new javax.swing.JCheckBoxMenuItem();
        mn62 = new javax.swing.JMenu();
        mn05 = new javax.swing.JMenu();
        mn51 = new javax.swing.JMenuItem();
        mn52 = new javax.swing.JMenuItem();
        mn54 = new javax.swing.JMenuItem();
        mn99 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SA-OKNA   <АРМ Технолог>");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setPreferredSize(new java.awt.Dimension(659, 80));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Tex.this.windowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Tex.this.windowClosing(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                Tex.this.windowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                Tex.this.windowIconified(evt);
            }
        });
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));

        tb7.setRollover(true);
        tb7.setPreferredSize(new java.awt.Dimension(68, 28));

        btn15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c018.gif"))); // NOI18N
        btn15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn15.setFocusable(false);
        btn15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn15.setMaximumSize(new java.awt.Dimension(26, 26));
        btn15.setMinimumSize(new java.awt.Dimension(26, 26));
        btn15.setPreferredSize(new java.awt.Dimension(26, 26));
        btn15.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRulecalc(evt);
            }
        });
        tb7.add(btn15);

        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c060.gif"))); // NOI18N
        btn2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn2.setFocusable(false);
        btn2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn2.setMaximumSize(new java.awt.Dimension(26, 26));
        btn2.setMinimumSize(new java.awt.Dimension(26, 26));
        btn2.setPreferredSize(new java.awt.Dimension(26, 26));
        btn2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGroup1(evt);
            }
        });
        tb7.add(btn2);

        getContentPane().add(tb7);

        tb1.setRollover(true);
        tb1.setMaximumSize(new java.awt.Dimension(150, 28));
        tb1.setMinimumSize(new java.awt.Dimension(150, 28));
        tb1.setPreferredSize(new java.awt.Dimension(146, 28));

        btn14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c008.gif"))); // NOI18N
        btn14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
        btn14.setFocusable(false);
        btn14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn14.setMaximumSize(new java.awt.Dimension(26, 26));
        btn14.setMinimumSize(new java.awt.Dimension(26, 26));
        btn14.setPreferredSize(new java.awt.Dimension(26, 26));
        btn14.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSyssize(evt);
            }
        });
        tb1.add(btn14);

        btn12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c010.gif"))); // NOI18N
        btn12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
        btn12.setFocusable(false);
        btn12.setMaximumSize(new java.awt.Dimension(26, 26));
        btn12.setMinimumSize(new java.awt.Dimension(26, 26));
        btn12.setPreferredSize(new java.awt.Dimension(26, 26));
        btn12.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnColor(evt);
            }
        });
        tb1.add(btn12);

        btn13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c007.gif"))); // NOI18N
        btn13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
        btn13.setFocusable(false);
        btn13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn13.setMaximumSize(new java.awt.Dimension(26, 26));
        btn13.setMinimumSize(new java.awt.Dimension(26, 26));
        btn13.setPreferredSize(new java.awt.Dimension(26, 26));
        btn13.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCurrency(evt);
            }
        });
        tb1.add(btn13);

        btn11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c022.gif"))); // NOI18N
        btn11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
        btn11.setFocusable(false);
        btn11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn11.setMaximumSize(new java.awt.Dimension(26, 26));
        btn11.setMinimumSize(new java.awt.Dimension(26, 26));
        btn11.setPreferredSize(new java.awt.Dimension(26, 26));
        btn11.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnParametr(evt);
            }
        });
        tb1.add(btn11);

        btn16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c017.gif"))); // NOI18N
        btn16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
        btn16.setFocusable(false);
        btn16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn16.setMaximumSize(new java.awt.Dimension(26, 26));
        btn16.setMinimumSize(new java.awt.Dimension(26, 26));
        btn16.setPreferredSize(new java.awt.Dimension(26, 26));
        btn16.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGroup2(evt);
            }
        });
        tb1.add(btn16);

        getContentPane().add(tb1);

        td5.setRollover(true);
        td5.setMaximumSize(new java.awt.Dimension(96, 28));
        td5.setMinimumSize(new java.awt.Dimension(96, 28));
        td5.setPreferredSize(new java.awt.Dimension(94, 28));

        btn52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c012.gif"))); // NOI18N
        btn52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        btn52.setFocusable(false);
        btn52.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn52.setMaximumSize(new java.awt.Dimension(26, 26));
        btn52.setMinimumSize(new java.awt.Dimension(26, 26));
        btn52.setPreferredSize(new java.awt.Dimension(26, 26));
        btn52.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn52.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBoxTypical(evt);
            }
        });
        td5.add(btn52);

        btn51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c021.gif"))); // NOI18N
        btn51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        btn51.setFocusable(false);
        btn51.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn51.setMaximumSize(new java.awt.Dimension(26, 26));
        btn51.setMinimumSize(new java.awt.Dimension(26, 26));
        btn51.setPreferredSize(new java.awt.Dimension(26, 26));
        btn51.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn51.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn94(evt);
            }
        });
        td5.add(btn51);

        btn53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c028.gif"))); // NOI18N
        btn53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        btn53.setFocusable(false);
        btn53.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn53.setMaximumSize(new java.awt.Dimension(26, 26));
        btn53.setMinimumSize(new java.awt.Dimension(26, 26));
        btn53.setPreferredSize(new java.awt.Dimension(26, 26));
        btn53.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn53.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn42(evt);
            }
        });
        td5.add(btn53);

        getContentPane().add(td5);

        tb2.setRollover(true);
        tb2.setMaximumSize(new java.awt.Dimension(176, 28));
        tb2.setMinimumSize(new java.awt.Dimension(176, 28));
        tb2.setPreferredSize(new java.awt.Dimension(198, 28));

        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c056.gif"))); // NOI18N
        btn1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        btn1.setFocusable(false);
        btn1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btn1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tb2.add(btn1);

        btn23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c023.gif"))); // NOI18N
        btn23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        btn23.setFocusable(false);
        btn23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn23.setMaximumSize(new java.awt.Dimension(26, 26));
        btn23.setMinimumSize(new java.awt.Dimension(26, 26));
        btn23.setPreferredSize(new java.awt.Dimension(26, 26));
        btn23.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnKits(evt);
            }
        });
        tb2.add(btn23);

        btn24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c024.gif"))); // NOI18N
        btn24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        btn24.setFocusable(false);
        btn24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn24.setMaximumSize(new java.awt.Dimension(26, 26));
        btn24.setMinimumSize(new java.awt.Dimension(26, 26));
        btn24.setPreferredSize(new java.awt.Dimension(26, 26));
        btn24.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mJoining(evt);
            }
        });
        tb2.add(btn24);

        btn25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c025.gif"))); // NOI18N
        btn25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        btn25.setFocusable(false);
        btn25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn25.setMaximumSize(new java.awt.Dimension(26, 26));
        btn25.setMinimumSize(new java.awt.Dimension(26, 26));
        btn25.setPreferredSize(new java.awt.Dimension(26, 26));
        btn25.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn25.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnElement(evt);
            }
        });
        tb2.add(btn25);

        btn26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c026.gif"))); // NOI18N
        btn26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        btn26.setFocusable(false);
        btn26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn26.setMaximumSize(new java.awt.Dimension(26, 26));
        btn26.setMinimumSize(new java.awt.Dimension(26, 26));
        btn26.setPreferredSize(new java.awt.Dimension(26, 26));
        btn26.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn26.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGlass(evt);
            }
        });
        tb2.add(btn26);

        btn27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c027.gif"))); // NOI18N
        btn27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        btn27.setFocusable(false);
        btn27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn27.setMaximumSize(new java.awt.Dimension(26, 26));
        btn27.setMinimumSize(new java.awt.Dimension(26, 26));
        btn27.setPreferredSize(new java.awt.Dimension(26, 26));
        btn27.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn27.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFurnityra(evt);
            }
        });
        tb2.add(btn27);

        btn21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c016.gif"))); // NOI18N
        btn21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        btn21.setFocusable(false);
        btn21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn21.setMaximumSize(new java.awt.Dimension(26, 26));
        btn21.setMinimumSize(new java.awt.Dimension(26, 26));
        btn21.setPreferredSize(new java.awt.Dimension(26, 26));
        btn21.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn21.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn41(evt);
            }
        });
        tb2.add(btn21);

        getContentPane().add(tb2);

        tb6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255)));
        tb6.setRollover(true);
        tb6.setMaximumSize(new java.awt.Dimension(96, 28));
        tb6.setMinimumSize(new java.awt.Dimension(96, 28));
        tb6.setPreferredSize(new java.awt.Dimension(96, 28));

        buttonBaseGroup1.add(btnT7);
        btnT7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c057.gif"))); // NOI18N
        btnT7.setSelected(true);
        btnT7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnT7.setFocusable(false);
        btnT7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnT7.setMaximumSize(new java.awt.Dimension(28, 25));
        btnT7.setMinimumSize(new java.awt.Dimension(28, 25));
        btnT7.setPreferredSize(new java.awt.Dimension(28, 25));
        btnT7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c061.gif"))); // NOI18N
        btnT7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnT7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBase(evt);
            }
        });
        tb6.add(btnT7);

        buttonBaseGroup1.add(btnT8);
        btnT8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c058.gif"))); // NOI18N
        btnT8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnT8.setFocusable(false);
        btnT8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnT8.setMaximumSize(new java.awt.Dimension(28, 25));
        btnT8.setMinimumSize(new java.awt.Dimension(28, 25));
        btnT8.setPreferredSize(new java.awt.Dimension(28, 25));
        btnT8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c062.gif"))); // NOI18N
        btnT8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnT8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBase(evt);
            }
        });
        tb6.add(btnT8);

        buttonBaseGroup1.add(btnT9);
        btnT9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c059.gif"))); // NOI18N
        btnT9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnT9.setFocusable(false);
        btnT9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnT9.setMaximumSize(new java.awt.Dimension(28, 25));
        btnT9.setMinimumSize(new java.awt.Dimension(28, 25));
        btnT9.setPreferredSize(new java.awt.Dimension(28, 25));
        btnT9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c063.gif"))); // NOI18N
        btnT9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnT9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBase(evt);
            }
        });
        tb6.add(btnT9);

        getContentPane().add(tb6);

        tb4.setRollover(true);
        tb4.setMaximumSize(new java.awt.Dimension(70, 28));
        tb4.setMinimumSize(new java.awt.Dimension(70, 28));
        tb4.setPreferredSize(new java.awt.Dimension(68, 28));

        btnTest.setText("Test");
        btnTest.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTest.setMaximumSize(new java.awt.Dimension(25, 25));
        btnTest.setMinimumSize(new java.awt.Dimension(25, 25));
        btnTest.setPreferredSize(new java.awt.Dimension(25, 25));
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest(evt);
            }
        });
        tb4.add(btnTest);

        btn42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        btn42.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btn42.setFocusable(false);
        btn42.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn42.setMaximumSize(new java.awt.Dimension(26, 26));
        btn42.setMinimumSize(new java.awt.Dimension(26, 26));
        btn42.setPreferredSize(new java.awt.Dimension(26, 26));
        btn42.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn42.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnExit(evt);
            }
        });
        tb4.add(btn42);

        getContentPane().add(tb4);

        mn0.setPreferredSize(new java.awt.Dimension(800, 25));

        mn01.setActionCommand("*Учреждение");
        mn01.setFont(frames.UGui.getFont(1,1));
        mn01.setLabel("  Настройки  ");

        mn15.setFont(frames.UGui.getFont(1,1));
        mn15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b020.gif"))); // NOI18N
        mn15.setText("Правила");
        mn15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRulecalc(evt);
            }
        });
        mn01.add(mn15);

        mn11.setFont(frames.UGui.getFont(1,1));
        mn11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b020.gif"))); // NOI18N
        mn11.setText("Настройки");
        mn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGroup1(evt);
            }
        });
        mn01.add(mn11);
        mn01.add(mn13);

        mn14.setFont(frames.UGui.getFont(1,1));
        mn14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b009.gif"))); // NOI18N
        mn14.setText("Выход");
        mn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnExit(evt);
            }
        });
        mn01.add(mn14);

        mn0.add(mn01);

        mn02.setFont(frames.UGui.getFont(1,1));
        mn02.setLabel("  Справочники  ");

        mn26.setFont(frames.UGui.getFont(1,1));
        mn26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn26.setText("Константы");
        mn26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSyssize(evt);
            }
        });
        mn02.add(mn26);

        mn21.setFont(frames.UGui.getFont(1,1));
        mn21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn21.setText("Текстуры");
        mn21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnColor(evt);
            }
        });
        mn02.add(mn21);

        mn24.setFont(frames.UGui.getFont(1,1));
        mn24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn24.setText("Валюта");
        mn24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCurrency(evt);
            }
        });
        mn02.add(mn24);

        mn23.setFont(frames.UGui.getFont(1,1));
        mn23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn23.setText("Параметры");
        mn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnParametr(evt);
            }
        });
        mn02.add(mn23);
        mn02.add(mn25);

        mn22.setFont(frames.UGui.getFont(1,1));
        mn22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn22.setText("Справочники");
        mn22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGroup2(evt);
            }
        });
        mn02.add(mn22);

        mn0.add(mn02);

        mn09.setFont(frames.UGui.getFont(1,1));
        mn09.setLabel("  Системы  ");

        mn91.setFont(frames.UGui.getFont(1,1));
        mn91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b054.gif"))); // NOI18N
        mn91.setText("Модели");
        mn91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBoxTypical(evt);
            }
        });
        mn09.add(mn91);

        mn94.setFont(frames.UGui.getFont(1,1));
        mn94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b054.gif"))); // NOI18N
        mn94.setText("Артикулы");
        mn94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn94(evt);
            }
        });
        mn09.add(mn94);
        mn09.add(mn93);

        mn92.setFont(frames.UGui.getFont(1,1));
        mn92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b054.gif"))); // NOI18N
        mn92.setText("Системы");
        mn92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn42(evt);
            }
        });
        mn09.add(mn92);

        mn0.add(mn09);

        mn03.setFont(frames.UGui.getFont(1,1));
        mn03.setLabel("  Составы  ");

        mn31.setFont(frames.UGui.getFont(1,1));
        mn31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn31.setText("Комплекты");
        mn31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnKits(evt);
            }
        });
        mn03.add(mn31);

        mn32.setFont(frames.UGui.getFont(1,1));
        mn32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn32.setText("Соединения");
        mn32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mJoining(evt);
            }
        });
        mn03.add(mn32);

        mn34.setFont(frames.UGui.getFont(1,1));
        mn34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn34.setText("Вставки");
        mn34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnElement(evt);
            }
        });
        mn03.add(mn34);

        mn35.setFont(frames.UGui.getFont(1,1));
        mn35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn35.setText("Заполнения");
        mn35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGlass(evt);
            }
        });
        mn03.add(mn35);

        mn36.setFont(frames.UGui.getFont(1,1));
        mn36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn36.setText("Фурнитура");
        mn36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFurnityra(evt);
            }
        });
        mn03.add(mn36);
        mn03.add(mn38);

        mn37.setFont(frames.UGui.getFont(1,1));
        mn37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn37.setText("Спецификация");
        mn37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn41(evt);
            }
        });
        mn03.add(mn37);

        mn0.add(mn03);

        mn06.setFont(frames.UGui.getFont(1,1));
        mn06.setLabel("  Сервис  ");

        mn63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b028.gif"))); // NOI18N
        mn63.setText("Установка соединения");
        mn63.setFont(frames.UGui.getFont(1,1));

        buttonMenuGroup.add(mn631);
        mn631.setFont(frames.UGui.getFont(1,1));
        mn631.setSelected(true);
        mn631.setText("База 1");
        mn631.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn631);

        buttonMenuGroup.add(mn632);
        mn632.setFont(frames.UGui.getFont(1,1));
        mn632.setText("База 2");
        mn632.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn632);

        buttonMenuGroup.add(mn633);
        mn633.setFont(frames.UGui.getFont(1,1));
        mn633.setText("База 3");
        mn633.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBase(evt);
            }
        });
        mn63.add(mn633);

        mn06.add(mn63);

        mn62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b061.gif"))); // NOI18N
        mn62.setText("Вид интерфейса");
        mn62.setFont(frames.UGui.getFont(1,1));
        mn06.add(mn62);

        mn0.add(mn06);

        mn05.setFont(frames.UGui.getFont(1,1));
        mn05.setLabel("  Справка  ");

        mn51.setFont(frames.UGui.getFont(1,1));
        mn51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn51.setText("Справка");
        mn51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn51ActionPerformed(evt);
            }
        });
        mn05.add(mn51);

        mn52.setFont(frames.UGui.getFont(1,1));
        mn52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn52.setText("Часто задаваемые вопросы");
        mn52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mHowTo(evt);
            }
        });
        mn05.add(mn52);

        mn54.setFont(frames.UGui.getFont(1,1));
        mn54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn54.setText("О программе");
        mn54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAboutBox(evt);
            }
        });
        mn05.add(mn54);

        mn0.add(mn05);

        mn99.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        mn99.setMargin(new java.awt.Insets(2, 80, 2, 2));
        mn0.add(mn99);

        setJMenuBar(mn0);

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnColor

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Color.createFrame(Tex.this);
            }
        });
}//GEN-LAST:event_mnColor

    private void mn42(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn42

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Systree.createFrame(Tex.this);
                btn1.setSelected(true);
            }
        });
}//GEN-LAST:event_mn42

    private void mnExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnExit
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.dispose());
}//GEN-LAST:event_mnExit

private void mnAboutBox(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAboutBox

    App.AboutBox.createFrame(Tex.this);
}//GEN-LAST:event_mnAboutBox

private void mn51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn51ActionPerformed
}//GEN-LAST:event_mn51ActionPerformed

private void mn41(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn41

    FrameProgress.create(Tex.this, new ListenerFrame() {
        public void actionRequest(Object obj) {
            App.Specification.createFrame(Tex.this);
            btn1.setSelected(true);
        }
    });
}//GEN-LAST:event_mn41

private void mn94(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn94

    FrameProgress.create(Tex.this, new ListenerFrame() {
        public void actionRequest(Object obj) {
            App.Artikles.createFrame(Tex.this);
        }
    });
}//GEN-LAST:event_mn94

    private void mHowTo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mHowTo

    }//GEN-LAST:event_mHowTo

    private void mnCurrency(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCurrency

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Currenc.createFrame(Tex.this);
            }
        });
    }//GEN-LAST:event_mnCurrency

    private void mnElement(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnElement

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                if (btn1.isSelected() == false) {
                    App.Element.createFrame(Tex.this);
                } else {
                    constructive();
                    iwin.calcElements = new builder.making.Elements(iwin);
                    iwin.calcElements.calc();
                    App.Element.createFrame(Tex.this, iwin.calcElements.listVariants);
                }
            }
        });
    }//GEN-LAST:event_mnElement

    private void mnFurnityra(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnFurnityra

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                if (btn1.isSelected() == false) {
                    App.Furniture.createFrame(Tex.this);
                } else {
                    constructive();
                    iwin.calcFurniture = new builder.making.Furniture(iwin); //фурнитура 
                    iwin.calcFurniture.calc();
                    App.Furniture.createFrame(Tex.this, iwin.calcFurniture.listVariants);
                }
            }
        });
    }//GEN-LAST:event_mnFurnityra

    private void mnParametr(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnParametr

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Param.createFrame(Tex.this);
            }
        });
    }//GEN-LAST:event_mnParametr

    private void mJoining(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mJoining

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                if (btn1.isSelected() == false) {
                    App.Joining.createFrame(Tex.this);
                } else {
                    constructive();
                    iwin.calcJoining = new builder.making.Joining(iwin);
                    iwin.calcJoining.calc();
                    App.Joining.createFrame(Tex.this, iwin.calcJoining.listVariants);

                }
            }
        });
    }//GEN-LAST:event_mJoining

    private void mnGlass(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnGlass

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                if (btn1.isSelected() == false) {
                    App.Filling.createFrame(Tex.this);
                } else {
                    constructive();
                    iwin.calcFilling = new builder.making.Filling(iwin);
                    iwin.calcFilling.calc();
                    App.Filling.createFrame(Tex.this, iwin.calcFilling.listVariants);
                }
            }
        });
    }//GEN-LAST:event_mnGlass

    private void mnKits(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnKits

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Kits.createFrame(Tex.this);
            }
        });
    }//GEN-LAST:event_mnKits

    private void mnSyssize(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSyssize

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Syssize.createFrame(Tex.this);
            }
        });
    }//GEN-LAST:event_mnSyssize

    private void mnBoxTypical(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnBoxTypical

        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Models.createFrame(Tex.this);
            }
        });
    }//GEN-LAST:event_mnBoxTypical

    private void windowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosing
        mnExit(null);
    }//GEN-LAST:event_windowClosing

    private void mnRulecalc(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRulecalc
        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.RuleCalc.createFrame(Tex.this);
            }
        });
    }//GEN-LAST:event_mnRulecalc

    private void windowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowIconified
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.setState(Frame.ICONIFIED));
    }//GEN-LAST:event_windowIconified

    private void windowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowDeiconified
        Arrays.asList(App.values()).stream().filter(el -> el.frame != null).forEach(el -> el.frame.setState(Frame.NORMAL));
    }//GEN-LAST:event_windowDeiconified

    private void mnBase(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnBase
        String num_base = (mn631.isSelected()) ? "1" : (mn632.isSelected()) ? "2" : "3";
        connectBaseNumb(num_base);
    }//GEN-LAST:event_mnBase

    private void btnBase(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBase
        String num_base = (btnT7.isSelected()) ? "1" : (btnT8.isSelected()) ? "2" : "3";
        connectBaseNumb(num_base);
    }//GEN-LAST:event_btnBase

    private void mnGroup2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnGroup2
        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Groups.createFrame(Tex.this, 0, 1, 2, 3);
            }
        });
    }//GEN-LAST:event_mnGroup2

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed

    }//GEN-LAST:event_windowClosed

    private void mnGroup1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnGroup1
        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.Groups.createFrame(Tex.this, 4, 5, 6);
            }
        });
    }//GEN-LAST:event_mnGroup1

    private void btnTest(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest
        FrameProgress.create(Tex.this, new ListenerFrame() {
            public void actionRequest(Object obj) {
                App.DBCompare.createFrame(Tex.this);
            }
        });
    }//GEN-LAST:event_btnTest

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        /*Arrays.asList(App.values()).forEach(act -> {
            if (act.frame != null && act != App.Top && act.frame.isVisible() == false) {
                act.frame.setVisible(true);
            }
        });*/
    }//GEN-LAST:event_formWindowActivated

// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btn1;
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn15;
    private javax.swing.JButton btn16;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn24;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn26;
    private javax.swing.JButton btn27;
    private javax.swing.JButton btn42;
    private javax.swing.JButton btn51;
    private javax.swing.JButton btn52;
    private javax.swing.JButton btn53;
    private javax.swing.JToggleButton btnT7;
    private javax.swing.JToggleButton btnT8;
    private javax.swing.JToggleButton btnT9;
    private javax.swing.JButton btnTest;
    private javax.swing.ButtonGroup buttonBaseGroup1;
    private javax.swing.ButtonGroup buttonBaseGroup2;
    private javax.swing.ButtonGroup buttonLookAndFiilGroup;
    private javax.swing.ButtonGroup buttonMenuGroup;
    private javax.swing.JMenuBar mn0;
    private javax.swing.JMenu mn01;
    private javax.swing.JMenu mn02;
    private javax.swing.JMenu mn03;
    private javax.swing.JMenu mn05;
    private javax.swing.JMenu mn06;
    private javax.swing.JMenu mn09;
    private javax.swing.JMenuItem mn11;
    private javax.swing.JSeparator mn13;
    private javax.swing.JMenuItem mn14;
    private javax.swing.JMenuItem mn15;
    private javax.swing.JMenuItem mn21;
    private javax.swing.JMenuItem mn22;
    private javax.swing.JMenuItem mn23;
    private javax.swing.JMenuItem mn24;
    private javax.swing.JPopupMenu.Separator mn25;
    private javax.swing.JMenuItem mn26;
    private javax.swing.JMenuItem mn31;
    private javax.swing.JMenuItem mn32;
    private javax.swing.JMenuItem mn34;
    private javax.swing.JMenuItem mn35;
    private javax.swing.JMenuItem mn36;
    private javax.swing.JMenuItem mn37;
    private javax.swing.JPopupMenu.Separator mn38;
    private javax.swing.JMenuItem mn51;
    private javax.swing.JMenuItem mn52;
    private javax.swing.JMenuItem mn54;
    private javax.swing.JMenu mn62;
    private javax.swing.JMenu mn63;
    private javax.swing.JCheckBoxMenuItem mn631;
    private javax.swing.JCheckBoxMenuItem mn632;
    private javax.swing.JCheckBoxMenuItem mn633;
    private javax.swing.JMenuItem mn91;
    private javax.swing.JMenuItem mn92;
    private javax.swing.JPopupMenu.Separator mn93;
    private javax.swing.JMenuItem mn94;
    private javax.swing.JMenu mn99;
    private javax.swing.JToolBar tb1;
    private javax.swing.JToolBar tb2;
    private javax.swing.JToolBar tb4;
    private javax.swing.JToolBar tb6;
    private javax.swing.JToolBar tb7;
    private javax.swing.JToolBar td5;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    private void initElements() {
        setTitle(eProfile.profile.title + UGui.designTitle());
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            JCheckBoxMenuItem mnIt = new javax.swing.JCheckBoxMenuItem();
            buttonLookAndFiilGroup.add(mnIt);
            hmLookAndFill.put(laf.getName(), mnIt);
            mn62.add(mnIt);
            mnIt.setFont(frames.UGui.getFont(1, 1));
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
            mn631.setSelected(true);
            btnT7.setSelected(true);
        } else if (eProperty.base_num.read().equals("2")) {
            mn632.setSelected(true);
            btnT8.setSelected(true);
        } else if (eProperty.base_num.read().equals("3")) {
            mn633.setSelected(true);
            btnT9.setSelected(true);
        }
    }
}
