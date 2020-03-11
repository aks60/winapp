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
import enums.TypeArtikl;
import enums.TypeElem;
import enums.TypeUse;
import wincalc.constr.Specification;

public class ElemGlass extends ElemSimple {

    public static final String RECTANGL = "Прямоугольное";              //
    public static final String RECTANGU_NOT = "Не прямоугольное";       // Параметры
    public static final String ARCHED = "Арочное";                      // формы заполнения
    public static final String ARCHED_NOT = "Не арочное";               //

    public float radiusGlass = 0;

    public ElemGlass(AreaSimple owner, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = LayoutArea.FULL;
        this.type = TypeElem.GLASS;

        if (param != null && param.isEmpty() == false) {
            String str = param.replace("'", "\"");
            JsonElement jsonElem = new Gson().fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            mapParam.put(ParamJson.nunic_iwin, jsonObj.get(ParamJson.nunic_iwin.name()));
        }
        initСonstructiv();
        parsing(param);

        if (TypeElem.ARCH == owner.type) {
            setDimension(owner.x1, owner.y1, owner.x2, iwin().heightAdd - owner.y2);
            //TODO putHmParam(13015, ARCHED);
        } else {
            setDimension(owner.x1, owner.y1, owner.x2, owner.y2);
            //TODO putHmParam(13015, RECTANGL);
        }
    }

    public void initСonstructiv() {

        Object code = mapParam.get(ParamJson.nunic_iwin);
        artiklRec = eArtikl.find2(String.valueOf(code));
        if (artiklRec == null) {
            Record sysreeRec = eSystree.find(iwin().nuni); //по умолчанию стеклопакет
            artiklRec = eArtikl.find2(sysreeRec.getStr(eSystree.glas));
        }
        sysprofRec = eSysprof.find3(iwin().nuni, TypeUse.FRAME, ProfileSide.LEFT); //у стеклопакет нет записи в Sysproa пэтому идёт подмена на Frame
        if (artiklRec.getDbl(eArtikl.size_falz) == 0) {
            artiklRec.set(eArtikl.tech_code, iwin().artiklRec.getStr(eArtikl.tech_code)); //TODO наследование дордома Профстроя
        }
        //Цвет стекла
        Record artdetRec = eArtdet.find2(artiklRec.getInt(eArtikl.id));
        Record colorRec = eColor.find(artdetRec.getInt(eArtdet.color_id));
        color1 = colorRec.getInt(eColor.code);
        color2 = colorRec.getInt(eColor.code);
        color3 = colorRec.getInt(eColor.code);

        //TODO Разобраться с цветом стекла
        color1 = iwin().colorNone;
        color2 = iwin().colorNone;
        color3 = iwin().colorNone;

        specificationRec.setArtiklRec(artiklRec);
    }

    public void setSpecifElement() {

        //indexUniq(specificationRec);

        float gzazo = Float.valueOf(mapFieldVal.get("GZAZO"));
        if (owner() instanceof AreaArch) { //если арка

            ElemFrame elemArch = root().mapFrame.get(LayoutArea.ARCH);
            ElemImpost elemImpost = null;  //первый импост в стеклопакете снизу;
            for (Com5t elemBase : root().listChild) {
                if (TypeElem.IMPOST == elemBase.type) {
                    elemImpost = (ElemImpost) elemBase;
                    break;
                }
            }
            y1 = y1 + elemArch.artiklRec.getInt(eArtikl.height) - elemArch.artiklRec.getInt(eArtikl.size_falz) + gzazo;
            y2 = y2 + elemImpost.artiklRec.getInt(eArtikl.size_falz) - gzazo;
            //height = y2 - y1;
            specificationRec.height = height();
            double r = ((AreaArch) root()).radiusArch - elemArch.artiklRec.getInt(eArtikl.height) + elemArch.artiklRec.getInt(eArtikl.size_falz) - gzazo;
            double l = Math.sqrt(2 * height() * r - height() * height());
            x1 = (owner().width() / 2) - (float) l;
            x2 = owner().width() - x1;
            radiusGlass = (float) r;
            //width = x2 - x1;
            
            specificationRec.width = width();

            specificationRec.id = id();
            specificationRec.element = "Арочное";
            specificationRec.setArtiklRec(artiklRec);
            specificationRec.color1 = color1;
            specificationRec.color2 = color2;
            specificationRec.color3 = color3;

        } else {

            Com5t elemTop = owner().iwin().mapJoin.get(owner().x1 + ":" + owner().y1).elemJoinRight;
            y1 = elemTop.y2 - elemTop.artiklRec.getInt(eArtikl.size_falz) + gzazo;

            Com5t elemBottom = owner().iwin().mapJoin.get(owner().x1 + ":" + owner().y2).elemJoinRight;
            y2 = elemBottom.y1 + elemBottom.artiklRec.getInt(eArtikl.size_falz) - gzazo;

            Com5t elemLeft = owner().iwin().mapJoin.get(owner().x1 + ":" + owner().y1).elemJoinBottom;
            x1 = elemLeft.x2 - elemLeft.artiklRec.getInt(eArtikl.size_falz) + gzazo;

            Com5t elemRight = owner().iwin().mapJoin.get(owner().x2 + ":" + owner().y1).elemJoinBottom;
            x2 = elemRight.x1 + elemRight.artiklRec.getInt(eArtikl.size_falz) - gzazo;

            specificationRec.width = width();
            specificationRec.height = height();
            specificationRec.id = id();
            specificationRec.element = "Прямоугольное";
            specificationRec.setArtiklRec(artiklRec);
            specificationRec.color1 = color1;
            specificationRec.color2 = color2;
            specificationRec.color3 = color3;
        }
    }
    
    @Override
    public void paint() { //рисуём стёкла
        iwin().gc2d.setColor(new java.awt.Color(226, 255, 250));

        if (owner().type == TypeElem.ARCH) {
            ElemFrame ef = root().mapFrame.get(LayoutArea.ARCH);
            float dz = ef.artiklRec.getFloat(eArtikl.height);
            double r = ((AreaArch) root()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(root().width() / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((root().width() - 2 * dz) / ((r - dz) * 2))); 
            fillArc((float)(root().width() / 2 - r + dz), dz, (float)((r - dz) * 2), (float)((r - dz) * 2), (float)ang2, (float)((90 - ang2) * 2));

        } else {
            float h = iwin().heightAdd - iwin().height;
            fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                    new int[]{(int) (y1 + h), (int) (y1 + h), (int) (y2 + h), (int) (y2 + h)}, 4);
        }
    }

    @Override
    //Добавление спесификаций зависимых элементов
    public void addSpecifSubelem(Specification specif) {
   
        //indexUniq(specif);
        specif.element = "ЗАП";
        if (TypeArtikl.GLASS.id2 == specif.artiklRec.getInt(eArtikl.level2) && specif.artiklRec.getInt(eArtikl.level1) == 5) { //стеклопакет
            return;

        } else if (TypeArtikl.SHTAPIK.id2 == specif.artiklRec.getInt(eArtikl.level2) && specif.artiklRec.getInt(eArtikl.level1) == 1) { //штапик
            specif.id = id();

        } else if (TypeArtikl.KONZEVPROF.id2 == specif.artiklRec.getInt(eArtikl.level2) && specif.artiklRec.getInt(eArtikl.level1) == 3) { //уплотнитель
            specif.id = id();

        } else {
            specif.id = id();
        }
        quantityMaterials(specif);        
        specificationRec.specificationList.add(specif);
    }

    @Override
    public TypeUse typeProfile() {
        return TypeUse.UNKNOWN;
    }

    @Override
    public String toString() {
        return super.toString() + ", radiusGlass=" + radiusGlass;
    }
}
