package wincalc;

import enums.eLayoutArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ElemGlass extends ElemBase {


    public static final String RECTANGL = "Прямоугольное";              //
    public static final String RECTANGU_NOT = "Не прямоугольное";       // Параметры
    public static final String ARCHED = "Арочное";                      // формы заполнения
    public static final String ARCHED_NOT = "Не арочное";               //

    private eLayoutArea side = eLayoutArea.FULL;
    protected float radiusGlass = 0;

    public ElemGlass(AreaBase root, AreaBase owner, String id) throws ParseException  {
        this(root, owner, id, null);
    }

    public ElemGlass(AreaBase root, AreaBase owner, String id, String paramJson) throws ParseException  {

        super(id);
        
//        if(paramJson != null) {
//            String str = paramJson.replace("'", "\"");
//            JSONObject jsonPar = (JSONObject) new JSONParser().parse(str);
//            hmParamJson.put(ParamJson.nunic_iwin, jsonPar.get(ParamJson.nunic_iwin.name()));
//        }
//        initСonstructiv();
//        parsingParamJson(root, paramJson);
//
//        if (TypeElem.ARCH == owner.getTypeElem()) {
//            setDimension(owner.x1, owner.y1, owner.x2, root.getIwin().getHeightAdd() - owner.y2);
//            putHmParam(13015, ARCHED);
//        } else {
//            setDimension(owner.x1, owner.y1, owner.x2, owner.y2);
//            putHmParam(13015, RECTANGL);
//        }
    }

    public void initСonstructiv() {

//        articlesRec = Artikls.get(getConst(), hmParamJson); //стеклопакет по параметру nunic_iwin
//        if(articlesRec == null)   {
//            Sysprof sysprofRec = Sysprof.get(getConst(), owner.iwin.nuni); //по умолчанию стеклопакет
//            articlesRec = Artikls.get(getConst(), sysprofRec.anumb, false);
//        }
//        sysproaRec = Sysproa.find(getConst(), owner.iwin.nuni, TypeProfile.FRAME, ProfileSide.Left); //у стеклопакет нет записи в Sysproa пэтому идёт подмена на Frame
//        if (articlesRec.asizn == 0) {
//            //articlesRec.atech = getRoot().getHmElemFrame().get(LayoutArea.LEFT).getArticlesRec().atech; //а может так!!!
//            articlesRec.atech = getRoot().getIwin().getArticlesRec().atech; //TODO наследование дордома Профстроя
//        }
//        //Цвет стекла
//        Artsvst artsvstRec = Artsvst.get2(getConst(), articlesRec.anumb);
//        colorBase = artsvstRec.clcod;
//        colorInternal = artsvstRec.clcod;
//        colorExternal = artsvstRec.clcod;
//        specificationRec.setArticlRec(articlesRec);
    }
}
