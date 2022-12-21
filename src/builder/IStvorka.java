package builder;

import com.google.gson.JsonObject;
import dataset.Record;
import enums.LayoutHandle;
import enums.TypeOpen1;

public interface IStvorka {

    //Цвет ручки
    int handleColor();

    //Цвет ручки
    void handleColor(int handleColor);

    //Высота ручки
    float handleHeight();

    //Высота ручки
    void handleHeight(float handleHeight);

    //Положение ручки на створке
    LayoutHandle handleLayout();

    //Положение ручки на створке
    void handleLayout(LayoutHandle handleLayout);

    //Ручка
    Record handleRec();

    //Ручка
    void handleRec(Record handleRec);

    void initFurniture(JsonObject param);

    //Угловые и прилегающие соединения
    void joining();

    //Цвет замка
    int lockColor();

    //Цвет замка
    void lockColor(int lockColor);

    //Замок
    Record lockRec();

    //Замок
    void lockRec(Record lockRec);

    //Цвет подвеса
    int loopColor();

    //Цвет подвеса
    void loopColor(int loopColor);

    //Подвес(петли)
    Record loopRec();

    //Подвес(петли)
    void loopRec(Record loopRec);

    float[] offset();

    //Прорисовка створки
    void paint();

    boolean[] paramCheck();

    void paramCheck(boolean[] paramCheck);

    //Фурнитура
    Record sysfurnRec();

    //Направление открывания
    TypeOpen1 typeOpen();

    //Направление открывания
    void typeOpen(TypeOpen1 typeOpen);

    //Москитная сетка
    Record mosqRec();

    //Сосав москитки 
    Record elementRec();
}
