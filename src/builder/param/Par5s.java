package builder.param;

import dataset.Record;
import domain.eSetting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import startup.Main;
import builder.Wincalc;
import builder.calc.SpecificRec;
import builder.model.Com5t;

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
                if (paramRec.getStr(TEXT).equals(rec.getStr(TEXT)) == false) {
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
    protected void message(SpecificRec spc, int code) {
        if (code >= 0) {
            System.err.println("ОШИБКА! ID " + spc.id + " КОД " + code + " VALUE " + spc.getParam(code) + " НЕ ОБРАБОТАНЫ.");
        }
    }
}
