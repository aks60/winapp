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
    public Specific spcRec() {
        return spcRec;
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
    public float[] anglFlat() {
        return anglFlat;
    }

    @Override
    public float[] anglCut() {
        return anglCut;
    }

    /**
     * Попадание клика мышки в контур элемента
     */
    @Override
    public boolean mouseClick(int X, int Y) {
        int x = (int) (X / winc.scale) - ICom5t.TRANSLATE_XY;
        int y = (int) (Y / winc.scale) - ICom5t.TRANSLATE_XY;
        return inside(x, y);
    }

    /**
     * Получить элемент прилегающих соединений. Прил. соед. используется для
     * определения координат примыкаемого элемента. (см. ElemXxx.setSpecific())
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
                    //Проверка начинает выполняться после появления в обратном цикле самого элемента(this) 
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

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz + ", length=" + length();
    }
}
