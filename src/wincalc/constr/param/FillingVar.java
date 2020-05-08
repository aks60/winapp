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

//Заполнения
public class FillingVar extends Par5s {

    private int[] parGrup = {13015, 13017, 13081, 13099};
    
    public FillingVar(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }
        
    protected boolean check(ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам заполнения
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) {
                return false;
            }
            switch (paramRec.getInt(PAR1)) {
                case 13001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13003:  //Тип проема 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13005:  //Заполнение типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13014:  //Угол ориентации стороны, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13015:  //Форма заполнения 
                    if (paramRec.getStr(PAR3).equals(elemSimple.specificationRec.getParam("empty", 13015)) == false) { //нужно проверить
                        return false;
                    }
                    break;
                case 13017:  //Код системы содержит строку 
                    Record sysprofRec = eSystree.find(iwin.nuni);
                    if (sysprofRec.getStr(eSystree.pref).contains(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 13081:  //Для внешнего/внутреннего угла плоскости, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13097:  //Трудозатраты по длине 
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
