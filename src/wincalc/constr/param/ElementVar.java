package wincalc.constr.param;

import wincalc.constr.*;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeGlass;
import enums.TypeJoin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import wincalc.model.ElemGlass;
import wincalc.model.ElemJoining;
import wincalc.model.ElemSimple;

//Составы
public class ElementVar extends Par5s {

    private int[] par = {31000, 31001, 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31015, 31016, 31020, 31033, 31034, 31037, 31041, 31050, 31052, 31055, 31056, 31080, 31085, 31090, 31095, 31097, 31099, 37001, 37002, 37009, 37010, 37030, 37042, 37056, 37080, 37085, 37099};

    public ElementVar(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }

    public boolean check(ElemSimple elemSimple, List<Record> paramList) {

        //Цикл по параметрам состава
        for (Record paramRec : paramList) {
            if (filterParamDef(paramRec) == false) {
                return false;
            }
            int parametr = paramRec.getInt(PAR1);
            try {
                switch (parametr) {

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
                    case 37002:  //Если артикул профиля контура    
                        if (LayoutArea.ARCH == elemSimple.layout() && "арочный".equals(paramRec.getStr(PAR3)) == false) {
                            return false;
                        } else if (LayoutArea.ARCH != elemSimple.layout() && "прямой".equals(paramRec.getStr(PAR3)) == false) {
                            return false;
                        }
                        break;
                    case 31003:  //Если соединенный артикул  T-обр.
                        message(paramRec.getInt(PAR1));
                        break;
                    case 31004:  //Если прилегающий артикул 
                        HashMap<String, ElemJoining> mapJoin = elemSimple.iwin().mapJoin;
                        pass = 0;
                        for (Map.Entry<String, ElemJoining> elemJoin : mapJoin.entrySet()) {
                            ElemJoining el = elemJoin.getValue();
                            if (TypeJoin.VAR4 == el.varJoin
                                    && el.joinElement1.artiklRec.equals(elemSimple.artiklRec)
                                    && el.joinElement2.artiklRec.equals(paramRec.getStr(PAR3))) {
                                pass = 1;
                            }
                        }
                        if (pass == 0) {
                            return false;
                        }
                        break;
                    case 31005:  //Коды основной текстуры контейнера 
                    case 37005:  //Коды основной текстуры контейнера    
                        if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color1) == false) {
                            return false;
                        }
                        break;
                    case 31006:  //Коды внутр. текстуры контейнера 
                    case 37006:  //Коды внутр. текстуры контейнера    
                        if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color2) == false) {
                            return false;
                        }
                        break;
                    case 31007:  //Коды внешн. текстуры контейнера 
                    case 37007:  //Коды внешн. текстуры контейнера    
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
                        if (paramRec.getStr(PAR3).equals(elemSimple.specificationRec.getParam("empty", 13015)) == false) {
                            return false;
                        }
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
                    case 37055:  //Коды внутр. и внешн. текстуры изд.    
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
                    case 31080:  //Сообщение-предупреждение 
                        message(paramRec.getInt(PAR1));
                        break;
                    case 31081:  //Для внешнего/внутреннего угла плоскости, ° 
                        message(paramRec.getInt(PAR1));
                        break;
                    case 31085:  //Надпись на элементе 
                    case 37085:  //Надпись на элементе     
                        elemSimple.specificationRec.putParam(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                        break;
                    case 31090:  //Изменение сторон покраски 
                        if (paramRec.getStr(PAR3).equals(elemSimple.specificationRec.getParam("empty", 31090)) == false) {
                            return false;
                        }
                        break;
                    case 31095:  //Если признак системы конструкции 
                        message(paramRec.getInt(PAR1));
                        break;
                    case 31098:  //Бригада, участок) 
                        message(paramRec.getInt(PAR1));
                        break;
                    case 31099:  //Трудозатраты, ч/ч. 
                    case 37099:  //Трудозатраты, ч/ч.    
                        elemSimple.specificationRec.putParam(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                        break;
                    case 31097:  //Трудозатраты по длине 
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
                    case 37008:  //Тип проема 
                        message(paramRec.getInt(PAR1));
                        break;
                    case 37009:  //Тип заполнения 
                        //Все, Произвольное, Прямоугольное, Арочное                                            
                        message(paramRec.getInt(PAR1), elemSimple.type(), ((ElemGlass) elemSimple).typeGlass, paramRec.getStr(PAR3));
                        if ("Прямоугольное".equals(paramRec.getStr(PAR3)) && TypeGlass.RECTANGL.text().equals(((ElemGlass) elemSimple).typeGlass.text()) == false) {
                            return false;
                        } else if ("Арочное".equals(paramRec.getStr(PAR3)) && TypeGlass.ARCH.text().equals(((ElemGlass) elemSimple).typeGlass.text()) == false) {
                            return false;
                        } 
                        break;
                    case 37010:  //Ограничение ширины/высоты листа, мм 
                        //message(paramRec.getInt(PAR1), elemSimple.type(), paramRec.getStr(PAR3));
                        Float[] arr = calcConstr.parserFloat2(paramRec.getStr(PAR3));
                        if(arr[0] > elemSimple.width() && arr[1] < elemSimple.width()) {
                            
                        }
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
                    case 37080:  //Сообщение-предупреждение 
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
            } catch (Exception e) {
                System.out.println("wincalc.constr.param.ElementVar.check()  parametr=" + parametr + "    " + e);
                return false;
            }
        }
        return true;
    }
}
