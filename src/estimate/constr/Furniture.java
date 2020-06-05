package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnpar1;
import domain.eFurnpar2;
import domain.eFurnside1;
import domain.eFurnside2;
import domain.eSysfurn;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeArtikl;
import enums.TypeElem;
import java.util.LinkedList;
import java.util.List;
import estimate.Wincalc;
import estimate.constr.param.FurnitureDet;
import estimate.constr.param.FurnitureVar;
import estimate.model.AreaStvorka;
import estimate.model.ElemFrame;
import java.util.HashMap;

/**
 * Фурнитура
 */
public class Furniture extends Cal5e {

    private FurnitureVar furnitureVar = null;
    private FurnitureDet furnitureDet = null;
    public String sideCheck = ""; //TODO Эту переменную надо вынести в map параметров!!!
    public int pass = 1; //pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры

    public Furniture(Wincalc iwin) {
        super(iwin);
        furnitureVar = new FurnitureVar(iwin);
        furnitureDet = new FurnitureDet(iwin);
    }

    public void calc() {

        for (pass = 1; pass < 4; pass++) {

            //Цикл по створкам
            LinkedList<AreaStvorka> stvorkaList = root().listElem(TypeElem.STVORKA);
            outterLoop:
            for (AreaStvorka areaStvorka : stvorkaList) {

                //Подбор фурнитуры
                List<Record> sysfurnList = eSysfurn.find(iwin().nuni);
                Record sysfurnRec = sysfurnList.get(0);
                int funic = Integer.valueOf(areaStvorka.mapParamUse.get(ParamJson.funic).toString());
                sysfurnRec = sysfurnList.stream().filter(rec -> rec.getInt(eSysfurn.id) == funic).findFirst().orElse(sysfurnRec);//теперь sysfurnRec соответствует параметру полученному из i-okna

                //Подбор текстуры ручки створки
                Object colorHandl = areaStvorka.mapParamUse.get(ParamJson.colorHandl);
                if (colorHandl == null) { //если цвет не установлен подбираю по основной текстуре
                    areaStvorka.mapParamUse.put(ParamJson.colorHandl, iwin().color1);
                }
                if (sysfurnRec.getStr(eSysfurn.hand_pos).equalsIgnoreCase("по середине")) {
                    areaStvorka.handleHeight = "по середине";
                } else if (sysfurnRec.getStr(eSysfurn.hand_pos).equalsIgnoreCase("константная")) {
                    areaStvorka.handleHeight = "константная";
                } else if (sysfurnRec.getStr(eSysfurn.hand_pos).equalsIgnoreCase("вариационная")) {
                    areaStvorka.handleHeight = "установлена";
                }

                Record furnityreRec = eFurniture.find(sysfurnRec.getInt(eSysfurn.furniture_id)); //первая запись в списке конструктива
                ElemFrame elemFrame = areaStvorka.mapFrame.get((LayoutArea) LayoutArea.ANY.find(furnityreRec.getInt(eFurniture.hand_side))); //Крепится ручка
                List<Record> furnside1List = eFurnside1.find(furnityreRec.getInt(eFurniture.id));
                boolean out = true;

                //Цикл по описанию сторон фурнитуры
                for (Record furnside1Rec : furnside1List) {
                    List<Record> parfurlList = eFurnpar1.find(furnside1Rec.getInt(eFurnside1.id));
                    ElemFrame elemFrame2 = areaStvorka.mapFrame.get((LayoutArea) LayoutArea.ANY.find(furnside1Rec.getInt(eFurnside1.side_num)));
                    
                    out = furnitureVar.check(elemFrame2, parfurlList); //ФИЛЬТР вариантов
                    if (out == false) {
                        break;
                        //continue outterLoop; //проверить!!!
                    }
                }
                if (out == false) {
                    continue;
                }
                middle(elemFrame, furnityreRec, 1);
            }
        }
    }

    protected void middle(ElemFrame elemFrame, Record furnitureRec, int count) {

        //Цыкл по детализации (уровень 1)
        List<Record> furndetList = eFurndet.find(furnitureRec.getInt(eFurniture.id));
        for (Record furndetRec : furndetList) {
            if (furndetRec.getInt(eFurndet.id) == furndetRec.getInt(eFurndet.furndet_id)) {
                boolean level1 = detail(elemFrame, furndetRec, count);
                if (level1 == true) {

                    //Цыкл по детализации (уровень 2)
                    for (Record furndetRec2 : furndetList) {
                        if (furndetRec2.getInt(eFurndet.furndet_id) == furndetRec.getInt(eFurndet.id)) {
                            boolean level2 = detail(elemFrame, furndetRec2, count);
                            if (level2 == true) {

                                //Цыкл по детализации (уровень 3)
                                for (Record furndetRec3 : furndetList) {
                                    if (furndetRec3.getInt(eFurndet.furndet_id) == furndetRec2.getInt(eFurndet.id)) {
                                        boolean level3 = detail(elemFrame, furndetRec3, count);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean detail(ElemFrame elemFrame, Record furndetRec, int count) {
        if (furndetRec.getInt(eFurndet.artikl_id) == -1) {
            return false;
        }
        HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры element и specific

        //Подбор текстуры ручки
        if (furndetRec.getInt(eFurndet.isset) == 0) {
            Record artiklRec = eArtikl.find(furndetRec.getInt(eArtikl.id), false);
            if (artiklRec != null && TypeArtikl.FURNRUCHKA.isType(artiklRec)) {

                int colorHandl = (elemFrame.mapParamUse.get(ParamJson.colorHandl) == null) ? iwin().colorNone : Integer.valueOf(elemFrame.mapParamUse.get(ParamJson.colorHandl).toString());
                if (furndetRec.getInt(eFurndet.color_fk) > 0) {
                    boolean empty = true;
                    List<Record> artdetList = eArtdet.find(furndetRec.getInt(eFurndet.artikl_id));
                    for (Record artdetRec : artdetList) {
                        if (artdetRec.getInt(eArtdet.color_fk) == colorHandl) {
                            empty = false;
                        }
                    }
                    if (empty == true) {
                        return false;
                    }
                }
            }
        }
        //Цикл по ограничению сторон фурнитуры
        List<Record> furnside2List = eFurnside2.find(furndetRec.getInt(eFurndet.id));
        for (Record furnside2Rec : furnside2List) {

            ElemFrame el = null;
            int side = furnside2Rec.getInt(eFurnside2.side_num);
            if (side < 0) {
                String[] par = sideCheck.split("/");
                if (side == -1) {
                    side = (par[0].equals("*") == true) ? 99 : Integer.valueOf(par[0]);
                } else if (side == -2) {
                    side = (par[1].equals("*") == true) ? 99 : Integer.valueOf(par[1]);
                }
            }
            if (side == 1) {
                el = elemFrame.root().mapFrame.get(LayoutArea.BOTTOM);
            } else if (side == 2) {
                el = elemFrame.root().mapFrame.get(LayoutArea.RIGHT);
            } else if (side == 3) {
                el = elemFrame.root().mapFrame.get(LayoutArea.TOP);
            } else if (side == 4) {
                el = elemFrame.root().mapFrame.get(LayoutArea.LEFT);
            }

            float width = el.specificationRec.width - 2 * el.artiklRec.getFloat(eArtikl.size_falz);
            if (furnside2Rec.getInt(eFurnside2.len_max) < width || (furnside2Rec.getInt(eFurnside2.len_min) > width)) {
                return false;
            }
        }
        List<Record> parfursList = eFurnpar2.find(furndetRec.getInt(eFurndet.id)); //ФИЛЬТР детализации
        if (furnitureDet.check(hmParam, elemFrame, parfursList) == false) {
            return false; //параметры детализации
        }
        //Наборы
        if (furndetRec.getInt(eFurndet.isset) == 1) {
            int count2 = (hmParam.get(24030) == null) ? 1 : Integer.valueOf((hmParam.get(24030)));
            Record furnitureRec = eFurniture.find(furndetRec.getInt(eFurndet.color_fk));
            try {
                middle(elemFrame, furnitureRec, count2); //рекурсия обработки наборов
            } catch (Exception e) {
                System.err.println("Ошибка CalcConstructiv.fittingMidle() " + e);
            }

        } else if (pass == 2) {
            //Спецификация
            Record artiklRec = eArtikl.find(furndetRec.getInt(eFurndet.artikl_id), false);
            if (artiklRec.getStr(eArtikl.code).charAt(0) != '@') {

                Specification specif = new Specification(artiklRec, elemFrame, hmParam);
                specif.count = Integer.valueOf(specif.getParam(specif.count, 24030));
                specif.count = specif.count * count;
                //specif.setColor(this, areaStvorka, furndetRec);
                specif.place = "FURN";
                elemFrame.addSpecific(specif); //добавим спецификацию в элемент
            }
            return true;
        }
        return true;
    }
}
