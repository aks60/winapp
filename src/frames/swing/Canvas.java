package frames.swing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import builder.Wincalc;
import builder.model.Com5t;
import builder.model.ElemSimple;
import enums.TypeElem;
import java.util.LinkedList;
import frames.swing.listener.ListenerFrame;

public class Canvas extends JPanel implements ListenerFrame<MouseEvent, MouseEvent> {

    private boolean visible = true;
    private Wincalc iwin = null;

    public Canvas(Wincalc iwin) {
        this.iwin = iwin;
        iwin.gc2d = (Graphics2D) this.getGraphics();
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                actionResponse(evt);
            }
        });
    }

    public void actionResponse(MouseEvent evt) {

        if (iwin != null && iwin.listElem != null) {
            iwin.listElem.forEach(el -> el.borderColor = Color.BLACK);
            repaint();
            iwin.rootArea.listElem(TypeElem.STVORKA_SIDE).forEach(el -> {
                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
                    ((ElemSimple) el).borderColor = Color.RED;
                    ((ElemSimple) el).paint();
                    repaint();
                }
            });
            iwin.rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.IMPOST, TypeElem.SHTULP).forEach(el -> {
                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
                    ((ElemSimple) el).borderColor = Color.RED;
                    ((ElemSimple) el).paint();
                    repaint();
                }
            });
            iwin.rootArea.listElem(TypeElem.GLASS).forEach(el -> {
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

    public void repaint(boolean b) {
        this.visible = b;
        repaint();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (iwin.rootArea != null && visible == true) {

            iwin.gc2d = (Graphics2D) g;
            iwin.gc2d.setColor(getBackground());
            iwin.gc2d.setStroke(new BasicStroke(2)); //толщина линии
            iwin.gc2d.translate(Com5t.TRANSLATE_XY, Com5t.TRANSLATE_XY);
            iwin.scale = (getWidth() / iwin.width > getHeight() / iwin.heightAdd)
                    ? getHeight() / (iwin.heightAdd + 240) : getWidth() / (iwin.width + 240);
            iwin.gc2d.scale(iwin.scale, iwin.scale);
            iwin.rootArea.draw(getWidth(), getHeight());

        } else {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }
}
