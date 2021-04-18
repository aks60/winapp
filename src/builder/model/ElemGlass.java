package builder.model;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import builder.calculate.SpecificRec;
import enums.PKjson;
import java.util.HashMap;

public class ElemGlass extends ElemSimple {

    public float radiusGlass = 0; //радиус арки
    public HashMap<Integer, Float> hmGsize = new HashMap();
    public float gzazo = 0; //зазор между фальцем и стеклопакетом  

    public ElemGlass(AreaSimple owner, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = LayoutArea.FULL;
        this.type = TypeElem.GLASS;

        initСonstructiv(param);

        if (TypeElem.ARCH == owner.type) {
            setDimension(owner.x1, owner.y1, owner.x2, iwin().heightAdd - owner.y2);
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
        spcRec.id = id();

        if (owner() instanceof AreaArch) { //если арка

            ElemFrame elemArch = root().mapFrame.get(LayoutArea.ARCH);
            ElemImpost elemImpost = null;  //первый импост в стеклопакете снизу;
            for (Com5t elemBase : root().listChild) {
                if (TypeElem.IMPOST == elemBase.type) {
                    elemImpost = (ElemImpost) elemBase;
                    break;
                }
            }
            y1 = y1 + elemArch.artiklRec.getFloat(eArtikl.height) - elemArch.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
            y2 = y2 + elemImpost.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            double r = ((AreaArch) root()).radiusArch - elemArch.artiklRec.getFloat(eArtikl.height) + elemArch.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            double l = Math.sqrt(2 * height() * r - height() * height());
            x1 = (owner().width() / 2) - (float) l;
            x2 = owner().width() - x1;
            radiusGlass = (float) r;

        } else if (TypeElem.STVORKA == owner().type()) {
            AreaStvorka stv = (AreaStvorka) owner();
            ElemSimple insideLeft = stv.mapFrame.get(LayoutArea.LEFT), insideTop = stv.mapFrame.get(LayoutArea.TOP), insideBott = stv.mapFrame.get(LayoutArea.BOTTOM), insideRight = stv.mapFrame.get(LayoutArea.RIGHT);

            if (iwin().syssizeRec.getInt(eSyssize.id) == -1) {
                x1 = insideLeft.x1 + hmGsize.get(insideLeft.artiklRec.getInt(eArtikl.id));
                y1 = insideTop.y1 + hmGsize.get(insideTop.artiklRec.getInt(eArtikl.id));
                x2 = insideRight.x2 - hmGsize.get(insideRight.artiklRec.getInt(eArtikl.id));
                y2 = insideBott.y2 - hmGsize.get(insideBott.artiklRec.getInt(eArtikl.id));
            } else {
                x1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                y1 = insideTop.y2 - insideTop.artiklRec.getFloat(eArtikl.size_falz) + gzazo;
                x2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
                y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz) - gzazo;
            }
        } else {
            ElemSimple insideLeft = join(LayoutArea.LEFT), insideTop = join(LayoutArea.TOP), insideBott = join(LayoutArea.BOTTOM), insideRight = join(LayoutArea.RIGHT);

            if (iwin().syssizeRec.getInt(eSyssize.id) == -1) {
                x1 = owner.x1 + hmGsize.get(insideLeft.artiklRec.getInt(eArtikl.id));
                y1 = owner.y1 + hmGsize.get(insideTop.artiklRec.getInt(eArtikl.id));
                x2 = owner.x2 - hmGsize.get(insideRight.artiklRec.getInt(eArtikl.id));
                y2 = owner.y2 - hmGsize.get(insideBott.artiklRec.getInt(eArtikl.id));
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

    @Override //Вложеная спецификация 
    public void addSpecific(SpecificRec spcAdd) {

        spcAdd.count = spc7d.calcCount(spcRec, spcAdd); //кол. ед. с учётом парам.
        spcAdd.count = spc7d.calcCountStep(spcRec, spcAdd); //кол. ед. с шагом
        spcAdd.width = spc7d.calcAmountMetr(spcRec, spcAdd); //поправка мм
        spcAdd.quant1 = spc7d.calcAmount(spcRec, spcAdd); //количество от параметра        

        //Стеклопакет
        if (TypeArtikl.X502.isType(spcAdd.artiklRec)) {
            return;

            //Штапик
        } else if (TypeArtikl.X108.isType(spcAdd.artiklRec)) {

            if (TypeElem.ARCH == owner().type()) {
                ((AreaArch) root()).calcShtapik(this, spcAdd);

            } else {
                //По горизонтали
                float widthFromParam = spcAdd.width;
                spcAdd.width += width() + 2 * gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                SpecificRec specificationHor1 = new SpecificRec(spcAdd);
                SpecificRec specificationHor2 = new SpecificRec(spcAdd);
                spcRec.spcList.add(specificationHor1);
                spcRec.spcList.add(specificationHor2);
                //По вертикали
                spcAdd.width = widthFromParam;
                spcAdd.width += height() + 2 * gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                SpecificRec specificationVer1 = new SpecificRec(spcAdd);
                SpecificRec specificationVer2 = new SpecificRec(spcAdd);
                spcRec.spcList.add(specificationVer1);
                spcRec.spcList.add(specificationVer2);

                if ("Нет".equals(spcAdd.mapParam.get(15010))) {
                    specificationVer1.width = specificationVer1.width - 2 * specificationHor1.height;
                    specificationVer2.width = specificationVer2.width - 2 * specificationHor2.height;
                }
            }

            //Концнвой профиль, уплотнение притвора, уплотнитель заполнения
        } else if (TypeArtikl.X135.isType(spcAdd.artiklRec)
                || TypeArtikl.X301.isType(spcAdd.artiklRec)) {
            if (TypeElem.ARCH == owner().type()) { //если уплотнитель в арке
                ((AreaArch) root()).calcPadding(this, spcAdd);

            } else {
                spcAdd.width = spcAdd.width * 4 + width() * 2 + height() * 2 + gzazo * 4; //поправка *4 плюс периметр плюс зазор * 4
                spcAdd.count = 1;
                spcRec.spcList.add(spcAdd);
            }
        } else if (TypeArtikl.X302.isType(spcAdd.artiklRec)) {
            if (TypeElem.ARCH == owner().type()) { //если уплотнитель в арке
                ((AreaArch) root()).calcPadding(this, spcAdd);

            } else {
                spcAdd.width = spcAdd.width * 4 + width() * 2 + height() * 2; //поправка *4 плюс периметр
                spcAdd.count = 1;
                spcRec.spcList.add(spcAdd);

            }
            //Всё остальное
        } else {
            spcAdd.width = spc7d.calcAmountLenght(spcRec, spcAdd); //длина мм

            if (TypeElem.RECTANGL == owner().type() || TypeElem.AREA == owner().type() || TypeElem.STVORKA == owner().type()) {
                for (int index = 0; index < 4; index++) {
                    spcRec.spcList.add(new SpecificRec(spcAdd));
                }
            } else if (TypeElem.ARCH == owner().type()) {
                for (int index = 0; index < 2; index++) {
                    spcRec.spcList.add(new SpecificRec(spcAdd));
                }
            } else {
                spcRec.spcList.add(new SpecificRec(spcAdd));
            }
        }
    }

    @Override
    public void paint() { //рисуём стёкла
        iwin().gc2d.setColor(new java.awt.Color(226, 255, 250));

        if (owner().type == TypeElem.ARCH) {
            ElemFrame ef = root().mapFrame.get(LayoutArea.ARCH);
            float dz = ef.artiklRec.getFloat(eArtikl.height);
            double r = ((AreaArch) root()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(root().width() / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((root().width() - 2 * dz) / ((r - dz) * 2)));
            iwin().gc2d.fillArc((int) ((int) root().width() / 2 - r + dz), (int) dz, (int) ((r - dz) * 2), (int) ((r - dz) * 2), (int) ang2, (int) ((90 - ang2) * 2));

        } else {
            float h = iwin().heightAdd - iwin().height;
            iwin().gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
                    new int[]{(int) (y1 + h), (int) (y1 + h), (int) (y2 + h), (int) (y2 + h)}, 4);
        }
    }

    @Override
    public UseArtiklTo useArtiklTo() {
        return UseArtiklTo.ANY;
    }

    @Override
    public String toString() {
        return super.toString() + ", radiusGlass=" + radiusGlass;
    }
}
