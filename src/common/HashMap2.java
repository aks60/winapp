package common;

import builder.IElem5e;
import builder.model.ElemJoining;
import enums.Form;
import enums.Layout;
import enums.Type;
import java.util.HashMap;

public class HashMap2 extends HashMap<String, ElemJoining> {

    public HashMap2() {
        super();
    }

    /**
     * Получить элемент соединения профилей.
     *
     * @param el - элемент соединения,
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл,
     * 2-прилег.артикл
     * @return - элемент соединения
     */
    public IElem5e get(IElem5e el, int side) {

        String str = point(el, side);
        ElemJoining ej = (ElemJoining) super.get(str);
        if (ej != null && side == 0) {
            return (el.type() == Type.IMPOST || el.type() == Type.SHTULP || el.type() == Type.STOIKA) ? ej.elem2 : ej.elem1;
        } else if (ej != null && side == 1) {
            return ej.elem2;
        } else if (ej != null && side == 2) {
            return ej.elem2;
        }
        System.err.println("Неудача:ElemSimple.joinElem() id=" + el.id() + " соединение не найдено");
        return null;
    }

    /**
     * Записать элемент соединения профилей.
     *
     * @param el - элемент соединения,
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл, 2-прилег.артикл
     * @param ej - класс описания соединения
     * 
     * @return - пред. класс описания соединения или null
     */    
    public ElemJoining put(IElem5e el, int side, ElemJoining ej) {
        String str = point(el, side);
        return super.put(str, ej);
    }

    /**
     * Точки описания присоединённых элементов
     * @param el - элемент соединения,
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл,
     * @return - x:y точка соединения
     */
    private String point(IElem5e el, int side) {

        if (el.owner().type() == Type.ARCH && el.layout() == Layout.TOP && el.winc().form == Form.TOP) {
            return (side == 0) ? el.x2() + ":" + Math.abs(el.winc().height1() - el.winc().height2())
                    : el.x1() + ":" + Math.abs(el.winc().height1() - el.winc().height2());

        } else if (el.owner().type() == Type.TRAPEZE && el.layout() == Layout.TOP && el.winc().form == Form.RIGHT) {
            return (side == 0) ? el.x2() + ":" + Math.abs(el.winc().height1() - el.winc().height2()) : el.x1() + ":" + el.y1();

        } else if (el.owner().type() == Type.TRAPEZE && el.layout() == Layout.TOP && el.winc().form == Form.LEFT) {
            return (side == 0) ? el.x2() + ":" + el.y2() : el.x1() + ":" + Math.abs(el.winc().height1() - el.winc().height2());

        } else if (el.layout() == Layout.BOTT) {
            return (side == 0) ? el.x1() + ":" + el.y2() : (side == 1) ? el.x2() + ":" + el.y2() : (el.x1() + (el.x2() - el.x1()) / 2) + ":" + el.y2(); //точки левого и правого нижнего углового и прилегающего соед.

        } else if (el.layout() == Layout.RIGHT) {
            return (side == 0) ? el.x2() + ":" + el.y2() : (side == 1) ? el.x2() + ":" + el.y1() : el.x2() + ":" + (el.y1() + (el.y2() - el.y1()) / 2); //точки нижнего и верхнего правого углового и прилегающего соед.

        } else if (el.layout() == Layout.TOP) {
            return (side == 0) ? el.x2() + ":" + el.y1() : (side == 1) ? el.x1() + ":" + el.y1() : (el.x1() + (el.x2() - el.x1()) / 2) + ":" + el.y2(); //точки правого и левого верхнего углового и прилегающего соед.

        } else if (el.layout() == Layout.LEFT) {
            return (side == 0) ? el.x1() + ":" + el.y1() : (side == 1) ? el.x1() + ":" + el.y2() : el.x1() + ":" + (el.y1() + (el.y2() - el.y1()) / 2); //точки верхнего и нижнего левого углового и прилегающего соед.

            //импост, штульп...    
        } else if (el.layout() == Layout.VERT) { //вектор всегда снизу вверх
            return (side == 0) ? el.x1() + (el.x2() - el.x1()) / 2 + ":" + el.y2() : (side == 1) ? (el.x1() + (el.x2() - el.x1()) / 2) + ":" + el.y1() : "0:0"; //точки нижнего и верхнего Т-обр и прилегающего соед.

        } else if (el.layout() == Layout.HORIZ) { //вектор всегда слева на право
            return (side == 0) ? el.x1() + ":" + el.y1() + (el.y2() - el.y1()) / 2 : (side == 1) ? el.x2() + ":" + (el.y1() + (el.y2() - el.y1()) / 2) : "0:0"; //точки левого и правого Т-обр и прилегающего соед. 
        }
        return null;
    }
}
