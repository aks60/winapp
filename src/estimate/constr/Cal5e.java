package estimate.constr;

import domain.eArtikl;
import enums.UseUnit;
import java.util.*;
import estimate.Wincalc;
import estimate.model.AreaSimple;
import estimate.model.ElemSimple;

public abstract class Cal5e {

    private Wincalc iwin = null;
    public Set listVariants = new HashSet();

    public Cal5e(Wincalc iwin) {
        this.iwin = iwin;
    }

    public abstract void calc();

    public Wincalc iwin() {
        return iwin;
    }

    public AreaSimple root() {
        return iwin.rootArea;
    } 
    
    //Расчёт количества материала в зависимости от ед. измерения
    public static void quantityMaterials(Specification specificationRec, Specification specif) {

        if (UseUnit.PIE.id == specif.artiklRec.getInt(eArtikl.unit)) { //шт
            specif.count = Integer.valueOf(specif.getParam(specif.count, 11030, 33030, 14030));

            if (specif.getParam(0, 33050).equals("0") == false) {
                float widthBegin = Float.valueOf(specif.getParam(0, 33040));
                int countStep = Integer.valueOf(specif.getParam(1, 33050, 33060));
                float count = (specificationRec.width - widthBegin) / Integer.valueOf(specif.getParam(1, 33050, 33060));

                if ((specificationRec.width - widthBegin) % Integer.valueOf(specif.getParam(1, 33050, 33060)) == 0) {
                    specif.count = (int) count;
                } else {
                    specif.count = (int) count + 1;
                }

                if (widthBegin != 0) {
                    ++specif.count;
                }
            }
        } else if (UseUnit.METR.id == specif.artiklRec.getInt(eArtikl.currenc_id)) { //метры
            if (specif.width == 0) {
                specif.width = specificationRec.width; //TODO вообще это неправильно, надо проанализировать. Без этой записи специф. считается неправильно.
            }
            specif.width = Float.valueOf(specif.getParam(specif.width, 34070)); //Длина, мм (должна быть первой)
            specif.width = specif.width + Float.valueOf(specif.getParam(0, 34051)); //Поправка, мм
        }
    }    
}
