package com;

import javax.swing.SwingUtilities;

public class Winapp {
    
    public Winapp() {
         Object ooo2 = getClass().getResource("/img16/b000.gif");
         Object ooo3 = getClass().getResource("/img16/b028.gif");
        //AksJFrame frame = new AksJFrame();
        //frame.setVisible(true);
        
        System.out.println("Hello World aks!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                new Winapp();
            }
        });
    }
}
