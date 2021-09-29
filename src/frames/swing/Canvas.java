package frames.swing;

import builder.Wincalc;
import builder.model.Com5t;
import builder.model.ElemSimple;
import enums.Type;
import frames.swing.listener.ListenerFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Canvas extends javax.swing.JPanel implements ListenerFrame<MouseEvent, MouseEvent> {

    private Wincalc iwin = null;

    public Canvas() {
        initComponents();
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                actionResponse(evt);
            }
        });
    }

    public void init(Wincalc iwin) {
       this.iwin = iwin; 
    }
    
    public void draw(Wincalc iwin) {
        this.iwin = iwin;
        scale(iwin);
        repaint();
    }

    public void actionResponse(MouseEvent evt) {

        if (iwin != null && iwin.listElem != null) {
            iwin.listElem.forEach(el -> el.borderColor = Color.BLACK);
            repaint();
            iwin.rootArea.listElem(Type.STVORKA_SIDE).forEach(el -> {
                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
                    ((ElemSimple) el).borderColor = Color.RED;
                    ((ElemSimple) el).paint();
                    repaint();
                }
            });
            iwin.rootArea.listElem(Type.FRAME_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA).forEach(el -> {
                if (((ElemSimple) el).mouseClick(evt.getX(), evt.getY())) {
                    ((ElemSimple) el).borderColor = Color.RED;
                    ((ElemSimple) el).paint();
                    repaint();
                }
            });
            iwin.rootArea.listElem(Type.GLASS).forEach(el -> {
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

    private void scale(Wincalc iwin) {
        iwin.scale = (getWidth() / iwin.width > getHeight() / iwin.height)
                ? getHeight() / (iwin.height + 24) : getWidth() / (iwin.width + 24);
    }

    //@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (iwin != null) {
            iwin.gc2d = (Graphics2D) g;
            iwin.gc2d.setColor(getBackground());
            iwin.gc2d.setStroke(new BasicStroke(2)); //толщина линии
            iwin.gc2d.translate(Com5t.TRANSLATE_XY, Com5t.TRANSLATE_XY);
            iwin.scale = (getWidth() / iwin.width > getHeight() / iwin.height)
                    ? getHeight() / (iwin.height + 24) : getWidth() / (iwin.width + 24);
            iwin.gc2d.scale(iwin.scale, iwin.scale);
            iwin.rootArea.draw();

        } else {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    //Создание изображение конструкции
    public static ImageIcon createIcon(Wincalc iwin, int length) {
        try {
            BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
            iwin.gc2d = bi.createGraphics();
            iwin.gc2d.fillRect(0, 0, length, length);
            iwin.scale = (length / iwin.width > length / iwin.height) ? length / (iwin.height + 200) : length / (iwin.width + 200);
            iwin.gc2d.translate(2, 2);
            iwin.gc2d.scale(iwin.scale, iwin.scale);
            iwin.rootArea.draw(); //рисую конструкцию
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
