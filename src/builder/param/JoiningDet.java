package builder.param;

import dataset.Record;
import domain.eArtikl;
import domain.eJoindet;
import domain.eJoinpar2;
import domain.eSysprof;
import domain.eSystree;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import builder.Wincalc;
import builder.model.AreaStvorka;
import builder.model.ElemJoining;
import builder.model.ElemSimple;
import common.Util;
import domain.eElement;

//Cоединения
public class JoiningDet extends Par5s {

    public JoiningDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean filter(HashMap<Integer, String> mapParam, ElemJoining elemJoin, Record joindetRec, Record elementRec) {

        List<Record> paramList = eJoinpar2.find(joindetRec.getInt(eJoindet.id));
        if (filterParamDef(paramList) == false) {
            return false;
        }
        //Цикл по параметрам соединения
        for (Record rec : paramList) {
            if (check(mapParam, elemJoin, rec, elementRec) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean check(HashMap<Integer, String> mapParam, ElemJoining elemJoin, Record rec, Record rec2) {

        int grup = rec.getInt(GRUP);
        try {
            switch (grup) {

                case 11000: //Для технологического кода контейнера 1/2
                case 12000: //Для технологического кода контейнера 1/2 
                {
                    String[] arr = rec.getStr(TEXT).split("/");
                    if (Uti4.is_STRING_XX000(arr[0], elemJoin.elem1) == false) {
                        return false;
                    }
                    if (arr.length > 1 && Uti4.is_STRING_XX000(arr[1], elemJoin.elem2) == false) {
                        return false;
                    }  
                }
                break;
                case 11001:  //Если признак состава Арт.1 
                case 12001:  //Если признак состава Арт.1 
                     if (rec.getStr(TEXT).equals(rec2.getStr(eElement.signset)) == false) {
                        return false;
                    }
                    break;
                case 11002:  //Если признак состава Арт.2 
                case 12002:  //Если признак состава Арт.2 
                    message(rec.getInt(GRUP));
                    break;
                case 11005:  //Контейнер типа 
                    message(rec.getInt(GRUP));
                    break;
                case 11008:  //Эффективное заполнение изд., мм 
                    message(rec.getInt(GRUP));
                    break;
                case 11009:  //Внешнее соединение 
                case 12009:  //Внешнее соединение                      
                    //message(rec.getInt(GRUP)); //У SA всегда внутреннее
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
                    int c1 = iwin.colorID1;
                    if (Util.containsNumb(rec.getStr(TEXT), c1) == false) {
                        return false;
                    }
                    break;
                case 11068:  //Коды внутр. текстуры изделия 
                case 12068:  //Коды внутр. текстуры изделия 
                    if (Util.containsNumb(rec.getStr(TEXT), iwin.colorID2) == false) {
                        return false;
                    }
                    break;
                case 11069:  //Коды внешн. текстуры изделия
                case 12069:  //Коды внешн. текстуры изделия     
                    int c3 = iwin.colorID3;
                    if (Util.containsNumb(rec.getStr(TEXT), c3) == false) {
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
                case 11095: //Если признак системы конструкции 
                case 12095: //Если признак системы конструкции 
                {
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
                }
                break;
                case 12005:  //Контейнер типа 
                    message(rec.getInt(GRUP));
                    break;
                case 12008:  //Эффективное заполнение изд., мм 
                    message(rec.getInt(GRUP));
                    break;
                case 12020:  //Рассчитывать с Артикулом 2 
                    message(rec.getInt(GRUP));
                    break;
                case 12027:  //Рассчитывать для профиля 
                    if ("с уплотнителем".equalsIgnoreCase(rec.getStr(TEXT)) == true && elemJoin.elem1.artiklRec.getInt(eArtikl.with_seal) == 0) {
                        return false;
                    }
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
                    assert !(grup > 0 && grup < 50000) : "Код " + grup + "  не обработан!!!";
            }
        } catch (Exception e) {
            System.err.println("Ошибка:param.JoiningDet.check()  parametr=" + grup + "    " + e);
            return false;
        }
        return true;
    }
    
    public boolean check(ElemJoining elemJoin, Record rec) {
        HashMap<Integer, String> mapParam = new HashMap();
        return JoiningDet.this.check(mapParam, elemJoin, rec, null);
    }    
}
