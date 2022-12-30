package builder.model;

import builder.IArea5e;
import builder.ICom5t;
import builder.IElem5e;
import builder.IStvorka;
import builder.making.Specific;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import common.LinkedList2;
import common.eProp;
import dataset.Record;
import domain.eArtikl;
import domain.eElement;
import enums.Layout;
import enums.PKjson;
import enums.Type;
import java.util.ArrayList;
import java.util.List;

public class ElemMosquit extends ElemSimple {

    public static IElem5e create(IStvorka stv) {
        IArea5e stvArea = (IArea5e) stv;
        JsonObject param = new JsonObject();
        param.addProperty(PKjson.artiklMosq, stv.mosqRec().getInt(eArtikl.id));
        param.addProperty(PKjson.elementID, stv.elementRec().getInt(eElement.id));
        GsonElem gson = new GsonElem(enums.Type.MOSKITKA, param.toString());
        IElem5e elem5e = (eProp.old.read().equals("0"))
                ? new builder.model.ElemMosquit(stvArea, gson)
                : new builder.model.ElemMosquit(stvArea, gson);
        ((IArea5e) stv).childs().add(elem5e);
        return elem5e;
    }

    public ElemMosquit(IArea5e owner, GsonElem gson) {
        super(gson.id(), owner.winc(), owner, gson);
        this.layout = Layout.FULL;

        initСonstructiv(gson.param());
        setLocation();
    }

    public void initСonstructiv(JsonObject param) {

        //Артикл
        if (isJson(param, PKjson.artiklID)) {
            artiklRec(eArtikl.find(param.get(PKjson.artiklID).getAsInt(), false));
        } else {
            artiklRec(eArtikl.virtualRec());
        }
        artiklRecAn(artiklRec);

        //Цвет
        if (isJson(param, PKjson.colorID1)) {
            colorID1 = param.get(PKjson.colorID1).getAsInt();
        } else {
            colorID1 = -3;
        }

        //Состав москитки
        if (isJson(param, PKjson.elementID)) {
            sysprofRec = eElement.find4(param.get(PKjson.elementID).getAsInt());
        } else {
            sysprofRec = eElement.up.newRecord();
        }
    }

    //Установка координат элементов окна
    @Override
    public void setLocation() {
        if (Type.ARCH == owner.type()) {
            setDimension(0, 0, owner.x2(), Math.abs(winc.height1() - winc.height2()));
        } else {
            setDimension(owner.x1(), owner.y1(), owner.x2(), owner.y2());
        }
    }

    //Главная спецификация    
    @Override
    public void setSpecific() {
    }

    //Вложенная спецификация
    @Override
    public void addSpecific(Specific spcAdd) {
        try {
            spcRec.spcList.add(spcAdd);

        } catch (Exception e) {
            System.err.println("Ошибка:ElemMosquit.addSpecific() " + e);
        }
    }

    @Override
    public void paint() { //рисуём стёкла
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
