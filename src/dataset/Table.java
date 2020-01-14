package dataset;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Aksenov Sergey
 *
 * <p>
 * Доступа к строкам таблицы </p>
 */
public class Table extends ArrayList<Record> {
    
    private static SimpleDateFormat fd = new SimpleDateFormat("dd.MM.yyyy");
    
    /**
     * Список активных полей таблицы
     */
    protected ArrayList<Field> fields = new ArrayList();

    /**
     * Поля и их индексы вошедшие в Record
     */
    private HashMap<String, Integer> fieldToIndex = new HashMap();

    /**
     * Поля и их индексы
     */
    protected HashMap columnToIndex = new HashMap<String, Integer>() {

        public Integer put(String key, Integer value) {
            return super.put(key.toLowerCase(), value);
        }

        public Integer get(Object key) {
            return (Integer) super.get(key.toString().toLowerCase());
        }
    };

    /**
     * Индекс первичного ключа Record
     */
    private Integer indexPk = null;

    /**
     * Значение первичного ключа и его токен
     */
    private TreeMap<Object, Record> pkToRecord = new TreeMap();

    public Table() {
    }

    public int index(String name) {
        return fieldToIndex.get(name);
    }

    /**
     * Переопределён чтобы отслеживать TreeMap<Object, DataRecord> map
     */
    public Record remove(int index) {
        if (indexPk != null) {
            Record record = get(index);
            pkToRecord.remove(record.get(indexPk));
        }
        return super.remove(index);
    }

    /**
     * Добавление данных если из сервера то fields[0] = SELECT если из клиента
     * то fields[0] = INSERT
     */
    public boolean add(Record record) {
        //Record record = isAl(elements);
        if (indexPk != null) {
            Object valuePk = record.get(indexPk);
            if (valuePk != null) {
                pkToRecord.put(valuePk, record);
            }
        }
        return super.add(record);
    }

    /**
     * Вставка данных если из сервера то fields[0] = SELECT если из клиента то
     * fields[0] = INSERT
     */
    public void add(int index, Record record) {
        //Record record = isAl(elements);
        if (indexPk != null) {
            Object valuePk = record.get(indexPk);
            if (valuePk != null) {
                pkToRecord.put(valuePk, record);
            }
        }
        super.add(index, record);
    }

    /*private Record isAl(Record elements) {
        Record record = (elements == null) ? new Record(this) : elements;
        if (elements == null) {
            for (Field field : fields) {
                record.add(field.value());
            }
        }
        return record;
    }*/  
    
    /**
     * Вставка данных fields[0] = UPDATE
     */
    public void set(Object value, int index, Field field) {
        Object v = get(index, field);
        if (v != null && value != null && v.equals(value)) {
            return;
        }
        ArrayList record = super.get(index);
        record.set(field.ordinal(), value);
    }

    /**
     * Возвращает значение поля field
     */
    public Object get(int index, Field field) {
        if (index == -1) {
            return null;
        }
        Record record = super.get(index);
        if (record == null) {
            return null;
        }
        return record.get(field);
    }

    /**
     * Возвращает значение поля field
     */
    public Integer getInt(int index, Field field) {
        if (index == -1) {
            return null;
        }
        Record record = super.get(index);
        if (record == null) {
            return null;
        }
        return record.getInt(field);
    }

    /**
     * Возвращает значение поля field
     */
    public String getStr(int index, Field field) {
        if (index == -1) {
            return null;
        }
        Record record = super.get(index);
        if (record == null) {
            return null;
        }
        return record.getStr(field);
    }

    /**
     * Возвращает значение поля field или def
     */
    public Object get(int index, Field field, Object def) {
        Object obj = get(index, field);
        return obj == null ? def : obj;
    }

    /**
     * Возвращает значение поля field или def
     */
    public <T> T getAs(int index, Field field, Object def) {
        Object obj = get(index, field);
        return (obj == null) ? (T) def : (T) obj;
    }

    /**
     * Возвращает значение поля field или def
     */
    public <T> T getAs(ArrayList record, Field field, Object def) {
        Object obj = record.get(field.ordinal());
        return (obj == null) ? (T) def : (T) obj;
    }

    public Object getAt(int index, Field field) {
        Record record = get(index);
        return record.getAt(field);
    }

    public Object getAt(int index, Field field, Object def) {
        Object obj = getAt(index, field);
        return obj == null ? def : obj;
    }

    /**
     * Возвращает HashMap<Integer, T>
     */
    public <T> HashMap<Integer, T> mapAdded(Field key, String value) {
        if (key == null) {
            return null;
        }
        HashMap<Integer, T> hm = new HashMap();
        Iterator itr = iterator();
        while (itr.hasNext()) {

            Record record = (Record) itr.next();
            if (value == null) {
                hm.put(record.getInt(key), (T) record);
            } else {
                hm.put(record.getInt(key), (T) record.get(value));
            }
        }
        return hm;
    }
    
    public Record newRecord(String up) {
        Record record = new Record(this);
        for (Field field : fields.get(0).fields()) {
            record.add(null);
        }
        record.set(0, up);
        return record;
    }
    
    /**
     * Возвращает Record по ключу используя TreeMap
     */
    public Record mapRecord(Object elementPk) {
        if (elementPk != null) {
            //if (elementPk instanceof Integer) {
            return pkToRecord.get(elementPk);
        }
        return null;
    }

    /**
     * Ищем индекс объекта DataRecord в таблице DataTable
     */
    public int getIndex(Record record) {
        //цикл по записям в таблице
        for (int indexRow = 0; indexRow < size(); ++indexRow) {
            if (this.get(indexRow).equals(record)) {
                return indexRow;
            }
        }
        return -1;
    }
}
