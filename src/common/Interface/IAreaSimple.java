package common.Interface;

import com.google.gson.JsonObject;
import enums.Type;

public interface IAreaSimple {

    //Форма контура
    public Type type();

    //Соединения ареа
    public void joining();

    //Рисуем конструкцию
    public void draw();
}
