package builder.script;

import java.awt.Color;
import java.util.LinkedList;

public class GsonScale {

    public Color color = Color.black;  //цвет выделения линии 
    private GsonElem gsonElem = null;
    public float length = 0;

    public GsonScale(GsonElem gsonElem) {
        this.gsonElem = gsonElem;
        length = gsonElem.length;
    }
    
    public GsonScale(GsonElem gsonElem, float length) {
        this.gsonElem = gsonElem;
        this.length = length;
    }

    public LinkedList<GsonElem> childs() {
        return gsonElem.childs();
    }
}
