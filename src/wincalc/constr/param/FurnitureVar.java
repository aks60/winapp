package wincalc.constr.param;

import wincalc.constr.*;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeJoin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import static wincalc.constr.ParamVariant.PAR1;
import static wincalc.constr.ParamVariant.PAR2;
import wincalc.model.ElemJoining;
import wincalc.model.ElemSimple;

//Фурнитура
public class FurnitureVar extends Par5s {

    public final int PAR1 = 3;   //Ключ 1  
    public final int PAR2 = 4;   //Ключ 2   
    public final int PAR3 = 5;   //Значение      
    private HashMap<Integer, String> hmParam = null;
    protected Wincalc iwin = null;
    private Constructiv calcConstr = null;

    public FurnitureVar(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }

    //int[] parFurl = {2101, 2104, 2140, 2185};
    protected boolean furniture(ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) {
                return false;
            }
            switch (paramRec.getInt(PAR1)) {
                
                case 21001:  //Форма контура 
                    if (TypeElem.FULLSTVORKA == elemSimple.type() && "прямоугольная".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 21004:  //Артикул створки 
                    if (elemSimple.artiklRec.getStr(eArtikl.code).equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 21005:  //Артикул заполнения по умолчанию 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21010:  //Ограничение длины стороны, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21011:  //Ограничение длины ручка константа, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21012:  //Ограничение длины ручка вариацион, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21013:  //Ограничение длины ручка по середине, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21016:  //Допустимое соотношение габаритов б/м) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21037:  //Диапазон высоты вариационной ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21040:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21050:  //Ориентация стороны, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21085:  //Надпись на эскизе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21088:  //Уравнивание складных створок 
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return true;
    }
}
