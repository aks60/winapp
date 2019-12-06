package dataset;

import dataset.Field.DOMAIN;
import dataset.Field.TYPE;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Date;

/**
 *
 * @author Aksenov Sergey
 *
 * <p>
 * Методанные таблицы</p>
 */
public class MetaField {

    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private boolean isnull = false; //запретить null в таблице
    private Field field = null; //enum
    public String field_name = null; //служебное для переопределение имени поля
    public String descr = ""; //описание поля
    private TYPE type = TYPE.OBJ; //тип поля
    private DOMAIN domain = null; //значение домена
    private Integer size = 0; //размер поля
    private Field.EDIT edit = Field.EDIT.TRUE; //запретить редактирование
    public Field foreignkey = null; //внешний ключ
    public Object value = null; // не используется
    public ArrayDeque<Object> stack = new ArrayDeque();

    public MetaField(Field e) {
        field = e;
        if (e instanceof Enum) {
            this.field_name = ((Enum) e).name(); //имя поля но умолчанию
        }
    }

    public void init(Object... p) {

        if (p.length < 5) {
            System.out.println("MetaField.init() - ОШИБКА! Количество параметров меньше 4. Поле <" + p[0].toString() + ">");
            return;
        }
        type(Integer.valueOf(p[Entity.type.ordinal()].toString())); //тип поля
        this.size = Integer.valueOf(p[Entity.size.ordinal()].toString());
        this.isnull = p[Entity.nullable.ordinal()].equals("1"); //равно null
        this.descr = p[Entity.comment.ordinal()].toString(); //описание поля 
        this.field_name = p[Entity.fname.ordinal()].toString(); //переопределение имени поля
    }

    /**
     * Проверка нужна для корректного ввода данных пользователем
     */
    public String validate(Object value) {

        if (field_name == null || size == 0) {
            return null;
        }
        try {
            if (value == null && isnull == true) {
                return null;
            } else if (value == null && isnull == false) {
                return "Поле <" + descr + "> должно иметь значение";
            } else if (type.type == Integer.class) {
                Integer.valueOf(String.valueOf(value));
            } else if (type.type == Double.class) {
                String str = String.valueOf(value).replace(',', '.');
                Double.valueOf(str);
            } else if (type.type == Float.class) {
                String str = String.valueOf(value).replace(',', '.');
                Float.valueOf(str);
            } else if (type.type == Date.class) {
                if (value instanceof Date) {
                    return null;
                } else {
                    try {
                        dateFormat.parse(value.toString());
                    } catch (ParseException e) {
                        return "Поле <" + descr + "> заполнено не корректно";
                    }

                }
            } else if (value.toString().length() > size) {
                return "Поле <" + descr + "> не должно иметь больше "
                        + String.valueOf(size) + " знаков";
            }
        } catch (Exception e) {
            return "Поле <" + descr + "> заполнено не корректно";
        }
        return null;
    }

    public String getColName() {
        return descr;
    }

    public void isnull(boolean emty) {
        this.isnull = emty;
    }

    public boolean isnull() {
        return isnull;
    }

    public void type(int type) {

        for (TYPE type2 : TYPE.values()) {
            if (type2.conv.contains(type) == true) {
                this.type = type2;
            }
        }
    }

    public TYPE type() {
        return type;
    }

    public Field foreignkey() {
        return foreignkey;
    }

    public boolean edit() {
        return edit.edit;
    }

    public int domain() {
        return domain.domain;
    }

    public void size(Integer size) {
        this.size = size;
    }

    public Integer size() {
        return size;
    }

    public int width() {
        return size;
    }

    public void value(Object value) {
        this.value = value;
    }

    public Object value() {
        return value;
    }

    public ArrayDeque<Object> getStack() {
        return stack;
    }

    public Field push(Object val) {
//        if(val.equals("null")) {
//            stack.clear();
//        } else {
//            stack.push(val);
//        }
        stack.push((val == null) ? "null" : val);
        return field;
    }

    public Object pop() {
        try {
            return stack.pop();
        } catch (Exception e) {
            return null;
        }
    }
}
