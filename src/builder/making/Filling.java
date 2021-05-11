package builder.making;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlasprof;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeElem;
import enums.UseArtiklTo;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import builder.Wincalc;
import builder.param.ElementDet;
import builder.param.FillingDet;
import builder.param.FillingVar;
import builder.model.ElemGlass;
import common.Util;
import dataset.Query;
import domain.eSyssize;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        super.calc();
        try {
            Record systreeRec = eSystree.find(iwin().nuni);
            String depthSet = systreeRec.getStr(eSystree.depth);
            List<Record> sysprofList = eSysprof.find(iwin().nuni);
            LinkedList<ElemGlass> elemGlassList = iwin().rootArea.listElem(TypeElem.GLASS);

            //Цикл по стеклопакетам
            for (ElemGlass elemGlass : elemGlassList) {
                UseArtiklTo typeProf = (elemGlass.owner().type() == TypeElem.STVORKA)
                        ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME; //стекло может быть в створке или коробке
                Float depth = elemGlass.artiklRec.getFloat(eArtikl.depth); //толщина стекда

                Set<Integer> setArt = new HashSet();
                if (TypeElem.ARCH != elemGlass.owner().type()) {
                    setArt.addAll(Arrays.asList(elemGlass.join(LayoutArea.LEFT).artiklRecAn.getInt(eArtikl.id), elemGlass.join(LayoutArea.TOP).artiklRecAn.getInt(eArtikl.id),
                            elemGlass.join(LayoutArea.BOTTOM).artiklRecAn.getInt(eArtikl.id), elemGlass.join(LayoutArea.RIGHT).artiklRecAn.getInt(eArtikl.id)));
                }

                Record artprofRec = null;
                //Цикл по системе конструкций, ищем артикул системы профилей
                for (Record sysprofRec : sysprofList) {
                    if (typeProf.id == sysprofRec.getInt(eSysprof.use_type)) {
                        artprofRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true); //включая аналог!
                        break;
                    }
                }
                //Цикл по группам заполнений
                for (Record glasgrpRec : eGlasgrp.findAll()) {
                    if (Util.containsNumb(glasgrpRec.getStr(eGlasgrp.depth), depth) == true) { //доступные толщины 
                        listVariants.add(glasgrpRec.getInt(eGlasgrp.id)); //сделано для запуска формы Filling на ветке Systree
                        List<Record> glasprofList = eGlasprof.find(glasgrpRec.getInt(eGlasgrp.id));

                        //Цикл по профилям в группах заполнений
                        for (Record glasprofRec : glasprofList) {
                            if (artprofRec.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) {
                                if (glasprofRec.getInt(eGlasprof.inside) == 1) {
                                    elemGlass.gzazo = glasgrpRec.getFloat(eGlasgrp.gap);

                                    //Данные для старого алгоритма расчёта 
                                    if (iwin().syssizeRec.getInt(eSyssize.id) == -1) {
                                        for (Integer id : setArt) {
                                            for (Record record : glasprofList) {
                                                if (id == record.getInt(eGlasprof.artikl_id)) {
                                                    elemGlass.hmGsize.put(id, record.getFloat(eGlasprof.gsize));
                                                }
                                            }
                                        }
                                    }
                                    detail(elemGlass, glasgrpRec);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Filling.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    protected void detail(ElemGlass elemGlass, Record glasgrpRec) {
        try {
            //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
            if (fillingVar.check(elemGlass, glasgrpRec) == true) {

                elemGlass.setSpecific(); //заполним спецификацию элемента

                List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec.getFloat(eArtikl.depth));

                //Цикл по списку детализации
                for (Record glasdetRec : glasdetList) {
                    HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific

                    //ФИЛЬТР детализации, параметры накапливаются в mapParam
                    if (fillingDet.check(mapParam, elemGlass, glasdetRec) == true) {
                        Record artiklRec = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        SpecificRec spcAdd = new SpecificRec(glasdetRec, artiklRec, elemGlass, mapParam);
                        if (Color.colorFromProduct(spcAdd, 1)
                                && Color.colorFromProduct(spcAdd, 2)
                                && Color.colorFromProduct(spcAdd, 3)) {

                            spcAdd.place = "ЗАП";
                            elemGlass.addSpecific(spcAdd);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Filling.detail() " + e);
        }
    }
}
