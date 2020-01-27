package wincalc.model;

import enums.LayoutArea;
import wincalc.constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.util.LinkedList;
import java.util.ListIterator;

public abstract class ElemSimple extends Com5t {

    protected float anglHoriz = -1; //угол к горизонту
    protected LayoutArea side = LayoutArea.NONE; //сторона расположения элемента

    public ElemSimple(String id) {
        super(id);
    }

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
