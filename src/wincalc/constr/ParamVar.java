package wincalc.constr;

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

/**
 * Перечень параметров конструктива (составов, заполнений...) Параметры верхней
 * части формы конструктива (составов, заполнений...)
 */
public class ParamVar {

    public final int PAR1 = 3;   //Ключ 1  
    public final int PAR2 = 4;   //Ключ 2   
    public final int PAR3 = 5;   //Значение      
    private HashMap<Integer, String> hmParam = null;
    protected Wincalc iwin = null;
    private Constructiv calcConstr = null;

    public ParamVar(Wincalc iwin, Constructiv calcConstr) {
        this.iwin = iwin;
        this.calcConstr = calcConstr;
    }

    private boolean filterParamDef(Record paramRec) {
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

    // Соединения
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

    // Составы
    //int[] parVstm = {31000, 31001, 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31015, 31016, 31020, 31033, 31034, 31037, 31041, 31050, 31052, 31055, 31056, 31080, 31085, 31090, 31095, 31097, 31099, 37001, 37002, 37009, 37010, 37030, 37042, 37056, 37080, 37085, 37099};
    protected boolean element(ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам состава
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) {
                return false;
            }
            switch (paramRec.getInt(PAR1)) {
                case 31000:  //Для технологического кода контейнера 
                    Record sysprofRec2 = elemSimple.sysprofRec;
                    Record artiklVRec = eArtikl.find(sysprofRec2.getInt(eSysprof.artikl_id), false);
                    if (artiklVRec.get(eArtikl.tech_code) == null) {
                        return false;
                    }
                    String[] strList = paramRec.getStr(PAR3).split(";");
                    String[] strList2 = artiklVRec.getStr(PAR3).split(";");
                    boolean ret2 = false;
                    for (String str : strList) {
                        for (String str2 : strList2) {
                            if (str.equals(str2)) {
                                ret2 = true;
                            }
                        }
                    }
                    if (ret2 == false) {
                        return false;
                    }
                    break;
                case 31001:  //Максимальное заполнение изделия, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31008:  //Эффективное заполнение изделия, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31002:  //Если профиль 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31003:  //Если соединенный артикул  T-обр.
                    message(paramRec.getInt(PAR1));
                    break;
                case 31004:  //Если прилегающий артикул 
                    HashMap<String, ElemJoining> mapJoin = elemSimple.iwin().mapJoin;
                    boolean ret = false;
                    for (Map.Entry<String, ElemJoining> elemJoin : mapJoin.entrySet()) {
                        ElemJoining el = elemJoin.getValue();
                        if (TypeJoin.VAR4 == el.varJoin
                                && el.joinElement1.artiklRec.equals(elemSimple.artiklRec)
                                && el.joinElement2.artiklRec.equals(paramRec.getStr(PAR3))) {
                            ret = true;
                        }
                    }
                    if (ret == false) {
                        return false;
                    }
                    break;
                case 31005:  //Коды основной текстуры контейнера 
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color1) == false) {
                        return false;
                    }
                    break;
                case 31006:  //Коды внутр. текстуры контейнера 
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color2) == false) {
                        return false;
                    }
                    break;
                case 31007:  //Коды внешн. текстуры контейнера 
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color3) == false) {
                        return false;
                    }
                    break;
                case 31011:  //Толщина внешнего/внутреннего заполнения, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31014:  //Заполнения одинаковой толщины 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31015:  //Разбиение профиля по уровням 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31016:  //Зазор_на_метр,_мм/Размер_,мм терморазрыва 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31019:  //Правило подбора текстур 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31020:  //Ограничение угла к горизонту, ° 
                    if (Constructiv.compareFloat(paramRec.getStr(PAR3), ((ElemSimple) elemSimple).anglHoriz) == false) {
                        return false;
                    }
                    break;
                case 31033:  //Если предыдущий артикул 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31034:  //Если следующий артикул 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31035:  //Уровень створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31037:  //Название фурнитуры содержит 
                    if (TypeElem.FULLSTVORKA == elemSimple.owner().type()) {
                        if (paramRec.getStr(PAR3).contains(elemSimple.artiklRec.getStr(eArtikl.name)) == false) {
                            return false;
                        }
                    }
                    break;
                case 31040:  //Поправка габарита накладки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31041:  //Ограничение длины профиля, мм 
                    if (Constructiv.compareFloat(paramRec.getStr(PAR3), elemSimple.width()) == false) {
                        return false;
                    }
                    break;
                case 31050:  //Контейнер имеет тип 
                    TypeElem type = elemSimple.type();
                    if (type.value != Integer.valueOf(paramRec.getStr(PAR3))) {
                        return false;
                    }
                    break;
                case 31051:  //Если створка фурнитуры 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31052:  //Поправка в спецификацию, мм 
                    if (elemSimple.layout() == LayoutArea.ARCH) {
                        elemSimple.specificationRec.putParam(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    }
                    break;
                case 31054:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31055:  //Коды внутр. и внешн. текстуры изд. 
                    if ((Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color2) == true
                            && Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color3) == true) == false) {
                        return false;
                    }
                    break;
                case 31056:  //Коды внутр. или внеш. текстуры изд. 
                case 37056:  //Коды внут. или внеш. текстуры изд. 
                    if ((Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color2) == true
                            || Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color3) == true) == false) {
                        return false;
                    }
                    break;
                case 31057:  //Внутренняя текстура равна внешней 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31060:  //Допустимый угол между плоскостями, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31073:  //Отправочная марка фасада 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31074:  //На прилегающей створке 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31081:  //Для внешнего/внутреннего угла плоскости, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31090:  //Изменение сторон покраски 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31080:  //Сообщение-предупреждение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31085:  //Надпись на элементе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31800:  //Код обработки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 31801:  //Доп.обработки
                    message(paramRec.getInt(PAR1));
                    break;
                case 37001:  //Установка жалюзи 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37002:  //Если артикул профиля контура 
                    if (LayoutArea.ARCH == elemSimple.layout() && "арочный".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    } else if (LayoutArea.ARCH != elemSimple.layout() && "прямой".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 37005:  //Коды основной текстуры контейнера 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37006:  //Коды внутр. текстуры контейнера 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37007:  //Коды внешн. текстуры контейнера 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37008:  //Тип проема 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37009:  //Тип заполнения 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37010:  //Ограничение ширины/высоты листа, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37030:  //Ограничение площади, кв.м. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37042:  //Допустимое соотношение габаритов б/м
                    message(paramRec.getInt(PAR1));
                    break;
                case 37054:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37055:  //Коды внутр. и внешн. текстуры изд. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37080:  //Сообщение-предупреждение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37085:  //Надпись на элементе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37098:  //Бригада участок
                    message(paramRec.getInt(PAR1));
                    break;
                case 37097:  //Трудозатраты по 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37108:  //Коэффициенты АКЦИИ 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37310:  //Сопротивление теплопередаче, м2*°С/Вт 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37320:  //Воздухопроницаемость, м3/ ч*м2
                    message(paramRec.getInt(PAR1));
                    break;
                case 37330:  //Звукоизоляция, дБА 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37340:  //Коэффициент пропускания света 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37350:  //Сопротивление ветровым нагрузкам, Па 
                    message(paramRec.getInt(PAR1));
                    break;
                case 37351:  //Номер поверхности 
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return true;
    }

    // Заполнения
    //int[] parGrup = {13015, 13017, 13081, 13099};    
    protected boolean filling(ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам заполнения
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) {
                return false;
            }
            switch (paramRec.getInt(PAR1)) {
                case 13001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13003:  //Тип проема 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13005:  //Заполнение типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13014:  //Угол ориентации стороны, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13015:  //Форма заполнения 
                    if (paramRec.getStr(PAR3).equals(elemSimple.specificationRec.getParam("empty", 13015)) == false) { //нужно проверить
                        return false;
                    }
                    break;
                case 13017:  //Код системы содержит строку 
                    Record sysprofRec = eSystree.find(iwin.nuni);
                    if (sysprofRec.getStr(eSystree.pref).contains(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 13081:  //Для внешнего/внутреннего угла плоскости, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 13097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return true;
    }

    // Фурнитура
    //int[] parFurl = {2101, 2104, 2140, 2185};
    protected boolean furniture(ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) {
                return false;
            }
            switch (paramRec.getInt(PAR1)) {
                case 21001:  //Форма контура 
                    if (TypeElem.FULLSTVORKA == elemSimple.type() && "прямоугольная".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 21004:  //Артикул створки 
                    if (elemSimple.artiklRec.getStr(eArtikl.code).equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 21005:  //Артикул заполнения по умолчанию 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21010:  //Ограничение длины стороны, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21011:  //Ограничение длины ручка константа, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21012:  //Ограничение длины ручка вариацион, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21013:  //Ограничение длины ручка по середине, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21016:  //Допустимое соотношение габаритов б/м) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21037:  //Диапазон высоты вариационной ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21040:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21050:  //Ориентация стороны, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21085:  //Надпись на эскизе 
                    message(paramRec.getInt(PAR1));
                    break;
                case 21088:  //Уравнивание складных створок 
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return true;
    }

    private void message(int code) {
        System.err.println("Parametr ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
    }
}
