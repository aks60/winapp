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

    public Color color = Color.black;  //цвет выделения линии 
    private AreaSimple win = null;

    public GsonScale(AreaSimple win, float id) {
        this.win = win;
    }

    public GsonElem elem() {
        return win.gson;
    }

    public LinkedList<GsonElem> childs() {
        return win.gson.childs;
    }

    public Layout layout() {
        return win.gson.layout();
    }

    public float width() {
        return win.gson.width();
    }

    public float height() {
        return win.gson.height();
    }

}
