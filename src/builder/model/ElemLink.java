package builder.model;

import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinvar;
import domain.eSysprof;
import enums.LayoutArea;
import enums.UseSide;
import enums.TypeArtikl;
import enums.TypeElem;
import enums.UseArtiklTo;
import builder.making.Specific;
import domain.eSyssize;
import enums.TypeJoin;
import common.Util;
import enums.PKjson;
import java.util.List;

public class ElemLink extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм
    
    public ElemLink(AreaSimple owner, TypeElem type, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = (owner.layout() == LayoutArea.HORIZ) ? LayoutArea.VERT : LayoutArea.HORIZ;
        colorID1 = iwin().colorID1;
        colorID2 = iwin().colorID2;
        colorID3 = iwin().colorID3;
        this.type = type;

        initСonstructiv(param);

        //Коррекция положения импоста арки (подкдадка ареа над импостом)
        if ((TypeElem.ARCH == owner.type || TypeElem.TRAPEZE == owner.type) && owner.listChild.isEmpty()) {
            float dh = artiklRec.getFloat(eArtikl.height) / 2;
            owner.listChild.add(new AreaSimple(iwin(), owner, owner.id() + .1f, TypeElem.AREA, LayoutArea.HORIZ, owner.width(), dh, -1, -1, -1, null));
        }
        setLocation();
    }

    public void initСonstructiv(String param) {

        if (param(param, PKjson.sysprofID) != -1) {
            sysprofRec = eSysprof.find3(param(param, PKjson.sysprofID));
        } else {
            if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.HORIZ, UseSide.ANY);

            } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                sysprofRec = eSysprof.find4(iwin().nuni, type.id2, UseSide.VERT, UseSide.ANY);
            }
        }
        spcRec.place = (LayoutArea.HORIZ == owner().layout()) ? LayoutArea.VERT.name : LayoutArea.HORIZ.name;
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    //Установка координат
    public void setLocation() {
        for (int index = owner().listChild.size() - 1; index >= 0; --index) {
            if (owner().listChild.get(index).type == TypeElem.AREA) {
                Com5t prevArea = owner().listChild.get(index); //index указывает на предыдущий элемент
                float db = artiklRecAn.getFloat(eArtikl.size_centr);

                if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                    setDimension(prevArea.x1, prevArea.y2 - db, prevArea.x2, prevArea.y2 + db);
                    anglHoriz = 0;

                } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                    setDimension(prevArea.x2 - db, prevArea.y1, prevArea.x2 + db, prevArea.y2);
                    anglHoriz = 90;
                }
                break;
            }
        }
    }

    @Override //Главная спецификация
    public void setSpecific() {

        spcRec.place = (LayoutArea.HORIZ == owner().layout()) ? "ВСТ.В" : "ВСТ.Г";
        spcRec.setArtiklRec(artiklRec);
        spcRec.colorID1 = colorID1;
        spcRec.colorID2 = colorID2;
        spcRec.colorID3 = colorID3;
        spcRec.anglCut2 = 90;
        spcRec.anglCut1 = 90;
        spcRec.anglHoriz = anglHoriz;

        //На эскизе заход импоста не показываю, сразу пишу в спецификацию
        if (iwin().syssizeRec.getInt(eSyssize.id) != -1) {
            float zax = iwin().syssizeRec.getFloat(eSyssize.zax);

            if (LayoutArea.HORIZ == owner().layout()) { //слева направо  
                ElemSimple insideTop = join(LayoutArea.TOP), insideBott = join(LayoutArea.BOTTOM);
                spcRec.width = insideBott.y1 - insideTop.y2 + zax * 2 + insideBott.artiklRec.getFloat(eArtikl.size_falz) + insideTop.artiklRec.getFloat(eArtikl.size_falz);
                spcRec.height = artiklRec.getFloat(eArtikl.height);

            } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
                ElemSimple insideLeft = join(LayoutArea.LEFT), insideRight = join(LayoutArea.RIGHT);
                spcRec.width = insideRight.x1 - insideLeft.x2 + zax * 2 + insideLeft.artiklRec.getFloat(eArtikl.size_falz) + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                spcRec.height = artiklRec.getFloat(eArtikl.height);
            }
        } else {
            if (LayoutArea.HORIZ == owner().layout()) { //слева направо  
                spcRec.width = y2 - y1;
                spcRec.height = artiklRec.getFloat(eArtikl.height);

            } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
                spcRec.width = x2 - x1;
                spcRec.height = artiklRec.getFloat(eArtikl.height);
            }
        }   
    }

    //@Override //Вложеная спецификация 
    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов

        spcAdd.count = spc7d.calcCount(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += spc7d.calcCountStep(this, spcAdd); //кол. ед. с шагом
        spcAdd.quant1 += spc7d.calcKitCountStep(this, spcAdd); //кол. с шагом
        spcAdd.width = spc7d.calcAmountMetr(spcRec, spcAdd); //поправка мм
        spcAdd.quant1 = spc7d.calcAmount(spcRec, spcAdd); //количество от параметра 
        spc7d.setAngl(spcAdd); //задать Угол_реза_1/Угол_реза_2

        //Армирование
        if (TypeArtikl.X107.isType(spcAdd.artiklRec)) {
            spcAdd.place = "ВСТ." + layout().name.substring(0, 1);
            spcAdd.anglCut1 = 90;
            spcAdd.anglCut2 = 90;
        }

        if (spcAdd.artiklRec.getInt(eArtikl.level1) == 1
                || spcAdd.artiklRec.getInt(eArtikl.level1) == 3
                || spcAdd.artiklRec.getInt(eArtikl.level1) == 5) {
            
            spcAdd.width += spcRec.width;
            spcAdd.width = spc7d.calcAmountLenght(spcRec, spcAdd); //длина мм
            spcAdd.width = spcAdd.width * spc7d.calcCoeff(spcRec, spcAdd);//"[ * коэф-т ]"            
        }

        spcRec.spcList.add(spcAdd);
    }

    //Вычисление захода импоста через параметр
    private float offset(ElemSimple joinElem1, ElemSimple joinElem2) {
        Record joiningRec = eJoining.find(joinElem1.artiklRec, joinElem2.artiklRec);
        List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
        Record joinvarRec = joinvarList.stream().filter(rec -> rec.getInt(eJoinvar.types) == TypeJoin.VAR40.id).findFirst().orElse(null);
        if (joinvarRec != null) {
            List<Record> joinpar1List = eJoinpar1.find(joinvarRec.getInt(eJoinvar.id));
            Record joinpar1Rec = joinpar1List.stream().filter(rec -> rec.getInt(eJoinpar1.params_id) == 4040).findFirst().orElse(null);
            if (joinpar1Rec != null) {
                return Util.getFloat(joinpar1Rec.getStr(eJoinpar1.text));
            }
        }
        return 0;
    }

    @Override
    public void paint() {

        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        if (LayoutArea.VERT == owner().layout()) {
            iwin().draw.strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);

        } else if (LayoutArea.HORIZ == owner().layout()) {
            iwin().draw.strokePolygon(x1, x2, x2, x1, y1, y1, y2, y2, rgb, borderColor);
        }
    }

//    @Override
//    public UseArtiklTo useArtiklTo() {
//        return UseArtiklTo.IMPOST;
//    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut=" + anglCut1 + ", anglCut=" + anglCut1;
    }
}
