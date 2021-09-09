package builder.making;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlasprof;
import domain.eSysprof;
import domain.eSystree;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import builder.Wincalc;
import builder.param.ElementDet;
import builder.param.FillingDet;
import builder.param.FillingVar;
import builder.model.ElemGlass;
import builder.model.ElemSimple;
import common.UCom;
import dataset.Query;
import static domain.eArtikl.depth;
import enums.Layout;
import enums.Type;
import enums.UseArtiklTo;
import java.util.Arrays;

/**
 * Заполнения
 */
public class Filling extends Cal5e {

    private FillingVar fillingVar = null;
    private FillingDet fillingDet = null;
    private ElementDet elementDet = null;

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
            LinkedList<ElemGlass> elemGlassList = rootArea().listElem(Type.GLASS);

            //Цикл по стеклопакетам
            for (ElemGlass elemGlass : elemGlassList) {
                boolean way = false;

                UseArtiklTo typeProf = (elemGlass.owner().type() == Type.STVORKA)
                        ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME; //стекло может быть в створке или коробке
                Record artprofRec = null;
                //Цикл по системе конструкций, ищем артикул системы профилей
                for (Record sysprofRec : sysprofList) {
                    if (typeProf.id == sysprofRec.getInt(eSysprof.use_type)) {
                        artprofRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true); //включая аналог!
                        break;
                    }
                }
                //Цикл по сторонам стеклопакета
                for (int side = 0; side < 4; ++side) {
                    elemGlass.anglHoriz = elemGlass.sideHoriz[side];

                    //Цикл по группам заполнений
                    for (Record glasgrpRec : eGlasgrp.findAll()) {
                        if (UCom.containsNumbJust(glasgrpRec.getStr(eGlasgrp.depth), depth) == true) { //доступные толщины 
                            List<Record> glasprofList = eGlasprof.find(glasgrpRec.getInt(eGlasgrp.id));
                            List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec.getFloat(eArtikl.depth));

                            //Цикл по профилям в группах заполнений
                            for (Record glasprofRec : glasprofList) {
                                if (artprofRec.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) { //если артикулы совпали
                                    if (Arrays.asList(1, 2, 3, 4).contains(glasprofRec.getInt(eGlasprof.inside))) {
                                        elemGlass.gzazo = glasgrpRec.getFloat(eGlasgrp.gap);
                                        if (way == false) {
                                            listVariants.add(glasgrpRec.getInt(eGlasgrp.id)); //сделано для запуска формы Filling на ветке Systree
                                            elemGlass.setSpecific(); //заполним спецификацию элемента
                                            way = true;
                                        }
                                        detail(elemGlass, glasgrpRec, glasdetList);
                                    }
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

    public void calc2() {
        super.calc();
        try {
            Record systreeRec = eSystree.find(iwin().nuni);
            String depthSet = systreeRec.getStr(eSystree.depth);
            List<Record> sysprofList = eSysprof.find(iwin().nuni);
            LinkedList<ElemGlass> elemGlassList = rootArea().listElem(Type.GLASS);

            //Цикл по группам заполнений
            for (Record glasgrpRec : eGlasgrp.findAll()) {

                //Цикл по стеклопакетам
                for (ElemGlass elemGlass : elemGlassList) {
                    ElemSimple elemFrameArr[] = {elemGlass.joinFlat(Layout.BOTT), elemGlass.joinFlat(Layout.RIGHT), elemGlass.joinFlat(Layout.TOP), elemGlass.joinFlat(Layout.LEFT)};
                    Float depth = elemGlass.artiklRec.getFloat(eArtikl.depth); //толщина стекда
                    boolean way = false;

                    if (UCom.containsNumbJust(glasgrpRec.getStr(eGlasgrp.depth), depth) == true) { //доступные толщины                         
                        List<Record> glasprofList = eGlasprof.find(glasgrpRec.getInt(eGlasgrp.id));

                        //Цикл по рамам стеклопакета
                        //for (ElemSimple elem5e : elemFrameArr) {
                        for (int side = 0; side < 4; ++side) {
                            elemGlass.anglHoriz = elemGlass.sideHoriz[side];
                            ElemSimple elem5e = elemFrameArr[side];

                            //Цикл по профилям в группах заполнений
                            for (Record glasprofRec : glasprofList) {
                                if (elem5e.artiklRecAn.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) { //если артикулы совпали
                                    if (Arrays.asList(1, 2, 3, 4).contains(glasprofRec.getInt(eGlasprof.inside))) {
                                        if (way == false) {
                                            elemGlass.gzazo = glasgrpRec.getFloat(eGlasgrp.gap);
                                            listVariants.add(glasgrpRec.getInt(eGlasgrp.id)); //сделано для запуска формы Filling на ветке Systree
                                            elemGlass.setSpecific(); //заполним спецификацию элемента
                                            way = true;
                                        }
                                        detail2(elem5e, elemGlass, glasgrpRec);
                                    }
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

    protected void detail2(ElemSimple elem5e, ElemGlass elemGlass, Record glasgrpRec) {
        try {
            //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
            if (fillingVar.filter(elemGlass, glasgrpRec) == true) {

                //elem5e.setSpecific(); //заполним спецификацию элемента
                List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elem5e.artiklRec.getFloat(eArtikl.depth));

                //Цикл по списку детализации
                for (Record glasdetRec : glasdetList) {
                    HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific                        

                    //ФИЛЬТР детализации, параметры накапливаются в mapParam
                    if (fillingDet.filter(mapParam, elemGlass, glasdetRec) == true) {
                        Record artiklRec = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        Specific spcAdd = new Specific(glasdetRec, artiklRec, elem5e, mapParam);

                        if (UColor.colorFromProduct(spcAdd, 1)
                                && UColor.colorFromProduct(spcAdd, 2)
                                && UColor.colorFromProduct(spcAdd, 3)) {

                            spcAdd.place = "ЗАП";
                            elem5e.addSpecific(spcAdd);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Filling.detail() " + e);
        }
    }

    protected void detail(ElemGlass elemGlass, Record glasgrpRec, List<Record> glasdetList) {
        try {
            //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
            if (fillingVar.filter(elemGlass, glasgrpRec) == true) {

                //elemGlass.setSpecific(); //заполним спецификацию элемента
                //List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec.getFloat(eArtikl.depth));
                //Цикл по списку детализации
                for (Record glasdetRec : glasdetList) {

                    HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific                        

                    //ФИЛЬТР детализации, параметры накапливаются в mapParam
                    if (fillingDet.filter(mapParam, elemGlass, glasdetRec) == true) {
                        Record artiklRec = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        Specific spcAdd = new Specific(glasdetRec, artiklRec, elemGlass, mapParam);

                        if (UColor.colorFromProduct(spcAdd, 1)
                                && UColor.colorFromProduct(spcAdd, 2)
                                && UColor.colorFromProduct(spcAdd, 3)) {

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
