package enums;

import static enums.ProfileSide.values;

/**
 * Тип профиля (SYSPROA.ATYPE) в системе конструкций
 */
public enum TypeProfile {
    UNKNOWN(0, "любой тип"),
    FRAME(1, "коробка"),
    STVORKA(2, "створка"),
    IMPOST(3, "импост"),
    STOIKA(5, "стойка"),
    POPERECHINA(6, "поперечина"),
    RASKLADKA(7, "раскладка"),
    SHTULP(9, "штульп"),
    ERKER(10, "эркер");

    public int value;
    public String name;

    TypeProfile(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public static TypeProfile get(int side) {
        for (TypeProfile profileSide : values()) {
            if (profileSide.value == side) {
                return profileSide;
            }
        }
        return null;
    }    
}
