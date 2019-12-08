
/**
1. В пс3 и пс4 разное количество полей в таблицах но список столбцов eEnum.values() для них один.
2. Отсутствующие поля в пс3 будут заполняться пустышками.
3. Поля не вошедшие в список столбцов eEnum.values() тоже будут переноситься для sql update таблиц и потом удаляться.
 */

package convdb;

import common.Utils;
import dataset.Field;
import dataset.Query;
import domain.eArtTarif;
import domain.eArtikls;
import domain.eCompSpec;
import domain.eComplet;
import domain.eDicConst;
import domain.eDicGrArt;
import domain.eDicGrText;
import domain.eDicRate;
import domain.eDicSysPar;
import domain.eTexture;
import domain.eFurnCh1;
import domain.eFurnCh2;
import domain.eFurnPar1;
import domain.eFurnSpec;
import domain.eGlasArt;
import domain.eGlasGrup;
import domain.eGlasProf;
import domain.eItems;
import domain.eItenSpec;
import domain.eJoinPar1;
import domain.eJoinPar2;
import domain.eJoinPar3;
import domain.eJoinSpec;
import domain.eJoinVar;
import domain.eJoining;
import domain.eTextPar;
import domain.eGlasPar2;
import domain.eGlasPar1;
import domain.eDicParam;
import domain.eItemPar1;
import domain.eItemPar2;
import domain.eRuleCalc;
import domain.eSysFurn;
import domain.eSysPar;
import domain.eSysProf;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Script {

    public static void script() {
        Field[] fieldsUp = {
            eArtikls.up
//                , eArtTarif.up, eTexture.up, eJoining.up, eJoinSpec.up, eJoinVar.up, eDicRate.up,
//            eFurnCh1.up, eFurnCh2.up, eFurnSpec.up, eGlasArt.up, eGlasGrup.up, eGlasProf.up,
//            eDicGrArt.up, eDicGrText.up, eComplet.up, eCompSpec.up, eTextPar.up, eJoinPar1.up, eJoinPar2.up,
//            eFurnPar1.up, eJoinPar3.up, eGlasPar1.up, eGlasPar2.up, eDicParam.up, eSysPar.up, eItemPar1.up, eItemPar2.up,
//            eRuleCalc.up, eDicSysPar.up, eItems.up, eItenSpec.up, eDicConst.up, eSysFurn.up, eSysProf.up
        //,eSpecific.up
        };
        try {
            Connection cn1 = java.sql.DriverManager.getConnection( //источник
                    "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            Connection cn2 = java.sql.DriverManager.getConnection( //приёмник
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");

            Utils.println("Подготовка методанных");
            List<String> listExistTable2 = new ArrayList<String>();
            List<String> listGenerator2 = new ArrayList<String>();

            Statement st1 = cn1.createStatement(); //мсточник   
            DatabaseMetaData mdb1 = cn1.getMetaData();
            Statement st2 = cn2.createStatement();//приёмник
            DatabaseMetaData mdb2 = cn2.getMetaData();
            ResultSet resultSet2 = mdb2.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet2.next()) {
                listExistTable2.add(resultSet2.getString("TABLE_NAME"));
            }
            //Генераторы приёмника
            resultSet2 = st2.executeQuery("select rdb$generator_name from rdb$generators");
            while (resultSet2.next()) {
                listGenerator2.add(resultSet2.getString("RDB$GENERATOR_NAME").trim());
            }
            Utils.println("Перенос данных");
            //Цыкл по доменам приложения
            for (Field fieldUp : fieldsUp) {

                HashSet<String[]> hsDeltaCol = new HashSet(); //поля не вошедшие в Field[], в последствии будут использоваться для sql update
                ResultSet rsc1 = mdb1.getColumns(null, null, fieldUp.meta().fname, null);
                while (rsc1.next()) {
                    String[] name = {rsc1.getString("COLUMN_NAME"), rsc1.getString("DATA_TYPE")};
                    boolean find = false;
                    for (Field field : fieldUp.fields()) {
                        if (field.meta().fname.equalsIgnoreCase(name[0])) {
                            find = true;
                        }
                    }
                    if (find == false) {
                        hsDeltaCol.add(name);
                    }
                }
                if (listGenerator2.contains("GEN_" + fieldUp.tname()) == true) {
                    st2.execute("DROP GENERATOR GEN_" + fieldUp.tname() + ";"); //удаление генератора приёмника
                }
                if (listExistTable2.contains(fieldUp.tname()) == true) {
                    st2.execute("DROP TABLE " + fieldUp.tname() + ";"); //удаление таблицы приёмника
                }
                //Создание таблицы приёмника
                for (String ddl : Script.createTable(fieldUp.fields())) {
                    st2.execute(ddl);
                }
                //Добавление столбцов не вошедших в eEnum.values()
                for (String ddl : Script.createColumn(hsDeltaCol, fieldUp.tname())) {
                    System.out.println(ddl);
                    //st2.execute(ddl);
                }
                //Конвертирование данных в таблицу приёмника                   
                convertTable(cn1, cn2, fieldUp.fields());

                st2.execute("CREATE GENERATOR GEN_" + fieldUp.tname()); //создание генератора приёмника
                Object obj = fieldUp.meta().fname;
                if ("id".equals(fieldUp.fields()[1].meta().fname)) {
                    st2.execute("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                }
                st2.execute("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);"); //DDL создание первичного ключа
            }

            Utils.println("Изменение структуры БД");
            for (Field field : fieldsUp) {
                st2.execute("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr + "'"); //DDL описание таблиц
            }
//            if (fieldsUp.length > 3) {
//                st2.execute("update artsvst set artikl_id = (select id from artikls a where a.code = artsvst.anumb)");
//                st2.execute("alter table artsvst drop anumb");
//
//            }
            Utils.println("Обновление закончено");

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

    public static ArrayList<String> createColumn(HashSet<String[]> hsDeltaCol, String tname) {

        ArrayList<String> batch = new ArrayList();
        for (String[] str : hsDeltaCol) {
            if ("4".equals(str[1]) == true) {
                batch.add("ALTER TABLE " + tname + " ADD " + str[0] + " INTEGER;");
            } else {
                batch.add("ALTER TABLE " + tname + " ADD " + str[0] + " VARCHAR(255);");
            }
        }
        return batch;
    }

    /**
     * Конвертор данных таблиц
     *
     * @param cn1 соединение источника
     * @param cn2 соединение приёмника
     * @param fields все поля таблицы
     */
    public static void convertTable(Connection cn1, Connection cn2, Field[] fields) {
        String sql = "";
        try {
            int count = 0;
            String tname1 = fields[0].meta().fname;
            String tname2 = fields[0].tname();
            HashSet hsExistField = new HashSet();
            boolean bash = true;
            Statement st1 = cn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement st2 = cn2.createStatement();
            ResultSet rs1 = st1.executeQuery("select count(*) from " + tname1);
            if (rs1.next()) {
                count = rs1.getInt(1);
            }
            for (int index_page = 0; index_page <= count / 500; ++index_page) {

                Utils.println(tname2 + " " + index_page);
                String nameCols2 = "";
                rs1 = st1.executeQuery("select first 500 skip " + index_page * 500 + " * from " + tname1);
                ResultSetMetaData md1 = rs1.getMetaData();
                for (int index = 1; index <= md1.getColumnCount(); index++) {

                    String fn = md1.getColumnLabel(index);
                    for (Field f : fields) {
                        if (f.meta().fname.equalsIgnoreCase(fn)) {
                            hsExistField.add(f);
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
                        //в ps3 и ps4 разное количество полей
                        if (hsExistField.contains(field)) {
                            Object val = rs1.getObject(field.meta().fname);
                            nameVal2 = nameVal2 + Query.wrapper(val, field) + ",";
                        } else {
                            nameVal2 = nameVal2 + "0" + ",";
                        }
                    }
                    nameVal2 = nameVal2.substring(0, nameVal2.length() - 1);
                    sql = "insert into " + tname2 + "(" + nameCols2 + ") values (" + nameVal2.toString() + ")";
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
