package estimate.model;

import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eSysprof;
import enums.LayoutArea;
import enums.UseSide;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import estimate.constr.Specification;
import domain.eSyssize;

public class ElemImpost extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм

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
            owner.listChild.add(new AreaSimple(iwin(), owner, owner.id() + .1f, TypeElem.AREA, LayoutArea.HORIZ, owner.width(), dh, -1, -1, -1, null));
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
            sysprofRec = eSysprof.find3(iwin(), UseArtiklTo.IMPOST, UseSide.HORIZ, UseSide.ANY);

        } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
            sysprofRec = eSysprof.find3(iwin(), UseArtiklTo.IMPOST, UseSide.VERT, UseSide.ANY);
        }
        specificationRec.place = (LayoutArea.HORIZ == owner().layout()) ? LayoutArea.VERT.name : LayoutArea.HORIZ.name;        
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
        specificationRec.setArtiklRec(artiklRec);
        //artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
    }

    @Override //Главная спецификация
    public void setSpecific() {
        
        specificationRec.place = "СОСТ." + specificationRec.place.substring(0, 1);
/*
        Record artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        specificationRec.place = (LayoutArea.HORIZ == owner().layout()) ? LayoutArea.VERT.name : LayoutArea.HORIZ.name;
        specificationRec.setArtiklRec(artiklRec);
        specificationRec.color1 = color1;
        specificationRec.color2 = color2;
        specificationRec.color3 = color3;
        specificationRec.discount = 0;
        specificationRec.anglHoriz = anglHoriz;

        if (LayoutArea.HORIZ == owner().layout()) { //слева направо
            ElemSimple elemPrior = prevElem();

            //Элемент слева
            String keyJoinLeft = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y1);
            ElemJoining joinLeft = iwin().mapJoin.get(keyJoinLeft);
            ElemSimple elemLeft = (joinLeft.joinElement1.equals(this)) ? joinLeft.joinElement2 : joinLeft.joinElement1;
            y1 = elemLeft.y2;
            //элемент справа
            String keyJoinRight = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y2);
            ElemJoining joinRight = iwin().mapJoin.get(keyJoinRight);
            ElemSimple elemRight = (joinRight.joinElement1.equals(this)) ? joinRight.joinElement2 : joinRight.joinElement1;
            y2 = elemRight.y1;

            specificationRec.width = y2 - y1 + iwin().sysconsRec.getFloat(eSyssize.zax) * 2 + elemLeft.artiklRec.getFloat(eArtikl.size_falz) + elemRight.artiklRec.getFloat(eArtikl.size_falz);
            specificationRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
            ElemSimple elemPrior = prevElem();

            //Элемент слева
            String keyJoinLeft = String.valueOf(elemPrior.x1) + ":" + String.valueOf(elemPrior.y2);
            ElemJoining joinLeft = iwin().mapJoin.get(keyJoinLeft);
            ElemSimple elemLeft = (joinLeft.joinElement1.equals(this)) ? joinLeft.joinElement2 : joinLeft.joinElement1;
            x1 = elemLeft.x2;
            //Элемент справа
            String keyJoinRight = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y2);
            ElemJoining joinRight = iwin().mapJoin.get(keyJoinRight);
            ElemSimple elemRight = (joinRight.joinElement1.equals(this)) ? joinRight.joinElement2 : joinRight.joinElement1;
            x2 = elemRight.x1;

            specificationRec.width = x2 - x1 + iwin().sysconsRec.getFloat(eSyssize.zax) * 2 + elemLeft.artiklRec.getFloat(eArtikl.size_falz) + elemRight.artiklRec.getFloat(eArtikl.size_falz);
            specificationRec.height = artiklRec.getFloat(eArtikl.height);
        }
        specificationRec.anglCut2 = 90;
        specificationRec.anglCut1 = 90;
*/
    }

    @Override //Вложеная спецификация      
    public void addSpecific(Specification specif) {

        Record artiklRec = specif.artiklRec;

        //Импост (если элемент включен в список состава)
        if (TypeArtikl.IMPOST.isType(artiklRec)) {
            specificationRec.name = (specif.name.equals("-")) ? specif.name : "-";
            specificationRec.setArtiklRec(specif.artiklRec);
            return;

            //Теперь армирование
        } else if (TypeArtikl.ARMIROVANIE.isType(artiklRec)) {
            specif.place = "СОСТ." + specificationRec.place.substring(0, 1);
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
    public String toString() {
        return super.toString() + ", anglCut=" + anglCut1 + ", anglCut=" + anglCut1;
    }
}
