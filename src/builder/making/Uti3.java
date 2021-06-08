package builder.making;

import domain.eArtikl;
import builder.param.ParamList;
import enums.UseUnit;
import builder.model.AreaStvorka;
import builder.model.ElemSimple;
import common.Util;
import domain.eSetting;
import java.util.List;

public class Uti3 {

    protected ElemSimple elem5e = null;

    public Uti3(ElemSimple elem5e) {
        this.elem5e = elem5e;
    }

    //Укорочение мм от высоты ручки
    public float p_25013(Specific spcRec, Specific spcAdd) {

        String ps = spcAdd.getParam("null", 25013); //Укорочение от
        List<String> list = ParamList.find(25013).dict();  //[длины стороны, высоты ручки, сторона выс-ручки, половины стороны]             
        float dx = spcAdd.getParam(25030); //"Укорочение, мм"

        if (list.get(0).equals(ps)) {
            return spcRec.width - dx;

        } else if (list.get(1).equals(ps)) {
            AreaStvorka stv = (AreaStvorka) spcAdd.elem5e.owner();
            return stv.handleHeight - dx;

        } else if (list.get(2).equals(ps)) {
            AreaStvorka stv = (AreaStvorka) spcAdd.elem5e.owner();
            return spcRec.width - stv.handleHeight - dx;

        } else if (list.get(3).equals(ps)) {
            return spcRec.width / 2 - dx;
        }
        return spcAdd.width;
    }

    //Расчёт количества ед. с шагом
    public float p_11050_14050_24050_33050_38050(ElemSimple elem5e, Specific spcAdd) {

        //if (UseUnit.PIE.id == spcAdd.artiklRec.getInt(eArtikl.unit)) {
        int step = Integer.valueOf(spcAdd.getParam(-1, 11050, 14050, 24050, 33050, 38050)); //Шаг, мм
        if (step != -1) {
            float width_begin = Util.getFloat(spcAdd.getParam(0, 11040, 14040, 24040, 33040, 38040)); //Порог расчета, мм
            int count_step = Integer.valueOf(spcAdd.getParam(1, 11060, 14060, 24060, 33060, 38060)); //"Количество на шаг"
            float width_next = elem5e.length() - width_begin;

            int count = (int) width_next / step;
            if (count_step == 1) {
                if (count < 1) {
                    return 1;
                }
                return (width_next % step > 0) ? ++count : count;
            } else {
                int count2 = (int) width_next / step;
                int count3 = (int) (width_next % step) / (step / count_step);
                return ((width_next % step) % (step / count_step) > 0) ? count2 * count_step + count3 + 1 : count2 * count_step + count3;
            }
        }
        //}
        return 0;
    }

    //Количество ед.
    public float p_11030_12060_14030_15040_25060_33030_34060_38030_39060(Specific spсRec, Specific spcAdd) {
        return Util.getFloat(spcAdd.getParam(spcAdd.count,
                11030, 12060, 14030, 15040, 25060, 33030, 34060, 38030, 39060));
    }

    //Пог. метры
    public float p_12050_15050_34050_34051_39020(Specific spcRec, Specific spcAdd) {
        if (UseUnit.METR.id == spcAdd.artiklDet.getInt(eArtikl.unit)) { //пог.м.
            return Util.getFloat(spcAdd.getParam(0, 12050, 15050, 34050, 34051, 39020)); //Поправка, мм
        }
        return spcAdd.width;
    }

    //Пог. метры длина
    public float p_12065_15045_25040_34070_39070(Specific spcRec, Specific spcAdd) {
        if (UseUnit.METR.id == spcAdd.artiklDet.getInt(eArtikl.unit)) { //пог.м.
            return Util.getFloat(spcAdd.getParam(spcAdd.width, 12065, 15045, 25040, 34070, 39070)); //Длина, мм 
        }
        return spcAdd.width;
    }

    //Коэффициент, [ * коэф-т ]
    public float p_12030_15030_25035_34030_39030(Specific spcRec, Specific spcAdd) {
        return Util.getFloat(spcAdd.getParam("1", 12040, 15031, 25036, 34040, 39040));  
    }

    //Коэффициент, [ / коэф-т ]
    public float p_12040_15031_25036_34040_39040(Specific spcRec, Specific spcAdd) {
        return Util.getFloat(spcAdd.getParam("1", 12030, 15030, 25035, 34030, 39030));    
    }

    //Othe
    public float p_11030_12060_14030_15040_24030_25060_33030_34060_38030_39060(Specific spcRec, Specific spcAdd) {
        return Util.getFloat(spcAdd.getParam(spcAdd.quant1,
                11030, 12060, 14030, 15040, 24030, 25060, 33030, 34060, 38030, 39060));
    }

    //Задать Угол_реза_1/Угол_реза_2, °
    public void p_34077_34078(Specific spcAdd) {
        if ("ps3".equals(eSetting.find(2))) {
            if (spcAdd.getParam("-1", 34077).equals("-1") == false) {
                spcAdd.anglCut1 = Util.getFloat(spcAdd.getParam("-1", 34077));
            }
            if (spcAdd.getParam("-1", 34078).equals("-1") == false) {
                spcAdd.anglCut2 = Util.getFloat(spcAdd.getParam("-1", 34078));
            }
        } else {
            if (spcAdd.getParam("-1", 34077).equals("-1") == false) {
                String[] arr = spcAdd.getParam("-1", 34077).split("/");
                if (arr[0].equals("*") == false) {
                    spcAdd.anglCut1 = Util.getFloat(arr[0]);
                }
                if (arr[1].equals("*") == false) {
                    spcAdd.anglCut2 = Util.getFloat(arr[1]);
                }
            }
        }
    }
}
