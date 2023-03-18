package common;

import builder.IElem5e;
import builder.model.ElemJoining;
import builder.model.ElemSimple;
import enums.Type;
import java.util.HashMap;

public class HashMap2<K extends String, V extends ElemJoining> extends HashMap<K, V> {

    public HashMap2() {
        super();
    }

    public IElem5e get(ElemSimple elem5e, int side) {
        
        String point = elem5e.joinPoint(side);       
        ElemJoining ej = get(point);       
        if (ej != null && side == 0) {
            return (elem5e.type() == Type.IMPOST || elem5e.type() == Type.SHTULP || elem5e.type() == Type.STOIKA) ? ej.elem2 : ej.elem1;
        } else if (ej != null && side == 1) {
            return ej.elem2;
        } else if (ej != null && side == 2) {
            return ej.elem2;
        }
        System.err.println("Неудача:ElemSimple.joinElem() id=" + elem5e.id() + " соединение не найдено");
        return null;
    }    
}
