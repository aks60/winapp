package builder.param;

import domain.eArtikl;
import enums.LayoutArea;
import enums.ParamList;
import enums.UseUnit;
import builder.specif.Specification;
import builder.model.AreaSimple;
import builder.model.AreaStvorka;
import builder.model.ElemFrame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Processing {

    public static void set(Specification spc, int code) {
//        try {
//            switch (code) {
//
//                case 25013: {
//                    List list = ParamList.find(code).dict();
//                    int dx = spc.getParam(25030);
//                    return;
//                }
//                case 33001: {
//
//                    return;
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Ошибка:param.Processing.set() " + e);
//        }
    }

    //Расчёт количества материала в зависимости от ед. измерения
    public static void amount(Specification spсRec, Specification spсAdd) {
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
            spсAdd.quantity = Float.valueOf(spсAdd.getParam(spсAdd.quantity, 11030, 33030, 14030));
        }
    }

    public static ElemFrame determOfSide(HashMap<Integer, String> mapParam, AreaSimple area5e) {

        //Через параметр
        if ("1".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.BOTTOM);
        } else if ("2".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.RIGHT);
        } else if ("3".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.TOP);
        } else if ("4".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.LEFT);
        } else {
            //Там где крепится ручка
            if (area5e instanceof AreaStvorka) {
                int id = ((AreaStvorka) area5e).typeOpen.id;
                if (Arrays.asList(1, 3, 11).contains(id)) {
                    return area5e.mapFrame.get(LayoutArea.LEFT);
                } else if (Arrays.asList(2, 4, 12).contains(id)) {
                    return area5e.mapFrame.get(LayoutArea.RIGHT);
                } else {
                    return area5e.mapFrame.get(LayoutArea.BOTTOM);
                }
            }
            return area5e.mapFrame.values().stream().findFirst().get();  //первая попавшаяся
        }
    }
}
