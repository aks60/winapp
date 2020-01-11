package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;

public class ElemFrame extends ElemBase {

    protected eLayoutArea side = eLayoutArea.NONE;
    protected float length = 0; //Длина арки
    protected float anglCut1 = 0; //Угол реза рамы
    protected float anglCut2 = 0; //Угол реза рамы

    public ElemFrame(String id) {
        super(id);
    }

    public eLayoutArea getLayout() {
        return side;
    }

    public ElemFrame(AreaBase owner, String id, eLayoutArea side) {
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


        if (eLayoutArea.TOP == getLayout() || eLayoutArea.BOTTOM == getLayout()) {
            width = x2 - x1;
            height = y2 - y1;

        } else if (eLayoutArea.LEFT == getLayout() || eLayoutArea.RIGHT == getLayout()) {
            width = y2 - y1;
            height = x2 - x1;
        }
    }

    public void initСonstructiv() {

//        sysproaRec = Sysproa.find(getConst(), owner.iwin.nuni, getTypeProfile());
//        articlesRec = Artikls.get(getConst(), sysproaRec.anumb, true);
//        specificationRec.setArticlRec(articlesRec);
    }   
    
    @Override
    public eTypeElem typeElem() {
        return eTypeElem.FRAME;
    }    
}
