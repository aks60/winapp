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
        
        iwin.graphics2D = (Graphics2D) this.getGraphics();
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
        float max1 = (getHeight() > getWidth()) ? getHeight() : getWidth();
        float max2 = (iwin.height > iwin.width) ? iwin.height : iwin.width;
        iwin.scale = max1 / max2;
        Graphics2D gc = (Graphics2D) g; 
        iwin.graphics2D = gc;
        iwin.rootArea.drawWin();
        
//        gc.setColor(Color.GREEN);
//        gc.drawRect(getX(), getY(), getWidth() - 20, getHeight() - 20);
//        gc.fillRect(getX(), getY(), getWidth() - 20, getHeight() - 20);
        
    }
}
