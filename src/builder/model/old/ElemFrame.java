package builder.model.old;

import builder.model.UMod;
import builder.IArea5e;
import builder.IElem5e;
import builder.making.Paint;
import builder.making.Specific;
import builder.model.AreaArch;
import builder.model.AreaStvorka;
import builder.model.ElemJoining;
import builder.model.ElemSimple;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import common.UCom;
import domain.eArtikl;
import domain.eColor;
import domain.eSetting;
import domain.eSysprof;
import domain.eSyssize;
import enums.Form;
import enums.Layout;
import enums.PKjson;
import enums.Type;
import enums.TypeArtikl;
import enums.TypeJoin;
import enums.UseSide;
import frames.swing.Draw;
import java.util.List;
import java.util.Map;

public class ElemFrame extends ElemSimple {

    protected float lengthArch = 0; //длина арки 

    //Сторона коробки
    public ElemFrame(IArea5e owner, GsonElem gson) {
        this(owner, gson.id(), gson.layout(), gson.param(), gson);
    }

    //Сторона створки
    public ElemFrame(IArea5e owner, float id, Layout layout, JsonObject param, GsonElem gson) {
        super(id, owner.winc(), owner, gson);
        this.layout = layout;

        initСonstructiv(param);
        setLocation();
    }

    public void initСonstructiv(JsonObject param) {

        colorID1 = (isJson(param, PKjson.colorID1)) ? param.get(PKjson.colorID1).getAsInt() : winc.colorID1;
        colorID2 = (isJson(param, PKjson.colorID2)) ? param.get(PKjson.colorID2).getAsInt() : winc.colorID2;
        colorID3 = (isJson(param, PKjson.colorID3)) ? param.get(PKjson.colorID3).getAsInt() : winc.colorID3;

        if (isJson(param, PKjson.sysprofID)) { //профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());

        } else if (owner.sysprofRec() != null) { //профили через параметр рамы, створки
            sysprofRec = owner.sysprofRec();
        } else {
            if (Layout.BOTT.equals(layout)) {
                sysprofRec = eSysprof.find5(winc.nuni, type().id2, UseSide.BOT, UseSide.HORIZ);
            } else if (Layout.RIGHT.equals(layout)) {
                sysprofRec = eSysprof.find5(winc.nuni, type().id2, UseSide.RIGHT, UseSide.VERT);
            } else if (Layout.TOP.equals(layout)) {
                sysprofRec = eSysprof.find5(winc.nuni, type().id2, UseSide.TOP, UseSide.HORIZ);
            } else if (Layout.LEFT.equals(layout)) {
                sysprofRec = eSysprof.find5(winc.nuni, type().id2, UseSide.LEFT, UseSide.VERT);
            }
        }
        artiklRec(eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false));
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    //Установка координат
    @Override
    public void setLocation() {

        //Арка
        if (owner.type() == Type.ARCH) {
            if (Layout.BOTT == layout) {
                setDimension(owner.x1(), owner.y2() - artiklRec().getFloat(eArtikl.height), owner.x2(), owner.y2());
                anglHoriz = 0;
            } else if (Layout.RIGHT == layout) {
                setDimension(owner.x2() - artiklRec().getFloat(eArtikl.height), owner.y2() - winc.height2(), owner.x2(), owner.y2());
                anglHoriz = 90;
            } else if (Layout.TOP == layout) {
                setDimension(owner.x1(), owner.y1(), owner.x2(), owner.y1()); // + artiklRec.getFloat(eArtikl.height));
                anglHoriz = 180;
            } else if (Layout.LEFT == layout) {
                setDimension(owner.x1(), owner.y2() - winc.height2(), owner.x1() + artiklRec().getFloat(eArtikl.height), owner.y2());
                anglHoriz = 270;
            }

            //Трапеция
        } else if (owner.type() == Type.TRAPEZE) {
            float H = Math.abs(winc.height1() - winc.height2());
            float W = root().width();

            if (Layout.BOTT == layout) {
                setDimension(owner.x1(), owner.y2() - artiklRec().getFloat(eArtikl.height), owner.x2(), owner.y2());
                anglHoriz = 0;

            } else if (Layout.RIGHT == layout) {
                if (winc.form == Form.RIGHT) {
                    setDimension(owner.x2() - artiklRec().getFloat(eArtikl.height), owner.y2() - winc.height2(), owner.x2(), owner.y2());
                    anglCut[1] = (float) (180 - Math.toDegrees(Math.atan(W / H))) / 2;
                } else if (winc.form == Form.LEFT) {
                    setDimension(owner.x2() - artiklRec().getFloat(eArtikl.height), owner.y1(), owner.x2(), owner.y2());
                    anglCut[0] = (float) Math.toDegrees(Math.atan(W / H)) / 2;
                }
                anglHoriz = 90;

            } else if (Layout.TOP == layout) {
                if (winc.form == Form.RIGHT) {
                    setDimension(owner.x1(), owner.y1(), owner.x2(), winc.height1() - winc.height2());
                    anglHoriz = (float) (180 - Math.toDegrees(Math.atan(H / W)));
                    anglCut[0] = (float) (180 - Math.toDegrees(Math.atan(W / H))) / 2;
                    anglCut[1] = (float) Math.toDegrees(Math.atan(W / H)) / 2;
                } else if (winc.form == Form.LEFT) {
                    setDimension(owner.x1(), winc.height2() - winc.height1(), owner.x2(), owner.y1());
                    anglHoriz = (float) (180 + Math.toDegrees(Math.atan(H / W)));
                    anglCut[1] = (float) (180 - Math.toDegrees(Math.atan(W / H))) / 2;
                    anglCut[0] = (float) Math.toDegrees(Math.atan(W / H)) / 2;
                }

            } else if (Layout.LEFT == layout) {
                if (winc.form == Form.RIGHT) {
                    setDimension(owner.x1(), owner.y1(), owner.x1() + artiklRec().getFloat(eArtikl.height), owner.y2());
                    anglCut[0] = (float) (Math.toDegrees(Math.atan(W / H))) / 2;
                } else if (winc.form == Form.LEFT) {
                    setDimension(owner.x1(), owner.y2() - winc.height1(), owner.x1() + artiklRec().getFloat(eArtikl.height), owner.y2());
                    anglCut[0] = (float) (180 - Math.toDegrees(Math.atan(W / H))) / 2;
                }
                anglHoriz = 270;
            }

            //Остальное
        } else {
            if (Layout.BOTT == layout) {
                setDimension(owner.x1(), owner.y2() - artiklRec().getFloat(eArtikl.height), owner.x2(), owner.y2());
                anglHoriz = 0;
            } else if (Layout.RIGHT == layout) {
                setDimension(owner.x2() - artiklRec().getFloat(eArtikl.height), owner.y1(), owner.x2(), owner.y2());
                anglHoriz = 90;
            } else if (Layout.TOP == layout) {
                setDimension(owner.x1(), owner.y1(), owner.x2(), owner.y1() + artiklRec().getFloat(eArtikl.height));
                anglHoriz = 180;
            } else if (Layout.LEFT == layout) {
                setDimension(owner.x1(), owner.y1(), owner.x1() + artiklRec().getFloat(eArtikl.height), owner.y2());
                anglHoriz = 270;
            }
        }
    }

    //Главная спецификация
    @Override
    public void setSpecific() {  //добавление основной спецификации

        spcRec.place = "ВСТ." + layout.name.substring(0, 1).toLowerCase();
        spcRec.setArtiklRec(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;
        spcRec.anglCut1 = anglCut[0];
        spcRec.anglCut2 = anglCut[1];
        spcRec.anglHoriz = anglHoriz;
        double katet = winc.syssizeRec.getDbl(eSyssize.prip) * Math.cos(Math.PI / 4);

        if (owner.type() == Type.ARCH) {
            if (owner.type() == Type.ARCH && Layout.TOP == layout) {
                AreaArch areaArch = (AreaArch) root();
                double angl = Math.toDegrees(Math.asin((width() / 2) / areaArch.radiusArch));
                lengthArch = (float) ((2 * Math.PI * areaArch.radiusArch) / 360 * angl * 2);
                spcRec.width = lengthArch + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = owner.frames().get(Layout.TOP).artiklRec().getFloat(eArtikl.height);
            } else if (Layout.BOTT == layout) {
                spcRec.width = x2 - x1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.LEFT == layout) {
                spcRec.width = y2 - y1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.RIGHT == layout) {
                spcRec.width = y2 - y1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            }
        } else if (owner.type() == Type.TRAPEZE) {
            if (Layout.TOP == layout) {
                double length = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow(Math.abs(winc.height1() - winc.height2()), 2));
                spcRec.width = (float) (length + katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.BOTT == layout) {
                spcRec.width = x2 - x1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.LEFT == layout) {
                spcRec.width = y2 - y1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.RIGHT == layout) {
                spcRec.width = y2 - y1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            }
        } else {
            if (Layout.TOP == layout) {
                spcRec.width = x2 - x1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.BOTT == layout) {
                spcRec.width = x2 - x1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.LEFT == layout) {
                spcRec.width = y2 - y1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            } else if (Layout.RIGHT == layout) {
                spcRec.width = y2 - y1 + (float) (katet / UCom.sin(anglCut[0]) + katet / UCom.sin(anglCut[1]));
                spcRec.height = artiklRec().getFloat(eArtikl.height);
            }
        }
    }

    //Вложеная спецификация
    @Override
    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов
        try {
            spcAdd.count = UMod.get_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
            spcAdd.count += UMod.get_14050_24050_33050_38050(spcRec, spcAdd); //кол. ед. с шагом
            spcAdd.width = UMod.get_12050_15050_34051_39020(spcRec, spcAdd); //поправка мм

            //Армирование
            if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X107)) {
                spcAdd.place = "ВСТ." + layout.name.substring(0, 1).toLowerCase();
                spcAdd.anglCut1 = 90;
                spcAdd.anglCut2 = 90;

                if (Type.TRAPEZE == owner.type()) {
                    if (Layout.TOP == layout) {
                        spcAdd.width += length();

                    } else if (Layout.BOTT == layout) {
                        spcAdd.width += x2 - x1;

                    } else if (Layout.LEFT == layout || Layout.RIGHT == layout) {
                        spcAdd.width += y2 - y1;
                    }
                } else {
                    if (Layout.TOP == layout || Layout.BOTT == layout) {
                        spcAdd.width += x2 - x1;

                    } else if (Layout.LEFT == layout || Layout.RIGHT == layout) {
                        spcAdd.width += y2 - y1;
                    }
                }
                if ("ps3".equals(eSetting.find(2))) {
                    if ("Да".equals(spcAdd.getParam(null, 34010))) {
                        Double dw1 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[0]));
                        Double dw2 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[1]));
                        spcAdd.width = spcAdd.width + 2 * winc.syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
                    }
                } else {
                    if ("от внутреннего угла".equals(spcAdd.getParam(null, 34010))) {
                        Double dw1 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[0]));
                        Double dw2 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[1]));
                        spcAdd.width = spcAdd.width + 2 * winc.syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();

                    } else if ("от внутреннего фальца".equals(spcAdd.getParam(null, 34010))) {
                        Double dw1 = (artiklRec().getDbl(eArtikl.height) - artiklRec().getDbl(eArtikl.size_falz)) / Math.tan(Math.toRadians(anglCut[0]));
                        Double dw2 = (artiklRec().getDbl(eArtikl.height) - artiklRec().getDbl(eArtikl.size_falz)) / Math.tan(Math.toRadians(anglCut[1]));
                        spcAdd.width = spcAdd.width + 2 * winc.syssizeRec.getFloat(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
                    }
                }

            } else {
                //Выбран авто расчет подвеса
                if (spcAdd.getParam("null", 24013).equals("null") == false) {
                    if (spcAdd.getParam("null", 24013).equals("Да")) {
                        int color = winc.colorID1;
                        if (winc.colorID1 != spcAdd.colorID1) {
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
                        for (Map.Entry<Layout, IElem5e> elem : elemStv.frames().entrySet()) {
                            for (Specific spc : elem.getValue().spcRec().spcList) {
                                if (spc.artiklRec.getInt(eArtikl.level1) == 2 && spc.artiklRec.getInt(eArtikl.level2) == 12) {
                                    colorID = Paint.colorFromArtikl(spcAdd.artiklRec.getInt(eArtikl.id), 1, spc.colorID1);
                                }
                            }
                        }

                    } else if ("по текстуре замка".equals(spcAdd.getParam("null", 24006))) {
                        for (Map.Entry<Layout, IElem5e> elem : elemStv.frames().entrySet()) {
                            for (Specific spc : elem.getValue().spcRec().spcList) {
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
                    if (builder.making.Furniture.determOfSide(owner) == this) {
                        AreaStvorka stv = (AreaStvorka) owner;
                        stv.handleHeight = UCom.getFloat(spcAdd.getParam(stv.handleHeight, 24072, 25072));
                    }
                }
                //Укорочение от
                if (spcAdd.getParam("null", 25013).equals("null") == false) {
                    if ("длины стороны".equals(spcAdd.getParam("null", 25013))) {
                        spcAdd.width = length() - UCom.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм

                    } else if ("высоты ручки".equals(spcAdd.getParam("null", 25013))) {
                        AreaStvorka stv = (AreaStvorka) owner;
                        spcAdd.width = stv.handleHeight - UCom.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм

                    } else if ("сторона - выс. ручки".equals(spcAdd.getParam("null", 25013))) {
                        AreaStvorka stv = (AreaStvorka) owner;
                        spcAdd.width = lengthArch - stv.handleHeight - UCom.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм                        

                    } else if ("половины стороны".equals(spcAdd.getParam("null", 25013))) {
                        spcAdd.width = (lengthArch / 2) - UCom.getFloat(spcAdd.getParam(0, 25030)); //укорочение, мм 
                    }
                }
                //Фурнитура
                if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X109)) {
                    if (layout.id == Integer.valueOf(spcAdd.getParam("0", 24010, 25010, 38010, 39002))) {  //"номер стороны"   
                        if ("null".equals(spcAdd.getParam("null", 25013)) == false //"укорочение от"
                                && spcAdd.getParam(0, 25030).equals(0) == false) { //"укорочение, мм"  
                            spcAdd.width = UMod.get_25013(spcRec, spcAdd); //укорочение от высоты ручки
                        }
                    } else {
                        spcAdd.width += width() + winc.syssizeRec.getFloat(eSyssize.prip) * 2;
                    }
                } else if (List.of(1, 3, 5).contains(spcAdd.artiklRec.getInt(eArtikl.level1))) {
                    spcAdd.width += spcRec.width;
                }
            }
            UMod.get_12075_34075_39075(this, spcAdd); //углы реза
            UMod.get_34077_39077(spcAdd); //задать Угол_реза_1/Угол_реза_2
            spcAdd.height = UCom.getFloat(spcAdd.getParam(spcAdd.height, 40006)); ////высота заполнения, мм 
            spcAdd.width = UMod.get_12065_15045_25040_34070_39070(spcRec, spcAdd); //длина мм
            spcAdd.width = UCom.getFloat(spcAdd.getParam(spcAdd.width, 40004)); //ширина заполнения, мм        
            spcAdd.width = spcAdd.width * UMod.get_12030_15030_25035_34030_39030(spcRec, spcAdd);//"[ * коэф-т ]"
            spcAdd.width = spcAdd.width / UMod.get_12040_15031_25036_34040_39040(spcRec, spcAdd);//"[ / коэф-т ]"
            UMod.get_40007(spcAdd); //высоту сделать длиной
            spcAdd.count = UMod.get_11070_12070_33078_34078(spcAdd); //ставить однократно
            spcAdd.count = UMod.get_39063(spcAdd); //округлять количество до ближайшего

            spcRec.spcList.add(spcAdd);
            
        } catch (Exception e) {
            System.err.println("Ошибка old :ElemFrame.addSpecific() " + e);
        }
    }

    @Override
    public void paint() {
        try {
            ElemJoining ej1 = winc.mapJoin.get(joinPoint(0));
            ElemJoining ej2 = winc.mapJoin.get(joinPoint(1));
            float dh = artiklRec().getFloat(eArtikl.height);
            float dh0 = (winc.mapJoin.get(joinPoint(0)).type == TypeJoin.VAR30 || winc.mapJoin.get(joinPoint(0)).type == TypeJoin.VAR31) ? 0 : dh;
            float dh1 = (winc.mapJoin.get(joinPoint(1)).type == TypeJoin.VAR30 || winc.mapJoin.get(joinPoint(1)).type == TypeJoin.VAR31) ? 0 : dh;
            int rgb = eColor.find(colorID2).getInt(eColor.rgb);

            //ARCH
            if (owner.type() == Type.ARCH) {

                if (Layout.BOTT == layout) {
                    Draw.strokePolygon(winc, x1 + dh0, x2 - dh1, x2, x1, y1, y1, y2, y2, rgb, borderColor);

                } else if (Layout.RIGHT == layout) {
                    double r = ((AreaArch) root()).radiusArch;
                    double ang2 = 90 - Math.toDegrees(Math.asin((root().width() - 2 * dh) / ((r - dh) * 2)));
                    double a = (r - dh) * UCom.sin(ang2);
                    Draw.strokePolygon(winc, x1, x2, x2, x1, (float) (r - a), y1, y2, y2 - dh0, rgb, borderColor);

                } else if (Layout.TOP == layout) { //прорисовка арки
                    //TODO для прорисовки арки добавил один градус, а это не айс!                  
                    double r = ((AreaArch) root()).radiusArch;
                    double ang1 = 90 - Math.toDegrees(Math.asin(owner.width() / (r * 2)));
                    double ang2 = 90 - Math.toDegrees(Math.asin((owner.width() - 2 * dh) / ((r - dh) * 2)));
                    Draw.strokeArc(winc, owner.width() / 2 - r + dh / 2, dh / 2 - 2, (r - dh / 2) * 2, (r - dh / 2) * 2, ang2, (90 - ang2) * 2 + 1, rgb, dh);
                    Draw.strokeArc(winc, owner.width() / 2 - r, -4, r * 2, r * 2, ang1, (90 - ang1) * 2 + 1, 0, 4);
                    Draw.strokeArc(winc, owner.width() / 2 - r + dh, dh - 2, (r - dh) * 2, (r - dh) * 2, ang2, (90 - ang2) * 2 + 1, 0, 4);

                } else if (Layout.LEFT == layout) {
                    double r = ((AreaArch) root()).radiusArch;
                    double ang2 = 90 - Math.toDegrees(Math.asin((root().width() - 2 * dh) / ((r - dh) * 2)));
                    double a = (r - dh) * UCom.sin(ang2);
                    Draw.strokePolygon(winc, x1, x2, x2, x1, y1, (float) (r - a), y2 - dh1, y2, rgb, borderColor);
                }
                //TRAPEZE
            } else if (owner.type() == Type.TRAPEZE) {
                if (Layout.BOTT == layout) {
                    Draw.strokePolygon(winc, x1 + dh0, x2 - dh1, x2, x1, y1, y1, y2, y2, rgb, borderColor);

                } else if (Layout.RIGHT == layout) {
                    double angl = (winc.form == Form.RIGHT) ? Math.toRadians(90 - anglCut[1]) : Math.toRadians(90 - anglCut[0]);
                    float dh2 = (float) (dh * Math.tan(angl));
                    Draw.strokePolygon(winc, x1, x2, x2, x1, y1 + dh2, y1, y2, y2 - dh0, rgb, borderColor);

                } else if (Layout.TOP == layout) {
                    float dy = (float) (artiklRecAn.getDbl(eArtikl.height) / UCom.sin(anglHoriz - 90));
                    Draw.strokePolygon(winc, x1, x2, x2, x1, y1, y2, y2 + dy, y1 + dy, rgb, borderColor);

                } else if (Layout.LEFT == layout) {
                    double angl = Math.toRadians(90 - anglCut[0]);
                    float dh2 = (float) (dh * Math.tan(angl));
                    Draw.strokePolygon(winc, x1, x2, x2, x1, y1, y1 + dh2, y2 - dh1, y2, rgb, borderColor);
                }
            } else {
                if (Layout.BOTT == layout) {
                    Draw.strokePolygon(winc, x1 + dh0, x2 - dh1, x2, x1, y1, y1, y2, y2, rgb, borderColor);
                } else if (Layout.RIGHT == layout) {
                    Draw.strokePolygon(winc, x1, x2, x2, x1, y1 + dh1, y1, y2, y2 - dh0, rgb, borderColor);
                } else if (Layout.TOP == layout) {
                    Draw.strokePolygon(winc, x1, x2, x2 - dh0, x1 + dh1, y1, y1, y2, y2, rgb, borderColor);
                } else if (Layout.LEFT == layout) {
                    Draw.strokePolygon(winc, x1, x2, x2, x1, y1, y1 + dh0, y2 - dh1, y2, rgb, borderColor);
                }
            }
        } catch (Exception s) {
            System.err.println("ОШИБКА:model.IElem5e.paint()");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut1=" + anglCut[0] + ", anglCut2=" + anglCut[1];
    }
}