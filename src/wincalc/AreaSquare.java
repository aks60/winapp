package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;

public class AreaSquare extends AreaBase {

    public AreaSquare(Wincalc iwin, String id, eLayoutArea layout, float width, float height, int color1, int color2, int color3, String paramJson) {
        super(null, id, layout, width, height, color1, color2, color3);
        this.iwin = iwin;
        parsingParamJson(this, paramJson);
    }

    @Override
    public eTypeElem typeElem() {
        return eTypeElem.SQUARE;
    }

    @Override
    public void passJoin() {

        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);

        //Угловое соединение левое верхнее
        ElemJoinig elemJoin1 = new ElemJoinig(iwin);
        elemJoin1.elemJoinRight = mapFrame.get(eLayoutArea.TOP);
        elemJoin1.elemJoinBottom = mapFrame.get(eLayoutArea.LEFT);
        elemJoin1.cutAngl1 = 45;
        elemJoin1.cutAngl2 = 45;
        elemJoin1.anglProf = 90;
        mapJoin().put(key1, elemJoin1);

        //Угловое соединение правое верхнее
        ElemJoinig elemJoin2 = new ElemJoinig(iwin);
        elemJoin2.elemJoinLeft = mapFrame.get(eLayoutArea.TOP);
        elemJoin2.elemJoinBottom = mapFrame.get(eLayoutArea.RIGHT);
        elemJoin2.cutAngl1 = 45;
        elemJoin2.cutAngl2 = 45;
        elemJoin2.anglProf = 90;
        mapJoin().put(key2, elemJoin2);

        //Угловое соединение правое нижнее
        ElemJoinig elemJoin3 = new ElemJoinig(iwin);
        elemJoin3.elemJoinTop = mapFrame.get(eLayoutArea.RIGHT);
        elemJoin3.elemJoinLeft = mapFrame.get(eLayoutArea.BOTTOM);
        elemJoin3.cutAngl1 = 45;
        elemJoin3.cutAngl2 = 45;
        elemJoin3.anglProf = 90;
        mapJoin().put(key3, elemJoin3);

        //Угловое соединение левое нижнее
        ElemJoinig elemJoin4 = new ElemJoinig(iwin);
        elemJoin4.elemJoinRight = mapFrame.get(eLayoutArea.BOTTOM);
        elemJoin4.elemJoinTop = mapFrame.get(eLayoutArea.LEFT);
        elemJoin4.cutAngl1 = 45;
        elemJoin4.cutAngl2 = 45;
        elemJoin4.anglProf = 90;
        mapJoin().put(key4, elemJoin4);
    }
}
