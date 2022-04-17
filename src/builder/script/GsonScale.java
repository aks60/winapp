package builder.script;

import builder.model.AreaSimple;
import enums.Layout;
import java.awt.Color;
import java.util.LinkedList;

public class GsonScale {

    public static Color BLACK = Color.BLACK;
    public static Color GRAY = Color.GRAY;
    public static Color CHANGE = Color.BLUE;
    public static Color ADJUST = Color.MAGENTA;
    public static Color RED = Color.RED;

    public Color color = Color.black;  //цвет выделения линии 
    private AreaSimple area = null;

    public GsonScale(AreaSimple win, float id) {
        this.area = win;
    }

    public AreaSimple area() {
        return area;
    }

    public GsonElem gson() {
        return area.gson;
    }

    public LinkedList<GsonElem> childs() {
        return area.gson.childs;
    }

    public Layout layout() {
        return area.gson.layout();
    }

    public float width() {
        return area.gson.width();
    }

    public float height() {
        return area.gson.height();
    }
}
