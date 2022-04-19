package frames.swing.draw;

import frames.swing.draw.Canvas;
import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.Com5t;
import builder.model.ElemSimple;
import frames.swing.draw.Scale;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;
import common.listener.ListenerObject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Color;

public class Scene extends javax.swing.JPanel {

    public ListenerObject listenerGson = null;
    private Gson gson = new GsonBuilder().create();
    private DecimalFormat df1 = new DecimalFormat("#0.#");
    private Wincalc winc = null;
    private Canvas canvas = null;
    public List<Scale> lineHoriz = new ArrayList();
    public List<Scale> lineVert = new ArrayList();

    private Timer timer = new Timer(160, new ActionListener() {

        public JButton btn = null;

        public void actionPerformed(ActionEvent evt) {

            if (evt.getSource() instanceof JButton) {
                btn = (JButton) evt.getSource();
                timer.start();
            } else {
//                if (btn == btn1) {
//                    btn1Action(evt);
//                } else if (btn == btn2) {
//                    btn2Action(evt);
//                } else if (btn == btn3) {
//                    btn3Action(evt);
//                }
            }
        }
    });

    public Scene(Canvas canvas, ListenerObject listenerGson) {
        initComponents();
        initElements();
        this.timer.setInitialDelay(1000);
        this.canvas = canvas;
        this.listenerGson = listenerGson;
        add(canvas, java.awt.BorderLayout.CENTER);
        this.canvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {

                //Если клик не на конструкции
                if (winc.rootArea.inside(evt.getX() / (float) winc.scale, evt.getY() / (float) winc.scale) == false) {
                    lineHoriz = Arrays.asList(new Scale(winc.rootArea));
                    lineVert = Arrays.asList(new Scale(winc.rootArea));

                } else { //На конструкции
                    for (ElemSimple crs : winc.listSortEl) {
                        if (Arrays.asList(Type.IMPOST, Type.SHTULP, Type.STOIKA).contains(crs.type)
                                && crs.inside(evt.getX() / (float) winc.scale, evt.getY() / (float) winc.scale)) {
                            List<Com5t> areaList = ((ElemSimple) crs).owner.childs;
                            for (int i = 0; i < areaList.size(); ++i) {
                                if (areaList.get(i).id() == crs.id()) {
                                    if (crs.layout == Layout.HORIZ) {
                                        lineVert = Arrays.asList(new Scale((AreaSimple) areaList.get(i - 1)), new Scale((AreaSimple) areaList.get(i + 1)));
                                    } else {
                                        lineHoriz = Arrays.asList(new Scale((AreaSimple) areaList.get(i - 1)), new Scale((AreaSimple) areaList.get(i + 1)));
                                    }
                                }
                            }
                        }
                    }
                }
                draw();
            }
        });
    }

    public void init(Wincalc winc) {
        this.winc = winc;
        if (winc != null) {
            lineHoriz = Arrays.asList(new Scale(winc.rootArea));
            lineVert = Arrays.asList(new Scale(winc.rootArea));
        }
        canvas.init(winc);
    }

    public void draw() {
        panSouth.repaint();
        panWest.repaint();
    }

    public Wincalc winc() {
        return winc;
    }

    //Рисуем на panSouth
    private void paintHorizontal(Graphics gc) {
        if (winc != null) {
            Graphics2D g = (Graphics2D) gc;
            g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, resizeFont()));
            float sum = 0;
            int curX = 16;
            for (Scale elem : lineHoriz) {
                int dx = (int) (elem.width() * winc.scale);
                g.drawLine(curX + dx, 6, curX + dx, 12);
                g.setColor(elem.color);
                int dw = g.getFontMetrics().stringWidth(df1.format(elem.width()));
                g.drawString(df1.format(elem.width()), curX + dx - dx / 2 - dw / 2, 12);
                curX = curX + dx;
            }
            g.setColor(Color.BLACK);
            g.drawLine(16, 6, 16, 12);

        } else {
            gc.setColor(getBackground());
            gc.fillRect(0, 0, panSouth.getWidth(), panSouth.getHeight());
        }
    }

    //Рисуем на panWest
    private void paintVertical(Graphics gc) {
        if (winc != null) {
            Graphics2D g = (Graphics2D) gc;
            g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, resizeFont()));
            float dh = 0, curY = 2;
            for (Scale gson : lineVert) {
                if (gson.gson().owner() != null && gson.gson().owner().type() == Type.STVORKA) {
                    dh = winc.listSortAr.stream().filter(it -> it.id() == gson.gson().owner().id()).findFirst().get().y1();
                }
                if (gson == lineVert.get(lineVert.size() - 1)) {
                    dh = -1 * dh;
                }
                float dy = (float) ((gson.height() + dh) * winc.scale);
                g.drawLine(0, (int) (curY + dy), 8, (int) (curY + dy));
                g.setColor(gson.color);
                int dw = g.getFontMetrics().stringWidth(df1.format(gson.height()));
                g.rotate(Math.toRadians(-90), 10, curY + dy - dy / 2 + dw / 2);
                g.drawString(df1.format(gson.height() + dh), 10, curY + dy - dy / 2 + dw / 2);
                g.rotate(Math.toRadians(90), 10, curY + dy - dy / 2 + dw / 2);
                curY = curY + dy;
            }
            g.setColor(Color.BLACK);
            g.drawLine(0, 2, 8, 2);

        } else {
            gc.setColor(getBackground());
            gc.fillRect(0, 0, panWest.getWidth(), panWest.getHeight());
        }
    }

    // 1 - изменение, 2 - перераспределение
    private int directionScaling(List<Scale> list) {
        boolean change = list.stream().anyMatch(el -> el.color == Color.BLUE); //цвет на изменение
        boolean adjust = list.stream().anyMatch(el -> el.color == Color.MAGENTA); //цвет на коррецию
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

    private int resizeFont() {
        if (winc.scale > .18) {
            return 12;
        } else if (winc.scale > .16) {
            return 11;
        } else if (winc.scale > .15) {
            return 10;
//        } else if (winc.scale > .13) {
//            return 9;
        } else {
            return 9;
        }
    }

    public void resizeLine(String dir) {
        for (Scale gsonScale : lineHoriz) {
            if (gsonScale.color == java.awt.Color.RED) {
                gsonScale.area().lengthX(gsonScale.area().lengthX() + 1);
            }
        }
        listenerGson.action(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panSouth = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintHorizontal(g);
            }
        };
        panWest = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintVertical(g);
            }
        };

        setLayout(new java.awt.BorderLayout());

        panSouth.setMinimumSize(new java.awt.Dimension(4, 14));
        panSouth.setName(""); // NOI18N
        panSouth.setPreferredSize(new java.awt.Dimension(4, 14));
        panSouth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panSouthClicked(evt);
            }
        });
        panSouth.setLayout(new java.awt.BorderLayout());
        add(panSouth, java.awt.BorderLayout.SOUTH);

        panWest.setMinimumSize(new java.awt.Dimension(14, 10));
        panWest.setPreferredSize(new java.awt.Dimension(14, 10));
        panWest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panWestClicked(evt);
            }
        });
        add(panWest, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void panWestClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panWestClicked
        float val_old = 0;
        lineVert.forEach(it -> it.color = Color.BLACK);
        lineHoriz.forEach(it -> it.color = Color.BLACK);

        for (Scale area : lineVert) {
            float val = (float) (evt.getY() / winc.scale);
            if (val_old < val && val < val_old + area.width()) {
                area.color = java.awt.Color.RED;
                panSouth.repaint();
                panWest.repaint();
            }
        }
    }//GEN-LAST:event_panWestClicked

    private void panSouthClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panSouthClicked
        float val_old = 0;
        lineVert.forEach(it -> it.color = Color.BLACK);
        lineHoriz.forEach(it -> it.color = Color.BLACK);

        for (Scale area : lineHoriz) {
            float val = (float) ((evt.getX() - 20) / winc.scale);
            if (val_old < val && val < val_old + area.width()) {
                area.color = java.awt.Color.RED;
                panSouth.repaint();
                panWest.repaint();
            }
        }
    }//GEN-LAST:event_panSouthClicked

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panSouth;
    private javax.swing.JPanel panWest;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 

    private void initElements() {
        //timerHor.addActionListener(btnMouseReleased);        
    }
}
