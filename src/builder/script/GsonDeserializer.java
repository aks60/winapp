package builder.script;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import enums.TypeElem;
import java.lang.reflect.Type;

public class GsonDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive classType = (JsonPrimitive) jsonObject.get("elemType");
        String name = classType.getAsString();
//        if (TypeElem.STVORKA.name().equals(name) || TypeElem.AREA.name().equals(name)) {
//            System.out.println(classType.getAsString());
//            return context.deserialize(jsonObject, AreaElem.class);
//        }        
        return context.deserialize(jsonObject, AreaElem.class);
    }
}
