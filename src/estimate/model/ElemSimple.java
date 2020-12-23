package estimate.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import enums.ParamJson;
import enums.UseUnit;
import estimate.constr.Specification;
import enums.UseArtiklTo;
import java.awt.Color;
import java.util.HashMap;
import estimate.Wincalc;

public abstract class ElemSimple extends Com5t {

    public float anglCut1 = 45; //Угол реза рамы
    public float anglCut2 = 45; //Угол реза рамы
    public Specification specificationRec = null; //спецификация элемента
    public float anglHoriz = -1; //угол к горизонту
    protected Color borderColor = Color.BLACK;
    public HashMap<String, String> mapFieldVal = new HashMap(); //свойства элемента <имя поля => значение>

    public ElemSimple(float id, Wincalc iwin, AreaSimple owner) {
        super(id, iwin, owner);
        specificationRec = new Specification(id, this);
    }

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y) {

        iwin().listElem.stream().forEach(el -> el.borderColor = java.awt.Color.BLACK);
        int x = (int) (X / iwin().scale1) - Com5t.TRANSLATE_X;
        int y = (int) (Y / iwin().scale1) - Com5t.TRANSLATE_Y;
        borderColor = (inside(x, y) == true) ? Color.RED : Color.BLACK;
        return inside(x, y);
    }

    //Использовать артикл для...
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
