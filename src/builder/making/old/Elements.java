package builder.making.old;

import builder.making.*;
import builder.ICom5t;
import dataset.Record;
import domain.eArtikl;
import domain.eElemdet;
import domain.eElement;
import enums.TypeArtikl;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import builder.Wincalc;
import builder.param.ElementDet;
import builder.param.ElementVar;
import builder.IElem5e;
import builder.model.UPar;
import common.UCom;
import dataset.Query;
import domain.eSysprof;
import enums.Type;

/**
 * Составы.
 */
public class Elements extends Cal5e {

    private ElementVar elementVar = null;
    private ElementDet elementDet = null;

    public Elements(Wincalc winc) {
        super(winc);
        elementVar = new ElementVar(winc);
        elementDet = new ElementDet(winc);
    }

    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //TODO При проверке параметров использую оригин. мат. ценность. (Непонятно!!!)
    @Override
    public void calc() {
        super.calc();
        LinkedList<IElem5e> listElem = winc.listElem.filter(Type.FRAME_SIDE,
                Type.STVORKA_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA, Type.GLASS, Type.MOSKITKA); //список элементов конструкции
        try {
            //Цикл по списку элементов конструкции
            for (IElem5e elem5e : listElem) {

                if (elem5e.type() == Type.MOSKITKA) {
                    //По id - профиля
                    List<Record> elementList4 = List.of(eElement.find4(((ICom5t) elem5e).sysprofRec().getInt(eSysprof.id)));
                    for (int side : List.of(0, 90, 180, 270)) {
                        elem5e.anglHoriz(side); //устан. угол. проверяемой стороны
                        detail(elementList4, elem5e);
                    }
                } else {
                    //По artikl_id - артикула профилей
                    List<Record> elementList3 = eElement.find2(elem5e.artiklRecAn().getInt(eArtikl.id));
                    detail(elementList3, elem5e);

                    //По groups1_id - серии профилей
                    int series_id = elem5e.artiklRecAn().getInt(eArtikl.groups4_id);
                    List<Record> elementList2 = eElement.find(series_id); //список элементов в серии
                    detail(elementList2, elem5e);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Elements.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    protected void detail(List<Record> elementList, IElem5e elem5e) {
        try {
            //Цикл по вариантам
            for (Record elementRec : elementList) {

                //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
                if (elementVar.filter(elem5e, elementRec) == true) {

                    //Выполним установочные параметры (не успользую)
                    elementVar.listenerFire();

                    setVariant.add(elementRec.getInt(eElement.id)); //сделано для фильтрации профилей вне конструктива

                    UColor.colorFromParam(elem5e); //правило подбора текстур по параметру
                    List<Record> elemdetList = eElemdet.find(elementRec.getInt(eElement.id)); //список элем. детализации

                    //Цикл по детализации
                    for (Record elemdetRec : elemdetList) {
                        HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры детализации

                        //ФИЛЬТР детализации, параметры накапливаются в mapParam
                        if (elementDet.filter(mapParam, elem5e, elemdetRec) == true) {

                            Record artiklRec = eArtikl.get(elemdetRec.getInt(eElemdet.artikl_id));
                            Specific spcAdd = new Specific(elemdetRec, artiklRec, elem5e, mapParam);

                            if (UColor.colorFromProduct(spcAdd, 1)
                                    && UColor.colorFromProduct(spcAdd, 2)
                                    && UColor.colorFromProduct(spcAdd, 3)) {

                                spcAdd.place = "ВСТ";

                                //Если (контейнер) в списке детализации, 
                                //например профиль с префиксом @ в осн. специф.
                                if (TypeArtikl.isType(artiklRec, TypeArtikl.X101, TypeArtikl.X102,
                                        TypeArtikl.X103, TypeArtikl.X104, TypeArtikl.X105, TypeArtikl.X520)) {
                                    elem5e.spcRec().setArtikl(spcAdd.artiklRec); //подмена артикула в основной спецификации
                                    UPar.to_40005_40010(spcAdd); //Поправка на стороны четные/нечетные, мм 

                                } else {
                                    elem5e.addSpecific(spcAdd); //в спецификацию
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Elements.detail() " + e);
        }
    }
}