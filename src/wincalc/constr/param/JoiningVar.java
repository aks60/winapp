package wincalc.constr.param;

import wincalc.constr.*;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeJoin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import static wincalc.constr.ParamVariant.PAR1;
import static wincalc.constr.ParamVariant.PAR2;
import wincalc.model.ElemJoining;
import wincalc.model.ElemSimple;

//Соединения
public class JoiningVar extends Par5s {

    //Соединения
    public JoiningVar(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }

    //1000 - прилегающее соединение, 2000 - угловое на ус, 3000 - угловое (левое, правое), 4000 - Т образное соединение
    //int[] parConv = {1005, 1008, 1010, 1011, 1012, 1013, 1020, 1040, 1085, 1099, 2005, 2012, 2013, 2020, 2030, 2061, 2099, 3002, 3003, 3005, 3015, 3020, 3031, 3050, 3099, 4002, 4005, 4011, 4012, 4013, 4015, 4018, 4020, 4040, 4044, 4085, 4095, 4099};
    protected boolean joining(ElemJoining elemJoin, List<Record> parconvList) {

        float angl = (ElemSimple.SIDE_START == ElemJoining.FIRST_SIDE) ? elemJoin.joinAngl(1) : elemJoin.joinAngl(2);
        ElemSimple joinElement1 = elemJoin.joinElement1;
        ElemSimple joinElement2 = elemJoin.joinElement2;
        boolean result = true;
        String strTxt = "";
        //цикл по параметрам элементов соединения
        for (Record paramRec : parconvList) {

            switch (paramRec.getInt(PAR1)) {
                case 0:  //Зрачение по умолчанию 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1005:  //Контейнер имеет тип Артикула1/Артикула2
                    message(paramRec.getInt(PAR1));
                    break;
                case 1008:  //Эффективное заполнение изд., мм
                    message(paramRec.getInt(PAR1));
                    break;
                case 1010:  //Внешнее соединение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1011:  //Для Артикула 1 указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1012:  //Для Артикула 2 указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1013:  //Для Артикулов не указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1020:  //Ограничение угла к горизонту, °
                    message(paramRec.getInt(PAR1));
                    break;
                case 1035:  //Уровень створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1039:  //Для типа открывания 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1040:  //Размер, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1043:  //Ограничение габарита контура, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1090:  //Смещение по толщине, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
                    break;
                case 1085:  //Надпись на элементе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2003:  //Угол варианта 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2005:  //Контейнер имеет тип Артикула1/Артикула2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2012:  //Для Артикулов указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2013:  //Для Артикулов не указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2015:  //Ориентация Артикула1/Артикула2, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2020:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2030:  //Припуск Артикула1/Артикула2 , мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2055:  //Продолжение общей арки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2061:  //Отступ для Артикула1/Артикула2, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2066:  //Расчет углов реза профилей 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2064:  //Поправка для состава Арт.1/Арт.2, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
                    break;
                case 2085:  //Надпись на элементе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3002:  //Вид L-образного варианта 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3003:  //Угол варианта 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3005:  //Контейнер имеет тип Артикула1/Артикула2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3012:  //Для Артикулов указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3013:  //Для Артикулов не указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3015:  //Ориентация Артикула1/Артикула2, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3020:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3030:  //Усечение Артикула1/Артикула2, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3031:  //Усечение Артикула1/Артикула2, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3045:  //Расстояние от уровня деления, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3050:  //Припуск Артикула1/Артикула2, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3064:  //Поправка для состава Арт.1/Арт.2 , мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3083:  //Проходит уровень деления 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3088:  //Вариант соединения для стойки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
                    break;
                case 3085:  //Надпись на элементе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4002:  //Вид Т-образного варианта (простое Т-обр. крестовое Т-обр. сложное Y-обр.) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4005:  //Контейнер имеет тип Артикула1/Артикула2  
                    message(paramRec.getInt(PAR1));
                    break;
                case 4010:  //Внешнее соединение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4011:  //Для Артикула 1 указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4012:  //Для Артикула 2 указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4013:  //Для Артикулов не указан состав 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4015:  //Ориентация Артикула1/Артикула2, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4018:  //От ручки не менее, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4020:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4040:  //Размер от оси профиля, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4044:  //Размер от края пакета, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4045:  //Расстояние от уровня деления, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4046:  //Длина Артикула 1, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4050:  //Припуск Артикула 1, мм. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4061:  //Максимальный размер шва, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4064:  //Поправка для состава, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4083:  //Проходит уровень деления 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4085:  //Надпись на элементе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4099:  //Трудозатраты, ч/ч.                 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4800:  //Код обработки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 4801:  //Доп.обработки
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return result;
    }
}
