package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.HashMap;
import wincalc.Wincalc;

public class AreaScene extends AreaContainer {

    public AreaScene(Wincalc iwin, AreaContainer owner, String id, LayoutArea layout, float width, float height) {
        super(iwin, owner, id, layout, width, height);
    }

    //Обход(схлопывание) соединений area
    public void passJoinArea(HashMap<String, ElemJoinig> mapJoin) {

        ElemJoinig elemJoinVal = null;
        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);

        elemJoinVal = mapJoin.get(key1);
        if (elemJoinVal == null) {
            mapJoin.put(key1, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key1);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.LEFT);
        }

        elemJoinVal = mapJoin.get(key2);
        if (elemJoinVal == null) {
            mapJoin.put(key2, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key2);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.RIGHT);
        }
        
        elemJoinVal = mapJoin.get(key3);
        if (elemJoinVal == null) {
            mapJoin.put(key3, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key3);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.RIGHT);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.BOTTOM);
        }

        elemJoinVal = mapJoin.get(key4);
        if (elemJoinVal == null) {
            mapJoin.put(key4, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key4);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.LEFT);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.BOTTOM);
        }
    }
    
    @Override
    public TypeElem typeElem() {
        return TypeElem.AREA;
    }

    @Override
    public void joinFrame() {
        System.out.println("Функция не определена");
    }        
}
