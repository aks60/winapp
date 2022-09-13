package builder.making;

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
import common.UCom;
import dataset.Query;
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
    //Но при проверке параметров использую оригин. мат. ценность. (Непонятно!!!)
    @Override
    public void calc() {
        super.calc();
        LinkedList<IElem5e> listElem = UCom.listSortObj(winc.listElem, Type.FRAME_SIDE,
                Type.STVORKA_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA, Type.GLASS); //список элементов конструкции
        try {
            //Цикл по списку элементов конструкции
            for (IElem5e elem5e : listElem) {

                //Варианты состава для артикула профиля
                int artikl_id = elem5e.artiklRecAn().getInt(eArtikl.id);

                //Варианты состава по артикулу элемента конструкции
                List<Record> elementList3 = eElement.find2(artikl_id);
                detail(elementList3, elem5e);

                //Варианты состава по серии профилей
                int series_id = elem5e.artiklRecAn().getInt(eArtikl.series_id);
                List<Record> elementList2 = eElement.find(series_id); //список элементов в серии
                detail(elementList2, elem5e);
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

                int element_id = elementRec.getInt(eElement.id);

                //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
                if (elementVar.filter(elem5e, elementRec) == true) {

                    //Если в проверочных парам. успех,
                    //выполним установочные параметры
                    elementVar.listenerFire();

                    setVariant.add(elementRec.getInt(eElement.id)); //сделано для запуска формы Elements на ветке Systree

                    UColor.colorFromParam(elem5e); //правило подбора текстур по параметру
                    List<Record> elemdetList = eElemdet.find(element_id); //список элем. детализации
                    
                    //Цикл по детализации
                    for (Record elemdetRec : elemdetList) {
                        HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры детализации

                        //ФИЛЬТР детализации, параметры накапливаются в mapParam
                        if (elementDet.filter(mapParam, elem5e, elemdetRec) == true) {

                            Record artiklRec = eArtikl.find(elemdetRec.getInt(eElemdet.artikl_id), false);
                            Specific spcAdd = new Specific(elemdetRec, artiklRec, elem5e, mapParam);
                            if (UColor.colorFromProduct(spcAdd, 1)
                                    && UColor.colorFromProduct(spcAdd, 2)
                                    && UColor.colorFromProduct(spcAdd, 3)) {

                                spcAdd.place = "ВСТ";

                                //Если (контейнер) в списке детализации, например профиль с префиксом @
                                if (TypeArtikl.isType(artiklRec, TypeArtikl.X101, TypeArtikl.X102, TypeArtikl.X103)) {
                                    elem5e.spcRec().setArtiklRec(spcAdd.artiklRec); //переназначаем артикул, как правило это c префиксом артикула @
                                    elem5e.spcRec().mapParam = spcAdd.mapParam; //переназначаем mapParam

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
