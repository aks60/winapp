package dataset;

import dataset.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Record<E> extends ArrayList<E> {

    public static boolean DIRTY = false;
    private Table table = null;

    public Record() {
        super();
    }
    
    public Record(Table table) {
        super();
        this.table = table;
    }

    public Record(int initialCapacity) {
        super(initialCapacity);
    }

    //ИЗМЕНЕНИЯ СТАТУСА ЗАПИСИ
    public E set(int index, E element) {
        if (index != 0 && Query.SEL.equals(get(0))) {
            super.set(0, (E) Query.UPD);
            DIRTY = true;
        }
        return super.set(index, element);
    }

    //ИЗМЕНЕНИЯ СТАТУСА ЗАПИСИ
    public E set(Field field, E element) {
        return (E) set(field.ordinal(), element);
    }

    //ИЗМЕНЕНИЯ СТАТУСА ЗАПИСИ
    public E setNo(int index, E element) {
        return super.set(index, element);
    }

    //ЗАПИСЬ БЕЗ ИЗМЕНЕНИЯ СТАТУСА
    public E setNo(Field field, E element) {
        return super.set(field.ordinal(), element);
    }

    public Object get(Field field) {
        return super.get(field.ordinal());
    }

    public String getStr(int index) {
        return (super.get(index) == null) ? "" : String.valueOf(super.get(index));
    }

    public int getInt(int index) {
        Object obj = super.get(index);
        return (obj == null) ? -1 : Integer.valueOf(String.valueOf(obj));
    }

    public float getFloat(int index) {
        Object obj = super.get(index);
        return (obj == null) ? -1 : Float.valueOf(String.valueOf(obj));
    }

    public double getDbl(int index) {
        Object obj = super.get(index);
        return (obj == null) ? -1 : Double.valueOf(String.valueOf(obj));
    }

    public Date getDate(int index) {
        return (super.get(index) == null) ? null : (Date) super.get(index);
    }

    public String getStr(Field field) {
        return (super.get(field.ordinal()) == null) ? "" : String.valueOf(super.get(field.ordinal()));
    }

    public int getInt(Field field) {
        try {
            Object obj = super.get(field.ordinal());
            return (obj == null) ? -1 : Integer.valueOf(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public float getFloat(Field field) {
        Object obj = super.get(field.ordinal());
        return (obj == null) ? -1 : Float.valueOf(String.valueOf(obj));
    }

    public double getDbl(Field field) {
        Object obj = super.get(field.ordinal());
        return (obj == null) ? -1 : Double.valueOf(String.valueOf(obj));
    }

    public Date getDate(Field field) {
        return (super.get(field.ordinal()) == null) ? null : (Date) super.get(field.ordinal());
    }

    //Проверка на корректность ввода
    public String validate(ArrayList<Field> fields) {
        for (int index = 1; index < fields.size(); index++) {
            MetaField prop = fields.get(index).meta();
            Object value = super.get(fields.get(index).ordinal());
            Object mes = prop.validate(value);
            if (mes != null) {
                return mes.toString();
            }
        }
        return null;
    }
}
