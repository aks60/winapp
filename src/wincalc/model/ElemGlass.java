package wincalc.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.ParamJson;
import enums.ProfileSide;
import enums.TypeElem;
import enums.TypeProfile;
import wincalc.Wincalc;

public class ElemGlass extends ElemBase {

    public static final String RECTANGL = "Прямоугольное";              //
    public static final String RECTANGU_NOT = "Не прямоугольное";       // Параметры
    public static final String ARCHED = "Арочное";                      // формы заполнения
    public static final String ARCHED_NOT = "Не арочное";               //

    private LayoutArea side = LayoutArea.FULL;
    protected float radiusGlass = 0;

    public ElemGlass(Wincalc iwin, AreaBase owner, String id) {
        this(iwin, owner, id, null);
    }

    public ElemGlass(Wincalc iwin, AreaBase owner, String id, String paramJson) {

        super(id);
        this.iwin = iwin;

        if (paramJson != null && paramJson.isEmpty() == false) {
            String str = paramJson.replace("'", "\"");
            JsonElement jsonElem = new Gson().fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            mapParam.put(ParamJson.nunic_iwin, jsonObj.get(ParamJson.nunic_iwin.name()));
        }
        initСonstructiv();
        parsingParam(iwin.rootArea, paramJson);

        if (TypeElem.ARCH == owner.typeElem()) {
            dimension(owner.x1, owner.y1, owner.x2, iwin.heightAdd - owner.y2);
            //TODO putHmParam(13015, ARCHED);
        } else {
            dimension(owner.x1, owner.y1, owner.x2, owner.y2);
            //TODO putHmParam(13015, RECTANGL);
        }
    }

    public void initСonstructiv() {

        Object code = mapParam.get(ParamJson.nunic_iwin);
        articlRec = eArtikl.query.select().stream().filter(rec -> code.equals(rec.get(eArtikl.code))).findFirst().orElse(null);
        if (articlRec == null) {
            Record sysreeRec = eSystree.query.select().stream().filter(rec -> iwin.nuni.equals(rec.get(eSystree.id))).findFirst().orElse(null); //по умолчанию стеклопакет
            articlRec = eArtikl.query.select().stream().filter(rec -> rec.getInt(eArtikl.id) == sysreeRec.getInt(eSystree.glas)).findFirst().orElse(null);
        }
        sysprofRec = eSysprof.query.select().stream() //у стеклопакет нет записи в Sysproa пэтому идёт подмена на Frame  
                .filter(rec -> iwin.nuni == rec.getInt(eSysprof.systree_id)
                && TypeProfile.FRAME.value == rec.getInt(eSysprof.types)
                && ProfileSide.Left.value == rec.getInt(eSysprof.side)).findFirst().orElse(null);
        if (articlRec.getDbl(eArtikl.size_falz) == 0) {
            articlRec.set(eArtikl.tech_code, iwin.articlesRec.getDbl(eArtikl.tech_code)); //TODO наследование дордома Профстроя
        }
        //Цвет стекла
        Record artdetRec = eArtdet.query.select().stream().filter(rec -> rec.getInt(eArtdet.artikl_id) == articlRec.getInt(eArtikl.id)).findFirst().orElse(null);
        Record colorRec = eColor.query.select().stream().filter(rec -> rec.getInt(eColor.id) == artdetRec.getInt(eArtdet.color_id)).findFirst().orElse(null);        
        color1 = colorRec.getInt(eColor.color);
        color2 = colorRec.getInt(eColor.color);
        color3 = colorRec.getInt(eColor.color); 
        
        specificationRec.setArticlRec(articlRec);
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.GLASS;
    }

    @Override
    public TypeProfile typeProfile() {
        return TypeProfile.UNKNOWN;
    }
}
