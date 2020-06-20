package estimate.constr;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlaspar2;
import domain.eGlasprof;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
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
        try {
            Record systreeRec = eSystree.find(iwin().nuni);
            String depthSet = systreeRec.getStr(eSystree.depth);
            List<Record> sysprofList = eSysprof.find(iwin().nuni);
            LinkedList<ElemGlass> elemGlassList = iwin().rootArea.listElem(TypeElem.GLASS);

            //Цикл по стеклопакетам
            for (ElemGlass elemGlass : elemGlassList) {
                UseArtiklTo typeProf = (elemGlass.owner().type() == TypeElem.STVORKA) ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME;
                Float depth = elemGlass.artiklRec.getFloat(eArtikl.depth);
                Record artprofRec = null;

                //Доступные толщины
                if (containsFloat(depthSet, depth) == false) {
                    //Цикл по системе конструкций, ищем артикул системы профилей
                    for (Record sysprofRec : sysprofList) {
                        if (typeProf.id == sysprofRec.getInt(eSysprof.use_type)) {
                            artprofRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
                            break;
                        }
                    }
                    //Цикл по группам заполнений
                    for (Record glasgrpRec : eGlasgrp.findAll()) {

                        //Доступные толщины
                        if (containsFloat(glasgrpRec.getStr(eGlasgrp.depth), depth) == false) {
                            listVariants.add(glasgrpRec.getInt(eGlasgrp.id)); //сделано для запуска формы Filling из формы Systree

                            //Цикл по профилям в группах заполнений
                            for (Record glasprofRec : eGlasprof.findAll()) {
                                if (glasgrpRec.getInt(eGlasgrp.id) == glasprofRec.getInt(eGlasprof.glasgrp_id)) {
                                    if (artprofRec.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) {

                                        elemGlass.mapFieldVal.put("GZAZO", String.valueOf(glasgrpRec.get(eGlasgrp.gap)));
                                        detail(elemGlass, glasgrpRec);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("estimate.constr.Filling.calc() " + e);
        }
    }

    protected boolean detail(ElemGlass elemGlass, Record glasgrpRec) {
        try {
            List<Record> glaspar1List = eGlaspar1.find(glasgrpRec.getInt(eGlasgrp.id));

            //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
            if (fillingVar.check(elemGlass, glaspar1List) == true) {
                elemGlass.setSpecific(); //заполним спецификацию элемента
                List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec.getFloat(eArtikl.depth));

                //Цикл по списку детализации
                for (Record glasdetRec : glasdetList) {
                    HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific
                    List<Record> glaspar2List = eGlaspar2.find(glasdetRec.getInt(eGlasdet.id)); //список параметров детализации  

                    //ФИЛЬТР детализации, параметры накапливаются в mapParam
                    if (fillingDet.check(mapParam, elemGlass, glaspar2List) == true) {
                        Record art = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        Specification specif = new Specification(art, elemGlass, mapParam);
                        specif.place = "ЗАП";
                        elemGlass.addSpecific(specif);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("estimate.constr.Filling.detail() " + e);
            return false;
        }
    }
}
