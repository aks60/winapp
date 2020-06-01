package estimate.constr.param;

import dataset.Record;
import domain.eSystree;
import enums.TypeGlass;
import java.util.List;
import estimate.Wincalc;
import estimate.model.ElemGlass;
import estimate.model.ElemSimple;

//Заполнения
public class FillingVar extends Par5s {

    private int[] parGrup = {13015, 13017, 13081, 13099};

    public FillingVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(ElemSimple elem5e, List<Record> paramList) {

        //Цикл по параметрам заполнения
        for (Record rec : paramList) {

            if (filterParamDef(rec) == false) {
                return false;
            }
            int grup = rec.getInt(GRUP);
            switch (grup) {
                case 13001:  //Если признак состава 
                    message(rec.getInt(GRUP));
                    break;
                case 13003:  //Тип проема 
                    message(rec.getInt(GRUP));
                    break;
                case 13005:  //Заполнение типа 
                    message(rec.getInt(GRUP));
                    break;
                case 13014:  //Угол ориентации стороны, ° 
                    message(rec.getInt(GRUP));
                    break;
                case 13015:  //Форма заполнения 
                    //"Прямоугольное", "Не прямоугольное", "Не арочное", "Арочное"
                    message(grup, elem5e.type(), ((ElemGlass) elem5e).typeGlass, rec.getStr(TEXT));
                    if ("Прямоугольное".equals(rec.getStr(TEXT)) && TypeGlass.RECTANGL.equals(((ElemGlass) elem5e).typeGlass) == false) {
                        return false;
                    } else if ("Арочное".equals(rec.getStr(TEXT)) && TypeGlass.ARCH.equals(((ElemGlass) elem5e).typeGlass) == false) {
                        return false;
                    }
                    break;
                case 13017:  //Код системы содержит строку 
                    Record sysprofRec = eSystree.find(iwin.nuni);
                    if (sysprofRec.getStr(eSystree.pref).contains(rec.getStr(TEXT)) == false) {
                        return false;
                    }
                    break;
                case 13081:  //Для внешнего/внутреннего угла плоскости, ° 
                    message(rec.getInt(GRUP), "Проверка в заполнении ломаного фасада");
                    break;
                case 13095:  //Если признак системы конструкции 
                    message(rec.getInt(GRUP));
                    break;
                case 13098:  //Бригада, участок) 
                    message(rec.getInt(GRUP));
                    break;
                case 13097:  //Трудозатраты по длине 
                    message(rec.getInt(GRUP));
                    break;
                case 13099:  //Трудозатраты, ч/ч. 
                    message(grup, rec.getStr(TEXT));
                    elem5e.specificationRec.putParam(grup, rec.getStr(TEXT));
                    break;
                default:
                    message(rec.getInt(GRUP));
                    break;
            }
        }
        return true;
    }
}
