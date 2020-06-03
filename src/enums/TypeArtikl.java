package enums;

import dataset.Record;
import domain.eArtikl;
import java.util.Arrays;
import java.util.List;

/**
 * Типы мат. ценностей
 */
public enum TypeArtikl {

    PPROFIL(1, 0, "Профили"),
    KOROBKA(1, 1, "Коробка"),
    STVORKA(1, 2, "Створка"),
    IMPOST(1, 3, "Импост"),
    X104(1, 4, "Штульп"),
    X105(1, 5, "Стойка"),
    X106(1, 6, "Стыковочный"),
    ARMIROVANIE(1, 7, "Армирование"),
    SHTAPIK(1, 8, "Штапик"),
    FURNITURA(1, 9, "Фурнитуоа"),
    X110(1, 10, "Наличник водоотб."),
    X111(1, 11, "Доп. профиль"),
    X112(1, 12, "Раскладка"),
    X113(1, 13, "Подоконник"),
    X114(1, 14, "Отлив"),
    X115(1, 15, "Откос"),
    X116(1, 16, "Поперечина"),
    MONTPROF(1, 17, "Монтажный профиль"),
    X118(1, 18, "Деревянный брус"),
    X119(1, 19, "Эркер"),
    X120(1, 20, "Профиль в составе  М/С"),
    X125(1, 25, "Профиль в составе стеклопакета"),
    X130(1, 30, "Профиль в составе роллеты"),
    X131(1, 31, "Профиль ламели"),
    X132(1, 32, "Вал"),
    X133(1, 33, "Шина"),
    X134(1, 34, "Короб"),
    KONZEVPROF(1, 35, "Концнвой профиль"),
    FIKSPROF(1, 36, "Фиксирующий профиль"),
    X137(1, 37, "Усиливающий профиль"),
    X149(1, 49, "Ламель жалюзи"),
    X150(1, 50, "Профиль в составе жалюзи"),
    X165(1, 65, "Стыковочный"),
    X170(1, 70, "Дистанционер фасада"),
    X175(1, 75, "Термомост"),
    ACSESYAR(2, 0, "Аксессуары"),
    X201(2, 1, "Штучный эдемент"),
    X202(2, 2, "Закладной крепёж"),
    X203(2, 3, "Монтажный крепёж"),
    X204(2, 4, "Расходный материал"),
    SOEDINITEL(2, 5, "Соединитель"),
    X206(2, 6, "Концевик"),
    X209(2, 9, "Фурнитура замок"),
    X210(2, 10, "Фурнитура штучная"),
    FURNRUCHKA(2, 11, "Фурнитура ручка"),
    FURNLOOP(2, 12, "Фурнитура петля"),
    X213(2, 13, "Фурнитура дополнительная"),
    X214(2, 14, "Фурнитура накладная"),
    X215(2, 15, "Аксессуар в составе откосов"),
    X220(2, 20, "Аксессуар в составе М/С"),
    X230(2, 30, "Аксессуар в составе роллеты"),
    X231(2, 31, "Привод в составе роллеты"),
    X250(2, 50, "Аксессуар в составе жалюзи"),
    X290(2, 90, "Эксклюзив"),
    POGONAG(3, 0, "Погонаж"),
    X301(3, 1, "Уплотнение притвора"),
    X302(3, 2, "Уплотнение заполнения"),
    X303(3, 3, "Лента"),
    X304(3, 4, "Шнур"),
    X315(3, 15, "Уплотнение в составе откосов"),
    X320(3, 20, "Уплотнение в составе М/С"),
    X330(3, 30, "Уплотнение в составе роллеты"),
    X350(3, 50, "Уплотнение в составе жалюзи"),
    X371(3, 71, "Внутреннее уплотнение фасада"),
    X372(3, 72, "Внешнее уплотнение фасада"),
    INSTRYMENT(4, 0, "Инструмент"),
    X401(4, 1, "Инструмент"),
    X402(4, 2, "Амортизация"),
    X403(4, 3, "Аренда"),
    ZAPOLNEN(5, 0, "Заполнения"),
    X501(5, 1, "Стекло"),
    GLASS(5, 2, "Стеклопакет"),
    X503(5, 3, "Сендвич"),
    X504(5, 4, "Вагонка"),
    X505(5, 5, "Алюминиевый лист"),
    X506(5, 6, "Специальное стекло"),
    X509(5, 9, "Конструктив"),
    X511(5, 11, "Тонируюшая плёнка"),
    X512(5, 12, "Бронирующая плёнка"),
    X515(5, 15, "Панель откоса"),
    X520(5, 20, "Москитная сетка (М/С)"),
    X550(5, 50, "Полотно жалюзи"),
    X590(5, 90, "Специальный тип (!)"),
    X599(5, 99, "Фиксированный блок");

    public int id1 = 0;
    public int id2 = 0;
    public String name = "";

    TypeArtikl(int id1, int id2, String name) {
        this.id1 = id1;
        this.id2 = id2;
        this.name = name;
    }

    public boolean isType(Record artiklsRec) {
        if (id1 == artiklsRec.getInt(eArtikl.level1) && id2 == artiklsRec.getInt(eArtikl.level2)) {
            return true;
        }
        return false;
    }

    public static TypeArtikl find(Record record) {
        return Arrays.stream(values()).filter(el -> (el.id1 == record.getInt(eArtikl.level1) && el.id2 == record.getInt(eArtikl.level2))).findFirst().orElse(PPROFIL);
    }

    public static String find(int _id1, int _id2) {
        return Arrays.stream(values()).filter(el -> (el.id1 == _id1 && el.id2 == _id2)).findFirst().orElse(PPROFIL).name;
    }

    public String toString() {
        return name;
    }
}
