package frames;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import common.FrameListener;
import common.FrameProgress;
import common.FrameToFile;
import common.eProperty;
import dataset.Record;
import domain.eArtikl;
import domain.eElemdet;
import domain.eFurndet;
import domain.eGlasdet;
import domain.eJoindet;
import domain.eModels;
import domain.eSystree;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import estimate.Wincalc;
import estimate.constr.Specification;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import static java.util.stream.Collectors.toList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import startup.App1;

public class Specific extends javax.swing.JFrame {

    private DecimalFormat df0 = new DecimalFormat("#0");
    private DecimalFormat df1 = new DecimalFormat("#0.0");
    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private DecimalFormat df3 = new DecimalFormat("#0.000");
    private estimate.Wincalc iwin = null;

    public Specific() {
        initComponents();
        int nuni = Integer.valueOf(eProperty.systree_nuni.read());
        Record record = eSystree.find(nuni);
        if (record.getInt(eSystree.models_id) == -1) {
            JOptionPane.showMessageDialog(this, "Выберите конструкцию в системе профилей", "Предупреждение", JOptionPane.OK_OPTION);;
        } else {
            initElements();
            createIwin();
            loadingData(iwin.listSpec);
        }
    }

    private void createIwin() {

        iwin = new Wincalc();
        int nuni = Integer.valueOf(eProperty.systree_nuni.read());
        setTitle("Спецификация.    Система: " + eSystree.patch(nuni, ""));
        Record record = eSystree.find(nuni);
        int models_id = record.getInt(eSystree.models_id);
        Record record2 = eModels.find(models_id);
        if (record2 != null) {
            String script = record2.getStr(eModels.script);
            if (script.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Выберите конструкцию в системе профилей", "Предупреждение", JOptionPane.OK_OPTION);
                //this.dispose();
            } else {
                JsonElement je = new Gson().fromJson(script, JsonElement.class);
                je.getAsJsonObject().addProperty("nuni", nuni);
                iwin.build(je.toString());
                iwin.constructiv();
            }
        }
    }

    private void loadingData(List<Specification> listSpec) {
        DefaultTableModel dtm = ((DefaultTableModel) tab1.getModel());
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tab1.getModel());
        tab1.setRowSorter(sorter);
        dtm.getDataVector().clear();
        int insexLast = listSpec.get(0).getVector(0).size();
        float sum1 = 0, sum2 = 0, sum3 = 0;
        for (int i = 0; i < listSpec.size(); i++) { //заполним спецификацию
            Vector v = listSpec.get(i).getVector(i);
            dtm.addRow(v);
            sum1 = sum1 + (Float) v.get(insexLast - 1);
            sum2 = sum2 + (Float) v.get(insexLast - 2);
            sum3 = sum3 + (Float) v.get(insexLast - 13);
        }
        Vector vectorLast = new Vector();
        for (int i = 0; i < insexLast; i++) {
            vectorLast.add(null);
        }
        vectorLast.set(insexLast - 1, sum1);
        vectorLast.set(insexLast - 2, sum2);
        vectorLast.set(insexLast - 13, sum3);
        dtm.addRow(vectorLast);
    }

    private List<Specification> groups() {
        HashSet<String> hs = new HashSet();
        List<Specification> list = new ArrayList();
        Map<String, Specification> map = new HashMap();
        for (Specification spc : iwin.listSpec) {
            String key = spc.name + spc.artikl + spc.colorID1 + spc.colorID2 + spc.colorID3 + spc.anglCut1
                    + spc.width + spc.height + spc.anglCut2 + spc.unit + spc.wastePrc + spc.inPrice + spc.discount;
            if (hs.add(key)) {
                map.put(key, new Specification(spc));
            } else {
                Specification s = map.get(key);
                s.weight = s.weight + spc.weight;
                s.anglCut1 = 0;
                s.anglCut2 = 0;
                s.anglHoriz = 0;
                s.count = s.count + spc.count;
                s.quantity = s.quantity + spc.quantity;
                s.quantity2 = s.quantity2 + spc.quantity2;
                s.outPrice = s.outPrice + spc.outPrice;
                s.inCost = s.inCost + spc.inCost;
                s.outCost = s.outCost + spc.outCost;
            }
        }
        map.entrySet().forEach(act -> list.add(act.getValue()));
        Collections.sort(list, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name).compareTo(o2.place.subSequence(0, 3) + o2.name));
        return list;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnArtikles = new javax.swing.JButton();
        btnConstructiv = new javax.swing.JButton();
        btnAll = new javax.swing.JButton();
        btnJon = new javax.swing.JButton();
        btnFix = new javax.swing.JButton();
        btnFill = new javax.swing.JButton();
        btnFurn = new javax.swing.JButton();
        btnFurn1 = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Спецификация");

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

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport.setFocusable(false);
        btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
            }
        });

        btnArtikles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c021.gif"))); // NOI18N
        btnArtikles.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnArtikles.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArtikles.setFocusable(false);
        btnArtikles.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnArtikles.setMaximumSize(new java.awt.Dimension(25, 25));
        btnArtikles.setMinimumSize(new java.awt.Dimension(25, 25));
        btnArtikles.setPreferredSize(new java.awt.Dimension(25, 25));
        btnArtikles.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnArtikles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtikles(evt);
            }
        });

        btnConstructiv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c055.gif"))); // NOI18N
        btnConstructiv.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnConstructiv.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnConstructiv.setFocusable(false);
        btnConstructiv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConstructiv.setMaximumSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setMinimumSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setPreferredSize(new java.awt.Dimension(25, 25));
        btnConstructiv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConstructiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConstructiv(evt);
            }
        });

        btnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c006.gif"))); // NOI18N
        btnAll.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnAll.setFocusable(false);
        btnAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAll.setMaximumSize(new java.awt.Dimension(26, 26));
        btnAll.setMinimumSize(new java.awt.Dimension(26, 26));
        btnAll.setPreferredSize(new java.awt.Dimension(26, 26));
        btnAll.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAll(evt);
            }
        });

        btnJon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c024.gif"))); // NOI18N
        btnJon.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnJon.setFocusable(false);
        btnJon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnJon.setMaximumSize(new java.awt.Dimension(26, 26));
        btnJon.setMinimumSize(new java.awt.Dimension(26, 26));
        btnJon.setPreferredSize(new java.awt.Dimension(26, 26));
        btnJon.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnJon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilter(evt);
            }
        });

        btnFix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c025.gif"))); // NOI18N
        btnFix.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFix.setFocusable(false);
        btnFix.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFix.setMaximumSize(new java.awt.Dimension(26, 26));
        btnFix.setMinimumSize(new java.awt.Dimension(26, 26));
        btnFix.setPreferredSize(new java.awt.Dimension(26, 26));
        btnFix.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnFix.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilter(evt);
            }
        });

        btnFill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c026.gif"))); // NOI18N
        btnFill.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFill.setFocusable(false);
        btnFill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFill.setMaximumSize(new java.awt.Dimension(26, 26));
        btnFill.setMinimumSize(new java.awt.Dimension(26, 26));
        btnFill.setPreferredSize(new java.awt.Dimension(26, 26));
        btnFill.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnFill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilter(evt);
            }
        });

        btnFurn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c027.gif"))); // NOI18N
        btnFurn.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFurn.setFocusable(false);
        btnFurn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFurn.setMaximumSize(new java.awt.Dimension(26, 26));
        btnFurn.setMinimumSize(new java.awt.Dimension(26, 26));
        btnFurn.setPreferredSize(new java.awt.Dimension(26, 26));
        btnFurn.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnFurn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilter(evt);
            }
        });

        btnFurn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c017.gif"))); // NOI18N
        btnFurn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFurn1.setFocusable(false);
        btnFurn1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFurn1.setMaximumSize(new java.awt.Dimension(26, 26));
        btnFurn1.setMinimumSize(new java.awt.Dimension(26, 26));
        btnFurn1.setPreferredSize(new java.awt.Dimension(26, 26));
        btnFurn1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c001.gif"))); // NOI18N
        btnFurn1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFurn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroups(evt);
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
                .addGap(18, 18, 18)
                .addComponent(btnArtikles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConstructiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnJon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFurn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFurn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
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
                    .addComponent(btnArtikles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConstructiv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnFurn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFurn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnJon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(900, 500));
        centr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);
        scr1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {"", "", "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nпп", "<HTML>ID</HTML>", "Расположенние", "Артикул", "Наименование", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Масса", "<html>Угол <br/>  1", "<html>Угол<br/>  2", "<html>Угол к<br/> горизонту", "<html>Кол.<br/>единиц", "Ед.изм", "<html>Кол.без<br/>отхода", "<html>Процент<br/> отх.", "<html>Кол. с <br/>отходом", "<html>Себест.<br/> за ед. измерения", "<html>Себест.<br/> с отх.", "<html>Стоим.<br/> без_скидки", "<html>Стоим. <br/>со_скидкой"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tab1.setFillsViewportHeight(true);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Specific.this.mousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(30);
            tab1.getColumnModel().getColumn(0).setMaxWidth(60);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setMaxWidth(60);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(46);
            tab1.getColumnModel().getColumn(2).setMaxWidth(60);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(120);
            tab1.getColumnModel().getColumn(4).setPreferredWidth(220);
            tab1.getColumnModel().getColumn(8).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(9).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(10).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(11).setPreferredWidth(30);
            tab1.getColumnModel().getColumn(12).setPreferredWidth(30);
            tab1.getColumnModel().getColumn(13).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(14).setPreferredWidth(20);
            tab1.getColumnModel().getColumn(15).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(16).setPreferredWidth(46);
            tab1.getColumnModel().getColumn(17).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(18).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(19).setPreferredWidth(44);
            tab1.getColumnModel().getColumn(20).setPreferredWidth(44);
            tab1.getColumnModel().getColumn(21).setPreferredWidth(52);
            tab1.getColumnModel().getColumn(22).setPreferredWidth(52);
        }

        centr.add(scr1, java.awt.BorderLayout.CENTER);

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
                filterUpdate(evt);
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

    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void filterUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterUpdate

        JTable table = tab1;
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((TableRowSorter<TableModel>) tab1.getRowSorter()).setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((TableRowSorter<TableModel>) tab1.getRowSorter()).setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterUpdate

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_mousePressed

    private void btnArtikles(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtikles

        int row = Util.getSelectedRec(tab1);
        Specification recordSpc = iwin.listSpec.get(row);
        int artId = recordSpc.artiklRec.getInt(eArtikl.id);
        FrameProgress.create(this, new FrameListener() {
            public void actionRequest(Object obj) {
                App1.eApp1.Artikles.createFrame(Specific.this, artId);
            }
        });
    }//GEN-LAST:event_btnArtikles

    private void btnConstructiv(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConstructiv
        int row = Util.getSelectedRec(tab1);
        Specification recordSpc = iwin.listSpec.get(row);
        String str = recordSpc.place.substring(0, 3);
        Record recordDet = recordSpc.detailRec;
        if (recordDet != null) {
            FrameProgress.create(Specific.this, new FrameListener() {
                public void actionRequest(Object obj) {
                    if (str.equals("ВСТ")) {
                        App1.eApp1.Element.createFrame(Specific.this, iwin.calcElements.listVariants, recordDet.getInt(eElemdet.id));

                    } else if (str.equals("СОЕ")) {
                        App1.eApp1.Joining.createFrame(Specific.this, iwin.calcJoining.listVariants, recordDet.getInt(eJoindet.id));

                    } else if (str.equals("ЗАП")) {
                        App1.eApp1.Filling.createFrame(Specific.this, iwin.calcFilling.listVariants, recordDet.getInt(eGlasdet.id));

                    } else if (str.equals("ФУР")) {
                        App1.eApp1.Furniture.createFrame(Specific.this, iwin.calcFurniture.listVariants, recordDet.getInt(eFurndet.id));
                    }
                }
            });
        } else {
            FrameProgress.create(Specific.this, new FrameListener() {
                public void actionRequest(Object obj) {
                    if (str.equals("ВСТ")) {
                        App1.eApp1.Systree.createFrame(Specific.this, recordSpc.artiklRec.getInt(eArtikl.id));

                    } else if (str.equals("ЗАП")) {
                        App1.eApp1.Systree.createFrame(Specific.this);
                    }
                }
            });
        }
    }//GEN-LAST:event_btnConstructiv

    private void btnFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilter
        JButton tab = (JButton) evt.getSource();
        List<Specification> listSpec = null;
        if (tab == btnJon) {
            listSpec = iwin.listSpec.stream().filter(rec -> "СОЕ".equals(rec.place.substring(0, 3))).collect(toList());
        } else if (tab == btnFix) {
            listSpec = iwin.listSpec.stream().filter(rec -> "ВСТ".equals(rec.place.substring(0, 3))).collect(toList());
        } else if (tab == btnFill) {
            listSpec = iwin.listSpec.stream().filter(rec -> "ЗАП".equals(rec.place.substring(0, 3))).collect(toList());
        } else if (tab == btnFurn) {
            listSpec = iwin.listSpec.stream().filter(rec -> "ФУР".equals(rec.place.substring(0, 3))).collect(toList());
        }
        loadingData(listSpec);
    }//GEN-LAST:event_btnFilter

    private void btnGroups(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroups
        loadingData(groups());
    }//GEN-LAST:event_btnGroups

    private void btnAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAll
        loadingData(iwin.listSpec);
    }//GEN-LAST:event_btnAll

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnArtikles;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConstructiv;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnFill;
    private javax.swing.JButton btnFix;
    private javax.swing.JButton btnFurn;
    private javax.swing.JButton btnFurn1;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnJon;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        tab1.getTableHeader().setPreferredSize(new Dimension(0, 32));
        DefaultTableCellRenderer cellRenderer0 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    if (Float.valueOf(value.toString()) > 0) {
                        value = df0.format(value);
                    }
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        DefaultTableCellRenderer cellRenderer1 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    if (Float.valueOf(value.toString()) > 0) {
                        value = df1.format(value);
                    }
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        DefaultTableCellRenderer cellRenderer2 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    if (Float.valueOf(value.toString()) > 0) {
                        value = df2.format(value);
                    }
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        DefaultTableCellRenderer cellRenderer3 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    if (Float.valueOf(value.toString()) > 0) {
                        value = df3.format(value);
                    }
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return label;
            }
        };
        tab1.getColumnModel().getColumn(8).setCellRenderer(cellRenderer1);
        tab1.getColumnModel().getColumn(9).setCellRenderer(cellRenderer1);
        tab1.getColumnModel().getColumn(10).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(11).setCellRenderer(cellRenderer0);
        tab1.getColumnModel().getColumn(12).setCellRenderer(cellRenderer0);
        tab1.getColumnModel().getColumn(13).setCellRenderer(cellRenderer0);
        tab1.getColumnModel().getColumn(16).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(17).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(18).setCellRenderer(cellRenderer3);
        tab1.getColumnModel().getColumn(19).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(20).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(21).setCellRenderer(cellRenderer2);
        tab1.getColumnModel().getColumn(22).setCellRenderer(cellRenderer2);
        FocusListener listenerFocus = new FocusListener() {

            javax.swing.border.Border border = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255));

            public void focusGained(FocusEvent e) {
                JTable table = (JTable) e.getSource();
                table.setBorder(border);
//            tabList.add(table);
//            tabActive = table;
//            tmActive = (TableModel) table.getModel();
                btnIns.setEnabled(true);
//            if (table != treeMat) {
//                btnDel.setEnabled(true);
//            }
            }

            public void focusLost(FocusEvent e) {
                JTable table = (JTable) e.getSource();
                table.setBorder(null);
                btnIns.setEnabled(false);
                btnDel.setEnabled(false);
            }
        };
        tab1.addFocusListener(listenerFocus);
    }
}
