package enums;

//Варианты рассчёта текстуры
import static enums.UseFurn1.values;

public enum UseColor implements Enam {

    MANUAL(0, "Указанная вручную"),
    PROF(11, "Профиль"),
    GLAS(15, "Заполнение"),
    COL1(1, "Основная"),
    COL2(2, "Внутренняя"),
    COL3(3, "Внешняя"),
    C1SER(6, "Основная и серия"),
    C2SER(7, "Внутренняя и серия"),
    C3SER(8, "Внешняя и серия"),
    PARAM(9, "Параметр основной"),
    P04(4, "№04 Параметр внутренний"),
    P12(12, "№12 Параметр внешний"),
    PARSER(10, "Параметр и серия"),    
    P05(5, "Параметр №05"),        
    P13(13, "Параметр №13"),
    P14(14, "Параметр №14");

    /* Непомню откуда я это дёрнул ?
    P09(9, "Параметр основа"),
    P04(4, "Параметр внутренняя"),
    P12(12, "Параметр внешняя"),    
     */
    public int id;
    public String name;

    private UseColor(int id, String name) {
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

    public static Object[] precision = {100000, "Точн.подбор"};
    public static Object[] automatic = {0, "Автоподбор"};
}

/*/////////////    АЛГОРИТМ   //////////////////
        вариант подбора основной текстуры в профстрой-3

==== Точный подбор -100000 ====
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
0  указанная вручную 
41 основная и серия
42 внутренняя и серия
43 внешняя и серия

==== Цвет выбран из парам. пользователя < 0 ====
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

==== БиМакс база  =====
0 - указана,
273 - на основе изделия,
3003 - по профилю,
4095 - по заполнению."
546 - по внутр. изделия,
801 - по основе изделия,
819 - по внешн. изделия,
1638 - по основе в серии,
1911 - по внутр. в серии,
2184 - по внешн. в серии,
3145 - по параметру (основа - 12),
1092 - по параметру (внутр. - 4),
3276 - по параметру (внешн.),
799 - по заполнению (зависимая?)

=========================== МАНУАЛ к базам ПРОФСТРОЯ.xls =======================

==VSTASPC==
"Тип подбора
0 - указана вручную
11 - профиль
31 - основная"


==FURNSPC==
"вариант подбора основной текстуры:
0 - указана,
799 - по заполнению (зависимая?)
273 - на основе изделия,
546 - по внутр. изделия,
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
================================================================================
 */
