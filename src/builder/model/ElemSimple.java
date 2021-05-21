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
import builder.making.CheckPar2;

public abstract class ElemSimple extends Com5t {

    public float anglCut1 = 45; //Угол реза рамы
    public float anglCut2 = 45; //Угол реза рамы
    public float anglHoriz = -1; //угол к горизонту
    public Specific spcRec = null; //спецификация элемента
    protected CheckPar2 spc7d = null;
    public Color borderColor = Color.BLACK;
    public HashMap<String, String> mapFieldVal = new HashMap(); //свойства элемента <имя поля => значение>

    public ElemSimple(float id, Wincalc iwin, AreaSimple owner) {
        super(id, iwin, owner);        
        spcRec = new Specific(id, this);
        spc7d = new CheckPar2(this);
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
