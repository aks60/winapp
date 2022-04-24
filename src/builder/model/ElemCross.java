package builder.model;

import domain.eArtikl;
import domain.eColor;
import domain.eSysprof;
import enums.Layout;
import enums.UseSide;
import enums.TypeArtikl;
import builder.making.Specific;
import static builder.param.test.ParamTest.param;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import domain.eSyssize;
import common.UCom;
import enums.Form;
import enums.PKjson;
import enums.Type;
import frames.swing.Draw;
import java.util.Arrays;

public class ElemCross extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм

    public ElemCross(AreaSimple owner, GsonElem gson) {
        super(gson.id(), owner.winc, owner, gson);
        this.layout = (owner.layout == Layout.HORIZ) ? Layout.VERT : Layout.HORIZ;

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
            if (Layout.VERT.equals(owner.layout)) { //сверху вниз
                sysprofRec = eSysprof.find4x(winc.nuni, type().id2, UseSide.HORIZ);

            } else if (Layout.HORIZ.equals(owner.layout)) { //слева направо
                sysprofRec = eSysprof.find4x(winc.nuni, type().id2, UseSide.VERT);
            }
        }
        spcRec.place = (Layout.HORIZ == owner.layout) ? Layout.VERT.name : Layout.HORIZ.name;
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    //Установка координат
    public void setLocation() {
        
        //Коррекция положения импоста арки (подкдадка ареа над импостом)
        if (Type.ARCH == owner.typeArea()) {
            AreaSimple prevArea = (AreaSimple) owner.childs.get(0); //опустим ареа на половину шир. иппоста
            prevArea.setDimension(prevArea.x1, prevArea.y1, prevArea.x2, prevArea.y2 + artiklRec.getFloat(eArtikl.height) / 2);
            
        } else if (Type.TRAPEZE == owner.typeArea()) {
            float dy = 0;
            AreaSimple prevArea = (AreaSimple) owner.childs.get(0);
            if(winc.form == Form.RIGHT) {
               float angl = root.frames.get(Layout.RIGHT).anglCut[1];
               dy = (float) (root.frames.get(Layout.RIGHT).artiklRec.getDbl(eArtikl.height) * Math.tan(Math.toRadians((double) (90 - angl))));
            }
            prevArea.setDimension(prevArea.x1, prevArea.y1, prevArea.x2, prevArea.y2 + artiklRec.getFloat(eArtikl.size_centr) + dy);
        } 
        //Установка координат
        for (int index = owner.childs.size() - 1; index >= 0; --index) {
            if (owner.childs.get(index) instanceof AreaSimple) {
                Com5t prevArea = owner.childs.get(index); //index указывает на предыдущий элемент
                float db = artiklRecAn.getFloat(eArtikl.size_centr);

                if (Layout.VERT.equals(owner.layout)) { //сверху вниз
                    setDimension(prevArea.x1, prevArea.y2 - db, prevArea.x2, prevArea.y2 + db);
                    anglHoriz = 0;

                } else if (Layout.HORIZ.equals(owner.layout)) { //слева направо
                    setDimension(prevArea.x2 - db, prevArea.y1, prevArea.x2 + db, prevArea.y2);
                    anglHoriz = 90;
                }
                break;
            }
        }
    }

    @Override //Главная спецификация
    public void setSpecific() {

        spcRec.place = (Layout.HORIZ == owner.layout) ? "ВСТ.в" : "ВСТ.г";
        spcRec.setArtiklRec(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;
        spcRec.anglCut2 = 90;
        spcRec.anglCut1 = 90;
        spcRec.anglHoriz = anglHoriz;

        if (type() == Type.IMPOST) {
            //На эскизе заход импоста не показываю, сразу пишу в спецификацию
            if (winc.syssizeRec.getInt(eSyssize.id) != -1) {
                float zax = winc.syssizeRec.getFloat(eSyssize.zax);

                if (Layout.HORIZ == owner.layout) { //слева направо  
                    ElemSimple insideTop = joinFlat(Layout.TOP), insideBott = joinFlat(Layout.BOTT);
                    spcRec.width = insideBott.y1 - insideTop.y2 + zax * 2 + insideBott.artiklRec.getFloat(eArtikl.size_falz) + insideTop.artiklRec.getFloat(eArtikl.size_falz);
                    spcRec.height = artiklRec.getFloat(eArtikl.height);

                } else if (Layout.VERT == owner.layout) { //снизу вверх
                    ElemSimple insideLeft = joinFlat(Layout.LEFT), insideRight = joinFlat(Layout.RIGHT);
                    spcRec.width = insideRight.x1 - insideLeft.x2 + zax * 2 + insideLeft.artiklRec.getFloat(eArtikl.size_falz) + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                    spcRec.height = artiklRec.getFloat(eArtikl.height);
                }
            } else {
                if (Layout.HORIZ == owner.layout) { //слева направо  
                    spcRec.width = y2 - y1;
                    spcRec.height = artiklRec.getFloat(eArtikl.height);

                } else if (Layout.VERT == owner.layout) { //снизу вверх
                    spcRec.width = x2 - x1;
                    spcRec.height = artiklRec.getFloat(eArtikl.height);
                }
            }
        } else if (type() == Type.SHTULP) {
            if (Layout.HORIZ == owner.layout) { //слева направо  
                spcRec.width = y2 - y1;
                spcRec.height = artiklRec.getFloat(eArtikl.height);

            } else if (Layout.VERT == owner.layout) { //сверху вниз
                spcRec.width = x2 - x1;
                spcRec.height = artiklRec.getFloat(eArtikl.height);
            }
        } else if (type() == Type.STOIKA) {
            if (Layout.HORIZ == owner.layout) { //слева направо  
                spcRec.width = y2 - y1;
                spcRec.height = artiklRec.getFloat(eArtikl.height);

            } else if (Layout.VERT == owner.layout) { //сверху вниз
                spcRec.width = x2 - x1;
                spcRec.height = artiklRec.getFloat(eArtikl.height);
            }
        }
    }

    //@Override //Вложеная спецификация 
    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов

        spcAdd.count = UMod.get_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += UMod.get_14050_24050_33050_38050(spcRec, spcAdd); //кол. ед. с шагом
        spcAdd.width = UMod.get_12050_15050_34051_39020(spcRec, spcAdd); //поправка мм 

        //Армирование
        if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X107)) {
            spcAdd.place = "ВСТ." + layout.name.substring(0, 1).toLowerCase();
            spcAdd.anglCut1 = 90;
            spcAdd.anglCut2 = 90;
        }
        if (Arrays.asList(1, 3, 5).contains(spcAdd.artiklRec.getInt(eArtikl.level1))) {
            spcAdd.width += spcRec.width;
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
    }
    
    @Override
    public void paint() {

        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        if (Layout.VERT == owner.layout) {
            Draw.strokePolygon(winc, x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (Layout.HORIZ == owner.layout) {
            Draw.strokePolygon(winc, x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut1=" + anglCut[0] + ", anglCut2=" + anglCut[1];
    }
}
