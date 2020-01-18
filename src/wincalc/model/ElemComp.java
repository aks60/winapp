package wincalc.model;

import enums.LayoutArea;
import wincalc.constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JComponent;

public abstract class ElemComp extends Component {

    protected float anglHoriz = -1; //угол к горизонту
    protected LayoutArea side = LayoutArea.NONE; //сторона расположения элемента

    public ElemComp(String id) {
        this.id = id;
    }
        
    @Override
    public LinkedList<Component> listChild() {
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
    public ElemComp prevElem() {
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
        LinkedList<ElemComp> elemList = root().elemList(TypeElem.FRAME, TypeElem.IMPOST, TypeElem.GLASS, TypeElem.STVORKA);
        for (ElemComp elemBase : elemList) {
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
