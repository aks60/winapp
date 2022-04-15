package frames.swing;

import builder.Wincalc;
import builder.model.Com5t;
import builder.model.ElemSimple;
import common.listener.ListenerFrame;
import enums.Layout;
import enums.Type;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Canvas extends javax.swing.JPanel implements ListenerFrame<MouseEvent, MouseEvent> {

    private Wincalc winc = null;

    public Canvas() {
        initComponents();
//        this.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent evt) {
//                actionResponse(evt);
//            }
//        });
    }

    public void init(Wincalc winc) {
        this.winc = winc;
    }

    public void draw() {
        scale(winc);
        repaint();
    }

    public void actionResponse(MouseEvent evt) {

//        if (winc != null && winc.listSortEl != null) {
//            winc.listSortEl.forEach(el -> el.borderColor = Color.BLACK);
//            repaint();
//            UCom.listSortObj(winc.listSortAr, Type.STVORKA_SIDE).forEach(el -> {
//                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
//                    ((ElemSimple) el).borderColor = Color.RED;
//                    ((ElemSimple) el).paint();
//                    repaint();
//                }
//            });
//            UCom.listSortObj(winc.listSortEl, Type.FRAME_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA).forEach(el -> {
//                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
//                    ((ElemSimple) el).borderColor = Color.RED;
//                    ((ElemSimple) el).paint();
//                    repaint();
//                }
//            });
//            UCom.listSortObj(winc.listSortEl, Type.GLASS).forEach(el -> {
//                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
//                    ((ElemSimple) el).borderColor = Color.RED;
//                    ((ElemSimple) el).paint();
//                    repaint();
//                }
//            });
//        }
    }

    public void saveImage(String name, String type) {

        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        try {
            ImageIO.write(image, type, new File(name + "." + type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scale(Wincalc winc) {
        if (winc != null) {
            winc.scale = (getWidth() / winc.width > getHeight() / winc.height)
                    ? getHeight() / (winc.height + 24) : getWidth() / (winc.width + 24);
        }
    }

    public List<ElemSimple> lineCross(Com5t cross) {
        ArrayList arrArea = new ArrayList();
        winc.listSortEl.forEach(imp -> {
            if (imp.id() == cross.id()) {
                List<Com5t> areaList = ((ElemSimple) imp).owner.childs;
                for (int i = 0; i < areaList.size(); ++i) {
                    if (areaList.get(i).id() == cross.id()) {
                        arrArea.add(areaList.get(i - 1));
                        arrArea.add(areaList.get(i + 1));
                    }
                }
            }
        });
        return arrArea;
    }

    //@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (winc != null) {
            winc.gc2d = (Graphics2D) g;
            winc.gc2d.setColor(getBackground());
            winc.gc2d.setStroke(new BasicStroke(2)); //толщина линии
            winc.gc2d.translate(Com5t.TRANSLATE_XY, Com5t.TRANSLATE_XY);
            winc.scale = ((getWidth() - 3) / winc.width > (getHeight() - 3) / winc.height)
                    ? (getHeight() - 3) / winc.height : (getWidth() - 3) / winc.width;
            winc.gc2d.scale(winc.scale, winc.scale);
            winc.rootArea.draw();

        } else {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    //Создание изображение конструкции
    public static ImageIcon createIcon(Wincalc winc, int length) {
        try {
            BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
            winc.gc2d = bi.createGraphics();
            winc.gc2d.fillRect(0, 0, length, length);
            winc.scale = (length / winc.width > length / winc.height) ? length / (winc.height + 200) : length / (winc.width + 200);
            winc.gc2d.scale(winc.scale, winc.scale);
            winc.rootArea.draw(); //рисую конструкцию
            return new ImageIcon(bi);
        } catch (Exception e) {
            System.err.println("Canvas.createImageIcon() " + e);
            return new ImageIcon();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Canvas.this.mouseClicked(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            //System.out.println("double clicked");
        } else {

            //Если клик не на конструкции
            if (winc.rootArea.inside(evt.getX(), evt.getY()) == false) {
//        product.scale_new_input('HORIZ', [winCalc.root]);
//        product.scale_new_input('VERT', [winCalc.root]);
//        $('#scale-hor input').css('color', 'rgb(0, 0, 0)');
//        $('#scale-ver input').css('color', 'rgb(0, 0, 0)');

            } else { //На конструкции
                winc.listSortEl.forEach((e) -> {
                    if (e.type == Type.IMPOST || e.type == Type.SHTULP || e.type == Type.STOIKA) {
                        if (e.inside(evt.getX(), evt.getY())) {
                            List<ElemSimple> m = lineCross(e);
                            if (e.layout == Layout.HORIZ) {
                                //product.scale_new_input('VERT', m.reverse());
                            } else {
                                //product.scale_new_input('HORIZ', m);
                            }
                        }
                    }
                });
            }
        }
    }//GEN-LAST:event_mouseClicked

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
