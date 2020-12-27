package builder.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.Arrays;
import builder.model.AreaSimple;
import builder.model.ElemSimple;

public class Mediate {

    public float id = -1;  // идентификатор элемента
    public Mediate owner = null; //владелец
    public AreaSimple area5e = null; //ссылка для добавления детей в контейнер
    public TypeElem type = TypeElem.NONE; //тип элемента
    public LayoutArea layout = LayoutArea.ANY;  //ориентация при расположении      
    public float width = 0;  //ширина area5e, мм
    public float height = 0; //высота area5e, мм   
    public String param = null;

    public Mediate(Mediate owner, float id, String type, String layout, String param) {

        this.id = id;
        this.owner = owner;
        this.type = Arrays.asList(TypeElem.values()).stream().filter(it -> it.name().equals(type)).findFirst().orElse(null);
        this.layout = Arrays.asList(LayoutArea.values()).stream().filter(it -> it.name().equals(layout)).findFirst().orElse(null);
        this.param = param;
    }

    public Mediate(Mediate owner, float id, String type, String layout,
            float width, float height, String param) {

        this(owner, id, type, layout, param);
        this.width = width;
        this.height = height;
    }

    public AreaSimple addArea(AreaSimple area2) {
        owner.area5e.listChild.add(area2); //теперь владелец имеет эту AreaSimple в списке
        this.area5e = area2; //тут будут лежать компоненты когда AreaSimple станет владельцем
        return area2;
    }

    public ElemSimple addElem(ElemSimple elem5e) {
        owner.area5e.listChild.add(elem5e); //теперь владелец имеет этот ElemSimple в списке
        return elem5e;
    }

    public String toString() {
        float owner2 = (owner == null) ? -1 : owner.id;
        return "ELEM- " + type.name() + ", owner=" + owner2 + ", id=" + id + ", type=" + type + ", layout="
                + layout + ", width= " + width + ", height=" + height + ", param=" + param;
    }
}
