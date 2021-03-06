package builder.model;

import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinvar;
import domain.eSysprof;
import enums.LayoutArea;
import enums.UseSide;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import builder.making.Specific;
import domain.eSyssize;
import enums.TypeJoin;
import common.Util;
import enums.PKjson;
import java.util.Arrays;
import java.util.List;

public class ElemShtulp extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм

    public ElemShtulp(AreaSimple owner, TypeElem type, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = (owner.layout() == LayoutArea.HORIZ) ? LayoutArea.VERT : LayoutArea.HORIZ;
        colorID1 = iwin().colorID1;
        colorID2 = iwin().colorID2;
        colorID3 = iwin().colorID3;
        this.type = type;

        initСonstructiv(param);

        //Коррекция положения импоста арки (подкдадка ареа над импостом)
        if ((TypeElem.ARCH == owner.type || TypeElem.TRAPEZE == owner.type) && owner.listChild.isEmpty()) {
            float dh = artiklRec.getFloat(eArtikl.height) / 2;
            owner.listChild.add(new AreaSimple(iwin(), owner, owner.id() + .1f, TypeElem.AREA, LayoutArea.HORIZ, owner.width(), dh, -1, -1, -1, null));
        }
        setLocation();
    }

    public void initСonstructiv(String param) {

        if (param(param, PKjson.sysprofID) != -1) {
            sysprofRec = eSysprof.find3(param(param, PKjson.sysprofID));
        } else {
            if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.HORIZ, UseSide.ANY);

            } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.VERT, UseSide.ANY);
            }
        }
        spcRec.place = (LayoutArea.HORIZ == owner().layout()) ? LayoutArea.VERT.name : LayoutArea.HORIZ.name;
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    //Установка координат
    public void setLocation() {
        for (int index = owner().listChild.size() - 1; index >= 0; --index) {
            if (owner().listChild.get(index).type == TypeElem.AREA) {
                Com5t prevArea = owner().listChild.get(index); //index указывает на предыдущий элемент
                float db = artiklRecAn.getFloat(eArtikl.size_centr);

                if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                    setDimension(prevArea.x1, prevArea.y2 - db, prevArea.x2, prevArea.y2 + db);
                    anglHoriz = 270;

                } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                    setDimension(prevArea.x2 - db, prevArea.y1, prevArea.x2 + db, prevArea.y2);
                    anglHoriz = 90;
                }
                break;
            }
        }
    }

    @Override //Главная спецификация
    public void setSpecific() {

        spcRec.place = (LayoutArea.HORIZ == owner().layout()) ? "ВСТ.В" : "ВСТ.Г";
        spcRec.setArtiklRec(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;
        spcRec.anglCut2 = 90;
        spcRec.anglCut1 = 90;
        spcRec.anglHoriz = anglHoriz;

        if (LayoutArea.HORIZ == owner().layout()) { //слева направо  
            spcRec.width = y2 - y1;
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
            spcRec.width = x2 - x1;
            spcRec.height = artiklRec.getFloat(eArtikl.height);
        }
    }

    //@Override //Вложеная спецификация 
    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов

        spcAdd.count = uti3.get_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += uti3.get_14050_24050_33050_38050(spcAdd); //кол. ед. с шагом
        spcAdd.width = uti3.get_12050_15050_34051_39020(spcRec, spcAdd); //поправка мм

        //Армирование
        if (TypeArtikl.isType(spcAdd.artiklDet, TypeArtikl.X107)) {
            spcAdd.place = "ВСТ." + layout().name.substring(0, 1);
            spcAdd.anglCut1 = 90;
            spcAdd.anglCut2 = 90;
        }

        if (Arrays.asList(1, 3, 5).contains(spcAdd.artiklDet.getInt(eArtikl.level1))) {
            spcAdd.width += spcRec.width;
        }
        uti3.get_12075_34075_39075(this, spcAdd); //углы реза
        uti3.get_34077_39077(spcAdd); //задать Угол_реза_1/Угол_реза_2 
        spcAdd.height = Util.getFloat(spcAdd.getParam(spcAdd.height, 40006)); ////высота заполнения, мм
        spcAdd.width = uti3.get_12065_15045_25040_34070_39070(spcRec, spcAdd); //длина мм
        spcAdd.width = Util.getFloat(spcAdd.getParam(spcAdd.width, 40004)); //ширина заполнения, мм 
        spcAdd.width = spcAdd.width * uti3.get_12030_15030_25035_34030_39030(spcRec, spcAdd);//"[ * коэф-т ]"
        spcAdd.width = spcAdd.width / uti3.get_12040_15031_25036_34040_39040(spcRec, spcAdd);//"[ / коэф-т ]"
        uti3.get_40007(spcAdd); //высоту сделать длиной
        spcAdd.count = uti3.get_11070_12070_33078_34078(spcAdd); //ставить однократно
        spcAdd.count = uti3.get_39063(spcAdd); //округлять количество до ближайшего

        spcRec.spcList.add(spcAdd);
    }

    @Override
    public void paint() {

        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        if (LayoutArea.VERT == owner().layout()) {
            iwin().draw.strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.HORIZ == owner().layout()) {
            iwin().draw.strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut1=" + anglCut[0] + ", anglCut2=" + anglCut[1];
    }
}
