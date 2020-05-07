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
import static wincalc.constr.ParamSpecific.PAR1;
import static wincalc.constr.ParamSpecific.PAR3;
import wincalc.model.Com5t;
import wincalc.model.ElemSimple;

//Фурнитура
public class FurnitureDet {

    protected Wincalc iwin = null;
    public String sideCheck = ""; //TODO Эту переменную надо вынести в map параметров!!!
    public static final int PAR1 = 3;   //Ключ 1  
    public static final int PAR2 = 4;   //Ключ 2   
    public static final int PAR3 = 5;   //Значение 
    protected Constructiv calcConstr = null;

    public int pass = 1; //pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры

    public FurnitureDet(Wincalc iwin, Constructiv calcConstr) {
        this.iwin = iwin;
        this.calcConstr = calcConstr;
    }

    //Фильтр параметров по умолчанию и i-okna
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
    
    protected boolean furniture(HashMap<Integer, String> hmParam, ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record paramRec : tableList) {
            switch (paramRec.getInt(PAR1)) {

                case 24001:  //Форма контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24002:  //Если артикул створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24003:  //Если артикул цоколя 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24004:  //Если створка прилегает к артикулу 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24005:  //Коды текстуры створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24006:  //Установить текстуру 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24007:  //Коды текстуры ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24009:  //Коды текстуры подвеса 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24008:  //Если серия створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24010:  //Номер стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24011:  //Расчет по общей арке 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24012:  //Направление открывания 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24013:  //Выбран авто расчет подвеса 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24032:  //Правильная полуарка 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24033:  //Фурнитура штульповая 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24036:  //Номер Стороны_X/Стороны_Y набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24037:  //Номер стороны по параметру набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24038:  //Проверять Cторону_L)/Cторону_W) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24039:  //Створка заднего плана 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24040:  //Порог расчета, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24063:  //Диапазон веса, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24064:  //Ограничение высоты ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24070:  //Если высота ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24072:  //Ручка от низа створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24073:  //Петля от низа створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24074:  //Петля по центру стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24075:  //Петля от верха створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24077:  //Смещение замка от ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24078:  //Замок от края профиля, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24800:  //Код основной обработки) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24801:  //Доп.основная обработка
                    message(paramRec.getInt(PAR1));
                    break;
                case 24802:  //Код симметр. обработки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24803:  //Доп.симметр. обработка
                    message(paramRec.getInt(PAR1));
                    break;
                case 25001:  //Форма контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25002:  //Если артикул створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25003:  //Если артикул цоколя 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25005:  //Коды текстуры створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25007:  //Коды текстуры ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25009:  //Коды текстуры подвеса 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25008:  //Если серия створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25010:  //Номер стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25011:  //Расчет по общей арке 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25012:  //Направление открывания 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25013:  //Укорочение от 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25030:  //Укорочение, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25032:  //Правильная полуарка 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25033:  //Фурнитура штульповая 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25035:  //[ * коэф-т ] 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25036:  //Номер Стороны_ X/Стороны_ Y набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25037:  //Номер стороны по параметру набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25038:  //Проверять Cторону_ L/Cторону_ W 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25040:  //Длина, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25060:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25063:  //Диапазон веса, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25064:  //Ограничение высоты ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25070:  //Если высота ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25072:  //Ручка от низа створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25800:  //Код основной обработки
                    message(paramRec.getInt(PAR1));
                    break;
                case 25801:  //Доп.основная обработка
                    message(paramRec.getInt(PAR1));
                    break;
                case 25802:  //Код симметр. обработки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25803:  //Доп.симметр. обработка
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        if (filterParamJson(elemSimple, tableList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }
        return true;
    }

    private void message(int code) {
        System.err.println("Parametr ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
    }
}
