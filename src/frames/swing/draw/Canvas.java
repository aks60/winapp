package frames.swing.draw;

import builder.Wincalc;
import builder.model.Com5t;
import builder.model.ElemSimple;
import frames.swing.draw.Scale;
import common.UCom;
import common.listener.ListenerFrame;
import enums.Layout;
import enums.Type;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Canvas extends javax.swing.JPanel implements ListenerFrame<MouseEvent, MouseEvent> {

    private Wincalc winc = null;

    public Canvas() {
        initComponents();
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                actionResponse(evt);
            }
        });
    }

    public void init(Wincalc winc) {
        this.winc = winc;
    }

    public void draw() {
        scale(winc);
        repaint();
    }

    public void actionResponse(MouseEvent evt) {

        if (winc != null && winc.listSortEl != null) {
            winc.listSortEl.forEach(el -> el.borderColor = Color.BLACK);
            repaint();
            UCom.listSortObj(winc.listSortAr, Type.STVORKA_SIDE).forEach(el -> {
                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
                    ((ElemSimple) el).borderColor = Color.RED;
                    ((ElemSimple) el).paint();
                    repaint();
                }
            });
            UCom.listSortObj(winc.listSortEl, Type.FRAME_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA).forEach(el -> {
                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
                    ((ElemSimple) el).borderColor = Color.RED;
                    ((ElemSimple) el).paint();
                    repaint();
                }
            });
            UCom.listSortObj(winc.listSortEl, Type.GLASS).forEach(el -> {
                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
                    ((ElemSimple) el).borderColor = Color.RED;
                    ((ElemSimple) el).paint();
                    repaint();
                }
            });
        }
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

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

// <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
}
