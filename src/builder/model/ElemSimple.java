package builder.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import enums.UseUnit;
import builder.making.Specific;
import enums.UseArtiklTo;
import java.awt.Color;
import java.util.HashMap;
import builder.Wincalc;
import builder.making.Uti3;
import enums.LayoutArea;

public abstract class ElemSimple extends Com5t {

    public float anglCut[] = {45, 45}; //угол реза рамы
    public float[] anglFlat = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    public float anglHoriz = -1; //угол к горизонту
    public ElemJoining joinElem[] = {null, null, null}; //соединения угловое, прилегающее     

    public Specific spcRec = null; //спецификация элемента
    protected Uti3 uti3 = null;
    public Color borderColor = Color.BLACK;

    public ElemSimple(float id, Wincalc iwin, AreaSimple owner) {
        super(id, iwin, owner);
        spcRec = new Specific(id, this);
        uti3 = new Uti3(this);
    }

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y) {
        int x = (int) (X / iwin().scale) - Com5t.TRANSLATE_XY;
        int y = (int) (Y / iwin().scale) - Com5t.TRANSLATE_XY;
        return inside(x, y);
    }

    //Главная спецификация
    public abstract void setSpecific();

    //Вложеная спецификация
    public abstract void addSpecific(Specific specification);

    //Точки соединения профилей (side 0-пред.артикл, 1-след.артикл или 0-левый, 1-правый)
    public String joinPoint(int side) {
        if (layout() == LayoutArea.BOTT) {
            return (side == 0) ? x1 + ":" + y2 : (side == 2) ? x2 + ":" + y2 : x1 + (x2 - x1) / 2 + ":" + y1; //точки левого, прав. нижнего углового и прилегающего соед.
        } else if (layout() == LayoutArea.RIGHT) {
            return (side == 0) ? x2 + ":" + y2 : (side == 2) ? x2 + ":" + y1 : x1 + ":" + y1 + (y2 - y1) / 2; //точки нижнего и верхнего правого углового и прилегающего соед.
        } else if (layout() == LayoutArea.TOP) {
            return (side == 0) ? x2 + ":" + y1 : (side == 2) ? x1 + ":" + y1 : x1 + (x2 - x1) / 2 + ":" + y2; //точки правого и левого верхнего углового и прилегающего соед.
        } else if (layout() == LayoutArea.LEFT) {
            return (side == 0) ? x1 + ":" + y1 : (side == 2) ? x1 + ":" + y2 : x2 + ":" + y1 + (y2 - y1) / 2; //точки верхнего и нижнего левого углового и прилегающего соед.
            //импост, штульп...    
        } else if (layout() == LayoutArea.VERT) { //вектор всегда снизу вверх
            return (side == 0) ? x2 + ":" + y2 : x1 + ":" + y1; //точки нижнего и верхнего Т-обр.
        } else if (layout() == LayoutArea.HORIZ) { //вектор всегда слева на право
            return (side == 0) ? x1 + ":" + y1 : x2 + ":" + y2; //точки левого и правого Т-обр.
        }
        return null;
    }

    //Элемент соединения 0-нач. вектора, 1-конец вектора, 2-середина вектора
    public ElemJoining getJoin(int side) {
        return iwin().mapJoin.get(joinPoint(side));
    }

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz + ", length=" + length();
    }
}
