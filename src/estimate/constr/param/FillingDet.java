package estimate.constr.param;

import dataset.Record;
import domain.eSystree;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import estimate.Wincalc;
import static estimate.constr.Cal5e.compareInt;
import estimate.model.ElemSimple;

//Заполнения
public class FillingDet extends Par5s {

    private int[]  par = {14000 ,14030 ,14040 ,14050 ,14060 ,14065 ,14068 ,15000 ,15005 ,15011 ,15013 ,15027 ,15030 ,15040,15045 ,15050 ,15055 ,15068 ,15069};            

    public FillingDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(HashMap<Integer, String> mapParam, ElemSimple ElemSimple, List<Record> paramList) {

        if (filterParamJson(ElemSimple, paramList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }        
        //Цикл по параметрам заполнения
        for (Record paramRec : paramList) {

            switch (paramRec.getInt(GRUP)) {
                case 14000:  //Для технологического кода контейнера
                case 15000:  //Для технологического кода контейнера 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14001:  //Если признак состава 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14005:  //Тип проема 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14009:  //Арочное заполнение 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14017:  //Код системы содержит строку 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14030:  //Количество 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14040:  //Порог расчета, мм 
                    mapParam.put(paramRec.getInt(GRUP), paramRec.getStr(TEXT));
                    break;
                case 14050:  //Шаг, мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14060:  //Количество на шаг 
                    mapParam.put(paramRec.getInt(GRUP), paramRec.getStr(TEXT));
                    break;
                case 14065:  //Ограничение угла, ° 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14067:  //Коды основной текстуры изделия 
                case 15067:  //Коды основной текстуры изделия    
                    int c1 = ElemSimple.iwin().color1;
                    if (compareInt(paramRec.getStr(TEXT), c1) == false) {
                        return false;
                    }
                    break;
                case 14068:  //Коды внутр. текстуры изделия 
                case 15068:  //Коды внутр. текстуры изделия     
                    int c2 = ElemSimple.iwin().color2;
                    if (compareInt(paramRec.getStr(TEXT), c2) == false) {
                        return false;
                    }
                    break;
                case 14069:  //Коды внешн. текстуры изделия 
                case 15069:  //Коды внешн. текстуры изделия     
                    int c3 = ElemSimple.iwin().color3;
                    if (compareInt(paramRec.getStr(TEXT), c3) == false) {
                        return false;
                    }
                    break;
                case 14081:  //Если артикул профиля контура 
                    message(paramRec.getInt(GRUP));
                    break;
                case 14095:  //Если признак системы конструкции 
                case 15095:  //Если признак системы конструкции    
                    Record systreeRec = eSystree.find(iwin.nuni);
                    String[] arr = paramRec.getStr(TEXT).split(";");
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
                    message(paramRec.getInt(GRUP));
                    break;
                case 15005:  //Тип проема 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15009:  //Арочное заполнение 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15010:  //Расчет реза штапика 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15011:  //Расчет реза штапика 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15017:  //Код системы содержит строку 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15013:  //Подбор дистанционных вставок пролета 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15027:  //Рассчитывать для профиля 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15030:  //[ * коэф-т ] 
                    mapParam.put(paramRec.getInt(GRUP), paramRec.getStr(TEXT));
                    break;
                case 15040:  //Количество 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15045:  //Длина, мм 
                    message(paramRec.getInt(GRUP));
                    break;
                case 15050:  //Поправка, мм 
                    mapParam.put(paramRec.getInt(GRUP), paramRec.getStr(TEXT));
                    break;
                case 15051:  //Удлинение на один пог.м., мм 
                    if (ElemSimple.specificationRec.getParam("0", 31052).equals(paramRec.getStr(TEXT)) == false) {
                        mapParam.put(paramRec.getInt(GRUP), paramRec.getStr(TEXT));
                    }
                    break;
                case 15055:  //Ограничение угла, ° 
                    message(paramRec.getInt(GRUP));
                    break; 
                case 15081:  //Если артикул профиля контура 
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
