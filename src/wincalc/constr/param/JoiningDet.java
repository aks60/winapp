package wincalc.constr.param;

import wincalc.constr.*;
import dataset.Record;
import enums.ParamJson;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import static wincalc.constr.ParamSpec.PAR1;
import static wincalc.constr.ParamSpec.PAR2;
import wincalc.model.Com5t;
import wincalc.model.ElemSimple;

//Cоединения
public class JoiningDet extends Par5s {

    public JoiningDet(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }

    protected boolean check(HashMap<Integer, String> hmParam, ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам соединения
        for (Record paramRec : tableList) {

            switch (paramRec.getInt(PAR1)) {
                case 11000:  //Для технологического кода контейнера 1/2
                    message(paramRec.getInt(PAR1));
                    break;
                case 11001:  //Если признак состава Арт.1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11002:  //Если признак состава Арт.2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11005:  //Контейнер типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11009:  //Внешнее соединение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11010:  //Рассчитывать с Артикулом 1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11020:  //Рассчитывать с Артикулом 2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11028:  //Диапозон веса заполнения, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11029:  //Расстояние узла от ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11040:  //Порог расчета, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11066:  //Если текстура профиля Арт.1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11070:  //Ставить однократно 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11072:  //Расчет по стороне 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12000:  //Для технологического кода контейнера 1/2
                    message(paramRec.getInt(PAR1));
                    break;
                case 12001:  //Если признак состава Арт.1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12002:  //Если признак состава Арт.2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12005:  //Контейнер типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12009:  //Внешнее соединение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12010:  //Рассчитывать с Артикулом 1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12020:  //Рассчитывать с Артикулом 2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12027:  //Рассчитывать для профилей 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12028:  //Диапозон веса заполнения, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12030:  //[ * коэф-т ] 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12050:  //Поправка, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12060:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12063:  //Углы реза по плоскости ригеля 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12064:  //Учёт в длине углов плоскостей 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12065:  //Длина, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12070:  //Ставить однократно 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12072:  //Расчет по стороне 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12075:  //Углы реза 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12095:  //Если признак системы конструкции 
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
