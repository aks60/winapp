package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import wincalc.Wincalc;

public class AreaSquare extends AreaSimple {

    public AreaSquare(Wincalc iwin, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, null, id, layout, width, height, color1, color2, color3); 
        parsing(param);
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.SQUARE;
    }

    @Override
    public void joinFrame() {

        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);

        //Угловое соединение левое верхнее
        ElemJoining elemJoin1 = new ElemJoining(iwin);
        elemJoin1.elemJoinRight = mapFrame.get(LayoutArea.TOP);
        elemJoin1.elemJoinBottom = mapFrame.get(LayoutArea.LEFT);
        elemJoin1.cutAngl1 = 45;
        elemJoin1.cutAngl2 = 45;
        elemJoin1.anglProf = 90;
        iwin.mapJoin.put(key1, elemJoin1);

        //Угловое соединение правое верхнее
        ElemJoining elemJoin2 = new ElemJoining(iwin);
        elemJoin2.elemJoinLeft = mapFrame.get(LayoutArea.TOP);
        elemJoin2.elemJoinBottom = mapFrame.get(LayoutArea.RIGHT);
        elemJoin2.cutAngl1 = 45;
        elemJoin2.cutAngl2 = 45;
        elemJoin2.anglProf = 90;
        iwin.mapJoin.put(key2, elemJoin2);

        //Угловое соединение правое нижнее
        ElemJoining elemJoin3 = new ElemJoining(iwin);
        elemJoin3.elemJoinTop = mapFrame.get(LayoutArea.RIGHT);
        elemJoin3.elemJoinLeft = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin3.cutAngl1 = 45;
        elemJoin3.cutAngl2 = 45;
        elemJoin3.anglProf = 90;
        iwin.mapJoin.put(key3, elemJoin3);

        //Угловое соединение левое нижнее
        ElemJoining elemJoin4 = new ElemJoining(iwin);
        elemJoin4.elemJoinRight = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin4.elemJoinTop = mapFrame.get(LayoutArea.LEFT);
        elemJoin4.cutAngl1 = 45;
        elemJoin4.cutAngl2 = 45;
        elemJoin4.anglProf = 90;
        iwin.mapJoin.put(key4, elemJoin4);
    }
}
