package wincalc.constr;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlasprof;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.TypeProfile;
import forms.Artikls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import static main.App1.eApp1.Artikls;
import wincalc.Wincalc;
import wincalc.model.AreaArch;
import wincalc.model.ElemGlass;

/**
 * Заполнения
 */
public class Filling extends Cal5e {

    public Filling(Wincalc iwin, CalcConstructiv calcConstructiv) {
        super(iwin, calcConstructiv);
    }

    public void fillingFirst() {
        for (calc.paramSpecific.pass = 1; calc.paramSpecific.pass < 4; calc.paramSpecific.pass++) {

            int systree_artikl_id = -1;
            List<Record> sysprofList = eSysprof.find(iwin.nuni);
            LinkedList<ElemGlass> elemGlassList = iwin.rootArea.listElem(TypeElem.GLASS);
            
            //Цикл по стеклопакетам
            for (ElemGlass elemGlass : elemGlassList) {

                //Цикл по группам заполнений
                for (Record glasgrpRec : eGlasgrp.select()) {

                    TypeProfile typeProf = (elemGlass.owner.typeElem() == TypeElem.FULLSTVORKA) ? TypeProfile.STVORKA : TypeProfile.FRAME;
                    //Цикл по системе конструкций, ищем артикул системы профилей
                    for (Record sysprofRec : sysprofList) {
                        if (typeProf.value == sysprofRec.getInt(eSysprof.types)) {

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
                            fillingSecond(elemGlass, glasgrpRec);
                        }
                    }
                }
            }
        }
    }

    /**
     * Заполнения
     */
    protected boolean fillingSecond(ElemGlass elemGlass, Record glasgrpRec) {

        //TODO в заполненииях текстура подбирается неправильно
        ArrayList<Record> pargrupList = eGlaspar1.find(glasgrpRec.getInt(eGlasgrp.id));
        boolean out = calc.paramVariant.checkPargrup(elemGlass, pargrupList); //ФИЛЬТР вариантов
        if (out == true) {

            if (calc.paramSpecific.pass == 2) {
                elemGlass.setSpecifElement(); //заполним спецификацию элемента
            }
            ArrayList<Glasart> glasartList = Glasart.find(constr, glasgrpRec.gnumb, elemGlass.getArticlesRec().afric);

            //Цикл по списку спецификаций
            for (Glasart glasartRec : glasartList) {

                HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
                ArrayList<ITParam> parglasList = Parglas.find(constr, glasartRec.gunic); //список параметров спецификации
                if (paramSpecific.checkFilling(hmParam, elemGlass, parglasList) == true) { //ФИЛЬТР спец.

                    if (paramSpecific.pass == 1 || paramSpecific.pass == 3) {
                        continue; //если нулевой и второй цыкл ничего не создаём
                    }
                    Specification specif = null;
                    Artikls artikl = Artikls.get(constr, glasartRec.anumb, true);
                    float gzazo = Float.valueOf(elemGlass.getHmFieldVal().get("GZAZO"));
                    Float overLength = (hmParam.get(15050) == null) ? 0.f : Float.valueOf(hmParam.get(15050).toString());

                    //Стеклопакет
                    if (TypeArtikl.GLASS.value2 == artikl.atypp) {

                        //Штапик
                    } else if (TypeArtikl.SHTAPIK.value2 == artikl.atypp) {

                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {

                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double dh2 = artikl.aheig - gzazo;

                            double r1 = elemGlass.getRadiusGlass() - dh2;
                            double h1 = elemGlass.getHeight() - 2 * dh2;
                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний периметр
                            double r2 = elemGlass.getRadiusGlass();
                            double h2 = elemGlass.getHeight();
                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                            double l3 = l2 - l1;
                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза

                            double r5 = elemGlass.getRadiusGlass() + gzazo;
                            double h5 = elemGlass.getHeight() + 2 * gzazo;
                            double l5 = overLength + 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда

                            specif.width = (float) l5;
                            specif.height = artikl.aheig;
                            specif.anglCut2 = (float) ang;
                            specif.anglCut1 = (float) ang;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                            double ang3 = 90 - (90 - ang2 + ang);
                            //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста
                            double koef = 2;
                            ElemBase ramaArch = elemGlass.getRoot().getHmElemFrame().get(LayoutArea.ARCH);
                            double R2 = ((AreaArch) root).getRadiusArch() - ramaArch.getSpecificationRec().height + artikl.aheig;
                            double L2 = root.getWidth() - ramaArch.getSpecificationRec().height * 2 + artikl.aheig * 2 - koef;
                            double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
                            double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
                            double Z = 3 * gzazo;
                            double R = elemGlass.getRadiusGlass();
                            double L = elemGlass.getWidth();
                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
                            specif.width = (float) (overLength + M2);
                            specif.height = artikl.aheig;
                            specif.anglCut2 = (float) ang3;
                            specif.anglCut1 = (float) ang3;

                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getWidth() + 2 * gzazo;
                            specif.height = artikl.aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (верхний/нижний)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getHeight() + 2 * gzazo;
                            specif.height = artikl.aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Уплотнитель
                    } else if (TypeArtikl.KONZEVPROF.value2 == artikl.atypp) { //уплотнитель
                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) { //если уплотнитель в арке
                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);

                            double dh2 = artikl.aheig - gzazo;
                            double r1 = elemGlass.getRadiusGlass() - dh2;
                            double h1 = elemGlass.getHeight() - 2 * dh2;
                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
                            double r2 = elemGlass.getRadiusGlass();
                            double h2 = elemGlass.getHeight();
                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                            double l3 = l2 - l1;
                            double r5 = elemGlass.getRadiusGlass() + gzazo;
                            double h5 = elemGlass.getHeight() + 2 * gzazo;
                            double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
                            specif.width = (float) l5;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = (float) ang;
                            specif.anglCut1 = (float) ang;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                            double ang3 = 90 - (90 - ang2 + ang);
                            double Z = 3 * gzazo;
                            double R = elemGlass.getRadiusGlass();
                            double L = elemGlass.getWidth();
                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
                            specif.width = (float) M;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = (float) ang3;
                            specif.anglCut1 = (float) ang3;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getWidth() + 2 * gzazo;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getHeight() + 2 * gzazo;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Всё остальное
                    } else {
                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
                        if (TypeElem.AREA == elemGlass.getOwner().getTypeElem()
                                || TypeElem.SQUARE == elemGlass.getOwner().getTypeElem()
                                || TypeElem.FULLSTVORKA == elemGlass.getOwner().getTypeElem()) {

                            for (int index = 0; index < 4; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(this, elemGlass, glasartRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {
                            for (int index = 0; index < 2; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(this, elemGlass, glasartRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else {
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif);
                        }
                    }
                }
            }
        }
        return true;
    }

    protected boolean fillingSecond2(ElemGlass elemGlass, Glasgrp glasgrpRec) {

        //TODO в заполненииях текстура подбирается неправильно
        ArrayList<ITParam> pargrupList = Pargrup.find(constr, glasgrpRec.gnumb);
        boolean out = paramVariant.checkPargrup(elemGlass, pargrupList); //ФИЛЬТР вариантов
        if (out == true) {

            elemGlass.setSpecifElement(); //заполним спецификацию элемента
            ArrayList<Glasart> glasartList = Glasart.find(constr, glasgrpRec.gnumb, elemGlass.getArticlesRec().afric);
            //Цикл по списку спецификаций
            for (Glasart glasartRec : glasartList) {

                Specification specif = null;
                HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
                Artikls artikl = Artikls.get(constr, glasartRec.anumb, true);
                float gzazo = Float.valueOf(elemGlass.getHmFieldVal().get("GZAZO"));
                ArrayList<ITParam> parglasList = Parglas.find(constr, glasartRec.gunic); //список параметров спецификации
                out = paramSpecific.checkSpecific(hmParam, elemGlass, parglasList); //ФИЛЬТР спецификаций
                Float overLength = (hmParam.get(15050) == null) ? 0.f : Float.valueOf(hmParam.get(15050).toString());
                if (out == true) {
                    //Стеклопакет
                    if (TypeArtikl.GLASS.value2 == artikl.atypp) {

                        //Штапик
                    } else if (TypeArtikl.SHTAPIK.value2 == artikl.atypp) {

                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {

                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double dh2 = artikl.aheig - gzazo;

                            double r1 = elemGlass.getRadiusGlass() - dh2;
                            double h1 = elemGlass.getHeight() - 2 * dh2;
                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний периметр
                            double r2 = elemGlass.getRadiusGlass();
                            double h2 = elemGlass.getHeight();
                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                            double l3 = l2 - l1;
                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза

                            double r5 = elemGlass.getRadiusGlass() + gzazo;
                            double h5 = elemGlass.getHeight() + 2 * gzazo;
                            double l5 = overLength + 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда

                            specif.width = (float) l5;
                            specif.height = artikl.aheig;
                            specif.anglCut2 = (float) ang;
                            specif.anglCut1 = (float) ang;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                            double ang3 = 90 - (90 - ang2 + ang);
                            //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста
                            double koef = 2;
                            ElemBase ramaArch = elemGlass.getRoot().getHmElemFrame().get(LayoutArea.ARCH);
                            double R2 = ((AreaArch) root).getRadiusArch() - ramaArch.getSpecificationRec().height + artikl.aheig;
                            double L2 = root.getWidth() - ramaArch.getSpecificationRec().height * 2 + artikl.aheig * 2 - koef;
                            double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
                            double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
                            double Z = 3 * gzazo;
                            double R = elemGlass.getRadiusGlass();
                            double L = elemGlass.getWidth();
                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
                            specif.width = (float) (overLength + M2);
                            specif.height = artikl.aheig;
                            specif.anglCut2 = (float) ang3;
                            specif.anglCut1 = (float) ang3;

                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию

                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getWidth() + 2 * gzazo;
                            specif.height = artikl.aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (верхний/нижний)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getHeight() + 2 * gzazo;
                            specif.height = artikl.aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Уплотнитель
                    } else if (TypeArtikl.KONZEVPROF.value2 == artikl.atypp) { //уплотнитель
                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) { //если уплотнитель в арке
                            //По основанию арки
                            specif = new Specification(art, elemGlass, hmParam);

                            double dh2 = artikl.aheig - gzazo;
                            double r1 = elemGlass.getRadiusGlass() - dh2;
                            double h1 = elemGlass.getHeight() - 2 * dh2;
                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
                            double r2 = elemGlass.getRadiusGlass();
                            double h2 = elemGlass.getHeight();
                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                            double l3 = l2 - l1;
                            double r5 = elemGlass.getRadiusGlass() + gzazo;
                            double h5 = elemGlass.getHeight() + 2 * gzazo;
                            double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
                            specif.width = (float) l5;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = (float) ang;
                            specif.anglCut1 = (float) ang;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)

                            //По дуге арки
                            specif = new Specification(art, elemGlass, hmParam);
                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                            double ang3 = 90 - (90 - ang2 + ang);
                            double Z = 3 * gzazo;
                            double R = elemGlass.getRadiusGlass();
                            double L = elemGlass.getWidth();
                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
                            specif.width = (float) M;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = (float) ang3;
                            specif.anglCut1 = (float) ang3;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
                        } else {
                            //По горизонтали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getWidth() + 2 * gzazo;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                            //По вертикали
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.width = elemGlass.getHeight() + 2 * gzazo;
                            specif.height = specif.getArticRec().aheig;
                            specif.anglCut2 = 45;
                            specif.anglCut1 = 45;
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
                        }

                        //Всё остальное
                    } else {
                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
                        if (TypeElem.AREA == elemGlass.getOwner().getTypeElem()
                                || TypeElem.SQUARE == elemGlass.getOwner().getTypeElem()
                                || TypeElem.FULLSTVORKA == elemGlass.getOwner().getTypeElem()) {

                            for (int index = 0; index < 4; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(this, elemGlass, glasartRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {
                            for (int index = 0; index < 2; index++) {
                                specif = new Specification(art, elemGlass, hmParam);
                                specif.setColor(this, elemGlass, glasartRec);
                                elemGlass.addSpecifSubelem(specif);
                            }
                        } else {
                            specif = new Specification(art, elemGlass, hmParam);
                            specif.setColor(this, elemGlass, glasartRec);
                            elemGlass.addSpecifSubelem(specif);
                        }
                        //if (specif != null)specif.element = "ЗАП";
                    }
                }
            }
        }
        return out;
    }

}
