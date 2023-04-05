/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * This frame contains a combo box to select a shape and a component to draw it.
 */
class TFrame extends JFrame {
    public TFrame() {
        setTitle("ShapeTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        final TPaintComp comp = new TPaintComp();
        add(comp, BorderLayout.CENTER);
        
        final JComboBox comboBox = new JComboBox();
        comboBox.addItem(new Line());
        comboBox.addItem(new Rectangle());
        comboBox.addItem(new RectangleRound());
        comboBox.addItem(new Ellipse());
        comboBox.addItem(new Arc());
        comboBox.addItem(new Polygon());
        comboBox.addItem(new QuadCurve());
        comboBox.addItem(new CubicCurve());
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                TShape shapeMaker = (TShape) comboBox.getSelectedItem();
                comp.setShapeMaker(shapeMaker);
            }
        });
        add(comboBox, BorderLayout.NORTH);
        comp.setShapeMaker((TShape) comboBox.getItemAt(0));
    }

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 600;
}