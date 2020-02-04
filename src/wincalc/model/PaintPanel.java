package wincalc.model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import wincalc.Wincalc;
import wincalc.script.Winscript;

public class PaintPanel extends JPanel {

    private boolean visible = true;
    private Wincalc iwin = null;

    public PaintPanel(Wincalc iwin) {
        this.iwin = iwin;
        //iwin.graphics2D = (Graphics2D) this.getGraphics();
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

    public void setVisible(boolean b) {
        this.visible = b;
        repaint();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (iwin.rootArea != null && visible == true) {
            float max1 = (getWidth() < getHeight()) ? getHeight() : getWidth();
            float max2 = (iwin.width > iwin.height) ? iwin.width : iwin.height;
            Graphics2D gc = (Graphics2D) g;
            gc.setColor(getBackground());
            float dx = max1 / max2 - .14f;
            AffineTransform at = new AffineTransform(dx, 0.0, 0.0, dx, 340.0, 90.0);
            gc.setTransform(at);
            iwin.graphics2D = gc;
            iwin.rootArea.draw(getWidth(), getHeight());

        } else {
            g.clearRect(0, 0, getWidth(), getHeight());
        }
    }
}
