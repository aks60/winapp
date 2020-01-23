package wincalc.model;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import wincalc.Wincalc;

public class PaintPanel extends JPanel {

    private Wincalc iwin = new Wincalc();
    
    public PaintPanel() {
        
        iwin.create(wincalc.script.Winscript.test(601002));
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

    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Graphics2D gc = (Graphics2D) g;        
        iwin.rootArea.drawWin(gc, getX(), getY(), getWidth(), getHeight(), true);
        
//        gc.setColor(Color.GREEN);
//        gc.drawRect(getX(), getY(), getWidth() - 20, getHeight() - 20);
//        gc.fillRect(getX(), getY(), getWidth() - 20, getHeight() - 20);
        
    }
}
