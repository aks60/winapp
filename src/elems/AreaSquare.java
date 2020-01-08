package elems;

import enums.eLayoutArea;
import enums.eTypeElem;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Является родителем для всех элементов образуюших простое окно.
 * Контролирует и управляет поведение элементов простого окна.
 */
public class AreaSquare extends AreaSimple {

    public AreaSquare(IWindows iwin, String id, eLayoutArea layout, float width, float height, int colorBase, int colorInterna, int colorExternal, String paramJson) {
        super(null, id, layout, width, height, colorBase, colorInterna, colorExternal);
        this.iwin = iwin;
        owner = this;
        setRoot(this);
        parsingParamJson(this, paramJson);
    }

    @Override
    public void passJoinRama() {

//        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
//        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
//        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
//        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);
//
//        //Угловое соединение левое верхнее
//        ElemJoinig elemJoin1 = new ElemJoinig(getConst());
//        elemJoin1.elemJoinRight = hmElemFrame.get(eLayoutArea.TOP);
//        elemJoin1.elemJoinBottom = hmElemFrame.get(eLayoutArea.LEFT);
//        elemJoin1.cutAngl1 = 45;
//        elemJoin1.cutAngl2 = 45;
//        elemJoin1.anglProf = 90;
//        getHmJoinElem().put(key1, elemJoin1);
//
//        //Угловое соединение правое верхнее
//        ElemJoinig elemJoin2 = new ElemJoinig(getConst());
//        elemJoin2.elemJoinLeft = hmElemFrame.get(eLayoutArea.TOP);
//        elemJoin2.elemJoinBottom = hmElemFrame.get(eLayoutArea.RIGHT);
//        elemJoin2.cutAngl1 = 45;
//        elemJoin2.cutAngl2 = 45;
//        elemJoin2.anglProf = 90;
//        getHmJoinElem().put(key2, elemJoin2);
//
//        //Угловое соединение правое нижнее
//        ElemJoinig elemJoin3 = new ElemJoinig(getConst());
//        elemJoin3.elemJoinTop = hmElemFrame.get(eLayoutArea.RIGHT);
//        elemJoin3.elemJoinLeft = hmElemFrame.get(eLayoutArea.BOTTOM);
//        elemJoin3.cutAngl1 = 45;
//        elemJoin3.cutAngl2 = 45;
//        elemJoin3.anglProf = 90;
//        getHmJoinElem().put(key3, elemJoin3);
//
//        //Угловое соединение левое нижнее
//        ElemJoinig elemJoin4 = new ElemJoinig(getConst());
//        elemJoin4.elemJoinRight = hmElemFrame.get(eLayoutArea.BOTTOM);
//        elemJoin4.elemJoinTop = hmElemFrame.get(eLayoutArea.LEFT);
//        elemJoin4.cutAngl1 = 45;
//        elemJoin4.cutAngl2 = 45;
//        elemJoin4.anglProf = 90;
//        getHmJoinElem().put(key4, elemJoin4);
    }

    @Override
    public eTypeElem getTypeElem() {
        return eTypeElem.SQUARE;
    }
}

