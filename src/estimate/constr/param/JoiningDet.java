package estimate.constr.param;

import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import domain.eSystree;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import estimate.Wincalc;
import static estimate.constr.Cal5e.compareInt;
import estimate.model.ElemSimple;

//Cоединения
public class JoiningDet extends Par5s {
    
    private int[] par = {11000, 11009, 11010, 11020, 11030, 11040, 11050, 11060, 11067, 11068, 11069, 11070, 11072, 11095, 12000, 12008, 12010, 12020, 12030, 12050, 12060, 12063, 12065, 12068, 12069, 12070, 12072, 12075};

    public JoiningDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(HashMap<Integer, String> mapParam, ElemSimple elem5e, List<Record> paramList) {

        if (filterParamJson(elem5e, paramList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }        
        //Цикл по параметрам соединения
        for (Record rec : paramList) {
            if (filterParamDef(rec) == false) {
                return false;
            }
            switch (rec.getInt(GRUP)) {

                case 11000:  //Для технологического кода контейнера 1/2
                case 12000:  //Для технологического кода контейнера 1/2    
                    Record sysprofRec = elem5e.sysprofRec;
                    Record artiklVRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
                    if (artiklVRec.get(eArtikl.tech_code) == null) {
                        return false;
                    }
                    String[] strList = rec.getStr(TEXT).split(";");
                    String[] strList2 = artiklVRec.getStr(eArtikl.tech_code).split(";");
                    boolean ret2 = false;
                    for (String str : strList) {
                        for (String str2 : strList2) {
                            if (str.equals(str2)) {
                                ret2 = true;
                            }
                        }
                    }
                    if (ret2 == false) {
                        return false;
                    }
                    break;
                case 11001:  //Если признак состава Арт.1 
                    message(rec.getInt(GRUP));
                    break;
                case 11002:  //Если признак состава Арт.2 
                    message(rec.getInt(GRUP));
                    break;
                case 11005:  //Контейнер типа 
                    message(rec.getInt(GRUP));
                    break;
                case 11008:  //Эффективное заполнение изд., мм 
                    message(rec.getInt(GRUP));
                    break;
                case 11009:  //Внешнее соединение 
                    message(rec.getInt(GRUP));
                    break;
                case 11010:  //Рассчитывать с Артикулом 1 
                case 12010:  //Рассчитывать с Артикулом 1                    
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 11020:  //Рассчитывать с Артикулом 2 
                    message(rec.getInt(GRUP));
                    break;
                case 11028:  //Диапозон веса заполнения, кг 
                    message(rec.getInt(GRUP));
                    break;
                case 11029:  //Расстояние узла от ручки, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 11030:  //Количество 
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 11040:  //Порог расчета, мм 
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 11050:  //Шаг, мм 
                case 12050:  //Поправка, мм    
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 11060:  //Количество на шаг 
                case 12060:  //Количество    
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 11066:  //Если текстура профиля Арт.1 
                    message(rec.getInt(GRUP));
                    break;
                case 11067:  //Коды основной текстуры изделия 
                case 12067:  //Коды основной текстуры изделия
                    int c1 = elem5e.iwin().color1;
                    if (compareInt(rec.getStr(TEXT), c1) == false) {
                        return false;
                    }
                    break;
                case 11068:  //Коды внутр. текстуры изделия 
                case 12068:  //Коды внутр. текстуры изделия 
                    int c2 = elem5e.iwin().color2;
                    if (compareInt(rec.getStr(TEXT), c2) == false) {
                        return false;
                    }
                    break;
                case 11069:  //Коды внешн. текстуры изделия
                case 12069:  //Коды внешн. текстуры изделия     
                    int c3 = elem5e.iwin().color3;
                    if (compareInt(rec.getStr(TEXT), c3) == false) {
                        return false;
                    }
                    break;
                case 11070:  //Ставить однократно 
                case 12070:  //Ставить однократно    
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 11072:  //Расчет по стороне 
                    message(rec.getInt(GRUP));
                    break;
                case 11095:  //Если признак системы конструкции 
                case 12095:  //Если признак системы конструкции     
                    Record systreefRec = eSystree.find(iwin.nuni);
                    String[] arr = rec.getStr(TEXT).split(";");
                    List<String> arrList = Arrays.asList(arr);
                    boolean ret = false;
                    for (String str : arrList) {
                        if (systreefRec.getInt(eSystree.types) == Integer.valueOf(str) == true) {
                            ret = true;
                        }
                    }
                    if (ret == false) {
                        return false;
                    }
                    break;
                case 12001:  //Если признак состава Арт.1 
                    message(rec.getInt(GRUP));
                    break;
                case 12002:  //Если признак состава Арт.2 
                    message(rec.getInt(GRUP));
                    break;
                case 12005:  //Контейнер типа 
                    message(rec.getInt(GRUP));
                    break;
                case 12008:  //Эффективное заполнение изд., мм 
                    message(rec.getInt(GRUP));
                    break;
                case 12009:  //Внешнее соединение 
                    message(rec.getInt(GRUP));
                    break;
                case 12020:  //Рассчитывать с Артикулом 2 
                    message(rec.getInt(GRUP));
                    break;
                case 12027:  //Рассчитывать для профилей 
                    message(rec.getInt(GRUP));
                    break;
                case 12028:  //Диапозон веса заполнения, кг 
                    message(rec.getInt(GRUP));
                    break;
                case 12030:  //[ * коэф-т ] 
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 12063:  //Углы реза по плоскости ригеля 
                    message(rec.getInt(GRUP));
                    break;
                case 12064:  //Учёт в длине углов плоскостей 
                    message(rec.getInt(GRUP));
                    break;
                case 12065:  //Длина, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 12072:  //Расчет по стороне 
                    message(rec.getInt(GRUP));
                    break;
                case 12075:  //Углы реза 
                    message(rec.getInt(GRUP));
                    break;
                default:
                    message(rec.getInt(GRUP));
                    break;
            }
        }
        return true;
    }
}
