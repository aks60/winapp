package estimate.constr;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlaspar2;
import domain.eGlasprof;
import enums.LayoutArea;
import enums.TypeArtikl;
import enums.TypeElem;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import estimate.Wincalc;
import estimate.constr.param.ElementDet;
import estimate.constr.param.FillingDet;
import estimate.constr.param.FillingVar;
import estimate.model.AreaArch;
import estimate.model.ElemFrame;
import estimate.model.ElemGlass;
import estimate.model.ElemSimple;
import java.util.Map;

/**
 * Заполнения
 */
public class Filling extends Cal5e {

    private FillingVar fillingVar = null;
    private FillingDet fillingDet = null;
    private ElementDet elementDet = null;
    public int pass = 1; //pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры

    public Filling(Wincalc iwin) {
        super(iwin);
        fillingVar = new FillingVar(iwin);
        fillingDet = new FillingDet(iwin);
        elementDet = new ElementDet(iwin);
    }

    public void calc() {

        //Цикл по стеклопакетам
        LinkedList<ElemGlass> elemGlassList = iwin().rootArea.listElem(TypeElem.GLASS);
        for (ElemGlass elemGlass : elemGlassList) {

            //Цыкл по элемента рамы(створки) стеклопакета
            for (Map.Entry<LayoutArea, ElemFrame> en : elemGlass.owner().mapFrame.entrySet()) {
                LayoutArea layoutArea = en.getKey();
                ElemFrame elemFrame = en.getValue();
                Record artiklRec = elemFrame.artiklRec;
                int artiklId = (artiklRec.getInt(eArtikl.analog_id) != -1) ? artiklRec.getInt(eArtikl.analog_id) : artiklRec.getInt(eArtikl.id);

                //Цыкл по профилям в группах заполнений
                List<Record> glasprofList = eGlasprof.find2(artiklId);
                for (Record glasprofRec : glasprofList) {

                    Record glasgrpRec = eGlasgrp.find(glasprofRec.getInt(eGlasprof.glasgrp_id)); //группа заполнений
                    String depth = String.valueOf((int) elemGlass.artiklRec.getFloat(eArtikl.depth));
                    if (glasgrpRec.getStr(eGlasgrp.depth).contains(depth)) {

                        elemGlass.mapFieldVal.put("GZAZO", String.valueOf(glasgrpRec.get(eGlasgrp.gap)));
                        detail(elemGlass, elemFrame, glasgrpRec);
                    }
                }
            }
        }
    }

    protected boolean detail(ElemGlass elemGlass, ElemFrame elemFrame, Record glasgrpRec) {

        //TODO в заполненииях текстура подбирается неправильно
        List<Record> glaspar1List = eGlaspar1.find(glasgrpRec.getInt(eGlasgrp.id));
        if (fillingVar.check(elemGlass, glaspar1List) == true) {  //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
            elemGlass.setSpecific(); //заполним спецификацию элемента
            List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec.getFloat(eArtikl.depth));

            //Цикл по списку детализации
            for (Record glasdetRec : glasdetList) {
                HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific
                List<Record> glaspar2List = eGlaspar2.find(glasdetRec.getInt(eGlasdet.id)); //список параметров детализации                
                if (fillingDet.check(mapParam, elemGlass, glaspar2List) == true) { //ФИЛЬТР детализации, параметры накапливаются в mapParam

                    Specification specif = null;
                    Record artiklRec = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), true);
                    float gzazo = Float.valueOf(elemGlass.mapFieldVal.get("GZAZO"));
                    Float overLength = (mapParam.get(15050) == null) ? 0.f : Float.valueOf(mapParam.get(15050).toString());

                    //Стеклопакет
                    if (TypeArtikl.GLASS.id2 == artiklRec.getInt(eArtikl.level2)) {

                        //Штапик
                    } else if (TypeArtikl.SHTAPIK.id2 == artiklRec.getInt(eArtikl.level2)) {

                        Record art = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        specif = new Specification(art, elemFrame, mapParam);
                        specif.setColor(elemFrame, glasdetRec);
                        elemGlass.addSpecific(specif);

                        //Уплотнитель
                    } else if (TypeArtikl.KONZEVPROF.id2 == artiklRec.getInt(eArtikl.level2)) {

                        Record art = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        specif = new Specification(art, elemFrame, mapParam);
                        specif.setColor(elemFrame, glasdetRec);
                        elemGlass.addSpecific(specif);

                        //Всё остальное
                    } else {
                        Record art = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        if (TypeElem.AREA == elemGlass.owner().type()
                                || TypeElem.RECTANGL == elemGlass.owner().type()
                                || TypeElem.STVORKA == elemGlass.owner().type()) {

                            specif = new Specification(art, elemFrame, mapParam);
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif);

                        } else if (TypeElem.ARCH == elemGlass.owner().type()) {
                            for (int index = 0; index < 2; index++) {
                                specif = new Specification(art, elemFrame, mapParam);
                                specif.setColor(elemGlass, glasdetRec);
                                elemGlass.addSpecific(specif);
                            }
                        } else {
                            specif = new Specification(art, elemFrame, mapParam);
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif);
                        }
                    }
                    specif.place = "ЗАП";
                }
            }
        }
        return true;
    }
}
