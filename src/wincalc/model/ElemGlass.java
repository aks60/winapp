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

public class ElemGlass extends ElemSimple {

    public static final String RECTANGL = "Прямоугольное";              //
    public static final String RECTANGU_NOT = "Не прямоугольное";       // Параметры
    public static final String ARCHED = "Арочное";                      // формы заполнения
    public static final String ARCHED_NOT = "Не арочное";               //

    protected float radiusGlass = 0;

    public ElemGlass(AreaSimple owner, String id) {
        this(owner, id, null);
    }

    public ElemGlass(AreaSimple owner, String id, String param) {

        super(id);
        this.owner = owner;
        this.iwin = owner.iwin;
        this.layout = LayoutArea.FULL;
        
        if (param != null && param.isEmpty() == false) {
            String str = param.replace("'", "\"");
            JsonElement jsonElem = new Gson().fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            mapParam.put(ParamJson.nunic_iwin, jsonObj.get(ParamJson.nunic_iwin.name()));
        }
        initСonstructiv();
        parsing(param);

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
        artiklRec = eArtikl.up.find2(String.valueOf(code));
        if (artiklRec == null) {
            Record sysreeRec = eSystree.up.find(iwin.nuni); //по умолчанию стеклопакет
            artiklRec = eArtikl.up.find2(sysreeRec.getStr(eSystree.glas));
        }
        sysprofRec = eSysprof.up.find3(iwin.nuni, TypeProfile.FRAME, ProfileSide.LEFT); //у стеклопакет нет записи в Sysproa пэтому идёт подмена на Frame
        if (artiklRec.getDbl(eArtikl.size_falz) == 0) {
            Object ooo = iwin.artiklRec.get(eArtikl.tech_code);
            artiklRec.set(eArtikl.tech_code, iwin.artiklRec.getStr(eArtikl.tech_code)); //TODO наследование дордома Профстроя
        }
        //Цвет стекла
        Record artdetRec = eArtdet.up.find(artiklRec.getInt(eArtikl.id));
        Record colorRec = eColor.up.find(artdetRec.getInt(eArtdet.color_id));  
        color1 = colorRec.getInt(eColor.color);
        color2 = colorRec.getInt(eColor.color);
        color3 = colorRec.getInt(eColor.color); 
        
        specificationRec.setArtiklRec(artiklRec);
    }

    @Override
    public void paint() {
        if (owner instanceof AreaArch) {
            ElemFrame ef = root().mapFrame.get(LayoutArea.ARCH);
            float dz = ef.artiklRec.getFloat(eArtikl.height);            
            double r = ((AreaArch) root()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(root().width / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((root().width - 2 * dz) / ((r - dz) * 2)));
            fillArc(root().width / 2 - r + dz, dz, (r - dz) * 2, (r - dz) * 2, ang2, (90 - ang2) * 2); //прорисовка на сцену
        } else {
            fillPoligon(x1, x2, x2, x1, y1, y1, y2, y2);
        }
    }
    
    @Override
    //Добавление спесификаций зависимых элементов
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
