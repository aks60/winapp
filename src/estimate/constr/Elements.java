package estimate.constr;

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
import estimate.Wincalc;
import estimate.constr.param.ElementDet;
import estimate.constr.param.ElementSet;
import estimate.constr.param.ElementVar;
import estimate.model.ElemSimple;

/**
 * Составы.
 */
public class Elements extends Cal5e {

    private ElementVar elementVar = null;
    private ElementDet elementDet = null;
    private ElementSet elementSet = null;

    public Elements(Wincalc iwin) {
        super(iwin);
        elementVar = new ElementVar(iwin);
        elementDet = new ElementDet(iwin);
        elementSet = new ElementSet(iwin);
    }

    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //Но при проверке параметров использую оригин. мат. ценность. (Непонятно!!!)
    public void calc() {
        listVariants.clear();
        LinkedList<ElemSimple> listElem = iwin().rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST);
        try {
            //Цикл по списку элементов конструкции
            for (ElemSimple elem5e : listElem) {

                elem5e.setSpecific(); //коррекция спецификации              

                //Варианты состава для серии профилей
                int series_id = elem5e.artiklRecAn.getInt(eArtikl.series_id);
                List<Record> elementList2 = eElement.find(series_id);
                detail(elementList2, elem5e);

                //Варианты состава для артикула профиля
                int artikl_id = elem5e.artiklRecAn.getInt(eArtikl.id);
                List<Record> elementList3 = eElement.find2(artikl_id);
                detail(elementList3, elem5e);
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Elements.calc() " + e);
        }
    }

    protected void detail(List<Record> elementList, ElemSimple elem5e) {
        try {
            //Цикл по вариантам
            for (Record elementRec : elementList) {
                int element_id = elementRec.getInt(eElement.id);
                List<Record> elempar1List = eElempar1.find3(element_id); //список параметров вариантов использования
                listVariants.add(elementRec.getInt(eElement.id)); //сделано для запуска формы Elements на ветке Systree

                //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
                if (elementVar.check(elem5e, elempar1List) == true) {

                    elementSet.change(elem5e.specificationRec); //коррекция основной спецификации параметрами

                    //Цикл по детализации
                    List<Record> elemdetList = eElemdet.find(element_id);
                    for (Record elemdetRec : elemdetList) {
                        HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры детализации
                        int elemdet_id = elemdetRec.getInt(eElemdet.id);
                        List<Record> elempar2List = eElempar2.find3(elemdet_id); //список параметров детализации 

                        //ФИЛЬТР детализации, параметры накапливаются в mapParam
                        if (elementDet.check(mapParam, elem5e, elempar2List) == true) {

                            Record artiklRec = eArtikl.find(elemdetRec.getInt(eElemdet.artikl_id), false);
                            Specification specif = new Specification(elemdetRec, artiklRec, elem5e, mapParam);
                            Color.setting(specif, elemdetRec);
                            specif.place = "СОСТ";

                            //Если (контейнер) в списке детализации, например профиль с префиксом @
                            if (TypeArtikl.isType(artiklRec, TypeArtikl.KOROBKA, TypeArtikl.STVORKA, TypeArtikl.IMPOST)) {
                                elem5e.specificationRec.setArtiklRec(specif.artiklRec); //переназначаем артикл, как правило это c префиксом артикла @
                                elem5e.specificationRec.mapParam = specif.mapParam; //переназначаем mapParam
                                elementSet.change(elem5e.specificationRec); //коррекция спецификации параметрами 

                            } else {
                                elem5e.addSpecific(specif); //коррекция спецификации
                                elementSet.change(specif);  //коррекция спецификации параметрами  
                                int mmm = 0;
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
