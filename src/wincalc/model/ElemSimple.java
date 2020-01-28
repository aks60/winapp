package wincalc.model;

import enums.LayoutArea;
import wincalc.constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public abstract class ElemSimple extends Com5t {

    protected float anglHoriz = -1; //угол к горизонту
    protected LayoutArea side = LayoutArea.NONE; //сторона расположения элемента

    public ElemSimple(String id) {
        super(id);
    }

    public boolean inside(float x, float y) {
        //return (x >= 0) && (x < width) && (y >= 0) && (y < height);
        return (x >= x1) && (x <= x2) && (y >= y1) && (y <= y2);
    }
    
//    public void passJoinElem(HashMap<String, String> map) {
//
//        String k1 = (int)owner.x1 + ":" + (int)owner.y1;
//        if (map.get(k1) == null) {
//            map.put(k1, "");
//        }
//        if (this.inside(x1, y1) == true) {
//            map.put(k1, map.get(k1) + " " + id);
//        }
//        String k2 = (int)owner.x1 + ":" + (int)owner.y2;
//        map.put(k2, "");
//        if (this.inside(x1, y2) == true) {
//            map.put(k2, map.get(k2) + " " + id);
//        }
//        String k3 = (int)owner.x2 + ":" + (int)owner.y2;
//        map.put(k3, "");
//        if (this.inside(x2, y2) == true) {
//            map.put(k3, map.get(k3) + " " + id);
//        }
//        String k4 = (int)owner.x2 + ":" + (int)owner.y1;
//        map.put(k4, "");
//        if (this.inside(x2, y1) == true) {
//            map.put(k4, map.get(k4) + " " + id);
//        }
//    }
    
    //Типы профилей
    public abstract TypeProfile typeProfile();

    //Добавить спецификацию в состав элемента
    public abstract void addSpecifSubelem(Specification specification);
        
    public void anglCut(int side, float anglCut) {
    }

    //Получить предыдущий элемент в контейнере
    public Com5t prevElem() {
        
        LinkedList<ElemSimple> list = root().listElem(root(), TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST, TypeElem.GLASS);
        for (ListIterator<ElemSimple> iter = list.listIterator(); iter.hasNext(); ) {
            if (iter.next().id == id) { //находим элемент в списке и от него движемся вверх
                iter.previous();
                if (iter.hasPrevious()) {
                    return iter.previous();
                }
                return iter.next();
            }
        }
        return owner;
    }

    //Генерация нового ключа
    public String genId() {
        int maxId = 0;
        LinkedList<ElemSimple> elemList = root().listElem(root(), TypeElem.FRAME_BOX, TypeElem.IMPOST, TypeElem.GLASS, TypeElem.FRAME_STV);
        for (ElemSimple elemBase : elemList) {
            for (Specification specification : elemBase.specificationRec.specificationList()) {
                if (Integer.valueOf(elemBase.specificationRec.id) > maxId) {
                    maxId = Integer.valueOf(elemBase.specificationRec.id);
                }
                if (Integer.valueOf(specification.id) > maxId) {
                    maxId = Integer.valueOf(specification.id);
                }
            }
        }
        return String.valueOf(++maxId);
    }

}
