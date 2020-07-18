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
    public static void amount(Specification specificationRec, Specification specificationAdd) {
        if (UseUnit.PIE.id == specificationAdd.artiklRec.getInt(eArtikl.unit)) { //шт
            specificationAdd.count = Integer.valueOf(specificationAdd.getParam(specificationAdd.count, 11030, 33030, 14030));

            if (specificationAdd.getParam(0, 33050).equals("0") == false) {
                float widthBegin = Float.valueOf(specificationAdd.getParam(0, 33040));
                int countStep = Integer.valueOf(specificationAdd.getParam(1, 33050, 33060));
                float count = (specificationRec.width - widthBegin) / Integer.valueOf(specificationAdd.getParam(1, 33050, 33060));

                if ((specificationRec.width - widthBegin) % Integer.valueOf(specificationAdd.getParam(1, 33050, 33060)) == 0) {
                    specificationAdd.count = (int) count;
                } else {
                    specificationAdd.count = (int) count + 1;
                }

                if (widthBegin != 0) {
                    ++specificationAdd.count;
                }
            }
        } else if (UseUnit.METR.id == specificationAdd.artiklRec.getInt(eArtikl.unit)) { //метры
            if (specificationAdd.width == 0) {
                specificationAdd.width = specificationRec.width; //TODO вообще это неправильно, надо проанализировать. Без этой записи специф. считается неправильно.
            }
            specificationAdd.width = Float.valueOf(specificationAdd.getParam(specificationAdd.width, 34070)); //Длина, мм (должна быть первой)
            specificationAdd.width = specificationAdd.width + Float.valueOf(specificationAdd.getParam(0, 34051)); //Поправка, мм
        }
    }
}
