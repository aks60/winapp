package builder.specif;

import builder.Wincalc;
import domain.eArtikl;
import enums.LayoutArea;
import enums.ParamList;
import enums.UseUnit;
import builder.specif.SpecificRec;
import builder.model.AreaSimple;
import builder.model.AreaStvorka;
import builder.model.ElemFrame;
import builder.model.ElemSimple;
import builder.param.Par5s;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpecificAdd {

    protected ElemSimple elem5e = null;

    public SpecificAdd(ElemSimple elem5e) {
        this.elem5e = elem5e;
    }

    //Укорочение мм
    public void heightHand(SpecificRec spc) {
        HashMap<Integer, String> map = spc.mapParam;
        ElemSimple el = spc.elem5e;

        if (spc.mapParam.get(25013) != null) {
            List<String> list = ParamList.find(25013).dict(); //{"длины стороны", "высоты ручки", "сторона выс-ручки", "половины стороны"}
            int dx = spc.getParam(25030); //"Укорочение, мм"
            if (list.get(0).equals(map.get(25013))) {
                spc.weight = spc.weight - dx;
            } else if (list.get(1).equals(map.get(25013))) {
                spc.weight = ((AreaStvorka) el.owner()).handlHeight - dx;
            } else if (list.get(2).equals(map.get(25013))) {
                AreaStvorka stv = (AreaStvorka) el.owner();
                ElemFrame fr = stv.mapFrame.get(stv.typeOpen.axisStv());
                spc.weight = fr.spcRec.weight - dx;
            } else if (list.get(3).equals(map.get(25013))) {
                spc.weight = spc.weight / 2 - dx;
            }
        }
    }

    //Количество ед.
    public void calcCount(SpecificRec spсRec, SpecificRec spcAdd) {

        spcAdd.count = Integer.valueOf(spcAdd.getParam(spcAdd.count, 11030, 12060, 14030, 15040, 25060, 33030, 34060, 38030, 39060));

        //с шагом
        if (spcAdd.getParam(0, 33050).equals("0") == false) {
            float widthBegin = Float.valueOf(spcAdd.getParam(0, 11040, 14040, 24040, 33040, 38040));
            //float widthBegin = Float.valueOf(spcAdd.getParam(0, 33040));
            int countStep = Integer.valueOf(spcAdd.getParam(1, 33060)); //"Количество на шаг"
            //float count = (spсRec.width - widthBegin) / Integer.valueOf(spcAdd.getParam(1, 33050, 33060));
            float count = (spсRec.width - widthBegin) / Integer.valueOf(spcAdd.getParam(1, 11050, 14050, 24050, 33050, 38050)); //

            //if ((spсRec.width - widthBegin) % Integer.valueOf(spcAdd.getParam(1, 33050, 33060)) == 0) {
           if ((spсRec.width - widthBegin) % Integer.valueOf(spcAdd.getParam(1, 11060, 14060, 24060, 33060, 38060)) == 0) {
                spcAdd.count = (int) count;
            } else {
                spcAdd.count = (int) count + 1;
            }
            if (widthBegin != 0) {
                ++spcAdd.count;
            }
        }
    }

    //
    public void calcAmount(SpecificRec spсRec, SpecificRec spcAdd) {

        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.
            if (spcAdd.width == 0) {
                spcAdd.width = spсRec.width; //TODO вообще это неправильно, надо проанализировать. Без этой записи специф. считается неправильно.
            }
            spcAdd.width = Float.valueOf(spcAdd.getParam(spcAdd.width, 34070)); //Длина, мм (должна быть первой)
            spcAdd.width = spcAdd.width + Float.valueOf(spcAdd.getParam(0, 34051)); //Поправка, мм

        } else if (UseUnit.ML.id == spcAdd.artiklRec.getInt(eArtikl.unit)) { //мл.
            spcAdd.quant1 = Float.valueOf(spcAdd.getParam(spcAdd.quant1, 11030, 33030, 14030));
        }
    }
}

/*
if (spcAdd.artiklRec.getInt(eArtikl.level1) == 1
        && spcAdd.artiklRec.getInt(eArtikl.level1) == 3) {
    //UseUnit.METR

} else if (spcAdd.artiklRec.getInt(eArtikl.level1) == 5) {
    //UseUnit.METR2);

} else {
    //UseUnit.PIE, UseUnit.ML, UseUnit.GRAM, UseUnit.KG, UseUnit.LITER, UseUnit.SET, UseUnit.DOSE, UseUnit.MONTH, UseUnit.PAIR;

}
 */
