package dataset;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Table extends ArrayList<Record> {

    protected Query root = null;
    protected HashMap<String, Query> mapQuery = new HashMap();

    private static SimpleDateFormat fd = new SimpleDateFormat("dd.MM.yyyy");

    protected ArrayList<Field> fields = new ArrayList();

    public Table() {
    }

    public Query table(String name_table) {
        return null;
    }

    public Record remove(int index) {
        return super.remove(index);
    }

    public void add() {
        for (Map.Entry<String, Query> entry : mapQuery.entrySet()) {
            Query table = entry.getValue();
            Record record = table.newRecord("UPD");
            table.add(record);
        }
    }

    public void set(Object value, int index, Field field) {
        Object v = get(index, field);
        if (v != null && value != null && v.equals(value)) {
            return;
        }
        Table table = table(field.tname());
        ArrayList record = table.get(index);
        record.set(field.ordinal(), value);
    }

    public Object get(int index, Field field) {
        if (index != -1) {
            Table table = table(field.tname());
            Record record = table.get(index);
            return (record == null) ? null : record.get(field);
        }
        return null;
    }

    public Object get(int index, Field field, Object def) {
        Object obj = get(index, field);
        return obj == null ? def : obj;
    }

    public <T> T getAs(int index, Field field, Object def) {
        Object obj = get(index, field);
        return (obj == null) ? (T) def : (T) obj;
    }

    public Record newRecord(String up) {
        Record record = new Record(this);
        for (Field field : fields.get(0).fields()) {
            record.add(null);
        }
        record.set(0, up);
        return record;
    }
}
