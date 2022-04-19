package builder.script;

import builder.Wincalc;
import builder.model.AreaSimple;
import enums.Layout;
import java.awt.Color;
import java.util.LinkedList;

public class GsonScale {

    public Color color = Color.black;  //цвет выделения линии 
    private AreaSimple area = null;

    public GsonScale(AreaSimple win) {
        this.area = win;
    }
    
    public void init() {
        this.area = area.winc.listSortAr.find(this.area.id());
    }

    public AreaSimple area() {
        return area;
    }
    
    public GsonElem gson() {
        return area.gson;
    }

    public float width() {
        return area.gson.width();
    }

    public float height() {
        return area.gson.height();
    }
}
