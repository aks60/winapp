package builder.specif;

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
import builder.model.Com5t;
import builder.param.ElementDet;
import builder.param.FillingDet;
import builder.param.FillingVar;
import builder.model.ElemGlass;
import builder.model.ElemSimple;
import dataset.Query;
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

                Set<Integer> setArr = new HashSet();
                if (TypeElem.ARCH == elemGlass.owner().type()) {

                    if (elemGlass.id() == 8 || elemGlass.id() == 13) {
//                        List<ElemSimple> listElem = root().listElem(TypeElem.IMPOST);
//                        ElemSimple tlemSimple = listElem.stream().filter(el -> el.inside(elemGlass.x1 + elemGlass.width() / 2, elemGlass.y2) == true && el.layout() != LayoutArea.ARCH).findFirst().orElse(null);
                        
                        List<ElemSimple> els = root().listElem(TypeElem.IMPOST);
                        Com5t com5t = els.stream().filter(it -> it.id() == 6).findFirst().orElse(null);
                        boolean bbbb = com5t.inside((elemGlass.x1 + elemGlass.width()) / 2, elemGlass.y2);
                        
                        System.err.println(elemGlass.join(LayoutArea.BOTTOM));
                        System.err.println((elemGlass.x1 + elemGlass.width()) / 2 + "   " + elemGlass.y2);
                        System.err.println(elemGlass);
                        System.err.println(elemGlass.owner());

                        
                        setArr.add(elemGlass.owner().mapFrame.get(LayoutArea.ARCH).artiklRec.getInt(eArtikl.id));
//                    setArr.addAll(Arrays.asList(elemGlass.join(LayoutArea.LEFT).artiklRec.getInt(eArtikl.id),
//                            elemGlass.join(LayoutArea.BOTTOM).artiklRec.getInt(eArtikl.id), elemGlass.join(LayoutArea.RIGHT).artiklRec.getInt(eArtikl.id)));
                    }
                } else {
                    setArr.addAll(Arrays.asList(elemGlass.join(LayoutArea.LEFT).artiklRec.getInt(eArtikl.id), elemGlass.join(LayoutArea.TOP).artiklRec.getInt(eArtikl.id),
                            elemGlass.join(LayoutArea.BOTTOM).artiklRec.getInt(eArtikl.id), elemGlass.join(LayoutArea.RIGHT).artiklRec.getInt(eArtikl.id)));
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

                    //Доступные толщины 
                    if (Util.containsFloat(glasgrpRec.getStr(eGlasgrp.depth), depth) == true) {
                        listVariants.add(glasgrpRec.getInt(eGlasgrp.id)); //сделано для запуска формы Filling на ветке Systree

                        Set<Integer> setArt2 = new HashSet();
                        for (Record glasprofRec : eGlasprof.findAll()) {
                            if (glasgrpRec.getInt(eGlasgrp.id) == glasprofRec.getInt(eGlasprof.glasgrp_id)) {
                                if (artprofRec.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) {
                                    if (glasprofRec.getInt(eGlasprof.inside) == 1) {
                                        setArt2.add(glasprofRec.getInt(eGlasprof.artikl_id));
                                    }
                                }
                            }
                        }

                        //Цикл по профилям в группах заполнений
                        for (Record glasprofRec : eGlasprof.findAll()) {

                            if (glasgrpRec.getInt(eGlasgrp.id) == glasprofRec.getInt(eGlasprof.glasgrp_id)) {
                                if (artprofRec.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) {
                                    if (glasprofRec.getInt(eGlasprof.inside) == 1) {

                                        //Данные для старого алгоритма расчёта
                                        elemGlass.gzazo = glasgrpRec.getFloat(eGlasgrp.gap);
                                        elemGlass.owner().gsize = glasprofRec.getFloat(eGlasprof.gsize);

                                        detail(elemGlass, glasgrpRec);
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
