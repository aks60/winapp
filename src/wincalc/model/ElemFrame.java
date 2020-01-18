package wincalc.model;

import domain.eArtikl;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeProfile;
import wincalc.constr.Specification;

public class ElemFrame extends ElemComp {

    protected float length = 0; //Длина арки
    protected float anglCut1 = 0; //Угол реза рамы
    protected float anglCut2 = 0; //Угол реза рамы

    public ElemFrame(String id) {
        super(id);
    }

    public ElemFrame(AreaContainer owner, String id, LayoutArea side) {
        super(id);
        this.side = side;
        color1 = owner.color1;
        color2 = owner.color2;
        color3 = owner.color3;
        initСonstructiv();
        if (LayoutArea.LEFT == side) {
            dimension(owner.x1, owner.y1, owner.x1 + articlRec.getInt(eArtikl.height), owner.y2);

        } else if (LayoutArea.RIGHT == side) {
            dimension(owner.x2 - articlRec.getInt(eArtikl.height), owner.y1, owner.x2, owner.y2);
            anglHoriz = 90;

        } else if (LayoutArea.TOP == side) {
            dimension(owner.x1, owner.y1, owner.x2, owner.y1 + articlRec.getInt(eArtikl.height));
            anglHoriz = 180;

        } else if (LayoutArea.BOTTOM == side) {
            dimension(owner.x1, owner.y2 - articlRec.getInt(eArtikl.height), owner.x2, owner.y2);
            anglHoriz = 0;

        } else if (LayoutArea.ARCH == side) {
            anglHoriz = 180;
        }

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

        articlRec = eArtikl.query.select().stream()
                .filter(rec -> rec.getInt(eArtikl.id) == sysprofRec.getInt(eSysprof.artikl_id)).findFirst().orElse(null);

        specificationRec.setArticlRec(articlRec);
    }

    /**
     * Добавление спесификаций зависимых элементов
     */
    @Override
    public void addSpecifSubelem(Specification specif) {
   /*
        indexUniq(specif);
        Artikls cpecifArtikls = specif.getArticRec();

        //Просто рама (если элемент включен в список состава)
        if (TypeArtikl.KOROBKA.isType(cpecifArtikls) || TypeArtikl.STVORKA.isType(cpecifArtikls)) {

            specificationRec.width = specificationRec.width + Float.valueOf(specif.getHmParam(0, 34051)); //Поправка, мм
            specificationRec.setArticlRec(specif.getArticRec());
            return;  //сразу выход т.к. элем. сам является держателем состава

            //Теперь армирование
        } else if (TypeArtikl.ARMIROVANIE.isType(cpecifArtikls)) {
            specif.element = side.name;

            if (LayoutArea.TOP == side || LayoutArea.BOTTOM == side) {
                specif.width = x2 - x1;

            }
            if (LayoutArea.LEFT == side || LayoutArea.RIGHT == side) {
                specif.width = y2 - y1;
            }
            if ("от внутреннего угла".equals(specif.getHmParam(null, 34010))) {
                Double dw1 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut1));
                Double dw2 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut2));
                specif.width = specif.width + 2 * getRoot().iwin.syssizeRec.ssizp - dw1.floatValue() - dw2.floatValue();

            } else {
                Double dw1 = 0.0;
                Double dw2 = 0.0;
                if (getAnglCut(1) != 90) {
                    dw1 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut1));
                }
                if (getAnglCut(1) != 90) {
                    dw2 = articlesRec.aheig / Math.tan(Math.toRadians(anglCut2));
                }
                //specif.width = specif.width + 2 * syssizeRec.ssizp - dw1.floatValue() - dw2.floatValue(); //TODO тут код незакончен
            }
            specif.anglCut1 = 90;
            specif.anglCut2 = 90;

            //Концевой профиль
        } else if (TypeArtikl.KONZEVPROF.isType(cpecifArtikls) == true) {
            String str = specif.getHmParam(0, 12030);
            str = str.replace(",", ".");
            Float koef = Float.valueOf(str);
            float ssizf = getRoot().getIwin().syssizeRec.ssizf;
            specif.width = (getWidth() - ssizf) + (getWidth() - ssizf) * koef;

            //Соединитель
        } else if (TypeArtikl.SOEDINITEL.isType(cpecifArtikls) == true) {

            specif.colorBase = getRoot().getIwin().getColorNone();
            specif.colorInternal = getRoot().getIwin().getColorNone();
            specif.colorExternal = getRoot().getIwin().getColorNone();
            //Всё остальное
        } else {
            //specif.element = "ЗАП";
        }
        quantityMaterials(specif);
        specificationRec.getSpecificationList().add(specif);
        */
    }
    
    public LayoutArea layout() {
        return side;
    }
    
    @Override
    public TypeElem typeElem() {
        return TypeElem.FRAME;
    }

    @Override
    public TypeProfile typeProfile() {
        return (TypeElem.FULLSTVORKA == owner.typeElem()) ? TypeProfile.STVORKA : TypeProfile.FRAME;
    }

    @Override
    public void anglCut(int side, float anglCut) {
        if (side == 1) {
            anglCut1 = anglCut;
        } else {
            anglCut2 = anglCut;
        }
    }
}
