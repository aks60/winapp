package common;

import builder.IElem5e;
import builder.model.ElemJoining;
import enums.LayoutJoin;
import enums.Type;
import enums.TypeJoin;
import java.util.ArrayList;

public class ArrayJoin extends ArrayList<ElemJoining> {

    public ArrayJoin() {
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
                if(el.type() == Type.IMPOST || el.type() == Type.STOIKA || el.type() == Type.ERKER) {
                    if (side == 0 && join.elem1.id() == el.id() && (join.layout == LayoutJoin.TLEFT || join.layout == LayoutJoin.TBOT)) {
                        return join;
                    } else if (side == 1 && join.elem1.id() == el.id() && (join.layout == LayoutJoin.TRIGH || join.layout == LayoutJoin.TTOP)) {
                        return join;
                    }
                } else {
                    if (side == 0 && join.elem2.id() == el.id()) { //Угловое левое
                        return join;
                    } else if (side == 1 && join.elem1.id() == el.id()) { //Угловое правое
                        return join;
                    } else if (side == 2 && join.elem1.id() == el.id() && join.type == TypeJoin.VAR10) { //Прилегающее
                        return join;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Неудача:Соединение не найдено. " + e);
        }
        return null;
    }
    public ElemJoining get2(IElem5e el, int side) {
        try {
            for (ElemJoining join : this) {
                if(el.type() == Type.IMPOST || el.type() == Type.STOIKA || el.type() == Type.ERKER) {
                    if (side == 0 && join.elem1.id() == el.id() && (join.layout == LayoutJoin.TLEFT || join.layout == LayoutJoin.TBOT)) {
                        return join;
                    } else if (side == 1 && join.elem1.id() == el.id() && (join.layout == LayoutJoin.TRIGH || join.layout == LayoutJoin.TTOP)) {
                        return join;
                    }
                } else {
                    if (side == 0 && join.elem2.id() == el.id()) { //Угловое левое
                        System.out.println(join);
                    } else if (side == 1 && join.elem1.id() == el.id()) { //Угловое правое
                        return join;
                    } else if (side == 2 && join.elem1.id() == el.id() && join.type == TypeJoin.VAR10) { //Прилегающее
                        return join;
                    }
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
        try {
            if (join != null) {

                if (side == 0) {
                    return (el.type() == Type.IMPOST || el.type() == Type.SHTULP || el.type() == Type.STOIKA) ? join.elem2 : join.elem1;
                } else if (side == 1) {
                    return join.elem2;
                } else if (side == 2 && join.type == TypeJoin.VAR10) {
                    return join.elem2;
                }
            }
        } catch (Exception e) {
            System.err.println("Неудача:Соединение не найдено. " + e);
        }
        return null;
    }

}
