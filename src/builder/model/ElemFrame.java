package builder.model;

import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import builder.making.Specific;
import builder.making.Uti3;
import domain.eGlasgrp;
import enums.PKjson;
import builder.param.ParamList;
import java.util.Arrays;

public class ElemFrame extends ElemSimple {

    protected float length = 0; //длина арки 

    public ElemFrame(AreaSimple owner, float id, LayoutArea layout, String param) {
        super(id, owner.iwin(), owner);
        this.layout = layout;
        colorID1 = iwin().colorID1;
        colorID2 = iwin().colorID2;
        colorID3 = iwin().colorID3;
        this.type = (TypeElem.STVORKA == owner.type) ? TypeElem.STVORKA_SIDE : TypeElem.FRAME_SIDE;
        initСonstructiv(param);
        setLocation();
    }

    public void initСonstructiv(String param) {

        colorID1 = (param(param, PKjson.colorID1) != -1) ? param(param, PKjson.colorID1) : colorID1;
        colorID2 = (param(param, PKjson.colorID2) != -1) ? param(param, PKjson.colorID2) : colorID2;
        colorID3 = (param(param, PKjson.colorID3) != -1) ? param(param, PKjson.colorID3) : colorID3;

        sysprofRec = (param(param, PKjson.sysprofID) != -1) ? eSysprof.find3(param(param, PKjson.sysprofID)) : owner().sysprofRec;
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);

        //Record rec = eGlasgrp.find(colorID1)
    }

    //Установка координат
    public void setLocation() {

        if (LayoutArea.BOTT == layout) {
            setDimension(owner().x1, owner().y2 - artiklRec.getFloat(eArtikl.height), owner().x2, owner().y2);
            anglHoriz = 0;
            
        } else if (LayoutArea.RIGHT == layout) {
            setDimension(owner().x2 - artiklRec.getFloat(eArtikl.height), owner().y1, owner().x2, owner().y2);
            anglHoriz = 90;  

        } else if (LayoutArea.TOP == layout) {
            setDimension(owner().x1, owner().y1, owner().x2, owner().y1 + artiklRec.getFloat(eArtikl.height));
            anglHoriz = 180;
            
        } else if (LayoutArea.LEFT == layout) {
            setDimension(owner().x1, owner().y1, owner().x1 + artiklRec.getFloat(eArtikl.height), owner().y2);
            anglHoriz = 270;

        } else if (LayoutArea.ARCH == layout) {
            setDimension(owner().x1, owner().y1, owner().x2, owner().y1 + artiklRec.getFloat(eArtikl.height));
            anglHoriz = 180;
        }
    }

    @Override //Главная спецификация
    public void setSpecific() {  //добавление основной спесификации

        spcRec.place = "ВСТ." + layout().name.substring(0, 1);
        spcRec.setArtiklRec(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;
        spcRec.anglCut2 = anglCut[1];
        spcRec.anglCut1 = anglCut[0];
        spcRec.anglHoriz = anglHoriz;
        double katet = iwin().syssizeRec.getDbl(eSyssize.prip) * Math.cos(Math.PI / 4);

        if (LayoutArea.ARCH == layout()) {
            ((AreaArch) root()).frame(this, katet);

        } else if (LayoutArea.TOP == layout) {
            spcRec.width = x2 - x1 + (float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.BOTT == layout) {
            spcRec.width = x2 - x1 + +(float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.LEFT == layout) {
            spcRec.width = y2 - y1 + (float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.RIGHT == layout) {
            spcRec.width = y2 - y1 + (float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);
        }
    }

    @Override //Вложеная спецификация
    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов

        spcAdd.count = uti3.p_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += uti3.p_11050_14050_24050_33050_38050(this, spcAdd); //кол. ед. с шагом
        spcAdd.width = uti3.p_12050_15050_34050_34051_39020(spcRec, spcAdd); //поправка мм
        uti3.p_34077_34078(spcAdd); //задать Угол_реза_1/Угол_реза_2

        //Армирование
        if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X107)) {
            spcAdd.place = "ВСТ." + layout().name.substring(0, 1);
            spcAdd.anglCut1 = 90;
            spcAdd.anglCut2 = 90;

            if (LayoutArea.TOP == layout || LayoutArea.BOTT == layout) {
                spcAdd.width += x2 - x1;

            } else if (LayoutArea.LEFT == layout || LayoutArea.RIGHT == layout) {
                spcAdd.width += y2 - y1;
            }
            if ("от внутреннего угла".equals(spcAdd.getParam(null, 34010))) {
                Double dw1 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[0]));
                Double dw2 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[1]));
                spcAdd.width = spcAdd.width + 2 * iwin().syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
            }

            //Фурнитура
        } else if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X109)) {
            if (layout.id == Integer.valueOf(spcAdd.getParam("0", 24010, 25010, 38010, 39002))) {  //"Номер стороны"   
                if ("no".equals(spcAdd.getParam("no", 25013)) == false //"Укорочение от"
                        && spcAdd.getParam(0, 25030).equals(0) == false) { //"Укорочение, мм"  
                    spcAdd.width = uti3.p_25013(spcRec, spcAdd); //Укорочение от высоты ручки
                }
            } else {
                spcAdd.width += width() + iwin().syssizeRec.getFloat(eSyssize.prip) * 2;
            }
            //Фурнитура штучная
        } else if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X210)) {
            //spcAdd.width = spcAdd.width + height();

        } else {
            if (Arrays.asList(1, 3, 5).contains(spcAdd.artiklRec.getInt(eArtikl.level1))) {
                spcAdd.width += spcRec.width;
            }
        }
        spcAdd.width = uti3.p_12065_15045_25040_34070_39070(spcRec, spcAdd); //длина мм
        spcAdd.width = spcAdd.width * uti3.p_12030_15030_25035_34030_39030(spcRec, spcAdd);//"[ * коэф-т ]"
        spcAdd.width = spcAdd.width / uti3.p_12040_15031_25036_34040_39040(spcRec, spcAdd);//"[ / коэф-т ]"

        spcRec.spcList.add(spcAdd);
    }

    @Override
    public void paint() {
        float d1z = artiklRec.getFloat(eArtikl.height);
        float w = root().width();

        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        if (LayoutArea.ARCH == layout) { //прорисовка арки
            //TODO для прорисовки арки добавил один градус, а это не айс!
            float d2z = artiklRec.getFloat(eArtikl.height);
            double r = ((AreaArch) root()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(owner().width() / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((owner().width() - 2 * d2z) / ((r - d2z) * 2)));

            iwin().draw.strokeArc(owner().width() / 2 - r + d2z / 2, d2z / 2 - 2, (r - d2z / 2) * 2, (r - d2z / 2) * 2, ang2, (90 - ang2) * 2 + 1, rgb, d2z);
            iwin().draw.strokeArc(owner().width() / 2 - r, -4, r * 2, r * 2, ang1, (90 - ang1) * 2 + 1, 0, 4);
            iwin().draw.strokeArc(owner().width() / 2 - r + d2z, d2z - 2, (r - d2z) * 2, (r - d2z) * 2, ang2, (90 - ang2) * 2 + 1, 0, 4);

        } else if (LayoutArea.TOP == layout) {
            iwin().draw.strokePolygon(x1, x2, x2 - d1z, x1 + d1z, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.BOTT == layout) {
            iwin().draw.strokePolygon(x1 + d1z, x2 - d1z, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.LEFT == layout) {
            if (TypeElem.ARCH == owner().type) {
                double r = ((AreaArch) root()).radiusArch;
                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * d1z) / ((r - d1z) * 2)));
                double a = (r - d1z) * Math.sin(Math.toRadians(ang2));
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1, (float) (r - a), y2 - d1z, y2, rgb, borderColor);
            } else {
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1, y1 + d1z, y2 - d1z, y2, rgb, borderColor);
            }
        } else if (LayoutArea.RIGHT == layout) {
            if (TypeElem.ARCH == owner().type) {
                double r = ((AreaArch) root()).radiusArch;
                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * d1z) / ((r - d1z) * 2)));
                double a = (r - d1z) * Math.sin(Math.toRadians(ang2));
                iwin().draw.strokePolygon(x1, x2, x2, x1, (float) (r - a), y1, y2, y2 - d1z, rgb, borderColor);
            } else {
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1 + d1z, y1, y2, y2 - d1z, rgb, borderColor);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut1=" + anglCut[0] + ", anglCut2=" + anglCut[1];
    }
}
