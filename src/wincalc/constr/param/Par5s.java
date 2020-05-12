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

    public final int PAR1 = 2;   //Ключ 1  
    public final int PAR2 = 3;   //Ключ 2   
    public final int PAR3 = 4;   //Значение      
    protected Wincalc iwin = null;
    protected Constructiv calcConstr = null;
    protected int pass = 1; //проверка на попадание либо pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры
    protected String sideCheck = ""; //TODO Эту переменную надо вынести в map параметров!!!
   
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
    
    //Не обработанные параметры
    protected void message(int code) {
        System.err.println("ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
    }    
}
/*
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

