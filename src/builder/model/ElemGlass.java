package builder.model;

import builder.making.Filling;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSystree;
import enums.Layout;
import enums.TypeArtikl;
import builder.making.Specific;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import common.Interface.IElem;
import common.UCom;
import domain.eGlasprof;
import enums.Form;
import enums.PKjson;
import enums.Type;
import enums.UseUnit;

public class ElemGlass extends ElemSimple implements IElem {

    public float radiusGlass = 0; //радиус стекла
    public float gzazo = 0; //зазор между фальцем и стеклопакетом 
    public float sideHoriz[] = {0, 90, 180, 270}; //угол боковой стороны к горизонту
    public float gsize[] = {0, 0, 0, 0}; //размер от оси до стеклопакета

    public ElemGlass(AreaSimple owner, GsonElem gson) {
        super(gson.id(), owner.winc, owner, gson);
        this.layout = Layout.FULL;

        initСonstructiv(gson.param());
        setLocation();

        Filling filling = new Filling(winc, true);
        filling.calc2(this);
    }

    public void initСonstructiv(JsonObject param) {

        if (isJson(param, PKjson.artglasID)) {
            artiklRec = eArtikl.find(param.get(PKjson.artglasID).getAsInt(), false);
        } else {
            Record sysreeRec = eSystree.find(winc.nuni); //по умолчанию стеклопакет
            artiklRec = eArtikl.find2(sysreeRec.getStr(eSystree.glas));
        }
        artiklRecAn = artiklRec;

        //Цвет стекла
        if (isJson(param, PKjson.colorGlass)) {
            colorID1 = param.get(PKjson.colorGlass).getAsInt();
        } else {
            Record artdetRec = eArtdet.find2(artiklRec.getInt(eArtikl.id));
            Record colorRec = eColor.find3(artdetRec.getInt(eArtdet.color_fk));
            colorID1 = colorRec.getInt(eColor.id);
            colorID2 = colorRec.getInt(eColor.id);
            colorID3 = colorRec.getInt(eColor.id);
        }
    }

    //Установка координат
    public void setLocation() {
        if (Type.ARCH == owner.type()) {
            setDimension(0, 0, owner.x2, winc.height - winc.heightAdd);
        } else {
            setDimension(owner.x1, owner.y1, owner.x2, owner.y2);
        }
    }    

    //Главная спецификация
    @Override 
    public void setSpecific() {

        spcRec.place = "ЗАП";
        spcRec.setArtiklRec(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;
        if (owner.type() == Type.ARCH) { //если арка
            ElemFrame elemArch = root().frames.get(Layout.TOP);
            ElemSimple elemImpost = joinFlat(Layout.BOTT);
            y1 = y1 + elemArch.artiklRec.getFloat(eArtikl.height) - elemArch.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
            y2 = y2 + elemImpost.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            double r = ((AreaArch) root()).radiusArch - elemArch.artiklRec.getFloat(eArtikl.height) + elemArch.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            double l = Math.sqrt(2 * height() * r - height() * height());
            x1 = (owner.width() / 2) - (float) l;
            x2 = owner.width() - x1;
            radiusGlass = (float) r;

        } else if (owner.type() == Type.TRAPEZE) {
            ElemSimple insideLeft = root().frames.get(Layout.LEFT), insideTop = root().frames.get(Layout.TOP), insideBott = joinFlat(Layout.BOTT), insideRight = root().frames.get(Layout.RIGHT);
            if (winc.form == Form.RIGHT) {
                x1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                ElemJoining ej = winc.mapJoin.get(insideTop.joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - (insideTop.artiklRec.getFloat(eArtikl.size_falz) - gzazo)) / UCom.cos(90 - ej.angl);
                float dy2 = (insideLeft.artiklRec.getFloat(eArtikl.height) - (insideLeft.artiklRec.getFloat(eArtikl.size_falz) - gzazo)) * UCom.tan(90 - ej.angl);
                y1 = insideTop.y1 + dy1 + dy2;
                x2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
                y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz) - gzazo;

            } else if (winc.form == Form.LEFT) {
                x1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                ElemJoining ej = winc.mapJoin.get(insideTop.joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - (insideTop.artiklRec.getFloat(eArtikl.size_falz) - gzazo)) / UCom.cos(90 - ej.angl);
                float dy2 = (insideRight.artiklRec.getFloat(eArtikl.height) - (insideRight.artiklRec.getFloat(eArtikl.size_falz) - gzazo)) * UCom.tan(90 - ej.angl);
                y2 = insideTop.y2 + dy1 - dy2;
                x2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
                y1 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            }

        } else {
            ElemSimple insideLeft = joinFlat(Layout.LEFT), insideTop = joinFlat(Layout.TOP), insideBott = joinFlat(Layout.BOTT), insideRight = joinFlat(Layout.RIGHT);

            if (winc.syssizeRec.getInt(eSyssize.id) == -1) {
                y2 = insideBott.y2 - insideBott.artiklRec.getFloat(eArtikl.size_centr) - gsize[0];
                x2 = insideRight.x2 - insideRight.artiklRec.getFloat(eArtikl.size_centr) - gsize[1];
                y1 = insideTop.y1 + insideTop.artiklRec.getFloat(eArtikl.size_centr) + gsize[2];
                x1 = insideLeft.x1 + insideLeft.artiklRec.getFloat(eArtikl.size_centr) + gsize[3];
            } else {
                x1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                y1 = insideTop.y2 - insideTop.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                x2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
                y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            }
        }
        spcRec.width = width();
        spcRec.height = height();
    }

    //Вложенная спецификация
    @Override  
    public void addSpecific(Specific spcAdd) {
        if (Type.ARCH == owner.type() && (anglHoriz == sideHoriz[1] || anglHoriz == sideHoriz[3])) {
            return;  //нет таких сторон у арки
        }
        spcAdd.count = UMod.get_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += UMod.get_14050_24050_33050_38050(spcRec, spcAdd); //кол. ед. с шагом
        spcAdd.width = UMod.get_12050_15050_34051_39020(spcRec, spcAdd); //поправка мм         
        if (TypeArtikl.X502.isType(spcAdd.artiklRec)) {
            return;  //если стеклопакет сразу выход
        }

        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) {
            //ARCH
            if (Type.ARCH == owner.type()) {
                ((AreaArch) root()).addFilling(this, spcAdd);

                //TRAPEZE
            } else if (Type.TRAPEZE == owner.type()) {
                ((AreaTrapeze) root()).addFilling(this, spcAdd);

                //STVORKA
            } else if (Type.STVORKA == owner.type()) {
                ((AreaStvorka) owner).addFilling(this, spcAdd);

                //AREA
            } else {
                if (anglHoriz == sideHoriz[0] || anglHoriz == sideHoriz[2]) { //по горизонтали
                    spcAdd.width += width() + 2 * gzazo;
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);

                } else if (anglHoriz == sideHoriz[1] || anglHoriz == sideHoriz[3]) { //по вертикали
                    spcAdd.width += height() + 2 * gzazo;
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);

                } else {
                    System.out.println("Промах:builder.model.AreaSimple.addFilling()");
                }
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = 45;
                spcRec.spcList.add(spcAdd);
            }

            if (anglHoriz == sideHoriz[0] || anglHoriz == sideHoriz[2]) { //по горизонтали
                if (spcAdd.mapParam.get(15010) != null) {
                    if ("Нет".equals(spcAdd.mapParam.get(15010)) == false) { //Усекать нижний штапик
                        spcAdd.width = spcAdd.width - 2 * spcAdd.height;
                    }
                }
                if (spcAdd.mapParam.get(15011) != null) {
                    if ("усекать боковой".equals(spcAdd.mapParam.get(15011))) { //Расчет реза штапика
                        spcAdd.width = spcAdd.width - 2 * spcAdd.height;
                    }
                }

            } else if (anglHoriz == sideHoriz[1] || anglHoriz == sideHoriz[3]) { //по вертикали
                if (spcAdd.mapParam.get(15010) != null) {
                    if ("Да".equals(spcAdd.mapParam.get(15010)) == false) { //Усекать нижний штапик
                        spcAdd.width = spcAdd.width - 2 * spcAdd.height;
                    }
                }
                if (spcAdd.mapParam.get(15011) != null) {
                    if ("усекать нижний".equals(spcAdd.mapParam.get(15011))) { //Расчет реза штапика
                        spcAdd.width = spcAdd.width - 2 * spcAdd.height;
                    }
                }
            }
            if ("по биссектрисе".equals(spcAdd.mapParam.get(15011))) { //Расчет реза штапика
                //
            }
            spcAdd.width = UMod.get_12065_15045_25040_34070_39070(spcRec, spcAdd); //длина мм
            spcAdd.width = spcAdd.width * UMod.get_12030_15030_25035_34030_39030(spcRec, spcAdd); //"[ * коэф-т ]"
            spcAdd.width = spcAdd.width / UMod.get_12040_15031_25036_34040_39040(spcRec, spcAdd); //"[ / коэф-т ]" 

        } else if (UseUnit.PIE.id == spcAdd.artiklRec.getInt(eArtikl.unit)) {

            if (spcAdd.mapParam.get(13014) != null) {
                if (UCom.containsNumbJust(spcAdd.mapParam.get(13014), anglHoriz) == true) { //Углы ориентации стороны
                    spcRec.spcList.add(spcAdd);
                }
            } else {
                spcRec.spcList.add(spcAdd);
            }
        } else {
            System.out.println("Элемент не обработан");
        }
    }

    @Override
    public void paint() { //рисуём стёкла

        Record colorRec = eColor.find3(colorID1);
        winc.gc2d.setColor(new java.awt.Color(colorRec.getInt(eColor.rgb)));

        if (owner.type() == Type.ARCH) {
            ElemFrame ef = root().frames.get(Layout.TOP);
            float dz = ef.artiklRec.getFloat(eArtikl.height);
            double r = ((AreaArch) root()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(root().width() / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((root().width() - 2 * dz) / ((r - dz) * 2)));
            winc.gc2d.fillArc((int) (root().width() / 2 - r + dz), (int) dz, (int) ((r - dz) * 2), (int) ((r - dz) * 2), (int) ang2, (int) ((90 - ang2) * 2));

        } else if (root().type() == Type.TRAPEZE) {
            if (winc.form == Form.RIGHT) {
                winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                        new int[]{(int) y1, (int) (root().height() - winc.heightAdd), (int) y2, (int) y2}, 4);
            } else if (winc.form == Form.LEFT) {
                winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                        new int[]{(int) (root().height() - winc.heightAdd), (int) y1, (int) y2, (int) y2}, 4);
            }
        } else {
            winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                    new int[]{(int) y1, (int) y1, (int) y2, (int) y2}, 4);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", radiusGlass=" + radiusGlass;
    }
}
