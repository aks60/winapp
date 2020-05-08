package wincalc.constr.param;

import wincalc.constr.*;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.ParamJson;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import wincalc.model.Com5t;
import wincalc.model.ElemSimple;

//Заполнения
public class FillingDet extends Par5s {

    private int[]  par = {14000 ,14030 ,14040 ,14050 ,14060 ,14065 ,14068 ,15000 ,15005 ,15011 ,15013 ,15027 ,15030 ,15040,15045 ,15050 ,15055 ,15068 ,15069};            

    public FillingDet(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }

    public boolean check(HashMap<Integer, String> hmParam, ElemSimple ElemSimple, List<Record> paramList) {

        //Цикл по параметрам заполнения
        for (Record paramRec : paramList) {

            switch (paramRec.getInt(PAR1)) {
                case 14000:  //Для технологического кода контейнера
                case 15000:  //Для технологического кода контейнера 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14005:  //Тип проема 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14009:  //Арочное заполнение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14040:  //Порог расчета, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 14050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14060:  //Количество на шаг 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 14065:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14067:  //Коды основной текстуры изделия 
                case 15067:  //Коды основной текстуры изделия    
                    int c1 = ElemSimple.iwin().color1;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c1) == false) {
                        return false;
                    }
                    break;
                case 14068:  //Коды внутр. текстуры изделия 
                case 15068:  //Коды внутр. текстуры изделия     
                    int c2 = ElemSimple.iwin().color2;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c2) == false) {
                        return false;
                    }
                    break;
                case 14069:  //Коды внешн. текстуры изделия 
                case 15069:  //Коды внешн. текстуры изделия     
                    int c3 = ElemSimple.iwin().color3;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c3) == false) {
                        return false;
                    }
                    break;
                case 14081:  //Если артикул профиля контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14095:  //Если признак системы конструкции 
                case 15095:  //Если признак системы конструкции    
                    Record systreeRec = eSystree.find(iwin.nuni);
                    String[] arr = paramRec.getStr(PAR3).split(";");
                    List<String> arrList = Arrays.asList(arr);
                    boolean ret = false;
                    for (String str : arrList) {
                        if (systreeRec.get(eSystree.types) == Integer.valueOf(str) == true) {
                            ret = true;
                        }
                    }
                    if (ret == false) {
                        return false;
                    }
                    break;                
                case 15001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15005:  //Тип проема 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15009:  //Арочное заполнение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15010:  //Расчет реза штапика 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15011:  //Расчет реза штапика 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15013:  //Подбор дистанционных вставок пролета 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15027:  //Рассчитывать для профиля 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15030:  //[ * коэф-т ] 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 15040:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15045:  //Длина, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15050:  //Поправка, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 15051:  //Удлинение на один пог.м., мм 
                    if (ElemSimple.specificationRec.getParam("0", 31052).equals(paramRec.getStr(PAR3)) == false) {
                        hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    }
                    break;
                case 15055:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break; 
                case 15081:  //Если артикул профиля контура 
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        if (filterParamJson(ElemSimple, paramList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }
        return true;
    }
}
