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

    //Укорочение мм от высоты ручки
    public void heightHand(SpecificRec spcRec, SpecificRec spcAdd) {
        
            String ps = spcAdd.getParam("null", 25013); //Укорочение от
            List<String> list = ParamList.find(25013).dict();  //[длины стороны, высоты ручки, сторона выс-ручки, половины стороны]             
            int dx = spcAdd.getParam(25030); //"Укорочение, мм"
            
            if (list.get(0).equals(ps)) {
                spcAdd.width = spcRec.height - dx;

            } else if (list.get(1).equals(ps)) {
                AreaStvorka stv = (AreaStvorka) spcAdd.elem5e.owner();
                spcAdd.width = stv.handleHeight - dx;

            } else if (list.get(2).equals(ps)) {
                AreaStvorka stv = (AreaStvorka) spcAdd.elem5e.owner();
                ElemFrame fr = stv.mapFrame.get(stv.typeOpen.axisStv());
                spcAdd.width = fr.spcRec.width - stv.handleHeight - dx;

            } else if (list.get(3).equals(ps)) {
                spcAdd.width = spcRec.height / 2 - dx;
            }
    }

    //Расчёт количества ед. с шагом
    public int calcCountStep(SpecificRec spcRec, SpecificRec spcAdd) {

        int width_step = Integer.valueOf(spcAdd.getParam(1, 11050, 14050, 24050, 33050, 38050)); //Шаг, мм
        if (width_step > 1) {
            float width_begin = Float.valueOf(spcAdd.getParam(0, 11040, 14040, 24040, 33040, 38040)); //Порог расчета, мм
            int count_step = Integer.valueOf(spcAdd.getParam(1, 11060, 14060, 24060, 33060, 38060)); //"Количество на шаг"

            return (int) (spcRec.width - width_begin) / width_step * count_step;
        }
        return spcAdd.count;
    }
    
    public int calcCountStep2(SpecificRec spcRec, SpecificRec spcAdd) {

        int step = Integer.valueOf(spcAdd.getParam(1, 11050, 14050, 24050, 33050, 38050)); //Шаг, мм
        if (step > 1) {
            float width_begin = Float.valueOf(spcAdd.getParam(0, 11040, 14040, 24040, 33040, 38040)); //Порог расчета, мм
            int count_step = Integer.valueOf(spcAdd.getParam(1, 11060, 14060, 24060, 33060, 38060)); //"Количество на шаг"

            float count = (spcRec.width - width_begin) / step;
            if ((spcRec.width - width_begin) % count_step == 0) {

                count = count + 1;
            }
            return (width_begin != 0) ? (int) (count + 1) : (int) count;
        }
        return spcAdd.count;
    }

    //Количество ед.
    public int calcCount(SpecificRec spсRec, SpecificRec spcAdd) {
        return Integer.valueOf(spcAdd.getParam(spcAdd.count,
                11030, 12060, 14030, 15040, 25060, 33030, 34060, 38030, 39060));
    }
    
    //Пог. метры
    public float calcAmountMetr(SpecificRec spcRec, SpecificRec spcAdd) {
        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.
            return Float.valueOf(spcAdd.getParam(0, 12050, 15050, 34050, 34051, 39020)); //Поправка, мм
        }
        return spcAdd.width;
    }

    //Пог. метры длина
    public float calcAmountLenght(SpecificRec spcRec, SpecificRec spcAdd) {
        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.
            return Float.valueOf(spcAdd.getParam(spcAdd.width, 12065, 15045, 25040, 34070, 39070)); //Длина, мм 
        }
        return spcAdd.width;
    }

    //Othe
    public float calcAmount(SpecificRec spcRec, SpecificRec spcAdd) {
        return Float.valueOf(spcAdd.getParam(spcAdd.quant1,
                11030, 12060, 14030, 15040, 24030, 25060, 33030, 34060, 38030, 39060));
    }
}
