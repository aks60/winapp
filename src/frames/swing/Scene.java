package frames.swing;

import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.Com5t;
import builder.model.ElemSimple;
import builder.script.GsonScale;
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
    public List<GsonScale> lineHoriz = new ArrayList();
    public List<GsonScale> lineVert = new ArrayList();

    private Timer timer = new Timer(160, new ActionListener() {

        public JButton btn = null;

        public void actionPerformed(ActionEvent evt) {

            if (evt.getSource() instanceof JButton) {
                btn = (JButton) evt.getSource();
                timer.start();
            } else {
                if (btn == btn1) {
                    btn1Action(evt);
                } else if (btn == btn2) {
                    btn2Action(evt);
                } else if (btn == btn3) {
                    btn3Action(evt);
                }
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
                    lineHoriz = Arrays.asList(new GsonScale(winc.rootArea));
                    lineVert = Arrays.asList(new GsonScale(winc.rootArea));

                } else { //На конструкции
                    for (ElemSimple crs : winc.listSortEl) {
                        if (Arrays.asList(Type.IMPOST, Type.SHTULP, Type.STOIKA).contains(crs.type)
                                && crs.inside(evt.getX() / (float) winc.scale, evt.getY() / (float) winc.scale)) {
                            List<Com5t> areaList = ((ElemSimple) crs).owner.childs;
                            for (int i = 0; i < areaList.size(); ++i) {
                                if (areaList.get(i).id() == crs.id()) {
                                    if (crs.layout == Layout.HORIZ) {
                                        lineVert = Arrays.asList(new GsonScale((AreaSimple) areaList.get(i - 1)), new GsonScale((AreaSimple) areaList.get(i + 1)));
                                    } else {
                                        lineHoriz = Arrays.asList(new GsonScale((AreaSimple) areaList.get(i - 1)), new GsonScale((AreaSimple) areaList.get(i + 1)));
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
            lineHoriz = Arrays.asList(new GsonScale(winc.rootArea));
            lineVert = Arrays.asList(new GsonScale(winc.rootArea));
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
            int curX = 20;
            for (GsonScale elem : lineHoriz) {
                int dx = (int) (elem.width() * winc.scale);
                g.drawLine(curX + dx, 10, curX + dx, 18);
                g.setColor(elem.color);
                int dw = g.getFontMetrics().stringWidth(df1.format(elem.width()));
                g.drawString(df1.format(elem.width()), curX + dx - dx / 2 - dw / 2, 16);
                curX = curX + dx;
            }
            g.setColor(Color.BLACK);
            g.drawLine(20, 10, 20, 18);

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
            for (GsonScale gson : lineVert) {
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
    private int directionScaling(List<GsonScale> list) {
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
        for (GsonScale gsonScale : lineHoriz) {
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
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        panEast = new javax.swing.JPanel();
        panNorth = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        panWest = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintVertical(g);
            }
        };

        setLayout(new java.awt.BorderLayout());

        panSouth.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));
        panSouth.setMinimumSize(new java.awt.Dimension(4, 18));
        panSouth.setName(""); // NOI18N
        panSouth.setPreferredSize(new java.awt.Dimension(4, 18));
        panSouth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panSouthClicked(evt);
            }
        });
        panSouth.setLayout(new java.awt.BorderLayout());

        btn2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 0, 0));
        btn2.setText("-");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn2.setMaximumSize(new java.awt.Dimension(16, 16));
        btn2.setMinimumSize(new java.awt.Dimension(16, 16));
        btn2.setPreferredSize(new java.awt.Dimension(16, 16));
        btn2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMouseReleased(evt);
            }
        });
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2Action(evt);
            }
        });
        panSouth.add(btn2, java.awt.BorderLayout.WEST);

        btn3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 0, 0));
        btn3.setText("+");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn3.setMaximumSize(new java.awt.Dimension(16, 16));
        btn3.setMinimumSize(new java.awt.Dimension(16, 16));
        btn3.setPreferredSize(new java.awt.Dimension(16, 16));
        btn3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMouseReleased(evt);
            }
        });
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3Action(evt);
            }
        });
        panSouth.add(btn3, java.awt.BorderLayout.EAST);

        add(panSouth, java.awt.BorderLayout.SOUTH);

        panEast.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        panEast.setMinimumSize(new java.awt.Dimension(2, 10));
        panEast.setPreferredSize(new java.awt.Dimension(2, 10));
        add(panEast, java.awt.BorderLayout.EAST);

        panNorth.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(0, 0, 0)));
        panNorth.setMinimumSize(new java.awt.Dimension(4, 18));
        panNorth.setPreferredSize(new java.awt.Dimension(4, 18));
        panNorth.setLayout(new java.awt.BorderLayout());

        btn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 0, 0));
        btn1.setText("+");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn1.setMaximumSize(new java.awt.Dimension(16, 16));
        btn1.setMinimumSize(new java.awt.Dimension(16, 16));
        btn1.setPreferredSize(new java.awt.Dimension(16, 16));
        btn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMouseReleased(evt);
            }
        });
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1Action(evt);
            }
        });
        panNorth.add(btn1, java.awt.BorderLayout.WEST);

        btn4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 0, 0));
        btn4.setText("~");
        btn4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btn4.setMaximumSize(new java.awt.Dimension(16, 16));
        btn4.setMinimumSize(new java.awt.Dimension(16, 16));
        btn4.setPreferredSize(new java.awt.Dimension(16, 16));
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4Action(evt);
            }
        });
        panNorth.add(btn4, java.awt.BorderLayout.EAST);

        add(panNorth, java.awt.BorderLayout.NORTH);

        panWest.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)));
        panWest.setPreferredSize(new java.awt.Dimension(18, 10));
        panWest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panWestClicked(evt);
            }
        });
        add(panWest, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void panWestClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panWestClicked
        lineHoriz.forEach(it -> it.color = Color.BLACK);
        lineVert.forEach(it -> {
            if (it.color == Color.BLACK) {
                it.color = Color.GRAY;
            }
        });
        float val_old = 0;
        for (GsonScale elem : lineVert) {
            float val = (float) (evt.getY() / winc.scale);
            if (val_old < val && val < val_old + elem.height()) {

                if (elem.color == Color.GRAY) {
                    elem.color = Color.BLUE;
                } else if (elem.color == Color.BLUE) {
                    elem.color = Color.MAGENTA;
                } else if (elem.color == Color.MAGENTA) {
                    elem.color = Color.GRAY;
                }
                panSouth.repaint();
                panWest.repaint();
                break;
            }
            val_old += elem.height();
        }
    }//GEN-LAST:event_panWestClicked

    private void panSouthClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panSouthClicked
        float val_old = 0;
        lineVert.forEach(it -> it.color = Color.BLACK);
        lineHoriz.forEach(it -> it.color = Color.BLACK);

        for (GsonScale elem : lineHoriz) {
            float val = (float) ((evt.getX() - 20) / winc.scale);
            if (val_old < val && val < val_old + elem.width()) {
                elem.color = java.awt.Color.RED;
                panSouth.repaint();
            }
        }
//        lineVert.forEach(it -> it.color = GsonScale.BLACK);
//        lineHoriz.forEach(it -> {
//            if (it.color == GsonScale.BLACK) {
//                it.color = GsonScale.GRAY;
//            }
//        });
//        float val_old = 0;
//        for (GsonScale elem : lineHoriz) {
//            float val = (float) ((evt.getX() - 20) / winc.scale);
//            if (val_old < val && val < val_old + elem.width()) {
//
//                if (elem.color == GsonScale.GRAY) {
//                    elem.color = GsonScale.CHANGE;
//                } else if (elem.color == GsonScale.CHANGE) {
//                    elem.color = GsonScale.ADJUST;
//                } else if (elem.color == GsonScale.ADJUST) {
//                    elem.color = GsonScale.GRAY;
//                }
//                panSouth.repaint();
//                panWest.repaint();
//                break;
//            }
//            val_old += elem.width();
//        }
    }//GEN-LAST:event_panSouthClicked

    private void btn1Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1Action
        int val = directionScaling(lineVert);
        if (val == 1) {//изменение
            List<GsonScale> list = lineVert.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(1, list, Layout.VERT);
            listenerGson.action(null);

        } else if (val == 2) { //перераспределение
            List<GsonScale> list = lineVert.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(1, list, Layout.VERT);
            list = lineVert.stream().filter(el -> el.color == Color.MAGENTA).collect(toList());
            winc.rootGson.resizElem(-1, list, Layout.VERT);
            listenerGson.action(null);
        }
    }//GEN-LAST:event_btn1Action

    private void btn2Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2Action

        //По горизонтпи
        int val = directionScaling(lineHoriz);
        if (val == 1) {//изменение
            List<GsonScale> list = lineHoriz.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(-1, list, Layout.HORIZ);
            listenerGson.action(null);

        } else if (val == 2) { //перераспределение
            List<GsonScale> list = lineHoriz.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(-1, list, Layout.HORIZ);
            list = lineHoriz.stream().filter(el -> el.color == Color.MAGENTA).collect(toList());
            winc.rootGson.resizElem(1, list, Layout.HORIZ);
            listenerGson.action(null);
        }

        //По вертикали
        val = directionScaling(lineVert);
        if (val == 1) {//изменение
            List<GsonScale> list = lineVert.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(-1, list, Layout.VERT);
            listenerGson.action(null);

        } else if (val == 2) { //перераспределение
            List<GsonScale> list = lineVert.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(-1, list, Layout.VERT);
            list = lineVert.stream().filter(el -> el.color == Color.MAGENTA).collect(toList());
            winc.rootGson.resizElem(1, list, Layout.VERT);
            listenerGson.action(null);
        }
    }//GEN-LAST:event_btn2Action

    private void btn3Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3Action
        int val = directionScaling(lineHoriz);
        if (val == 1) { //изменение
            List<GsonScale> list = lineHoriz.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(1, list, Layout.HORIZ);
            listenerGson.action(null);

        } else if (val == 2) { //перераспределение
            List<GsonScale> list = lineHoriz.stream().filter(el -> el.color == Color.BLUE).collect(toList());
            winc.rootGson.resizElem(1, list, Layout.HORIZ);
            list = lineHoriz.stream().filter(el -> el.color == Color.MAGENTA).collect(toList());
            winc.rootGson.resizElem(-1, list, Layout.HORIZ);
            listenerGson.action(null);
        }
    }//GEN-LAST:event_btn3Action

    private void btn4Action(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4Action
        winc.rootGson.resizUp(lineHoriz, Layout.HORIZ);
        winc.rootGson.resizUp(lineVert, Layout.VERT);
        listenerGson.action(null);
    }//GEN-LAST:event_btn4Action

    private void btnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMousePressed
        timer.getActionListeners()[0].actionPerformed(new ActionEvent(evt.getSource(), evt.getID(), null));
    }//GEN-LAST:event_btnMousePressed

    private void btnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMouseReleased
        timer.stop();
    }//GEN-LAST:event_btnMouseReleased

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JPanel panEast;
    private javax.swing.JPanel panNorth;
    private javax.swing.JPanel panSouth;
    private javax.swing.JPanel panWest;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 

    private void initElements() {
        //timerHor.addActionListener(btnMouseReleased);        
    }
}
