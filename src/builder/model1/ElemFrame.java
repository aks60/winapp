package builder.model1;

import builder.IArea5e;
import builder.IElem5e;
import builder.IStvorka;
import builder.model2.UGeo;
import domain.eArtikl;
import domain.eColor;
import domain.eSyssize;
import domain.eSysprof;
import enums.Layout;
import enums.TypeArtikl;
import builder.making.Specific;
import builder.making.UColor;
import enums.PKjson;
import common.UCom;
import domain.eSetting;
import enums.Type;
import java.util.Map;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import enums.Form;
import enums.TypeJoin;
import enums.UseSide;
import frames.swing.DrawStroke;
import java.util.List;

public class ElemFrame extends ElemSimple {

    protected double lengthArch = 0; //длина арки 

    //Сторона коробки
    public ElemFrame(IArea5e owner, GsonElem gson) {
        this(owner, gson.id(), gson.layout(), gson.param(), gson);
    }

    //Сторона створки
    public ElemFrame(IArea5e owner, double id, Layout layout, JsonObject param, GsonElem gson) {
        super(id, owner.winc(), owner, gson);
        this.layout = layout;

        initСonstructiv(param);
        setLocation();
    }

    /**
     * Профиль через параметр или первая запись в системе см. табл. sysprof Цвет
     * если нет параметра то берём winc.color.
     */
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
                sysprofRec = eSysprof.find5(winc.nuni(), type().id2, UseSide.BOT, UseSide.HORIZ);
            } else if (Layout.RIGHT.equals(layout)) {
                sysprofRec = eSysprof.find5(winc.nuni(), type().id2, UseSide.RIGHT, UseSide.VERT);
            } else if (Layout.TOP.equals(layout) || Layout.TOPR.equals(layout) || Layout.TOPL.equals(layout)) {
                sysprofRec = eSysprof.find5(winc.nuni(), type().id2, UseSide.TOP, UseSide.HORIZ);
            } else if (Layout.LEFT.equals(layout)) {
                sysprofRec = eSysprof.find5(winc.nuni(), type().id2, UseSide.LEFT, UseSide.VERT);
            }
        }
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);

        //Системные константы как правило на всю систему профилей
        if (winc.syssizeRec() == null) {
            winc.syssizeRec(eSyssize.find(artiklRec));
        }
    }

    /**
     * Установка координат фреймов с учётов типа конст. и формы контура x1y1 -
     * верхняя левая точка x2y2 - нижняя правая точка
     */
    public void setLocation() {

        //Арка
        if (owner.type() == Type.ARCH) {
            if (Layout.BOTT == layout) {
                setDimension(owner.x1(), owner.y2(), owner.x2(), owner.y2());
            } else if (Layout.RIGHT == layout) {
                setDimension(owner.x2(), owner.y2(), owner.x2(), owner.y2() - winc.height2());
            } else if (Layout.TOP == layout) {
                setDimension(owner.x1(), owner.y1(), owner.x2(), owner.y1());
            } else if (Layout.LEFT == layout) {
                setDimension(owner.x1(), owner.y2() - winc.height2(), owner.x1(), owner.y2());
            }

            //Трапеция
        } else if (owner.type() == Type.TRAPEZE) {
            double H = Math.abs(winc.height1() - winc.height2());
            double W = root().width();
            double Dx = (winc.width1() - winc.width2()) / 2;
            double Dy = (winc.height1() - winc.height2()) / 2;

            if (Layout.BOTT == layout) {
                setDimension(owner.x1(), owner.y2(), owner.x2(), owner.y2());

            } else if (Layout.RIGHT == layout) {
                if (winc.form == Form.RIGHT) {
                    setDimension(owner.x2(), owner.y2(), owner.x2(), winc.height1() - winc.height2());
                } else if (winc.form == Form.LEFT) {
                    setDimension(owner.x2(), owner.y2(), owner.x2(), owner.y1());
                } else if (winc.form == Form.SYMM && winc.height1() == winc.height2()) {
                    setDimension(owner.x2(), owner.y2(), owner.x2() - Dx, owner.y1());
                } else if (winc.form == Form.SYMM && winc.height1() != winc.height2()) {
                    setDimension(owner.x2(), owner.y2(), owner.x2(), owner.y1() + Dy);
                }
            } else if (Layout.TOPR == layout && winc.form == Form.SYMM) {
                setDimension(owner.x2(), owner.y1() + Dy, owner.x2() - Dx, owner.y1());
                
            } else if (Layout.TOP == layout) {
                if (winc.form == Form.RIGHT) {
                    setDimension(owner.x2(), winc.height1() - winc.height2(), owner.x1(), owner.y1());
                } else if (winc.form == Form.LEFT) {
                    setDimension(owner.x2(), owner.y1(), owner.x1(), winc.height2() - winc.height1());
                } else if (winc.form == Form.SYMM && winc.height1() == winc.height2()) {
                    setDimension(owner.x2() - Dx, owner.y1(), owner.x1() + Dx, owner.y1());              
                } else if (winc.form == Form.SYMM && winc.height1() != winc.height2()) {
                    setDimension(owner.x2() - Dx, owner.y1(), owner.x1() + Dx, owner.y1());
                }               
            } else if (Layout.TOPL == layout && winc.form == Form.SYMM) { 
                setDimension(owner.x1() + Dx, owner.y1(), owner.x1(), owner.y1() + Dy);

            } else if (Layout.LEFT == layout) {
                if (winc.form == Form.RIGHT) {
                    setDimension(owner.x1(), owner.y1(), owner.x1(), owner.y2());
                } else if (winc.form == Form.LEFT) {
                    setDimension(owner.x1(), winc.height2() - winc.height1(), owner.x1(), owner.y2());
                } else if (winc.form == Form.SYMM && winc.height1() == winc.height2()) {
                    setDimension(owner.x1() + Dx, owner.y1(), owner.x1(), owner.y2());
                } else if (winc.form == Form.SYMM && winc.height1() != winc.height2()) {
                    setDimension(owner.x1(), owner.y1() + Dy, owner.x1(), owner.y2());
                }
            }

            //Остальное
        } else {
            if (Layout.BOTT == layout) {
                setDimension(owner.x1(), owner.y2(), owner.x2(), owner.y2());
            } else if (Layout.RIGHT == layout) {
                setDimension(owner.x2(), owner.y2(), owner.x2(), owner.y1());
            } else if (Layout.TOP == layout) {
                setDimension(owner.x2(), owner.y1(), owner.x1(), owner.y1());
            } else if (Layout.LEFT == layout) {
                setDimension(owner.x1(), owner.y1(), owner.x1(), owner.y2());
            }
        }
    }

    //Главная спецификация
    @Override
    public void setSpecific() {  //добавление основной спецификации
        try {
            spcRec.place = "ВСТ." + layout.name.substring(0, 1).toLowerCase();
            spcRec.setArtikl(artiklRec);
            spcRec.colorID1 = colorID1;
            spcRec.colorID2 = colorID2;
            spcRec.colorID3 = colorID3;
            spcRec.anglCut0 = anglCut[0];
            spcRec.anglCut1 = anglCut[1];
            spcRec.anglHoriz = anglHoriz;

            if (owner.type() == Type.ARCH) {
                if (Layout.TOP == layout) {
                    AreaArch areaArch = (AreaArch) root();
                    double angl = UGeo.asin((width() / 2) / areaArch.radiusArch);
                    lengthArch = ((2 * Math.PI * areaArch.radiusArch) / 360 * angl * 2);
                    spcRec.width = lengthArch + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = owner.frames().get(Layout.TOP).artiklRec().getDbl(eArtikl.height);
                } else if (Layout.BOTT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.LEFT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.RIGHT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                }
            } else if (owner.type() == Type.TRAPEZE) {
                if (Layout.TOP == layout) {
                    double length = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow(Math.abs(winc.height1() - winc.height2()), 2));
                    spcRec.width = length + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.BOTT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.LEFT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.RIGHT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                }
            } else {
                if (Layout.BOTT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.RIGHT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.TOP == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                } else if (Layout.LEFT == layout) {
                    spcRec.width = length() + 2 * winc.syssizeRec().getDbl(eSyssize.prip);
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:ElemFrame.setSpecific() " + e);
        }
    }

    //Вложеная спецификация
    @Override
    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов
        try {
            spcAdd.count = UPar.to_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcAdd); //кол. ед. с учётом парам. 
            spcAdd.count += UPar.to_14050_24050_33050_38050(spcRec, spcAdd); //кол. ед. с шагом
            spcAdd.width += UPar.to_12050_15050_34051_39020(spcAdd); //поправка мм

            //Армирование
            if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X107)) {
                spcAdd.place = "ВСТ." + layout.name.substring(0, 1).toLowerCase();
                spcAdd.anglCut0 = 90;
                spcAdd.anglCut1 = 90;

                if (Type.TRAPEZE == owner.type()) {
                    if (Layout.TOP == layout) {
                        spcAdd.width += length();

                    } else if (Layout.BOTT == layout) {
                        spcAdd.width += length();

                    } else if (Layout.LEFT == layout || Layout.RIGHT == layout) {
                        spcAdd.width += length();
                    }
                } else {
                    if (Layout.TOP == layout || Layout.BOTT == layout) {
                        spcAdd.width += length();

                    } else if (Layout.LEFT == layout || Layout.RIGHT == layout) {
                        spcAdd.width += length();
                    }
                }
                if ("ps3".equals(eSetting.val(2))) {
                    if ("Да".equals(spcAdd.getParam(null, 34010))) {
                        Double dw1 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[0]));
                        Double dw2 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[1]));
                        spcAdd.width = spcAdd.width + 2 * winc.syssizeRec().getDbl(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
                    }
                } else {
                    if ("от внутреннего угла".equals(spcAdd.getParam(null, 34010))) {
                        Double dw1 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[0]));
                        Double dw2 = artiklRec().getDbl(eArtikl.height) / Math.tan(Math.toRadians(anglCut[1]));
                        spcAdd.width = spcAdd.width + 2 * winc.syssizeRec().getDbl(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();

                    } else if ("от внутреннего фальца".equals(spcAdd.getParam(null, 34010))) {
                        Double dw1 = (artiklRec().getDbl(eArtikl.height) - artiklRec().getDbl(eArtikl.size_falz)) / Math.tan(Math.toRadians(anglCut[0]));
                        Double dw2 = (artiklRec().getDbl(eArtikl.height) - artiklRec().getDbl(eArtikl.size_falz)) / Math.tan(Math.toRadians(anglCut[1]));
                        spcAdd.width = spcAdd.width + 2 * winc.syssizeRec().getDbl(eSyssize.prip) - dw1.floatValue() - dw2.floatValue();
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
                    IStvorka elemStv = ((IStvorka) owner);
                    IArea5e areaStv = ((IArea5e) owner);
                    if ("по текстуре ручки".equals(spcAdd.getParam("null", 24006))) {
                        colorID = UColor.colorFromArtikl(spcAdd.artiklRec.getInt(eArtikl.id), 1, elemStv.handleColor());

                    } else if ("по текстуре подвеса".equals(spcAdd.getParam("null", 24006))) {
                        for (Map.Entry<Layout, IElem5e> elem : areaStv.frames().entrySet()) {
                            for (Specific spc : elem.getValue().spcRec().spcList) {
                                if (spc.artiklRec.getInt(eArtikl.level1) == 2 && spc.artiklRec.getInt(eArtikl.level2) == 12) {
                                    colorID = UColor.colorFromArtikl(spcAdd.artiklRec.getInt(eArtikl.id), 1, spc.colorID1);
                                }
                            }
                        }

                    } else if ("по текстуре замка".equals(spcAdd.getParam("null", 24006))) {
                        for (Map.Entry<Layout, IElem5e> elem : areaStv.frames().entrySet()) {
                            for (Specific spc : elem.getValue().spcRec().spcList) {
                                if (spc.artiklRec.getInt(eArtikl.level1) == 2 && spc.artiklRec.getInt(eArtikl.level2) == 9) {
                                    colorID = UColor.colorFromArtikl(spcAdd.artiklRec.getInt(eArtikl.id), 1, spc.colorID1);
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
                        IStvorka stv = (IStvorka) owner;
                        stv.handleHeight(UCom.getDbl(spcAdd.getParam(stv.handleHeight(), 24072, 25072)));
                    }
                }
                //Укорочение от
                if (spcAdd.getParam("null", 25013).equals("null") == false) {
                    if ("длины стороны".equals(spcAdd.getParam("null", 25013))) {
                        spcAdd.width = length() - UCom.getDbl(spcAdd.getParam(0, 25030)); //укорочение, мм

                    } else if ("высоты ручки".equals(spcAdd.getParam("null", 25013))) {
                        IStvorka stv = (IStvorka) owner;
                        spcAdd.width = stv.handleHeight() - UCom.getDbl(spcAdd.getParam(0, 25030)); //укорочение, мм

                    } else if ("сторона - выс. ручки".equals(spcAdd.getParam("null", 25013))) {
                        IStvorka stv = (IStvorka) owner;
                        spcAdd.width = lengthArch - stv.handleHeight() - UCom.getDbl(spcAdd.getParam(0, 25030)); //укорочение, мм                        

                    } else if ("половины стороны".equals(spcAdd.getParam("null", 25013))) {
                        spcAdd.width = (lengthArch / 2) - UCom.getDbl(spcAdd.getParam(0, 25030)); //укорочение, мм 
                    }
                }
                //Фурнитура
                if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X109)) {
                    if (layout.id == Integer.valueOf(spcAdd.getParam("0", 24010, 25010, 38010, 39002))) {  //"номер стороны"   
                        if ("null".equals(spcAdd.getParam("null", 25013)) == false //"укорочение от"
                                && spcAdd.getParam(0, 25030).equals(0) == false) { //"укорочение, мм"  
                            spcAdd.width = UPar.to_25013(spcRec, spcAdd); //укорочение от высоты ручки
                        }
                    } else {
                        spcAdd.width += width() + winc.syssizeRec().getDbl(eSyssize.prip) * 2;
                    }

                } else if (List.of(1, 3, 5).contains(spcAdd.artiklRec.getInt(eArtikl.level1)) && spcRec.id != spcAdd.id) {
                    spcAdd.width += spcRec.width;
                }
            }
            UPar.to_12075_34075_39075(this, spcAdd); //углы реза
            UPar.to_34077_39077(spcAdd); //задать Угол_реза_1/Угол_реза_2
            spcAdd.height = UCom.getDbl(spcAdd.getParam(spcAdd.height, 40006)); //высота заполнения, мм 
            spcAdd.width = UPar.to_12065_15045_25040_34070_39070(spcAdd); //длина мм
            spcAdd.width = UCom.getDbl(spcAdd.getParam(spcAdd.width, 40004)); //ширина заполнения, мм        
            spcAdd.width = spcAdd.width * UPar.to_12030_15030_25035_34030_39030(spcAdd);//"[ * коэф-т ]"
            spcAdd.width = spcAdd.width / UPar.to_12040_15031_25036_34040_39040(spcAdd);//"[ / коэф-т ]"
            UPar.to_40005_40010(spcAdd); //Поправка на стороны четные/нечетные (ширины/высоты), мм
            UPar.to_40007(spcAdd); //высоту сделать длиной
            spcAdd.count = UPar.to_11070_12070_33078_34078(spcAdd); //ставить однократно
            spcAdd.count = UPar.to_39063(spcAdd); //округлять количество до ближайшего

            if (spcRec.id != spcAdd.id) {
                spcRec.spcList.add(spcAdd);
            }

        } catch (Exception e) {
            System.err.println("Ошибка:ElemFrame.addSpecific() " + e);
        }
    }

    @Override
    public void paint() {
        try {
            int rgb = eColor.find(colorID2).getInt(eColor.rgb);

            //ARCH
            if (owner.type() == Type.ARCH) {
                double dh = artiklRec.getDbl(eArtikl.height); //угловое на ус или угловое

                if (Layout.BOTT == layout) {
                    DrawStroke.strokePolygon(winc, x1, x2, x2 - dh, x1 + dh, y1, y2, y1 - dh, y2 - dh, rgb, borderColor);

                } else if (Layout.RIGHT == layout) {
                    double r = ((AreaArch) root()).radiusArch;
                    double ang2 = 90 - UGeo.asin((root().width() - 2 * dh) / ((r - dh) * 2));
                    double a = (r - dh) * UGeo.sin(ang2);
                    DrawStroke.strokePolygon(winc, x1 - dh, x2, x2, x1 - dh, y1 - dh, y1, y2, (r - a), rgb, borderColor);

                } else if (Layout.TOP == layout) { //прорисовка арки               
                    double r = ((AreaArch) root()).radiusArch;
                    double dr = .5; //TODO для прорисовки арки добавил 0.5 градус, а это не айс! 
                    double ang1 = 90 - UGeo.asin(owner.width() / (r * 2));
                    double ang2 = 90 - UGeo.asin((owner.width() - 2 * dh) / ((r - dh) * 2));
                    DrawStroke.strokeArc(winc, owner.width() / 2 - r + dh / 2, dh / 2 - 2, (r - dh / 2) * 2, (r - dh / 2) * 2, ang2, (90 - ang2) * 2 + dr, rgb, dh);
                    DrawStroke.strokeArc(winc, owner.width() / 2 - r, -4, r * 2, r * 2, ang1, (90 - ang1) * 2 + dr, 0, 4);
                    DrawStroke.strokeArc(winc, owner.width() / 2 - r + dh, dh - 2, (r - dh) * 2, (r - dh) * 2, ang2, (90 - ang2) * 2 + dr, 0, 4);

                } else if (Layout.LEFT == layout) {
                    double r = ((AreaArch) root()).radiusArch;
                    double ang2 = 90 - UGeo.asin((root().width() - 2 * dh) / ((r - dh) * 2));
                    double a = (r - dh) * UGeo.sin(ang2);
                    DrawStroke.strokePolygon(winc, x1, x2, x2 + dh, x1 + dh, y1, y2, y2 - dh, (r - a), rgb, borderColor);
                }
            } else {
                IElem5e e1 = winc.listJoin.elem(this, 0);
                IElem5e e2 = winc.listJoin.elem(this, 1);
                double h[] = UGeo.diff(this, this.artiklRec().getDbl(eArtikl.height));
                double h1[] = UGeo.diff(e1, e1.artiklRec().getDbl(eArtikl.height));
                double h2[] = UGeo.diff(e2, e2.artiklRec().getDbl(eArtikl.height));
                double p1[] = UGeo.crossOnLine(x1 + h[0], y1 + h[1], x2 + h[0], y2 + h[1], e1.x1() + h1[0], e1.y1() + h1[1], e1.x2() + h1[0], e1.y2() + h1[1]);
                double p2[] = UGeo.crossOnLine(x1 + h[0], y1 + h[1], x2 + h[0], y2 + h[1], e2.x1() + h2[0], e2.y1() + h2[1], e2.x2() + h2[0], e2.y2() + h2[1]);
                DrawStroke.strokePolygon(winc, x1, x2, p2[0], p1[0], y1, y2, p2[1], p1[1], rgb, borderColor);
                //System.out.println(this.layout + " = " + p1[0] + ":" + p2[1] + " angl = " + this.anglHoriz); //(dh / UCom.sin(this.anglHoriz)));                
            }
        } catch (Exception s) {
            System.err.println("ОШИБКА:model.IElem5e.paint() " + s);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut1=" + anglCut[0] + ", anglCut2=" + anglCut[1];
    }
}
