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
            Connection cn1 = java.sql.DriverManager.getConnection( //источник
                    "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            Connection cn2 = java.sql.DriverManager.getConnection( //приёмник
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");

            Utils.println("Подготовка методанных");
            List<String> listTable1 = new ArrayList<String>();
            List<String> listTable2 = new ArrayList<String>();
            List<String> listGenerator2 = new ArrayList<String>();

            //Таблицы источника
            Statement st1 = cn1.createStatement();
            DatabaseMetaData metaData1 = cn1.getMetaData();
            ResultSet resultSet1 = metaData1.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet1.next()) {
                listTable1.add(resultSet1.getString("TABLE_NAME"));
            }
            //Таблицы приёмника (если есть)
            Statement st2 = cn2.createStatement();
            DatabaseMetaData metaData2 = cn2.getMetaData();
            ResultSet resultSet2 = metaData2.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet2.next()) {
                listTable2.add(resultSet2.getString("TABLE_NAME"));
            }
            //Генераторы приёмника
            resultSet2 = st2.executeQuery("select rdb$generator_name from rdb$generators");
            while (resultSet2.next()) {
                listGenerator2.add(resultSet2.getString("RDB$GENERATOR_NAME").trim());
            }
            Utils.println("Перенос данных");
            //Цыкл по доменам приложения
            for (Field fieldUp : fieldsUp) {

                if (listGenerator2.contains("GEN_" + fieldUp.tname()) == true) {
                    st2.execute("DROP GENERATOR GEN_" + fieldUp.tname() + ";"); //удаление генератора приёмника
                }
                if (listTable2.contains(fieldUp.tname()) == true) {
                    st2.execute("DROP TABLE " + fieldUp.tname() + ";"); //удаление таблицы приёмника
                }
                //Создание таблицы приёмника
                ArrayList<String> batch = Script.createTable(fieldUp.fields());
                for (String ddl : batch) {
                    st2.execute(ddl);
                }
                //Конвертирование данных в таблицу приёмника
                if (listTable1.contains(fieldUp.meta().field_name) == true) {
                    convertTable(cn1, cn2, fieldUp.fields());
                }

                st2.execute("CREATE GENERATOR GEN_" + fieldUp.tname()); //создание генератора приёмника
                if ("id".equals(fieldUp.meta().field_name)) {
                    st2.execute("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                }
                st2.execute("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);"); //DDL создание первичного ключа
            }
            
            Utils.println("Обновление структуры БД");
            for (Field field : fieldsUp) {
                st2.execute("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr + "'"); //DDL описание всех полей таблицы
            }
            if (fieldsUp.length > 3) {
                st2.execute("update artsvst set artikl_id = (select id from artikls a where a.code = artsvst.anumb)");
                st2.execute("alter table artsvst drop anumb");

            }
            Utils.println("Обновление закончено!");

        } catch (Exception e) {
            System.err.println("SQL-SCRIPT: " + e);
        }
    }

    /**
     * Создание таблицы
     *
     * @param f все поля соэдаваемой таблицы
     * @return список ddl операторов создания таблицы
     */
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

    /**
     * Конвертор данных таблицы
     *
     * @param cn1 соединение источника
     * @param cn2 соединение приёмника
     * @param fields все поля таблицы
     */
    public static void convertTable(Connection cn1, Connection cn2, Field[] fields) {
        String sql = "";
        try {
            int count = 0;
            String nameTable2 = fields[0].tname();
            boolean bash = true;
            Statement st1 = cn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement st2 = cn2.createStatement();

            ResultSet rs1 = st1.executeQuery("select count(*) from " + fields[0].meta().field_name);
            if (rs1.next()) {
                count = rs1.getInt(1);
            }
            for (int index_page = 0; index_page <= count / 500; ++index_page) {

                Utils.println(nameTable2 + " " + index_page);
                String nameCols2 = "";
                rs1 = st1.executeQuery("select first 500 skip " + index_page * 500 + " * from " + fields[0].meta().field_name);
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

    /**
     * Типы полей
     */
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
