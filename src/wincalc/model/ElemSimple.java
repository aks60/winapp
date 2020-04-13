package wincalc.model;

import domain.eArtikl;
import enums.MeasUnit;
import wincalc.constr.Specification;
import enums.TypeUse;
import java.awt.Color;
import java.util.HashMap;
import wincalc.Wincalc;

public abstract class ElemSimple extends Com5t {

    public float anglHoriz = -1; //угол к горизонту
    protected Color borderColor = Color.BLACK;
    public HashMap<String, String> mapFieldVal = new HashMap(); //свойства элемента <имя поля => значение>

    public ElemSimple(float id, Wincalc iwin, AreaSimple owner) {
        super(id, iwin, owner);
    }

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y) {  
        
        iwin().listElem.stream().forEach(el -> el.borderColor = java.awt.Color.BLACK);
        int x = (int) (X / iwin().scale1) - Com5t.TRANSLATE_X;
        int y = (int) (Y / iwin().scale1) - Com5t.TRANSLATE_Y;        
        borderColor = (inside(x, y) == true) ? Color.RED : Color.BLACK;                
        return inside(x, y);        
    }

    //Типы профилей
    public abstract TypeUse typeProfile();

    //Добавить спецификацию в состав элемента
    public abstract void addSpecifSubelem(Specification specification);

    public void anglCut(int layout, float anglCut) {
    }

    //Расчёт материала в зависимости от ед. измерения
    public void quantityMaterials(Specification specif) {

        if (MeasUnit.PIE.id == specif.artiklRec.getInt(eArtikl.currenc_id)) { //шт
            specif.count = Integer.valueOf(specif.getParam(specif.count, 11030, 33030, 14030));

            if (specif.getParam(0, 33050).equals("0") == false) {
                float widthBegin = Float.valueOf(specif.getParam(0, 33040));
                int countStep = Integer.valueOf(specif.getParam(1, 33050, 33060));
                float count = (specificationRec.width - widthBegin) / Integer.valueOf(specif.getParam(1, 33050, 33060));

                if ((specificationRec.width - widthBegin) % Integer.valueOf(specif.getParam(1, 33050, 33060)) == 0)
                    specif.count = (int) count;
                else specif.count = (int) count + 1;

                if (widthBegin != 0) ++specif.count;
            }
        } else if (MeasUnit.METR.id == specif.artiklRec.getInt(eArtikl.currenc_id)) { //метры
            if (specif.width == 0)
                specif.width = specificationRec.width; //TODO вообще это неправильно, надо проанализировать. Без этой записи специф. считается неправильно.
            specif.width = Float.valueOf(specif.getParam(specif.width, 34070)); //Длина, мм (должна быть первой)
            specif.width = specif.width + Float.valueOf(specif.getParam(0, 34051)); //Поправка, мм
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz;
    }
}
