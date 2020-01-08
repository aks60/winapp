package elems;

import constr.Specification;
import enums.eLayoutArea;
import enums.eTypeArtikl;
import enums.eTypeElem;
import enums.eTypeProfile;
import javafx.scene.paint.Color;

/**
 * Элементы рамы окна
 */
public class ElemFrame extends ElemBase {

    protected eLayoutArea side = eLayoutArea.NONE;
    protected float length = 0; //Длина арки
    protected float anglCut1 = 0; //Угол реза рамы
    protected float anglCut2 = 0; //Угол реза рамы

    public ElemFrame(AreaSimple owner, String id, eLayoutArea side) {
        super(id);
//        this.owner = owner;
//        setRoot(owner);
//        this.side = side;
//        colorBase = getRoot().colorBase;
//        colorInternal = getRoot().colorInternal;
//        colorExternal = getRoot().colorExternal;
//        initСonstructiv();
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
//
//
//        if (eLayoutArea.TOP == getLayout() || eLayoutArea.BOTTOM == getLayout()) {
//            width = x2 - x1;
//            height = y2 - y1;
//
//        } else if (eLayoutArea.LEFT == getLayout() || eLayoutArea.RIGHT == getLayout()) {
//            width = y2 - y1;
//            height = x2 - x1;
//        }
    }

    public void initСonstructiv() {

//        sysproaRec = Sysproa.find(getConst(), owner.iwin.nuni, getTypeProfile());
//        articlesRec = Artikls.get(getConst(), sysproaRec.anumb, true);
//        specificationRec.setArticlRec(articlesRec);
    }

//    public void setSpecifElement(Sysproa sysproaRec) {

//        specificationRec.element = side.name;
//        float ssizp = getRoot().iwin.syssizeRec.ssizp;
//        Artikls articlesRec = Artikls.get(getConst(), sysproaRec.anumb, false);
//        specificationRec.setArticlRec(articlesRec);
//        specificationRec.colorBase = colorBase;
//        specificationRec.colorInternal = colorInternal;
//        specificationRec.colorExternal = colorExternal;
//        specificationRec.discount = 0;
//        specificationRec.anglHoriz = anglHoriz;
//        indexUniq(specificationRec);
//
//        //Простое окно
//        if (eLayoutArea.TOP == side) {
//            specificationRec.width = x2 - x1 + ssizp * 2;
//            specificationRec.height = articlesRec.aheig;
//
//        } else if (eLayoutArea.BOTTOM == side) {
//            specificationRec.width = x2 - x1 + ssizp * 2;
//            specificationRec.height = articlesRec.aheig;
//
//        } else if (eLayoutArea.LEFT == side) {
//            specificationRec.width = y2 - y1 + getRoot().iwin.syssizeRec.ssizp * 2;
//            specificationRec.height = articlesRec.aheig;
//
//        } else if (eLayoutArea.RIGHT == side) {
//            specificationRec.width = y2 - y1 + ssizp * 2;
//            specificationRec.height = articlesRec.aheig;
//        }
//
//        // Коррекция формы окна
//        owner.setSpecifElement(this, sysproaRec);
//
//        width = specificationRec.width;
//        height = specificationRec.height;
//
//        specificationRec.anglCut2 = anglCut2;
//        specificationRec.anglCut1 = anglCut1;
//    }

    /**
     * Добавление спесификаций зависимых элементов
     */
    @Override
    public void addSpecifSubelem(Specification specif) {

//        indexUniq(specif);
//        Artikls cpecifArtikls = specif.getArticRec();
//
//        //Просто рама (если элемент включен в список состава)
//        if (eParamJson.KOROBKA.isType(cpecifArtikls) || eParamJson.STVORKA.isType(cpecifArtikls)) {
//
//            specificationRec.width = specificationRec.width + Float.valueOf(specif.getHmParam(0, 34051)); //Поправка, мм
//            specificationRec.setArticlRec(specif.getArticRec());
//            return;  //сразу выход т.к. элем. сам является держателем состава
//
//            //Теперь армирование
//        } else if (eParamJson.ARMIROVANIE.isType(cpecifArtikls)) {
//            specif.element = side.name;
//
//            if (eLayoutArea.TOP == side || eLayoutArea.BOTTOM == side) {
//                specif.width = x2 - x1;
//
//            }
//            if (eLayoutArea.LEFT == side || eLayoutArea.RIGHT == side) {
//                specif.width = y2 - y1;
//            }
//            if ("от внутреннего угла".equals(specif.getHmParam(null, 34010))) {
//                Double dw1 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut1));
//                Double dw2 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut2));
//                specif.width = specif.width + 2 * getRoot().iwin.syssizeRec.ssizp - dw1.floatValue() - dw2.floatValue();
//
//            } else {
//                Double dw1 = 0.0;
//                Double dw2 = 0.0;
//                if (getAnglCut(1) != 90) {
//                    dw1 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut1));
//                }
//                if (getAnglCut(1) != 90) {
//                    dw2 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut2));
//                }
//                //specif.width = specif.width + 2 * syssizeRec.ssizp - dw1.floatValue() - dw2.floatValue(); //TODO тут код незакончен
//            }
//            specif.anglCut1 = 90;
//            specif.anglCut2 = 90;
//
//            //Концевой профиль
//        } else if (eParamJson.KONZEVPROF.isType(cpecifArtikls) == true) {
//            String str = specif.getHmParam(0, 12030);
//            str = str.replace(",", ".");
//            Float koef = Float.valueOf(str);
//            float ssizf = getRoot().getIwin().syssizeRec.ssizf;
//            specif.width = (getWidth() - ssizf) + (getWidth() - ssizf) * koef;
//
//            //Соединитель
//        } else if (eParamJson.SOEDINITEL.isType(cpecifArtikls) == true) {
//
//            specif.colorBase = getRoot().getIwin().getColorNone();
//            specif.colorInternal = getRoot().getIwin().getColorNone();
//            specif.colorExternal = getRoot().getIwin().getColorNone();
//            //Всё остальное
//        } else {
//            //specif.element = "ЗАП";
//        }
//        quantityMaterials(specif);
//        specificationRec.getSpecificationList().add(specif);
    }

    @Override
    public void drawElemList() {
//        float dz = articlesRec.aheig;
//        float h = getRoot().getIwin().getHeightAdd() - getRoot().getIwin().getHeight();
//        float w = owner.getRoot().width;
//        float y1h = y1 + h;
//        float y2h = y2 + h;
//
//        int rgb = Colslst.get2(getRoot().getConst(), colorInternal).cview;
//        if (eLayoutArea.TOP == side) {
//            strokePolygon(x1, x2, x2 - dz, x1 + dz, y1, y1, y2, y2, rgb, Color.BLACK, 4);
//
//        } else if (eLayoutArea.BOTTOM == side) {
//            strokePolygon(x1 + dz, x2 - dz, x2, x1, y1, y1, y2, y2, rgb, Color.BLACK, 4);
//
//        } else if (eLayoutArea.LEFT == side) {
//            if (owner.getRoot() instanceof AreaArch) {
//                double r = ((AreaArch) owner.getRoot()).radiusArch;
//                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * dz) / ((r - dz) * 2)));
//                double a = (r - dz) * Math.sin(Math.toRadians(ang2));
//                strokePolygon(x1, x2, x2, x1, y1, (float) (r - a - h), y2 - dz, y2, rgb, Color.BLACK, 4);
//            } else {
//                strokePolygon(x1, x2, x2, x1, y1, y1 + dz, y2 - dz, y2, rgb, Color.BLACK, 4);
//            }
//        } else if (eLayoutArea.RIGHT == side) {
//            if (owner.getRoot() instanceof AreaArch) {
//                double r = ((AreaArch) owner.getRoot()).radiusArch;
//                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * dz) / ((r - dz) * 2)));
//                double a = (r - dz) * Math.sin(Math.toRadians(ang2));
//                strokePolygon(x1, x2, x2, x1, (float) (r - a - h), y1, y2, y2 - dz, rgb, Color.BLACK, 4);
//            } else {
//                strokePolygon(x1, x2, x2, x1, y1 + dz, y1, y2, y2 - dz, rgb, Color.BLACK, 4);
//            }
//
//        }
    }

    @Override
    public void setAnglCut(int side, float anglCut) {
        if (side == 1) anglCut1 = anglCut;
        else anglCut2 = anglCut;
    }

    @Override
    public eLayoutArea getLayout() {
        return side;
    }

    @Override
    public eTypeElem getTypeElem() {
        return (eTypeElem.FULLSTVORKA == owner.getTypeElem()) ? eTypeElem.STVORKA : eTypeElem.FRAME;
    }

    @Override
    public eTypeProfile getTypeProfile() {
        return (eTypeElem.FULLSTVORKA == owner.getTypeElem()) ? eTypeProfile.STVORKA : eTypeProfile.FRAME;
    }

    @Override
    public float getAnglCut(int num) {
        return (num == 1) ? anglCut1 : anglCut2;
    }

    @Override
    public String toString() {
        return "FRAME ID=" + id + ", сторона=" + side + ", угол1=" + anglCut1 + ", угол2=" + anglCut2;
    }
}

