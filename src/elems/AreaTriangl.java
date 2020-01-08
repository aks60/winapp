package elems;

import enums.eTypeElem;
import enums.eLayoutArea;


/**
 * Является родителем для всех элементов образуюших треугольное окно
 * Контролирует и управляет поведение элементов треугольного окна
 */
public class AreaTriangl extends AreaSimple {

    public AreaTriangl(IWindows iwin, String id, eLayoutArea layout, float width, float height, int colorBase, int colorInterna, int colorExternal, String paramJson) {
        super(null, id, layout, width, height, colorBase, colorInterna, colorExternal);
        this.iwin = iwin;
        setRoot(this);
        parsingParamJson(this, paramJson);
    }

    @Override
    public eTypeElem getTypeElem() {
        return eTypeElem.TRIANGL;
    }
}
