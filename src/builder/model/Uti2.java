package builder.model;

import enums.LayoutArea;

public class Uti2 {

    //Точка соединения профилей
    public static String joinPoint(ElemSimple elem5e, int side) {
        if (elem5e.layout() == LayoutArea.BOTTOM) {
            return (side == 0) ? elem5e.x1 + ":" + elem5e.y2 : elem5e.x2 + ":" + elem5e.y2;
        } else if (elem5e.layout() == LayoutArea.RIGHT) {
            return (side == 0) ? elem5e.x2 + ":" + elem5e.y2 : elem5e.x2 + ":" + elem5e.y1;
        } else if (elem5e.layout() == LayoutArea.TOP) {
            return (side == 0) ? elem5e.x2 + ":" + elem5e.y1 : elem5e.x1 + ":" + elem5e.y1;
        } else if (elem5e.layout() == LayoutArea.LEFT) {
            return (side == 0) ? elem5e.x1 + ":" + elem5e.y2 : elem5e.x1 + ":" + elem5e.y1;
        } else if (elem5e.layout() == LayoutArea.VERT) {
            return (side == 0) ? (elem5e.x1 + (elem5e.x2 - elem5e.x1) / 2) + ":" + elem5e.y2 : (elem5e.x1 + (elem5e.x2 - elem5e.x1) / 2) + ":" + elem5e.y1;
        } else if (elem5e.layout() == LayoutArea.HORIZ) {
            return (side == 0) ? elem5e.x1 + ":" + (elem5e.y1 + (elem5e.y2 - elem5e.y1) / 2) : elem5e.x2 + ":" + (elem5e.y1 + (elem5e.y2 - elem5e.y1) / 2);
        }
        return null;
    }
}
