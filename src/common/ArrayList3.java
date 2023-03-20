package common;

import builder.IElem5e;
import builder.model.ElemJoining;
import enums.Type;
import java.util.ArrayList;

public class ArrayList3 extends ArrayList<ElemJoining> {

    public ArrayList3() {
        super();
    }

    /**
     * Получить элемент соединения профилей.
     *
     * @param el - элемент соединения,
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл,
     * 2-прилег.артикл
     * @return - класс описатель соединения
     */
    public ElemJoining get(IElem5e el, int side) {
        for (ElemJoining join : this) {
            if (side == 0 && join.elem2.id() == el.id()) {
                return join;
            } else if (side == 1 && join.elem1.id() == el.id()) {
                return join;
            } else if (side == 2 && join.elem2.id() == el.id()) {
                return join;
            }
        }
        throw new NullPointerException("Неудача:Соединение не найдено.");
    }

    /**
     * Получить элемент соединения профилей.
     *
     * @param el - элемент соединения,
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл,
     * 2-прилег.артикл
     * @return - элемент соединения
     */
    public IElem5e elem(IElem5e el, int side) {
        ElemJoining ej = get(el, side);
        if (ej != null && side == 0) {
            return (el.type() == Type.IMPOST || el.type() == Type.SHTULP || el.type() == Type.STOIKA) ? ej.elem2 : ej.elem1;
        } else if (ej != null && side == 1) {
            return ej.elem2;
        } else if (ej != null && side == 2) {
            return ej.elem2;
        }
        System.err.println("Неудача:HashMap2.elem() id=" + el.id() + " соединение не найдено");
        return null;
    }

}
