package frames;

import frames.swing.listener.ListenerFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingWorker;

public class FrameProgress extends javax.swing.JDialog {

    public static FrameProgress progress = null;
    private static ListenerFrame listener = null;

    public FrameProgress(java.awt.Window owner, ListenerFrame listener) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        this.listener = listener;
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight() - 48) / 2 + 48);
        progressBar.setIndeterminate(true);
        startupProgress();
    }

    public static void create(java.awt.Window owner, ListenerFrame listener) {
        progress = new FrameProgress(owner, listener);
        progress.setVisible(true);
    }

    private static void startupProgress() {

        new SwingWorker() {

            protected Object doInBackground() throws Exception {
                listener.actionRequest(null);
                return null;
            }

            public void done() {
                progressBar.setIndeterminate(false);
                listener.actionResponse(null);
                progress.dispose();
            }
        }.execute();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lab = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(300, 34));
        setUndecorated(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(0, 0, 255), null));
        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 30));
        jPanel1.setLayout(new java.awt.BorderLayout());

        lab.setBackground(new java.awt.Color(85, 85, 255));
        lab.setFont(frames.Uti4.getFont(0,1));
        lab.setForeground(new java.awt.Color(255, 255, 51));
        lab.setText("    Пожалуйста, подождите...");
        lab.setFocusable(false);
        lab.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lab.setMinimumSize(new java.awt.Dimension(250, 12));
        lab.setOpaque(true);
        jPanel1.add(lab, java.awt.BorderLayout.NORTH);

        progressBar.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        progressBar.setMinimumSize(new java.awt.Dimension(360, 8));
        progressBar.setPreferredSize(new java.awt.Dimension(360, 8));
        jPanel1.add(progressBar, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(85, 85, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 12));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(360, 12));
        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel3.setBackground(new java.awt.Color(85, 85, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setBackground(new java.awt.Color(85, 85, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.add(jPanel4, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lab;
    public static javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
