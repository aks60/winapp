package builder.model;

import builder.IArea5e;
import builder.IElem5e;
import builder.geoms.PointPoligon3;
import builder.geoms.UGeo;
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
import common.UCom;
import common.listener.ListenerReload;
import enums.Form;
import enums.PKjson;
import enums.Type;
import enums.TypeJoin;
import enums.UseUnit;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class ElemGlass extends ElemSimple {

    public double radiusGlass = 0; //радиус стекла
    public double gzazo = 0; //зазор между фальцем и стеклопакетом 
    public double gsize[] = {0, 0, 0, 0}; //размер от оси до стеклопакета

    private Record rasclRec = eArtikl.virtualRec(); //раскладка
    private int rasclColor = -3; //цвет раскладки
    private int rasclNumber[] = {2, 2}; //количество проёмов раскладки 

    public ElemGlass(IArea5e owner, GsonElem gson) {
        super(gson.id(), owner.winc(), owner, gson);
        this.layout = Layout.FULL;

        initСonstructiv(gson.param());
        setLocation();

        //Фича определения gzazo и gsize на раннем этапе построения. 
        //Используются до выполнения конструктива в ElemGlass.setSpecific()
        Filling filling = new Filling(winc, true);
        filling.calc(this);
    }

    public void initСonstructiv(JsonObject param) {

        if (isJson(param, PKjson.artglasID)) {
            artiklRec(eArtikl.find(param.get(PKjson.artglasID).getAsInt(), false));
        } else {
            Record sysreeRec = eSystree.find(winc.nuni()); //по умолчанию стеклопакет
            artiklRec(eArtikl.find2(sysreeRec.getStr(eSystree.glas)));
        }
        artiklRecAn(artiklRec);

        //Цвет стекла
        if (isJson(param, PKjson.colorGlass)) {
            colorID1 = param.get(PKjson.colorGlass).getAsInt();
        } else {
            Record artdetRec = eArtdet.find(artiklRec().getInt(eArtikl.id));
            Record colorRec = eColor.find3(artdetRec.getInt(eArtdet.color_fk));
            colorID1 = colorRec.getInt(eColor.id);
            colorID2 = colorRec.getInt(eColor.id);
            colorID3 = colorRec.getInt(eColor.id);
        }

        //Раскладка
        if (isJson(param, PKjson.artiklRascl)) {
            rasclRec = eArtikl.find(param.get(PKjson.artiklRascl).getAsInt(), false);
            //Текстура
            if (isJson(param, PKjson.colorRascl)) {
                rasclColor = eColor.get(param.get(PKjson.colorRascl).getAsInt()).getInt(eColor.id);
            } else {
                rasclColor = eArtdet.find(rasclRec.getInt(eArtikl.id)).getInt(eArtdet.color_fk); //цвет по умолчанию
            }
            //Проёмы гориз.
            if (isJson(param, PKjson.rasclHor)) {
                rasclNumber[0] = param.get(PKjson.rasclHor).getAsInt();
            }
            //Проёмы вертик.
            if (isJson(param, PKjson.rasclVert)) {
                rasclNumber[1] = param.get(PKjson.rasclVert).getAsInt();
            }
        }
    }

    /**
     * Установка координат заполнений с учётов типа конст. x1y1 - верхняя левая
     * точка x2y2 - нижняя правая точка
     */
    public void setLocation() {
        if (Type.ARCH == owner.type()) {
            setDimension(0, 0, owner.x2(), Math.abs(winc.height1() - winc.height2()));
        } else {
            setDimension(owner.x1(), owner.y1(), owner.x2(), owner.y2());
        }
    }

    //Главная спецификация    
    @Override
    public void setSpecific() {

        spcRec.place = "ЗАП";
        spcRec.setArtikl(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;
        if (owner.type() == Type.ARCH) { //если арка
            IElem5e elemArch = root().frames().get(Layout.TOP);
            IElem5e elemImpost = owner.joinSide(Layout.BOTT);
            y1 = y1 + elemArch.artiklRec().getDbl(eArtikl.height) - elemArch.artiklRec().getDbl(eArtikl.size_falz) + gzazo;
            y2 = y2 + elemImpost.artiklRec().getDbl(eArtikl.size_falz) - gzazo;
            double r = ((AreaArch) root()).radiusArch - elemArch.artiklRec().getDbl(eArtikl.height) + elemArch.artiklRec().getDbl(eArtikl.size_falz) - gzazo;
            double w = Math.sqrt(2 * height() * r - height() * height());
            x1 = (owner.width() / 2) - w;
            x2 = owner.width() - x1;
            radiusGlass = r;

        } else if (owner.type() == Type.TRAPEZE && winc.form != Form.SYMM) {

            IElem5e inLeft = root().frames().get(Layout.LEFT), inTop = root().frames().get(Layout.TOP), inBott = owner.joinSide(Layout.BOTT), inRight = root().frames().get(Layout.RIGHT);
            x1 = inLeft.x1() + inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_falz) + gzazo;
            x2 = inRight.x1() - inRight.artiklRec().getDbl(eArtikl.height) + inRight.artiklRec().getDbl(eArtikl.size_falz) - gzazo;

            if (winc.form == Form.RIGHT) {
                ElemJoining ej = winc.listJoin.get(inTop, 1);
                double dy1 = (inTop.artiklRec().getDbl(eArtikl.height) - inTop.artiklRec().getDbl(eArtikl.size_falz) + gzazo) / UGeo.sin(ej.angl);
                double dy2 = (inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_falz) + gzazo) * UGeo.tan(90 - ej.angl);
                y1 = dy1 + dy2;
            } else if (winc.form == Form.LEFT) {
                ElemJoining ej = winc.listJoin.get(inTop, 0);
                double dy1 = (inTop.artiklRec().getDbl(eArtikl.height) - inTop.artiklRec().getDbl(eArtikl.size_falz) + gzazo) / UGeo.sin(ej.angl);
                double dy2 = (inRight.artiklRec().getDbl(eArtikl.height) - inRight.artiklRec().getDbl(eArtikl.size_falz) + gzazo) * UGeo.tan(90 - ej.angl);
                y1 = dy1 + dy2;
            }
            y2 = inBott.y2() - (inBott.artiklRec().getDbl(eArtikl.height) - inBott.artiklRec().getDbl(eArtikl.size_centr)) + inBott.artiklRec().getDbl(eArtikl.size_falz) - gzazo;

        } else {
            IElem5e inLeft = owner.joinSide(Layout.LEFT), inTop = owner.joinSide(Layout.TOP), inBott = owner.joinSide(Layout.BOTT), inRight = owner.joinSide(Layout.RIGHT);

            if (winc.syssizeRec().getInt(eSyssize.id) == -1) {
                x1 = x1 + inLeft.artiklRec().getDbl(eArtikl.size_centr) + gsize[3];
                y1 = y1 + inTop.artiklRec().getDbl(eArtikl.size_centr) + gsize[2];
                x2 = x2 - inRight.artiklRec().getDbl(eArtikl.size_centr) - gsize[1];
                y2 = y2 - inBott.artiklRec().getDbl(eArtikl.size_centr) - gsize[0];
            } else {
                x1 = x1 + inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_centr) - inLeft.artiklRec().getDbl(eArtikl.size_falz) + gzazo;
                y1 = y1 + inTop.artiklRec().getDbl(eArtikl.height) - inTop.artiklRec().getDbl(eArtikl.size_centr) - inTop.artiklRec().getDbl(eArtikl.size_falz) + gzazo;
                x2 = x2 - inRight.artiklRec().getDbl(eArtikl.height) + inRight.artiklRec().getDbl(eArtikl.size_centr) + inRight.artiklRec().getDbl(eArtikl.size_falz) - gzazo;
                y2 = y2 - inBott.artiklRec().getDbl(eArtikl.height) + inBott.artiklRec().getDbl(eArtikl.size_centr) + inBott.artiklRec().getDbl(eArtikl.size_falz) - gzazo;
            }
        }
        spcRec.width = width();
        spcRec.height = height();
    }

    //Вложенная спецификация
    @Override
    public void addSpecific(Specific spcAdd) {
        try {
            if (Type.ARCH == owner.type() && (anglHoriz == 90 || anglHoriz == 270)) {
                return;  //нет таких сторон у арки
            }
            spcAdd.count = UPar.to_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcAdd); //кол. ед. с учётом парам. 
            spcAdd.count += UPar.to_14050_24050_33050_38050(spcRec, spcAdd); //кол. ед. с шагом
            spcAdd.width += UPar.to_12050_15050_34051_39020(spcAdd); //поправка мм         
            if (TypeArtikl.X502.isType(spcAdd.artiklRec)) {
                return;  //если стеклопакет сразу выход
            }

            if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) {

                if (Type.ARCH == owner.type()) {
                    // <editor-fold defaultstate="collapsed" desc="ARCH"> 
                    Double dw = spcAdd.width;
                    IElem5e imp = owner().joinSide(Layout.BOTT);
                    IElem5e arch = root.frames().get(Layout.TOP);
                    double radiusArch = ((AreaArch) winc.rootArea).radiusArch;

                    if (anglHoriz() == 0) { //по основанию арки
                        double r1 = radiusArch - arch.artiklRec().getDbl(eArtikl.height) + arch.artiklRec().getDbl(eArtikl.size_falz); //внешний радиус штапика
                        double h1 = imp.y1() - imp.artiklRec().getDbl(eArtikl.height) + imp.artiklRec().getDbl(eArtikl.size_centr)
                                + imp.artiklRec().getDbl(eArtikl.size_falz) - arch.artiklRec().getDbl(eArtikl.height) + arch.artiklRec().getDbl(eArtikl.size_falz);
                        double w1 = Math.sqrt((2 * r1 * h1) - (h1 * h1)); //длина нижней стороны штапика
                        double r2 = r1 - spcAdd.artiklRec.getDbl(eArtikl.height); //внутренний радиус
                        double h2 = h1 - 2 * spcAdd.artiklRec.getDbl(eArtikl.height);
                        double w2 = Math.sqrt((2 * r2 * h2) - (h2 * h2)); //длина верхней стороны штапика
                        double ang1 = Math.toDegrees(Math.atan(spcAdd.artiklRec.getDbl(eArtikl.height) / (w1 - w2))); //угол реза
                        spcAdd.width = (2 * w1 + dw);
                        spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                        spcAdd.anglCut1 = ang1;
                        spcAdd.anglCut0 = ang1;
                        spcRec().spcList.add(spcAdd); //добавим спецификацию

                    } else if (anglHoriz() == 180) { //по дуге арки   
                        if (spcAdd.artiklRec.getInt(eArtikl.level1) == 1 && spcAdd.artiklRec.getInt(eArtikl.level2) == 8) { //Штапик

                            double r1 = radiusArch - arch.artiklRec().getDbl(eArtikl.height) + arch.artiklRec().getDbl(eArtikl.size_falz); //внешний радиус штапика
                            double h1 = imp.y1() - imp.artiklRec().getDbl(eArtikl.height) + imp.artiklRec().getDbl(eArtikl.size_centr)
                                    + imp.artiklRec().getDbl(eArtikl.size_falz) - arch.artiklRec().getDbl(eArtikl.height);
                            double w1 = Math.sqrt((2 * r1 * h1) - (h1 * h1)); //длина нижней стороны основания арки штапика
                            double r2 = r1 - spcAdd.artiklRec.getDbl(eArtikl.height); //внутренний радиус
                            double h2 = h1 - 2 * spcAdd.artiklRec.getDbl(eArtikl.height);
                            double w2 = Math.sqrt((2 * r2 * h2) - (h2 * h2)); //длина верхней стороны основания арки штапика   
                            double ang1 = Math.toDegrees(Math.atan(spcAdd.artiklRec.getDbl(eArtikl.height) / (w1 - w2))); //угол реза
                            double ang2 = Math.toDegrees(Math.asin(w1 / r1)); //угол дуги арки
                            //double ang2 = Math.toDegrees(Math.asin((owner.width() / 2) / radiusArch)); //угол дуги арки
                            double w4 = ((2 * Math.PI * r1) / 360) * ang2 * 2; //длина верхней стороны арки штапика
                            double ang3 = 90 - (90 - ang2 + ang1);
                            spcAdd.width = (dw + w4);
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut1 = ang3;
                            spcAdd.anglCut0 = ang3;
                            spcRec().spcList.add(spcAdd); //добавим спецификацию

                        } else {
                            //В PS4 длина уплотнения равна длине штапика, в SA нет.
                            double r1 = radiusArch - arch.artiklRec().getDbl(eArtikl.height) + spcAdd.artiklRec.getDbl(eArtikl.height); //внешний радиус уплотнения
                            double h1 = imp.y1() - imp.artiklRec().getDbl(eArtikl.size_centr) - arch.artiklRec().getDbl(eArtikl.height);
                            double w1 = Math.sqrt((2 * r1 * h1) - (h1 * h1)); //длина нижней стороны основания арки уплотнения
                            double r2 = r1 - spcAdd.artiklRec.getDbl(eArtikl.height); //внутренний радиус
                            double h2 = h1 - 2 * spcAdd.artiklRec.getDbl(eArtikl.height);
                            double w2 = Math.sqrt((2 * r2 * h2) - (h2 * h2)); //длина верхней стороны основания арки уплотнения   
                            double ang1 = Math.toDegrees(Math.atan(spcAdd.artiklRec.getDbl(eArtikl.height) / (w1 - w2))); //угол реза
                            double ang2 = Math.toDegrees(Math.asin(w1 / r1)); //угол дуги арки
                            double w4 = ((2 * Math.PI * r1) / 360) * ang2 * 2; //длина верхней стороны арки уплотнения
                            double ang3 = 90 - (90 - ang2 + ang1);
                            spcAdd.width = (dw + w4);
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut1 = ang3;
                            spcAdd.anglCut0 = ang3;
                            spcRec().spcList.add(spcAdd); //добавим спецификацию
                        }
                    }
                    // </editor-fold> 
                } else if (Type.TRAPEZE == owner.type()) {
                    // <editor-fold defaultstate="collapsed" desc="TRAPEZE"> 
                    IElem5e inTop = owner.joinSide(Layout.TOP), inBott = owner.joinSide(Layout.BOTT), inRigh = owner.joinSide(Layout.RIGHT), inLeft = owner.joinSide(Layout.LEFT);

                    if (winc.form == Form.RIGHT) {
                        if (anglHoriz() == 0) {
                            spcAdd.width += width() + 2 * gzazo();
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inBott.anglCut(0);
                            spcAdd.anglCut1 = inBott.anglCut(1);
                            spcAdd.anglHoriz = inBott.anglHoriz();

                        } else if (anglHoriz() == 90) {
                            double dy1 = (inBott.artiklRec().getDbl(eArtikl.height) - inBott.artiklRec().getDbl(eArtikl.size_centr) - inBott.artiklRec().getDbl(eArtikl.size_falz));
                            double dy2 = (inRigh.artiklRec().getDbl(eArtikl.height) - inRigh.artiklRec().getDbl(eArtikl.size_falz)) * UGeo.tan(90 - inRigh.anglCut(1));
                            spcAdd.width += Math.abs(inRigh.y2() - inRigh.y1()) - (dy1 + dy2);
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inRigh.anglCut(0);
                            spcAdd.anglCut1 = inRigh.anglCut(1);
                            spcAdd.anglHoriz = inRigh.anglHoriz();

                        } else if (anglHoriz() == 180) {
                            double dx1 = inLeft.x2() + inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_falz);
                            double dx2 = inRigh.x2() - inRigh.artiklRec().getDbl(eArtikl.height) + inRigh.artiklRec().getDbl(eArtikl.size_falz);
                            spcAdd.width += (dx2 - dx1) / UGeo.sin(2 * inTop.anglCut(1));
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inTop.anglCut(0);
                            spcAdd.anglCut1 = inTop.anglCut(1);
                            spcAdd.anglHoriz = inTop.anglHoriz();

                        } else if (anglHoriz() == 270) {
                            double dy1 = (inBott.artiklRec().getDbl(eArtikl.height) - inBott.artiklRec().getDbl(eArtikl.size_centr) - inBott.artiklRec().getDbl(eArtikl.size_falz));
                            double dy2 = (inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_falz)) * UGeo.tan(90 - inLeft.anglCut(0));
                            spcAdd.width += Math.abs(inLeft.y2() - inLeft.y1()) - (dy1 + dy2);
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inLeft.anglCut(0);
                            spcAdd.anglCut1 = inLeft.anglCut(1);
                            spcAdd.anglHoriz = inLeft.anglHoriz();
                        }
                    } else if (winc.form == Form.LEFT) {
                        if (anglHoriz() == 0) {
                            spcAdd.width += width() + 2 * gzazo();
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inBott.anglCut(0);
                            spcAdd.anglCut1 = inBott.anglCut(1);
                            spcAdd.anglHoriz = inBott.anglHoriz();

                        } else if (anglHoriz() == 90) {
                            double dy1 = (inBott.artiklRec().getDbl(eArtikl.height) - inBott.artiklRec().getDbl(eArtikl.size_centr) - inBott.artiklRec().getDbl(eArtikl.size_falz));
                            double dy2 = (inRigh.artiklRec().getDbl(eArtikl.height) - inRigh.artiklRec().getDbl(eArtikl.size_falz)) * UGeo.tan(90 - inRigh.anglCut(1));
                            spcAdd.width += Math.abs(inRigh.y2() - inRigh.y1()) - (dy1 + dy2);
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inRigh.anglCut(0);
                            spcAdd.anglCut1 = inRigh.anglCut(1);
                            spcAdd.anglHoriz = inRigh.anglHoriz();

                        } else if (anglHoriz() == 180) {
                            double dx1 = inLeft.x2() + inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_falz);
                            double dx2 = inRigh.x2() - inRigh.artiklRec().getDbl(eArtikl.height) + inRigh.artiklRec().getDbl(eArtikl.size_falz);
                            spcAdd.width += (dx2 - dx1) / UGeo.sin(2 * inTop.anglCut(0));
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inTop.anglCut(0);
                            spcAdd.anglCut1 = inTop.anglCut(1);
                            spcAdd.anglHoriz = inTop.anglHoriz();

                        } else if (anglHoriz() == 270) {
                            double dy1 = (inBott.artiklRec().getDbl(eArtikl.height) - inBott.artiklRec().getDbl(eArtikl.size_centr) - inBott.artiklRec().getDbl(eArtikl.size_falz));
                            double dy2 = (inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_falz)) * UGeo.tan(90 - inLeft.anglCut(0));
                            spcAdd.width += Math.abs(inLeft.y2() - inLeft.y1()) - (dy1 + dy2);
                            spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);
                            spcAdd.anglCut0 = inLeft.anglCut(0);
                            spcAdd.anglCut1 = inLeft.anglCut(1);
                            spcAdd.anglHoriz = inLeft.anglHoriz();
                        }
                    }
                    spcRec().spcList.add(spcAdd); //добавим спецификацию
                    // </editor-fold>
                } else {
                    // <editor-fold defaultstate="collapsed" desc="AREA and STVORKA"> 
                    if (anglHoriz == 0 || anglHoriz == 180) { //по горизонтали
                        spcAdd.width += width() + 2 * gzazo;
                        spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);

                    } else if (anglHoriz == 90 || anglHoriz == 270) { //по вертикали
                        spcAdd.width += height() + 2 * gzazo;
                        spcAdd.height = spcAdd.artiklRec.getDbl(eArtikl.height);

                    } else {
                        System.out.println("Промах:builder.model.IArea5e.addFilling()");
                    }
                    spcAdd.anglCut0 = 45;
                    spcAdd.anglCut1 = 45;
                    spcAdd.anglHoriz = anglHoriz;
                    spcRec.spcList.add(spcAdd);
                    // </editor-fold>
                }

                if (anglHoriz == 0 || anglHoriz == 180) { //по горизонтали
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

                } else if (anglHoriz == 90 || anglHoriz == 270) { //по вертикали
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
                spcAdd.width = UPar.to_12065_15045_25040_34070_39070(spcAdd); //длина мм
                spcAdd.width = spcAdd.width * UPar.to_12030_15030_25035_34030_39030(spcAdd); //"[ * коэф-т ]"
                spcAdd.width = spcAdd.width / UPar.to_12040_15031_25036_34040_39040(spcAdd); //"[ / коэф-т ]" 

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
        } catch (Exception e) {
            System.err.println("Ошибка:ElemGlass.addSpecific()  " + e);
        }
    }

    @Override
    public double gzazo() {
        return gzazo;
    }

    @Override
    public void gzazo(double zazo) {
        this.gzazo = zazo;
    }

    @Override
    public double[] gsize() {
        return gsize;
    }

    @Override
    public double radiusGlass() {
        return radiusGlass;
    }

    @Override
    public Record rasclRec() {
        return rasclRec;
    }

    @Override
    public int rasclColor() {
        return rasclColor;
    }

    @Override
    public int rasclNumber(int num) {
        return rasclNumber[num];
    }

    private List<Point2D> chaosPoint() {
        List<Point2D> list = new LinkedList();
        Area area = new Area(new Rectangle2D.Double(owner.x1(), owner.y1(), owner.x2() - owner.x1(), owner.y2() - owner.y1()));
        for (ElemJoining ej : winc.listJoin) {
            if (ej.type != TypeJoin.VAR10) {
                double h1[] = UGeo.diff(area, ej.elem1, ej.elem1.artiklRec().getDbl(eArtikl.height) - ej.elem1.artiklRec().getDbl(eArtikl.size_centr));
                double h2[] = UGeo.diff(area, ej.elem2, ej.elem2.artiklRec().getDbl(eArtikl.height) - ej.elem2.artiklRec().getDbl(eArtikl.size_centr));
                double p[] = UGeo.cross(
                        ej.elem1.x1() + h1[0], ej.elem1.y1() + h1[1], ej.elem1.x2() + h1[0], ej.elem1.y2() + h1[1],
                        ej.elem2.x1() + h2[0], ej.elem2.y1() + h2[1], ej.elem2.x2() + h2[0], ej.elem2.y2() + h2[1]);
                if (area.contains(p[0], p[1])) {
                    list.add(new Point.Double(p[0], p[1]));
                }
            }
        }
        return list;
    }
    
    @Override
    public void paint() { //рисуём стёкла
        
        System.out.println("**********" + this.id());
        Record colorRec = eColor.find3(colorID1);
        winc.gc2d.setColor(new java.awt.Color(colorRec.getInt(eColor.rgb)));
        
        Point[] arr = {new Point(0, 0), new Point(200, 0), new Point(300, 100), new Point(200, 200), new Point(0, 200)};
        PointPoligon3.JarvisConvexHull(arr); //ранжирование вершин
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i].x + ":" + arr[i].y);
        }

        
//        List<Point2D> list = chaosPoint(); //список вершин 
//        Point2D[] arr = list.toArray(new Point2D[0]);
//        PointPoligon.passJarvis(arr); //ранжирование вершин
//
//        GeneralPath shape = new GeneralPath();
//        shape.moveTo(arr[0].getX(), arr[0].getY());
//        System.out.println(arr[0].getX() + ":" + arr[0].getY());
//        
//        for (int i = 1; i < arr.length; i++) {
//            shape.lineTo(arr[i].getX(), arr[i].getY());
//            System.out.println(arr[i].getX() + ":" + arr[i].getY());
//        }
//        shape.closePath();
//        winc.gc2d.fill(shape);

//
//        if (owner.type() == Type.ARCH) {
//            IElem5e ef = root().frames().get(Layout.TOP);
//            double dz = ef.artiklRec().getDbl(eArtikl.height);
//            double r = ((AreaArch) root()).radiusArch;
//            double ang1 = 90 - Math.toDegrees(Math.asin(root().width() / (r * 2)));
//            double ang2 = 90 - Math.toDegrees(Math.asin((root().width() - 2 * dz) / ((r - dz) * 2)));
//            winc.gc2d.fillArc((int) (root().width() / 2 - r + dz), (int) dz, (int) ((r - dz) * 2),
//                    (int) ((r - dz) * 2), (int) ang2, (int) ((90 - ang2) * 2));
//
//        } else if (owner.type() == Type.TRAPEZE) {
//            IElem5e inLeft = root().frames().get(Layout.LEFT), inTop = root().frames().get(Layout.TOP),
//                    inBott = owner.joinSide(Layout.BOTT), inRight = root().frames().get(Layout.RIGHT);
//
//            if (owner.type() == Type.STVORKA) {
//                if (winc.form == Form.RIGHT) {
//                    double dy = width() * UGeo.tan(90 - (inTop.anglHoriz() - 90));
//                    winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
//                            new int[]{(int) y1, (int) (y1 + dy), (int) y2, (int) y2}, 4);
//                } else if (winc.form == Form.LEFT) {
//                    double dy = width() * UGeo.tan((inTop.anglHoriz() - 180));
//                    winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
//                            new int[]{(int) (y1 + dy), (int) y1, (int) y2, (int) y2}, 4);
//                }
//            } else {
//                ElemJoining ej = winc.listJoin.get(inTop, 1);
//                if (winc.form == Form.RIGHT) {
//                    double dy = width() * UGeo.tan(90 - (inTop.anglHoriz() - 90));
//                    winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
//                            new int[]{(int) y1, (int) (y1 + dy), (int) y2, (int) y2}, 4);
//                } else if (winc.form == Form.LEFT) {
//                    double dy = width() * UGeo.tan((inTop.anglHoriz() - 180));
//                    winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
//                            new int[]{(int) (y1 + dy), (int) y1, (int) y2, (int) y2}, 4);
//                } else if (winc.form == Form.SYMM) {
//                    double dx = (winc.width1() - winc.width2()) / 2 + width() * UGeo.tan((inTop.anglHoriz() - 180));
//                    winc.gc2d.fillPolygon(new int[]{(int) (x1 + dx), (int) (x2 - dx), (int) x2, (int) x1},
//                            new int[]{(int) y1, (int) y1, (int) y2, (int) y2}, 4);
//                }
//            }
//
//        } else {
//            winc.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
//                    new int[]{(int) y1, (int) y1, (int) y2, (int) y2}, 4);
//        }
    }

    //Раскладка
    @Override
    public void rascladkaPaint() {

        if (this.rasclRec.isVirtual() == false) {
            IElem5e elemL = owner.joinSide(Layout.LEFT), elemT = owner.joinSide(Layout.TOP), elemB = owner.joinSide(Layout.BOTT), elemR = owner.joinSide(Layout.RIGHT);
            double artH = Math.round(this.rasclRec().getDbl(eArtikl.height));
            final int numX = (gson.param().get(PKjson.rasclHor) == null) ? 2 : gson.param().get(PKjson.rasclHor).getAsInt();
            final int numY = (gson.param().get(PKjson.rasclVert) == null) ? 2 : gson.param().get(PKjson.rasclVert).getAsInt();
            final double dy = (elemB.y1() - elemT.y2()) / numY, dx = (elemR.x1() - elemL.x2()) / numX;

            ListenerReload reloadHor = () -> {
                double h = 0;
                for (int i = 1; i < numY; i++) {
                    h = h + dy;
                    winc.gc2d.setColor((winc.scale < 0.1) ? Color.black : Color.white);
                    winc.gc2d.fillRect((int) elemL.x2(), (int) Math.round(elemT.y2() + h - artH / 2), (int) (elemR.x1() - elemL.x2()), (int) artH);
                    winc.gc2d.setColor((winc.scale < 0.1) ? Color.getHSBColor(242, 242, 242) : Color.black);
                    winc.gc2d.drawRect((int) elemL.x2(), (int) Math.round(elemT.y2() + h - artH / 2), (int) (elemR.x1() - elemL.x2()), (int) artH);
                }
            };
            ListenerReload reloadVer = () -> {
                double w = 0;
                for (int i = 1; i < numX; i++) {
                    w = w + dx;
                    winc.gc2d.setColor((winc.scale < 0.1) ? Color.black : Color.white);
                    winc.gc2d.fillRect((int) Math.round(elemL.x2() + w - artH / 2), (int) elemT.y2(), (int) artH, (int) (elemB.y1() - elemT.y2()));
                    winc.gc2d.setColor((winc.scale < 0.1) ? Color.getHSBColor(242, 242, 242) : Color.black);
                    winc.gc2d.drawRect((int) Math.round(elemL.x2() + w - artH / 2), (int) elemT.y2(), (int) artH, (int) (elemB.y1() - elemT.y2()));
                }
            };
            //reloadHor.reload(); //целые вертикальные
            //reloadVer.reload();
            reloadVer.reload(); //целые горизонтальные
            reloadHor.reload();
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", radiusGlass=" + radiusGlass;
    }
}
