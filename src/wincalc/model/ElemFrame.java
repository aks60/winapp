package wincalc.model;

import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSysprof;
import enums.LayoutArea;
import enums.LayoutProfile;
import enums.TypeArtikl1;
import enums.TypeElem;
import enums.UseArtikl;
import wincalc.constr.Specification;

public class ElemFrame extends ElemSimple {

    protected float length = 0; //Длина арки
    protected float anglCut1 = 45; //Угол реза рамы
    protected float anglCut2 = 45; //Угол реза рамы

    public ElemFrame(AreaSimple owner, float id, LayoutArea layout) {
        super(id, owner.iwin(), owner);
        this.layout = layout;
        color1 = owner.color1;
        color2 = owner.color2;
        color3 = owner.color3;
        this.type = (TypeElem.FULLSTVORKA == owner.type) ? TypeElem.FRAME_STV : TypeElem.FRAME_BOX;
        initСonstructiv();

        if (LayoutArea.LEFT == layout) {
            setDimension(owner.x1, owner.y1, owner.x1 + artiklRec.getFloat(eArtikl.height), owner.y2);

        } else if (LayoutArea.RIGHT == layout) {
            setDimension(owner.x2 - artiklRec.getFloat(eArtikl.height), owner.y1, owner.x2, owner.y2);
            anglHoriz = 90;

        } else if (LayoutArea.TOP == layout) {
            setDimension(owner.x1, owner.y1, owner.x2, owner.y1 + artiklRec.getFloat(eArtikl.height));
            anglHoriz = 180;

        } else if (LayoutArea.BOTTOM == layout) {
            setDimension(owner.x1, owner.y2 - artiklRec.getFloat(eArtikl.height), owner.x2, owner.y2);
            anglHoriz = 0;

        } else if (LayoutArea.ARCH == layout) {
            anglHoriz = 180;
        }
    }

    public void initСonstructiv() {

        if (layout == LayoutArea.ARCH || layout == LayoutArea.TOP) {
            sysprofRec = eSysprof.find3(iwin().nuni, typeProfile(), LayoutProfile.TOP);
        } else if (layout == LayoutArea.BOTTOM) {
            sysprofRec = eSysprof.find3(iwin().nuni, typeProfile(), LayoutProfile.BOTTOM);
        } else if (layout == LayoutArea.LEFT) {
            sysprofRec = eSysprof.find3(iwin().nuni, typeProfile(), LayoutProfile.LEFT);
        } else if (layout == LayoutArea.RIGHT) {
            sysprofRec = eSysprof.find3(iwin().nuni, typeProfile(), LayoutProfile.RIGHT);
        }
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
        specificationRec.setArtiklRec(artiklRec);
    }

     public void setSpecifElement(Record sysprofRec) {  //добавление основной спесификации

        specificationRec.element = layout.name;
        float napl = iwin().sysconsRec.getFloat(eSyssize.napl);
        Record artiklRec =  eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        specificationRec.setArtiklRec(artiklRec);
        specificationRec.color1 = color1;
        specificationRec.color2 = color2;
        specificationRec.color3 = color3;
        specificationRec.discount = 0;
        specificationRec.anglHoriz = anglHoriz;
        specificationRec.id = ++iwin().specId;

        //Простое окно
        if (LayoutArea.TOP == layout) {
            specificationRec.width = x2 - x1 + napl * 2;
            specificationRec.height = height(); //artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.BOTTOM == layout) {
            specificationRec.width = x2 - x1 + napl * 2;
            specificationRec.height = height(); //artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.LEFT == layout) {
            specificationRec.width = y2 - y1 +  iwin().sysconsRec.getFloat(eSyssize.prip);
            specificationRec.height = height(); //artiklRec.getFloat(eArtikl.height);

        } else if (LayoutArea.RIGHT == layout) {
            specificationRec.width = y2 - y1 + napl * 2;
            specificationRec.height = height(); //artiklRec.getFloat(eArtikl.height);
        }

        // Коррекция формы окна
        //owner.setSpecifElement(this, sysprofRec);

        specificationRec.anglCut2 = anglCut2;
        specificationRec.anglCut1 = anglCut1;
    }
        
    
    @Override
    public void addSpecifSubelem(Specification specif) { //добавление спесификаций зависимых элементов
        
        //indexUniq(specif);
        Record artiklRec = specif.artiklRec;

        //Просто рама (если элемент включен в список состава)
        if (TypeArtikl1.KOROBKA.isType(artiklRec) || TypeArtikl1.STVORKA.isType(artiklRec)) {

            specificationRec.width = specificationRec.width + Float.valueOf(specif.getParam(0, 34051)); //Поправка, мм
            specificationRec.setArtiklRec(specif.artiklRec);
            return;  //сразу выход т.к. элем. сам является держателем состава

            //Теперь армирование
        } else if (TypeArtikl1.ARMIROVANIE.isType(artiklRec)) {
            specif.element = layout.name;

            if (LayoutArea.TOP == layout || LayoutArea.BOTTOM == layout) {
                specif.width = x2 - x1;

            }
            if (LayoutArea.LEFT == layout || LayoutArea.RIGHT == layout) {
                specif.width = y2 - y1;
            }
            if ("от внутреннего угла".equals(specif.getParam(null, 34010))) {
                Double dw1 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut1));
                Double dw2 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut2));
                specif.width = specif.width + 2 * iwin().sysconsRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();

            } else {
                Double dw1 = 0.0;
                Double dw2 = 0.0;
                if (anglCut1 != 90) {
                    dw1 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut1));
                }
                if (anglCut1 != 90) {
                    dw2 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut2));
                }
                //specif.width = specif.width + 2 * syssizeRec.ssizp - dw1.floatValue() - dw2.floatValue(); //TODO тут код незакончен
            }
            specif.anglCut1 = 90;
            specif.anglCut2 = 90;

            //Концевой профиль
        } else if (TypeArtikl1.KONZEVPROF.isType(artiklRec) == true) {
            String str = specif.getParam(0, 12030);
            str = str.replace(",", ".");
            Float koef = Float.valueOf(str);
            float ssizf = iwin().sysconsRec.getFloat(eSyssize.naxl);
            specif.width = (width() - ssizf) + (width() - ssizf) * koef;

            //Соединитель
        } else if (TypeArtikl1.SOEDINITEL.isType(artiklRec) == true) {
            specif.color1 = iwin().colorNone;
            specif.color2 = iwin().colorNone;
            specif.color3 = iwin().colorNone;
            
            //Всё остальное
        } else {
            //specif.element = "ЗАП";
        }
        quantityMaterials(specif);
        specificationRec.specificationList.add(specif);
    }

    @Override
    public void paint() {
        float d1z = artiklRec.getFloat(eArtikl.height);
        float h = iwin().heightAdd - iwin().height;
        float w = root().width();
        float y1h = y1 + h;
        float y2h = y2 + h;

        int rgb = eColor.find(color2).getInt(eColor.rgb);
        if (LayoutArea.ARCH == layout) { //прорисовка арки
            //TODO для прорисовки арки добавил один градус, а это не айс!
            //ElemFrame ef = owner.mapFrame.get(LayoutArea.ARCH);
            float d2z = artiklRec.getFloat(eArtikl.height);
            double r = ((AreaArch) root()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(owner().width() / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((owner().width() - 2 * d2z) / ((r - d2z) * 2)));
            strokeArc(owner().width() / 2 - r, -4, r * 2, r * 2, ang1, (90 - ang1) * 2 + 1, 0, 4); //прорисовка на сцену
            strokeArc(owner().width() / 2 - r + d2z, d2z - 2, (r - d2z) * 2, (r - d2z) * 2, ang2, (90 - ang2) * 2 + 1, 0, 4); //прорисовка на сцену
            strokeArc(owner().width() / 2 - r + d2z / 2, d2z / 2 - 2, (r - d2z / 2) * 2, (r - d2z / 2) * 2, ang2, (90 - ang2) * 2 + 1, rgb, d2z); //прорисовка на сцену

        } else if (LayoutArea.TOP == layout) {
            strokePolygon(x1, x2, x2 - d1z, x1 + d1z, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.BOTTOM == layout) {
            strokePolygon(x1 + d1z, x2 - d1z, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.LEFT == layout) {
            if (TypeElem.ARCH == owner().type) {
                double r = ((AreaArch) root()).radiusArch;
                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * d1z) / ((r - d1z) * 2)));
                double a = (r - d1z) * Math.sin(Math.toRadians(ang2));
                strokePolygon(x1, x2, x2, x1, y1, (float) (r - a - h), y2 - d1z, y2, rgb, borderColor);
            } else {
                strokePolygon(x1, x2, x2, x1, y1, y1 + d1z, y2 - d1z, y2, rgb, borderColor);
            }
        } else if (LayoutArea.RIGHT == layout) {
            if (TypeElem.ARCH == owner().type) {
                double r = ((AreaArch) root()).radiusArch;
                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * d1z) / ((r - d1z) * 2)));
                double a = (r - d1z) * Math.sin(Math.toRadians(ang2));
                strokePolygon(x1, x2, x2, x1, (float) (r - a - h), y1, y2, y2 - d1z, rgb, borderColor);
            } else {
                strokePolygon(x1, x2, x2, x1, y1 + d1z, y1, y2, y2 - d1z, rgb, borderColor);
            }
        }
    }

    @Override
    public UseArtikl typeProfile() {
        return (TypeElem.FULLSTVORKA == owner().type) ? UseArtikl.STVORKA : UseArtikl.FRAME;
    }

    @Override
    public void anglCut(int side, float anglCut) {
        if (side == 1) {
            anglCut1 = anglCut;
        } else {
            anglCut2 = anglCut;
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut=" + anglCut1 + ", anglCut=" + anglCut1;
    }
}
