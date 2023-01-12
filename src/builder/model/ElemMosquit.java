package builder.model;

import builder.IArea5e;
import builder.IElem5e;
import builder.making.Specific;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eElement;
import enums.Layout;
import enums.PKjson;
import enums.TypeArtikl;
import frames.UGui;
import java.awt.Color;
import java.util.HashSet;

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
        }
//        if (colorID1 == -1) {
//            HashSet<Record> hsColor = UGui.artiklToColorSet(artiklRec.getInt(eArtikl.id));
//            if (hsColor.isEmpty() == false) {
//                colorID1 = hsColor.iterator().next().getInt(eArtikl.id);
//            }
//        }
        if (colorID1 == -1) {
            colorID1 = -3;
        }

        //Состав москитки. ВНИМАЕИЕ! elementID подменён на sysprofRec
        if (isJson(param, PKjson.elementID)) {
            sysprofRec = eElement.find4(param.get(PKjson.elementID).getAsInt());
        } else {
            sysprofRec = eElement.up.newRecord();
        }
    }

    //Установка координат элементов окна
    @Override
    public void setLocation() {
        IElem5e bott = owner().frames().get(Layout.BOTT), right = owner().frames().get(Layout.RIGHT), top = owner().frames().get(Layout.TOP), left = owner().frames().get(Layout.LEFT);
        setDimension(left.x2(), top.y2(), right.x1(), bott.y1());
    }

    //Главная спецификация    
    @Override
    public void setSpecific() {
        try {
            spcRec.place = "ВСТ.м";
            spcRec.setArtikl(artiklRec);
            spcRec.colorID1 = colorID1;
            spcRec.colorID2 = colorID2;
            spcRec.colorID3 = colorID3;
            spcRec.width = width();
            spcRec.height = height();

        } catch (Exception e) {
            System.err.println("Ошибка:ElemMosquit.setSpecific() " + e);
        }
    }

    //Вложенная спецификация
    @Override
    public void addSpecific(Specific spcAdd) {
        try {
            spcAdd.width = UPar.to_12050_15050_34051_39020(spcAdd); //поправка мм 
            
            //Профиль в составе  М/С
            if (TypeArtikl.isType(spcAdd.artiklRec, TypeArtikl.X120)) {
                if(anglHoriz == 0 || anglHoriz == 180) {
                    spcAdd.width += spcAdd.elem5e.owner().width();
                } else if(anglHoriz == 90 || anglHoriz == 270) {
                    spcAdd.width += spcAdd.elem5e.owner().height();
                }
            }
            
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
