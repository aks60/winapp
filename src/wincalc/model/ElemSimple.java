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

    public ElemSimple(String id) {
        super(id);
    }

    //Точка попадает в контур элемента
    public boolean inside(float x, float y) {
        return (x >= x1) && (x <= x2) && (y >= y1) && (y <= y2);
    }
    
    //Типы профилей
    public abstract TypeProfile typeProfile();

    //Добавить спецификацию в состав элемента
    public abstract void addSpecifSubelem(Specification specification);
        
    public void anglCut(int layout, float anglCut) {
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
  
    public String toString() {
        return id;
    } 
}
