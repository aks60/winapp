package wincalc;

import dataset.Query;
import dataset.Record;
import domain.eArtikls;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeProfile;
import java.util.ArrayList;

public class ElemFrame extends ElemBase {

    protected LayoutArea side = LayoutArea.NONE;
    protected float length = 0; //Длина арки
    protected float anglCut1 = 0; //Угол реза рамы
    protected float anglCut2 = 0; //Угол реза рамы

    public ElemFrame(String id) {
        super(id);
    }

    public LayoutArea layout() {
        return side;
    }

    public ElemFrame(AreaBase owner, String id, LayoutArea side) {
        super(id);
        this.side = side;
        color1 = owner.color1;
        color2 = owner.color2;
        color3 = owner.color3;
        initСonstructiv();
//        if (eLayoutArea.LEFT == side) {
//            setDimension(owner.x1, owner.y1, owner.x1 + articlesRec.aheig, owner.y2);
//
//        } else if (eLayoutArea.RIGHT == side) {
//            setDimension(owner.x2 - articlesRec.aheig, owner.y1, owner.x2, owner.y2);
//            anglHoriz = 90;
//
//        } else if (eLayoutArea.TOP == side) {
//            setDimension(owner.x1, owner.y1, owner.x2, owner.y1 + articlesRec.aheig);
//            anglHoriz = 180;
//
//        } else if (eLayoutArea.BOTTOM == side) {
//            setDimension(owner.x1, owner.y2 - articlesRec.aheig, owner.x2, owner.y2);
//            anglHoriz = 0;
//
//        } else if (eLayoutArea.ARCH == side) {
//            anglHoriz = 180;
//        }

        if (LayoutArea.TOP == layout() || LayoutArea.BOTTOM == layout()) {
            width = x2 - x1;
            height = y2 - y1;

        } else if (LayoutArea.LEFT == layout() || LayoutArea.RIGHT == layout()) {
            width = y2 - y1;
            height = x2 - x1;
        }
    }

    public void initСonstructiv() {

        sysprofRec = eSysprof.query.select().stream()
                .filter(rec -> rec.getInt(eSysprof.systree_id) == iwin.nuni 
                        && rec.getInt(eSysprof.types) == typeProfile().value).findFirst().orElse(null);
       
        articlRec = eArtikls.query.select().stream()
                .filter(rec -> rec.getInt(eArtikls.id) == sysprofRec.getInt(eSysprof.artikl_id)).findFirst().orElse(null);

//        specificationRec.setArticlRec(articlesRec);
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.FRAME;
    }

    @Override
    public TypeProfile typeProfile() {
        return (TypeElem.FULLSTVORKA == owner.typeElem()) ? TypeProfile.STVORKA : TypeProfile.FRAME;
    }
}
