package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.HashMap;
import java.util.LinkedList;
import wincalc.Wincalc;

public class AreaScene extends AreaContainer {

    public AreaScene(Wincalc iwin, AreaContainer owner, String id, LayoutArea layout, float width, float height) {
        super(iwin, owner, id, layout, width, height);
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
