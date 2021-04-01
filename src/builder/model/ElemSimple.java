package builder.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import enums.UseUnit;
import builder.specif.Specification;
import enums.UseArtiklTo;
import java.awt.Color;
import java.util.HashMap;
import builder.Wincalc;

public abstract class ElemSimple extends Com5t {

    public float anglCut1 = 45; //Угол реза рамы
    public float anglCut2 = 45; //Угол реза рамы
    public Specification spcRec = null; //спецификация элемента
    public float anglHoriz = -1; //угол к горизонту
    public Color borderColor = Color.BLACK;
    public HashMap<String, String> mapFieldVal = new HashMap(); //свойства элемента <имя поля => значение>

    public ElemSimple(float id, Wincalc iwin, AreaSimple owner) {
        super(id, iwin, owner);
        spcRec = new Specification(id, this);
    }

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y) {
        int x = (int) (X / iwin().scale) - Com5t.TRANSLATE_XY;
        int y = (int) (Y / iwin().scale) - Com5t.TRANSLATE_XY;
        return inside(x, y);
    }

    //Использовать артикл для "коробка", "створка", "импост", "стойка", "поперечина", "штульп", "раскладка", "эркер"
    public abstract UseArtiklTo useArtiklTo();

    //Главная спецификация
    public abstract void setSpecific();

    //Вложеная спецификация
    public abstract void addSpecific(Specification specification);

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz + ", length" + length();
    }
}
