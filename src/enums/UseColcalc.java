package enums;

//Варианты рассчёта текстуры
import static enums.UseFurn1.values;

public enum UseColcalc implements Enam {

    P00(0, "Указанная вручную"),
    P01(1, "Основная"),
    P02(2, "Внутренняя"),
    P03(3, "Внешняя"),
    P04(4, "Параметр внутренняя"),
    P06(6, "Основная и серия"),
    P07(7, "Внутренняя и серия"),
    P08(8, "Внешняя и серия"),
    P09(9, "Параметр основная"),
    P11(11, "Профиль"),
    P12(12, "Параметр внешняя"),
    P15(15, "Заполнение");

    public int id;
    public int id2;
    public String name;

    private UseColcalc(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return id;
    }

    public String text() {
        return name;
    }

    public Enam[] fields() {
        return values();
    }

    public static String[] precision = {"100000", "Точный подбор"};
    public static String[] automatic = {"0", "Автоатический подбор"};
}

/*/////////////    АЛГОРИТМ   //////////////////
        вариант подбора основной текстуры в профстрой-3

==== Точный подбор -100000 ====
0 указанная вручную -не устанавливается
11 профиль 
21 заполнение
31 основная
32 внутренняя
33 внешняя
41 основная и серия
42 внутренняя и серия
43 внешняя и серия
50 параметр -устанавливается  на "профиль -11"
60 параметр и серия  -устанавливается на "профиль -11"

====Авто_подбор  -0 ====
0 указанная вручную -не устанавливается
11 профиль 
21 заполнение
31 основная
32 внутренняя
33 внешняя
41 основная и серия
42 внутренняя и серия
43 внешняя и серия
50 параметр -устанавливается на "профиль -11"
60 параметр и серия  -устанавливается на "профиль -11"

==== Цвет выбран из парам. системных > 0 ====
указанная вручную  -0 -всегда устанавливаетя, других вариантов нет

==== Цвет выбран из парам. пользователя < 0 ====
0 указанная вручную -не устанавливается
11 профиль 
21 заполнение
31 основная
32 внутренняя
33 внешняя
41 основная и серия
42 внутренняя и серия
43 внешняя и серия
50 параметр
60 параметр и серия

БиМакс база
0 - указана,
273 - на основе изделия,
546 - по внутр. изделия,
799 - по заполнению (зависимая?)
801 - по основе изделия,
819 - по внешн. изделия,
1092 - по параметру (внутр.),
1638 - по основе в серии,
1911 - по внутр. в серии,
2184 - по внешн. в серии,
3003 - по профилю,
3145 - по параметру (основа),
3276 - по параметру (внешн.),
4095 - по заполнению."
 */
