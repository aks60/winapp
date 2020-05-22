package calculate.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.Arrays;
import calculate.model.AreaSimple;
import calculate.model.ElemSimple;

public class Intermediate {

    public float id = -1;  // идентификатор элемента
    public Intermediate owner = null; //владелец
    public AreaSimple area5e = null; //ссылка для добавления детей в контейнер
    public TypeElem type = TypeElem.NONE; //тип элемента
    public LayoutArea layout = LayoutArea.ANY;  //ориентация при располодении      
    public float width = 0;  //ширина area5e, мм
    public float height = 0; //высота area5e, мм   
    public String param = null;   

    public Intermediate(Intermediate owner, float id, String type, String layout, String param) {

        this.id = id;
        this.owner = owner;
        this.type = Arrays.asList(TypeElem.values()).stream().filter(it -> it.name().equals(type)).findFirst().orElse(null);
        this.layout = Arrays.asList(LayoutArea.values()).stream().filter(it -> it.name().equals(layout)).findFirst().orElse(null);
        this.param = param;
    }

    public Intermediate(Intermediate owner, float id, String type, String layout,
            float width, float height, String param) {

        this(owner, id, type, layout, param);
        this.width = width;
        this.height = height;
    }
    
    public AreaSimple addArea(AreaSimple area2) {
        owner.area5e.listChild.add(area2);
        this.area5e = area2;
        return area2;
    }

    public ElemSimple addElem(ElemSimple elem5e) {
        owner.area5e.listChild.add(elem5e);
        return elem5e;
    }
    
    public String toString() {
        float owner2 = (owner == null) ?-1 :owner.id; 
        return "ELEM- " + type.name() + ", owner=" + owner2 + ", id=" + id + ", type=" + type + ", layout="
                + layout + ", width= " + width + ", height=" + height + ", param=" + param;
    }
}
