package builder.specif;

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
import builder.Wincalc;
import builder.param.ElementDet;
import builder.param.FillingDet;
import builder.param.FillingVar;
import builder.model.AreaArch;
import builder.model.ElemFrame;
import builder.model.ElemGlass;
import builder.model.ElemSimple;
import dataset.Query;
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

    public void calc2() {
        super.calc();
        try {
            Record systreeRec = eSystree.find(iwin().nuni); //ветка системы
            String depthSet = systreeRec.getStr(eSystree.depth); //доступные толщины
            List<Record> sysprofList = eSysprof.find(iwin().nuni); //список профилей в ветке
            LinkedList<ElemGlass> elemGlassList = iwin().rootArea.listElem(TypeElem.GLASS); //список стеклопакетов

            //Цикл по стеклопакетам
            for (ElemGlass elemGlass : elemGlassList) {
                UseArtiklTo typeProf = (elemGlass.owner().type() == TypeElem.STVORKA)
                        ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME; //стекло может быть в створке или раме
                Float depth = elemGlass.artiklRec.getFloat(eArtikl.depth); //толщина стекла в стеклопакете
                
                //Цикл по профилям в стеклопакете    
                for (Map.Entry<LayoutArea, ElemFrame> entry : elemGlass.owner().mapFrame.entrySet()) {
                    ElemFrame elemFrame = entry.getValue();

                    //Цикл по группам заполнений
                    for (Record glasgrpRec : eGlasgrp.findAll()) {

                        //Доступные толщины в группе заполнения 
                        if (Util.containsFloat(glasgrpRec.getStr(eGlasgrp.depth), depth) == true) {
                            listVariants.add(glasgrpRec.getInt(eGlasgrp.id)); //сделано для запуска формы Filling на ветке Systree

                            //Цикл по профилям в группах заполнений
                            for (Record glasprofRec : eGlasprof.findAll()) {

                                if (glasgrpRec.getInt(eGlasgrp.id) == glasprofRec.getInt(eGlasprof.glasgrp_id)) {
                                    if (elemFrame.artiklRecAn.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) {
                                        if (glasprofRec.getInt(eGlasprof.inside) == 1) { //пока только для внутреннего

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
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Filling.calc() " + e);
        } finally {
            Query.conf = conf;
        }
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
                        ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME; //стекло может быть в створке или раме
                Float depth = elemGlass.artiklRec.getFloat(eArtikl.depth); //толщина стекда
                Record artprofRec = null;

                //Если есть такая допустимая толщина. Мы уже пользовались этой проверкой при постоении модели
                if (Util.containsFloat(depthSet, depth) == true) {

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

                            //Цикл по профилям в группах заполнений
                            for (Record glasprofRec : eGlasprof.findAll()) {

                                if (glasgrpRec.getInt(eGlasgrp.id) == glasprofRec.getInt(eGlasprof.glasgrp_id)) {
                                    //if (artprofRec != null && artprofRec.getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) {
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
                        Specification specif = new Specification(glasdetRec, artiklRec, elemGlass, mapParam);
                        if (Color.colorFromProduct(specif, 1)
                                && Color.colorFromProduct(specif, 2)
                                && Color.colorFromProduct(specif, 3)) {

                            specif.place = "ЗАП";
                            elemGlass.addSpecific(specif);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Filling.detail() " + e);
        }
    }
}
