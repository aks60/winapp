package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;
import enums.eTypeOpen;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AreaStvorka extends AreaBase {    public String handleHeight = ""; //высота ручки

    protected eTypeOpen typeOpen = eTypeOpen.OM_INVALID; //тип открывания

    public AreaStvorka(Wincalc iwin, AreaBase owner, String id, String paramJson) {

        super(id);
        this.iwin = iwin;
        this.owner = owner;
        this.width = owner.width;
        this.height = owner.height;
        setDimension(owner.x1, owner.y1, owner.x2, owner.y2);
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
//        initСonstructiv();
//        parsingParamJson(getRoot(), paramJson);
    }

    public void initСonstructiv() {

//        Sysproa sysproaRec = Sysproa.find(getConst(), iwin.nuni, TypeProfile.STVORKA);
//        articlesRec = Artikls.get(getConst(), sysproaRec.anumb, true);
//        if (articlesRec.asizn == 0) {
//            articlesRec.asizn = iwin.articlesRec.asizn; //TODO наследование дордома Профстроя
//        }
//        specificationRec.setArticlRec(articlesRec);
    }

    public eTypeOpen getTypeOpen() {
        return eTypeOpen.OM_INVALID;
    }
    
    @Override
    public eTypeElem getTypeArea() {
        return eTypeElem.STVORKA;
    }    
}
