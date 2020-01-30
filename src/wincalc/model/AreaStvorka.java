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
import enums.TypeOpen;
import enums.TypeProfile;
import enums.VariantJoin;
import java.awt.Color;
import java.util.HashMap;
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
            articlRec.setNo(eArtikl.size_falz, iwin.articlesRec.getDbl(eArtikl.size_falz)); //TODO наследование дордома Профстроя
        }
        specificationRec.setArticlRec(articlRec);
    }

    public void setCorrection() {

        //Коррекция створки с учётом нахлёста
        ElemJoining ownerLeftTop = iwin.mapJoin.get(x1 + ":" + y1);
        ElemJoining ownerRightBott = iwin.mapJoin.get(x2 + ":" + y2);
        ElemSimple elemLeft = null, elemTop = null, elemBott = null, elemRight = null;
        
        //По умолчанию угловое на ус
        elemLeft = ownerLeftTop.joinElement1;
        elemTop = ownerLeftTop.joinElement2;
        elemBott = ownerRightBott.joinElement2;
        elemRight = ownerRightBott.joinElement1;

        if (ownerLeftTop.varJoin == VariantJoin.VAR4) {
            elemLeft = (ownerLeftTop.elemJoinTop == ownerLeftTop.elemJoinBottom) ? ownerLeftTop.joinElement2 : ownerLeftTop.joinElement1;
            elemTop = (ownerLeftTop.elemJoinTop == ownerLeftTop.elemJoinBottom) ? ownerLeftTop.joinElement1 : ownerLeftTop.joinElement2;
        }
        if (ownerRightBott.varJoin == VariantJoin.VAR4) {
            if (ownerRightBott.elemJoinTop == ownerRightBott.elemJoinBottom && ownerRightBott.elemJoinLeft != null && ownerRightBott.elemJoinRight == null) {
                elemBott = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement1 : ownerRightBott.joinElement2;
                elemRight = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement2 : ownerRightBott.joinElement1;
            } else {
                elemBott = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement2 : ownerRightBott.joinElement1;
                elemRight = (ownerRightBott.elemJoinLeft == ownerRightBott.elemJoinLeft) ? ownerRightBott.joinElement1 : ownerRightBott.joinElement2;
            }
        }
        Float naxl = iwin.sysconsRec.getFloat(eSyscons.naxl);
        Float napl = elemLeft.articlRec.getFloat(eSyscons.napl);
        x1 = elemLeft.x2 - napl - naxl;
        y1 = elemTop.y2 - napl - naxl;
        x2 = elemRight.x1 + napl + naxl;
        y2 = elemBott.y1 + napl + naxl;
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
        addFrame(new ElemFrame(this, "11", LayoutArea.LEFT));
        addFrame(new ElemFrame(this, "12", LayoutArea.RIGHT));
        addFrame(new ElemFrame(this, "13", LayoutArea.TOP));
        addFrame(new ElemFrame(this, "14", LayoutArea.BOTTOM));
        
        for (Map.Entry<LayoutArea, ElemFrame> elem : mapFrame.entrySet()) {
            elem.getValue().anglCut1 = 45;
            elem.getValue().anglCut2 = 45;
        }

    }
    
    public TypeOpen typeOpen() {
        return TypeOpen.OM_INVALID;
    }

    /*public void joinElements(HashMap<String, ElemJoining> mapJoin) {
        
        super.joinElements(mapJoin);
        
        //Прилигающее верхнее
        ElemJoining elemJoin_top = new ElemJoining(iwin);
        elemJoin_top.id = id;
        elemJoin_top.elemJoinTop = mapFrame.get(LayoutArea.TOP);
        elemJoin_top.elemJoinBottom = mapFrame.get(LayoutArea.TOP);
        elemJoin_top.joinElement1 = mapFrame.get(LayoutArea.TOP);
        elemJoin_top.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.TOP) : owner.adjoinedElem(LayoutArea.TOP);
        elemJoin_top.cutAngl1 = 0;
        elemJoin_top.cutAngl2 = 0;
        elemJoin_top.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x1 + width / 2) + ":" + String.valueOf(y1 + 1), elemJoin_top);

        //Прилигающее нижнее
        ElemJoining elemJoin_bottom = new ElemJoining(iwin);
        elemJoin_bottom.id = id;
        elemJoin_bottom.elemJoinTop = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin_bottom.elemJoinBottom = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin_bottom.joinElement1 = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin_bottom.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.BOTTOM) : owner.adjoinedElem(LayoutArea.BOTTOM);
        elemJoin_bottom.cutAngl1 = 0;
        elemJoin_bottom.cutAngl2 = 0;
        elemJoin_bottom.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x1 + width / 2) + ":" + String.valueOf(y2 - 1), elemJoin_bottom);

        //Прилигающее левое
        ElemJoining elemJoin_left = new ElemJoining(iwin);
        elemJoin_left.id = id;
        elemJoin_left.elemJoinLeft = mapFrame.get(LayoutArea.LEFT);
        elemJoin_left.elemJoinRight = mapFrame.get(LayoutArea.LEFT);
        elemJoin_left.joinElement1 = mapFrame.get(LayoutArea.LEFT);
        elemJoin_left.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.LEFT) : owner.adjoinedElem(LayoutArea.LEFT);
        elemJoin_left.cutAngl1 = 0;
        elemJoin_left.cutAngl2 = 0;
        elemJoin_left.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x1 + 1) + ":" + String.valueOf(y1 + height / 2), elemJoin_left);

        //Прилигающее правое
        ElemJoining elemJoin_right = new ElemJoining(iwin);
        elemJoin_right.id = id;
        elemJoin_right.elemJoinLeft = mapFrame.get(LayoutArea.RIGHT);
        elemJoin_right.elemJoinRight = mapFrame.get(LayoutArea.RIGHT);
        elemJoin_right.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
        elemJoin_right.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.RIGHT) : owner.adjoinedElem(LayoutArea.RIGHT);
        elemJoin_right.cutAngl1 = 0;
        elemJoin_right.cutAngl2 = 0;
        elemJoin_right.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x2 - 1) + ":" + String.valueOf(y1 + height / 2), elemJoin_right);        
    }*/
    
    public void passJoinFrame() {

        //Угловое соединение левое верхнее
        ElemJoining elemJoin1 = new ElemJoining(iwin);
        elemJoin1.elemJoinRight = mapFrame.get(LayoutArea.TOP);
        elemJoin1.elemJoinBottom = mapFrame.get(LayoutArea.LEFT);
        elemJoin1.joinElement1 = mapFrame.get(LayoutArea.LEFT);
        elemJoin1.joinElement2 = mapFrame.get(LayoutArea.TOP);
        elemJoin1.cutAngl1 = 45;
        elemJoin1.cutAngl2 = 45;
        elemJoin1.varJoin = VariantJoin.VAR2;
        iwin.mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1), elemJoin1);

        //Угловое соединение правое верхнее
        ElemJoining elemJoin2 = new ElemJoining(iwin);
        elemJoin2.elemJoinLeft = mapFrame.get(LayoutArea.TOP);
        elemJoin2.elemJoinBottom = mapFrame.get(LayoutArea.RIGHT);
        elemJoin2.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
        elemJoin2.joinElement2 = mapFrame.get(LayoutArea.TOP);
        elemJoin2.cutAngl1 = 45;
        elemJoin2.cutAngl2 = 45;
        elemJoin2.varJoin = VariantJoin.VAR2;
        iwin.mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1), elemJoin2);

        //Угловое соединение правое нижнее
        ElemJoining elemJoin3 = new ElemJoining(iwin);
        elemJoin3.elemJoinTop = mapFrame.get(LayoutArea.RIGHT);
        elemJoin3.elemJoinLeft = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin3.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
        elemJoin3.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin3.cutAngl1 = 45;
        elemJoin3.cutAngl2 = 45;
        elemJoin3.varJoin = VariantJoin.VAR2;
        iwin.mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y2), elemJoin3);

        //Угловое соединение левое нижнее
        ElemJoining elemJoin4 = new ElemJoining(iwin);
        elemJoin4.elemJoinRight = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin4.elemJoinTop = mapFrame.get(LayoutArea.LEFT);
        elemJoin4.joinElement1 = mapFrame.get(LayoutArea.LEFT);
        elemJoin4.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin4.cutAngl1 = 45;
        elemJoin4.cutAngl2 = 45;
        elemJoin4.varJoin = VariantJoin.VAR2;
        iwin.mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y2), elemJoin4);

        //Прилигающее верхнее
        ElemJoining elemJoin_top = new ElemJoining(iwin);
        elemJoin_top.id = id;
        elemJoin_top.elemJoinTop = mapFrame.get(LayoutArea.TOP);
        elemJoin_top.elemJoinBottom = mapFrame.get(LayoutArea.TOP);
        elemJoin_top.joinElement1 = mapFrame.get(LayoutArea.TOP);
        elemJoin_top.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.TOP) : owner.adjoinedElem(LayoutArea.TOP);
        elemJoin_top.cutAngl1 = 0;
        elemJoin_top.cutAngl2 = 0;
        elemJoin_top.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x1 + width / 2) + ":" + String.valueOf(y1 + 1), elemJoin_top);

        //Прилигающее нижнее
        ElemJoining elemJoin_bottom = new ElemJoining(iwin);
        elemJoin_bottom.id = id;
        elemJoin_bottom.elemJoinTop = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin_bottom.elemJoinBottom = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin_bottom.joinElement1 = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin_bottom.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.BOTTOM) : owner.adjoinedElem(LayoutArea.BOTTOM);
        elemJoin_bottom.cutAngl1 = 0;
        elemJoin_bottom.cutAngl2 = 0;
        elemJoin_bottom.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x1 + width / 2) + ":" + String.valueOf(y2 - 1), elemJoin_bottom);

        //Прилигающее левое
        ElemJoining elemJoin_left = new ElemJoining(iwin);
        elemJoin_left.id = id;
        elemJoin_left.elemJoinLeft = mapFrame.get(LayoutArea.LEFT);
        elemJoin_left.elemJoinRight = mapFrame.get(LayoutArea.LEFT);
        elemJoin_left.joinElement1 = mapFrame.get(LayoutArea.LEFT);
        elemJoin_left.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.LEFT) : owner.adjoinedElem(LayoutArea.LEFT);
        elemJoin_left.cutAngl1 = 0;
        elemJoin_left.cutAngl2 = 0;
        elemJoin_left.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x1 + 1) + ":" + String.valueOf(y1 + height / 2), elemJoin_left);

        //Прилигающее правое
        ElemJoining elemJoin_right = new ElemJoining(iwin);
        elemJoin_right.id = id;
        elemJoin_right.elemJoinLeft = mapFrame.get(LayoutArea.RIGHT);
        elemJoin_right.elemJoinRight = mapFrame.get(LayoutArea.RIGHT);
        elemJoin_right.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
        elemJoin_right.joinElement2 = (owner == owner.root()) ? owner.mapFrame.get(LayoutArea.RIGHT) : owner.adjoinedElem(LayoutArea.RIGHT);
        elemJoin_right.cutAngl1 = 0;
        elemJoin_right.cutAngl2 = 0;
        elemJoin_right.varJoin = VariantJoin.VAR1;
        iwin.mapJoin.put(String.valueOf(x2 - 1) + ":" + String.valueOf(y1 + height / 2), elemJoin_right);
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
    
    @Override
    public TypeElem typeElem() {
        return TypeElem.FULLSTVORKA;
    }

    @Override
    public void joinFrame() {
        System.out.println("Функция не определена");
    }
}
