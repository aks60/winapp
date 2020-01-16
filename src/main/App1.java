package main;

import common.Util;
import common.FrameListener;
import common.FrameProgress;
import common.FrameToFile;
import common.eProfile;
import common.eProp;
import convdb.Convert;
import domain.eArtikl;
import java.util.Locale;
import javax.swing.SwingWorker;
import forms.Artikls;
import forms.Sysprof;
import forms.Design;
import forms.Element;
import forms.Furn;
import forms.Glass;
import forms.Joining;
import forms.Kits;
import forms.Param;
import forms.Rate;
import forms.Color;
import forms.Order;
import forms.Partner;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * <p>
 * Технолог</p>
 */
public class App1 extends javax.swing.JFrame {

    javax.swing.Timer timer = null;

    private Locale locale = null;
    private FrameListener listenerMenu;
    private FrameListener<Object, Object> listenerDate = new FrameListener() {

        public void request(Object obj) {
        }

        public void response(Object obj) {
            Util.setGregorianCalendar(obj);
            btn11.setText(Util.getDateStr(null));
        }
    };

    /**
     * Creates new form PersTar
     */
    public App1() {

        initComponents();

        if (eProp.lookandfeel.read().equals("Metal")) {
            mn0443.setSelected(true);
        } else if (eProp.lookandfeel.read().equals("Nimbus")) {
            mn0442.setSelected(true);
        } else if (eProp.lookandfeel.read().equals("Windows")) {
            mn0441.setSelected(true);
        } else if (System.getProperty("os.name").equals("Linux")) {
            mn0443.setSelected(true);
        } else {
            mn0441.setSelected(true);
        }
        btn11.setText(Util.getDateStr(null));
//        if (Main.dev == true) {
//            new FrameListener2() {
//
//                public void request(Object obj) {
//                    eApp1.Material.createFrame(App1.this);
//                }
//            };
//        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonLookAndFiilGroup = new javax.swing.ButtonGroup();
        tb1 = new javax.swing.JToolBar();
        btn12 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn22 = new javax.swing.JButton();
        btn11 = new javax.swing.JButton();
        tb2 = new javax.swing.JToolBar();
        btn23 = new javax.swing.JButton();
        btn24 = new javax.swing.JButton();
        btn25 = new javax.swing.JButton();
        btn26 = new javax.swing.JButton();
        btn27 = new javax.swing.JButton();
        btn28 = new javax.swing.JButton();
        td3 = new javax.swing.JToolBar();
        btn29 = new javax.swing.JButton();
        btn30 = new javax.swing.JButton();
        btn33 = new javax.swing.JButton();
        tb4 = new javax.swing.JToolBar();
        btn31 = new javax.swing.JButton();
        btn32 = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        mn0 = new javax.swing.JMenuBar();
        mn01 = new javax.swing.JMenu();
        mn15 = new javax.swing.JMenuItem();
        mn11 = new javax.swing.JMenu();
        mn0111 = new javax.swing.JMenuItem();
        mn0112 = new javax.swing.JMenuItem();
        mn0113 = new javax.swing.JMenuItem();
        mn0114 = new javax.swing.JMenuItem();
        mn0115 = new javax.swing.JMenuItem();
        mn0116 = new javax.swing.JMenuItem();
        mn12 = new javax.swing.JMenu();
        mn0121 = new javax.swing.JMenuItem();
        mn0122 = new javax.swing.JMenuItem();
        mn0123 = new javax.swing.JMenuItem();
        mn13 = new javax.swing.JSeparator();
        mn14 = new javax.swing.JMenuItem();
        mn02 = new javax.swing.JMenu();
        mn21 = new javax.swing.JMenuItem();
        mn24 = new javax.swing.JMenuItem();
        mn23 = new javax.swing.JMenuItem();
        mn22 = new javax.swing.JPopupMenu.Separator();
        mn25 = new javax.swing.JMenuItem();
        mn03 = new javax.swing.JMenu();
        mn31 = new javax.swing.JMenuItem();
        mn32 = new javax.swing.JMenuItem();
        mn34 = new javax.swing.JMenuItem();
        mn35 = new javax.swing.JMenuItem();
        mn36 = new javax.swing.JMenuItem();
        mn38 = new javax.swing.JPopupMenu.Separator();
        mn37 = new javax.swing.JMenuItem();
        mn08 = new javax.swing.JMenu();
        mn81 = new javax.swing.JMenuItem();
        mn82 = new javax.swing.JMenuItem();
        mn83 = new javax.swing.JPopupMenu.Separator();
        mn84 = new javax.swing.JMenuItem();
        mn04 = new javax.swing.JMenu();
        mn41 = new javax.swing.JMenuItem();
        mn43 = new javax.swing.JMenuItem();
        mn42 = new javax.swing.JMenuItem();
        mn07 = new javax.swing.JMenu();
        mn71 = new javax.swing.JMenuItem();
        mn06 = new javax.swing.JMenu();
        mn61 = new javax.swing.JMenuItem();
        mn62 = new javax.swing.JMenu();
        mn0441 = new javax.swing.JCheckBoxMenuItem();
        mn0442 = new javax.swing.JCheckBoxMenuItem();
        mn0443 = new javax.swing.JCheckBoxMenuItem();
        mn444 = new javax.swing.JCheckBoxMenuItem();
        mn05 = new javax.swing.JMenu();
        mn51 = new javax.swing.JMenuItem();
        mn52 = new javax.swing.JMenuItem();
        mn54 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SA-OKNA   <АРМ Технолог>");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));

        tb1.setRollover(true);
        tb1.setPreferredSize(new java.awt.Dimension(110, 27));

        btn12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c010.gif"))); // NOI18N
        btn12.setMaximumSize(new java.awt.Dimension(25, 25));
        btn12.setMinimumSize(new java.awt.Dimension(25, 25));
        btn12.setPreferredSize(new java.awt.Dimension(25, 25));
        btn12.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDictColor(evt);
            }
        });
        tb1.add(btn12);

        btn13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btn13.setFocusable(false);
        btn13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn13.setMaximumSize(new java.awt.Dimension(25, 25));
        btn13.setMinimumSize(new java.awt.Dimension(25, 25));
        btn13.setPreferredSize(new java.awt.Dimension(25, 25));
        btn13.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn24(evt);
            }
        });
        tb1.add(btn13);

        btn22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c022.gif"))); // NOI18N
        btn22.setFocusable(false);
        btn22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn22.setMaximumSize(new java.awt.Dimension(25, 25));
        btn22.setMinimumSize(new java.awt.Dimension(25, 25));
        btn22.setPreferredSize(new java.awt.Dimension(25, 25));
        btn22.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn22.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parametr(evt);
            }
        });
        tb1.add(btn22);

        btn11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c021.gif"))); // NOI18N
        btn11.setFocusable(false);
        btn11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn11.setMaximumSize(new java.awt.Dimension(25, 25));
        btn11.setMinimumSize(new java.awt.Dimension(25, 25));
        btn11.setPreferredSize(new java.awt.Dimension(25, 25));
        btn11.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn25(evt);
            }
        });
        tb1.add(btn11);

        getContentPane().add(tb1);

        tb2.setRollover(true);
        tb2.setMaximumSize(new java.awt.Dimension(220, 27));
        tb2.setMinimumSize(new java.awt.Dimension(220, 27));
        tb2.setPreferredSize(new java.awt.Dimension(160, 27));

        btn23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c023.gif"))); // NOI18N
        btn23.setFocusable(false);
        btn23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn23.setMaximumSize(new java.awt.Dimension(25, 25));
        btn23.setMinimumSize(new java.awt.Dimension(25, 25));
        btn23.setPreferredSize(new java.awt.Dimension(25, 25));
        btn23.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnKits(evt);
            }
        });
        tb2.add(btn23);

        btn24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c024.gif"))); // NOI18N
        btn24.setFocusable(false);
        btn24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn24.setMaximumSize(new java.awt.Dimension(25, 25));
        btn24.setMinimumSize(new java.awt.Dimension(25, 25));
        btn24.setPreferredSize(new java.awt.Dimension(25, 25));
        btn24.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mJoining(evt);
            }
        });
        tb2.add(btn24);

        btn25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c025.gif"))); // NOI18N
        btn25.setFocusable(false);
        btn25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn25.setMaximumSize(new java.awt.Dimension(25, 25));
        btn25.setMinimumSize(new java.awt.Dimension(25, 25));
        btn25.setPreferredSize(new java.awt.Dimension(25, 25));
        btn25.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn25.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnElement(evt);
            }
        });
        tb2.add(btn25);

        btn26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c026.gif"))); // NOI18N
        btn26.setFocusable(false);
        btn26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn26.setMaximumSize(new java.awt.Dimension(25, 25));
        btn26.setMinimumSize(new java.awt.Dimension(25, 25));
        btn26.setPreferredSize(new java.awt.Dimension(25, 25));
        btn26.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn26.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGlass(evt);
            }
        });
        tb2.add(btn26);

        btn27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c027.gif"))); // NOI18N
        btn27.setFocusable(false);
        btn27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn27.setMaximumSize(new java.awt.Dimension(25, 25));
        btn27.setMinimumSize(new java.awt.Dimension(25, 25));
        btn27.setPreferredSize(new java.awt.Dimension(25, 25));
        btn27.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn27.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFurnityra(evt);
            }
        });
        tb2.add(btn27);

        btn28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c028.gif"))); // NOI18N
        btn28.setFocusable(false);
        btn28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn28.setMaximumSize(new java.awt.Dimension(25, 25));
        btn28.setMinimumSize(new java.awt.Dimension(25, 25));
        btn28.setPreferredSize(new java.awt.Dimension(25, 25));
        btn28.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn28.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConstr(evt);
            }
        });
        tb2.add(btn28);

        getContentPane().add(tb2);

        td3.setRollover(true);
        td3.setPreferredSize(new java.awt.Dimension(110, 27));

        btn29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c030.gif"))); // NOI18N
        btn29.setFocusable(false);
        btn29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn29.setMaximumSize(new java.awt.Dimension(25, 25));
        btn29.setMinimumSize(new java.awt.Dimension(25, 25));
        btn29.setPreferredSize(new java.awt.Dimension(25, 25));
        btn29.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn29.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPartner(evt);
            }
        });
        td3.add(btn29);

        btn30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c047.gif"))); // NOI18N
        btn30.setFocusable(false);
        btn30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn30.setMaximumSize(new java.awt.Dimension(25, 25));
        btn30.setMinimumSize(new java.awt.Dimension(25, 25));
        btn30.setPreferredSize(new java.awt.Dimension(25, 25));
        btn30.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn30.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnOrder(evt);
            }
        });
        td3.add(btn30);

        btn33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c011.gif"))); // NOI18N
        btn33.setFocusable(false);
        btn33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn33.setMaximumSize(new java.awt.Dimension(25, 25));
        btn33.setMinimumSize(new java.awt.Dimension(25, 25));
        btn33.setPreferredSize(new java.awt.Dimension(25, 25));
        btn33.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn33.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDesign(evt);
            }
        });
        td3.add(btn33);

        getContentPane().add(td3);

        tb4.setRollover(true);

        btn31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c013.gif"))); // NOI18N
        btn31.setFocusable(false);
        btn31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn31.setMaximumSize(new java.awt.Dimension(25, 25));
        btn31.setMinimumSize(new java.awt.Dimension(25, 25));
        btn31.setPreferredSize(new java.awt.Dimension(25, 25));
        btn31.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn31.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mHelp(evt);
            }
        });
        tb4.add(btn31);

        btn32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        btn32.setFocusable(false);
        btn32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn32.setMaximumSize(new java.awt.Dimension(25, 25));
        btn32.setMinimumSize(new java.awt.Dimension(25, 25));
        btn32.setPreferredSize(new java.awt.Dimension(25, 25));
        btn32.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btn32.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mExit(evt);
            }
        });
        tb4.add(btn32);

        getContentPane().add(tb4);

        progressBar.setBorder(null);
        progressBar.setMinimumSize(new java.awt.Dimension(120, 4));
        progressBar.setPreferredSize(new java.awt.Dimension(146, 6));
        getContentPane().add(progressBar);

        mn0.setPreferredSize(new java.awt.Dimension(800, 25));

        mn01.setText("Настройки");
        mn01.setActionCommand("*Учреждение");
        mn01.setFont(common.Util.getFont(1,1));

        mn15.setFont(common.Util.getFont(1,1));
        mn15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn15.setText("Сстемные константы");
        mn15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn15mExit(evt);
            }
        });
        mn01.add(mn15);

        mn11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn11.setText("Расчётные данные");
        mn11.setFont(common.Util.getFont(1,1));

        mn0111.setFont(common.Util.getFont(1,1));
        mn0111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0111.setText("Ценовые");
        mn0111.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn0111ActionPerformed(evt);
            }
        });
        mn11.add(mn0111);

        mn0112.setFont(common.Util.getFont(1,1));
        mn0112.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0112.setText("Скидки/Наценки");
        mn0112.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn0112(evt);
            }
        });
        mn11.add(mn0112);

        mn0113.setFont(common.Util.getFont(1,1));
        mn0113.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0113.setText("Расчётные");
        mn0113.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn0113(evt);
            }
        });
        mn11.add(mn0113);

        mn0114.setFont(common.Util.getFont(1,1));
        mn0114.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0114.setText("Технологические");
        mn0114.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn0114(evt);
            }
        });
        mn11.add(mn0114);

        mn0115.setFont(common.Util.getFont(1,1));
        mn0115.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0115.setText("Дополнительно");
        mn0115.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn0115(evt);
            }
        });
        mn11.add(mn0115);

        mn0116.setFont(common.Util.getFont(1,1));
        mn0116.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0116.setText("Раскрои");
        mn0116.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn0116(evt);
            }
        });
        mn11.add(mn0116);

        mn01.add(mn11);

        mn12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn12.setText("Графические настройки");
        mn12.setFont(common.Util.getFont(1,1));

        mn0121.setFont(common.Util.getFont(1,1));
        mn0121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0121.setText("Размерные");
        mn0121.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDictColor(evt);
            }
        });
        mn12.add(mn0121);

        mn0122.setFont(common.Util.getFont(1,1));
        mn0122.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0122.setText("Форматы");
        mn0122.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn0122ActionPerformed(evt);
            }
        });
        mn12.add(mn0122);

        mn0123.setFont(common.Util.getFont(1,1));
        mn0123.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn0123.setText("Надписи");
        mn0123.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDictDicAddr(evt);
            }
        });
        mn12.add(mn0123);

        mn01.add(mn12);
        mn01.add(mn13);

        mn14.setFont(common.Util.getFont(1,1));
        mn14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b009.gif"))); // NOI18N
        mn14.setText("Выход");
        mn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mExit(evt);
            }
        });
        mn01.add(mn14);

        mn0.add(mn01);

        mn02.setText("Справочники");
        mn02.setFont(common.Util.getFont(1,1));

        mn21.setFont(common.Util.getFont(1,1));
        mn21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b057.gif"))); // NOI18N
        mn21.setText("Текстуры");
        mn21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDictColor(evt);
            }
        });
        mn02.add(mn21);

        mn24.setFont(common.Util.getFont(1,1));
        mn24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b057.gif"))); // NOI18N
        mn24.setText("Валюты");
        mn24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn24(evt);
            }
        });
        mn02.add(mn24);

        mn23.setFont(common.Util.getFont(1,1));
        mn23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b057.gif"))); // NOI18N
        mn23.setText("Параметры");
        mn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parametr(evt);
            }
        });
        mn02.add(mn23);
        mn02.add(mn22);

        mn25.setFont(common.Util.getFont(1,1));
        mn25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b057.gif"))); // NOI18N
        mn25.setText("Артикулы");
        mn25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn25(evt);
            }
        });
        mn02.add(mn25);

        mn0.add(mn02);

        mn03.setText("Конструкции");
        mn03.setFont(common.Util.getFont(1,1));

        mn31.setFont(common.Util.getFont(1,1));
        mn31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn31.setText("Комплекты");
        mn31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnKits(evt);
            }
        });
        mn03.add(mn31);

        mn32.setFont(common.Util.getFont(1,1));
        mn32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn32.setText("Соединения");
        mn32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mJoining(evt);
            }
        });
        mn03.add(mn32);

        mn34.setFont(common.Util.getFont(1,1));
        mn34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn34.setText("Составы");
        mn34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnElement(evt);
            }
        });
        mn03.add(mn34);

        mn35.setFont(common.Util.getFont(1,1));
        mn35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn35.setText("Заполнения");
        mn35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGlass(evt);
            }
        });
        mn03.add(mn35);

        mn36.setFont(common.Util.getFont(1,1));
        mn36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn36.setText("Фурнитура");
        mn36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFurnityra(evt);
            }
        });
        mn03.add(mn36);
        mn03.add(mn38);

        mn37.setFont(common.Util.getFont(1,1));
        mn37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b056.gif"))); // NOI18N
        mn37.setText("Система");
        mn37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConstr(evt);
            }
        });
        mn03.add(mn37);

        mn0.add(mn03);

        mn08.setText("Заказы");
        mn08.setFont(common.Util.getFont(1,1));

        mn81.setFont(common.Util.getFont(1,1));
        mn81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn81.setText("Контрагенты");
        mn81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPartner(evt);
            }
        });
        mn08.add(mn81);

        mn82.setFont(common.Util.getFont(1,1));
        mn82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn82.setText("Заказы");
        mn82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnOrder(evt);
            }
        });
        mn08.add(mn82);
        mn08.add(mn83);

        mn84.setFont(common.Util.getFont(1,1));
        mn84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn84.setText("Конструктор");
        mn84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDesign(evt);
            }
        });
        mn08.add(mn84);

        mn0.add(mn08);

        mn04.setText("Документы");
        mn04.setFont(common.Util.getFont(1,1));

        mn41.setFont(common.Util.getFont(1,1));
        mn41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn41.setText("Спецификация");
        mn41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn41mAdminUserToDb(evt);
            }
        });
        mn04.add(mn41);

        mn43.setFont(common.Util.getFont(1,1));
        mn43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn43.setText("Задание в цех");
        mn43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mAdminPathToDb(evt);
            }
        });
        mn04.add(mn43);

        mn42.setFont(common.Util.getFont(1,1));
        mn42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b055.gif"))); // NOI18N
        mn42.setText("Паспорт изделия");
        mn42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDicNewDate(evt);
            }
        });
        mn04.add(mn42);

        mn0.add(mn04);

        mn07.setText("Конвертор");
        mn07.setFont(common.Util.getFont(1,1));

        mn71.setFont(common.Util.getFont(1,1));
        mn71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn71.setText("SA-OKNA <= ПрофСтрой(3,4)");
        mn71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConvert(evt);
            }
        });
        mn07.add(mn71);

        mn0.add(mn07);

        mn06.setText("Сервис");
        mn06.setFont(common.Util.getFont(1,1));

        mn61.setFont(common.Util.getFont(1,1));
        mn61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn61.setText("Путь к базе данных");
        mn61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn61ActionPerformed(evt);
            }
        });
        mn06.add(mn61);

        mn62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn62.setText("Вид интерфейса");
        mn62.setFont(common.Util.getFont(1,1));

        buttonLookAndFiilGroup.add(mn0441);
        mn0441.setFont(common.Util.getFont(1,1));
        mn0441.setText("Win Classic");
        mn0441.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLookAndFeel(evt);
            }
        });
        mn62.add(mn0441);

        buttonLookAndFiilGroup.add(mn0442);
        mn0442.setFont(common.Util.getFont(1,1));
        mn0442.setText("Java Nimbus");
        mn0442.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLookAndFeel(evt);
            }
        });
        mn62.add(mn0442);

        buttonLookAndFiilGroup.add(mn0443);
        mn0443.setFont(common.Util.getFont(1,1));
        mn0443.setText("Java Metal");
        mn0443.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLookAndFeel(evt);
            }
        });
        mn62.add(mn0443);

        buttonLookAndFiilGroup.add(mn444);
        mn444.setFont(common.Util.getFont(1,1));
        mn444.setText("CDE/Motif");
        mn444.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn444mnLookAndFeel(evt);
            }
        });
        mn62.add(mn444);

        mn06.add(mn62);

        mn0.add(mn06);

        mn05.setText("Справка");
        mn05.setFont(common.Util.getFont(1,1));

        mn51.setFont(common.Util.getFont(1,1));
        mn51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn51.setText("Справка");
        mn51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn51ActionPerformed(evt);
            }
        });
        mn05.add(mn51);

        mn52.setFont(common.Util.getFont(1,1));
        mn52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn52.setText("Часто задаваемые вопросы");
        mn52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mHowTo(evt);
            }
        });
        mn05.add(mn52);

        mn54.setFont(common.Util.getFont(1,1));
        mn54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img16/b031.gif"))); // NOI18N
        mn54.setText("О программе");
        mn54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAboutBox(evt);
            }
        });
        mn05.add(mn54);

        mn0.add(mn05);

        setJMenuBar(mn0);

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mDictColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mDictColor

        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Color.createFrame(App1.this);
            }
        };
}//GEN-LAST:event_mDictColor

    private void mnConstr(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConstr
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Sysprof.createFrame(App1.this);
            }
        };
}//GEN-LAST:event_mnConstr

    private void mExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mExit
        System.exit(0);
}//GEN-LAST:event_mExit

private void mHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mHelp
    //Object obj = eArtikls.query;
}//GEN-LAST:event_mHelp

private void mAdminPathToDb(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mAdminPathToDb
    //setTitle(eProfile.P02.getTitle());
}//GEN-LAST:event_mAdminPathToDb

private void mDictDicAddr(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mDictDicAddr

}//GEN-LAST:event_mDictDicAddr

private void mnAboutBox(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAboutBox
//        new FrameListener2() {
//
//            public void request(Object obj) {
                eApp1.AboutBox.createFrame(App1.this);
//            }
//        };
}//GEN-LAST:event_mnAboutBox

private void mn51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn51ActionPerformed
}//GEN-LAST:event_mn51ActionPerformed

private void mn0113(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn0113

}//GEN-LAST:event_mn0113

private void mn0112(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn0112

}//GEN-LAST:event_mn0112

private void mn0114(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn0114

}//GEN-LAST:event_mn0114

private void mnDicNewDate(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDicNewDate

    btn11.setText(Util.getDateStr(null));
}//GEN-LAST:event_mnDicNewDate

private void mn0111ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn0111ActionPerformed

}//GEN-LAST:event_mn0111ActionPerformed

private void mnLookAndFeel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnLookAndFeel
    if (evt.getSource() == mn0441) {
        eProp.lookandfeel.write("Window");
    } else if (evt.getSource() == mn0443) {
        eProp.lookandfeel.write("Metal");
    } else if (evt.getSource() == mn0442) {
        eProp.lookandfeel.write("Nimbus");
    }
    eProp.save();
}//GEN-LAST:event_mnLookAndFeel

private void mn41mAdminUserToDb(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn41mAdminUserToDb

}//GEN-LAST:event_mn41mAdminUserToDb

private void mn25(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn25

    new FrameListener2() {

        public void request(Object obj) {
            eApp1.Artikls.createFrame(App1.this);
        }
    };
}//GEN-LAST:event_mn25

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (locale != null) {
            this.setLocale(locale);
            this.getInputContext().selectInputMethod(locale);
        }
    }//GEN-LAST:event_formWindowClosed

    private void mn0122ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn0122ActionPerformed

    }//GEN-LAST:event_mn0122ActionPerformed

    private void mn15mExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn15mExit
        // 
    }//GEN-LAST:event_mn15mExit

    private void mHowTo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mHowTo

    }//GEN-LAST:event_mHowTo

    private void mn0115(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn0115
        //
    }//GEN-LAST:event_mn0115

    private void mn0116(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn0116
        //
    }//GEN-LAST:event_mn0116

    private void mn24(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn24
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Rate.createFrame(App1.this);
            }
        };
    }//GEN-LAST:event_mn24

    private void mnElement(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnElement
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Elements.createFrame(App1.this);
            }
        };
    }//GEN-LAST:event_mnElement

    private void mnFurnityra(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnFurnityra
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Furn.createFrame(App1.this);
            }
        };
    }//GEN-LAST:event_mnFurnityra

    private void mn61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn61ActionPerformed
        //
    }//GEN-LAST:event_mn61ActionPerformed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        eApp1.disposeFrame();
    }//GEN-LAST:event_formWindowIconified

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        System.out.println("winapp.App1.formWindowDeiconified()");
    }//GEN-LAST:event_formWindowDeiconified

    private void mnPartner(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPartner

        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Partner.createFrame(App1.this);
            }
        }; 
    }//GEN-LAST:event_mnPartner

    private void parametr(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parametr
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Param.createFrame(App1.this);
            }
        };
    }//GEN-LAST:event_parametr

    private void mJoining(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mJoining
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Joining.createFrame(App1.this);
            }
        };
    }//GEN-LAST:event_mJoining

    private void mnGlass(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnGlass
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Glass.createFrame(App1.this);
            }
        };      
    }//GEN-LAST:event_mnGlass

    private void mnDesign(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDesign
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Design.createFrame(App1.this);
            }
        }; 
    }//GEN-LAST:event_mnDesign

    private void mnKits(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnKits
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Kits.createFrame(App1.this);
            }
        }; 
    }//GEN-LAST:event_mnKits

    private void mnConvert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConvert
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Convert.createFrame(App1.this);
            }
        }; 
    }//GEN-LAST:event_mnConvert

    private void mnOrder(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnOrder
        new FrameListener2() {

            public void request(Object obj) {
                eApp1.Order.createFrame(App1.this);
            }
        }; 
    }//GEN-LAST:event_mnOrder

    private void mn444mnLookAndFeel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn444mnLookAndFeel
        //
    }//GEN-LAST:event_mn444mnLookAndFeel
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn24;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn26;
    private javax.swing.JButton btn27;
    private javax.swing.JButton btn28;
    private javax.swing.JButton btn29;
    private javax.swing.JButton btn30;
    private javax.swing.JButton btn31;
    private javax.swing.JButton btn32;
    private javax.swing.JButton btn33;
    private javax.swing.ButtonGroup buttonLookAndFiilGroup;
    private javax.swing.JMenuBar mn0;
    private javax.swing.JMenu mn01;
    private javax.swing.JMenuItem mn0111;
    private javax.swing.JMenuItem mn0112;
    private javax.swing.JMenuItem mn0113;
    private javax.swing.JMenuItem mn0114;
    private javax.swing.JMenuItem mn0115;
    private javax.swing.JMenuItem mn0116;
    private javax.swing.JMenuItem mn0121;
    private javax.swing.JMenuItem mn0122;
    private javax.swing.JMenuItem mn0123;
    private javax.swing.JMenu mn02;
    private javax.swing.JMenu mn03;
    private javax.swing.JMenu mn04;
    private javax.swing.JCheckBoxMenuItem mn0441;
    private javax.swing.JCheckBoxMenuItem mn0442;
    private javax.swing.JCheckBoxMenuItem mn0443;
    private javax.swing.JMenu mn05;
    private javax.swing.JMenu mn06;
    private javax.swing.JMenu mn07;
    private javax.swing.JMenu mn08;
    private javax.swing.JMenu mn11;
    private javax.swing.JMenu mn12;
    private javax.swing.JSeparator mn13;
    private javax.swing.JMenuItem mn14;
    private javax.swing.JMenuItem mn15;
    private javax.swing.JMenuItem mn21;
    private javax.swing.JPopupMenu.Separator mn22;
    private javax.swing.JMenuItem mn23;
    private javax.swing.JMenuItem mn24;
    private javax.swing.JMenuItem mn25;
    private javax.swing.JMenuItem mn31;
    private javax.swing.JMenuItem mn32;
    private javax.swing.JMenuItem mn34;
    private javax.swing.JMenuItem mn35;
    private javax.swing.JMenuItem mn36;
    private javax.swing.JMenuItem mn37;
    private javax.swing.JPopupMenu.Separator mn38;
    private javax.swing.JMenuItem mn41;
    private javax.swing.JMenuItem mn42;
    private javax.swing.JMenuItem mn43;
    private javax.swing.JCheckBoxMenuItem mn444;
    private javax.swing.JMenuItem mn51;
    private javax.swing.JMenuItem mn52;
    private javax.swing.JMenuItem mn54;
    private javax.swing.JMenuItem mn61;
    private javax.swing.JMenu mn62;
    private javax.swing.JMenuItem mn71;
    private javax.swing.JMenuItem mn81;
    private javax.swing.JMenuItem mn82;
    private javax.swing.JPopupMenu.Separator mn83;
    private javax.swing.JMenuItem mn84;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JToolBar tb1;
    private javax.swing.JToolBar tb2;
    private javax.swing.JToolBar tb4;
    private javax.swing.JToolBar td3;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 

    public class FrameListener2 implements FrameListener {

        public FrameListener2() {
            if (Util.progressFrame) {
                //прогресс бар это специальная дополнительная форма
                FrameProgress.create(App1.this, this);
            } else {
                //прогресс бар находится на самой форме
                new SwingWorker() {

                    protected Object doInBackground() throws Exception {
                        progressBar.setIndeterminate(true);

                        //это создание формы
                        request(null);
                        return null;
                    }

                    public void done() {
                        progressBar.setIndeterminate(false);
                    }
                }.execute();
            }
        }

        //тут то в наследнике и создаётся форма
        public void request(Object obj) {
        }

        public void response(Object obj) {
        }
    }

    public enum eApp1 {

        App1, Convert, Rate, Color, Artikls, Joining, Design, Elements, Param, 
        Glass, Furn, Kits, Sysprof, Partner, Order, AboutBox;
        java.awt.Frame frame;

        public void createFrame(java.awt.Window parent, Object... param) {
            if (frame == null) {
                switch (this) {
                    case AboutBox:
                        frame = new AboutBox();
                        break;
                    case Artikls:
                        frame = new Artikls();
                        break;
                    case Convert:
                        frame = new Convert();
                        break;
                    case Color:
                        frame = new Color();
                        break;
                    case Joining:
                        frame = new Joining();
                        break;
                    case Rate:
                        frame = new Rate();
                        break;
                    case Design:
                        frame = new Design();
                        break;
                    case Elements:
                        frame = new Element();
                        break;
                    case Param:
                        frame = new Param();
                        break;
                    case Glass:
                        frame = new Glass();
                        break;
                    case Furn:
                        frame = new Furn();
                        break;
                    case Kits:
                        frame = new Kits();
                        break;
                    case Sysprof:
                        frame = new Sysprof();
                        break;
                    case Partner:
                        frame = new Partner();
                        break;
                    case Order:
                        frame = new Order();
                        break;
                }
                frame.setName(this.name());
                FrameToFile.setFrameSize(frame);
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowDeiconified(java.awt.event.WindowEvent evt) {
                        App1.frame.setExtendedState(JFrame.NORMAL);
                    }
                });
            }
            frame.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage());
            frame.setVisible(true);
        }

        public static void createApp(eProfile profile) {

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (profile.equals(eProfile.P16)) {
                eProp.open_dict = false;
                eProfile.role_user = "user";
                App1.frame = new App1();
                App1.frame.setName(eProfile.P16.name());
            }
            App1.frame.setLocation(0, 0);
            App1.frame.setSize(screenSize.width, App1.frame.getHeight());
            App1.frame.setVisible(true);
        }

        public static void disposeFrame() {
            for (eApp1 e : values()) {
                if (e.frame != null) {
                    e.frame.setState(JFrame.ICONIFIED);
                }
            }
        }
    }
}
