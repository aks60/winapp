package estimate.constr.param;

import dataset.Record;
import domain.eSystree;
import enums.TypeElem;
import java.util.List;
import estimate.Wincalc;
import estimate.model.ElemGlass;

//Заполнения
public class FillingVar extends Par5s {

    private int[] parGrup = {13015, 13017, 13081, 13099};

    public FillingVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(ElemGlass elem5e, List<Record> paramList) {

        //Цикл по параметрам заполнения
        for (Record rec : paramList) {

            if (filterParamDef(rec) == false) {
                return false;
            }
            int grup = rec.getInt(GRUP);
            try {
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
                        //"Прямоугольное", "Не прямоугольное", "Не арочное", "Арочное" (TypeElem.AREA - глухарь)
                        if ("Прямоугольное".equals(rec.getStr(TEXT)) && TypeElem.RECTANGL.equals(elem5e.owner().type()) == false
                                && TypeElem.AREA.equals(elem5e.owner().type()) == false && TypeElem.STVORKA.equals(elem5e.owner().type()) == false) {
                            return false;
                        } else if ("Не прямоугольное".equals(rec.getStr(TEXT)) && (TypeElem.TRAPEZE.equals(elem5e.owner().type()) == false
                                && TypeElem.TRIANGL.equals(elem5e.owner().type()) == false)) {
                            return false;
                        } else if ("Арочное".equals(rec.getStr(TEXT)) && TypeElem.ARCH.equals(elem5e.owner().type()) == false) {
                            return false;
                        } else if ("Не арочное".equals(rec.getStr(TEXT)) && TypeElem.ARCH.equals(elem5e.owner().type()) == true) {
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
                        elem5e.specificationRec.putParam(grup, rec.getStr(TEXT));
                        break;
                    default:
                        message(rec.getInt(GRUP));
                        break;
                }
            } catch (Exception e) {
                System.err.println("wincalc.constr.param.FillingVar.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
