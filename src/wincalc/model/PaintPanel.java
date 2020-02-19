package wincalc.model;

import common.FrameListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import wincalc.Wincalc;

public class PaintPanel extends JPanel implements FrameListener<MouseEvent, MouseEvent> {

    private boolean visible = true;
    private Wincalc iwin = null;

    public PaintPanel(Wincalc iwin) {
        this.iwin = iwin;
        iwin.gc2d = (Graphics2D) this.getGraphics();
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                response(evt);
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

    public void setVisible(boolean b) {
        this.visible = b;
        repaint();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (iwin.rootArea != null && visible == true) {

            float max1 = (getWidth() > getHeight()) ? getHeight() : getWidth();
            float max2 = (iwin.width > iwin.heightAdd) ? iwin.width + Com5t.SPACE_DX : iwin.heightAdd + Com5t.SPACE_DY;
            iwin.scaleDxy = max1 / max2;
            Graphics2D gc = (Graphics2D) g;
            gc.setColor(getBackground());
            gc.scale(iwin.scaleDxy , iwin.scaleDxy );
            gc.translate(Com5t.TRANSLATE_X, Com5t.TRANSLATE_Y);
            iwin.gc2d = gc;
            iwin.rootArea.draw(getWidth(), getHeight());
        } else {
            g.clearRect(0, 0, getWidth(), getHeight());
        }
    }
}
