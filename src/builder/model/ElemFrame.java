package builder.model;

import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import builder.calculate.SpecificRec;
import builder.calculate.SpecificAdd;
import domain.eGlasgrp;
import enums.PKjson;
import enums.ParamList;

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

        if (LayoutArea.LEFT == layout) {
            setDimension(owner().x1, owner().y1, owner().x1 + artiklRec.getFloat(eArtikl.height), owner().y2);
            anglHoriz = 270;

        } else if (LayoutArea.RIGHT == layout) {
            setDimension(owner().x2 - artiklRec.getFloat(eArtikl.height), owner().y1, owner().x2, owner().y2);
            anglHoriz = 90;

        } else if (LayoutArea.TOP == layout) {
            setDimension(owner().x1, owner().y1, owner().x2, owner().y1 + artiklRec.getFloat(eArtikl.height));
            anglHoriz = 180;

        } else if (LayoutArea.BOTTOM == layout) {
            setDimension(owner().x1, owner().y2 - artiklRec.getFloat(eArtikl.height), owner().x2, owner().y2);
            anglHoriz = 0;

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
        spcRec.anglCut2 = anglCut2;
        spcRec.anglCut1 = anglCut1;
        spcRec.anglHoriz = anglHoriz;
        float prip = iwin().syssizeRec.getFloat(eSyssize.prip);

        if (LayoutArea.ARCH == layout()) {
            AreaArch areaArch = (AreaArch) root();
            double angl = Math.toDegrees(Math.asin(width() / (areaArch.radiusArch * 2)));
            length = (float) (Math.PI * areaArch.radiusArch * angl * 2) / 180;
            spcRec.width = length + prip; // ssizp * 2; //TODO ВАЖНО !!! расчет требует корректировки
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.TOP == layout) {
            spcRec.width = x2 - x1 + prip * 2;
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.BOTTOM == layout) {
            spcRec.width = x2 - x1 + prip * 2;
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.LEFT == layout) {
            spcRec.width = y2 - y1 + prip * 2;
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.RIGHT == layout) {
            spcRec.width = y2 - y1 + prip * 2;
            spcRec.height = artiklRec.getFloat(eArtikl.height);
        }
    }

    @Override //Вложеная спецификация
    public void addSpecific(SpecificRec spcAdd) { //добавление спесификаций зависимых элементов

        spcAdd.count = spc7d.calcCount(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += spc7d.calcCountStep(this, spcAdd); //кол. ед. с шагом
        spcAdd.quant1 += spc7d.calcKitCountStep(this, spcAdd); //кол. с шагом
        spcAdd.width = spc7d.calcAmountMetr(spcRec, spcAdd); //поправка мм
        spcAdd.quant1 = spc7d.calcAmount(spcRec, spcAdd); //количество от параметра                

        //Армирование
        if (TypeArtikl.X107.isType(spcAdd.artiklRec)) {
            spcAdd.place = "ВСТ." + layout().name.substring(0, 1);
            spcAdd.anglCut1 = 90;
            spcAdd.anglCut2 = 90;

            if (LayoutArea.TOP == layout || LayoutArea.BOTTOM == layout) {
                spcAdd.width += x2 - x1;

            } else if (LayoutArea.LEFT == layout || LayoutArea.RIGHT == layout) {
                spcAdd.width += y2 - y1;
            }
            if ("от внутреннего угла".equals(spcAdd.getParam(null, 34010))) {
                Double dw1 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut1));
                Double dw2 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut2));
                spcAdd.width = spcAdd.width + 2 * iwin().syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
            }

        } else if (TypeArtikl.X109.isType(spcAdd.artiklRec)) {
            if (layout.id == Integer.valueOf(spcAdd.getParam("0", 24010, 25010, 38010, 39002))) {  //"Номер стороны"   
                if ("no".equals(spcAdd.getParam("no", 25013)) == false //"Укорочение от"
                        && spcAdd.getParam(0, 25030).equals(0) == false) { //"Укорочение, мм"  

                    spcAdd.width = spc7d.heightHand(spcRec, spcAdd); //Укорочение от высоты ручки
                }
            } else {
                spcAdd.width += width() + iwin().syssizeRec.getFloat(eSyssize.prip) * 2;
            }
        } else {
            if (spcAdd.artiklRec.getInt(eArtikl.level1) == 1
                    || spcAdd.artiklRec.getInt(eArtikl.level1) == 3
                    || spcAdd.artiklRec.getInt(eArtikl.level1) == 5) {
                spcAdd.width += spcRec.width;
            }
        }
        spcAdd.width = spc7d.calcAmountLenght(spcRec, spcAdd); //длина мм
        spcAdd.width = spcAdd.width * spc7d.calcCoeff(spcRec, spcAdd);//"[ * коэф-т ]"

        spcRec.spcList.add(spcAdd);
    }

    @Override
    public void paint() {
        float d1z = artiklRec.getFloat(eArtikl.height);
        float h = iwin().heightAdd - iwin().height;
        float w = root().width();
        float y1h = y1 + h;
        float y2h = y2 + h;

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

        } else if (LayoutArea.BOTTOM == layout) {
            iwin().draw.strokePolygon(x1 + d1z, x2 - d1z, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.LEFT == layout) {
            if (TypeElem.ARCH == owner().type) {
                double r = ((AreaArch) root()).radiusArch;
                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * d1z) / ((r - d1z) * 2)));
                double a = (r - d1z) * Math.sin(Math.toRadians(ang2));
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1, (float) (r - a - h), y2 - d1z, y2, rgb, borderColor);
            } else {
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1, y1 + d1z, y2 - d1z, y2, rgb, borderColor);
            }
        } else if (LayoutArea.RIGHT == layout) {
            if (TypeElem.ARCH == owner().type) {
                double r = ((AreaArch) root()).radiusArch;
                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * d1z) / ((r - d1z) * 2)));
                double a = (r - d1z) * Math.sin(Math.toRadians(ang2));
                iwin().draw.strokePolygon(x1, x2, x2, x1, (float) (r - a - h), y1, y2, y2 - d1z, rgb, borderColor);
            } else {
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1 + d1z, y1, y2, y2 - d1z, rgb, borderColor);
            }
        }
    }

    @Override
    public UseArtiklTo useArtiklTo() {
        return (TypeElem.STVORKA == owner().type) ? UseArtiklTo.STVORKA : UseArtiklTo.FRAME;
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut=" + anglCut1 + ", anglCut=" + anglCut1;
    }
}
