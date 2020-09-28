package estimate.model;

import frames.swing.Draw;
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
import estimate.constr.Cal5e;

public class ElemImpost extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм

    public ElemImpost(AreaSimple owner, float id) {

        super(id, owner.iwin(), owner);
        this.layout = (owner.layout() == LayoutArea.HORIZ) ? LayoutArea.VERT : LayoutArea.HORIZ;
        colorID1 = iwin().colorID1;
        colorID2 = iwin().colorID2;
        colorID3 = iwin().colorID3;
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
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    @Override //Главная спецификация
    public void setSpecific() {

        specificationRec.place = (LayoutArea.HORIZ == owner().layout()) ? "СОСТ.В" : "СОСТ.Г";
        specificationRec.setArtiklRec(artiklRec);
        specificationRec.colorID1 = colorID1;
        specificationRec.colorID2 = colorID2;
        specificationRec.colorID3 = colorID3;
        specificationRec.anglCut2 = 90;
        specificationRec.anglCut1 = 90;
        specificationRec.anglHoriz = anglHoriz;
        float zax = iwin().syssizeRec.getFloat(eSyssize.zax);

        if (LayoutArea.HORIZ == owner().layout()) { //слева направо  
            ElemSimple insideTop = join(LayoutArea.TOP), insideBott = join(LayoutArea.BOTTOM);
            specificationRec.width = insideBott.y1 - insideTop.y2 + zax * 2 + insideBott.artiklRec.getFloat(eArtikl.size_falz) + insideTop.artiklRec.getFloat(eArtikl.size_falz);
            specificationRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
            ElemSimple insideLeft = join(LayoutArea.LEFT), insideRight = join(LayoutArea.RIGHT);
            specificationRec.width = insideRight.x1 - insideLeft.x2 + zax * 2 + insideLeft.artiklRec.getFloat(eArtikl.size_falz) + insideRight.artiklRec.getFloat(eArtikl.size_falz);
            specificationRec.height = artiklRec.getFloat(eArtikl.height);
        }
    }

    @Override //Вложеная спецификация      
    public void addSpecific(Specification specif) {

        //Импост (если элемент включен в список состава)
        if (TypeArtikl.IMPOST.isType(specif.artiklRec)) {
            artiklRec = specif.artiklRec; //переназначаем артикл, как правило это c префиксом артикла @
            setSpecific(); //дополнительно если был фиктивный профиль, т.е. с префиксом @
            return; //сразу выход т.к. элем. сам является держателем состава

            //Теперь армирование
        } else if (TypeArtikl.ARMIROVANIE.isType(specif.artiklRec)) {
            specif.place = "СОСТ." + layout().name.substring(0, 1);
            specif.anglCut2 = 90;
            specif.anglCut1 = 90;
            specif.width = specificationRec.width;

            //Соединитель
        } else if (TypeArtikl.SOEDINITEL.isType(specif.artiklRec)) {
            //specif.colorID1 = iwin().colorNone;
            //specif.colorID2 = iwin().colorNone;
            //specif.colorID3 = iwin().colorNone;

            //Остальные
        } else {
            //
        }
        Cal5e.amount(specificationRec, specif);
        specificationRec.specificationList.add(specif);
    }

    @Override
    public void paint() {

        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        if (LayoutArea.VERT == owner().layout()) {
            Draw.strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.HORIZ == owner().layout()) {
            Draw.strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);
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
