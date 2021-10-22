package builder.model;

import builder.making.Specific;
import java.awt.Color;
import builder.Wincalc;
import builder.script.GsonElem;
import enums.Form;
import enums.Layout;
import enums.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public abstract class ElemSimple extends Com5t {

    public float anglCut[] = {45, 45}; //угол реза
    public float[] anglFlat = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    public float anglHoriz = -1; //угол к горизонту    

    public Specific spcRec = null; //спецификация элемента
    protected UMod uti3 = null;
    public Color borderColor = Color.BLACK;

    public ElemSimple(float id, Wincalc iwin, AreaSimple owner) {
        super(id, iwin, owner);
        spcRec = new Specific(id, this);
        uti3 = new UMod(this);
    }

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y) {
        int x = (int) (X / iwin.scale) - Com5t.TRANSLATE_XY;
        int y = (int) (Y / iwin.scale) - Com5t.TRANSLATE_XY;
        return inside(x, y);
    }

    //Главная спецификация
    public abstract void setSpecific();

    //Вложенная спецификация
    public abstract void addSpecific(Specific specification);

    //Точки соединения профилей (side 0-нач. вектора, 1-конец вектора, 2-точка прилегающего вектора)
    //В этих точках лежат мапы соединений см. Wincalc.mapJoin
    public String joinPoint(int side) {

        if (owner.type() == Type.ARCH && layout() == Layout.TOP && iwin.form == Form.NUM3) {
            return (side == 0) ? x2 + ":" + (iwin.height - iwin.heightAdd) : x1 + ":" + (iwin.height - iwin.heightAdd);

        } else if (owner.type() == Type.TRAPEZE && layout() == Layout.TOP && iwin.form == Form.NUM2) {
            return (side == 0) ? x2 + ":" + (iwin.height - iwin.heightAdd) : x1 + ":" + y1;

        } else if (owner.type() == Type.TRAPEZE && layout() == Layout.TOP && iwin.form == Form.NUM4) {
            return (side == 0) ? x2 + ":" + y1 : x1 + ":" + (iwin.height - iwin.heightAdd);

        } else if (layout() == Layout.BOTT) {
            return (side == 0) ? x1 + ":" + y2 : (side == 1) ? x2 + ":" + y2 : x1 + (x2 - x1) / 2 + ":" + y2; //точки левого и правого нижнего углового и прилегающего соед.

        } else if (layout() == Layout.RIGHT) {
            return (side == 0) ? x2 + ":" + y2 : (side == 1) ? x2 + ":" + y1 : x2 + ":" + y1 + (y2 - y1) / 2; //точки нижнего и верхнего правого углового и прилегающего соед.

        } else if (layout() == Layout.TOP) {
            return (side == 0) ? x2 + ":" + y1 : (side == 1) ? x1 + ":" + y1 : x1 + (x2 - x1) / 2 + ":" + y2; //точки правого и левого верхнего углового и прилегающего соед.

        } else if (layout() == Layout.LEFT) {
            return (side == 0) ? x1 + ":" + y1 : (side == 1) ? x1 + ":" + y2 : x1 + ":" + y1 + (y2 - y1) / 2; //точки верхнего и нижнего левого углового и прилегающего соед.

            //импост, штульп...    
        } else if (layout() == Layout.VERT) { //вектор всегда снизу вверх
            return (side == 0) ? x1 + (x2 - x1) / 2 + ":" + y2 : (side == 1) ? x1 + (x2 - x1) / 2 + ":" + y1 : "0:0"; //точки нижнего и верхнего Т-обр и прилегающего соед.

        } else if (layout() == Layout.HORIZ) { //вектор всегда слева на право
            return (side == 0) ? x1 + ":" + y1 + (y2 - y1) / 2 : (side == 1) ? x2 + ":" + y1 + (y2 - y1) / 2 : "0:0"; //точки левого и правого Т-обр и прилегающего соед. 
        }
        return null;
    }

    //Прилегающее соединения
    public ElemSimple joinFlat(Layout layoutSide) {
        LinkedList<ElemSimple> listElem = root().listElem(Type.STVORKA_SIDE, Type.FRAME_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA); //список элементов
        Collections.sort(listElem, Collections.reverseOrder((a, b) -> Float.compare(a.id(), b.id())));
        ElemSimple ret = null;
        if (Layout.BOTT == layoutSide) {
            float Y2 = (y2 > y1) ? y2 : y1;
            ret = listElem.stream().filter(el -> el != this && el.layout != Layout.VERT && el.inside(x1 + (x2 - x1) / 2, Y2) == true).findFirst().orElse(null);
        } else if (Layout.LEFT == layoutSide) {
            ret = listElem.stream().filter(el -> el != this && el.layout != Layout.HORIZ && el.inside(x1, y1 + (y2 - y1) / 2) == true).findFirst().orElse(null);
        } else if (Layout.TOP == layoutSide) {
            float Y1 = (y2 > y1) ? y1 : y2;
            ret = listElem.stream().filter(el -> el != this && el.layout != Layout.VERT && el.inside(x1 + (x2 - x1) / 2, Y1) == true && (el.owner.type == Type.ARCH && el.layout() == Layout.TOP) == false).findFirst().orElse(null);
        } else if (Layout.RIGHT == layoutSide) {
            ret = listElem.stream().filter(el -> el != this && el.layout != Layout.HORIZ && el.inside(x2, y1 + (y2 - y1) / 2)).findFirst().orElse(null);
        }
        if (ret == null) {
            throw new IllegalArgumentException("Неудача:ElemSimple.joinFlat() Прилегающий элемент не обнаружен!");
        }
        return ret;
    }

    //Для маятниковых дверей, т.к. прилегающее соединение отсутствует
    public ElemSimple joinFlat2(Layout layoutSide) {
        LinkedList<ElemSimple> listElem = root().listElem(Type.STVORKA_SIDE, Type.FRAME_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA); //список элементов
        Collections.sort(listElem, Collections.reverseOrder((a, b) -> Float.compare(a.id(), b.id())));

        if (Layout.BOTT == layoutSide) {
            return listElem.stream().filter(el -> el != this && el.layout != Layout.VERT && el.inside(x1 + (x2 - x1) / 2, y2 + 8) == true).findFirst().orElse(null);
        } else if (Layout.LEFT == layoutSide) {
            return listElem.stream().filter(el -> el != this && el.layout != Layout.HORIZ && el.inside(x1 - 8, y1 + (y2 - y1) / 2) == true).findFirst().orElse(null);
        } else if (Layout.TOP == layoutSide) {
            return listElem.stream().filter(el -> el != this && el.layout != Layout.VERT && el.inside(x1 + (x2 - x1) / 2, y1 - 8) == true && (el.owner.type == Type.ARCH && el.layout() == Layout.TOP) == false).findFirst().orElse(null);
        } else if (Layout.RIGHT == layoutSide) {
            return listElem.stream().filter(el -> el != this && el.layout != Layout.HORIZ && el.inside(x2 + 8, y1 + (y2 - y1) / 2)).findFirst().orElse(null);
        }
        throw new IllegalArgumentException("Неудача:ElemSimple.joinFlat() Придегающий элемент не обнаружен!");
    }

    //Элемент соединения 0-пред.артикл, 1-след.артикл, 2-прилег. артикл
    public ElemSimple joinElem(int side) {
        ElemJoining ej = iwin.mapJoin.get(joinPoint(side));
        if (ej != null && side == 0) {
            return (this.type == Type.IMPOST || this.type == Type.SHTULP || this.type == Type.STOIKA) ? ej.elem2 : ej.elem1;
        } else if (ej != null && side == 1) {
            return ej.elem2;
        } else if (ej != null && side == 2) {
            return ej.elem2;
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz + ", length=" + length();
    }
}
