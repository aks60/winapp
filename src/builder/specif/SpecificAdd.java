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

public class SpecificAdd extends Par5s {

    public SpecificAdd(Wincalc iwin) {
        super(iwin);
    }

    public void param(SpecificRec spc, int code) {
        try {
            HashMap<Integer, String> map = spc.mapParam;
            ElemSimple el = spc.elem5e;

            switch (code) {

                case 25013: {
                    List<String> list = ParamList.find(code).dict(); //{"длины стороны", "высоты ручки", "сторона выс-ручки", "половины стороны"}
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
                    return;
                }
                case 33001: {

                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Processing.param() " + e);
        }
    }

    //Расчёт количества материала в зависимости от ед. измерения
    public void amount(SpecificRec spсRec, SpecificRec spсAdd) {

        if (UseUnit.PIE.id == spсAdd.artiklRec.getInt(eArtikl.unit)) { //шт.
            spсAdd.count = Integer.valueOf(spсAdd.getParam(spсAdd.count, 11030, 33030, 14030));
            if (spсAdd.getParam(0, 33050).equals("0") == false) {
                float widthBegin = Float.valueOf(spсAdd.getParam(0, 33040));
                int countStep = Integer.valueOf(spсAdd.getParam(1, 33050, 33060));
                float count = (spсRec.width - widthBegin) / Integer.valueOf(spсAdd.getParam(1, 33050, 33060));
                if ((spсRec.width - widthBegin) % Integer.valueOf(spсAdd.getParam(1, 33050, 33060)) == 0) {
                    spсAdd.count = (int) count;

                } else {
                    spсAdd.count = (int) count + 1;
                }
                if (widthBegin != 0) {
                    ++spсAdd.count;
                }
            }

        } else if (UseUnit.METR.id == spсAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.
            if (spсAdd.width == 0) {
                spсAdd.width = spсRec.width; //TODO вообще это неправильно, надо проанализировать. Без этой записи специф. считается неправильно.
            }
            spсAdd.width = Float.valueOf(spсAdd.getParam(spсAdd.width, 34070)); //Длина, мм (должна быть первой)
            spсAdd.width = spсAdd.width + Float.valueOf(spсAdd.getParam(0, 34051)); //Поправка, мм

        } else if (UseUnit.ML.id == spсAdd.artiklRec.getInt(eArtikl.unit)) { //мл.
            spсAdd.quant1 = Float.valueOf(spсAdd.getParam(spсAdd.quant1, 11030, 33030, 14030));
        }
    }
}
