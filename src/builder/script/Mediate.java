package builder.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.Arrays;
import builder.model.AreaSimple;
import builder.model.ElemSimple;

public class Mediate extends AreaElem {

    public Mediate owner = this; //владелец
    public AreaSimple area5e = null; //для добавления детей в контейнер

    public Mediate(Mediate owner, float id, String type, String layout, String param) {
        
        this.id = id;
        if (owner != null) {
            this.owner = owner;
        }
        this.elemType = Arrays.asList(TypeElem.values()).stream().filter(it -> it.name().equals(type)).findFirst().orElse(null);
        this.layoutArea = Arrays.asList(LayoutArea.values()).stream().filter(it -> it.name().equals(layout)).findFirst().orElse(null);
        this.paramJson = param;
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
}
