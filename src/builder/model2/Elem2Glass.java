package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eSystree;
import enums.PKjson;

public class Elem2Glass extends Elem2Simple {

    private Record rasclRec = eArtikl.virtualRec(); //раскладка
    private int rasclColor = -3; //цвет раскладки
    private int rasclNumber[] = {2, 2}; //количество проёмов раскладки     
    
    public Elem2Glass(Wingeo wing, GeoElem gson, Com6s owner) {
        super(wing, gson, owner);
    }

    public void initСonstructiv(JsonObject param) {

        if (isJson(param, PKjson.artglasID)) {
            artiklRec = eArtikl.find(param.get(PKjson.artglasID).getAsInt(), false);
        } else {
            Record sysreeRec = eSystree.find(wing.nuni); //по умолчанию стеклопакет
            artiklRec = eArtikl.find2(sysreeRec.getStr(eSystree.glas));
        }
        artiklRecAn = artiklRec;

        //Цвет стекла
        if (isJson(param, PKjson.colorGlass)) {
            colorID1 = param.get(PKjson.colorGlass).getAsInt();
        } else {
            Record artdetRec = eArtdet.find(artiklRec.getInt(eArtikl.id));
            Record colorRec = eColor.find3(artdetRec.getInt(eArtdet.color_fk));
            colorID1 = colorRec.getInt(eColor.id);
            colorID2 = colorRec.getInt(eColor.id);
            colorID3 = colorRec.getInt(eColor.id);
        }

        //Раскладка
        if (isJson(param, PKjson.artiklRascl)) {
            rasclRec = eArtikl.find(param.get(PKjson.artiklRascl).getAsInt(), false);
            //Текстура
            if (isJson(param, PKjson.colorRascl)) {
                rasclColor = eColor.get(param.get(PKjson.colorRascl).getAsInt()).getInt(eColor.id);
            } else {
                rasclColor = eArtdet.find(rasclRec.getInt(eArtikl.id)).getInt(eArtdet.color_fk); //цвет по умолчанию
            }
            //Проёмы гориз.
            if (isJson(param, PKjson.rasclHor)) {
                rasclNumber[0] = param.get(PKjson.rasclHor).getAsInt();
            }
            //Проёмы вертик.
            if (isJson(param, PKjson.rasclVert)) {
                rasclNumber[1] = param.get(PKjson.rasclVert).getAsInt();
            }
        }
    }
    
    public void setLocation() {
        super.setLocation();
    }

    public void paint() {
    }    
}
