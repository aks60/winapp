package convdb;

import common.Util;
import dataset.Field;
import dataset.Query;
import domain.eArtDet;
import domain.eArtikls;
import domain.eCompDet;
import domain.eComplet;
import domain.eDicConst;
import domain.eDicGrArt;
import domain.eTextGrp;
import domain.eDicRate;
import domain.eDicSysPar;
import domain.eTexture;
import domain.eFurnCh1;
import domain.eFurnCh2;
import domain.eFurnPar1;
import domain.eFurnDet;
import domain.eGlasArt;
import domain.eGlasGrup;
import domain.eGlasProf;
import domain.eItems;
import domain.eItenDet;
import domain.eJoinPar1;
import domain.eJoinPar2;
import domain.eJoinPar3;
import domain.eJoinDet;
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

/**
 * В пс3 и пс4 разное количество полей в таблицах, но список столбцов в
 * прилшжении eEnum.values() для них один. Отсутствующие поля пс3 в
 * eEnum.values() будут заполняться пустышками. 3. Поля не вошедшие в список
 * столбцов eEnum.values() тоже будут переноситься для sql update и потом
 * удаляться. Обновление данных выполняется пакетом, если была ошибка в пакете,
 * откат и пакет обслуживается отдельными insert.
 */
public class Script {

    public static void script() {
        Field[] fieldsUp = {
            eArtikls.up, eArtDet.up, eTexture.up, eTextPar.up, eComplet.up, eCompDet.up,
            eGlasPar1.up, eGlasPar2.up, eGlasArt.up, eGlasGrup.up, eGlasProf.up,
            eJoining.up, eJoinDet.up, eJoinVar.up, eJoinPar1.up, eJoinPar2.up, eJoinPar3.up,
            eFurnCh1.up, eFurnCh2.up, eFurnDet.up, eFurnPar1.up,
            eItems.up, eItenDet.up, eItemPar1.up, eItemPar2.up,
            eSysPar.up, eSysFurn.up, eSysProf.up, eRuleCalc.up,
            eDicConst.up, eDicSysPar.up, eDicRate.up, eDicGrArt.up, eTextGrp.up, eDicParam.up
        };
        try {
            Connection cn1 = java.sql.DriverManager.getConnection( //источник
                    "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            Connection cn2 = java.sql.DriverManager.getConnection( //приёмник
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");

            Util.println("Подготовка методанных");
            Statement st1 = cn1.createStatement(); //мсточник   
            DatabaseMetaData mdb1 = cn1.getMetaData();
            Statement st2 = cn2.createStatement();//приёмник
            DatabaseMetaData mdb2 = cn2.getMetaData();
            ResultSet resultSet2 = mdb2.getTables(null, null, null, new String[]{"TABLE"});
            List<String> listExistTable2 = new ArrayList<String>();//таблицы приёмника
            List<String> listGenerator2 = new ArrayList<String>();//генераторы приёмника 

            while (resultSet2.next()) {
                listExistTable2.add(resultSet2.getString("TABLE_NAME"));
            }
            //Генераторы приёмника
            resultSet2 = st2.executeQuery("select rdb$generator_name from rdb$generators");
            while (resultSet2.next()) {
                listGenerator2.add(resultSet2.getString("RDB$GENERATOR_NAME").trim());
            }
            Util.println("Перенос данных");
            //Цыкл по доменам приложения
            for (Field fieldUp : fieldsUp) {

                //Поля не вошедшие в eEnum.values()
                HashSet<String[]> hsDeltaCol = deltaColumn(mdb1, fieldUp);//в последствии будут использоваться для sql update

                if (listGenerator2.contains("GEN_" + fieldUp.tname()) == true) {
                    st2.execute("DROP GENERATOR GEN_" + fieldUp.tname() + ";"); //удаление генератора приёмника
                }
                if (listExistTable2.contains(fieldUp.tname()) == true) {
                    st2.execute(print("DROP TABLE " + fieldUp.tname() + ";")); //удаление таблицы приёмника
                }
                //Создание таблицы приёмника
                for (String ddl : Script.createTable(fieldUp.fields())) {
                    st2.execute(ddl);
                }
                //Добавление столбцов не вошедших в eEnum.values()
                for (Object[] deltaCol : hsDeltaCol) {
                    st2.execute(print("ALTER TABLE " + fieldUp.tname() + " ADD " + deltaCol[0] + " " + Util.typeSql(Field.TYPE.type(deltaCol[1]), deltaCol[2]) + ";"));
                }
                //Конвертирование данных в таблицу приёмника                   
                convertTable(cn1, cn2, fieldUp.fields(), hsDeltaCol);

                st2.execute("CREATE GENERATOR GEN_" + fieldUp.tname()); //создание генератора приёмника
                if ("id".equals(fieldUp.fields()[1].meta().fname)) {
                    st2.execute("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                }
                st2.execute(print("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);")); //DDL создание первичного ключа
            }
            Util.println("Изменение структуры БД");
            for (Field field : fieldsUp) {
                st2.execute("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr + "'"); //DDL описание таблиц
            }
            if (fieldsUp.length > 1) {
                st2.execute("update art_text set artikl_id = (select id from artikls a where a.code = art_text.anumb)");
                st2.execute("update art_text set texture_id = (select id from texture a where a.ccode = art_text.clcod and a.cnumb = art_text.clnum)");
            }
            //Удаление столбцов не вошедших в eEnum.values()
            for (Field fieldUp : fieldsUp) {
                HashSet<String[]> hsDeltaCol = deltaColumn(mdb1, fieldUp);
                for (Object[] deltaCol : hsDeltaCol) {
                    st2.execute("ALTER TABLE " + fieldUp.tname() + " DROP  " + deltaCol[0] + ";");
                }
            }
            Util.println("Обновление завершено");

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
            ddl = ddl + "\n" + f2.name() + "  " + Util.typeSql(f2.meta().type(), f2.meta().size());
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
     * Конвертор данных таблиц
     *
     * @param cn1 соединение источника
     * @param cn2 соединение приёмника
     * @param fields все поля таблицы
     * @param hsDeltaCol поля не вошедшие в eEnum.values()
     */
    public static void convertTable(Connection cn1, Connection cn2, Field[] fields, HashSet<String[]> hsDeltaCol) {
        String sql = "";
        try {
            int count = 0; //колчество записей для расчёта кол. пакетов
            String tname1 = fields[0].meta().fname;
            String tname2 = fields[0].tname();
            HashSet hsExistField = new HashSet(); //список полей которые есть в источнике и в eEnum.values()
            boolean bash = true;
            Statement st1 = cn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement st2 = cn2.createStatement();
            ResultSet rs1 = st1.executeQuery("select count(*) from " + tname1);
            if (rs1.next()) {
                count = rs1.getInt(1);
            }
            //Цыкл по пакетам
            for (int index_page = 0; index_page <= count / 500; ++index_page) {

                Util.println("Таблица:" + tname2 + " пакет:" + index_page);
                String nameCols2 = "";
                rs1 = st1.executeQuery("select first 500 skip " + index_page * 500 + " * from " + tname1);
                ResultSetMetaData md1 = rs1.getMetaData();
                for (int index = 1; index <= md1.getColumnCount(); index++) {

                    //Список полей которые есть в источнике и в eEnum.values()
                    String fn = md1.getColumnLabel(index);
                    for (Field f : fields) {
                        if (f.meta().fname.equalsIgnoreCase(fn)) {
                            hsExistField.add(f);
                        }
                    }
                }
                //Строка insert into(...)
                for (int index = 1; index < fields.length; index++) {
                    Field field = fields[index];
                    nameCols2 = nameCols2 + field.name() + ",";
                }
                for (Object[] str : hsDeltaCol) {//поля для sql update (в конце будут удалены)
                    nameCols2 = nameCols2 + str[0] + ",";
                }
                nameCols2 = nameCols2.substring(0, nameCols2.length() - 1);
                //Строка values(...)
                while (rs1.next()) {
                    String nameVal2 = "";
                    //Цыкл по полям eEnum.values()
                    for (int index = 1; index < fields.length; index++) {
                        Field field = fields[index];
                        if (hsExistField.contains(field)) { //т.к. ps3 и ps4 разное количество полей
                            Object val = rs1.getObject(field.meta().fname);
                            nameVal2 = nameVal2 + Util.wrapperSql(val, field.meta().type()) + ",";
                        } else {
                            nameVal2 = nameVal2 + "0" + ",";
                        }
                    }
                    //Цыкл по полям не вошедших в eEnum.values()
                    for (String[] str : hsDeltaCol) {
                        Object val = rs1.getObject(str[0]);
                        nameVal2 = nameVal2 + Util.wrapperSql(val, Field.TYPE.type(str[1])) + ",";
                    }
                    nameVal2 = nameVal2.substring(0, nameVal2.length() - 1);
                    sql = "insert into " + tname2 + "(" + nameCols2 + ") values (" + nameVal2.toString() + ")";
                    if (bash == true) {
                        st2.addBatch(sql);
                    } else {
                        try {
                            //Если была ошибка в пакете выполняю отдельные sql insert
                            st2.executeUpdate(sql);
                        } catch (SQLException e) {
                            System.out.println("SCRIPT-INSERT:  " + e + "  " + sql);
                        }
                    }
                }
                bash = true;
                cn2.setAutoCommit(false);
                try {
                    //Пакетный insert
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

    private static HashSet<String[]> deltaColumn(DatabaseMetaData mdb1, Field fieldUp) {
        try {
            HashSet<String[]> hsDeltaCol = new HashSet(); //поля не вошедшие в eEnum.values(), в последствии будут использоваться для sql update
            ResultSet rsc1 = mdb1.getColumns(null, null, fieldUp.meta().fname, null);
            while (rsc1.next()) {
                String[] name = {rsc1.getString("COLUMN_NAME"), rsc1.getString("DATA_TYPE"), rsc1.getString("COLUMN_SIZE")};
                boolean find = false;
                for (Field field : fieldUp.fields()) {
                    if (field.meta().fname.equalsIgnoreCase(name[0].toString())) {
                        find = true;
                    }
                }
                if (find == false) {
                    hsDeltaCol.add(name);
                }
            }
            return hsDeltaCol;
        } catch (SQLException e) {
            System.err.println("DELTA-COLUMN: " + e);
            return null;
        }
    }
    
    private static String print(String str) {
        System.out.println(str);
        return str;
    }
 }
