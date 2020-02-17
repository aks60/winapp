package spring;

import java.lang.reflect.Field;

public class Artdet {

    public int up;
    public int id;// "Идентификатор", "id"),
//    public float coef_nakl;//коэффициент накладных расходов
//    public float cost_unit;//тариф единицы измерения
//    public float cost_cl1;//тариф основной текстуры
//    public float cost_cl2;//тариф внутренний текстуры
//    public float cost_cl3;//тариф внешний текстуры
//    public float cost_cl4;//тариф двухсторонний текстуры
//    public String artikl1;//артикул склада
//    public String artikl2;//артикул 1С
//    public int cways;//галочки по приоритетности текстур (основной, внутренней, внешней): 0 - нет галочек (по всем текстурам этот цвет не основной для материала), 1 - галочка на внутренней текстуре, 2 - галочка на внешней текстуре, 3 - галочки на внутренней и внешней текстурах, 4 - галочка на основной текстуре, 5 - галочки на основной и внутреней текстурах, 6 - галочки на основной и внешней текстурах, 7 - галочки на всех текстурах (по всем текстурам основной цвет),
//    public int artikl_id;//ссылка
//    public int color_id;//ссылка  

    public Artdet(int up, int id) {
        this.up = up;
        this.id = id;

    }

    public void get() {
        try {
        //Artdet this = new Artdet(555, 777);
        
        Class<? extends Artdet> carClass = this.getClass();
        Field field1 = carClass.getDeclaredField("id");
        int horsepowerValue = field1.getInt(this);
        System.out.println(horsepowerValue);

        Field field2 = carClass.getField("up");
        System.out.println("Before change:" + field2.get(this));
        field2.set(this, 33);
        System.out.println("After change:" + field2.get(this));

        } catch(Exception e) {
            
        }
    }
}
