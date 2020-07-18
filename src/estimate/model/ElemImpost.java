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

public class ElemImpost extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм

    public ElemImpost(AreaSimple owner, float id) {

        super(id, owner.iwin(), owner);
        this.layout = (owner.layout() == LayoutArea.HORIZ) ? LayoutArea.VERT : LayoutArea.HORIZ;
        color1 = owner.color1;
        color2 = owner.color2;
        color3 = owner.color3;
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
    }

    @Override //Главная спецификация
    public void setSpecific() {

        specificationRec.place = (LayoutArea.HORIZ == owner().layout()) ? "СОСТ.В" : "СОСТ.Г";
        specificationRec.setArtiklRec(artiklRec);
        specificationRec.color1 = color1;
        specificationRec.color2 = color2;
        specificationRec.color3 = color3;
        specificationRec.anglCut2 = 90;
        specificationRec.anglCut1 = 90;
        specificationRec.anglHoriz = anglHoriz;

        if (LayoutArea.HORIZ == owner().layout()) { //слева направо           
            ElemJoining joinLeft = iwin().mapJoin.get(x1 + (x2 - x1) / 2 + ":" + y1); //элемент слева            
            ElemJoining joinRight = iwin().mapJoin.get(x1 + (x2 - x1) / 2 + ":" + y2); //элемент справа
            specificationRec.width = joinRight.joinElement2.y1 - joinLeft.joinElement2.y2 + iwin().sysconsRec.getFloat(eSyssize.zax) * 2
                    + joinLeft.joinElement2.artiklRec.getFloat(eArtikl.size_falz)
                    + joinRight.joinElement2.artiklRec.getFloat(eArtikl.size_falz);
            specificationRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
            ElemJoining joinLeft = iwin().mapJoin.get(x1 + ":" + (y1 + (y2 - y1) / 2)); //элемент слева 
            ElemJoining joinRight = iwin().mapJoin.get(x2 + ":" + (y1 + (y2 - y1) / 2)); //элемент справа
            specificationRec.width = joinRight.joinElement2.x1 - joinLeft.joinElement2.x2 + iwin().sysconsRec.getFloat(eSyssize.zax) * 2
                    + joinLeft.joinElement2.artiklRec.getFloat(eArtikl.size_falz)
                    + joinRight.joinElement2.artiklRec.getFloat(eArtikl.size_falz);
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
            if (LayoutArea.HORIZ == owner().layout()) { //слева направо           
                ElemJoining joinLeft = iwin().mapJoin.get(x1 + (x2 - x1) / 2 + ":" + y1); //элемент слева            
                ElemJoining joinRight = iwin().mapJoin.get(x1 + (x2 - x1) / 2 + ":" + y2); //элемент справа
                specif.width = joinRight.joinElement2.y1 - joinLeft.joinElement2.y2 + iwin().sysconsRec.getFloat(eSyssize.zax) * 2
                        + joinLeft.joinElement2.artiklRec.getFloat(eArtikl.size_falz)
                        + joinRight.joinElement2.artiklRec.getFloat(eArtikl.size_falz);

            } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
                ElemJoining joinLeft = iwin().mapJoin.get(x1 + ":" + (y1 + (y2 - y1) / 2)); //элемент слева 
                ElemJoining joinRight = iwin().mapJoin.get(x2 + ":" + (y1 + (y2 - y1) / 2)); //элемент справа
                specif.width = joinRight.joinElement2.x1 - joinLeft.joinElement2.x2 + iwin().sysconsRec.getFloat(eSyssize.zax) * 2
                        + joinLeft.joinElement2.artiklRec.getFloat(eArtikl.size_falz)
                        + joinRight.joinElement2.artiklRec.getFloat(eArtikl.size_falz);
            }

            //Соединитель
        } else if (TypeArtikl.SOEDINITEL.isType(specif.artiklRec)) {
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
