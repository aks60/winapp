package wincalc.constr.param;

import dataset.Record;
import enums.ParamJson;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import wincalc.constr.Constructiv;
import wincalc.model.Com5t;

public class Par5s {

    public final int PAR1 = 3;   //Ключ 1  
    public final int PAR2 = 4;   //Ключ 2   
    public final int PAR3 = 5;   //Значение      
    private HashMap<Integer, String> hmParam = null;
    protected Wincalc iwin = null;
    private Constructiv calcConstr = null;
    public int pass = 1; //pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры
    public String sideCheck = ""; //TODO Эту переменную надо вынести в map параметров!!!

    
    public Par5s(Wincalc iwin, Constructiv calcConstr) {
        this.iwin = iwin;
        this.calcConstr = calcConstr;
    }

    //Фильтр параметров
    protected boolean filterParamDef(Record paramRec) {
        if (paramRec.getInt(PAR1) < 0) {
            if (iwin.mapParamDef.get(paramRec.getInt(PAR1)) == null) {
                return false;
            }
            int id1 = Integer.valueOf(iwin.mapParamDef.get(paramRec.getInt(PAR1))[1].toString());
            int id2 = paramRec.getInt(PAR2);
            if ((id1 == id2) == false) {
                return false;
            }
        }
        return true;
    }

    //Фильтр параметров
    protected boolean filterParamJson(Com5t com5t, List<Record> paramList) {

        HashMap<Integer, Object[]> paramJson = new HashMap();
        HashMap<Integer, Object[]> paramTotal = new HashMap();
        paramTotal.putAll(iwin.mapParamDef); //добавим параметры по умолчанию

        //Все владельцы этого элемента
        LinkedList<Com5t> ownerList = new LinkedList();
        Com5t el = com5t;
        ownerList.add(el);
        do {
            el = el.owner();
            ownerList.add(el);
        } while (el != iwin.rootArea);

        //Цикл по владельцам этого элемента
        for (int index = ownerList.size() - 1; index >= 0; index--) {

            el = ownerList.get(index);
            HashMap<Integer, Object[]> pJson = (HashMap) el.mapParam.get(ParamJson.pro4Params2);
            if (pJson != null && pJson.isEmpty() == false) {  // если параметры от i-okna есть
                if (pass == 1) {
                    paramTotal.putAll(pJson); //к пар. по умолч. наложим парам. от i-win
                } else {
                    for (Map.Entry<Integer, Object[]> entry : pJson.entrySet()) {
                        Object[] val = entry.getValue();
                        if (val[2].equals(1)) { //
                            paramTotal.put(entry.getKey(), entry.getValue()); //по умолчанию и i-win
                        }
                    }
                }
                paramJson.putAll(pJson); //к парам. i-win верхнего уровня наложим парам. i-win нижнего уровня
            }
        }
        for (Record paramRec : paramList) {
            if (paramRec.getInt(PAR1) < 0) {

                if (paramTotal.get(paramRec.getInt(PAR1)) == null) {
                    return false; //усли в базе парам. нет, сразу выход
                }
                //В данной ветке есть попадание в paramRec.getInt(PAR1)
                Object[] totalVal = paramTotal.get(paramRec.getInt(PAR1));
                if (totalVal[1].equals(paramRec.getInt(PAR2)) == false) { //если в param.znumb() попадания нет

                    //на третьей итерации дополняю ...
                    return false;

                } else if (paramJson != null && paramJson.isEmpty() == false && paramJson.get(paramRec.getInt(PAR1)) != null) {
                    totalVal[2] = 1; //если попадание было, то записываю 1 в третий элемент массива
                }
            }
        }
        return true;
    }
    
    protected void message(int code) {
        System.err.println("Parametr ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
    }    
}
