package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;

public class ElemImpost extends ElemBase {

    private eLayoutArea side = eLayoutArea.NONE;
    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм
    protected float anglCut1 = 90; //угол реза импоста
    protected float anglCut2 = 90; //угол реза импоста

        public ElemImpost(String id) {
        super(id);
    }
        
    public ElemImpost(AreaBase root, AreaBase owner, String id) {

        super(id);
        this.side = (owner.getLayout() == eLayoutArea.HORIZONTAL) ?eLayoutArea.VERTICAL :eLayoutArea.HORIZONTAL;
        color1 = root.color1;
        color2 = root.color2;
        color3 = root.color3;
        initСonstructiv();

        //Коррекция положения импоста арки
        if((eTypeElem.ARCH == owner.getTypeArea() || eTypeElem.TRAPEZE == owner.getTypeArea()) && owner.getChildList().isEmpty()) {

//                float dh = articlesRec.aheig / 2;
//                owner.addElem(new AreaBase(owner.getIwin(), root, owner, genId(), eLayoutArea.HORIZONTAL, owner.width, dh));
        }
        //Установка координат
//        for (int index = owner.getChildList().size() - 1; index >= 0; --index) {
//            if (owner.getChildList().get(index) instanceof AreaSimple) {
//                ElemBase prevArea = owner.getChildList().get(index); //index указывает на предыдущий элемент
//                float dx = articlesRec.asizb;
//
//                if (LayoutArea.VERTICAL.equals(owner.getLayout())) { //сверху вниз
//                    setDimension(owner.x1, prevArea.y2 - dx, prevArea.x2, prevArea.y2 + dx);
//                    anglHoriz = 0;
//
//                } else if (LayoutArea.HORIZONTAL.equals(owner.getLayout())) { //слева направо
//                    setDimension(prevArea.x2 - dx, prevArea.y1, prevArea.x2 + dx, prevArea.y2);
//                    anglHoriz = 90;
//                }
//                break;
//            }
//        }
    }

    public void initСonstructiv() {
//        if (LayoutArea.VERTICAL.equals(owner.getLayout())) { //сверху вниз
//            sysproaRec = Sysproa.find(getConst(), owner.iwin.nuni, TypeProfile.IMPOST, ProfileSide.Horiz);
//        } else if (LayoutArea.HORIZONTAL.equals(owner.getLayout())) { //слева направо
//            sysproaRec = Sysproa.find(getConst(), owner.iwin.nuni, TypeProfile.IMPOST, ProfileSide.Vert);
//        }
//        articlesRec = Artikls.get(getConst(), sysproaRec.anumb, true);
//        specificationRec.setArticlRec(articlesRec);
    }
}
