package builder.param;

import dataset.Record;
import domain.eArtikl;
import domain.eFurnpar1;
import domain.eFurnside1;
import domain.eSetting;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.List;
import builder.Wincalc;
import builder.model.ElemSimple;
import common.Util;

//Фурнитура
public class FurnitureVar extends Par5s {    

    public FurnitureVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(ElemSimple elem5e, Record furnside1Rec) {

        List<Record> paramList = eFurnpar1.find(furnside1Rec.getInt(eFurnside1.id));
        if (filterParamDef(paramList) == false) {
            return false;
        }        
        //Цикл по параметрам фурнитуры
        for (Record rec : paramList) {

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
                    case 21010: //Ограничение длины стороны, мм 
                    {
                        float length = 0;
                        if (LayoutArea.LEFT == elem5e.layout() || LayoutArea.RIGHT == elem5e.layout()) {
                            length = elem5e.height();
                        } else if (LayoutArea.TOP == elem5e.layout() || LayoutArea.BOTT == elem5e.layout()) {
                            length = elem5e.width();
                        }
                        if ("ps3".equals(eSetting.find(2))) { //Минимальная длина, мм
                            if (rec.getInt(TEXT) > length) {
                                return false;
                            }
                        } else {
                            String[] arr = rec.getStr(TEXT).split("/");
                            if (Integer.valueOf(arr[0]) > length || Integer.valueOf(arr[1]) < length) {
                                return false;
                            }
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
                    case 21039:  //Минимальный угол, °
                        if ("ps3".equals(eSetting.find(2))) {
                            if (elem5e.anglHoriz < rec.getFloat(TEXT)) {
                                return false;
                            }
                        }                         
                        break;
                    case 21040:  //Ограничение угла, ° или Угол максимальный, ° для ps3 
                        if ("ps3".equals(eSetting.find(2))) {
                            if (rec.getFloat(TEXT) > elem5e.anglHoriz) {
                                return false;
                            }
                        } else if (Util.compareBetween(rec.getStr(TEXT), elem5e.anglHoriz) == false) {
                            return false;
                        }
                        break;
                    case 21044:  //Точный угол 
                        if ("ps3".equals(eSetting.find(2))) {
                            if (rec.getFloat(TEXT) != elem5e.anglHoriz) {
                                return false;
                            }
                        }
                        break;                        
                    case 21045: //Исключить угол, °
                        if ("ps3".equals(eSetting.find(2))) {
                            if (rec.getFloat(TEXT) == elem5e.anglHoriz) {
                                return false;
                            }
                        }
                        break;
                    case 21050:  //Ориентация стороны, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 21085:  //Надпись на эскизе 
                        elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
                        break;
                    case 21088:  //Уравнивание складных створок 
                        message(rec.getInt(GRUP));
                        break;
                    default:
                        assert !(grup > 0 && grup < 50000) : "Код " + grup + "  не обработан!!!";
                }
            } catch (Exception e) {
                System.err.println("Ошибка: param.FurnitureVar.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
//private int[] par = {2101, 2104, 2140, 2185};
