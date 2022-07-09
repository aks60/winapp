package frames.swing.draw;

import builder.model.AreaSimple;
import builder.model.IArea5e;
import builder.script.GsonElem;
import enums.Type;
import java.awt.Color;

public class Scale {

    double X1, X2, Y1, Y2;
    public Color color = Color.black;  //цвет выделения линии 
    private IArea5e area = null;

    public Scale(IArea5e area) {
        this.area = area;
    }
    
    public void init() {
        this.area = area.winc().listArea.find(this.area.id());  
    }

    public IArea5e area() {
        return area;
    }
    
    public GsonElem gson() {
        return area.gson();
    }

    public float width() {
        return (float) (X2 - X1);
    }

    public float height() {
        return (float) (Y2 - Y1);
    }
    
    public float widthGson() {
        return area.gson().width();
    }

    public float heightGson() {
        return area.gson().height();
    }
}
