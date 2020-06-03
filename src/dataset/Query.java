package dataset;

import common.eProfile;
import static dataset.Query.INS;
import static dataset.Query.connection;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
        mapQuery.put(fields[0].tname(), this);
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
        mapQuery.put(fieldsArr[0][0].tname(), this);
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

    public Query table(Field field) {
        return root.mapQuery.get(field.tname());
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
                
                table.clear(); //удаляем данные
                
                for (Field field : table.fields) {
                    str = str + ", " + field.tname() + "." + field.name();
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
                        Object value = recordset.getObject(1 + index + selector);
                        record.setNo(field, value);
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

    public void insert(Record record) {
        try {
            if (Query.INS.equals(record.getStr(0))) {
                Field[] f = fields.get(0).fields();
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
                    String sql = "insert into " + schema + fields.get(0).tname() + "(" + nameCols + ") values(" + nameVals + ")";
                    System.out.println("SQL-INSERT " + sql);
                    statement.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            System.out.println("Query.insert() " + e);
        }
    }

    public void update(Record record) {
        try {
            if (Query.UPD.equals(record.getStr(0))) {
                String nameCols = "";
                Statement statement = statement = connection.createStatement();
                //цикл по полям таблицы
                for (Field field : fields) {
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
                    statement.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            System.out.println("Query.update() " + e);
        }
    }

    public void delete(Record record) {
        try {
            if (Query.DEL.equals(record.getStr(0))) {
                Statement statement = connection.createStatement();
                Field[] f = fields.get(0).fields();
                String sql = "delete from " + schema + fields.get(0).tname() + " where " + f[1].name() + " = " + wrapper(record, f[1]);
                System.out.println("SQL-DELETE " + sql);
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println("Query.delete() " + e);
        }
    }

    public String execsql() {
        try {
            for (Record record : this) {
                if (record.get(0).equals(Query.UPD) || record.get(0).equals(INS)) {
                    if (record.validate(fields) != null) { //проверка на корректность ввода данных
                        
                        JOptionPane.showMessageDialog(eProfile.appframe, record.validate(fields), "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                        return record.validate(fields);
                    }
                }
                if (Query.INS.equals(record.getStr(0))) {
                    insert(record);
                    record.setNo(0, Query.SEL);
                } else if (Query.UPD.equals(record.getStr(0))) {
                    update(record);
                    record.setNo(0, Query.SEL);
                } else if (Query.DEL.equals(record.getStr(0))) {
                    delete(record);
                }
            }
            return "";
        } catch (Exception e) {
            System.out.println(e);
            return "";
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
        for (Record record : this) {
            if ("UPD".equals(record.getStr(0)) || "INS".equals(record.getStr(0))) {
                return true;
            }
        }
        return false;
    }
}
