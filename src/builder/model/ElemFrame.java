package builder.model;

import builder.making.Paint;
import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSysprof;
import enums.Layout;
import enums.TypeArtikl;
import builder.making.Specific;
import enums.PKjson;
import common.Util;
import domain.eSetting;
import enums.Type;
import java.util.Arrays;
import java.util.Map;
import builder.making.Furniture;
import enums.UseSide;

public class ElemFrame extends ElemSimple {

    protected float length = 0; //длина арки 

    public ElemFrame(AreaSimple owner, float id, Layout layout, String param) {
        super(id, owner.iwin(), owner);
        this.layout = layout;
        colorID1 = iwin().colorID1;
        colorID2 = iwin().colorID2;
        colorID3 = iwin().colorID3;
        this.type = (Type.STVORKA == owner.type) ? Type.STVORKA_SIDE : Type.FRAME_SIDE;
        initСonstructiv(param);
        setLocation();
    }

    public void initСonstructiv(String par) {

        colorID1 = (param(par, PKjson.colorID1) != -1) ? param(par, PKjson.colorID1) : colorID1;
        colorID2 = (param(par, PKjson.colorID2) != -1) ? param(par, PKjson.colorID2) : colorID2;
        colorID3 = (param(par, PKjson.colorID3) != -1) ? param(par, PKjson.colorID3) : colorID3;
        
        if (param(par, PKjson.sysprofID) != -1) { //профили через параметр
            sysprofRec = eSysprof.find3(param(par, PKjson.sysprofID));
            
        } else if(owner.sysprofRec != null) { //профили через параметр рамы, створки
            sysprofRec = owner.sysprofRec;
        } else {
            if (Layout.BOTT.equals(layout())) {
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.MANUAL, UseSide.BOT, UseSide.HORIZ, UseSide.ANY);
            } else if (Layout.RIGHT.equals(layout())) {
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.MANUAL, UseSide.RIGHT, UseSide.VERT, UseSide.ANY);
            } else if (Layout.TOP.equals(layout())) {
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.MANUAL, UseSide.TOP, UseSide.HORIZ, UseSide.ANY);
            } else if (Layout.LEFT.equals(layout())) {
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.MANUAL, UseSide.LEFT, UseSide.VERT, UseSide.ANY);
            } else if (Layout.ARCH.equals(layout())) {
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.MANUAL, UseSide.TOP, UseSide.HORIZ, UseSide.ANY);
            }
        }
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    //Установка координат
    public void setLocation() {

        if (Layout.BOTT == layout) {
            setDimension(owner().x1, owner().y2 - artiklRec.getFloat(eArtikl.height), owner().x2, owner().y2);
            anglHoriz = 0;

        } else if (Layout.RIGHT == layout) {
            setDimension(owner().x2 - artiklRec.getFloat(eArtikl.height), owner().y1, owner().x2, owner().y2);
            anglHoriz = 90;

        } else if (Layout.TOP == layout) {
            setDimension(owner().x1, owner().y1, owner().x2, owner().y1 + artiklRec.getFloat(eArtikl.height));
            anglHoriz = 180;

        } else if (Layout.LEFT == layout) {
            setDimension(owner().x1, owner().y1, owner().x1 + artiklRec.getFloat(eArtikl.height), owner().y2);
            anglHoriz = 270;

        } else if (Layout.ARCH == layout) {
            setDimension(owner().x1, owner().y1, owner().x2, owner().y1); // + artiklRec.getFloat(eArtikl.height));
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
        spcRec.anglCut1 = anglCut[0];
        spcRec.anglCut2 = anglCut[1];
        spcRec.anglHoriz = anglHoriz;
        double katet = iwin().syssizeRec.getDbl(eSyssize.prip) * Math.cos(Math.PI / 4);

        if (Layout.ARCH == layout()) {
            ((AreaArch) root()).frame(this, katet);

        } else if (Layout.TOP == layout) {
            spcRec.width = x2 - x1 + (float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (Layout.BOTT == layout) {
            spcRec.width = x2 - x1 + +(float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (Layout.LEFT == layout) {
            spcRec.width = y2 - y1 + (float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);

        } else if (Layout.RIGHT == layout) {
            spcRec.width = y2 - y1 + (float) (katet / Math.sin(Math.toRadians(anglCut[0])) + katet / Math.sin(Math.toRadians(anglCut[1])));
            spcRec.height = artiklRec.getFloat(eArtikl.height);
        }
        spcRec.width = spcRec.width + Util.getFloat(spcRec.getParam(0, 2030, 3050, 4050));
        spcRec.width = spcRec.width + Util.getFloat(spcRec.getParam(0, 2040, 3060));
    }

    @Override //Вложеная спецификация
    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов

        spcAdd.count = uti3.get_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += uti3.get_14050_24050_33050_38050(spcAdd); //кол. ед. с шагом
        spcAdd.width = uti3.get_12050_15050_34051_39020(spcRec, spcAdd); //поправка мм

        //Армирование
        if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X107)) {
            spcAdd.place = "ВСТ." + layout().name.substring(0, 1);
            spcAdd.anglCut1 = 90;
            spcAdd.anglCut2 = 90;

            if (Layout.TOP == layout || Layout.BOTT == layout) {
                spcAdd.width += x2 - x1;

            } else if (Layout.LEFT == layout || Layout.RIGHT == layout) {
                spcAdd.width += y2 - y1;
            }
            if ("ps3".equals(eSetting.find(2))) {
                if ("Да".equals(spcAdd.getParam(null, 34010))) {
                    Double dw1 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[0]));
                    Double dw2 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[1]));
                    spcAdd.width = spcAdd.width + 2 * iwin().syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
                }
            } else {
                if ("от внутреннего угла".equals(spcAdd.getParam(null, 34010))) {
                    Double dw1 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[0]));
                    Double dw2 = artiklRec.getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[1]));
                    spcAdd.width = spcAdd.width + 2 * iwin().syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();

                } else if ("от внутреннего фальца".equals(spcAdd.getParam(null, 34010))) {
                    Double dw1 = (artiklRec.getDbl(eArtikl.height) - artiklRec.getDbl(eArtikl.size_falz)) / Math.tan(Math.toRadians(anglCut[0]));
                    Double dw2 = (artiklRec.getDbl(eArtikl.height) - artiklRec.getDbl(eArtikl.size_falz)) / Math.tan(Math.toRadians(anglCut[1]));
                    spcAdd.width = spcAdd.width + 2 * iwin().syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
                }
            }

        } else {
            //Выбран авто расчет подвеса
            if (spcAdd.getParam("null", 24013).equals("null") == false) {
                if (spcAdd.getParam("null", 24013).equals("Да")) {
                    int color = iwin().colorID1;
                    if (iwin().colorID1 != spcAdd.colorID1) {
                        return;
                    }
                }
            }
            //Установить текстуру
            if (spcAdd.getParam("null", 24006).equals("null") == false) {
                int colorID = -1;
                AreaStvorka elemStv = ((AreaStvorka) owner);
                if ("по текстуре ручки".equals(spcAdd.getParam("null", 24006))) {
                    colorID = Paint.colorFromArtikl(spcAdd.artiklRec.getInt(eArtikl.id), 1, elemStv.handleColor);

                } else if ("по текстуре подвеса".equals(spcAdd.getParam("null", 24006))) {
                    for (Map.Entry<Layout, ElemFrame> elem : elemStv.mapFrame.entrySet()) {
                        for (Specific spc : elem.getValue().spcRec.spcList) {
                            if (spc.artiklRec.getInt(eArtikl.level1) == 2 && spc.artiklRec.getInt(eArtikl.level2) == 12) {
                                colorID = Paint.colorFromArtikl(spcAdd.artiklRec.getInt(eArtikl.id), 1, spc.colorID1);
                            }
                        }
                    }

                } else if ("по текстуре замка".equals(spcAdd.getParam("null", 24006))) {
                    for (Map.Entry<Layout, ElemFrame> elem : elemStv.mapFrame.entrySet()) {
                        for (Specific spc : elem.getValue().spcRec.spcList) {
                            if (spc.artiklRec.getInt(eArtikl.level1) == 2 && spc.artiklRec.getInt(eArtikl.level2) == 9) {
                                colorID = Paint.colorFromArtikl(spcAdd.artiklRec.getInt(eArtikl.id), 1, spc.colorID1);
                            }
                        }
                    }
                }
                if (colorID != -1) {
                    spcAdd.colorID1 = colorID;
                    spcAdd.colorID2 = colorID;
                    spcAdd.colorID3 = colorID;
                }
            }
            //Ручка от низа створки, мм 
            if (spcAdd.getParam("null", 24072, 25072).equals("null") == false) {
                if (Furniture.determOfSide(owner) == this) {
                    AreaStvorka stv = (AreaStvorka) owner;
                    stv.handleHeight = Util.getFloat(spcAdd.getParam(stv.handleHeight, 24072, 25072));
                }
            }
            //Укорочение от
            if (spcAdd.getParam("null", 25013).equals("null") == false) {
                if ("длины стороны".equals(spcAdd.getParam("null", 25013))) {
                    spcAdd.width = length() - Util.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм

                } else if ("высоты ручки".equals(spcAdd.getParam("null", 25013))) {
                    AreaStvorka stv = (AreaStvorka) owner;
                    spcAdd.width = stv.handleHeight - Util.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм

                } else if ("сторона - выс. ручки".equals(spcAdd.getParam("null", 25013))) {
                    AreaStvorka stv = (AreaStvorka) owner;
                    spcAdd.width = length - stv.handleHeight - Util.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм                        

                } else if ("половины стороны".equals(spcAdd.getParam("null", 25013))) {
                    spcAdd.width = (length / 2) - Util.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм 
                }
            }
            //Фурнитура
            if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X109)) {
                if (layout.id == Integer.valueOf(spcAdd.getParam("0", 24010, 25010, 38010, 39002))) {  //"номер стороны"   
                    if ("null".equals(spcAdd.getParam("null", 25013)) == false //"укорочение от"
                            && spcAdd.getParam(0, 25030).equals(0) == false) { //"укорочение, мм"  
                        spcAdd.width = uti3.get_25013(spcRec, spcAdd); //укорочение от высоты ручки
                    }
                } else {
                    spcAdd.width += width() + iwin().syssizeRec.getFloat(eSyssize.prip) * 2;
                }
            } else if (Arrays.asList(1, 3, 5).contains(spcAdd.artiklRec.getInt(eArtikl.level1))) {
                spcAdd.width += spcRec.width;
            }
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
        float d1z = artiklRec.getFloat(eArtikl.height);
        float w = root().width();

        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        if (Layout.ARCH == layout) { //прорисовка арки
            //TODO для прорисовки арки добавил один градус, а это не айс!
            float d2z = artiklRec.getFloat(eArtikl.height);
            double r = ((AreaArch) root()).radiusArch;
            double ang1 = 90 - Math.toDegrees(Math.asin(owner().width() / (r * 2)));
            double ang2 = 90 - Math.toDegrees(Math.asin((owner().width() - 2 * d2z) / ((r - d2z) * 2)));

            iwin().draw.strokeArc(owner().width() / 2 - r + d2z / 2, d2z / 2 - 2, (r - d2z / 2) * 2, (r - d2z / 2) * 2, ang2, (90 - ang2) * 2 + 1, rgb, d2z);
            iwin().draw.strokeArc(owner().width() / 2 - r, -4, r * 2, r * 2, ang1, (90 - ang1) * 2 + 1, 0, 4);
            iwin().draw.strokeArc(owner().width() / 2 - r + d2z, d2z - 2, (r - d2z) * 2, (r - d2z) * 2, ang2, (90 - ang2) * 2 + 1, 0, 4);

        } else if (Layout.TOP == layout) {
            iwin().draw.strokePolygon(x1, x2, x2 - d1z, x1 + d1z, y1, y1, y2, y2, rgb, borderColor);

        } else if (Layout.BOTT == layout) {
            iwin().draw.strokePolygon(x1 + d1z, x2 - d1z, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (Layout.LEFT == layout) {
            if (Type.ARCH == owner().type) {
                double r = ((AreaArch) root()).radiusArch;
                double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * d1z) / ((r - d1z) * 2)));
                double a = (r - d1z) * Math.sin(Math.toRadians(ang2));
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1, (float) (r - a), y2 - d1z, y2, rgb, borderColor);
            } else {
                iwin().draw.strokePolygon(x1, x2, x2, x1, y1, y1 + d1z, y2 - d1z, y2, rgb, borderColor);
            }
        } else if (Layout.RIGHT == layout) {
            if (Type.ARCH == owner().type) {
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
