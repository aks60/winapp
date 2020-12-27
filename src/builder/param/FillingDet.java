package builder.param;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlaspar2;
import domain.eSetting;
import domain.eSystree;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import builder.Wincalc;
import builder.constr.Util;
import builder.model.ElemSimple;

//Заполнения
public class FillingDet extends Par5s {

    private int[] par = {14000, 14030, 14040, 14050, 14060, 14065, 14068, 15000, 15005, 15011, 15013, 15027, 15030, 15040, 15045, 15050, 15055, 15068, 15069};

    public FillingDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(HashMap<Integer, String> mapParam, ElemSimple elem5e, Record glasdetRec) {

        List<Record> paramList = eGlaspar2.find(glasdetRec.getInt(eGlasdet.id)); //список параметров детализации  
        if (filterParamDef(paramList) == false) {
            return false; //параметры по умолчанию
        }
        //Цикл по параметрам заполнения
        for (Record rec : paramList) {

            int grup = rec.getInt(GRUP);
            try {
                switch (grup) {
                    case 14000: //Для технологического кода контейнераi
                    case 15000: //Для технологического кода контейнера 
                    {
                        Record artiklRec = iwin.artiklRec;
                        if (artiklRec.get(eArtikl.tech_code) == null) {
                            return false;
                        }
                        String[] strList = rec.getStr(TEXT).split(";");
                        String[] strList2 = artiklRec.getStr(eArtikl.tech_code).split(";");
                        boolean ret2 = false;
                        for (String str : strList) {
                            for (String str2 : strList2) {
                                if (str.equalsIgnoreCase(str2)) {
                                    ret2 = true;
                                }
                            }
                        }
                        if (ret2 == false) {
                            return false;
                        }
                    }
                    break;
                    case 14001:  //Если признак состава 
                        message(rec.getInt(GRUP));
                        break;
                    case 14005:  //Тип проема 
                        message(rec.getInt(GRUP));
                        break;
                    case 14008:  //Эффективное заполнение изд., мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 14009:  //Арочное заполнение 
                        message(rec.getInt(GRUP));
                        break;
                    case 14017:  //Код системы содержит строку 
                        message(rec.getInt(GRUP));
                        break;
                    case 14030:  //Количество 
                        mapParam.put(grup, rec.getStr(TEXT));
                        break;
                    case 14040:  //Порог расчета, мм 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 14050:  //Шаг, мм 
                        mapParam.put(grup, rec.getStr(TEXT));
                        break;
                    case 14060:  //Количество на шаг 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 14065:  //Ограничение угла, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 14066: //Исключить угол, °
                    case 15056:
                        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) {
                            if (rec.getFloat(TEXT) == elem5e.anglHoriz) {
                                return false;
                            }
                        }
                        break;
                    case 14067:  //Коды основной текстуры изделия 
                    case 15067:  //Коды основной текстуры изделия    
                        int c1 = elem5e.iwin().colorID1;
                        if (Util.containsInt(rec.getStr(TEXT), c1) == false) {
                            return false;
                        }
                        break;
                    case 14068:  //Коды внутр. текстуры изделия 
                    case 15068:  //Коды внутр. текстуры изделия     
                        int c2 = elem5e.iwin().colorID2;
                        if (Util.containsInt(rec.getStr(TEXT), c2) == false) {
                            return false;
                        }
                        break;
                    case 14069:  //Коды внешн. текстуры изделия 
                    case 15069:  //Коды внешн. текстуры изделия     
                        int c3 = elem5e.iwin().colorID3;
                        if (Util.containsInt(rec.getStr(TEXT), c3) == false) {
                            return false;
                        }
                        break;
                    case 14081:  //Если артикул профиля контура 
                        message(rec.getInt(GRUP));
                        break;
                    case 14095:  //Если признак системы конструкции 
                    case 15095: //Если признак системы конструкции  
                    {
                        Record systreeRec = eSystree.find(iwin.nuni);
                        String[] arr = rec.getStr(TEXT).split(";");
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
                    }
                    break;
                    case 15001:  //Если признак состава 
                        message(rec.getInt(GRUP));
                        break;
                    case 15005:  //Тип проема 
                        message(rec.getInt(GRUP));
                        break;
                    case 15008:  //Эффективное заполнение изд., мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 15009:  //Арочное заполнение 
                        message(rec.getInt(GRUP));
                        break;
                    case 15010:  //Расчет реза штапика 
                        mapParam.put(grup, rec.getStr(TEXT));
                        break;
                    case 15011:  //Расчет реза штапика 
                        mapParam.put(grup, rec.getStr(TEXT));
                        break;
                    case 15017:  //Код системы содержит строку 
                        message(rec.getInt(GRUP));
                        break;
                    case 15013:  //Подбор дистанционных вставок пролета 
                        message(rec.getInt(GRUP));
                        break;
                    case 15027:  //Рассчитывать для профиля 
                        if ("с уплотнителем".equalsIgnoreCase(rec.getStr(TEXT)) == true && elem5e.artiklRec.getInt(eArtikl.with_seal) == 0) {
                            return false;
                        }
                        break;
                    case 15030:  //[ * коэф-т ] 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 15040:  //Количество 
                        message(rec.getInt(GRUP));
                        break;
                    case 15045:  //Длина, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 15050:  //Поправка, мм 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 15051:  //Удлинение на один пог.м., мм 
                        if (elem5e.specificationRec.getParam("0", 31052).equalsIgnoreCase(rec.getStr(TEXT)) == false) {
                            mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        }
                        break;
                    case 15055:  //Ограничение угла, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 15081:  //Если артикул профиля контура 
                        message(rec.getInt(GRUP));
                        break;
                    default:
                        message(rec.getInt(GRUP));
                        break;
                }
            } catch (Exception e) {
                System.err.println("Ошибка:param.FillingDet.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
