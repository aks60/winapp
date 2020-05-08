package wincalc.constr.param;

import wincalc.constr.*;
import dataset.Record;
import domain.eArtikl;
import enums.LayoutArea;
import enums.TypeElem;
import enums.UseArtiklTo;
import java.util.HashMap;
import java.util.List;
import wincalc.Wincalc;
import static wincalc.constr.ParamSpecific.PAR1;
import static wincalc.constr.ParamSpecific.PAR3;
import wincalc.model.AreaSimple;
import wincalc.model.AreaStvorka;
import wincalc.model.ElemFrame;
import wincalc.model.ElemSimple;

//Фурнитура
public class FurnitureDet  extends Par5s {

    private int[] par = {24001, 24002, 25002, 24004, 24006, 24010, 25010, 24012, 24030, 25030, 24033, 24038, 24063, 24067, 25067, 24068, 24069, 24070, 24072, 24073, 24074, 24075, 24095, 24099, 25013, 25035, 25040, 25060, 25067};    
    
    public FurnitureDet(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }

    protected boolean furniture(HashMap<Integer, String> hmParam, ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record paramRec : tableList) {
            switch (paramRec.getInt(PAR1)) {

                case 24001:  //Форма контура 
                case 25001:  //Форма контура 
                    if (TypeElem.FULLSTVORKA == elemSimple.type() && "прямоугольная".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 24002:  //Если артикул створки 
                case 25002:  //Если артикул створки    
                    if (elemSimple.artiklRec.getStr(eArtikl.code).equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
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
                case 25012:  //Направление открывания     
                    if (((AreaStvorka) elemSimple.owner()).typeOpen().side.equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 24013:  //Выбран авто расчет подвеса 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24030:  //Количество 
                case 25060:  //Количество     
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 24032:  //Правильная полуарка 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24033:  //Фурнитура штульповая 
                case 25033:  //Фурнитура штульповая    
                    if (((AreaStvorka) elemSimple.owner()).typeOpen().side.equals("левое")) {
                        ElemFrame el = ((AreaSimple) elemSimple.owner()).mapFrame.get(LayoutArea.LEFT);
                        if (paramRec.getStr(PAR3).equals("Да") && el.useArtiklTo() != UseArtiklTo.SHTULP) {
                            return false;
                        } else if (paramRec.getStr(PAR3).equals("Нет") && el.useArtiklTo() == UseArtiklTo.SHTULP) {
                            return false;
                        }
                    } else if (((AreaStvorka) elemSimple.owner()).typeOpen().side.equals("правое")) {
                        ElemFrame el = ((AreaSimple) elemSimple.owner()).mapFrame.get(LayoutArea.RIGHT);
                        if (paramRec.getStr(PAR3).equals("Да") && el.useArtiklTo() != UseArtiklTo.SHTULP) {
                            return false;
                        }
                        if (el.useArtiklTo() == UseArtiklTo.SHTULP && paramRec.getStr(PAR3).equals("Нет")) {
                            return false;
                        }
                    }
                    break;
                case 24036:  //Номер Стороны_X/Стороны_Y набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24037:  //Номер стороны по параметру набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24038:  //Проверять Cторону_(L))/Cторону_(W) 
                case 25038:  //Проверять Cторону_(L)/Cторону_(W)     
                    //Тут полные непонятки
                    sideCheck = paramRec.getStr(PAR3);
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
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
                case 25069:  //Коды внешн. текстуры изделия     
                    int c3 = elemSimple.iwin().color3;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c3) == false) {
                        return false;
                    }
                    break;
                case 24070:  //Если высота ручки 
                case 25070:  //Если высота ручки     
                    String str = ((AreaStvorka) elemSimple.owner()).handleHeight;
                    if ("по середине".equals(str) == true && paramRec.getStr(PAR3).equals("не константная") == false
                            || "константная".equals(str) == true && paramRec.getStr(PAR3).equals("константная") == false) {
                        return false;
                    }
                    break;
                case 24072:  //Ручка от низа створки, мм 
                case 25072:  //Ручка от низа створки, мм     
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
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
                case 25013:  //Укорочение от 
                case 25030:  //Укорочение, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 25017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25032:  //Правильная полуарка 
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
                case 25040:  //Длина, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
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
}
