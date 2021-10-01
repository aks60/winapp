package builder.script;

import java.awt.Color;
import java.util.LinkedList;

public class GsonScale {

    public Color color = Color.black;  //цвет выделения линии 
    private GsonElem gsonElem = null;

    public GsonScale(GsonElem gsonElem) {
        this.gsonElem = gsonElem;
    }

    public LinkedList<GsonElem> childs() {
        return gsonElem.childs();
    }
    
    public float width() {
       return  gsonElem.width();
    }
    
    public float height() {
        return gsonElem.height();
    }
}
