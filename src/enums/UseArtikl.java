package enums;

import static enums.LayoutProfile.values;
import static enums.UseColcalc.values;
import java.util.stream.Stream;

/**
 * Тип профиля (SYSPROA.ATYPE) в системе конструкций
 */
public enum UseArtikl implements Enam {
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

    UseArtikl(int id, String name) {
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
    
    public static UseArtikl get(int id) {
        return Stream.of(values()).filter(en -> en.numb() == id).findFirst().orElse(null);
    }    
}
