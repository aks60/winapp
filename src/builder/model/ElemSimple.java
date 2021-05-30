package builder.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import enums.UseUnit;
import builder.making.Specific;
import enums.UseArtiklTo;
import java.awt.Color;
import java.util.HashMap;
import builder.Wincalc;
import builder.making.Uti3;

public abstract class ElemSimple extends Com5t {

    public float anglCut[] = {45, 45}; //угол реза рамы
    public float anglCut1 = 45; //угол реза рамы
    public float anglCut2 = 45; //угол реза рамы
    public float anglPlane[] = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    public float anglHoriz = -1; //угол к горизонту
    public ElemJoining joinElem[] = {null, null, null, null}; //соединения 

    public Specific spcRec = null; //спецификация элемента
    protected Uti3 uti3 = null;
    public Color borderColor = Color.BLACK;
    //public HashMap<String, String> mapFieldVal = new HashMap(); //свойства элемента <имя поля => значение>

    public ElemSimple(float id, Wincalc iwin, AreaSimple owner) {
        super(id, iwin, owner);
        spcRec = new Specific(id, this);
        uti3 = new Uti3(this);
    }

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y) {
        int x = (int) (X / iwin().scale) - Com5t.TRANSLATE_XY;
        int y = (int) (Y / iwin().scale) - Com5t.TRANSLATE_XY;
        return inside(x, y);
    }

    //Главная спецификация
    public abstract void setSpecific();

    //Вложеная спецификация
    public abstract void addSpecific(Specific specification);

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz + ", length=" + length();
    }
}
