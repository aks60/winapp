package builder.script;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.awt.geom.Point2D;
import java.util.List;

public class GeoRoot {

    protected static transient double genId = 0;  //идентификатор   
    public String version = null; //версия
    public Integer prj = null; //PNUMB - номер тестируемого проекта, поле пока нужно только для тестов при сравнении с PS4
    public Integer ord = null; //ONUMB - номер тестируемого заказа, поле пока нужно только для тестов при сравнении с PS4 
    public Integer nuni = null;  //nuni профиля (PRO4_SYSPROF.NUNI)    
    public String name = null;  //название пректа    
    public List<Double> poly = null; //многоугольник
    public List<Double> line = null; //линии
    public JsonObject param = new JsonObject(); //параметры элемента

    public GeoRoot(String version, Integer prj, Integer ord, Integer nuni, String name, List poly, List line) {

        this.version = version;
        this.prj = prj;
        this.ord = ord;
        this.nuni = nuni;
        this.name = name;
        this.poly = poly;
        this.line = line;
    }


    public String toJson() {
        return new GsonBuilder().create().toJson(this);
    }
}
