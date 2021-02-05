package frames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.DialogListener;
import dataset.Record;
import domain.eColor;
import domain.eSysprod;
import enums.PKjson;
import frames.dialog.DicColor2;
import frames.swing.DefMutableTreeNode;
import java.awt.event.ActionEvent;
import java.util.HashSet;

public class UtilJson {
  
    private Gson gson = new GsonBuilder().create();
    
    UtilJson() {
        
    }
    
    private void btn09Action(ActionEvent evt) {                             
/*        HashSet<Record> set = new HashSet();
        String[] arr1 = (txt15.getText().isEmpty() == false) ? txt15.getText().split(";") : null;
        String jfield = (evt.getSource() == btn09) ? txt03.getText() : (evt.getSource() == btn13) ? txt04.getText() : txt05.getText();
        Integer[] arr2 = builder.specif.Util.parserInt(jfield);
        if (arr1 != null) {
            for (String s1 : arr1) { //группы
                HashSet<Record> se2 = new HashSet();
                boolean b = false;
                for (Record rec : eColor.query()) {

                    if (rec.getStr(eColor.colgrp_id).equals(s1)) {
                        se2.add(rec); //текстуры группы

                        for (int i = 0; i < arr2.length; i = i + 2) { //тестуры
                            if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                b = true;
                            }
                        }
                    }
                }
                if (b == false) { //если небыло пападаний то добавляем всю группу
                    set.addAll(se2);
                }
            }
        }
        if (arr2.length != 0) {
            for (Record rec : eColor.query()) {
                if (arr1 != null) {

                    for (String s1 : arr1) { //группы
                        if (rec.getStr(eColor.colgrp_id).equals(s1)) {
                            for (int i = 0; i < arr2.length; i = i + 2) { //текстуры
                                if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                                    set.add(rec);
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < arr2.length; i = i + 2) { //тестуры
                        if (rec.getInt(eColor.id) >= arr2[i] && rec.getInt(eColor.id) <= arr2[i + 1]) {
                            set.add(rec);
                        }
                    }
                }
            }
        }

        DialogListener listenerColor = (colorRec) -> {

            float id = ((DefMutableTreeNode) treeWin.getLastSelectedPathComponent()).com5t().id();
            builder.script.JsonElem rootArea = iwin.jsonRoot.find(id);
            if (rootArea != null) {
                String paramStr = (rootArea.param().isEmpty()) ? "{}" : rootArea.param();
                Gson gson = new GsonBuilder().create();
                JsonObject jsonObject = gson.fromJson(paramStr, JsonObject.class);

                if (evt.getSource() == btn09) {
                    jsonObject.addProperty(PKjson.colorID1, colorRec.getStr(eColor.id));
                } else if (evt.getSource() == btn13) {
                    jsonObject.addProperty(PKjson.colorID2, colorRec.getStr(eColor.id));
                } else if (evt.getSource() == btn02) {
                    jsonObject.addProperty(PKjson.colorID3, colorRec.getStr(eColor.id));
                }
                paramStr = gson.toJson(jsonObject);
                rootArea.param(paramStr);
                String script = gson.toJson(iwin.jsonRoot);
                Record sysprodRec = qSysprod.get(Util.getSelectedRec(tab5));
                sysprodRec.set(eSysprod.script, script);
                qSysprod.update(sysprodRec);
                selectionTab5();
            }
        };
        if (arr1 == null && arr2.length == 0) {
            new DicColor2(this, listenerColor);
        } else {
            new DicColor2(this, listenerColor, set);
        }*/
    }                            
    
    public static JsonObject getAsJsonObject(JsonObject obj, String key) {

        if (obj.getAsJsonObject(key) == null) {
            obj.add(key, new JsonObject());
        }
        return obj.getAsJsonObject(key);
    }    
}
