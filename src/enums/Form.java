package enums;

import static enums.Layout.values;

public enum Form {
    N0(0, "Любая"),
    N1(1, "Нижняя"),
    N2(2, "Правая"),
    N3(3, "Верхняя"),
    N4(4, "Левая");

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
