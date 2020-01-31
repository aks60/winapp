package wincalc.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eSyscons;
import domain.eSysprof;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeElem;
import enums.TypeJoin;
import enums.TypeOpen;
import enums.TypeProfile;
import enums.VariantJoin;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import wincalc.Wincalc;

public class AreaStvorka extends AreaSimple {

    public String handleHeight = ""; //высота ручки
    protected TypeOpen typeOpen = TypeOpen.OM_INVALID; //тип открывания

    public AreaStvorka(Wincalc iwin, AreaSimple owner, String id, String param) {

        super(id);
        this.iwin = iwin;
        this.owner = owner;
        this.width = owner.width;
        this.height = owner.height;
        dimension(owner.x1, owner.y1, owner.x2, owner.y2);
        this.color1 = iwin.color1;
        this.color2 = iwin.color2;
        this.color3 = iwin.color3;
        if (param != null && param.isEmpty() == false) {
            String str = param.replace("'", "\"");
            Gson gson = new Gson();
            JsonElement jsonElem = gson.fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            mapParam.put(ParamJson.typeOpen, jsonObj.get(ParamJson.typeOpen.name()));
            mapParam.put(ParamJson.funic, jsonObj.get(ParamJson.funic.name()));
            if (mapParam.get(ParamJson.typeOpen) != null) {

                int key = Integer.valueOf(mapParam.get(ParamJson.typeOpen).toString());
                for (TypeOpen typeOpen : TypeOpen.values()) {
                    if (typeOpen.value == key) {
                        this.typeOpen = typeOpen;
                    }
                }
            }

        }
        initСonstructiv();
        parsing(param);
    }

    public void initСonstructiv() {

        sysprofRec = eSysprof.query.select().stream()
                .filter(rec -> rec.getInt(eSysprof.systree_id) == iwin.nuni
                && rec.getInt(eSysprof.types) == TypeProfile.STVORKA.value).findFirst().orElse(null);
        articlRec = eArtikl.query.select().stream()
                .filter(rec -> rec.getInt(eArtikl.id) == sysprofRec.getInt(eSysprof.artikl_id)).findFirst().orElse(null);
        if (articlRec.getFloat(eArtikl.size_falz) == 0) {
            articlRec.setNo(eArtikl.size_falz, iwin.articlRec.getDbl(eArtikl.size_falz)); //TODO наследование дордома Профстроя
        }
        specificationRec.setArticlRec(articlRec);
    }

    public void correction() {

        //Коррекция створки с учётом нахлёста
        ElemJoining ownerLeftTop = iwin.mapJoin.get(x1 + ":" + y1);
        ElemJoining ownerRightBott = iwin.mapJoin.get(x2 + ":" + y2);
        ElemSimple elemLeft = null, elemTop = null, elemBott = null, elemRight = null;

        //По умолчанию угловое на ус
        elemLeft = ownerLeftTop.joinElement1;
        elemTop = ownerLeftTop.joinElement2;
        elemBott = ownerRightBott.joinElement2;
        elemRight = ownerRightBott.joinElement1;

//        if (ownerLeftTop.varJoin == VariantJoin.VAR4) {
//            elemLeft = (ownerLeftTop.elemJoinTop == ownerLeftTop.elemJoinBottom) ? ownerLeftTop.joinElement2 : ownerLeftTop.joinElement1;
//            elemTop = (ownerLeftTop.elemJoinTop == ownerLeftTop.elemJoinBottom) ? ownerLeftTop.joinElement1 : ownerLeftTop.joinElement2;
//        }
//        if (ownerRightBott.varJoin == VariantJoin.VAR4) {
//            if (ownerRightBott.elemJoinTop == ownerRightBott.elemJoinBottom && ownerRightBott.elemJoinLeft != null && ownerRightBott.elemJoinRight == null) {
//                elemBott = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement1 : ownerRightBott.joinElement2;
//                elemRight = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement2 : ownerRightBott.joinElement1;
//            } else {
//                elemBott = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement2 : ownerRightBott.joinElement1;
//                elemRight = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement1 : ownerRightBott.joinElement2;
//            }
//        }
        
        
        Float naxl = iwin.sysconsRec.getFloat(eSyscons.naxl);
        Float size_falz =  articlRec.getFloat(eArtikl.size_falz); 
        x1 = elemLeft.x2 - size_falz - naxl;
        y1 = elemTop.y2 - size_falz - naxl;
        x2 = elemRight.x1 + size_falz + naxl;
        y2 = elemBott.y1 + size_falz + naxl;
        width = x2 - x1;
        height = y2 - y1;
        specificationRec.width = width;
        specificationRec.height = height;
        
        //Коррекция стеклопакета с учётом нахлёста створки
        ElemGlass elemGlass = null;
        for (Com5t com5t : listChild()) {
            if (TypeElem.GLASS == com5t.typeElem()) {
                elemGlass = (ElemGlass) com5t;
            }
        }
        elemGlass.x1 = x1;
        elemGlass.x2 = x2;
        elemGlass.y1 = y1;
        elemGlass.y2 = y2;
        elemGlass.width = width;
        elemGlass.height = height;
        elemGlass.specificationRec.width = width;
        elemGlass.specificationRec.height = height;

        //Добавим рамы створки
        addFrame(new ElemFrame(this, id + ".1a", LayoutArea.LEFT));
        addFrame(new ElemFrame(this, id + ".2a", LayoutArea.RIGHT));
        addFrame(new ElemFrame(this, id + ".3a", LayoutArea.TOP));
        addFrame(new ElemFrame(this, id + ".4a", LayoutArea.BOTTOM));

        for (Map.Entry<LayoutArea, ElemFrame> elem : mapFrame.entrySet()) {
            elem.getValue().anglCut1 = 45;
            elem.getValue().anglCut2 = 45;
        }

    }

    @Override
    public void joinFrame() {

        LinkedList<ElemSimple> listElem = root().listElem(root(), TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST); //список элементов
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin);
            el.id = id + "." + (index + 1) + "V";
            el.varJoin = VariantJoin.VAR2;
            el.cutAngl1 = 45;
            el.cutAngl2 = 45;

            if (index == 0) {
                el.name = "Угловое соединение левое верхнее";
                el.typeJoin = TypeJoin.LTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin.mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1), el);

            } else if (index == 1) {
                el.name = "Угловое соединение левое нижнее";
                el.typeJoin = TypeJoin.LBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin.mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y2), el);

            } else if (index == 2) {
                el.name = "Угловое соединение правое нижнее";
                el.typeJoin = TypeJoin.RBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin.mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y2), el);

            } else if (index == 3) {
                el.name = "Угловое соединение правое верхнее";
                el.typeJoin = TypeJoin.RTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin.mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1), el);
            }
        }
        
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin);
            el.id = id + "." + (index + 1) + "Z";
            el.varJoin = VariantJoin.VAR1;
            el.cutAngl1 = 0;
            el.cutAngl2 = 0;

            if (index == 0) {
                el.name = "Прилигающее верхнее";
                el.joinElement1 = mapFrame.get(LayoutArea.TOP);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside((x2 -x1) / 2, y1) == true).findFirst().orElse(null);
                iwin.mapJoin.put(String.valueOf(x1 + width / 2) + ":" + String.valueOf(y1 + 1), el);

            } else if (index == 1) {
                el.name = "Прилигающее нижнее";
                el.joinElement1 = mapFrame.get(LayoutArea.BOTTOM);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside((x2 -x1) / 2, y2) == true).findFirst().orElse(null);
                iwin.mapJoin.put(String.valueOf(x1 + width / 2) + ":" + String.valueOf(y2 - 1), el);               

            } else if (index == 2) {
                el.name = "Прилигающее левое";
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside(x1, (y2 -y1) / 2) == true).findFirst().orElse(null);
                iwin.mapJoin.put(String.valueOf(x1 + 1) + ":" + String.valueOf(y1 + height / 2), el);               

            } else if (index == 3) {
                el.name = "Прилигающее правое";
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside(x2, (y2 -y1) / 2) == true).findFirst().orElse(null);
                iwin.mapJoin.put(String.valueOf(x2 - 1) + ":" + String.valueOf(y1 + height / 2), el);              
            }
        }
    }

    @Override
    public void paint() {

        mapFrame.get(LayoutArea.TOP).paint();
        mapFrame.get(LayoutArea.BOTTOM).paint();
        mapFrame.get(LayoutArea.LEFT).paint();
        mapFrame.get(LayoutArea.RIGHT).paint();

        if (mapParam.get(ParamJson.typeOpen) != null) {
            float dx = 20, dy = 60, X1 = 0, Y1 = 0;
            String value = mapParam.get(ParamJson.typeOpen).toString();
            ElemSimple elemL = mapFrame.get(LayoutArea.LEFT);
            ElemSimple elemR = mapFrame.get(LayoutArea.RIGHT);
            ElemSimple elemT = mapFrame.get(LayoutArea.TOP);
            ElemSimple elemB = mapFrame.get(LayoutArea.BOTTOM);
            if (value.equals("1") || value.equals("3")) {
                X1 = elemR.x1 + (elemR.x2 - elemR.x1) / 2;
                Y1 = elemR.y1 + (elemR.y2 - elemR.y1) / 2;
                strokeLine(elemL.x1, elemL.y1, elemR.x2, elemR.y1 + (elemR.y2 - elemR.y1) / 2, Color.BLACK, 1);
                strokeLine(elemL.x1, elemL.y2, elemR.x2, elemR.y1 + (elemR.y2 - elemR.y1) / 2, Color.BLACK, 1);

            } else if (value.equals("2") || value.equals("4")) {
                X1 = elemL.x1 + (elemL.x2 - elemL.x1) / 2;
                Y1 = elemL.y1 + (elemL.y2 - elemL.y1) / 2;
                strokeLine(elemR.x2, elemR.y1, elemL.x1, elemL.y1 + (elemL.y2 - elemL.y1) / 2, Color.BLACK, 1);
                strokeLine(elemR.x2, elemR.y2, elemL.x1, elemL.y1 + (elemL.y2 - elemL.y1) / 2, Color.BLACK, 1);
            }
            if (value.equals("3") || value.equals("4")) {
                strokeLine(elemB.x1, elemB.y2, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1, Color.BLACK, 1);
                strokeLine(elemB.x2, elemB.y2, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1, Color.BLACK, 1);
            }
            strokePolygon(X1 - dx, X1 + dx, X1 + dx, X1 - dx, Y1 - dy, Y1 - dy, Y1 + dy, Y1 + dy, 0xFFFFFFFF, Color.BLACK, 2);
            dx = dx - 12;
            Y1 = Y1 + 20;
            strokePolygon(X1 - dx, X1 + dx, X1 + dx, X1 - dx, Y1 - dy, Y1 - dy, Y1 + dy, Y1 + dy, 0xFFFFFFFF, Color.BLACK, 2);
        }
    }

    public TypeOpen typeOpen() {
        return TypeOpen.OM_INVALID;
    }
    
    @Override
    public TypeElem typeElem() {
        return TypeElem.FULLSTVORKA;
    }
}
