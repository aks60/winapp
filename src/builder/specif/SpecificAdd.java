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
                spc.weight = ((AreaStvorka) el.owner()).handleHeight - dx;
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
    public int calcCount(SpecificRec spсRec, SpecificRec spcAdd) {
        return Integer.valueOf(spcAdd.getParam(spcAdd.count,
                11030, 12060, 14030, 15040, 25060, 33030, 34060, 38030, 39060));
    }

    //Количество ед. с шагом
    public int calcCountStep(SpecificRec spсRec, SpecificRec spcAdd) {

        int step = Integer.valueOf(spcAdd.getParam(1, 11050, 14050, 24050, 33050, 38050)); //Шаг, мм
        if (step > 1) {
            float width_begin = Float.valueOf(spcAdd.getParam(0, 11040, 14040, 24040, 33040, 38040)); //Порог расчета, мм
            int count_step = Integer.valueOf(spcAdd.getParam(1, 11060, 14060, 24060, 33060, 38060)); //"Количество на шаг"

            float count = (spсRec.width - width_begin) / step;
            if ((spсRec.width - width_begin) % count_step == 0) {

                count = count + 1;
            }
            return (width_begin != 0) ? (int) (count + 1) : (int) count;
        }
        return spcAdd.count;
    }

    //Пог. метры
    public float calcAmountMetr(SpecificRec spcRec, SpecificRec spcAdd) {
        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.

            if (spcAdd.width == 0) {
                spcAdd.width = spcRec.width; //TODO вообще это неправильно, надо проанализировать. Без этой записи специф. считается неправильно.
            }
            float width = Float.valueOf(spcAdd.getParam(spcAdd.width, 12065, 15045, 25040, 34070, 39070)); //Длина, мм (должна быть первой)
            return width + Float.valueOf(spcAdd.getParam(0, 12050, 15050, 34050, 34051, 39020)); //Поправка, мм
        }
        return spcAdd.width;
    }

    //Othe
    public float calcAmount(SpecificRec spcRec, SpecificRec spcAdd) {
        return Float.valueOf(spcAdd.getParam(spcAdd.quant1,
                11030, 12060, 14030, 15040, 24030, 25060, 33030, 34060, 38030, 39060));
    }
}
