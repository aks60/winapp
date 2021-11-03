package builder.param;

import dataset.Record;
import domain.eSetting;
import java.util.HashMap;
import java.util.List;
import builder.Wincalc;
import builder.making.Specific;
import builder.model.AreaStvorka;
import builder.model.ElemJoining;
import builder.model.ElemSimple;

public class Par5s {

    protected final int ID = 1;   //Ключ в таблице  
    protected final int GRUP = 3;   //Ключ параметра    
    protected final int TEXT = 2;   //Текст 
    protected Wincalc iwin = null;
    protected String versionDb = eSetting.find(2);
    public HashMap<Integer, String> mapParamTmp = new HashMap();
    public Record detailRec = null; //текущий элемент детализации

    public Par5s(Wincalc iwin) {
        this.iwin = iwin;
    }
    
    //Фильтр параметров по умолчанию + выбранных клиентом
    protected boolean filterParamDef(List<Record> paramList) {

        for (Record paramRec : paramList) {
            if (paramRec.getInt(GRUP) < 0) {
                Record rec = iwin.mapPardef.get(paramRec.getInt(GRUP));
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
//        if (code >= 0) {
//            //if (ParamList.find(code).pass() != 0) {
//            String str = ParamList.find(code).text();
//            System.err.println("Не обработан:  " + code + "-" + str);
//            //}
//        }
    }

    //Необработанные параметры
    protected void message(HashMap<Integer, String> mapParam, int code) {
        if (code >= 0) {
            if (ParamList.find(code).pass() != 0) {
                System.err.println("ВНИМАНИЕ! ПАРААМЕТР " + code + " VALUE " + mapParam.get(code) + " В РАЗРАБОТКЕ.");
            }
        }
    }

    //Необработанные параметры
    protected void message(Specific spc, int code) {
        if (code >= 0) {
            if (ParamList.find(code).pass() != 0) {
                System.err.println("ВНИМАНИЕ! ID " + spc.id + " ПАРААМЕТР " + code + " VALUE " + spc.getParam("-1", code) + " В РАЗРАБОТКЕ.");
            }
        }
    }
}
