package estimate.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import frames.swing.Draw;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eGlasdet;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.ParamJson;
import enums.UseSide;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import estimate.constr.Cal5e;
import estimate.constr.Specification;
import java.util.LinkedList;

public class ElemGlass extends ElemSimple {

    public float radiusGlass = 0; //радиус арки
    public int artikleID = -1;

    public ElemGlass(AreaSimple owner, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = LayoutArea.FULL;
        this.type = TypeElem.GLASS;

        if (param != null && param.isEmpty() == false) {
            String str = param.replace("'", "\"");
            JsonObject jsonObj = new Gson().fromJson(str, JsonObject.class);
            this.artikleID = (jsonObj.get(ParamJson.artikleID.name()) == null) ? -1 : jsonObj.get(ParamJson.artikleID.name()).getAsInt();
        }
        initСonstructiv();

        if (TypeElem.ARCH == owner.type) {
            setDimension(owner.x1, owner.y1, owner.x2, iwin().heightAdd - owner.y2);
            //specificationRec.putParam(13015, TypeGlass.ARCH.text());
        } else {
            setDimension(owner.x1, owner.y1, owner.x2, owner.y2);
            //specificationRec.putParam(13015, TypeGlass.RECTANGL.text());
        }
    }

    public void initСonstructiv() {

        if (artikleID != -1) {
            artiklRec = eArtikl.find(artikleID, false);
        }
        if (artiklRec == null) {
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

        specificationRec.place = "ЗАП";
        specificationRec.setArtiklRec(artiklRec);
        specificationRec.colorID1 = colorID1;
        specificationRec.colorID2 = colorID2;
        specificationRec.colorID3 = colorID3;
        specificationRec.id = id();

        float gzazo = (mapFieldVal.get("GZAZO") != null) ? Float.valueOf(mapFieldVal.get("GZAZO")) : 0;
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

        } else {
            ElemSimple insideLeft = join(LayoutArea.LEFT), insideTop = join(LayoutArea.TOP), insideBott = join(LayoutArea.BOTTOM), insideRight = join(LayoutArea.RIGHT);
            float size_falz = (insideLeft.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : insideLeft.artiklRec.getFloat(eArtikl.size_falz);
            x1 = insideLeft.x2 - size_falz + gzazo;
            size_falz = (insideTop.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : insideTop.artiklRec.getFloat(eArtikl.size_falz);
            y1 = insideTop.y2 - size_falz + gzazo;
            size_falz = (insideRight.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : insideRight.artiklRec.getFloat(eArtikl.size_falz);
            x2 = insideRight.x1 + size_falz - gzazo;
            size_falz = (insideBott.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : insideBott.artiklRec.getFloat(eArtikl.size_falz);
            y2 = insideBott.y1 + size_falz - gzazo;
        }
        specificationRec.width = width();
        specificationRec.height = height();
    }

    @Override //Вложеная спецификация 
    public void addSpecific(Specification specif) {
        Float gzazo = Float.valueOf(mapFieldVal.get("GZAZO"));

        //Стеклопакет
        if (TypeArtikl.GLASS.isType(specif.artiklRec)) {
            return;

            //Штапик
        } else if (TypeArtikl.SHTAPIK.isType(specif.artiklRec)) {

            Float overLength = (specif.getParam(null, 15050) == null) ? 0.f : Float.valueOf(specif.getParam(0, 15050).toString());
            if (TypeElem.ARCH == owner().type()) {

                //По основанию арки
                double dh2 = specif.artiklRec.getDbl(eArtikl.height) - gzazo;
                double r1 = radiusGlass - dh2;
                double h1 = height() - 2 * dh2;
                double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний периметр
                double r2 = radiusGlass;
                double h2 = height();
                double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                double l3 = l2 - l1;
                double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза

                double r5 = radiusGlass + gzazo;
                double h5 = height() + 2 * gzazo;
                double l5 = overLength + 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда

                specif.width = (float) l5;
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specif.anglCut2 = (float) ang;
                specif.anglCut1 = (float) ang;
                specificationRec.specificationList.add(new Specification(specif)); //добавим спецификацию

                //По дуге арки
                double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                double ang3 = 90 - (90 - ang2 + ang);
                double koef = 2; //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста                
                ElemSimple ramaArch = root().mapFrame.get(LayoutArea.ARCH);
                double R2 = ((AreaArch) iwin().rootArea).radiusArch - ramaArch.specificationRec.height + specif.artiklRec.getDbl(eArtikl.height);
                double L2 = iwin().rootArea.width() - ramaArch.specificationRec.height * 2 + specif.artiklRec.getDbl(eArtikl.height) * 2 - koef;
                double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
                double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
                double Z = 3 * gzazo;
                double R = radiusGlass;
                double L = width();
                double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                double M = ((R + Z) * 2) * Math.toRadians(ang5);
                specif.width = (float) (overLength + M2);
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specif.anglCut2 = (float) ang3;
                specif.anglCut1 = (float) ang3;
                specificationRec.specificationList.add(new Specification(specif)); //добавим спецификацию

            } else if (TypeElem.RECTANGL == owner().type() || TypeElem.AREA == owner().type() || TypeElem.STVORKA == owner().type()) { //глухарь или створка
                specif.anglCut2 = 45;
                specif.anglCut1 = 45;
                //По горизонтали                
                specif.width = width() + 2 * gzazo;
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specificationRec.specificationList.add(new Specification(specif));
                specificationRec.specificationList.add(new Specification(specif));
                //По вертикали
                specif.width = height() + 2 * gzazo;
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specificationRec.specificationList.add(new Specification(specif));
                specificationRec.specificationList.add(new Specification(specif));
            }

            //Уплотнитель
        } else if (TypeArtikl.KONZEVPROF.isType(specif.artiklRec)) {

            if (TypeElem.ARCH == owner().type()) { //если уплотнитель в арке
                //По основанию арки
                double dh2 = specif.artiklRec.getFloat(eArtikl.height) - gzazo;
                double r1 = radiusGlass - dh2;
                double h1 = height() - 2 * dh2;
                double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
                double r2 = radiusGlass;
                double h2 = height();
                double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
                double l3 = l2 - l1;
                double r5 = radiusGlass + gzazo;
                double h5 = height() + 2 * gzazo;
                double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
                double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
                specif.width = (float) l5;
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specif.anglCut2 = (float) ang;
                specif.anglCut1 = (float) ang;
                specificationRec.specificationList.add(new Specification(specif)); //добавим спецификацию

                //По дуге арки
                double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                double ang3 = 90 - (90 - ang2 + ang);
                double Z = 3 * gzazo;
                double R = radiusGlass;
                double L = width();
                double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                double M = ((R + Z) * 2) * Math.toRadians(ang5);
                specif.width = (float) M;
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specif.anglCut2 = (float) ang3;
                specif.anglCut1 = (float) ang3;
                specificationRec.specificationList.add(new Specification(specif)); //добавим спецификацию

            } else if (TypeElem.RECTANGL == owner().type() || TypeElem.AREA == owner().type() || TypeElem.STVORKA == owner().type()) { //глухарь или створка
                specif.anglCut2 = 45;
                specif.anglCut1 = 45;
                //По горизонтали                
                specif.width = width() + 2 * gzazo;
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specificationRec.specificationList.add(new Specification(specif));
                specificationRec.specificationList.add(new Specification(specif));
                //По вертикали
                specif.width = height() + 2 * gzazo;
                specif.height = specif.artiklRec.getFloat(eArtikl.height);
                specificationRec.specificationList.add(new Specification(specif));
                specificationRec.specificationList.add(new Specification(specif));
            }
            //Всё остальное
        } else {
            Cal5e.amount(specificationRec, specif);

            if (TypeElem.RECTANGL == owner().type() || TypeElem.AREA == owner().type() || TypeElem.STVORKA == owner().type()) {
                for (int index = 0; index < 4; index++) {
                    specificationRec.specificationList.add(new Specification(specif));
                }
            } else if (TypeElem.ARCH == owner().type()) {
                for (int index = 0; index < 2; index++) {
                    specificationRec.specificationList.add(new Specification(specif));
                }
            } else {
                specificationRec.specificationList.add(new Specification(specif));
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
            Draw.fillArc((float) (root().width() / 2 - r + dz), dz, (float) ((r - dz) * 2), (float) ((r - dz) * 2), (float) ang2, (float) ((90 - ang2) * 2));

        } else {
            float h = iwin().heightAdd - iwin().height;
            Draw.fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
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
