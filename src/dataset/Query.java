package dataset;

import common.Util;
import static dataset.Query.INS;
import static dataset.Query.connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.swing.JOptionPane;

public class Query extends Table {

    private static String schema = "";
    public static Connection connection = null;
    public static String INS = "INS";
    public static String SEL = "SEL";
    public static String UPD = "UPD";
    public static String DEL = "DEL";

    public Query(Query query) {
        this.root = query;
    }

    public Query(Field... fields) {
        this.root = this;
        for (Field field : fields) {
            if (!field.name().equals("up")) {
                if (mapQuery.get(field.tname()) == null) {
                    mapQuery.put(field.tname(), new Query(this));
                }
                mapQuery.get(field.tname()).fields.add(field);
            }
        }
    }

    public Query(Field[]... fieldsArr) {
        this.root = this;
        for (Field[] fields : fieldsArr) {
            for (Field field : fields) {
                if (!field.name().equals("up")) {
                    if (mapQuery.get(field.tname()) == null) {
                        mapQuery.put(field.tname(), new Query(this));
                    }
                    mapQuery.get(field.tname()).fields.add(field);
                }
            }
        }
    }

    public Field[] fields() {
        ArrayList<Field> arr = new ArrayList();
        for (Map.Entry<String, Query> q : root.mapQuery.entrySet()) {
            Query q2 = q.getValue();
            arr.addAll(q2.fields);
        }
        return arr.toArray(new Field[arr.size()]);
    }

    public Query table(String name_table) {
        return root.mapQuery.get(name_table);
    }

    public Query select(Object... s) {

        String sql = String.valueOf(s[0]);
        if (String.valueOf(s[0]).substring(0, 6).equalsIgnoreCase("select") == false) {
            sql = "";
            for (Object p : s) {
                if (p instanceof Field) {
                    Field f = (Field) p;
                    if ("up".equals(f.name())) {
                        sql = sql + " " + f.tname();
                    } else {
                        sql = sql + " " + f.tname() + "." + f.name();
                    }
                } else {
                    sql = sql + " " + p;
                }
            }
            String str = "";
            for (Map.Entry<String, Query> q : root.mapQuery.entrySet()) {
                Query table = q.getValue();
                table.clear();
                for (Field field : table.fields) {
                    if (!field.name().equals("up")) {
                        str = str + ", " + field.tname() + "." + field.name();
                    }
                }
            }
            sql = "select " + str.toLowerCase().substring(1, str.length()) + " from " + sql;
            sql = sql.replace("' ", "'");
            sql = sql.replace(" '", "'");
        }
        System.out.println("SQL-SELECT: " + sql);
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet recordset = statement.executeQuery(sql);
            while (recordset.next()) {
                int selector = 0;
                for (Map.Entry<String, Query> q : root.mapQuery.entrySet()) {
                    Query table = q.getValue();
                    Record record = table.newRecord(SEL);
                    table.add(record);
                    for (int index = 0; index < table.fields.size(); index++) {
                        Field field = table.fields.get(index);
                        if (!field.name().equals("up")) {

                            Object value = recordset.getObject(1 + index + selector);
                            record.setNo(field, value);
                        }
                    }
                    selector = selector + table.fields.size();
                }
            }
            return this;

        } catch (SQLException e) {
            System.out.println(e + "  " + sql);
            return null;
        }
    }

    public int insert(Record record) throws SQLException {

        Field[] f = fields.get(0).fields();
        if (Query.INS.equals(record.get(f[0])) == false) {
            return 0;
        }
        String sql = "";
        Statement statement = connection.createStatement();
        //если нет, генерю сам
        String nameCols = "", nameVals = "";
        //цикл по полям таблицы
        for (int k = 0; k < fields.size(); k++) {
            Field field = fields.get(k);
            if (field.meta().type() != Field.TYPE.OBJ) {
                nameCols = nameCols + field.name() + ",";
                nameVals = nameVals + wrapper(record, field) + ",";
            }
        }
        if (nameCols != null && nameVals != null) {
            nameCols = nameCols.substring(0, nameCols.length() - 1);
            nameVals = nameVals.substring(0, nameVals.length() - 1);
            sql = "insert into " + schema + fields.get(0).tname() + "(" + nameCols + ") values(" + nameVals + ")";
            System.out.println("SQL-INSERT " + sql);
            return statement.executeUpdate(sql);
        }
        return 0;
    }

    public int update(Record record) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String nameCols = "";
            //цикл по полям таблицы
            for (Field field : fields.get(0).fields()) {
                if (field.meta().type() != Field.TYPE.OBJ) {
                    nameCols = nameCols + field.name() + " = " + wrapper(record, field) + ",";
                }
            }
            Field[] f = fields.get(0).fields();
            if (nameCols.isEmpty() == false) {
                nameCols = nameCols.substring(0, nameCols.length() - 1);
                String sql = "update " + schema + fields.get(0).tname() + " set "
                        + nameCols + " where " + f[1].name() + " = " + wrapper(record, f[1]);
                System.out.println("SQL-UPDATE " + sql);
                return statement.executeUpdate(sql);
            }
        } catch (Exception e) {
            System.out.println(fields.get(0).tname() + ".update() " + e);
            return -1;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Query.update() " + e);
            }
        }
        return 0;
    }

    public int delete(Record record) throws SQLException {
        //if (Query.DEL.equals(record.get(fields[0])) == false) {  return 0;  }
        String sql = fields.get(0).delete(record);
        Statement statement = connection.createStatement();
        if (sql != null) {
            //Util.println("SQL-DELETE " + sql);
            return statement.executeUpdate(sql);
        } else {
            Field[] f = fields.get(0).fields();
            sql = "delete from " + schema + fields.get(0).tname() + " where " + f[1].name() + " = " + wrapper(record, f[1]);
            //Util.println("SQL-DELETE " + sql);
            return statement.executeUpdate(sql);
        }
    }

    public void execsql() {
        try {
            for (Map.Entry<String, Query> q : root.mapQuery.entrySet()) {
                Query query = q.getValue();
                for (Record record : query) {
                    if (record.get(0).equals(Query.UPD) || record.get(0).equals(INS)) {

                        //Util.println(record);
                        if (record.validate(fields) != null) { //проверка на корректность ввода данных
                            JOptionPane.showMessageDialog(null, record.validate(fields), "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    if ("INS".equals(record.getStr(0))) {
                        query.insert(record);
                        record.setNo(0, Query.SEL);
                    } else if ("UPD".equals(record.getStr(0))) {
                        query.update(record);
                        record.setNo(0, Query.SEL);
                    } else if ("DEL".equals(record.getStr(0))) {
                        query.delete(record);
                        //entry.getValue().remove(record);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String wrapper(Record record, Field field) {
        try {
            if (record.get(field) == null) {
                return null;
            } else if (Field.TYPE.STR.equals(field.meta().type())) {
                return "'" + record.getStr(field) + "'";
            } else if (Field.TYPE.BOOL.equals(field.meta().type())) {
                return "'" + record.getStr(field) + "'";
            } else if (Field.TYPE.DATE.equals(field.meta().type())) {
                if (record.get(field) instanceof java.util.Date) {
                    return " '" + new SimpleDateFormat("dd.MM.yyyy").format(record.getDate(field)) + "' ";
                } else {
                    return " '" + record.getStr(field) + "' ";
                }
            }
            return record.getStr(field);
        } catch (Exception e) {
            System.out.println("Query.vrapper() " + e);
            return null;
        }
    }

    public boolean isUpdate() {
        for (Map.Entry<String, Query> q : mapQuery.entrySet()) {
            for (Record record : q.getValue()) {
                if ("UPD".equals(record.getStr(0)) || "INS".equals(record.getStr(0))) {
                    return true;
                }
            }
        }
        return false;
    }
}
