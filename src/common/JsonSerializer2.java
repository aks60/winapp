/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

import builder.script.GsonElem;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class JsonSerializer2 implements JsonSerializer<GsonElem> {

    @Override
    public JsonElement serialize(GsonElem gwin, Type typeOfSrc, JsonSerializationContext context) {
        final JsonElement retElem = new GsonBuilder().create().toJsonTree(gwin);
        if (gwin.param().size() == 0) {
            if (retElem.isJsonObject()) {
                retElem.getAsJsonObject().remove("param");
            }
        }
        return retElem;
    }
}
