package convdb;

import common.Utils;
import dataset.Field;
import dataset.Query;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Script {

    public static void script(Field... fieldsUp) {
        try {
            Connection cn1 = java.sql.DriverManager.getConnection(
                    "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
                    //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            Connection cn2 = java.sql.DriverManager.getConnection(
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");

            List<String> tableList = new ArrayList<String>();
            List<String> generatorList = new ArrayList<String>();

            Statement st2 = cn2.createStatement();
            DatabaseMetaData metaData2 = cn2.getMetaData();
            ResultSet rs = metaData2.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableList.add(rs.getString("TABLE_NAME"));
            }
            rs = st2.executeQuery("select rdb$generator_name from rdb$generators");
            while (rs.next()) {
                generatorList.add(rs.getString("RDB$GENERATOR_NAME").trim());
            }
            for (Field fieldUp : fieldsUp) {

                if (generatorList.contains("GEN_" + fieldUp.tname()) == true) {
                    st2.execute("DROP GENERATOR GEN_" + fieldUp.tname() + ";");
                }
                if (tableList.contains(fieldUp.tname()) == true) {
                    st2.execute("DROP TABLE " + fieldUp.tname() + ";");
                }
                ArrayList<String> batch = Script.createTable(fieldUp.fields());
                for (String s : batch) {
                    st2.execute(s);
                }
                convertTable(cn1, cn2, fieldUp.fields());

                st2.execute("CREATE GENERATOR GEN_" + fieldUp.tname());
                st2.execute("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)");
                st2.execute("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);");
            }
            for (Field field : fieldsUp) {
                st2.execute("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr + "'");
            }
            if (fieldsUp.length > 3) {
                st2.execute("update artsvst set artikl_id = (select id from artikls a where a.code = artsvst.anumb)");
                st2.execute("alter table artsvst drop anumb");
            }

        } catch (Exception e) {
            System.err.println("SQL-SCRIPT: " + e);
        }
    }

    public static ArrayList<String> createTable(Field... f) {

        ArrayList<String> batch = new ArrayList();
        String ddl = "CREATE TABLE " + f[0].tname() + " (";
        for (int i = 1; i < f.length; ++i) {

            Field f2 = f[i];
            ddl = ddl + "\n" + f2.name() + "  " + typeColumn(f2);
            if (f2.meta().isnull() == false) {
                ddl = ddl + " NOT NULL";
            }
            ddl += ",";
        }
        ddl = ddl.substring(0, ddl.length() - 1) + ");";
        batch.add(ddl);
        for (int i = 1; i < f.length; ++i) {
            batch.add("COMMENT ON COLUMN \"" + f[i].tname() + "\"." + f[i].name() + " IS '" + f[i].meta().descr + "';");
        }
        return batch;
    }

    public static void convertTable(Connection cn1, Connection cn2, Field[] fields) {
        String sql = "";
        try {
            int count = 0;
            String nameTable2 = fields[0].tname();
            boolean bash = true;
            Statement st1 = cn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement st2 = cn2.createStatement();

            ResultSet rs1 = st1.executeQuery("select count(*) from " + fields[0].tname());
            if (rs1.next()) {
                count = rs1.getInt(1);
            }
            for (int index_page = 0; index_page <= count / 500; ++index_page) {
            //for (int index_page = 8065; index_page <= count / 500; ++index_page) {

                Utils.println(nameTable2 + " " + index_page);
                String nameCols2 = "";
                rs1 = st1.executeQuery("select first 500 skip " + index_page * 500 + " * from " + fields[0].tname());
                ResultSetMetaData md1 = rs1.getMetaData();
                HashSet hsNonField = new HashSet();
                for (int index = 1; index <= md1.getColumnCount(); index++) {

                    String fn = md1.getColumnLabel(index);
                    for (Field f : fields) {
                        if (f.meta().field_name.equalsIgnoreCase(fn)) {
                            hsNonField.add(f);
                        }
                    }
                }
                for (int index = 1; index < fields.length; index++) {
                    Field field = fields[index];
                    nameCols2 = nameCols2 + field.name() + ",";
                }
                nameCols2 = nameCols2.substring(0, nameCols2.length() - 1);

                while (rs1.next()) {
                    String nameVal2 = "";
                    for (int index = 1; index < fields.length; index++) {
                        Field field = fields[index];
                        if (hsNonField.contains(field)) {
                            Object val = rs1.getObject(field.meta().field_name);
                            nameVal2 = nameVal2 + Query.wrapper(val, field) + ",";
                        } else {
                            nameVal2 = nameVal2 + "0" + ",";
                        }
                    }
                    nameVal2 = nameVal2.substring(0, nameVal2.length() - 1);
                    sql = "insert into " + nameTable2 + "(" + nameCols2 + ") values (" + nameVal2.toString() + ")";
                    if (bash == true) {
                        st2.addBatch(sql);
                    } else {
                        try {
                            System.out.println(sql);
                            st2.executeUpdate(sql);
                        } catch (SQLException e) {
                            System.out.println("SCRIPT-INSERT:  " + e + "  " + sql);
                        }
                    }
                }
                bash = true;
                cn2.setAutoCommit(false);
                try {
                    st2.executeBatch();
                    cn2.commit();
                    st2.clearBatch();
                    cn2.setAutoCommit(true);

                } catch (SQLException e) {
                    cn2.rollback();
                    bash = false;
                    --index_page;
                    cn2.setAutoCommit(true);
                    System.out.println("SCRIPT-BATCH:  " + e);
                }
            }
        } catch (SQLException e) {
            System.out.println("CONVERT-TABLE:  " + e + "  " + sql);
        }
    }

    private static String typeColumn(Field field) {

        switch (field.meta().type().name()) {
            case "INT":
                return "INTEGER";
            case "DBL":
                return "DOUBLE PRECISION";
            case "FLT":
                return "FLOAT";
            case "STR":
                return "VARCHAR(" + field.meta().size() + ")";
            case "DATE":
                return "DATE";
            case "BLOB":
                return "BLOB SUB_TYPE 1 SEGMENT SIZE " + field.meta().size();
        }
        return "";
    }
}
