package wincalc.model;

import enums.LayoutArea;
import wincalc.constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JComponent;

public abstract class ElemComponent extends Common {

    protected float anglHoriz = -1; //угол к горизонту
    protected LayoutArea side = LayoutArea.NONE; //сторона расположения элемента

    public ElemComponent(String id) {
        this.id = id;
    }

    public boolean inside(int x, int y) {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height);
    }
        
    @Override
    public LinkedList<Common> listChild() {
        return new LinkedList();
    }

    /**
     * Типы профилей
     */
    public abstract TypeProfile typeProfile();

    /**
     * Добавить спецификацию в состав элемента
     */
    public abstract void addSpecifSubelem(Specification specification);

    public void anglCut(int side, float anglCut) {
    }

    /**
     * Получить предыдущий элемент в контейнере
     */
    public ElemComponent prevElem() {
        /*
        for (ListIterator<ElemBase> iter = getAreaElemList().listIterator(); iter.hasNext(); ) {
            if (iter.next().id == id) { //находим элемент в списке и от него движемся вверх
                iter.previous();
                if (iter.hasPrevious()) {
                    return iter.previous();
                }
                return iter.next();
            }
        }
        return owner;*/
        return null;
    }

    /**
     * Генерация нового ключа
     */
    public String genId() {
        int maxId = 0;
        LinkedList<ElemComponent> elemList = root().elemList(TypeElem.FRAME, TypeElem.IMPOST, TypeElem.GLASS, TypeElem.STVORKA);
        for (ElemComponent elemBase : elemList) {
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
