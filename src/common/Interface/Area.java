package common.Interface;

import com.google.gson.JsonObject;
import enums.Type;

public interface Area {

    //Форма контура
    public Type type();

    ////T - соединения
    public void joining();

    //Рисуем конструкцию
    public void draw();
}
