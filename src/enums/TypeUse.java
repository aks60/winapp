package enums;

import static enums.SideProfile.values;
import static enums.VarColcalc.values;

/**
 * Тип профиля (SYSPROA.ATYPE) в системе конструкций
 */
public enum TypeUse implements Enam {
    UNKNOWN(0, "Любой тип"),
    FRAME(1, "Коробка"),
    STVORKA(2, "Створка"),
    IMPOST(3, "Импост"),
    STOIKA(5, "Стойка"),
    POPERECHINA(6, "Поперечина"),
    RASKLADKA(7, "Раскладка"),
    SHTULP(9, "Штульп"),
    ERKER(10, "Эркер");

    public int id;
    public String name;

    TypeUse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return Integer.valueOf(id);
    }

    public String text() {
        return name;
    }
    
    public Enam[] fields() {
        return values();
    } 
    
    public static TypeUse get(int side) {
        for (TypeUse profileSide : values()) {
            if (profileSide.id == side) {
                return profileSide;
            }
        }
        return null;
    }    
}
