package frames.swing.draw;

import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.Com5t;
import builder.model.ElemSimple;
import enums.Layout;
import enums.Type;
import common.listener.ListenerReload;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Timer;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Scene extends javax.swing.JPanel {

    private DecimalFormat df1 = new DecimalFormat("#0.#");
    private Wincalc winc = null;
    private Canvas canvas = null;
    private JSpinner spinner = null;

    private ListenerReload listenerWinc = null;
    private ChangeListener listenerSpinner = new ChangeListener() {

        public void stateChanged(ChangeEvent ce) {

            if (timer.isRunning() == false) {
                timer.setRepeats(false);
                timer.start();
            }
        }
    };

    public List<Scale> lineHoriz = new ArrayList();
    public List<Scale> lineVert = new ArrayList();

    private Timer timer = new Timer(800, (evt) -> {
        resizeLine();
    });

    public Scene(Canvas canvas, JSpinner spinner, ListenerReload listenerWinc) {
        initComponents();
        initElements();
        this.canvas = canvas;
        this.spinner = spinner;
        this.listenerWinc = listenerWinc;
        add(canvas, java.awt.BorderLayout.CENTER);

        this.canvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {

                //Если клик не на конструкции
                if (winc.rootArea.inside(evt.getX() / (float) winc.scale, evt.getY() / (float) winc.scale) == false) {
                    lineHoriz = Arrays.asList(new Scale(winc.rootArea));
                    lineVert = Arrays.asList(new Scale(winc.rootArea));

                } else { //На конструкции
                    for (ElemSimple crs : winc.listElem) {
                        if (Arrays.asList(Type.IMPOST, Type.SHTULP, Type.STOIKA).contains(crs.type())
                                && crs.inside(evt.getX() / (float) winc.scale, evt.getY() / (float) winc.scale)) {
                            List<Com5t> areaChilds = ((ElemSimple) crs).owner.childs; //дети импоста на котором был клик
                            for (int i = 0; i < areaChilds.size(); ++i) {
                                if (areaChilds.get(i).id() == crs.id()) {
                                    if (crs.layout() == Layout.HORIZ) { //area слева и справа от импоста
                                        lineVert = Arrays.asList(new Scale((AreaSimple) areaChilds.get(i - 1)), new Scale((AreaSimple) areaChilds.get(i + 1)));
                                    } else {
                                        lineHoriz = Arrays.asList(new Scale((AreaSimple) areaChilds.get(i - 1)), new Scale((AreaSimple) areaChilds.get(i + 1)));
                                    }
                                }
                            }
                        }
                    }
                }
                draw();
            }
        });
        spinner.addChangeListener(listenerSpinner);
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
        panHoriz.repaint();
        panWert.repaint();
    }

    public Wincalc winc() {
        return winc;
    }

    //Рисуем на panSouth
    private void paintHorizontal(Graphics gc) {
        if (winc != null) {
            Graphics2D g = (Graphics2D) gc;
            g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, resizeFont()));
            float sum = 0, curX = 16 + (float) (lineHoriz.get(0).area().x1() * winc.scale);
            g.setColor(Color.BLACK);
            g.drawLine((int) curX, 6, (int) curX, 12);
            for (Scale elem : lineHoriz) {
                int dx = (int) (elem.widthGson() * winc.scale);
                g.drawLine((int) (curX + dx), 6, (int) (curX + dx), 12);
                g.setColor(elem.color);
                int dw = g.getFontMetrics().stringWidth(df1.format(elem.widthGson()));
                g.drawString(df1.format(elem.widthGson()), curX + dx - dx / 2 - dw / 2, 12);
                curX = curX + dx;
            }

        } else {
            gc.setColor(getBackground());
            gc.fillRect(0, 0, panHoriz.getWidth(), panHoriz.getHeight());
        }
    }

    //Рисуем на panWest
    private void paintVertical(Graphics gc) {
        adaptingVertical();
        if (winc != null) {
            double k = winc.scale;
            Graphics2D g = (Graphics2D) gc;
            g.translate(0, Com5t.TRANSLATE_XY);
            g.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, resizeFont()));

            //1 - шкала
            g.setColor(Color.BLACK);
            Scale sc1 = lineVert.get(0);
            g.drawLine(0, (int) (sc1.Y1 * k), 8, (int) (sc1.Y1 * k));
            g.drawLine(0, (int) (sc1.Y2 * k), 8, (int) (sc1.Y2 * k));
            g.setColor(sc1.color);
            int dw = g.getFontMetrics().stringWidth(df1.format(sc1.heightGson()));
            double val = (sc1.Y1 + sc1.height() / 2) * k + dw / 2;
            g.rotate(Math.toRadians(-90), 11, val);
            g.drawString(df1.format(sc1.height()), 11, (int) val);
            g.rotate(Math.toRadians(90), 11, val);
            if (lineVert.size() == 2) {

                //2 - шкала
                Scale sc2 = lineVert.get(1);
                g.setColor(sc2.color);
                dw = g.getFontMetrics().stringWidth(df1.format(sc2.heightGson()));
                val = (sc2.Y1 + sc2.height() / 2) * k + dw / 2;
                g.rotate(Math.toRadians(-90), 11, val);
                g.drawString(df1.format(sc2.height()), 11, (int) val);
                g.rotate(Math.toRadians(90), 11, val);
                g.setColor(Color.BLACK);
                g.drawLine(0, (int) (sc2.Y2 * k), 8, (int) (sc2.Y2 * k));
            }
        } else {
            gc.setColor(getBackground());
            gc.fillRect(0, 0, panWert.getWidth(), panWert.getHeight());
        }
    }

    private int resizeFont() {
        if (winc.scale > .18) {
            return 12;
        } else if (winc.scale > .16) {
            return 11;
        } else if (winc.scale > .15) {
            return 10;
        } else {
            return 9;
        }
    }

    //Изменить размер и перерисовать шкалу
    private void resizeLine() {
        try {
            float val = Float.valueOf(spinner.getValue().toString());

            //Горизонтальное выделение красн.
            Scale scaleHor = lineHoriz.stream().filter(sc -> sc.color == Color.RED).findFirst().orElse(null);
            if (scaleHor != null) {
                float dx = val - scaleHor.width();
                if (dx != 0) {
                    for (Scale scale : lineHoriz) {
                        if (scale.color == java.awt.Color.RED) {
                            scale.area().lengthX(scale.area().lengthX() + dx);
                        } else {
                            scale.area().lengthX(scale.area().lengthX() - dx);
                        }
                    }
                }
            }
            //Вертикальное выделение красн.
            Scale scaleVer = lineVert.stream().filter(sc -> sc.color == Color.RED).findFirst().orElse(null);
            if (scaleVer != null) {
                float dy = val - scaleVer.height();
                if (dy != 0) {
                    for (Scale scale : lineVert) {

                        if (scale.color == java.awt.Color.RED) {
                            scale.area().lengthY(scale.area().lengthY() + dy);
                        } else {
                            scale.area().lengthY(scale.area().lengthY() - dy);
                        }
                    }
                }
            }
            listenerWinc.reload();
        } catch (Exception e) {
            System.err.println("Ошибка:Scene.resizeLine() " + e);
        }
    }

    private void adaptingVertical() {
        if (lineVert.size() > 0) {

            Scale sc1 = lineVert.get(0);
            sc1.Y1 = sc1.area().y1();
            sc1.Y2 = sc1.area().y2();

            if (lineVert.size() == 2) {
                Scale sc2 = lineVert.get(1);
                if (sc1.area().typeArea() == Type.ARCH && sc2.area().typeArea() == Type.AREA) {
                    sc1.Y1 = 0;
                    sc1.Y2 = sc1.gson().length();
                    sc2.Y1 = sc1.gson().length();
                    sc2.Y2 = sc1.gson().length() + sc2.gson().length();

                } else if (sc1.area().typeArea() == Type.TRAPEZE && sc2.area().typeArea() == Type.AREA) {
                    sc1.Y1 = 0;
                    sc1.Y2 = sc1.gson().length();
                    sc2.Y1 = sc1.gson().length();
                    sc2.Y2 = sc1.gson().length() + sc2.gson().length();

                } else if (sc1.area().root.type() == Type.DOOR) {
                    sc1.Y1 = 0;
                    sc1.Y2 = sc1.area().y2();
                    sc2.Y1 = sc1.area().y2();
                    sc2.Y2 = sc2.area().root.height();

                } else if (sc1.area().typeArea() == Type.AREA && sc2.area().typeArea() == Type.AREA) {
                    sc2.Y1 = sc2.area().y1();
                    sc2.Y2 = sc2.area().y2();

                } else {
                    System.err.println("Ошибка:frames.swing.draw.Scene.adaptingVertical()");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panHoriz = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintHorizontal(g);
            }
        };
        panWert = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintVertical(g);
            }
        };

        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        setLayout(new java.awt.BorderLayout());

        panHoriz.setMinimumSize(new java.awt.Dimension(4, 14));
        panHoriz.setName(""); // NOI18N
        panHoriz.setPreferredSize(new java.awt.Dimension(4, 14));
        panHoriz.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panHorizClicked(evt);
            }
        });
        panHoriz.setLayout(new java.awt.BorderLayout());
        add(panHoriz, java.awt.BorderLayout.SOUTH);

        panWert.setMinimumSize(new java.awt.Dimension(14, 10));
        panWert.setPreferredSize(new java.awt.Dimension(14, 10));
        panWert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panWertClicked(evt);
            }
        });
        add(panWert, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void panWertClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panWertClicked
        lineVert.forEach(it -> it.color = Color.BLACK);
        lineHoriz.forEach(it -> it.color = Color.BLACK);
        spinner.removeChangeListener(listenerSpinner);
        spinner.setValue(0);
        for (Scale scale : lineVert) {
            double Y = evt.getY();
            double y1 = scale.Y1 * winc.scale;
            double y2 = scale.Y2 * winc.scale;
            if (y1 < Y && Y < y2) {
                scale.color = java.awt.Color.RED;
                spinner.setValue(scale.Y2 - scale.Y1);
            }
        }
        spinner.addChangeListener(listenerSpinner);
        panHoriz.repaint();
        panWert.repaint();
    }//GEN-LAST:event_panWertClicked

    private void panHorizClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panHorizClicked
        lineVert.forEach(it -> it.color = Color.BLACK);
        lineHoriz.forEach(it -> it.color = Color.BLACK);
        spinner.removeChangeListener(listenerSpinner);
        spinner.setValue(0);
        double x1 = lineVert.get(0).area().x1() * winc.scale;
        for (Scale scale : lineHoriz) {
            float X = evt.getX() - 12;
            double x2 = scale.area().x2() * winc.scale;
            if (x1 < X && X < x2) {
                scale.color = java.awt.Color.RED;
                spinner.setValue(scale.widthGson());
            }
            x1 += scale.widthGson() * winc.scale;
        }
        spinner.addChangeListener(listenerSpinner);
        panHoriz.repaint();
        panWert.repaint();
    }//GEN-LAST:event_panHorizClicked

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panHoriz;
    private javax.swing.JPanel panWert;
    // End of variables declaration//GEN-END:variables
    // </editor-fold> 

    private void initElements() {
        //timerHor.addActionListener(btnMouseReleased);        
    }
}
