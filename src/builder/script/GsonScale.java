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

    public void width(float val) {
        gsonElem.width(val);
    }

    public float height() {
        return gsonElem.height();
    }

    public void height(float val) {
        gsonElem.height(val);
    }

    public void resizRoot(float length, List<GsonScale> list, Layout layout) {

        List<GsonScale> changeList = list.stream().filter(it -> it.color == GsonScale.CHANGE).collect(toList());
        float lenSum = 0;

        //    Горизонтальное перераспределение
        if (layout == Layout.HORIZ) {
            for (GsonScale gs : changeList) {
                lenSum += gs.width();
            }
            float k = lenSum / this.width();
            for (GsonScale elem : changeList) {
                if (this.gsonElem.layout == Layout.HORIZ) { //расположение по горизонтали
                    elem.width(k * elem.width());
                    elem.resizAll(elem.width(), layout);
                } else {
                    elem.resizAll(length, layout);
                }
            }
            ((GsonRoot) this.gsonElem).width = length;

            //Вертикальное перераспределение
        } else if (layout == Layout.VERT) {
            for (GsonScale gs : changeList) {
                lenSum += gs.height();
            }
            float k = lenSum / this.height();
            for (GsonScale elem : changeList) {
                if (this.gsonElem.layout == Layout.VERT) { //расположение по вертикали
                    elem.height(k * elem.height());
                    elem.resizAll(elem.height(), layout);
                }
                elem.resizAll(length, layout);
            }
            GsonRoot gsonRoot = (GsonRoot) this.gsonElem;
            gsonRoot.heightAdd = (length / gsonRoot.height) * gsonRoot.heightAdd;
            gsonRoot.height = length;
        }
    }

    public void resizAll(float length2, Layout layout2) {
        List<GsonElem> areaList = this.childs().stream().filter(it -> it.type == Type.AREA).collect(toList());
        for (GsonElem elem : areaList) {

            if (layout2 == Layout.HORIZ && this.layout() == Layout.HORIZ) { //горизонтальное перераспределение и расположение
                elem.length = (length2 / this.width()) * elem.width();
                elem.resizAll(elem.length, layout2);

            } else if (layout2 == Layout.VERT && this.layout() == Layout.VERT) { //вертикальное перераспределение и расположение
                elem.length = (length2 / this.height()) * elem.height();
                elem.resizAll(elem.length, layout2);
            }
            elem.resizAll(length2, layout2);
        }
    }
}
