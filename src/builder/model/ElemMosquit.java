package builder.model;

import builder.IArea5e;
import builder.making.Specific;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import domain.eArtikl;
import enums.Layout;
import enums.PKjson;

public class ElemMosquit extends ElemSimple {
    
    private int elementID = -3; //состав москитки
    
    public static ElemMosquit create(IArea5e owner) {
        GsonElem gsonElem = new GsonElem();
        
        return null;
    }
    
    public ElemMosquit(IArea5e owner, GsonElem gson) {
        super(GsonElem.genId(), owner.winc(), owner, gson);
        this.layout = Layout.FULL;
        
        initСonstructiv(gson.param());
    }

    public void initСonstructiv(JsonObject param) {

        if (isJson(param, PKjson.artiklID)) {
            artiklRec(eArtikl.find(param.get(PKjson.artiklID).getAsInt(), false));
        } else {
            artiklRec(eArtikl.virtualRec());
        }
        artiklRecAn(artiklRec);

        //Цвет стекла
        if (isJson(param, PKjson.colorID1)) {
            colorID1 = param.get(PKjson.colorID1).getAsInt();
        } else {
            colorID1 = -3;
        }
    }

    //Установка координат элементов окна
    @Override
    public void setLocation() {
    }

    //Главная спецификация    
    @Override
    public void setSpecific() {
    }

    //Вложенная спецификация
    @Override
    public void addSpecific(Specific spcAdd) {
    }

    @Override
    public void paint() { //рисуём стёкла
    }

    @Override
    public String toString() {
        return super.toString() + ", москитка";
    }
}
