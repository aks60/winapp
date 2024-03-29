package builder.model1;

import builder.IArea5e;
import builder.IElem5e;
import builder.ICom5t;
import builder.model2.UGeo;
import domain.eArtikl;
import domain.eColor;
import domain.eSysprof;
import enums.Layout;
import enums.UseSide;
import enums.TypeArtikl;
import builder.making.Specific;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import domain.eSyssize;
import common.UCom;
import common.eProp;
import enums.Form;
import enums.PKjson;
import enums.Type;
import frames.swing.DrawStroke;
import java.util.List;

public class ElemCross extends ElemSimple {

    protected double truncation = 0; //усечение параметр Артикула1/Артикула2, мм

    public ElemCross(IArea5e owner, GsonElem gson) {
        super(gson.id(), owner.winc(), owner, gson);
        this.layout = (owner.layout() == Layout.HORIZ) ? Layout.VERT : Layout.HORIZ;

        initСonstructiv(gson.param());
        setLocation();
    }

    public void initСonstructiv(JsonObject param) {

        colorID1 = (isJson(param, PKjson.colorID1)) ? param.get(PKjson.colorID1).getAsInt() : winc.colorID1;
        colorID2 = (isJson(param, PKjson.colorID2)) ? param.get(PKjson.colorID2).getAsInt() : winc.colorID2;
        colorID3 = (isJson(param, PKjson.colorID3)) ? param.get(PKjson.colorID3).getAsInt() : winc.colorID3;

        if (isJson(param, PKjson.sysprofID)) { //профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());
        } else {
            if (Layout.VERT.equals(owner.layout())) { //сверху вниз
                sysprofRec = eSysprof.find4(winc.nuni(), type().id2, UseSide.HORIZ);

            } else if (Layout.HORIZ.equals(owner.layout())) { //слева направо
                sysprofRec = eSysprof.find4(winc.nuni(), type().id2, UseSide.VERT);
            }
        }
        spcRec.place = (Layout.HORIZ == owner.layout()) ? Layout.VERT.name : Layout.HORIZ.name;
        artiklRec(eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false));
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    /**
     * Установка координат поперечин с учётов типа конст. и формы контура x1y1 -
     * верхняя левая точка x2y2 - нижняя правая точка
     */
    public void setLocation() {

        //Коррекция положения импоста (подкдадка ареа над импостом)
        if (Type.ARCH == owner.type()) {
            IArea5e prevArea = (IArea5e) owner.childs().get(0); //опустим ареа на половину шир. иппоста
            prevArea.setDimension(prevArea.x1(), prevArea.y1(), prevArea.x2(), prevArea.y2() + artiklRec().getDbl(eArtikl.height) / 2);

        } else if (Type.TRAPEZE == owner.type()) {
            double dy = 0;           
            IArea5e prevArea = (IArea5e) owner.childs().get(0);

            if (eProp.dev == true && winc.rootGson.project() != null && (winc.rootGson.project() == 605001 || winc.rootGson.project() == -605001)) { //тест
                prevArea.setDimension(prevArea.x1(), prevArea.y1(), prevArea.x2(), 400);
                
            } else {
                if (winc.form == Form.RIGHT) {
                    double angl = root.frames().get(Layout.RIGHT).anglCut(1);
                    dy = (root.frames().get(Layout.RIGHT).artiklRec().getDbl(eArtikl.height) * UGeo.tan(90 - angl));
                    prevArea.setDimension(prevArea.x1(), prevArea.y1(), prevArea.x2(), prevArea.y2() + artiklRec().getDbl(eArtikl.size_centr) + dy);
                } else if (winc.form == Form.LEFT) {
                    double angl = root.frames().get(Layout.LEFT).anglCut(0);
                    dy = (root.frames().get(Layout.LEFT).artiklRec().getDbl(eArtikl.height) * Math.tan(Math.toRadians(90 - angl)));
                    prevArea.setDimension(prevArea.x1(), prevArea.y1(), prevArea.x2(), prevArea.y2() + artiklRec().getDbl(eArtikl.size_centr) + dy);
                } else if (winc.form == Form.SYMM && root.frames().get(Layout.TOPL) == null && root.frames().get(Layout.TOPR) == null) {
                    double angl = root.frames().get(Layout.LEFT).anglCut(0);
                    dy = (root.frames().get(Layout.LEFT).artiklRec().getDbl(eArtikl.height) * UGeo.tan(90 - angl));
                    prevArea.setDimension(prevArea.x1(), prevArea.y1(), prevArea.x2(), prevArea.y2() + artiklRec().getDbl(eArtikl.size_centr) + dy);
                } else if (winc.form == Form.SYMM&& root.frames().get(Layout.TOPL) != null && root.frames().get(Layout.TOPR) != null) {
                    for (int i = 0; i < winc.listElem.filter(Type.AREA).size(); i++) {
                         if(i == 0) {
                             IElem5e area = winc.listElem.get(i);
                         }
                    }
                }
                    
                    
//                    IArea5e oneArea = (IArea5e) owner.childs().get(0), twoArea = (IArea5e) owner.childs().get(1), threeArea = (IArea5e) owner.childs().get(2);
//                    oneArea.setDimension(prevArea.x2() + dy, oneArea.y2(), oneArea.x2() + dy, oneArea.y1());  

//                      if (prevArea.x1() == 0) {
//                        double angl = root.frames().get(Layout.TOP).anglCut(0);
//                        dy = (root.frames().get(Layout.TOP).artiklRec().getDbl(eArtikl.height) * Math.tan(Math.toRadians(angl)));
//                        prevArea.setDimension(prevArea.x2() + dy, prevArea.y2(), prevArea.x2() + dy, prevArea.y1());
//                    } else {
//                        IArea5e lastArea = (IArea5e) owner.childs().get(owner.childs().size() - 1);
//                        double angl = root.frames().get(Layout.TOP).anglCut(0);
//                        dy = (root.frames().get(Layout.TOP).artiklRec().getDbl(eArtikl.height) * Math.tan(Math.toRadians(angl)));
//                        setDimension(lastArea.x1() - dy, lastArea.y2(), lastArea.x1() - dy, lastArea.y1());
//                    }
//                }
            }
        }
        //Установка координат
        for (int index = owner.childs().size() - 1; index >= 0; --index) {
            if (owner.childs().get(index) instanceof IArea5e) {
                ICom5t prevArea = owner.childs().get(index); //index указывает на предыдущий элемент

                if (Layout.VERT.equals(owner.layout())) { //ареа сверху вниз
                    setDimension(prevArea.x1(), prevArea.y2(), prevArea.x2(), prevArea.y2());

                } else if (Layout.HORIZ.equals(owner.layout())) { //ареа слева направо
                    setDimension(prevArea.x2(), prevArea.y2(), prevArea.x2(), prevArea.y1());
                }
                break;
            }
        }
    }

    @Override
    //Главная спецификация   
    public void setSpecific() {
        try {
            spcRec.place = (Layout.HORIZ == owner.layout()) ? "ВСТ.в" : "ВСТ.г";
            spcRec.setArtikl(artiklRec);
            spcRec.colorID1 = colorID1;
            spcRec.colorID2 = colorID2;
            spcRec.colorID3 = colorID3;
            spcRec.anglCut1 = 90;
            spcRec.anglCut0 = 90;
            spcRec.anglHoriz = anglHoriz;

            if (type() == Type.IMPOST) {
                //На эскизе заход импоста не показываю, сразу пишу в спецификацию
                if (winc.syssizeRec().getInt(eSyssize.id) != -1) {
                    double zax = winc.syssizeRec().getDbl(eSyssize.zax);
                    if (Layout.HORIZ == owner.layout()) { //ареа слева направо  
                        IElem5e inTop = joinFlat(Layout.TOP), inBott = joinFlat(Layout.BOTT);
                        spcRec.width = (inBott.y1() - inBott.artiklRec().getDbl(eArtikl.height) + inBott.artiklRec().getDbl(eArtikl.size_centr))
                                - (inTop.y2() + inTop.artiklRec().getDbl(eArtikl.height) - inTop.artiklRec().getDbl(eArtikl.size_centr))
                                + zax * 2 + inBott.artiklRec().getDbl(eArtikl.size_falz) + inTop.artiklRec().getDbl(eArtikl.size_falz);
                        spcRec.height = artiklRec().getDbl(eArtikl.height);

                    } else if (Layout.VERT == owner.layout()) { //ареа сверху вниз
                        IElem5e inLeft = joinFlat(Layout.LEFT), inRight = joinFlat(Layout.RIGHT);
                        spcRec.width = (inRight.x1() - inRight.artiklRec().getDbl(eArtikl.height) + inRight.artiklRec().getDbl(eArtikl.size_centr))
                                - (inLeft.x1() + inLeft.artiklRec().getDbl(eArtikl.height) - inLeft.artiklRec().getDbl(eArtikl.size_centr))
                                + zax * 2 + inLeft.artiklRec().getDbl(eArtikl.size_falz) + inRight.artiklRec().getDbl(eArtikl.size_falz);
                        spcRec.height = artiklRec().getDbl(eArtikl.height);
                    }
                } else {
                    if (Layout.HORIZ == owner.layout()) { //слева направо  
                        spcRec.width = length();
                        spcRec.height = artiklRec().getDbl(eArtikl.height);

                    } else if (Layout.VERT == owner.layout()) { //снизу вверх
                        spcRec.width = length();
                        spcRec.height = artiklRec().getDbl(eArtikl.height);
                    }
                }
            } else if (type() == Type.SHTULP) {
                if (Layout.HORIZ == owner.layout()) { //слева направо  
                    spcRec.width = y2 - y1;
                    spcRec.height = artiklRec().getDbl(eArtikl.height);

                } else if (Layout.VERT == owner.layout()) { //сверху вниз
                    spcRec.width = x2 - x1;
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                }
            } else if (type() == Type.STOIKA) {
                if (Layout.HORIZ == owner.layout()) { //слева направо  
                    spcRec.width = y2 - y1;
                    spcRec.height = artiklRec().getDbl(eArtikl.height);

                } else if (Layout.VERT == owner.layout()) { //сверху вниз
                    spcRec.width = x2 - x1;
                    spcRec.height = artiklRec().getDbl(eArtikl.height);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:ElemCross.setSpecific() " + e);
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
            }
            if (List.of(1, 3, 5).contains(spcAdd.artiklRec.getInt(eArtikl.level1)) && spcRec.id != spcAdd.id) {
                spcAdd.width += spcRec.width;
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
            System.err.println("Ошибка:ElemCross.addSpecific() " + e);
        }
    }

    @Override
    public void paint() {

        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        double dh = this.artiklRec.getDbl(eArtikl.size_centr);
        if (Layout.VERT == owner.layout()) {
            DrawStroke.strokePolygon(winc, x1, x2, x2, x1, y1 + dh, y1 + dh, y2 - dh, y2 - dh, rgb, borderColor);

        } else if (Layout.HORIZ == owner.layout()) {
            DrawStroke.strokePolygon(winc, x1 - dh, x2 + dh, x2 + dh, x1 - dh, y1, y1, y2, y2, rgb, borderColor);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut1=" + anglCut[0] + ", anglCut2=" + anglCut[1];
    }
}
