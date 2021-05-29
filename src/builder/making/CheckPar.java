package builder.making;

import domain.eArtikl;
import builder.param.ParamList;
import enums.UseUnit;
import builder.model.AreaStvorka;
import builder.model.ElemSimple;
import common.Util;
import domain.eSetting;
import java.util.List;

public class CheckPar {

    protected ElemSimple elem5e = null;

    public CheckPar(ElemSimple elem5e) {
        this.elem5e = elem5e;
    }

    //Укорочение мм от высоты ручки
    public float p25013(Specific spcRec, Specific spcAdd) {

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
        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.
            return Util.getFloat(spcAdd.getParam(0, 12050, 15050, 34050, 34051, 39020)); //Поправка, мм
        }
        return spcAdd.width;
    }

    //Пог. метры длина
    public float p_12065_15045_25040_34070_39070(Specific spcRec, Specific spcAdd) {
        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.
            return Util.getFloat(spcAdd.getParam(spcAdd.width, 12065, 15045, 25040, 34070, 39070)); //Длина, мм 
        }
        return spcAdd.width;
    }

    //Коэффициент
    public float p_12030_15030_25035_34030_39030(Specific spcRec, Specific spcAdd) {
        return Util.getFloat(spcAdd.getParam("1", 12040, 15031, 25036, 34040, 39040)); //"[ * коэф-т ]" 
    }

    public float p_12040_15031_25036_34040_39040(Specific spcRec, Specific spcAdd) {
        return Util.getFloat(spcAdd.getParam("1", 12030, 15030, 25035, 34030, 39030)); //"[ / коэф-т ]"    
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
/*
==PS3==
P2010(2010, "Угол минимальный, °", check_FLOAT),
P3010(3010, "Угол минимальный, °", check_FLOAT),
P4020(4020, "Угол минимальный, °", check_FLOAT),
P21039(21039, "Минимальный угол, °", check_FLOAT),

P2020(2020, "Угол максимальный, °", check_FLOAT),
P3020(3020, "Угол максимальный, °", check_FLOAT),
P4030(4030, "Угол максимальный, °", check_FLOAT),
P21040(21040, "Максимальный угол, °", check_FLOAT),

P2021(2021, "Точный угол, °", check_FLOAT),
P3021(3021, "Точный угол, °", check_FLOAT),
P4031(4031, "Точный угол, °", check_FLOAT),
P14065(14065, "Точный угол, °", check_FLOAT),
P15055(15055, "Точный угол, °", check_FLOAT),
P21044(21044, "Точный угол, °", check_FLOAT),

P2022(2022, "Исключить угол, °", check_FLOAT),
P3022(3022, "Исключить угол, °", check_FLOAT),
P4032(4032, "Исключить угол, °", check_FLOAT),

P1020(1020, "Угол к горизонту минимальный, °", check_FLOAT),
P1030(1030, "Угол к горизонту максимальный, °", check_FLOAT),

P1031(1031, "Точный угол к горизонту, °", check_FLOAT),
P31031(31031, "Точный угол к горизонту, °", check_FLOAT),

P1032(1032, "Исключить угол к горизонту, °", check_FLOAT),

P33083(33083, "Точный внутр. угол плоскости, °", check_FLOAT),

P34088(34088, "Точный внешний угол плоскости, °", check_FLOAT),
P33088(33088, "Точный внешний угол плоскости, °", check_FLOAT),
P34088(34088, "Точный внешний угол плоскости, °", check_FLOAT),

P13081(13081, "Мин. внутр. угол плоскости, °", check_FLOAT),
P13082(13082, "Макс. внутр. угол плоскости, °", check_FLOAT),
P13086(13086, "Мин. внешний угол плоскости, °", check_FLOAT),
P13087(13087, "Макс. внешний угол плоскости, °", check_FLOAT),

==PS4==
P2020(2020, "Ограничение угла, °", check_FLOAT),
P3020(3020, "Ограничение угла, °", check_FLOAT),
P4020(4020, "Ограничение угла, °", check_FLOAT),
P14065(14065, "Ограничение угла, °", check_FLOAT),
P15055(15055, "Ограничение угла, °", check_FLOAT),
P21040(21040, "Ограничение угла, °", check_FLOAT),

P1020(1020, "Ограничение угла к горизонту, °", check_INT),
P31020(31020, "Ограничение угла к горизонту, °", check_FLOAT),

P31060(31060, "Допустимый угол между плоскостями, °", check_FLOAT),
P13081(13081, "Для внешнего/внутреннего угла плоскости, °", check_FLOAT_LIST2),
P31081(31081, "Для внешнего/внутреннего угла плоскости, °", check_FLOAT_LIST2),
P33081(33081, "Для внешнего/внутреннего угла плоскости, °", check_FLOAT_LIST2),
P34081(34081, "Для внешнего/внутреннего угла плоскости, °", check_FLOAT_LIST2),
 */
