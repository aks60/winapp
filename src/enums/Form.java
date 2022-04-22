package enums;

import static enums.Layout.values;

public enum Form {
    NULL(0, "Любая"),
    BOTT(1, "Нижняя"),
    RIGHT(2, "Правая"),
    TOP(3, "Верхняя"),
    LEFT(4, "Левая");

    public int id;
    public String name;

    Form(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Form get(int id) {
        for (Form form : values()) {
            if (form.id == id) {
                return form;
            }
        }
        return null;
    }
}
