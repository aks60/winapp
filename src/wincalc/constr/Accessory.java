package wincalc.constr;

import dataset.Record;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnpar1;
import domain.eFurnside1;
import domain.eSysfurn;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeArtikl1;
import enums.TypeElem;
import frame.Artikles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import wincalc.Wincalc;
import wincalc.model.AreaStvorka;
import wincalc.model.ElemFrame;
import static main.App1.eApp1.Artikles;

/**
 * Фурнитура
 */
public class Accessory extends Cal5e {

    public Accessory(Wincalc iwin, CalcConstructiv calcConstructiv) {
        super(iwin, calcConstructiv);
    }

    public void build() {
        for (calc().paramSpecific.pass = 1; calc().paramSpecific.pass < 4; calc().paramSpecific.pass++) {

            LinkedList<AreaStvorka> elemStvorkaList = root().listElem(TypeElem.FULLSTVORKA);
            //цикл по створкам
            for (AreaStvorka fullStvorka : elemStvorkaList) {

                //Подбор фурнитуры
                List<Record> sysfurnList = eSysfurn.find(iwin().nuni);
                Record sysfurnRec = sysfurnList.get(0);
                int funic = Integer.valueOf(fullStvorka.mapParam.get(ParamJson.funic).toString());
                if (funic != -1) {
                    for (Record sysproaRec2 : sysfurnList) {
                        if (sysproaRec2.getInt(eSysfurn.id) == funic) {
                            sysfurnRec = sysproaRec2; //теперь sysprosRec соответствует параметру полученному из i-okna
                            break;
                        }
                    }
                }
                //Подбор текстуры ручки створки
                Object colorHandl = fullStvorka.mapParam.get(ParamJson.colorHandl);
                if (colorHandl == null) { //если цвет не установлен подбираю по основной текстуре
                    fullStvorka.mapParam.put(ParamJson.colorHandl, iwin().color1);
                }

                if (sysfurnRec.getStr(eSysfurn.hand_pos).equalsIgnoreCase("по середине"))
                    fullStvorka.handleHeight = "по середине";
                else if (sysfurnRec.getStr(eSysfurn.hand_pos).equalsIgnoreCase("константная"))
                    fullStvorka.handleHeight = "константная";
                else if (sysfurnRec.getStr(eSysfurn.hand_pos).equalsIgnoreCase("вариационная"))
                    fullStvorka.handleHeight = "установлена";

                Record furnityreRec = eFurniture.find(sysfurnRec.getInt(eSysfurn.furniture_id)); //первая запись в списке конструктива
                List<Record> furnside1List = eFurnside1.find(furnityreRec.getInt(eFurniture.id));
                boolean out = true;
                //Цикл по описанию сторон фурнитуры
                for (Record furnside1Rec : furnside1List) {

                    List<Record> parfurlList = eFurnpar1.find(furnside1Rec.getInt(eFurnside1.id));
                    out = calc().paramVariant.checkParfurl(fullStvorka, parfurlList); //параметры вариантов
                    if (out == false) break;
                }
                if (out == false) continue;

                nested(fullStvorka, furnityreRec, 1);
            }
        }
    }

    protected void nested(AreaStvorka fullStvorka, Record furnitureRec, int count) {

        List<Record> furndetList = eFurndet.find(furnitureRec.getInt(eFurniture.id));
        for (Record furndetRec : furndetList) {
//            
//          ****  Закомментировал т.к. поле eFurndet.level удалил  ***
//
//            if (furndetRec.getInt(eFurndet.level) == 1) {
//                boolean ret1 = nested2(fullStvorka, furndetRec, count);
//                if (ret1 == true) {
//
//                    for (Record furndetRec2 : furndetList) {
//                        if (furndetRec2.getInt(eFurndet.level) == 2 && furndetRec.getInt(eFurndet.id) == furndetRec2.getInt(eFurndet.furndet_id)) {
//                            boolean ret2 = nested2(fullStvorka, furndetRec2, count);
//                            if (ret2 == true) {
//
//                                for (Record furndetRec3 : furndetList) {
//                                    if (furndetRec3.getInt(eFurndet.level) == 3 && furndetRec2.getInt(eFurndet.id) == furndetRec3.getInt(eFurndet.furndet_id)) {
//                                        boolean ret3 = nested2(fullStvorka, furndetRec3, count);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    protected boolean nested2(AreaStvorka elStvorka, Record furndetRec, int count) {

    /*    HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific
        //подбор текстуры ручки
        if (furndetRec.anumb.equals("НАБОР") == false) {
            Artikls artiklRec = Artikls.get(furndetRec.anumb, false);
            if (artiklRec != null && TypeArtikl.FURNRUCHKA.isType(artiklRec)) {
                int colorHandl = Integer.valueOf(elStvorka.mapParam.get(ParamJson.colorHandl).toString());
                if (furndetRec.clnum > 0) {
                    boolean empty = true;
                    ArrayList<Artsvst> artsvstList = Artsvst.get(furndetRec.anumb);
                    for (Artsvst artsvst : artsvstList) {
                        if (artsvst.clcod == colorHandl) {
                            empty = false;
                        }
                    }
                    if (empty == true) return false;
                }
            }
        }
        List<Record> furnlesList = Furnles.find(furndetRec.getInt(eFurndet.fincb));
        //Цикл по ограничению сторон фурнитуры
        for (Furnles furnlesRec : furnlesList) {

            ElemFrame el = null;
            int side = furnlesRec.lnumb;
            if (furnlesRec.lnumb < 0) {
                String[] par = sideCheck.split("/");
                if (furnlesRec.lnumb == -1) {
                    side = (par[0].equals("*") == true) ? 99 : Integer.valueOf(par[0]);
                } else if (furnlesRec.lnumb == -2) {
                    side = (par[1].equals("*") == true) ? 99 : Integer.valueOf(par[1]);
                }
            }
            if (side == 1) el = elStvorka.root().mapFrame.get(LayoutArea.BOTTOM);
            else if (side == 2) el = elStvorka.root().mapFrame.get(LayoutArea.RIGHT);
            else if (side == 3) el = elStvorka.root().mapFrame.get(LayoutArea.TOP);
            else if (side == 4) el = elStvorka.root().mapFrame.get(LayoutArea.LEFT);

            float width = el.getSpecificationRec().width - 2 * el.artiklRec.asizn;
            if (furnlesRec.lmaxl < width || (furnlesRec.lminl > width)) return false;

        }
        //Фильтр параметров
        List<Record> parfursList = Parfurs.find(furndetRec.getInt(eFurndet.fincb));
        if (calc.paramSpecific.checkParfurs(hmParam, elStvorka, parfursList) == false) return false; //параметры спецификаций

        //Наборы
        if ("НАБОР".equals(furndetRec.anumb)) {
            int count2 = (hmParam.get(24030) == null) ? 1 : Integer.valueOf((hmParam.get(24030)));
            Furnlst furnlstRec2 = Furnlst.find2(furndetRec.clnum);
            try {
                fittingMidle(elStvorka, furnlstRec2, count2); //рекурсия обработки наборов
            } catch (Exception e) {
                System.err.println("Ошибка CalcConstructiv.fittingMidle() " + e);
            }

        } else if (calc.paramSpecific.pass == 2) {
            //Спецификация
            Artikls artikl = Artikls.get(furndetRec.anumb, false);
            if (artikl != null && furndetRec.anumb.charAt(0) != '@') {

                Specification specif = new Specification(artikl, elStvorka, hmParam);
                specif.count = Integer.valueOf(specif.mapParam(specif.count, 24030));
                specif.count = specif.count * count;
                specif.setColor(this, elStvorka, furndetRec);
                specif.element = "FURN";
                elStvorka.addSpecifSubelem(specif); //добавим спецификацию в элемент
            }
            return true;
        }*/
        return true;
    }    
}
