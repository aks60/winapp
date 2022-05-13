package builder.model;

import builder.making.Specific;
import java.awt.Color;
import builder.Wincalc;
import builder.script.GsonElem;
import enums.Form;
import enums.Layout;
import enums.Type;

public abstract class ElemSimple extends Com5t {

    public float anglCut[] = {45, 45}; //угол реза
    public float[] anglFlat = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    public float anglHoriz = -1; //угол к горизонту    

    public Specific spcRec = null; //спецификация элемента
    public Color borderColor = Color.BLACK;

    public ElemSimple(float id, Wincalc winc, AreaSimple owner, GsonElem gson) {
        super(id, winc, owner, gson);
        winc.listElem.add(this);
        winc.listAll.add(this);
        spcRec = new Specific(id, this);
    }

    //Главная спецификация
    public abstract void setSpecific();

    //Вложенная спецификация
    public abstract void addSpecific(Specific specification);

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y) {
        int x = (int) (X / winc.scale) - Com5t.TRANSLATE_XY;
        int y = (int) (Y / winc.scale) - Com5t.TRANSLATE_XY;
        return inside(x, y);
    }

    //Точки соединения профилей (side 0-нач. вектора, 1-конец вектора, 2-точка прилегающего вектора)
    //В этих точках лежат мапы соединений см. Wincalc.mapJoin
    public String joinPoint(int side) {

        if (owner.type() == Type.ARCH && layout == Layout.TOP && winc.form == Form.TOP) {
            return (side == 0) ? x2 + ":" + (winc.height1 - winc.height2) : x1 + ":" + (winc.height1 - winc.height2);

        } else if (owner.type() == Type.TRAPEZE && layout == Layout.TOP && winc.form == Form.RIGHT) {
            return (side == 0) ? x2 + ":" + (winc.height1 - winc.height2) : x1 + ":" + y1;

        } else if (owner.type() == Type.TRAPEZE && layout == Layout.TOP && winc.form == Form.LEFT) {
            return (side == 0) ? x2 + ":" + y1 : x1 + ":" + (winc.height1 - winc.height2);

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

    //Прилегающие соединения
    public ElemSimple joinFlat(Layout layoutSide) {
        boolean begin = false;
        try {
            for (int index = winc.listAll.size() - 1; index >= 0; --index) {
                Com5t com5t = winc.listAll.get(index);
                if (com5t instanceof ElemSimple) {
                    ElemSimple el = (ElemSimple) com5t;

                    if (begin == true && el.type() != Type.GLASS) {
                        if (Layout.BOTT == layoutSide && el.layout != Layout.VERT) {
                            float Y2 = (y2 > y1) ? y2 : y1;
                            if (el.inside(x1 + (x2 - x1) / 2, Y2) == true) {
                                return (ElemSimple) el;
                            }
                        } else if (Layout.LEFT == layoutSide && el.layout != Layout.HORIZ) {
                            if (el.inside(x1, y1 + (y2 - y1) / 2) == true) {
                                return (ElemSimple) el;
                            }
                        } else if (Layout.TOP == layoutSide && el.layout != Layout.VERT) {
                            float Y1 = (y2 > y1) ? y1 : y2;
                            if (el.inside(x1 + (x2 - x1) / 2, Y1) == true && (el.owner.type() == Type.ARCH && el.layout == Layout.TOP) == false) {
                                return (ElemSimple) el;
                            }
                        } else if (Layout.RIGHT == layoutSide && el.layout != Layout.HORIZ) {
                            if (el.inside(x2, y1 + (y2 - y1) / 2)) {
                                return (ElemSimple) el;
                            }
                        }
                    }
                    if (this == el) {
                        begin = true;
                    }
                }
            }
            System.err.println("Неудача: id=" + this.id() + ", " + layoutSide + " соединение не найдено");
            return null;

        } catch (Exception e) {
            System.err.println("Ошибка:ElemSimple.joinFlat() " + e);
            return null;
        }
    }

    //Элемент соединения 0-пред.артикул, 1-след.артикл, 2-прилег. артикл
    public ElemSimple joinElem(int side) {
        ElemJoining ej = winc.mapJoin.get(joinPoint(side));
        if (ej != null && side == 0) {
            return (this.type() == Type.IMPOST || this.type() == Type.SHTULP || this.type() == Type.STOIKA) ? ej.elem2 : ej.elem1;
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
