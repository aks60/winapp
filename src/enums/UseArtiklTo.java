package enums;

import dataset.Record;
import java.util.stream.Stream;

/**
 * Тип профиля (SYSPROA.ATYPE) в системе конструкций
 */
public enum UseArtiklTo implements Enam {
    ANY(0, 0, "Любой тип"),
    FRAME(1, 1, "Коробка"),
    STVORKA(2, 2, "Створка"),
    IMPOST(3, 3, "Импост"),
    STOIKA(5, 5, "Стойка"),
    POPERECHINA(6, 16, "Поперечина"),
    RASKLADKA(7, 12, "Раскладка"),
    SHTULP(9, 4, "Штульп"),
    ERKER(10, 19, "Эркер");

    public int id;
    public int id2;
    public String name;
    public Record sysprofRec;

    UseArtiklTo(int id, int id2, String name) {
        this.id = id;
        this.id2 = id2;
        this.name = name;
    }

    UseArtiklTo(int id, String name) {
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

    public static UseArtiklTo get(int id) {
        return Stream.of(values()).filter(en -> en.numb() == id).findFirst().orElse(null);
    }
}
