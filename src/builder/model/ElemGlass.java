package builder.model;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSystree;
import enums.Layout;
import enums.TypeArtikl;
import builder.making.Specific;
import common.UCom;
import domain.eGlasprof;
import enums.PKjson;
import enums.Type;
import enums.UseUnit;

public class ElemGlass extends ElemSimple {

    public float radiusGlass = 0; //радиус арки
    public float gzazo = 0; //зазор между фальцем и стеклопакетом 
    public float sideHoriz[] = {0, 90, 180, 270}; //угол боковой стороны к горизонту

    public ElemGlass(AreaSimple owner, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = Layout.FULL;
        this.type = Type.GLASS;

        initСonstructiv(param);

        if (Type.ARCH == owner.type) {
            setDimension(0, 0, owner.x2, iwin().heightAdd - iwin().height);
        } else {
            setDimension(owner.x1, owner.y1, owner.x2, owner.y2);
        }
    }

    public void initСonstructiv(String param) {

        if (param(param, PKjson.artglasID) != -1) {
            artiklRec = eArtikl.find(param(param, PKjson.artglasID), false);
        } else {
            Record sysreeRec = eSystree.find(iwin().nuni); //по умолчанию стеклопакет
            artiklRec = eArtikl.find2(sysreeRec.getStr(eSystree.glas));
        }
        artiklRecAn = artiklRec;

        //Цвет стекла
        Record artdetRec = eArtdet.find2(artiklRec.getInt(eArtikl.id));
        Record colorRec = eColor.find3(artdetRec.getInt(eArtdet.color_fk));
        colorID1 = colorRec.getInt(eColor.id);
        colorID2 = colorRec.getInt(eColor.id);
        colorID3 = colorRec.getInt(eColor.id);
    }

    @Override //Главная спецификация
    public void setSpecific() {

        spcRec.place = "ЗАП";
        spcRec.setArtiklRec(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;

        if (owner().type() == Type.ARCH) { //если арка
            ElemFrame elemArch = rootArea().mapFrame.get(Layout.TOP);
            ElemSimple elemImpost = joinFlat(Layout.BOTT);
            /*for (Com5t elemBase : root().listChild) { //первый импост в стеклопакете снизу;
                if (Type.IMPOST == elemBase.type) {
                    elemImpost = (ElemImpost) elemBase;
                    break;
                }
            }*/
            y1 = y1 + elemArch.artiklRec.getFloat(eArtikl.height) - elemArch.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
            y2 = y2 + elemImpost.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            double r = ((AreaArch) rootArea()).radiusArch - elemArch.artiklRec.getFloat(eArtikl.height) + elemArch.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            double l = Math.sqrt(2 * height() * r - height() * height());
            x1 = (owner().width() / 2) - (float) l;
            x2 = owner().width() - x1;
            radiusGlass = (float) r;

        } else if (owner().type() == Type.STVORKA) {
            AreaStvorka stv = (AreaStvorka) owner();
            ElemSimple insideLeft = stv.mapFrame.get(Layout.LEFT), insideTop = stv.mapFrame.get(Layout.TOP), insideBott = stv.mapFrame.get(Layout.BOTT), insideRight = stv.mapFrame.get(Layout.RIGHT);

            if (iwin().syssizeRec.getInt(eSyssize.id) == -1) {
                x1 = insideLeft.x1 + eGlasprof.find2(insideLeft.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
                y1 = insideTop.y1 + eGlasprof.find2(insideTop.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
                x2 = insideRight.x2 - eGlasprof.find2(insideRight.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
                y2 = insideBott.y2 - eGlasprof.find2(insideBott.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
            } else {
                x1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                y1 = insideTop.y2 - insideTop.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                x2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
                y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            }
        } else {
            ElemSimple insideLeft = joinFlat(Layout.LEFT), insideTop = joinFlat(Layout.TOP), insideBott = joinFlat(Layout.BOTT), insideRight = joinFlat(Layout.RIGHT);
            if (iwin().syssizeRec.getInt(eSyssize.id) == -1) {
                x1 = owner.x1 + eGlasprof.find2(insideLeft.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
                y1 = owner.y1 + eGlasprof.find2(insideTop.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
                x2 = owner.x2 - eGlasprof.find2(insideRight.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
                y2 = owner.y2 - eGlasprof.find2(insideBott.artiklRec.getInt(eArtikl.id)).getFloat(eGlasprof.gsize);
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

    @Override //Вложенная спецификация 
    public void addSpecific(Specific spcAdd) {
        spcAdd.count = uti3.get_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += uti3.get_14050_24050_33050_38050(spcAdd); //кол. ед. с шагом
        spcAdd.width = uti3.get_12050_15050_34051_39020(spcRec, spcAdd); //поправка мм         
        if (TypeArtikl.X502.isType(spcAdd.artiklRec)) {
            return;  //если стеклопакет сразу выход
        }

        if (UseUnit.METR.id == spcAdd.artiklRec.getInt(eArtikl.unit)) {
            if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X108)) {  //штапик

                if (Type.ARCH == owner().type()) { //штапик в арке
                    ((AreaArch) rootArea()).shtapik(this, spcAdd);

                } else { //штапик в прямоугольнике
                    if (anglHoriz == sideHoriz[0] || anglHoriz == sideHoriz[2]) { //по горизонтали
                        spcAdd.width += width() + 2 * gzazo;
                        spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
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
                        spcAdd.width += height() + 2 * gzazo;
                        spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
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
                    spcAdd.anglCut1 = 45;
                    spcAdd.anglCut2 = 45;
                    spcRec.spcList.add(spcAdd);
                }
            } else { //всё остальное
                if (Type.ARCH == owner().type()) { //в арке
                    ((AreaArch) rootArea()).padding(this, spcAdd);
                } else {
                    if (anglHoriz == sideHoriz[0] || anglHoriz == sideHoriz[2]) { //по горизонтали
                        spcAdd.width = spcAdd.width + width() + gzazo;

                    } else if (anglHoriz == sideHoriz[1] || anglHoriz == sideHoriz[3]) { //по вертикали
                        spcAdd.width = spcAdd.width + height() + gzazo;
                    }

                    spcRec.spcList.add(spcAdd);
                }
            }
            spcAdd.width = uti3.get_12065_15045_25040_34070_39070(spcRec, spcAdd); //длина мм
            spcAdd.width = spcAdd.width * uti3.get_12030_15030_25035_34030_39030(spcRec, spcAdd); //"[ * коэф-т ]"
            spcAdd.width = spcAdd.width / uti3.get_12040_15031_25036_34040_39040(spcRec, spcAdd); //"[ / коэф-т ]" 

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
        iwin().gc2d.setColor(new java.awt.Color(226, 255, 250));

        if (owner().type == Type.ARCH) {
            ElemFrame ef = rootArea().mapFrame.get(Layout.TOP);
            float dz = ef.artiklRec.getFloat(eArtikl.height);
            double r = ((AreaArch) rootArea()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(rootArea().width() / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((rootArea().width() - 2 * dz) / ((r - dz) * 2)));
            iwin().gc2d.fillArc((int) ((int) rootArea().width() / 2 - r + dz), (int) dz, (int) ((r - dz) * 2), (int) ((r - dz) * 2), (int) ang2, (int) ((90 - ang2) * 2));

        } else if (owner().type == Type.TRAPEZE) {
            if (owner.view == 2) {
                iwin().gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                        new int[]{(int) y1, (int) (y2 - iwin().heightAdd), (int) y2, (int) y2}, 4);
            } else {
                iwin().gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                        new int[]{(int) y1, (int) y2, (int) y2, (int) (y2 - iwin().heightAdd)}, 4);
            }
        } else {
            iwin().gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                    new int[]{(int) y1, (int) y1, (int) y2, (int) y2}, 4);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", radiusGlass=" + radiusGlass;
    }
}
