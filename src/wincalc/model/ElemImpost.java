package wincalc.model;

import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eSysprof;
import enums.LayoutArea;
import enums.UseSide;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import java.awt.Color;
import wincalc.constr.Specification;

public class ElemImpost extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм
    protected float anglCut1 = 90; //угол реза импоста
    protected float anglCut2 = 90; //угол реза импоста

    public ElemImpost(AreaSimple owner, float id) {

        super(id, owner.iwin(), owner);
        this.layout = (owner.layout() == LayoutArea.HORIZ) ? LayoutArea.VERT : LayoutArea.HORIZ;
        color1 = iwin().color1;
        color2 = iwin().color2;
        color3 = iwin().color3;
        this.type = TypeElem.IMPOST;
        initСonstructiv();

        //Коррекция положения импоста арки
        if ((TypeElem.ARCH == owner.type || TypeElem.TRAPEZE == owner.type) && owner.listChild.isEmpty()) {
            float dh = artiklRec.getFloat(eArtikl.height) / 2;
            owner.listChild.add(new AreaRectangl(iwin(), owner, owner.id() + .1f, TypeElem.AREA, LayoutArea.HORIZ, owner.width(), dh, -1, -1, -1, null));
        }
        //Установка координат
        for (int index = owner.listChild.size() - 1; index >= 0; --index) {
            if (owner.listChild.get(index).type == TypeElem.AREA) {
                Com5t prevArea = owner.listChild.get(index); //index указывает на предыдущий элемент
                float dx = artiklRec.getFloat(eArtikl.size_centr);

                if (LayoutArea.VERT.equals(owner.layout())) { //сверху вниз
                    setDimension(owner.x1, prevArea.y2 - dx, prevArea.x2, prevArea.y2 + dx);
                    anglHoriz = 0;

                } else if (LayoutArea.HORIZ.equals(owner.layout())) { //слева направо
                    setDimension(prevArea.x2 - dx, prevArea.y1, prevArea.x2 + dx, prevArea.y2);
                    anglHoriz = 90;
                }
                break;
            }
        }
    }

    public void initСonstructiv() {

        if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
            sysprofRec = eSysprof.find3(iwin().nuni, UseArtiklTo.IMPOST, UseSide.HORIZ);

        } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
            sysprofRec = eSysprof.find3(iwin().nuni, UseArtiklTo.IMPOST, UseSide.VERT);
        }
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        Record analogRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
        specificationRec.setArtiklRec(analogRec);
    }

    //Вариант использования
    public void setSpecification(Record sysproaRec) {
/*
        specificationRec.section = (LayoutArea.HORIZ == owner().layout()) ? LayoutArea.VERT.name : LayoutArea.HORIZ.name;
        specificationRec.setArtiklRec(Artikls.get(getConst(), sysproaRec.anumb, false));
        specificationRec.color1 = color1;
        specificationRec.color2 = color2;
        specificationRec.color3 = color3;
        specificationRec.discount = 0;
        specificationRec.anglHoriz = anglHoriz;

        if (LayoutArea.HORIZ == owner().layout()) { //слева направо
            ElemBase elemPrior = prevElem();

            //Элемент слева
            String keyJoinLeft = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y1);
            ElemJoinig joinLeft = getRoot().getHmJoinElem().get(keyJoinLeft);
            ElemBase elemLeft = (joinLeft.joinElement1.equals(this)) ? joinLeft.joinElement2 : joinLeft.joinElement1;
            y1 = elemLeft.y2;
            //элемент справа
            String keyJoinRight = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y2);
            ElemJoinig joinRight = getRoot().getHmJoinElem().get(keyJoinRight);
            ElemBase elemRight = (joinRight.joinElement1.equals(this)) ? joinRight.joinElement2 : joinRight.joinElement1;
            y2 = elemRight.y1;

            specificationRec.width = y2 - y1 + getRoot().iwin.syssizeRec.ssizi * 2 + elemLeft.artiklesRec.asizn + elemRight.artiklesRec.asizn;;
            specificationRec.height = artiklesRec.aheig;

        } else if (LayoutArea.VERTICAL == owner.getLayout()) { //сверху вниз
            ElemBase elemPrior = prevElem();

            //Элемент слева
            String keyJoinLeft = String.valueOf(elemPrior.x1) + ":" + String.valueOf(elemPrior.y2);
            ElemJoinig joinLeft = getRoot().getHmJoinElem().get(keyJoinLeft);
            ElemBase elemLeft = (joinLeft.joinElement1.equals(this)) ? joinLeft.joinElement2 : joinLeft.joinElement1;
            x1 = elemLeft.x2;
            //Элемент справа
            String keyJoinRight = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y2);
            ElemJoinig joinRight = getRoot().getHmJoinElem().get(keyJoinRight);
            ElemBase elemRight = (joinRight.joinElement1.equals(this)) ? joinRight.joinElement2 : joinRight.joinElement1;
            x2 = elemRight.x1;

            specificationRec.width = x2 - x1 + getRoot().iwin.syssizeRec.ssizi * 2 + elemLeft.artiklesRec.asizn + elemRight.artiklesRec.asizn;
            specificationRec.height = artiklesRec.aheig;
        }
        specificationRec.anglCut2 = 90;
        specificationRec.anglCut1 = 90;
*/
    }

    @Override //Детализация варианта      
    public void addSpecification(Specification specif) {

        Record artiklRec = specif.artiklRec;

        //Импост (если элемент включен в список состава)
        if (TypeArtikl.IMPOST.isType(artiklRec)) {
            specificationRec.name = (specif.name.equals("-")) ? specif.name : "-";
            specificationRec.setArtiklRec(specif.artiklRec);
            return;

            //Теперь армирование
        } else if (TypeArtikl.ARMIROVANIE.isType(artiklRec)) {
            specif.section = layout.name;
            specif.width = specificationRec.width;
            specif.anglCut2 = 90;
            specif.anglCut1 = 90;

            //Соединитель
        } else if (TypeArtikl.SOEDINITEL.isType(artiklRec)) {
            specif.color1 = iwin().colorNone;
            specif.color2 = iwin().colorNone;
            specif.color3 = iwin().colorNone;

            //Остальные
        } else {
            //
        }
        specif.section = "СОСТ";
        quantityMaterials(specif);
        specificationRec.specificationList.add(specif);
    }

    @Override
    public void paint() {

        int rgb = eColor.find(color2).getInt(eColor.rgb);
        if (LayoutArea.VERT == owner().layout()) {
            strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.HORIZ == owner().layout()) {
            strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);
        }
    }

    @Override
    public UseArtiklTo useArtiklTo() {
        return UseArtiklTo.IMPOST;
    }

    @Override
    public void anglCut(int side, float anglCut) {
        if (side == 1) {
            anglCut1 = anglCut;
        } else {
            anglCut2 = anglCut;
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut=" + anglCut1 + ", anglCut=" + anglCut1;
    }
}
