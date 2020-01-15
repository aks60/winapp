package wincalc;

import domain.eArtikls;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeOpen;
import enums.TypeProfile;

public class AreaStvorka extends AreaBase {

    public String handleHeight = ""; //высота ручки

    protected TypeOpen typeOpen = TypeOpen.OM_INVALID; //тип открывания

    public AreaStvorka(Wincalc iwin, AreaBase owner, String id, String paramJson) {

        super(id);
        this.iwin = iwin;
        this.owner = owner;
        this.width = owner.width;
        this.height = owner.height;
        dimension(owner.x1, owner.y1, owner.x2, owner.y2);
        this.color1 = iwin.color1;
        this.color2 = iwin.color2;
        this.color3 = iwin.color3;
//        try {
//            String str = paramJson.replace("'", "\"");
//            JSONObject jsoPar = (JSONObject) new JSONParser().parse(str);
//            hmParamJson.put(ParamJson.typeOpen, jsoPar.get(ParamJson.typeOpen.name()));
//            hmParamJson.put(ParamJson.funic, jsoPar.get(ParamJson.funic.name()));
//
//        } catch (ParseException e) {
//            System.err.println("Ошибка AreaStvorka() " + e);
//        }
//        if (hmParamJson.get(ParamJson.typeOpen) != null) {
//            int key = Integer.valueOf(hmParamJson.get(ParamJson.typeOpen).toString());
//            for (TypeOpen typeOpen : TypeOpen.values()) {
//                if (typeOpen.value == key) {
//                    this.typeOpen = typeOpen;
//                }
//            }
//        }
//        setRoot(this);
        initСonstructiv();
//        parsingParamJson(getRoot(), paramJson);
    }

    public void initСonstructiv() {
        
        sysprofRec = eSysprof.query.select().stream()
                .filter(rec -> rec.getInt(eSysprof.systree_id) == iwin.nuni
                && rec.getInt(eSysprof.types) == TypeProfile.STVORKA.value).findFirst().orElse(null);
        articlRec = eArtikls.query.select().stream()
                .filter(rec -> rec.getInt(eArtikls.id) == sysprofRec.getInt(eSysprof.artikl_id)).findFirst().orElse(null);
        if (articlRec.getFloat(eArtikls.size_falz) == 0) {
            articlRec.setNo(eArtikls.size_falz, iwin.articlesRec.getDbl(eArtikls.size_falz)); //TODO наследование дордома Профстроя
        }
        specificationRec.setArticlRec(articlRec);
    }

    public TypeOpen typeOpen() {
        return TypeOpen.OM_INVALID;
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.STVORKA;
    }

    @Override
    public void joinRama() {
        System.out.println("Функция не определена");
    }
}
