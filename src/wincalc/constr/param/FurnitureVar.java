package wincalc.constr.param;

import wincalc.constr.*;
import dataset.Record;
import domain.eArtikl;
import enums.TypeElem;
import java.util.List;
import wincalc.Wincalc;
import wincalc.model.ElemSimple;

//Фурнитура
public class FurnitureVar extends Par5s {

    private int[] par = {2101, 2104, 2140, 2185};
    
    public FurnitureVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean furniture(ElemSimple elem5e, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) {
                return false;
            }
            switch (paramRec.getInt(GRUP)) {
                
                case 21001:  //Форма контура 
                    if (TypeElem.FULLSTVORKA == elem5e.type() && "прямоугольная".equals(paramRec.getStr(TEXT)) == false) {
                        return false;
                    }
                    break;
                case 21004:  //Артикул створки 
                    if (elem5e.artiklRec.getStr(eArtikl.code).equals(paramRec.getStr(TEXT)) == false) {
                        return false;
                    }
                    break;
                case 21005:  //Артикул заполнения по умолчанию 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21010:  //Ограничение длины стороны, мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21011:  //Ограничение длины ручка константа, мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21012:  //Ограничение длины ручка вариацион, мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21013:  //Ограничение длины ручка по середине, мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21016:  //Допустимое соотношение габаритов б/м) 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21037:  //Диапазон высоты вариационной ручки, мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21040:  //Ограничение угла, ° 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21050:  //Ориентация стороны, ° 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21085:  //Надпись на эскизе 
                    message(paramRec.getInt(GRUP));
                    break;
                case 21088:  //Уравнивание складных створок 
                    message(paramRec.getInt(GRUP));
                    break;
                default:
                    message(paramRec.getInt(GRUP));
                    break;
            }
        }
        return true;
    }
}
