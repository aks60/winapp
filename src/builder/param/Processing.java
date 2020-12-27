package builder.param;

import domain.eArtikl;
import enums.LayoutArea;
import enums.ParamList;
import enums.UseUnit;
import builder.specif.Specification;
import builder.model.AreaSimple;
import builder.model.ElemFrame;
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
    public static void amount(Specification specificationRec, Specification specificationAdd) {
        if (UseUnit.PIE.id == specificationAdd.artiklRec.getInt(eArtikl.unit)) { //шт.
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
        } else if (UseUnit.METR.id == specificationAdd.artiklRec.getInt(eArtikl.unit)) { //пог.м.
            if (specificationAdd.width == 0) {
                specificationAdd.width = specificationRec.width; //TODO вообще это неправильно, надо проанализировать. Без этой записи специф. считается неправильно.
            }
            specificationAdd.width = Float.valueOf(specificationAdd.getParam(specificationAdd.width, 34070)); //Длина, мм (должна быть первой)
            specificationAdd.width = specificationAdd.width + Float.valueOf(specificationAdd.getParam(0, 34051)); //Поправка, мм

        } else if (UseUnit.ML.id == specificationAdd.artiklRec.getInt(eArtikl.unit)) { //мл.
            specificationAdd.quantity = Float.valueOf(specificationAdd.getParam(specificationAdd.quantity, 11030, 33030, 14030));
        }
    }

    public static ElemFrame determOfSide(HashMap<Integer, String> mapParam, AreaSimple area5e) {

        if ("1".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.BOTTOM);
        } else if ("2".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.RIGHT);
        } else if ("3".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.TOP);
        } else if ("4".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.LEFT);
        } else {
            return area5e.mapFrame.values().stream().findFirst().get();  //первая попавшаяся
        }
    }
}
