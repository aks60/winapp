package estimate.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import estimate.constr.Specification;

public class ElemGlass extends ElemSimple {

    public float radiusGlass = 0; //радиус арки

    public ElemGlass(AreaSimple owner, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = LayoutArea.FULL;
        this.type = TypeElem.GLASS;

        if (param != null && param.isEmpty() == false) {
            String str = param.replace("'", "\"");
            JsonElement jsonElem = new Gson().fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            mapParamUse.put(ParamJson.nunic_iwin, jsonObj.get(ParamJson.nunic_iwin.name()).getAsInt());
        }
        initСonstructiv();
        parsing(param);

        if (TypeElem.ARCH == owner.type) {
            setDimension(owner.x1, owner.y1, owner.x2, iwin().heightAdd - owner.y2);
            //specificationRec.putParam(13015, TypeGlass.ARCH.text());
        } else {
            setDimension(owner.x1, owner.y1, owner.x2, owner.y2);
            //specificationRec.putParam(13015, TypeGlass.RECTANGL.text());
        }
    }

    public void initСonstructiv() {

        Object id = mapParamUse.get(ParamJson.nunic_iwin);
        if (id != null) {
            artiklRec = eArtikl.find(Integer.valueOf(id.toString()), false);
        }
        if (artiklRec == null) {
            Record sysreeRec = eSystree.find(iwin().nuni); //по умолчанию стеклопакет
            artiklRec = eArtikl.find2(sysreeRec.getStr(eSystree.glas));
        }

        //Цвет стекла
        Record artdetRec = eArtdet.find2(artiklRec.getInt(eArtikl.id));
        Record colorRec = eColor.find(artdetRec.getInt(eArtdet.color_fk));
//        color1 = colorRec.getInt(eColor.code);
//        color2 = colorRec.getInt(eColor.code);
//        color3 = colorRec.getInt(eColor.code);

        //TODO Разобраться с цветом стекла
        color1 = iwin().colorNone;
        color2 = iwin().colorNone;
        color3 = iwin().colorNone;
        
        specificationRec.setArtiklRec(artiklRec);
    }

    @Override //Главная спецификация
    public void setSpecific() {

        specificationRec.place = "ЗАП";
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
            y1 = y1 + elemArch.artiklRec.getInt(eArtikl.height) - elemArch.artiklRec.getInt(eArtikl.size_falz) + gzazo;
            y2 = y2 + elemImpost.artiklRec.getInt(eArtikl.size_falz) - gzazo;
            //height = y2 - y1;
            specificationRec.height = height();
            double r = ((AreaArch) root()).radiusArch - elemArch.artiklRec.getInt(eArtikl.height) + elemArch.artiklRec.getInt(eArtikl.size_falz) - gzazo;
            double l = Math.sqrt(2 * height() * r - height() * height());
            x1 = (owner().width() / 2) - (float) l;
            x2 = owner().width() - x1;
            radiusGlass = (float) r;

            specificationRec.width = width();

            specificationRec.id = id();
            specificationRec.setArtiklRec(artiklRec);
            specificationRec.color1 = color1;
            specificationRec.color2 = color2;
            specificationRec.color3 = color3;

        } else {

            ElemSimple elemTop = iwin().mapJoin.get(owner().x1 + ":" + owner().y1).joinElement1;
            y1 = elemTop.y2 - elemTop.artiklRec.getInt(eArtikl.size_falz) + gzazo;

            ElemSimple elemBottom = iwin().mapJoin.get(owner().x1 + ":" + owner().y2).joinElement2;
            y2 = elemBottom.y1 + elemBottom.artiklRec.getInt(eArtikl.size_falz) - gzazo;

            ElemSimple elemLeft = iwin().mapJoin.get(owner().x1 + ":" + owner().y1).joinElement1;
            x1 = elemLeft.x2 - elemLeft.artiklRec.getInt(eArtikl.size_falz) + gzazo;

            ElemSimple elemRight = iwin().mapJoin.get(owner().x2 + ":" + owner().y1).joinElement2;
            x2 = elemRight.x1 + elemRight.artiklRec.getInt(eArtikl.size_falz) - gzazo;

            specificationRec.width = width();
            specificationRec.height = height();
            specificationRec.id = id();
            specificationRec.setArtiklRec(artiklRec);
            specificationRec.color1 = color1;
            specificationRec.color2 = color2;
            specificationRec.color3 = color3;
        }
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
                double dh2 = artiklRec.getDbl(eArtikl.height) - gzazo;
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
                specif.height = artiklRec.getFloat(eArtikl.height);
                specif.anglCut2 = (float) ang;
                specif.anglCut1 = (float) ang;

                //По дуге арки
                double ang2 = Math.toDegrees(Math.asin(l2 / r2));
                double ang3 = 90 - (90 - ang2 + ang);
                //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста
                double koef = 2;
                ElemSimple ramaArch = root().mapFrame.get(LayoutArea.ARCH);
                double R2 = ((AreaArch) iwin().rootArea).radiusArch - ramaArch.specificationRec.height + artiklRec.getDbl(eArtikl.height);
                double L2 = iwin().rootArea.width() - ramaArch.specificationRec.height * 2 + artiklRec.getDbl(eArtikl.height) * 2 - koef;
                double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
                double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
                double Z = 3 * gzazo;
                double R = radiusGlass;
                double L = width();
                double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
                double M = ((R + Z) * 2) * Math.toRadians(ang5);
                specif.width = (float) (overLength + M2);
                specif.height = artiklRec.getFloat(eArtikl.height);
                specif.anglCut2 = (float) ang3;
                specif.anglCut1 = (float) ang3;

            } else if (TypeElem.AREA == owner().type() || TypeElem.STVORKA == owner().type()) { //глухарь или створка
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
                double dh2 = artiklRec.getFloat(eArtikl.height) - gzazo;
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

            } else if (TypeElem.AREA == owner().type() || TypeElem.STVORKA == owner().type()) { //глухарь или створка
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
            if (TypeElem.AREA == owner().type() || TypeElem.AREA == owner().type() || TypeElem.STVORKA == owner().type()) {
                for (int index = 0; index < 4; index++) {
                    specificationRec.specificationList.add(specif);
                }
            } else if (TypeElem.ARCH == owner().type()) {
                for (int index = 0; index < 2; index++) {
                    specificationRec.specificationList.add(specif);
                }
            } else {
                specificationRec.specificationList.add(specif);
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
            fillArc((float) (root().width() / 2 - r + dz), dz, (float) ((r - dz) * 2), (float) ((r - dz) * 2), (float) ang2, (float) ((90 - ang2) * 2));

        } else {
            float h = iwin().heightAdd - iwin().height;
            fillPolygon(new int[]{(int) x1, (int) x2, (int) x2, (int) x1},
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
