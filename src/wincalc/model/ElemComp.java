package wincalc.model;

import enums.LayoutArea;
import wincalc.constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import javafx.scene.shape.ArcType;
import static wincalc.model.Com5t.moveXY;

public abstract class ElemComp extends Com5t {

    protected float anglHoriz = -1; //угол к горизонту
    protected LayoutArea side = LayoutArea.NONE; //сторона расположения элемента

    public ElemComp(String id) {        
        super(id);
    }

    @Override
    public LinkedList<Com5t> listChild() {
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
        LinkedList<ElemComp> elemList = root().listElem(TypeElem.FRAME_BOX, TypeElem.IMPOST, TypeElem.GLASS, TypeElem.FRAME_STV);
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
