package builder.model;

import common.FrameListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import builder.Wincalc;

public class PaintPanel extends JPanel implements FrameListener<MouseEvent, MouseEvent> {

    private boolean visible = true;
    private Wincalc iwin = null;
    //private Graphics2D gc = null;

    public PaintPanel(Wincalc iwin) {
        this.iwin = iwin;
        iwin.gc2d = (Graphics2D) this.getGraphics();
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                actionResponse(evt);
            }
        });
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
            if (getWidth() / iwin.width > getHeight() / iwin.heightAdd) {
                
                double scale = getHeight() / (iwin.heightAdd + 180);
                iwin.gc2d.scale(scale, scale);
            } else {
                double scale = getWidth() / (iwin.width + 180);
                iwin.gc2d.scale(scale, scale);
            }
            iwin.rootArea.draw(getWidth(), getHeight());

        } else {
            g.clearRect(0, 0, getWidth(), getHeight());
        }
    }
}
