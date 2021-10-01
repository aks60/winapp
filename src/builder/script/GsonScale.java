package builder.script;

import enums.Layout;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class GsonScale {

    private static int GRAY = 0;
    private static int CHANGE = 1;
    private static int ADJUST = 2;
    
    public Color color = Color.black;  //цвет выделения линии 
    private GsonElem gsonElem = null;

    public GsonScale(GsonElem gsonElem) {
        this.gsonElem = gsonElem;
    }

    
    public void resize(List<GsonScale> list2, Layout layout2, float val2) {
      // List<GsonScale> change = list2.stream().filter(el -> )
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
