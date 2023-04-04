
package builder.geom;
/*
   This program is a part of the companion code for Core Java 8th ed.
   (http://horstmann.com/corejava)
    
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
    
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
    
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * This program demonstrates the various 2D shapes.
 * @version 1.02 2007-08-16
 * @author Cay Horstmann
 */
//
//См. Java.2014-Том 2. Расш.средства прогр
//
public class ShapeTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new ShapeTestFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

