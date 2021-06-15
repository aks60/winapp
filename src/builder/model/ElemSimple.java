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
import enums.TypeElem;
import java.util.LinkedList;

public abstract class ElemSimple extends Com5t {

    public float anglCut[] = {45, 45}; //угол реза рамы
    public float[] anglFlat = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    public float anglHoriz = -1; //угол к горизонту
    public ElemJoining joinElem[] = {null, null, null}; //соединения угловое и прилегающее     

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

    //Вложенная спецификация
    public abstract void addSpecific(Specific specification);

    //Точки соединения профилей (side 0-нач. вектора, 1-конец вектора, 2-точка прилегающего)
    //В этих точках лежат мапы соединений см. Wincalc.mapJoin
    public String joinPoint(int side) {
        if (layout() == LayoutArea.BOTT) {
            return (side == 0) ? x1 + ":" + y2 : (side == 1) ? x2 + ":" + y2 : x1 + (x2 - x1) / 2 + ":" + y2; //точки левого и правого нижнего углового и прилегающего соед.
        } else if (layout() == LayoutArea.RIGHT) {
            return (side == 0) ? x2 + ":" + y2 : (side == 1) ? x2 + ":" + y1 : x2 + ":" + y1 + (y2 - y1) / 2; //точки нижнего и верхнего правого углового и прилегающего соед.
        } else if (layout() == LayoutArea.TOP) {
            return (side == 0) ? x2 + ":" + y1 : (side == 1) ? x1 + ":" + y1 : x1 + (x2 - x1) / 2 + ":" + y2; //точки правого и левого верхнего углового и прилегающего соед.
        } else if (layout() == LayoutArea.LEFT) {
            return (side == 0) ? x1 + ":" + y1 : (side == 1) ? x1 + ":" + y2 : x1 + ":" + y1 + (y2 - y1) / 2; //точки верхнего и нижнего левого углового и прилегающего соед.

            //импост, штульп...    
        } else if (layout() == LayoutArea.VERT) { //вектор всегда снизу вверх
            return (side == 0) ? x2 + ":" + y2 : x1 + ":" + y1; //точки нижнего и верхнего Т-обр.
            
        } else if (layout() == LayoutArea.HORIZ) { //вектор всегда слева на право
            return (side == 0) ? x1 + ":" + y1 : x2 + ":" + y2; //точки левого и правого Т-обр 
        }
        return null;
    }

    //Прилегающие соединения. Используется при построении конструкции, когда соединения ещё не определены  
    public ElemSimple joinFlat(LayoutArea layoutArea) {
        LinkedList<ElemSimple> listElem = root().listElem(TypeElem.STVORKA_SIDE, TypeElem.FRAME_SIDE, TypeElem.IMPOST, TypeElem.SHTULP); //список элементов
        if (LayoutArea.BOTT == layoutArea) {
            return listElem.stream().filter(el -> el != this && el.inside(x1 + (x2 - x1) / 2, y2) == true && el.layout() != LayoutArea.ARCH).findFirst().orElse(null);
        } else if (LayoutArea.LEFT == layoutArea) {
            return listElem.stream().filter(el -> el != this && el.inside(x1, y1 + (y2 - y1) / 2) == true && el.layout() != LayoutArea.ARCH).findFirst().orElse(null);
        } else if (LayoutArea.TOP == layoutArea) {
            return listElem.stream().filter(el -> el != this && el.inside(x1 + (x2 - x1) / 2, y1) == true && el.layout() != LayoutArea.ARCH).findFirst().orElse(null);
        } else if (LayoutArea.RIGHT == layoutArea) {
            return listElem.stream().filter(el -> el != this && el.inside(x2, y1 + (y2 - y1) / 2) == true && el.layout() != LayoutArea.ARCH).findFirst().orElse(null);
        }
        return null;
    }

    //Элемент соединения 0-пред.артикл, 1-след.артикл, 2-прилег. артикл
    public ElemSimple joinElem(int side) {
        ElemJoining ej = iwin().mapJoin.get(joinPoint(side));
        if (ej != null && side == 0) {
            return (this.type == TypeElem.IMPOST || this.type == TypeElem.SHTULP) ? ej.joinElem2 : ej.joinElem1;
        } else if (ej != null && side == 1) {
            return ej.joinElem2;
        } else if (ej != null && side == 2) {
            return ej.joinElem2;
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz + ", length=" + length();
    }
}
