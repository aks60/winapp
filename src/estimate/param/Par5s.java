package estimate.param;

import dataset.Record;
import domain.eSetting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import startup.Main;
import estimate.Wincalc;
import estimate.constr.Specification;
import estimate.model.Com5t;

public class Par5s {

    protected final int ID = 1;   //Ключ в таблице  
    protected final int GRUP = 3;   //Ключ параметра    
    protected final int TEXT = 2;   //Текст 
    protected Wincalc iwin = null;
    protected String versionDb = eSetting.find(2).getStr(eSetting.val);
    protected int pass = 1; //проверка на попадание либо pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры
    public HashMap<Integer, String> mapParamTmp = new HashMap();
    public Record detailRec = null; //для тестирования

    public Par5s(Wincalc iwin) {
        this.iwin = iwin;
    }

    public boolean compareBetween(String ptext, float value) {
        if (ptext == null) {
            return true;
        }
        ptext = ptext.replace(",", "."); //парсинг параметра
        char symmetry = ptext.charAt(ptext.length() - 1);
        if (symmetry == '@') {
            ptext = ptext.substring(0, ptext.length() - 1);
        }
        String[] arr = ptext.split(";");
        for (String str : Arrays.asList(arr)) {

            String[] p = str.split("-");
            if (p.length == 1) {
                Float valueOne = Float.valueOf(p[0]);
                if (value <= valueOne) {
                    return true;
                }

            } else if (p.length == 2) {
                Float valueMin = Float.valueOf(p[0]);
                Float valueMax = Float.valueOf(p[1]);
                if (valueMin <= value && valueMax >= value) {
                    return true;
                }
            }
        }
        return false;
    }

    //Фильтр параметров по умолчанию + выбранных клиентом
    protected boolean filterParamDef(List<Record> paramList) {

        for (Record paramRec : paramList) {
            if (paramRec.getInt(GRUP) < 0) {
                Record rec = iwin.mapParamDef.get(paramRec.getInt(GRUP));
                if (rec == null) {
                    return false; //если группы нет
                }
                if (rec.getInt(GRUP) != paramRec.getInt(GRUP)) {
                    return false; //если группа есть, а параметр не совпал
                }
            }
        }
        return true;
    }

    //Необработанные параметры
    protected void message(int code) {
        if (code >= 0) {
            System.err.println("ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
        }
    }

    //Необработанные параметры
    protected void message(HashMap<Integer, String> mapParam, int code) {
        if (code >= 0) {
            System.err.println("ОШИБКА! КОД " + code + " VALUE " + mapParam.get(code) + " НЕ ОБРАБОТАНЫ.");
        }
    }

    //Необработанные параметры
    protected void message(Specification spc, int code) {
        if (code >= 0) {
            System.err.println("ОШИБКА! ID " + spc.id + " КОД " + code + " VALUE " + spc.getParam(code) + " НЕ ОБРАБОТАНЫ.");
        }
    }
}

/*
    //Проверяет, должен ли применяться заданный тариф мат-ценности для заданной текстуры
    protected boolean IsArtTariffAppliesForColor(Artsvst artsvstRec, Colslst colslstRec) {
        if (artsvstRec.clnum < 0) {    //этот тариф задан для группы текстур

            if ((-1 * colslstRec.cgrup) == artsvstRec.clnum) {
                return true;
            }
        } else {  //проверяем не только colorCode, а еще и colorNumber
            if (colslstRec.ccode == artsvstRec.clcod) {
                return true;

            } else if (colslstRec.cnumb == artsvstRec.clnum) {
                return true;
            }
        }
        return false;
    }

    //Все параметры БиМакс
    public static int[] paramSum = {
            11000, 11009, 11010, 11020, 11030, 11040, 11050, 11060, 11067, 11068, 11069, 11070, 11072, 11095, 12000, 12008, 12010, 12020, 12030, 12050, 12060,
            12063, 12065, 12068, 12069, 12070, 12072, 12075, 14000, 14030, 14040, 14050, 14060, 14065, 14068, 15000, 15005, 15011, 15013, 15027, 15030, 15040,
            15045, 15050, 15055, 15068, 15069, 24001, 24002, 24004, 24006, 24010, 24012, 24030, 24033, 24038, 24063, 24067, 24068, 24069, 24070, 24072, 24073, 24074, 24075, 24095,
            24099, 25002, 25010, 25013, 25030, 25035, 25040, 25060, 25067, 33000, 33005, 33008, 33030, 33040, 33050, 33060, 33066, 33067, 33069, 33078, 33095, 34000, 34004, 34005, 34006,
            34007, 34008, 34010, 34011, 34015, 34030, 34051, 34060, 34066, 34067, 34068, 34069, 34070, 34071, 34075, 34095, 34099, 38004, 38010, 38030, 38050,
            38060, 38067, 38068, 38069, 39002, 39005, 39020, 39060, 39068, 39069, 39075, 39077, 39093, 40010, 40067, 40068, 40069};
    //Соединения
    public static int[] parCons = {11000, 11009, 11010, 11020, 11030, 11040, 11050, 11060, 11067, 11068, 11069, 11070, 11072, 11095,
            12000, 12008, 12010, 12020, 12030, 12050, 12060, 12063, 12065, 12068, 12069, 12070, 12072, 12075};
    //Составы
    public static int[] parVsts = {33000, 33005, 33008, 33030, 33040, 33050, 33060, 33066, 33067, 33069, 33078, 33095, 34000, 34004, 34005,
            34006, 34007, 34008, 34010, 34011, 34015, 34030, 34051, 34060, 34066, 34067, 34068, 34069, 34070, 34071, 34075, 34095, 34099,
            38004, 38010, 38030, 38050, 38060, 38067, 38068, 38069, 39002, 39005, 39020, 39060, 39068, 39069, 39075, 39077, 39093,
            40010, 40067, 40068, 40069};
    //Заполнения
    public static int[] parGlas = {14000, 14030, 14040, 14050, 14060, 14065, 14068, 15000, 15005, 15011, 15013, 15027, 15030, 15040,
            15045, 15050, 15055, 15068, 15069};
    //фурнитура
    public static int[] parFurs = {24001, 24002, 25002, 24004, 24006, 24010, 25010, 24012, 24030, 25030, 24033, 24038, 24063, 24067, 25067, 24068, 24069, 24070, 24072, 24073, 24074, 24075,
            24095, 24099, 25013, 25035, 25040, 25060, 25067};
 */
