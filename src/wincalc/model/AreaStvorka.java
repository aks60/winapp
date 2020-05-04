package wincalc.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eSyssize;
import domain.eSysprof;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeOpen1;
import enums.UseArtikl;
import enums.TypeJoin;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import wincalc.Wincalc;

public class AreaStvorka extends AreaSimple {

    public String handleHeight = ""; //высота ручки
    protected TypeOpen1 typeOpen = TypeOpen1.OM_INVALID; //тип открывания

    public AreaStvorka(Wincalc iwin, AreaSimple owner, float id, String param) {
        super(iwin, owner, id, TypeElem.FULLSTVORKA, LayoutArea.VERT, (owner.x2 - owner.x1), (owner.y2 - owner.y1), iwin.color1, iwin.color2, iwin.color3);

        if (param != null && param.isEmpty() == false) {
            String str = param.replace("'", "\"");
            Gson gson = new Gson();
            JsonElement jsonElem = gson.fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            mapParam.put(ParamJson.typeOpen, jsonObj.get(ParamJson.typeOpen.name()));
            mapParam.put(ParamJson.funic, jsonObj.get(ParamJson.funic.name()));
            if (mapParam.get(ParamJson.typeOpen) != null) {

                int key = Integer.valueOf(mapParam.get(ParamJson.typeOpen).toString());
                for (TypeOpen1 typeOpen : TypeOpen1.values()) {
                    if (typeOpen.id == key) {
                        this.typeOpen = typeOpen;
                    }
                }
            }
        }
        initСonstructiv();

        //Коррекция створки с учётом нахлёста
        LinkedList<ElemSimple> listElem = root().listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST); //список элементов
        ElemSimple insideLeft = listElem.stream().filter(el -> el.inside(x1, y1 + (y2 - y1) / 2) == true).findFirst().orElse(null),
                insideTop = listElem.stream().filter(el -> el.inside(x1 + (x2 - x1) / 2, y1) == true).findFirst().orElse(null),
                insideBott = listElem.stream().filter(el -> el.inside(x1 + (x2 - x1) / 2, y2) == true).findFirst().orElse(null),
                insideRight = listElem.stream().filter(el -> el.inside(x2, y1 + (y2 - y1) / 2) == true).findFirst().orElse(null);

        Float naxl = iwin.sysconsRec.getFloat(eSyssize.naxl);
        Float size_falz = artiklRec.getFloat(eArtikl.size_falz);
        x1 = insideLeft.x2 - size_falz - naxl;
        y1 = insideTop.y2 - size_falz - naxl;
        x2 = insideRight.x1 + size_falz + naxl;
        y2 = insideBott.y1 + size_falz + naxl;
        specificationRec.width = width();
        specificationRec.height = height();

        //Добавим рамы створки        
        addFrame(new ElemFrame(this, id + .1f, LayoutArea.BOTTOM));
        addFrame(new ElemFrame(this, id + .2f, LayoutArea.RIGHT));
        addFrame(new ElemFrame(this, id + .3f, LayoutArea.TOP));
        addFrame(new ElemFrame(this, id + .4f, LayoutArea.LEFT));

        parsing(param);
    }

    public void initСonstructiv() {

        sysprofRec = eSysprof.find2(iwin().nuni, UseArtikl.STVORKA);
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        if (artiklRec.getFloat(eArtikl.size_falz) == 0) {
            
            artiklRec.setNo(eArtikl.size_falz, iwin().artiklRec.getDbl(eArtikl.size_falz)); //TODO наследование дордома Профстроя
        }
        specificationRec.setArtiklRec(artiklRec);
    }

    @Override
    public void joinFrame() {

        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.id = id() + (index + 1);
            el.varJoin = TypeJoin.VAR2;
            el.cutAngl1 = 45;
            el.cutAngl2 = 45;

            if (index == 0) {
                el.name = "Угловое соединение левое верхнее";
                el.typeJoin = LayoutJoin.LTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1), el);

            } else if (index == 1) {
                el.name = "Угловое соединение левое нижнее";
                el.typeJoin = LayoutJoin.LBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y2), el);

            } else if (index == 2) {
                el.name = "Угловое соединение правое нижнее";
                el.typeJoin = LayoutJoin.RBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y2), el);

            } else if (index == 3) {
                el.name = "Угловое соединение правое верхнее";
                el.typeJoin = LayoutJoin.RTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1), el);
            }
        }

        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.id = id() + (index + 1);
            el.varJoin = TypeJoin.VAR1;
            el.cutAngl1 = 0;
            el.cutAngl2 = 0;

            if (index == 0) {
                el.name = "Прилигающее верхнее";
                el.joinElement1 = mapFrame.get(LayoutArea.TOP);
                el.joinElement2 = iwin().listElem.stream().filter(el2 -> el2.inside((x2 - x1) / 2, y1) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1 + width() / 2) + ":" + String.valueOf(y1 + 1), el);

            } else if (index == 1) {
                el.name = "Прилигающее нижнее";
                el.joinElement1 = mapFrame.get(LayoutArea.BOTTOM);
                el.joinElement2 = iwin().listElem.stream().filter(el2 -> el2.inside((x2 - x1) / 2, y2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1 + width() / 2) + ":" + String.valueOf(y2 - 1), el);

            } else if (index == 2) {
                el.name = "Прилигающее левое";
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = iwin().listElem.stream().filter(el2 -> el2.inside(x1, (y2 - y1) / 2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1 + 1) + ":" + String.valueOf(y1 + height() / 2), el);

            } else if (index == 3) {
                el.name = "Прилигающее правое";
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = iwin().listElem.stream().filter(el2 -> el2.inside(x2, (y2 - y1) / 2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x2 - 1) + ":" + String.valueOf(y1 + height() / 2), el);
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
            float DX = 20, DY = 60, X1 = 0, Y1 = 0;
            String value = mapParam.get(ParamJson.typeOpen).toString();
            ElemSimple elemL = mapFrame.get(LayoutArea.LEFT);
            ElemSimple elemR = mapFrame.get(LayoutArea.RIGHT);
            ElemSimple elemT = mapFrame.get(LayoutArea.TOP);
            ElemSimple elemB = mapFrame.get(LayoutArea.BOTTOM);

            float dy = iwin().heightAdd - iwin().height;

            if (value.equals("1") || value.equals("3")) {
                X1 = elemR.x1 + (elemR.x2 - elemR.x1) / 2;
                Y1 = elemR.y1 + (elemR.y2 - elemR.y1) / 2;
                drawLine(elemL.x1, elemL.y1 + dy, elemR.x2, elemR.y1 + dy + (elemR.y2 - elemR.y1) / 2);
                drawLine(elemL.x1, elemL.y2 + dy, elemR.x2, elemR.y1 + dy + (elemR.y2 - elemR.y1) / 2);

            } else if (value.equals("2") || value.equals("4")) {
                X1 = elemL.x1 + (elemL.x2 - elemL.x1) / 2;
                Y1 = elemL.y1 + (elemL.y2 - elemL.y1) / 2;
                drawLine(elemR.x2, elemR.y1 + dy, elemL.x1, elemL.y1 + dy + (elemL.y2 - elemL.y1) / 2);
                drawLine(elemR.x2, elemR.y2 + dy, elemL.x1, elemL.y1 + dy + (elemL.y2 - elemL.y1) / 2);
            }
            if (value.equals("3") || value.equals("4")) {
                drawLine(elemB.x1, elemB.y2 + dy, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1 + dy);
                drawLine(elemB.x2, elemB.y2 + dy, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1 + dy);
            }
            strokePolygon(X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
            DX = DX - 12;
            Y1 = Y1 + 20;
            strokePolygon(X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
        }
    }

    public TypeOpen1 typeOpen() {
        return TypeOpen1.OM_INVALID;
    }
}
