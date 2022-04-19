package builder.script;

import builder.Wincalc;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import enums.Layout;
import enums.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import startup.Main;

public class GsonRoot extends GsonElem {

    public String name = "Конструкция";
    public int prj = 1; //PNUMB - номер тестируемого проекта, поле пока нужно только для тестов 
    public int ord = 1; //ONUMB - номер тестируемого заказа, поле пока нужно только для тестов 
    private Integer nuni = -3;  //nuni профиля (PRO4_SYSPROF.NUNI)
    protected Float width = null; //ширина area, мм.
    protected Float height = null; //высота area, мм 
    protected Float widthAdd = 0f;  //дополнительная ширина, мм.
    protected Float heightAdd = 0f;  //дополнительная высота, мм.
    public int form = 0;  //форма контура (параметр в развитии)
    public Integer color1 = -3;  //основная текстура
    public Integer color2 = -3;  //внутренняя текстура
    public Integer color3 = -3;  //внешняя текстура    

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height, int color1, int color2, int color3) {
        init(prj, ord, nuni, name, layout, type, width, height, height, color1, color2, color3, null);
    }

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height, int color1, int color2, int color3, String paramJson) {
        init(prj, ord, nuni, name, layout, type, width, height, height, color1, color2, color3, paramJson);
    }

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height1, float height2, int color1, int color2, int color3) {

        if (type == Type.TRAPEZE) {
            if (height1 > height2) {
                init(prj, ord, nuni, name, layout, type, width, height1, height2, color1, color2, color3, null);
                form = 2;
            } else {
                init(prj, ord, nuni, name, layout, type, width, height2, height1, color1, color2, color3, null);
                form = 4;
            }
        } else {
            if (type == Type.ARCH) {
                form = 3;
            }
            init(prj, ord, nuni, name, layout, type, width, height1, height2, color1, color2, color3, null);
        }
    }

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height1, float height2, int color1, int color2, int color3, String paramJson) {
        init(prj, ord, nuni, name, layout, type, width, height1, height2, color1, color2, color3, paramJson);
    }

    public void init(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height1, float height2, int color1, int color2, int color3, String paramJson) {
        super.genId = 0;
        super.id = 0;
        this.prj = prj;
        this.ord = ord;
        this.nuni = nuni;
        this.name = name;
        this.layout = layout;
        this.type = type;
        this.width = width;
        this.height = height1;
        this.heightAdd = height2;
        this.length = null;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        if(paramJson != null)
        this.param = new Gson().fromJson(paramJson, JsonObject.class);
    }

    public void propery(String prj, int nuni, String name) {
        this.nuni = nuni;
        this.prj = Integer.valueOf(prj);
        this.name = (name == null) ? this.name : name;
        if (nuni == -3 && Main.dev == false) {
            this.color1 = -3;
            this.color2 = -3;
            this.color3 = -3;
        }
    }

    public float height() {
        return height;
    }

    public void height(float h) {
        height = h;
    }

    public float heightAdd() {
        return heightAdd;
    }

    public void heightAdd(float h) {
        heightAdd = h;
    }

    public float width() {
        return width;
    }
    
    public void width(float w) {
        width = w;
    }

    public int nuni() {
        return nuni;
    }

    @Deprecated
    public List<GsonScale> lineArea(Wincalc winc, Layout layout) {
        Set<GsonElem> set1 = new LinkedHashSet(), set2 = new LinkedHashSet();
        Set<GsonScale> setOut = new LinkedHashSet();
        lineArea(set1, this, layout);
        for (GsonElem elem : set1) {
            set2.clear();
            lineArea(set2, elem, layout);
            if (set2.isEmpty()) {
                setOut.add(new GsonScale(winc.rootArea));
            }
        }
        if (setOut.isEmpty()) {
            setOut.add(new GsonScale(winc.rootArea));
        }
        return new ArrayList(setOut);
    }

    @Deprecated
    public void lineArea(Set<GsonElem> set, GsonElem elem, Layout layout) {
        for (int i = 0; i < elem.childs.size(); ++i) {
            GsonElem elem2 = elem.childs.get(i);
            if (elem2.owner.layout == layout && (elem2.type == Type.IMPOST || elem2.type == Type.SHTULP || elem2.type == Type.STOIKA)) {
                set.add(elem.childs.get(i - 1));
                set.add(elem.childs.get(i + 1));
            }
            lineArea(set, elem2, layout);
        }
    }   
}
