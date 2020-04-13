package enums;

import static enums.SideProfile.values;
import static enums.VarColcalc.values;

/**
 * Тип профиля (SYSPROA.ATYPE) в системе конструкций
 */
public enum TypeUse implements Enam {
    UNKNOWN(0, "любой тип"),
    FRAME(1, "коробка"),
    STVORKA(2, "створка"),
    IMPOST(3, "импост"),
    STOIKA(5, "стойка"),
    POPERECHINA(6, "поперечина"),
    RASKLADKA(7, "раскладка"),
    SHTULP(9, "штульп"),
    ERKER(10, "эркер");

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
