package builder.model;

import builder.IArea5e;
import builder.IElem5e;
import builder.ICom5t;
import builder.making.Specific;
import java.awt.Color;
import builder.Wincalc;
import builder.script.GsonElem;
import enums.Form;
import enums.Layout;
import enums.Type;

public abstract class ElemSimple extends Com5t implements IElem5e {

    protected float anglCut[] = {45, 45}; //угол реза
    protected float[] anglFlat = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    protected float anglHoriz = -1; //угол к горизонту    

    protected Specific spcRec = null; //спецификация элемента
    protected Color borderColor = Color.BLACK;

    public ElemSimple(float id, Wincalc winc, IArea5e owner, GsonElem gson) {
        super(id, winc, owner, gson);
        winc.listElem.add(this);
        winc.listAll.add(this);
        spcRec = new Specific(id, this);
    }

    @Override
    public float anglHoriz() {
        return anglHoriz;
    }

    @Override
    public void anglHoriz(float angl) {
        this.anglHoriz = angl;
    }

    @Override
    public Specific spcRec() {
        return spcRec;
    }

    @Override
    public float[] anglFlat() {
        return anglFlat;
    }

    @Override
    public float[] anglCut() {
        return anglCut;
    }

    //Попадание клика мышки в контур элемента
    @Override
    public boolean mouseClick(int X, int Y) {
        int x = (int) (X / winc.scale) - ICom5t.TRANSLATE_XY;
        int y = (int) (Y / winc.scale) - ICom5t.TRANSLATE_XY;
        return inside(x, y);
    }

    /**
     * Точки соединения профилей (side 0-нач.вектора, 1-конец вектора, 2-точка
     * прилегающего вектора) В этих точках лежат мапы соединений
     * см.Wincalc.mapJoin
     *
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл,
     * 2-прилег.артикл
     * @return - координата соединения
     */
    @Override
    public String joinPoint(int side) {

        if (owner.type() == Type.ARCH && layout == Layout.TOP && winc.form == Form.TOP) {
            return (side == 0) ? x2 + ":" + Math.abs(winc.height1() - winc.height2()) : x1 + ":" + Math.abs(winc.height1() - winc.height2());

        } else if (owner.type() == Type.TRAPEZE && layout == Layout.TOP && winc.form == Form.RIGHT) {
            return (side == 0) ? x2 + ":" + Math.abs(winc.height1() - winc.height2()) : x1 + ":" + y1;

        } else if (owner.type() == Type.TRAPEZE && layout == Layout.TOP && winc.form == Form.LEFT) {
            return (side == 0) ? x2 + ":" + y2 : x1 + ":" + Math.abs(winc.height1() - winc.height2());

        } else if (layout == Layout.BOTT) {
            return (side == 0) ? x1 + ":" + y2 : (side == 1) ? x2 + ":" + y2 : (x1 + (x2 - x1) / 2) + ":" + y2; //точки левого и правого нижнего углового и прилегающего соед.

        } else if (layout == Layout.RIGHT) {
            return (side == 0) ? x2 + ":" + y2 : (side == 1) ? x2 + ":" + y1 : x2 + ":" + (y1 + (y2 - y1) / 2); //точки нижнего и верхнего правого углового и прилегающего соед.

        } else if (layout == Layout.TOP) {
            return (side == 0) ? x2 + ":" + y1 : (side == 1) ? x1 + ":" + y1 : (x1 + (x2 - x1) / 2) + ":" + y2; //точки правого и левого верхнего углового и прилегающего соед.

        } else if (layout == Layout.LEFT) {
            return (side == 0) ? x1 + ":" + y1 : (side == 1) ? x1 + ":" + y2 : x1 + ":" + (y1 + (y2 - y1) / 2); //точки верхнего и нижнего левого углового и прилегающего соед.

            //импост, штульп...    
        } else if (layout == Layout.VERT) { //вектор всегда снизу вверх
            return (side == 0) ? x1 + (x2 - x1) / 2 + ":" + y2 : (side == 1) ? (x1 + (x2 - x1) / 2) + ":" + y1 : "0:0"; //точки нижнего и верхнего Т-обр и прилегающего соед.

        } else if (layout == Layout.HORIZ) { //вектор всегда слева на право
            return (side == 0) ? x1 + ":" + y1 + (y2 - y1) / 2 : (side == 1) ? x2 + ":" + (y1 + (y2 - y1) / 2) : "0:0"; //точки левого и правого Т-обр и прилегающего соед. 
        }
        return null;
    }

    /**
     * Получить элемент прилегающие соединения. Прил. соед. используется для
     * определения координат примыкаемого соединения. (см. )
     *
     * @param layoutSide - сторона прилегания
     * @return - элемент прилегания
     */
    @Override
    public IElem5e joinFlat(Layout layoutSide) {
        boolean begin = false;
        try {
            //Цикл по элементам кострукции
            for (int index = winc.listElem.size() - 1; index >= 0; --index) {
                IElem5e el = (IElem5e) winc.listElem.get(index);

                if (begin == true && el.type() != Type.GLASS) {
                    if (Layout.BOTT == layoutSide && el.layout() != Layout.VERT) {
                        float Y2 = (y2 > y1) ? y2 : y1;
                        if (el.inside(x1 + (x2 - x1) / 2, Y2) == true) {
                            return (IElem5e) el;
                        }
                    } else if (Layout.LEFT == layoutSide && el.layout() != Layout.HORIZ) {
                        if (el.inside(x1, y1 + (y2 - y1) / 2) == true) {
                            return (IElem5e) el;
                        }
                    } else if (Layout.TOP == layoutSide && el.layout() != Layout.VERT) {
                        float Y1 = (y2 > y1) ? y1 : y2;
                        if (el.inside(x1 + (x2 - x1) / 2, Y1) == true && (el.owner().type() == Type.ARCH && el.layout() == Layout.TOP) == false) {
                            return (IElem5e) el;
                        }
                    } else if (Layout.RIGHT == layoutSide && el.layout() != Layout.HORIZ) {
                        if (el.inside(x2, y1 + (y2 - y1) / 2)) {
                            return (IElem5e) el;
                        }
                    }
                }
                if (this == el) {
                    begin = true;
                }
            }
            System.err.println("Неудача:ElemSimple.joinFlat() id=" + this.id() + ", " + layoutSide + " соединение не найдено");
            return null;

        } catch (Exception e) {
            System.err.println("Ошибка:IElem5e.joinFlat() " + e);
            return null;
        }
    }

    /**
     * Получить элемент соединения.
     *
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл,
     * 2-прилег.артикл
     * @return - элемент соединения
     */
    @Override
    public IElem5e joinElem(int side) {
        ElemJoining ej = winc.mapJoin.get(joinPoint(side));
        if (ej != null && side == 0) {
            return (this.type() == Type.IMPOST || this.type() == Type.SHTULP || this.type() == Type.STOIKA) ? ej.elem2 : ej.elem1;
        } else if (ej != null && side == 1) {
            return ej.elem2;
        } else if (ej != null && side == 2) {
            return ej.elem2;
        }
        System.err.println("Неудача:ElemSimple.joinElem() id=" + this.id() + " соединение не найдено");
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz + ", length=" + length();
    }
}
