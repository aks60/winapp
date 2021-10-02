package frames.swing;

import builder.Wincalc;
import builder.script.GsonElem;
import builder.script.GsonScale;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import frames.swing.listener.ListenerObject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class Scene extends javax.swing.JPanel {

    public ListenerObject listenerGson = null;
    private Gson gson = new GsonBuilder().create();
    private DecimalFormat df1 = new DecimalFormat("#0.#");
    private Wincalc iwin = null;
    private Canvas canvas = null;

    public List<GsonScale> lineHoriz = null;
    public List<GsonScale> lineVert = null;

    private float areaId = 0;
    private int sizeArea = 1350;

    public Scene(Canvas canvas, ListenerObject listenerGson) {
        initComponents();
        initElements();
        this.canvas = canvas;
        this.listenerGson = listenerGson;
        add(canvas, java.awt.BorderLayout.CENTER);
        this.canvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                lineHoriz.forEach(it -> it.color = GsonScale.BLACK);
                lineVert.forEach(it -> it.color = GsonScale.BLACK);
                draw();
            }
        });
    }

    public void init(Wincalc iwin) {
        this.iwin = iwin;
        if (iwin != null) {
            lineHoriz = iwin.rootGson.lineArea(iwin, Layout.HORIZ);
            lineVert = iwin.rootGson.lineArea(iwin, Layout.VERT);
        }
        canvas.init(iwin);
    }

    public void draw() {
        pan1.repaint();
        pan4.repaint();
    }

    private void paintHorizontal(Graphics gc) {
        if (iwin != null) {
            Graphics2D g = (Graphics2D) gc;
            g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, sizeFont()));
            int x = 20;
            for (GsonScale elem : lineHoriz) {
                int dx = (int) (elem.width() * iwin.scale);
                g.drawLine(x + dx, 10, x + dx, 18);
                g.setColor(elem.color);
                int dw = g.getFontMetrics().stringWidth(df1.format(elem.width()));
                g.drawString(df1.format(elem.width()), x + dx - dx / 2 - dw / 2, 16);
                x = x + dx;
            }
            g.setColor(GsonScale.BLACK);
            g.drawLine(20, 10, 20, 18);

        } else {
            gc.setColor(getBackground());
            gc.fillRect(0, 0, pan1.getWidth(), pan1.getHeight());
        }
    }

    private void paintVertical(Graphics gc) {
        if (iwin != null) {
            Graphics2D g = (Graphics2D) gc;
            g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, sizeFont()));
            int y = 2;
            for (GsonScale elem : lineVert) {
                int dy = (int) (elem.height() * iwin.scale);
                g.drawLine(0, y + dy, 8, y + dy);
                g.setColor(elem.color);
                int dw = g.getFontMetrics().stringWidth(df1.format(elem.height()));
                g.rotate(Math.toRadians(-90), 10, y + dy - dy / 2 + dw / 2);
                g.drawString(df1.format(elem.height()), 10, y + dy - dy / 2 + dw / 2);
                g.rotate(Math.toRadians(90), 10, y + dy - dy / 2 + dw / 2);
                y = y + dy;
            }
            g.setColor(GsonScale.BLACK);
            g.drawLine(0, 2, 8, 2);

        } else {
            gc.setColor(getBackground());
            gc.fillRect(0, 0, pan4.getWidth(), pan4.getHeight());
        }
    }

    private int directionScaling(List<GsonScale> list) {
        boolean change = list.stream().anyMatch(el -> el.color == GsonScale.CHANGE);
        boolean adjust = list.stream().anyMatch(el -> el.color == GsonScale.ADJUST);
        if (change == false && adjust == false) {
            return 0;
        } else {
            if (change == false) {
                return 0;
            }
            if (change != false && adjust == false) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    private int sizeFont() {
        if (iwin.scale > .18) {
            return 12;
        } else if (iwin.scale > .16) {
            return 11;
        } else if (iwin.scale > .15) {
            return 10;
        } else if (iwin.scale > .13) {
            return 9;
        } else {
            return 8;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan1 = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintHorizontal(g);
            }
        };
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        pan2 = new javax.swing.JPanel();
        pan3 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        pan4 = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintVertical(g);
            }
        };

        setLayout(new java.awt.BorderLayout());

        pan1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));
        pan1.setMinimumSize(new java.awt.Dimension(4, 18));
        pan1.setPreferredSize(new java.awt.Dimension(4, 18));
        pan1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pan1Clicked(evt);
            }
        });
        pan1.setLayout(new java.awt.BorderLayout());

        btn2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 0, 0));
        btn2.setText("-");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(16, 16));
        btn2.setMinimumSize(new java.awt.Dimension(16, 16));
        btn2.setPreferredSize(new java.awt.Dimension(16, 16));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2Action(evt);
            }
        });
        pan1.add(btn2, java.awt.BorderLayout.WEST);

        btn3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 0, 0));
        btn3.setText("+");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(16, 16));
        btn3.setMinimumSize(new java.awt.Dimension(16, 16));
        btn3.setPreferredSize(new java.awt.Dimension(16, 16));
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3Action(evt);
            }
        });
        pan1.add(btn3, java.awt.BorderLayout.EAST);

        add(pan1, java.awt.BorderLayout.SOUTH);

        pan2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        pan2.setMinimumSize(new java.awt.Dimension(2, 10));
        pan2.setPreferredSize(new java.awt.Dimension(2, 10));
        add(pan2, java.awt.BorderLayout.EAST);

        pan3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(0, 0, 0)));
        pan3.setMinimumSize(new java.awt.Dimension(4, 18));
        pan3.setPreferredSize(new java.awt.Dimension(4, 18));
        pan3.setLayout(new java.awt.BorderLayout());

        btn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 0, 0));
        btn1.setText("+");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(16, 16));
        btn1.setMinimumSize(new java.awt.Dimension(16, 16));
        btn1.setPreferredSize(new java.awt.Dimension(16, 16));
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1Action(evt);
            }
        });
        pan3.add(btn1, java.awt.BorderLayout.WEST);

        add(pan3, java.awt.BorderLayout.NORTH);

        pan4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)));
        pan4.setPreferredSize(new java.awt.Dimension(18, 10));
        pan4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pan4Clicked(evt);
            }
        });
        add(pan4, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void pan4Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pan4Clicked
        lineHoriz.forEach(it -> it.color = GsonScale.BLACK);
        lineVert.forEach(it -> {
            if (it.color == GsonScale.BLACK) {
                it.color = GsonScale.GRAY;
            }
        });
        float val_old = 0;
        for (GsonScale elem : lineVert) {
            float val = (float) (evt.getY() / iwin.scale);
            if (val_old < val && val < val_old + elem.height()) {

                if (elem.color == GsonScale.GRAY) {
                    elem.color = GsonScale.CHANGE;
                } else if (elem.color == GsonScale.CHANGE) {
                    elem.color = GsonScale.ADJUST;
                } else if (elem.color == GsonScale.ADJUST) {
                    elem.color = GsonScale.GRAY;
                }
                pan1.repaint();
                pan4.repaint();
                break;
            }
            val_old += elem.height();
        }
    }//GEN-LAST:event_pan4Clicked

    private void pan1Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pan1Clicked
        lineVert.forEach(it -> it.color = GsonScale.BLACK);
        lineHoriz.forEach(it -> {
            if (it.color == GsonScale.BLACK) {
                it.color = GsonScale.GRAY;
            }
        });
        float val_old = 0;
        for (GsonScale elem : lineHoriz) {
            float val = (float) ((evt.getX() - 20) / iwin.scale);
            if (val_old < val && val < val_old + elem.width()) {

                if (elem.color == GsonScale.GRAY) {
                    elem.color = GsonScale.CHANGE;
                } else if (elem.color == GsonScale.CHANGE) {
                    elem.color = GsonScale.ADJUST;
                } else if (elem.color == GsonScale.ADJUST) {
                    elem.color = GsonScale.GRAY;
                }
                pan1.repaint();
                pan4.repaint();
                break;
            }
            val_old += elem.width();
        }
    }//GEN-LAST:event_pan1Clicked

    private void btn1Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1Action
        int val = directionScaling(lineVert);
        if (val == 1) {                   
            List<GsonScale> list = lineVert.stream().filter(el -> el.color == GsonScale.CHANGE).collect(toList());
            iwin.rootGson.resizRoot(1, list, Layout.VERT);
            listenerGson.action(null);
              
        } else if (val == 2) {
            
        }
    }//GEN-LAST:event_btn1Action

    private void btn2Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2Action
        
        //По горизонтпи
        int val = directionScaling(lineHoriz);
        if (val == 1) {                   
            List<GsonScale> list = lineHoriz.stream().filter(el -> el.color == GsonScale.CHANGE).collect(toList());
            iwin.rootGson.resizRoot(-1, list, Layout.HORIZ);
            listenerGson.action(null);
              
        } else if (val == 2) {
            
        }
        
        //По вертикали
        val = directionScaling(lineVert);
        if (val == 1) {                   
            List<GsonScale> list = lineVert.stream().filter(el -> el.color == GsonScale.CHANGE).collect(toList());
            iwin.rootGson.resizRoot(-1, list, Layout.VERT);
            listenerGson.action(null);
              
        } else if (val == 2) {
            
        } 
    }//GEN-LAST:event_btn2Action

    private void btn3Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3Action
        int val = directionScaling(lineHoriz);
        if (val == 1) {                   
            List<GsonScale> list = lineHoriz.stream().filter(el -> el.color == GsonScale.CHANGE).collect(toList());
            iwin.rootGson.resizRoot(1, list, Layout.HORIZ);
            listenerGson.action(null);
              
        } else if (val == 2) {
            
        }
    }//GEN-LAST:event_btn3Action

    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan2;
    private javax.swing.JPanel pan3;
    private javax.swing.JPanel pan4;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 

    private void initElements() {
    }
}
