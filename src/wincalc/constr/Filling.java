package wincalc.constr;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import wincalc.Wincalc;
import wincalc.constr.param.ElementDet;
import wincalc.constr.param.FillingDet;
import wincalc.constr.param.FillingVar;
import wincalc.model.AreaArch;
import wincalc.model.ElemGlass;
import wincalc.model.ElemSimple;

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
        for (pass = 1; pass < 4; pass++) {

            int systree_artikl_id = -1;
            List<Record> sysprofList = eSysprof.find(iwin().nuni);
            LinkedList<ElemGlass> elemGlassList = iwin().rootArea.listElem(TypeElem.GLASS);
            
            //Цикл по стеклопакетам
            for (ElemGlass elemGlass : elemGlassList) {

                //Цикл по группам заполнений
                for (Record glasgrpRec : eGlasgrp.select()) {

                    UseArtiklTo typeProf = (elemGlass.owner().type() == TypeElem.FULLSTVORKA) ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME;
                    //Цикл по системе конструкций, ищем артикул системы профилей
                    for (Record sysprofRec : sysprofList) {
                        if (typeProf.id == sysprofRec.getInt(eSysprof.use_type)) {

                            Record artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true); //запишем технологический код контейнера
                            systree_artikl_id = (artiklRec.getInt(eArtikl.analog_id) != -1) ? artiklRec.getInt(eArtikl.analog_id) : artiklRec.getInt(eArtikl.id);
                            break;
                        }
                    }

                    List<Record> glasprofList = eGlasprof.find(glasgrpRec.getInt(eGlasgrp.id));
                    //Цикл по профилям в группе заполнений
                    for (Record glasprofRec : glasprofList) {

                        if (systree_artikl_id != -1 && systree_artikl_id == glasprofRec.getInt(eGlasprof.artikl_id) == true) { //если профиль есть в группе

                            elemGlass.mapFieldVal.put("GZAZO", String.valueOf(glasgrpRec.get(eGlasgrp.gap)));
                            nested(elemGlass, glasgrpRec);
                        }
                    }
                }
            }
        }
    }

    protected boolean nested(ElemGlass elemGlass, Record glasgrpRec) {

        //TODO в заполненииях текстура подбирается неправильно
        ArrayList<Record> pargrupList = eGlaspar1.find(glasgrpRec.getInt(eGlasgrp.id));
        boolean out = fillingVar.check(elemGlass, pargrupList); //ФИЛЬТР вариантов
        if (out == true) {

            if (pass == 2) {
                elemGlass.setSpecifElement(); //заполним спецификацию элемента
            }
            ArrayList<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec.getFloat(eArtikl.depth));

            //Цикл по списку детализации
            for (Record clasdetRec : glasdetList) {

                HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
                List<Record> glaspar2List = eGlaspar2.find(clasdetRec.getInt(eGlasdet.id)); //список параметров детализации
                if (fillingDet.check(hmParam, elemGlass, glaspar2List) == true) { //ФИЛЬТР спец.

                    if (pass == 1 || pass == 3) {
                        continue; //если нулевой и второй цыкл ничего не создаём
                    }
                    Specification specif = null;
                    Record artiklRec = eArtikl.find(clasdetRec.getInt(eGlasdet.artikl_id), true);
                    float gzazo = Float.valueOf(elemGlass.mapFieldVal.get("GZAZO"));
                    Float overLength = (hmParam.get(15050) == null) ? 0.f : Float.valueOf(hmParam.get(15050).toString());

                    //Стеклопакет
                    if (TypeArtikl.GLASS.id2 == artiklRec.getInt(eArtikl.level2)) {

                        //Штапик
                    } else if (TypeArtikl.SHTAPIK.id2 == artiklRec.getInt(eArtikl.level2)) {

                        Record art = eArtikl.find(clasdetRec.getInt(eGlasdet.artikl_id), false);
                        if (TypeElem.ARCH == elemGlass.owner().type()) {

                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);
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
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
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

                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.width() + 2 * gzazo;
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (верхний/нижний)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.height() + 2 * gzazo;
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Уплотнитель
                    } else if (TypeArtikl.KONZEVPROF.id2 == artiklRec.getInt(eArtikl.level2)) { //уплотнитель
                        Record art = eArtikl.find(clasdetRec.getInt(eArtdet.artikl_id), false);
                        if (TypeElem.ARCH == elemGlass.owner().type()) { //если уплотнитель в арке
                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);

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
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
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
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.width() + 2 * gzazo;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.height() + 2 * gzazo;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Всё остальное
                    } else {
                        Record art = eArtikl.find(clasdetRec.getInt(eArtdet.artikl_id), false);
                        if (TypeElem.AREA == elemGlass.owner().type()
                                || TypeElem.RECTANGL == elemGlass.owner().type()
                                || TypeElem.FULLSTVORKA == elemGlass.owner().type()) {

                            for (int index = 0; index < 4; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(elemGlass, clasdetRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else if (TypeElem.ARCH == elemGlass.owner().type()) {
                            for (int index = 0; index < 2; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(elemGlass, clasdetRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else {
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.setColor(elemGlass, clasdetRec);
                            elemGlass.addSpecifSubelem(specif);
                        }
                    }
                }
            }
        }
        return true;
    }

    protected boolean nested2(ElemGlass elemGlass, Record glasgrpRec) {

        //TODO в заполненииях текстура подбирается неправильно
        List<Record> glaspar1List = eGlaspar1.find(glasgrpRec.getInt(eGlasgrp.id));
        boolean out = fillingVar.check(elemGlass, glaspar1List); //ФИЛЬТР вариантов
        if (out == true) {

            elemGlass.setSpecifElement(); //заполним спецификацию элемента
            List<Record> glasdetList = eGlasdet.find(glasgrpRec.getInt(eGlasgrp.id), elemGlass.artiklRec.getInt(eArtikl.depth));
            //Цикл по списку детализации
            for (Record glasdetRec : glasdetList) {

                Specification specif = null;
                HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
                Record artiklRec = eArtikl.find(glasdetRec.getInt(eArtdet.artikl_id), true);
                float gzazo = Float.valueOf(elemGlass.mapFieldVal.get("GZAZO"));
                List<Record> glaspar2List = eGlaspar2.find(glasdetRec.getInt(eGlasdet.id)); //список параметров детализации
                out = elementDet.check(hmParam, elemGlass, glaspar2List); //ФИЛЬТР детализации
                Float overLength = (hmParam.get(15050) == null) ? 0.f : Float.valueOf(hmParam.get(15050).toString());
                if (out == true) {
                    //Стеклопакет
                    if (TypeArtikl.GLASS.id2 == artiklRec.getInt(eArtikl.level2)) {

                        //Штапик
                    } else if (TypeArtikl.SHTAPIK.id2 == artiklRec.getInt(eArtikl.level2)) {

                        Record art = eArtikl.find(glasdetRec.getInt(eArtdet.artikl_id), false);
                        if (TypeElem.ARCH == elemGlass.owner().type()) {

                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double dh2 = artiklRec.getFloat(eArtikl.height) - gzazo;

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
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                            double ang3 = 90 - (90 - ang2 + ang);
                            //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста
                            double koef = 2;
                            ElemSimple ramaArch = elemGlass.root().mapFrame.get(LayoutArea.ARCH);
                            double R2 = ((AreaArch) iwin().rootArea).radiusArch - ramaArch.specificationRec.height + artiklRec.getFloat(eArtikl.height);
                            double L2 = iwin().rootArea.width() - ramaArch.specificationRec.height * 2 + artiklRec.getFloat(eArtikl.height) * 2 - koef;
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
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.width() + 2 * gzazo;
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (верхний/нижний)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.height() + 2 * gzazo;
                            specif.height = artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Уплотнитель
                    } else if (TypeArtikl.KONZEVPROF.id2 == artiklRec.getInt(eArtikl.level2)) { //уплотнитель
                        Record art = eArtikl.find(glasdetRec.getInt(eArtdet.artikl_id), false);
                        if (TypeElem.ARCH == elemGlass.owner().type()) { //если уплотнитель в арке
                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);

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
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
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
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.width() + 2 * gzazo;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.height() + 2 * gzazo;
                            specif.height = specif.artiklRec.getFloat(eArtikl.height);
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Всё остальное
                    } else {
                        Record art = eArtikl.find(glasdetRec.getInt(eArtdet.artikl_id), false);
                        if (TypeElem.AREA == elemGlass.owner().type()
                                || TypeElem.RECTANGL == elemGlass.owner().type()
                                || TypeElem.FULLSTVORKA == elemGlass.owner().type()) {

                            for (int index = 0; index < 4; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(elemGlass, glasdetRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else if (TypeElem.ARCH == elemGlass.owner().type()) {
                            for (int index = 0; index < 2; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(elemGlass, glasdetRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else {
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.setColor(elemGlass, glasdetRec);
                            elemGlass.addSpecifSubelem(specif);
                        }
                    }
                }
            }
        }
        return out;
    }

}
