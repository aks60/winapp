
package wincalc.constr;

import wincalc.Wincalc;


public class SubFilling extends CalcBase {
    
    public SubFilling(Wincalc iwin) {
        super(iwin);
    }     
//    /**
//     * Заполнения
//     */
//    public void fillingFirst() {
//        for (paramSpecific.pass = 1; paramSpecific.pass < 4; paramSpecific.pass++) {
//
//            String artiklSysProf = null;
//            ArrayList<Sysproa> sysproaList = Sysproa.find(constr, nuni);
//            LinkedList<ElemGlass> elemGlassList = root.getElemList(TypeElem.GLASS);
//            //Цикл по стеклопакетам
//            for (ElemGlass elemGlass : elemGlassList) {
//
//                //Цикл по группам заполнений
//                for (Glasgrp glasgrpRec : constr.glasgrpList) {
//
//                    TypeProfile typeProf = (elemGlass.getOwner().getTypeElem() == TypeElem.FULLSTVORKA) ? TypeProfile.STVORKA : TypeProfile.FRAME;
//                    //Цикл по системе конструкций, ищем артикул системы профилей
//                    for (Sysproa sysproaRec : sysproaList) {
//                        if (typeProf.value == sysproaRec.atype) {
//
//                            Artikls artikls = Artikls.get(constr, sysproaRec.anumb, true); //запишем технологический код контейнера
//                            artiklSysProf = (artikls.amain != null && artikls.amain.isEmpty() == false) ? artikls.amain : artikls.anumb;
//                            break;
//                        }
//                    }
//
//                    ArrayList<Glaspro> glasproList = Glaspro.find(constr, glasgrpRec.gnumb);
//                    //Цикл по профилям в группе заполнений
//                    for (Glaspro glasproRec : glasproList) {
//
//                        if (artiklSysProf != null && artiklSysProf.equals(glasproRec.anumb) == true) { //если профиль есть в группе
//
//                            //artiklTech = Artikls.get(constr, glasproRec.anumb, false);
//                            elemGlass.getHmFieldVal().put("GZAZO", String.valueOf(glasgrpRec.gzazo));
//                            fillingSecond(elemGlass, glasgrpRec);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Заполнения
//     */
//    protected boolean fillingSecond(ElemGlass elemGlass, Glasgrp glasgrpRec) {
//
//        //TODO в заполненииях текстура подбирается неправильно
//        ArrayList<ITParam> pargrupList = Pargrup.find(constr, glasgrpRec.gnumb);
//        boolean out = paramVariant.checkPargrup(elemGlass, pargrupList); //ФИЛЬТР вариантов
//        if (out == true) {
//
//            if (paramSpecific.pass == 2) elemGlass.setSpecifElement(); //заполним спецификацию элемента
//            ArrayList<Glasart> glasartList = Glasart.find(constr, glasgrpRec.gnumb, elemGlass.getArticlesRec().afric);
//
//            //Цикл по списку спецификаций
//            for (Glasart glasartRec : glasartList) {
//
//                HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
//                ArrayList<ITParam> parglasList = Parglas.find(constr, glasartRec.gunic); //список параметров спецификации
//                if (paramSpecific.checkFilling(hmParam, elemGlass, parglasList) == true) { //ФИЛЬТР спец.
//
//                    if (paramSpecific.pass == 1 || paramSpecific.pass == 3) continue; //если нулевой и второй цыкл ничего не создаём
//
//                    Specification specif = null;
//                    Artikls artikl = Artikls.get(constr, glasartRec.anumb, true);
//                    float gzazo = Float.valueOf(elemGlass.getHmFieldVal().get("GZAZO"));
//                    Float overLength = (hmParam.get(15050) == null) ? 0.f : Float.valueOf(hmParam.get(15050).toString());
//
//                    //Стеклопакет
//                    if (TypeArtikl.GLASS.value2 == artikl.atypp) {
//
//                        //Штапик
//                    } else if (TypeArtikl.SHTAPIK.value2 == artikl.atypp) {
//
//                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
//                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {
//
//                            //По основанию арки
//                            specif = new Specification(art, elemGlass, hmParam);
//                            double dh2 = artikl.aheig - gzazo;
//
//                            double r1 = elemGlass.getRadiusGlass() - dh2;
//                            double h1 = elemGlass.getHeight() - 2 * dh2;
//                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний периметр
//                            double r2 = elemGlass.getRadiusGlass();
//                            double h2 = elemGlass.getHeight();
//                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
//                            double l3 = l2 - l1;
//                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
//
//                            double r5 = elemGlass.getRadiusGlass() + gzazo;
//                            double h5 = elemGlass.getHeight() + 2 * gzazo;
//                            double l5 = overLength + 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
//
//                            specif.width = (float) l5;
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = (float) ang;
//                            specif.anglCut1 = (float) ang;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию
//
//                            //По дуге арки
//                            specif = new Specification(art, elemGlass, hmParam);
//                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
//                            double ang3 = 90 - (90 - ang2 + ang);
//                            //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста
//                            double koef = 2;
//                            ElemBase ramaArch = elemGlass.getRoot().getHmElemFrame().get(LayoutArea.ARCH);
//                            double R2 = ((AreaArch) root).getRadiusArch() - ramaArch.getSpecificationRec().height + artikl.aheig;
//                            double L2 = root.getWidth() - ramaArch.getSpecificationRec().height * 2 + artikl.aheig * 2 - koef;
//                            double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
//                            double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
//                            double Z = 3 * gzazo;
//                            double R = elemGlass.getRadiusGlass();
//                            double L = elemGlass.getWidth();
//                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
//                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
//                            specif.width = (float) (overLength + M2);
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = (float) ang3;
//                            specif.anglCut1 = (float) ang3;
//
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию
//
//                        } else {
//                            //По горизонтали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getWidth() + 2 * gzazo;
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
//
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (верхний/нижний)
//                            //По вертикали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getHeight() + 2 * gzazo;
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
//                        }
//
//                        //Уплотнитель
//                    } else if (TypeArtikl.KONZEVPROF.value2 == artikl.atypp) { //уплотнитель
//                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
//                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) { //если уплотнитель в арке
//                            //По основанию арки
//                            specif = new Specification(art, elemGlass, hmParam);
//
//                            double dh2 = artikl.aheig - gzazo;
//                            double r1 = elemGlass.getRadiusGlass() - dh2;
//                            double h1 = elemGlass.getHeight() - 2 * dh2;
//                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
//                            double r2 = elemGlass.getRadiusGlass();
//                            double h2 = elemGlass.getHeight();
//                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
//                            double l3 = l2 - l1;
//                            double r5 = elemGlass.getRadiusGlass() + gzazo;
//                            double h5 = elemGlass.getHeight() + 2 * gzazo;
//                            double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
//                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
//                            specif.width = (float) l5;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = (float) ang;
//                            specif.anglCut1 = (float) ang;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
//
//                            //По дуге арки
//                            specif = new Specification(art, elemGlass, hmParam);
//                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
//                            double ang3 = 90 - (90 - ang2 + ang);
//                            double Z = 3 * gzazo;
//                            double R = elemGlass.getRadiusGlass();
//                            double L = elemGlass.getWidth();
//                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
//                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
//                            specif.width = (float) M;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = (float) ang3;
//                            specif.anglCut1 = (float) ang3;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
//                        } else {
//                            //По горизонтали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getWidth() + 2 * gzazo;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
//                            //По вертикали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getHeight() + 2 * gzazo;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
//                        }
//
//                        //Всё остальное
//                    } else {
//                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
//                        if (TypeElem.AREA == elemGlass.getOwner().getTypeElem()
//                                || TypeElem.SQUARE == elemGlass.getOwner().getTypeElem()
//                                || TypeElem.FULLSTVORKA == elemGlass.getOwner().getTypeElem()) {
//
//                            for (int index = 0; index < 4; index++) {
//                                specif = new Specification(art, elemGlass, hmParam);
//                                specif.setColor(this, elemGlass, glasartRec);
//                                elemGlass.addSpecifSubelem(specif);
//                            }
//                        } else if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {
//                            for (int index = 0; index < 2; index++) {
//                                specif = new Specification(art, elemGlass, hmParam);
//                                specif.setColor(this, elemGlass, glasartRec);
//                                elemGlass.addSpecifSubelem(specif);
//                            }
//                        } else {
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif);
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    protected boolean fillingSecond2(ElemGlass elemGlass, Glasgrp glasgrpRec) {
//
//        //TODO в заполненииях текстура подбирается неправильно
//        ArrayList<ITParam> pargrupList = Pargrup.find(constr, glasgrpRec.gnumb);
//        boolean out = paramVariant.checkPargrup(elemGlass, pargrupList); //ФИЛЬТР вариантов
//        if (out == true) {
//
//            elemGlass.setSpecifElement(); //заполним спецификацию элемента
//            ArrayList<Glasart> glasartList = Glasart.find(constr, glasgrpRec.gnumb, elemGlass.getArticlesRec().afric);
//            //Цикл по списку спецификаций
//            for (Glasart glasartRec : glasartList) {
//
//                Specification specif = null;
//                HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
//                Artikls artikl = Artikls.get(constr, glasartRec.anumb, true);
//                float gzazo = Float.valueOf(elemGlass.getHmFieldVal().get("GZAZO"));
//                ArrayList<ITParam> parglasList = Parglas.find(constr, glasartRec.gunic); //список параметров спецификации
//                out = paramSpecific.checkSpecific(hmParam, elemGlass, parglasList); //ФИЛЬТР спецификаций
//                Float overLength = (hmParam.get(15050) == null) ? 0.f : Float.valueOf(hmParam.get(15050).toString());
//                if (out == true) {
//                    //Стеклопакет
//                    if (TypeArtikl.GLASS.value2 == artikl.atypp) {
//
//                        //Штапик
//                    } else if (TypeArtikl.SHTAPIK.value2 == artikl.atypp) {
//
//                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
//                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {
//
//                            //По основанию арки
//                            specif = new Specification(art, elemGlass, hmParam);
//                            double dh2 = artikl.aheig - gzazo;
//
//                            double r1 = elemGlass.getRadiusGlass() - dh2;
//                            double h1 = elemGlass.getHeight() - 2 * dh2;
//                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний периметр
//                            double r2 = elemGlass.getRadiusGlass();
//                            double h2 = elemGlass.getHeight();
//                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
//                            double l3 = l2 - l1;
//                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
//
//                            double r5 = elemGlass.getRadiusGlass() + gzazo;
//                            double h5 = elemGlass.getHeight() + 2 * gzazo;
//                            double l5 = overLength + 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
//
//                            specif.width = (float) l5;
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = (float) ang;
//                            specif.anglCut1 = (float) ang;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию
//
//                            //По дуге арки
//                            specif = new Specification(art, elemGlass, hmParam);
//                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
//                            double ang3 = 90 - (90 - ang2 + ang);
//                            //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста
//                            double koef = 2;
//                            ElemBase ramaArch = elemGlass.getRoot().getHmElemFrame().get(LayoutArea.ARCH);
//                            double R2 = ((AreaArch) root).getRadiusArch() - ramaArch.getSpecificationRec().height + artikl.aheig;
//                            double L2 = root.getWidth() - ramaArch.getSpecificationRec().height * 2 + artikl.aheig * 2 - koef;
//                            double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
//                            double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
//                            double Z = 3 * gzazo;
//                            double R = elemGlass.getRadiusGlass();
//                            double L = elemGlass.getWidth();
//                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
//                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
//                            specif.width = (float) (overLength + M2);
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = (float) ang3;
//                            specif.anglCut1 = (float) ang3;
//
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию
//
//                        } else {
//                            //По горизонтали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getWidth() + 2 * gzazo;
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
//
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (верхний/нижний)
//                            //По вертикали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getHeight() + 2 * gzazo;
//                            specif.height = artikl.aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
//                        }
//
//                        //Уплотнитель
//                    } else if (TypeArtikl.KONZEVPROF.value2 == artikl.atypp) { //уплотнитель
//                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
//                        if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) { //если уплотнитель в арке
//                            //По основанию арки
//                            specif = new Specification(art, elemGlass, hmParam);
//
//                            double dh2 = artikl.aheig - gzazo;
//                            double r1 = elemGlass.getRadiusGlass() - dh2;
//                            double h1 = elemGlass.getHeight() - 2 * dh2;
//                            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
//                            double r2 = elemGlass.getRadiusGlass();
//                            double h2 = elemGlass.getHeight();
//                            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
//                            double l3 = l2 - l1;
//                            double r5 = elemGlass.getRadiusGlass() + gzazo;
//                            double h5 = elemGlass.getHeight() + 2 * gzazo;
//                            double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
//                            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
//                            specif.width = (float) l5;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = (float) ang;
//                            specif.anglCut1 = (float) ang;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
//
//                            //По дуге арки
//                            specif = new Specification(art, elemGlass, hmParam);
//                            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
//                            double ang3 = 90 - (90 - ang2 + ang);
//                            double Z = 3 * gzazo;
//                            double R = elemGlass.getRadiusGlass();
//                            double L = elemGlass.getWidth();
//                            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
//                            double M = ((R + Z) * 2) * Math.toRadians(ang5);
//                            specif.width = (float) M;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = (float) ang3;
//                            specif.anglCut1 = (float) ang3;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (верхний/нижний)
//                        } else {
//                            //По горизонтали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getWidth() + 2 * gzazo;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
//                            //По вертикали
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.width = elemGlass.getHeight() + 2 * gzazo;
//                            specif.height = specif.getArticRec().aheig;
//                            specif.anglCut2 = 45;
//                            specif.anglCut1 = 45;
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif); //добавим спецификацию в элемент (левый/правый)
//                            elemGlass.addSpecifSubelem(new Specification(specif)); //добавим спецификацию в элемент (левый/правый)
//                        }
//
//                        //Всё остальное
//                    } else {
//                        Artikls art = Artikls.get(constr, glasartRec.anumb, false);
//                        if (TypeElem.AREA == elemGlass.getOwner().getTypeElem()
//                                || TypeElem.SQUARE == elemGlass.getOwner().getTypeElem()
//                                || TypeElem.FULLSTVORKA == elemGlass.getOwner().getTypeElem()) {
//
//                            for (int index = 0; index < 4; index++) {
//                                specif = new Specification(art, elemGlass, hmParam);
//                                specif.setColor(this, elemGlass, glasartRec);
//                                elemGlass.addSpecifSubelem(specif);
//                            }
//                        } else if (TypeElem.ARCH == elemGlass.getOwner().getTypeElem()) {
//                            for (int index = 0; index < 2; index++) {
//                                specif = new Specification(art, elemGlass, hmParam);
//                                specif.setColor(this, elemGlass, glasartRec);
//                                elemGlass.addSpecifSubelem(specif);
//                            }
//                        } else {
//                            specif = new Specification(art, elemGlass, hmParam);
//                            specif.setColor(this, elemGlass, glasartRec);
//                            elemGlass.addSpecifSubelem(specif);
//                        }
//                        //if (specif != null)specif.element = "ЗАП";
//                    }
//                }
//            }
//        }
//        return out;
//    }
    
}
