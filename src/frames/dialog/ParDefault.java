package frames.dialog;

import frames.FrameToFile;
import frames.Uti5;
import dataset.Query;
import dataset.Record;
import domain.eParams;
import frames.swing.DefTableModel;
import frames.swing.listener.ListenerRecord;

public class ParDefault extends javax.swing.JDialog {

    private static final int serialVersionUID = 2067173679;
    private Query qParam = new Query(eParams.up.values());
    private ListenerRecord listener;
    
    public ParDefault(java.awt.Frame parent, ListenerRecord listener) {
        super(parent, true);
        initComponents();
        initElements();
        this.listener = listener;
        loadingModel(null);
        setVisible(true);        
    }
    
    public ParDefault(java.awt.Frame parent, ListenerRecord listener, int grup) {
        super(parent, true);
        initComponents();
        initElements();
        this.listener = listener;
        loadingModel(grup);
        setVisible(true);       
    }
    
    private void loadingModel(Integer grup) {

        if(grup == null) {
          qParam.select(eParams.up, "where", eParams.id, "=", eParams.params_id, "order by", eParams.text);
        } else {
          qParam.select(eParams.up, "where", eParams.params_id, "=", grup, "and", eParams.id, "!=", eParams.params_id, "order by", eParams.text);  
        }
        tab1.setModel(new DefTableModel(tab1, qParam, eParams.id, eParams.text));
        ((DefTableModel) tab1.getModel()).fireTableDataChanged();
        Uti5.setSelectedRow(tab1);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pannorth = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnChouce = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        south = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Справочник параметров");

        pannorth.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pannorth.setMaximumSize(new java.awt.Dimension(32767, 31));
        pannorth.setPreferredSize(new java.awt.Dimension(300, 29));

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

        btnChouce.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c044.gif"))); // NOI18N
        btnChouce.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnChouce.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnChouce.setFocusable(false);
        btnChouce.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChouce.setMaximumSize(new java.awt.Dimension(25, 25));
        btnChouce.setMinimumSize(new java.awt.Dimension(25, 25));
        btnChouce.setPreferredSize(new java.awt.Dimension(25, 25));
        btnChouce.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChouce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChouce(evt);
            }
        });

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c042.gif"))); // NOI18N
        btnRemove.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnRemove.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove(evt);
            }
        });

        javax.swing.GroupLayout pannorthLayout = new javax.swing.GroupLayout(pannorth);
        pannorth.setLayout(pannorthLayout);
        pannorthLayout.setHorizontalGroup(
            pannorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pannorthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChouce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pannorthLayout.setVerticalGroup(
            pannorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pannorthLayout.createSequentialGroup()
                .addGroup(pannorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pannorthLayout.createSequentialGroup()
                        .addGroup(pannorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnChouce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(pannorth, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(300, 500));
        centr.setLayout(new java.awt.BorderLayout());

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "name1"},
                {null, "name2"}
            },
            new String [] {
                "Код", "Наименование"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab1.setFillsViewportHeight(true);
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(0).setPreferredWidth(40);
            tab1.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        centr.add(scr1, java.awt.BorderLayout.CENTER);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(300, 20));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
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

    private void btnChouce(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChouce
        Record record = qParam.get(Uti5.getIndexRec(tab1));
        listener.action(record);
        this.dispose();
    }//GEN-LAST:event_btnChouce

    private void btnRemove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove
        Record record = eParams.up.newRecord();
        listener.action(record);
        this.dispose();
    }//GEN-LAST:event_btnRemove

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        if (evt.getClickCount() == 2) {
            btnChouce(null);
        }
    }//GEN-LAST:event_tab1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChouce;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRemove;
    private javax.swing.JPanel centr;
    private javax.swing.JPanel pannorth;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    // End of variables declaration//GEN-END:variables
    private void initElements() {
        FrameToFile.setFrameSize(this);
        new FrameToFile(this, btnClose);
    }
}
