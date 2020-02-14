
package wincalc.constr;

import wincalc.Wincalc;

public class SubAccessory extends CalcBase {
    
    public SubAccessory(Wincalc iwin) {
        super(iwin);
    }    
//
//    /**
//     * Фурнитура
//     */
//    public void fittingFirst() {
//        for (paramSpecific.pass = 1; paramSpecific.pass < 4; paramSpecific.pass++) {
//
//            LinkedList<AreaStvorka> elemStvorkaList = root.getElemList(TypeElem.FULLSTVORKA);
//            //цикл по створкам
//            for (AreaStvorka fullStvorka : elemStvorkaList) {
//
//                //Подбор фурнитуры
//                ArrayList<Syspros> sysprosList = Syspros.find(constr, nuni);
//                Syspros sysprosRec = sysprosList.get(0);
//                int funic = Integer.valueOf(fullStvorka.getHmParamJson().get(ParamJson.funic).toString());
//                if (funic != -1) {
//                    for (Syspros sysproaRec2 : sysprosList) {
//                        if (sysproaRec2.funic == funic) {
//                            sysprosRec = sysproaRec2; //теперь sysprosRec моответствует параметру полученному из i-okna
//                            break;
//                        }
//                    }
//                }
//                //Подбор текстуры ручки створки
//                Object colorHandl = fullStvorka.getHmParamJson().get(ParamJson.colorHandl);
//                if (colorHandl == null) { //если цвет не установлен подбираю по основной текстуре
//                    fullStvorka.getHmParamJson().put(ParamJson.colorHandl, root.getIwin().getColorProfile(1));
//                }
//
//                if (sysprosRec.nruch.equalsIgnoreCase("по середине"))
//                    fullStvorka.handleHeight = "по середине";
//                else if (sysprosRec.nruch.equalsIgnoreCase("константная"))
//                    fullStvorka.handleHeight = "константная";
//                else if (sysprosRec.nruch.equalsIgnoreCase("вариационная"))
//                    fullStvorka.handleHeight = "установлена";
//
//                Furnlst furnlstRec = Furnlst.find2(constr, sysprosRec.funic); //первая запись в списке конструктива
//                ArrayList<Furnlen> furnlenList = Furnlen.find(constr, furnlstRec.funic);
//                boolean out = true;
//                //Цикл по описанию сторон фурнитуры
//                for (Furnlen furnlenRec : furnlenList) {
//
//                    ArrayList<ITParam> parfurlList = Parfurl.find(constr, furnlenRec.fincr);
//                    out = paramVariant.checkParfurl(fullStvorka, parfurlList); //параметры вариантов
//                    if (out == false) break;
//                }
//                if (out == false) continue;
//
//                fittingMidle(fullStvorka, furnlstRec, 1);
//            }
//        }
//    }
//
//    /**
//     * Фурнитура
//     */
//    protected void fittingMidle(AreaStvorka fullStvorka, Furnlst furnlstRec, int count) {
//
//        ArrayList<Furnspc> furnspcList = Furnspc.find(constr, furnlstRec.funic);
//        for (Furnspc furnspcRec : furnspcList) {
//
//            if (furnspcRec.fleve == 1) {
//                boolean ret1 = fittingSecond(fullStvorka, furnspcRec, count);
//                if (ret1 == true) {
//
//                    for (Furnspc furnspcRec2 : furnspcList) {
//                        if (furnspcRec2.fleve == 2 && furnspcRec.fincb == furnspcRec2.fincs) {
//                            boolean ret2 = fittingSecond(fullStvorka, furnspcRec2, count);
//                            if (ret2 == true) {
//
//                                for (Furnspc furnspcRec3 : furnspcList) {
//                                    if (furnspcRec3.fleve == 3 && furnspcRec2.fincb == furnspcRec3.fincs) {
//                                        boolean ret3 = fittingSecond(fullStvorka, furnspcRec3, count);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    protected boolean fittingSecond(AreaStvorka elStvorka, Furnspc furnspcRec, int count) {
//
//        HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
//        //подбор текстуры ручки
//        if (furnspcRec.anumb.equals("НАБОР") == false) {
//            Artikls artiklRec = Artikls.get(constr, furnspcRec.anumb, false);
//            if (artiklRec != null && TypeArtikl.STVORKA_HANDL.isType(artiklRec)) {
//                int colorHandl = Integer.valueOf(elStvorka.getHmParamJson().get(ParamJson.colorHandl).toString());
//                if (furnspcRec.clnum > 0) {
//                    boolean empty = true;
//                    ArrayList<Artsvst> artsvstList = Artsvst.get(constr, furnspcRec.anumb);
//                    for (Artsvst artsvst : artsvstList) {
//                        if (artsvst.clcod == colorHandl) {
//                            empty = false;
//                        }
//                    }
//                    if (empty == true) return false;
//                }
//            }
//        }
//        ArrayList<Furnles> furnlesList = Furnles.find(constr, furnspcRec.fincb);
//        //Цикл по ограничению сторон фурнитуры
//        for (Furnles furnlesRec : furnlesList) {
//
//            ElemFrame el = null;
//            int side = furnlesRec.lnumb;
//            if (furnlesRec.lnumb < 0) {
//                String[] par = sideCheck.split("/");
//                if (furnlesRec.lnumb == -1) {
//                    side = (par[0].equals("*") == true) ? 99 : Integer.valueOf(par[0]);
//                } else if (furnlesRec.lnumb == -2) {
//                    side = (par[1].equals("*") == true) ? 99 : Integer.valueOf(par[1]);
//                }
//            }
//            if (side == 1) el = elStvorka.getRoot().getHmElemFrame().get(LayoutArea.BOTTOM);
//            else if (side == 2) el = elStvorka.getRoot().getHmElemFrame().get(LayoutArea.RIGHT);
//            else if (side == 3) el = elStvorka.getRoot().getHmElemFrame().get(LayoutArea.TOP);
//            else if (side == 4) el = elStvorka.getRoot().getHmElemFrame().get(LayoutArea.LEFT);
//
//            float width = el.getSpecificationRec().width - 2 * el.getArticlesRec().asizn;
//            if (furnlesRec.lmaxl < width || (furnlesRec.lminl > width)) return false;
//
//        }
//        //Фильтр параметров
//        ArrayList<ITParam> parfursList = Parfurs.find(constr, furnspcRec.fincb);
//        if (paramSpecific.checkParfurs(hmParam, elStvorka, parfursList) == false) return false; //параметры спецификаций
//
//        //Наборы
//        if ("НАБОР".equals(furnspcRec.anumb)) {
//            int count2 = (hmParam.get(24030) == null) ? 1 : Integer.valueOf((hmParam.get(24030)));
//            Furnlst furnlstRec2 = Furnlst.find2(constr, furnspcRec.clnum);
//            try {
//                fittingMidle(elStvorka, furnlstRec2, count2); //рекурсия обработки наборов
//            } catch (Exception e) {
//                System.out.println("Ошибка CalcConstructiv.fittingMidle() " + e);
//            }
//
//        } else if (paramSpecific.pass == 2) {
//            //Спецификация
//            Artikls artikl = Artikls.get(constr, furnspcRec.anumb, false);
//            if (artikl != null && furnspcRec.anumb.charAt(0) != '@') {
//
//                Specification specif = new Specification(artikl, elStvorka, hmParam);
//                specif.count = Integer.valueOf(specif.getHmParam(specif.count, 24030));
//                specif.count = specif.count * count;
//                specif.setColor(this, elStvorka, furnspcRec);
//                specif.element = "FURN";
//                elStvorka.addSpecifSubelem(specif); //добавим спецификацию в элемент
//            }
//            return true;
//        }
//        return true;
//    }    
}
