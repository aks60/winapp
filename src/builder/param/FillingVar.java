package builder.param;

import dataset.Record;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eSystree;
import enums.TypeElem;
import java.util.List;
import builder.Wincalc;
import builder.making.Uti3;
import builder.model.ElemGlass;
import domain.eSetting;

//Заполнения
public class FillingVar extends Par5s {

    private int[] parGrup = {13015, 13017, 13081, 13099};

    public FillingVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(ElemGlass elem5e, Record glasgrpRec) {

        List<Record> paramList = eGlaspar1.find(glasgrpRec.getInt(eGlasgrp.id));
        if (filterParamDef(paramList) == false) {
            return false;
        }
        //Цикл по параметрам заполнения
        for (Record rec : paramList) {

            int grup = rec.getInt(GRUP);
            try {
                switch (grup) {
                    case 13001:  //Если признак состава 
                        message(rec.getInt(GRUP));
                        break;
                    case 13003:  //Тип проема 
                        if (!Uti5.dic_13003_14005_15005_37008(rec.getStr(TEXT), elem5e)) {
                            return false;
                        }
                        break;
                    case 13005:  //Заполнение типа 
                        message(rec.getInt(GRUP));
                        break;
                    case 13014:  //Угол ориентации стороны, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 13015:  //Форма заполнения 
                        //"Прямоугольное", "Не прямоугольное", "Не арочное", "Арочное" (TypeElem.AREA - глухарь)
                        if ("Прямоугольное".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.RECTANGL.equals(elem5e.owner().type()) == false
                                && TypeElem.AREA.equals(elem5e.owner().type()) == false && TypeElem.STVORKA.equals(elem5e.owner().type()) == false) {
                            return false;
                        } else if ("Не прямоугольное".equalsIgnoreCase(rec.getStr(TEXT)) && (TypeElem.TRAPEZE.equals(elem5e.owner().type()) == false
                                && TypeElem.TRIANGL.equals(elem5e.owner().type()) == false)) {
                            return false;
                        } else if ("Арочное".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.ARCH.equals(elem5e.owner().type()) == false) {
                            return false;
                        } else if ("Не арочное".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.ARCH.equals(elem5e.owner().type()) == true) {
                            return false;
                        }
                        break;
                    case 13017:  //Код системы содержит строку 
                        Record sysprofRec = eSystree.find(iwin.nuni);
                        if (sysprofRec.getStr(eSystree.pref).contains(rec.getStr(TEXT)) == false) {
                            return false;
                        }
                        break;
                    case 13081:  //Для внешнего/внутреннего угла плоскости, ° или Мин. внутр. угол плоскости, ° 
                        if ("ps3".equals(eSetting.find(2))) {
                            if (elem5e.anglPlane[0] > rec.getFloat(TEXT)) {
                                return false;
                            }
                        } else if (Uti5.p_13081_13082_13086_13087(elem5e, rec.getStr(TEXT))) {
                            return false;
                        }
                    case 13082:  //Макс. внутр. угол плоскости, °
                        break;
                    case 13086:  //Мин. внешний угол плоскости, °
                        break;
                    case 13087:  //Макс. внешний угол плоскости, °
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
                        elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
                        break;
                    default:
                        message(rec.getInt(GRUP));
                        break;
                }
            } catch (Exception e) {
                System.err.println("Ошибка:param.FillingVar.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
