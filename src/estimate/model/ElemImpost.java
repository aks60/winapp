package estimate.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import estimate.constr.Specification;
import domain.eSyssize;
import enums.ParamJson;
import enums.TypeJoin;
import estimate.constr.Cal5e;
import estimate.constr.Util;
import java.util.List;

public class ElemImpost extends ElemSimple {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм

    public ElemImpost(AreaSimple owner, float id, String param) {

        super(id, owner.iwin(), owner);
        this.layout = (owner.layout() == LayoutArea.HORIZ) ? LayoutArea.VERT : LayoutArea.HORIZ;
        colorID1 = iwin().colorID1;
        colorID2 = iwin().colorID2;
        colorID3 = iwin().colorID3;
        this.type = TypeElem.IMPOST;

        initСonstructiv(param);

        //Коррекция положения импоста арки (подкдадка ареа над импостом)
        if ((TypeElem.ARCH == owner.type || TypeElem.TRAPEZE == owner.type) && owner.listChild.isEmpty()) {
            float dh = artiklRec.getFloat(eArtikl.height) / 2;
            owner.listChild.add(new AreaSimple(iwin(), owner, owner.id() + .1f, TypeElem.AREA, LayoutArea.HORIZ, owner.width(), dh, -1, -1, -1, null));
            root().dy = dh;
        }
        setLocation();
    }

    public void initСonstructiv(String param) {

        if (getParam(param, ParamJson.artikleID) != -1) {
            sysprofRec = eSysprof.find3(getParam(param, ParamJson.artikleID));
        } else {
            if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                sysprofRec = eSysprof.find4(iwin().nuni, UseArtiklTo.IMPOST, UseSide.HORIZ, UseSide.ANY);

            } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                sysprofRec = eSysprof.find4(iwin().nuni, UseArtiklTo.IMPOST, UseSide.VERT, UseSide.ANY);
            }
        }
        specificationRec.place = (LayoutArea.HORIZ == owner().layout()) ? LayoutArea.VERT.name : LayoutArea.HORIZ.name;
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    //Установка координат
    public void setLocation() {
        for (int index = owner().listChild.size() - 1; index >= 0; --index) {
            if (owner().listChild.get(index).type == TypeElem.AREA) {
                Com5t prevArea = owner().listChild.get(index); //index указывает на предыдущий элемент
                float db = artiklRec.getFloat(eArtikl.size_centr);

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

        specificationRec.place = (LayoutArea.HORIZ == owner().layout()) ? "ВСТ.В" : "ВСТ.Г";
        specificationRec.setArtiklRec(artiklRec);
        specificationRec.colorID1 = colorID1;
        specificationRec.colorID2 = colorID2;
        specificationRec.colorID3 = colorID3;
        specificationRec.anglCut2 = 90;
        specificationRec.anglCut1 = 90;
        specificationRec.anglHoriz = anglHoriz;

        //Заход импоста на эскизе не показываю, сразу пишу в спецификацию
        if (iwin().syssizeRec.getInt(eSyssize.id) != -1) {
            float zax = iwin().syssizeRec.getFloat(eSyssize.zax);

            if (LayoutArea.HORIZ == owner().layout()) { //слева направо  
                ElemSimple insideTop = join(LayoutArea.TOP), insideBott = join(LayoutArea.BOTTOM);
                specificationRec.width = insideBott.y1 - insideTop.y2 + zax * 2 + insideBott.artiklRec.getFloat(eArtikl.size_falz) + insideTop.artiklRec.getFloat(eArtikl.size_falz);
                specificationRec.height = artiklRec.getFloat(eArtikl.height);
            } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
                ElemSimple insideLeft = join(LayoutArea.LEFT), insideRight = join(LayoutArea.RIGHT);
                specificationRec.width = insideRight.x1 - insideLeft.x2 + zax * 2 + insideLeft.artiklRec.getFloat(eArtikl.size_falz) + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                specificationRec.height = artiklRec.getFloat(eArtikl.height);
            }
        } else {
            if (LayoutArea.HORIZ == owner().layout()) { //слева направо  
                ElemSimple insideTop = join(LayoutArea.TOP), insideBott = join(LayoutArea.BOTTOM);
                specificationRec.width = insideBott.y2 - insideTop.y1 - 2 * offset(this, join(LayoutArea.TOP));
                specificationRec.height = artiklRec.getFloat(eArtikl.height);
            } else if (LayoutArea.VERT == owner().layout()) { //сверху вниз
                ElemSimple insideLeft = join(LayoutArea.LEFT), insideRight = join(LayoutArea.RIGHT);
                specificationRec.width = insideRight.x1 - insideLeft.x2 - 2 * offset(this, join(LayoutArea.LEFT));
                specificationRec.height = artiklRec.getFloat(eArtikl.height);
            }
        }
    }

    @Override //Вложеная спецификация      
    public void addSpecific(Specification specificationAdd) {

        //Армирование
        if (TypeArtikl.ARMIROVANIE.isType(specificationAdd.artiklRec)) {
            specificationAdd.place = "ВСТ." + layout().name.substring(0, 1);
            specificationAdd.anglCut2 = 90;
            specificationAdd.anglCut1 = 90;
            specificationAdd.width = specificationRec.width;

            //Соединитель
        } else if (TypeArtikl.SOEDINITEL.isType(specificationAdd.artiklRec)) {
            //

            //Остальные
        } else {
            //
        }
        Cal5e.amount(specificationRec, specificationAdd); //количество от параметра
        specificationRec.specificationList.add(specificationAdd);
    }

    //Вычисление захода импоста через параметр
    private float offset(ElemSimple joinElem1, ElemSimple joinElem2) {
        Record joiningRec = eJoining.find(joinElem1.artiklRec, joinElem2.artiklRec);
        List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
        Record joinvarRec = joinvarList.stream().filter(rec -> rec.getInt(eJoinvar.types) == TypeJoin.VAR40.id).findFirst().orElse(null);
        if (joinvarRec != null) {
            List<Record> joinpar1List = eJoinpar1.find(joinvarRec.getInt(eJoinvar.id));
            Record joinpar1Rec = joinpar1List.stream().filter(rec -> rec.getInt(eJoinpar1.grup) == 4040).findFirst().orElse(null);
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

    @Override
    public UseArtiklTo useArtiklTo() {
        return UseArtiklTo.IMPOST;
    }

    @Override
    public String toString() {
        return super.toString() + ", anglCut=" + anglCut1 + ", anglCut=" + anglCut1;
    }
}
