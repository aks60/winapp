package builder.making;

import dataset.Record;
import domain.eArtikl;
import domain.eElemdet;
import domain.eElement;
import domain.eElempar1;
import domain.eElempar2;
import enums.TypeArtikl;
import enums.TypeElem;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import builder.Wincalc;
import builder.param.ElementDet;
import builder.param.ElementVar;
import builder.model.ElemSimple;
import dataset.Query;
import java.util.Map;

/**
 * Составы.
 */
public class Elements extends Cal5e {

    private ElementVar elementVar = null;
    private ElementDet elementDet = null;

    public Elements(Wincalc iwin) {
        super(iwin);
        elementVar = new ElementVar(iwin);
        elementDet = new ElementDet(iwin);
    }

    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //Но при проверке параметров использую оригин. мат. ценность. (Непонятно!!!)
    public void calc() {
        super.calc();
        LinkedList<ElemSimple> listElem = iwin().rootArea.listElem(TypeElem.FRAME_SIDE,
                 TypeElem.STVORKA_SIDE, TypeElem.IMPOST, TypeElem.SHTULP); //список сторон рам, створок и импостов
        try {
            //Цикл по списку элементов конструкции
            for (ElemSimple elem5e : listElem) {
                
                elem5e.setSpecific(); //коррекция спецификации              

                //Варианты состава для артикула профиля
                int artikl_id = elem5e.artiklRecAn.getInt(eArtikl.id);
                //Найдем список элементов по артикулу элемента конструкции
                List<Record> elementList3 = eElement.find2(artikl_id);
                detail(elementList3, elem5e);

                //Варианты состава для серии профилей
                int series_id = elem5e.artiklRecAn.getInt(eArtikl.series_id);
                //Найдем список элементов по артикулу элемента конструкции
                List<Record> elementList2 = eElement.find(series_id);
                detail(elementList2, elem5e);
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Elements.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    protected void detail(List<Record> elementList, ElemSimple elem5e) {
        try {
            //Цикл по вариантам
            for (Record elementRec : elementList) {
                
                int element_id = elementRec.getInt(eElement.id);
                listVariants.add(elementRec.getInt(eElement.id)); //сделано для запуска формы Elements на ветке Systree

                //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
                if (elementVar.filter(elem5e, elementRec) == true) {
                    Color.colorFromParam(elem5e); //правило подбора текстур по параметру

                    //Цикл по детализации
                    List<Record> elemdetList = eElemdet.find(element_id);
                    for (Record elemdetRec : elemdetList) {
                        HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры детализации
                        int elemdet_id = elemdetRec.getInt(eElemdet.id);

                        //ФИЛЬТР детализации, параметры накапливаются в mapParam
                        if (elementDet.filter(mapParam, elem5e, elemdetRec) == true) {

                            Record artiklRec = eArtikl.find(elemdetRec.getInt(eElemdet.artikl_id), false);
                            Specific spcAdd = new Specific(elemdetRec, artiklRec, elem5e, mapParam);
                            if (Color.colorFromProduct(spcAdd, 1)
                                    && Color.colorFromProduct(spcAdd, 2)
                                    && Color.colorFromProduct(spcAdd, 3)) {

                                spcAdd.place = "ВСТ";

                                //Если (контейнер) в списке детализации, например профиль с префиксом @
                                if (TypeArtikl.isType(artiklRec, TypeArtikl.X101, TypeArtikl.X102, TypeArtikl.X103)) {
                                    elem5e.spcRec.setArtiklRec(spcAdd.artiklDet); //переназначаем артикл, как правило это c префиксом артикла @
                                    elem5e.spcRec.mapParam = spcAdd.mapParam; //переназначаем mapParam

                                } else {
                                    elem5e.addSpecific(spcAdd); //коррекция спецификации 
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Сomposition.detail() " + e);
        }
    }
}
