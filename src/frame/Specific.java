package frame;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import common.FrameListener;
import common.FrameToFile;
import common.eProperty;
import dataset.Query;
import dataset.Record;
import domain.eSysprod;
import domain.eSystree;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collections;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import wincalc.Wincalc;
import wincalc.constr.Specification;

public class Specific extends javax.swing.JFrame {

    private wincalc.Wincalc iwin = null;
    private Window owner = null;

    public Specific() {
        initComponents();
        initElements();
        createWincalc();
        loadingData();
        loadingModel();
    }

    public Specific(java.awt.Window owner, Wincalc iwin) {
        initComponents();
        initElements();
        this.owner = owner;
        this.iwin = iwin;
        owner.setEnabled(false);
        loadingData();
        loadingModel();
    }

    private void loadingData() {

        iwin.constructiv();
        Collections.sort(iwin.listSpec, (o1, o2) -> Float.compare(o1.id, o2.id));
    }

    private void loadingModel() {
        DefaultTableModel dtm = ((DefaultTableModel) tab1.getModel());
        dtm.getDataVector().clear();
        for (Specification specRec : iwin.listSpec) {
            Vector vector = specRec.getVector();
            dtm.addRow(vector);
        }        
    }
    
    private void createWincalc() {

        iwin = new Wincalc();
        int nuni = Integer.valueOf(eProperty.systree_nuni.read());
        setTitle(getTitle() + ".    Система: <<" + eSystree.patch(nuni) + ">>");
        Record record = eSystree.find(nuni);
        int sysprod_id = record.getInt(eSystree.sysprod_id);
        Record record2 = eSysprod.find(sysprod_id);
        if (record2 != null) {
            String script = record2.getStr(eSysprod.script);
            JsonElement je = new Gson().fromJson(script, JsonElement.class);
            je.getAsJsonObject().addProperty("nuni", nuni);
            iwin.create(je.toString());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        panSouth = new javax.swing.JPanel();
        panCentr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Спецификация");
        setPreferredSize(new java.awt.Dimension(900, 449));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Specific.this.windowClosed(evt);
            }
        });

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(900, 29));

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
                btnCloseClose(evt);
            }
        });

        btnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c020.gif"))); // NOI18N
        btnHelp.setToolTipText(bundle.getString("Справка")); // NOI18N
        btnHelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnHelp.setFocusable(false);
        btnHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHelp.setMaximumSize(new java.awt.Dimension(25, 25));
        btnHelp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnHelp.setPreferredSize(new java.awt.Dimension(25, 25));
        btnHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelp(evt);
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

        btnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        btnFind.setToolTipText(bundle.getString("Поиск")); // NOI18N
        btnFind.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFind.setFocusable(false);
        btnFind.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFind.setMaximumSize(new java.awt.Dimension(25, 25));
        btnFind.setMinimumSize(new java.awt.Dimension(25, 25));
        btnFind.setPreferredSize(new java.awt.Dimension(25, 25));
        btnFind.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindChoice(evt);
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
                btnReportChoice(evt);
            }
        });

        javax.swing.GroupLayout panNorthLayout = new javax.swing.GroupLayout(panNorth);
        panNorth.setLayout(panNorthLayout);
        panNorthLayout.setHorizontalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125)
                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 532, Short.MAX_VALUE)
                .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHelp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(900, 20));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 884, Short.MAX_VALUE)
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        panCentr.setPreferredSize(new java.awt.Dimension(900, 400));
        panCentr.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(null);

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", "", "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {"", "", "", "", "", "", "", "", "", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "ID area", "ID elem", "TypeElem", "Расп.", "Артикул", "Наимен.", "Текстура", "Внутренняя", "Внешняя", "Длина", "Ширина", "Масса", "Угол1", "Угол2", "Кол.единиц", "Ед.изм", "Кол. без отхода", "Процент отхода", "Кол. с отходом", "Собес-сть за ед. изм.", "Собест. с отх", "Стоим. без скидки", "Стоим. со скидкой", "Скидка", "Угол к гор."
            }
        ));
        tab1.setFillsViewportHeight(true);
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(5).setMinWidth(120);
            tab1.getColumnModel().getColumn(6).setMinWidth(169);
            tab1.getColumnModel().getColumn(10).setMinWidth(40);
            tab1.getColumnModel().getColumn(11).setMinWidth(40);
        }

        panCentr.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelp
        //ExecuteCmd.startHelp(this.getClass().getName());
    }//GEN-LAST:event_btnHelp

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh

    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnFindChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindChoice

    }//GEN-LAST:event_btnFindChoice

    private void btnReportChoice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportChoice

    }//GEN-LAST:event_btnReportChoice

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        if (owner != null)
            owner.setEnabled(true);
    }//GEN-LAST:event_windowClosed

// <editor-fold defaultstate="collapsed" desc="Generated Code">     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JTable tab1;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
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
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Заказы клиентов", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
    }
}
