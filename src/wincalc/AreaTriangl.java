package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;

public class AreaTriangl extends AreaBase {

    public AreaTriangl(Wincalc iwin, String id, eLayoutArea layout, float width, float height, int color1, int color2, int color3, String paramJson) {
        super(null, id, layout, width, height, color1, color2, color3);
        this.iwin = iwin;
        parsingParam(this, paramJson);
    }

    @Override
    public eTypeElem typeElem() {
        return eTypeElem.TRIANGL;
    }

    @Override
    public void joinRama() {
        System.out.println("Функция не определена");
    }
}
