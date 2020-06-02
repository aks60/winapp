package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlaspar2;
import domain.eGlasprof;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import enums.UseSide;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import estimate.Wincalc;
import estimate.constr.param.ElementDet;
import estimate.constr.param.FillingDet;
import estimate.constr.param.FillingVar;
import estimate.model.AreaArch;
import estimate.model.ElemGlass;
import estimate.model.ElemSimple;

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

    public void build() {
        LinkedList<ElemGlass> elemGlassList = iwin().rootArea.listElem(TypeElem.GLASS);

        //Цикл по стеклопакетам
        for (ElemGlass elemGlass : elemGlassList) {
            UseArtiklTo useArtiklTo = (elemGlass.owner().type() == TypeElem.STVORKA) ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME; //владелец стеклопакета
            Record sysprofRec = eSysprof.find3(iwin().nuni, useArtiklTo, UseSide.LEFT, UseSide.VERT, UseSide.ANY);
            Record artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true); //запишем технологический код контейнера
            //Профиль который используется для вставки стеклопакета
            int profileId = (artiklRec.getInt(eArtikl.analog_id) != -1) ? artiklRec.getInt(eArtikl.analog_id) : artiklRec.getInt(eArtikl.id);

            //Цикл по группам заполнений
            for (Record glasgrpRec : eGlasgrp.findAll()) {
                //Цикл по системе конструкций, ищем артикул системы профилей
                List<Record> glasprofList = eGlasprof.find(glasgrpRec.getInt(eGlasgrp.id));
                //Цикл по профилям в группе заполнений
                for (Record glasprofRec : glasprofList) {
                    if (profileId != -1 && profileId == glasprofRec.getInt(eGlasprof.artikl_id) == true) { //если профиль есть в группе

                        elemGlass.mapFieldVal.put("GZAZO", String.valueOf(glasgrpRec.get(eGlasgrp.gap)));
                        detail(elemGlass, glasgrpRec);
                    }
                }
            }
        }
    }

    protected boolean detail(ElemGlass elemGlass, Record glasgrpRec) {

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
                        if (TypeElem.ARCH == elemGlass.owner().type()) {

                            //По основанию арки
                            specif = new Specification(art, elemGlass, mapParam);
                            double dh2 = artiklRec.getDbl(eArtikl.height) - gzazo;

                            double r1 = elemGlass.radiusGlass - dh2;
                            double h1 = elemGlass.height() - 2 * dh2;
                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний периметр
                            double r2 = elemGlass.radiusGlass;
                            double h2 = elemGlass.height();
                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                            double l3 = l2 - l1;
                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза

                            double r5 = elemGlass.radiusGlass + gzazo;
                            double h5 = elemGlass.height() + 2 * gzazo;
                            double l5 = overLength + 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда

                            specif.width = (float) l5;
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = (float) ang;
                            specif.anglCut1 = (float) ang;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию

                            //По дуге арки
                            specif = new Specification(art, elemGlass, mapParam);
                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                            double ang3 = 90 - (90 - ang2 + ang);
                            //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста
                            double koef = 2;
                            ElemSimple ramaArch = elemGlass.root().mapFrame.get(LayoutArea.ARCH);
                            double R2 = ((AreaArch) iwin().rootArea).radiusArch - ramaArch.specificationRec.height + artiklRec.getDbl(eArtikl.height);
                            double L2 = iwin().rootArea.width() - ramaArch.specificationRec.height * 2 + artiklRec.getDbl(eArtikl.height) * 2 - koef;
                            double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
                            double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
                            double Z = 3 * gzazo;
                            double R = elemGlass.radiusGlass;
                            double L = elemGlass.width();
                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
                            specif.width = (float) (overLength + M2);
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = (float) ang3;
                            specif.anglCut1 = (float) ang3;

                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию

                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, mapParam);
                            specif.width = elemGlass.width() + 2 * gzazo;
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию в элемент (верхний/нижний)
                            elemGlass.addSpecific(new Specification(specif)); //добавим спецификацию в элемент (верхний/нижний)
                            //По вертикали
                            specif = new Specification(art, elemGlass, mapParam);
                            specif.width = elemGlass.height() + 2 * gzazo;
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecific(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Уплотнитель
                    } else if (TypeArtikl.KONZEVPROF.id2 == artiklRec.getInt(eArtikl.level2)) { //уплотнитель
                        Record art = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        if (TypeElem.ARCH == elemGlass.owner().type()) { //если уплотнитель в арке
                            //По основанию арки
                            specif = new Specification(art, elemGlass, mapParam);

                            double dh2 = artiklRec.getFloat(eArtikl.height) - gzazo;
                            double r1 = elemGlass.radiusGlass - dh2;
                            double h1 = elemGlass.height() - 2 * dh2;
                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
                            double r2 = elemGlass.radiusGlass;
                            double h2 = elemGlass.height();
                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                            double l3 = l2 - l1;
                            double r5 = elemGlass.radiusGlass + gzazo;
                            double h5 = elemGlass.height() + 2 * gzazo;
                            double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
                            specif.width = (float) l5;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = (float) ang;
                            specif.anglCut1 = (float) ang;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию в элемент (верхний/нижний)

                            //По дуге арки
                            specif = new Specification(art, elemGlass, mapParam);
                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                            double ang3 = 90 - (90 - ang2 + ang);
                            double Z = 3 * gzazo;
                            double R = elemGlass.radiusGlass;
                            double L = elemGlass.width();
                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
                            specif.width = (float) M;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = (float) ang3;
                            specif.anglCut1 = (float) ang3;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию в элемент (верхний/нижний)
                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, mapParam);
                            specif.width = elemGlass.width() + 2 * gzazo;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecific(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                            //По вертикали
                            specif = new Specification(art, elemGlass, mapParam);
                            specif.width = elemGlass.height() + 2 * gzazo;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecific(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecific(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Всё остальное
                    } else {
                        Record art = eArtikl.find(glasdetRec.getInt(eGlasdet.artikl_id), false);
                        if (TypeElem.AREA == elemGlass.owner().type()
                                || TypeElem.RECTANGL == elemGlass.owner().type()
                                || TypeElem.STVORKA == elemGlass.owner().type()) {

                            for (int index = 0; index < 4; index++) {
                                specif = new Specification(art, elemGlass, mapParam);
                                specif.setColor(elemGlass, glasdetRec);
                                elemGlass.addSpecific(specif);
                            }
                        } else if (TypeElem.ARCH == elemGlass.owner().type()) {
                            for (int index = 0; index < 2; index++) {
                                specif = new Specification(art, elemGlass, mapParam);
                                specif.setColor(elemGlass, glasdetRec);
                                elemGlass.addSpecific(specif);
                            }
                        } else {
                            specif = new Specification(art, elemGlass, mapParam);
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
