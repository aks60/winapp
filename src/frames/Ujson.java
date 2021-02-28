package frames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eColor;
import domain.eSysprod;
import enums.PKjson;
import frames.dialog.DicColor2;
import frames.swing.DefMutableTreeNode;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import common.RecordListener;

public class Ujson {
  
    private Gson gson = new GsonBuilder().create();
    
    Ujson() {
        
    }

    public static JsonObject getAsJsonObject(JsonObject obj, String key) {

        if (obj.getAsJsonObject(key) == null) {
            obj.add(key, new JsonObject());
        }
        return obj.getAsJsonObject(key);
    }    
}
