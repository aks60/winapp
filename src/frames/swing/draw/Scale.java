package frames.swing.draw;

import builder.Wincalc;
import builder.model.AreaSimple;
import builder.script.GsonElem;
import enums.Layout;
import java.awt.Color;
import java.util.LinkedList;

public class Scale {

    public Color color = Color.black;  //цвет выделения линии 
    private AreaSimple area = null;

    public Scale(AreaSimple win) {
        this.area = win;
    }
    
    public void init() {
        this.area = area.winc.listArea.find(this.area.id());
    }

    public AreaSimple area() {
        return area;
    }
    
    public GsonElem gson() {
        return area.gson;
    }

    public float widthGson() {
        return area.gson.width();
    }

    public float heightGson() {
        return area.gson.height();
    }
}
