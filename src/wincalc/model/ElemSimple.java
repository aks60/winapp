package wincalc.model;

import wincalc.constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.awt.Color;
import java.util.LinkedList;

public abstract class ElemSimple extends Com5t {

    protected float anglHoriz = -1; //угол к горизонту
    protected Color borderColor = Color.BLACK;

    public ElemSimple(String id) {
        super(id);
    }

    //Попадание точки в элемент
    public boolean contains(int X, int Y) {  
        
        iwin.listElem.stream().forEach(el -> el.borderColor = java.awt.Color.BLACK);
        int x = (int) (X / iwin.scaleDxy) - Com5t.TRANSLATE_X;
        int y = (int) (Y / iwin.scaleDxy) - Com5t.TRANSLATE_Y;        
        borderColor = (inside(x, y) == true) ? Color.RED : Color.BLACK;                
        return inside(x, y);        
    }

    //Точка попадает в контур элемента
    public boolean inside(float x, float y) {
        if (((int) x2 | (int) y2) < 0) {
            return false;
        }
        if (x < x1 || y < y1) {
            return false;
        }
        return ((x2 < x1 || x2 >= x) && (y2 < y1 || y2 >= y));
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

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz;
    }
}
