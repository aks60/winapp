package estimate.constr.param;

import dataset.Record;
import domain.eArtikl;
import domain.eSetting;
import enums.TypeElem;
import java.util.List;
import estimate.Wincalc;
import estimate.model.ElemSimple;

//Фурнитура
public class FurnitureVar extends Par5s {

    private int[] par = {2101, 2104, 2140, 2185};

    public FurnitureVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(ElemSimple elem5e, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record rec : tableList) {

            if (filterParamDef(rec) == false) {
                return false;
            }
            int grup = rec.getInt(GRUP);
            try {
                switch (grup) {

                    case 21001:  //Форма контура 
                        //"Прямоугольное", "Не прямоугольное", "Не арочное", "Арочное" (TypeElem.AREA - глухарь)
                        if ("прямоугольная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.RECTANGL.equals(elem5e.owner().type()) == false
                                && TypeElem.AREA.equals(elem5e.owner().type()) == false && TypeElem.STVORKA.equals(elem5e.owner().type()) == false) {
                            return false;
                        } else if ("трапециевидная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.TRAPEZE.equals(elem5e.owner().type()) == false) {
                            return false;
                        } else if ("арочная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.ARCH.equals(elem5e.owner().type()) == false) {
                            return false;
                        } else if ("не арочная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.ARCH.equals(elem5e.owner().type()) == true) {
                            return false;
                        }
                        break;
                    case 21004:  //Артикул створки 
                        if (elem5e.artiklRec.getStr(eArtikl.code).equalsIgnoreCase(rec.getStr(TEXT)) == false) {
                            return false;
                        }
                        break;
                    case 21005:  //Артикул заполнения по умолчанию 
                        message(rec.getInt(GRUP));
                        break;
                    case 21010:  //Ограничение длины стороны, мм 
                        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) { //Минимальная длина, мм
                            if (rec.getInt(TEXT) < elem5e.width()) {
                                return false;
                            }
                        } else {
                            String[] arr = rec.getStr(TEXT).split("/");
                            if (Integer.valueOf(arr[0]) > elem5e.width() || Integer.valueOf(arr[1]) < elem5e.width()) {
                                return false;
                            }
                        }
                        break;
                    case 21011:  //Ограничение длины ручка константа, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 21012:  //Ограничение длины ручка вариацион, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 21013:  //Ограничение длины ручка по середине, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 21016:  //Допустимое соотношение габаритов б/м) 
                        message(rec.getInt(GRUP));
                        break;
                    case 21037:  //Диапазон высоты вариационной ручки, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 21040:  //Ограничение угла, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 2145: //Исключить угол, °
                        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) {
                            if (rec.getFloat(TEXT) == elem5e.anglHoriz) {
                                return false;
                            }
                        }
                        break;
                    case 21050:  //Ориентация стороны, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 21085:  //Надпись на эскизе 
                        elem5e.specificationRec.putParam(grup, rec.getStr(TEXT));
                        break;
                    case 21088:  //Уравнивание складных створок 
                        message(rec.getInt(GRUP));
                        break;
                    default:
                        message(rec.getInt(GRUP));
                        break;
                }
            } catch (Exception e) {
                System.err.println("wincalc.constr.param.FurnitureVar.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
