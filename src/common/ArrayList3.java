package common;

import builder.IElem5e;
import builder.model.ElemJoining;
import enums.Type;
import enums.TypeJoin;
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
        try {
            for (ElemJoining join : this) {
                if (side == 0 && join.elem2.id() == el.id()) {
                    return join;
                } else if (side == 1 && join.elem1.id() == el.id()) {
                    return join;
                } else if (side == 2 && join.elem1.id() == el.id() && join.type == TypeJoin.VAR10) {
                    return join;
                }
            }
        } catch (Exception e) {
            System.err.println("Неудача:Соединение не найдено. " + e);
        }
        return null;
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
        ElemJoining join = get(el, side);
        if (join != null && side == 0) {
            return (el.type() == Type.IMPOST || el.type() == Type.SHTULP || el.type() == Type.STOIKA) ? join.elem2 : join.elem1;
        } else if (join != null && side == 1) {
            return join.elem2;
        } else if (join != null && side == 2 && join.type == TypeJoin.VAR10) {
            return join.elem2;
        }
        System.err.println("Неудача:HashMap2.elem() id=" + el.id() + " соединение не найдено");
        return null;
    }

}
