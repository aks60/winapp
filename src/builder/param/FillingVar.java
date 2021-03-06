package builder.param;

import dataset.Record;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eSystree;
import enums.TypeElem;
import java.util.List;
import builder.Wincalc;
import builder.model.ElemGlass;
import builder.model.ElemSimple;
import domain.eArtikl;
import domain.eElement;
import domain.eSetting;
import domain.eSyssize;

//Заполнения
public class FillingVar extends Par5s {

    private int[] parGrup = {13015, 13017, 13081, 13099};

    public FillingVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean filter(ElemGlass elem5e, Record glasgrpRec) {

        List<Record> paramList = eGlaspar1.find(glasgrpRec.getInt(eGlasgrp.id));
        if (filterParamDef(paramList) == false) {
            return false;
        }
        //Цикл по параметрам заполнения
        for (Record rec : paramList) {
            if (check(elem5e, rec) == false) {
                return false;
            }
        }
        return true;
    }

    //@Override
    public boolean check(ElemSimple elem5e, Record rec) {

        int grup = rec.getInt(GRUP);
        try {
            switch (grup) {
                case 13001:  //Если признак состава 
                    if (Uti4.is_11001_11002_12001_12002_13001_14001_15001_33001_34001(rec.getStr(TEXT), elem5e) == false) {
                        return false;
                    }
                    break;
                case 13003:  //Тип проема 
                    if (!Uti4.is_13003_14005_15005_37008(rec.getStr(TEXT), elem5e)) {
                        return false;
                    }
                    break;
                case 13005: //Заполнение типа 
                    if ("Стекло".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 1) {
                        return false;
                    } else if ("Стеклопакет".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 2) {
                        return false;
                    } else if ("Сендвич".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 3) {
                        return false;
                    } else if ("Вагонка".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 4) {
                        return false;
                    } else if ("Алюминевый лист".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 5) {
                        return false;
                    } else if ("Специальное стекло".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 6) {
                        return false;
                    } else if ("Конструктив".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 9) {
                        return false;
                    } else if ("Панель откоса".equals(rec.getStr(TEXT)) && elem5e.artiklRecAn.getInt(eArtikl.level2) != 15) {
                        return false;
                    }
                    break;
                case 13014:  //Углы ориентации стороны, ° 
                    elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
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
                case 13017: //Код системы содержит строку 
                    if (Uti4.is_13017_14017_24017_25017_31017_33017_34017_37017_38017(rec.getStr(TEXT), iwin) == false) {
                        return false;
                    }
                    break;
                case 13081:  //Для внешнего/внутреннего угла плоскости, ° или Мин. внутр. угол плоскости, ° 
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elem5e.anglFlat[0] > rec.getFloat(TEXT)) {
                            return false;
                        }
                    } else if (Uti4.is_13081_13082_13086_13087(elem5e, rec.getStr(TEXT))) {
                        return false;
                    }
                case 13082:  //Макс. внутр. угол плоскости, °
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elem5e.anglFlat[1] > rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                case 13086:  //Мин. внешний угол плоскости, °
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elem5e.anglFlat[2] > rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                case 13087:  //Макс. внешний угол плоскости, °
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elem5e.anglFlat[3] > rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                case 13095:  //Если признак системы конструкции 
                    if (!Uti4.is_11095_12095_31095_33095_34095_37095_38095_39095_40095(rec.getStr(TEXT), iwin.nuni)) {
                        return false;
                    }
                    break;
                case 13097:  //Трудозатраты по длине 
                    message(rec.getInt(GRUP));
                    break;
                case 13098:  //Бригада, участок) 
                    message(rec.getInt(GRUP));
                    break;
                case 13099:  //Трудозатраты, ч/ч. 
                    elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
                    break;
                default:
                    assert !(grup > 0 && grup < 50000) : "Код " + grup + "  не обработан!!!";
            }
        } catch (Exception e) {
            System.err.println("Ошибка:param.FillingVar.check()  parametr=" + grup + "    " + e);
            return false;
        }
        return true;
    }
}
