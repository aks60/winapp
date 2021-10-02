package builder.script;

import enums.Layout;
import enums.Type;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class GsonScale {

    public static Color BLACK = Color.BLACK;
    public static Color GRAY = Color.GRAY;
    public static Color CHANGE = Color.BLUE;
    public static Color ADJUST = Color.MAGENTA;

    public Color color = Color.black;  //цвет выделения линии 
    private GsonElem gsonElem = null;

    public GsonScale(GsonElem gsonElem) {
        this.gsonElem = gsonElem;
    }

    public GsonElem gsonElem() {
        return gsonElem;
    }

    public LinkedList<GsonElem> childs() {
        return gsonElem.childs();
    }

    public Layout layout() {
        return gsonElem.layout();
    }

    public float width() {
        return gsonElem.width();
    }

//    public void width(float val) {
//        gsonElem.width(val);
//    }

    public float height() {
        return gsonElem.height();
    }

//    public void height(float val) {
//        gsonElem.height(val);
//    }
}
