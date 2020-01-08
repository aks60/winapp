package frames;

import javax.swing.JButton;
import script.Area;
import script.ScriptBuild;

public class Design extends javax.swing.JFrame {

    private Area rootArea;

    public Design() {
        initComponents();
        initElements();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        centr = new javax.swing.JPanel();
        south = new javax.swing.JPanel();
        west = new javax.swing.JPanel();
        east = new javax.swing.JPanel();
        panNorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        panSouth = new javax.swing.JPanel();
        panCentr = new javax.swing.JPanel();
        design = new javax.swing.JPanel();
        panNorth2 = new javax.swing.JPanel();
        panSouth2 = new javax.swing.JPanel();
        panEast2 = new javax.swing.JPanel();
        panWest2 = new javax.swing.JPanel();
        panEast = new javax.swing.JPanel();
        btnSquare = new javax.swing.JButton();
        btnArch3 = new javax.swing.JButton();
        btnArch2 = new javax.swing.JButton();
        btnTrapeze = new javax.swing.JButton();
        btnTrapeze2 = new javax.swing.JButton();
        btnSquare1 = new javax.swing.JButton();
        panWest = new javax.swing.JPanel();
        btnImpostVert = new javax.swing.JButton();
        btnImpostGoriz = new javax.swing.JButton();
        btnSquare4 = new javax.swing.JButton();
        btnSquare5 = new javax.swing.JButton();

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout centrLayout = new javax.swing.GroupLayout(centr);
        centr.setLayout(centrLayout);
        centrLayout.setHorizontalGroup(
            centrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
        centrLayout.setVerticalGroup(
            centrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout westLayout = new javax.swing.GroupLayout(west);
        west.setLayout(westLayout);
        westLayout.setHorizontalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        westLayout.setVerticalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout eastLayout = new javax.swing.GroupLayout(east);
        east.setLayout(eastLayout);
        eastLayout.setHorizontalGroup(
            eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        eastLayout.setVerticalGroup(
            eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 600));

        panNorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panNorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        panNorth.setPreferredSize(new java.awt.Dimension(800, 29));

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
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 412, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panNorthLayout.setVerticalGroup(
            panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNorthLayout.createSequentialGroup()
                .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(panNorth, java.awt.BorderLayout.NORTH);

        panSouth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panSouth.setMinimumSize(new java.awt.Dimension(100, 20));
        panSouth.setPreferredSize(new java.awt.Dimension(800, 40));

        javax.swing.GroupLayout panSouthLayout = new javax.swing.GroupLayout(panSouth);
        panSouth.setLayout(panSouthLayout);
        panSouthLayout.setHorizontalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );
        panSouthLayout.setVerticalGroup(
            panSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        getContentPane().add(panSouth, java.awt.BorderLayout.SOUTH);

        panCentr.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panCentr.setLayout(new java.awt.BorderLayout());

        design.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 255, 255)));

        javax.swing.GroupLayout designLayout = new javax.swing.GroupLayout(design);
        design.setLayout(designLayout);
        designLayout.setHorizontalGroup(
            designLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 377, Short.MAX_VALUE)
        );
        designLayout.setVerticalGroup(
            designLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        panCentr.add(design, java.awt.BorderLayout.CENTER);

        panNorth2.setPreferredSize(new java.awt.Dimension(700, 60));

        javax.swing.GroupLayout panNorth2Layout = new javax.swing.GroupLayout(panNorth2);
        panNorth2.setLayout(panNorth2Layout);
        panNorth2Layout.setHorizontalGroup(
            panNorth2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );
        panNorth2Layout.setVerticalGroup(
            panNorth2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        panCentr.add(panNorth2, java.awt.BorderLayout.PAGE_START);

        panSouth2.setPreferredSize(new java.awt.Dimension(744, 60));

        javax.swing.GroupLayout panSouth2Layout = new javax.swing.GroupLayout(panSouth2);
        panSouth2.setLayout(panSouth2Layout);
        panSouth2Layout.setHorizontalGroup(
            panSouth2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );
        panSouth2Layout.setVerticalGroup(
            panSouth2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        panCentr.add(panSouth2, java.awt.BorderLayout.SOUTH);

        panEast2.setPreferredSize(new java.awt.Dimension(60, 376));

        javax.swing.GroupLayout panEast2Layout = new javax.swing.GroupLayout(panEast2);
        panEast2.setLayout(panEast2Layout);
        panEast2Layout.setHorizontalGroup(
            panEast2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panEast2Layout.setVerticalGroup(
            panEast2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 352, Short.MAX_VALUE)
        );

        panCentr.add(panEast2, java.awt.BorderLayout.LINE_END);

        panWest2.setPreferredSize(new java.awt.Dimension(60, 336));

        javax.swing.GroupLayout panWest2Layout = new javax.swing.GroupLayout(panWest2);
        panWest2.setLayout(panWest2Layout);
        panWest2Layout.setHorizontalGroup(
            panWest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panWest2Layout.setVerticalGroup(
            panWest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 352, Short.MAX_VALUE)
        );

        panCentr.add(panWest2, java.awt.BorderLayout.LINE_START);

        getContentPane().add(panCentr, java.awt.BorderLayout.CENTER);

        panEast.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panEast.setPreferredSize(new java.awt.Dimension(38, 500));
        panEast.setLayout(new javax.swing.BoxLayout(panEast, javax.swing.BoxLayout.Y_AXIS));

        btnSquare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare.setName("areaSquare"); // NOI18N
        btnSquare.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        panEast.add(btnSquare);

        btnArch3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d030.gif"))); // NOI18N
        btnArch3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch3.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch3.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch3.setName("areaArch"); // NOI18N
        btnArch3.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        panEast.add(btnArch3);

        btnArch2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d031.gif"))); // NOI18N
        btnArch2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnArch2.setMaximumSize(new java.awt.Dimension(32, 32));
        btnArch2.setMinimumSize(new java.awt.Dimension(32, 32));
        btnArch2.setName("areaArch2"); // NOI18N
        btnArch2.setPreferredSize(new java.awt.Dimension(32, 32));
        btnArch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        panEast.add(btnArch2);

        btnTrapeze.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d028.gif"))); // NOI18N
        btnTrapeze.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze.setName("areaTrapeze"); // NOI18N
        btnTrapeze.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        panEast.add(btnTrapeze);

        btnTrapeze2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d027.gif"))); // NOI18N
        btnTrapeze2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnTrapeze2.setMaximumSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.setMinimumSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.setName("areaTrapeze2"); // NOI18N
        btnTrapeze2.setPreferredSize(new java.awt.Dimension(32, 32));
        btnTrapeze2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        panEast.add(btnTrapeze2);

        btnSquare1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d024.gif"))); // NOI18N
        btnSquare1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare1.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare1.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare1.setName("areaSquare"); // NOI18N
        btnSquare1.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSquare1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArea(evt);
            }
        });
        panEast.add(btnSquare1);

        getContentPane().add(panEast, java.awt.BorderLayout.EAST);

        panWest.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panWest.setPreferredSize(new java.awt.Dimension(38, 500));
        panWest.setLayout(new javax.swing.BoxLayout(panWest, javax.swing.BoxLayout.Y_AXIS));

        btnImpostVert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d023.gif"))); // NOI18N
        btnImpostVert.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostVert.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostVert.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostVert.setName("impostVert"); // NOI18N
        btnImpostVert.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostVert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElem(evt);
            }
        });
        panWest.add(btnImpostVert);

        btnImpostGoriz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d022.gif"))); // NOI18N
        btnImpostGoriz.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnImpostGoriz.setMaximumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.setMinimumSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.setName("impostGoriz"); // NOI18N
        btnImpostGoriz.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImpostGoriz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElem(evt);
            }
        });
        panWest.add(btnImpostGoriz);

        btnSquare4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare4.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare4.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare4.setName("areaSquare"); // NOI18N
        btnSquare4.setPreferredSize(new java.awt.Dimension(32, 32));
        panWest.add(btnSquare4);

        btnSquare5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d032.gif"))); // NOI18N
        btnSquare5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSquare5.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSquare5.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSquare5.setName("areaSquare"); // NOI18N
        btnSquare5.setPreferredSize(new java.awt.Dimension(32, 32));
        panWest.add(btnSquare5);

        getContentPane().add(panWest, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClose
        this.dispose();
    }//GEN-LAST:event_btnCloseClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh

    }//GEN-LAST:event_btnRefresh

    private void btnSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave

    }//GEN-LAST:event_btnSave

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

    }//GEN-LAST:event_btnInsert

    private void btnArea(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArea
        JButton btn = (JButton) evt.getSource();
        ScriptBuild.addArea(btn.getName());
    }//GEN-LAST:event_btnArea

    private void btnElem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElem
        JButton btn = (JButton) evt.getSource();
        ScriptBuild.addElem(btn.getName());
    }//GEN-LAST:event_btnElem

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArch2;
    private javax.swing.JButton btnArch3;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnImpostGoriz;
    private javax.swing.JButton btnImpostVert;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSquare;
    private javax.swing.JButton btnSquare1;
    private javax.swing.JButton btnSquare4;
    private javax.swing.JButton btnSquare5;
    private javax.swing.JButton btnTrapeze;
    private javax.swing.JButton btnTrapeze2;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel design;
    private javax.swing.JPanel east;
    private javax.swing.JPanel north;
    private javax.swing.JPanel panCentr;
    private javax.swing.JPanel panEast;
    private javax.swing.JPanel panEast2;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panNorth2;
    private javax.swing.JPanel panSouth;
    private javax.swing.JPanel panSouth2;
    private javax.swing.JPanel panWest;
    private javax.swing.JPanel panWest2;
    private javax.swing.JPanel south;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables

    private void initElements() {
    }
// </editor-fold> 
}
