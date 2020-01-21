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
import wincalc.constr.Specification;

public class ElemGlass extends ElemComp {

    public static final String RECTANGL = "Прямоугольное";              //
    public static final String RECTANGU_NOT = "Не прямоугольное";       // Параметры
    public static final String ARCHED = "Арочное";                      // формы заполнения
    public static final String ARCHED_NOT = "Не арочное";               //

    protected float radiusGlass = 0;

    public ElemGlass(AreaContainer owner, String id) {
        this(owner, id, null);
    }

    public ElemGlass(AreaContainer owner, String id, String paramJson) {

        super(id);
        this.owner = owner;
        this.iwin = owner.iwin;
        this.side = LayoutArea.FULL;
        
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
        articlRec = eArtikl.find2(String.valueOf(code));
        if (articlRec == null) {
            Record sysreeRec = eSystree.find(iwin.nuni); //по умолчанию стеклопакет
            articlRec = eArtikl.find2(sysreeRec.getStr(eSystree.glas));
        }
        Object obj = articlRec.getDbl(eArtikl.size_falz);
        sysprofRec = eSysprof.find2(iwin.nuni);
        if (articlRec.getDbl(eArtikl.size_falz) == 0) {
            
            articlRec.set(eArtikl.tech_code, iwin.articlesRec.getDbl(eArtikl.tech_code)); //TODO наследование дордома Профстроя
        }
        //Цвет стекла
        Record artdetRec = eArtdet.find(articlRec.getInt(eArtikl.id));
        Record colorRec = eColor.find(artdetRec.getInt(eArtdet.color_id));  
        color1 = colorRec.getInt(eColor.color);
        color2 = colorRec.getInt(eColor.color);
        color3 = colorRec.getInt(eColor.color); 
        
        specificationRec.setArticlRec(articlRec);
    }

    /**
     * Добавление спесификаций зависимых элементов
     */    
    @Override
    public void addSpecifSubelem(Specification specif) {
    /*
        indexUniq(specif);
        specif.element = "ЗАП";
        if (TypeArtikl.GLASS.value2 == specif.getArticRec().atypp && specif.getArticRec().atypm == 5) { //стеклопакет
            return;

        } else if (TypeArtikl.SHTAPIK.value2 == specif.getArticRec().atypp && specif.getArticRec().atypm == 1) { //штапик
            specif.id = getId();

        } else if (TypeArtikl.KONZEVPROF.value2 == specif.getArticRec().atypp && specif.getArticRec().atypm == 3) { //уплотнитель
            specif.id = getId();

        } else {
            specif.id = getId();
        }
        quantityMaterials(specif);
        specificationRec.getSpecificationList().add(specif);*/
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
