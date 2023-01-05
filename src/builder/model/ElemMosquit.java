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
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ElemMosquit extends ElemSimple {

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

        //Состав москитки. ВНИМАЕИЕ! sysprofRec подменён на elementID
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
    public void paint() {          
            if (this.artiklRec.isVirtual() == false) {
                winc.gc2d.setColor(Color.getHSBColor(242, 242, 242));
                IElem5e bott = owner().frames().get(Layout.BOTT), right = owner().frames().get(Layout.RIGHT), top = owner().frames().get(Layout.TOP), left = owner().frames().get(Layout.LEFT);
                int z = (winc.scale < 0.1) ? 80 : 30;
                int h = 0, w = 0;

                for (int i = 1; i < (bott.y1() - top.y2()) / z; i++) {
                    h = h + z;
                    winc.gc2d.drawLine((int) left.x2(), (int) (top.y2() + h), (int) right.x1(), (int) (top.y2() + h));
                }
                for (int i = 1; i < (right.x1() - left.x2()) / z; i++) {
                    w = w + z;
                    winc.gc2d.drawLine((int) (left.x2() + w), (int) top.y2(), (int) (left.x2() + w), (int) bott.y1());
                }
            }        
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
