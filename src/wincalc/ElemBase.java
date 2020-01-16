package wincalc;

import constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.util.LinkedList;
import javax.swing.JComponent;

public abstract class ElemBase extends Base {

    protected float anglHoriz = -1; //угол к горизонту
    
    public ElemBase(String id) {
        this.id = id;
    }

    @Override
    public LinkedList<Base> listChild() {
        return new LinkedList();
    }
    
    /**
     * Типы профилей
     */
    public abstract TypeProfile typeProfile();

    public void anglCut(int side, float anglCut) {        
    } 
    
    /**
     * Генерация нового ключа
     */
    public String genId() {
        int maxId = 0;
        LinkedList<ElemBase> elemList = root().elemList(null);
        for (ElemBase elemBase : elemList) {
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
