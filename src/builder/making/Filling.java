package builder.making;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlasprof;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import builder.Wincalc;
import builder.param.ElementDet;
import builder.param.FillingDet;
import builder.param.FillingVar;
import builder.IElem5e;
import common.UCom;
import dataset.Query;
import enums.Layout;
import enums.Type;

/**
 * Заполнения
 */
public class Filling extends Cal5e {

    private FillingVar fillingVar = null;
    private FillingDet fillingDet = null;
    private ElementDet elementDet = null;

    public Filling(Wincalc winc) {
        super(winc);
        fillingVar = new FillingVar(winc);
        fillingDet = new FillingDet(winc);
        elementDet = new ElementDet(winc);
    }

    public Filling(Wincalc winc, boolean shortPass) {
        super(winc);
        fillingVar = new FillingVar(winc);
        fillingDet = new FillingDet(winc);
        elementDet = new ElementDet(winc);
        this.shortPass = shortPass;
    }

    @Override
    public void calc() {
        LinkedList<IElem5e> elemGlassList = winc.listElem.filter(Type.GLASS);
        //Цикл по списку заполнений
        for (IElem5e elemGlass : elemGlassList) {
            calc(elemGlass);
        }
    }

    public void calc(IElem5e elemGlass) {
        super.calc();
        try {
            Float depth = elemGlass.artiklRec().getFloat(eArtikl.depth); //толщина стекда
            
            List<IElem5e> elemFrameList = null;  //список рам конструкции
            if (elemGlass.owner().type() == Type.ARCH) {
                elemFrameList = List.of(rootArea().frames().get(Layout.BOTT), rootArea().frames().get(Layout.RIGHT), rootArea().frames().get(Layout.TOP), rootArea().frames().get(Layout.LEFT));
            } else {
                elemFrameList = List.of(elemGlass.joinFlat(Layout.BOTT), elemGlass.joinFlat(Layout.RIGHT), elemGlass.joinFlat(Layout.TOP), elemGlass.joinFlat(Layout.LEFT));
            }

            //Цикл по сторонам стеклопакета
            for (int side = 0; side < 4; ++side) {
                IElem5e elemFrame = elemFrameList.get(side);
                elemGlass.anglHoriz(UCom.sideHoriz[side]); //устан. угол. проверяемой стороны

                //Цикл по группам заполнений
                for (Record glasgrpRec : eGlasgrp.findAll()) {
                    if (UCom.containsNumbJust(glasgrpRec.getStr(eGlasgrp.depth), depth) == true) { //доступные толщины 
                        List<Record> glasprofList = eGlasprof.find(glasgrpRec.getInt(eGlasgrp.id)); //список профилей в группе заполнений
                        
                        //Цикл по профилям в группах заполнений
                        for (Record glasprofRec : glasprofList) {
                            if (elemFrame.artiklRecAn().getInt(eArtikl.id) == glasprofRec.getInt(eGlasprof.artikl_id)) { //если артикулы совпали
                                if (List.of(1, 2, 3, 4).contains(glasprofRec.getInt(eGlasprof.inside))) {  //внутреннее заполнение допустимо

                                    //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
                                    if (fillingVar.filter(elemGlass, glasgrpRec) == true) {
                                        
                                        elemGlass.gzazo(glasgrpRec.getFloat(eGlasgrp.gap)); //зазор между фальцем и стеклопакетом
                                        elemGlass.gsize()[side] = glasprofRec.getFloat(eGlasprof.gsize); //размер от оси до стеклопакета

                                        if (shortPass == false) {
                                            List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec().getFloat(eArtikl.depth));
                                            detail(elemGlass, glasgrpRec, glasdetList);
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

    protected void detail(IElem5e elemGlass, Record glasgrpRec, List<Record> glasdetList) {
        try {
            //Цикл по списку детализации
            for (Record glasdetRec : glasdetList) {

                HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific                        

                //ФИЛЬТР детализации, параметры накапливаются в mapParam
                if (fillingDet.filter(mapParam, elemGlass, glasdetRec) == true) {
                    setVariant.add(glasgrpRec.getInt(eGlasgrp.id)); //сделано для запуска формы Filling на ветке Systree
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
        } catch (Exception e) {
            System.err.println("Ошибка:Filling.detail() " + e);
        }
    }
}
